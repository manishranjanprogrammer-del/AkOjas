package com.ojassoft.astrosage.customadapters;

import static com.ojassoft.astrosage.utils.CGlobalVariables.MODULE_SUGGESTED_QUESTIONS_KEY;
import static com.ojassoft.astrosage.utils.CGlobalVariables.matchingScreenIds;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.CURRENT_SCREEN_ID_KEY;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_CONVERSATION_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_IS_CHAT_FOR_HOME_CATEGORY;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_SCREEN_NAME;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.MATCHING_KEY;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.matching.OutputMatchingMasterActivity;
import com.ojassoft.astrosage.ui.fragments.Astroshop_Frag;
import com.ojassoft.astrosage.ui.fragments.Horoscope_Frag;
import com.ojassoft.astrosage.ui.fragments.KundliModules_Frag;
import com.ojassoft.astrosage.ui.fragments.Panchang_Frag;
import com.ojassoft.astrosage.ui.fragments.Reports_frag;
import com.ojassoft.astrosage.ui.fragments.vratfragment.Frag_Year;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.ui.MiniChatWindow;

import java.util.ArrayList;

/**
 * Created by Amit RAutela on 22/2/16.
 * this Adapter is used to set modules in the classes
 */
public class MainModulesAdapter extends BaseAdapter {

    private static int CONSTANT_TO_UPDATE_POSITION_FOR_BELOW_GRID = 4;
    Integer[] moduleIconList;
    String[] moduleNameList;
    Context context;
    Typeface typeface;
    Fragment fragment;
    int LANGUAGE_CODE = 0;
    boolean isBelowGrid;

    public MainModulesAdapter(Context context, Integer[] moduleIconList, String[] moduleNameList, Typeface typeface, Fragment fragment) {
        this.moduleIconList = moduleIconList;
        this.context = context;
        this.moduleNameList = moduleNameList;
        this.typeface = typeface;
        this.fragment = fragment;
        this.LANGUAGE_CODE = ((ActAppModule) context).LANGUAGE_CODE;
        if (moduleNameList.length > CONSTANT_TO_UPDATE_POSITION_FOR_BELOW_GRID) {
            isBelowGrid = true;
        }
    }

    @Override
    public int getCount() {
        return moduleIconList.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(context).inflate(
                    R.layout.home_screen_grid_item, viewGroup, false);
        }

        ImageView rashiIcon = (ImageView) view
                .findViewById(R.id.imageViewRashiIcon);
        ImageView imageViewNew = (ImageView) view.findViewById(R.id.imageViewNew);
        TextView rashiName = (TextView) view
                .findViewById(R.id.textViewRashiName);
        setImageViewResource(rashiIcon,moduleIconList[position]);

        if (moduleNameList[position].contains("$")) {
            if (LANGUAGE_CODE == CGlobalVariables.HINDI) {
                //   rashiName.setText(moduleNameList[position].replace("$", "") + " " + CUtils.getStringData(context, CGlobalVariables.ASTROASKAQUESTIONPRICE, "").replace(".","-"));
                rashiName.setText(moduleNameList[position].replace("$", ""));

            } else {
                //     rashiName.setText(moduleNameList[position].replace("$", "") + " " + CUtils.getStringData(context, CGlobalVariables.ASTROASKAQUESTIONPRICE, ""));
                rashiName.setText(moduleNameList[position].replace("$", ""));

            }
        } else {

            //check plan to update page count text
            if (moduleNameList[position].contains("#")) {
                rashiName.setText(moduleNameList[position].replace("#",
                        CUtils.getCurrentPlanCustomReturnString(CUtils.getUserPurchasedPlanFromPreference(context))));
            } else {
                rashiName.setText(moduleNameList[position]);
            }
        }


            if ((moduleIconList[position] == R.drawable.icon_cogni_astro)) {
                rashiName.setTypeface(CUtils.getRobotoFont(context, LANGUAGE_CODE, CGlobalVariables.medium));
                if (fragment instanceof KundliModules_Frag) {
                    rashiName.setText(context.getResources().getString(R.string.text_varta));
                    rashiIcon.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary_day_night));
                    setImageViewResource(rashiIcon,R.drawable.icon_varta);
                } else {
                    rashiName.setText(context.getResources().getString(R.string.module_cogni_astro));
                    setImageViewResource(rashiIcon,R.drawable.icon_cogni_astro);
                }
            }

            if ((moduleIconList[position] == R.drawable.ic_kundali)) {
                if (LANGUAGE_CODE == CGlobalVariables.TELUGU || LANGUAGE_CODE == CGlobalVariables.TAMIL ||
                        LANGUAGE_CODE == CGlobalVariables.KANNADA || LANGUAGE_CODE == CGlobalVariables.MALAYALAM) {
                    setGifImageViewResource(rashiIcon,R.drawable.icon_south_kundli_ai_gif);
                } else if (LANGUAGE_CODE == CGlobalVariables.BANGALI) {
                    setGifImageViewResource(rashiIcon,R.drawable.icon_east_kundli_ai_gif);
                } else {
                    setGifImageViewResource(rashiIcon,R.drawable.icon_kundli_ai_gif);
                }
            }


        if (fragment instanceof Frag_Year) {
            rashiName.setTextSize(15);
            rashiName.setMinLines(3);
        }
        if (fragment instanceof KundliModules_Frag) {
//            float scale = 0.0f;
//            try {
//                scale = context.getResources().getConfiguration().fontScale;
//            } catch (Exception e) {
//                //
//            }
//            if (scale > 1.0 && scale < 1.2) {
//                rashiName.setTextSize(12);
//            } else if (scale > 1.2 && scale < 1.35) {
//                rashiName.setTextSize(10);
//            } else if (scale > 1.35) {
//                rashiName.setTextSize(9);
//            }
            rashiName.setPadding(10,15,10,15);
            rashiName.setTextSize(14);
            rashiName.setMinLines(2);
        }

        rashiName.setTypeface(typeface);
        view.setId(position);

        view.setOnClickListener(view1 -> {
            callModulesFromCallingMethods(position);
        });

        return view;
    }


    private void setImageViewResource(ImageView imageView,int resource){
        try{
            imageView.setImageResource(resource);
        } catch (Exception e){

        }
    }

    private void setGifImageViewResource(ImageView imageView, int resource) {
        Glide.with(imageView).load(resource).into(imageView);
    }


    /**
     * @created by : Amit Rautela
     * @created on : 2/2/16
     * @disc : This method is used to call modules from calling fragments
     */
    private void callModulesFromCallingMethods(int position) {

        try {
            if (fragment instanceof KundliModules_Frag) {
                //condition to check and update grid click event
                if (isBelowGrid) {
                    KundliModules_Frag.callActivity(position + CONSTANT_TO_UPDATE_POSITION_FOR_BELOW_GRID);
                } else {
                    KundliModules_Frag.callActivity(position);
                }
            } else if (fragment instanceof Frag_Year) {
                Frag_Year.callActivity(position);
            } else if (fragment instanceof Reports_frag) {
                Reports_frag.callActivity(position);
            } else if (fragment instanceof Panchang_Frag) {
                Panchang_Frag.callActivity(position);
            } else if (fragment instanceof Horoscope_Frag) {
                Horoscope_Frag.callActivity(position, context);
            } else if (fragment instanceof Astroshop_Frag) {
                //Astroshop_Frag.callActivity(position);
            }
        }catch (Exception e){
            //
        }
    }

}
