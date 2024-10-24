import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;

public class DoctorReviewGUI extends JFrame {

	private JFrame frame;
	private JComboBox<Integer> comboBoxRating;
	private JTextArea comment;
	private  Doctor selectedDoctor;
	private ProfileGUI profileGUI; // Reference to the ProfileGUI


	public static void main(String[] args) {

		Doctor doctor = new Doctor(); // Assuming Doctor has a default constructor

		EventQueue eventQueue = new EventQueue();
		EventQueue.invokeLater(() -> {
			try {
				DoctorReviewGUI window = new DoctorReviewGUI(doctor);
				window.frame.setVisible(true);

			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public DoctorReviewGUI(Doctor selectedDoctor) {
		this.selectedDoctor = selectedDoctor;
		initialize();
	}

	public void initialize() {


		frame = new JFrame();
		frame.setBounds(100, 100, 558, 656);

		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Only dispose this window, not all windows
		frame.setTitle("Doctor Review");

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBackground(new Color(36, 48, 110));
		panel_1.setBounds(0, 0, 629, 70);
		panel.add(panel_1);

		
		
		
		JLabel lblNewLabel = new JLabel("Medical Centre");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 24));
		lblNewLabel.setBounds(27, 20, 178, 27);
		panel_1.add(lblNewLabel);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(192, 192, 192), 1, true));
		panel_2.setBounds(54, 258, 432, 280);
		panel.add(panel_2);
		panel_2.setLayout(null);

		JLabel lblNewLabel_1 = new JLabel("Select your rating:");
		lblNewLabel_1.setFont(new Font("Arial", Font.BOLD, 14));
		lblNewLabel_1.setBounds(10, 27, 145, 19);
		panel_2.add(lblNewLabel_1);

		// Create a combo box for integer rating
		comboBoxRating = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
		comboBoxRating.setBounds(165, 28, 75, 19);
		panel_2.add(comboBoxRating);

		JLabel lblNewLabel_1_1 = new JLabel("Leave a comment:");
		lblNewLabel_1_1.setFont(new Font("Arial", Font.BOLD, 14));
		lblNewLabel_1_1.setBounds(10, 77, 145, 19);
		panel_2.add(lblNewLabel_1_1);

		comment = new JTextArea();
		comment.setLineWrap(true);
		comment.setBounds(20, 110, 388, 144);
		panel_2.add(comment);

		JButton btnSubmit = new JButton("Submit");
		btnSubmit.setForeground(new Color(255, 255, 255));
		btnSubmit.setBackground(new Color(36, 48, 110));
		btnSubmit.setFont(new Font("Arial", Font.BOLD, 14));
		btnSubmit.setBounds(233, 560, 100, 27);
		panel.add(btnSubmit);

		
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
		doctorInfoPanel.setBounds(54, 98, 432, 150);
		panel.add(doctorInfoPanel, BorderLayout.NORTH);

		panel.add(Box.createVerticalStrut(20), BorderLayout.CENTER);
		
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				submitFeedback();
			}
		});
	}

	public void NewScreen() {
		// TODO Auto-generated method stub

		EventQueue.invokeLater(() -> {
			try {
				DoctorReviewGUI window = new DoctorReviewGUI(selectedDoctor);
				window.frame.setVisible(true);

			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}


	private void submitFeedback() {
		// Get the selected rating and comment
		int integerRating = (int) comboBoxRating.getSelectedItem();
		String userComment = comment.getText();

		// Check if the rating is valid and the userComment is not null and not empty
		if (integerRating == 0 || userComment == null||userComment.trim().isEmpty()) {
			// Display an error message
			showErrorMessage("Please select a rating and enter feedback before submitting!");
		} else {
			saveFeedbackToDatabase(integerRating, userComment);
			//profileGUI.populateFeedbackComments(); // Populate feedback comments after submitting
		}
	}




	// Helper method to show a success message to the user
	private void showSuccessMessage() {
		JOptionPane.showMessageDialog(frame, "Feedback submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
	}

	// Helper method to show an error message to the user
	private void showErrorMessage(String errorMessage) {
		JOptionPane.showMessageDialog(frame, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
	}




	public void saveFeedbackToDatabase(int rating, String comment) {
		// Ensure selectedDoctor is not null
		if (selectedDoctor == null) {
			// Handle the case where selectedDoctor is null
			return;
		}

		// SQLite connection string
		String url = "jdbc:sqlite:doctors.db";

		int doctorId = selectedDoctor.getId();


		try (Connection conn = DriverManager.getConnection(url)) {
			// Save the feedback to the feedback table
			PreparedStatement pstmtFeedback = conn.prepareStatement("INSERT INTO feedback(new_rating, comment, doctor_id) VALUES (?, ?, ?)");
			pstmtFeedback.setDouble(1, rating);
			pstmtFeedback.setString(2, comment);
			pstmtFeedback.setInt(3, doctorId); // Insert doctor's ID as a foreign key
			pstmtFeedback.executeUpdate();

			// Update the doctor's rating and total reviews
			double newRating = calculateNewRating(selectedDoctor.getReviewRating(), selectedDoctor.getTotalReviews(), rating);


			updateDoctorRating(conn, selectedDoctor.getId(), newRating, selectedDoctor.getTotalReviews() + 1);

			DecimalFormat decimalFormat = new DecimalFormat("#.#");
			String formattedRating = decimalFormat.format(newRating);
			newRating = Double.parseDouble(formattedRating);


			// Show success message
			showSuccessMessage();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			// Show error message
			showErrorMessage("Failed to save feedback to the database!");
		}
	}



	// Calculate new average rating
	private double calculateNewRating(double oldRating, int totalReviews, int newRating) {
		return ((oldRating * totalReviews) + newRating) / (totalReviews + 1);
	}



	private void updateDoctorRating(Connection conn, int doctorId, double newRating, int newTotalReviews) throws SQLException {
		String updateQuery = "UPDATE doctors SET review_Rating = ?, total_Reviews = ? WHERE doctor_id = ?";
		try (PreparedStatement pstmtUpdate = conn.prepareStatement(updateQuery)) {
			// Format the new rating to one decimal point
			DecimalFormat decimalFormat = new DecimalFormat("#.#");
			String formattedRating = decimalFormat.format(newRating);
			double formattedRatingDouble = Double.parseDouble(formattedRating);

			pstmtUpdate.setDouble(1, formattedRatingDouble);
			pstmtUpdate.setInt(2, newTotalReviews);
			pstmtUpdate.setInt(3, doctorId);
			pstmtUpdate.executeUpdate();

			String commentText = comment.getText();
			selectedDoctor.getFeedbackComments().add(commentText);

		}
	}
}

