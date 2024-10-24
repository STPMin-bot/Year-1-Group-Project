import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.GroupLayout.Alignment;


public class ProfileGUI extends JFrame {

	private JPanel feedbackPanel;
	private  Doctor selectedDoctor;
	private List<String> feedbackComments;
	private JTextArea commentLabel;

	public ProfileGUI(Doctor doctor) {
		this.selectedDoctor = doctor;

		setTitle("Doctor Profile");
		setSize(640, 680);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		setLocationRelativeTo(null);
		feedbackComments = new ArrayList<>();

		initComponents();
	}

	private void initComponents() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(null);

		// Doctor Information Panel
		JPanel doctorInfoPanel = new JPanel();
		doctorInfoPanel.setBorder(BorderFactory.createTitledBorder("Doctor Information"));
		doctorInfoPanel.setLayout(null);
		JLabel nameLabel = new JLabel("Name: " + selectedDoctor.getName());
		nameLabel.setBounds(20, 20, 300, 20);
		doctorInfoPanel.add(nameLabel);
		JLabel specialityLabel = new JLabel("Speciality: " + selectedDoctor.getSpeciality());
		specialityLabel.setBounds(20, 50, 300, 20);
		doctorInfoPanel.add(specialityLabel);
		JLabel reviewRatingLabel = new JLabel("Review Rating: " + String.format("%.1f", selectedDoctor.getReviewRating()));
		reviewRatingLabel.setBounds(20, 80, 300, 20);
		doctorInfoPanel.add(reviewRatingLabel);
		JLabel totalReviewsLabel = new JLabel("Total Reviews: " + selectedDoctor.getTotalReviews());
		totalReviewsLabel.setBounds(20, 110, 300, 20);
		doctorInfoPanel.add(totalReviewsLabel);
		doctorInfoPanel.setBounds(20, 92, 580, 150);
		mainPanel.add(doctorInfoPanel, BorderLayout.NORTH);

		mainPanel.add(Box.createVerticalStrut(20), BorderLayout.CENTER);


		// Feedback Comments Panel
		feedbackPanel = new JPanel();
		JScrollPane scrollPane = new JScrollPane(feedbackPanel);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		feedbackPanel.setLayout(new BoxLayout(feedbackPanel, BoxLayout.Y_AXIS));
		scrollPane.setBounds(20, 307, 580, 300);
		mainPanel.add(scrollPane);

		// Populate feedback comments
		populateFeedbackComments();

		getContentPane().add(mainPanel);

		JLabel lblNewLabel_1 = new JLabel("Reviews");
		lblNewLabel_1.setFont(new Font("Arial", Font.BOLD, 20));
		lblNewLabel_1.setBounds(20, 269, 96, 19);
		mainPanel.add(lblNewLabel_1);

		// Add the "Provide a Review" button to mainPanel
		JButton provideReviewButton = new JButton("Provide a Review");
		provideReviewButton.setToolTipText("");
		provideReviewButton.setForeground(new Color(255, 255, 255));
		provideReviewButton.setFont(new Font("Arial", Font.PLAIN, 15));
		provideReviewButton.setBackground(new Color(36, 48, 110));
		provideReviewButton.setBounds(407, 267, 166, 27);
		mainPanel.add(provideReviewButton);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBackground(new Color(36, 48, 110));
		panel_1.setBounds(0, 0, 629, 70);
		mainPanel.add(panel_1);
		
		JLabel lblNewLabel = new JLabel("Medical Centre");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 24));
		lblNewLabel.setBounds(27, 20, 178, 27);
		panel_1.add(lblNewLabel);

		// Attach ActionListener to the "Provide a Review" button
		provideReviewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

		        DoctorReviewGUI review = new DoctorReviewGUI(selectedDoctor);
				//DoctorReviewGUI review = new DoctorReviewGUI(selectedDoctor);
				review.NewScreen();
			}
		});
	}

	public void populateFeedbackComments() {
		if (selectedDoctor == null) {
			System.out.println("Selected doctor is null");
			return;
		}

		// Clear existing comments
		feedbackPanel.removeAll();

		// Fetch comments from the database
		String url = "jdbc:sqlite:doctors.db";

		try (Connection conn = DriverManager.getConnection(url)) {
			String query = "SELECT new_rating, comment FROM feedback WHERE doctor_id = ?";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, selectedDoctor.getId());
			ResultSet resultSet = pstmt.executeQuery();

			if (!resultSet.isBeforeFirst()) {
				// No feedback found, add a JLabel with the message
				JLabel noFeedbackLabel = new JLabel("No feedback for this doctor yet");
				feedbackPanel.add(noFeedbackLabel);
			} else {
				// Populate comments
				while (resultSet.next()) {
					double rating = resultSet.getDouble("new_rating");
					String comment = resultSet.getString("comment");
					feedbackComments.add(comment);
					
					// Create a panel for each comment and review rating, then add it to the feedback panel
					JPanel commentPanel = new JPanel();
					commentPanel.setLayout(new BoxLayout(commentPanel, BoxLayout.Y_AXIS));
					commentPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

					
					JTextArea ratingTextArea = new JTextArea("Patient's Rating: " + rating);
					ratingTextArea.setFont(ratingTextArea.getFont().deriveFont(Font.BOLD, 14));
					ratingTextArea.setWrapStyleWord(true);
					ratingTextArea.setLineWrap(true);
					ratingTextArea.setEditable(false);
					ratingTextArea.setMaximumSize(new Dimension(580, 300)); // Adjust the maximum height as needed


					JTextArea commentTextArea = new JTextArea("Feedback: " + comment);
					commentTextArea.setFont(commentTextArea.getFont().deriveFont(Font.BOLD, 14));
					commentTextArea.setWrapStyleWord(true);
					commentTextArea.setLineWrap(true);
					commentTextArea.setEditable(false);

					commentPanel.add(ratingTextArea);
					commentPanel.add(commentTextArea);



					if (feedbackPanel.getComponentCount() > 0) {
						// Add empty space after each row, except for the first row
						feedbackPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add vertical space of 10 pixels
					}


					feedbackPanel.add(commentPanel);



				}

				feedbackPanel.revalidate();
				feedbackPanel.repaint();
			}
		} catch (SQLException e) {
			System.out.println("Error fetching feedback comments: " + e.getMessage());
		}
	}



	public void NewScreen() {
		// TODO Auto-generated method stub


		EventQueue.invokeLater(() -> {
			try {
				ProfileGUI window = new ProfileGUI(selectedDoctor);
				window.setVisible(true);

			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}


	public static void main(String[] args) {
		Doctor doctor = new Doctor(); 

		EventQueue.invokeLater(() -> {
			try {
				ProfileGUI window = new ProfileGUI(doctor);
				window.setVisible(true);

			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
