package ui.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class chatUI extends JFrame implements ActionListener, UserListener, IChatUI {

    //mulicastHandler networkHandler;
    IMultiCastNetwork networkHandler;
    //ArrayList<String> currentUsers;

//    // Skapa en DefaultListModel enklare att lägga till och ta bort i en textarea:
    DefaultListModel<String> usersModel = new DefaultListModel<>();
//
//// Lägg till element i modellen
//model.addElement("Alice");
//model.addElement("Bob");
//model.addElement("Charlie");
//Dialog box vid start för användarnamn input
    JDialog dialog;
    JButton okButton;
    JTextField textField;
//Chat Ui:t
    JList<String> userList = new JList<>(usersModel);

    JButton bClose = new JButton("Koppla ner!");
    JPanel p = new JPanel();
    JTextArea tl = new JTextArea();
    //JTextArea tr = new JTextArea();
    JPanel chatp = new JPanel();
    JTextField inpBox = new JTextField(100);

    private String userName = "";

    public chatUI(IMultiCastNetwork handler) {
        networkHandler = handler;
        //currentUsers = new ArrayList<>();
        setSize(450, 700);
        add(bClose, BorderLayout.NORTH);
        p.setLayout(new GridLayout(1,2));
        add(p);
        chatp.setLayout(new BorderLayout());
        add(chatp, BorderLayout.SOUTH);
        chatp.add(inpBox);
        inpBox.setEnabled(false); //hindrar att skriva i chatten innan användaren skrivit sitt namn.
        p.add(tl);
        p.add(userList);
        tl.append("Test Left\n");
        tl.append("Test Left2\n");
        usersModel.addElement("I chatten just nu:\n");
        tl.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        userList.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        //add ActioListeners
        bClose.addActionListener(this);
        inpBox.addActionListener(this);

        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }
    public void start(){

        showDialog();
//        userName = JOptionPane.showInputDialog("Namn?:");
//        JOptionPane.showMessageDialog(null, "chat öppnas för:" + userName);
//        setTitle("Chat " + userName);
//        networkHandler.setUserName(userName);
//
//        try {
//
//            networkHandler.joinGroup("NEWUSER:" + userName);
//
//        }catch(IOException | InterruptedException e){
//            e.printStackTrace();
//        }

    }

    public void showDialog(){
        dialog = new JDialog(this, "Skriv namn", false);
        dialog.setLayout(new FlowLayout());

        textField = new JTextField(20);
        okButton = new JButton("OK");

        dialog.add(textField);
        dialog.add(okButton);

        dialog.pack();
        okButton.addActionListener(e -> handleDialogOk());
        SwingUtilities.invokeLater(() -> dialog.setVisible(true));

    }
    public void handleDialogOk(){
        this.userName = textField.getText();

        setTitle("Chat " + this.userName);
        networkHandler.setUserName(this.userName);

        try {
            networkHandler.sendMessage("NEWUSER:" + userName);
            IO.println("hello sending newuser?");

        }catch(IOException | InterruptedException e){
            e.printStackTrace();
        }
        inpBox.setEnabled(true);
        dialog.dispose();
    }


    @Override
    public void actionPerformed(ActionEvent e){
        String message = null;

        if(e.getSource() == bClose){
            message = "EXIT:" + userName;
            //tl.append("closed");
        }
        if(e.getSource() == inpBox){

            message = "MSG:" +userName + ":" + inpBox.getText();
        }
        try {
            networkHandler.sendMessage(message);
            IO.println("sent message:" + message);
        }catch(IOException | InterruptedException ex){
            ex.printStackTrace();
        }
    }


    @Override
    public void onUserJoined(String username) {
            if(!usersModel.contains(username)) {
                usersModel.addElement(username);
                tl.append(username + ":" + "UPPKOPPLAD" + "\n");
            }/*currentUsers.add(username)*/

    }
    @Override
    public void UpdateOldUserForNewUser(String userName){
        if(!usersModel.contains(userName)) {
            usersModel.addElement(userName);
        }
    }

    @Override
    public void onUserExit(String username){

            removeUser(username);
            if(username.equals(this.userName)){
                dispose();
                System.exit(0);
            }
    }
    public void removeUser(String userName){
        usersModel.removeElement(userName);

    }
    public void onGotMessage(String message){

        tl.append(message + "\n");
    }
}
