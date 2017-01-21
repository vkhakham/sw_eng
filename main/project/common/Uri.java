package common;

public enum Uri  {
    // expects password(Integer)in data, gets Boolean in reply data.
    Login,
    // gets List<Appointment> in reply data.
    EmployeeGetQueue,
    // gets FreeAppointments object in reply data.
    PatientGetFreeAppointmentsForGp,
    // expects specialist id(Integer) in data, gets FreeAppointments object in reply data.
    PatientGetFreeAppointmentsForSpecialist,
    // expects String role in data, gets List<Doctor> in reply data.
    PatientGetSpecialistDoctorList,
    // expects scheduledAppointment object in data, gets Boolean value of success.
    PatientScheduleAppointment,
    // gets list of scheduled appointments in reply.
    PatientGetFutureScheduledAppointments,
    // expects scheduledAppointment object in data, gets Boolean value of success.
    PatientUnscheduleAppointment,
}
