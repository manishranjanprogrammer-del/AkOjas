package com.ojassoft.astrosage.varta.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.ojassoft.astrosage.varta.ui.activity.AINotificationChatActivity;

import java.util.concurrent.TimeUnit;

@SuppressLint("AppCompatCustomView")
public class TypeWriterNotification extends TextView {

    private static MyCountDownTimer countDownTimer;
    private  final int instaceIndex;

    public TypeWriterNotification(Context context) {
        super(context);
        instaceIndex = 0;
    }

    public TypeWriterNotification(Context context, AttributeSet attrs) {
        super(context, attrs);
        instaceIndex = 0;
    }
    public TypeWriterNotification(Context context, int instaceIndex) {
        super(context);
        this.instaceIndex = instaceIndex;
    }

    public void animateText(CharSequence text, AINotificationChatActivity mActivity) {
        setText("");
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        mActivity.toggleSendStopButtonVisibility(false);
        long millisInFuture = text.length() * 25L;
        countDownTimer = new MyCountDownTimer(millisInFuture, 25, text, 0, mActivity, this);
        countDownTimer.start();
    }
    public void animateText( CharSequence text , AINotificationChatActivity mActivity,OnTypingComplete onComplete ) {

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
    public void updateAnimateText(int position, CharSequence text, AINotificationChatActivity mActivity) {
        setText(text.subSequence(0, position));
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        mActivity.toggleSendStopButtonVisibility(false);
        long millisInFuture = 600000;
        countDownTimer = new MyCountDownTimer(millisInFuture, 25, text, position, mActivity, this);
        countDownTimer.start();
    }
    public void updateAnimatedText(CharSequence text){
        countDownTimer.updateMessage(text);
    }

    public static class MyCountDownTimer extends CountDownTimer {
        private  CharSequence mText;
        private int mIndex;
        private final AINotificationChatActivity mActivity;
        private final TextView textView;
        private final int instanceIndex;
        private  final OnTypingComplete onTypingComplete;

        public MyCountDownTimer ( long millisInFuture , long countDownInterval ,  CharSequence mText,int mIndex,AINotificationChatActivity mActivity,TextView textView) {
            super(millisInFuture, countDownInterval);
            this.mText=mText;
            this.mIndex=mIndex;
            this.mActivity=mActivity;
            this.textView=textView;
            this.instanceIndex =0;
            this.onTypingComplete = null;
        }

        public MyCountDownTimer ( long millisInFuture , long countDownInterval ,  CharSequence mText,int mIndex,AINotificationChatActivity mActivity,TextView textView,int instanceIndex,OnTypingComplete onComplete) {
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
            if (mIndex < mText.length()) {
                try {
                    if (mText.charAt(mIndex) == '\n') {
                        moveTypeWriterToNext();
                    }
                }catch (Exception ignore){}
                String message = mText.subSequence(0, ++mIndex).toString().replaceAll("\\\\n","<br>");
                textView.setText(Html.fromHtml(message));
                mActivity.mIndexTypeWriter = mIndex;
                //for scroll with typing
                if (mActivity.scrollWhileTypeWriter) {
                    final float y = mActivity.messagesListView.getBottom();
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

        public void updateMessage(CharSequence text){
            this.mText = text;
        }
    }
    public interface OnTypingComplete{
        void onFinishTyping(int index);
    }
}