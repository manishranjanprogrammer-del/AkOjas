package com.ojassoft.astrosage.ui.act;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.fragments.horoscope.TtsCallbackListener;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.Locale;

/**
 * Created by Ankit on 26/3/2019
 */

/**
 * SUDO CODE FOR TTS
 * 1.TTS will initialize on DetailedHoroscope activity
 * 2.After successful initializing it give callback on onInit() method in DetailedHoroscope
 * 3.In Case of error disable play button on all fragments
 * 4.TtsCallbacklistener to provide call back to the fragments
 * 5.OnDone and OnError text speech update particular fragment using TtsCallbacklistener
 * 6.CUtils Have all common methods used in TTS, Share , copy and to provide selected language code.
 * 7.DailyHoroscopeFragment,MonthlyHoroscopeFragment,WeeklyHoroscopeFragment,WeeklyLoveHoroscopeFragment and YearlyHoroscope
 */
public class BaseTtsActivity extends BaseInputActivity implements TextToSpeech.OnInitListener {

    public static TextToSpeech mTextToSpeech;
    public static int TTS_CHAR_LIMIT = 3999;
    private View view;
    public static boolean isTextToSpeechAvailable;
    public static TtsCallbackListener mTtsCallbackListener;
    View playedView;

    public static void setmTtsCallbackListener(TtsCallbackListener mTtsCallbackListener) {
        BaseTtsActivity.mTtsCallbackListener = mTtsCallbackListener;
    }

    public void setPlayedView(View playedView) {
        this.playedView = playedView;
    }

    public BaseTtsActivity(int titleRes) {
        super(titleRes);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = findViewById(android.R.id.content);
        initializeTexttoSpeech();
    }


    /**
     * initialize Text to Speech
     */
    private void initializeTexttoSpeech() {
        if (mTextToSpeech == null)
            mTextToSpeech = new TextToSpeech(this, this);
    }

    @SuppressLint("NewApi")
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = 0;

            if (mTextToSpeech == null) return;
            Log.i("TTS", " Is AvailableLanguages  " + mTextToSpeech.isLanguageAvailable(new Locale(CUtils.getTtsLanguageCode(LANGUAGE_CODE))));
//            Log.i("TTS", " AvailableLanguages "  + mTextToSpeech.getAvailableLanguages().toString());

            // if language available on device return 0 on success
            if (result == mTextToSpeech.isLanguageAvailable(new Locale(CUtils.getTtsLanguageCode(LANGUAGE_CODE)))) {
                result = mTextToSpeech.setLanguage(new Locale(CUtils.getTtsLanguageCode(LANGUAGE_CODE)));
                if (mTtsCallbackListener != null)
                    mTtsCallbackListener.enablePlayButton(playedView);
                isTextToSpeechAvailable = true;
            } else {
                disableTtsButton();
            }

            if (result == TextToSpeech.LANG_MISSING_DATA) {
                String text = "This Language missing data";
                Log.e("TTS", text);
                //if condition to handle double callback
                disableTtsButton();
                CUtils.showAlertDialog(this, LANGUAGE_CODE);

            } else if (result == TextToSpeech.LANG_NOT_SUPPORTED) {
                String text = "This Language is not supported";
                Log.e("TTS", text);
                disableTtsButton();
            } else {

                mTextToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String utteranceId) {

                    }

                    @Override
                    public void onDone(String utteranceId) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mTtsCallbackListener != null)
                                    mTtsCallbackListener.resetSpeakBtn(playedView);
                            }
                        });
                    }

                    //Called when an error has occurred during processing.
                    @Override
                    public void onError(String utteranceId) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                CUtils.showSnakbar(view, getString(R.string.language_not_support));
                                if (mTtsCallbackListener != null) {
                                    mTtsCallbackListener.resetSpeakBtn(playedView);
                                    mTtsCallbackListener.disablePlayButton(playedView);
                                }
                            }
                        });
                    }
                });
            }

        } else {
            disableTtsButton();
            Log.e("TTS", "Initilization Failed!");
        }
    }


    @Override
    protected void onPause() {
        if (mTextToSpeech != null && mTextToSpeech.isSpeaking()) {
            mTextToSpeech.stop();
            mTextToSpeech.shutdown();
            if (mTtsCallbackListener != null)
                mTtsCallbackListener.resetSpeakBtn(playedView);
        }
        mTextToSpeech = null;
        super.onPause();
        try {
            /*CUtils.removeAdvertisement(DetailedHoroscope.this,
                    (LinearLayout) findViewById(R.id.advLayout));*/
        } catch (Exception e) {

        }
    }

    @Override
    protected void onDestroy() {
        // Don't forget to shutdown tts!
        if (mTextToSpeech != null) {
            mTextToSpeech.stop();
            mTextToSpeech.shutdown();
        }
        mTextToSpeech = null;
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        initializeTexttoSpeech();
        super.onResume();
    }

    private void disableTtsButton() {
        if (mTtsCallbackListener != null)
            mTtsCallbackListener.disablePlayButton(playedView);
        isTextToSpeechAvailable = false;
    }

    protected void updatePlayButton() {
        if (mTtsCallbackListener != null) {
            if (isTextToSpeechAvailable) {
                mTtsCallbackListener.enablePlayButton(playedView);
            } else {
                mTtsCallbackListener.disablePlayButton(playedView);
            }
        }

        if (mTtsCallbackListener != null) {
            mTtsCallbackListener.resetSpeakBtn(playedView);
        }
    }
}
