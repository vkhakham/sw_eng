package server.src;

class Queries {
    static final String GET_SCHEDULED_APPOINTMENTS_FOR_DOCTOR_QUERY =
            "SELECT goodhealth.appointments.time, goodhealth.appointments.patient,  goodhealth.patients.name \n" +
            "from goodhealth.appointments \n" +
            "join goodhealth.patients \n" +
            "on goodhealth.appointments.patient = goodhealth.patients.id \n" +
            "and goodhealth.appointments.time >= ? \n" +
            "and goodhealth.appointments.time < date(? + interval 1 day) \n" +
            "and goodhealth.appointments.doctor = ? \n" +
            "order by time;";

    static final String GET_FUTURE_SCHEDULED_APPOINTMENTS_FOR_PATIENT_QUERY =
            "SELECT goodhealth.appointments.time, goodhealth.employees.name, goodhealth.employees.role,\n" +
            "goodhealth.employees.branch_id, goodhealth.appointments.patient, goodhealth.appointments.doctor\n" +
            "from goodhealth.appointments\n" +
            "join goodhealth.employees\n" +
            "on goodhealth.appointments.doctor=goodhealth.employees.id\n" +
            "and goodhealth.appointments.patient = ?\n" +
            "and goodhealth.appointments.time >= date(now() + interval 1 day)\n" +
            "order by time;\n";

    static final String GET_UNSCHEDULED_APPOINTMENTS_QUERY =
            "SELECT goodhealth.appointments.time\n" +
            "from goodhealth.appointments\n" +
            "where goodhealth.appointments.time >= now()\n" +
            "and goodhealth.appointments.time < date(now() + interval ? day)\n" +
            "and goodhealth.appointments.doctor = ?\n" +
            "and goodhealth.appointments.patient = 0\n" +
            "order by time;";

    static final String GET_DOCTOR_INFORMATION = "select name, branch_id, role from goodhealth.employees where id = ?;";

    static final String GET_GP_DOCTOR_ID = "select goodhealth.employees.id from goodhealth.employees \n" +
            "join goodhealth.patients \n" +
            "on goodhealth.patients.family_doctor = goodhealth.employees.id and goodhealth.patients.id = ?;";

    static final String GET_SPECIALIST_DOCTOR_LIST_FOR_PATIENT =
            "select EMP.id, EMP.name, EMP.branch_id, EMP.role, last_visit\n" +
					"from goodhealth.employees EMP \n" +
					"left join \n" +
					"(\n" +
					"\tselect APPNT.doctor, (max(APPNT.time)) AS last_visit \n" +
					"\tfrom goodhealth.appointments APPNT \n" +
					"\tWHERE APPNT.patient = ? and APPNT.time<= now() \n" +
					"\tgroup by APPNT.doctor\n" +
					") APPNT on EMP.id=APPNT.doctor where EMP.role=?\n" +
					"order by last_visit desc\n";

    static final String SET_APPOINTMET = "update goodhealth.appointments\n" +
            "set goodhealth.appointments.patient = ?\n" +
            "where goodhealth.appointments.time=? and doctor=? and patient=0;";

    static final String UNSET_APPOINTMET = "update goodhealth.appointments\n" +
            "set goodhealth.appointments.patient = 0\n" +
            "where goodhealth.appointments.time=? \n" +
            "and goodhealth.appointments.time >= date(now() + interval 1 day) \n" +
            "and doctor=? and patient=?;";

}
