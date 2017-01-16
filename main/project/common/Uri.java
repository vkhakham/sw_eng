package common;

public enum Uri {
    // expects password(Integer)in data, gets Boolean in reply data.
    Login,
    // gets List<Appointment> in reply data.
    EmployeeGetQueue,
    // gets List<Long> in reply data.
    PatientGetFreeAppointmentsForGp,
    // expects specialist id(Integer) in data, gets List<Long> in reply data.
    PatientGetFreeAppointmentsForSpecialist,
    // gets List<Doctor> in reply data.
    PatientGetSpecialistDoctorListWithFreeAppointments,

    PatientScheduleAppointment,
}
