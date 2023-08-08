package com.example.fitnessapp;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OpenFoodFactsAPI {

    public static String getFoodProductInfo(String barcode) {
        try {
            String apiUrl = "https://world.openfoodfacts.org/api/v0/product/" + barcode + ".json";

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return(response.toString());
            } else {
                return("Błąd podczas zapytania API. Kod odpowiedzi: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return("Błąd podczas zapytania API.");
    }

    public static String returnInfo(String barcode){
        String jsonResponse = getFoodProductInfo(barcode);

        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONObject productInfo = jsonObject.getJSONObject("product");

            String productName = productInfo.optString("product_name", "Unknown Product Name");
            String brands = productInfo.optString("brands", "Unknown Brand");
            //String categories = productInfo.optString("categories", "Unknown Categories");
            String ingredients_text = productInfo.optString("ingredients_text","Unknown ingredients");

            return ("Product Name: " + productName + "\nBrands: " + brands + "\nIngredients: " + ingredients_text + "\nNutrition (for 100g): " + setNutritionList(productInfo));
            //return ("Product Name: " + productName + " Brands: " + brands + " Ingredients: " + ingredients_text + "fat: " + fat);

        } catch (JSONException e) {
            e.printStackTrace();
            return "Error";
        }
    }

    public static StringBuilder setNutritionList(JSONObject productInfo) throws JSONException {

        String[] nutrientKeys = {"energy", "proteins", "sugars", "fat", "fiber" ,"casein","starch"};

        StringBuilder nutritionList = new StringBuilder();

        for (String nutrientKey : nutrientKeys) {
            String nutrientValue = getNutrientValue(productInfo, nutrientKey);
            nutritionList.append(nutrientKey + ": " + nutrientValue + ", ");
        }

        nutritionList.delete(nutritionList.length()-2,nutritionList.length()-1);

        return nutritionList;
    }

    public static String getNutrientValue(JSONObject productInfo, String nutrientKey) throws JSONException {
        JSONObject nutriments = productInfo.getJSONObject("nutriments");
        if (nutriments.has(nutrientKey)) {
            return String.valueOf(nutriments.getDouble(nutrientKey));
        } else {
            return "-";  // If key is not present
        }
    }
}
