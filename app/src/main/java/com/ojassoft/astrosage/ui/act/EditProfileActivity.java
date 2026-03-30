package com.ojassoft.astrosage.ui.act;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.libojassoft.android.misc.VolleyResponse;
import com.libojassoft.android.misc.VolleyServiceHandler;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.model.GmailAccountInfo;
import com.ojassoft.astrosage.ui.act.horoscope.DetailedHoroscope;
import com.ojassoft.astrosage.ui.act.horoscope.HoroscopeHomeActivity;
import com.ojassoft.astrosage.ui.act.indnotes.BaseActivity;
import com.ojassoft.astrosage.ui.act.matching.OutputMatchingMasterActivity;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.ui.fragments.SetProfilePictureDialoge;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.model.LiveAstrologerModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ojas on 22/6/16.
 */
public class EditProfileActivity extends BaseInputActivity implements VolleyResponse, View.OnClickListener {
    TextView nameTV, mobileTV, materialStatusTV, occupationTV, headingTV, line1TV, line2TV;
    TextView footerHeaderTV, userDetailTV;
    EditText nameET, mobileET, headingET, line1ET, line2ET;
    TextInputLayout nameTIL, mobileTIL, headingTIL, line1TIL, line2TIL;
    TextView userName, userId, title;
    String userEmailId;
    EditText userNameEditText, mobileEditText;
    Toolbar toolbar;
    String oldPassword, newPassword;
    Button saveButton;
    String activityName;
    ImageView edit_profile_img;
    boolean doLogout;
    Typeface regularTypeface;
    private Uri fileUri; // file url to store image/video
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100, GALLERY_IMAGE_REQUEST_CODE = 200;

    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final String IMAGE_DIRECTORY_NAME = "Astrosage Camera";
    String useridusedforfetchpic;
    private TextInputLayout inputLayUserName, inputLayMobile;
    private RequestQueue queue;
    CustomProgressDialog pd = null;
    GmailAccountInfo gmailAccountInfo;
    private TextView mChangePasswordTV;
    Spinner matrialStatusSpinner, occupationStatusSpinner;
    int planId;
    CardView footerCard;

    private static final int PERMISSION_REQ_CODE = 1;
    private String[] PERMISSIONS = {
            Manifest.permission.CAMERA
    };

