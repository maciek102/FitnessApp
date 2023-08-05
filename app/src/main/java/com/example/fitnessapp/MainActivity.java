package com.example.fitnessapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class MainActivity extends AppCompatActivity {

    private TextView display;
    private AppCompatButton btnScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnScan = findViewById(R.id.buttonScan);

        /*display = findViewById(R.id.text1);
        new FetchProductInfoTask().execute("737628064502");*/
    }

    private void scanCode(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() != null){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Result");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }
    });

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