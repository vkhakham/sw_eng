package server.src;

import common.Doctor;
import common.ScheduledAppointment;
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

    private Connection conn;
    private static final DateFormat dtDate = new SimpleDateFormat("yyyy-MM-dd");
    private static final DateFormat dtDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm");

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
//        createAppointmentsForDcotor(5, false);
//        createAppointments();
//        List<ScheduledAppointment> dasd = getSechduledAppointments(1, "2017-01-01");
//        List<String> dasd = getFreeAppointments(2);
//        List<Doctor> doctors = getSpecialistDoctorList(4, "dr_orthopedic");
//        scheduleAppointment(1, 1, "2017-01-01 08:00:00");
//        int doctor_id = getGpDoctorIdForPatient(1);
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

    Object getFutureScheduledAppointments(Integer id) {
        List<ScheduledAppointment> scheduledAppointments = new ArrayList<>();
        try
        {
            PreparedStatement pstmt = conn.prepareStatement(Queries.GET_FUTURE_SCHEDULED_APPOINTMENTS_FOR_PATIENT_QUERY);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String date = dtDateTime.format(dtDateTime.parse(rs.getString(1)));
                int patientId = rs.getInt(2);
                String patientName = rs.getString(3);
                scheduledAppointments.add(new ScheduledAppointment(date, patientId, doctorId, patientName, null, null));
            }
        } catch (ParseException | SQLException e) {e.printStackTrace();}
        return scheduledAppointments;

    }

    boolean scheduleAppointment(int patientId, int doctorID, String date) {
        int res = 0;
        try
        {
            PreparedStatement pstmt = conn.prepareStatement(Queries.SET_APPOINTMET);
            pstmt.setInt(1, patientId);
            pstmt.setString(2, date);
            pstmt.setInt(3, doctorID);
            res = pstmt.executeUpdate();
        } catch (RuntimeException | SQLException e) {e.printStackTrace();}

        return res != 0;
    }

    List<Doctor> getSpecialistDoctorList(int patientId, String role) {
        List<Doctor> doctors = new ArrayList<>();
        try
        {
            PreparedStatement pstmt = conn.prepareStatement(Queries.GET_SPECIALIST_DOCTOR_LIST_FOR_PATIENT);
            pstmt.setInt(1, patientId);
            pstmt.setString(2, role);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                doctors.add(new Doctor(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
        } catch (RuntimeException | SQLException e) {e.printStackTrace();}
        return doctors;
    }

    int getGpDoctorIdForPatient(int patientId) {
        int doctorId = 0;
        try
        {
            PreparedStatement pstmt = conn.prepareStatement(Queries.GET_GP_DOCTOR_ID);
            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                doctorId = rs.getInt(1);
            } else throw new RuntimeException("patientId id's gp not found.");
        } catch (RuntimeException | SQLException e) {e.printStackTrace();}
        return doctorId;
    }

    List<String> getFreeAppointments(int doctorId) {
        List<String> freeAppointments = new ArrayList<>();
        try
        {
            PreparedStatement pstmt = conn.prepareStatement(Queries.GET_DOCTOR_ROLE);
            pstmt.setInt(1, doctorId);
            ResultSet rs = pstmt.executeQuery();
            String doctorRole;
            if (rs.next()) {
                doctorRole = rs.getString(1);
            } else throw new RuntimeException("doctor id not found.");
            pstmt = conn.prepareStatement(Queries.GET_UNSCHEDULED_APPOINTMENTS_QUERY);
            // TODO change to 30 90
            pstmt.setInt(1, doctorRole.equals("dr_gp") ? 2 : 5);
            pstmt.setInt(2, doctorId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String date = dtDateTime.format(dtDateTime.parse(rs.getString(1)));
                freeAppointments.add(date);
            }
        } catch (ParseException | RuntimeException | SQLException e) {e.printStackTrace();}
        return freeAppointments;
    }

    List<ScheduledAppointment> getSechduledAppointments(int doctorId, String todayDate) {
        List<ScheduledAppointment> scheduledAppointments = new ArrayList<>();
        try
        {
            PreparedStatement pstmt = conn.prepareStatement(Queries.GET_SCHEDULED_APPOINTMENTS_FOR_DOCTOR_QUERY);
            pstmt.setString(1, todayDate);
            pstmt.setString(2, todayDate);
            pstmt.setInt(3, doctorId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String date = dtDateTime.format(dtDateTime.parse(rs.getString(1)));
                int patientId = rs.getInt(2);
                String patientName = rs.getString(3);
                scheduledAppointments.add(new ScheduledAppointment(date, patientId, doctorId, patientName, null, null));
            }
        } catch (ParseException | SQLException e) {e.printStackTrace();}
        return scheduledAppointments;
    }

    Calendar incrementCalendarDays(String date, int increment) throws ParseException {
        Calendar dayLaterCal = Calendar.getInstance();
        dayLaterCal.setTime( dtDate.parse( date ) );
        dayLaterCal.add( Calendar.DATE, increment );
        return dayLaterCal;
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
