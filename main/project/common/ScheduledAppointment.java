package common;

import java.io.Serializable;

public class ScheduledAppointment implements Serializable {
    public String date;
    public Integer patientId;
    public Integer doctorId;
    public String patientName;
    public String branch;
    public String doctorName;
    public String role;

    public ScheduledAppointment() {}

    public ScheduledAppointment setDate(String date) {
        this.date = date;
        return this;
    }

    public ScheduledAppointment setPatientId(Integer patientId) {
        this.patientId = patientId;
        return this;
    }

    public ScheduledAppointment setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
        return this;
    }

    public ScheduledAppointment setPatientName(String patientName) {
        this.patientName = patientName;
        return this;
    }

    public ScheduledAppointment setBranch(String branch) {
        this.branch = branch;
        return this;
    }

    public ScheduledAppointment setDoctorName(String doctorName) {
        this.doctorName = doctorName;
        return this;
    }

    public ScheduledAppointment setRole(String role) {
        this.role = role;
        return this;
    }
}
