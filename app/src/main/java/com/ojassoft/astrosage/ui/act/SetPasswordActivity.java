package com.ojassoft.astrosage.ui.act;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

//import com.google.analytics.tracking.android.EasyTracker;
import com.libojassoft.android.custom.controls.CustomToast;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.ui.activity.BaseActivity;

/**
 * Created by ojas on 22/6/16.
 */
public class SetPasswordActivity extends BaseActivity {
    Toolbar toolbar;
    TextView title;
    Button nextButton;
    EditText passwordEditText, verifyPasswordEditText;
    int LANGUAGE_CODE;
    Typeface robotoRegularTypeface;
    Typeface meduimTypeface;
    Typeface regularTypeface;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_password_activity_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar_magazine);
        nextButton = (Button) findViewById(R.id.nextBtn);
        passwordEditText = (EditText) findViewById(R.id.password_edittext);
        verifyPasswordEditText = (EditText) findViewById(R.id.verify_password_edittext);
        title = (TextView) toolbar.findViewById(R.id.tvToolBarTitle);
        setSupportActionBar(toolbar);
        title.setText(getResources().getString(R.string.set_password_text));
        setTypefaceOfViews();

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        if (((AstrosageKundliApplication) getApplication()).getLanguageCode() == CGlobalVariables.ENGLISH) {
            nextButton.setText(getResources().getString(R.string.button_next).toUpperCase());
        }

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwordText = passwordEditText.getText().toString().trim();
                String verifyPasswordText = verifyPasswordEditText.getText().toString().trim();
                if (!passwordText.equals("") && !verifyPasswordText.equals("")) {
                    if (passwordText.equals(verifyPasswordText)) {
                        Intent intent = new Intent(SetPasswordActivity.this, EditProfileActivity.class);
                        intent.putExtra("password", passwordText);
                        intent.putExtra("activity", getCallingActivity().getClassName());
                        intent.putExtra("dologout", getIntent().getBooleanExtra("dologout", true));
                        startActivity(intent);
                    } else {
                        new CustomToast(SetPasswordActivity.this, getLayoutInflater(), SetPasswordActivity.this, regularTypeface).show(getResources().getString(R.string.password_notmatched));
                        //Toast.makeText(SetPasswordActivity.this, getResources().getString(R.string.password_notmatched), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    new CustomToast(SetPasswordActivity.this, getLayoutInflater(), SetPasswordActivity.this, regularTypeface).show(getResources().getString(R.string.please_enter_password));
                    //Toast.makeText(SetPasswordActivity.this, getResources().getString(R.string.please_enter_password), Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setTypefaceOfViews() {
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication()).getLanguageCode();
        robotoRegularTypeface = CUtils.getRobotoMedium(SetPasswordActivity.this);
        meduimTypeface = CUtils.getRobotoFont(
                getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.medium);
        regularTypeface = CUtils.getRobotoFont(
                getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
        nextButton.setTypeface(meduimTypeface);
        passwordEditText.setTypeface(robotoRegularTypeface);
        verifyPasswordEditText.setTypeface(robotoRegularTypeface);
        title.setTypeface(regularTypeface);

    }

}
