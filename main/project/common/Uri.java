package common;

public enum Uri  {
    // expects password(Integer)in data, gets Boolean in reply data.
    Login,
    // gets List<Appointment> in reply data.
    EmployeeGetQueue,
    // gets List<Long> in reply data.
    PatientGetFreeAppointmentsForGp,
    // expects specialist id(Integer) in data, gets List<Long> in reply data.
    PatientGetFreeAppointmentsForSpecialist,
    // expects String role in data, gets List<Doctor> in reply data.
    PatientGetSpecialistDoctorList,
    // expects doctorId and time in data, gets Boolean value of success.
    PatientScheduleAppointment,
    // gets list of scheduled appontments in reply.
    PatientGetScheduledAppointments,
    // expects doctor and time in data, gets Boolean value of success.
    PatientUnscheduleAppointment,
}
