package com.londonappbrewery.bitcointicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {

    // Constants:
    // TODO: Create the base URL
    private final String BASE_URL = "https://api.coindesk.com/v1/bpi/currentprice/";
    //private final  String APP_ID = "NzI0MjM4Njc1NGQ3NDU4Mzg1NWU3YYmU4MTdiMaA";

    // Member Variables:
    TextView mPriceTextView;
    String currency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPriceTextView = findViewById(R.id.priceLabel);
        Spinner spinner = findViewById(R.id.currency_spinner);

        // Create an ArrayAdapter using the String array and a spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // TODO: Set an OnItemSelected listener on the spinner
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("item","" + adapterView.getItemAtPosition(i));
                currency = (String) adapterView.getItemAtPosition(i);
                String finalURL = BASE_URL + currency ;
                Log.i("myURL", "" + finalURL);
                letsDoSomeNetworking(finalURL);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    // TODO: complete the letsDoSomeNetworking() method
    private void letsDoSomeNetworking(String URL) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL,new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                Log.d("Clima", "JSON: " + response.toString());
                try {
                    String price = response.getJSONObject("bpi").getJSONObject(currency).getString("rate");
                    Log.i("printed ", currency + price);
                    mPriceTextView.setText(price);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("Clima", "Request fail! Status code: " + statusCode);
                Toast.makeText(MainActivity.this,"Failed " + statusCode, Toast.LENGTH_SHORT).show();
                Log.d("Clima", "Fail response: " + response);
                Log.e("ERROR", e.toString());
            }
        });


    }


}
