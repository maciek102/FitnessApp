package com.example.fitnessapp;

import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

public class MainMenuOperations {

    public void setProgressBars(ViewGroup mainLayout, MainActivity activity){
        if(mainLayout!=null){
            FrameLayout containerEnergy = mainLayout.findViewById(R.id.progressBarContainerEnergy);
            FrameLayout containerProteins = mainLayout.findViewById(R.id.progressBarContainerProteins);
            FrameLayout containerCarbohydrates = mainLayout.findViewById(R.id.progressBarContainerCarbohydrates);
            FrameLayout containerFat = mainLayout.findViewById(R.id.progressBarContainerFat);
            FrameLayout containerSugars = mainLayout.findViewById(R.id.progressBarContainerSugars);
            setAlertDialogs(containerEnergy, "limitEnergy", activity);
            setAlertDialogs(containerProteins, "limitProteins", activity);
            setAlertDialogs(containerCarbohydrates, "limitCarbohydrates", activity);
            setAlertDialogs(containerFat, "limitFat", activity);
            setAlertDialogs(containerSugars, "limitSugars", activity);
        }
    }

    public void setAlertDialogs(FrameLayout container, String variableName, MainActivity activity) {
        container.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(), R.style.AlertDialogTheme);
            builder.setTitle(R.string.dialog_limit_title);

            final EditText input = new EditText(v.getContext());
            input.setHint(R.string.dialog_limit_massage);
            input.setHintTextColor(Color.GRAY);
            input.setTextColor(Color.WHITE);
            builder.setView(input);

            builder.setPositiveButton(R.string.dialog_save, (dialog, id) -> {
                String userInput = input.getText().toString();
                if (!userInput.isEmpty()) {
                    double newValue = Double.parseDouble(userInput);
                    switch (variableName) {
                        case "limitEnergy":
                            activity.setLimitEnergy(newValue);
                            activity.savePreference("limitEnergy",newValue);
                            break;
                        case "limitProteins":
                            activity.setLimitProteins(newValue);
                            activity.savePreference("limitProteins",newValue);
                            break;
                        case "limitCarbohydrates":
                            activity.setLimitCarbohydrates(newValue);
                            activity.savePreference("limitCarbohydrates",newValue);
                            break;
                        case "limitFat":
                            activity.setLimitFat(newValue);
                            activity.savePreference("limitFat",newValue);
                            break;
                        case "limitSugars":
                            activity.setLimitSugars(newValue);
                            activity.savePreference("limitSugars",newValue);
                            break;
                        default:
                            activity.setStatistics();
                    }
                    activity.setStatistics();
                } else {
                    dialog.cancel();
                }
            });

            // Set negative button (optional)
            builder.setNegativeButton("Cancel", (dialog, which) -> {
                // Dismiss the dialog
                dialog.dismiss();
            });

            // Create and show the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }
}