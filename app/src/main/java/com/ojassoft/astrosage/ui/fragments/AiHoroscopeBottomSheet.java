package com.ojassoft.astrosage.ui.fragments;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.varta.model.UserProfileData;
import com.ojassoft.astrosage.varta.ui.activity.ProfileForChat;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AiHoroscopeBottomSheet extends BottomSheetDialogFragment {

    Spinner tvRelationship, tvOccupation;
    int selectedRelationShipStatus = -1;
    int selectedOccupation = -1;
   String[] relationShipOptions = new String[]{"NotSpecified","Single",  "Married", "Divorced","In a Relationship", "Complicated", "Widowed"};
   String[] occupationOption = new String[] {"NotSpecified","Student","Businessperson","Employee","Retired","Housewife"};
   TextView tvReveal;
    String[] maritalStatusOptionsKey = new String[] {"NotSpecified","Single","Married","Divorced","In a Relationship", "Complicated", "Widowed"};
    String[] occupationOptionsKey = new String[] {"NotSpecified","Student","Businessperson","Employee","Retired","Housewife"};
    CustomProgressDialog pd;

    private OnUserProfileSavedListener callback;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        Fragment parent = getParentFragment();

        if (parent instanceof OnUserProfileSavedListener) {
            callback = (OnUserProfileSavedListener) parent;
        } else {
            throw new RuntimeException("AIHoroscopeFragment must implement OnUserProfileSavedListener");
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottomsheet_ai_horoscope, container, false);




        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(d -> {
            BottomSheetDialog sheetDialog = (BottomSheetDialog) d;
            FrameLayout bottomSheet =
                    sheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);

            if (bottomSheet != null) {
                bottomSheet.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        return dialog;
    }

    private void initView(View view) {
        tvRelationship = view.findViewById(R.id.spinnerRelationshipStatus);
        tvOccupation = view.findViewById(R.id.spinnerOccupation);
        tvReveal = view.findViewById(R.id.tvReveal);
        relationShipOptions = getResources().getStringArray(R.array.marital_status_list);
        occupationOption = getResources().getStringArray(R.array.occupation_list);

        ArrayAdapter<CharSequence> relationshipStatusAdapter = new ArrayAdapter<CharSequence>(requireContext(), R.layout.spinner_list_item2,
                relationShipOptions) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                // ((TextView) v).setTextColor(Color.WHITE);
                //((TextView) v).setTypeface(((DashBoardActivity) activity).regularTypeface);
                v.setBackgroundColor(requireContext().getResources().getColor(R.color.bg_card_view_color));
                FontUtils.changeFont(requireContext(), ((TextView) v), CGlobalVariables.FONTS_POPPINS_SEMI_BOLD);
                ((TextView) v).setTextColor(requireContext().getResources().getColor(R.color.black));
                ((TextView) v).setTextSize(14);
                v.setPadding(10, 0, 10, 0);
                return v;
            }

            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                // ((TextView) v).setTextColor(Color.WHITE);
                //((TextView) v).setTypeface(((DashBoardActivity) activity).regularTypeface);
                if (selectedRelationShipStatus == position) {
                    v.setBackgroundColor(requireContext().getResources().getColor(R.color.colorPrimary_day_night));
                    ((TextView) v).setTextColor(requireContext().getResources().getColor(R.color.white));
                }
                FontUtils.changeFont(requireContext(), ((TextView) v), CGlobalVariables.FONTS_POPPINS_LIGHT);

                return v;
            }
        };
        tvRelationship.setAdapter(relationshipStatusAdapter);

        tvRelationship.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRelationShipStatus = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> occupationAdapter = new ArrayAdapter<CharSequence>(requireContext(), R.layout.spinner_list_item2,
                occupationOption) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                // ((TextView) v).setTextColor(Color.WHITE);
                //((TextView) v).setTypeface(((DashBoardActivity) activity).regularTypeface);
                v.setBackgroundColor(requireContext().getResources().getColor(R.color.bg_card_view_color));
                FontUtils.changeFont(requireContext(), ((TextView) v), CGlobalVariables.FONTS_POPPINS_SEMI_BOLD);
                ((TextView) v).setTextColor(requireContext().getResources().getColor(R.color.black));
                ((TextView) v).setTextSize(14);
                v.setPadding(10, 0, 10, 0);
                return v;
            }

            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                // ((TextView) v).setTextColor(Color.WHITE);
                //((TextView) v).setTypeface(((DashBoardActivity) activity).regularTypeface);
                if (selectedOccupation == position) {
                    v.setBackgroundColor(requireContext().getResources().getColor(R.color.colorPrimary_day_night));
                    ((TextView) v).setTextColor(requireContext().getResources().getColor(R.color.white));
                }
                FontUtils.changeFont(requireContext(), ((TextView) v), CGlobalVariables.FONTS_POPPINS_LIGHT);

                return v;
            }
        };
        tvOccupation.setAdapter(occupationAdapter);

        tvOccupation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedOccupation = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        tvReveal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateSpinners()){
                    sendUserProfileDataToServer();

                }

            }
        });
        UserProfileData userProfileData = CUtils.getUserSelectedProfileFromPreference(requireContext());

        if(userProfileData.getMaritalStatus() != null && userProfileData.getMaritalStatus().length()>0) {
            tvRelationship.setSelection(getMaritalStatusPos(userProfileData.getMaritalStatus()));
        }else {
            tvRelationship.setSelection(0);
        }
        if(userProfileData.getOccupation() != null && userProfileData.getOccupation().length()>0) {
            tvOccupation.setSelection(getOccupationStatusPos(userProfileData.getOccupation()));
        }else{
            tvOccupation.setSelection(0);
        }

    }

    private int  getMaritalStatusPos(String maritalStatus){
        int maritalPos=0;
        for(int i=0; i<maritalStatusOptionsKey.length; i++) {
            if (maritalStatus.equalsIgnoreCase(maritalStatusOptionsKey[i])) {
                maritalPos = i;
            }
        }
        return maritalPos;
    }

    private int getOccupationStatusPos(String occupationStatus){
        int occupationPos=0;
        for(int i=0; i<occupationOptionsKey.length; i++) {
            if (occupationStatus.equalsIgnoreCase(occupationOptionsKey[i])) {
                occupationPos = i;
            }
        }
        return occupationPos;
    }
    private boolean validateSpinners() {

        if (tvRelationship.getSelectedItemPosition() == 0) {
            Toast.makeText(requireContext(), "Please select relationship status", Toast.LENGTH_SHORT).show();
            tvRelationship.requestFocus();
            return false;
        }

        if (tvOccupation.getSelectedItemPosition() == 0) {
            Toast.makeText(requireContext(), "Please select occupation", Toast.LENGTH_SHORT).show();
            tvOccupation.requestFocus();
            return false;
        }

        return true;
    }

    private void sendUserProfileDataToServer(){
        UserProfileData userProfileData = CUtils.getUserSelectedProfileFromPreference(requireContext());

        userProfileData.setName(userProfileData.getName());
        userProfileData.setUserPhoneNo(CUtils.getUserID(getActivity()));


        userProfileData.setGender(userProfileData.getGender());

        int maritalPos = tvRelationship.getSelectedItemPosition();
        String maritalStr = maritalStatusOptionsKey[maritalPos];
        userProfileData.setMaritalStatus(maritalStr);

        int occupationPos = tvOccupation.getSelectedItemPosition();
        String occupationStr = occupationOptionsKey[occupationPos];
        userProfileData.setOccupation(occupationStr);

            userProfileData.setDay(userProfileData.getDay());
            userProfileData.setMonth(userProfileData.getMonth());
            userProfileData.setYear(userProfileData.getYear());
            userProfileData.setHour(userProfileData.getHour());
            userProfileData.setMinute(userProfileData.getMinute());
            userProfileData.setSecond(userProfileData.getSecond());

            userProfileData.setPlace(userProfileData.getPlace());
            userProfileData.setLatdeg(userProfileData.getLatdeg());
            userProfileData.setLongdeg(userProfileData.getLongdeg());
            userProfileData.setLongmin(userProfileData.getLongmin());
            userProfileData.setLatmin(userProfileData.getLatmin());
            userProfileData.setLongew(userProfileData.getLongew());
            userProfileData.setLatns(userProfileData.getLatns());
            userProfileData.setTimezone(userProfileData.getTimezone());

            sendToServer(userProfileData);
    }

    private void sendToServer(UserProfileData userProfileData1) {

        if (!CUtils.isConnectedWithInternet(requireContext())) {
            CUtils.showSnackbar(tvOccupation, getResources().getString(R.string.no_internet), requireContext());
        } else {
            if (pd == null)
                pd = new CustomProgressDialog(requireContext());
            pd.show();
            pd.setCancelable(false);
            //Log.e("LoadMore Url ",CGlobalVariables.PROFILE_UPDATE_SUBMIT_URL);
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.PROFILE_UPDATE_SUBMIT_URL,
//                    ProfileFragment.this, false, getParams(userProfileData1), 1).getMyStringRequest();
//            queue.add(stringRequest);
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.updateUserProfile(getParams(userProfileData1));
           // Log.e("LoadMore Url ",call.request().url().toString());

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        hideProgressBar();
                        String myResponse = response.body().string();
                       // Log.e("LoadMore myResponse ",myResponse);

                        JSONObject jsonObject = new JSONObject(myResponse);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("1")) {
                            CUtils.saveUserSelectedProfileInPreference(requireContext(), userProfileData1);
                            CUtils.saveProfileForChatInPreference(requireContext(), userProfileData1);
                            com.ojassoft.astrosage.utils.CUtils.saveStringData(requireContext(), com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_HOROSCOPE, "");
                            CUtils.showSnackbar(tvRelationship, getResources().getString(R.string.update_successfully), requireContext());
                            dismiss();
                            if (callback != null) {
                                callback.onUserProfileSaved();
                            }
                        } else
                        {
                            CUtils.showSnackbar(tvRelationship, getResources().getString(R.string.update_not_successfully), requireContext());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                }
            });
        }
    }

    public Map<String, String> getParams(UserProfileData userProfileDataBean) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(requireContext()));
        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(requireContext()));
        headers.put(CGlobalVariables.USER_PHONE_NO, userProfileDataBean.getUserPhoneNo());
        headers.put(CGlobalVariables.REG_SOURCE, CGlobalVariables.APP_SOURCE);
        headers.put("name", userProfileDataBean.getName());
        headers.put("gender", userProfileDataBean.getGender());
        headers.put("place", userProfileDataBean.getPlace());
        headers.put("day", userProfileDataBean.getDay());
        headers.put("month", userProfileDataBean.getMonth());
        headers.put("year", userProfileDataBean.getYear());
        headers.put("hour", userProfileDataBean.getHour());
        headers.put("minute", userProfileDataBean.getMinute());
        headers.put("second", userProfileDataBean.getSecond());
        headers.put("longdeg", userProfileDataBean.getLongdeg());
        headers.put("longmin", userProfileDataBean.getLongmin());
        headers.put("longew", userProfileDataBean.getLongew());
        headers.put("latmin", userProfileDataBean.getLatmin());
        headers.put("latdeg", userProfileDataBean.getLatdeg());
        headers.put("latns", userProfileDataBean.getLatns());
        headers.put("timezone", userProfileDataBean.getTimezone());
        headers.put("maritalStatus", userProfileDataBean.getMaritalStatus());
        headers.put("occupation", userProfileDataBean.getOccupation());
        headers.put(CGlobalVariables.LANG,CUtils.getLanguageKey(LANGUAGE_CODE));
        //Log.e("profile_data1"," : "+CUtils.getApplicationSignatureHashCode(activity)+""+CUtils.getCountryCode(activity) +""+userProfileDataBean.getUserPhoneNo() +""+userProfileDataBean.getName() +""+userProfileDataBean.getGender() +""+userProfileDataBean.getPlace() +""+userProfileDataBean.getDay() +""+userProfileDataBean.getMonth() +""+userProfileDataBean.getYear() +""+userProfileDataBean.getHour() +""+userProfileDataBean.getMinute() +""+userProfileDataBean.getSecond() +""+userProfileDataBean.getLongdeg() +""+userProfileDataBean.getLongmin() +""+userProfileDataBean.getLongew() +""+userProfileDataBean.getLatmin() +""+userProfileDataBean.getLatdeg() +""+userProfileDataBean.getLatns() +""+userProfileDataBean.getTimezone() +""+userProfileDataBean.getMaritalStatus() +""+userProfileDataBean.getOccupation() +""+CUtils.getLanguageKey(LANGUAGE_CODE));
        return CUtils.setRequiredParams(headers);
    }
    public void hideProgressBar() {
        try {
            if (pd != null & pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnUserProfileSavedListener {
        void onUserProfileSaved();
    }

}

