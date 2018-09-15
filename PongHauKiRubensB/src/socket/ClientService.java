package socket;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Rubens Santos Barbosa
 */
public class ClientService {
 
    private String host = "127.0.0.1";
    private int port = 10001;
    private Socket socket = null;
    private ObjectOutputStream outputStream;
    
    public Socket connect() {
        try {
            this.socket = new Socket(host, port);
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        return socket;
    }
    
    public void send(ProtocolMessage msg) {
        try {
            outputStream.writeObject(msg);
        } catch (IOException ex) {
            Logger.getLogger(ClientService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
