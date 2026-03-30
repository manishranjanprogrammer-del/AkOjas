package com.ojassoft.astrosage.ui.fragments.horoscope;

import android.view.View;

/**
 * text to speech callback
 */
public interface TtsCallbackListener {

    void resetSpeakBtn(View view);

    void disablePlayButton(View view);

    void enablePlayButton(View view);

}
