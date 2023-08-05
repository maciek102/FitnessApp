package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.text1);
        new FetchProductInfoTask().execute("737628064502");
    }


    //Performing network request asynchronously not to perform network operations on main thread, to avoid freezing the application
    private class FetchProductInfoTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... barcodes) {
            if (barcodes.length > 0) {
                String barcode = barcodes[0];
                return OpenFoodFactsAPI.getFoodProductInfo(barcode);
            }
            return "No barcode provided.";
        }

        @Override
        protected void onPostExecute(String response) {
            display.setText(response);
        }
    }
}