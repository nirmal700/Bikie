package com.bikie.in;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;

public class CustomTimePickerDialog extends TimePickerDialog {

    public CustomTimePickerDialog(Context context, TimePickerDialog.OnTimeSetListener callBack, int hourOfDay, int minute, boolean is24HourView) {
        super(context, callBack, hourOfDay, minute / 30 * 30, is24HourView);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getButton(DialogInterface.BUTTON_NEGATIVE).setVisibility(View.GONE);
        getButton(DialogInterface.BUTTON_POSITIVE).setVisibility(View.GONE);
        getButton(DialogInterface.BUTTON_NEUTRAL).setVisibility(View.GONE);

        final TimePicker timePicker = findViewById(getContext().getResources().getIdentifier("timePicker", "id", "android"));
        if (timePicker != null) {
            timePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> {
                timePicker.setMinute(minute / 30 * 30);
            });
        }
    }
}