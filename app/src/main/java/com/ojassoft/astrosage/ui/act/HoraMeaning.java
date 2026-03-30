package com.ojassoft.astrosage.ui.act;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.utils.CGlobalVariables;

public class HoraMeaning extends DialogFragment implements View.OnClickListener {

    private TextView txvHoraPlanetMeaning, txvHoraIsAuspicious,
            txvHoraPlanetMeanings, txvHoraPlanetMeaningswiki,
            txvHoraPlanetMeaningssecond, txvHoraPlanetMeaningprahar,
            txvdoghatimuhurat;
    Button butSet;
    // public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    // public Typeface regularTypeface, mediumTypeface;
    Activity activity;
    String Horatag;
    String horaPlanet;
    String horaPlanetMeaning;

    String horaPlanetMeanings;
    String wiki;
    String second;
    String doghatimuhurat;

    public static HoraMeaning newInstance(String Horatag, String horaPlanet, String horaPlanetMeaning) {
        Bundle args = new Bundle();
        args.putString("Horatag", Horatag);
        args.putString("horaPlanet", horaPlanet);
        args.putString("horaPlanetMeaning", horaPlanetMeaning);
        HoraMeaning fragment = new HoraMeaning();
        fragment.setArguments(args);
        return fragment;
    }

    public static HoraMeaning newInstance(String horaPlanet, String horaPlanetMeaning, String horaPlanetMeanings, String Horatag, String second, String wiki, String doghatimuhurat) {
        Bundle args = new Bundle();
        args.putString("horaPlanet", horaPlanet);
        args.putString("horaPlanetMeaning", horaPlanetMeaning);
        args.putString("horaPlanetMeanings", horaPlanetMeanings);
        args.putString("Horatag", Horatag);
        args.putString("second", second);
        args.putString("wiki", wiki);
        args.putString("doghatimuhurat", doghatimuhurat);
        HoraMeaning fragment = new HoraMeaning();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Horatag = getArguments().getString("Horatag");
        horaPlanet = getArguments().getString("horaPlanet");
        horaPlanetMeaning = getArguments().getString("horaPlanetMeaning");
        horaPlanetMeanings = getArguments().getString("horaPlanetMeanings");
        wiki = getArguments().getString("wiki");
        second = getArguments().getString("second");
        doghatimuhurat = getArguments().getString("doghatimuhurat");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Dialog dialog = getDialog();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        View view = inflater.inflate(R.layout.layhorameaning, container);
        txvHoraPlanetMeaning = (TextView) view.findViewById(R.id.txvHoraPlanetMeaning);
        txvHoraPlanetMeanings = (TextView) view.findViewById(R.id.txvHoraPlanetMeanings);
        txvHoraPlanetMeaningswiki = (TextView) view.findViewById(R.id.txvHoraPlanetMeaningswiki);
        txvHoraPlanetMeaningssecond = (TextView) view.findViewById(R.id.txvHoraPlanetMeaningssecond);
        txvHoraPlanetMeaningprahar = (TextView) view.findViewById(R.id.txvHoraPlanetMeaningprahar);
        txvdoghatimuhurat = (TextView) view.findViewById(R.id.txvdoghatimuhurat);
        txvHoraIsAuspicious = (TextView) view.findViewById(R.id.txvHoraIsAuspicious);
//        txvHoraIsAuspicious1 = (TextView) view.findViewById(R.id.txvHoraIsAuspicious1);
        butSet = (Button) view.findViewById(R.id.butSet);
        butSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        setTypefaceOfView();
        int langCode = ((AstrosageKundliApplication) getActivity().getApplication()).getLanguageCode();
        if (Horatag.equalsIgnoreCase("Horatag")) {
            txvHoraIsAuspicious
                    .setText(getResources().getString(
                            R.string.hora_is_auspicious_for_heading));

            if (langCode == CGlobalVariables.ENGLISH) {
                txvHoraPlanetMeaning.setText(horaPlanet
                        + " "
                        + getResources().getString(
                        R.string.hora_is_auspicious_for) + " " + horaPlanetMeaning.toLowerCase());
            } else if (langCode == CGlobalVariables.HINDI) {
                txvHoraPlanetMeaning.setText(horaPlanetMeaning + " " + horaPlanet
                        + " "
                        + getResources().getString(
                        R.string.hora_is_auspicious_for));
            } else if (langCode == CGlobalVariables.TAMIL) {
                txvHoraPlanetMeaning.setText(horaPlanetMeaning + " " + horaPlanet
                        + " "
                        + getResources().getString(
                        R.string.hora_is_auspicious_for));
            } else if (langCode == CGlobalVariables.BANGALI) {
                txvHoraPlanetMeaning.setText(horaPlanetMeaning + " " + horaPlanet
                        + " "
                        + getResources().getString(
                        R.string.hora_is_auspicious_for));
            } else if (langCode == CGlobalVariables.MARATHI) {
                txvHoraPlanetMeaning.setText(horaPlanetMeaning + " " + horaPlanet
                        + " "
                        + getResources().getString(
                        R.string.hora_is_auspicious_for));
            }else if (langCode == CGlobalVariables.TELUGU) {
                txvHoraPlanetMeaning.setText(horaPlanetMeaning + " " + horaPlanet
                        + " "
                        + getResources().getString(
                        R.string.hora_is_auspicious_for));
            }else if (langCode == CGlobalVariables.KANNADA) {
                txvHoraPlanetMeaning.setText(horaPlanetMeaning + " " + horaPlanet
                        + " "
                        + getResources().getString(
                        R.string.hora_is_auspicious_for));
            }else if (langCode == CGlobalVariables.GUJARATI) {
                txvHoraPlanetMeaning.setText(horaPlanetMeaning + " " + horaPlanet
                        + " "
                        + getResources().getString(
                        R.string.hora_is_auspicious_for));
            }else if (langCode == CGlobalVariables.MALAYALAM) {
                txvHoraPlanetMeaning.setText(horaPlanetMeaning + " " + horaPlanet
                        + " "
                        + getResources().getString(
                        R.string.hora_is_auspicious_for));
            }


        } else if (Horatag.equalsIgnoreCase("doghati")) {
            txvHoraPlanetMeanings.setVisibility(View.VISIBLE);
            txvHoraPlanetMeaningswiki.setVisibility(View.VISIBLE);
            txvHoraPlanetMeaningprahar.setVisibility(View.VISIBLE);
            txvHoraPlanetMeaningssecond.setVisibility(View.VISIBLE);
            txvHoraPlanetMeaning.setText(getResources().getString(
                    R.string.podanic_muhurat)
                    + ": " + horaPlanetMeanings);

            txvHoraPlanetMeaningswiki.setText(getResources().getString(
                    R.string.yoga_doghati)
                    + ": " + wiki);
            txvHoraPlanetMeanings.setText(getResources().getString(
                    R.string.muhurat)
                    + ": " + second);
            txvHoraIsAuspicious.setText(

                    getResources().getString(R.string.doghati_is_auspicious_for_heading));
            txvHoraPlanetMeaningssecond.setText(getResources().getString(
                    R.string.nakshatra)
                    + ": " + horaPlanetMeaning);
            txvHoraPlanetMeaningprahar.setText(getResources().getString(
                    R.string.doghati_prahar)
                    + ": " + horaPlanet);
            if (doghatimuhurat == null
                    || doghatimuhurat == "" || doghatimuhurat.length() < 3) {
                txvdoghatimuhurat.setVisibility(View.GONE);
            } else {
                txvdoghatimuhurat.setVisibility(View.VISIBLE);
                txvdoghatimuhurat.setText(doghatimuhurat);


            }

        } else {

            txvHoraIsAuspicious
                    .setText(getResources().getString(
                            R.string.chogadia_is_auspicious_for_heading));
            if (langCode == CGlobalVariables.HINDI) {
                txvHoraPlanetMeaning.setText(horaPlanet + " " +
                        getResources().getString(
                                R.string.chogadia_is_auspicious_for) + " -- " + horaPlanetMeaning);
            } else if (langCode == CGlobalVariables.ENGLISH) {
                txvHoraPlanetMeaning.setText(horaPlanet + " " +
                        getResources().getString(
                                R.string.chogadia_is_auspicious_for) + " " + horaPlanetMeaning.toLowerCase());
            } else {
                txvHoraPlanetMeaning.setText(horaPlanet + " " +
                        getResources().getString(
                                R.string.chogadia_is_auspicious_for) + " " + horaPlanetMeaning);
            }



/*

            txvHoraIsAuspicious.setText(horaPlanet
                    + " "
                    + getResources().getString(
                    R.string.chogadia_is_auspicious_for));
            txvHoraPlanetMeaning.setText(horaPlanetMeaning);
*/

        }
        // txvHoraPlanetMeaning.setText(horaPlanetMeaning);
        return view;
    }

