package common;

import java.io.Serializable;
import java.util.Calendar;

public class ScheduledAppointment implements Serializable {
    public String date;
    public Integer patientId;
    public String patientName;

    public ScheduledAppointment(String date, int patientId, String patientName) {
        this.date = date;
        this.patientId = patientId;
        this.patientName = patientName;
    }

    public String getDate() {
        return date;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public String getPatientName() {
        return patientName;
    }
}
