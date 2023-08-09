package com.example.fitnessapp;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;

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
            String ingredients_text = productInfo.optString("ingredients_text","Unknown ingredients");

            return (brands + " - " + productName + "\n\nIngredients: " + ingredients_text + "\n\nMain nutrients (for 100g): " + setMainNutritionList(productInfo) + "\n\nNutrients (for 100g): " + setNutritionList(productInfo));

        } catch (JSONException e) {
            e.printStackTrace();
            return "Error";
        }
    }

    public static StringBuilder setMainNutritionList(JSONObject productInfo) throws JSONException {

        String[] nutrientKeys = {"energy", "proteins", "fat","carbohydrates", "sugars"};

        StringBuilder nutritionList = new StringBuilder();

        for (String nutrientKey : nutrientKeys) {
            String nutrientValue = getNutrientValue(productInfo, nutrientKey);
            nutritionList.append("\n" + nutrientKey + ": " + nutrientValue);
        }

        nutritionList.delete(nutritionList.length()-2,nutritionList.length()-1);

        return nutritionList;
    }

    public static String getNutrientValue(JSONObject productInfo, String nutrientKey) throws JSONException {
        JSONObject nutriments = productInfo.getJSONObject("nutriments");
        if (nutriments.has(nutrientKey)) {
            return String.valueOf(nutriments.getDouble(nutrientKey));
        } else {
            return "0.0";  // If key is not present
        }
    }

    public static StringBuilder setNutritionList(JSONObject productInfo) throws JSONException {

        String[] nutrientKeys = {"casein", "serum-proteins", "nucleotides", "sucrose", "glucose", "fructose", "lactose", "maltose", "maltodextrins", "starch", "polyols", "saturated-fat", "butyric-acid", "caproic-acid", "caprylic-acid", "capric-acid", "lauric-acid", "myristic-acid", "palmitic-acid", "stearic-acid", "arachidic-acid", "behenic-acid", "lignoceric-acid", "cerotic-acid", "montanic-acid", "melissic-acid", "monounsaturated-fat", "polyunsaturated-fat", "omega-3-fat", "alpha-linolenic-acid", "eicosapentaenoic-acid", "docosahexaenoic-acid", "omega-6-fat", "linoleic-acid", "arachidonic-acid", "gamma-linolenic-acid", "dihomo-gamma-linolenic-acid", "omega-9-fat", "oleic-acid", "elaidic-acid", "gondoic-acid", "mead-acid", "erucic-acid", "nervonic-acid", "trans-fat", "cholesterol", "fiber", "sodium", "alcohol: % vol of alcohol", "vitamin-a", "vitamin-d", "vitamin-e", "vitamin-k", "vitamin-c", "vitamin-b1", "vitamin-b2", "vitamin-pp", "vitamin-b6", "vitamin-b9", "vitamin-b12", "biotin", "pantothenic-acid", "silica", "bicarbonate", "potassium", "chloride", "calcium", "phosphorus", "iron", "magnesium", "zinc", "copper", "manganese", "fluoride", "selenium", "chromium", "molybdenum", "iodine", "caffeine", "taurine"};

        StringBuilder nutritionList = new StringBuilder();

        for (String nutrientKey : nutrientKeys) {
            String nutrientValue = getNutrientValue(productInfo, nutrientKey);
            nutritionList.append(nutrientKey + ": " + nutrientValue + ", ");
        }

        nutritionList.delete(nutritionList.length()-2,nutritionList.length()-1);

        return nutritionList;
    }
}
