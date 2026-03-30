package com.ojassoft.astrosage.ui.customcontrols;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.customadapters.CustomExpandableListBookMarkHistoryAdapter;
import com.ojassoft.astrosage.jinterface.IHistoryCollectionScreen;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.Locale;

public class ActBookmarkAndHistory extends BaseInputActivity implements IHistoryCollectionScreen {
    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    public Typeface typeface;

    public ActBookmarkAndHistory() {
        super(R.id.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_bookmark);
        LANGUAGE_CODE = CUtils.getLanguageCodeFromPreference(this);
        typeface = CUtils.getRobotoFont(getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
        ExpandableListView myList = (ExpandableListView) findViewById(R.id.bookexpendable);
        myList.setCacheColorHint(Color.TRANSPARENT);

        myList.setChildDivider(new ColorDrawable(getResources().getColor(R.color.light_gray)));
        myList.setDivider(new ColorDrawable(getResources().getColor(R.color.black_87)));
        myList.setDividerHeight(1);
        /*myList.setSelected(true);*/
        //myList.setDrawSelectorOnTop(true);
        //myList.setGroupIndicator(ResourcesCompat.getDrawable(getResources(), R.drawable.expandable_collapse_icons, null));
        myList.setGroupIndicator(null);
        myList.setChildIndicator(null);
        //myList.setSelector(android.R.color.transparent);
        Configuration config = getResources().getConfiguration();
        config.locale = Locale.getDefault();
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        CustomExpandableListBookMarkHistoryAdapter myAdapter = new CustomExpandableListBookMarkHistoryAdapter(this, this, typeface, LANGUAGE_CODE);
        myList.setAdapter(myAdapter);
        //setContentView(myList);

        myList.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                try {
                    //DISABLED BY BIJENDRA  ON 10-06-14
                    ///CUtils.gotoUserSelectedScreen(groupPosition, childPosition, ActBookmarkAndHistory.this);
                    //ActBookmarkAndHistory.this.finish();
                    //END
                    //ADDED BY BIJENDRA ON 10-06-14
                    sendSelectedValuesToShow(groupPosition, childPosition);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        myList.expandGroup(0);
        myList.expandGroup(1);

       /* ViewTreeObserver vto = myList.getViewTreeObserver();

        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    myList.setIndicatorBounds(myList.getRight() - 100, myList.getWidth());
                } else {
                    myList.setIndicatorBoundsRelative(myList.getRight() - 100, myList.getWidth());
                }
            }
        });*/

    }

    private void sendSelectedValuesToShow(int groupPosition, int childPosition) {
        Bundle bundle = new Bundle();
        bundle.putInt("GORUP_ID", groupPosition);
        bundle.putInt("CHILD_ID", childPosition);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        this.finish();
    }

    //ADDED BY BIJENDRA ON 10-06-14
    @Override
    public void onSelectHistoryitem(int groupId, int childId) {
        sendSelectedValuesToShow(groupId, childId);
    }



}
