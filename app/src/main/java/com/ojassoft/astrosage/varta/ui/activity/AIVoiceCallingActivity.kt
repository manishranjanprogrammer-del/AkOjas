package com.ojassoft.astrosage.varta.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.PictureInPictureParams
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Point
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioAttributes
import android.media.AudioDeviceCallback
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.media.SoundPool
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.util.Log
import android.util.Rational
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isGone
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.ojassoft.astrosage.BuildConfig
import com.ojassoft.astrosage.R
import com.ojassoft.astrosage.networkcall.ApiList
import com.ojassoft.astrosage.networkcall.RetrofitClient
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication
import com.ojassoft.astrosage.varta.dialog.FeedbackDialog
import com.ojassoft.astrosage.varta.dialog.PaymentFailDialog
import com.ojassoft.astrosage.varta.dialog.PaymentSucessfulDialog
import com.ojassoft.astrosage.varta.dialog.QuickRechargeBottomSheet
import com.ojassoft.astrosage.varta.dialog.RechargeSuggestionBottomSheet
import com.ojassoft.astrosage.varta.model.UserProfileData
import com.ojassoft.astrosage.varta.model.WalletAmountBean
import com.ojassoft.astrosage.varta.receiver.LivekitSingleton
import com.ojassoft.astrosage.varta.service.AIVoiceCallingService
import com.ojassoft.astrosage.varta.service.AiVoiceCallObserverService
import com.ojassoft.astrosage.varta.service.PreFetchDataservice
import com.ojassoft.astrosage.varta.ui.fragments.RechargePopUpAfterFreeChat
import com.ojassoft.astrosage.varta.utils.CGlobalVariables
import com.ojassoft.astrosage.varta.utils.CUtils
import com.ojassoft.astrosage.varta.utils.ChatUtils
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog
import com.ojassoft.astrosage.varta.utils.FontUtils
import com.twilio.audioswitch.AudioDevice
import io.livekit.android.ConnectOptions
import io.livekit.android.annotations.Beta
import io.livekit.android.events.RoomEvent
import io.livekit.android.events.collect
import io.livekit.android.room.Room
import io.livekit.android.room.participant.ConnectionQuality
import io.livekit.android.room.participant.Participant
import io.livekit.android.room.participant.RemoteParticipant
import io.livekit.android.room.track.RemoteAudioTrack
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.max

class AIVoiceCallingActivity: AppCompatActivity(), SensorEventListener {

