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
        Map<Integer, Integer> loggedInPatients = new ConcurrentHashMap<>();
        Map<Integer, Integer> loggedInEmployees = new ConcurrentHashMap<>();
        loggedInUsers = new EnumMap<>(ClientType.class);
        loggedInUsers.put(ClientType.Employee, loggedInEmployees);
        loggedInUsers.put(ClientType.Patient, loggedInPatients);
    }

    @Override
    protected void handleMessageFromClient(Object objMsg, ConnectionToClient client) {
        Message msg = Message.class.cast(objMsg);
        Message reply;
        switch (msg.uri) {
            case Login:
                reply = handleLogin(msg); break;
            case EmployeeGetQueue:
                reply = employeeGetQueue(msg); break;
            default: throw new RuntimeException("URI not recognized.");
        }
        try {
            client.sendToClient(reply);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Message employeeGetQueue(Message msg) {
        verifySessionId(msg.id, msg.sessionId, msg.clientType);
        Message reply = msg.clone();
        String date = String.class.cast(msg.data);
        List<Appointment> appointments = sqlConnection.getAppointments(msg.id, date);
        msg.data = appointments;
        if (appointments.size() == 0) {
            reply.error = ErrorType.NotFound;
        }
        return reply;
    }

    protected Message handleLogin(Message msg) {
        Message reply = msg.clone();
        // if user is logged in
        if (loggedInUsers.get(msg.clientType).get(msg.id) != null) {
            reply.data = Boolean.FALSE;
            reply.error = ErrorType.UserLoggedIn;
        }
        // if user doesn't exist - refuse log in.
        if (!sqlConnection.userExists(msg.id, Integer.class.cast(msg.data), msg.clientType)) {
            reply.data = Boolean.FALSE;
            reply.error = ErrorType.NotFound;
        } else {   // user exists and login is valid
            int sessionId = ThreadLocalRandom.current().nextInt(0, 1000000);
            loggedInUsers.get(msg.clientType).put(msg.id, sessionId);
            reply.data = Boolean.TRUE;
            reply.sessionId = sessionId;
        }
        return reply;
    }

    protected boolean verifySessionId(int id, int sessionId, ClientType clientType) {
        Integer storedSessionId = loggedInUsers.get(clientType).get(id);
        return storedSessionId.equals(sessionId);
    }
}
