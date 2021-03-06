package edu.illinois.cs.cs125.sunrise;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ImageView;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

public class MainActivity extends AppCompatActivity {

    public String sunRise;
    public String sunSet;
    public String dayLength;
    public String temp;

    TextView RISE;
    TextView SET;
    TextView LENGTH;
    TextView weatherTemp1;
    TextView coorD;

    public String latitudeInput;
    public String longitudeInput;
    public String ZIP;

    /** Request queue for our API requests. */
    private static RequestQueue requestQueue;

    /** Default logging tag for messages from the main activity. */
    private static final String TAG = "MP7:Sunrise";

    public MainActivity() throws JSONException {
     }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /** Request queue for our API requests. */
        requestQueue = Volley.newRequestQueue(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Please enter your Zip Code to find your Coordinates", Toast.LENGTH_LONG).show();
                //Snackbar.make(view, "Please enter your Zip Code to find your Coordinates", Snackbar.LENGTH_LONG)
                        //.setAction("Action", null).show();
                    try {
                        TextView longitude = (TextView) findViewById(R.id.longitude);
                        longitudeInput = longitude.getText().toString();
                        TextView latitude = (TextView) findViewById(R.id.latitude);
                        latitudeInput = latitude.getText().toString();

                        TextView zipCode = (TextView) findViewById(R.id.ZIPCODE);
                        ZIP = zipCode.getText().toString();
                    } catch(Exception e) {
                        longitudeInput = "-88.2272";
                        latitudeInput = "40.1020";
                    }

                try {
                    startAPICall1();

                    String sunriseTxt = getSunRise();
                    String sunsetTxt = getSunset();
                    String daylengthTxt = getDayLength();
                    SET = (TextView) findViewById(R.id.Sunset);
                    RISE = (TextView) findViewById(R.id.Sunrise);
                    LENGTH = (TextView) findViewById(R.id.Daylength);
                    SET.setText(sunsetTxt);
                    RISE.setText(sunriseTxt);
                    LENGTH.setText(daylengthTxt);

                } catch(Exception e) {
                    Snackbar.make(view, "Please re-enter the Zipcode & Coordinates", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                try {
                    startAPICall2();
                    String tempTxt = getTemp();
                    Log.d(TAG,"temperature worked");
                    weatherTemp1 = (TextView) findViewById(R.id.wTemp);
                    weatherTemp1.setText(tempTxt);

                    String ZipcodeCoord = getCOORD();
                    coorD = (TextView) findViewById(R.id.COORD);
                    coorD.setText(ZipcodeCoord);

                } catch(Exception e) {
                    Snackbar.make(view, "Please re-enter the Coordinates", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    JSONObject json;
    void startAPICall1() {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://api.sunrise-sunset.org/json?lat=" + latitudeInput + "&lng=" + longitudeInput,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            json = response;
                            Log.d(TAG, response.toString());
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(final VolleyError error) {
                            Log.d(TAG, error.toString());
                    }
                });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();
        }
    }

    public JSONObject getResult() {
        JSONObject result;
        try {
            result = json.getJSONObject("results");
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getSunRise() throws JSONException {
        JSONObject result = this.getResult();
        sunRise = result.getString("sunrise");
        Log.d(TAG,sunRise);
        return "The sun rises at " + sunRise;
    }
    public String getSunset() throws JSONException {
        JSONObject result = this.getResult();
        sunSet = result.getString("sunset");
        Log.d(TAG,sunSet);
        return "The sun sets at " + sunSet;
    }
    public String getDayLength() throws JSONException {
        JSONObject result = this.getResult();
        dayLength = result.getString("day_length");
        return "The total time of daylight is " + dayLength + " hours";
    }




    JSONObject json2;
    void startAPICall2() {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "http://api.openweathermap.org/data/2.5/weather?zip=" + ZIP + ",us&appid=" + weatherAPIKey,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            json2 = response;
                            Log.d(TAG, response.toString());
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.d(TAG, error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();
        }
    }


    public JSONObject getResult1() {
        JSONObject result;
        try {
            result = json2.getJSONObject("main");
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getTemp() throws JSONException {
        JSONObject result = this.getResult1();
        double tempD = result.getDouble("temp");
        int tempI = (int) (tempD - 273) * 9 / 5 + 32;
        return "The average temperature is " + tempI + " Degrees Fahrenheit";
    }
    public String getCOORD() throws JSONException {
        JSONObject result = json2.getJSONObject("coord");
        String lng = result.getString("lon");
        String lat = result.getString("lat");
        return "Your latitude is: " + lat + " degrees and your longitude is: " + lng + " degrees";
    }
}
