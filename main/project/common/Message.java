package common;

import java.io.Serializable;

public class Message implements Serializable {
    public String uri;
    public Integer id;
    public Integer sessionId;
    public ClientType clientType;
    public Object data;

    public Message(String uri, Integer id, Integer sessionId, ClientType clientType, Object data) {
        this.uri = uri;
        this.data = data;
        this.id = id;
        this.sessionId = sessionId;
        this.clientType = clientType;
    }

    public Message clone() {
        return new Message(uri, id, sessionId, clientType, data);
    }
}
