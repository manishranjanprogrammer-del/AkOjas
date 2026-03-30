package com.ojassoft.astrosage.ui;

import static com.ojassoft.astrosage.utils.CGlobalVariables.ASTROSAGE_AI_PACKAGE_NAME;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TelUsMoreDialog extends Dialog implements View.OnClickListener {

    TextView tvBoring,tvNotTure,tvRepetitive,tvOutofCharacter,tvBadMoney;
    TextView tvTooLong,tvTooShort,tvChatEndEarly,tvFunny,tvInteresting,tvHelpful;
    EditText editTextMsg;
    float rating;
    String answerID;

    public TelUsMoreDialog(@NonNull Context context, float rating, String anwerId) {
        super(context);
        this.rating = rating;
        this.answerID = anwerId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        View view = getLayoutInflater().inflate(R.layout.tell_us_more_bottom_sheet_layout, null);
        setContentView(R.layout.tell_us_more_bottom_sheet_layout);

        tvBoring =  findViewById(R.id.tv_Boring);
        tvNotTure =  findViewById(R.id.tv_not_true);
        tvRepetitive =  findViewById(R.id.tv_repetitive);
        tvOutofCharacter =  findViewById(R.id.tv_o_f_c);
        tvBadMoney =  findViewById(R.id.tv_bad_money);
        tvTooLong =  findViewById(R.id.tv_too_long);
        tvTooShort =  findViewById(R.id.tv_too_short);
        tvChatEndEarly =  findViewById(R.id.tv_e_c_e);
        tvFunny =  findViewById(R.id.tv_funny);
        tvInteresting =  findViewById(R.id.tv_interesting);
        tvHelpful =  findViewById(R.id.tv_helpful);
        Button cancelButton = findViewById(R.id.cancel_button);
        Button submitButton = findViewById(R.id.submit_button);
        editTextMsg = findViewById(R.id.edit_text_msg);
        cancelButton.setOnClickListener(v->dismiss());
        submitButton.setOnClickListener(view -> {
            String labels = getLabels();
            if(!labels.isEmpty()){
                send(labels);
                dismiss();
            }else{
                Toast.makeText(getContext(), getContext().getString(R.string.please_select_one_label), Toast.LENGTH_SHORT).show();
            }

        });
        tvHelpful.setOnClickListener(this);
        tvBoring.setOnClickListener(this);
        tvNotTure.setOnClickListener(this);
        tvRepetitive.setOnClickListener(this);
        tvOutofCharacter.setOnClickListener(this);
        tvBadMoney.setOnClickListener(this);
        tvTooLong.setOnClickListener(this);
        tvTooShort.setOnClickListener(this);
        tvChatEndEarly.setOnClickListener(this);
        tvInteresting.setOnClickListener(this);
        tvFunny.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch ( view.getId()){
            case R.id.tv_Boring: selectLabel(tvBoring);
            break;
            case R.id.tv_not_true: selectLabel(tvNotTure);
                break;
            case R.id.tv_repetitive: selectLabel(tvRepetitive);
                break;
            case R.id.tv_o_f_c: selectLabel(tvOutofCharacter);
                break;
            case R.id.tv_bad_money: selectLabel(tvBadMoney);
                break;
            case R.id.tv_too_long: selectLabel(tvTooLong);
                break;
            case R.id.tv_too_short: selectLabel(tvTooShort);
                break;
            case R.id.tv_e_c_e: selectLabel(tvChatEndEarly);
                break;
            case R.id.tv_funny: selectLabel(tvFunny);
                break;
            case R.id.tv_interesting: selectLabel(tvInteresting);
                break;
            case R.id.tv_helpful: selectLabel(tvHelpful);
                break;
        }
    }

    private void selectLabel(TextView selected){
        selected.setSelected(!selected.isSelected());

        if(selected.isSelected())
            selected.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
        else
             selected.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

    }
    private void send(String labels){
        HashMap<String, String> params = new HashMap<>();

        params.put("key", CUtils.getApplicationSignatureHashCode(getContext()));
        params.put("packagename", ASTROSAGE_AI_PACKAGE_NAME);
        params.put(CGlobalVariables.DEVICE_ID,CUtils.getMyAndroidId(getContext()));
        params.put(CGlobalVariables.APP_VERSION, BuildConfig.VERSION_NAME);
        params.put("methodname","getaiastroratingcomments");
        params.put("astroid","38");
        params.put("userrating",""+Math.round(rating));
        params.put("usercommentlabel",labels);
        params.put("usercomment",editTextMsg.getText().toString());
        params.put("conversationid","1");
        params.put("answerid",answerID);
        params.put("lang",""+ CUtils.getIntData(getContext(),CGlobalVariables.APP_PREFS_AppLanguage,0));
        params.put("userid",com.ojassoft.astrosage.utils.CUtils.getUserName(getContext()));
        params.put(CGlobalVariables.COUNTRY_CODE,CUtils.getCountryCode(getContext()));
        params.put(CGlobalVariables.COUNTRY_CODE,CUtils.getCountryCode(getContext()));
        Call<ResponseBody> call  = RetrofitClient.getAIInstance().create(ApiList.class).sendAnswerFeedback(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String resString = response.body().string();
                    JSONObject jsonObject = new JSONObject(resString);
                    if(jsonObject.has("status") && jsonObject.getString("status").equals("1")){
                        Toast.makeText(getContext(), getContext().getString(R.string.feedback_success), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getContext(), jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), getContext().getString(R.string.feedback_success), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private String getLabels(){
        String labels = "";
        if (tvBoring.isSelected())
                labels = labels + tvBoring.getText()+",";
        if (tvNotTure.isSelected())
            labels = labels + tvNotTure.getText()+",";
        if (tvRepetitive.isSelected())
            labels = labels + tvRepetitive.getText()+",";
        if (tvOutofCharacter.isSelected())
            labels = labels + tvOutofCharacter.getText()+",";
        if (tvBadMoney.isSelected())
            labels = labels + tvBadMoney.getText()+",";
        if (tvTooLong.isSelected())
            labels = labels + tvTooLong.getText()+",";
        if (tvTooShort.isSelected())
            labels = labels + tvTooShort.getText()+",";
        if (tvFunny.isSelected())
            labels = labels + tvFunny.getText()+",";
        if (tvChatEndEarly.isSelected())
            labels = labels + tvChatEndEarly.getText()+",";
        if (tvInteresting.isSelected())
            labels = labels + tvInteresting.getText()+",";
        if (tvHelpful.isSelected())
            labels = labels + tvHelpful.getText();

        if(labels.isEmpty()){
            return "";
        }
        if(labels.charAt(0) == ',')
            labels.replaceFirst(",","");

        if(labels.charAt(labels.length()-1) == ',')
            labels.replace(",","");
        Log.d("Labels", labels);
        return labels;
    }
}
