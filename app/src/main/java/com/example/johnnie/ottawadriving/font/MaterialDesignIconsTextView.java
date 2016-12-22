package com.example.johnnie.ottawadriving.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Come from the temppate to present costumed icon design
 * Created by Jiangqi on 2016-12-19.
 */

public class MaterialDesignIconsTextView extends TextView {

    private static Typeface sMaterialDesignIcons;

    public MaterialDesignIconsTextView(Context context) {
        this(context, null);
    }

    public MaterialDesignIconsTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaterialDesignIconsTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (isInEditMode()) return;//Won't work in Eclipse graphical layout
        setTypeface();
    }

    private void setTypeface() {
        if (sMaterialDesignIcons == null) {
            sMaterialDesignIcons = Typeface.createFromAsset(getContext().getAssets(), "fonts/MaterialDesignIcons.ttf");
        }
        setTypeface(sMaterialDesignIcons);
    }
}
