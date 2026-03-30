package com.ojassoft.vartalive.activities;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.ui.activity.BaseActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CircularNetworkImageView;
import com.ojassoft.astrosage.varta.utils.FontUtils;

public class InternetCallActivity extends BaseActivity implements View.OnClickListener, SensorEventListener {

    Context context;
    CircularNetworkImageView civInternetCallDP;
    ConstraintLayout clInternetCallTVContainer, clInternetCallParent;
    TextView tvInternetCallAstroName,tvInternetCallTimer;
    ImageView ivInternetCallSpeaker,ivInternetCallMic,ivInternetCallEnd;
    private SensorManager mSensorManager;
    private Sensor mProximity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_call);
        context = this;

        clInternetCallParent = findViewById(R.id.clInternetCallParent);
        civInternetCallDP = findViewById(R.id.civInternetCallDP);
        clInternetCallTVContainer = findViewById(R.id.clInternetCallTVContainer);
        tvInternetCallAstroName = findViewById(R.id.tvInternetCallAstroName);
        tvInternetCallTimer = findViewById(R.id.tvInternetCallTimer);
        ivInternetCallSpeaker = findViewById(R.id.ivInternetCallSpeaker);
        ivInternetCallMic = findViewById(R.id.ivInternetCallMic);
        ivInternetCallEnd = findViewById(R.id.ivInternetCallEnd);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        FontUtils.changeFont(context,tvInternetCallAstroName, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(context,tvInternetCallTimer, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        ivInternetCallSpeaker.setOnClickListener(this);
        ivInternetCallMic.setOnClickListener(this);
        ivInternetCallEnd.setOnClickListener(this);

        new Handler().postDelayed(this::connectCall,3000);

    }

    private void connectCall(){
        animateViews(false);
        new Handler().postDelayed(() -> {
            rearrangeViews();
            animateViews(true);
        }, 300);
    }

    private void startTimer(){

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivInternetCallSpeaker:
                toggleSpeakerButton();
                break;
            case R.id.ivInternetCallMic:
                toggleMicButton();
                break;
            case R.id.ivInternetCallEnd:
                endCallButton();
                break;
        }
    }

    private void toggleSpeakerButton(){

    }

    private void toggleMicButton(){

    }

    private void endCallButton(){

    }

    private void rearrangeViews(){
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(clInternetCallParent);
        constraintSet.connect(R.id.civInternetCallDP,ConstraintSet.RIGHT,ConstraintSet.PARENT_ID,ConstraintSet.RIGHT,0);
        constraintSet.connect(R.id.civInternetCallDP,ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP,0);
        constraintSet.connect(R.id.civInternetCallDP,ConstraintSet.BOTTOM,ConstraintSet.PARENT_ID,ConstraintSet.BOTTOM,0);
        constraintSet.connect(R.id.civInternetCallDP,ConstraintSet.LEFT,ConstraintSet.PARENT_ID,ConstraintSet.LEFT,0);

        constraintSet.connect(R.id.clInternetCallTVContainer,ConstraintSet.RIGHT,ConstraintSet.PARENT_ID,ConstraintSet.RIGHT,0);
        constraintSet.connect(R.id.clInternetCallTVContainer,ConstraintSet.LEFT,ConstraintSet.PARENT_ID,ConstraintSet.LEFT,0);
        constraintSet.connect(R.id.clInternetCallTVContainer,ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP, CUtils.convertDpToPx(context,40));

        constraintSet.applyTo(clInternetCallParent);
    }

    private void animateViews(boolean show){
        if (show){
            clInternetCallTVContainer.animate().alpha(1f).setDuration(300);
            civInternetCallDP.animate().alpha(1f).setDuration(300);
        } else {
            clInternetCallTVContainer.animate().alpha(0f).setDuration(300);
            civInternetCallDP.animate().alpha(0f).setDuration(300);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float distance = event.values[0];
        if (distance < 5.0){
            findViewById(R.id.viewInternetCallBlackScreen).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.viewInternetCallBlackScreen).setVisibility(View.GONE);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

}