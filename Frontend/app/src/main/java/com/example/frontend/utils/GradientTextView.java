package com.example.frontend.utils;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;

public class GradientTextView extends AppCompatTextView {
    private int startColor;
    private int centerColor;
    private int endColor;

    public GradientTextView(Context context) {
        super(context);
        init();
    }

    public GradientTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GradientTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        startColor = 0xFFFBAA47; // Example start color
        centerColor = 0xFFD91A46; // Example center color
        endColor = 0xFFA60F93; // Example end color
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = getPaint();
        int width = getWidth();
        int height = getHeight();

        // Create gradient
        Shader shader = new LinearGradient(0, 0, width, height,
                new int[]{startColor, centerColor, endColor},
                null, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        super.onDraw(canvas);
    }
}
