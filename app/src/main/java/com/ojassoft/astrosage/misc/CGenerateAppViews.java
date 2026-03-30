package com.ojassoft.astrosage.misc;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.BounceInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.BeanKpCIL;
import com.ojassoft.astrosage.beans.BeanKpKhullarCIL;
import com.ojassoft.astrosage.beans.BeanKpPlanetSigWithStrenght;
import com.ojassoft.astrosage.beans.BeanNakshtraNadi;
import com.ojassoft.astrosage.beans.BeanTajikVarshaphal;
import com.ojassoft.astrosage.beans.KundliPlanetArray;
import com.ojassoft.astrosage.beans.OutBirthDetail;
import com.ojassoft.astrosage.beans.OutKpHouseSignificators;
import com.ojassoft.astrosage.beans.OutKpMisc;
import com.ojassoft.astrosage.beans.OutKpRulingPlanets;
import com.ojassoft.astrosage.controller.ControllerManager;
import com.ojassoft.astrosage.customexceptions.UIKpSystemMiscException;
import com.ojassoft.astrosage.customexceptions.UIKundliMiscCalculationException;
import com.ojassoft.astrosage.ui.act.OutputMasterActivity;
import com.ojassoft.astrosage.ui.customviews.basic.ViewAstakvargaTable;
import com.ojassoft.astrosage.ui.customviews.basic.ViewBirthDetails;
import com.ojassoft.astrosage.ui.customviews.basic.ViewDrawBhavaBeginAndEnd;
import com.ojassoft.astrosage.ui.customviews.basic.ViewPanchang;
import com.ojassoft.astrosage.ui.customviews.basic.viewMiscDrawable;
import com.ojassoft.astrosage.ui.customviews.common.ViewDrawRotateChalitKundli;
import com.ojassoft.astrosage.ui.customviews.common.ViewDrawRotateKundli;
import com.ojassoft.astrosage.ui.customviews.kp.ViewKPNakshtraNadi;
import com.ojassoft.astrosage.ui.customviews.kp.ViewKpCuspalPositions;
import com.ojassoft.astrosage.ui.customviews.kp.ViewKpExtension4Step;
import com.ojassoft.astrosage.ui.customviews.kp.ViewKpExtensionCIL;
import com.ojassoft.astrosage.ui.customviews.kp.ViewKpExtensionKhullarCIL;
import com.ojassoft.astrosage.ui.customviews.kp.ViewKpHouseSignificators;
import com.ojassoft.astrosage.ui.customviews.kp.ViewKpMisc;
import com.ojassoft.astrosage.ui.customviews.kp.ViewKpPlanetSigWithStrenght;
import com.ojassoft.astrosage.ui.customviews.kp.ViewKpPlanetSignification;
import com.ojassoft.astrosage.ui.customviews.kp.ViewKpPlanetaryPositions;
import com.ojassoft.astrosage.ui.customviews.kp.ViewKpRulingPlanets;
import com.ojassoft.astrosage.ui.customviews.predictions.WebViewPredictions;
import com.ojassoft.astrosage.ui.customviews.shodashvarga.ViewShodashvargaNorthHora;
import com.ojassoft.astrosage.ui.customviews.tajik.ViewVarshphalTable;
import com.ojassoft.astrosage.ui.fragments.AppViewFragment;
import com.ojassoft.astrosage.ui.fragments.FragDasa;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CScreenConstants;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.kpextension.BeanKP4Step;

public class CGenerateAppViews {

    static ViewKpPlanetaryPositions viewKpPlanetaryPositions;
    static viewMiscDrawable viewMiscDrawables;
    static ViewKpCuspalPositions viewKpCuspalPositions;
    static ViewKpPlanetSignification viewPlanetSignification;
    static ViewKpHouseSignificators viewKpHouseSignificators;
    static Context cxt;
    static CScreenConstants cscreenConstants;
    static View planetSubView1;
    static View viewChalitKundli;
    static View birthDetailView;
    static View panchangView;
    static View astakvargaView;
    static View tajikVarshphalViews = null;
    static View tajikVarshphalViewsPlant = null;

    public static View getViewFor(int moduleId, int subModuleId, Context context, Typeface typeface, int chartStyle, int languageCode, CScreenConstants SCREEN_CONSTANTS, int yearNumber) throws Exception {
       /* if (languageCode == 5) {
            languageCode = 0;
        }
*/
        View view = null;
        switch (moduleId) {

            case CGlobalVariables.MODULE_BASIC:
                try {

                    view = getViewOfBasic(subModuleId, context, typeface, chartStyle, languageCode, SCREEN_CONSTANTS);
                } catch (UIKpSystemMiscException e) {
                    e.printStackTrace();
                }
                break;
            case CGlobalVariables.MODULE_DASA:
                //dasha does not have any view
                break;
            case CGlobalVariables.MODULE_PREDICTION:
                view = getViewOfPredictions(subModuleId, context, languageCode, SCREEN_CONSTANTS);
                break;
            case CGlobalVariables.MODULE_KP:
                try {
                    view = getViewOfKP(subModuleId, context, typeface, chartStyle, languageCode, SCREEN_CONSTANTS);
                } catch (UIKpSystemMiscException e) {
                    e.printStackTrace();
                }

                break;
            case CGlobalVariables.MODULE_SHODASHVARGA:
                try {
                    view = getViewOfShodashvarga(subModuleId, context, typeface, chartStyle, languageCode, SCREEN_CONSTANTS);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case CGlobalVariables.MODULE_LALKITAB:
                try {
                    view = getViewOfLalkitab(subModuleId, context, typeface, chartStyle, languageCode, SCREEN_CONSTANTS, yearNumber);
                } catch (UIKundliMiscCalculationException e) {
                    e.printStackTrace();
                }
                break;
            case CGlobalVariables.MODULE_VARSHAPHAL:

                view = getViewOfTajikVarshphal(subModuleId, context, typeface, chartStyle, languageCode, SCREEN_CONSTANTS, CGlobal.getCGlobalObject().getBeanTajikVarshaphal(), yearNumber);

                break;
            case CGlobalVariables.MODULE_MISC:
                view = getViewOfMiscellaneous(subModuleId, context, languageCode, SCREEN_CONSTANTS);
                break;

        }

        return view;
    }

    public static FragDasa getDasaFragment() {
        FragDasa fragDasa = new FragDasa();
        return fragDasa;
    }

    public static AppViewFragment getAppViewFragment() {
        AppViewFragment fragAppViewFrag = new AppViewFragment();
        return fragAppViewFrag;
    }

    public static View getViewOfKP(int subModuleId, Context context, Typeface typeface, int chartStyle, int languageCode, CScreenConstants SCREEN_CONSTANTS) throws UIKpSystemMiscException {

        View view = null;
        cxt = context;
        cscreenConstants = SCREEN_CONSTANTS;

        ControllerManager objControllerManager = new ControllerManager();
        boolean isTablet = CUtils.isTablet(context);

        switch (subModuleId) {

            case CGlobalVariables.SUB_MODULE_KP_KP_CHART:
                //kP CHART
                double[] planetDegreeArray = null;
                planetDegreeArray = objControllerManager.getKPPlanetsDegreeArray(CGlobal.getCGlobalObject().getInKPPlanetsAndCuspObject());
                double[] cuspDegreeArray = null;
                cuspDegreeArray = objControllerManager.getKpCuspArray(CGlobal.getCGlobalObject().getInKPPlanetsAndCuspObject());
                if (chartStyle == 0) {
                    ViewDrawRotateChalitKundli viewDrawRotateChalitKundliNorth = new ViewDrawRotateChalitKundli(context,
                            context.getResources().getStringArray(R.array.VarChartPlanets), planetDegreeArray, cuspDegreeArray, cuspDegreeArray, CGlobalVariables.enuKundliType.NORTH, context.getResources().getStringArray(R.array.VarChartPlanets)[12], isTablet, SCREEN_CONSTANTS);

                    viewDrawRotateChalitKundliNorth.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    //final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    //scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    layout.addView(viewDrawRotateChalitKundliNorth);
                    //scrollView.addView(layout);

                    view = layout;
                } else if (chartStyle == 1) {
                    ViewDrawRotateChalitKundli viewDrawRotateChalitKundliNorth = new ViewDrawRotateChalitKundli(context,
                            context.getResources().getStringArray(R.array.VarChartPlanets), planetDegreeArray, cuspDegreeArray, cuspDegreeArray, CGlobalVariables.enuKundliType.SOUTH, context.getResources().getStringArray(R.array.VarChartPlanets)[12], isTablet, SCREEN_CONSTANTS);

                    viewDrawRotateChalitKundliNorth.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));

                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    //final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    //scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    layout.addView(viewDrawRotateChalitKundliNorth);
                    //scrollView.addView(layout);
                    view = layout;
                } else if (chartStyle == 2) {
                    ViewDrawRotateChalitKundli viewDrawRotateChalitKundliNorth = new ViewDrawRotateChalitKundli(context,
                            context.getResources().getStringArray(R.array.VarChartPlanets), planetDegreeArray, cuspDegreeArray, cuspDegreeArray, CGlobalVariables.enuKundliType.EAST, context.getResources().getStringArray(R.array.VarChartPlanets)[12], isTablet, SCREEN_CONSTANTS);

                    viewDrawRotateChalitKundliNorth.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));

                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    //final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    //scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    layout.addView(viewDrawRotateChalitKundliNorth);
                    //scrollView.addView(layout);
                    view = layout;


                }
                break;
            case CGlobalVariables.SUB_MODULE_KP_RASHI_CHART:
                //KP RASI CHART
                int[] planetsInRashiLagna2 = objControllerManager.getKPLagnaRashiKundliPlanetsRashiArray(CGlobal.getCGlobalObject().getInKPPlanetsAndCuspObject());
                if (chartStyle == 0) {
                    ViewDrawRotateKundli ViewDrawRotateKundliNorth = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiLagna2,
                            CGlobalVariables.enuKundliType.NORTH, isTablet, SCREEN_CONSTANTS, -1, languageCode);
                    ViewDrawRotateKundliNorth.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    //final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    //scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    layout.addView(ViewDrawRotateKundliNorth);
                    //scrollView.addView(layout);
                    view = layout;
                } else if (chartStyle == 1) {
                    ViewDrawRotateKundli ViewDrawRotateKundliSouth = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiLagna2,
                            CGlobalVariables.enuKundliType.SOUTH, isTablet, SCREEN_CONSTANTS, -1, languageCode);
                    ViewDrawRotateKundliSouth.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    //final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    //scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    layout.addView(ViewDrawRotateKundliSouth);
                    //scrollView.addView(layout);
                    view = layout;
                } else if (chartStyle == 2) {
                    ViewDrawRotateKundli ViewDrawRotateKundliSouth = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiLagna2,
                            CGlobalVariables.enuKundliType.EAST, isTablet, SCREEN_CONSTANTS, -1, languageCode);
                    ViewDrawRotateKundliSouth.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    //final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    //scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    layout.addView(ViewDrawRotateKundliSouth);
                    //scrollView.addView(layout);
                    view = layout;
                }
                break;
            case CGlobalVariables.SUB_MODULE_KP_PLANETS:
                //KP PLANET POSITION
                String[] KpPlanetaryPositionsHeading = null;
                String[] KpPlanetaryPositionsPlanets = null;
                KpPlanetaryPositionsHeading = context.getResources().getStringArray(R.array.kp_planetary_positions_sub_heading_list);
                KpPlanetaryPositionsPlanets = context.getResources().getStringArray(R.array.planet_and_lagna_name_list);
                String[] arrDirect = null;
                String[] arrCombust = null;
                arrDirect = CUtils.getDirectArray(context);
                arrCombust = CUtils.getCombustArray(context);

                String[][] kpPlanetPosition = objControllerManager.getKpPlanetaryPositions(CGlobal.getCGlobalObject().getInKPPlanetsAndCuspObject(), context.getResources().getStringArray(R.array.rasi_lord_short_name_list), context.getResources().getStringArray(R.array.nak_lord_short_name_list), context.getResources().getString(R.string.degree_sign), context.getResources().getString(R.string.minute_sign), context.getResources().getString(R.string.second_sign));
                viewKpPlanetaryPositions = new ViewKpPlanetaryPositions(context, kpPlanetPosition, KpPlanetaryPositionsHeading, KpPlanetaryPositionsPlanets, arrDirect, arrCombust, typeface, context.getResources().getString(R.string.left_bracket), context.getResources().getString(R.string.right_bracket), SCREEN_CONSTANTS, context.getResources().getStringArray(R.array.kp_planet_note_item_list));
                viewKpPlanetaryPositions.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth, (int) SCREEN_CONSTANTS.DeviceScreenHeight));

                //To make scrollabel chart
                final RelativeLayout layout = new RelativeLayout(context);
                //final ScrollView scrollView = new ScrollView(context);
                layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                //scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                //End
                layout.addView(viewKpPlanetaryPositions);
                //scrollView.addView(layout);
                view = layout;
                break;
            case CGlobalVariables.SUB_MODULE_KP_CUSPS:
                //KP CUSPAL POSITIONS
                String[] KpCuspalPositionsHeading = context.getResources().getStringArray(R.array.VarKPCuspalPositionsHeading);
                String[][] kpCuspalPosition = objControllerManager.getKpCuspalPositions(CGlobal.getCGlobalObject().getInKPPlanetsAndCuspObject(), context.getResources().getStringArray(R.array.rasi_lord_short_name_list), context.getResources().getStringArray(R.array.nak_lord_short_name_list), context.getResources().getString(R.string.degree_sign), context.getResources().getString(R.string.minute_sign), context.getResources().getString(R.string.second_sign));

                viewKpCuspalPositions = new ViewKpCuspalPositions(context, kpCuspalPosition, KpCuspalPositionsHeading, typeface, SCREEN_CONSTANTS, context.getResources().getStringArray(R.array.kp_planet_note_item_list));
                viewKpCuspalPositions.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                //To make scrollabel chart
                final RelativeLayout layout_cusp = new RelativeLayout(context);
                //final ScrollView scrollView_cusp = new ScrollView(context);
                layout_cusp.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                //scrollView_cusp.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                //End
                layout_cusp.addView(viewKpCuspalPositions);
                //scrollView_cusp.addView(layout_cusp);
                view = layout_cusp;
                break;
            case CGlobalVariables.SUB_MODULE_KP_PLANET_SIG:
                //KP PLANET SIGNIFICATION
                String[] KpPlanetSignificationHeading = context.getResources().getStringArray(R.array.VarKPPlanetSigHeading);
                //String[]  KpPlanetSignificationPlanets = getResources().getStringArray(R.array.VarChartPlanets);
                String[] kpPlanetSig = objControllerManager.getKpPlanetSignificators(CGlobal.getCGlobalObject().getInKpPlanetSignificationObject());

                viewPlanetSignification = new ViewKpPlanetSignification(context, kpPlanetSig, KpPlanetSignificationHeading, context.getResources().getStringArray(R.array.VarChartPlanets), typeface, SCREEN_CONSTANTS, context.getResources().getStringArray(R.array.kp_planet_sig_note_list), languageCode);
                viewPlanetSignification.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth, (int) SCREEN_CONSTANTS.DeviceScreenHeight));

                //To make scrollabel chart
                final RelativeLayout layout_planet_sig = new RelativeLayout(context);
                //final ScrollView scrollView_planet_sig = new ScrollView(context);
                layout_planet_sig.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                //scrollView_planet_sig.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                //End
                layout_planet_sig.addView(viewPlanetSignification);
                //scrollView_planet_sig.addView(layout_planet_sig);
                view = layout_planet_sig;

                break;
            case CGlobalVariables.SUB_MODULE_KP_HOUSE_SIG:
                // KP HOUSE SIGNIFICATION
                String[] KpHouseSignificatorsHeading = context.getResources().getStringArray(R.array.kp_house_sig_heading_list);
                OutKpHouseSignificators outKpHouseSignificators = objControllerManager.getKpHouseSignificators(CGlobal.getCGlobalObject().getInKpPlanetSignificationObject());
                viewKpHouseSignificators = new ViewKpHouseSignificators(context, outKpHouseSignificators, KpHouseSignificatorsHeading, context.getResources().getStringArray(R.array.VarChartPlanets), typeface, SCREEN_CONSTANTS);
                viewKpHouseSignificators.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                view = viewKpHouseSignificators;
                //To make scrollabel chart
                final RelativeLayout layout_house_sig = new RelativeLayout(context);
                //final ScrollView scrollView_house_sig = new ScrollView(context);
                layout_house_sig.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                //scrollView_house_sig.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                //End
                layout_house_sig.addView(viewKpHouseSignificators);
                //scrollView_house_sig.addView(layout_house_sig);
                view = layout_house_sig;


                break;
            case CGlobalVariables.SUB_MODULE_KP_PLANET_SIG_VIEW_2:
                BeanKpPlanetSigWithStrenght[] beanKPPstrength = objControllerManager.getPlanetSignWithStrengthBeansArray(CGlobal.getCGlobalObject().getInKPPlanetsAndCuspObject());

                ViewKpPlanetSigWithStrenght viewKpPlanetSigWithStrenght = new ViewKpPlanetSigWithStrenght(context,
                        beanKPPstrength,
                        context.getResources().getStringArray(R.array.VarChartPlanets),
                        context.getResources().getStringArray(R.array.kp_significator_heading_list),
                        context.getResources().getString(R.string.comma),
                        context.getResources().getString(R.string.plus_sign),
                        isTablet,
                        context.getResources().getStringArray(R.array.kp_significator_notes_list), typeface, SCREEN_CONSTANTS);
                viewKpPlanetSigWithStrenght.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                view = viewKpPlanetSigWithStrenght;

                final RelativeLayout layout_planet_sigv2 = new RelativeLayout(context);
               // final ScrollView scrollView_planet_sigv2 = new ScrollView(context);
                layout_planet_sigv2.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                //scrollView_planet_sigv2.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                //End
                layout_planet_sigv2.addView(viewKpPlanetSigWithStrenght);
                //scrollView_planet_sigv2.addView(layout_planet_sigv2);
                view = layout_planet_sigv2;
                break;
            case CGlobalVariables.SUB_MODULE_KP_NAKSHATRA_NADI:

                // KP MISC NAKSHTRA NADI
                BeanNakshtraNadi[] nakBeans = objControllerManager.getNakshtraNadi(CGlobal.getCGlobalObject().getInKPPlanetsAndCuspObject());
                ViewKPNakshtraNadi viewKPNakshtraNadi = new ViewKPNakshtraNadi(context, nakBeans,
                        context.getResources().getStringArray(R.array.kp_chart_planet_ext_list),
                        context.getResources().getStringArray(R.array.kp_nakshatra_nadi_heading_list),
                        context.getResources().getStringArray(R.array.kp_nakshatra_nadi_notes_list),
                        isTablet,
                        typeface, languageCode, SCREEN_CONSTANTS);
                viewKPNakshtraNadi.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                view = viewKPNakshtraNadi;
                break;
            case CGlobalVariables.SUB_MODULE_KP_KCIL:

                //KP KHULLAR  CUSPAL INTER LINK
                BeanKpKhullarCIL[] beanKpKCIL = objControllerManager.getKCILBeansArray(CGlobal.getCGlobalObject().getInKPPlanetsAndCuspObject());
                ViewKpExtensionKhullarCIL ViewKpExtensionKhullarCIL = new ViewKpExtensionKhullarCIL(context, beanKpKCIL, context.getResources().getStringArray(R.array.kp_planet_full_name_ext_list), context.getResources().getStringArray(R.array.kp_kcil_heading_list),
                        context.getResources().getString(R.string.comma)/*COMMA*/, typeface, SCREEN_CONSTANTS);
                ViewKpExtensionKhullarCIL.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth, LayoutParams.MATCH_PARENT));
                view = ViewKpExtensionKhullarCIL;
                break;
            case CGlobalVariables.SUB_MODULE_KP_4_STEP:

                //KP 4 STEP
                BeanKP4Step[] beanKP4Step = objControllerManager.getBeanKp4StepBeansArrayNew(CGlobal.getCGlobalObject().getInKPPlanetsAndCuspObject());
                ViewKpExtension4Step viewKpExtension4Step = new ViewKpExtension4Step(context, beanKP4Step,
                        context.getResources().getStringArray(R.array.day_lord_full_name_list),
                        context.getResources().getString(R.string.left_bracket)/*LEFT BRACKET*/,
                        context.getResources().getString(R.string.right_bracket)/*RIGHT BRACKET*/,
                        context.getResources().getString(R.string.comma)/*COMMA*/,
                        context.getResources().getString(R.string.colon)/*COLON TITLE*/,
                        context.getResources().getString(R.string.conj)/*COJUNCTION TITLE*/,
                        context.getResources().getString(R.string.asp)/*ASPECT TITLE*/,
                        context.getResources().getString(R.string.cuspal)/*CUSPAL TITLE*/,
                        context.getResources().getString(R.string.plus_sign)/*PLUS SIGN*/,
                        context.getResources().getStringArray(R.array.kp_four_step_notes_list)/*FOUR STEP NOTES ARRAY*/,
                        context.getResources().getString(R.string.four_step_star_lord)/*FOUR STEP STAR LORD TITLE*/,
                        context.getResources().getString(R.string.four_step_sub_lord)/*FOUR STEP SUB LORD TITLE*/,
                        context.getResources().getString(R.string.four_step_star_lord_of_sub_lord)/*FOUR STEP STAR LORD OF  SUB LORD TITLE*/,
                        typeface, SCREEN_CONSTANTS, languageCode);
                viewKpExtension4Step.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth, LayoutParams.MATCH_PARENT));
                view = viewKpExtension4Step;
                break;
            case CGlobalVariables.SUB_MODULE_KP_CIL:
                //KP CUSPAL INTERLINK
                BeanKpCIL[] beanKpCIL = objControllerManager.getKPCILBeansArray(CGlobal.getCGlobalObject().getInKPPlanetsAndCuspObject());
                ViewKpExtensionCIL viewKpExtensionCIL = new ViewKpExtensionCIL(context, beanKpCIL,
                        context.getResources().getStringArray(R.array.kp_cil_heading_list),
                        context.getResources().getString(R.string.comma)/*COMMA*/,
                        context.getResources().getStringArray(R.array.kp_cil_notes_list),
                        isTablet,
                        typeface, SCREEN_CONSTANTS);
                viewKpExtensionCIL.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth, LayoutParams.MATCH_PARENT));
                view = viewKpExtensionCIL;
                break;
            case CGlobalVariables.SUB_MODULE_KP_RULING_PLANET:

                // KP RULING PLANETS
                OutKpRulingPlanets outKpRulingPlanets = objControllerManager
                        .getKpRulingPlanets(CGlobal.getCGlobalObject()
                                        .getInKPPlanetsAndCuspObject(), context.getResources().getStringArray(
                                R.array.rasi_lord_full_name_list),
                                context.getResources().getStringArray(
                                        R.array.nakshatra_lord_full_name_list));
                ViewKpRulingPlanets viewKpRulingPlanets = new ViewKpRulingPlanets(context, outKpRulingPlanets, context.getResources().getStringArray(R.array.kp_ruling_planets_heading_list),
                        CUtils.getFullNameofDayLord(context, outKpRulingPlanets.getBirthDayLord()), typeface, SCREEN_CONSTANTS);
                viewKpRulingPlanets.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                view = viewKpRulingPlanets;
                break;

            case CGlobalVariables.SUB_MODULE_KP_MISC:
                // KP MISC
                OutKpMisc outKpMisc = objControllerManager.getKpMisc(CGlobal.getCGlobalObject().getInKPPlanetsAndCuspObject(), context.getResources().getStringArray(R.array.rasi_lord_full_name_list), context.getResources().getStringArray(R.array.nakshatra_lord_full_name_list));
                ViewKpMisc viewKpMisc = new ViewKpMisc(context, outKpMisc, context.getResources().getStringArray(R.array.kp_misc_list), typeface, context.getResources().getString(R.string.degree_sign), context.getResources().getString(R.string.minute_sign), context.getResources().getString(R.string.second_sign), SCREEN_CONSTANTS);
                viewKpMisc.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                view = viewKpMisc;
                break;
            case CGlobalVariables.SUB_MODULE_BASIC_KPCUSP:
                View predictionsViews = new View(context);
                BeanHoroPersonalInfo horoPersonalInfo = CGlobal.getCGlobalObject().getHoroPersonalInfoObject();
                boolean sessionExist = false;
                String kpCuspURL = CUtils.getPredictionURL(WebViewPredictions.CALCULATE_KP_CUSP, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, languageCode, sessionExist,context);
                predictionsViews.setTag(kpCuspURL);
           //     Log.e("urlCheck", "SUB_MODULE_BASIC_KPCUSP: "+kpCuspURL);
                return predictionsViews;

            case CGlobalVariables.SUB_MODULE_KP_CURRENT_RULING_PLANET:
                View predictionsViews1 = new View(context);

                boolean sessionExist1 = false;
                BeanHoroPersonalInfo horoPersonalInfo1 = CGlobal.getCGlobalObject().getHoroPersonalInfoObject();
                String rulingPlanetURL = CUtils.getMiscURL(WebViewPredictions.MISC_Current_Ruling_Planets, horoPersonalInfo1, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, languageCode, sessionExist1,context);
                predictionsViews1.setTag(rulingPlanetURL);
              //  Log.e("urlCheck", "SUB_MODULE_KP_CURRENT_RULING_PLANET: "+rulingPlanetURL);
                return predictionsViews1;

        }

