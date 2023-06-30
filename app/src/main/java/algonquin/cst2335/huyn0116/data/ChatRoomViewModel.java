package algonquin.cst2335.huyn0116.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ChatRoomViewModel extends ViewModel {
    public MutableLiveData <ArrayList<String>> messages = new MutableLiveData<>();
}
