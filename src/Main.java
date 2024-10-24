import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import org.eclipse.wb.swing.FocusTraversalOnArray;

public class Main extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField user_input;
	private JPanel doctorsPanel;
	private JScrollPane scrollPane;
	private JComboBox<String> filter;
	private List<Doctor> doctors;
	private  Doctor selectedDoctor;
	private  String searchText;

		

	public static void main(String[] args) {
		Doctor doctor = new Doctor(); 
		
		EventQueue.invokeLater(() -> {
			try {
				Main window = new Main();
				window.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}


	public void loadDataFromDatabase() {
		try {
			String url = "jdbc:sqlite:doctors.db";
			Connection connection = DriverManager.getConnection(url);

			String query = "SELECT doctor_id, name, speciality, review_rating, total_reviews FROM doctors";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();

			doctors = new ArrayList<>();
			doctorsPanel.removeAll();

			boolean matchFound = false;
			
			

			while (resultSet.next()) {
				int id = resultSet.getInt("doctor_id"); // Get the id from the result set
				String name = resultSet.getString("name");
				String speciality = resultSet.getString("speciality");
				double reviewRating = resultSet.getDouble("review_rating");
				int totalReviews = resultSet.getInt("total_reviews");

				Doctor doctor = new Doctor(id, name, speciality, reviewRating, totalReviews);
				doctors.add(doctor);

				// Only display doctors matching the user's input
				if (name.toLowerCase().contains(user_input.getText().toLowerCase())
						|| speciality.toLowerCase().contains(user_input.getText().toLowerCase())) {
					matchFound = true;
					addDoctorPanel(doctor);
				}
			}

			  // If no matches were found, add a label indicating this
	        if (!matchFound) {
	            JLabel noResultsLabel = new JLabel("No matching resutls found!");
	            doctorsPanel.add(noResultsLabel);
	        }

			doctorsPanel.revalidate();
			doctorsPanel.repaint();


			// Close the resources
			resultSet.close();
			preparedStatement.close();
			connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}



	public Main() {

        initComponents(); // Call the method to initialize components
	}
	
	

		private void initComponents() {
		setTitle("Search GUI");

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 660, 706); 
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		//right click doctorsPanel + surround with
		scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(39, 155, 560, 471);
		contentPane.add(scrollPane);

		doctorsPanel = new JPanel();
		doctorsPanel.setBounds(78, 111, 614, 426);
		doctorsPanel.setLayout(new BoxLayout(doctorsPanel, BoxLayout.Y_AXIS));


		scrollPane.setViewportView(doctorsPanel);

		Panel panel = new Panel();
		panel.setBackground(new Color(36, 48, 110));
		panel.setBounds(0, 0, 643, 130);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("Medical Centre");
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 24));
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setBounds(44, 22, 178, 27);
		panel.add(lblNewLabel);

		filter = new JComboBox<>();
		filter.setBackground(Color.WHITE);
		filter.setBounds(490, 63, 114, 32);
		panel.add(filter);

		user_input = new JTextField();
		user_input.setBounds(44, 64, 432, 32);
		panel.add(user_input);

		user_input.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 10) {

					searchText = user_input.getText();

					loadDataFromDatabase();
				}
			}
		});

		user_input.setColumns(10);
		contentPane.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{scrollPane, doctorsPanel}));
		setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{doctorsPanel, scrollPane, contentPane}));

		filter.addItem("Highest rating");
		filter.addItem("Lowest rating");

		filter.addActionListener(e -> {
			if (doctors != null) {
				filterAndSortDoctors();
			}
		});
	}




	private void filterAndSortDoctors() {
		doctorsPanel.removeAll();

		// Filter the doctors based on user input
		List<Doctor> filteredDoctors = new ArrayList<>();
		for (Doctor doctor : doctors) {
			if (doctor.getName().toLowerCase().contains(user_input.getText().toLowerCase())
					|| doctor.getSpeciality().toLowerCase().contains(user_input.getText().toLowerCase())) {
				filteredDoctors.add(doctor);
			}
		}

		
		
		// Sort the filtered doctors list based on the selected filter option
		if (filter.getSelectedItem().equals("Highest rating")) {
			Collections.sort(filteredDoctors, Comparator.comparingDouble(Doctor::getReviewRating).reversed());
		} else if (filter.getSelectedItem().equals("Lowest rating")) {
			Collections.sort(filteredDoctors, Comparator.comparingDouble(Doctor::getReviewRating));
		}

		// Display the filtered and sorted doctors
		for (Doctor doctor : filteredDoctors) {
			addDoctorPanel(doctor);
		}

		doctorsPanel.revalidate();
		doctorsPanel.repaint();
	}


	private void addDoctorPanel(Doctor doctor) {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		panel.setLayout(new GridLayout(1, 2)); 

		// Panel for doctor details (left side)
		JPanel detailsPanel = new JPanel();
		detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
		detailsPanel.setMaximumSize(new Dimension(560, 471)); // Adjust the maximum height as needed


		JLabel nameLabel = new JLabel("Name: " + doctor.getName());
		JLabel specialityLabel = new JLabel("Speciality: " + doctor.getSpeciality());
		JLabel ratingLabel = new JLabel("Rating: " + doctor.getReviewRating());
		JLabel totalReviewsLabel = new JLabel("Total Reviews: " + doctor.getTotalReviews());

		detailsPanel.add(nameLabel);
		detailsPanel.add(specialityLabel);
		detailsPanel.add(ratingLabel);
		detailsPanel.add(totalReviewsLabel);

		// Panel for buttons (right side)
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.setMaximumSize(new Dimension(560, 471)); // Adjust the maximum height as needed


		JButton viewProfileButton = new JButton("View Profile");
		JButton provideReviewButton = new JButton("Provide a Review");

		viewProfileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ProfileGUI profileGUI = new ProfileGUI(doctor);
				profileGUI.NewScreen();
			}
		});



		provideReviewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Selected Doctor: " + doctor.getName() + ", ID: " + doctor.getId());

				DoctorReviewGUI review = new DoctorReviewGUI(doctor);

				review.NewScreen();
			}
		});


		buttonPanel.add(viewProfileButton);
		buttonPanel.add(provideReviewButton);

		// Add both sub-panels to the main panel
		panel.add(detailsPanel);
		panel.add(buttonPanel);

		// Add the main panel to the doctorsPanel
		doctorsPanel.add(panel);

	}


	public void NewScreen() {
		// TODO Auto-generated method stub

		EventQueue.invokeLater(() -> {
			try {
				Main window = new Main();
				window.setVisible(true);

			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}


}









