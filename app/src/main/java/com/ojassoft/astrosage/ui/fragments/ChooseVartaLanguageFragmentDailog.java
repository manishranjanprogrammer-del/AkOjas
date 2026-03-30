package com.ojassoft.astrosage.ui.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.constants.AppConstants;
import com.ojassoft.astrosage.customadapters.ChooseVartaLanguageAdapter;
import com.ojassoft.astrosage.misc.FontUtils;
import com.ojassoft.astrosage.model.LanguageModel;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.ListingCreationActivity;
import com.ojassoft.astrosage.ui.act.VartaReqJoinActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;

import java.util.ArrayList;
import java.util.List;


public class ChooseVartaLanguageFragmentDailog extends AstroParentFrag {

    private TextView textViewHeading;
    private RecyclerView recyclerView;
    private List<LanguageModel> languageModels;
    private ChooseVartaLanguageAdapter languageAdapter;
    private Button butChooseLanguageOk;
    private Button butChooseLanguageCancel;
    private String sectedLanguages;

    private Context context;

    public ChooseVartaLanguageFragmentDailog() {
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
        View view = inflater.inflate(R.layout.choose_varta_language_dialog, container);

        Bundle bundle = getArguments();
        if (bundle != null) {
            sectedLanguages = bundle.getString(CGlobalVariables.KEY_LANGUAGES);
        }
        textViewHeading = view.findViewById(R.id.textViewHeading);
        butChooseLanguageOk = view.findViewById(R.id.butChooseLanguageOk);
        butChooseLanguageCancel = view.findViewById(R.id.butChooseLanguageCancel);

        FontUtils.changeFont(getActivity(), butChooseLanguageOk, AppConstants.FONT_ROBOTO_MEDIUM);
        FontUtils.changeFont(getActivity(), butChooseLanguageCancel, AppConstants.FONT_ROBOTO_MEDIUM);
        FontUtils.changeFont(getActivity(), textViewHeading, AppConstants.FONT_ROBOTO_MEDIUM);

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
                ChooseVartaLanguageFragmentDailog.this.dismiss();
            }
        });
        return view;
    }


    private void goToChooseLanguageOK() {
        String sectedLanguages = "";
        String sectedLanguagesToDisplay = "";
        if (languageModels != null) {
            for (int i = 0; i < languageModels.size(); i++) {
                LanguageModel languageModel = languageModels.get(i);
                if (languageModel == null) continue;
                if (languageModel.isSelected()) {
                    sectedLanguages = sectedLanguages + languageModel.getLanguageCode() + ",";
                    sectedLanguagesToDisplay = sectedLanguagesToDisplay + languageModel.getLanguageName() + ", ";
                }
            }
        }
        if (TextUtils.isEmpty(sectedLanguages)) {
            ((BaseInputActivity) context).showSnackbar(recyclerView, context.getString(R.string.msg_choose_lang_title));
            return;
        }
        sectedLanguages = sectedLanguages.substring(0, sectedLanguages.length() - 1);
        sectedLanguagesToDisplay = sectedLanguagesToDisplay.substring(0, sectedLanguagesToDisplay.length() - 2);
        if (getActivity() instanceof VartaReqJoinActivity) {
            ((VartaReqJoinActivity) getActivity()).setSelectedLanguage(sectedLanguages, sectedLanguagesToDisplay);
        } else if (getActivity() instanceof ListingCreationActivity) {
            ((ListingCreationActivity) getActivity()).setSelectedLanguage(sectedLanguages, sectedLanguagesToDisplay);
        }

        this.dismiss();
    }

    private void getLanguageList() {
        languageModels = getLanguageModelList();

        if (TextUtils.isEmpty(sectedLanguages)) return;
        String[] langArr = sectedLanguages.split(",");
        if (langArr == null || langArr.length == 0) return;

        for (int i = 0; i < langArr.length; i++) {
            String selectedLang = langArr[i];
            for (int j = 0; j < languageModels.size(); j++) {
                LanguageModel languageModel = languageModels.get(j);
                if (languageModel.getLanguageCode().equals(selectedLang)) {
                    languageModel.setSelected(true);
                    break;
                }
            }
        }
    }

    private List<LanguageModel> getLanguageModelList() {

        List<LanguageModel> languageModelList = new ArrayList<>();

        LanguageModel languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.LANG_HINDI);
        languageModel.setLanguageCode(CGlobalVariables.LANG_HINDI);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.LANG_ENGLISH);
        languageModel.setLanguageCode(CGlobalVariables.LANG_ENGLISH);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.LANG_BANGALI);
        languageModel.setLanguageCode(CGlobalVariables.LANG_BANGALI);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.LANG_MARATHI);
        languageModel.setLanguageCode(CGlobalVariables.LANG_MARATHI);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.LANG_TELUGU);
        languageModel.setLanguageCode(CGlobalVariables.LANG_TELUGU);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.LANG_TAMIL);
        languageModel.setLanguageCode(CGlobalVariables.LANG_TAMIL);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.LANG_GUJARATI);
        languageModel.setLanguageCode(CGlobalVariables.LANG_GUJARATI);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.LANG_KANNADA);
        languageModel.setLanguageCode(CGlobalVariables.LANG_KANNADA);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.LANG_MALAYALAM);
        languageModel.setLanguageCode(CGlobalVariables.LANG_MALAYALAM);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.LANG_ASSAMESE);
        languageModel.setLanguageCode(CGlobalVariables.LANG_ASSAMESE);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.LANG_ODIA);
        languageModel.setLanguageCode(CGlobalVariables.LANG_ODIA);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.LANG_PUNJABI);
        languageModel.setLanguageCode(CGlobalVariables.LANG_PUNJABI);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.LANG_URDU);
        languageModel.setLanguageCode(CGlobalVariables.LANG_URDU);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.LANG_BHOJPURI);
        languageModel.setLanguageCode(CGlobalVariables.LANG_BHOJPURI);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.LANG_NEPALI);
        languageModel.setLanguageCode(CGlobalVariables.LANG_NEPALI);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.LANG_MAITHILI);
        languageModel.setLanguageCode(CGlobalVariables.LANG_MAITHILI);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.LANG_DOGRI);
        languageModel.setLanguageCode(CGlobalVariables.LANG_DOGRI);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.LANG_KASHMIRI);
        languageModel.setLanguageCode(CGlobalVariables.LANG_KASHMIRI);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.LANG_KONKANI);
        languageModel.setLanguageCode(CGlobalVariables.LANG_KONKANI);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.LANG_SINDHI);
        languageModel.setLanguageCode(CGlobalVariables.LANG_SINDHI);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.LANG_HARYANVI);
        languageModel.setLanguageCode(CGlobalVariables.LANG_HARYANVI);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.LANG_RAJASTHANI);
        languageModel.setLanguageCode(CGlobalVariables.LANG_RAJASTHANI);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.LANG_MANIPURI);
        languageModel.setLanguageCode(CGlobalVariables.LANG_MANIPURI);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.LANG_SANSKRI);
        languageModel.setLanguageCode(CGlobalVariables.LANG_SANSKRI);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.LANG_KUMAONI);
        languageModel.setLanguageCode(CGlobalVariables.LANG_KUMAONI);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.LANG_TULU);
        languageModel.setLanguageCode(CGlobalVariables.LANG_TULU);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.LANG_SANTALI);
        languageModel.setLanguageCode(CGlobalVariables.LANG_SANTALI);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.LANG_SPANISH);
        languageModel.setLanguageCode(CGlobalVariables.LANG_SPANISH);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.LANG_FRENCH);
        languageModel.setLanguageCode(CGlobalVariables.LANG_FRENCH);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.LANG_ARABIC);
        languageModel.setLanguageCode(CGlobalVariables.LANG_ARABIC);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.LANG_CHINESE);
        languageModel.setLanguageCode(CGlobalVariables.LANG_CHINESE);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.LANG_RUSSIAN);
        languageModel.setLanguageCode(CGlobalVariables.LANG_RUSSIAN);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.LANG_PORTUGUESE);
        languageModel.setLanguageCode(CGlobalVariables.LANG_PORTUGUESE);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.LANG_INDONESIAN);
        languageModel.setLanguageCode(CGlobalVariables.LANG_INDONESIAN);
        languageModelList.add(languageModel);

        languageModel = new LanguageModel();
        languageModel.setLanguageName(CGlobalVariables.LANG_JAPANESE);
        languageModel.setLanguageCode(CGlobalVariables.LANG_JAPANESE);
        languageModelList.add(languageModel);

        return languageModelList;
    }
}
