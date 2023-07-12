package algonquin.cst2335.huyn0116.user_interface;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import algonquin.cst2335.huyn0116.databinding.MessageDetailsLayoutBinding;

public class MessageDetailsFragment extends Fragment {

    ChatMessage thisMessage;
    public MessageDetailsFragment(ChatMessage toShow) {
        thisMessage = toShow;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle instance){
        MessageDetailsLayoutBinding binding = MessageDetailsLayoutBinding.inflate(inflater);

        binding.messageText.setText(thisMessage.message);
        binding.timeText.setText(thisMessage.timeSent);
        binding.idText.setText(Long.toString(thisMessage.id));

        if(thisMessage.isSent = true){
            binding.isSentText.setText("Send");
        } else
            binding.isSentText.setText("Receive");
        return binding.getRoot();
    }
}