    private var room: Room? = null
    var callSId: String? = null
    var token: String? = null
    var livekitToken: String? = null
    var astrologerName: String? = null
    var astrologerProfileUrl: String? = null
    var serverUrl: String? = null
    var callDuration: String? = null
    var astrologerId: String? = null
    var aiAstrologerId: String? = null
    var timeRemaining: String? = null
    var chatJsonObject: String?=null
    var isFreeCall = false
    var intentNotificationId: Int = -1
    var isCallConnected: Boolean = false
    var offerTypeDuringInitCall: String?=null
    var isDisconnectBeforeConnect: Boolean = false
    var MIC_STATUS: Boolean = false
    var isFromNotification = false
    var END_CALL_DATA: Boolean = false
    var LANGUAGE_CODE = CGlobalVariables.ENGLISH
    private var isConnectivityDisconnected = false
    var pd: CustomProgressDialog? = null
    var readRef: DatabaseReference? = null
    var endCallValueEventListener: ValueEventListener? = null
    private var valueEventListenerNetConnection: ValueEventListener? = null
    private var countDownTimer: CountDownTimer? = null
    lateinit var tvInternetCallTimer: TextView
    lateinit var ivCallEnd: ImageView
    lateinit var ivSpeaker: ImageView
    lateinit var ivCallMic: ImageView
    lateinit var ivCallAgain: ImageView
    lateinit var ivChatIcon: ImageView
    lateinit var ivCancelCall: ImageView
    lateinit var ivConnectingImg: ImageView
    lateinit var tvAstroName: TextView
    lateinit var ivAstroImg: ImageView
    lateinit var tvCallAgain: TextView
    lateinit var tvChat: TextView
    lateinit var tvCancel: TextView
    lateinit var guideview: View
    lateinit var audioDeviceListView: ListView
    private lateinit var mSensorManager: SensorManager
    private var mProximity: Sensor? = null
    private lateinit var parentLayout: ConstraintLayout
    private var soundPool: SoundPool? = null
    private lateinit var audioManager: AudioManager
    private var audioDeviceCallback: AudioDeviceCallback? = null
    private var ringingSoundId: Int = 0
    private var hangupSoundId: Int = 0
    private var alterToneId: Int = 0
    private var beepSound: Int = 0
    lateinit var controlLayout: ConstraintLayout
    lateinit var paddindView: View
    lateinit var topPadding: View
    lateinit var llProfile: LinearLayout
    lateinit var tvUserName: TextView
    lateinit var ivUserProfImg: ImageView
    lateinit var tvLowBal: TextView
    lateinit var tvLowBalDesc: TextView
    lateinit var btnRecharge: Button
    lateinit var lowBalLayout: View
    lateinit var linLyoutPoorConnection: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enable edge-to-edge mode with sensible defaults (from AndroidX EdgeToEdge library)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ai_voice_calling)
        applyEdgeToEdgeInsets(this)
        initViews()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        room = LivekitSingleton.getInstance(this)
        Log.d("testAiCAll", "onCreate: ")
        LANGUAGE_CODE = (application as AstrosageKundliApplication).languageCode
        getDataFromIntent(intent)
        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        startRinging()
        initData()
        room = LivekitSingleton.getInstance(this)
        requestPermissions()
        // Handle back press
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                AstrosageKundliApplication.IS_AGORA_CALL_RUNNING = false
                CGlobalVariables.callTimerTime = 0
                if (isCallDisconnected) {
                    finish()
                } else {
                    enterPIPMode()
                }
            }
        })
        CUtils.hideMyKeyboard(this)
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        LocalBroadcastManager.getInstance(this).registerReceiver(finishReceiver, IntentFilter(CGlobalVariables.FINISH_CALL_ACTIVITY_ACTION))
        LocalBroadcastManager.getInstance(this).registerReceiver(micOnOffReceiver, IntentFilter(CGlobalVariables.MIC_ON_OFF_ACTIVITY_ACTION))
        //for audio device change
        audioManager.registerAudioDeviceCallback(audioDeviceCallback, null)
    }

    fun applyEdgeToEdgeInsets(activity: Activity) {
        try {
            // Tell the system that this Activity will handle fitting system windows manually
            // (status bar, nav bar, keyboard) instead of letting the system do it

            WindowCompat.setDecorFitsSystemWindows(activity.window, false) // enable edge-to-edge

            ViewCompat.setOnApplyWindowInsetsListener(
                activity.window.decorView
            ) { v: View, insets: WindowInsetsCompat ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    // Android 11+ (API 30+)
                    val navBars =
                        insets.getInsets(WindowInsetsCompat.Type.navigationBars())
                    val ime = insets.getInsets(WindowInsetsCompat.Type.ime())

                    // Pick whichever is larger (IME or nav bar) for bottom
                    val bottomInset =
                        max(navBars.bottom.toDouble(), ime.bottom.toDouble()).toInt()

                    v.setPadding(
                        navBars.left,  // keep nav bar left
                        0,  // ignore status bar (top = 0)
                        navBars.right,  // keep nav bar right
                        bottomInset // bottom inset
                    )
                } else {
                    // Fallback for API < 30
                    val systemInsets = insets.toWindowInsets()
                    if (systemInsets != null) {
                        val bottomInset = systemInsets.systemWindowInsetBottom

                        v.setPadding(
                            systemInsets.systemWindowInsetLeft,
                            0,  // ignore status bar
                            systemInsets.systemWindowInsetRight,
                            bottomInset
                        )
                    }
                }
                insets
            }
        } catch (e: java.lang.Exception) {
            //
        }
    }

    private fun initViews() {
        tvInternetCallTimer = findViewById(R.id.tv_call_duration)
        ivCallEnd = findViewById(R.id.ivCallEnd)
        ivSpeaker = findViewById(R.id.ivCallSpeaker)
        ivCallMic = findViewById(R.id.ivCallMic)
        ivAstroImg = findViewById(R.id.ivAstrologerImage)
        tvAstroName = findViewById(R.id.tvAstrologerName)
        guideview = findViewById(R.id.guideView)
        ivConnectingImg = findViewById(R.id.ivConnecting)
        ivCallAgain = findViewById(R.id.call_again)
        ivChatIcon = findViewById(R.id.iv_chat_icon)
        ivCancelCall = findViewById(R.id.iv_cancel_call)
        audioDeviceListView = findViewById(R.id.audio_device_list)
        parentLayout = findViewById(R.id.layoutCallParent)
        tvCallAgain = findViewById(R.id.tv_call_again)
        tvChat = findViewById(R.id.tv_chat)
        tvCancel = findViewById(R.id.tv_cancel)
        callAgainLayout = findViewById(R.id.call_again_layout)
        ivCallEnd.setBackgroundResource(R.drawable.bg_disable_call_gray)
        ivCallEnd.isEnabled = false
        controlLayout = findViewById(R.id.consLayoutBottom)
        paddindView = findViewById(R.id.paddingView)
        topPadding = findViewById(R.id.top_padding)
        tvLowBal = findViewById(R.id.tv_low_bal)
        tvLowBalDesc = findViewById(R.id.tv_60_sec)
        btnRecharge = findViewById(R.id.btn_recharge)
        lowBalLayout = findViewById(R.id.recharge_layout)
        linLyoutPoorConnection = findViewById(R.id.linLyoutPoorConnection)
        llProfile = findViewById(R.id.llUserProfile)
        tvUserName = findViewById(R.id.tv_user_name)
        ivUserProfImg = findViewById(R.id.iv_user_image)
        //end call button calick
        ivCallEnd.setOnClickListener {

            if (isCallConnected) {
                voiceCallCompleted(CGlobalVariables.COMPLETED, CGlobalVariables.USER_ENDED, true)
            }else{
                CUtils.fcmAnalyticsEvents(
                    CGlobalVariables.END_CALL_CLICK_BEFORE_AI_ASTRO_CONNECT,
                    AstrosageKundliApplication.currentEventType,
                    ""
                )
                isDisconnectBeforeConnect = true
                endRemarks = CGlobalVariables.USER_ENDED
                showCallFailed()
            }
        }
        //mute button
        ivCallMic.isActivated = CGlobalVariables.MIC_MUTE_STATUS
        if(ivCallMic.isActivated) ivCallMic.setColorFilter(ContextCompat.getColor(this, R.color.white))
        ivCallMic.setOnClickListener { view -> toggleMicButton(view) }

        //speaker button calick
        ivSpeaker.isActivated = true
        ivSpeaker.setOnClickListener { view ->
            speakerSwitch()
        }
        FontUtils.changeFont(this, tvAstroName, CGlobalVariables.FONTS_OPEN_SANS_BOLD)
        FontUtils.changeFont(this, tvInternetCallTimer, CGlobalVariables.FONTS_OPEN_SANS_LIGHT)
        FontUtils.changeFont(this, tvLowBal, CGlobalVariables.FONTS_OPEN_SANS_BOLD)
        FontUtils.changeFont(this, tvLowBalDesc, CGlobalVariables.FONTS_OPEN_SANS_REGULAR)
        FontUtils.changeFont(this, btnRecharge, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD)
        val startAnimation = AnimationUtils.loadAnimation(this, R.anim.blinking_animation)
        tvLowBal.startAnimation(startAnimation)
        tvInternetCallTimer.text = getString(R.string.calling)
        //ivConnectingImg.visibility = View.VISIBLE
        Glide.with(ivConnectingImg).load(R.drawable.typing).into(ivConnectingImg)
        initEndChatListener()
        audioDeviceCallback = object : AudioDeviceCallback() {
            override fun onAudioDevicesAdded(addedDevices: Array<out AudioDeviceInfo?>?) {
                setAudioMode()
            }

            override fun onAudioDevicesRemoved(removedDevices: Array<out AudioDeviceInfo?>?) {
                setAudioMode()
            }
        }

        llProfile.setOnClickListener {
            openProfileDialog()
        }
    }

    fun speakerSwitch(){
        room?.audioSwitchHandler?.let {
            if(it.availableAudioDevices.size > 2){
                showDevicesList(it.availableAudioDevices)
            }else{
                if(it.selectedAudioDevice is AudioDevice.Speakerphone){
                    it.selectDevice(it.availableAudioDevices.find{ it is AudioDevice.Earpiece } ?: it.selectedAudioDevice)
                    ivSpeaker.isActivated = false
                    ivSpeaker.setColorFilter(ContextCompat.getColor(this,R.color.no_change_white))

                }else if(it.selectedAudioDevice is AudioDevice.Earpiece){
                    it.selectDevice(it.availableAudioDevices.find{ it is AudioDevice.Speakerphone } ?: it.selectedAudioDevice)
                    ivSpeaker.isActivated = true
                    ivSpeaker.setColorFilter(ContextCompat.getColor(this,R.color.white))
                }
            }
        }
    }

    fun startRinging() {

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
            .build()

        soundPool = SoundPool.Builder()
            .setAudioAttributes(audioAttributes)
            .setMaxStreams(1) // allow both sounds to play together
            .build()

        ringingSoundId = soundPool!!.load(this, R.raw.call_ringing, 10)
        hangupSoundId = soundPool!!.load(this, R.raw.hang_up_tone, 10)
        alterToneId = soundPool!!.load(this, R.raw.alert_tone, 10)
        beepSound = soundPool!!.load(this, R.raw.beep_sound, 10)
        soundPool!!.setOnLoadCompleteListener { _, sampleId, status ->
            if (status == 0 && sampleId == ringingSoundId) {
               if(!isFromNotification) {
                   playSound(ringingSoundId, -1)
               }
            }
        }
    }

    fun playSound(soundId: Int, loopCount: Int = 0) {
        if (soundId != 0) {
            soundPool?.play(
                soundId,
                1.0f,  // left volume
                1.0f,  // right volume
                10,  // priority (higher number = more important)
                loopCount,      // loop count
                1.0f    // playback rate
            )
        }
    }

    private fun stopSound(streamId: Int) {
        soundPool?.stop(streamId)
    }

    override fun onResume() {
        super.onResume()
        if(isCallConnected){
           registerReceiverBackgroundLogin()
        }
        if(isPermissionsSettingOpen){
            if(CUtils.checkPermissionsAudio(this)){
                getToken()
            }else{
                showPermissionDeniedDialog()
            }
            isPermissionsSettingOpen = false
        }
        ContextCompat.registerReceiver(this,finishReceiver, IntentFilter(CGlobalVariables.FINSIH_ACTIVITY_ACTION),ContextCompat.RECEIVER_NOT_EXPORTED)
        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        mSensorManager.unregisterListener(this)
        //unregisterReceiver(finishReceiver)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.e("onNewIntent","onNewIntent")
        //notification end call click handeled
        if (intent.action == CGlobalVariables.HANG_UP_ACTION) {
            //Log.d("testAiCAll", "voiceCallCompleted() from onNewIntent()")
            if (isCallConnected) {
                voiceCallCompleted(CGlobalVariables.COMPLETED, "${CGlobalVariables.USER_ENDED} From notification", true)
            }else{
                voiceCallCompleted(CGlobalVariables.FAILED, "${CGlobalVariables.USER_ENDED} From notification", true)
            }
        }else if(intent.action == CGlobalVariables.SPEAKER_ACTION){
            if(room?.state == Room.State.CONNECTED){
                speakerSwitch()
            }
        }else if(intent.action == CGlobalVariables.MIC_ACTION){
            toggleMicButton(ivCallMic)
        }
        val bundle = intent.extras
        var isRecharged = false
        if (bundle != null) {
            isRecharged = bundle.getBoolean(CGlobalVariables.IS_RECHARGED)
        }
        Log.e("onNewIntent","isRecharged==>"+isRecharged)
        try {
            if (QuickRechargeBottomSheet.getInstance() != null) {
                QuickRechargeBottomSheet.getInstance().dismiss()
            }
            if (RechargePopUpAfterFreeChat.newInstance() != null) {
                RechargePopUpAfterFreeChat.newInstance().dismiss()
            }
        } catch (e: java.lang.Exception) {
            Log.e("testLogs", e.toString() + "")
        }
        try {
            if (RechargeSuggestionBottomSheet.getInstance() != null) {
                RechargeSuggestionBottomSheet.getInstance().dismiss()
            }
        } catch (e: java.lang.Exception) {
        }
        if (isRecharged) {
            val orderID = bundle!!.getString(CGlobalVariables.ORDER_ID)
            val orderStatus = bundle.getString(CGlobalVariables.ORDER_STATUS)
            val rechargeAmount = bundle.getString(CGlobalVariables.RECHARGE_AMOUNT)
            val paymentMode = bundle.getString(CGlobalVariables.PAYMENT_MODE)
            val razorpayid = bundle.getString("razorpayid")
            if (orderStatus == "1") {
                udatePaymentStatusOnServer(
                    parentLayout,
                    orderStatus,
                    orderID,
                    rechargeAmount,
                    razorpayid
                )
            }
        }

    }


    private fun getDataFromIntent(intent: Intent?) {
        AstrosageKundliApplication.isBackFromCall = false
        if (intent != null) {
            isFromNotification = intent.getBooleanExtra(CGlobalVariables.IS_FROM_RETURN_TO_CALL, false)
            MIC_STATUS = intent.getBooleanExtra(CGlobalVariables.AGORA_CALL_MIC_STATUS, true)
            astrologerProfileUrl = intent.getStringExtra(CGlobalVariables.ASTROLOGER_PROFILE_URL)
            callSId = intent.getStringExtra(CGlobalVariables.AGORA_CALLS_ID)
            //token = intent.getStringExtra(CGlobalVariables.AGORA_TOKEN)
            livekitToken = intent.getStringExtra(CGlobalVariables.AGORA_TOKEN)
            astrologerName = intent.getStringExtra(CGlobalVariables.ASTROLOGER_NAME)
            callDuration = intent.getStringExtra(CGlobalVariables.AGORA_CALL_DURATION)
            astrologerId = intent.getStringExtra(CGlobalVariables.ASTROLOGER_ID)
            serverUrl = intent.getStringExtra(CGlobalVariables.LIVEKIT_SERVER_URL)
            isFreeCall = intent.getBooleanExtra(CGlobalVariables.ISFREE_CONSULTATION, false)
            intentNotificationId = intent.getIntExtra(CGlobalVariables.NOTIFICATION_ID, -1)
            timeRemaining = callDuration
            if (callSId.isNullOrBlank()) {
                processEndCall(true)
            }
            CUtils.setEndChatButtonVisibilityTimer(
                this,
                CGlobalVariables.CHAT_INITIATE_TYPE_ASTROLOGER_AI
            )
            initEndChatDisableTimer()
            //Log.d("testAiCAll", "isFreeCall : $isFreeCall, astrologerName : $astrologerName duration: $callDuration")
            //Log.d("testAiCAll", "time_remaining : ${intent.getStringExtra("time_remaining")}")
            val chatJsonObject = intent.getStringExtra("connect_chat_bean")
            chatJsonObject?.let {
                val connectChat = JSONObject(it)
                if (connectChat.has("astrologer")) {
                    val astroJason = connectChat.getJSONObject("astrologer")
                    aiAstrologerId = astroJason.optString("aiai") ?: "4"
                    astrologerProfileUrl = astroJason.optString("imagefilelarge")
                }
            }
            if (isFromNotification) {
                isCallConnected = true
                ivConnectingImg.visibility = View.GONE
                timeRemaining = if(AIVoiceCallingService.REMAINING_TIME != null) {
                    AIVoiceCallingService.REMAINING_TIME
                }else{
                    intent.getStringExtra("time_remaining")
                }
                timeSetOnTimer(timeRemaining?.trim())

            }
            Log.d("testAiCAll", "CallSId : $callSId")
        }
    }


    private fun initData() {
        tvAstroName.text = astrologerName?.replace("+", " ")

        if (astrologerProfileUrl != null && astrologerProfileUrl!!.length > 0) {
            val newAstrologerProfileUrl = CGlobalVariables.IMAGE_DOMAIN + astrologerProfileUrl
            Glide.with(ivAstroImg).load(newAstrologerProfileUrl).centerCrop().into(ivAstroImg)
        }

    }


    @OptIn(Beta::class)
    private fun joinRoom(token: String?, serverUrl: String?) {

        lifecycleScope.launch { // Use lifecycleScope for coroutines in Activities/Fragments
            try {
                room!!.connect(
                    url = serverUrl!!,
                    token = token!!,
                    options = ConnectOptions(autoSubscribe = true, audio = true)
                )
                setRoomFlow()
            } catch(ex: CancellationException) {
                // Handle JobCancellationException specifically if needed
                Log.e("testAiCAll:", "Job was cancelled", ex)
            }catch (e: Exception) {
                endRemarks = "livekit error ${e.javaClass.name}"
                showCallFailed()
                CUtils.sendLogDataRequest(astrologerId,callSId,"Livekit Error : ${e.message}")
                Log.e("testAiCAll:", "Error initiating connection", e)
            }
        }
    }


    suspend fun setRoomFlow() {
        room!!.events.collect { event ->
            when (event) {
                is RoomEvent.FailedToConnect -> {
                    Log.e("testAiCAll:", "Failed to connect", event.error)
                }

                is RoomEvent.Connected -> {
                    room?.localParticipant?.setMicrophoneEnabled(true)
                    Log.e("testAiCAll:", "Success to connect room: ${event.room.name}")
                    room?.remoteParticipants?.forEach {
                        Log.e(
                            "testAiCAll:",
                            "Remote Participant: ${it.key}, SID: ${it.value}"
                        )
                    }
                    CUtils.fcmAnalyticsEvents(
                        CGlobalVariables.LIVEKIT_ROOM_CONNECTED,
                        AstrosageKundliApplication.currentEventType,
                        ""
                    )
                }

                is RoomEvent.Reconnecting -> {
                    Log.e("testAiCAll:", "Reconnecting to room: ${event.room.name}")
                }

                is RoomEvent.Reconnected -> {
                    Log.e("testAiCAll:", "Reconnected to room: ${event.room.name}")
                    callEndTimer?.cancel()
                }

                is RoomEvent.TrackSubscribed -> {
                    if (!isCallConnected && event.publication.track is RemoteAudioTrack) {
                        // Switch to the main thread to update the UI
                        lifecycleScope.launch(Dispatchers.Main) {
                            ivConnectingImg.visibility = View.GONE
                            unableToConnectTimer?.cancel()
                            stopSound(ringingSoundId)
                            timeSetOnTimer(callDuration)
                            isCallConnected = true
                            offerTypeDuringInitCall = CUtils.getCallChatOfferType(this@AIVoiceCallingActivity)
                            userCallAccepted()
                            startForegroundService()
                            setAudioMode()
                            registerReceiverBackgroundLogin()
                        }
                    }


                }

                is RoomEvent.ParticipantConnected -> {
                    //Toast.makeText(this@AIVoiceCallingActivity, "connected : ", Toast.LENGTH_SHORT).show()
                    Log.e(
                        "testAiCAll:",
                        "ParticipantConnected  =>  ${event.participant.identity}"
                    )

                }

                is RoomEvent.ParticipantStateChanged -> {
                    Log.e(
                        "testAiCAll:",
                        "ParticipantStateChanged  =>  ${event.participant.identity} : ${event.participant.state}"
                    )
                    if (event.participant is RemoteParticipant && event.participant.state == Participant.State.ACTIVE) {
                        Log.e(
                            "testAiCAll:",
                            "RemoteParticipant is Active =>  ${event.participant.identity} : ${event.participant.state}"
                        )
                    }
                }

                is RoomEvent.ActiveSpeakersChanged -> {
                    Log.e(
                        "testAiCAll:",
                        "ActiveSpeakersChanged  =>  ${if (event.speakers.isNotEmpty()) event.speakers.toString() else "NON"}"
                    )
                    if (event.speakers.isEmpty()) {
                        setUserIdeal()
                    } else {
                        removeUserIdeal()
                    }
                }

                is RoomEvent.ParticipantDisconnected -> {
                    Log.e(
                        "testAiCAll:",
                        "ParticipantDisconnected  =>  ${event.participant.identity}"
                    )
                    startCallEndTimer()
                }

                is RoomEvent.ConnectionQualityChanged -> {
                    Log.e(
                        "testAiCAll:",
                        "ConnectionQualityChanged  =>  ${event.participant.name} : ${event.quality}"
                    )
                    showConnectionQuality(event.quality)
                }

                else -> {
                    Log.e("testAiCAll:", "else =>  $event")
                }
            }
        }

    }

    var agentDisconnectRunnable: Runnable = object : Runnable {
        override fun run() {
            Log.e("testAiCAll:", "UserIdeal->showCallFailed()")
            CUtils.fcmAnalyticsEvents(
                CGlobalVariables.AI_CALL_END_DUE_AI_ASTRO_SILENCE,
                AstrosageKundliApplication.currentEventType,
                ""
            )
            endRemarks = CGlobalVariables.DICONNECTED_DUE_TO_INACTIVE
            if(!isCallDisconnected) {
                showCallFailed()
            }
        }
    }
    val handler = Handler(Looper.getMainLooper())
    var endRemarks = CGlobalVariables.COMPLETED

    fun setUserIdeal() {
        handler.removeCallbacks(agentDisconnectRunnable)
        handler.postDelayed(agentDisconnectRunnable, 60000)
    }

    fun removeUserIdeal() {
        handler.removeCallbacks(agentDisconnectRunnable)
    }

    private var isCallDisconnected = false

    fun showCallFailed() {
       // stopLowNetworkBeep()
        stopSound(beepSound)
        updatePoorConnectionIndicator(false)
        ivSpeaker.visibility = View.GONE
        ivCallMic.visibility = View.GONE
        ivCallEnd.visibility = View.GONE
        llProfile.visibility = View.GONE

        ivConnectingImg.visibility = View.GONE
        //Log.d("TestPIP", "isInPictureInPictureModefailed: $isActivityInPIPMode")
        if (isActivityInPIPMode){
            ivCallAgain.visibility = View.GONE
            ivChatIcon.visibility = View.GONE
            ivCancelCall.visibility = View.GONE
            tvCallAgain.visibility = View.GONE
            tvChat.visibility = View.GONE
            tvCancel.visibility = View.GONE
        }else{
            ivCallAgain.visibility = View.VISIBLE
            ivChatIcon.visibility = View.VISIBLE
            ivCancelCall.visibility = View.VISIBLE
            tvCallAgain.visibility = View.VISIBLE
            tvChat.visibility = View.VISIBLE
            tvCancel.visibility = View.VISIBLE
        }
        if (!isCallConnected) {
            tvInternetCallTimer.text = getString(R.string.call_failed_user)
            ivConnectingImg.visibility = View.GONE
        } else {
            ivConnectingImg.visibility = View.GONE
        }
        //if(CUtils.isConnectedWithInternet(this@AIVoiceCallingActivity)) {
            if (isCallConnected) {
                voiceCallCompleted(
                    CGlobalVariables.COMPLETED,
                    endRemarks,
                    false
                )
            } else {
                voiceCallCompleted(
                    CGlobalVariables.FAILED,
                    endRemarks,
                    false
                )
            }
       /* }else{
            setEndChatOverValue(callSId)
            processEndCall(false)
        }*/

        cancelEndCallScheduler(this)
        ivCallAgain.setOnClickListener {
            val astrologerBean = AstrosageKundliApplication.selectedAstrologerDetailBean
            if (astrologerBean != null) {
                val userDetails = CUtils.getUserSelectedProfileFromPreference(this)
                ChatUtils.getInstance(this).connectAIVoiceCall(astrologerBean, userDetails)
                finish()
                CUtils.fcmAnalyticsEvents(
                    CGlobalVariables.AI_CALL_AIGAIN_CLICK,
                    AstrosageKundliApplication.currentEventType,
                    ""
                )
            }
        }

        ivChatIcon.setOnClickListener {
            val astrologerBean = AstrosageKundliApplication.selectedAstrologerDetailBean
            if (astrologerBean != null) {
                ChatUtils.getInstance(this).initChat(astrologerBean)
                finish()
            }
            CUtils.fcmAnalyticsEvents(
                CGlobalVariables.AI_CALL_CHAT_BTN_CLICK,
                AstrosageKundliApplication.currentEventType,
                ""
            )
        }

        ivCancelCall.setOnClickListener { finish() }
    }

    fun setAudioMode(){
            if(
                !CUtils.getBooleanData(this,
                    com.ojassoft.astrosage.utils.CGlobalVariables.IS_AI_CALL_DEFAULT_SPEAKER_ON,
                          true)
                ){
                room?.audioSwitchHandler?.let {
                    it.selectDevice(it.availableAudioDevices.find { it is AudioDevice.Earpiece }
                        ?: it.selectedAudioDevice)
                    ivSpeaker.isActivated = false
                }
            }
            room?.audioSwitchHandler?.let {
                if(it.selectedAudioDevice is AudioDevice.BluetoothHeadset){
                    ivSpeaker.setImageResource(R.drawable.ic_bluetooth)
                }else{
                    if(ivSpeaker.isActivated){
                        it.selectDevice(it.availableAudioDevices.find{ it is AudioDevice.Speakerphone } ?: it.selectedAudioDevice)
                    }else{
                        it.selectDevice(it.availableAudioDevices.find{ it is AudioDevice.Earpiece } ?: it.selectedAudioDevice)
                    }
                    ivSpeaker.setImageResource(R.drawable.ic_speaker)
                }

            }
    }

    //var networkBottomSheetDialog: BottomSheetDialog? = null
