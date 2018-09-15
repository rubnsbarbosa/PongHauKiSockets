package socket;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import socket.ProtocolMessage.Action;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.text.DefaultCaret;
import ponghauki.Buttons;
import ponghauki.MoveButtonsCircle;

/**
 * Created by Rubens Santos Barbosa
 */
public class ClientChatGame extends javax.swing.JFrame {

    private Socket socket = null;
    private ProtocolMessage msg;
    private ClientService service;

    MoveButtonsCircle circle;
    Buttons pOneOrange = null;
    Buttons pTwoOrange = null;
    Buttons pOneYellow = null;
    Buttons pTwoYellow = null;

    public ClientChatGame() {
        initComponents();
        this.circle = new MoveButtonsCircle();

        setBallsPositions();

        mOrangeOne();
        mOrangeTwo();
        mYellowOne();
        mYellowTwo();
        
        // update messages in textArea
        DefaultCaret caret = (DefaultCaret)textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    }

    private void setBallsPositions() {
        pOneOrange = new Buttons(p1Orange, 0);
        pTwoOrange = new Buttons(p2Orange, 1);
        pOneYellow = new Buttons(p1Yellow, 2);
        pTwoYellow = new Buttons(p2Yellow, 3);

        circle.setPlayerOrangeOne(pOneOrange);
        circle.setPlayerOrangeTwo(pTwoOrange);
        circle.setPlayerYellowOne(pOneYellow);
        circle.setPlayerYellowTwo(pTwoYellow);
    }

