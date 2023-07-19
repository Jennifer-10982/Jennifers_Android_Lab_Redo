package algonquin.cst2335.huyn0116;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import algonquin.cst2335.huyn0116.databinding.ActivityMainBinding;


/**
 * This page prompts the user to input their password where it will valiadte the input
 * whether it meets the requirement of obtaining 1 uppercase, 1 lowercase, 1 special character and 1 number.
 * A message will appear if the user got their input wrong or if they got it right.
 *
 * @author Jennifer Huynh
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /*
    private TextView textView = null;

    private EditText editText = null;

    private Button btn = null;
    */


    protected String cityName;

//    create a Volley object that will connect to a server
    protected RequestQueue queue = null;

    private ActivityMainBinding binding;

    Bitmap image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.textView);
        EditText edittext = findViewById(R.id.textpwd);
        Button btn = findViewById(R.id.login);

        btn.setOnClickListener(clk -> {
            String password = edittext.getText().toString();
            checkPasswordComplexity(password);
        });
        */
        queue = Volley.newRequestQueue(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.forecastButton.setOnClickListener(click -> {
            cityName = binding.cityTextField.getText().toString();
            String stringURL="";

            try{
                stringURL = new StringBuilder()
                        .append("https://api.openweathermap.org/data/2.5/weather?q=")
                                .append(URLEncoder.encode(cityName, "UTF-8"))
                                        .append("&appid=7e943c97096a9784391a981c4d878b22&units=metric").toString();
            } catch(UnsupportedEncodingException e ){
                e.printStackTrace();
            }

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,stringURL,null,
                    (response)->{
                            try {

                                JSONObject coord = response.getJSONObject("coord");
                                JSONArray weatherArray = response.getJSONArray("weather");
                                JSONObject position0 = weatherArray.getJSONObject(0);

                                String description = position0.getString("description");
                                String iconName = position0.getString("icon");

                                JSONObject mainObject = response.getJSONObject("main");
                                double current = mainObject.getDouble("temp");
                                double min = mainObject.getDouble("temp_min");
                                double max = mainObject.getDouble("temp_max");
                                int humidity = mainObject.getInt("humidity");

                                runOnUiThread(() -> {
                                    binding.temp.setText("The current temperature is " + current);
                                    binding.temp.setVisibility(View.VISIBLE);

                                    binding.minTemp.setText("The current temperature is " + min);
                                    binding.minTemp.setVisibility(View.VISIBLE);

                                    binding.maxTemp.setText("The current temperature is " + max);
                                    binding.maxTemp.setVisibility(View.VISIBLE);

                                    binding.humidity.setText("The current temperature is " + humidity + "%");
                                    binding.humidity.setVisibility(View.VISIBLE);

                                    binding.icon.setImageBitmap(image);
                                    binding.icon.setVisibility(View.VISIBLE);

                                    binding.description.setText(description);
                                    binding.description.setVisibility(View.VISIBLE);
                                });


                                String pathname = getFilesDir() + "/" + iconName + ".png";
                                File file = new File(pathname);
                                if (file.exists()) {
                                    image = BitmapFactory.decodeFile(pathname);
                                } else {
                                    ImageRequest imgReq = new ImageRequest("https://openweathermap.org/img/w/" +
                                            iconName + ".png", new Response.Listener<Bitmap>() {
                                        @Override
                                        public void onResponse(Bitmap bitmap) {
                                            try {
                                                image = bitmap;
                                                image.compress(Bitmap.CompressFormat.PNG, 100,
                                                        MainActivity.this.openFileOutput(iconName + ".png", Activity.MODE_PRIVATE));
                                                binding.icon.setImageBitmap(image);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, 1024, 1024, ScaleType.CENTER, null, (error) -> {
                                        Toast.makeText(MainActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                                    });
                                    queue.add(imgReq);
                                }
                            } catch(JSONException e ){e.printStackTrace();};

                    },(error)->{});
                queue.add(request);
            });
        }
}



    /*
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
*/


