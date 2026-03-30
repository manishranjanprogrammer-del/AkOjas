package com.ojassoft.astrosage.ui.fragments;

import static android.content.Context.MODE_PRIVATE;

import static com.ojassoft.astrosage.utils.CGlobalVariables.KUNDLI_AI_PRIMIUM_PLAN_ID_11;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.customadapters.HomeNavigationAdapter;
import com.ojassoft.astrosage.jinterface.IHomeNavigationDrawerFragment;
import com.ojassoft.astrosage.misc.FontUtils;
import com.ojassoft.astrosage.misc.HomeMenuItemInformation;
import com.ojassoft.astrosage.model.GmailAccountInfo;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.ActAskQuestion;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.EditProfileActivity;
import com.ojassoft.astrosage.ui.act.OutputMasterActivity;
import com.ojassoft.astrosage.ui.act.matching.OutputMatchingMasterActivity;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalMatching;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.model.UserProfileData;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.ChatUtils;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Amit Rautela on 24/2/16.
 * This fragment is used to show Navigation drawer
 */
public class HomeNavigationDrawerFragment extends Fragment {
    String TAG = "HomeNavigationDrawerFragment";
    HomeNavigationAdapter navigationAdapter;
    private IHomeNavigationDrawerFragment iHomeNavigationDrawerFragment;
    Activity activity;
    RecyclerView recyclerView;//,recyclerViewAstrologer;
    private DrawerLayout mDrawerLayout;
    private View containerView;
    private ActionBarDrawerToggle mDrawerToggle;
    public boolean isDrawerOpen;
    ImageView imgProfilePic;
    TextView defaultKundliUserName, defaultKundliUserData, tvSignIn, tvSignUp;
    List<String> subMenuItems;
    List<Drawable> subMenuItemsIcon;
    List<Integer> subMenuItemsIndex;
    RelativeLayout containerDrawerUserData;//,container1,container2;
    RelativeLayout containerDrawerProfileForMatchMaking;
    LinearLayout kundliDetailsLayout;
    RelativeLayout innerContainerAskAQuestion;
//    ImageView mIvEditProfile;
    TextView tvBoyName, tvBoyData, tvGirlName, tvGirlData, tvQuestionPrice, tvFreeQuestion , tvUserName , tvPlanName;

