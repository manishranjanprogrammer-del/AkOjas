package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanDasa;
import com.ojassoft.astrosage.beans.PlanetData;
import com.ojassoft.astrosage.controller.ControllerManager;
import com.ojassoft.astrosage.customadapters.ListAdapterDasa;
import com.ojassoft.astrosage.customexceptions.UIDasaVimsottriException;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.OutputMasterActivity;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

/**
 * This fragment is used to show Vimsottri Dasa
 *
 * @author Bijendra
 * @date 03-dec-13
 * @copyright Ojas softech Pvt. Ltd
 */

public class FragDasa extends Fragment {
    Activity activity;
    private final String FORWARD_SLASH = "/";
    private final String $_PARSER = "$";

    /* Local Variables */
    private String VimPlanet[] = null;
    //private Typeface _typeFace;
    ListView dasaList = null;
    String[] _dasaLevelTitles = null;
    private ListAdapterDasa listAdapterDasa = null;
    IDasaCallbacks _iDasaCallbacks;
    private String arrVimDasaLevel[] = null;
    // private int iFLPositionClicked = -1;
    private int iVimFirstLevelClicked = -1;
    private int iVimSecondLevelClicked = -1;
    private int iVimThirdLevelClicked = -1;
    private int iVimFourthLevelClicked = -1;

    private double _dDob = 0;
    private double lastEndDate;
    private int level = 0;
    private BeanDasa[] cVimMahaDasa = null;
    private BeanDasa[] cPratyanterDasa = null;
    private BeanDasa[] cVimsottriDasaLevel3 = null;
    private BeanDasa[] cVimsottriDasaLevel4 = null;
    private BeanDasa[] cVimsottriDasaLevel5 = null;

    private String strVimFirstLevelShow[] = new String[9];
    private String strVimSecondLevelShow[] = new String[9];
    private String strVimThirdLevelShow[] = new String[9];
    private String strVimForthLevelShow[] = new String[9];
    private String strVimFifthLevelShow[] = new String[9];

    private int _year;
    private int _month;
    private int _day;
    PlanetData _planetData;
    String _blankSpace = "                                         ";
    private boolean _isBack = false;
    // private int iDasaLabel = 0;

    TextView _tvDasaTitle;
    Button backDasaButton;
    int _languageCode = CGlobalVariables.ENGLISH;

