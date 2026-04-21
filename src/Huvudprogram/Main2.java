package Huvudprogram;

import networkUtils.multiCastHandlerv2;
import ui.UI.chatUI;

import javax.swing.*;
import java.io.IOException;

public class Main2 {

    public static void main() throws IOException, InterruptedException {


        multiCastHandlerv2 network = new multiCastHandlerv2();
        chatUI ui = new chatUI(network);
        network.joinGroup("tempname");
        //network.sendMessage("tempname");



            ui.start();

            Thread.startVirtualThread(() -> {


                while(true) {
                    String message = null;

                    try {
                        IO.println("starts");
                        message = network.startListening();
                        IO.println("har fått meddelande från listener ");
                    } catch (IOException e) {
                        IO.println("meddelandet mottogs inte korrekt i main.");
                    }

                    if (message != null) {

                        String messageResult = message;
                        IO.println(message + "från main");


                        // Uppdatera UI
                        SwingUtilities.invokeLater(() -> {

                            IO.println("starts swing utils samtidigt");
                            if (messageResult.startsWith("NEWUSER:")) {
                                String username = messageResult.substring(8);
                                IO.println("after messageResult");
                                ui.onUserJoined(username);

                            }
                            if(messageResult.startsWith("UPDATEFORNEWUSER:")){
                                String username = messageResult.substring("UPDATEFORNEWUSER:".length());
                                ui.UpdateOldUserForNewUser(username);

                            }
                            if (messageResult.startsWith("MSG:")) {
                                ui.onGotMessage(messageResult.substring(4));
                            }
                            if (messageResult.startsWith("EXIT:")) {
                                String username = messageResult.substring(5);
                                IO.println("message starts with EXIT and has" + username);
                                IO.println(username);
                                ui.onUserExit(username);

                            }
                        });

                        String userName;
                        if (messageResult.startsWith("EXIT:")) {
                             userName = messageResult.substring(5);

                            if (network.getUserName().equals(userName)) {
                                try {
                                    network.leave();
                                    network.closeSocket();
                                } catch(IOException e) {
                                    IO.println("error at exit");
                                }
                                break;
                            }
                        }

                        if (messageResult.startsWith("NEWUSER:")) {
                            String username = messageResult.substring(8);
                            IO.println("after messageResult");
                            ui.onUserJoined(username);

                                try{
                                    network.sendMessage("UPDATEFORNEWUSER:"+ network.getUserName());
                                    IO.println("update user" + network.getUserName());
                                }catch(IOException | InterruptedException e){
                                    IO.println("gick inte att skicak updatera till new user");
                                }
                        }
                    }
                }

                    });
        };
    }