    LinearLayout loggedOutLayout , loggedInLayout;
    public HomeNavigationDrawerFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        iHomeNavigationDrawerFragment = (IHomeNavigationDrawerFragment) activity;
        this.activity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        iHomeNavigationDrawerFragment = null;
        activity = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lay_home_navigation_drawer_fragment, container, false);
        recyclerView = view.findViewById(R.id.drawerList);
        setLayRef(view);
        setSignInSignUpLayout();
        return view;
    }

    private void setSignInSignUpLayout() {
        int appLanguageCode = CUtils.getLanguageCode(activity);
        if (appLanguageCode == 0) {
            loggedOutLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams tvSignInLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            tvSignInLayoutParams.setMargins(getResources().getDimensionPixelSize(R.dimen.dimen_5dp), 0, getResources().getDimensionPixelSize(R.dimen.dimen_5dp),0);
            tvSignIn.setLayoutParams(tvSignInLayoutParams);

            LinearLayout.LayoutParams tvSIgnUpLayouParam = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            tvSIgnUpLayouParam.setMargins(getResources().getDimensionPixelSize(R.dimen.dimen_5dp), 0, getResources().getDimensionPixelSize(R.dimen.dimen_20dp), 0);
            tvSignUp.setLayoutParams(tvSIgnUpLayouParam);

        } else {
            loggedOutLayout.setOrientation(LinearLayout.VERTICAL);

            LinearLayout.LayoutParams tvSignInLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            tvSignInLayoutParams.setMargins(getResources().getDimensionPixelSize(R.dimen.dimen_10dp), 0, getResources().getDimensionPixelSize(R.dimen.dimen_20dp), getResources().getDimensionPixelSize(R.dimen.dimen_8dp));
            tvSignIn.setLayoutParams(tvSignInLayoutParams);

            LinearLayout.LayoutParams tvSIgnUpLayouParam = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            tvSIgnUpLayouParam.setMargins(getResources().getDimensionPixelSize(R.dimen.dimen_10dp), 0, getResources().getDimensionPixelSize(R.dimen.dimen_20dp), 0);
            tvSignUp.setLayoutParams(tvSIgnUpLayouParam);

        }
    }

    private void setLayRef(View view) {
        tvSignIn = view.findViewById(R.id.tvSignIn);

        tvSignUp = view.findViewById(R.id.tvSignUp);
        FontUtils.changeFont(getContext(),tvSignIn, CGlobalVariables.FONTS_POPPINS_REGULAR);
        FontUtils.changeFont(getContext(),tvSignUp, CGlobalVariables.FONTS_POPPINS_REGULAR);

        defaultKundliUserName = view.findViewById(R.id.defaultKundliUserName);
        defaultKundliUserData = view.findViewById(R.id.defaultKundliUserData);
        containerDrawerUserData =  view.findViewById(R.id.containerDrawerUserData);
        containerDrawerProfileForMatchMaking = view.findViewById(R.id.containerDrawerProfileForMatchMaking);
        kundliDetailsLayout = view.findViewById(R.id.kundli_details_layout);
        innerContainerAskAQuestion = view.findViewById(R.id.innerContainerAskAQuestion);
        tvBoyName =  view.findViewById(R.id.tvBoyName);
        tvBoyData = view.findViewById(R.id.tvBoyData);
        tvGirlName =view.findViewById(R.id.tvGirlName);
        tvGirlData =view.findViewById(R.id.tvGirlData);
//        mIvEditProfile =view.findViewById(R.id.edit_profile);
//        mIvEditProfile.setVisibility(View.GONE);
        tvQuestionPrice =view.findViewById(R.id.tvQuestionPrice);
        tvQuestionPrice.setTypeface(((BaseInputActivity) activity).robotRegularTypeface);
        tvFreeQuestion = view.findViewById(R.id.tvFreeQuestion);
        tvFreeQuestion.setTypeface(((BaseInputActivity) activity).robotRegularTypeface);
        imgProfilePic = view.findViewById(R.id.imgProfilePic);
        loggedOutLayout = view.findViewById(R.id.signInSignUpLayout);
        loggedInLayout = view.findViewById(R.id.user_detail_layout);
        tvUserName = view.findViewById(R.id.tvusername);
        tvPlanName = view.findViewById(R.id.plantv);
        FontUtils.changeFont(getContext(),tvUserName, CGlobalVariables.FONTS_POPPINS_SEMI_BOLD);
        FontUtils.changeFont(getContext(),tvGirlName, CGlobalVariables.FONTS_POPPINS_SEMI_BOLD);
        FontUtils.changeFont(getContext(),tvBoyName, CGlobalVariables.FONTS_POPPINS_SEMI_BOLD);
        FontUtils.changeFont(getContext(),tvPlanName, CGlobalVariables.FONTS_POPPINS_REGULAR);
        FontUtils.changeFont(getContext(),tvGirlData, CGlobalVariables.FONTS_POPPINS_REGULAR);
        FontUtils.changeFont(getContext(),tvBoyData, CGlobalVariables.FONTS_POPPINS_REGULAR);


        imgProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    checkUserLoginPerformAction();
            }
        });

