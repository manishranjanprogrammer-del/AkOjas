package com.ojassoft.astrosage.ui.act;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.toolbox.NetworkImageView;
import com.google.android.material.tabs.TabLayout;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.ui.fragments.SetChoosePhotoDialoge;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.HttpUtilityImageUpload;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DirectoryListingProfileImageActivity extends BaseInputActivity implements View.OnClickListener {
    private Typeface typeface;
    private Toolbar toolbar;
    TabLayout tabLayout;
    TextView tvTitle;
    NetworkImageView networkProfileIV;
    TextView uploadPhotoTV;
    Button chooseFileBtn;
    Button submitBtn;
    TextView photoInfoTV;
    ImageView selectedImageIV;
    ImageView profileImg;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 1000, GALLERY_IMAGE_REQUEST_CODE = 2000;
    private Bitmap bitmapImage = null;
    private String base64Image;
    public static final String image = "image";
    public static final String imageId = "imageid";
    private String profileImageUrl;
    private CustomProgressDialog pd;
    static int imageSizeInKB;

    public DirectoryListingProfileImageActivity() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.directory_listing_profile_image_layout);
        init();
        enableToolBar();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_file_btn:
                onChooseButtonClicked();
                break;
            case R.id.submit_btn:
                if (CUtils.isConnectedWithInternet(this)) {
                    if (bitmapImage == null) {
                        showSnackbar(submitBtn, getResources().getString(R.string.upload_img_err));
                    } else {
                        if (imageSizeInKB > (2 * 1024)) { //max image size will be 2 mb
                            showSnackbar(submitBtn, getResources().getString(R.string.upload_img_size_err));
                        } else {
                            uploadBitmap();
                        }
                    }
                } else {
                    showSnackbar(submitBtn, getResources().getString(R.string.no_internet));
                }
                break;

        }
    }

    private void init() {

        LANGUAGE_CODE = ((AstrosageKundliApplication) DirectoryListingProfileImageActivity.this.getApplication()).getLanguageCode();
        typeface = CUtils.getRobotoFont(DirectoryListingProfileImageActivity.this, LANGUAGE_CODE, CGlobalVariables.regular);
        toolbar = findViewById(R.id.tool_barAppModule);
        setSupportActionBar(toolbar);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.directory_listing));
        tvTitle.setVisibility(View.VISIBLE);

        uploadPhotoTV = findViewById(R.id.upload_photo);
        chooseFileBtn = findViewById(R.id.choose_file_btn);
        photoInfoTV = findViewById(R.id.photo_info);
        selectedImageIV = findViewById(R.id.selected_image);
        networkProfileIV = findViewById(R.id.networkProfileIV);
        profileImg = findViewById(R.id.profile_img);
        submitBtn = findViewById(R.id.submit_btn);
        uploadPhotoTV.setTypeface(mediumTypeface);
        photoInfoTV.setTypeface(regularTypeface);
        chooseFileBtn.setTypeface(mediumTypeface);
        submitBtn.setTypeface(mediumTypeface);

        chooseFileBtn.setOnClickListener(this);
        selectedImageIV.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
        if (getIntent() != null) {
            profileImageUrl = getIntent().getStringExtra("profileImageUrl");
        }

        if (TextUtils.isEmpty(profileImageUrl)) {
            isShowServerProfileImage(false);
        } else {
            isShowServerProfileImage(true);
            int time = (int) (System.currentTimeMillis());
            profileImageUrl = profileImageUrl+"?timestamp="+time;
            networkProfileIV.setImageUrl(profileImageUrl, VolleySingleton.getInstance(this).getImageLoader());
        }
    }

    private void isShowServerProfileImage(boolean isShow) {
        if (!isShow) {
            networkProfileIV.setVisibility(View.GONE);
            profileImg.setVisibility(View.VISIBLE);
        } else {
            networkProfileIV.setVisibility(View.VISIBLE);
            profileImg.setVisibility(View.GONE);
        }
        selectedImageIV.setVisibility(View.GONE);
    }

    private void showLocalProfileImage() {
        profileImg.setVisibility(View.GONE);
        selectedImageIV.setVisibility(View.VISIBLE);
        networkProfileIV.setVisibility(View.GONE);
    }

    private void enableToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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

    private void onChooseButtonClicked() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        SetChoosePhotoDialoge profilePictureDialoge = new SetChoosePhotoDialoge();
        profilePictureDialoge.show(fm, "setProfilePic");
        ft.commit();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                bitmapImage = (Bitmap) data.getExtras().get("data");
                BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmapImage);
                selectedImageIV.setBackground(bitmapDrawable);
                photoInfoTV.setText(R.string.pic_selected);
                showLocalProfileImage();
            } catch (Exception e) {
                //
            }

        } else if (requestCode == GALLERY_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                bitmapImage = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmapImage);
                selectedImageIV.setBackground(bitmapDrawable);
                photoInfoTV.setText(R.string.pic_selected);
                showLocalProfileImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadBitmap() {

        try {

            base64Image = encodeToBase64(bitmapImage);

            //Async task to encode a image into Base64 and send to server

            Map<String, String> params = new HashMap<String, String>();

            params.put(image, base64Image);
            params.put(imageId, String.valueOf(System.currentTimeMillis()));
            params.put("asusid", CUtils.getUserName(this));
            params.put("key", CUtils.getApplicationSignatureHashCode(this));
            params.put("deviceid", CUtils.getMyAndroidId(this));
            params.put("packagename", BuildConfig.APPLICATION_ID);
            params.put("appversion", BuildConfig.VERSION_NAME);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                new DirectoryListingProfileImageActivity.UploadFile().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
            } else {
                new DirectoryListingProfileImageActivity.UploadFile().execute(params);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String encodeToBase64(Bitmap image) {
        Bitmap bitmap = image;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        imageSizeInKB = (bytes.length / 1024);
        String imageEncoded = Base64.encodeToString(bytes, Base64.DEFAULT);
        return imageEncoded;
    }

    private void showProgressBar() {
        try {
            pd = new CustomProgressDialog(this, regularTypeface);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class UploadFile extends AsyncTask<Map<String, String>, Void, String> {


        protected void onPreExecute() {
            showProgressBar();
        }

        @Override
        protected String doInBackground(Map<String, String>... objects) {

            String data = "-1";
            try {
                int image_id = 0;
                String url = "https://astrologers.astrosage.com/app/save-image.asp";
                //String url = "https://api2.astrosage.com/GCM/test.asp";


                JSONArray jsonResponse = HttpUtilityImageUpload.sendPostRequest(url, objects[0], DirectoryListingProfileImageActivity.this, image_id);
                if (jsonResponse != null) {
                    for (int i = 0; i < jsonResponse.length(); i++) {
                        try {
                            data = jsonResponse.getJSONObject(i).getString("result");
                        } catch (JSONException e) {
                            //
                        }
                    }
                }
            } catch (Exception e) {

            }
            return data;
        }


        protected void onPostExecute(String result) {
            // NOTE: You can call UI Element here.
            hideProgressBar();
            try {
                if (result.equalsIgnoreCase("1")) {
                    showSnackbar(submitBtn, getResources().getString(R.string.upload_img_suc));
                } else {
                    showSnackbar(submitBtn, getResources().getString(R.string.something_wrong_error));
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                showSnackbar(submitBtn, getResources().getString(R.string.something_wrong_error));
            }
        }

    }

}

