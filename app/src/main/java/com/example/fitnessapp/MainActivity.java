package com.example.fitnessapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class MainActivity extends AppCompatActivity {
    private TextView productInfoText;
    private AppCompatButton btnScan;
    private AppCompatButton btnBackToMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* btnScan = findViewById(R.id.buttonScan);
        btnScan.setOnClickListener(y ->{
            scanCode();
        });*/

        //View productInfoLayout = LayoutInflater.from(this).inflate(R.layout.product_info,null);
        //btnBackToMenu = productInfoLayout.findViewById(R.id.buttonBackToMenu);
        //btnBackToMenu.setOnClickListener(v -> backToMenu());
    }

    public void scanCode(View view){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() != null){

            productInfoLayout();
            productInfoText =findViewById(R.id.productInfoList);
            new FetchProductInfoTask(productInfoText).execute(result.getContents());
        }
    });

    public void mainLayout(View view){
        setContentView(R.layout.activity_main);

    }

    public void productInfoLayout(){
        setContentView(R.layout.product_info);
    }


    //Performing network request asynchronously not to perform network operations on main thread, to avoid freezing the application
    private class FetchProductInfoTask extends AsyncTask<String, Void, String> {

        private TextView textView;

        public FetchProductInfoTask(TextView textView) {
            this.textView = textView;
        }

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
            if(textView != null) {
                textView.setText(response);
            }
        }
    }
}