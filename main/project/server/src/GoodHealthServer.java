package server.src;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

import common.ClientType;
import common.Message;
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
            case "login":
                reply = handleLogin(msg); break;
            default: throw new RuntimeException("URI not recognized.");
        }
        try {
            client.sendToClient(reply);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected Message handleLogin(Message msg) {
        Message reply = msg.clone();
        // if user is logged in or doesn't exist - refuse log in.
        if (loggedInUsers.get(msg.clientType).get(msg.id) != null ||
                !sqlConnection.employeeExists(msg.id, Integer.class.cast(msg.data))) {
            reply.data = Boolean.FALSE;
        } else {   // user exists
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
