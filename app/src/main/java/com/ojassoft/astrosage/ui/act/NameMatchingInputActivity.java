package com.ojassoft.astrosage.ui.act;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.customadapters.NameSwarSpinnerAdapter;
import com.ojassoft.astrosage.model.NameSwarModel;
import com.ojassoft.astrosage.ui.act.matching.HomeMatchMakingInputScreen;
import com.ojassoft.astrosage.ui.fragments.matching.NameMatchingDialog;
import com.ojassoft.astrosage.utils.CUtils;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_BOY_SWAR;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_GIRL_NAME;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_GIRL_SWAR;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_MATCHING_DATA;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_NAME;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_RESULT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_SWAR_LIST;
import static com.ojassoft.astrosage.utils.CGlobalVariables.NAME_MATCHING_RESULT_NOT_FOUND;

public class NameMatchingInputActivity extends NameMatchingBaseActivity {

    Activity currentActivity;
    Context context;
    private Toolbar tool_barAppModule;
    private TabLayout tabLayout;
    private TextView tvTitle;
    private EditText etUserNamePerson1, etUserNamePerson2;
    private TextInputLayout boyInputLayout, girlInputLayout;
    private Spinner boySwarSpinner, girlSwarSpinner;
    private Button buttonSubmit;
    private Button buttonSubmitSwar;
    private LinearLayout inputLL;
    private LinearLayout spinnerLL;
    private List<NameSwarModel> nameSwarModelList;

