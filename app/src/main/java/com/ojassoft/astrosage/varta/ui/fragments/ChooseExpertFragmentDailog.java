package com.ojassoft.astrosage.varta.ui.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.adapters.ChooseVartaLanguageAdapter;
import com.ojassoft.astrosage.varta.model.LanguageModel;
import com.ojassoft.astrosage.varta.ui.activity.VartaReqJoinActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;

import java.util.ArrayList;
import java.util.List;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;
import static com.ojassoft.astrosage.varta.utils.CUtils.showSnackbar;


public class ChooseExpertFragmentDailog extends AstroParentFrag {

    private TextView textViewHeading;
    private RecyclerView recyclerView;
    private List<LanguageModel> languageModels;
    private ChooseVartaLanguageAdapter languageAdapter;
    private Button butChooseLanguageOk;
    private Button butChooseLanguageCancel;
    private String sectedExperts;

    private Context context;

    public ChooseExpertFragmentDailog() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final Dialog dialog = getDialog();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.color.bg_transparent);
        LANGUAGE_CODE = AstrosageKundliApplication.getAppContext().getLanguageCode();
        CUtils.getRobotoFont(context, LANGUAGE_CODE, CGlobalVariables.regular);
        View view = inflater.inflate(R.layout.choose_varta_language_dialog, container);

        Bundle bundle = getArguments();
        if (bundle != null) {
            sectedExperts = bundle.getString(CGlobalVariables.KEY_EXPERTS);
        }
        textViewHeading = view.findViewById(R.id.textViewHeading);
        butChooseLanguageOk = view.findViewById(R.id.butChooseLanguageOk);
        butChooseLanguageCancel = view.findViewById(R.id.butChooseLanguageCancel);

        FontUtils.changeFont(getActivity(), butChooseLanguageOk, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(getActivity(), butChooseLanguageCancel, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(getActivity(), textViewHeading, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        textViewHeading.setText(getString(R.string.system_known));

        getLanguageList();
        recyclerView = view.findViewById(R.id.recyclerView);
        languageAdapter = new ChooseVartaLanguageAdapter(context, languageModels);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(languageAdapter);

        butChooseLanguageOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goToChooseLanguageOK();
            }
        });
        butChooseLanguageCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ChooseExpertFragmentDailog.this.dismiss();
            }
        });
        return view;
    }


    private void goToChooseLanguageOK() {
        String sectedExperts = "";
        String sectedExpertssToDisplay = "";
        if (languageModels != null) {
            for (int i = 0; i < languageModels.size(); i++) {
                LanguageModel languageModel = languageModels.get(i);
                if (languageModel == null) continue;
                if (languageModel.isSelected()) {
                    sectedExperts = sectedExperts + languageModel.getLanguageName() + ",";
                    sectedExpertssToDisplay = sectedExpertssToDisplay + languageModel.getLanguageName() + ", ";
                }
            }
        }
        if (TextUtils.isEmpty(sectedExperts)) {
            showSnackbar(recyclerView, context.getString(R.string.msg_choose_lang_title),getActivity());
            return;
        }
        sectedExperts = sectedExperts.substring(0, sectedExperts.length() - 1);
        sectedExpertssToDisplay = sectedExpertssToDisplay.substring(0, sectedExpertssToDisplay.length() - 2);
        ((VartaReqJoinActivity) getActivity()).setSelectedExperts(sectedExperts, sectedExpertssToDisplay);
        this.dismiss();
    }

    private void getLanguageList() {
        languageModels = getLanguageModelList();

        if (TextUtils.isEmpty(sectedExperts)) return;
        String[] langArr = sectedExperts.split(",");
        if (langArr == null || langArr.length == 0) return;

        for (int i = 0; i < langArr.length; i++) {
            String selectedLang = langArr[i];
            for (int j = 0; j < languageModels.size(); j++) {
                LanguageModel languageModel = languageModels.get(j);
                if (languageModel.getLanguageName().equals(selectedLang)) {
                    languageModel.setSelected(true);
                    break;
                }
            }
        }
    }

    private List<LanguageModel> getLanguageModelList() {

        List<LanguageModel> languageModelList = new ArrayList<>();

        LanguageModel languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.EXPERT_VEDIC);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.EXPERT_KP_SYSTEM);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.EXPERT_LAL_KITAB);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.EXPERT_VASTU);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.EXPERT_TAROT_READING);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.EXPERT_NADI);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.EXPERT_NUMEROLOGY);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.EXPERT_ASHTAKVARGA);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.EXPERT_PALMISTRY);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.EXPERT_RAMAL);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.EXPERT_JAIMINI);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.EXPERT_TAJIK);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.EXPERT_WESTERN);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.EXPERT_KERALA);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.EXPERT_SWAR_SHASTRA);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.EXPERT_REIKI);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.EXPERT_CRYSTAL_HEALING);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.EXPERT_ANGEL_READING);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.EXPERT_FENG_SHUI);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.EXPERT_PRASHNA);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.EXPERT_PENDULUM_DOWSING);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.EXPERT_PSYCHIC_READING);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.EXPERT_FACE_READING);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.EXPERT_MUHURTA);
        languageModelList.add(languageModel);

        return languageModelList;
    }
}
