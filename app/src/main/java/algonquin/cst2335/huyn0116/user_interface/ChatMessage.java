package algonquin.cst2335.huyn0116.user_interface;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//@Entity annotation use to save objects of this class into a database
// Basically means that this is something that can go into a database
@Entity
public class ChatMessage {

    //@ColumnInfor is used to specify that this variable will go into a database column named XXX
    @ColumnInfo(name ="message")
    protected String message;

    @ColumnInfo (name = "timeSent")
    protected String timeSent;

   @ColumnInfo (name = "isSent")
    protected boolean isSent;

   @PrimaryKey (autoGenerate = true)
   @ColumnInfo (name ="id")
   public int id;

   public ChatMessage(){

   }
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
