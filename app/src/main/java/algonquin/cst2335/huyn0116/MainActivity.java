package algonquin.cst2335.huyn0116;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.w("MainActivity","In onCreate() - Loading Widget");
        Log.d(TAG,"Message");

        SharedPreferences prefs = getSharedPreferences("MyData",Context.MODE_PRIVATE);
        String emailAddress = prefs.getString("LoginName", "");

        Button loginButton = findViewById(R.id.button);
        EditText emailEditText = findViewById(R.id.editText);

        emailEditText.setText(emailAddress);

        loginButton.setOnClickListener(clk->{
            Intent nextPage = new Intent(MainActivity.this, SecondActivity.class);
            nextPage.putExtra("EmailAddress", emailEditText.getText().toString());

            SharedPreferences.Editor editor = prefs.edit();
            //creating a string
            editor.putString("LoginName", emailEditText.getText().toString());
            editor.apply();

            startActivity(nextPage);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.w("MainActivity", "In onStart() - The application is now visible on screen");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w("MainActivity", "In onResume() - The application is now responding to user input");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w("MainActivity", "In onPause() - The app is no longer responds to user input");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w("MainActivity", "In onStop() - The application is no longer visible");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w("MainActvity", "In onDestroy() - Any memory used by the application is freed.");
    }
}