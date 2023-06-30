package algonquin.cst2335.huyn0116.user_interface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import algonquin.cst2335.huyn0116.R;
import algonquin.cst2335.huyn0116.data.ChatRoomViewModel;
import algonquin.cst2335.huyn0116.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.huyn0116.databinding.ReceiveMessageBinding;
import algonquin.cst2335.huyn0116.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity {
    public ActivityChatRoomBinding binding;
    ArrayList<String> messages;
    ChatRoomViewModel chatModel;

    ArrayList<ChatMessage> message = new ArrayList<>();

    private RecyclerView.Adapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue();

        if (messages == null){
            chatModel.messages.postValue(messages = new ArrayList<>());
        }

        binding.sendButton.setOnClickListener(click -> {
            messages.add(binding.textInput.getText().toString());
            myAdapter.notifyItemInserted(messages.size()-1);

            String m = binding.textInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            boolean sent = true;

            ChatMessage chatMessage = new ChatMessage(m, currentDateandTime, sent);
            message.add(chatMessage);

            myAdapter.notifyDataSetChanged();
            binding.textInput.setText("");
        });

        binding.receive.setOnClickListener(click ->{
            messages.add(binding.textInput.getText().toString());
            String m = binding.textInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            boolean sent = false;

            ChatMessage chatMessage = new ChatMessage(m, currentDateandTime, sent);
            message.add(chatMessage);
            myAdapter.notifyItemInserted(messages.size()-1);
            myAdapter.notifyDataSetChanged();

            binding.textInput.setText("");
        });

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView;
                if (viewType == 1){
                    ReceiveMessageBinding binding = ReceiveMessageBinding.inflate(getLayoutInflater());
                    itemView=binding.getRoot();
                    return new MyRowHolder(itemView);
//                    return new MyRowHolder(binding.getRoot());
                }else{
                    SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater());
                    itemView = binding.getRoot();
                    return new MyRowHolder(itemView);
//                    return new MyRowHolder(binding.getRoot());}
            }
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                ChatMessage chatMessage = message.get(position);
                holder.messageText.setText(chatMessage.message);
                holder.timeText.setText(chatMessage.timeSent);

//                holder.messageText.setText("");
//                holder.timeText.setText("");
//                String obj = messages.get(position);
//                holder.messageText.setText(obj);
//                holder.timeText.setText(obj);
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            @Override
            public int getItemViewType (int position){
                ChatMessage chatMessage = message.get(position);
                if (chatMessage.isSentButton == true) {
                    return 0;
                }else
                    return 1;
            }
        });

    }
    class MyRowHolder extends RecyclerView.ViewHolder{
        TextView messageText;
        TextView timeText;
        public MyRowHolder (@NonNull View itemView){
            super (itemView);

            messageText = itemView.findViewById(R.id.messageText);
            timeText = itemView.findViewById(R.id.timeText);
        }
    }
}

