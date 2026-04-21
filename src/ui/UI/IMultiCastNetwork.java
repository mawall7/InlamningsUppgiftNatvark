package ui.UI;

import java.io.IOException;

public interface IMultiCastNetwork {
    public void sendMessage(String message) throws IOException, InterruptedException;
    public String startListening() throws IOException;
    public void setUserName(String name);
    public void joinGroup(String joinMessage) throws IOException, InterruptedException;

}
