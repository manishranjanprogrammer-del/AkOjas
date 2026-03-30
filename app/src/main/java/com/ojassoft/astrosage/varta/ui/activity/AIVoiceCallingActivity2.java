package com.ojassoft.astrosage.varta.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.mlkit.nl.languageid.LanguageIdentification;
import com.google.mlkit.nl.languageid.LanguageIdentifier;
import com.ojassoft.astrosage.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AIVoiceCallingActivity2 extends BaseActivity {
    private static final String TAG = "TestVoice";
    private static final int MAX_RETRY_ATTEMPTS = 3;
    private static final int MAX_CONTEXT_MESSAGES = 10;
    private static final long CALL_TIMEOUT_MS = 5 * 60 * 1000; // 5 minutes
    private static final long MIN_SILENCE_BEFORE_LISTEN = 300; // 300ms minimum silence before starting to listen
    private long lastSpeechEndTime = 0;

    // Call States
    private enum CallState {
        IDLE, CONNECTING, ACTIVE, ERROR, TERMINATED
    }

    private CallState currentCallState = CallState.IDLE;

    // Turn Management
    private boolean isAISpeaking = false;
    private boolean isUserSpeaking = false;
    private boolean requestInProgress = false;

    // Context Management
    private int retryAttempts = 0;
    private Timer callTimeoutTimer;
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    private TextView tvStatus;
    private Button btnStart, btnStop;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    private TextToSpeech textToSpeech;
    private String detectedLanguageCode = "en"; // fallback
    private boolean isListening = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_aivoice_calling2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeCallTimeout();

        tvStatus = findViewById(R.id.tv_status);
        btnStart = findViewById(R.id.btn_start);
        btnStop = findViewById(R.id.btn_stop);

// In onCreate, set the TTS listener ONCE
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(Locale.US);
                updateCallState(CallState.IDLE);
            } else {
                updateCallState(CallState.ERROR);
                showError("Text to speech initialization failed");
            }
        });
        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {
                isAISpeaking = true;
                Log.i(TAG, "AI started speaking");
                updateUIState();
            }

            @Override
            public void onDone(String s) {
                runOnUiThread(() -> {
                    isAISpeaking = false;
                    Log.i(TAG, "AI finished speaking");
                    lastSpeechEndTime = System.currentTimeMillis();
                    
                    if (currentCallState == CallState.ACTIVE) {
                        isUserSpeaking = false;
                        // Add a small delay before starting to listen to prevent false triggers
                        mainHandler.postDelayed(() -> {
                            if (!isAISpeaking && !isUserSpeaking) {
                                startListening();
                            }
                        }, MIN_SILENCE_BEFORE_LISTEN);
                    }
                    updateUIState();
                });
            }

            @Override
            public void onError(String s) {
                showError("TTS Error: " + s);
                isAISpeaking = false;
                updateUIState();
            }
        });
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onResults(Bundle results) {
                lastSpeechEndTime = System.currentTimeMillis();
                isUserSpeaking = false;
                isListening = false;
                
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    String spokenText = matches.get(0);
                    if (!spokenText.trim().isEmpty()) {
                        Log.i(TAG, "User finished speaking: " + spokenText);
                        tvStatus.setText("You said: " + spokenText);
                        detectLanguageAndSendToAI(spokenText);
                    } else {
                        Log.i(TAG, "Empty speech detected, restarting listening");
                        startListening();
                    }
                } else {
                    Log.i(TAG, "No speech detected, restarting listening");
                    startListening();
                }
            }

            // You can leave other overridden methods empty or with logs
            @Override
            public void onReadyForSpeech(Bundle params) {
            }

            @Override
            public void onBeginningOfSpeech() {
                Log.i(TAG, "User started speaking");
            }

            @Override
            public void onRmsChanged(float rmsdB) {
            }

            @Override
            public void onBufferReceived(byte[] buffer) {
            }

            @Override
            public void onEndOfSpeech() {
            }

            @Override
            public void onError(int error) {
                isListening = false;
                isUserSpeaking = false;
                
                // Only restart listening for non-critical errors
                switch (error) {
                    case SpeechRecognizer.ERROR_NO_MATCH:
                    case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                        Log.i(TAG, "Non-critical speech error: " + error + ", restarting listening");
                        startListening();
                        break;
                    default:
                        Log.e(TAG, "Critical speech error: " + error);
                        tvStatus.setText("Error: " + error);
                        handleError();
                        break;
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
            }

            @Override
            public void onEvent(int eventType, Bundle params) {
            }
        });

        btnStart.setOnClickListener(v -> {
            if (currentCallState == CallState.IDLE) {
                startCall();
            }
        });

        btnStop.setOnClickListener(v -> {
            endCall();
        });

    }

    private void startCall() {
        // Only start if we're in IDLE state
        if (currentCallState != CallState.IDLE) {
            Log.i(TAG, "Cannot start call in state: " + currentCallState);
            return;
        }
        
        // Reset all states and clear history
        stopListeningAndSpeaking();
        retryAttempts = 0;
        requestInProgress = false;
        lastSpeechEndTime = 0;
        
        // Start the call
        updateCallState(CallState.CONNECTING);
        resetCallTimeout();
        
        // Begin listening after a short delay to ensure clean start
        mainHandler.postDelayed(this::startListening, 300);
    }

    private void startListening() {
        if (currentCallState == CallState.TERMINATED) return;
        
        // Check if enough time has passed since last speech
        long timeSinceLastSpeech = System.currentTimeMillis() - lastSpeechEndTime;
        if (timeSinceLastSpeech < MIN_SILENCE_BEFORE_LISTEN) {
            Log.i(TAG, "Too soon to start listening, waiting for silence");
            mainHandler.postDelayed(this::startListening, MIN_SILENCE_BEFORE_LISTEN - timeSinceLastSpeech);
            return;
        }

        // Don't start if AI is speaking
        if (isAISpeaking) {
            Log.i(TAG, "Cannot start listening while AI is speaking");
            return;
        }

        // Reset speaking states
        isListening = true;
        isUserSpeaking = true;
        isAISpeaking = false;
        
        Log.i(TAG, "Ready for user input");
        updateUIState();
        try {
            speechRecognizer.startListening(speechRecognizerIntent);
        } catch (Exception e) {
            Log.e(TAG, "Error starting speech recognition", e);
            showError("Failed to start listening");
            handleError();
        }
    }

    private void endCall() {
        // First stop all ongoing activities
        stopListeningAndSpeaking();
        if (callTimeoutTimer != null) {
            callTimeoutTimer.cancel();
        }
        
        // Clear conversation and reset attempts
        retryAttempts = 0;
        requestInProgress = false;
        
        // Reset the call state to IDLE so we can start a new call
        updateCallState(CallState.IDLE);
        
        // Update UI to show ready state
        runOnUiThread(() -> {
            tvStatus.setText("Ready to start new call");
            btnStart.setEnabled(true);
            btnStop.setEnabled(false);
        });
    }

    private void stopListeningAndSpeaking() {
        // Reset all speaking states
        isListening = false;
        isUserSpeaking = false;
        isAISpeaking = false;
        lastSpeechEndTime = 0; // Reset speech timing
        
        // Stop speech recognition and TTS
        if (speechRecognizer != null) {
            try {
                speechRecognizer.stopListening();
                speechRecognizer.cancel();
            } catch (Exception e) {
                Log.e(TAG, "Error stopping speech recognizer", e);
            }
        }
        
        if (textToSpeech != null) {
            try {
                textToSpeech.stop();
            } catch (Exception e) {
                Log.e(TAG, "Error stopping TTS", e);
            }
        }
        
        updateUIState();
    }

    private void detectLanguageAndSendToAI(String text) {
        LanguageIdentifier languageIdentifier = LanguageIdentification.getClient();
        languageIdentifier.identifyLanguage(text)
                .addOnSuccessListener(langCode -> {
                    if (!langCode.equals("und")) {
                        detectedLanguageCode = langCode;
                        Log.d("Language", "Detected: " + langCode);
                    }
                    getAIResponse(text, detectedLanguageCode);
                })
                .addOnFailureListener(e -> {
                    Log.e("Language", "Detection failed", e);
                    getAIResponse(text, "en"); // fallback
                });
    }

    private void updateCallState(CallState newState) {
        Log.i(TAG, String.format("Call State: %s -> %s", currentCallState, newState));
        currentCallState = newState;
        updateUIState();
    }

    private void updateUIState() {
        runOnUiThread(() -> {
            String status = "";
            switch (currentCallState) {
                case IDLE:
                    status = "Ready to start call";
                    btnStart.setEnabled(true);
                    btnStop.setEnabled(false);
                    break;
                case CONNECTING:
                    status = "Connecting...";
                    btnStart.setEnabled(false);
                    btnStop.setEnabled(true);
                    break;
                case ACTIVE:
                    if (isAISpeaking) status = "AI is speaking...";
                    else if (isUserSpeaking) status = "Listening...";
                    else status = "Active";
                    btnStart.setEnabled(false);
                    btnStop.setEnabled(true);
                    break;
                case ERROR:
                    status = "Error occurred";
                    btnStart.setEnabled(true);
                    btnStop.setEnabled(false);
                    break;
                case TERMINATED:
                    status = "Call ended";
                    btnStart.setEnabled(true);
                    btnStop.setEnabled(false);
                    break;
            }
            tvStatus.setText(status);
        });
    }

    private void initializeCallTimeout() {
        callTimeoutTimer = new Timer();
    }

    private void resetCallTimeout() {
        if (callTimeoutTimer != null) {
            callTimeoutTimer.cancel();
            callTimeoutTimer = new Timer();
            callTimeoutTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mainHandler.post(() -> {
                        if (currentCallState == CallState.ACTIVE) {
                            showError("Call timed out");
                            endCall();
                        }
                    });
                }
            }, CALL_TIMEOUT_MS);
        }
    }

    private void handleError() {
        retryAttempts++;
        if (retryAttempts < MAX_RETRY_ATTEMPTS) {
            mainHandler.postDelayed(this::startListening, 2000);
        } else {
            updateCallState(CallState.ERROR);
        }
    }

    private void showError(String message) {
        Log.e(TAG, message);
        runOnUiThread(() -> tvStatus.setText("Error: " + message));
    }

    private void getAIResponse(String userSpeech, String languageCode) {
        OkHttpClient client = new OkHttpClient();
        String apiKey = "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";
        String prompt = "The user is speaking in language code: " + languageCode + ". Reply in same language. User said: " + userSpeech;

        MediaType mediaType = MediaType.parse("application/json");
        String json = "{ \"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \"" + prompt + "\"}] }";
        RequestBody body = RequestBody.create(mediaType, json);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .post(body)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TestVoice", "Failure", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    String aiText = extractAIText(responseBody);
                    Log.e("TestVoice", "Response: " + aiText);

                    runOnUiThread(() -> {
                        tvStatus.setText(aiText);
                        speakResponse(aiText, languageCode);
                    });
                } else {
                    Log.e("TestVoice", "Error: " + response.code());
                    startListening();
                }
            }
        });
    }

    private String extractAIText(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray choices = obj.getJSONArray("choices");
            JSONObject message = choices.getJSONObject(0).getJSONObject("message");
            return message.getString("content").trim();
        } catch (JSONException e) {
            e.printStackTrace();
            return "Error parsing response";
        }
    }


    private void speakResponse(String response, String langCode) {
        if (currentCallState != CallState.ACTIVE && currentCallState != CallState.CONNECTING)
            return;

        try {
            // Stop any ongoing speech recognition before AI speaks
            if (isListening) {
                speechRecognizer.stopListening();
                isListening = false;
            }
            isUserSpeaking = false;
            isAISpeaking = true;
            
            Locale locale = new Locale(langCode);
            textToSpeech.setLanguage(locale);
            textToSpeech.speak(response, TextToSpeech.QUEUE_FLUSH, null, "ttsId");

            // Update call state if we're still connecting
            if (currentCallState == CallState.CONNECTING) {
                updateCallState(CallState.ACTIVE);
            }

        } catch (Exception e) {
            Log.e(TAG, "Error in text-to-speech", e);
            isAISpeaking = false;
            startListening(); // Try to recover by letting user speak again
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        if (callTimeoutTimer != null) {
            callTimeoutTimer.cancel();
            callTimeoutTimer = null;
        }
        mainHandler.removeCallbacksAndMessages(null);
    }
}