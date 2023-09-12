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
        nutritionHistoryDeque.offer(new NutritionData());
    }

    public void pushObj(NutritionData nutritionData) {
        if (!nutritionHistoryDeque.isEmpty()) {
            NutritionData peekLast = nutritionHistoryDeque.peekLast();
            if (isSameDay(peekLast.getDate(), nutritionData.getDate())) {
                peekLast.setEnergy(peekLast.getEnergy() + nutritionData.getEnergy());
                peekLast.setProteins(peekLast.getProteins() + nutritionData.getProteins());
                peekLast.setCarbohydrates(peekLast.getCarbohydrates() + nutritionData.getCarbohydrates());
                peekLast.setSugars(peekLast.getSugars() + nutritionData.getSugars());
                peekLast.setFat(peekLast.getFat() + nutritionData.getFat());
            } else {
                nutritionHistoryDeque.offer(nutritionData);
            }
        } else {
            nutritionHistoryDeque.offer(nutritionData);
        }

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

    private boolean isSameDay(Date date1, Date date2) {
        return date1.getDate() == date2.getDate() &&
                date1.getMonth() == date2.getMonth() &&
                date1.getYear() == date2.getYear();
    }
}