//		String[] planetNames=null;

//		 String[] rasiLord=null;
//		 String []nakLord =null;
//		 String leftBracket="";
//		 String rightBracket="";
//		 String degSign="";
//		 String minSign="";
//		 String secSign="";

        //INIT VARIABLED VALUES		
//		 rasiLord = context.getResources().getStringArray(R.array.VarRasiLordShort);
//		 nakLord = context.getResources().getStringArray(R.array.VarNakLordShort);

//		 planetNames=context.getResources().getStringArray(R.array.VarChartPlanets);
//		 leftBracket=context.getResources().getString(R.string.left_bracket);
//		 rightBracket=context.getResources().getString(R.string.right_bracket);
//		 degSign=context.getResources().getString(R.string.degree_sign);
//		 minSign=context.getResources().getString(R.string.minute_sign);
//		 secSign=context.getResources().getString(R.string.second_sign);
        //END 


        return view;
    }
    public static int convertDpToPixel(float dp, Context context){
        return (int)(dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }
    public static View getViewOfBasic(int subModuleId, Context context, Typeface typeface, int chartStyle, int languageCode, CScreenConstants SCREEN_CONSTANTS) throws UIKpSystemMiscException {
        View view = null;
        cscreenConstants = SCREEN_CONSTANTS;
        ControllerManager objControllManager = new ControllerManager();
        boolean isTablet = CUtils.isTablet(context);
        final RelativeLayout relativeLayout = new RelativeLayout(context);
        //final ScrollView newScrollView = new ScrollView(context);
        //tejinder for testing
        View predictionsViews = new View(context);
        boolean sessionExist = false;
        BeanHoroPersonalInfo horoPersonalInfo = CGlobal.getCGlobalObject().getHoroPersonalInfoObject();
        //end of work tejinder
        switch (subModuleId) {
            case CGlobalVariables.SUB_MODULE_BASIC_LAGNA_CHART:
                ViewDrawRotateKundli.OnKundliCellClickListener onKundliCellClickListener;
                try {
                    onKundliCellClickListener = (OutputMasterActivity)context;
                }catch (Exception ignore){ onKundliCellClickListener = null;}
                //Log.e("SAN ", " CGAV case == SUB_MODULE_BASIC_LAGNA_CHART ");
                int[] planetsInRashiLagna = new int[13];
                double[] planetDegreeArray = new double[13];
                try {
                    planetsInRashiLagna = objControllManager.getLagnaKundliPlanetsRashiArray(CGlobal.getCGlobalObject().getPlanetDataObject());
                    planetDegreeArray = objControllManager.getPlanetsDegreeArray(CGlobal.getCGlobalObject().getPlanetDataObject());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (chartStyle == 0) {
                    //To make scrollabel chart
                    //Log.e("SAN ", " CGAV chartStyle == 0 ");

                    // SAN Kundli checked from here

                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End

                    ViewDrawRotateKundli resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiLagna, planetDegreeArray, CGlobalVariables.enuKundliType.NORTH, isTablet, SCREEN_CONSTANTS, CGlobalVariables.SUB_MODULE_BASIC_LAGNA_CHART, languageCode,true);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, ((int) SCREEN_CONSTANTS.DeviceScreenHeight) + 150)/*-convertDpToPixel(550,context))*/);
                    resultView.setKundliCellClickListener(onKundliCellClickListener);
                    layout.addView(resultView);

                    // SAN exp start

                    final RelativeLayout layoutRelativeMain = new RelativeLayout(context);
                    RelativeLayout.LayoutParams lLParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lLParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    lLParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    float h = SCREEN_CONSTANTS.DeviceScreenWidth;
                    float h1 = (SCREEN_CONSTANTS.VKUNDLISpace + h) / 12;
                    lLParams.setMargins(0, (int) ( SCREEN_CONSTANTS.DeviceScreenWidth + ( h1 * 2.5 )  ), 0,0);
                    //lLParams.setMargins(0, (int) ( SCREEN_CONSTANTS.KUNDLI_BOTTOM_LAST_Y + SCREEN_CONSTANTS.KUNDLI_BOTTOM_H1 ), 0,0);
                    layoutRelativeMain.setLayoutParams(lLParams);

                    LinearLayout layoutLinearMain = generateNineBoxView(context);

                    layoutRelativeMain.addView(layoutLinearMain);

                    layout.addView(layoutRelativeMain, lLParams);
                    // SAN exp end

                    scrollView.addView(layout);

                    view = scrollView;
                } else if (chartStyle == 1) {
                    //To make scrollabel chart
                    //Log.e("SAN ", " CGAV chartStyle == 1 ");
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End

                    ViewDrawRotateKundli resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiLagna, planetDegreeArray, CGlobalVariables.enuKundliType.SOUTH, isTablet, SCREEN_CONSTANTS, CGlobalVariables.SUB_MODULE_BASIC_LAGNA_CHART, languageCode,true);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, ((int) SCREEN_CONSTANTS.DeviceScreenHeight) + 150));
                    resultView.setKundliCellClickListener(onKundliCellClickListener);
                    layout.addView(resultView);

                    // SAN exp start

                    final RelativeLayout layoutRelativeMain = new RelativeLayout(context);
                    RelativeLayout.LayoutParams lLParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lLParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    lLParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    float h = SCREEN_CONSTANTS.DeviceScreenWidth;
                    float h1 = (SCREEN_CONSTANTS.VKUNDLISpace + h) / 12;
                    lLParams.setMargins(0, (int) ( SCREEN_CONSTANTS.DeviceScreenWidth + ( h1 * 2.5 )  ), 0,0);
                    //lLParams.setMargins(0, (int) ( SCREEN_CONSTANTS.KUNDLI_BOTTOM_LAST_Y + SCREEN_CONSTANTS.KUNDLI_BOTTOM_H1 ), 0,0);
                    layoutRelativeMain.setLayoutParams(lLParams);

                    LinearLayout layoutLinearMain = generateNineBoxView(context);

                    layoutRelativeMain.addView(layoutLinearMain);

                    layout.addView(layoutRelativeMain, lLParams);
                    // SAN exp end

                    scrollView.addView(layout);
                    view = scrollView;
                } else if (chartStyle == 2) {
                    //To make scrollabel chart
                    //Log.e("SAN ", " CGAV chartStyle == 2 ");
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    ViewDrawRotateKundli resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiLagna, planetDegreeArray, CGlobalVariables.enuKundliType.EAST, isTablet, SCREEN_CONSTANTS, CGlobalVariables.SUB_MODULE_BASIC_LAGNA_CHART, languageCode,true);
                    resultView.setKundliCellClickListener(onKundliCellClickListener);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, ((int) SCREEN_CONSTANTS.DeviceScreenHeight) + 150));
                    layout.addView(resultView);

                    // SAN exp start

                    final RelativeLayout layoutRelativeMain = new RelativeLayout(context);
                    RelativeLayout.LayoutParams lLParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lLParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    lLParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    float h = SCREEN_CONSTANTS.DeviceScreenWidth;
                    float h1 = (SCREEN_CONSTANTS.VKUNDLISpace + h) / 12;
                    lLParams.setMargins(0, (int) ( SCREEN_CONSTANTS.DeviceScreenWidth + ( h1 * 2.5 )  ), 0,0);
                    //lLParams.setMargins(0, (int) ( SCREEN_CONSTANTS.KUNDLI_BOTTOM_LAST_Y + SCREEN_CONSTANTS.KUNDLI_BOTTOM_H1 ), 0,0);
                    layoutRelativeMain.setLayoutParams(lLParams);

                    LinearLayout layoutLinearMain = generateNineBoxView(context);

                    layoutRelativeMain.addView(layoutLinearMain);

                    layout.addView(layoutRelativeMain, lLParams);
                    // SAN exp end

                    scrollView.addView(layout);
                    view = scrollView;
                }
                break;


            case CGlobalVariables.SUB_MODULE_BASIC_NAVAMSHA_CHART:

                //Log.e("SAN ", " CGAV case == SUB_MODULE_BASIC_NAVAMSHA_CHART ");

                int[] planetsInRashiNavmansha = new int[13];
                try {
                    planetsInRashiNavmansha = objControllManager.getPlanetsInRashiFromShodashvarga(CGlobal.getCGlobalObject().getOutShodashvargaObject().get_navamsha());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (chartStyle == 0) {
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End

                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiNavmansha, CGlobalVariables.enuKundliType.NORTH, isTablet, SCREEN_CONSTANTS, -1, languageCode);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    view = scrollView;
                } else if (chartStyle == 1) {
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiNavmansha, CGlobalVariables.enuKundliType.SOUTH, isTablet, SCREEN_CONSTANTS, -1, languageCode);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    view = scrollView;
                } else if (chartStyle == 2) {

                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiNavmansha, CGlobalVariables.enuKundliType.EAST, isTablet, SCREEN_CONSTANTS, -1, languageCode);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    view = scrollView;
                }
                break;
            case CGlobalVariables.SUB_MODULE_BASIC_MOON_CHART:

                //Log.e("SAN ", " CGAV case == SUB_MODULE_BASIC_MOON_CHART ");

                int[] planetsInRashiChandra = new int[13];
                try {
                    planetsInRashiChandra = objControllManager.getChandraKundliPlanetsRashiArray(CGlobal.getCGlobalObject().getPlanetDataObject());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (chartStyle == 0) {
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiChandra, CGlobalVariables.enuKundliType.NORTH, isTablet, SCREEN_CONSTANTS, -1, languageCode);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    view = scrollView;
                } else if (chartStyle == 1) {
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiChandra, CGlobalVariables.enuKundliType.SOUTH, isTablet, SCREEN_CONSTANTS, -1, languageCode);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    view = scrollView;
                } else if (chartStyle == 2) {
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiChandra, CGlobalVariables.enuKundliType.EAST, isTablet, SCREEN_CONSTANTS, -1, languageCode);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    view = scrollView;
                }
                break;
            case CGlobalVariables.SUB_MODULE_BASIC_CHALIT_CHART:

                //Log.e("SAN ", " CGAV case == SUB_MODULE_BASIC_CHALIT_CHART ");

                double[] bhavaDegreeArray = new double[12];
                double[] bhavaMidDegreeArray = new double[12];
                double[] planetDegreeArray2 = new double[13];

                try {
                    bhavaDegreeArray = objControllManager.getCuspsDegreeArray(CGlobal.getCGlobalObject().getInKPPlanetsAndCuspObject(), CGlobal.getCGlobalObject().getPlanetDataObject());
                    bhavaMidDegreeArray = objControllManager.getCuspsMidDegreeArrayForChalit(CGlobal.getCGlobalObject().getInKPPlanetsAndCuspObject(), CGlobal.getCGlobalObject().getPlanetDataObject());
                    planetDegreeArray2 = objControllManager.getPlanetsDegreeArray(CGlobal.getCGlobalObject().getPlanetDataObject());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (chartStyle == 0) {
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateChalitKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetDegreeArray2, bhavaDegreeArray, bhavaMidDegreeArray, CGlobalVariables.enuKundliType.NORTH, context.getResources().getStringArray(R.array.VarChartPlanets)[12], isTablet, SCREEN_CONSTANTS);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    view = scrollView;
                } else if (chartStyle == 1) {
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateChalitKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetDegreeArray2, bhavaDegreeArray, bhavaMidDegreeArray, CGlobalVariables.enuKundliType.SOUTH, context.getResources().getStringArray(R.array.VarChartPlanets)[12], isTablet, SCREEN_CONSTANTS);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    view = scrollView;
                } else if (chartStyle == 2) {
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateChalitKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetDegreeArray2, bhavaDegreeArray, bhavaMidDegreeArray, CGlobalVariables.enuKundliType.EAST, context.getResources().getStringArray(R.array.VarChartPlanets)[12], isTablet, SCREEN_CONSTANTS);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    view = scrollView;

                }
                break;
            case CGlobalVariables.SUB_MODULE_BASIC_PLANETS:

                //Log.e("SAN ", " CGAV case == SUB_MODULE_BASIC_PLANETS ");

                KundliPlanetArray kundliPlanetArray_obj = null;
                try {
                    kundliPlanetArray_obj = objControllManager.getLagnaKundli(CGlobal.getCGlobalObject().getPlanetDataObject());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                viewMiscDrawables = new viewMiscDrawable(context, kundliPlanetArray_obj, 0, SCREEN_CONSTANTS, CUtils.getDirectArray(context), CUtils.getCombustArray(context), languageCode, context.getResources().getStringArray(R.array.planet_and_lagna_name_list), context.getResources().getStringArray(R.array.rasi_short_name_list), context.getResources().getStringArray(
                        R.array.nakshatra_short_name_list), context.getResources().getStringArray(R.array.nak_lord_short_name_list), context.getResources().getStringArray(R.array.rasi_lord_short_name_list), context.getResources().getString(R.string.degree_sign), context.getResources().getString(R.string.minute_sign), context.getResources().getString(R.string.second_sign), context.getResources().getStringArray(R.array.planet_position_normal_heading_list), context.getResources().getStringArray(R.array.kp_planetary_positions_sub_heading_list), context.getResources().getStringArray(R.array.kp_planet_note_item_list));
                ///testing
                viewMiscDrawables.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                //To make scrollabel chart
                relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
               // newScrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                //End
                relativeLayout.addView(viewMiscDrawables);
                //newScrollView.addView(relativeLayout);
                view = relativeLayout;
                //end

                break;
            case CGlobalVariables.SUB_MODULE_BASIC_PLANETS_SUB:

                //Log.e("SAN ", " CGAV case == SUB_MODULE_BASIC_PLANETS_SUB ");

                KundliPlanetArray kundliPlanetArray_obj2 = null;
                try {
                    kundliPlanetArray_obj2 = objControllManager.getLagnaKundli(CGlobal.getCGlobalObject().getPlanetDataObject());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                planetSubView1 = new viewMiscDrawable(context, kundliPlanetArray_obj2, 1, SCREEN_CONSTANTS, CUtils.getDirectArray(context), CUtils.getCombustArray(context), languageCode, context.getResources().getStringArray(R.array.planet_and_lagna_name_list), context.getResources().getStringArray(R.array.rasi_short_name_list), context.getResources().getStringArray(
                        R.array.nakshatra_short_name_list), context.getResources().getStringArray(R.array.nak_lord_short_name_list), context.getResources().getStringArray(R.array.rasi_lord_short_name_list), context.getResources().getString(R.string.degree_sign), context.getResources().getString(R.string.minute_sign), context.getResources().getString(R.string.second_sign), context.getResources().getStringArray(R.array.planet_position_normal_heading_list), context.getResources().getStringArray(R.array.kp_planetary_positions_sub_heading_list), context.getResources().getStringArray(R.array.kp_planet_note_item_list));
                planetSubView1.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                //To make scrollabel chart
                relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                //newScrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                //End
                relativeLayout.addView(planetSubView1);
                //newScrollView.addView(relativeLayout);
                view = relativeLayout;
                break;
            case CGlobalVariables.SUB_MODULE_BASIC_CHALIT_TABLE:

                //Log.e("SAN ", " CGAV case == SUB_MODULE_BASIC_CHALIT_TABLE ");

                double[] bhavaDegreeArray2 = new double[12];
                double[] bhavaMidDegreeArray2 = new double[12];

                try {
                    bhavaDegreeArray2 = objControllManager.getCuspsDegreeArray(CGlobal.getCGlobalObject().getInKPPlanetsAndCuspObject(), CGlobal.getCGlobalObject().getPlanetDataObject());
                    bhavaMidDegreeArray2 = objControllManager.getCuspsMidDegreeArrayForChalit(CGlobal.getCGlobalObject().getInKPPlanetsAndCuspObject(), CGlobal.getCGlobalObject().getPlanetDataObject());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                viewChalitKundli = new ViewDrawBhavaBeginAndEnd(context, bhavaDegreeArray2, bhavaMidDegreeArray2, SCREEN_CONSTANTS, context.getResources().getStringArray(R.array.rasi_short_name_list), context.getResources().getStringArray(R.array.chalit_bhava_table_heading_list), languageCode);

                viewChalitKundli.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                //To make scrollabel chart
                relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                //newScrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                //End
                relativeLayout.addView(viewChalitKundli);
                //newScrollView.addView(relativeLayout);
                view = relativeLayout;


                break;
            case CGlobalVariables.SUB_MODULE_BASIC_BIRTH_DETAILS:

                //Log.e("SAN ", " CGAV case == SUB_MODULE_BASIC_BIRTH_DETAILS ");

                OutBirthDetail outBirthDetail = null;
                try {

                    outBirthDetail = objControllManager.getBirthDetail(CGlobal
                            .getCGlobalObject().getHoroPersonalInfoObject());
                    //Log.e("output from:: ", "" + outBirthDetail.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                birthDetailView = new ViewBirthDetails(context, outBirthDetail, context
                        .getResources().getStringArray(
                                R.array.birth_detail_heading_list), languageCode,
                        context.getResources().getString(R.string.degree_sign),
                        context.getResources().getString(R.string.minute_sign),
                        context.getResources().getString(R.string.second_sign),
                        SCREEN_CONSTANTS, context.getResources().getStringArray(
                        R.array.year_month_day_short_list), context
                        .getResources().getStringArray(
                                R.array.mangal_dosh_list), context
                        .getResources().getStringArray(
                                R.array.rasi_full_name_list), context
                        .getResources().getStringArray(
                                R.array.Vimshottari_dasa_planets_list),
                        context.getResources().getStringArray(R.array.ayan_list),
                        context.getResources().getString(
                                R.string.birth_detail_top_heading));
                birthDetailView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                //To make scrollabel chart
                relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                //newScrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                //End
                relativeLayout.addView(birthDetailView);
                //newScrollView.addView(relativeLayout);
                view = relativeLayout;

                break;
        /*case CGlobalVariables.SUB_MODULE_BASIC_PANCHANG:
            String pakshaName = "";
			if(languageCode==CGlobalVariables.ENGLISH) {
				pakshaName = CUtils.makeFirstLatterInCapitalInString(CGlobal.getCGlobalObject().getOutPanchangObject().getPaksha(),languageCode);
					
			}else if(languageCode==CGlobalVariables.HINDI || languageCode == CGlobalVariables.TAMIL) {
				pakshaName = CUtils.getPakshaNameInHindi(context,CGlobal.getCGlobalObject().getOutPanchangObject().getPaksha());
			}
			String tithiName = "";
			tithiName=CGlobal.getCGlobalObject().getOutPanchangObject().getTitAtBirth();
			if(languageCode==CGlobalVariables.ENGLISH) {
				tithiName=tithiName.toLowerCase();
				// **************    below if block is  added by avinash because from web service PRATIPADA is coming "PRATIPAD" So a is added in this string
				if(tithiName.equalsIgnoreCase("pratipad"))
				{
					tithiName = tithiName + "a";
				}
				// ************************ up to here added by avinash    
				tithiName=CUtils.makeFirstLatterInCapitalInString(tithiName,languageCode);				
							
			} else if(languageCode==CGlobalVariables.HINDI || languageCode == CGlobalVariables.TAMIL) {
				tithiName = CUtils.getTithiNameInHindi(context,tithiName);
			}
			CGlobal.getCGlobalObject().getOutPanchangObject().setNakshatraIndex( CUtils.getNakshatraNumber(CGlobal.getCGlobalObject().getPlanetDataObject().getMoon()));
			CGlobal.getCGlobalObject().getOutPanchangObject().setNakshatraCharan( CUtils.getPlntCharan(CGlobal.getCGlobalObject().getPlanetDataObject().getMoon()));
			String nakChanran = "";
			nakChanran=" "+context.getResources().getString(R.string.open_bracket)+ String.valueOf(CGlobal.getCGlobalObject().getOutPanchangObject().getNakshatraCharan())+
				 context.getResources().getString(R.string.close_bracket);
			
			String yogName = "";
			if(languageCode==CGlobalVariables.ENGLISH)
				yogName = CUtils.makeFirstLatterInCapitalInString(CGlobal.getCGlobalObject().getOutPanchangObject().getYoga(),languageCode);
			else if(languageCode==CGlobalVariables.HINDI)
				yogName =  CUtils.getYogaNameInHindi(context,CGlobal.getCGlobalObject().getOutPanchangObject().getYoga());
			String karanName = "";
			if(languageCode==CGlobalVariables.ENGLISH)
				karanName = CUtils.makeFirstLatterInCapitalInString(CGlobal.getCGlobalObject().getOutPanchangObject().getKaran(),languageCode);
			else if(languageCode==CGlobalVariables.HINDI)
				karanName = CUtils.getKaranNameInHindi(context,CGlobal.getCGlobalObject().getOutPanchangObject().getKaran());
			view = new ViewPanchang(context, CGlobal.getCGlobalObject().getOutPanchangObject(),
					context.getResources().getStringArray(R.array.panchang_heading_list),context.getResources().getStringArray(R.array.week_day_sunday_to_saturday_list),
					context.getResources().getStringArray(R.array.week_day_monday_to_sunday_list),languageCode,SCREEN_CONSTANTS,context.getResources().getStringArray(R.array.nakshatra_short_name_list),context.getResources().getString(R.string.panchang_top_heading),pakshaName,tithiName,nakChanran,yogName,karanName);
			break;*/

            case CGlobalVariables.SUB_MODULE_BASIC_PANCHANG:

                //Log.e("SAN ", " CGAV case == SUB_MODULE_BASIC_PANCHANG ");

                String pakshaName = "";
                if (languageCode == CGlobalVariables.ENGLISH) {
                    pakshaName = CUtils.makeFirstLatterInCapitalInString(CGlobal
                                    .getCGlobalObject().getOutPanchangObject().getPaksha(),
                            languageCode);

                } else if (languageCode == CGlobalVariables.HINDI
                        || languageCode == CGlobalVariables.TAMIL || languageCode == CGlobalVariables.BANGALI || languageCode == CGlobalVariables.MARATHI || languageCode == CGlobalVariables.KANNADA || languageCode == CGlobalVariables.TELUGU || languageCode == CGlobalVariables.GUJARATI || languageCode == CGlobalVariables.MALAYALAM) {
                    pakshaName = CUtils.getPakshaNameInHindi(context, CGlobal
                            .getCGlobalObject().getOutPanchangObject().getPaksha());
                }
                String tithiName = "";
                tithiName = CGlobal.getCGlobalObject().getOutPanchangObject()
                        .getTitAtBirth();
                if (languageCode == CGlobalVariables.ENGLISH) {
                    tithiName = tithiName.toLowerCase();
                    // ************** below if block is added by avinash because
                    // from web service PRATIPADA is coming "PRATIPAD" So a is added
                    // in this string
                    if (tithiName.equalsIgnoreCase("pratipad")) {
                        tithiName = tithiName + "a";
                    }
                    // ************************ up to here added by avinash
                    tithiName = CUtils.makeFirstLatterInCapitalInString(tithiName,
                            languageCode);

                } else if (languageCode == CGlobalVariables.HINDI
                        || languageCode == CGlobalVariables.TAMIL || languageCode == CGlobalVariables.BANGALI || languageCode == CGlobalVariables.MARATHI || languageCode == CGlobalVariables.KANNADA || languageCode == CGlobalVariables.TELUGU || languageCode == CGlobalVariables.GUJARATI || languageCode == CGlobalVariables.MALAYALAM) {
                    tithiName = CUtils.getTithiNameInHindi(context, tithiName);
                }
                CGlobal.getCGlobalObject()
                        .getOutPanchangObject()
                        .setNakshatraIndex(
                                CUtils.getNakshatraNumber(CGlobal
                                        .getCGlobalObject().getPlanetDataObject()
                                        .getMoon()));
                CGlobal.getCGlobalObject()
                        .getOutPanchangObject()
                        .setNakshatraCharan(
                                CUtils.getPlntCharan(CGlobal.getCGlobalObject()
                                        .getPlanetDataObject().getMoon()));
                String nakChanran = "";
                nakChanran = " "
                        + context.getResources().getString(R.string.open_bracket)
                        + String.valueOf(CGlobal.getCGlobalObject()
                        .getOutPanchangObject().getNakshatraCharan())
                        + context.getResources().getString(R.string.close_bracket);
        /*	String dayName = "";
            if (languageCode == CGlobalVariables.ENGLISH)
				dayName = CUtils.makeFirstLatterInCapitalInString(CGlobal
						.getCGlobalObject().getOutPanchangObject().getDay(),
						languageCode);
			else if (languageCode == CGlobalVariables.HINDI
					|| languageCode == CGlobalVariables.TAMIL)
				dayName = CUtils.getYogaNameInHindi(context, CGlobal
						.getCGlobalObject().getOutPanchangObject().getDay());*/
                String yogName = "";
                if (languageCode == CGlobalVariables.ENGLISH)
                    yogName = CUtils.makeFirstLatterInCapitalInString(CGlobal
                                    .getCGlobalObject().getOutPanchangObject().getYoga(),
                            languageCode);
                else if (languageCode == CGlobalVariables.HINDI
                        || languageCode == CGlobalVariables.TAMIL || languageCode == CGlobalVariables.BANGALI || languageCode == CGlobalVariables.MARATHI || languageCode == CGlobalVariables.KANNADA || languageCode == CGlobalVariables.TELUGU || languageCode == CGlobalVariables.GUJARATI || languageCode == CGlobalVariables.MALAYALAM)
                    yogName = CUtils.getYogaNameInHindi(context, CGlobal
                            .getCGlobalObject().getOutPanchangObject().getYoga());
                String karanName = "";
                if (languageCode == CGlobalVariables.ENGLISH)
                    karanName = CUtils.makeFirstLatterInCapitalInString(CGlobal
                                    .getCGlobalObject().getOutPanchangObject().getKaran(),
                            languageCode);
                else if (languageCode == CGlobalVariables.HINDI
                        || languageCode == CGlobalVariables.TAMIL || languageCode == CGlobalVariables.BANGALI || languageCode == CGlobalVariables.MARATHI || languageCode == CGlobalVariables.KANNADA || languageCode == CGlobalVariables.TELUGU || languageCode == CGlobalVariables.GUJARATI || languageCode == CGlobalVariables.MALAYALAM)
                    karanName = CUtils.getKaranNameInHindi(context, CGlobal
                            .getCGlobalObject().getOutPanchangObject().getKaran());
                panchangView = new ViewPanchang(context, CGlobal.getCGlobalObject()
                        .getOutPanchangObject(), context.getResources()
                        .getStringArray(R.array.panchang_heading_list), context
                        .getResources().getStringArray(
                                R.array.week_day_sunday_to_saturday_list), context
                        .getResources().getStringArray(
                                R.array.week_day_monday_to_sunday_list),
                        languageCode, SCREEN_CONSTANTS, context.getResources()
                        .getStringArray(R.array.nakshatra_short_name_list),
                        context.getResources().getString(
                                R.string.panchang_top_heading), pakshaName,
                        tithiName, nakChanran, yogName, karanName);
                panchangView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                //To make scrollabel chart
                relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                //newScrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                //End
                relativeLayout.addView(panchangView);
                //newScrollView.addView(relativeLayout);
                view = relativeLayout;

                break;
            case CGlobalVariables.SUB_MODULE_BASIC_ASHTAKVARGA:

                //Log.e("SAN ", " CGAV case == SUB_MODULE_BASIC_ASHTAKVARGA ");

                astakvargaView = new ViewAstakvargaTable(context, SCREEN_CONSTANTS, context.getResources().getStringArray(R.array.astakvarga_table_heading_list));
                astakvargaView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                //To make scrollabel chart
                relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                //newScrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                //End
                relativeLayout.addView(astakvargaView);
               // newScrollView.addView(relativeLayout);
                view = relativeLayout;

                break;
            //tejinder


            case CGlobalVariables.SUB_MODULE_BASIC_JAIMINI_KARAKAMSA:

                //Log.e("SAN ", " CGAV case == SUB_MODULE_BASIC_JAIMINI_KARAKAMSA ");

                int[] planetsInRashiKarakamsa = new int[13];
                double[] planetDegreeArrayKarakamsa = new double[13];
                int[] planetsInRashiNavmanshaCalculateAtmKark = new int[13];
                try {
                    planetsInRashiKarakamsa = objControllManager.getLagnaKundliPlanetsRashiArray(CGlobal.getCGlobalObject().getPlanetDataObject());
                    planetDegreeArrayKarakamsa = objControllManager.getPlanetsDegreeArray(CGlobal.getCGlobalObject().getPlanetDataObject());
                    planetsInRashiNavmanshaCalculateAtmKark = objControllManager.getPlanetsInRashiFromShodashvarga(CGlobal.getCGlobalObject().getOutShodashvargaObject().get_navamsha());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (chartStyle == 0) {
                    //To make scrollabel chart
                    final RelativeLayout layoutSwamsa = new RelativeLayout(context);
                    final ScrollView scrollViewSwamsa = new ScrollView(context);
                    layoutSwamsa.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollViewSwamsa.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiKarakamsa, planetDegreeArrayKarakamsa, CGlobalVariables.enuKundliType.NORTH, isTablet, SCREEN_CONSTANTS, -1, languageCode, "KARAKAMSA", planetsInRashiNavmanshaCalculateAtmKark);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layoutSwamsa.addView(resultView);
                    scrollViewSwamsa.addView(layoutSwamsa);
                    view = scrollViewSwamsa;
                } else if (chartStyle == 1) {
                    //To make scrollabel chart
                    final RelativeLayout layoutSwamsa = new RelativeLayout(context);
                    final ScrollView scrollViewSwamsa = new ScrollView(context);
                    layoutSwamsa.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollViewSwamsa.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiKarakamsa, planetDegreeArrayKarakamsa, CGlobalVariables.enuKundliType.SOUTH, isTablet, SCREEN_CONSTANTS, -1, languageCode, "KARAKAMSA", planetsInRashiNavmanshaCalculateAtmKark);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layoutSwamsa.addView(resultView);
                    scrollViewSwamsa.addView(layoutSwamsa);
                    view = scrollViewSwamsa;
                } else if (chartStyle == 2) {
                    //To make scrollabel chart
                    final RelativeLayout layoutSwamsa = new RelativeLayout(context);
                    final ScrollView scrollViewSwamsa = new ScrollView(context);
                    layoutSwamsa.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollViewSwamsa.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiKarakamsa, planetDegreeArrayKarakamsa, CGlobalVariables.enuKundliType.EAST, isTablet, SCREEN_CONSTANTS, -1, languageCode, "KARAKAMSA", planetsInRashiNavmanshaCalculateAtmKark);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layoutSwamsa.addView(resultView);
                    scrollViewSwamsa.addView(layoutSwamsa);
                    view = scrollViewSwamsa;

                }
                break;


            case CGlobalVariables.SUB_MODULE_BASIC_JAIMINI_SWAMSA:
                // Transit
                //Log.e("SAN ", " CGAV case == SUB_MODULE_BASIC_JAIMINI_SWAMSA ");

                int[] planetsInRashiNavmanshaSwamsa = new int[13];
                double[] planetDegreeArraySwamsa = new double[13];
                try {
                    planetsInRashiNavmanshaSwamsa = objControllManager.getPlanetsInRashiFromShodashvarga(CGlobal.getCGlobalObject().getOutShodashvargaObject().get_navamsha());
                    planetDegreeArraySwamsa = objControllManager.getPlanetsDegreeArray(CGlobal.getCGlobalObject().getPlanetDataObject());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (chartStyle == 0) {
                    //To make scrollabel chart
                    final RelativeLayout layoutSwamsa = new RelativeLayout(context);
                    final ScrollView scrollViewSwamsa = new ScrollView(context);
                    layoutSwamsa.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollViewSwamsa.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End

                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiNavmanshaSwamsa, CGlobalVariables.enuKundliType.NORTH, isTablet, SCREEN_CONSTANTS, -1, languageCode, "SWAMSA", planetDegreeArraySwamsa);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layoutSwamsa.addView(resultView);
                    scrollViewSwamsa.addView(layoutSwamsa);
                    view = scrollViewSwamsa;
                } else if (chartStyle == 1) {
                    //To make scrollabel chart
                    final RelativeLayout layoutSwamsa = new RelativeLayout(context);
                    final ScrollView scrollViewSwamsa = new ScrollView(context);
                    layoutSwamsa.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollViewSwamsa.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiNavmanshaSwamsa, CGlobalVariables.enuKundliType.SOUTH, isTablet, SCREEN_CONSTANTS, -1, languageCode, "SWAMSA", planetDegreeArraySwamsa);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layoutSwamsa.addView(resultView);
                    scrollViewSwamsa.addView(layoutSwamsa);
                    view = scrollViewSwamsa;
                } else if (chartStyle == 2) {
                    //To make scrollabel chart
                    final RelativeLayout layoutSwamsa = new RelativeLayout(context);
                    final ScrollView scrollViewSwamsa = new ScrollView(context);
                    layoutSwamsa.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollViewSwamsa.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiNavmanshaSwamsa, CGlobalVariables.enuKundliType.EAST, isTablet, SCREEN_CONSTANTS, -1, languageCode, "SWAMSA", planetDegreeArraySwamsa);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layoutSwamsa.addView(resultView);
                    scrollViewSwamsa.addView(layoutSwamsa);
                    view = scrollViewSwamsa;
                }
                break;


            case CGlobalVariables.SUB_MODULE_BASIC_SHADBALA:

                //Log.e("SAN ", " CGAV case == SUB_MODULE_BASIC_SHADBALA ");

                String shadBalaUrl = CUtils.getPredictionURL(WebViewPredictions.CALCULATE_SHADBALA, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, languageCode, sessionExist,context);
                predictionsViews.setTag(shadBalaUrl);
           //     Log.e("urlCheck", "SUB_MODULE_BASIC_SHADBALA: "+shadBalaUrl );
                return predictionsViews;

            case CGlobalVariables.SUB_MODULE_BASIC_PRASTHARAHTAKBARGA:

                //Log.e("SAN ", " CGAV case == SUB_MODULE_BASIC_PRASTHARAHTAKBARGA ");

                String prastharahtakbargaUrl = CUtils.getPredictionURL(WebViewPredictions.CALCULATE_PRASTHARASHTAKVARGA, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, languageCode, sessionExist,context);
                predictionsViews.setTag(prastharahtakbargaUrl);
           //     Log.e("urlCheck", "SUB_MODULE_BASIC_PRASTHARAHTAKBARGA: "+prastharahtakbargaUrl );
                return predictionsViews;

            case CGlobalVariables.SUB_MODULE_BASIC_BHAV_MADHYA:

                //Log.e("SAN ", " CGAV case == SUB_MODULE_BASIC_BHAV_MADHYA ");

                String bhavMadhyaUrl = CUtils.getPredictionURL(WebViewPredictions.CALCULATE_BHAV_MADHYA, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, languageCode, sessionExist,context);
                predictionsViews.setTag(bhavMadhyaUrl);
                Log.e("urlCheck", "SUB_MODULE_BASIC_BHAV_MADHYA: "+bhavMadhyaUrl );
                return predictionsViews;

            case CGlobalVariables.SUB_MODULE_BASIC_FRIENDSHIP:

                //Log.e("SAN ", " CGAV case == SUB_MODULE_BASIC_FRIENDSHIP ");

                String friendShipUrl = CUtils.getPredictionURL(WebViewPredictions.CALCULATE_FRIENDSHIP, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, languageCode, sessionExist,context);
                predictionsViews.setTag(friendShipUrl);
             //   Log.e("urlCheck", "SUB_MODULE_BASIC_FRIENDSHIP: "+friendShipUrl );
                return predictionsViews;
            case CGlobalVariables.SUB_MODULE_BASIC_PERSONAL_DETAIL:

                //Log.e("SAN ", " CGAV case == SUB_MODULE_BASIC_PERSONAL_DETAIL ");

                String personaLDetailUrl = CUtils.getPredictionURL(WebViewPredictions.CALCULATE_PERSONAL_DETAIL, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, languageCode, sessionExist,context);
                predictionsViews.setTag(personaLDetailUrl);
           //     Log.e("urlCheck", "SUB_MODULE_BASIC_PERSONAL_DETAIL: "+personaLDetailUrl );
                return predictionsViews;
            case CGlobalVariables.SUB_MODULE_BASIC_AVAKAHADACHAKRA:

                //Log.e("SAN ", " CGAV case == SUB_MODULE_BASIC_AVAKAHADACHAKRA ");

                String AvakahadachakraUrl = CUtils.getPredictionURL(WebViewPredictions.CALCULATE_AVAKAHADA_CHAKRA, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, languageCode, sessionExist,context);
                predictionsViews.setTag(AvakahadachakraUrl);
         //       Log.e("urlCheck", "SUB_MODULE_BASIC_AVAKAHADACHAKRA: "+AvakahadachakraUrl );
                return predictionsViews;
            case CGlobalVariables.SUB_MODULE_BASIC_GATHAK_FAVOURABLE_POINTS:

                //Log.e("SAN ", " CGAV case == SUB_MODULE_BASIC_GATHAK_FAVOURABLE_POINTS ");

                String gathakFavourableUrl = CUtils.getPredictionURL(WebViewPredictions.CALCULATE_GATHAK_FAVOURABLE_POINTS, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, languageCode, sessionExist,context);
                predictionsViews.setTag(gathakFavourableUrl);
         //       Log.e("urlCheck", "SUB_MODULE_BASIC_GATHAK_FAVOURABLE_POINTS: "+gathakFavourableUrl );
                return predictionsViews;
        /*case CGlobalVariables.SUB_MODULE_BASIC_JAIMINI_KARAKAMSA:
            predictionsViews.setTag(CUtils.getPredictionURL(WebViewPredictions.CALCULATE_JAMINISYSTEMKARAKAMSASWAMSA,horoPersonalInfo,SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE,languageCode, sessionExist));
			  return predictionsViews; */


            //tejinder end work

        }
        return view;
    }

    public static LinearLayout generateNineBoxView( Context context ) {

        final LinearLayout layoutLinearMain = new LinearLayout(context);
        layoutLinearMain.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layoutLinearMain.setOrientation(LinearLayout.VERTICAL);
        layoutLinearMain.setPadding(10,10,10,10);
        layoutLinearMain.setWeightSum(1.0f);
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        final LinearLayout layoutLinearI = new LinearLayout(context);
        layoutLinearI.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layoutLinearI.setOrientation(LinearLayout.HORIZONTAL);
        layoutLinearI.setPadding(0,10,0,10);
        layoutLinearI.setWeightSum(1.0f);

        TextView textView1 = new TextView(context);
        TextView textView2 = new TextView(context);
        TextView textView3 = new TextView(context);

        textView1.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT, 0.33f));
        textView1.setText(context.getResources().getString(R.string.hora_planet_name));
        textView1.setPadding(10,20,10,20);
        textView1.setBackground( context.getResources().getDrawable(R.drawable.bg_border_kundli_button) );
        textView1.setGravity(Gravity.CENTER);
        textView1.setTextColor(context.getResources().getColor(R.color.black));
        textView1.setForeground(AppCompatResources.getDrawable(context,outValue.resourceId));
        FontUtils.changeFont(context, textView1, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);


        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("SAN ", " currentActivity " + currentActivity.getLocalClassName().toString() );
                try {
                    getAppViewFragment().callMethodTextView1(context);
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_KUNDALI_BOTTOM_BUTTON_PLANET, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    //((OutputMasterActivity) currentActivity).moduleNavigate(CGlobalVariables.SUB_MODULE_BASIC_PANCHANG);
                }  catch (Exception e){
                    //Log.e("SAN ", " textView1 click exp " + e.toString());
                }
            }
        });

        textView2.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT, 0.33f));
        textView2.setText( context.getResources().getString(R.string.Dasha) );
        textView2.setPadding(10,20,10,20);
        textView2.setBackground( context.getResources().getDrawable(R.drawable.bg_border_kundli_button));
        textView2.setGravity(Gravity.CENTER);
        textView2.setTextColor(context.getResources().getColor(R.color.black));
        setMargin(textView2, 10, 0, 0, 0);
        textView2.setForeground(AppCompatResources.getDrawable(context,outValue.resourceId));
        FontUtils.changeFont(context, textView2, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getAppViewFragment().callMethodTextView2(context);
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_KUNDALI_BOTTOM_BUTTON_DASHA, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                }  catch (Exception e){
                    //Log.e("SAN ", " textView1 click exp " + e.toString());
                }
            }
        });

        textView3.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT, 0.34f));
        textView3.setText(context.getResources().getString(R.string.Predictions));
        textView3.setPadding(10,20,10,20);
        textView3.setBackground( context.getResources().getDrawable(R.drawable.bg_border_kundli_button));
        textView3.setGravity(Gravity.CENTER);
        textView3.setTextColor(context.getResources().getColor(R.color.black));
        setMargin(textView3, 10, 0, 0, 0);
        textView3.setForeground(AppCompatResources.getDrawable(context,outValue.resourceId));
        FontUtils.changeFont(context, textView3, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);

        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAppViewFragment().callMethodTextView3(context);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_KUNDALI_BOTTOM_BUTTON_PREDICTIONS, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            }
        });

        layoutLinearI.addView(textView1 );
        layoutLinearI.addView(textView2 );
        layoutLinearI.addView(textView3 );

        layoutLinearMain.addView(layoutLinearI);

        /////////// Second Row

        final LinearLayout layoutLinearII = new LinearLayout(context);
        layoutLinearII.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layoutLinearII.setOrientation(LinearLayout.HORIZONTAL);
        layoutLinearII.setPadding(0,10,0,10);
        layoutLinearII.setWeightSum(1.0f);

        TextView textView4 = new TextView(context);
        TextView textView5 = new TextView(context);
        TextView textView6 = new TextView(context);

        textView4.setLayoutParams( new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT, 0.33f) );
        textView4.setText(context.getResources().getString(R.string.KPSystem));
        textView4.setPadding(10,20,10,20);
        textView4.setBackground( context.getResources().getDrawable(R.drawable.bg_border_kundli_button));
        textView4.setGravity(Gravity.CENTER);
        textView4.setTextColor(context.getResources().getColor(R.color.black));
        textView4.setForeground(AppCompatResources.getDrawable(context,outValue.resourceId));
        FontUtils.changeFont(context, textView4, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);

        textView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getAppViewFragment().callMethodTextView4(context);
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_KUNDALI_BOTTOM_BUTTON_KPS_SYSTEM, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                }  catch (Exception e){
                    //Log.e("SAN ", " textView1 click exp " + e.toString());
                }
            }
        });

        textView5.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT, 0.33f));
        textView5.setText(context.getResources().getString(R.string.Shodashvarga));
        textView5.setPadding(10,20,10,20);
        textView5.setBackground( context.getResources().getDrawable(R.drawable.bg_border_kundli_button));
        textView5.setGravity(Gravity.CENTER);
        textView5.setTextColor(context.getResources().getColor(R.color.black));
        setMargin(textView5, 10, 0, 0, 0);
        textView5.setForeground(AppCompatResources.getDrawable(context,outValue.resourceId));
        FontUtils.changeFont(context, textView5, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);

        textView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAppViewFragment().callMethodTextView5(context);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_KUNDALI_BOTTOM_BUTTON_SHODASHVARGA, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            }
        });

        textView6.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT, 0.34f));
        textView6.setText(context.getResources().getString(R.string.LalKitab));
        textView6.setPadding(10,20,10,20);
        textView6.setBackground( context.getResources().getDrawable(R.drawable.bg_border_kundli_button));
        textView6.setGravity(Gravity.CENTER);
        textView6.setTextColor(context.getResources().getColor(R.color.black));
        setMargin(textView6, 10, 0, 0, 0);
        textView6.setForeground(AppCompatResources.getDrawable(context,outValue.resourceId));
        FontUtils.changeFont(context, textView6, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);

        textView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAppViewFragment().callMethodTextView6(context);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_KUNDALI_BOTTOM_BUTTON_LAL_KITAB, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            }
        });

        layoutLinearII.addView(textView4 );
        layoutLinearII.addView(textView5 );
        layoutLinearII.addView(textView6 );

        layoutLinearMain.addView(layoutLinearII);

        //////// Third Row

        final LinearLayout layoutLinearIII = new LinearLayout(context);
        layoutLinearIII.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layoutLinearIII.setOrientation(LinearLayout.HORIZONTAL);
        layoutLinearIII.setPadding(0,10,0,10);
        layoutLinearIII.setWeightSum(1.0f);

        TextView textView7 = new TextView(context);
        TextView textView8 = new TextView(context);
        TextView textView9 = new TextView(context);

        textView7.setLayoutParams( new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT, 0.33f) );
        textView7.setText(context.getResources().getString(R.string.Varshphal));
        textView7.setPadding(10,20,10,20);
        textView7.setBackground( context.getResources().getDrawable(R.drawable.bg_border_kundli_button));
        textView7.setGravity(Gravity.CENTER);
        textView7.setTextColor(context.getResources().getColor(R.color.black));
        textView7.setForeground(AppCompatResources.getDrawable(context,outValue.resourceId));
        FontUtils.changeFont(context, textView7, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);

        textView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAppViewFragment().callMethodTextView7(context);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_KUNDALI_BOTTOM_BUTTON_VARSHPHAL, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            }
        });

        textView8.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT, 0.33f));
        textView8.setText(context.getResources().getString(R.string.rajyoga));
        textView8.setPadding(10,20,10,20);
        textView8.setBackground( context.getResources().getDrawable(R.drawable.bg_border_kundli_button));
        textView8.setGravity(Gravity.CENTER);
        textView8.setTextColor(context.getResources().getColor(R.color.black));
        textView8.setForeground(AppCompatResources.getDrawable(context,outValue.resourceId));
        setMargin(textView8, 10, 0, 0, 0);
        FontUtils.changeFont(context, textView8, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);

        textView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAppViewFragment().callMethodTextView8(context);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_KUNDALI_BOTTOM_BUTTON_RAJ_YOGA, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            }
        });

        textView9.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT, 0.34f));
        textView9.setText(context.getResources().getString(R.string.transit));
        textView9.setPadding(10,20,10,20);
        textView9.setBackground( context.getResources().getDrawable(R.drawable.bg_border_kundli_button));
        textView9.setGravity(Gravity.CENTER);
        textView9.setTextColor(context.getResources().getColor(R.color.black));
        setMargin(textView9, 10, 0, 0, 0);
        FontUtils.changeFont(context, textView9, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        textView9.setForeground(AppCompatResources.getDrawable(context,outValue.resourceId));
        textView9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAppViewFragment().callMethodTextView9(context);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_KUNDALI_BOTTOM_BUTTON_GOCHAR, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            }
        });

        layoutLinearIII.addView(textView7 );
        layoutLinearIII.addView(textView8 );
        layoutLinearIII.addView(textView9 );

        layoutLinearMain.addView(layoutLinearIII);

        return layoutLinearMain;

    }

    public static View getViewOfPredictions(int subModuleId, Context context, int LANGUAGE_CODE, CScreenConstants SCREEN_CONSTANTS) {

        View predictionsViews = new View(context);

        boolean sessionExist = false;
        BeanHoroPersonalInfo horoPersonalInfo = CGlobal.getCGlobalObject().getHoroPersonalInfoObject();

        switch (subModuleId) {
            case CGlobalVariables.SUB_MODULE_PREDICTION_LIFE_PREDICTIONS:
                String url1 = CUtils.getPredictionURL(WebViewPredictions.LIFE_PREDICTIONS, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, LANGUAGE_CODE, sessionExist,context);
                predictionsViews.setTag(url1);
             //   Log.e("urlCheck", "SUB_MODULE_PREDICTION_LIFE_PREDICTIONS: "+url1 );
                break;
            case CGlobalVariables.SUB_MODULE_PREDICTION_MONTHLY_PREDICTIONS:
                String mPredURL = CUtils.getPredictionURL(WebViewPredictions.MONTHLY_PREDICTIONS, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, LANGUAGE_CODE, sessionExist,context);
                predictionsViews.setTag(mPredURL);
            //    Log.e("urlCheck", "SUB_MODULE_PREDICTION_MONTHLY_PREDICTIONS: "+mPredURL );
                break;
            case CGlobalVariables.SUB_MODULE_PREDICTION_DAILY_PREDICTIONS:
                String dailyPredUrl = CUtils.getPredictionURL(WebViewPredictions.DAILY_PREDICTIONS, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, LANGUAGE_CODE, sessionExist,context);
                predictionsViews.setTag(dailyPredUrl);
            //    Log.e("urlCheck", "SUB_MODULE_PREDICTION_DAILY_PREDICTIONS: "+dailyPredUrl );
                break;
            case CGlobalVariables.SUB_MODULE_PREDICTION_MANGAL_DOSH:
                String mngalUrl = CUtils.getPredictionURL(WebViewPredictions.MANGAL_DOSH, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, LANGUAGE_CODE, sessionExist,context);
                predictionsViews.setTag(mngalUrl);
            //    Log.e("urlCheck", "SUB_MODULE_PREDICTION_MANGAL_DOSH: "+ mngalUrl);
                break;
            case CGlobalVariables.SUB_MODULE_PREDICTION_SADE_SATI:
                String sadeSatiUrl = CUtils.getPredictionURL(WebViewPredictions.SADE_SATI, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, LANGUAGE_CODE, sessionExist,context);
                predictionsViews.setTag(sadeSatiUrl);
             //   Log.e("urlCheck", "SUB_MODULE_PREDICTION_SADE_SATI: "+sadeSatiUrl );
                break;
            case CGlobalVariables.SUB_MODULE_PREDICTION_KAAL_SARP_DOSHA:
                String kaalSarpUrl =CUtils.getPredictionURL(WebViewPredictions.KAAL_SARP_DOSHA, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, LANGUAGE_CODE, sessionExist,context);
                predictionsViews.setTag(kaalSarpUrl);
            //    Log.e("urlCheck", "SUB_MODULE_PREDICTION_KAAL_SARP_DOSHA: "+kaalSarpUrl );
                break;
            case CGlobalVariables.SUB_MODULE_PREDICTION_LALKITAB_DEBT:
                String debtUrl = CUtils.getPredictionURL(WebViewPredictions.LALKITAB_DEBT, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, LANGUAGE_CODE, sessionExist,context);
                predictionsViews.setTag(debtUrl);
              //  Log.e("urlCheck", "SUB_MODULE_PREDICTION_LALKITAB_DEBT: "+debtUrl );

                break;
            case CGlobalVariables.SUB_MODULE_PREDICTION_LALKITAB_TEVA_TYPE:
                String tevaUrl = CUtils.getPredictionURL(WebViewPredictions.LALKITAB_TEVA_TYPE, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, LANGUAGE_CODE, sessionExist,context);
                predictionsViews.setTag(tevaUrl);

             //   Log.e("urlCheck", "SUB_MODULE_PREDICTION_LALKITAB_TEVA_TYPE: "+tevaUrl );
                break;
            case CGlobalVariables.SUB_MODULE_PREDICTION_LAL_KITAB_REMEDIES:
                String remediesUrl = CUtils.getPredictionURL(WebViewPredictions.LAL_KITAB_REMEDIES, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, LANGUAGE_CODE, sessionExist,context);
                predictionsViews.setTag(remediesUrl);
             //   Log.e("urlCheck", "SUB_MODULE_PREDICTION_LAL_KITAB_REMEDIES: "+remediesUrl );

                break;
            case CGlobalVariables.SUB_MODULE_PREDICTION_ASCENDANT_PREDICTION:
                String ascendentUrl = CUtils.getPredictionURL(WebViewPredictions.ASCENDANT_PREDICTION, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, LANGUAGE_CODE, sessionExist,context);
                predictionsViews.setTag(ascendentUrl);
              //  Log.e("urlCheck", "SUB_MODULE_PREDICTION_ASCENDANT_PREDICTION: "+ascendentUrl );

                break;
            case CGlobalVariables.SUB_MODULE_PREDICTION_PLANET_CONSIDERATION:
                String planetUrl = CUtils.getPredictionURL(WebViewPredictions.PLANET_CONSIDERATION, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, LANGUAGE_CODE, sessionExist,context);
                predictionsViews.setTag(planetUrl);
             //   Log.e("urlCheck", "SUB_MODULE_PREDICTION_PLANET_CONSIDERATION: "+planetUrl );

                break;
            case CGlobalVariables.SUB_MODULE_PREDICTION_GEMSTONE_REPORT:
                String gemStoneUrl = CUtils.getPredictionURL(WebViewPredictions.GEMSTONE_REPORT, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, LANGUAGE_CODE, sessionExist,context);
                predictionsViews.setTag(gemStoneUrl);
                Log.e("urlCheck", "SUB_MODULE_PREDICTION_GEMSTONE_REPORT: "+gemStoneUrl );

                break;
            case CGlobalVariables.SUB_MODULE_PREDICTION_TRANSIT_TODAY:
                String transUrl = CUtils.getPredictionURL(WebViewPredictions.TRANSIT_TODAY, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, LANGUAGE_CODE, sessionExist,context);
                predictionsViews.setTag(transUrl);
             //   Log.e("urlCheck", "SUB_MODULE_PREDICTION_TRANSIT_TODAY: "+transUrl );
                break;
            case CGlobalVariables.SUB_MODULE_PREDICTION_MAHADASHA_PHALA:
                String mahadashaUrl = CUtils.getPredictionURL(WebViewPredictions.MAHADASHA_PHALA, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, LANGUAGE_CODE, sessionExist,context);
                predictionsViews.setTag(mahadashaUrl);
             //   Log.e("urlCheck", "SUB_MODULE_PREDICTION_MAHADASHA_PHALA: "+mahadashaUrl );
                break;
            case CGlobalVariables.SUB_MODULE_PREDICTION_NAKSHATRA_REPORT:
                String nakshatraUrl = CUtils.getPredictionURL(WebViewPredictions.NAKSHATRA_REPORT, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, LANGUAGE_CODE, sessionExist,context);
                predictionsViews.setTag(nakshatraUrl);
              //  Log.e("urlCheck", "SUB_MODULE_PREDICTION_NAKSHATRA_REPORT: "+nakshatraUrl );
                break;
            case CGlobalVariables.SUB_MODULE_PREDICTION_VARSHPHAL:   //ADDED BY HEVENDRA ON 13-01-2015
                String varshPhalUrl = CUtils.getPredictionURL(WebViewPredictions.PREDICTION_VARSHPHAL, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, LANGUAGE_CODE, sessionExist,context);
                predictionsViews.setTag(varshPhalUrl);
              //  Log.e("urlCheck", "SUB_MODULE_PREDICTION_VARSHPHAL: "+varshPhalUrl );
                break;
            //tejinder added
            case CGlobalVariables.SUB_MODULE_PREDICTION_BABYNAME:   //ADDED BY HEVENDRA ON 13-01-2015
                String babyNameUrl = CUtils.getPredictionURL(WebViewPredictions.PREDICTION_BABYNAME, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, LANGUAGE_CODE, sessionExist,context);
                predictionsViews.setTag(babyNameUrl);
              //  Log.e("urlCheck", "SUB_MODULE_PREDICTION_BABYNAME: "+babyNameUrl );
                break;
            case CGlobalVariables.SUB_MODULE_PREDICTION_MOONWESTERN:   //ADDED BY HEVENDRA ON 13-01-2015
                String moonwesternUrl = CUtils.getPredictionURL(WebViewPredictions.PREDICTION_MOONWESTERN, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, LANGUAGE_CODE, sessionExist,context);
                predictionsViews.setTag(moonwesternUrl);
              //  Log.e("urlCheck", "SUB_MODULE_PREDICTION_MOONWESTERN: "+moonwesternUrl );
                break;
            case CGlobalVariables.SUB_MODULE_PREDICTION_MOON:   //ADDED BY HEVENDRA ON 13-01-2015
                String moonUrl = CUtils.getPredictionURL(WebViewPredictions.PREDICTION_MOON, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, LANGUAGE_CODE, sessionExist,context);
                predictionsViews.setTag(moonUrl);
             //   Log.e("urlCheck", "SUB_MODULE_PREDICTION_MOON: "+moonUrl );
                break;
            case CGlobalVariables.SUB_MODULE_PREDICTION_CharAntardasha:
                String charAntarUrl = CUtils.getPredictionURL(WebViewPredictions.CALCULATE_CHARANTARDSASA, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, LANGUAGE_CODE, sessionExist,context);
                predictionsViews.setTag(charAntarUrl);
             //   Log.e("urlCheck", "SUB_MODULE_PREDICTION_CharAntardasha: "+charAntarUrl );
                break;
            case CGlobalVariables.SUB_MODULE_PREDICTION_YoginiDasha:
                String yoginiDasaUrl = CUtils.getPredictionURL(WebViewPredictions.CALCULATE_YOGINIDASHA, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, LANGUAGE_CODE, sessionExist,context);
                predictionsViews.setTag(yoginiDasaUrl);
             //   Log.e("urlCheck", "SUB_MODULE_PREDICTION_YoginiDasha: "+yoginiDasaUrl );
                break;
            case CGlobalVariables.SUB_MODULE_PREDICTION_RUDRAKSHA:
                String rudraUrl = CUtils.getPredictionURL(WebViewPredictions.CALCULATE_RUDRAKSHA, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, LANGUAGE_CODE, sessionExist,context);
                predictionsViews.setTag(rudraUrl);
            //    Log.e("urlCheck", "SUB_MODULE_PREDICTION_RUDRAKSHA: "+rudraUrl );
                break;
            case CGlobalVariables.SUB_MODULE_PREDICTION_JADI:
                String jadiUrl = CUtils.getPredictionURL(WebViewPredictions.CALCULATE_JADI, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, LANGUAGE_CODE, sessionExist,context);
                predictionsViews.setTag(jadiUrl);
           //     Log.e("urlCheck", "SUB_MODULE_PREDICTION_JADI: "+jadiUrl );
                break;
            case CGlobalVariables.SUB_MODULE_PREDICTION_YANTRA:
                String yantraUrl = CUtils.getPredictionURL(WebViewPredictions.CALCULATE_YANTRA, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, LANGUAGE_CODE, sessionExist,context);
                predictionsViews.setTag(yantraUrl);
         //       Log.e("urlCheck", "SUB_MODULE_PREDICTION_YANTRA: "+yantraUrl );
                break;
            default: predictionsViews = null;

        }

        return predictionsViews;
    }


    public static View getViewOfLalkitab(int subModuleId, Context context, Typeface typeface, int chartStyle, int languageCode, CScreenConstants SCREEN_CONSTANTS, int inputYear) throws UIKundliMiscCalculationException {

        View lalkitabViews = null;
        int[] planetsInRashiLagna = new int[13];
        int[] planetsInRashiLagnaVayshPhal = new int[13];
        boolean isTablet = CUtils.isTablet(context);
        BeanHoroPersonalInfo horoPersonalInfo = CGlobal.getCGlobalObject().getHoroPersonalInfoObject();
        String[] planetNames = context.getResources().getStringArray(R.array.VarChartPlanets);
        ControllerManager objControllerManager = new ControllerManager();

        boolean sessionExist = false;
        switch (subModuleId) {
            case CGlobalVariables.SUB_MODULE_LALKITAB_LAGNA:
                planetsInRashiLagna = objControllerManager.getLalKitabKundliPlanetsRashiArray(CGlobal.getCGlobalObject().getPlanetDataObject(), -1);
                if (chartStyle == 0) {
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, planetNames, planetsInRashiLagna, CGlobalVariables.enuKundliType.NORTH, isTablet, SCREEN_CONSTANTS, -1, languageCode);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    lalkitabViews = scrollView;
                } else if (chartStyle == 1) {
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, planetNames, planetsInRashiLagna, CGlobalVariables.enuKundliType.SOUTH, isTablet, SCREEN_CONSTANTS, -1, languageCode);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    lalkitabViews = scrollView;
                } else if (chartStyle == 2) {
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, planetNames, planetsInRashiLagna, CGlobalVariables.enuKundliType.EAST, isTablet, SCREEN_CONSTANTS, -1, languageCode);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    lalkitabViews = scrollView;
                }
                break;
            case CGlobalVariables.SUB_MODULE_LALKITAB_REMEDIES:
