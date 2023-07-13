package algonquin.cst2335.huyn0116.user_interface;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//@Entity annotation use to save objects of this class into a database
// Basically means that this is something that can go into a database
@Entity
public class ChatMessage {

    @PrimaryKey(autoGenerate = true)//increment the ids for us
    @ColumnInfo(name = "id")
    public long id;

    //@ColumnInfor is used to specify that this variable will go into a database column named XXX
    @ColumnInfo(name ="message")
    protected String message;

    @ColumnInfo (name = "timeSent")
    protected String timeSent;

   @ColumnInfo (name = "isSent")
    protected boolean isSent;

   public ChatMessage(){

   }
    ChatMessage (String m, String t, boolean sent){
        message = m;
        timeSent = t;
        isSent = sent;
    }

    public void setId(long id){
       this.id = id;
    }
    public String getMessage(){
        return message;
    }

    public String getTimeSent(){
        return timeSent;
    }

    public long getId(){return id;}

    public boolean getisSent(){
        return isSent;
    }
}
