package socket;

import socket.ProtocolMessage.Action;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Rubens Santos Barbosa
 */
public class ServerService {
    
    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private Map<String, ObjectOutputStream> mapUsersOnline = new HashMap<String, ObjectOutputStream>();
    private int port = 10001;
    
    ServerService() {
        try {
            
            serverSocket = new ServerSocket(port);
            System.out.println("Aguardando conexão...");
          
            while (true) {
                socket = serverSocket.accept(); // monitoring clients
                System.out.println("Conexão Estabelecida."); 
                
                new Thread(new ListennerSocketServer(socket)).start();
            }
          
        } catch (IOException e) {
            e.printStackTrace();
        }  
          
    }
    
    private class ListennerSocketServer implements Runnable {
        
        private ObjectOutputStream outputStream;
        private ObjectInputStream  inputStream;
        
        ListennerSocketServer(Socket socket) {
            try {
                this.outputStream = new ObjectOutputStream(socket.getOutputStream());
                this.inputStream  = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            } 
        }
        
        // Starting thread to listen clients
        @Override
        public void run() {
            ProtocolMessage message = null;
            try {                         
                while ((message = (ProtocolMessage) inputStream.readObject()) != null) {
                    Action action = message.getAction();
                    
                    if (action.equals(Action.CONNECT)) {
                        boolean isConnect = connect(message, outputStream);
                        if (isConnect) {
                            mapUsersOnline.put(message.getNameClient(), outputStream);
                        }
                    } else if (action.equals(Action.DISCONNECT)) {
                        disconnect(message);
                        return;
                    } else if (action.equals(Action.SEND_ONE)) {
                        sendOne(message, outputStream);
                    } else if (action.equals(Action.SEND_ALL)) {
                        sendAll(message);
                    } else if (action.equals(Action.MOVE)) {
                        sendMove(message, outputStream);
                    } else if (action.equals(Action.RESTART)) {
                        sendRestart(message);
                    }
                }
            } catch (IOException ex) {
                
                //Logger.getLogger(ClientChatGame.class.getName()).log(Level.SEVERE, null, ex);
                disconnect(message);
                //System.out.println(message.getNameClient() + " logout"); // print this when close window is press (X)
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClientChatGame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    private boolean connect(ProtocolMessage message, ObjectOutputStream outputStream) {
        if (mapUsersOnline.size() == 0) { // no one connected 
            message.setMessage("Você joga com as bolinhas laranjas e aguarda o movimento do openente.");
            message.setMyTurn(false); // set player orange with: player 2
            sendOne(message, outputStream); // send this message to client
            return true; // connection ok
        }
        
        // if the list of clients has an online then check name is equals
        for (Map.Entry<String, ObjectOutputStream> keyClient : mapUsersOnline.entrySet()) {
            if (keyClient.getKey().equals(message.getNameClient())) {
                message.setMessage("NO");
                sendOne(message, outputStream); // send this message to client
                return false; // not connection if names equals
            } else {
                message.setMessage("Você joga com as bolinhas amarelas e inicia a partida.");
                message.setMyTurn(true); // set player yellow with: player 1
                sendOne(message, outputStream); // send this message to client
                return true;
            }
        }
        
        return false;
    }
    
    private void disconnect(ProtocolMessage message) { 
        mapUsersOnline.remove(message.getNameClient()); // remove client that request leave the game
        message.setMessage("desistiu e vc venceu por WO!");
        
        message.setAction(Action.SEND_ONE);

        sendGiveup(message);
    }
    
    private void sendOne(ProtocolMessage message, ObjectOutputStream outputStream) {
        try {
            outputStream.writeObject(message);
            
            //outputStream.flush();
        } catch (IOException ex) {
            Logger.getLogger(ServerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void sendAll(ProtocolMessage message) {
        for (Map.Entry<String, ObjectOutputStream> keyClient : mapUsersOnline.entrySet()) {
            // not send message to myself
            if (!keyClient.getKey().equals(message.getNameClient())) {
                message.setAction(Action.SEND_ONE);
                try {
                    keyClient.getValue().writeObject(message);
                    keyClient.getValue().reset();
                   // keyClient.getValue().flush();
                } catch (IOException ex) {
                    Logger.getLogger(ServerService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void sendGiveup(ProtocolMessage message) {
        for (Map.Entry<String, ObjectOutputStream> keyClient : mapUsersOnline.entrySet()) {
            // not send message to myself
            if (!keyClient.getKey().equals(message.getNameClient())) {
                try {
                    keyClient.getValue().writeObject(message);
                } catch (IOException ex) {
                    Logger.getLogger(ServerService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    private void sendMove(ProtocolMessage message, ObjectOutputStream outputStream) {
        for (Map.Entry<String, ObjectOutputStream> keyClient : mapUsersOnline.entrySet()) {
            // not send message to myself
            message.setAction(Action.MOVE);
            if (!keyClient.getKey().equals(message.getNameClient())) {
                try {
                    keyClient.getValue().writeObject(message);
                    keyClient.getValue().reset();
                    keyClient.getValue().flush();
                    System.out.println("entrando no send move sever...");
                    
                } catch (IOException ex) {
                    Logger.getLogger(ServerService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void sendRestart(ProtocolMessage message) {
        message.setMessage("vc venceu!");
        
        message.setAction(Action.RESTART);
        System.out.println("test restsrt server");
        sendGiveup(message);
    }
    
}
