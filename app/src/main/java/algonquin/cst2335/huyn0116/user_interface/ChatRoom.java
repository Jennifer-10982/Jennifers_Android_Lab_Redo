package algonquin.cst2335.huyn0116.user_interface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Entity;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.huyn0116.R;
import algonquin.cst2335.huyn0116.data.ChatRoomViewModel;
import algonquin.cst2335.huyn0116.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.huyn0116.databinding.ReceiveMessageBinding;
import algonquin.cst2335.huyn0116.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity {
    public ActivityChatRoomBinding binding;
    ArrayList<ChatMessage> messages;
    ChatRoomViewModel chatModel;

    ChatMessageDAO mDAO;

//    ArrayList<ChatMessage> message = new ArrayList<>();

    private RecyclerView.Adapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //****Use to Open Database*******//
        MessageDatabase db = Room.databaseBuilder(getApplicationContext(),
                             MessageDatabase.class,"database-name").build();
        mDAO = db.cmDAO();
        //****End of Opening Database*******//

        messages = new ArrayList<ChatMessage>();

        //inflating the layout using data binding.
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //*****Surviving Rotational Changes*********//
        //use to initialize the OnCreate()
        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);

        //use to retrieve the ArrayList<> that it is storing.
        messages = chatModel.messages.getValue();

        //verify if the chatModel.messages variable has never been set before.
        if (messages == null){
            chatModel.messages.postValue(messages = new ArrayList<ChatMessage>());
        }
        //*****End of Rotational Changes***********//

        /*-------Registering as a listener to the MutableLiveData Object-----*/
        chatModel.selectedMessage.observe(this,(newMessageValue)->{
            MessageDetailsFragment chatFragment = new MessageDetailsFragment(newMessageValue);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLocation, chatFragment).commit();
        });
        /*--------------------End of Code-------------------------------------*/

        binding.sendButton.setOnClickListener(click -> {
            //Adding an item to the list
//            messages.add(binding.textInput.getText().toString());

            //takes String message that was typed
            String msg = binding.textInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            //Formatting the string into date and time
            String currentDateandTime = sdf.format(new Date());
            boolean sent = true;
            //Creating a ChatMessage object
            ChatMessage chatMessage = new ChatMessage(msg, currentDateandTime, sent);
            //adding the chatMessage to the list
            messages.add(chatMessage);

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(()->{
                mDAO.insertMessage(chatMessage);
                runOnUiThread(()->binding.recycleView.setAdapter(myAdapter));
            });

            //Tells the Adapter which row has been redrawn.
            //messages.size()-1 = we are adding back to the ArrayLost
            myAdapter.notifyItemInserted(messages.size()-1);

            //Is called Whenever entire ArrayList has changed (like loading from a database)
            myAdapter.notifyDataSetChanged();

            //use to clear the previous text
            binding.textInput.setText("");
        });

        binding.receiveButton.setOnClickListener(click -> {
            //Adding an item to the list
//            messages.add(binding.textInput.getText().toString());

            //takes String message that was typed
            String msg = binding.textInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            //Formatting the string into date and time
            String currentDateandTime = sdf.format(new Date());
            boolean sent = false;
            //Creating a ChatMessage object
            ChatMessage chatMessage = new ChatMessage(msg, currentDateandTime, sent);
            //adding the chatMessage to the list
            messages.add(chatMessage);

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(()->{
                mDAO.insertMessage(chatMessage);
                runOnUiThread(()->binding.recycleView.setAdapter(myAdapter));
            });
            //Tells the Adapter which row has been redrawn.
            //messages.size()-1 = we are adding back to the ArrayLost
            myAdapter.notifyItemInserted(messages.size()-1);

            //Is called Whenever entire ArrayList has changed (like loading from a database)
            myAdapter.notifyDataSetChanged();

            //use to clear the previous text
            binding.textInput.setText("");

        });

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        //use to initialize the variable myAdapter

        if (messages == null){
            chatModel.messages.setValue(messages = new ArrayList<>());
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->{
                messages.addAll(mDAO.getAllMessages());
                runOnUiThread(()-> binding.recycleView.setAdapter(myAdapter));
            });
        }

        binding.recycleView.setAdapter( myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            //Returns an int which is the parameter that gets passed in to the onCreateViewHolder()
            @Override
            public int getItemViewType(int position) {
                ChatMessage chatMessage=messages.get(position);
                if (chatMessage.isSent == true){
                    return 0;
                } else
                    return 1;
//                return 0;
            }

            @NonNull
            @Override
            //Responsible for creating a layout for a row, and setting the TextViews in code
            //Functions to load the correct View for the type viewType
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if(viewType == 0){
                    SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder( binding.getRoot());
                } else{
                    ReceiveMessageBinding binding = ReceiveMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder( binding.getRoot());
                }
            }

            //Use to set the objects in the layout for the row.
            //Currently, the MyRowHeader has 2 TextView objects. Therefore the function is meant
            //to set the data for your ViewHolder object that will go at row position in list
            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                holder.messageText.setText("");
                holder.timeText.setText("");

                ChatMessage chatMessage = messages.get(position);
                holder.messageText.setText(chatMessage.message);
                holder.timeText.setText(chatMessage.timeSent);
//                String obj = messages.get(position);
//                holder.messageText.setText(obj);
            }

            //Returns the number of rows in the list where the row will be the size of the list
            //Just want to show whatever is in the ArrayList.
            @Override
            public int getItemCount() {
                return messages.size();
            }
        });

    }
    //An object for representing everything that goes on a row in the list.
    // Class used to maintain variables for what is needed to be set on each row in the list
    class MyRowHolder extends RecyclerView.ViewHolder{
        TextView messageText;
        TextView timeText;
        public MyRowHolder (@NonNull View itemView){
            super (itemView);

            itemView.setOnClickListener(clk->{
                //function that tells you which row(position) this row is currently in the adapter object
                //Aka to know which row we clicked on
                int position = getAbsoluteAdapterPosition();

                ChatMessage selected = messages.get(position);
                chatModel.selectedMessage.postValue(selected);



            /*
                //creating an alert window
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);

                //use to set the message on the alert window
                builder.setMessage("Do you want to delete the message: " + messageText.getText())
                //to set the title of the alert dialog
                .setTitle("Question: ")

                //Text will appear on the button
                //lambda function is the click handler for what happens if you click on the button
                //No btn should not do anything therefore {} is emppty.
                .setNegativeButton("No",(dialog, cl)->{})
                .setPositiveButton("Yes",(dialog,cl)->{
                    ChatMessage removedMessage = messages.get(position);
                    messages.remove(position);
                    myAdapter.notifyItemRemoved(position);

                    Executor thread = Executors.newSingleThreadExecutor();
                    thread.execute(()->{
                        mDAO.deleteMessage(removedMessage);
                        runOnUiThread(()-> binding.recycleView.setAdapter(myAdapter));
                    });
                    Snackbar.make(messageText,"You deleted mess #" + position, Snackbar.LENGTH_LONG)
                            .setAction("Undo", click ->{
                                messages.add(position, removedMessage);
                                myAdapter.notifyItemInserted(position);
                            })
                            .show();
                });

                //makes the alert window appear
                builder.create().show();
                */
            });

            messageText = itemView.findViewById(R.id.messageText);
            timeText = itemView.findViewById(R.id.timeText);
        }
    }
}

