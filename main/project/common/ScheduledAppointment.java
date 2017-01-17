package common;

import java.io.Serializable;
import java.util.Calendar;

public class ScheduledAppointment implements Serializable {
    public String date;
    public Integer patientId;
    public Integer doctorId;
    public String patientName;
    public String branch;
    public String doctorName;

    public ScheduledAppointment(String date, int patientId, Integer doctorId,
                                String patientName, String branch, String doctorName) {
        this.date = date;
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorId = doctorId;
        this.branch = branch;
        this.doctorName = doctorName;
    }
}