//        mIvEditProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                checkUserLoginPerformAction();
//            }
//        });

        setProfileview();


    }
    public Bitmap retrivingProfilePic(String id) {
        SharedPreferences myPrefrence = requireContext().getSharedPreferences("MyProfilePicture", MODE_PRIVATE);
        String str = myPrefrence.getString(id, "");
        Bitmap bm = decodeBase64(str);
        //Log.d("retrivingProfilePic",bm.toString());

        // edit_profile_img.setImageBitmap(bm);
        return bm;
    }
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
        //setTypefaceOfViews();
    }
    private void checkUserLoginPerformAction(){
        if (CUtils.isUserLogedIn(activity)) {
            String className = activity.getLocalClassName();
            Intent intent = new Intent(activity, EditProfileActivity.class);
            intent.putExtra("password", CUtils.getUserPassword(activity));
            intent.putExtra("activity", className);
            intent.putExtra("dologout", false);
            startActivity(intent);
        }
    }

    private void setProfileview() {
        if (CUtils.isUserLogedIn(activity)) {
            userSignInFunctionality();
//            mIvEditProfile.setVisibility(View.VISIBLE);
        } else {
            usersignoutFuntionality();
//            mIvEditProfile.setVisibility(View.INVISIBLE);
        }
    }

    public void usersignoutFuntionality() {
        loggedOutLayout.setVisibility(View.VISIBLE);
        loggedInLayout.setVisibility(View.GONE);
        tvSignIn.setText(activity.getResources().getString(R.string.button_sign_in));
        tvSignUp.setText(activity.getResources().getString(R.string.sign_up));
        tvSignIn.setOnClickListener(new ButtonClickListener());
        tvSignUp.setOnClickListener(new ButtonClickListener());
    }

    private void userSignInFunctionality() {
        try {
            loggedOutLayout.setVisibility(View.GONE);
            loggedInLayout.setVisibility(View.VISIBLE);
            tvUserName.setText(CUtils.getUserName(activity));

            int planid = CUtils.getUserPurchasedPlanFromPreference(activity);
            String planText = getResources().getStringArray(R.array.cloud_plans_on_menu)[planid - 1];
            //if user has AI pass plan append in plan text
            if (com.ojassoft.astrosage.varta.utils.CUtils.isKundliAiProPlan(getContext())) {
                planText = planText + " & " + getString(R.string.unlimited_ai_chats);
            }
            tvPlanName.setText(planText);
            tvPlanName.setOnClickListener(new ButtonClickListener());
        } catch (Exception e) {
            //
        }
    }

    private void setHomeNavigationAdapter() {
        navigationAdapter = new HomeNavigationAdapter(activity,  HomeNavigationDrawerFragment.this, getData(), ((BaseInputActivity) activity).regularTypeface);
        recyclerView.setAdapter(navigationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
    }

    public void setup(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolBar, List<String> subMenuItems
            , List<Drawable> subMenuItemsIcon, List<Integer> subMenuItemsIndex) {
        this.subMenuItems = subMenuItems;
        this.subMenuItemsIcon = subMenuItemsIcon;
        this.subMenuItemsIndex = subMenuItemsIndex;
        setHomeNavigationAdapter();
        containerView = activity.findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(activity, mDrawerLayout, toolBar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                isDrawerOpen = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                isDrawerOpen = false;
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                    if (slideOffset < 0.6)
                        toolBar.setAlpha(1 - slideOffset);
            }
        };
        ((android.widget.ImageView) toolBar.findViewById(R.id.ivToggleImage)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableSoftKeys();
                mDrawerLayout.openDrawer(containerView);
            }
        });
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private List<HomeMenuItemInformation> getData() {
        setPersonalInfo();
        List<HomeMenuItemInformation> data = new ArrayList<HomeMenuItemInformation>();
        try {
            for (int i = 0; i < subMenuItems.size(); i++) {
                data.add(new HomeMenuItemInformation(subMenuItems.get(i), subMenuItemsIcon.get(i), subMenuItemsIndex.get(i), false));
            }
        } catch (Exception ex) {
            //Log.i("Exception", ex.getMessage().toString());
        }
        return data;
    }

    private void setPersonalInfo() {
        try {
            boolean isProfileViewAlreadySet = false;
            if (activity instanceof OutputMasterActivity) {
                containerDrawerUserData.setVisibility(View.VISIBLE);
                containerDrawerProfileForMatchMaking.setVisibility(View.GONE);
                innerContainerAskAQuestion.setVisibility(View.GONE);
                kundliDetailsLayout.setVisibility(View.VISIBLE);
                String username = CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getName();
                String[] dtPlace = CUtils.formatDateTimeOfKundliInputToShowOnMenu(CGlobal.getCGlobalObject().getHoroPersonalInfoObject());
                String gender = CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getGender();
                imgProfilePic.setVisibility(View.VISIBLE);
                if (gender.equals("M") || gender.equals("Male")) {
                    imgProfilePic.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.male_user));
                } else {
                    imgProfilePic.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.female_user));
                }
                defaultKundliUserName.setText(username);
                defaultKundliUserData.setText(dtPlace[0] + " | " + dtPlace[1]);
                isProfileViewAlreadySet = true;
            } else if (activity instanceof OutputMatchingMasterActivity) {
                containerDrawerUserData.setVisibility(View.GONE);
                containerDrawerProfileForMatchMaking.setVisibility(View.VISIBLE);
                kundliDetailsLayout.setVisibility(View.GONE);
                innerContainerAskAQuestion.setVisibility(View.GONE);
                tvBoyName.setText(CGlobalMatching.getCGlobalMatching().getBoyPersonalDetail().getName());
                String[] dtPlaceBoy = CUtils.formatDateTimeOfKundliInputToShowOnMenu(CGlobalMatching.getCGlobalMatching().getBoyPersonalDetail());
                if (dtPlaceBoy != null) {
                    tvBoyData.setText(dtPlaceBoy[0] + " | " + dtPlaceBoy[1]);
                }
                tvGirlName.setText(CGlobalMatching.getCGlobalMatching().getGirlPersonalDetail().getName());
                String[] dtPlaceGirl = CUtils.formatDateTimeOfKundliInputToShowOnMenu(CGlobalMatching.getCGlobalMatching().getGirlPersonalDetail());
                if (dtPlaceGirl != null) {
                    tvGirlData.setText(dtPlaceGirl[0] + " | " + dtPlaceGirl[1]);
                }
                isProfileViewAlreadySet = true;
            } else if (activity instanceof ActAppModule) {
                GmailAccountInfo gmailAccountInfo = CUtils.getGmailAccountInfo(requireContext());
                setUserProfile(gmailAccountInfo);

            }else if (activity instanceof ActAskQuestion) {
                containerDrawerUserData.setVisibility(View.VISIBLE);
                containerDrawerProfileForMatchMaking.setVisibility(View.GONE);
                innerContainerAskAQuestion.setVisibility(View.VISIBLE);
            }

            if (!isProfileViewAlreadySet) {
                if (activity instanceof ActAppModule) {
                    GmailAccountInfo gmailAccountInfo = CUtils.getGmailAccountInfo(requireContext());
                    setUserProfile(gmailAccountInfo);


                }
                defaultKundliUserName.setVisibility(View.GONE);
                defaultKundliUserData.setVisibility(View.GONE);
            }
        }catch (Exception e){
            //
        }
    }

    private void setUserProfile(GmailAccountInfo gmailAccountInfo){
        int genderRes = R.drawable.male_user;
        //String gender = CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getGender();
        UserProfileData userProfileData = com.ojassoft.astrosage.varta.utils.CUtils.getUserSelectedProfileFromPreference(requireContext());
        String gender = userProfileData.getGender();

        if (!gender.isEmpty() && !gender.equals("N")){
        if (gender.equals("M") || gender.equals("Male")) {
            genderRes = R.drawable.male_user;;
        } else {
            genderRes = R.drawable.female_user;;
        }
        }else {
            genderRes = R.drawable.male_user;;

        }
        Bitmap bm = null;

        try {
            bm = retrivingProfilePic(gmailAccountInfo.getId());
        } catch (Exception e) {
            //
        }

        if (bm != null) {
            imgProfilePic.setImageBitmap(bm);
        } else {
            String profileImage = com.ojassoft.astrosage.varta.utils.CUtils.getGoogleFacebookProfile(requireContext());

            if (profileImage != null && !profileImage.equals("")) {
                Glide.with(this).load(profileImage).placeholder(genderRes).into(imgProfilePic);
            } else{
                imgProfilePic.setImageResource(genderRes);
            }
        }
    }
    public void closeDrawer() {
        mDrawerLayout.closeDrawer(containerView);
    }

    public void openDrawer() {
        mDrawerLayout.openDrawer(containerView);
    }

    private void disableSoftKeys() {

        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }

    }

    public void updateLoginDetials(boolean isLoginSuccess, String loginName, String loginPwd, List<String> subMenuItems
            , List<Drawable> subMenuItemsIcon, List<Integer> subMenuItemsIndex) {
        updateLayout(subMenuItems, subMenuItemsIcon, subMenuItemsIndex);
    }

    public void updateLayout(List<String> subMenuItems, List<Drawable> subMenuItemsIcon, List<Integer> subMenuItemsIndex) {

        setProfileview();
        this.subMenuItems = subMenuItems;
        this.subMenuItemsIcon = subMenuItemsIcon;
        this.subMenuItemsIndex = subMenuItemsIndex;
        navigationAdapter = new HomeNavigationAdapter(activity, HomeNavigationDrawerFragment.this, getData(), ((BaseInputActivity) activity).regularTypeface);
        recyclerView.setAdapter(navigationAdapter);
        navigationAdapter.notifyDataSetChanged();
    }

    class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            switch (view.getId()) {

                case R.id.tvSignIn:
                        switchContent(BaseInputActivity.TAG_SIGN_IN);
                    break;

                case R.id.tvSignUp:
                    switchContent(BaseInputActivity.TAG_SIGN_UP);
                    break;
                case R.id.plantv:
                    switchContent(BaseInputActivity.TAG_REMOVE_ADS);
                    break;
            }

        }
    }

    public void switchContent(int position) {
        iHomeNavigationDrawerFragment.switchContent(position);
        closeDrawer();
    }

    public void setDrawerLockMode(boolean enabled) {
        try {
            int lockMode = enabled ? DrawerLayout.LOCK_MODE_UNLOCKED :
                    DrawerLayout.LOCK_MODE_LOCKED_CLOSED;

            mDrawerLayout.setDrawerLockMode(lockMode);
        } catch (Exception ex) {
            //Log.e("Errro",ex.getMessage());
        }
    }

    public void setsetDrawerIndicatorEnabled(boolean value) {
        mDrawerToggle.setDrawerIndicatorEnabled(value);
        mDrawerToggle.syncState();
        if (!value) {
            mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!mDrawerToggle.isDrawerIndicatorEnabled())
                        activity.onBackPressed();
                }
            });
        }
    }

    /**
     * @param freeQues
     * @param quesPrice
     * @author Amit Rautela
     * This method is used to update Ask a question price
     */
    public void updateAskAQuestionPrice(String freeQues, String quesPrice) {
        if (tvFreeQuestion != null && tvQuestionPrice != null) {
            tvFreeQuestion.setText(freeQues);
            tvQuestionPrice.setText(quesPrice);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setSignInSignUpLayout();
    }
}