//    private val lowNetworkBeepHandler = Handler(Looper.getMainLooper())
//    private var isLowNetworkBeepRunning = false
//    private val lowNetworkBeepRunnable = object : Runnable {
//        override fun run() {
//            if (!isLowNetworkBeepRunning || isCallDisconnected) return
//            playSound(beepSound)
//            lowNetworkBeepHandler.postDelayed(this, 3000)
//        }
//    }
//
//    private fun startLowNetworkBeep() {
//        if (isLowNetworkBeepRunning || beepSound == 0) return
//        isLowNetworkBeepRunning = true
//        lowNetworkBeepHandler.post(lowNetworkBeepRunnable)
//    }
//
//    private fun stopLowNetworkBeep() {
//        isLowNetworkBeepRunning = false
//        lowNetworkBeepHandler.removeCallbacks(lowNetworkBeepRunnable)
//    }

    /**
     * Updates poor connection warning visibility and blinking state.
     */
    private fun updatePoorConnectionIndicator(show: Boolean) {
        if (show) {
            linLyoutPoorConnection.visibility = View.VISIBLE
            if (linLyoutPoorConnection.animation == null) {
                val blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blinking_animation)
                linLyoutPoorConnection.startAnimation(blinkAnimation)
            }
        } else {
            linLyoutPoorConnection.clearAnimation()
            linLyoutPoorConnection.visibility = View.GONE
        }
    }

    var showPoorNetworkView = false
    fun showConnectionQuality(connectionQuality: ConnectionQuality){
        if(connectionQuality == ConnectionQuality.POOR){
            //startLowNetworkBeep()
            playSound(beepSound,-1)

            updatePoorConnectionIndicator(true)
//            if(networkBottomSheetDialog?.isShowing == true) networkBottomSheetDialog?.dismiss()
//            networkBottomSheetDialog = BottomSheetDialog(this).apply {
//                setContentView(R.layout.network_status_layout)
//                window?.findViewById<View?>(R.id.design_bottom_sheet)?.setBackgroundResource(android.R.color.transparent)
//            }

//            val close_network_view: ImageView? = networkBottomSheetDialog?.findViewById<ImageView?>(R.id.close_network_view)
//            close_network_view?.setOnClickListener(object : View.OnClickListener {
//                override fun onClick(view: View?) {
//                    networkBottomSheetDialog?.dismiss()
//                }
//            })
            showPoorNetworkView = false
//            networkBottomSheetDialog?.show()
//            networkBottomSheetDialog?.setOnDismissListener(object :
//                DialogInterface.OnDismissListener {
//                override fun onDismiss(dialogInterface: DialogInterface?) {
//                    CUtils.fcmAnalyticsEvents(
//                        CGlobalVariables.CLOSE_POOR_NETWORK_CONNECTION_VIEW,
//                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,
//                        ""
//                    )
//                    Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
//                        override fun run() {
//                            showPoorNetworkView = true
//                        }
//                    }, 10000)
//                }
//            })

        }else{
            //stopLowNetworkBeep()
            stopSound(beepSound)
            updatePoorConnectionIndicator(false)
           // networkBottomSheetDialog?.dismiss()
        }

        parentLayout.setOnClickListener {
            audioDeviceListView.visibility = View.GONE
        }
    }

    private fun requestPermissions() {
        if(checkCallingPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }else{
            if(!isFromNotification) {
                getToken()
            }else{
                lifecycleScope.launch { setRoomFlow() }
            }
        }

    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { grant ->
            if (grant) {
                if(!isFromNotification) {
                    getToken()
                }else{
                    lifecycleScope.launch { setRoomFlow() }
                }
            } else {
                showPermissionDeniedDialog()
            }
        }

    private fun initEndChatListener() {
        endCallValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot != null) {
                    try {
                        END_CALL_DATA = dataSnapshot.value as Boolean
                        CUtils.errorLogs =
                            CUtils.errorLogs + "initEndCallListener()" + "END_CALL_DATA==>>" + END_CALL_DATA + "\n"
                        if (END_CALL_DATA) {
                            if (countDownTimer != null) {
                                countDownTimer!!.cancel()
                            }
                            AstrosageKundliApplication.IS_AGORA_CALL_RUNNING = false
                            CGlobalVariables.callTimerTime = 0
                            processEndCall(true)
                            CUtils.fcmAnalyticsEvents("status_time_over_chat_completed", AstrosageKundliApplication.currentEventType, "");
                        }
                    } catch (_: Exception) { }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        readRef = getFirebaseDatabase(callSId).child(CGlobalVariables.END_CHAT_OVER_FBD_KEY)
        readRef!!.addValueEventListener(endCallValueEventListener!!)
    }

    var endCallBtnDisabledTimer: CountDownTimer? = null
    fun initEndChatDisableTimer() {
        if (endCallBtnDisabledTimer != null) {
            endCallBtnDisabledTimer = null
            //ivCallEnd.setVisibility(View.INVISIBLE)
            ivCallEnd.setBackgroundResource(R.drawable.bg_disable_call_gray)
            ivCallEnd.setEnabled(false)
        }

        endCallBtnDisabledTimer = object :
            CountDownTimer(AstrosageKundliApplication.endChatTimeShowMilliSeconds, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                AstrosageKundliApplication.endChatTimeShowMilliSeconds = millisUntilFinished
            }

            override fun onFinish() {
                //ivCallEnd.setVisibility(View.VISIBLE)
                ivCallEnd.setBackgroundResource(R.drawable.bg_circle_red)
                ivCallEnd.setEnabled(true)
            }
        }.start()
    }

    private fun processEndCall(isCallFailed: Boolean = false) {
        try {
            CGlobalVariables.chatTimerTime = 0
            stopTimerAndListener()
            cancelOnDisconnectListener()
            unRegisterConnectivityStatusReceiver()
            CUtils.updateChatCallOfferType(this, true, CGlobalVariables.CALL_CLICK)
            isCallConnected = false
            isCallDisconnected = true
            CGlobalVariables.MIC_MUTE_STATUS = false
            val stopServiceIntent = Intent(this, AIVoiceCallingService::class.java)
            stopService(stopServiceIntent)
            if(isActivityInPIPMode) {
                val intent = Intent(this, AIVoiceCallingActivity::class.java)
                startActivity(intent)
            }
            if(isCallFailed){
                showCallFailed()
            }
            LivekitSingleton.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun timeSetOnTimer(timeString: String?) {
        //talktime = "36:24 minutes";
        if (!timeString.isNullOrBlank()) {
            val parts = timeString.split(':')

            if (parts.size != 2) {
                throw IllegalArgumentException("Invalid time format. Expected 'mm:ss', but got '$timeString'")
            }

            try {
                val minutes = parts[0].toLong()
                val seconds = parts[1].toLong()

                val minutesInMillis = TimeUnit.MINUTES.toMillis(minutes)
                val secondsInMillis = TimeUnit.SECONDS.toMillis(seconds)

                val longTotalVerificationTime = minutesInMillis + secondsInMillis
                startTimer(longTotalVerificationTime)

            } catch (_: NumberFormatException) { }
        }
        registerConnectivityStatusReceiver()
        setOnDisconnectListener()
    }

    private fun cancelOnDisconnectListener() {
        try {
            val channelId = callSId!!
            getFirebaseDatabase(channelId).child(CGlobalVariables.USER_DISCONNECT_TIME_FBD_KEY)
                .onDisconnect().cancel()
        } catch (_: Exception) {
            //
        }
    }

    private fun setOnDisconnectListener() {
        try {
            val channelId = callSId!!
            //Log.d("OnDisconnectListner", "setOnDisconnectListner channelId="+channelId);
            getFirebaseDatabase(channelId).child(CGlobalVariables.USER_DISCONNECT_TIME_FBD_KEY)
                .onDisconnect().setValue(ServerValue.TIMESTAMP)
        } catch (_: Exception) {
            //
        }
    }

    private fun startTimer(longTotalVerificationTime: Long) {
        try {
            if (countDownTimer != null) {
                countDownTimer!!.cancel()
                countDownTimer = null
            }
            countDownTimer = object : CountDownTimer(longTotalVerificationTime, 1000) {
                @SuppressLint("SetTextI18n")
                override fun onTick(millisUntilFinished: Long) {

                    // First, calculate the time components
                    val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
                    val minutes =
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                            TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
                        )
                    val seconds =
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                        )
                    /*
                     * show low balance layout
                     */
                    if(millisUntilFinished < 60000){
                        if(lowBalLayout.isGone) playSound(alterToneId)
                        showLowBalanceLayout((millisUntilFinished/1000).toInt())
                    }
                    // Format the time components into a string
                    val text = if (hours > 0) {
                        String.format(
                            Locale.US,
                            "%02d:%02d:%02d",
                            hours,
                            minutes,
                            seconds
                        )
                    } else {
                        String.format(
                            Locale.US,
                            "%02d:%02d",
                            minutes,
                            seconds
                        )
                    }
                    tvInternetCallTimer.text =
                        resources.getString(R.string.time_remaining) + " " + text
                    timeRemaining = text
                    CGlobalVariables.callTimerTime = millisUntilFinished
                    CGlobalVariables.chatTimerTime = millisUntilFinished

                    //Log.d("testVideoCallActivity", "timer is calling" + remTime);
                }

                override fun onFinish() {
                    isCallConnected = false
                    CGlobalVariables.callTimerTime = 0
                    CGlobalVariables.chatTimerTime = 0
                    // CUtils.cancelOnDisconnentEvent(agoraCallSId);
                    CUtils.fcmAnalyticsEvents(
                        CGlobalVariables.END_AI_VOICE_CALL_TIME_OVER,
                        AstrosageKundliApplication.currentEventType,
                        ""
                    )
//                    Log.d("testAiCAll", "count down timer onFinish")
                    voiceCallCompleted(CGlobalVariables.COMPLETED,CGlobalVariables.TIME_OVER, true)
                }
            }.start()
        } catch (_: Exception) {
//            Log.d("testAiCAll", "start timer Error : $e")
        }
    }

    private fun userCallAccepted() {
        CUtils.fcmAnalyticsEvents(
            CGlobalVariables.USER_AI_VOICE_CALL_ACCEPT_API_CALL,
            AstrosageKundliApplication.currentEventType,
            ""
        )
        setUserAcceptedOnFirebase(callSId)
        if (!CUtils.isConnectedWithInternet(this)) {
            Toast.makeText(
                this,
                getResources().getString(R.string.no_internet),
                Toast.LENGTH_SHORT
            ).show()
        } else {

            val api = RetrofitClient.getInstance().create<ApiList?>(ApiList::class.java)
            val call = api.userAICallAccept(getCallAcceptedParams(callSId))

            call.enqueue(object : Callback<ResponseBody?> {
                override fun onResponse(
                    call: Call<ResponseBody?>,
                    response: Response<ResponseBody?>
                ) {
                    try {
                        val myResponse = response.body()?.string();
                        if (myResponse != null) {
                            CUtils.fcmAnalyticsEvents(
                                CGlobalVariables.USER_AI_VOICE_CALL_ACCEPT_API_RESPONSE,
                                AstrosageKundliApplication.currentEventType,
                                ""
                            )
//                            Log.d("testAiCAll", "userCallAccepted Response : $myResponse")
                            val jsonObject = JSONObject(myResponse)
                            if(jsonObject.has("status") && jsonObject.getString("status") == "1") {
                                //joinRoom(livekitToken, serverUrl)
                            }


                        }
                    }catch(_: Exception) {
//                        Log.d("testAiCAll", "userCallAccepted Error : $e")

                    }

                }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                }
            })
        }
    }

    private fun getCallAcceptedParams(channelID: String?): MutableMap<String?, String?> {
        val headers = java.util.HashMap<String?, String?>()
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(this))
        headers.put(CGlobalVariables.CHAT_DURATION, changeMinToSec())
        headers.put(CGlobalVariables.CHANNEL_ID, channelID)
        headers.put(CGlobalVariables.ASTROLOGER_ID, CUtils.getSelectedAstrologerID(this))
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(CGlobalVariables.LANGUAGE_CODE))

        //Log.d("testVideoCallActivity", "getAgoraCallAcceptedParams   =    " + headers);
        return CUtils.setRequiredParams(headers)
    }

    fun changeMinToSec(): String {
        var totalSec = "00"
        callDuration?.let{
            val prats = it.split(":")
            val minute = prats[0]
            val second = prats[1]
            if (minute.isNotEmpty() && second.isNotEmpty()) {
                val convertSec = minute.toInt() * 60
                 totalSec = (convertSec + second.toInt()).toString()
            }

        }
        return totalSec
    }

    fun voiceCallCompleted(status: String,remark: String, isFinishActivity: Boolean) {
        room?.disconnect()
        room?.release()
        playSound(hangupSoundId)
        endCallBtnDisabledTimer?.cancel()
        AstrosageKundliApplication.isEndChatCompleted = true
        isCallConnected = false
        AstrosageKundliApplication.IS_AGORA_CALL_RUNNING = false
        CUtils.changeFirebaseKeyStatus(callSId, "NA", true, remark)
        CGlobalVariables.callTimerTime = 0
        CGlobalVariables.chatTimerTime = 0
        Log.d("testAiCAll", "voiceCallCompleted: remark : $remark  status : $status")
        showProgressBar()
        if (!CUtils.isConnectedWithInternet(this@AIVoiceCallingActivity)) {
            AstrosageKundliApplication.isEndChatReqOnGoing = false
            processEndCall()
        } else {
            val api = RetrofitClient.getInstance().create(ApiList::class.java)
            val call = api.endAIcall(getCallCompleteParams(status,remark))
            AstrosageKundliApplication.isEndChatReqOnGoing = true
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    AstrosageKundliApplication.isEndChatReqOnGoing = false
                    hideProgressBar()
                    var status = ""
                    try {
                        val myRespose = response.body()!!.string()
//                        Log.d("testAiCAll", "camplete vaice call is == $myRespose");
                        val jsonObject = JSONObject(myRespose)
                        status = jsonObject.getString("status")
                    } catch (_: Exception) {
                        status = ""
                    }

                    /*if (status != "1") { // end-chat-api fail
                        setEndChatOverValue(callSId)
                    }*/

                    if (status == "1") {
                        CUtils.startUserAiChatCategoryDataService(
                            this@AIVoiceCallingActivity,
                            offerTypeDuringInitCall,
                            callSId,
                            com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_AI_CALL
                        )
                    } else { // end-chat-api fail
                        setEndChatOverValue(callSId)
                    }

                    processEndCall()
                    if(!isDisconnectBeforeConnect) {
                        showEndCallPopup()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    hideProgressBar()
                    AstrosageKundliApplication.isEndChatReqOnGoing = false
                    processEndCall()
                }
            })

        }
    }

    private fun getCallCompleteParams(status: String, remarks: String): Map<String, String?> {
        val headers = HashMap<String, String?>()
        CGlobalVariables.CHAT_END_STATUS = CGlobalVariables.COMPLETED
        headers[CGlobalVariables.APP_KEY] =
            CUtils.getApplicationSignatureHashCode(this@AIVoiceCallingActivity)
        headers[CGlobalVariables.STATUS] = status
        headers[CGlobalVariables.CHAT_DURATION] = calculateCallDurationInSecond(timeRemaining)
        headers[CGlobalVariables.CHANNEL_ID] = callSId
        headers[CGlobalVariables.ASTROLOGER_ID] = astrologerId
        headers[CGlobalVariables.PACKAGE_NAME] = CUtils.getAppPackageName(this)
        headers[CGlobalVariables.REMARKS] = remarks
        headers[CGlobalVariables.LANG] = CUtils.getLanguageKey(CGlobalVariables.LANGUAGE_CODE)
        headers[CGlobalVariables.KEY_NAME] = CUtils.getUserFullName(this)
        headers[CGlobalVariables.CALLS_ID] = callSId
        headers["activity"] = "AIVoiceCallActivity"
        headers[CGlobalVariables.APP_VERSION] = BuildConfig.VERSION_NAME
        headers[CGlobalVariables.DEVICE_ID] = CUtils.getMyAndroidId(this)

        //Log.d("testAiCAll", "get Call completed params ==>>" + headers)

        return headers
    }

    private fun calculateCallDurationInSecond(userRemainingTime: String?): String {
        //talktime = "36:24 minutes";
        var userChatDuration = "00"
        var totalTime: Long = 0
        var remainingUserTime: Long = 0
        var actualChatUserTime: Long = 0

        try {
            if (!callDuration.isNullOrBlank()) {

                val parts = callDuration!!.split(":")
                val minutes = TimeUnit.MINUTES.toMillis(parts[0].toLong())
                val seconds = TimeUnit.SECONDS.toMillis(parts[1].toLong())

                // Calculate the total seconds and return the result.
                totalTime = minutes + seconds
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }

        try {
            if (!userRemainingTime.isNullOrBlank() && userRemainingTime != "00:00:00") {
                val remainingTimeArr = userRemainingTime.split(":")

                if (remainingTimeArr.size == 3) { // Format: hh:mm:ss
                    val hourTime = TimeUnit.HOURS.toMillis(remainingTimeArr[0].toLong())
                    val minTime = TimeUnit.MINUTES.toMillis(remainingTimeArr[1].toLong())
                    val secTime = TimeUnit.SECONDS.toMillis(remainingTimeArr[2].toLong())
                    remainingUserTime = hourTime + minTime + secTime
                } else if (remainingTimeArr.size == 2) { // Format: mm:ss
                    val minTime = TimeUnit.MINUTES.toMillis(remainingTimeArr[0].toLong())
                    val secTime = TimeUnit.SECONDS.toMillis(remainingTimeArr[1].toLong())
                    remainingUserTime = minTime + secTime
                }
            }else{
                return "00"
            }

            actualChatUserTime = totalTime - remainingUserTime
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }

        try {
            val seconds = TimeUnit.MILLISECONDS.toSeconds(actualChatUserTime)
            userChatDuration = seconds.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return userChatDuration
    }

    private fun setEndChatOverValue(channelID: String?) {
        if (!TextUtils.isEmpty(channelID)) {
            getFirebaseDatabase(channelID).child(CGlobalVariables.END_CHAT_OVER_FBD_KEY)
                .setValue(true)
        }
    }

    private fun showProgressBar() {
        if (pd == null) {
            pd = CustomProgressDialog(this)
        }
        pd!!.setCanceledOnTouchOutside(false)
        if (!pd!!.isShowing()) {
            pd!!.show()
        }
    }

    /**
     * hide Progress Bar
     */
    fun hideProgressBar() {
        try {
            if (pd != null && pd!!.isShowing) pd!!.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun toggleMicButton(view: View) {

        if (ivCallMic.isActivated) {
            room?.setMicrophoneMute(false)
            ivCallMic.isActivated = false
            ivCallMic.setColorFilter(ContextCompat.getColor(this, R.color.no_change_white))
            CGlobalVariables.MIC_MUTE_STATUS = false
        } else {
            room?.setMicrophoneMute(true)
            ivCallMic.isActivated = true
            ivCallMic.setColorFilter(ContextCompat.getColor(this, R.color.white))
            CGlobalVariables.MIC_MUTE_STATUS = true
        }
    }

    private fun getToken() {
       startUnableToConnectTimer()
        if(!CUtils.getBooleanData(this,"is_profile_shown",false)) {
            openHoverHelpDialog()
            CUtils.saveBooleanData(this,"is_profile_shown",true)
        }

         val call = RetrofitClient.getInstance().create(ApiList::class.java).getRequest(CGlobalVariables.LIVEKIT_TOKEN_URL,getTokenParameters())
        //Log.d("testAiCAll", "tokenUrl: $tokenUrl")
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                p0: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) {
                response.body()?.let {
                    try {
                        val myResponse = it.string()
                        Log.d("testAiCAll", "response : $myResponse")
                        val jsonObject = JSONObject(myResponse)
                        if(jsonObject.has("token")){
                            token = jsonObject.getString("token")
                            serverUrl = jsonObject.getString("serverurl")
                            joinRoom(token,serverUrl)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(
                p0: Call<ResponseBody?>,
                p1: Throwable
            ) {
                p1.printStackTrace()
            }

        })
    }

    fun updateUserProfileOnLivekit(){
        val userProfile = CUtils.getUserSelectedProfileFromPreference(this)
        val place = userProfile.place?.split(" ")?.get(0) ?: "Agra"
        tvUserName.text = userProfile.name?.split(" ")[0]
        if (userProfile.gender.equals("M",true) || userProfile.gender.equals("Male",true)){
            ivUserProfImg.setImageResource(R.drawable.male_user)
        }else{
            ivUserProfImg.setImageResource(R.drawable.female_user)
        }
        val userMap =  mutableMapOf<String, String>().apply {
            this["n"] = userProfile.name?.lowercase() ?: "User"
            this["sx"] = userProfile.gender
            this["dob"] = "${userProfile.year}:${userProfile.month}:${userProfile.day}:${userProfile.hour}:${userProfile.minute}:${userProfile.second}"
            this["lo"] = "${userProfile.longdeg}:${userProfile.longmin}"
            this["lt"] = "${userProfile.latdeg}:${userProfile.latmin}"
            this["p"] = place
            this["tz"] = userProfile.timezone ?: "+5.5"
            this["dst"] = "0"
        }
        val userData = userMap.entries.joinToString(
            separator = ", ",
            prefix = "{ ",
            postfix = " }"
        ) {
            "'${it.key}': '${it.value}'" // This formats the single quotes
        }
        //Log.d("profileUpdate", "updateUserProfileOnLivekit: userData : $userData" )

        room?.localParticipant?.updateAttributes(mapOf("all_attributes" to userData))
    }

    fun getTokenParameters():Map<String, String>{

        val userdata = if(!checkBirthDetails(CUtils.getUserSelectedProfileFromPreference(this))){
                // if user has not sufficient birth details then we are putting default data
            com.ojassoft.astrosage.utils.CUtils.prepareUserProfile(this)
        }else{
            CUtils.getUserSelectedProfileFromPreference(this)
        }

        val place = userdata.place?.split(",")?.get(0) ?: "Agra"
        tvUserName.text = userdata.name

        if (userdata.gender.equals("M",true) || userdata.gender.equals("Male",true)){
            ivUserProfImg.setImageResource(R.drawable.male_user)
        }else{
            ivUserProfImg.setImageResource(R.drawable.female_user)
        }
        val userOfferType = CUtils.getUserIntroOfferType(this)
        val modelName = if(userOfferType == CGlobalVariables.INTRO_OFFER_TYPE_FREE){
            1
        } else if(userOfferType.contains(CGlobalVariables.REDUCED_PRICE_OFFER)){
            2
        }else{
            0
        }
        return mutableMapOf<String, String>().apply {
            this["n"] = userdata.name?.lowercase() ?: "User"
            this["sx"] = userdata.gender
            this["dob"] = "${userdata.year}:${userdata.month}:${userdata.day}:${userdata.hour}:${userdata.minute}:${userdata.second}"
            this["lo"] = "${userdata.longdeg}:${userdata.longmin}"
            this["lt"] = "${userdata.latdeg}:${userdata.latmin}"
            this["p"] = place
            this["tz"] = userdata.timezone ?: "+5.5"
            this["dst"] = "0"
            this["uid"] = CUtils.getCountryCode(this@AIVoiceCallingActivity)+CUtils.getUserID(this@AIVoiceCallingActivity)
            this["aid"] = aiAstrologerId ?: "42"
            this["pg"] = "1"
            this["av"] = BuildConfig.VERSION_NAME
            this["callsid"] = callSId ?: "CALLS_ID_NOT_AVAILABLE"
            this["dv"] = CUtils.getMyAndroidId(this@AIVoiceCallingActivity)
            this["ml"] = modelName.toString()
            this["lg"] = LANGUAGE_CODE.toString()
            this["cp"] = CUtils.getCallChatOfferType(this@AIVoiceCallingActivity)
            this["sfc"] = if(CUtils.isSecondFreeChat(this@AIVoiceCallingActivity)) "1" else "0"
        }
    }

    fun getFirebaseDatabase(channelIDD: String?): DatabaseReference {
        //channelIDD = "FCH9911764722P137A1620053834631";
        var channelIDD = channelIDD
        if (channelIDD == null || channelIDD.isEmpty()) {
            channelIDD = ""
        }
        val mFirebaseInstance = FirebaseDatabase.getInstance()

        return mFirebaseInstance.getReference(CGlobalVariables.CHANNELS).child(channelIDD)
    }

    private fun enterPIPMode() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val display = windowManager.defaultDisplay
                val point = Point()
                display.getSize(point)
                val width = point.x
                val height = point.y
                val ratio = Rational(width, height)
                val pipBuilder = PictureInPictureParams.Builder()

                pipBuilder.setAspectRatio(ratio).build()
                findViewById<View>(R.id.viewInternetCallBlackScreen).visibility = View.GONE
                enterPictureInPictureMode(pipBuilder.build())
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode)
        if (lifecycle.currentState == Lifecycle.State.CREATED) {
            //Log.d("TestPIP", "close PIP");
            //when user click on Close button of PIP this will trigger
            CUtils.fcmAnalyticsEvents(
                CGlobalVariables.EXIT_AI_VOICE_CALL_FROM_PIP,
                AstrosageKundliApplication.currentEventType,
                ""
            )
            if(room?.state == Room.State.CONNECTED){
                room?.disconnect()
                room?.release()
//                Log.d("testAiCAll", "voiceCallCompleted() from ivCallEnd onPictureInPictureModeChanged()")
                if(isCallConnected) {
                    voiceCallCompleted(
                        CGlobalVariables.COMPLETED,
                        CGlobalVariables.USER_ENDED,
                        true
                    )
                }else{
                    voiceCallCompleted(
                        CGlobalVariables.FAILED,
                        CGlobalVariables.USER_ENDED,
                        true
                    )
                }
            }

            return
        }
        isActivityInPIPMode = isInPictureInPictureMode
        //Log.d("TestPIP", "isInPictureInPictureMode: $isInPictureInPictureMode")
        if (isInPictureInPictureMode) {
            ivCallEnd.visibility = View.GONE
            ivSpeaker.visibility = View.GONE
            ivCallMic.visibility = View.GONE
            ivCallAgain.visibility = View.GONE
            ivChatIcon.visibility = View.GONE
            ivCancelCall.visibility = View.GONE
            tvCallAgain.visibility = View.GONE
            tvChat.visibility = View.GONE
            tvCancel.visibility = View.GONE
            tvAstroName.textSize = 13f
            tvInternetCallTimer.textSize = 8f
            tvInternetCallTimer.gravity = Gravity.CENTER
            tvAstroName.gravity = Gravity.CENTER
            audioDeviceListView.visibility = View.GONE
            controlLayout.visibility = View.GONE
            paddindView.visibility = View.GONE
            topPadding.visibility = View.GONE
            llProfile.visibility = View.GONE
            setBottomMargin(guideview,10)

        } else {

            if (isCallDisconnected){
                ivCallAgain.visibility = View.VISIBLE
                ivChatIcon.visibility = View.VISIBLE
                ivCancelCall.visibility = View.VISIBLE
                tvCallAgain.visibility = View.VISIBLE
                tvChat.visibility = View.VISIBLE
                tvCancel.visibility = View.VISIBLE
            }

            if(ivCallAgain.visibility != View.VISIBLE) {
                ivCallEnd.visibility = View.VISIBLE
                ivSpeaker.visibility = View.VISIBLE
                ivCallMic.visibility = View.VISIBLE
            }
            ivAstroImg.visibility = View.VISIBLE
            guideview.visibility = View.VISIBLE
            tvAstroName.textSize = 22f
            tvInternetCallTimer.textSize = 18f
            tvInternetCallTimer.gravity = Gravity.START
            tvAstroName.gravity = Gravity.START
            controlLayout.visibility = View.VISIBLE
            paddindView.visibility = View.VISIBLE
            topPadding.visibility = View.VISIBLE
            llProfile.visibility = View.VISIBLE
            setBottomMargin(guideview,40)
        }
    }

    override fun onPictureInPictureRequested(): Boolean {
        return super.onPictureInPictureRequested()
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (isCallConnected) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                enterPIPMode()
            } else {
                startForegroundService()
            }
        }
    }

    fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

    fun setBottomMargin(view: View, newMarginDp: Int) {
        val context = view.context
        val newMarginPx = newMarginDp.dpToPx(context)

        val layoutParams = view.layoutParams

        if (layoutParams is ConstraintLayout.LayoutParams) {

            layoutParams.bottomMargin = newMarginPx
            view.layoutParams = layoutParams
        }
    }

    private fun openProfileDialog() {
        try {
            CUtils.openProfileOrKundliAct(
                this,
                AstrosageKundliApplication.selectedAstrologerDetailBean.getUrlText(),
                "profile_send",
                com.ojassoft.astrosage.utils.CGlobalVariables.BACK_FROM_PROFILE_CHAT_DIALOG
            )
        } catch (e: java.lang.Exception) {
            //
        }
    }

    fun checkBirthDetails(userProfileData: UserProfileData):Boolean{

        val year: String? = userProfileData.getYear()
        val hour: String? = userProfileData.getHour()

        return (!TextUtils.isEmpty(userProfileData.getName())
                && !com.ojassoft.astrosage.utils.CUtils.isGenderNotDefined(userProfileData.getGender())
                && !TextUtils.isEmpty(year) && (year != "0")
                && !TextUtils.isEmpty(hour) && !TextUtils.isEmpty(userProfileData.getPlace())
                )

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("AiCAllTest", "onActivityResult(): requestCode = $requestCode")
        if (requestCode == com.ojassoft.astrosage.utils.CGlobalVariables.BACK_FROM_PROFILE_CHAT_DIALOG) {
            if (data != null) {
                val isProceed = data.getExtras()!!.getBoolean("IS_PROCEED")
                val fromWhere: String = data.getStringExtra("fromWhere")?:""
                if (isProceed) {
                    AstrosageKundliApplication.backgroundLoginCountForChat = 0
                    if (!TextUtils.isEmpty(fromWhere) && fromWhere.equals(
                            "profile_send",
                            ignoreCase = true
                        )
                    ) {
                        CUtils.errorLogs = CUtils.errorLogs + "goto api hit\n"
                        updateUserProfileOnLivekit()
                    }
                }

                if (!isProceed && data.extras?.containsKey("openKundliList") == true) {
                    CUtils.openSavedKundliList(
                        this,
                        AstrosageKundliApplication.selectedAstrologerDetailBean.getUrlText(),
                        "profile_send",
                        com.ojassoft.astrosage.utils.CGlobalVariables.BACK_FROM_PROFILE_CHAT_DIALOG
                    )
                } else if (!isProceed && data.getExtras()?.containsKey("openProfileForChat") == true) {
                    var prefillData = true
                    if (data.getExtras()?.containsKey("prefillData") == true) {
                        prefillData = data.getBooleanExtra("prefillData", true)
                    }
                    val bundle = data.  extras
                    CUtils.openProfileForChat(
                        this,
                        AstrosageKundliApplication.selectedAstrologerDetailBean.getUrlText(),
                        "profile_send",
                        bundle,
                        prefillData,
                        com.ojassoft.astrosage.utils.CGlobalVariables.BACK_FROM_PROFILE_CHAT_DIALOG
                    )
                }
            }
        } else if (requestCode == CGlobalVariables.REQUEST_FOR_QUICK_RECHARGE) {
            // isQuickRechargeClicked = false
            if (data != null) {
                val bundle = data.extras
                val isRecharged = bundle?.getBoolean(CGlobalVariables.IS_RECHARGED)

                if (isRecharged == true) {
                    val orderID = bundle.getString(CGlobalVariables.ORDER_ID)
                    val orderStatus = bundle.getString(CGlobalVariables.ORDER_STATUS)
                    val rechargeAmount = bundle.getString(CGlobalVariables.RECHARGE_AMOUNT)
                    val paymentMode = bundle.getString(CGlobalVariables.PAYMENT_MODE)
                    val razorpayid = bundle.getString("razorpayid")
                    val phonepeid = bundle.getString(CGlobalVariables.PHONEPE_ID)
                    val phonepeOrderId = bundle.getString(CGlobalVariables.PHONEPE_ORDER_ID)
                    val od = bundle.getString(CGlobalVariables.ORDER_ID_PHONEPE)
                    // Log.d("isRecharged","orderID = "+ orderID + " orderStatus = "+ orderStatus + " recharge = "+ rechargeAmount);
                    //demoTxt.setText("orderID = "+ orderID + " orderStatus = "+ orderStatus + " recharge = "+ rechargeAmount);
                    if (orderStatus == "0") {
                        CUtils.fcmAnalyticsEvents(
                            CGlobalVariables.GOOGLE_ANALYTICS_CHAT_WINDOW_CHAT_EXTEND_RECHARGE_FAILED,
                            com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,
                            "")

                        startTimer(CGlobalVariables.callTimerTime)
                        startForegroundService()
                        updatePaymentStatusOnServerFailed(
                            orderStatus,
                            orderID,
                            rechargeAmount,
                            paymentMode
                        )
                    } else {
                        timeTakenForRecharge =
                            Calendar.getInstance().getTimeInMillis() - timeTakenForRecharge
                        val extendedWaitTime: Long = timeTakenForRecharge / 1000

                        verifyRechargeAndUpdateTimer(
                            orderStatus,
                            orderID,
                            rechargeAmount,
                            razorpayid,
                            extendedWaitTime,
                            phonepeid,
                            phonepeOrderId,
                            od
                        )
                    }
                }
            } else {
                startTimer(CGlobalVariables.callTimerTime)
                startForegroundService()
            }
        }
    }

    private fun stopTimerAndListener() {
        try {
            room?.disconnect()
            room?.release()
        } catch (_: Exception) { }
        unableToConnectTimer?.cancel()
        handler.removeCallbacks(agentDisconnectRunnable)
        if (callEndTimer != null) {
            callEndTimer!!.cancel()
        }
        if (countDownTimer != null) {
            countDownTimer!!.cancel()
        }
        if (readRef != null && endCallValueEventListener != null) {
            readRef!!.removeEventListener(endCallValueEventListener!!)
        }
    }
    var permissionDialog: AlertDialog? = null
    var isPermissionsSettingOpen = false

    // To show the dialog to open settings
    private fun showPermissionDeniedDialog() {
        if(permissionDialog?.isShowing == true){
            return
        }
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_mic_permission, null)
        builder.setView(dialogView)
        // Get references to the views
        val notNowButton = dialogView.findViewById<TextView>(R.id.btn_not_now)
        val closeBtn = dialogView.findViewById<ImageView>(R.id.close_btn)
        val txtTitle = dialogView.findViewById<TextView>(R.id.txtTitle)
        val txtSubText = dialogView.findViewById<TextView>(R.id.txtSubText)
        val txtSubText1 = dialogView.findViewById<TextView>(R.id.txtSubText1)
        val settingsButton = dialogView.findViewById<Button>(R.id.btn_settings)

        // Apply fonts
        FontUtils.changeFont(this, notNowButton, CGlobalVariables.FONTS_OPEN_SANS_BOLD)
        FontUtils.changeFont(this, settingsButton, CGlobalVariables.FONTS_OPEN_SANS_BOLD)
        FontUtils.changeFont(this, txtTitle, CGlobalVariables.FONTS_OPEN_SANS_BOLD)
        FontUtils.changeFont(this, txtSubText, CGlobalVariables.FONTS_OPEN_SANS_REGULAR)
        FontUtils.changeFont(this, txtSubText1, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD)

        // Create and show the dialog
        permissionDialog = builder.create()
        permissionDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        permissionDialog?.setCancelable(false)
        permissionDialog?.show()

        // Set click listeners
        notNowButton.setOnClickListener {
            permissionDialog?.dismiss()
            voiceCallCompleted(CGlobalVariables.FAILED, CGlobalVariables.USER_PERMISSION_DENIED,false)
        }

        closeBtn.setOnClickListener {
            permissionDialog?.dismiss()
            voiceCallCompleted(CGlobalVariables.FAILED, CGlobalVariables.USER_PERMISSION_DENIED,false)
        }

        settingsButton.setOnClickListener {
            permissionDialog?.dismiss()
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            isPermissionsSettingOpen = true
            startActivity(intent)
        }
    }

    /**
     * Shows a dialog to the user when microphone permission is declined, explaining why it's needed
     * and providing a way to navigate to the app settings to enable it.
     */
