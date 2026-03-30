package com.ojassoft.astrosage.varta.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Application;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;

import com.google.gson.Gson;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.model.AstrologerDetailBean;
import com.ojassoft.astrosage.varta.model.ConnectAgoraCallBean;
import com.ojassoft.astrosage.varta.model.UserProfileData;
import com.ojassoft.astrosage.varta.service.AgoraCallInitiateService;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Unit tests for the human voice-call branch and Agora service hand-off in {@link ChatUtils}.
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 33, application = Application.class)
public class ChatUtilsConnectHumanVoiceCallTest {

    static {
        String roboHome = "C:\\Users\\mmani\\.codex\\memories\\robolectric-home";
        new File(roboHome + "\\.m2\\repository").mkdirs();
        new File(roboHome + "\\tmp").mkdirs();
        System.setProperty("user.home", roboHome);
        System.setProperty("maven.repo.local", roboHome + "\\.m2\\repository");
        System.setProperty("java.io.tmpdir", roboHome + "\\tmp");
    }

    /**
     * Verifies Agora responses from `connectHumanVoiceCall` delegate to the Agora initializer path.
     */
    @Test
    public void connectHumanVoiceCall_agoraStatusDelegatesToAgoraInitializer() throws Exception {
        Activity activity = createActivityMock();
        ChatUtils chatUtils = spy(ChatUtils.getInstance(activity));
        UserProfileData userProfileData = new UserProfileData();
        AstrologerDetailBean astrologerDetailBean = createAstrologer();
        Map<String, String> fakeParams = new HashMap<>();
        fakeParams.put("type", "internetaudiocall");
        doReturn(fakeParams).when(chatUtils).getCallParams(userProfileData, "internetaudiocall", astrologerDetailBean);
        doNothing().when(chatUtils).initAudioVideoCallAfterResponse(any(JSONObject.class));

        try (MockedStatic<CUtils> cUtilsMock = org.mockito.Mockito.mockStatic(CUtils.class);
             MockedStatic<RetrofitClient> retrofitClientMock = org.mockito.Mockito.mockStatic(RetrofitClient.class)) {
            Callback<ResponseBody> callback = prepareConnectCall(chatUtils, activity, astrologerDetailBean, userProfileData, cUtilsMock, retrofitClientMock);

            callback.onResponse(mock(Call.class), Response.success(jsonBody("{\"status\":\"11\",\"msg\":\"CALL_INITIATED\",\"callsid\":\"AGORA123\"}")));

            verify(chatUtils).getCallParams(userProfileData, "internetaudiocall", astrologerDetailBean);
            verify(chatUtils).initAudioVideoCallAfterResponse(any(JSONObject.class));
        }
    }

    /**
     * Verifies network-phone responses do not enter the Agora initializer path.
     */
    @Test
    public void connectHumanVoiceCall_networkStatusDoesNotUseAgoraInitializer() throws Exception {
        Activity activity = createActivityMock();
        ChatUtils chatUtils = spy(ChatUtils.getInstance(activity));
        UserProfileData userProfileData = new UserProfileData();
        AstrologerDetailBean astrologerDetailBean = createAstrologer();
        Map<String, String> fakeParams = new HashMap<>();
        fakeParams.put("type", "internetaudiocall");
        doReturn(fakeParams).when(chatUtils).getCallParams(userProfileData, "internetaudiocall", astrologerDetailBean);
        doNothing().when(chatUtils).initAudioVideoCallAfterResponse(any(JSONObject.class));

        try (MockedStatic<CUtils> cUtilsMock = org.mockito.Mockito.mockStatic(CUtils.class);
             MockedStatic<RetrofitClient> retrofitClientMock = org.mockito.Mockito.mockStatic(RetrofitClient.class)) {
            Callback<ResponseBody> callback = prepareConnectCall(chatUtils, activity, astrologerDetailBean, userProfileData, cUtilsMock, retrofitClientMock);

            callback.onResponse(mock(Call.class), Response.success(jsonBody("{\"status\":\"1\",\"msg\":\"CALL_INITIATED\",\"callsid\":\"PHONE123\"}")));

            verify(chatUtils, never()).initAudioVideoCallAfterResponse(any(JSONObject.class));
        }
    }

