
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 */
public class Connect {
     /**
     * Connect to a sample database
     * @throws ClassNotFoundException 
     */
	
	
    public static void connect() throws ClassNotFoundException {

        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:doctors.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            System.out.println("Connection to SQLite has been established.");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    /**
     * @param args the command line arguments
     * @throws ClassNotFoundException 
     */
    public static void main(String[] args) throws ClassNotFoundException {

        connect();
    }
	public Connection getConnection() {
		// TODO Auto-generated method stub
		return null;
	}
}