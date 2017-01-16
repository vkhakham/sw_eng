package server.src;

public class Queries {
    static final String GET_APPOINTMENTS_QUERY =
            "SELECT goodhealth.appointments.time, goodhealth.appointments.patient,  goodhealth.patients.name\n" +
            "from goodhealth.appointments\n" +
            "join goodhealth.patients\n" +
            "on goodhealth.appointments.patient = goodhealth.patients.id\n" +
            "and goodhealth.appointments.time >= ? \n" +
            "and goodhealth.appointments.time < ? \n" +
            "and goodhealth.appointments.doctor = ?" +
            "order by time;";
}
