package server.src;

import common.ClientType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

class SqlConnection {
    Connection conn;
    SqlConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {  System.out.println("Exception: " + ex.getMessage());}

        try {
            conn = DriverManager.getConnection("jdbc:mysql://104.155.33.106/goodhealth","root",null);
            System.out.println("SQL connection succeed");
        } catch (SQLException ex)
        {/* handle any errors*/
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
//        createAppointments();
    }

    boolean userExists(int username, int password, ClientType clientType) {
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM goodhealth."
                    + (clientType == ClientType.Employee ? "employees" : "patients")
                    +" where id = ? and password = ?;");
            pstmt.setInt(1, username);
            pstmt.setInt(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    void getAppointments() {
        String query = "SELECT goodhealth.appointments.time, goodhealth.appointments.patient,  goodhealth.patients.name\n" +
                "from goodhealth.appointments\n" +
                "join goodhealth.patients\n" +
                "on goodhealth.appointments.patient = goodhealth.patients.id\n" +
                "and goodhealth.appointments.time >= '2017-01-01' \n" +
                "and goodhealth.appointments.time < '2017-01-03' \n" +
                "and goodhealth.appointments.doctor = 1;";
    }

    void createAppointments() {
        Statement stmt;
        try
        {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id,role FROM goodhealth.employees");
            while (rs.next()) {
                createAppointmentsForDcotor(rs.getInt(1), rs.getString(2).equals("dr_gp"));
            }
        } catch (SQLException e) {e.printStackTrace();}
    }

    private void createAppointmentsForDcotor(Integer doctor, boolean isGp) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2017);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        int incrementMinutes = isGp ? 15 : 20;
        for (int month = 0; month < 1; month++) {
            cal.set(Calendar.MONTH, month);
            for (int day = 1; day < cal.getActualMaximum(Calendar.DAY_OF_MONTH); day++) {
                cal.set(Calendar.DAY_OF_MONTH, day);
                if (cal.get(Calendar.DAY_OF_WEEK) <= 5) {
                    for (int hour = 8; hour < 16; hour++) {
                        cal.set(Calendar.HOUR_OF_DAY, hour);
                        for (int minutes = 0; minutes < 60; minutes+=incrementMinutes){
                            cal.set(Calendar.MINUTE, minutes);
                            try
                            {
                                PreparedStatement pstmt = conn.prepareStatement(
                                        "INSERT INTO goodhealth.appointments(time, doctor, patient)VALUES(?, ? , ?);");
                                Object param = new java.sql.Timestamp(cal.getTime().getTime());
                                System.out.println("doctor:" + doctor + " time: " + new java.sql.Timestamp(cal.getTime().getTime()));
                                pstmt.setObject(1, param);
                                pstmt.setInt(2, doctor);
                                pstmt.setInt(3, 0);
                                pstmt.executeUpdate();
                            } catch (SQLException e) {e.printStackTrace();}
                        }
                    }
                }
            }
        }
    }

}
