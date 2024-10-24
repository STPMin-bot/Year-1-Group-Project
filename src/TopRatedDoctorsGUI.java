import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TopRatedDoctorsGUI extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
    private JPanel topRatedDoctorsPanel; // Panel for displaying top-rated doctors
    private List<Doctor> doctors;
    private JLabel lblNewLabel;


    public static void main(String[] args) {
        TopRatedDoctorsGUI frame = new TopRatedDoctorsGUI();
        frame.setVisible(true);
    }

    public TopRatedDoctorsGUI() {
		setTitle("Top Rated Doctors");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 660, 706);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Initialize the top-rated doctors panel
        topRatedDoctorsPanel = new JPanel();
        topRatedDoctorsPanel.setLayout(new BoxLayout(topRatedDoctorsPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(topRatedDoctorsPanel);
        scrollPane.setBounds(39, 206, 560, 420);
        contentPane.add(scrollPane);
        
        lblNewLabel = new JLabel("Top Rated Doctors");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setFont(new Font("Arial", Font.BOLD, 18));
        lblNewLabel.setBounds(225, 158, 179, 22);
        contentPane.add(lblNewLabel);
        
        Panel panel = new Panel();
        panel.setLayout(null);
        panel.setBackground(new Color(36, 48, 110));
        panel.setBounds(0, 0, 643, 130);
        contentPane.add(panel);
        
        JLabel lblNewLabel_1 = new JLabel("Medical Centre");
        lblNewLabel_1.setForeground(Color.WHITE);
        lblNewLabel_1.setFont(new Font("Arial", Font.BOLD, 24));
        lblNewLabel_1.setBounds(44, 22, 178, 27);
        panel.add(lblNewLabel_1);
        
        JButton search_button = new JButton("Search by doctor's name or speciality");
        search_button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		loadSearchResults();
        	}
        });
        
        search_button.setFont(new Font("Arial", Font.BOLD, 14));
        search_button.setBounds(44, 73, 549, 27);
        panel.add(search_button);

  

        // Load and display top-rated doctors when the GUI is initialized
        loadTopRatedDoctors();
    }

    private void loadTopRatedDoctors() {
        // Retrieve top-rated doctors from the database
        doctors = getTopRatedDoctorsFromDatabase();

        // Display top-rated doctors
        if (doctors != null && !doctors.isEmpty()) {
            doctors.forEach(this::addTopRatedDoctorPanel);
        } else {
            JLabel noTopRatedLabel = new JLabel("No top-rated doctors found.");
            topRatedDoctorsPanel.add(noTopRatedLabel);
        }
    }

    private List<Doctor> getTopRatedDoctorsFromDatabase() {
        List<Doctor> topRatedDoctors = new ArrayList<>();
        try {
            String url = "jdbc:sqlite:doctors.db";
            Connection connection = DriverManager.getConnection(url);

            // Use a PreparedStatement to execute a SQL query
            String query = "SELECT doctor_id, name, speciality, review_rating, total_reviews FROM doctors " +
                    "ORDER BY review_rating DESC LIMIT 5"; // Limit to top 5 doctors
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("doctor_id");
                String name = resultSet.getString("name");
                String speciality = resultSet.getString("speciality");
                double reviewRating = resultSet.getDouble("review_rating");
                int totalReviews = resultSet.getInt("total_reviews");

                Doctor doctor = new Doctor(id, name, speciality, reviewRating, totalReviews);
                topRatedDoctors.add(doctor);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topRatedDoctors;
    }

    private void addTopRatedDoctorPanel(Doctor doctor) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.setLayout(new GridLayout(1, 2)); // Use GridLayout with 1 row and 2 columns

        // Panel for doctor details (left side)
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));

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
	            //DoctorReviewGUI review = new DoctorReviewGUI(doctor, Main.this); // Pass a reference to the Main frame

	            DoctorReviewGUI review = new DoctorReviewGUI(doctor);
	          
	            review.NewScreen();
	        }
	    });

        buttonPanel.add(viewProfileButton);
        buttonPanel.add(provideReviewButton);

        // Add both sub-panels to the main panel
        panel.add(detailsPanel);
        panel.add(buttonPanel);

        // Add the main panel to the topRatedDoctorsPanel
        topRatedDoctorsPanel.add(panel);
    }

    private void loadSearchResults() {
      
        Main main = new Main();
        main.NewScreen();
		main.loadDataFromDatabase();
    }
}
