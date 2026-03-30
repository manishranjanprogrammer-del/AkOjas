package com.ojassoft.astrosage.varta.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TimeUtils;
import android.widget.TextView;

import androidx.core.text.HtmlCompat;

import com.ojassoft.astrosage.varta.ui.activity.AIChatWindowActivity;

import java.util.concurrent.TimeUnit;

@SuppressLint("AppCompatCustomView")
public class TypeWriter extends TextView {

    /**
     * Holds the active typing timer for this specific TypeWriter view instance.
     */
    private MyCountDownTimer countDownTimer;
    /**
     * Holds the delayed/static rendering timer for this specific TypeWriter view instance.
     */
    private CountDownTimer timer;
    private  final int instaceIndex;

    public TypeWriter(Context context) {
        super(context);
        instaceIndex = 0;
    }

    public TypeWriter(Context context, AttributeSet attrs) {
        super(context, attrs);
        instaceIndex = 0;
    }
    public TypeWriter(Context context, int instaceIndex) {
        super(context);
        this.instaceIndex = instaceIndex;
    }

    public void animateText(String text, AIChatWindowActivity mActivity) {
        setText("");
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        mActivity.toggleSendStopButtonVisibility(false);
        long millisInFuture = text.length() * 25L;
        countDownTimer = new MyCountDownTimer(millisInFuture, 25, text, 0, mActivity, this);
        countDownTimer.start();
    }
    public void animateTextInfinite(CharSequence text){
        if(timer != null) timer.cancel();
        //Log.e("animateTextInfinite",text.toString());
        timer =  new CountDownTimer(TimeUnit.MINUTES.toMillis(10),200){
            int count = 0;
            @Override
            public void onTick(long millisUntilFinished) {
                if(count > text.length()) count=0;
                //Log.e("animateTextInfinite",text.subSequence(0,count++).toString());
                setText(text.subSequence(0,count++));
            }

            @Override
            public void onFinish() {
                timer.cancel();
            }
        };
        timer.start();
    }
    public void animateText( String text , AIChatWindowActivity mActivity,OnTypingComplete onComplete ) {

        setText("");
        if(countDownTimer!=null) {
            //countDownTimer.onFinish ();
            //SAN Exp comment
            // countDownTimer.setFullText ();
            countDownTimer.cancel ();
        }

        mActivity.toggleSendStopButtonVisibility(false);
        //Log.d("MyTag","10 minutes in miliseconds" + TimeUnit.MINUTES.toMillis(10));
        long millisInFuture = TimeUnit.MINUTES.toMillis(10);
        countDownTimer=new MyCountDownTimer (millisInFuture, 25,text,0,mActivity,this,instaceIndex,onComplete);
        countDownTimer.start ();
    }

    public void setText(String text,OnTypingComplete onComplete){
        setText(Html.fromHtml(text));
        onComplete.onFinishTyping(instaceIndex);
    }

    public void animateText( String text , AIChatWindowActivity mActivity,boolean isDelayed,OnTypingComplete onComplete ) {
        //Log.d("MyTag", "text = " + text);
        setText("");
        if(timer != null) timer.cancel();

        if(isDelayed) {
           timer =  new CountDownTimer(4000, 4000) {
                @Override
                public void onTick(long millisUntilFinished) {
                   Log.d("MyTag", "onTick() = " + text);
                    setText(Html.fromHtml(text));
                    if (mActivity.scrollWhileTypeWriter){
                        mActivity.scrollMyListViewToBottom();
                    }
                }

                @Override
                public void onFinish() {
                    //Log.d("MyTag", "onFinish() =");
                    if (onComplete != null) {
                        onComplete.onFinishTyping(instaceIndex);
                    }
                    if (mActivity.scrollWhileTypeWriter) {
                        mActivity.scrollMyListViewToBottom();
                    }
                }
            }.start();
        }else{
            //Log.d("MyTag", "else() ");
            setText(Html.fromHtml(text.toString()));
            if (onComplete != null) {
                onComplete.onFinishTyping(instaceIndex);
            }
        }
    }

