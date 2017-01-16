package server.src;

import common.Appointment;
import common.ClientType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
        runTests();
    }

    private void runTests() {
        //        createAppointments();
        //        List<Appointment> dasd = getAppointments(1, "2017-01-01");
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
// '2017-01-01' '2017-01-03'
    List<Appointment> getAppointments(int doctorId, String date) {
        List<Appointment> appointments = new ArrayList<>();
        try
        {
            PreparedStatement pstmt = conn.prepareStatement(Queries.GET_APPOINTMENTS_QUERY);
            // increment day
            DateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
            Calendar dayLaterCal = Calendar.getInstance();
            dayLaterCal.setTime( dt.parse( date ) );
            dayLaterCal.add( Calendar.DATE, 1 );
            //set pstmt params
            pstmt.setString(1, date);
            pstmt.setString(2, dt.format(dayLaterCal.getTime()));
            pstmt.setInt(3, doctorId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Calendar cal = new Calendar.Builder().setInstant(rs.getTimestamp(1).getTime()).build();
                int patientId = rs.getInt(2);
                String patientName = rs.getString(3);
                appointments.add(new Appointment(cal, patientId, patientName));
            }
        } catch (ParseException | SQLException e) {e.printStackTrace();}
        return appointments;
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
