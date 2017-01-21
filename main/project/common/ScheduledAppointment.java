package common;

import java.io.Serializable;

public class ScheduledAppointment implements Serializable {
    public String date;
    private Doctor doctor;
    private Patient patient;

    public String getDate() {
        return date;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public ScheduledAppointment setDate(String date) {
        this.date = date;
        return this;
    }

    public ScheduledAppointment setDoctor(Doctor doctor) {
        this.doctor = doctor;
        return this;
    }

    public ScheduledAppointment setPatient(Patient patient) {
        this.patient = patient;
        return this;
    }
}
