package com.ojassoft.astrosage.varta.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.ojassoft.astrosage.varta.ui.MiniChatWindow;

import java.util.concurrent.TimeUnit;

@SuppressLint("AppCompatCustomView")
public class TypeWritterKundali extends TextView {

    private static TypeWritterKundali.MyCountDownTimer countDownTimer;
    private  final int instaceIndex ;
    private static CountDownTimer timer;

    public TypeWritterKundali(Context context) {
        super(context);
        instaceIndex = 0;
    }

    public TypeWritterKundali(Context context, AttributeSet attrs) {
        super(context, attrs);
        instaceIndex = 0;
    }
    public TypeWritterKundali(Context context, int instaceIndex) {
        super(context);
        this.instaceIndex = instaceIndex;
    }

    public void animateText(String text , MiniChatWindow mActivity, TypeWritterKundali.OnTypingComplete onComplete ) {

        setText("");
        if(countDownTimer!=null) {
            //countDownTimer.onFinish ();
            //SAN Exp comment
            // countDownTimer.setFullText ();
            countDownTimer.cancel ();
        }

        mActivity.toggleSendStopButtonVisibility(false);
        long millisInFuture = TimeUnit.MINUTES.toMillis(10);
        countDownTimer=new TypeWritterKundali.MyCountDownTimer(millisInFuture, 25,text,0,mActivity,this,instaceIndex,onComplete);
        countDownTimer.start ();
    }

    public void updateAnimatedText(String text){
        countDownTimer.updateMessage(text);
    }

    public static class MyCountDownTimer extends CountDownTimer {
        private String mText;
        private int mIndex;
        private final MiniChatWindow mActivity;
        private final TextView textView;
        private final int instanceIndex;
        private  final TypeWritterKundali.OnTypingComplete onTypingComplete;

        public MyCountDownTimer ( long millisInFuture , long countDownInterval ,  String mText,int mIndex,MiniChatWindow mActivity,TextView textView) {
            super(millisInFuture, countDownInterval);
            this.mText=mText;
            this.mIndex=mIndex;
            this.mActivity=mActivity;
            this.textView=textView;
            this.instanceIndex =0;
            this.onTypingComplete = null;
        }

        public MyCountDownTimer (long millisInFuture , long countDownInterval , String mText, int mIndex, MiniChatWindow mActivity, TextView textView, int instanceIndex, TypeWritterKundali.OnTypingComplete onComplete) {
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
//            Log.e("KundliAIChatTesting", "TypeWriter onTick: timer "+millisUntilFinished );

            if (mIndex < mText.length()) {
                try {
                    if (mText.contains("\n\n") && mText.indexOf("\n\n") <= mIndex) {
                        moveTypeWriterToNext();
                    }
                }catch (Exception ignore){}
                String message = mText.substring(0, ++mIndex).replaceAll("\\\\n","<br>");
//                Log.e("KundliAIChatTesting", "TypeWriter onTick: Message "+message );
                textView.setText(Html.fromHtml(message));
                mActivity.mIndexTypeWriter = mIndex;
                //for scroll with typing
                if (mActivity.scrollWhileTypeWriter) {
                    final float y = mActivity.messagesListView.getBottom();
                    mActivity.recycleNestedScrollView.fling(0);
                    mActivity.recycleNestedScrollView.smoothScrollTo(0, (int) y);
                }
//               Log.d("MyTag","is milis to finish()" + millisUntilFinished /*(mText.length() >= mIndex && millisUntilFinished < 595000)*/);
//                Log.d("MyTag","lenth() = " + mText.length() + "index = " + mIndex);

                if (mActivity.stopTypeWriter){
                    moveTypeWriterToNext();
                }
            }else{
                //  Log.d("MyTag","String.valueOf(mText).contains(\\n)" + String.valueOf(mText).contains("\\n") /*(mText.length() >= mIndex && millisUntilFinished < 595000)*/);
                if(millisUntilFinished < 595000 || String.valueOf(mText).contains("\n") || this.instanceIndex > 0){
                    //Log.e("KundliAIChatTesting", "onTypingComplete =" +onTypingComplete);
                    moveTypeWriterToNext();
                }
            }
        }
        private void moveTypeWriterToNext(){
//            Log.e("KundliAIChatTesting", "moveTypeWriterToNext: "+onTypingComplete);
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

        public void updateMessage(String text){
//            Log.e("KundliAIChatTesting", "updateMessage:with countdown  "+text );
            mText = text;
        }
    }
    public interface OnTypingComplete{
        void onFinishTyping(int index);
    }
}