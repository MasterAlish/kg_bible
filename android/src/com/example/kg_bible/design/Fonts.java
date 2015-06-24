package com.example.kg_bible.design;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

public class Fonts {
    public static final String HELVETICA_NEUE_LIGHT = "fonts/HelveticaNeue-Light.ttf";
    public static final String HELVETICA_NEUE_MEDIUM = "fonts/HelveticaNeue-Medium.ttf";
    public static final String HELVETICA_NEUE_BOLD = "fonts/HelveticaNeue-Bold.ttf";

    public static void setFontForTextView(Context context, TextView textView, String fontName){
        Typeface face = Typeface.createFromAsset(context.getAssets(), fontName);
        textView.setTypeface(face);
    }
}
