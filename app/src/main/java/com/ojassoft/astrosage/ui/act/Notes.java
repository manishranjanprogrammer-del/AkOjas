package com.ojassoft.astrosage.ui.act;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.libojassoft.android.misc.VolleyResponse;
import com.libojassoft.android.misc.VolleyServiceHandler;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;

import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Notes extends BaseInputActivity implements VolleyResponse {

    private String notes, onlineChartId, userId, password;
    private Toolbar toolBar;
    private ImageView ivToggleImage, ivSave;
    private EditText editText;
    private int position = 0;
    private boolean isEdit = false;
    private ProgressBar progressBar;
    private TextView tvQuestionLimit, tvTitle;
    boolean isDismissDialog;

    public Notes() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        //Getting intent
        notes = getIntent().getStringExtra("notes");
        onlineChartId = getIntent().getStringExtra("onlineChartId");
        userId = getIntent().getStringExtra("userId");
        password = getIntent().getStringExtra("password");

        //Initializing layout ids
        toolBar = (Toolbar) findViewById(R.id.toolbar_notes);
        ivToggleImage = (ImageView) findViewById(R.id.ivToggleImage);
        ivSave = (ImageView) findViewById(R.id.ivSave);
        editText = (EditText) findViewById(R.id.edit_text);
        tvQuestionLimit = (TextView) findViewById(R.id.tvQuestionLimit);
        tvQuestionLimit.setText(getResources().getString(R.string.astro_shop_user_question_250).replace("200", "4000"));
        tvQuestionLimit.setTypeface(robotRegularTypeface);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        if (notes != null && !notes.equals("")) {
            editText.setText(notes);
            editText.setSelection(editText.getText().length());
            progressBar.setVisibility(View.GONE);
        } else {
            saveAndGetNotes(this, userId, password, "", onlineChartId, false);
        }

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.notes);
        tvTitle.setTypeface(regularTypeface);

        //Set listeners of ivToggleImage
        ivToggleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Notes.this.finish();
            }
        });

        //Set listeners of ivSave
        ivSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CUtils.isConnectedWithInternet(Notes.this)) {
                    if (!editText.getText().toString().isEmpty()) {
                        saveNotes(false);
                    }
                } else {
                    MyCustomToast mct2 = new MyCustomToast(Notes.this, getLayoutInflater(), Notes.this, regularTypeface);
                    mct2.show(getResources().getString(R.string.internet_is_not_working));
                }
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        saveNotes(true);
    }

    private void saveAndGetNotes(Context context, String userid, String password, String usercomment, String onlinechartid, boolean isDismissDialog) {
        //new SaveNotesOnServer(context, userid, password, usercomment, onlinechartid, isDismissDialog).execute();
        saveNotesOnServer(isDismissDialog, usercomment);
    }


    /*private class SaveNotesOnServer extends AsyncTask<String, Long, Void> {
        CustomProgressDialog pd = null;
        String userid, password, usercomment, onlinechartid;
        Context context;
        String result = "";
        boolean isDismissDialog;

        public SaveNotesOnServer(Context context, String userid, String password, String usercomment, String onlinechartid, boolean isDismissDialog) {
            this.context = context;
            this.userid = userid;
            this.password = password;
            this.usercomment = usercomment;
            this.onlinechartid = onlinechartid;
            this.isDismissDialog = isDismissDialog;
        }

        @Override
        protected void onPreExecute() {
            if (!isDismissDialog)
                progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... arg0) {

            try {
                result = new ControllerManager().saveNotesOnserver(context, userid, password, usercomment, onlinechartid);
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result1) {
            try {

                System.out.println("Notes Result " + result);
                if (isDismissDialog) {
                    //dialog.dismiss();
                } else {
                    progressBar.setVisibility(View.GONE);
                    String comment = parseData(result);
                    if (!comment.equals("")) {
                        editText.setText(comment);
                        editText.setSelection(editText.getText().length());
                        OutputMasterActivity.notes = comment;
                        notes = comment;

                    }
                }

            } catch (Exception e) {

            }
        }

    }*/

    private String parseData(String result) {
        String comment = "";
        try {
            JSONArray jsonArray = new JSONArray(result);
            String resultCode = jsonArray.getJSONObject(0).getString("Result");
            if (resultCode.equals("1") || resultCode.equals("6")) {
                comment = jsonArray.getJSONObject(1).getString("userComment");
                if (resultCode.equals("1")) {
                    Toast.makeText(Notes.this, getResources().getString(R.string.save_notes), Toast.LENGTH_SHORT).show();

                }
            } else if (resultCode.equals("5")) {
//                Toast.makeText(Notes.this, getResources().getString(R.string.not_save_notes), Toast.LENGTH_SHORT).show();
                Toast.makeText(Notes.this, getResources().getString(R.string.save_notes_error), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {

        }
        return comment;
    }

    private String validText(String str) {
        String notes;
        //(<([^>]+)>)
        notes = str.replace("'", "''");
        notes = notes.replace("cast(", "");
        notes = notes.replace("char(", "");
        notes = notes.replaceAll("[\\<,\\(,\\[,\\^,\\>,\\],\\+,\\),\\>,]", "");
        //notes = str.replaceAll("[<([^>]+)>]", "");

        return notes;
    }

    private void saveNotes(boolean isDismissDialog) {
        String noteText = editText.getText().toString();
        if (noteText != null && !noteText.equals(notes)) {
            OutputMasterActivity.notes = noteText;
            noteText = validText(noteText);
            saveAndGetNotes(this, userId, password, noteText, onlineChartId, isDismissDialog);
            //Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
        }

        // CUtils.hideMyKeyboard(this);
    }


    /**
     * Save notes on server.
     */
    public void saveNotesOnServer(boolean isDismissDialog, String usercomment) {
        this.isDismissDialog = isDismissDialog;
        if (!isDismissDialog) {
            showProgressBar();
        }
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
        String url = CGlobalVariables.SAVE_NOTES_URL;
        StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url, this, true, getParams(usercomment), 0).getMyStringRequest();
        queue.add(stringRequest);
    }

    @Override
    public void onResponse(String response, int method) {

        try {
            response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            if (isDismissDialog) {
                Log.i("", response);
            } else {
                hideProgressBar();
                String comment = parseData(response);
                if (!comment.equals("")) {
                    editText.setText(comment);
                    editText.setSelection(editText.getText().length());
                    OutputMasterActivity.notes = comment;
                    notes = comment;
                }
            }
        } catch (Exception e) {
            hideProgressBar();
        }
    }

    @Override
    public void onError(VolleyError error) {

        hideProgressBar();
    }

    public Map<String, String> getParams(String usercomment) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("key", CUtils.getApplicationSignatureHashCode(Notes.this));
        params.put(CGlobalVariables.KEY_USER_ID, CUtils.replaceEmailChar(userId));
        params.put(CGlobalVariables.KEY_PASSWORD, password);
        params.put("usercomment", usercomment);
        params.put("onlinechartid", onlineChartId);

        return params;
    }

    /**
     * show Progress Bar
     */
    private void showProgressBar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    /**
     * hide Progress Bar
     */
    private void hideProgressBar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }
}
