package com.example.fitnessapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.Date;

public class ProductNutrition {

    private Date date;
    private double energy;
    private double proteins;
    private double carbohydrates;
    private double sugars;
    private double fat;

    public double getEnergy() {
        return energy;
    }

    public double getProteins() {
        return proteins;
    }

    public double getCarbohydrates() {
        return carbohydrates;
    }

    public double getSugars() {
        return sugars;
    }

    public double getFat() {
        return fat;
    }

    public Date getDate(){
        return date;
    }

    ProductNutrition(){
        date = null;
        energy = 0;
        proteins = 0;
        carbohydrates = 0;
        sugars = 0;
        fat = 0;
    }

    public void setProductNutrition(String barcode){
        String jsonResponse = OpenFoodFactsAPI.getFoodProductInfo(barcode);

        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONObject productInfo = jsonObject.getJSONObject("product");
            JSONObject nutriments = productInfo.getJSONObject("nutriments");

            date = new Date();
            energy = getNutrimentValue(nutriments, "energy");
            proteins = getNutrimentValue(nutriments, "proteins");
            carbohydrates = getNutrimentValue(nutriments, "carbohydrates");
            sugars = getNutrimentValue(nutriments, "sugars");
            fat = getNutrimentValue(nutriments, "fat");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public double getNutrimentValue(JSONObject nutriments, String key) throws JSONException {
        if(nutriments.has(key)){
            return nutriments.getDouble(key);
        }
        else {
            return 0.0;
        }
    }
}
