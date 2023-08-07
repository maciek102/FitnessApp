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
        String jsonResponse = getFoodProductInfo(barcode);  // Replace with your actual JSON response

        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONObject productInfo = jsonObject.getJSONObject("product");

            // Now you can extract specific information from the "productInfo" JSONObject
            String productName = productInfo.optString("product_name", "Unknown Product Name");
            String brands = productInfo.optString("brands", "Unknown Brand");
            String categories = productInfo.optString("categories", "Unknown Categories");

            // Print the extracted information
            /*System.out.println("Product Name: " + productName);
            System.out.println("Brands: " + brands);
            System.out.println("Categories: " + categories);*/

            return ("Product Name: " + productName + " Brands: " + brands + " Categories: " + categories);

        } catch (JSONException e) {
            e.printStackTrace();
            return "Error";
        }
    }
}
