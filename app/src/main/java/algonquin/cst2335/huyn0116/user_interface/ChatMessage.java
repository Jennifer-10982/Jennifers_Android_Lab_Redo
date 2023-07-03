package algonquin.cst2335.huyn0116.user_interface;

public class ChatMessage {
    String message;
    String timeSent;
    boolean isSent;

    ChatMessage (String m, String t, boolean sent){
        message = m;
        timeSent = t;
        isSent = sent;
    }

    public String getMessage(){
        return message;
    }

    public String getTimeSent(){
        return timeSent;
    }

    public boolean getisSent(){
        return isSent;
    }
}
