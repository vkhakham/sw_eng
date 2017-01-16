package common;

import java.io.Serializable;
import java.util.Calendar;

public class ScheduledAppointment implements Serializable {
    public Calendar cal;
    public Integer patientId;
    public String patientName;

    public ScheduledAppointment(Calendar cal, int patientId, String patientName) {
        this.cal = cal;
        this.patientId = patientId;
        this.patientName = patientName;
    }
}