    public NameMatchingInputActivity() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_name_matching_input);
        initViews();
        initListener();
    }

    private void initViews() {
        nameSwarModelList = new ArrayList<>();
        tool_barAppModule = findViewById(R.id.tool_barAppModule);
        setSupportActionBar(tool_barAppModule);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvTitle = findViewById(R.id.tvTitle);
        boySwarSpinner = findViewById(R.id.boy_swar_spinner);
        girlSwarSpinner = findViewById(R.id.girl_swar_spinner);
        etUserNamePerson1 = findViewById(R.id.etUserNamePerson1);
        etUserNamePerson2 = findViewById(R.id.etUserNamePerson2);
        boyInputLayout = findViewById(R.id.input_layout_boy_name);
        girlInputLayout = findViewById(R.id.input_layout_girl_name);
        inputLL = findViewById(R.id.inputLL);
        spinnerLL = findViewById(R.id.spinnerLL);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        tvTitle.setText(getString(R.string.title_name_matching_input));
        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonSubmitSwar = findViewById(R.id.buttonSubmitSwar);
        tvTitle.setTypeface(regularTypeface);
        etUserNamePerson1.addTextChangedListener(new MyTextWatcher(etUserNamePerson1));
        etUserNamePerson2.addTextChangedListener(new MyTextWatcher(etUserNamePerson2));
    }

    @Override
    protected void initContext() {
        currentActivity = NameMatchingInputActivity.this;
        context = NameMatchingInputActivity.this;
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

    private void initListener() {
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CUtils.hideMyKeyboard(currentActivity);
                if (submitForm()) {
                    calculateValues();
                }
            }
        });
        buttonSubmitSwar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CUtils.hideMyKeyboard(currentActivity);
                nameBoySwarModel = (NameSwarModel) boySwarSpinner.getSelectedItem();
                nameGirlSwarModel = (NameSwarModel) girlSwarSpinner.getSelectedItem();
                getMatchingDetails(nameBoySwarModel.getSwar(), nameGirlSwarModel.getSwar());

            }
        });
    }

    public void initBoySwarSpinner() {
        inputLL.setVisibility(View.GONE);
        spinnerLL.setVisibility(View.VISIBLE);

        NameSwarSpinnerAdapter boySwaradapter = new NameSwarSpinnerAdapter(currentActivity, R.layout.items_swar_spinner, nameSwarModelList);
        boySwarSpinner.setAdapter(boySwaradapter);

        NameSwarSpinnerAdapter girlSwaradapter = new NameSwarSpinnerAdapter(currentActivity, R.layout.items_swar_spinner, nameSwarModelList);
        girlSwarSpinner.setAdapter(girlSwaradapter);

    }

    private void calculateValues() {
        getMatchingDetails(etUserNamePerson1.getText().toString(), etUserNamePerson2.getText().toString());

    }

    private boolean submitForm() {
        boolean result = false;
        if (validateName(etUserNamePerson1, boyInputLayout, getString(R.string.please_enter_boy_name_v1))
                && validateName(etUserNamePerson2, girlInputLayout, getString(R.string.please_enter_girl_name_v1))) {
            result = true;
        }

        return result;
    }

    private boolean validateName(EditText name, TextInputLayout inputLayout, String message) {
        if (name.getText().toString().trim().isEmpty()) {
            inputLayout.setError(message);
            inputLayout.setErrorEnabled(true);
            requestFocus(name);
            name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
            return false;
        } else {
            inputLayout.setErrorEnabled(false);
            inputLayout.setError(null);
            name.getBackground().setColorFilter(null);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.etUserNamePerson1:
                    if (HomeMatchMakingInputScreen.isMenuItemClicked) {
                        //HomeMatchMakingInputScreen.isMenuItemClicked=false;
                        boyInputLayout.setErrorEnabled(false);
                        boyInputLayout.setError(null);
                        etUserNamePerson1.getBackground().setColorFilter(null);
                    } else {
                        validateName(etUserNamePerson1, boyInputLayout, getString(R.string.please_enter_boy_name_v1));
                    }
                    break;
                case R.id.etUserNamePerson2:
                    if (HomeMatchMakingInputScreen.isMenuItemClicked) {
                        HomeMatchMakingInputScreen.isMenuItemClicked = false;
                        girlInputLayout.setErrorEnabled(false);
                        girlInputLayout.setError(null);
                        etUserNamePerson2.getBackground().setColorFilter(null);
                    } else {
                        validateName(etUserNamePerson2, girlInputLayout, getString(R.string.please_enter_girl_name_v1));
                    }
                    break;

            }
        }
    }

    @Override
    protected void setMatchingResult(String response) {
        if (response != null && !response.isEmpty()) {
            try {
                try {
                    response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("response = ", response);
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.has(KEY_RESULT)) {
                    int result = jsonObject.getInt(KEY_RESULT);
                    if (result == NAME_MATCHING_RESULT_NOT_FOUND) {
                        Gson gson = new Gson();
                        List<NameSwarModel> nameSwarModels = Arrays.asList(gson.fromJson(jsonObject.getString(KEY_SWAR_LIST), NameSwarModel[].class));
                        nameSwarModelList.clear();
                        nameSwarModelList.addAll(nameSwarModels);

                        toOpenMatchingDialogue();
                        return;
                    }
                }
                Intent intent = new Intent(currentActivity, NameMatchingOutputActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(KEY_NAME, etUserNamePerson1.getText().toString());
                bundle.putString(KEY_GIRL_NAME, etUserNamePerson2.getText().toString());
                bundle.putString(KEY_GIRL_NAME, etUserNamePerson2.getText().toString());
                bundle.putString(KEY_MATCHING_DATA, response);
                if (nameBoySwarModel != null && nameGirlSwarModel != null) {
                    bundle.putString(KEY_BOY_SWAR, nameBoySwarModel.getSwar());
                    bundle.putString(KEY_GIRL_SWAR, nameGirlSwarModel.getSwar());
                }
                intent.putExtras(bundle);

                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void toOpenMatchingDialogue() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        FragmentManager fm = getSupportFragmentManager();
        Fragment prev = fm.findFragmentByTag("nameMatchingDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        NameMatchingDialog nameMatchingDialog = NameMatchingDialog.getInstance();
        nameMatchingDialog.show(fm, "nameMatchingDialog");
        ft.commit();

    }

}
