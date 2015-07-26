package com.erikbuto.workoutprogram.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Utilisateur on 26/07/2015.
 */
public class TextViewThinFont extends TextView {

        public TextViewThinFont(Context context) {
            super(context);
            Typeface face = Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf");
            this.setTypeface(face);
        }

        public TextViewThinFont(Context context, AttributeSet attrs) {
            super(context, attrs);
            Typeface face = Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf");
            this.setTypeface(face);
        }

        public TextViewThinFont(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            Typeface face = Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf");
            this.setTypeface(face);
        }

        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
        }
}