//			sessionExist = true;
//			lalkitabViews = new WebViewPredictions(context,CUtils.getPredictionURL(WebViewPredictions.LAL_KITAB_REMEDIES,horoPersonalInfo,SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE,languageCode, sessionExist));
                lalkitabViews = new View(context);
                String remedyURL = CUtils.getPredictionURL(WebViewPredictions.LAL_KITAB_REMEDIES, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, languageCode, sessionExist,context);
                lalkitabViews.setTag(remedyURL);
           //     Log.e("urlCheck", "SUB_MODULE_LALKITAB_REMEDIES: "+remedyURL);
                break;
            case CGlobalVariables.SUB_MODULE_LALKITAB_VARSHA_CHART:
                planetsInRashiLagnaVayshPhal = objControllerManager.getLalKitabKundliPlanetsRashiArray(CGlobal.getCGlobalObject().getPlanetDataObject(), inputYear);
                if (chartStyle == 0) {
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, planetNames, planetsInRashiLagnaVayshPhal, CGlobalVariables.enuKundliType.NORTH, isTablet, SCREEN_CONSTANTS, -1, languageCode);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    lalkitabViews = scrollView;
                } else if (chartStyle == 1) {
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, planetNames, planetsInRashiLagnaVayshPhal, CGlobalVariables.enuKundliType.SOUTH, isTablet, SCREEN_CONSTANTS, -1, languageCode);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    lalkitabViews = scrollView;
                } else if (chartStyle == 2) {
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, planetNames, planetsInRashiLagnaVayshPhal, CGlobalVariables.enuKundliType.EAST, isTablet, SCREEN_CONSTANTS, -1, languageCode);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    lalkitabViews = scrollView;
                }
                break;
            case CGlobalVariables.SUB_MODULE_LALKITAB_DEBTS:
                //lalkitabViews = new WebViewPredictions(context, CUtils.getPredictionURL(WebViewPredictions.LALKITAB_DEBT,horoPersonalInfo,SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE,languageCode, sessionExist));
                lalkitabViews = new View(context);
                String url = CUtils.getPredictionURL(WebViewPredictions.LALKITAB_DEBT, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, languageCode, sessionExist,context);
                lalkitabViews.setTag(url);
          //      Log.e("urlCheck", "SUB_MODULE_LALKITAB_DEBTS: "+url);
                break;
            case CGlobalVariables.SUB_MODULE_LALKITAB_TEVA_TYPE:
