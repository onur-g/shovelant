package com.gunduzonur.changle2048;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Box extends FrameLayout {
    private int value = 0;
    private TextView text;

    public Box(Context context) {
        super(context);
        setValue(0);

        text = new TextView(getContext());
        text.setTextSize(32);
        text.setBackgroundColor(Color.parseColor("#ADD8E6"));
        text.setGravity(Gravity.CENTER);

        LayoutParams lp = new LayoutParams(-1, -1);
        lp.setMargins(20, 20, 0, 0);
        addView(text, lp);

        setValue(0);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;

        if (value < 2) {
            text.setText("");
        } else {
            text.setText(value);
        }
    }

    public boolean equals(Box other) {
        return getValue() == other.getValue();
    }
}