    public EditProfileActivity() {
        super(R.id.app_name);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        planId = CUtils.getUserPurchasedPlanFromPreference(this);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        //added by Ankit on 23-12-2019 104,105 line
        int languageCode = ((AstrosageKundliApplication) getApplication()).getLanguageCode();
        CUtils.getRobotoFont(EditProfileActivity.this, languageCode, CGlobalVariables.regular);
        setContentView(R.layout.edit_profile_activity_layout);
        Intent intent = getIntent();
        if (intent != null) {
            newPassword = intent.getStringExtra("password");
            activityName = intent.getStringExtra("activity");
            doLogout = intent.getBooleanExtra("dologout", true);
        }
        oldPassword = CUtils.getUserPassword(this);
        saveButton = (Button) findViewById(R.id.saveBtn);
        userName = (TextView) findViewById(R.id.user_name);
        userId = (TextView) findViewById(R.id.user_id);
        userNameEditText = (EditText) findViewById(R.id.user_name_edittext);
        mobileEditText = (EditText) findViewById(R.id.mobile_no_edittext);
        edit_profile_img = (ImageView) findViewById(R.id.edit_profile_img);
        toolbar = (Toolbar) findViewById(R.id.toolbar_magazine);
        title = (TextView) toolbar.findViewById(R.id.tvToolBarTitle);
        inputLayUserName = (TextInputLayout) findViewById(R.id.inputLayUserName);
        inputLayMobile = (TextInputLayout) findViewById(R.id.inputLayMobileNo);
        mChangePasswordTV = (TextView) findViewById(R.id.tv_change_password);
        footerCard = (CardView) findViewById(R.id.footer_card);
        if (planId == CGlobalVariables.PLATINUM_PLAN_ID
                || planId == CGlobalVariables.PLATINUM_PLAN_ID_9
                || planId == CGlobalVariables.PLATINUM_PLAN_ID_10
                || planId == CGlobalVariables.KUNDLI_AI_PRIMIUM_PLAN_ID_11) {
            footerCard.setVisibility(View.VISIBLE);
        } else {
            footerCard.setVisibility(View.GONE);
        }

        nameTV = (TextView) findViewById(R.id.textViewUserName);
        mobileTV = (TextView) findViewById(R.id.textViewMobileNo);
        materialStatusTV = (TextView) findViewById(R.id.textViewMaterialStatus);
        occupationTV = (TextView) findViewById(R.id.textViewOccupation);
        headingTV = (TextView) findViewById(R.id.textViewHeading);
        line1TV = (TextView) findViewById(R.id.textViewLine1);
        line2TV = (TextView) findViewById(R.id.textViewLine2);
        userDetailTV = (TextView) findViewById(R.id.user_detail_heading);
        footerHeaderTV = (TextView) findViewById(R.id.footer_detail_heading);

        nameET = (EditText) findViewById(R.id.user_name_edittext);
        mobileET = (EditText) findViewById(R.id.mobile_no_edittext);
        headingET = (EditText) findViewById(R.id.heading_edittext);
        line1ET = (EditText) findViewById(R.id.line1_edittext);
        line2ET = (EditText) findViewById(R.id.line2_edittext);

        nameTIL = (TextInputLayout) findViewById(R.id.inputLayUserName);
        mobileTIL = (TextInputLayout) findViewById(R.id.inputLayMobileNo);
        headingTIL = (TextInputLayout) findViewById(R.id.inputLayHeading);
        line1TIL = (TextInputLayout) findViewById(R.id.inputLayLine1);
        line2TIL = (TextInputLayout) findViewById(R.id.inputLayLine2);


        matrialStatusSpinner = (Spinner) findViewById(R.id.material_status_spinner);
        occupationStatusSpinner = (Spinner) findViewById(R.id.occupation_spinner);
        setSpinnerData(matrialStatusSpinner, getResources().getStringArray(R.array.material_status));
        setSpinnerData(occupationStatusSpinner, getResources().getStringArray(R.array.occupations));

        //inputLayUserName.setHintEnabled(false);
        //inputLayMobile.setHintEnabled(false);
        nameTIL.setHintEnabled(false);
        mobileTIL.setHintEnabled(false);
        headingTIL.setHintEnabled(false);
        line1TIL.setHintEnabled(false);
        line2TIL.setHintEnabled(false);

        mChangePasswordTV.setOnClickListener(this);
        setSupportActionBar(toolbar);

        title.setText(getResources().getString(R.string.edit_profile));

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        GmailAccountInfo gmailAccountInfo = CUtils.getGmailAccountInfo(this);
        setTypefaceOfViews();
        try {
            if (gmailAccountInfo != null) {
                useridusedforfetchpic = gmailAccountInfo.getId();
                userName.setText(gmailAccountInfo.getUserName());
                userId.setText(gmailAccountInfo.getId());

                userNameEditText.setText(gmailAccountInfo.getUserName()!=null?gmailAccountInfo.getUserName():"");
                userNameEditText.setSelection(userNameEditText.getText().length());
                mobileET.setText(gmailAccountInfo.getMobileNo());
                headingET.setText(gmailAccountInfo.getHeading());
                line1ET.setText(gmailAccountInfo.getAddress1());
                line2ET.setText(gmailAccountInfo.getAddress2());
                matrialStatusSpinner.setSelection(gmailAccountInfo.getMaritalStatus());
                occupationStatusSpinner.setSelection(gmailAccountInfo.getOccupation());
            }
        } catch (Exception ex) {
            //
        }
        /* else {
            userName.setText(CUtils.getUserFirstName(EditProfileActivity.this));
            userId.setText(CUtils.getUserName(EditProfileActivity.this));
            userNameEditText.setText(CUtils.getUserFirstName(EditProfileActivity.this));
        }*/
        if (((AstrosageKundliApplication) getApplication()).getLanguageCode() == CGlobalVariables.ENGLISH) {
            saveButton.setText(getResources().getString(R.string.save_kundli).toUpperCase());
        } else {
            saveButton.setText(getResources().getString(R.string.save_kundli));
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(),"user id from editprofileactivity on save click  "+useridusedforfetchpic,Toast.LENGTH_SHORT).show();
                if (userNameEditText.getText().toString().trim().isEmpty()) {
                    inputLayUserName.setErrorEnabled(true);
                    requestFocus(userNameEditText);
                    inputLayUserName.setError(getResources().getString(R.string.fill_user_name));
                    userNameEditText.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                    //new ChangePasswordAndLoginInfo().execute();
                } else if (mobileEditText.getText().toString().trim().isEmpty()) {
                    inputLayMobile.setErrorEnabled(true);
                    requestFocus(mobileEditText);
                    inputLayMobile.setError(getResources().getString(R.string.phone_one_v_astro_service));
                    mobileEditText.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                    //new ChangePasswordAndLoginInfo().execute();
                }else if(com.ojassoft.astrosage.varta.utils.CUtils.checkIsEmojiInString(userNameEditText.getText().toString().trim())){
                    inputLayUserName.setErrorEnabled(true);
                    requestFocus(userNameEditText);
                    inputLayUserName.setError(getResources().getString(R.string.text_name_emoji_in_name));
                    userNameEditText.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                }else if(userNameEditText.getText().toString().trim().length() > 49){
                    inputLayUserName.setErrorEnabled(true);
                    requestFocus(userNameEditText);
                    inputLayUserName.setError(getResources().getString(R.string.text_max_length));
                    userNameEditText.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                }else {
                    inputLayUserName.setErrorEnabled(false);
                    inputLayUserName.setError(null);
                    inputLayMobile.setErrorEnabled(false);
                    inputLayMobile.setError(null);
                    userNameEditText.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
                    mobileEditText.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
                    changePasswordAndLoginInfo();
                }
            }
        });
        edit_profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // captureImage();
                try {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();

                    SetProfilePictureDialoge profilePictureDialoge = new SetProfilePictureDialoge();
                    profilePictureDialoge.show(fm, "setProfilePic");
                    ft.commit();
                } catch (Exception e) {
                    //
                }
            }
        });
        TextWatcher inputTextWatcher = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                userName.setText(s.toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };

        userNameEditText.addTextChangedListener(inputTextWatcher);

        Bitmap bm = retrivingProfilePic(gmailAccountInfo.getId());
        if (bm != null) {
            edit_profile_img.setImageBitmap(bm);
        } else {
            edit_profile_img.setImageResource(R.drawable.ic_profile_view);
        }
        //getBrandingDetailFromServer();
        gmailAccountInfo = CUtils.getGmailAccountInfo(EditProfileActivity.this);
        // userName = gmailAccountInfo.getUserName();
        userEmailId = gmailAccountInfo.getId();
        //Log.i("userEmailId>>", CUtils.replaceEmailChar("qaz12345"));
        getBrandingDetailFromServer();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void setSpinnerData(Spinner spinner, String[] array) {
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array){
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = getLayoutInflater().inflate(R.layout.toolbar_spinner_item_layout, null);
                final TextView textView = (TextView) view.findViewById(R.id.textview);
//                textView.setTextColor(Color.WHITE);
                textView.setTextColor(getResources().getColor(R.color.black));
                textView.setTextSize(16);
                textView.setText(array[position]);
                textView.setTypeface(regularTypeface);


                return view;
            }


            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = getLayoutInflater().inflate(R.layout.toolbar_spinner_item_layout, null);
                view.setBackgroundColor(getColor(R.color.bg_card_view_color));
                TextView textView = (TextView) view.findViewById(R.id.textview);
                textView.setTextColor(getColor(R.color.black));
                textView.setTextSize(16);
                String title = array[position];

                textView.setText(title);
                textView.setTypeface(regularTypeface);
                return view;
            }
        };
       // aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);
    }


    @Override
    protected void onStart() {
        super.onStart();
        //EasyTracker.getInstance().activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //EasyTracker.getInstance().activityStop(this);
    }

    public Bitmap retrivingProfilePic(String id) {
        SharedPreferences myPrefrence = getSharedPreferences("MyProfilePicture", MODE_PRIVATE);
        String str = myPrefrence.getString(id, "");
        Bitmap bm = decodeBase64(str);

        // edit_profile_img.setImageBitmap(bm);
        return bm;
    }

    public void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public void galleryImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_IMAGE_REQUEST_CODE);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");



            /*SharedPreferences myPrefrence = getSharedPreferences("MyProfile",MODE_PRIVATE);
            SharedPreferences.Editor editor = myPrefrence.edit();

            editor.putString("imagePreferance", encodeToBase64(photo));//added by neeraj 23/06/2016 use method to encode and then store to shared preference
            editor.commit();*/
            // SharedPreferences myPrefrence1 = getSharedPreferences("MyProfile",MODE_PRIVATE);
            // edit_profile_img.setImageBitmap(decodeBase64(myPrefrence1.getString("imagePreferance","Default")));//added by neeraj 23/06/2016  encoding and getting bitmap
            // Bitmap roundphoto = getRoundedShape(photo);
            //  saveToSharedPreferences(photo);
            edit_profile_img.setImageBitmap(photo);
            //  Log.d("photo from onActivityRe",roundphoto.toString());


        } else if (requestCode == GALLERY_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {


            try {
                Bitmap photo = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                // Bitmap roundphoto = getRoundedShape(photo);
                // saveToSharedPreferences(photo);
                edit_profile_img.setImageBitmap(photo);
                //   Log.d("photo from onActivityRe",roundphoto.toString());

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }


   /*

   //never used commented by Neeraj 5/7/2016
   public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = 50;
        int targetHeight = 50;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }*/

    private String encodeToBase64(Bitmap photo) {
        Bitmap immage = photo;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        android.util.Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
        //setTypefaceOfViews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    private void finishAllActivity() {
        Intent intent = null;
        if (activityName.contains("ActAppModule")) {
            intent = new Intent(this, ActAppModule.class);
        }
        if (activityName.contains("HomeInputScreen")) {
            intent = new Intent(this, HomeInputScreen.class);
        }
        if (activityName.contains("OutputMasterActivity")) {
            intent = new Intent(this, OutputMasterActivity.class);
        }
        if (activityName.contains("OutputMatchingMasterActivity")) {
            intent = new Intent(this, OutputMatchingMasterActivity.class);
        }
        if (activityName.contains("InputPanchangActivity")) {
            intent = new Intent(this, InputPanchangActivity.class);
        }
        if (activityName.contains("HoroscopeHomeActivity")) {
            intent = new Intent(this, HoroscopeHomeActivity.class);
        }
        if (activityName.contains("DetailedHoroscope")) {
            intent = new Intent(this, DetailedHoroscope.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_change_password:
                //set Analytic
                CUtils.fcmAnalyticsEvents(CGlobalVariables.CHANGE_PASSWORD_BTN_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                Intent intent = new Intent(this, ActChangePassword.class);
                startActivity(intent);
                break;
        }
    }
    /*private class ChangePasswordAndLoginInfo extends AsyncTask<String, String, String> {

        CustomProgressDialog pd;
        String userName;
        String userId;
        String resultString;
        GmailAccountInfo gmailAccountInfo;
        Bitmap roundphoto;
        HashMap IdPic;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            gmailAccountInfo = CUtils.getGmailAccountInfo(EditProfileActivity.this);
            userName = userNameEditText.getText().toString().trim();
            userId = gmailAccountInfo.getId();
            roundphoto = ((BitmapDrawable) edit_profile_img.getDrawable()).getBitmap();//added ny neeraj 4/7/2016
            IdPic = new HashMap();
            pd = new CustomProgressDialog(EditProfileActivity.this, regularTypeface);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String key = CUtils.getApplicationSignatureHashCode(EditProfileActivity.this);
                IdPic.put(userId, roundphoto);
                saveToSharedPreferences(IdPic);
                resultString = new ControllerManager()
                        .changePasswordAndProfile(gmailAccountInfo.getId(), newPassword, oldPassword, userName, key);
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                if (pd != null & pd.isShowing())
                    pd.dismiss();
                String resultStr = parseResult(resultString);
                if (resultStr.equals("-1")) {
                    showCustomMsg(getResources().getString(R.string.password_not_updated));
                } else if (resultStr.equals("0")) {
                    showCustomMsg(getResources().getString(R.string.all_fields_required));
                } else if (resultStr.equals("1")) {
                    if (doLogout) {
                        showCustomMsg(getResources().getString(R.string.sign_out_success));
                        CUtils.updateLoginDetials(false, "", "", "", EditProfileActivity.this);
                    } else {
                        if (newPassword.equals(oldPassword)) {
                            showCustomMsg(getResources().getString(R.string.porfile_updated_successfully));
                            CUtils.saveLoginDetailInPrefs(EditProfileActivity.this, gmailAccountInfo.getId(),
                                    newPassword, userNameEditText.getText().toString(), true, CUtils.isAutoGeneratedPassword(EditProfileActivity.this));
                        } else {
                            showCustomMsg(getResources().getString(R.string.password_reset_successfully));
                            CUtils.saveLoginDetailInPrefs(EditProfileActivity.this, gmailAccountInfo.getId(),
                                    newPassword, userNameEditText.getText().toString(), true, false);
                        }
                        GmailAccountInfo gmailAccountInfo = new GmailAccountInfo();
                        gmailAccountInfo.setId(this.gmailAccountInfo.getId());
                        gmailAccountInfo.setUserName(userNameEditText.getText().toString());
                        CUtils.saveGmailAccountInfo(EditProfileActivity.this, gmailAccountInfo);
                    }

                    finishAllActivity();
                } else if (resultStr.equals("4")) {
                    showCustomMsg(getResources().getString(R.string.sign_up_validation_authentication_failed));
                }
            } catch (Exception e) {
            }

        }
    }*/

    private void saveToSharedPreferences(HashMap idPic) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    SharedPreferences myPrefrence = getSharedPreferences("MyProfilePicture", MODE_PRIVATE);
                    SharedPreferences.Editor editor = myPrefrence.edit();
                    Set mapSet = idPic.entrySet();
                    //Create iterator on Set
                    Iterator mapIterator = mapSet.iterator();
                    System.out.println("Display the key/value of HashMap.");
                    while (mapIterator.hasNext()) {
                        Map.Entry mapEntry = (Map.Entry) mapIterator.next();
                        // getKey Method of HashMap access a key of map
                        String email = (String) mapEntry.getKey();
                        //getValue method returns corresponding key's value
                        try {
                            Bitmap photo = (Bitmap) mapEntry.getValue();
                            editor.putString(email, encodeToBase64(photo));//added by neeraj 23/06/2016 use method to encode and then store to shared preference
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        editor.commit();
                    }
                } catch (Exception e) {
                    //
                }
            }
        });
    }

    private String parseResult(String resultString) {
        String result = "";
        try {
            JSONArray jsonArray = new JSONArray(resultString);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            result = jsonObject.get("Result").toString();
        } catch (Exception e) {

        }
        return result;
    }

    private void showCustomMsg(String msg) {

        MyCustomToast mct = new MyCustomToast(EditProfileActivity.this,
                getLayoutInflater(), EditProfileActivity.this,
                regularTypeface);
        mct.show(msg);
    }

    private void setTypefaceOfViews() {
        int LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication()).getLanguageCode();
        Typeface robotoRegularTypeface = CUtils.getRobotoMedium(EditProfileActivity.this);
        Typeface meduimTypeface = CUtils.getRobotoFont(
                getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.medium);

        regularTypeface = CUtils.getRobotoFont(
                getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
        Typeface robotMediumTypeface = CUtils.getRobotoMedium(getApplicationContext());
        Typeface robotRegularTypeface = CUtils.getRobotoRegular(getApplicationContext());
        userName.setTypeface(robotRegularTypeface);
        userId.setTypeface(robotRegularTypeface);
        title.setTypeface(regularTypeface);
        userNameEditText.setTypeface(robotoRegularTypeface);
        mobileEditText.setTypeface(robotoRegularTypeface);
        saveButton.setTypeface(meduimTypeface);
        mChangePasswordTV.setTypeface(regularTypeface);
        nameTV.setTypeface(regularTypeface);
        mobileTV.setTypeface(regularTypeface);
        materialStatusTV.setTypeface(regularTypeface);
        occupationTV.setTypeface(regularTypeface);
        headingTV.setTypeface(regularTypeface);
        line1TV.setTypeface(regularTypeface);
        line2TV.setTypeface(regularTypeface);
        userDetailTV.setTypeface(meduimTypeface);
        footerHeaderTV.setTypeface(meduimTypeface);
    }

    /**
     * Change Login and paswword info
     */
    public void changePasswordAndLoginInfo() {
        showProgressBar();
        gmailAccountInfo = CUtils.getGmailAccountInfo(EditProfileActivity.this);
        // userName = gmailAccountInfo.getUserName();
        userEmailId = gmailAccountInfo.getId();
        Bitmap roundphoto = ((BitmapDrawable) edit_profile_img.getDrawable()).getBitmap();//added ny neeraj 4/7/2016
        HashMap IdPic = new HashMap();
        IdPic.put(userEmailId, roundphoto);

        saveToSharedPreferences(IdPic);
        String url = CGlobalVariables.CHANGE_PASSWORD_USERNAME;
        StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url, this, true, getParamsNew(), 0).getMyStringRequest();
        queue.add(stringRequest);
    }

    @Override
    public void onResponse(String response, int method) {
        hideProgressBar();
        if (method == 0) {
            try {

                String resultStr = parseResult(response);
                if (resultStr.equals("-1")) {
                    showCustomMsg(getResources().getString(R.string.password_not_updated));
                } else if (resultStr.equals("0")) {
                    showCustomMsg(getResources().getString(R.string.all_fields_required));
                } else if (resultStr.equals("1")) {
                    if (doLogout) {
                        showCustomMsg(getResources().getString(R.string.sign_out_success));
                        CUtils.updateLoginDetials(false, "", "", "", EditProfileActivity.this);
                    } else {
                        if (newPassword.equals(oldPassword)) {
                            showCustomMsg(getResources().getString(R.string.porfile_updated_successfully));
                            CUtils.saveLoginDetailInPrefs(EditProfileActivity.this, gmailAccountInfo.getId(),
                                    newPassword, userNameEditText.getText().toString(), true, CUtils.isAutoGeneratedPassword(EditProfileActivity.this));
                        } else {
                            showCustomMsg(getResources().getString(R.string.password_reset_successfully));
                            CUtils.saveLoginDetailInPrefs(EditProfileActivity.this, gmailAccountInfo.getId(),
                                    newPassword, userNameEditText.getText().toString(), true, false);
                        }
                        GmailAccountInfo gmailAccountInfo = new GmailAccountInfo();
                        gmailAccountInfo.setId(this.gmailAccountInfo.getId());
                        gmailAccountInfo.setUserName(userNameEditText.getText().toString());
                        gmailAccountInfo.setMobileNo(mobileEditText.getText().toString());
                        gmailAccountInfo.setHeading(headingET.getText().toString());
                        gmailAccountInfo.setAddress1(line1ET.getText().toString());
                        gmailAccountInfo.setAddress2(line2ET.getText().toString());
                        gmailAccountInfo.setMaritalStatus(matrialStatusSpinner.getSelectedItemPosition());
                        gmailAccountInfo.setOccupation(occupationStatusSpinner.getSelectedItemPosition());

                        CUtils.saveGmailAccountInfo(EditProfileActivity.this, gmailAccountInfo);
                    }

                    finishAllActivity();
                } else if (resultStr.equals("4")) {
                    showCustomMsg(getResources().getString(R.string.sign_up_validation_authentication_failed));
                }
            } catch (Exception e) {

            }
        } else if (method == 1) {
            //  Log.i("Branding Result-", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                String responsecode = jsonObject.getString("responsecode");
                if (responsecode.equals("1")) {
                    String message = jsonObject.getString("message");
                    String firstName = jsonObject.getString("firstName");
                    String occupation = jsonObject.getString("occupation");
                    String companyName = jsonObject.getString("companyName");
                    String mobile = jsonObject.getString("mobile");
                    String addressLine1 = jsonObject.getString("addressLine1");
                    String addressLine2 = jsonObject.getString("addressLine2");
                    String userId = jsonObject.getString("userId");
                    String email = jsonObject.getString("email");
                    String maritalStatus = jsonObject.getString("maritalStatus");

                    GmailAccountInfo gmailAccountInfo = new GmailAccountInfo();
                    gmailAccountInfo.setId(this.gmailAccountInfo.getId());
                    gmailAccountInfo.setUserName(firstName);
                    gmailAccountInfo.setMobileNo(mobile);
                    gmailAccountInfo.setHeading(companyName);
                    gmailAccountInfo.setAddress1(addressLine1);
                    gmailAccountInfo.setAddress2(addressLine2);
                    gmailAccountInfo.setMaritalStatus(Integer.parseInt(maritalStatus));
                    gmailAccountInfo.setOccupation(Integer.parseInt(occupation));

                    CUtils.saveGmailAccountInfo(EditProfileActivity.this, gmailAccountInfo);
                    userNameEditText.setText(!TextUtils.isEmpty(firstName)?firstName:""); // to avoid null on edittext
                    mobileEditText.setText(mobile);
                    headingET.setText(companyName);
                    line1ET.setText(addressLine1);
                    line2ET.setText(addressLine2);
                    matrialStatusSpinner.setSelection(Integer.parseInt(maritalStatus));
                    occupationStatusSpinner.setSelection(Integer.parseInt(occupation));
                }

            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onError(VolleyError error) {
        hideProgressBar();
        //Log.i("Branding", error.getMessage());
    }

    public Map<String, String> getParamsNew() {
        gmailAccountInfo = CUtils.getGmailAccountInfo(EditProfileActivity.this);
        String userName = userNameEditText.getText().toString().trim();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("key", CUtils.getApplicationSignatureHashCode(EditProfileActivity.this));
        params.put(CGlobalVariables.KEY_EMAIL_ID, CUtils.replaceEmailChar(gmailAccountInfo.getId()));
        params.put("npw", newPassword);
        params.put("opw", oldPassword);
        params.put("userfirstname", userName);

        params.put("asuserplanid", String.valueOf(CUtils.getUserPurchasedPlanFromPreference(EditProfileActivity.this)));
        params.put("companyName", headingET.getText().toString());
        params.put("companyLogoImgUrl", "");
        params.put("addressLine1", line1ET.getText().toString());
        params.put("addressLine2", line2ET.getText().toString());
        params.put("mobileNo", mobileET.getText().toString());
        params.put("userMaritalStatus", String.valueOf(matrialStatusSpinner.getSelectedItemPosition()));
        params.put("userOccupation", String.valueOf(occupationStatusSpinner.getSelectedItemPosition()));

        //Log.e("LOGINN ", "profile " + params.toString());
        return params;
    }

    /**
     * show Progress Bar
     */
    private void showProgressBar() {
        if (pd == null) {
            pd = new CustomProgressDialog(EditProfileActivity.this, regularTypeface);
        }
        pd.setCanceledOnTouchOutside(false);
        if (!pd.isShowing()) {
            pd.show();
        }
    }

    /**
     * hide Progress Bar
     */
    private void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getBrandingDetailFromServer() {
        showProgressBar();
        String url = CGlobalVariables.GET_BRANDING_DETAIL_URL;
        StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url, this, true, getParamsForBrandingDetail(), 1).getMyStringRequest();
        queue.add(stringRequest);
    }

    private HashMap<String, String> getParamsForBrandingDetail() {
        gmailAccountInfo = CUtils.getGmailAccountInfo(EditProfileActivity.this);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("key", CUtils.getApplicationSignatureHashCode(EditProfileActivity.this));
        params.put("us", CUtils.replaceEmailChar(gmailAccountInfo.getId()));
        params.put("pw", oldPassword);

        return params;
    }

    public void checkPermissions() {
        boolean granted = true;
        for (String per : PERMISSIONS) {
            if (!permissionGranted(per)) {
                granted = false;
                break;
            }
        }

        if (granted) {
            captureImage();
        } else {
            requestPermissions();
        }
    }

    private boolean permissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(
                this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQ_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQ_CODE) {
            boolean granted = true;
            for (int result : grantResults) {
                granted = (result == PackageManager.PERMISSION_GRANTED);
                if (!granted) break;
            }

            if (granted) {
                captureImage();
            }
        }
    }

}
