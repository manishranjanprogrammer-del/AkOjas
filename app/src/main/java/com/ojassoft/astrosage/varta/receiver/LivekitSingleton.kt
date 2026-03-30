package com.ojassoft.astrosage.varta.receiver

import android.content.Context
import android.util.Log
import io.livekit.android.AudioOptions
import io.livekit.android.AudioType
import io.livekit.android.LiveKit
import io.livekit.android.LiveKitOverrides
import io.livekit.android.room.Room

object LivekitSingleton {
    private var INSTANCE: Room? = null

    fun getInstance(context: Context): Room {
        if(INSTANCE == null){
            Log.d("testAiCAll", "INSTANCE ==  null")
            INSTANCE = LiveKit.create(
                appContext = context.applicationContext,
                overrides = LiveKitOverrides(
                    audioOptions = AudioOptions(
                        audioOutputType = AudioType.CallAudioType()
                    )
                )
            )
        }
        return INSTANCE!!
    }

    fun release(){
        try {
            INSTANCE?.disconnect()
            INSTANCE?.release()
        }catch(_:Exception){}
        INSTANCE = null
    }
}