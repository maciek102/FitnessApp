package com.example.fitnessapp;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HelpingFunctions {
    public static String formatDate(Date date) {

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("E d-M-y");

        return dateFormat.format(date);
    }
}
