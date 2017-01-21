package common;

import java.util.LinkedList;
import java.util.List;

public class FreeAppointments {
    private Doctor doctor;
    private List<String> dates;

    public FreeAppointments() {
        dates = new LinkedList<>();
    }

    public FreeAppointments setDoctor(Doctor doctor) {
        this.doctor = doctor;
        return this;
    }

    public FreeAppointments addDate(String date) {
        dates.add(date);
        return this;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public List<String> getDates() {
        return dates;
    }
}
