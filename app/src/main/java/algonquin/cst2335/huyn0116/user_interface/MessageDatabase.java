package algonquin.cst2335.huyn0116.user_interface;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ChatMessage.class}, version =1)
public abstract class MessageDatabase extends RoomDatabase {

    //use to return the DAO for interacting with the database
    public abstract ChatMessageDAO cmDAO();
}