    public void updateAnimateText(int position, String text, AIChatWindowActivity mActivity) {
        setText(text.subSequence(0, position));
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        mActivity.toggleSendStopButtonVisibility(false);
        long millisInFuture = 600000;
        countDownTimer = new MyCountDownTimer(millisInFuture, 25, text, position, mActivity, this);
        countDownTimer.start();
    }
    public void updateAnimatedText(String text){
        countDownTimer.updateMessage(text);
    }

    public static class MyCountDownTimer extends CountDownTimer {
        private  String mText;
        private int mIndex;
        private final AIChatWindowActivity mActivity;
        private final TextView textView;
        private final int instanceIndex;
        private  final OnTypingComplete onTypingComplete;

        public MyCountDownTimer ( long millisInFuture , long countDownInterval ,  String mText,int mIndex,AIChatWindowActivity mActivity,TextView textView) {
            super(millisInFuture, countDownInterval);
            this.mText=mText;
            this.mIndex=mIndex;
            this.mActivity=mActivity;
            this.textView=textView;
            this.instanceIndex =0;
            this.onTypingComplete = null;
        }

        public MyCountDownTimer ( long millisInFuture , long countDownInterval ,  String mText,int mIndex,AIChatWindowActivity mActivity,TextView textView,int instanceIndex,OnTypingComplete onComplete) {
            super(millisInFuture, countDownInterval);
            this.mText=mText;
            this.mIndex=mIndex;
            this.mActivity=mActivity;
            this.textView=textView;
            this.instanceIndex = instanceIndex;
            this.onTypingComplete = onComplete;
        }

        @Override
        public void onTick(long millisUntilFinished) {
      //      Log.e("KundliAIChatTesting", "TypeWriter onTick: timer "+millisUntilFinished );

            if (mIndex < mText.length()) {
                try {
                    if (mText.contains("\n\n") && mText.indexOf("\n\n") <= mIndex) {
                        moveTypeWriterToNext();
                    }
                }catch (Exception ignore){}
                mIndex = TypeWriterRenderCursor.advanceToNextRenderableIndex(mText, mIndex);
                String message = mText.subSequence(0, mIndex).toString().replaceAll("\\\\n","<br>");
                textView.setText(Html.fromHtml(message));
                mActivity.mIndexTypeWriter = mIndex;
                //for scroll with typing
                if (mActivity.scrollWhileTypeWriter) {
                    mActivity.scrollMyListViewToBottom();
                }
//               Log.d("MyTag","is milis to finish()" + millisUntilFinished /*(mText.length() >= mIndex && millisUntilFinished < 595000)*/);
//                Log.d("MyTag","lenth() = " + mText.length() + "index = " + mIndex);

                if (mActivity.stopTypeWriter){
                    moveTypeWriterToNext();
                }
            }else{
              //  Log.d("MyTag","String.valueOf(mText).contains(\\n)" + String.valueOf(mText).contains("\\n") /*(mText.length() >= mIndex && millisUntilFinished < 595000)*/);
                if(millisUntilFinished < 595000 || String.valueOf(mText).contains("\n")){
                    //Log.e("MyTag", "onTypingComplete =" +onTypingComplete);
                    moveTypeWriterToNext();
                }
            }
        }
        private void moveTypeWriterToNext(){
            if(onTypingComplete != null){
                onTypingComplete.onFinishTyping(this.instanceIndex);
            }else {
                mActivity.toggleSendStopButtonVisibility(true);
            }
            this.cancel();
        }
        @Override
        public void onFinish() {
            textView.setText(mText);
            if(onTypingComplete != null){
                onTypingComplete.onFinishTyping(this.instanceIndex);
            }else {
                mActivity.toggleSendStopButtonVisibility(true);
            }
        }

        public void setFullText() {
            //Log.e("SAN ", " ********** onTick ********** setFullText() called mText " + mText.length() + "<=>" + mText );
            textView.setText(mText);
        }

        public void updateMessage(String text){
            this.mText = text;
        }
    }
    public interface OnTypingComplete{
        void onFinishTyping(int index);
    }
}
