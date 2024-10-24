import java.util.ArrayList;
import java.util.List;

public class Doctor {
    private int id; // Add id field
    private String name;
    private String speciality;
    private double reviewRating;
    private int totalReviews;
    private List<String> feedbackComments;
    private double newRating;
    
    // Constructor with parameters including id
    public Doctor(int id, String name, String speciality, double reviewRating, int totalReviews) {
        this.id = id;
        this.name = name;
        this.speciality = speciality;
        this.reviewRating = reviewRating;
        this.totalReviews = totalReviews;
        this.feedbackComments = new ArrayList<>();
    }

    // Default constructor
    public Doctor() {
    }

    // Getter and setter for id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Getter for speciality
    public String getSpeciality() {
        return speciality;
    }

    // Getter for reviewRating
    public double getReviewRating() {
        reviewRating = Double.parseDouble(String.format("%.1f", reviewRating));
        return reviewRating;

    }
    
    
    public double getnewRating() {
        newRating = Double.parseDouble(String.format("%.1f", newRating));
        return newRating;
    }

    // Getter for totalReviews
    public int getTotalReviews() {
        return totalReviews;
    }

    // Getter for feedbackComments
    public List<String> getFeedbackComments() {
        return feedbackComments;
    }

    // Method to add feedback
    
    // toString method
    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", speciality='" + speciality + '\'' +
                ", reviewRating=" + reviewRating +
                ", totalReviews=" + totalReviews +
                ", feedbackComments=" + feedbackComments +
                '}';
    }
}