//			lalkitabViews = new WebViewPredictions(context,CUtils.getPredictionURL(WebViewPredictions.LALKITAB_TEVA_TYPE,horoPersonalInfo,SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE,languageCode, sessionExist));
                lalkitabViews = new View(context);
                String url1 = CUtils.getPredictionURL(WebViewPredictions.LALKITAB_TEVA_TYPE, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, languageCode, sessionExist,context);
                lalkitabViews.setTag(url1);
//                Log.e("urlCheck", "SUB_MODULE_LALKITAB_TEVA_TYPE: "+url1);
                break;
            case CGlobalVariables.SUB_MODULE_LALKITAB_DASHA_TYPE:
               lalkitabViews = new View(context);
                String url2 = CUtils.getPredictionURL(WebViewPredictions.LALKITAB_DASHA_TYPE, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, languageCode, sessionExist,context);
                lalkitabViews.setTag(url2);
                break;
            case CGlobalVariables.SUB_MODULE_LALKITAB_PLANET_TYPE:
                lalkitabViews = new View(context);
                String url3 = CUtils.getPredictionURL(WebViewPredictions.LALKITAB_PLANET_TYPE, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, languageCode, sessionExist,context);
                lalkitabViews.setTag(url3);
                break;
            case CGlobalVariables.SUB_MODULE_LALKITAB_HOUSE_TYPE:
                lalkitabViews = new View(context);
                String url4 = CUtils.getPredictionURL(WebViewPredictions.LALKITAB_HOUSE_TYPE, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, languageCode, sessionExist,context);
                lalkitabViews.setTag(url4);
                break;

        }

        return lalkitabViews;
    }

    public static View getViewOfShodashvarga(int subModuleId, Context context, Typeface typeface,
                                             int chartStyle, int LANGUAGE_CODE,
                                             CScreenConstants SCREEN_CONSTANTS) throws Exception {

        View shodashvargaViews = null;
        boolean isTablet = CUtils.isTablet(context);
        int[] planetsInRashiLagna = new int[13];
        int[] planetsInRashDrekkana = new int[13];
        int[] planetsInRashiChaturthamsha = new int[13];
        int[] planetsInRashiSaptamamsha = new int[13];
        int[] planetsInRashiNavamsha = new int[13];
        int[] planetsInRashiDashamamsha = new int[13];
        int[] planetsInRashiDwadashamamsha = new int[13];
        int[] planetsInRashiShodashamsha = new int[13];
        int[] planetsInRashiVimshamsha = new int[13];
        int[] planetsInRashiChaturvimshamsha = new int[13];
        int[] planetsInRashiSaptavimshamsha = new int[13];
        int[] planetsInRashiTrimshamsha = new int[13];
        int[] planetsInRashiKhavedamsha = new int[13];
        int[] planetsInRashiAkshvedamsha = new int[13];
        int[] planetsInRashiShashtiamsha = new int[13];

        ControllerManager objControllManager = new ControllerManager();

        switch (subModuleId) {

            case CGlobalVariables.SUB_MODULE_SHODASHVARGA_LAGNA:
                planetsInRashiLagna = objControllManager.getPlanetsInRashiFromShodashvarga(CGlobal.getCGlobalObject().getOutShodashvargaObject().get_lagna());

                if (chartStyle == 0) {
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiLagna, null, CGlobalVariables.enuKundliType.NORTH, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                } else if (chartStyle == 1) {
//			shodashvargaViews[LAGNA] = new ViewDrawRotateKundli(context,context.getResources().getStringArray(R.array.VarChartPlanets),planetsInRashiLagna,null, CGlobalVariables.enuKundliType.SOUTH,isTablet,SCREEN_CONSTANTS);
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiLagna, null, CGlobalVariables.enuKundliType.SOUTH, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                }
                //@Tejinder singh added for EastStyle chart
                else if (chartStyle == 2) {
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiLagna, null, CGlobalVariables.enuKundliType.EAST, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                }
                break;
            case CGlobalVariables.SUB_MODULE_SHODASHVARGA_HORA:
                if (chartStyle == 0) {
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewShodashvargaNorthHora(context, CGlobalVariables.enuShodashvarga.SHODASHVARGA_HORA, SCREEN_CONSTANTS, context.getResources().getStringArray(R.array.VarChartPlanets));
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                } else if (chartStyle == 1) {
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewShodashvargaNorthHora(context, CGlobalVariables.enuShodashvarga.SHODASHVARGA_HORA, SCREEN_CONSTANTS, context.getResources().getStringArray(R.array.VarChartPlanets));
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                } else if (chartStyle == 2) {
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewShodashvargaNorthHora(context, CGlobalVariables.enuShodashvarga.SHODASHVARGA_HORA, SCREEN_CONSTANTS, context.getResources().getStringArray(R.array.VarChartPlanets));
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                }
                break;
            case CGlobalVariables.SUB_MODULE_SHODASHVARGA_DREKKANA:
                planetsInRashDrekkana = objControllManager.getPlanetsInRashiFromShodashvarga(CGlobal.getCGlobalObject().getOutShodashvargaObject().get_drekkana());

                if (chartStyle == 0) {
//			shodashvargaViews[DREKKANA] = new ViewDrawRotateKundli(context,context.getResources().getStringArray(R.array.VarChartPlanets),planetsInRashDrekkana,null, CGlobalVariables.enuKundliType.NORTH,isTablet,SCREEN_CONSTANTS); 
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashDrekkana, null, CGlobalVariables.enuKundliType.NORTH, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                } else if (chartStyle == 1) {
//			shodashvargaViews[DREKKANA] = new ViewDrawRotateKundli(context,context.getResources().getStringArray(R.array.VarChartPlanets),planetsInRashDrekkana,null, CGlobalVariables.enuKundliType.SOUTH,isTablet,SCREEN_CONSTANTS);
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashDrekkana, null, CGlobalVariables.enuKundliType.SOUTH, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                } else if (chartStyle == 2) {
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashDrekkana, null, CGlobalVariables.enuKundliType.EAST, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                }
                break;
            case CGlobalVariables.SUB_MODULE_SHODASHVARGA_CHATURTHAMSHA:

                planetsInRashiChaturthamsha = objControllManager.getPlanetsInRashiFromShodashvarga(CGlobal.getCGlobalObject().getOutShodashvargaObject().get_chaturthamsha());

                if (chartStyle == 0) {
//			shodashvargaViews[CHATURTHAMSHA] = new ViewDrawRotateKundli(context,context.getResources().getStringArray(R.array.VarChartPlanets),planetsInRashiChaturthamsha,null, CGlobalVariables.enuKundliType.NORTH,isTablet,SCREEN_CONSTANTS); 
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiChaturthamsha, null, CGlobalVariables.enuKundliType.NORTH, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                } else if (chartStyle == 1) {
//			shodashvargaViews[CHATURTHAMSHA] = new ViewDrawRotateKundli(context,context.getResources().getStringArray(R.array.VarChartPlanets),planetsInRashiChaturthamsha,null, CGlobalVariables.enuKundliType.SOUTH,isTablet,SCREEN_CONSTANTS);
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiChaturthamsha, null, CGlobalVariables.enuKundliType.SOUTH, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                } else if (chartStyle == 2) {
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiChaturthamsha, null, CGlobalVariables.enuKundliType.EAST, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                }
                break;
            case CGlobalVariables.SUB_MODULE_SHODASHVARGA_SAPTAMSHA:
                planetsInRashiSaptamamsha = objControllManager.getPlanetsInRashiFromShodashvarga(CGlobal.getCGlobalObject().getOutShodashvargaObject().get_saptamamsha());

                if (chartStyle == 0) {
//			shodashvargaViews[SAPTAMSHA] = new ViewDrawRotateKundli(context,context.getResources().getStringArray(R.array.VarChartPlanets),planetsInRashiSaptamamsha,null, CGlobalVariables.enuKundliType.NORTH,isTablet,SCREEN_CONSTANTS); 
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiSaptamamsha, null, CGlobalVariables.enuKundliType.NORTH, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                } else if (chartStyle == 1) {
//			shodashvargaViews[SAPTAMSHA] = new ViewDrawRotateKundli(context,context.getResources().getStringArray(R.array.VarChartPlanets),planetsInRashiSaptamamsha,null, CGlobalVariables.enuKundliType.SOUTH,isTablet,SCREEN_CONSTANTS);
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiSaptamamsha, null, CGlobalVariables.enuKundliType.SOUTH, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                } else if (chartStyle == 2) {
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiSaptamamsha, null, CGlobalVariables.enuKundliType.EAST, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                }
                break;
            case CGlobalVariables.SUB_MODULE_SHODASHVARGA_NAVAMSHA:
                planetsInRashiNavamsha = objControllManager.getPlanetsInRashiFromShodashvarga(CGlobal.getCGlobalObject().getOutShodashvargaObject().get_navamsha());

                if (chartStyle == 0) {
//			shodashvargaViews[NAVAMSHA] = new ViewDrawRotateKundli(context,context.getResources().getStringArray(R.array.VarChartPlanets),planetsInRashiNavamsha,null, CGlobalVariables.enuKundliType.NORTH,isTablet,SCREEN_CONSTANTS); 
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiNavamsha, null, CGlobalVariables.enuKundliType.NORTH, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                } else if (chartStyle == 1) {
//			shodashvargaViews[NAVAMSHA] = new ViewDrawRotateKundli(context,context.getResources().getStringArray(R.array.VarChartPlanets),planetsInRashiNavamsha,null, CGlobalVariables.enuKundliType.SOUTH,isTablet,SCREEN_CONSTANTS);
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiNavamsha, null, CGlobalVariables.enuKundliType.SOUTH, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                } else if (chartStyle == 2) {
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiNavamsha, null, CGlobalVariables.enuKundliType.EAST, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                }
                break;
            case CGlobalVariables.SUB_MODULE_SHODASHVARGA_DASHAMSHA:

                planetsInRashiDashamamsha = objControllManager.getPlanetsInRashiFromShodashvarga(CGlobal.getCGlobalObject().getOutShodashvargaObject().get_dashamamsha());

                if (chartStyle == 0) {
//			shodashvargaViews[DASHAMSHA] = new ViewDrawRotateKundli(context,context.getResources().getStringArray(R.array.VarChartPlanets),planetsInRashiDashamamsha,null, CGlobalVariables.enuKundliType.NORTH,isTablet,SCREEN_CONSTANTS); 
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiDashamamsha, null, CGlobalVariables.enuKundliType.NORTH, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                } else if (chartStyle == 1) {
//			shodashvargaViews[DASHAMSHA] = new ViewDrawRotateKundli(context,context.getResources().getStringArray(R.array.VarChartPlanets),planetsInRashiDashamamsha,null, CGlobalVariables.enuKundliType.SOUTH,isTablet,SCREEN_CONSTANTS);
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiDashamamsha, null, CGlobalVariables.enuKundliType.SOUTH, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                } else if (chartStyle == 2) {
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiDashamamsha, null, CGlobalVariables.enuKundliType.EAST, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                }
                break;
            case CGlobalVariables.SUB_MODULE_SHODASHVARGA_DWADASHMSHA:

                planetsInRashiDwadashamamsha = objControllManager.getPlanetsInRashiFromShodashvarga(CGlobal.getCGlobalObject().getOutShodashvargaObject().get_dwadashamamsha());

                if (chartStyle == 0) {
//			shodashvargaViews[DWADASHMSHA] = new ViewDrawRotateKundli(context,context.getResources().getStringArray(R.array.VarChartPlanets),planetsInRashiDwadashamamsha,null, CGlobalVariables.enuKundliType.NORTH,isTablet,SCREEN_CONSTANTS); 
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiDwadashamamsha, null, CGlobalVariables.enuKundliType.NORTH, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                } else if (chartStyle == 1) {
//			shodashvargaViews[DWADASHMSHA] = new ViewDrawRotateKundli(context,context.getResources().getStringArray(R.array.VarChartPlanets),planetsInRashiDwadashamamsha,null, CGlobalVariables.enuKundliType.SOUTH,isTablet,SCREEN_CONSTANTS);
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiDwadashamamsha, null, CGlobalVariables.enuKundliType.SOUTH, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                } else if (chartStyle == 2) {
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiDwadashamamsha, null, CGlobalVariables.enuKundliType.EAST, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                }
                break;
            case CGlobalVariables.SUB_MODULE_SHODASHVARGA_SHODASHAMSHA:


                planetsInRashiShodashamsha = objControllManager.getPlanetsInRashiFromShodashvarga(CGlobal.getCGlobalObject().getOutShodashvargaObject().get_shodashamsha());

                if (chartStyle == 0) {
//			shodashvargaViews[SHODASHAMSHA] = new ViewDrawRotateKundli(context,context.getResources().getStringArray(R.array.VarChartPlanets),planetsInRashiShodashamsha,null, CGlobalVariables.enuKundliType.NORTH,isTablet,SCREEN_CONSTANTS); 
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiShodashamsha, null, CGlobalVariables.enuKundliType.NORTH, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                } else if (chartStyle == 1) {
//			shodashvargaViews[SHODASHAMSHA] = new ViewDrawRotateKundli(context,context.getResources().getStringArray(R.array.VarChartPlanets),planetsInRashiShodashamsha,null, CGlobalVariables.enuKundliType.SOUTH,isTablet,SCREEN_CONSTANTS);
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiShodashamsha, null, CGlobalVariables.enuKundliType.SOUTH, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                } else if (chartStyle == 2) {
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiShodashamsha, null, CGlobalVariables.enuKundliType.EAST, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                }
                break;
            case CGlobalVariables.SUB_MODULE_SHODASHVARGA_VISHAMSHA:
                planetsInRashiVimshamsha = objControllManager.getPlanetsInRashiFromShodashvarga(CGlobal.getCGlobalObject().getOutShodashvargaObject().get_vimshamsha());

                if (chartStyle == 0) {
//			shodashvargaViews[VISHAMSHA] = new ViewDrawRotateKundli(context,context.getResources().getStringArray(R.array.VarChartPlanets),planetsInRashiVimshamsha,null, CGlobalVariables.enuKundliType.NORTH,isTablet,SCREEN_CONSTANTS); 
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiVimshamsha, null, CGlobalVariables.enuKundliType.NORTH, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                } else if (chartStyle == 1) {
//			shodashvargaViews[VISHAMSHA] = new ViewDrawRotateKundli(context,context.getResources().getStringArray(R.array.VarChartPlanets),planetsInRashiVimshamsha,null, CGlobalVariables.enuKundliType.SOUTH,isTablet,SCREEN_CONSTANTS);
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiVimshamsha, null, CGlobalVariables.enuKundliType.SOUTH, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                } else if (chartStyle == 2) {
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiVimshamsha, null, CGlobalVariables.enuKundliType.EAST, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                }
                break;
            case CGlobalVariables.SUB_MODULE_SHODASHVARGA_CHATURVISHAMSHA:

                planetsInRashiChaturvimshamsha = objControllManager.getPlanetsInRashiFromShodashvarga(CGlobal.getCGlobalObject().getOutShodashvargaObject().get_chaturvimshamsha());

                if (chartStyle == 0) {
//			shodashvargaViews[CHATURVISHAMSHA] = new ViewDrawRotateKundli(context,context.getResources().getStringArray(R.array.VarChartPlanets),planetsInRashiChaturvimshamsha,null, CGlobalVariables.enuKundliType.NORTH,isTablet,SCREEN_CONSTANTS); 
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiChaturvimshamsha, null, CGlobalVariables.enuKundliType.NORTH, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                } else if (chartStyle == 1) {
//			shodashvargaViews[CHATURVISHAMSHA] = new ViewDrawRotateKundli(context,context.getResources().getStringArray(R.array.VarChartPlanets),planetsInRashiChaturvimshamsha,null, CGlobalVariables.enuKundliType.SOUTH,isTablet,SCREEN_CONSTANTS);
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiChaturvimshamsha, null, CGlobalVariables.enuKundliType.SOUTH, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                } else if (chartStyle == 2) {
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiChaturvimshamsha, null, CGlobalVariables.enuKundliType.EAST, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                }
                break;
            case CGlobalVariables.SUB_MODULE_SHODASHVARGA_SAPTAVISHMASHA:

                planetsInRashiSaptavimshamsha = objControllManager.getPlanetsInRashiFromShodashvarga(CGlobal.getCGlobalObject().getOutShodashvargaObject().get_saptavimshamsha());

                if (chartStyle == 0) {
//			shodashvargaViews[SAPTAVISHMASHA] = new ViewDrawRotateKundli(context,context.getResources().getStringArray(R.array.VarChartPlanets),planetsInRashiSaptavimshamsha,null, CGlobalVariables.enuKundliType.NORTH,isTablet,SCREEN_CONSTANTS); 
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiSaptavimshamsha, null, CGlobalVariables.enuKundliType.NORTH, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                } else if (chartStyle == 1) {
//			shodashvargaViews[SAPTAVISHMASHA] = new ViewDrawRotateKundli(context,context.getResources().getStringArray(R.array.VarChartPlanets),planetsInRashiSaptavimshamsha,null, CGlobalVariables.enuKundliType.SOUTH,isTablet,SCREEN_CONSTANTS);
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiSaptavimshamsha, null, CGlobalVariables.enuKundliType.SOUTH, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                } else if (chartStyle == 2) {
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiSaptavimshamsha, null, CGlobalVariables.enuKundliType.EAST, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                }
                break;
            case CGlobalVariables.SUB_MODULE_SHODASHVARGA_TRISAHAMSHA:

                planetsInRashiTrimshamsha = objControllManager.getPlanetsInRashiFromShodashvarga(CGlobal.getCGlobalObject().getOutShodashvargaObject().get_trimshamsha());

                if (chartStyle == 0) {
//			shodashvargaViews[TRISAHAMSHA] = new ViewDrawRotateKundli(context,context.getResources().getStringArray(R.array.VarChartPlanets),planetsInRashiTrimshamsha,null, CGlobalVariables.enuKundliType.NORTH,isTablet,SCREEN_CONSTANTS); 
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiTrimshamsha, null, CGlobalVariables.enuKundliType.NORTH, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                } else if (chartStyle == 1) {
//			shodashvargaViews[TRISAHAMSHA] = new ViewDrawRotateKundli(context,context.getResources().getStringArray(R.array.VarChartPlanets),planetsInRashiTrimshamsha,null, CGlobalVariables.enuKundliType.SOUTH,isTablet,SCREEN_CONSTANTS);
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiTrimshamsha, null, CGlobalVariables.enuKundliType.SOUTH, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                } else if (chartStyle == 2) {
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiTrimshamsha, null, CGlobalVariables.enuKundliType.EAST, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                }
                break;
            case CGlobalVariables.SUB_MODULE_SHODASHVARGA_KHAVEDAMSHA:
                planetsInRashiKhavedamsha = objControllManager.getPlanetsInRashiFromShodashvarga(CGlobal.getCGlobalObject().getOutShodashvargaObject().get_khavedamsha());

                if (chartStyle == 0) {
//			shodashvargaViews[KHAVEDAMSHA] = new ViewDrawRotateKundli(context,context.getResources().getStringArray(R.array.VarChartPlanets),planetsInRashiKhavedamsha,null, CGlobalVariables.enuKundliType.NORTH,isTablet,SCREEN_CONSTANTS); 
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiKhavedamsha, null, CGlobalVariables.enuKundliType.NORTH, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                } else if (chartStyle == 1) {
//			shodashvargaViews[KHAVEDAMSHA] = new ViewDrawRotateKundli(context,context.getResources().getStringArray(R.array.VarChartPlanets),planetsInRashiKhavedamsha,null, CGlobalVariables.enuKundliType.SOUTH,isTablet,SCREEN_CONSTANTS);
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiKhavedamsha, null, CGlobalVariables.enuKundliType.SOUTH, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                } else if (chartStyle == 2) {
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiKhavedamsha, null, CGlobalVariables.enuKundliType.EAST, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                }
                break;
            case CGlobalVariables.SUB_MODULE_SHODASHVARGA_AKSHAVEDAMSHA:
                planetsInRashiAkshvedamsha = objControllManager.getPlanetsInRashiFromShodashvarga(CGlobal.getCGlobalObject().getOutShodashvargaObject().get_akshvedamsha());

                if (chartStyle == 0) {
//			shodashvargaViews[AKSHAVEDAMSHA] = new ViewDrawRotateKundli(context,context.getResources().getStringArray(R.array.VarChartPlanets),planetsInRashiAkshvedamsha,null, CGlobalVariables.enuKundliType.NORTH,isTablet,SCREEN_CONSTANTS); 
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiAkshvedamsha, null, CGlobalVariables.enuKundliType.NORTH, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                } else if (chartStyle == 1) {
//			shodashvargaViews[AKSHAVEDAMSHA] = new ViewDrawRotateKundli(context,context.getResources().getStringArray(R.array.VarChartPlanets),planetsInRashiAkshvedamsha,null, CGlobalVariables.enuKundliType.SOUTH,isTablet,SCREEN_CONSTANTS);
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiAkshvedamsha, null, CGlobalVariables.enuKundliType.SOUTH, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                } else if (chartStyle == 2) {
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiAkshvedamsha, null, CGlobalVariables.enuKundliType.EAST, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                }
                break;
            case CGlobalVariables.SUB_MODULE_SHODASHVARGA_SHASHTIMSHA:
                planetsInRashiShashtiamsha = objControllManager.getPlanetsInRashiFromShodashvarga(CGlobal.getCGlobalObject().getOutShodashvargaObject().get_shastiamsha());

                if (chartStyle == 0) {
//			shodashvargaViews[SHASHTIMSHA] = new ViewDrawRotateKundli(context,context.getResources().getStringArray(R.array.VarChartPlanets),planetsInRashiShashtiamsha,null, CGlobalVariables.enuKundliType.NORTH,isTablet,SCREEN_CONSTANTS); 
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiShashtiamsha, null, CGlobalVariables.enuKundliType.NORTH, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                } else if (chartStyle == 1) {
//			shodashvargaViews[SHASHTIMSHA] = new ViewDrawRotateKundli(context,context.getResources().getStringArray(R.array.VarChartPlanets),planetsInRashiShashtiamsha,null, CGlobalVariables.enuKundliType.SOUTH,isTablet,SCREEN_CONSTANTS);
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiShashtiamsha, null, CGlobalVariables.enuKundliType.SOUTH, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                } else if (chartStyle == 2) {
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), planetsInRashiShashtiamsha, null, CGlobalVariables.enuKundliType.EAST, isTablet, SCREEN_CONSTANTS, -1, LANGUAGE_CODE,false);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    shodashvargaViews = scrollView;
                }
                break;
            case CGlobalVariables.SUB_MODULE_BASIC_SHODASHVARGATABLE:
                BeanHoroPersonalInfo horoPersonalInfo = CGlobal.getCGlobalObject().getHoroPersonalInfoObject();
                View predictionsViews = new View(context);
                String url = CUtils.getPredictionURL(WebViewPredictions.CALCULATE_SHODASHVARGA, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, LANGUAGE_CODE, false,context);
                predictionsViews.setTag(url);
              //  Log.e("urlCheck", "SUB_MODULE_BASIC_SHODASHVARGATABLE: "+url);
                shodashvargaViews = predictionsViews;

                return shodashvargaViews;
        }

        return shodashvargaViews;
    }

    public static View getViewOfTajikVarshphal(int subModuleId, Context context, Typeface typeface, int chartStyle, int languageCode, CScreenConstants SCREEN_CONSTANTS, BeanTajikVarshaphal beanTajikVarshaphal, int yearNumber) throws Exception {


        int[] planetsInRashiTajik = new int[13];
        double[] planetDegreeArray = new double[13];
        boolean isTablet = CUtils.isTablet(context);
        cscreenConstants = SCREEN_CONSTANTS;
        ControllerManager objControllManager = new ControllerManager();
        String[] planetNames = context.getResources().getStringArray(R.array.VarChartPlanets);

        //Log.e("SAN ", " CGAV getViewOfTajikVarshphal subModuleId " + subModuleId);

        switch (subModuleId) {
            case CGlobalVariables.SUB_MODULE_TAJIK_VARSHPHAL_CHART:
                planetsInRashiTajik = objControllManager.getLagnaKundliPlanetsRashiArray(beanTajikVarshaphal.getPlanetData());
                if (chartStyle == 0) {
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, planetNames, planetsInRashiTajik, CGlobalVariables.enuKundliType.NORTH, isTablet, SCREEN_CONSTANTS, -1, languageCode);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    tajikVarshphalViews = scrollView;
                } else if (chartStyle == 1) {
                    //To make scrollabel chart
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, planetNames, planetsInRashiTajik, CGlobalVariables.enuKundliType.SOUTH, isTablet, SCREEN_CONSTANTS, -1, languageCode);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    tajikVarshphalViews = scrollView;
                } else if (chartStyle == 2) {
                    final RelativeLayout layout = new RelativeLayout(context);
                    final ScrollView scrollView = new ScrollView(context);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    //End
                    View resultView = new ViewDrawRotateKundli(context, planetNames, planetsInRashiTajik, CGlobalVariables.enuKundliType.EAST, isTablet, SCREEN_CONSTANTS, -1, languageCode);
                    resultView.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                    layout.addView(resultView);
                    scrollView.addView(layout);
                    tajikVarshphalViews = scrollView;
                }
                break;
            case CGlobalVariables.SUB_MODULE_TAJIK_VARSHPHAL_PLANETS:
                //To make scrollabel chart
                final RelativeLayout layout = new RelativeLayout(context);
                final ScrollView scrollView = new ScrollView(context);
                layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                //End
                planetDegreeArray = objControllManager.getPlanetsDegreeArray(beanTajikVarshaphal.getPlanetData());
                planetsInRashiTajik = objControllManager.getLagnaKundliPlanetsRashiArray(beanTajikVarshaphal.getPlanetData());
                tajikVarshphalViewsPlant = new ViewVarshphalTable(context, planetDegreeArray,
                        planetsInRashiTajik, planetNames,
                        context.getResources().getStringArray(R.array.rasi_short_name_list)
                        , context.getResources().getStringArray(R.array.tajik_planet_table_heading_list),
                        context.getResources().getString(R.string.degree_sign),
                        context.getResources().getString(R.string.minute_sign),
                        context.getResources().getString(R.string.second_sign),
                        SCREEN_CONSTANTS, languageCode, context.getResources().getString(R.string.munthaBhav), beanTajikVarshaphal.getMuntha());
                tajikVarshphalViewsPlant.setLayoutParams(new LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight));
                layout.addView(tajikVarshphalViewsPlant);
                scrollView.addView(layout);

                tajikVarshphalViews = scrollView;

                break;
            /*case CGlobalVariables.SUB_MODULE_TAJIK_VARSHPHAL_MUNTHA:

				//To make scrollabel chart
				final RelativeLayout layout = new RelativeLayout(context);
			    final ScrollView scrollView = new ScrollView(context);
			    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
			    scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			   //End
			    View resultView = new ViewVarshphalMuntha(context,
						context.getResources().getString(R.string.munthaBhav),
						beanTajikVarshaphal.getMuntha(), SCREEN_CONSTANTS, languageCode);

			    resultView.setLayoutParams(new LayoutParams((int)SCREEN_CONSTANTS.DeviceScreenWidth+3, (int)SCREEN_CONSTANTS.DeviceScreenHeight));
			    layout.addView(resultView);
			    scrollView.addView(layout);
			    tajikVarshphalViews = scrollView;
				break;*/
            case CGlobalVariables.SUB_MODULE_TAJIK_VARSHPHAL_PREDICTIONS:
                BeanHoroPersonalInfo horoPersonalInfo = CGlobal.getCGlobalObject().getHoroPersonalInfoObject();
                String predURL = CUtils.getTajikYearlyPredictionUrl(context,yearNumber, CGlobal.getCGlobalObject().getHoroPersonalInfoObject(), SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, languageCode);
                //Log.e("SAN ", " CGAV predURL " + predURL);
                tajikVarshphalViews = new View(context);
              //  Log.e("urlCheck", "SUB_MODULE_TAJIK_VARSHPHAL_PREDICTIONS: "+predURL );
                tajikVarshphalViews.setTag(predURL);
                break;
            case CGlobalVariables.SUB_MODULE_TAJIK_VARSHPHAL_Panchadhikari:
                //BeanHoroPersonalInfo horoPersonalInfo1 = CGlobal.getCGlobalObject().getHoroPersonalInfoObject();
                String predURL1 = CUtils.getTajikYearlyMiscPanchadhikariUrl(context,yearNumber, CGlobal.getCGlobalObject().getHoroPersonalInfoObject(), SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, languageCode);
                //Log.e("SAN ", " CGAV CGlobalVariables.SUB_MODULE_MISC_Panchadhikari predURL1 " + predURL1);
                tajikVarshphalViews = new View(context);
                tajikVarshphalViews.setTag(predURL1);
              //  Log.e("urlCheck", "SUB_MODULE_TAJIK_VARSHPHAL_Panchadhikari: "+predURL1 );
                break;
        }
        return tajikVarshphalViews;
    }

    public static View getViewOfMiscellaneous(int subModuleId, Context context, int LANGUAGE_CODE, CScreenConstants SCREEN_CONSTANTS) {

        View predictionsViews = new View(context);

        boolean sessionExist = false;
        BeanHoroPersonalInfo horoPersonalInfo = CGlobal.getCGlobalObject().getHoroPersonalInfoObject();

        switch (subModuleId) {
            /*case CGlobalVariables.SUB_MODULE_MISC_Panchadhikari:
                predictionsViews.setTag(CUtils.getMiscURL(WebViewPredictions.MISC_Panchadhikari, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, LANGUAGE_CODE, sessionExist));
                break;
            */case CGlobalVariables.SUB_MODULE_MISC_Yoga_Dosha_Summary:
                predictionsViews.setTag(CUtils.getMiscURL(WebViewPredictions.MISC_Yoga_Dosha_summary, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, LANGUAGE_CODE, sessionExist,context));
                break;
            case CGlobalVariables.SUB_MODULE_MISC_Remedies_Recommendations:
                predictionsViews.setTag(CUtils.getMiscURL(WebViewPredictions.MISC_Remedies_Recommendations, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, LANGUAGE_CODE, sessionExist,context));
                break;
            case CGlobalVariables.SUB_MODULE_MISC_Karak:
                predictionsViews.setTag(CUtils.getMiscURL(WebViewPredictions.MISC_Karak, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, LANGUAGE_CODE, sessionExist,context));
                break;
            case CGlobalVariables.SUB_MODULE_MISC_Avastha:
                predictionsViews.setTag(CUtils.getMiscURL(WebViewPredictions.MISC_Avastha, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, LANGUAGE_CODE, sessionExist,context));
                break;
            case CGlobalVariables.SUB_MODULE_MISC_Navatara:
                predictionsViews.setTag(CUtils.getMiscURL(WebViewPredictions.MISC_Navatara, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, LANGUAGE_CODE, sessionExist,context));
                break;
            case CGlobalVariables.SUB_MODULE_MISC_Upgraha_Table:
                predictionsViews.setTag(CUtils.getMiscURL(WebViewPredictions.MISC_Upgraha_Table, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, LANGUAGE_CODE, sessionExist,context));
                break;
            case CGlobalVariables.SUB_MODULE_MISC_Upgraha_Chart:
                predictionsViews.setTag(CUtils.getMiscURL(WebViewPredictions.MISC_Upgraha_Chart, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, LANGUAGE_CODE, sessionExist,context));
                break;
            case CGlobalVariables.SUB_MODULE_MISC_Arudha_Chart:
                predictionsViews.setTag(CUtils.getMiscURL(WebViewPredictions.MISC_Arudha_Chart, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, LANGUAGE_CODE, sessionExist,context));
                break;
            case CGlobalVariables.SUB_MODULE_MISC_Current_Ruling_Planets:
                predictionsViews.setTag(CUtils.getMiscURL(WebViewPredictions.MISC_Current_Ruling_Planets, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, LANGUAGE_CODE, sessionExist,context));
                break;
            case CGlobalVariables.SUB_MODULE_MISC_Shodashvarta_Table_Rashi:
                predictionsViews.setTag(CUtils.getMiscURL(WebViewPredictions.MISC_Shodashvarta_Table_Rashi, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, LANGUAGE_CODE, sessionExist,context));
                break;
            case CGlobalVariables.SUB_MODULE_MISC_Shodashvarga_Bhava:
                predictionsViews.setTag(CUtils.getMiscURL(WebViewPredictions.MISC_Shodashvarga_Bhava, horoPersonalInfo, SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE, LANGUAGE_CODE, sessionExist,context));
                break;

        }

        return predictionsViews;
    }


    static public void setParamsOfViewKpPlanetaryPositions(int height) {
        viewKpPlanetaryPositions.setLayoutParams(new RelativeLayout.LayoutParams((int) cscreenConstants.DeviceScreenWidth, height));

    }

    static public void setParamsOfViewKpCuspalPositions(int height) {
        viewKpCuspalPositions.setLayoutParams(new RelativeLayout.LayoutParams((int) cscreenConstants.DeviceScreenWidth, height));

    }

    static public void setParamsOfViewPlanetSignification(int height) {
        viewPlanetSignification.setLayoutParams(new RelativeLayout.LayoutParams((int) cscreenConstants.DeviceScreenWidth, height));

    }

    static public void setParamsOfView(int height) {
        viewKpPlanetaryPositions.setLayoutParams(new RelativeLayout.LayoutParams((int) cscreenConstants.DeviceScreenWidth, height));

    }

    static public void setParamsOfViewMiscDrawable(int height) {
        viewMiscDrawables.setLayoutParams(new RelativeLayout.LayoutParams((int) cscreenConstants.DeviceScreenWidth, height));

    }

    static public void setParamsOfViewMiscDrawableSubPlanet(int height) {
        planetSubView1.setLayoutParams(new RelativeLayout.LayoutParams((int) cscreenConstants.DeviceScreenWidth, height));
    }

    static public void setHeightParamChalitKundli(int heightParamChalitKundli) {
        viewChalitKundli.setLayoutParams(new RelativeLayout.LayoutParams((int) cscreenConstants.DeviceScreenWidth, heightParamChalitKundli));
    }

    static public void setHeightParamBirthDetail(int heightParamChalitKundli) {
        birthDetailView.setLayoutParams(new RelativeLayout.LayoutParams((int) cscreenConstants.DeviceScreenWidth, heightParamChalitKundli));
    }

    static public void setHeightParamPanchangDetail(int heightParamChalitKundli) {
        panchangView.setLayoutParams(new RelativeLayout.LayoutParams((int) cscreenConstants.DeviceScreenWidth, heightParamChalitKundli));
    }

    static public void setParamsOfViewAstakVarga(int heightParamChalitKundli) {
        astakvargaView.setLayoutParams(new RelativeLayout.LayoutParams((int) cscreenConstants.DeviceScreenWidth, heightParamChalitKundli));
    }

    static public void setParamsOfViewKpHouseSignificators(int height) {
        viewKpHouseSignificators.setLayoutParams(new RelativeLayout.LayoutParams((int) cscreenConstants.DeviceScreenWidth, height));
    }

    static public void setParamsOfViewVarshphalTable(int height) {
        tajikVarshphalViewsPlant.setLayoutParams(new RelativeLayout.LayoutParams((int) cscreenConstants.DeviceScreenWidth, height));
    }

    public static void setMargin(View view, int left, int right, int top, int bottom) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)
                view.getLayoutParams();
        params.setMargins(left, top, right, bottom);
        view.setLayoutParams(params);
    }

}


