package algonquin.cst2335.huyn0116.user_interface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Entity;
import androidx.room.Room;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

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

    int position;

//    ArrayList<ChatMessage> message = new ArrayList<>();

    private RecyclerView.Adapter myAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
            getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        TextView messageText;
//        switch (item.getItemId()){
//            case R.id.item_1:
                if(item.getItemId() == R.id.item_1){
                chatModel.selectedMessage.postValue(messages.get(position));
                messageText = findViewById(R.id.messageText);

                AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
                builder.setMessage("Do you want to delete the message: " + messageText.getText())
                .setTitle("Question: ")
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
                builder.create().show();}
                else if(item.getItemId() == R.id.item_2) {

                    Toast.makeText(ChatRoom.this, "Version 1.0, created by Jennifer Huynh", Toast.LENGTH_SHORT).show();
                }
            return true;
    }

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
        setSupportActionBar(binding.myToolbar);

        //*****Surviving Rotational Changes*********//
        //use to initialize the OnCreate()
        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        chatModel.selectedMessage.observe(this, newMessage->{

            if(newMessage != null) {
                //newMessage is what is posted to the value
                MessageDetailsFragment detailsFragment = new MessageDetailsFragment(newMessage);

                //show the fragment on screen
                FragmentManager fMgr = getSupportFragmentManager();
                FragmentTransaction tx = fMgr.beginTransaction();
                tx.addToBackStack("");
                tx.replace(R.id.fragmentLocation, detailsFragment);
                tx.commit(); // go and do it
            }
        });


        //use to retrieve the ArrayList<> that it is storing.
        messages = chatModel.messages.getValue();

        //verify if the chatModel.messages variable has never been set before.
        if (messages == null){
            chatModel.messages.postValue(messages = new ArrayList<ChatMessage>());
        }
        //*****End of Rotational Changes***********//

        //ERROR OCCURS HERE "INDEXOUTOFBOUNDSERROR
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
                long id = mDAO.insertMessage(chatMessage);
                //getting the last message of the list
                messages.get(messages.size()-1).setId(id);
                runOnUiThread(()->binding.recycleView.setAdapter(myAdapter));
            });

            //Tells the Adapter which row has been redrawn.
            //messages.size()-1 = we are adding back to the ArrayList
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
                long id = mDAO.insertMessage(chatMessage);
                //getting the last message of the list
                messages.get(messages.size()-1).setId(id);

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
            /* --> ERROR MESSAGE OCCURS HERE MIGHT BE FROM THE INDEXOUTOFBOUND*/
            @Override
            public int getItemViewType(int position) {
                ChatMessage chatMessage = messages.get(position);
                if (chatMessage.isSent == true){
                    return 1;
                } else
                    return 0;
            }

            @NonNull
            @Override
            //Responsible for creating a layout for a row, and setting the TextViews in code
            //Functions to load the correct View for the type viewType
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //int viewType is what layout to load


                //loading the row_layout
                if(viewType == 1){
                    SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder( binding.getRoot());
                } else{
                    ReceiveMessageBinding binding = ReceiveMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder( binding.getRoot());
                }
            }

            //Use to set the objects in the layout for the row.
            //This initializes the row to data
            //Currently, the MyRowHeader has 2 TextView objects. Therefore the function is meant
            //to set the data for your ViewHolder object that will go at row position in list
            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                ChatMessage chatMessage = messages.get(position);

                holder.messageText.setText("");
                holder.timeText.setText("");

                holder.messageText.setText(chatMessage.message);
                holder.timeText.setText(chatMessage.timeSent);
            }

            //Returns the number of rows in the list where the row will be the size of the list
            //Just want to show whatever is in the ArrayList.
            //Shows how many rows there is
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
                position = getAbsoluteAdapterPosition();
//               ChatMessage selectedMessage = messages.get(position);

               chatModel.selectedMessage.postValue(messages.get(position));

               /*
                //function that tells you which row(position) this row is currently in the adapter object
                //Aka to know which row we clicked on
                int position = getAbsoluteAdapterPosition();

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

            //this holds the message
            messageText = itemView.findViewById(R.id.messageText);
            timeText = itemView.findViewById(R.id.timeText);

        }
    }
}

