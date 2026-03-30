package com.ojassoft.astrosage.varta.dialog;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.utils.OnlineAstrologerAdapter;
import com.ojassoft.astrosage.varta.model.AstrologerDetailBean;
import com.ojassoft.astrosage.varta.ui.activity.AstrologerDescriptionActivity;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.ChatUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AstroBusyAlertDialog extends DialogFragment {
    private static final String ARG_ASTROLOGER_DETAIL = "arg_astrologer_detail";
    private static final String ARG_DIALOG_MESSAGE = "arg_dialog_message";
    private AstrologerDetailBean astrologerDetailBean;
    private AstrologerDetailBean astrologerDetailBeanNew;
    private boolean isAIAstroClicked;
    String msg;
    TextView astroBusyHeading;

    public AstroBusyAlertDialog() {
    }

    /**
     * Creates a new dialog instance with required arguments to survive fragment recreation.
     */
    public static AstroBusyAlertDialog newInstance(AstrologerDetailBean astrologerDetailBean, String msg) {
        AstroBusyAlertDialog dialog = new AstroBusyAlertDialog();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ASTROLOGER_DETAIL, astrologerDetailBean);
        args.putString(ARG_DIALOG_MESSAGE, msg);
        dialog.setArguments(args);
        return dialog;
    }

    private ArrayList<AstrologerDetailBean> randomAstrologerList;
    private ArrayList<AstrologerDetailBean> arrayListAiAstrologer, astrologerArrayList;
    private ArrayList<String> astrologerLanguageList;

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);

        View rootView = inflater.inflate(R.layout.layout_astro_busy_alert_new_dialog, container);
        readDialogArguments();
        setCancelable(false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        inti(rootView);
        return rootView;
    }

    /**
     * Reads previously saved fragment arguments and restores dialog state.
     */
    private void readDialogArguments() {
        Bundle args = getArguments();
        if (args == null) return;
        if (args.containsKey(ARG_ASTROLOGER_DETAIL)) {
            astrologerDetailBean = (AstrologerDetailBean) args.getSerializable(ARG_ASTROLOGER_DETAIL);
        }
        msg = args.getString(ARG_DIALOG_MESSAGE, msg);
    }

    private void inti(View view) {
        try {
            ImageView closeDialog = view.findViewById(R.id.iv_close_view);
//            LinearLayout mainView1 = view.findViewById(R.id.main_view_1);
//            LinearLayout mainView2 = view.findViewById(R.id.main_view_2);
//            mainView2.setVisibility(View.GONE);
//            mainView1.setVisibility(View.VISIBLE);
//            TextView messageTV = view.findViewById(R.id.tvNotifAlertMsg);
//            messageTV.setText(msg);
//            TextView chatInfoText = view.findViewById(R.id.text3);
//            Button chatNo = view.findViewById(R.id.chat_no);
//            Button chatYes = view.findViewById(R.id.chat_yes);
//            ImageView ivAstroImageChat = view.findViewById(R.id.ivAstroImageChat);
//            ImageView ivAIAstroImage = view.findViewById(R.id.ivAIAstroImage);
//            Glide.with(requireActivity()).load(R.drawable.astro_mr_krishnamurti).circleCrop().placeholder(R.drawable.ic_profile_view).into(ivAIAstroImage);
            TextView similarAiAstrologer = view.findViewById(R.id.chat_with_simi_ai_astrologer);
            astroBusyHeading = view.findViewById(R.id.astrologer_busy_heading);
            String astrologerName = "";
            if (astrologerDetailBean != null && astrologerDetailBean.getName() != null) {
                astrologerName = astrologerDetailBean.getName();
            }
            astroBusyHeading.setText(getString(R.string.oops_astrologer_busy, astrologerName));
//            ConstraintLayout similarAstrologer = view.findViewById(R.id.chat_with_similar_astrologer);
            TextView getNotificationAstrologerFree = view.findViewById(R.id.getNotificationAstrologerFree);
            getAiAstrologerList();
//            getAstrologerList();
            closeDialog.setOnClickListener(v -> {
                AstrosageKundliApplication.selectedAstrologerDetailBean = null;
                dismiss();
            });
            similarAiAstrologer.setOnClickListener(v -> {
                isAIAstroClicked = true;
                if (arrayListAiAstrologer != null && !arrayListAiAstrologer.isEmpty()) {
                    int aiAstroListSize = arrayListAiAstrologer.size();
                    for (int i = 0; i < aiAstroListSize; i++) {
                        // Log.d("testId",arrayListAiAstrologer.get(i).getAiAstrologerId()  + arrayListAiAstrologer.get(i).getName());
                        if (arrayListAiAstrologer.get(i).getAiAstrologerId().equals("4")) {
                            astrologerDetailBeanNew = arrayListAiAstrologer.get(i);
                            break;
                        }

                    }
                    if (astrologerDetailBeanNew != null) {
                        astrologerDetailBeanNew.setCallSource("AstroBusyAlertDialogAiChat");
                        AstrosageKundliApplication.selectedAstrologerDetailBean = astrologerDetailBeanNew;
                        com.ojassoft.astrosage.utils.CUtils.openAINotificationChatWindow(getActivity(), "","custom" , "4","My lagna", true);
                        dismiss();
                    }
                }
            });
//            similarAstrologer.setOnClickListener(v -> {
//                isAIAstroClicked = false;
//                if (randomAstrologerList != null && !randomAstrologerList.isEmpty()) {
//                    int aiAstroListSize = randomAstrologerList.size();
//                    int randomAstro = (int) (Math.random() * aiAstroListSize);
//                    astrologerDetailBeanNew = randomAstrologerList.get(randomAstro);
//                    String astroName = astrologerDetailBeanNew.getName();
//                    String astroServicePrice = astrologerDetailBeanNew.getServicePrice();
//                    String setTextString = getResources().getString(R.string.do_you_want_to_start_chat_at_u20b9_min_with_);
//                    String newTextString1 = setTextString.replace("##", astroServicePrice);
//                    String newTextString2 = newTextString1.replace("@@", astroName);
//                    String astrologerProfileUrl = "";
//                    if (astrologerDetailBeanNew.getImageFile() != null && !astrologerDetailBeanNew.getImageFile().isEmpty()) {
//                        astrologerProfileUrl = CGlobalVariables.IMAGE_DOMAIN + astrologerDetailBeanNew.getImageFile();
//                        Glide.with(requireActivity()).load(astrologerProfileUrl).circleCrop().placeholder(R.drawable.ic_profile_view).into(ivAstroImageChat);
//                    }
//                    chatInfoText.setText(newTextString2);
//                    mainView1.setVisibility(View.GONE);
//                    mainView2.setVisibility(View.VISIBLE);
//                }
//            });
            getNotificationAstrologerFree.setOnClickListener(v -> {
                sendNotification(astrologerDetailBean);
                AstrosageKundliApplication.selectedAstrologerDetailBean = null;
            });
//            chatNo.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mainView1.setVisibility(View.VISIBLE);
//                    mainView2.setVisibility(View.GONE);
//                }
//            });
//            chatYes.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (isAIAstroClicked) {
//                        astrologerDetailBeanNew.setCallSource("AstroBusyAlertDialogAiChat");
//                        AstrosageKundliApplication.chatMessagesHistory = null;
//                        AstrosageKundliApplication.statusMessageSetHistory = null;
//                        AstrosageKundliApplication.isChatAgainButtonClick = false;
//                        ChatUtils.getInstance(getActivity()).initAIChat(astrologerDetailBeanNew);
//                        dismiss();
//                    } else {
//                        astrologerDetailBeanNew.setCallSource("AstroBusyAlertDialogHumanChat");
//                        AstrosageKundliApplication.chatMessagesHistory = null;
//                        AstrosageKundliApplication.statusMessageSetHistory = null;
//                        AstrosageKundliApplication.isChatAgainButtonClick = false;
//                        ChatUtils.getInstance(getActivity()).initChat(astrologerDetailBeanNew);
//                        dismiss();
//                    }
//
//                }
//            });
        } catch (Exception e) {
            AstrosageKundliApplication.selectedAstrologerDetailBean = null;
        }

    }

    public void getAiAstrologerList() {
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.getAIAstrologerList(getParams());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    if (response.body() != null) {
                        String myResponse = response.body().string();
                        parseAstrologerList(myResponse);
                    }
                } catch (Exception e) {
                    //
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //
            }
        });
    }

    public HashMap<String, String> getParams() {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(getActivity()));
        boolean isLogin = CUtils.getUserLoginStatus(getActivity());
        String offerType = CUtils.getCallChatOfferType(getActivity());
        try {
            if (isLogin) {
                headers.put(CGlobalVariables.PHONE_NO, CUtils.getUserID(getActivity()));
                headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(getActivity()));
            } else {
                headers.put(CGlobalVariables.PHONE_NO, "");
                headers.put(CGlobalVariables.COUNTRY_CODE, "");
            }
        } catch (Exception e) {
        }
        headers.put(CGlobalVariables.OFFER_TYPE, offerType);
        headers.put(CGlobalVariables.FETCHALL, "1");
        headers.put(CGlobalVariables.PACKAGE_NAME, "" + BuildConfig.APPLICATION_ID);
        headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(getActivity()));
        headers.put(CGlobalVariables.PREF_SECOND_FREE_CHAT, "" + CUtils.isSecondFreeChat(getActivity()));
        return headers;
    }

    private void parseAstrologerList(String responseData) {

        if (arrayListAiAstrologer == null) {
            arrayListAiAstrologer = new ArrayList();
        } else {
            arrayListAiAstrologer.clear();
        }
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            if (jsonObject.length() > 0) {

                if (jsonObject.has("verifiedicon")) {
                    try {
                        CUtils.setVerifiedAndOfferImage(URLDecoder.decode(jsonObject.getString("verifiedicon"), "UTF-8"), URLDecoder.decode(jsonObject.getString("verifiedicondetail"), "UTF-8"),
                                URLDecoder.decode(jsonObject.getString("offericon"), "UTF-8"), URLDecoder.decode(jsonObject.getString("offericondetail"), "UTF-8"));
                    } catch (Exception ex) {

                    }
                }

                JSONArray jsonArray = jsonObject.getJSONArray("astrologers");
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        AstrologerDetailBean astrologerDetailBean = CUtils.parseAstrologerObject(object);
                        if (astrologerDetailBean == null) continue;
                        arrayListAiAstrologer.add(astrologerDetailBean);
                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getAstrologerList() {
        String response = CUtils.getAstroList();
        if (!TextUtils.isEmpty(response)) {
            parseAstrologerListHuman(response);
        } else {
            fetchAllAstrologer();
        }

    }

    private void fetchAllAstrologer() {
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.getAstrologerList(getParams());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    if (response.body() != null) {
                        String myResponse = response.body().string();
                        CUtils.saveAstroList(myResponse);
                        parseAstrologerListHuman(myResponse);
                    }
                } catch (Exception e) {
                    //
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //
            }
        });
    }

    private void parseAstrologerListHuman(String responseData) {
        parseAstrologerLanguages(astrologerDetailBean.getLanguage());
        try {
            if (astrologerArrayList == null) {
                astrologerArrayList = new ArrayList<>();
            } else {
                astrologerArrayList.clear();
            }
            JSONObject jsonObject = new JSONObject(responseData);
            if (jsonObject.length() > 0) {

                if (jsonObject.has("verifiedicon")) {
                    try {
                        CUtils.setVerifiedAndOfferImage(URLDecoder.decode(jsonObject.getString("verifiedicon"), "UTF-8"), URLDecoder.decode(jsonObject.getString("verifiedicondetail"), "UTF-8"),
                                URLDecoder.decode(jsonObject.getString("offericon"), "UTF-8"), URLDecoder.decode(jsonObject.getString("offericondetail"), "UTF-8"));
                    } catch (Exception ex) {

                    }
                }
                JSONArray jsonArray = jsonObject.getJSONArray("astrologers");
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        AstrologerDetailBean astrologerDetailBean = CUtils.parseAstrologerObject(object);
                        if (astrologerDetailBean == null) continue;
                        astrologerArrayList.add(astrologerDetailBean);
                    }
                    ArrayList<Integer> randomNumbers = new ArrayList<>();
                    while (randomNumbers.size() < 10) {
                        int rnd = new Random().nextInt(jsonArray.length());

                        for (int i = 0; i < astrologerLanguageList.size(); i++) {
                            if (!randomNumbers.contains(rnd) &&
                                    astrologerArrayList.get(rnd).getLanguage().contains(astrologerLanguageList.get(i))) {
                                randomNumbers.add(rnd);
                            }
                        }
                    }
                    randomAstrologerList = new ArrayList<>();
                    for (int i = 0; i < randomNumbers.size(); i++) {
                        JSONObject object = jsonArray.getJSONObject(randomNumbers.get(i));
                        AstrologerDetailBean astrologerDetailBean = CUtils.parseAstrologerObject(object);
                        if (astrologerDetailBean == null) continue;
                        randomAstrologerList.add(astrologerDetailBean);
                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // added by shreyans to show similar astrologers filtered by language
    private void parseAstrologerLanguages(String languages) {
        astrologerLanguageList = new ArrayList<>();
        languages = languages.replace(" ", "");
        if (!languages.contains(",")) {
            astrologerLanguageList.add(languages);
        } else {
            int length = languages.length();
            String newLang = "";
            for (int i = 0; i < length; i++) {
                String nextChar = String.valueOf(languages.charAt(i));
                if (!nextChar.equals(",")) {
                    newLang += String.valueOf(languages.charAt(i));
                } else {
                    astrologerLanguageList.add(newLang);
                    System.out.println(newLang);
                    newLang = "";
                }

                if (i == length - 1) {
                    astrologerLanguageList.add(newLang);
                    newLang = "";
                }
            }

        }
    }

    private void sendNotification(AstrologerDetailBean astrologerDetailBean) {
        if (astrologerDetailBean == null) {
            Context context = getContext();
            if (context != null) {
                Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if (CUtils.isConnectedWithInternet(getContext())) {
            /*StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.ADD_TO_QUEUE,
                    this, false, getQueueParams(astrologerDetailBean), ADD_TO_QUEUE_METHOD).getMyStringRequest();
            queue.add(stringRequest);*/

            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.getNotificationIfAstroBusy(getQueueParams(astrologerDetailBean));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        String myResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(myResponse);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("msg");
                        CUtils.showSnackbar(getActivity().findViewById(android.R.id.content), message, getContext());
                        dismiss();
                    } catch (Exception e) {
                        //
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                }
            });
        }
    }

    public Map<String, String> getQueueParams(AstrologerDetailBean astrologerDetailBean) {
        HashMap<String, String> headers = new HashMap<String, String>();
        Context context = AstrosageKundliApplication.getAppContext();
        String urlText = "";
        String astrologerId = "";
        if (astrologerDetailBean != null) {
            if (astrologerDetailBean.getUrlText() != null) {
                urlText = astrologerDetailBean.getUrlText();
            }
            if (astrologerDetailBean.getAstrologerId() != null) {
                astrologerId = astrologerDetailBean.getAstrologerId();
            }
        }
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(context));
        headers.put(CGlobalVariables.PHONE_NO, CUtils.getUserID(context));
        headers.put(CGlobalVariables.URL_TEXT, urlText);
        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(context));
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(context));
        headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.DEVICE_ID, "" + CUtils.getMyAndroidId(context));
        headers.put(CGlobalVariables.ASTROLOGER_ID, astrologerId);
        headers.put(CGlobalVariables.KEY_USER_ID, "" + CUtils.getUserIdForBlock(context));
        return headers;
    }
}
