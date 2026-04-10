package ui.UI;

public interface IChatUI {

    public void onUserJoined(String username);
    public void onUserExit(String username);
    public void removeUser(String userName);
    public void onGotMessage(String message);
    public void UpdateOldUserForNewUser(String username);

}
