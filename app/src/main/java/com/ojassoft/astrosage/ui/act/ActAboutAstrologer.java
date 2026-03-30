package com.ojassoft.astrosage.ui.act;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ojassoft.astrosage.R;

/**
 * Created by ojas-02 on 27/3/17.
 */

public class ActAboutAstrologer extends BaseInputActivity {
    private TextView txtexperince, txtexperincecontent, txtexpertise, txtexpertiseinfo, txtaboutastrologer, txtaboutastrologerdes;
    private String astroloname, focusarea, description, experience;
    private Toolbar tool_barAppModule;
    private TabLayout tabLayout;
    Typeface typeface;
    private TextView tvTitle;

    public ActAboutAstrologer() {
        super(R.string.app_name);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_astrologer);
        tool_barAppModule = (Toolbar) findViewById(R.id.tool_barAppModule);
        setSupportActionBar(tool_barAppModule);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvTitle = (TextView) findViewById(R.id.tvTitle);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        Bundle value = getIntent().getExtras();
        astroloname = value.getString("astroloname");
        focusarea = value.getString("focusarea");
        description = value.getString("description");
        experience = value.getString("experience");
        tvTitle.setText(astroloname);
        tvTitle.setTypeface(typeface);
        txtexperince = (TextView) findViewById(R.id.txtexperince);
        txtexperincecontent = (TextView) findViewById(R.id.txtexperincecontent);
        txtexperincecontent.setText(experience);
        txtexpertise = (TextView)
                findViewById(R.id.txtexpertise);
        txtexpertiseinfo = (TextView) findViewById(R.id.txtexpertiseinfo);
        txtexpertiseinfo.setText(focusarea);
        txtaboutastrologer = (TextView) findViewById(R.id.txtaboutastrologer);
        txtaboutastrologerdes = (TextView) findViewById(R.id.txtaboutastrologerdes);
        txtaboutastrologerdes.setText(" " + Html.fromHtml(description.replace("@^", "<").replace("^@", " >")));
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
