package com.example.fitnessapp;

import java.io.Serializable;
import java.util.Date;

public class NutritionData implements Serializable {
    private Date date;
    private double energy;
    private double proteins;
    private double carbohydrates;
    private double sugars;
    private double fat;

    public NutritionData() {
        date = new Date();
        energy = 0;
        proteins = 0;
        carbohydrates = 0;
        sugars = 0;
        fat = 0;
    }

    public NutritionData(Date date, double energy, double proteins, double carbohydrates, double sugars, double fat) {
        this.date = date;
        this.energy = energy;
        this.proteins = proteins;
        this.carbohydrates = carbohydrates;
        this.sugars = sugars;
        this.fat = fat;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public void setProteins(double proteins) {
        this.proteins = proteins;
    }

    public void setCarbohydrates(double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public void setSugars(double sugars) {
        this.sugars = sugars;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

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

}
