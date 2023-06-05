package algonquin.cst2335.huyn0116;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SecondActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        TextView text_View = findViewById(R.id.textView);
        EditText phoneNumber = findViewById(R.id.editTextPhone);
        Button callbtn = findViewById(R.id.button2);
        Button changeBtn = findViewById(R.id.button3);
        ImageView profileImage = findViewById(R.id.img);

        Intent fromPrevious = getIntent();
        String emailAddress = fromPrevious.getStringExtra("EmailAddress");
        text_View.setText("Welcome back: " + emailAddress);

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String phoneNum = prefs.getString("PhoneNumber", "");
        phoneNumber.setText(phoneNum);

        callbtn.setOnClickListener(clk ->{
            Intent call = new Intent(Intent.ACTION_DIAL);
            call.setData(Uri.parse("tel: " + phoneNumber.getText().toString()));
            call.putExtra("Phone", phoneNumber.getText().toString());
            startActivity(call);
        });

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode()== Activity.RESULT_OK){
                            Intent data = result.getData();
                            Bitmap thumbnail = data.getParcelableExtra("data");
                            profileImage.setImageBitmap(thumbnail);

                            FileOutputStream fOut = null;
                            try {
                                //open a file in Sandbox
                                //has filename Picture.png
                                fOut = openFileOutput("Picture.png", Context.MODE_PRIVATE);
                                thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                fOut.flush();
                                fOut.close();
                            }
                            catch (FileNotFoundException e){
                                e.printStackTrace();
                            }
                            catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                    }
                });
        //getFilesDir() -> returns a file object representing directory where app is running from called Sandbox
        File file = new File(getFilesDir(), "Picture.png");
        if (file.exists()){
            Bitmap theImage = BitmapFactory.decodeFile(file.toString());
            profileImage.setImageBitmap(theImage);
        }

        changeBtn.setOnClickListener(clk ->{
            cameraResult.launch(cameraIntent);
        });
    }

    @Override
    //app no longer responses to user input
    protected void onPause() {
        super.onPause();
        //use to save the object
        EditText phoneNumber = findViewById(R.id.editTextPhone);

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("PhoneNumber", phoneNumber.getText().toString());
        editor.apply();

    }
}