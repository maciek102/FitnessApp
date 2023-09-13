package com.example.fitnessapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    protected ProductNutrition productNutrition;
    protected NutritionHistory nutritionHistory;
    protected MainMenuOperations mainMenuOperations;
    private double limitEnergy = 1000;
    private double limitProteins = 1000;
    private double limitCarbohydrates = 1000;
    private double limitFat = 1000;
    private double limitSugars = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewGroup mainLayout = findViewById(R.id.mainLayout);

        productNutrition = new ProductNutrition();
        nutritionHistory = new NutritionHistory();
        mainMenuOperations = new MainMenuOperations();
        nutritionHistory = NutritionHistory.deserialize(this);
        if(nutritionHistory != null){
            setStatistics(nutritionHistory.getLastElement());
        }
        else{
            nutritionHistory = new NutritionHistory();
            setStatistics(new NutritionData());
        }
        mainMenuOperations.setProgressBars(mainLayout,this);
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
            TextView productInfoText = findViewById(R.id.productInfoList);
            new FetchProductInfoTask(productInfoText).execute(result.getContents());
        }
    });

    public void addProductToProgress(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        builder.setTitle(R.string.dialog_title);

        final EditText input = new EditText(this);
        input.setHint(R.string.dialog_message);
        input.setHintTextColor(Color.GRAY);
        input.setTextColor(Color.WHITE);
        builder.setView(input);

        builder.setPositiveButton(R.string.dialog_save, (dialog, id) -> {
            String userInput = input.getText().toString();
            if (!userInput.isEmpty()) {
                double quantity = Double.parseDouble(userInput);
                NutritionData nutritionData = new NutritionData(productNutrition.getDate(), (productNutrition.getEnergy()*quantity)/100, (productNutrition.getProteins()*quantity)/100, (productNutrition.getCarbohydrates()*quantity)/100, (productNutrition.getSugars()*quantity)/100, (productNutrition.getFat()*quantity)/100);
                nutritionHistory.pushObj(nutritionData);
                nutritionHistory.serialize(this);

                NutritionData temp = nutritionHistory.getLastElement();

                setContentView(R.layout.activity_main);
                setStatistics(temp);
            }
            else{
            dialog.cancel();
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, (dialog, id) -> {
            dialog.cancel();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void mainLayout(View view){
        setContentView(R.layout.activity_main);
        if(nutritionHistory != null){
            setStatistics(nutritionHistory.getLastElement());
        }
        else{
            setStatistics(new NutritionData());
        }
    }

    public void productInfoLayout(){
        setContentView(R.layout.product_info);
    }


    //Performing network request asynchronously not to perform network operations on main thread, to avoid freezing the application
    private class FetchProductInfoTask extends AsyncTask<String, Void, String> {

        private final TextView textView;

        public FetchProductInfoTask(TextView textView) {
            this.textView = textView;
        }

        @Override
        protected String doInBackground(String... barcodes) {
            if (barcodes.length > 0) {
                String barcode = barcodes[0];
                productNutrition.setProductNutrition(barcode);
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

    public void setStatistics(NutritionData obj){
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        String roundedValue = null;

        TextView textView = findViewById(R.id.menuSign);
        textView.setText(HelpingFunctions.formatDate(nutritionHistory.getLastElement().getDate()));

        textView = findViewById(R.id.textViewProgressEnergy);
        roundedValue = decimalFormat.format(obj.getEnergy());
        textView.setText(roundedValue + "/" + limitEnergy);
        ProgressBar progressBar = findViewById(R.id.progressBarEnergy);
        progressBar.setProgress((int)((obj.getEnergy()/limitEnergy)*100));

        textView = findViewById(R.id.textViewProgressProteins);
        roundedValue = decimalFormat.format(obj.getProteins());
        textView.setText(roundedValue + "/" + limitProteins);
        progressBar = findViewById(R.id.progressBarProteins);
        progressBar.setProgress((int)((obj.getProteins()/limitProteins)*100));

        textView = findViewById(R.id.textViewProgressCarbohydrates);
        roundedValue = decimalFormat.format(obj.getCarbohydrates());
        textView.setText(roundedValue + "/" + limitCarbohydrates);
        progressBar = findViewById(R.id.progressBarCarbohydrates);
        progressBar.setProgress((int)((obj.getCarbohydrates()/limitCarbohydrates)*100));

        textView = findViewById(R.id.textViewProgressFat);
        roundedValue = decimalFormat.format(obj.getFat());
        textView.setText(roundedValue + "/" + limitFat);
        progressBar = findViewById(R.id.progressBarFat);
        progressBar.setProgress((int)((obj.getFat()/limitFat)*100));

        textView = findViewById(R.id.textViewProgressSugars);
        roundedValue = decimalFormat.format(obj.getSugars());
        textView.setText(roundedValue + "/" + limitSugars);
        progressBar = findViewById(R.id.progressBarSugars);
        progressBar.setProgress((int)((obj.getSugars()/limitSugars)*100));
    }

    public void setStatistics(){
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        String roundedValue = null;

        TextView textView = findViewById(R.id.menuSign);
        textView.setText(HelpingFunctions.formatDate(nutritionHistory.getLastElement().getDate()));

        textView = findViewById(R.id.textViewProgressEnergy);
        roundedValue = decimalFormat.format(nutritionHistory.getLastElement().getEnergy());
        textView.setText(roundedValue + "/" + limitEnergy);
        ProgressBar progressBar = findViewById(R.id.progressBarEnergy);
        progressBar.setProgress((int)((nutritionHistory.getLastElement().getEnergy()/limitEnergy)*100));

        textView = findViewById(R.id.textViewProgressProteins);
        roundedValue = decimalFormat.format(nutritionHistory.getLastElement().getProteins());
        textView.setText(roundedValue + "/" + limitProteins);
        progressBar = findViewById(R.id.progressBarProteins);
        progressBar.setProgress((int)((nutritionHistory.getLastElement().getProteins()/limitProteins)*100));

        textView = findViewById(R.id.textViewProgressCarbohydrates);
        roundedValue = decimalFormat.format(nutritionHistory.getLastElement().getCarbohydrates());
        textView.setText(roundedValue + "/" + limitCarbohydrates);
        progressBar = findViewById(R.id.progressBarCarbohydrates);
        progressBar.setProgress((int)((nutritionHistory.getLastElement().getCarbohydrates()/limitCarbohydrates)*100));

        textView = findViewById(R.id.textViewProgressFat);
        roundedValue = decimalFormat.format(nutritionHistory.getLastElement().getFat());
        textView.setText(roundedValue + "/" + limitFat);
        progressBar = findViewById(R.id.progressBarFat);
        progressBar.setProgress((int)((nutritionHistory.getLastElement().getFat()/limitFat)*100));

        textView = findViewById(R.id.textViewProgressSugars);
        roundedValue = decimalFormat.format(nutritionHistory.getLastElement().getSugars());
        textView.setText(roundedValue + "/" + limitSugars);
        progressBar = findViewById(R.id.progressBarSugars);
        progressBar.setProgress((int)((nutritionHistory.getLastElement().getSugars()/limitSugars)*100));
    }

    public void setLimitEnergy(double limitEnergy) {
        this.limitEnergy = limitEnergy;
    }

    public void setLimitProteins(double limitProteins) {
        this.limitProteins = limitProteins;
    }

    public void setLimitCarbohydrates(double limitCarbohydrates) {
        this.limitCarbohydrates = limitCarbohydrates;
    }

    public void setLimitFat(double limitFat) {
        this.limitFat = limitFat;
    }

    public void setLimitSugars(double limitSugars) {
        this.limitSugars = limitSugars;
    }
}