package algonquin.cst2335.huyn0116.user_interface;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ChatMessageDAO {

    //Allows the Room Library to take care of the SQL Syntax for inserting
    @Insert
    //This function will insert a ChatMessage object and then retrun the
    //the newly created ID as a long
    public long insertMessage(ChatMessage m);

    @Query("Select * from ChatMessage")
    List<ChatMessage> getAllMessages();

    @Delete
    void deleteMessage(ChatMessage m);
}