    public FragDasa() {

    }


    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        this.activity = activity;
        _iDasaCallbacks = (OutputMasterActivity) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
        _iDasaCallbacks = null;
    }

    /*
         * This function is used to init variables
         */
    private void initAllViews() {
        cVimMahaDasa = null;
        cPratyanterDasa = null;
        cVimsottriDasaLevel3 = null;
        cVimsottriDasaLevel4 = null;
        cVimsottriDasaLevel5 = null;
        level = 0;
        _dDob = _year + _month / 12.00 + _day / 365.00;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            int LANGUAGE_CODE = CUtils.getLanguageCodeFromPreference(getActivity());
            CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);
        }catch (Exception e){
            //
        }
        View view = inflater.inflate(R.layout.lay_dasa, container, false);


        arrVimDasaLevel = getActivity().getResources().getStringArray(R.array.Vimshottari_dasa_heading_list);
        VimPlanet = getActivity().getResources().getStringArray(R.array.Vimshottari_dasa_planets_list);
        _year = CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getDateTime().getYear();
        _month = CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getDateTime().getMonth();
        _day = CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getDateTime().getDay();
        //_languageCode = CUtils.getLanguageCodeFromPreference(getActivity().getApplicationContext());
        _languageCode = ((AstrosageKundliApplication) getActivity().getApplication()).getLanguageCode();//ADDED BY HEVENDRA ON 24-12-2014
        //_typeFace = CUtils.getUserSelectedLanguageFontType(getActivity(), _languageCode);
        //tvNote.setTypeface((((OutputMasterActivity) activity).regularTypeface), Typeface.BOLD);
        _planetData = CGlobal.getCGlobalObject().getPlanetDataObject();

        initAllViews();
        _tvDasaTitle = (TextView) view.findViewById(R.id.tvDasaTitle);
        View footer = getActivity().getLayoutInflater().inflate(R.layout.dasha_list_footer_lay, null, false);

        backDasaButton = (Button) footer.findViewById(R.id.butDasaLevel);
        if (((AstrosageKundliApplication) activity.getApplication())
                .getLanguageCode() == CGlobalVariables.ENGLISH) {
            backDasaButton.setText(getResources().getString(R.string.back_button).toUpperCase());
        }
        backDasaButton.setTypeface((((OutputMasterActivity) activity).regularTypeface), Typeface.BOLD);
        backDasaButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                backDashaLevel();
            }
        });

        dasaList = (ListView) view.findViewById(R.id.lstDasa);
        dasaList.addFooterView(footer, null, false);
        TextView tvNote = (TextView) view.findViewById(R.id.tvNote);
        tvNote.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        dasaList.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                {
                    _iDasaCallbacks.setLavelBoolean(true);
                    if (!_isBack) {
                        level += 1;

                        if (level <= 4)
                            showDasaLevels(position);

                        if (level > 4)
                            level = 4;
                    }

                }

            }
        });
        showDasaLevels(level);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    // DASA LEVEL FUNCTIONS

    /**
     * This function is used to show back dasa
     */
    private void backDashaLevel() {
        level -= 1;
        if (level < 0)
            level = 0;

        switch (level + 1) {
            case 1:
                backVimsottriDashaLevel1();
                break;
            case 2:
                backVimsottriDashaLevel2();
                break;
            case 3:
                backVimsottriDashaLevel3();
                break;
            case 4:
                backVimsottriDashaLevel4();
                break;

        }
        if (level > 0) {
            backDasaButton.setVisibility(View.VISIBLE);
        } else {
            backDasaButton.setVisibility(View.GONE);
            _iDasaCallbacks.setLavelBoolean(false);
        }
        showDasaLevelTitle(level);

        _isBack = false;
    }


    /**
     * This function is used to show different dasa levels according to selected
     * in list
     *
     * @param position
     * @author Bijendra
     * @date 5-Nov-2012
     */
    private void showDasaLevels(int position) {

        dasaList.setVisibility(View.INVISIBLE);
        showDasaLevelTitle(level);

        switch (level) {
            case 0:

                showVimsottriMahaDasa();

                break;
            case 1:
                iVimFirstLevelClicked = position;
                showVimsottriPratyanterDasa(position);

                break;
            case 2:
                iVimSecondLevelClicked = position;
                showVimsottriDashaLevel3(position);

                break;
            case 3:
                iVimThirdLevelClicked = position;
                showVimsottriDashaLevel4(position);

                break;
            case 4:
                iVimFourthLevelClicked = position;
                showVimsottriDashaLevel5(position);

                break;

        }
        dasaList.setVisibility(View.VISIBLE);

        if (level > 0)
            backDasaButton.setVisibility(View.VISIBLE);
        else
            backDasaButton.setVisibility(View.GONE);

    }

    /**
     * This function is used to show maha dasa
     */
    private void showVimsottriMahaDasa() {
        try {
            cVimMahaDasa = new com.ojassoft.astrosage.controller.ControllerManager()
                    .vimsottriMahaDasha(_dDob, lastEndDate, CGlobal
                            .getCGlobalObject().getPlanetDataObject());

            for (int inxKey = 0; inxKey < 9; inxKey++) {
                strVimFirstLevelShow[inxKey] = VimPlanet[cVimMahaDasa[inxKey]
                        .getPlanetNo()]
                        + $_PARSER
                        + CUtils.doubleToStringDateDDMMYY(
                        cVimMahaDasa[inxKey].getDasaTime(),
                        FORWARD_SLASH/*CUtils.getForwardSlash(_languageCode)*/);
                //+ _blankSpace;


            }

            listAdapterDasa = new ListAdapterDasa(getActivity(),
                    android.R.layout.simple_list_item_1, strVimFirstLevelShow,
                    ((OutputMasterActivity) activity).regularTypeface);
            dasaList.setAdapter(listAdapterDasa);

            // iDasaLabel += 1;
        } catch (UIDasaVimsottriException ce) {
            Toast.makeText(getActivity(), ce.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }

    }

    /**
     * This function is used to show Pratyanter das
     *
     * @param pos
     */
    private void showVimsottriPratyanterDasa(int pos) {
        try {
            cPratyanterDasa = new ControllerManager().vimOtherLevelDasha(_dDob,
                    lastEndDate, pos, cVimMahaDasa);
            for (int inxKey = 0; inxKey < 9; inxKey++) {

                strVimSecondLevelShow[inxKey] = VimPlanet[cVimMahaDasa[iVimFirstLevelClicked]
                        .getPlanetNo()]
                        + CUtils.getForwardSlash(_languageCode)
                        + VimPlanet[cPratyanterDasa[inxKey].getPlanetNo()]
                        + $_PARSER
                        + CUtils.doubleToStringDateDDMMYY(
                        cPratyanterDasa[inxKey].getDasaTime(),
                        FORWARD_SLASH/*CUtils.getForwardSlash(_languageCode)*/);
                //+ _blankSpace;

            }

            listAdapterDasa = new ListAdapterDasa(getActivity(),
                    android.R.layout.simple_list_item_1, strVimSecondLevelShow,
                    ((OutputMasterActivity) activity).regularTypeface);
            dasaList.setAdapter(listAdapterDasa);
        } catch (UIDasaVimsottriException ce) {
            Toast.makeText(getActivity(), ce.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }

    }

    /**
     * This function is sued to show dasa lelev 3
     *
     * @param pos
     */
    private void showVimsottriDashaLevel3(int pos) {
        String strTemp = null;

        try {
            cVimsottriDasaLevel3 = new ControllerManager()
                    .getNewVimOtherLevelDasa(_dDob, lastEndDate, pos,
                            cPratyanterDasa,
                            cVimMahaDasa[iVimFirstLevelClicked].getDasaTime());
            strTemp = VimPlanet[cVimMahaDasa[iVimFirstLevelClicked]
                    .getPlanetNo()]
                    + CUtils.getForwardSlash(_languageCode)
                    + VimPlanet[cPratyanterDasa[iVimSecondLevelClicked]
                    .getPlanetNo()]
                    + CUtils.getForwardSlash(_languageCode);
            for (int inxKey = 0; inxKey < 9; inxKey++) {
                strVimThirdLevelShow[inxKey] = strTemp
                        + VimPlanet[cVimsottriDasaLevel3[inxKey].getPlanetNo()]
                        + $_PARSER
                        + CUtils.doubleToStringDateDDMMYY(
                        cVimsottriDasaLevel3[inxKey].getDasaTime(),
                        FORWARD_SLASH/*CUtils.getForwardSlash(_languageCode)*/);
                //+ _blankSpace;
            }
            listAdapterDasa = new ListAdapterDasa(getActivity(),
                    android.R.layout.simple_list_item_1, strVimThirdLevelShow,
                    ((OutputMasterActivity) activity).regularTypeface);

            dasaList.setAdapter(listAdapterDasa);
        } catch (UIDasaVimsottriException ce) {
            Toast.makeText(getActivity(), ce.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }

    }

    /**
     * This function is used to show dasa level 4
     *
     * @param pos
     */
    private void showVimsottriDashaLevel4(int pos) {
        String strTemp = null;
        double lastTime = 0.0;
        try {
            // ADDED BY BIJENDRA ON 31-AUG-13
            if (pos == 0) {
                if (iVimSecondLevelClicked > 0)
                    lastTime = cPratyanterDasa[iVimSecondLevelClicked - 1]
                            .getDasaTime();
                else
                    lastTime = cVimMahaDasa[iVimFirstLevelClicked]
                            .getDasaTime();

            } else
                lastTime = cVimsottriDasaLevel3[pos - 1].getDasaTime();

            cVimsottriDasaLevel4 = new ControllerManager()
                    .getNewVimOtherLevelDasa(_dDob, lastEndDate, pos,
                            cVimsottriDasaLevel3, lastTime);

            // END
            strTemp = VimPlanet[cVimMahaDasa[iVimFirstLevelClicked]
                    .getPlanetNo()]
                    + CUtils.getForwardSlash(_languageCode)
                    + VimPlanet[cPratyanterDasa[iVimSecondLevelClicked]
                    .getPlanetNo()]
                    + CUtils.getForwardSlash(_languageCode)
                    + VimPlanet[cVimsottriDasaLevel3[iVimThirdLevelClicked]
                    .getPlanetNo()]
                    + CUtils.getForwardSlash(_languageCode);
            for (int inxKey = 0; inxKey < 9; inxKey++) {
                strVimForthLevelShow[inxKey] = strTemp
                        + VimPlanet[cVimsottriDasaLevel4[inxKey].getPlanetNo()]
                        + $_PARSER
                        + CUtils.doubleToStringDateDDMMYY(
                        cVimsottriDasaLevel4[inxKey].getDasaTime(),
                        FORWARD_SLASH/*CUtils.getForwardSlash(_languageCode)*/);
                //+ _blankSpace;
            }
            listAdapterDasa = new ListAdapterDasa(getActivity(),
                    android.R.layout.simple_list_item_1, strVimForthLevelShow,
                    ((OutputMasterActivity) activity).regularTypeface);

            dasaList.setAdapter(listAdapterDasa);
        } catch (UIDasaVimsottriException ce) {
            Toast.makeText(getActivity(), ce.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }

    }

    /**
     * Thsi function is used to show dasa level 5
     *
     * @param pos
     */
    private void showVimsottriDashaLevel5(int pos) {
        String strTemp = null;
        double lastTime = 0.0;

        try {
            // ADDED BY BIJENDRA ON 31-AUG-13
            if (pos == 0) {
                if (iVimThirdLevelClicked > 0) {
                    lastTime = cVimsottriDasaLevel3[iVimThirdLevelClicked - 1]
                            .getDasaTime();

                }

                if (iVimThirdLevelClicked == 0) {
                    if (iVimSecondLevelClicked > 0)
                        lastTime = cPratyanterDasa[iVimSecondLevelClicked - 1]
                                .getDasaTime();
                    else
                        lastTime = cVimMahaDasa[iVimFirstLevelClicked]
                                .getDasaTime();
                }

            } else
                lastTime = cVimsottriDasaLevel4[pos - 1].getDasaTime();

            cVimsottriDasaLevel5 = new ControllerManager()
                    .getNewVimOtherLevelDasa(_dDob, lastEndDate, pos,
                            cVimsottriDasaLevel4, lastTime);

            // END
            strTemp = VimPlanet[cVimMahaDasa[iVimFirstLevelClicked]
                    .getPlanetNo()]
                    + CUtils.getForwardSlash(_languageCode)
                    + VimPlanet[cPratyanterDasa[iVimSecondLevelClicked]
                    .getPlanetNo()]
                    + CUtils.getForwardSlash(_languageCode)
                    + VimPlanet[cVimsottriDasaLevel3[iVimThirdLevelClicked]
                    .getPlanetNo()]
                    + CUtils.getForwardSlash(_languageCode)
                    + VimPlanet[cVimsottriDasaLevel4[iVimFourthLevelClicked]
                    .getPlanetNo()]
                    + CUtils.getForwardSlash(_languageCode);
            for (int inxKey = 0; inxKey < 9; inxKey++) {
                strVimFifthLevelShow[inxKey] = strTemp
                        + VimPlanet[cVimsottriDasaLevel5[inxKey].getPlanetNo()]
                        + $_PARSER
                        + CUtils.doubleToStringDateDDMMYY(
                        cVimsottriDasaLevel5[inxKey].getDasaTime(),
                        FORWARD_SLASH/*CUtils.getForwardSlash(_languageCode)*/);
                //+ _blankSpace;
            }

            listAdapterDasa = new ListAdapterDasa(getActivity(),
                    android.R.layout.simple_list_item_1, strVimFifthLevelShow,
                    ((OutputMasterActivity) activity).regularTypeface);
            dasaList.setAdapter(listAdapterDasa);
        } catch (UIDasaVimsottriException ce) {
            Toast.makeText(getActivity(), ce.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }

    }

    /**
     * This function is used to show dasa level 1 on clicking back button
     */
    private void backVimsottriDashaLevel1() {

        listAdapterDasa = new ListAdapterDasa(getActivity(),
                android.R.layout.simple_list_item_1, strVimFirstLevelShow,
                ((OutputMasterActivity) activity).regularTypeface);
        dasaList.setAdapter(listAdapterDasa);

    }

    /**
     * This function is used to show dasa level 2 on clicking back button
     */
    private void backVimsottriDashaLevel2() {
        listAdapterDasa = new ListAdapterDasa(getActivity(),
                android.R.layout.simple_list_item_1, strVimSecondLevelShow,
                ((OutputMasterActivity) activity).regularTypeface);
        dasaList.setAdapter(listAdapterDasa);
    }

    /**
     * This function is used to show dasa level 3 on clicking back button
     */
    private void backVimsottriDashaLevel3() {
        listAdapterDasa = new ListAdapterDasa(getActivity(),
                android.R.layout.simple_list_item_1, strVimThirdLevelShow,
                ((OutputMasterActivity) activity).regularTypeface);
        dasaList.setAdapter(listAdapterDasa);
    }

    /**
     * This function is used to show dasa level 4 on clicking back button
     */
    private void backVimsottriDashaLevel4() {
        listAdapterDasa = new ListAdapterDasa(getActivity(),
                android.R.layout.simple_list_item_1, strVimForthLevelShow,
                ((OutputMasterActivity) activity).regularTypeface);
        dasaList.setAdapter(listAdapterDasa);

    }

    /**
     * This function is used to show dasa level title
     *
     * @param levelIndex
     */
    private void showDasaLevelTitle(int levelIndex) {
        _tvDasaTitle.setText(arrVimDasaLevel[levelIndex]);
        _tvDasaTitle.setTypeface(((OutputMasterActivity) activity).mediumTypeface);
    }

    public void backButtonPressedForLavelUp() {
        backDashaLevel();
    }

    public static interface IDasaCallbacks {
        public void setLavelBoolean(boolean b);
    }
}