    private void setTypefaceOfView() {

        if (((AstrosageKundliApplication) getActivity().getApplication()).getLanguageCode() != CGlobalVariables.HINDI) {
            //txvHoraIsAuspicious.setTypeface(((BaseInputActivity) activity).mediumTypeface);
//            txvHoraIsAuspicious1.setTypeface(((BaseInputActivity) activity).mediumTypeface);
            txvHoraPlanetMeaning.setTypeface(((BaseInputActivity) activity).regularTypeface);
            txvHoraPlanetMeanings.setTypeface(((BaseInputActivity) activity).regularTypeface);
            txvHoraPlanetMeaningswiki.setTypeface(((BaseInputActivity) activity).regularTypeface);
            txvHoraPlanetMeaningssecond.setTypeface(((BaseInputActivity) activity).regularTypeface);
            txvHoraPlanetMeaningprahar.setTypeface(((BaseInputActivity) activity).regularTypeface);
            txvdoghatimuhurat.setTypeface(((BaseInputActivity) activity).regularTypeface);

        }
        butSet.setTypeface(((BaseInputActivity) activity).mediumTypeface);

    }

    public void goToFinish(View v) {
        activity.finish();
    }

    @Override
    public void onClick(View v) {
        goToFinish(v);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        Dialog dialog = getDialog();
        if (dialog != null) {
            WindowManager.LayoutParams wmlp = dialog.getWindow()
                    .getAttributes();
            wmlp.width = (int) width - 40;
            wmlp.height = android.view.WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(wmlp);
            // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }


}
