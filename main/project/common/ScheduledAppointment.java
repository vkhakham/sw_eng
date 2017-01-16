package common;

import java.util.Calendar;

public class ScheduledAppointment {
    public Calendar cal;
    public Integer patientId;
    public String patientName;
    public ScheduledAppointment(Calendar cal, int patientId, String patientName) {
        this.cal = cal;
        this.patientId = patientId;
        this.patientName = patientName;
    }
}