    private void mOrangeOne() {
        p1Orange.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (msg.isMyTurn() == false) {
                    JOptionPane.showMessageDialog(null, "Aguarde sua vez de jogar");
                } else {
                    circle.movePlayerOrangeOne();
                    sendMove2server("orangeOne");
                    msg.setMyTurn(false);
                }
            }
        });
    }

    private void mOrangeTwo() {
        p2Orange.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (msg.isMyTurn() == false) {
                    JOptionPane.showMessageDialog(null, "Aguarde sua vez de jogar");
                } else {
                    circle.movePlayerOrangeTwo();
                    sendMove2server("orangeTwo");
                    msg.setMyTurn(false);
                }
            }
        });
    }

    private void mYellowOne() {
        p1Yellow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (msg.isMyTurn() == true) {
                    circle.movePlayerYellowOne();
                    sendMove2server("yellowOne");
                    msg.setMyTurn(false);
                } else {
                    JOptionPane.showMessageDialog(null, "Aguarde sua vez de jogar");
                }
            }
        });
    }

    private void mYellowTwo() {
        p2Yellow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (msg.isMyTurn() == true) {
                    circle.movePlayerYellowTwo();
                    sendMove2server("yellowTwo");
                    msg.setMyTurn(false);
                } else {
                    JOptionPane.showMessageDialog(null, "Aguarde sua vez de jogar");
                }
            }
        });
    }

    private void sendMove2server(String nameBall) {
        this.msg.setMessage(nameBall);
        this.msg.setAction(Action.MOVE);
        this.service.send(msg);
    }

    // begin class thread of client
    private class ListennerSocketClient implements Runnable {

        private ObjectInputStream inputStream;

        ListennerSocketClient(Socket socket) {
            try {
                this.inputStream = new ObjectInputStream(socket.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(ClientChatGame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void run() {
            ProtocolMessage msg = null;
            try {
                while ((msg = (ProtocolMessage) inputStream.readObject()) != null) {
                    Action action = msg.getAction();

                    if (action.equals(Action.CONNECT)) {
                        connected(msg);
                    } else if (action.equals(Action.DISCONNECT)) {
                        disconnected();
                        socket.close();
                    } else if (action.equals(Action.SEND_ONE)) {
                        receive(msg);
                    } else if (action.equals(Action.MOVE)) {
                        receiveMoveBalls(msg);
                    } else if (action.equals(Action.RESTART)) {
                        receiveRestart(msg);
                    }

                }
            } catch (IOException ex) {
                Logger.getLogger(ClientChatGame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClientChatGame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void connected(ProtocolMessage msg) {
        if (msg.getMessage().equals("NO")) {
            this.txtName.setText("");
            JOptionPane.showMessageDialog(this, "Conexão não realizada!\n Tente um outro nome.");
            return;
        }
        // send the value to client
        this.msg = msg;
        this.btnConnect.setEnabled(false);
        this.txtName.setEditable(false);
        this.textArea.setEnabled(true);
        this.txt2send.setEnabled(true);
        this.btnSend.setEnabled(true);
        JOptionPane.showMessageDialog(this, msg.getNameClient() + ", bem vindo(a) ao jogo Pong-Hau-Ki! " + msg.getMessage());
    }

    private void disconnected() {
        System.exit(0);
    }

    // receive message from server
    private void receive(ProtocolMessage msg) {
        this.textArea.append(msg.getNameClient() + ": " + msg.getMessage() + "\n");
    }

    private void receiveMoveBalls(ProtocolMessage msg) {
        if ("orangeOne".equals(msg.getMessage())) {
            circle.movePlayerOrangeOne();
            this.msg.setMyTurn(true);
        } else if ("orangeTwo".equals(msg.getMessage())) {
            circle.movePlayerOrangeTwo();
            this.msg.setMyTurn(true);
        } else if ("yellowOne".equals(msg.getMessage())) {
            circle.movePlayerYellowOne();
            this.msg.setMyTurn(true);
        } else if ("yellowTwo".equals(msg.getMessage())) {
            circle.movePlayerYellowTwo();
            this.msg.setMyTurn(true);
        }
    }

    private void receiveRestart(ProtocolMessage msg) {
        System.out.println("test restart client");
        this.textArea.append(msg.getNameClient() + ": " + msg.getMessage() + "\n");
        setPositions();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        pong_hau_ki = new javax.swing.JLabel();
        rocket = new javax.swing.JLabel();
        p1Orange = new javax.swing.JButton();
        p2Orange = new javax.swing.JButton();
        p1Yellow = new javax.swing.JButton();
        p2Yellow = new javax.swing.JButton();
        no = new javax.swing.JLabel();
        no1 = new javax.swing.JLabel();
        no2 = new javax.swing.JLabel();
        no3 = new javax.swing.JLabel();
        no4 = new javax.swing.JLabel();
        ux = new javax.swing.JLabel();
        restart = new javax.swing.JButton();
        surrender = new javax.swing.JButton();
        lb_restart = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();
        txt2send = new javax.swing.JTextField();
        img_chat = new javax.swing.JLabel();
        btnSend = new javax.swing.JButton();
        txtName = new javax.swing.JTextField();
        btnConnect = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pong Hau Ki");
        setBounds(new java.awt.Rectangle(0, 0, 0, 0));
        setMinimumSize(new java.awt.Dimension(970, 520));
        setSize(new java.awt.Dimension(0, 0));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(44, 62, 80));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 500));

        jPanel2.setBackground(new java.awt.Color(15, 191, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pong_hau_ki.setFont(new java.awt.Font("Lucida Grande", 1, 26)); // NOI18N
        pong_hau_ki.setForeground(new java.awt.Color(255, 255, 255));
        pong_hau_ki.setText("Pong-Hau-Ki");
        jPanel2.add(pong_hau_ki, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 20, 190, 40));

        rocket.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/rocket.png"))); // NOI18N
        jPanel2.add(rocket, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 10, -1, 70));

        p1Orange.setBackground(new java.awt.Color(15, 191, 255));
        p1Orange.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/circleOrange-2.png"))); // NOI18N
        p1Orange.setBorderPainted(false);
        p1Orange.setContentAreaFilled(false);
        jPanel2.add(p1Orange, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 100, 84, 84));

        p2Orange.setBackground(new java.awt.Color(15, 191, 255));
        p2Orange.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/circleOrange-2.png"))); // NOI18N
        p2Orange.setBorderPainted(false);
        p2Orange.setContentAreaFilled(false);
        jPanel2.add(p2Orange, new org.netbeans.lib.awtextra.AbsoluteConstraints(456, 100, 84, 84));

        p1Yellow.setBackground(new java.awt.Color(15, 191, 255));
        p1Yellow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/circleYellow-2.png"))); // NOI18N
        p1Yellow.setBorderPainted(false);
        p1Yellow.setContentAreaFilled(false);
        jPanel2.add(p1Yellow, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 320, 84, 84));

        p2Yellow.setBackground(new java.awt.Color(15, 191, 255));
        p2Yellow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/circleYellow-2.png"))); // NOI18N
        p2Yellow.setBorderPainted(false);
        p2Yellow.setContentAreaFilled(false);
        jPanel2.add(p2Yellow, new org.netbeans.lib.awtextra.AbsoluteConstraints(456, 320, 84, 84));

        no.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/circleZ.png"))); // NOI18N
        jPanel2.add(no, new org.netbeans.lib.awtextra.AbsoluteConstraints(266, 207, 75, 75));

        no1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/circleZ.png"))); // NOI18N
        jPanel2.add(no1, new org.netbeans.lib.awtextra.AbsoluteConstraints(74, 106, 75, 75));

        no2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/circleZ.png"))); // NOI18N
        jPanel2.add(no2, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 106, 75, 75));

        no3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/circleZ.png"))); // NOI18N
        jPanel2.add(no3, new org.netbeans.lib.awtextra.AbsoluteConstraints(74, 326, 75, 75));

        no4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/circleZ.png"))); // NOI18N
        jPanel2.add(no4, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 326, 75, 75));

        ux.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/UX.png"))); // NOI18N
        jPanel2.add(ux, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 130, 410, 250));

        restart.setBackground(new java.awt.Color(15, 191, 255));
        restart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/restart.png"))); // NOI18N
        restart.setBorderPainted(false);
        restart.setContentAreaFilled(false);
        restart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restartActionPerformed(evt);
            }
        });
        jPanel2.add(restart, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 6, 70, 70));

        surrender.setBackground(new java.awt.Color(15, 191, 255));
        surrender.setFont(new java.awt.Font("Lucida Grande", 0, 11)); // NOI18N
        surrender.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/surrender.png"))); // NOI18N
        surrender.setBorderPainted(false);
        surrender.setContentAreaFilled(false);
        surrender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                surrenderActionPerformed(evt);
            }
        });
        jPanel2.add(surrender, new org.netbeans.lib.awtextra.AbsoluteConstraints(532, 6, 70, 70));

        lb_restart.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lb_restart.setForeground(new java.awt.Color(255, 255, 255));
        lb_restart.setText("Reiniciar");
        jPanel2.add(lb_restart, new org.netbeans.lib.awtextra.AbsoluteConstraints(13, 76, -1, -1));

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Desistir");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(538, 76, -1, -1));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(59, 0, 610, 500));

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(693, 0, 280, -1));

        textArea.setEditable(false);
        textArea.setColumns(20);
        textArea.setLineWrap(true);
        textArea.setRows(5);
        textArea.setWrapStyleWord(true);
        textArea.setEnabled(false);
        jScrollPane1.setViewportView(textArea);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 40, 300, 420));

        txt2send.setEnabled(false);
        getContentPane().add(txt2send, new org.netbeans.lib.awtextra.AbsoluteConstraints(669, 459, 234, 40));

        img_chat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/chat.png"))); // NOI18N
        getContentPane().add(img_chat, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 0, -1, 40));

        btnSend.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        btnSend.setText("Enviar");
        btnSend.setEnabled(false);
        btnSend.setOpaque(true);
        btnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendActionPerformed(evt);
            }
        });
        getContentPane().add(btnSend, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 459, 70, 40));
        getContentPane().add(txtName, new org.netbeans.lib.awtextra.AbsoluteConstraints(702, 0, 170, 40));

        btnConnect.setText("Conectar");
        btnConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnectActionPerformed(evt);
            }
        });
        getContentPane().add(btnConnect, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 0, 100, 40));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        String text = this.txt2send.getText();
        String name = this.msg.getNameClient();

        if (!text.isEmpty()) {
            // clear obj message
            this.msg = new ProtocolMessage();
            this.msg.setNameClient(name);
            this.msg.setMessage(text);
            this.msg.setAction(Action.SEND_ALL);

            this.textArea.append("I said: " + text + "\n");

            this.service.send(this.msg);
        }
        this.txt2send.setText(""); // cleaning textField to user send a new message
        this.txt2send.requestFocus(); // mouse cursor in textField
    }//GEN-LAST:event_btnSendActionPerformed

    private void btnConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConnectActionPerformed
        String name = this.txtName.getText(); // get name from textField: txtName
        
        if (!name.isEmpty()) {
            this.msg = new ProtocolMessage();
            this.msg.setAction(Action.CONNECT);
            this.msg.setNameClient(name);

            this.service = new ClientService();
            this.socket = this.service.connect();

            new Thread(new ListennerSocketClient(this.socket)).start();

            this.service.send(this.msg);
        }
    }//GEN-LAST:event_btnConnectActionPerformed

    
    private void restartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restartActionPerformed

        setPositions();       
        
        this.msg.setAction(Action.RESTART);
        this.service.send(msg);

        JOptionPane.showMessageDialog(this, "Você perdeu a partida!");

    }//GEN-LAST:event_restartActionPerformed

    private void setPositions() {
        pOneOrange.startingPositionOrangeOne(p1Orange);
        pTwoOrange.startingPositionOrangeTwo(p2Orange);
        pOneYellow.startingPositionYellowOne(p1Yellow);
        pTwoYellow.startingPositionYellowTwo(p2Yellow);
    }
    
    private void surrenderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_surrenderActionPerformed
        this.msg.setAction(Action.DISCONNECT);
        this.service.send(this.msg); // msg sended to server
        disconnected();
    }//GEN-LAST:event_surrenderActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConnect;
    private javax.swing.JButton btnSend;
    private javax.swing.JLabel img_chat;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lb_restart;
    private javax.swing.JLabel no;
    private javax.swing.JLabel no1;
    private javax.swing.JLabel no2;
    private javax.swing.JLabel no3;
    private javax.swing.JLabel no4;
    public javax.swing.JButton p1Orange;
    public javax.swing.JButton p1Yellow;
    public javax.swing.JButton p2Orange;
    public javax.swing.JButton p2Yellow;
    private javax.swing.JLabel pong_hau_ki;
    private javax.swing.JButton restart;
    private javax.swing.JLabel rocket;
    private javax.swing.JButton surrender;
    private javax.swing.JTextArea textArea;
    private javax.swing.JTextField txt2send;
    private javax.swing.JTextField txtName;
    private javax.swing.JLabel ux;
    // End of variables declaration//GEN-END:variables
}
