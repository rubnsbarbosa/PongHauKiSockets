package socket;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Rubens Santos Barbosa
 */
public class ProtocolMessage implements Serializable {
    
    private String nameClient;
    private String message;
    private boolean myTurn;
    private Set<String> clientsOnline = new HashSet<String>();
    private Action action;

    public String getNameClient() {
        return nameClient;
    }

    public void setNameClient(String nameClient) {
        this.nameClient = nameClient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        
    }

    public boolean isMyTurn() {
        return myTurn;
    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }
    
    public Set<String> getClientsOnline() {
        return clientsOnline;
    }

    public void setClientsOnline(Set<String> clientsOnline) {
        this.clientsOnline = clientsOnline;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    
    public enum Action {
        CONNECT,
        DISCONNECT,
        SEND_ONE,
        SEND_ALL,
        MOVE,
        RESTART
    }
    
}
