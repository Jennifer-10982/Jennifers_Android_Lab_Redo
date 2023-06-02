package algonquin.cst2335.huyn0116;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        TextView text_View = findViewById(R.id.textView);
        EditText phoneNumber = findViewById(R.id.editTextPhone);
        Button callbtn = findViewById(R.id.button2);

        Intent fromPrevious = getIntent();
        String emailAddress = fromPrevious.getStringExtra("EmailAddress");

        text_View.setText("Welcome back: " + emailAddress);

        callbtn.setOnClickListener(clk ->{
            Intent call = new Intent(Intent.ACTION_DIAL);
            call.setData(Uri.parse("tel: " + phoneNumber));

            call.putExtra("Phone", phoneNumber.getText().toString());
            startActivity(call);
        });
    }
}