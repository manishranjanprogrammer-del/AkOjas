package com.ojassoft.astrosage.varta.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.adapters.LanguageSelectionAdapter;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LanguageSelectionActivity extends BaseActivity
{

    ImageView ivback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_selection);

        ivback = (ImageView)findViewById(R.id.ivback);
        if(CGlobalVariables.fromSetting==0)
        {
            ivback.setVisibility(View.GONE);
        }
        else
        {
            ivback.setVisibility(View.VISIBLE);
        }
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        CGlobalVariables.LANGUAGE_CODE = CUtils.getLanguageCodeFromPreference(LanguageSelectionActivity.this);
        if(CUtils.getScreenCodeFromPreference(LanguageSelectionActivity.this)!=0)
        {
            if(CGlobalVariables.fromSetting==0)
            {
                //Intent intent = new Intent(LanguageSelectionActivity.this, AppIntroActivity.class);
                //startActivity(intent);
                //finishAffinity();
            }
        }

        String[] myListData = new String[] {
                getString(R.string.english),
                getString(R.string.hindi),
                getString(R.string.tamil),
                getString(R.string.bangali),
                getString(R.string.telugu),
                getString(R.string.marathi),
                getString(R.string.kannad),
                getString(R.string.gujarati),
                getString(R.string.malayalam),
                getString(R.string.punjabi)

        };
        int[] myListDataValue = new int[] {
                CGlobalVariables.ENGLISH,
                CGlobalVariables.HINDI,
                CGlobalVariables.TAMIL,
                CGlobalVariables.BANGALI,
                CGlobalVariables.TELUGU,
                CGlobalVariables.MARATHI,
                CGlobalVariables.KANNADA,
                CGlobalVariables.GUJARATI,
                CGlobalVariables.MALAYALAM,
                CGlobalVariables.PUNJABI
        };

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LanguageSelectionAdapter adapter = new LanguageSelectionAdapter(LanguageSelectionActivity.this,LanguageSelectionActivity.this, myListData, myListDataValue);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    protected void onPause()
    {
        super.onPause();
        CGlobalVariables.fromSetting=0;
    }
}