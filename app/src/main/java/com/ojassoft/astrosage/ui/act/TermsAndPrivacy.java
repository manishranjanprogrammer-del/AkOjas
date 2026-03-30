package com.ojassoft.astrosage.ui.act;

import android.content.Intent;
import android.graphics.Typeface;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.notification.ActShowOjasSoftArticles;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

public class TermsAndPrivacy extends BaseInputActivity {

    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    private Typeface typeface;
    private Toolbar tool_barAppModule;
    private TextView tvTitle,tvText;
    private TabLayout tabLayout;

    public TermsAndPrivacy() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_policy);

        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication())
                .getLanguageCode();
        typeface = CUtils.getRobotoFont(
                getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);

        tool_barAppModule = (Toolbar) findViewById(R.id.tool_barAppModule);

        setSupportActionBar(tool_barAppModule);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(getResources().getString(R.string.terms_and_privacy));
        tvTitle.setTypeface(mediumTypeface);

        String text = getResources().getString(R.string.terms_and_privacy_content);
        SpannableString ss = new SpannableString(text);

        ClickableSpan span1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent intent = new Intent(TermsAndPrivacy.this, ActShowOjasSoftArticles.class);
                intent.putExtra(CGlobalVariables.ASTRO_WEBVIEW_TITLE_KEY,CGlobalVariables.MODULE_ABOUT_US);
                intent.putExtra("URL", CGlobalVariables.ASTROSAGE_TERMS_CONDITIONS_URL);
                intent.putExtra("TITLE_TO_SHOW", "Terms and Conditions");
                startActivity(intent);
            }
        };

        ClickableSpan span2 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent intent = new Intent(TermsAndPrivacy.this, ActShowOjasSoftArticles.class);
                intent.putExtra(CGlobalVariables.ASTRO_WEBVIEW_TITLE_KEY,CGlobalVariables.MODULE_ABOUT_US);
                intent.putExtra("URL", CGlobalVariables.ASTROSAGE_PRIVACY_POLICY_URL);
                intent.putExtra("TITLE_TO_SHOW", "Privacy Policy");
                startActivity(intent);
            }
        };

        ss.setSpan(span1, 82, 102, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(span2, 107, 121, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvText = (TextView)findViewById(R.id.tvText);
        tvText.setTypeface(robotRegularTypeface);

        tvText.setText(ss);
        tvText.setMovementMethod(LinkMovementMethod.getInstance());
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
}
