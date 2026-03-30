package com.ojassoft.astrosage.varta.utils;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;


public class RoundImage extends androidx.appcompat.widget.AppCompatImageView {

    private int borderWidth;
    private int viewWidth;
    private int viewHeight;
    private Bitmap image;
    private Paint paint;
    private Paint paintBorder;
    private BitmapShader shader;

    public RoundImage(Context context) {
        super(context);
        setup();
    }

    public RoundImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public RoundImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setup() {
        // init paint
        paint = new Paint();
        paint.setAntiAlias(true);

        paintBorder = new Paint();
        //setBorderColor(getContext().getResources().getColor(R.color.colorPrimary));
        paintBorder.setAntiAlias(true);
        //paintBorder.setShadowLayer(2f, 0f, 0f, getContext().getResources().getColor(R.color.colorPrimaryDark));
        this.setLayerType(LAYER_TYPE_SOFTWARE, paintBorder);
        this.borderWidth = 0;
        //paintBorder.setColor(getContext().getResources().getColor(R.color.button_bg_start));
        //paintBorder.setShadowLayer(4.0f, 0.0f, 2.0f, Color.BLACK);


        //borderWidth = 0;

    }

    public void setShadowLayer(float radius, float dx, float dy, int shadowColor) {

        paintBorder.setShadowLayer(radius, dx, dy, shadowColor);
        this.invalidate();
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        this.invalidate();
    }

    public void setBorderColor(int borderColor) {
        if (paintBorder != null)
            paintBorder.setColor(borderColor);

        this.invalidate();
    }

    private void loadBitmap() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) this.getDrawable();

        if (bitmapDrawable != null)
            image = bitmapDrawable.getBitmap();
    }

    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas) {
        // load the bitmap
        loadBitmap();

        // init shader
        if (image != null) {
            shader = new BitmapShader(Bitmap.createScaledBitmap(image,
                    canvas.getWidth(), canvas.getHeight(), false),
                    Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(shader);
            int circleCenter = viewWidth / 2;

            // circleCenter is the x or y of the view's center
            // radius is the radius in pixels of the cirle to be drawn
            // paint contains the shader that will texture the shape
            canvas.drawCircle(circleCenter + borderWidth, circleCenter
                            + borderWidth, circleCenter + borderWidth - 4.0f,
                    paintBorder);
            canvas.drawCircle(circleCenter + borderWidth, circleCenter
                    + borderWidth, circleCenter - 4.0f, paint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec, widthMeasureSpec);

        viewWidth = width - (borderWidth * 2);
        viewHeight = height - (borderWidth * 2);

        setMeasuredDimension(width, height);
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text
            result = viewWidth;
        }

        return result;
    }

    private int measureHeight(int measureSpecHeight, int measureSpecWidth) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpecHeight);
        int specSize = MeasureSpec.getSize(measureSpecHeight);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text (beware: ascent is a negative number)
            result = viewHeight;
        }

        return (result + 2);
    }
}