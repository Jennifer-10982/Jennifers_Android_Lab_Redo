package algonquin.cst2335.huyn0116.user_interface;

import static algonquin.cst2335.huyn0116.databinding.DetailsLayoutBinding.inflate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import algonquin.cst2335.huyn0116.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.huyn0116.databinding.DetailsLayoutBinding;

public class MessageDetailsFragment extends Fragment{
    ChatMessage selected;


    public MessageDetailsFragment (ChatMessage m){
        selected = m;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        DetailsLayoutBinding binding = inflate(inflater);

        binding.message.setText(selected.message);
        binding.timeSent.setText(selected.timeSent);
        binding.id.setText("Id = " + selected.id);

        return binding.getRoot();
    }
}
