package com.ojassoft.astrosage.varta.adapters;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.FrameLayout;

import androidx.recyclerview.widget.RecyclerView;
//ZoomableRecyclerViewLayout

public class ZoomableRecyclerViewLayout extends RecyclerView {
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;
    private float scaleFactor = 1.0f;
    private final float minScale = 1.0f, maxScale = 3.0f;
    private float focusX = 0f, focusY = 0f;
    private float translateX = 0f, translateY = 0f;
    private float lastTouchX, lastTouchY;
    private boolean isDragging = false;
    private boolean isScaling = false;

    public ZoomableRecyclerViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOverScrollMode(OVER_SCROLL_NEVER); // Prevent overscroll bounce
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return scaleGestureDetector.onTouchEvent(event) || super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);

        if (scaleFactor > 1.0f && !isScaling) { // Enable smooth panning only when zoomed
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    lastTouchX = event.getX();
                    lastTouchY = event.getY();
                    isDragging = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    float dx = event.getX() - lastTouchX;
                    float dy = event.getY() - lastTouchY;

                    if (Math.abs(dx) > 5 || Math.abs(dy) > 5) {
                        isDragging = true;
                    }

                    if (isDragging) {
                        float maxTranslateX = (getWidth() * (scaleFactor - 1)) / 2;
                        float maxTranslateY = (getHeight() * (scaleFactor - 1)) / 2;

                        translateX = Math.max(-maxTranslateX, Math.min(translateX + dx, maxTranslateX));
                        translateY = Math.max(-maxTranslateY, Math.min(translateY + dy, maxTranslateY));

                        applyTransformation(false); // Smooth movement without flickering
                    }

                    lastTouchX = event.getX();
                    lastTouchY = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    isDragging = false;
                    break;
            }
        }

        return super.onTouchEvent(event);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            isScaling = true;
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float previousScale = scaleFactor;
            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(minScale, Math.min(scaleFactor, maxScale)); // Limit zoom range

            if (scaleFactor == minScale) {
                resetZoom(); // Fix view misalignment when zooming out fully
            } else {
                float focusShiftX = detector.getFocusX() - focusX;
                float focusShiftY = detector.getFocusY() - focusY;

                if (previousScale != scaleFactor) {
                    translateX -= focusShiftX * (scaleFactor - previousScale);
                    translateY -= focusShiftY * (scaleFactor - previousScale);
                }

                focusX = detector.getFocusX();
                focusY = detector.getFocusY();
                applyTransformation(true);
            }

            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            isScaling = false;
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            resetZoom();
            return true;
        }
    }

    private void applyTransformation(boolean isScalingNow) {
        if (isScalingNow) {
            setScaleX(scaleFactor);
            setScaleY(scaleFactor);
        } else {
            animate().translationX(translateX).translationY(translateY).setDuration(0).start();
        }

        // Fix potential empty spaces after zoom-out
        if (scaleFactor == minScale) {
            setTranslationX(0);
            setTranslationY(0);
        }

        requestLayout();
    }

    private void resetZoom() {
        scaleFactor = 1.0f;
        translateX = 0f;
        translateY = 0f;
        setTranslationX(0);
        setTranslationY(0);
        setScaleX(1.0f);
        setScaleY(1.0f);
        requestLayout();
    }
}