    /**
     * Verifies the Agora launcher starts the Agora initiate service and preserves the response payload.
     */
    @Test
    public void startCallInitiateService_startsAgoraInitiateServiceWithPayload() throws Exception {
        Activity activity = createActivityMock();
        ChatUtils chatUtils = ChatUtils.getInstance(activity);
        chatUtils.consultationType = CGlobalVariables.TYPE_VOICE_CALL;
        JSONObject jsonObject = new JSONObject()
                .put("status", CGlobalVariables.AGORA_CALL_INITIATED)
                .put("msg", "CALL_INITIATED")
                .put("tokenid", "firebase-token")
                .put("callsid", "AGORA123")
                .put("participanttoken", "participant-token");

        try (MockedStatic<CUtils> cUtilsMock = org.mockito.Mockito.mockStatic(CUtils.class)) {
            chatUtils.startCallInitiateService(jsonObject);
        }

        ArgumentCaptor<Intent> intentCaptor = ArgumentCaptor.forClass(Intent.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            verify(activity).startForegroundService(intentCaptor.capture());
        } else {
            verify(activity).startService(intentCaptor.capture());
        }

        Intent serviceIntent = intentCaptor.getValue();
        assertNotNull(serviceIntent);
        assertEquals(AgoraCallInitiateService.class.getName(), serviceIntent.getComponent().getClassName());
        assertEquals(jsonObject.toString(), serviceIntent.getStringExtra(CGlobalVariables.AGORA_CALL_INITIATE_OBJECT));
        assertEquals(CGlobalVariables.TYPE_VOICE_CALL, serviceIntent.getStringExtra(CGlobalVariables.AGORA_CALL_TYPE));

        ConnectAgoraCallBean connectAgoraCallBean = new Gson().fromJson(jsonObject.toString(), ConnectAgoraCallBean.class);
        assertEquals(connectAgoraCallBean.getCallsid(), CUtils.connectAgoraCallBean.getCallsid());
        assertEquals(BuildConfig.APPLICATION_ID, serviceIntent.getComponent().getPackageName());
    }

    /**
     * Clears singleton state so each test runs with a fresh ChatUtils instance.
     */
    @After
    public void tearDown() throws Exception {
        setStaticField(ChatUtils.class, "chatUtils", null);
        setStaticField(ChatUtils.class, "activity", null);
        AstrosageKundliApplication.selectedAstrologerDetailBean = null;
        CUtils.connectAgoraCallBean = null;
    }

    /**
     * Prepares a mocked Retrofit response callback for the private human-call method.
     */
    private Callback<ResponseBody> prepareConnectCall(
            ChatUtils chatUtils,
            Activity activity,
            AstrologerDetailBean astrologerDetailBean,
            UserProfileData userProfileData,
            MockedStatic<CUtils> cUtilsMock,
            MockedStatic<RetrofitClient> retrofitClientMock) throws Exception {
        retrofit2.Retrofit retrofit = mock(retrofit2.Retrofit.class);
        ApiList apiList = mock(ApiList.class);
        @SuppressWarnings("unchecked")
        Call<ResponseBody> call = mock(Call.class);
        ArgumentCaptor<Callback<ResponseBody>> callbackCaptor = ArgumentCaptor.forClass(Callback.class);

        cUtilsMock.when(() -> CUtils.isConnectedWithInternet(activity)).thenReturn(true);
        retrofitClientMock.when(RetrofitClient::getInstance).thenReturn(retrofit);
        when(retrofit.create(ApiList.class)).thenReturn(apiList);
        when(apiList.connectCall(anyMap())).thenReturn(call);

        invokeConnectHumanVoiceCall(chatUtils, astrologerDetailBean, userProfileData);
        verify(call).enqueue(callbackCaptor.capture());

        return callbackCaptor.getValue();
    }

    /**
     * Invokes the private voice-call method so the tests can verify its response branching.
     */
    private void invokeConnectHumanVoiceCall(ChatUtils chatUtils, AstrologerDetailBean astrologerDetailBean, UserProfileData userProfileData) throws Exception {
        Method method = ChatUtils.class.getDeclaredMethod("connectHumanVoiceCall", AstrologerDetailBean.class, UserProfileData.class);
        method.setAccessible(true);
        method.invoke(chatUtils, astrologerDetailBean, userProfileData);
    }

    /**
     * Creates a lightweight mocked activity suitable for local unit tests.
     */
    private Activity createActivityMock() {
        Activity activity = mock(Activity.class);
        when(activity.getPackageName()).thenReturn(BuildConfig.APPLICATION_ID);
        doAnswer(invocation -> null).when(activity).runOnUiThread(any(Runnable.class));
        when(activity.startService(any(Intent.class))).thenAnswer(invocation -> {
            Intent intent = invocation.getArgument(0);
            return new ComponentName(BuildConfig.APPLICATION_ID, intent.getComponent().getClassName());
        });
        when(activity.startForegroundService(any(Intent.class))).thenAnswer(invocation -> {
            Intent intent = invocation.getArgument(0);
            return new ComponentName(BuildConfig.APPLICATION_ID, intent.getComponent().getClassName());
        });
        return activity;
    }

    /**
     * Creates a minimal astrologer fixture for the call-flow tests.
     */
    private AstrologerDetailBean createAstrologer() {
        AstrologerDetailBean astrologerDetailBean = new AstrologerDetailBean();
        astrologerDetailBean.setUrlText("astro-url");
        astrologerDetailBean.setCallSource("test");
        astrologerDetailBean.setFreeForCall(false);
        return astrologerDetailBean;
    }

    /**
     * Creates a JSON response body that mimics the Retrofit payload.
     */
    private ResponseBody jsonBody(String json) {
        return ResponseBody.create(json, MediaType.get("application/json"));
    }

    /**
     * Writes to private static fields so singleton test state stays isolated.
     */
    private void setStaticField(Class<?> type, String fieldName, Object value) throws Exception {
        Field field = type.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(null, value);
    }
}
