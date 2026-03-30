package com.ojassoft.astrosage.ui.act;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.widget.Toolbar;
import android.text.util.Linkify;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

//import com.google.analytics.tracking.android.EasyTracker;
import com.ojassoft.astrosage.R;

import java.util.List;

public class ActDisclaimerAndPrivacyPolicy extends BaseInputActivity {

    Toolbar tool_barAppModule;
    TabLayout tabs;
    TextView tvTitle;

    public ActDisclaimerAndPrivacyPolicy() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        int screenType = getIntent().getIntExtra("screenType", 0);
        if (screenType == 0) {
            setContentView(R.layout.lay_disclaimer);
        } else {
            setContentView(R.layout.lay_privacy_policy);
            TextView txtEmail = (TextView) findViewById(R.id.textView8);
            if (isEmailClientAvailble()) {
                Linkify.addLinks(txtEmail, Linkify.EMAIL_ADDRESSES);
            }
        }

        tool_barAppModule = (Toolbar) findViewById(R.id.tool_barAppModule);
        setSupportActionBar(tool_barAppModule);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tabs = (TabLayout)findViewById(R.id.tabs);
        tabs.setVisibility(View.GONE);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setTypeface(robotRegularTypeface);

        if (screenType == 0) {
            tvTitle.setText(getResources().getString(R.string.disclaimer_on_about));
        }else{
            tvTitle.setText(getResources().getString(R.string.privacy_policy_on_aboutus));
        }

    }

    private boolean isEmailClientAvailble() {
        Uri uri = Uri.parse("mailto:customercare@AstroSage.com");
        Intent i = new Intent(Intent.ACTION_VIEW, uri);
        List<ResolveInfo> ia = getApplicationContext().getPackageManager().queryIntentActivities(i, PackageManager.MATCH_DEFAULT_ONLY);
        return (ia.size() > 0);
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onStart()
     */
    @Override
    protected void onStart() {
        super.onStart();
        //EasyTracker.getInstance().activityStart(this);
    }

    /* (non-Javadoc)
     * @see com.actionbarsherlock.app.SherlockFragmentActivity#onStop()
     */
    @Override
    protected void onStop() {
        super.onStop();
        //EasyTracker.getInstance().activityStop(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
