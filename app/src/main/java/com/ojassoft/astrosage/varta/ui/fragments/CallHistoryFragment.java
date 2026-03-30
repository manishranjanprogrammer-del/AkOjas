package com.ojassoft.astrosage.varta.ui.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.networkcall.RetrofitResponses;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.adapters.CallHistoryAdapter;
import com.ojassoft.astrosage.varta.model.CallHistoryBean;
import com.ojassoft.astrosage.varta.service.Loginservice;
import com.ojassoft.astrosage.varta.ui.activity.ConsultantHistoryActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.CONSULT_HISTORY_CALL;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CALL;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CHAT;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallHistoryFragment extends Fragment implements  RetrofitResponses {
    RecyclerView recyclerView;
    LinearLayout linearLayout;
    FrameLayout frCallHistory;
    TextView linkTextView,txtViewNoDataFound;
    CustomProgressDialog pdFrag;
    Activity currentActivity;
    private final int DATA_CALL = 2;
    ArrayList<CallHistoryBean> arrayList = new ArrayList<>();
    CallHistoryAdapter callHistoryAdapter;
    CompoundButton.OnCheckedChangeListener swRecordingEnableDisableListeners;
    Dialog dialog;
    private AudioManager audioManager;
    private AudioFocusRequest audioFocusRequest;
    AudioManager.OnAudioFocusChangeListener afChangeListener;
    boolean isRecordingEnableOrDisable;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.call_history_frag_layout, container, false);
        linearLayout = view.findViewById(R.id.no_item_available);
        frCallHistory = view.findViewById(R.id.frCallHistory);

        linkTextView = view.findViewById(R.id.link_tv);
        txtViewNoDataFound = view.findViewById(R.id.txtViewNoDataFound);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        LANGUAGE_CODE = AstrosageKundliApplication.getAppContext().getLanguageCode();
        txtViewNoDataFound.setTypeface(CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.medium));
        linkTextView.setTypeface(CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular));
        /*
        ArrayList<CallHistoryBean> arrayList = ((ConsultantHistoryActivity) getActivity()).consultantHistoryBean.getCallHistoryList();
        if (arrayList != null && arrayList.size() > 0) {
            CallHistoryAdapter callHistoryAdapter = new CallHistoryAdapter(getActivity(), arrayList);
            recyclerView.setAdapter(callHistoryAdapter);
        } else {
            showErrorMsg(false);
        }
        */

        currentActivity = getActivity();

        ArrayList<CallHistoryBean> arrayList = ((ConsultantHistoryActivity) getActivity()).consultantHistoryBean.getCallHistoryList();
        if (arrayList != null && arrayList.size() > 0) {
            displayDataCall();
        }else
        {
            getCallData();
        }

        linkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*boolean isAIChatDisplayed = com.ojassoft.astrosage.utils.CUtils.isAIChatDisplayed(getActivity());
                if (isAIChatDisplayed) {
                    CUtils.switchToConsultTab(FILTER_TYPE_CHAT, getActivity());//redirect to chat list
                } else {
                    CUtils.switchToConsultTab(FILTER_TYPE_CALL, getActivity());//redirect to AI list
                }*/

                CUtils.switchToConsultTab(FILTER_TYPE_CALL, getActivity());//redirect to AI list
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //Log.e("SAN ", " dy => " + dy);
                if (!recyclerView.canScrollVertically(1) && dy > 0) {
                    getCallData();
                }
            }
        });
        audioManager = (AudioManager) requireActivity().getSystemService(Context.AUDIO_SERVICE);
        return view;
    }




    private void getCallData() {
        //Log.e("SAN ", " CHF getCallData()" );
        //errorLogsConsultation = errorLogsConsultation + " CHF getCallData()\n";
        if (CUtils.isConnectedWithInternet(currentActivity)) {
            //showProgressBar();
            String lastId = "0";
            if (!arrayList.isEmpty())
                lastId = arrayList.get(arrayList.size() - 1).getConsultationId();

           // CUtils.getConsultationHistory(CallHistoryFragment.this, CONSULT_HISTORY_CALL, lastId, DATA_CALL);
            CUtils.getConsultationHistoryViaRetrofit(CallHistoryFragment.this, CONSULT_HISTORY_CALL, lastId, "CallHistoryFragment");
        }
    }

    public void displayDataCall(){
        arrayList = ((ConsultantHistoryActivity) getActivity()).consultantHistoryBean.getCallHistoryList();
        if (arrayList != null && arrayList.size() > 0) {
            callHistoryAdapter = new CallHistoryAdapter(getContext(), arrayList, CallHistoryFragment.this);
            recyclerView.setAdapter(callHistoryAdapter);
        } else {
            showErrorMsg(false);
        }
    }

    public void showErrorMsg(boolean isDataAvaiable) {
        if (linearLayout != null && recyclerView != null) {
            if (isDataAvaiable) {
                linearLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                linearLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }
    }

    private void showProgressBar() {

        if (pdFrag == null) {
            pdFrag = new CustomProgressDialog(getActivity());
        }

        pdFrag.setCanceledOnTouchOutside(false);
        pdFrag.setCancelable(false);
        pdFrag.show();
    }

    private void hideProgressBar() {
        try {
            if (pdFrag != null && pdFrag.isShowing())
                pdFrag.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        hideProgressBar();
        if (response.body() != null) {
            try {
                String myResponse = response.body().string();
              //Log.d("TestHistoryResponse","TestHistory==>>"+myResponse);
                parseConsulList(myResponse);
            }catch (Exception e){

            }

        }

    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        try {
            showErrorMsg(false);
            CUtils.showSnackbar(frCallHistory,getContext().getString(R.string.something_wrong_error)+" ( "+t+" )",getContext());

        }catch (Exception e){
            //
        }
        }


    private void parseConsulList(String response){
       // hideProgressBar();
        try {
            if (!TextUtils.isEmpty(response)) {

                ArrayList<CallHistoryBean> callHistoryList = new ArrayList();
                CallHistoryBean callHistoryBean;
                JSONArray consultations = null;

                JSONObject jsonObject = new JSONObject(response);

                String walletBalance = jsonObject.getString("walletbalance");
                ((ConsultantHistoryActivity) getActivity()).consultantHistoryBean.setWalletbalance(walletBalance);
                ((ConsultantHistoryActivity) getActivity()).updateWalletBalance();

                if(jsonObject.has("consultations")){
                    consultations = jsonObject.getJSONArray("consultations");

                    if(consultations != null && consultations.length()>0) {
                        for (int i = 0; i < consultations.length(); i++) {

                            callHistoryBean = new CallHistoryBean();
                            String userPhoneNo = consultations.getJSONObject(i).getString("userPhoneNo");
                            String astrologerPhoneNo = consultations.getJSONObject(i).getString("astrologerPhoneNo");
                            String astrologerName = consultations.getJSONObject(i).getString("astrologerName");
                            String consultationTime = consultations.getJSONObject(i).getString("consultationTime");
                            String callDuration = consultations.getJSONObject(i).getString("callDuration");
                            String callAmount = consultations.getJSONObject(i).getString("callAmount");
                            String astrologerImageFile = consultations.getJSONObject(i).getString("astrologerImageFile");
                            String astrologerServiceRs = consultations.getJSONObject(i).getString("astrologerServiceRs");
                            String astroWalletId = consultations.getJSONObject(i).getString("astroWalletId");
                            String urlText = consultations.getJSONObject(i).getString("urlText");
                            String consultationMode = consultations.getJSONObject(i).getString("consultationMode");
                            String callChatId = consultations.getJSONObject(i).getString("callChatId");
                            String refundStatus = consultations.getJSONObject(i).getString("refundStatus");
                            String consultationId = consultations.getJSONObject(i).getString("consultationId");
                            String recordingUrl = consultations.getJSONObject(i).getString("recordingUrl");
                            String provider = consultations.getJSONObject(i).getString("provider");
                            String aiai = consultations.getJSONObject(i).optString("aiai");
                            String durationUnitType = consultations.getJSONObject(i).optString("durationUnitType");
                            String callDurationMin = consultations.getJSONObject(i).optString("calldurationmin");


                            //Log.e("SAN ", "CHF response parse consultationId " + consultationId );

                            callHistoryBean.setUserPhoneNo(userPhoneNo);
                            callHistoryBean.setAstrologerPhoneNo(astrologerPhoneNo);
                            callHistoryBean.setAstrologerName(astrologerName);
                            callHistoryBean.setConsultationTime(consultationTime);
                            callHistoryBean.setCallDuration(callDuration);
                            callHistoryBean.setCallAmount(callAmount);
                            callHistoryBean.setAstrologerImageFile(astrologerImageFile);
                            callHistoryBean.setAstrologerServiceRs(astrologerServiceRs);
                            callHistoryBean.setAstroWalletId(astroWalletId);
                            callHistoryBean.setUrlText(urlText);
                            callHistoryBean.setConsultationMode(consultationMode);
                            callHistoryBean.setCallChatId(callChatId);
                            callHistoryBean.setRefundStatus(refundStatus);
                            callHistoryBean.setConsultationId(consultationId);
                            callHistoryBean.setRecordingUrl(recordingUrl);
                            callHistoryBean.setProvider(provider);
                            callHistoryBean.setAiAstroId(aiai);
                            callHistoryBean.setDurationUnitType(durationUnitType);
                            callHistoryBean.setCallDurationMin(callDurationMin);

                            callHistoryList.add(callHistoryBean);
                        }
                    }

                    arrayList.addAll(callHistoryList);
                    //((ConsultantHistoryActivity) getActivity()).consultantHistoryBean.setCallHistoryList(callHistoryList);
                    ((ConsultantHistoryActivity) getActivity()).consultantHistoryBean.setCallHistoryList(arrayList);

                    if (callHistoryAdapter != null) {
                        callHistoryAdapter.historyRecordsUpdate(arrayList);
                    } else {
                        displayDataCall();
                    }
                } else {
                    if (arrayList != null && arrayList.size() == 0) {
                        showErrorMsg(false);
                    }
                }
            }

            if (arrayList != null && arrayList.size() == 0) {
                showErrorMsg(false);
            }
            hideProgressBar();
        } catch (Exception e) {
            //Log.e("SAN ", "CHF response parse exp " + e.toString());
            //errorLogsConsultation = errorLogsConsultation + "CHF response parse exp" + e.toString() +"\n";
            String status="";
            try {
                JSONObject jsonObject = new JSONObject(response);
                status = jsonObject.getString("status");
                if ( status.equals("100") ) {

                    LocalBroadcastManager.getInstance(currentActivity).registerReceiver(mReceiverBackgroundLoginServiceCall
                            , new IntentFilter(CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));

                    startBackgroundLoginService();
                }
            } catch (Exception exception){
                //Log.e("SAN CHA response ", " pasrse exp " + exception.toString() );
                //Log.e("SAN ", "CHA gift exception 100 exp 2 " + e.toString() );
                //errorLogsConsultation = errorLogsConsultation + "CHA gift exception 100 exp 2 " + e.toString()+"\n";
            }

            if ( !status.equals("100") ) {
                hideProgressBar();
                showErrorMsg(false);
            }
        }
    }

    private void startBackgroundLoginService() {
        try {
            if (CUtils.getUserLoginStatus(currentActivity)) {
                CUtils.fcmAnalyticsEvents("bg_login_from_consult_history",CGlobalVariables.VARTA_BACKGROUND_LOGIN,"");

                Intent intent = new Intent(currentActivity, Loginservice.class);
                currentActivity.startService(intent);
            }
        } catch (Exception e) {}
    }


    private final BroadcastReceiver mReceiverBackgroundLoginServiceCall = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //errorLogsConsultation = errorLogsConsultation + "CHA gift mReceiverBackgroundLoginService \n";
            //Log.e("SAN ", " Broadcast received call ");
            String status = intent.getStringExtra("status");
            if (status.equals(CGlobalVariables.SUCCESS)) {
                getCallData();
            } else {
                showErrorMsg(false);
            }

            if (mReceiverBackgroundLoginServiceCall != null) {
                LocalBroadcastManager.getInstance(currentActivity).unregisterReceiver(mReceiverBackgroundLoginServiceCall);
            }

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiverBackgroundLoginServiceCall != null) {
            LocalBroadcastManager.getInstance(currentActivity).unregisterReceiver(mReceiverBackgroundLoginServiceCall);
        }
    }

    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    SeekBar seekBar;

    TextView startTime, fullTime;
    Runnable updateSeekBarTime = new Runnable() {
        public void run() {
            if (mediaPlayer.isPlaying()) {

                seekBar.setProgress(mediaPlayer.getCurrentPosition());

                String currentPositionText = formatTime(mediaPlayer.getCurrentPosition());
                String durationText = formatTime(mediaPlayer.getDuration());
                startTime.setText(currentPositionText);
                fullTime.setText(durationText);
                handler.postDelayed(this, 100);
            }
        }
    };

    private String formatTime(int timeInMillis) {
        int seconds = (timeInMillis / 1000) % 60;
        int minutes = (timeInMillis / (1000 * 60)) % 60;
        //int hours = (timeInMillis / (1000 * 60 * 60)) % 24;
        //return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        return String.format("%02d:%02d", minutes, seconds);
    }

    public void playAudio(String channelId,String astroName,String provider, String url) {

        //Log.e("SAN ", " CHF playAudio() " );

        try {


            if ( dialog != null && dialog.isShowing() ){
                return;
            }

            if ( mediaPlayer != null && mediaPlayer.isPlaying()){
                return;
            }

            if ( dialog == null ) {
                dialog = new Dialog(currentActivity);
            }


            dialog.setContentView(R.layout.play_audio_dialog);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setGravity(Gravity.BOTTOM);

            ImageView buttonPlayPause;

            String audioUrl = url;

            if ( mediaPlayer == null ){
                afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
                    @Override
                    public void onAudioFocusChange(int focusChange) {
                        switch (focusChange) {
                            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                                // Temporary loss of focus (like an incoming call)
                                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                                    mediaPlayer.pause();
                                }
                                break;
                            case AudioManager.AUDIOFOCUS_LOSS:
                                // Permanent loss of focus, stop playback
                                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                                    mediaPlayer.stop();
                                }
                                break;
                        }
                    }
                };

                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    audioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                            .setAudioAttributes(audioAttributes)
                            .setOnAudioFocusChangeListener(afChangeListener)
                            .setAcceptsDelayedFocusGain(true)
                            .setWillPauseWhenDucked(true)
                            .build();
                    audioManager.requestAudioFocus(audioFocusRequest);
                }
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioAttributes(audioAttributes);
            }
            buttonPlayPause = dialog.findViewById(R.id.button_play_pause);
            seekBar = dialog.findViewById(R.id.seek_bar);
            startTime = dialog.findViewById(R.id.current_time_text);
            fullTime = dialog.findViewById(R.id.total_time_text);
            ImageView loaderImage = dialog.findViewById(R.id.loader_image);

            TextView txtViewCallRecording = dialog.findViewById(R.id.txtViewCallRecording);

            FontUtils.changeFont(currentActivity, txtViewCallRecording, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

            txtViewCallRecording.setText( currentActivity.getResources().getString(R.string.call_recording) +" \n"+ astroName);

            buttonPlayPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        buttonPlayPause.setImageResource(R.drawable.ic_play);
                    } else {
                        mediaPlayer.start();
                        buttonPlayPause.setImageResource(R.drawable.ic_pause);
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                        handler.postDelayed(updateSeekBarTime, 100);
                    }
                }
            });

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        mediaPlayer.seekTo(progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}

            });

            try {

                Glide.with(getContext().getApplicationContext()).load(R.drawable.loader).into(loaderImage);

                if(provider.equals(CGlobalVariables.EXOTEL_PROVIDE)){
                    prepareMediaSource(getRecordingParams(audioUrl));
                }else if(url.startsWith("gs://")){
                    playGcsAudio(getAiCallRecordingParams(channelId,audioUrl));
                    Log.d("SANNN", "playAudio: " + url);
                }else {
                    mediaPlayer.setDataSource(audioUrl);
                    mediaPlayer.prepareAsync();
                }
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {

                        loaderImage.setVisibility(View.GONE);
                        seekBar.setMax(mediaPlayer.getDuration());
                        startTime.setText(formatTime(mediaPlayer.getCurrentPosition()));
                        fullTime.setText(formatTime(mediaPlayer.getDuration()));

                        mediaPlayer.start();
                        buttonPlayPause.setImageResource(R.drawable.ic_pause);
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                        handler.postDelayed(updateSeekBarTime, 100);

                    }
                });

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        buttonPlayPause.setImageResource(R.drawable.ic_play);
                        seekBar.setProgress(0);
                    }
                });

            } catch (Exception e) {
                //Log.e("SAN ", " CHF playAudio() mediaPlayer sync exp " + e.toString() );
            }

            ImageView close = dialog.findViewById(R.id.button_close);

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v1) {
                    dialog.dismiss();
                    clearPlayer();
                }
            });

            dialog.setCancelable(false);
            dialog.show();

        } catch (Exception e) {
            //Log.e("SAN ", " CHF playAudio() exp " + e.toString() );
        }

    }

    private void showFailedPlaying(){
        Dialog alertDialog =  new Dialog(currentActivity);
        alertDialog.setContentView(R.layout.player_error_layout);
        alertDialog.getWindow().setGravity(Gravity.CENTER);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        alertDialog.show();
        alertDialog.findViewById(R.id.tv_ok).setOnClickListener((v)->alertDialog.dismiss());
    }

    private void prepareMediaSource(Map<String,String> params){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        //Log.d("MyDataSource","params = "+ params);

        executor.execute(() -> {
            byte[] buffer = new byte[4096];
            File audioFile = new File(requireActivity().getCacheDir(), "temp_audio.mp3");
            RetrofitClient.getInstance().create(ApiList.class).getPlayRecordingUrl(params).enqueue(
                    new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                if (response.isSuccessful() && response.body() != null) {
                                    InputStream stream = response.body().byteStream();
                                    long fileSize = 0;
                                    int size;
                                    OutputStream outputStream = new FileOutputStream(audioFile);
                                    while ((size = stream.read(buffer)) != -1) {
                                        outputStream.write(buffer, 0, size);
                                        fileSize += size;
                                    }
                                    outputStream.flush();
                                    playAudio(audioFile);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
            );
        });
    }

    private void playGcsAudio(Map<String,String> params){
        Call<ResponseBody> call = RetrofitClient.getInstance().create(ApiList.class).getAiCallRecordingUrl(params);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                 if(response.isSuccessful()){
                     Log.d("SANNN ->", "response.isSuccessful()" );
                     byte[] buffer = new byte[4096];
                     File audioFile = new File(requireActivity().getCacheDir(), "temp_audio.mp3");
                     if (response.isSuccessful() && response.body() != null) {
                         InputStream stream = response.body().byteStream();
                         int size;
                         OutputStream outputStream = new FileOutputStream(audioFile);
                         while ((size = stream.read(buffer)) != -1) {
                             outputStream.write(buffer, 0, size);
                         }
                         outputStream.flush();
                         playAudio(audioFile);
                     }

                 }else{
                     Log.d("SANNN ->", "response.failed()" );
                     if (dialog != null && dialog.isShowing()) {
                         dialog.dismiss();
                     }
                     clearPlayer();
                     showFailedPlaying();
                 }
                }catch(Exception e){
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    clearPlayer();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    public void clearPlayer(){
        handler.removeCallbacks(updateSeekBarTime);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && audioManager != null && audioFocusRequest != null) {
            audioManager.abandonAudioFocusRequest(audioFocusRequest);
        } else if (audioManager != null && afChangeListener != null) {
            audioManager.abandonAudioFocus(afChangeListener);
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        clearPlayer();
        audioManager = null;
    }


    private Map<String,String> getRecordingParams(String audioUrl){
        Map<String,String> params = new HashMap<>();
        params.put("url",audioUrl);
        params.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(currentActivity));
        params.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(currentActivity));
        params.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        params.put(CGlobalVariables.USER_PHONE_NO, CUtils.getUserID(currentActivity));
        params.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(currentActivity));
        return params;
    }

    private Map<String,String> getAiCallRecordingParams(String channelId,String audioUrl){
        Map<String,String> params = new HashMap<>();
        params.put("recordingurl",audioUrl);
        params.put("channelid",channelId);
        params.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(currentActivity));
        params.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(currentActivity));
        params.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        params.put(CGlobalVariables.USER_PHONE_NO, CUtils.getUserID(currentActivity));
        params.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(currentActivity));
        return params;
    }

    private void playAudio(File audioFile){
        try {
            mediaPlayer.setDataSource(audioFile.getAbsolutePath());
            mediaPlayer.prepareAsync();
        }catch (Exception e){
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }

    }

}
