package socket;

/**
 * Created by Rubens Santos Barbosa
 */
public class Client {
    public static void main(String args[]) {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClientChatGame().setVisible(true);
            }
        });        
    }
}
