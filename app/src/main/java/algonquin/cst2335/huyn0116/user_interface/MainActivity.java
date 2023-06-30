package algonquin.cst2335.huyn0116.user_interface;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import algonquin.cst2335.huyn0116.R;

/**
 * This page prompts the user to input their password where it will valiadte the input
 * whether it meets the requirement of obtaining 1 uppercase, 1 lowercase, 1 special character and 1 number.
 * A message will appear if the user got their input wrong or if they got it right.
 *
 * @author Jennifer Huynh
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /**
     * This holds the text at the centre of the screen
     */
    private TextView textView = null;

    /**
     * This holds the user input for the password
     */
    private EditText editText = null;

    /**
     * This holds the button to validate whether the password meets the requirement
     */
    private Button btn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.textView);
        EditText edittext = findViewById(R.id.textpwd);
        Button btn = findViewById(R.id.login);

        btn.setOnClickListener(clk -> {
            String password = edittext.getText().toString();
            checkPasswordComplexity(password);
        });
    }

    ;

    /**
     * This function is used to check the complexity of the password where if the password
     * is missung an Uppercase, a lowercase, a number or a special symbol, an error message
     * will occur dependent on the missung information
     *
     * @param pw The String object that we are checking
     * @return boolean - true if all tha four requirement is met or false if one of the requirement are not met
     */
    boolean checkPasswordComplexity(String pw) {
        TextView textView = findViewById(R.id.textView);
        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;
        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;
        for (int i = 0; i < pw.length(); i++) {
            char c = pw.charAt(i);
            foundUpperCase = foundUpperCase || Character.isUpperCase(c);
            foundLowerCase = foundLowerCase || Character.isLowerCase(c);
            foundNumber = foundNumber || Character.isDigit(c);
            foundSpecial = isSpecial(c) || foundSpecial;
        }
        if (!foundUpperCase) {
            Toast.makeText(MainActivity.this, "Missing Uppercase letter", Toast.LENGTH_SHORT).show();
            textView.setText("You shall not Pass!");
            return false;
        } else if (!foundLowerCase) {
            Toast.makeText(MainActivity.this, "Missing Lowercase letter", Toast.LENGTH_SHORT).show();
            textView.setText("You shall not Pass!");
            return false;
        } else if (!foundNumber) {
            Toast.makeText(MainActivity.this, "Missing Number", Toast.LENGTH_SHORT).show();
            textView.setText("You shall not Pass!");
            return false;
        } else if (!foundSpecial) {
            Toast.makeText(MainActivity.this, "Missing Special Character", Toast.LENGTH_SHORT).show();
            ;
            textView.setText("You shall not Pass!");
            return false;
        } else
            textView.setText("Your Password Is Complex Enough");
        return true;
    }

    /**
     * Each of the case is to return true if the character matches any of the cases. If no match
     * is found, the function will return false
     *
     * @param c is the character from the String password
     * @return boolean dependent if the character matches the special character (TRUE). No match returns false
     */
    boolean isSpecial(char c) {
        switch (c) {
            case '#':
            case '$':
            case '%':
            case '^':
            case '&':
            case '*':
            case '!':
            case '@':
            case '?':
                return true;
            default:
                return false;
        }
    }
}