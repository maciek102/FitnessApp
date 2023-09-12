package com.example.fitnessapp;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

public class NutritionHistory implements Serializable {
    Deque<NutritionData> nutritionHistoryDeque;
    final static int SIZE = 7;

    public NutritionHistory(){
        nutritionHistoryDeque = new ArrayDeque<>();
    }

    public void pushObj(NutritionData nutritionData) {
        if (!nutritionHistoryDeque.isEmpty()) {
            NutritionData peekLast = nutritionHistoryDeque.peekLast();
            if (isSameDay(peekLast.getDate(), nutritionData.getDate())) {
                // Combine entries with the same date
                peekLast.setEnergy(peekLast.getEnergy() + nutritionData.getEnergy());
                peekLast.setProteins(peekLast.getProteins() + nutritionData.getProteins());
                peekLast.setCarbohydrates(peekLast.getCarbohydrates() + nutritionData.getCarbohydrates());
                peekLast.setSugars(peekLast.getSugars() + nutritionData.getSugars());
                peekLast.setFat(peekLast.getFat() + nutritionData.getFat());
            } else {
                // Add a new entry if the date is different
                nutritionHistoryDeque.offer(nutritionData);
            }
        } else {
            // If the deque is empty, add the first entry
            nutritionHistoryDeque.offer(nutritionData);
        }

        // Ensure that the deque does not exceed the maximum size
        while (nutritionHistoryDeque.size() > SIZE) {
            nutritionHistoryDeque.poll();
        }
    }

    public NutritionData getLastElement(){
        if(!nutritionHistoryDeque.isEmpty()){
            return nutritionHistoryDeque.peekLast();
        }
        else {
            NutritionData temp = new NutritionData();
            return temp;
        }
    }

    public void serialize(Context context){

        try {
            FileOutputStream fos = context.openFileOutput("nutrition_history.ser", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(this);
            os.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*public void serialize(NutritionHistory obj){

        try {
            FileOutputStream fileOut = new FileOutputStream("nutrition_history.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(obj);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    /*public void serialize(Context context) {
        try {
            // Get the internal storage directory
            File internalStorageDir = context.getFilesDir();

            // Create a file within the internal storage directory
            File file = new File(internalStorageDir, "nutrition_history.ser");

            // Serialize the object to the specified file
            try (FileOutputStream fileOut = new FileOutputStream(file);
                 ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
                out.writeObject(this); // Serialize the current object
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public static NutritionHistory deserialize(Context context) {
        NutritionHistory obj = null;

        try {
            FileInputStream fis = context.openFileInput("nutrition_history.ser");
            ObjectInputStream is = new ObjectInputStream(fis);
            obj = (NutritionHistory) is.readObject();
            is.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return obj;
    }

    /*public NutritionHistory deserialize(){
        NutritionHistory deserializedObj = null;

        try {
            FileInputStream fileIn = new FileInputStream("nutrition_history.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            deserializedObj = (NutritionHistory) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return deserializedObj;
    }*/

    /*public static NutritionHistory deserialize(Context context) {
        NutritionHistory deserializedObj = null;

        try {
            // Get the internal storage directory
            File internalStorageDir = context.getFilesDir();

            // Create a file within the internal storage directory
            File file = new File(internalStorageDir, "nutrition_history.ser");

            // Deserialize the object from the specified file
            try (FileInputStream fileIn = new FileInputStream(file);
                 ObjectInputStream in = new ObjectInputStream(fileIn)) {
                deserializedObj = (NutritionHistory) in.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return deserializedObj;
    }*/

    private boolean isSameDay(Date date1, Date date2) {
        // Compare dates at the day level
        // Note: You may need to implement this method based on your specific requirements
        // This is a simplified example.
        // You should handle date comparison more accurately, considering time zones, etc.
        return date1.getDate() == date2.getDate() &&
                date1.getMonth() == date2.getMonth() &&
                date1.getYear() == date2.getYear();
    }
}
