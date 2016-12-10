import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class mysqlConnection {

	public static void main(String[] args) 
	{
		try 
		{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {/* handle the error*/}
        
        try 
        {
        	Connection conn =
        			DriverManager.getConnection("jdbc:mysql://localhost/test","student","student");

            System.out.println("SQL connection succeed");

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT price FROM flights WHERE num=387");
            if (rs.next()){
                System.out.println("New flight 387 price:" + rs.getString(1));
            }
            System.out.println("original:");
            printCourses(conn);
            incrementPriceResultSet(conn);
            System.out.println("** increment prices with ResultSet:");
            printCourses(conn);
            incrementPricePreparedStatement(conn);
            System.out.println("** increment prices with prepared statement:");
            printCourses(conn);

            conn.close();
     	} catch (SQLException ex) 
     	    {/* handle any errors*/
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            }
   	}

   	static void incrementPriceResultSet(Connection con) {
        Statement stmt;
        try
        {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery("SELECT * FROM flights WHERE distance > 1000");
            while (rs.next()) {
                rs.updateFloat("price", rs.getFloat("price") + 50);
                rs.updateRow();
            }
        } catch (SQLException e) {e.printStackTrace();}
    }

    static void incrementPricePreparedStatement(Connection con) {
        PreparedStatement stmt;
        try
        {
            stmt = con.prepareStatement("UPDATE flights SET price = (price + 50) WHERE distance > 1000");
            int ret = stmt.executeUpdate();
        } catch (SQLException e) {e.printStackTrace();}
    }

	public static void printCourses(Connection con)
	{
		Statement stmt;
		try 
		{
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM flights;");
	 		while(rs.next())
	 		{
				 // Print out the values
				 System.out.println(rs.getString(1)+"  " +rs.getString(2) + "  " + rs.getString(3) + "  " + rs.getString(4)+ "  " + rs.getString(5));
			} 
			//rs.close();
	 	    if (rs != null) {
	 	        try {
	 	            rs.close();
	 	        } catch (SQLException e) { /* ignored */}
	 	    }
	 	    if (stmt != null) {
	 	        try {
	 	            stmt.close();
	 	        } catch (SQLException e) { /* ignored */}
	 	    }
			//stmt.executeUpdate("UPDATE course SET semestr=\"W08\" WHERE num=61309");
		} catch (SQLException e) {e.printStackTrace();}
	}
}
