package algonquin.cst2335.huyn0116.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

import algonquin.cst2335.huyn0116.user_interface.ChatMessage;

public class ChatRoomViewModel extends ViewModel {
    public MutableLiveData <ArrayList<ChatMessage>> messages = new MutableLiveData<>();

    /*
    * Variable to show details about the message on side if it's on a tablet
    * */
    public MutableLiveData<ChatMessage> selectedMessage = new MutableLiveData<>();
}
