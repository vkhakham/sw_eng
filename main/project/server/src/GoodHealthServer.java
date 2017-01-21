package server.src;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

import common.*;
import ocsf.server.*;

public class GoodHealthServer extends AbstractServer {
    private SqlConnection sqlConnection;
    private EnumMap<ClientType, Map<Integer, Integer>> loggedInUsers;

    /**
     * Constructs a new server.
     *
     * @param port the port number on which to listen.
     */
    public GoodHealthServer(int port) {
        super(port);
        sqlConnection = new SqlConnection();
        loggedInUsers = new EnumMap<>(ClientType.class);
        loggedInUsers.put(ClientType.Employee, new ConcurrentHashMap<>());
        loggedInUsers.put(ClientType.Patient, new ConcurrentHashMap<>());
    }

    @Override
    protected void handleMessageFromClient(Object objMsg, ConnectionToClient client) {
        Message msg = Message.class.cast(objMsg);
        Message reply = msg.clone();
        switch (msg.uri) {
            case Login:
                handleLogin(msg, reply); break;
            case EmployeeGetQueue:
                employeeGetQueue(msg, reply); break;
            case PatientGetFreeAppointmentsForGp:
                patientGetFreeAppointmentsForGp(msg, reply); break;
            case PatientGetFreeAppointmentsForSpecialist:
                PatientGetFreeAppointmentsForSpecialist(msg, reply); break;
            case PatientGetSpecialistDoctorList:
                PatientGetSpecialistDoctorList(msg, reply); break;
            case PatientScheduleAppointment:
                PatientScheduleAppointment(msg, reply); break;
            case PatientGetFutureScheduledAppointments:
                PatientGetFutureScheduledAppointments(msg, reply); break;
            case PatientUnscheduleAppointment:
                PatientUnscheduleAppointment(msg, reply); break;
            default: throw new RuntimeException("URI not recognized.");
        }
        try {
            client.sendToClient(reply);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void PatientUnscheduleAppointment(Message msg, Message reply) {
        verifySessionId(msg.id, msg.sessionId, msg.clientType);
        ScheduledAppointment appointment = ScheduledAppointment.class.cast(msg.data);
        reply.data = sqlConnection.unscheduleAppointment(msg.id, appointment.getDoctor().getId(), appointment.date);
    }

    private void PatientGetFutureScheduledAppointments(Message msg, Message reply) {
        verifySessionId(msg.id, msg.sessionId, msg.clientType);
        reply.data = sqlConnection.getFutureScheduledAppointments(msg.id);
    }

    private void PatientScheduleAppointment(Message msg, Message reply) {
        verifySessionId(msg.id, msg.sessionId, msg.clientType);
        ScheduledAppointment appointment = ScheduledAppointment.class.cast(msg.data);
        reply.data = sqlConnection.scheduleAppointment(msg.id, appointment.getDoctor().getId(), appointment.date);
    }

    private void PatientGetSpecialistDoctorList(Message msg, Message reply) {
        verifySessionId(msg.id, msg.sessionId, msg.clientType);
        reply.data = sqlConnection.getSpecialistDoctorList(msg.id, String.class.cast(msg.data));
    }

    private void PatientGetFreeAppointmentsForSpecialist(Message msg, Message reply) {
        verifySessionId(msg.id, msg.sessionId, msg.clientType);
        reply.data = sqlConnection.getFreeAppointments(Integer.class.cast(msg.data));
    }

    private void patientGetFreeAppointmentsForGp(Message msg, Message reply) {
        verifySessionId(msg.id, msg.sessionId, msg.clientType);
        reply.data = sqlConnection.getFreeAppointments(sqlConnection.getGpDoctorIdForPatient(msg.id));
    }

    private void employeeGetQueue(Message msg, Message reply) {
        verifySessionId(msg.id, msg.sessionId, msg.clientType);
        String date = String.class.cast(msg.data);
        List<ScheduledAppointment> scheduledAppointments = sqlConnection.getSechduledAppointmentsForDate(msg.id, date);
        reply.data = scheduledAppointments;
        if (scheduledAppointments.size() == 0) {
            reply.error = ErrorType.NotFound;
        }
    }

    private void handleLogin(Message msg, Message reply) {
        // if user is logged in
        if (loggedInUsers.get(msg.clientType).get(msg.id) != null) {
            reply.data = Boolean.FALSE;
            reply.error = ErrorType.UserLoggedIn;
        }
        // if user doesn't exist - refuse log in.
        if (!sqlConnection.userExists(msg.id, Integer.class.cast(msg.data), msg.clientType)) {
            reply.data = Boolean.FALSE;
            reply.error = ErrorType.UserNotFound;
        } else {   // user exists and login is valid
            int sessionId = ThreadLocalRandom.current().nextInt(0, 1000000);
            loggedInUsers.get(msg.clientType).put(msg.id, sessionId);
            reply.data = Boolean.TRUE;
            reply.sessionId = sessionId;
        }
    }

    private boolean verifySessionId(int id, int sessionId, ClientType clientType) {
        Integer storedSessionId = loggedInUsers.get(clientType).get(id);
        return storedSessionId.equals(sessionId);
    }
}
