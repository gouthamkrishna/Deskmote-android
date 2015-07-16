package com.beaconapp.user.navigation.classes;

import com.github.mikephil.charting.utils.ValueFormatter;

public class YvalueFormatter implements ValueFormatter {

    public YvalueFormatter() {

    }

    @Override
    public String getFormattedValue(float value) {
        int hour, minute;
        minute = (int) value/(60000);
        hour = minute/60;
        minute = minute%60;

        return ""+hour+" : "+minute;
    }
}
