package com.example.fitnessapp;

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
}
