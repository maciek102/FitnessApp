package com.example.fitnessapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private TextView display;
    TextView temp;
    private AppCompatButton btnScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnScan = findViewById(R.id.buttonScan);

        btnScan.setOnClickListener(y ->{
            scanCode();
        });

        /*display = findViewById(R.id.text1);
        new FetchProductInfoTask().execute("737628064502");*/
    }

    private void scanCode(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() != null){

            productInfoLayout();
            temp=findViewById(R.id.textView);
            //temp.setText(result.getContents());
            new FetchProductInfoTask().execute(result.getContents());
        }
    });

    public void mainLayout(){
        setContentView(R.layout.activity_main);
    }

    public void productInfoLayout(){
        setContentView(R.layout.product_info);
    }

    //Performing network request asynchronously not to perform network operations on main thread, to avoid freezing the application
    private class FetchProductInfoTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... barcodes) {
            if (barcodes.length > 0) {
                String barcode = barcodes[0];
                //return OpenFoodFactsAPI.getFoodProductInfo(barcode);
                return OpenFoodFactsAPI.returnInfo(barcode);
            }
            return "No barcode provided.";
        }

        @Override
        protected void onPostExecute(String response) {
            temp.setText(response);
        }
    }
}