//    private fun showPermissionDeniedDialog() {
//        AlertDialog.Builder(this)
//            .setTitle(getString(R.string.mic_permission_title))
//            .setMessage(getString(R.string.mic_permission_text))
//            .setPositiveButton(getString(R.string.goto_setting)) { dialog, _ ->
//                // Create an intent to open the app's settings page
//                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                val uri = Uri.fromParts("package", packageName, null)
//                intent.data = uri
//                startActivity(intent)
//                dialog.dismiss()
//            }
//            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
//                dialog.dismiss()
//            }
//            .setCancelable(false) // Make the dialog non-cancelable by touching outside
//            .show()
//    }

    private fun unRegisterConnectivityStatusReceiver() {
        valueEventListenerNetConnection?.let {
            FirebaseDatabase.getInstance().getReference(".info/connected")
                .removeEventListener(it)
        }
    }

    private val  connectivityRunnable = object : Runnable {
        override fun run() {
            isConnectivityDisconnected = false
            CGlobalVariables.CHAT_END_STATUS = CGlobalVariables.CALL_INTERNET_DISCONNECTION
            CUtils.changeFirebaseKeyStatus(callSId, "true", true, CGlobalVariables.CALL_INTERNET_DISCONNECTION)
            endRemarks = CGlobalVariables.CALL_INTERNET_DISCONNECTION
            //Log.d("testAiCAll", "connectivityRunnable ->  showCallFailed()")

            if(!isCallDisconnected) showCallFailed()
        }
    }

    private fun startConnectivityTimer() {
        try {
            handler.removeCallbacks(connectivityRunnable)
            handler.postDelayed(connectivityRunnable, 50000)
        } catch (_: Exception) {
            //
        }
    }

    private fun registerConnectivityStatusReceiver() {
        unRegisterConnectivityStatusReceiver()
        val connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected")
        valueEventListenerNetConnection =
            connectedRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val connected: Boolean =
                        snapshot.getValue<Boolean?>(Boolean::class.java) == true
                    if (connected) {
                        // Log.d("CHAT", "connected");
                        if (isConnectivityDisconnected) {
                            setConnectivityRegainInFirebase()
                            stopConnectivityTimer()
                            isConnectivityDisconnected = false
                        }
                    } else {
                        // Log.d("CHAT", "not connected");
                        if (!isConnectivityDisconnected) {
                            setOnDisconnectListner()
                            startConnectivityTimer()
                            isConnectivityDisconnected = true
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    //Log.w("TAG", "Listener was cancelled");
                }
            })
    }

    private fun setOnDisconnectListner() {
        try {
            val channelId = callSId
            //Log.d("OnDisconnectListner", "setOnDisconnectListner channelId="+channelId);
            getFirebaseDatabase(channelId).child(CGlobalVariables.USER_DISCONNECT_TIME_FBD_KEY)
                .onDisconnect().setValue(System.currentTimeMillis())
        } catch (_: Exception) {
            //
        }
    }

    private fun setConnectivityRegainInFirebase() {
        try {
            val channelId = callSId
            Log.d("OnDisconnectListner", "setConnectivityRegainInFirebase channelId= $channelId")
            getFirebaseDatabase(channelId).child("UserConnRegainTime")
                .setValue(ServerValue.TIMESTAMP)
        } catch (_: Exception) {
            //
        }
    }

    private fun stopConnectivityTimer() {
        try {
            handler.removeCallbacks(connectivityRunnable)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val distance = event?.values?.getOrNull(0) ?: 10f
        if (distance < 3.0) {
            findViewById<View?>(R.id.viewInternetCallBlackScreen)?.visibility = View.VISIBLE
        } else {
            findViewById<View?>(R.id.viewInternetCallBlackScreen)?.visibility = View.GONE
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private var unableToConnectTimer: CountDownTimer? = null
    private fun startUnableToConnectTimer() {
        cancelEndCallScheduler(this)
        unableToConnectTimer = object : CountDownTimer(60000, 1000) {

            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                Log.e("testAiCAll:", "unableToConnectTimer onFinish() ")
                if (!isCallConnected) {
                    CUtils.fcmAnalyticsEvents(
                        CGlobalVariables.CALL_END_DUE_AI_ASTRO_NOT_CONNECTED,
                        AstrosageKundliApplication.currentEventType,
                        ""
                    )
                    endRemarks = CGlobalVariables.ASTROLGER_NOT_CONNECTED
                    isDisconnectBeforeConnect = true
                    showCallFailed()
                }

            }
        }.start()
    }


    override fun onStop() {
        super.onStop()
        Log.d("AiCAllTest", "onStop:()")
        isActivityInPIPMode = false
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        unableToConnectTimer?.cancel()
        LocalBroadcastManager.getInstance(this)
            .unregisterReceiver(mReceiverBackgroundLoginService)
    }

    fun startForegroundService() {
        val serviceIntent = Intent(this, AIVoiceCallingService::class.java)
        val bundle = Bundle().apply {
            putBoolean(CGlobalVariables.AGORA_CALL_MIC_STATUS, MIC_STATUS)
            putString(CGlobalVariables.AGORA_CALL_TYPE, CGlobalVariables.TYPE_VOICE_CALL)
            putString(CGlobalVariables.AGORA_CALLS_ID, callSId)
            putString(CGlobalVariables.AGORA_TOKEN, token)
            putBoolean("astrologer_connected", isCallConnected)
            putString(CGlobalVariables.ASTROLOGER_NAME, astrologerName)
            putString(CGlobalVariables.ASTROLOGER_PROFILE_URL, astrologerProfileUrl)
            putString(CGlobalVariables.AGORA_CALL_DURATION, timeRemaining)
            putString(CGlobalVariables.ASTROLOGER_ID, astrologerId)
        }
        serviceIntent.putExtras(bundle)
        ContextCompat.startForegroundService(this, serviceIntent)
    }

    private val finishReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if (CGlobalVariables.FINSIH_ACTIVITY_ACTION == intent.action) {
                if (isCallConnected) {
                    voiceCallCompleted(CGlobalVariables.COMPLETED, CGlobalVariables.USER_ENDED, true)
                }else{
                    voiceCallCompleted(CGlobalVariables.FAILED, CGlobalVariables.USER_ENDED, true)
                }
            }
            if (CGlobalVariables.FINISH_CALL_ACTIVITY_ACTION == intent.action) {
                val remarks = intent.getStringExtra(CGlobalVariables.REMARKS) ?: ""

                if (isCallConnected) {
                    voiceCallCompleted(CGlobalVariables.COMPLETED, "$remarks From yellow strip", true)
                }else{
                    voiceCallCompleted(CGlobalVariables.FAILED, "$remarks From yellow strip", true)
                }
            }
        }
    }

    private val micOnOffReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if (CGlobalVariables.MIC_ON_OFF_ACTIVITY_ACTION == intent.action) {
                if (CGlobalVariables.MIC_MUTE_STATUS) {
                    room?.setMicrophoneMute(false)
                    ivCallMic.isActivated = false
                    ivCallMic.setColorFilter(ContextCompat.getColor(this@AIVoiceCallingActivity, R.color.no_change_white))
                    CGlobalVariables.MIC_MUTE_STATUS = false
                } else {
                    room?.setMicrophoneMute(true)
                    ivCallMic.isActivated = true
                    ivCallMic.setColorFilter(ContextCompat.getColor(this@AIVoiceCallingActivity, R.color.white))
                    CGlobalVariables.MIC_MUTE_STATUS = true
                }
            }
        }
    }

    fun showDevicesList(list: List<AudioDevice>){
//        Log.d("testAiCAll", "showDevicesList: audion device list = $list" )
        val adapter = object: ArrayAdapter<AudioDevice>(this,0,list){
            override fun getView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                var itemView = convertView
                if(itemView == null){
                    itemView = layoutInflater.inflate(R.layout.audio_device_item,parent,false)
                }
                val item = getItem(position)
                val img = itemView.findViewById<ImageView>(R.id.device_img)
                val tvDeviceName = itemView.findViewById<TextView>(R.id.device_name)

                when(item) {
                    is AudioDevice.BluetoothHeadset -> {
                        img.setImageResource(R.drawable.ic_bluetooth)
                        tvDeviceName.text = item.name
                    }

                    is AudioDevice.Earpiece -> {
                        img.setImageResource(R.drawable.ic_iphone)
                        tvDeviceName.text = item.name
                    }

                    is AudioDevice.Speakerphone -> {
                        img.setImageResource(R.drawable.speaker)
                        tvDeviceName.text = item.name
                    }

                    is AudioDevice.WiredHeadset -> {
                        img.setImageResource(R.drawable.ic_support_headset)
                        tvDeviceName.text = item.name
                    }
                    null -> {

                    }
                }
                return itemView
            }
        }
        audioDeviceListView.visibility = View.VISIBLE
        audioDeviceListView.adapter = adapter

        audioDeviceListView.setOnItemClickListener({parent, view, position, id ->
            val selectedDevice = list[position]
            room?.audioSwitchHandler?.selectDevice(selectedDevice)
            audioDeviceListView.visibility = View.GONE
            when(selectedDevice){
                is AudioDevice.BluetoothHeadset -> ivSpeaker.setImageResource(R.drawable.ic_bluetooth)
                is AudioDevice.Earpiece -> ivSpeaker.setImageResource(R.drawable.ic_iphone)
                is AudioDevice.Speakerphone -> ivSpeaker.setImageResource(R.drawable.ic_speaker)
                is AudioDevice.WiredHeadset -> ivSpeaker.setImageResource(R.drawable.ic_support_headset)
            }
        })
    }

    var callAgainLayout: LinearLayout ?= null
    fun showCallAgainPopUnder(servicePrice: Int, actualServicePrice: String, offerType: String){
        val tvCallAgainMsg = findViewById<TextView>(R.id.tv_call_again_msg)
        val button = findViewById<Button>(R.id.btn_continue_call)
        FontUtils.changeFont(this, tvCallAgainMsg, CGlobalVariables.FONTS_OPEN_SANS_REGULAR)
        FontUtils.changeFont(this, button, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD)
        guideview.visibility = View.GONE
        progressbar?.visibility = View.GONE
        llProfile.visibility = View.GONE
        callAgainLayout?.visibility = View.VISIBLE
        val msg = if(offerType == CGlobalVariables.INTRO_OFFER_TYPE_FREE){
            getString(R.string.continue_call_msg_for_free, CUtils.getUserFullName(this),resources.getString(R.string.title_free))
        }else{
            getString(R.string.call_again_text, CUtils.getUserFullName(this), actualServicePrice,servicePrice)
        }
        val actualPrice = if(offerType == CGlobalVariables.INTRO_OFFER_TYPE_FREE){
            resources.getString(R.string.title_free).trim()
        }else{
            resources.getString(R.string.price_min, actualServicePrice)
        }
        val spannable = SpannableString(msg)
        val rateStart = msg.indexOf(actualPrice)
        val rateEnd = rateStart + actualPrice.length

        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorPrimary_day_night)),
            rateStart,
            rateEnd,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            StyleSpan(Typeface.BOLD),
            rateStart,
            rateEnd,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        if(offerType != CGlobalVariables.INTRO_OFFER_TYPE_FREE) {
            val servicePrice = getString(R.string.astrologer_rate, servicePrice)
            val oldRateStart = msg.indexOf(servicePrice)
            val oldRateEnd = oldRateStart + servicePrice.length

            spannable.setSpan(
                StrikethroughSpan(),
                oldRateStart,
                oldRateEnd,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannable.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(this, R.color.grey_7c)),
                oldRateStart,
                oldRateEnd,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannable.setSpan(
                RelativeSizeSpan(0.8f),
                oldRateStart,
                oldRateEnd,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        tvCallAgainMsg.text = spannable

        button.setOnClickListener {
            if(AstrosageKundliApplication.selectedAstrologerDetailBean == null)
                return@setOnClickListener


            if (!TextUtils.isEmpty(offerType)) {
                AstrosageKundliApplication.selectedAstrologerDetailBean.useIntroOffer=true
                AstrosageKundliApplication.selectedAstrologerDetailBean.isFreeForCall= true
            }else{
                AstrosageKundliApplication.selectedAstrologerDetailBean.useIntroOffer=false
                AstrosageKundliApplication.selectedAstrologerDetailBean.isFreeForCall= false
            }
            val astrologerBean = AstrosageKundliApplication.selectedAstrologerDetailBean
            AstrosageKundliApplication.chatAstrologerDetailBeanAfterRecharge = astrologerBean
            if (astrologerBean != null) {
                val userDetails = CUtils.getUserSelectedProfileFromPreference(this)
                astrologerBean.callSource = CGlobalVariables.AI_CALLING_ACTIVITY_CALL_AGAIN_BTN
                ChatUtils.getInstance(this).connectAIVoiceCall(astrologerBean, userDetails)
                //finish()
                CUtils.fcmAnalyticsEvents(
                    CGlobalVariables.AI_CALL_AIGAIN_CLICK,
                    AstrosageKundliApplication.currentEventType,
                    ""
                )
            }
        }
    }

    private fun registerReceiverBackgroundLogin() {
        try {
            LocalBroadcastManager.getInstance(this).registerReceiver(
                mReceiverBackgroundLoginService,
                IntentFilter(CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN)
            )
        } catch (e: Exception) {
            //
        }
    }

    private val mReceiverBackgroundLoginService: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            try {
                LocalBroadcastManager.getInstance(this@AIVoiceCallingActivity)
                    .unregisterReceiver(mReceiverBackgroundLoginService)
                val currentOfferType = CUtils.getCallChatOfferType(this@AIVoiceCallingActivity)

                if (isCallDisconnected) {
                    if (isFreeCall && currentOfferType.contains(CGlobalVariables.REDUCED_PRICE_OFFER)) { //in case of user took complete free chat and now got REDUCEDPRICE
                        CUtils.callLocalNotificationForNewUser(this@AIVoiceCallingActivity)
                        showRechargeAfterFreeChat()
                        statusPriceApi(offerType = currentOfferType)

                    }else {
                        //in case of user took incomplete REDUCEDPRICE chat and now has no offer
                        statusPriceApi(currentOfferType)
                    }
                }
            } catch (e: Exception) {
                //
            }
        }
    }

    fun statusPriceApi(offerType: String){
        val call  = RetrofitClient
                        .getInstance()
                        .create(ApiList::class.java)
                        .getAstrologerStatusPrice(getAstroStatusParams(astrologerId,offerType))

        call.enqueue(object : Callback<ResponseBody>{
            override fun onResponse(
                p0: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) {
                try {
                    if (response.isSuccessful) {
                        val myResponse = response.body()?.string()
                        if(myResponse != null) {
                            val jsonObject = JSONObject(myResponse)
                            val servicePrice = jsonObject.getString("servicePrice")
                            val actualServicePriceInt = jsonObject.getString("actualServicePriceInt")
                            val minbalrequired = jsonObject.getString("minbalrequired")

                            showCallAgainPopUnder(
                                actualServicePriceInt.toInt(),
                                servicePrice,
                                offerType
                            )
                        }
                    }
                }catch (e: Exception){
                    CUtils.showSnackbar(
                        parentLayout,
                        getResources().getString(R.string.something_wrong_error),
                        this@AIVoiceCallingActivity
                    )
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                p0: Call<ResponseBody?>,
                p1: Throwable
            ) {
                CUtils.showSnackbar(
                    parentLayout,
                    getResources().getString(R.string.something_wrong_error),
                    this@AIVoiceCallingActivity
                )            }

        })
    }


    fun getAstroStatusParams(astroId: String?, currentOfferType: String?): Map<String, String> {
        val headers = HashMap<String, String>()
        headers[CGlobalVariables.APP_KEY] = CUtils.getApplicationSignatureHashCode(this)

        val isLogin = CUtils.getUserLoginStatus(this)
        try {
            if (isLogin) {
                headers[CGlobalVariables.PHONE_NO] = CUtils.getUserID(this)
                headers[CGlobalVariables.COUNTRY_CODE] = CUtils.getCountryCode(this)
            } else {
                headers[CGlobalVariables.PHONE_NO] = ""
                headers[CGlobalVariables.COUNTRY_CODE] = ""
            }
        } catch (e: Exception) {
            // handle silently as in original code
        }
        headers[CGlobalVariables.ASTROLOGER_ID] = astroId ?: ""
        headers[CGlobalVariables.PACKAGE_NAME] = CUtils.getAppPackageName(this)
        headers[CGlobalVariables.APP_VERSION] = BuildConfig.VERSION_NAME
        headers[CGlobalVariables.DEVICE_ID] = CUtils.getMyAndroidId(this)
        headers[CGlobalVariables.LANG] = CUtils.getLanguageKey(LANGUAGE_CODE)
        headers[CGlobalVariables.KEY_USER_ID] = CUtils.getUserIdForBlock(this)
        headers[CGlobalVariables.OFFER_TYPE] = currentOfferType ?: ""

        if (!TextUtils.isEmpty(currentOfferType)) {
            headers.put(CGlobalVariables.USE_INTRO_OFFER, "1")
        } else {
            headers.put(CGlobalVariables.USE_INTRO_OFFER, "2")
        }
        return headers
    }
    var progressbar: ProgressBar?=null
    fun showEndCallPopup() {
        AstrosageKundliApplication.isEndChatCompleted = true
        progressbar = findViewById(R.id.progress_bar)
        progressbar?.visibility = View.VISIBLE
        guideview.visibility = View.GONE
        lowBalLayout.visibility = View.GONE
        getAstrologerFeedbackStatus()
    }

    fun getAstrologerFeedbackStatus() {
        val headers = HashMap<String, String>().apply {
            put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(this@AIVoiceCallingActivity))
            put(CGlobalVariables.PHONE_NO, CUtils.getUserID(this@AIVoiceCallingActivity))
            put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(this@AIVoiceCallingActivity))
            put(CGlobalVariables.ASTROLOGER_ID, astrologerId?:"")
            put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(this@AIVoiceCallingActivity))
            put(CGlobalVariables.APP_VERSION, BuildConfig.VERSION_NAME)
            put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(this@AIVoiceCallingActivity))
            put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE))
            put(CGlobalVariables.KEY_USER_ID, CUtils.getUserIdForBlock(this@AIVoiceCallingActivity))
            put("languagecode", LANGUAGE_CODE.toString())
            put("name", CUtils.getUserFullName(this@AIVoiceCallingActivity))
        }

        val api = RetrofitClient.getInstance().create<ApiList?>(ApiList::class.java)
        val call = api.getAstrologerFeedbackStatus(headers)
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) {
                try {
                    val myResponse = response.body()?.string()
                    val jsonObject = JSONObject(myResponse!!)
                    val isFeedbackEnabled = jsonObject.optBoolean("enablefeedbacks")

                    if (isFeedbackEnabled) {
                        //Log.e("TestFreeChat", " showRatingDialogToUser");
                        showRatingDialogToUser()
                    }
                    if (!isFreeCall) {
                        statusPriceApi("")
                    }
                } catch (e: Exception) {
                    //
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                //Log.e("TestFreeChat", " AF onFailure="+t);
            }
        })
    }

    var feedbackDialog: FeedbackDialog? = null
    fun showRatingDialogToUser() {
        try {
            if (TextUtils.isEmpty(astrologerId)) {
                return
            }
            if (feedbackDialog != null && feedbackDialog!!.isVisible) {
                return
            }
            feedbackDialog = FeedbackDialog(
                CGlobalVariables.CON_TYPE_CALL,
                AIChatWindowActivity.channelIdForNps,
                supportFragmentManager,
                AstrosageKundliApplication.selectedAstrologerDetailBean
            )
            feedbackDialog!!.show(supportFragmentManager, "FeedbackDialog")
            CUtils.fcmAnalyticsEvents(
                CGlobalVariables.FBA_ASTRO_DETAIL_FEEDBACK_BTN,
                CGlobalVariables.SHOW_DIALOG_EVENT,
                ""
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    var rechargePopUpAfterFreeChat: RechargePopUpAfterFreeChat? = null
    fun showRechargeAfterFreeChat() {
        try {
            if (feedbackDialog != null && feedbackDialog!!.isVisible()) {
                return
            }
            if (rechargePopUpAfterFreeChat != null && rechargePopUpAfterFreeChat!!.isVisible) {
                return
            }
            CUtils.fcmAnalyticsEvents(
                CGlobalVariables.FBA_OPEN_NEXT_OFFER_RECHARGE_DIALOG,
                CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, ""
            )
            rechargePopUpAfterFreeChat = RechargePopUpAfterFreeChat(CGlobalVariables.AIVOICECALLINGACTIVITY)
            rechargePopUpAfterFreeChat!!.show(
                supportFragmentManager,
                "RechargePopUpAfterFreeChat"
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setUserAcceptedOnFirebase(channel: String?){
        getFirebaseDatabase(channel).child("UserAccepted").setValue("true")
        getFirebaseDatabase(channel).child("UserAcceptedTime").setValue(ServerValue.TIMESTAMP)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("testAiCAll", "onDestroy: ")
        stopSound(beepSound)
        removeUserIdeal()
        handler.removeCallbacks(connectivityRunnable)
        audioManager.unregisterAudioDeviceCallback(audioDeviceCallback)
        LocalBroadcastManager.getInstance(this).unregisterReceiver(micOnOffReceiver)
        LocalBroadcastManager.getInstance(this).unregisterReceiver(finishReceiver)
        soundPool?.release()
    }

    companion object{
        const val CALL_END_SCHEDULER = "AI_CALL_END_SCHEDULER"
        @JvmField
        var isActivityInPIPMode = false

        @JvmStatic
        fun startEndCallScheduler(context: Context, callSId: String, astrologerId: String){
            val inputData = Data.Builder()
                .putString(CGlobalVariables.AGORA_CALLS_ID, callSId)
                .putString(CGlobalVariables.ASTROLOGER_ID, astrologerId)
                .build()
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val endCallScheduler = OneTimeWorkRequest.Builder(AiVoiceCallObserverService::class.java)
                .setInputData(inputData)
                .setInitialDelay(1, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .addTag(CALL_END_SCHEDULER)
                .build()

            WorkManager.getInstance(context.applicationContext).enqueue(endCallScheduler)
        }

        @JvmStatic
        fun cancelEndCallScheduler(context: Context) {
            WorkManager.getInstance(context.applicationContext)
                .cancelAllWorkByTag(CALL_END_SCHEDULER)
        }
    }

    var callEndTimer: CountDownTimer? = null

    fun startCallEndTimer(){
        callEndTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(p0: Long) {
                //Log.d("testAiCAll", "onTick: remaing time : $p0")
            }

            override fun onFinish() {
                voiceCallCompleted(CGlobalVariables.COMPLETED, CGlobalVariables.CALL_END_DUE_TO_ASTROLOGER_DISCONNECTE, true)
            }

        }
        callEndTimer!!.start()
    }
    /**
     * Attaches the base context to the activity.
     * This method is called before the activity is attached to the application.
     * It is used to update the base context with the user's selected language.
     */
    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences(
            com.ojassoft.astrosage.utils.CGlobalVariables.MYKUNDLI_LANGUAGE_PREFS_NAME,
            MODE_PRIVATE
        )
        val langCode = prefs.getInt(
            com.ojassoft.astrosage.utils.CGlobalVariables.APP_PREFS_AppLanguage,
            com.ojassoft.astrosage.utils.CGlobalVariables.ENGLISH
        )
        val code = com.ojassoft.astrosage.utils.CUtils.getLanguageKey(langCode)
        super.attachBaseContext(wrap(newBase, code))
    }

    /**
     * Wraps the context with the specified language code.
     * This method is called during the activity's creation and attaches the base context with the user's
     * selected language.
     */
    private fun wrap(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        return context.createConfigurationContext(config)
    }

    /**
     *
     */
    fun cancelRechargeAfterChat() {
        ChatUtils.getInstance(this).cancelRechargeAfterChat(
            AIChatWindowActivity.channelIdForNps,
            "INSUFFICIENT_DIALOG_CANCEL_BTN"
        )

    }
    private var bottomServiceListUsedFor: String? = null

    fun openWalletScreen(openFrom: String) {
        if (openFrom == CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT) {
            CUtils.fcmAnalyticsEvents(
                CGlobalVariables.FBA_AI_CHAT_WINDOW_LOW_BALANCE_OPEN_WALLET,
                CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN,
                ""
            )
            com.ojassoft.astrosage.utils.CUtils.createSession(
                this,
                CGlobalVariables.AI_CHAT_WINDOW_INSUFFICIANT_BALANCE_RECHARGE_PARTNER_ID
            )
        } else {
            CUtils.fcmAnalyticsEvents(
                CGlobalVariables.FBA_AI_CHAT_WINDOW_LOW_BALANCE_SUBSCRIBE_OPEN_WALLET,
                CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN,
                ""
            )
            com.ojassoft.astrosage.utils.CUtils.createSession(
                this,
                CGlobalVariables.AI_CHAT_WINDOW_SUBSCRIBE_INSUFFICIANT_BALANCE_RECHARGE_PARTNER_ID
            )
        }
        openWalletScreen()
//        if (openFrom == CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT) {
//            if (CUtils.getCountryCode(this) == com.ojassoft.astrosage.utils.CGlobalVariables.COUNTRY_CODE_IND) {
//                //intent = new Intent(AIChatWindowActivity.this, MiniPaymentInformationActivity.class);
//                bottomServiceListUsedFor =
//                    com.ojassoft.astrosage.utils.CGlobalVariables.CONTINUE_CHAT
//                    openQuickRechargeSheet()
//            } else {
//                openWalletScreen()
//            }
//        } else {
//            openWalletScreen()
//        }
    }
    val quickRechargeBottomSheet: QuickRechargeBottomSheet by lazy { QuickRechargeBottomSheet.getInstance()}
    private fun openQuickRechargeSheet() {
        try {
            if(quickRechargeBottomSheet.isVisible) return
            val bundle = Bundle()
            bundle.putString(
                com.ojassoft.astrosage.utils.CGlobalVariables.BOTTOMSERVICELISTUSEDFOR,
                bottomServiceListUsedFor
            )
            bundle.putString(CGlobalVariables.CALLING_ACTIVITY, CGlobalVariables.AIVOICECALLINGACTIVITY)
            bundle.putString("minBalanceNeededText", "")
            quickRechargeBottomSheet.arguments = bundle
            quickRechargeBottomSheet.show(supportFragmentManager, QuickRechargeBottomSheet.TITLE)
        } catch (e: java.lang.Exception) {
            //
        }
    }

    private fun openWalletScreen() {
        val intent = Intent(
            this,
            WalletActivity::class.java
        )
        intent.putExtra(CGlobalVariables.CALLING_ACTIVITY, CGlobalVariables.AIVOICECALLINGACTIVITY)
        startActivity(intent)
    }

    fun udatePaymentStatusOnServer(
        containerLayout: View?,
        orderStatus: String?,
        orderID: String?,
        amount: String?,
        razorpayid: String?
    ) {
        //Log.d("PaymentStatus", "udatePaymentStatusOnserver()");
       // this.amount = amount
        if (!CUtils.isConnectedWithInternet(this)) {
            CUtils.showSnackbar(
                containerLayout, resources.getString(R.string.no_internet), this
            )
        } else {
            if (pd == null) pd = CustomProgressDialog(this)
            pd!!.show()
            pd!!.setCancelable(false)

            val api = RetrofitClient.getInstance().create(ApiList::class.java)
            val call =
                api.walletRecharge(getParamsForWalletRecharge(orderStatus, orderID, razorpayid))
            call.enqueue(object : Callback<ResponseBody?> {
                override fun onResponse(
                    call: Call<ResponseBody?>, response: Response<ResponseBody?>
                ) {
                    hideProgressBar()
                    startPrefetchDataService()
                    //Log.e("TestFreeChat", "chatAstrologerDetailBeanAfterRecharge1="+AstrosageKundliApplication.chatAstrologerDetailBeanAfterRecharge);
                    if (AstrosageKundliApplication.chatAstrologerDetailBeanAfterRecharge != null) { //initiate chat automatically in case of insufficient balance dialog
                        AstrosageKundliApplication.selectedAstrologerDetailBean =
                            AstrosageKundliApplication.chatAstrologerDetailBeanAfterRecharge
                        val astrologerBean = AstrosageKundliApplication.selectedAstrologerDetailBean
                        if (astrologerBean != null) {
                            val userDetails =
                                CUtils.getUserSelectedProfileFromPreference(this@AIVoiceCallingActivity)
                            ChatUtils.getInstance(this@AIVoiceCallingActivity)
                                .connectAIVoiceCall(astrologerBean, userDetails)
                            AstrosageKundliApplication.chatAstrologerDetailBeanAfterRecharge = null
                        }
                    }
                }

                override fun onFailure(p0: Call<ResponseBody?>, p1: Throwable) {
                    //TODO("Not yet implemented")
                }
            })
        }
    }

    private fun getParamsForWalletRecharge(
        orderStatus: String?,
        orderId: String?,
        razorpayid: String?
    ): Map<String, String> {
        val headers = java.util.HashMap<String, String>()
        try {
            headers["key"] = CUtils.getApplicationSignatureHashCode(
                this
            )
            headers["od"] = orderId ?: ""
            headers["isSucess"] = orderStatus?: ""
            headers["paycurr"] = "INR"
            headers["razorpayid"] = razorpayid?: ""

            //headers.put("callfrom", "admin");
        } catch (e: java.lang.Exception) {
        }
        return CUtils.setRequiredParams(headers)
    }

    private fun startPrefetchDataService() {
        try {
            val intentService = Intent(
                this,
                PreFetchDataservice::class.java
            )
            startService(intentService)
        } catch (e:Exception) {
        }
    }
    var dialog:Dialog? = null
    fun openHoverHelpDialog() {
        try {
            val alphaAnimation: Animation = AlphaAnimation(0.0f, 1.0f)
            val scaleAnimation: Animation = ScaleAnimation(
                0.1f, 1.0f, 0.1f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f
            )
            scaleAnimation.setDuration(500)
            alphaAnimation.setDuration(500)
            dialog = Dialog(this)
            val layoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = layoutInflater.inflate(R.layout.layout_call_profile_change_highlight, null, false)
            val window = dialog?.window
            window?.requestFeature(Window.FEATURE_NO_TITLE)
            dialog?.setContentView(view)
            view.startAnimation(alphaAnimation)
            view.startAnimation(scaleAnimation)
            window?.setBackgroundDrawable(
                Color.TRANSPARENT.toDrawable()
            )
            view.setOnClickListener {
                dialog?.dismiss()
            }
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(window?.attributes)
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.MATCH_PARENT
            dialog?.show()
            window?.setAttributes(lp)
            Handler(Looper.getMainLooper()).postDelayed({
                dialog?.dismiss()
            },15000)
        } catch (e: java.lang.Exception) {
            //
        }
    }
    fun millisToMMSS(millis: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
        val seconds = (TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(minutes))

        return String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds)
    }
    private fun showLowBalanceLayout(second:Int){
        lowBalLayout.visibility = View.VISIBLE
        tvLowBalDesc.text = resources.getString(R.string.call_ends_in_txt,second)
        btnRecharge.setOnClickListener {
            CUtils.fcmAnalyticsEvents(
                com.ojassoft.astrosage.utils.CGlobalVariables.AI_CALL_EXTEND_BTN_CALICK,
                CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,
                ""
            )

            bottomServiceListUsedFor = com.ojassoft.astrosage.utils.CGlobalVariables.EXTEND_CHAT
            openQuickRechargeSheet()
        }
    }

    var timeTakenForRecharge:Long

    fun gotoPaymentInfoActivity(mSelectedPosition: Int, walletAmountBean: WalletAmountBean?) {
        //isQuickRechargeClicked = true
        quickRechargeBottomSheet.dismiss()
        if (countDownTimer != null) {
            countDownTimer!!.cancel()
        }
        removeUserIdeal()
        countDownTimer = null
        callDuration = millisToMMSS(CGlobalVariables.callTimerTime)
        stopService(Intent(this, AIVoiceCallingService::class.java))
        timeTakenForRecharge = Calendar.getInstance().getTimeInMillis()
        Log.d("quickrecharge","gotoPaymentInfoActivity="+CGlobalVariables.chatTimerTime);
        val bundle = Bundle()
        bundle.putSerializable(CGlobalVariables.WALLET_INFO, walletAmountBean)
        bundle.putInt(CGlobalVariables.SELECTED_POSITION, mSelectedPosition)
        bundle.putString(CGlobalVariables.CALLING_ACTIVITY, CGlobalVariables.AIVOICECALLINGACTIVITY)
        bundle.putString(CGlobalVariables.ASTROLGER_URL_TEXT, astrologerProfileUrl)
        bundle.putString(CGlobalVariables.ASTROLOGER_MOB_NUMBER, "")
        bundle.putString(CGlobalVariables.CHANNEL_ID, AIChatWindowActivity.CHANNEL_ID)


        val intent = Intent(this, PaymentInformationActivity::class.java)
        intent.putExtras(bundle)
        startActivityForResult(intent, CGlobalVariables.REQUEST_FOR_QUICK_RECHARGE)
        removeUserIdeal()
    }

    fun quickRechargeVerifyParams(orderStatus: String?,orderId: String?, razorpayid: String?, extendedWaitTime: Long,
                                  phonepeid: String?, phonepeOrderId: String?,od: String?): Map<String, String>{
        val headers = HashMap<String?, String?>()
        try {
            headers.put("key", CUtils.getApplicationSignatureHashCode(this))
            headers.put("od", orderId)
            headers.put("isSucess", orderStatus)
            headers.put("paycurr", "INR")
            headers.put("razorpayid", razorpayid)
//            headers.put("rechargefromchat", "1")
//            headers.put(
//                "urltext",
//                AstrosageKundliApplication.selectedAstrologerDetailBean.getUrlText()
//            )
//            headers.put("channelid", AIChatWindowActivity.CHANNEL_ID)
//            headers.put("extendedwaittime", extendedWaitTime.toString())
            if (!TextUtils.isEmpty(od)) {
                headers.put(CGlobalVariables.ORDER_ID_PHONEPE, od)
                headers.put(CGlobalVariables.PHONEPE_ID, phonepeid)
                headers.put(CGlobalVariables.PHONEPE_ORDER_ID, phonepeOrderId)
            }
            //headers.put("callfrom", "admin");
        } catch (e: Exception) {
            //
        }
        return CUtils.setRequiredParams(headers)

    }


    private fun verifyRechargeAndUpdateTimer(orderStatus: String?, orderID: String?, rechargeAmount: String?, razorpayid: String?, extendedWaitTime: Long,
                                             phonepeid: String?, phonepeOrderId: String?, od: String?) {
        if (!CUtils.isConnectedWithInternet(this)) {
            CUtils.showSnackbar(
                parentLayout,
                getResources().getString(R.string.no_internet),
                this
            )
        } else {
            showProgressBar()
            val api = RetrofitClient.getInstance().create(ApiList::class.java)
            val call = api.walletRecharge(
                quickRechargeVerifyParams(
                    orderStatus,
                    orderID,
                    razorpayid,
                    extendedWaitTime,
                    phonepeid,
                    phonepeOrderId,
                    od
                )
            )

            call.enqueue(object : Callback<ResponseBody> {

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {

                    try {
                        hideProgressBar()
                        QuickRechargeBottomSheet.getInstance().dismiss()
                        //tvFreeChatBlinker.setVisibility(View.GONE)
                        val myResponse = response.body()?.string()
                        Log.d("quickrecharge", "myResponse: $myResponse")
                        if(myResponse != null) {
                            val jsonObject = JSONObject(myResponse)
                            val status = jsonObject.getString("status")
                            val msg = jsonObject.getString("msg")
                            if (status == "1") {
                                //Log.d("quickrecharge response", "1");
                                CUtils.fcmAnalyticsEvents(
                                    CGlobalVariables.GOOGLE_ANALYTICS_CHAT_WINDOW_CHAT_EXTEND_SUCCESS,
                                    CGlobalVariables.FIREBASE_EVENT_PAYMENT_SUCCESS,
                                    ""
                                )
                                /*
                                 * Commented for Now, Its for extend timer
                                 */
                                lowBalLayout.visibility = View.GONE
//                                val newChatDurationMillis = TimeUnit.SECONDS.toMillis(
//                                    jsonObject.getString("chatduration").toLong()
//                                )
                                //val newCallTimer = CGlobalVariables.callTimerTime + newChatDurationMillis

                                //show payment sucess dialog
                                val dialog = PaymentSucessfulDialog("" + rechargeAmount)
                                dialog.show(supportFragmentManager, "PaymentSucessfulDialog")

                                startTimer(CGlobalVariables.callTimerTime)
                                timeRemaining = millisToMMSS(CGlobalVariables.callTimerTime)
                                startForegroundService()

                            } else {
                                CUtils.fcmAnalyticsEvents(
                                    CGlobalVariables.GOOGLE_ANALYTICS_CHAT_WINDOW_CHAT_EXTEND_FAILED,
                                    CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED,
                                    ""
                                )
                                startTimer(CGlobalVariables.chatTimerTime)
                                startForegroundService()
                                CUtils.showSnackbar(parentLayout, msg, this@AIVoiceCallingActivity)
                            }
                        }
                    } catch (e: Exception) {
                        // if (llConnectAgain.getVisibility() != View.VISIBLE) {
                        startTimer(CGlobalVariables.chatTimerTime)
                        // }
                        /*if(!bottomSheetDialog.isShowing()){
                            initializingCountDownTimer(CGlobalVariables.chatTimerTime);
                        }*/
                    }
                }

                override fun onFailure(call: Call<ResponseBody?>, error: Throwable) {
                    hideProgressBar()
                    CUtils.showSnackbar(
                        parentLayout,
                        getResources().getString(R.string.something_wrong_error),
                        this@AIVoiceCallingActivity
                    )
                }
            })
        }
    }

    private fun updatePaymentStatusOnServerFailed(orderStatus: String?,orderID: String?,amount: String?,paymentMode: String?){
        val orderUrl = if (paymentMode.equals(CGlobalVariables.PAYTM, ignoreCase = true)) {
             CGlobalVariables.UPDATE_STATUS_RAZOR_PAY
        } else {
             CGlobalVariables.UPDATE_PAY_STATUS_ASKQUE
        }

        val headers = java.util.HashMap<String?, String?>()
        headers.put("key", CUtils.getApplicationSignatureHashCode(this))
        headers.put("amount", amount)
        headers.put("orderid", orderID)
        headers.put("paycurr", "INR")
        headers.put("status", orderStatus)
        headers.put("profile_Id", CUtils.getUserID(this))

        if (paymentMode.equals(CGlobalVariables.PAYTM, ignoreCase = true)) {
            headers.put("chatid", "")
        } else {
            headers.put("paymentid", "")
            headers.put("razorpayresponse", "0")
        }
        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(this))
        headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME)
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(this))

        val call = RetrofitClient.getInstance().create(ApiList::class.java).postRequest(orderUrl,headers)
        call.enqueue(object : Callback<ResponseBody>{
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                try {
                    val myResponse = response.body()?.string()
                    val array = JSONArray(myResponse)
                    val obj = array.getJSONObject(0)
                    val result = obj.getString("Result")

                    //for paytm  //1 success or fail update & 5 when s2s update server api
                    //result = 1 for succesfully updated and result =2 for data updated from server to server
                    if (result.equals("1") || result.equals("2")) {
                        //callback.getCallBack("1", callBack.POST_RAZORPAYSTATUS, "", "");
                    } else {
                        //result = 5 for data not succesfully updated
                        //callback.getCallBack("0", callBack.POST_RAZORPAYSTATUS, "", "");
                    }
                    val dialog = PaymentFailDialog()
                    dialog.show(supportFragmentManager, "PaymentFailDialog")

                }catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(
                p0: Call<ResponseBody>,
                err: Throwable
            ) {
                //TODO("Not yet implemented")
            }

        })

    }

}
