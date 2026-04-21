package networkUtils;

import ui.UI.IMultiCastNetwork;

import java.io.IOException;
import java.net.*;

public class multiCastHandlerv2 implements IMultiCastNetwork {




        private MulticastSocket socket;
        //private UserListener listener;
        private String userName;
        String ip = "234.235.236.237";
        int toPort = 12540;
        InetAddress toAdr;
        InetSocketAddress toSAdr;
        private NetworkInterface netIf;

        public multiCastHandlerv2(){


            try{
                //wifi eller ethernet adress 192.168.1.68
                NetworkInterface netIf = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());

                socket = new MulticastSocket(null);
                socket.setReuseAddress(true);
                socket.bind(new InetSocketAddress(toPort));
                socket.setNetworkInterface(netIf);
                toAdr = InetAddress.getByName(ip);

                socket.setOption(StandardSocketOptions.IP_MULTICAST_LOOP, true); //gör att man kan ta emot sina egna paket
                toSAdr = new InetSocketAddress(toAdr, toPort);

            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        public void setUserName(String name){
            userName  = name;
        }
        public String getUserName(){
            return userName;
        }

        public void joinGroup(String joinMessage) throws IOException, InterruptedException{
            socket.joinGroup(toSAdr, netIf);
            //sendMessage(joinMessage); //to do userName != null
            IO.println("joingroupmessag:" + joinMessage);
        }


        public void sendMessage(String message) throws IOException, InterruptedException{
            if(message.equals("quit")){
                System.exit(0);
            }
            System.out.println("?:");

            byte[] data = message.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, toAdr, toPort);
            socket.send(packet);
            System.out.println("Skickar:" + message);

            System.out.println("?: ");
        }
        public void closeSocket() {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }

        public void leave() throws IOException{
            socket.leaveGroup(toSAdr, netIf);

        }

        public String startListening() throws IOException {

            String message = null;
            try {
                    byte[] buf = new byte[256];
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    message = new String(packet.getData(), 0, packet.getLength());


            } catch (IOException e) {
                e.getMessage();
                IO.println("Användaren lämnade chatten");
            }catch(NullPointerException e){
                IO.println("socket.receive data var null");
            }

        return message;
        }
    }



