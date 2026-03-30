package com.ojassoft.astrosage.ui.customcontrols;


import android.app.Activity;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class CGestureDetector extends SimpleOnGestureListener {

    public final static int SWIPE_UP = 1;
    public final static int SWIPE_DOWN = 2;
    public final static int SWIPE_LEFT = 3;
    public final static int SWIPE_RIGHT = 4;

    public final static int MODE_TRANSPARENT = 0;
    public final static int MODE_SOLID = 1;
    public final static int MODE_DYNAMIC = 2;

    private final static int ACTION_FAKE = -13;

    private int mode = MODE_DYNAMIC;
    private boolean running = true;
    private boolean tapIndicator = true;

    private Activity context;
    private GestureDetector detector;
    public SimpleGestureListener listener;


    public CGestureDetector(Activity context, SimpleGestureListener sgl) {
        this.context = context;
        this.detector = new GestureDetector(context, this);
        this.listener = sgl;
    }

    public void onTouchEvent(MotionEvent event) {

        if (!this.running)
            return;

        boolean result = this.detector.onTouchEvent(event);

        if (this.mode == MODE_SOLID)
            event.setAction(MotionEvent.ACTION_CANCEL);
        else if (this.mode == MODE_DYNAMIC) {

            if (event.getAction() == ACTION_FAKE)
                event.setAction(MotionEvent.ACTION_UP);
            else if (result)
                event.setAction(MotionEvent.ACTION_CANCEL);
            else if (this.tapIndicator) {
                event.setAction(MotionEvent.ACTION_DOWN);
                this.tapIndicator = false;
            }

        }
        //else just do nothing, it's Transparent
    }

    public void setMode(int m) {
        this.mode = m;
    }

    public int getMode() {
        return this.mode;
    }

    public void setEnabled(boolean status) {
        this.running = status;
    }


    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        this.listener.onSingleTapUp(e);
        this.tapIndicator = true;
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent arg0) {
        this.listener.onDoubleTap();
        ;
        return true;
    }


    @Override
    public boolean onDoubleTapEvent(MotionEvent arg0) {
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent arg0) {
        try{
            if (this.mode == MODE_DYNAMIC) {        // we owe an ACTION_UP, so we fake an
                arg0.setAction(ACTION_FAKE);      //action which will be converted to an ACTION_UP later.
                this.context.dispatchTouchEvent(arg0);
            }
        }catch (Exception e){
            //
        }


        return false;
    }

    public static interface SimpleGestureListener {
        void onDoubleTap();
        boolean onSingleTapUp(MotionEvent e);
    }
}
