package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.OutputMasterActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ojas-08 on 22/11/16.
 */
public class NotesDialogFragment extends DialogFragment implements VolleyResponse {
    String notes, onlineChartId, userId, password;
    EditText editText;
    int position = 0;
    boolean isEdit = false;
    // RecyclerView recyclerView;
    //NoteAdapter noteAdapter;
    Dialog dialog;
    ProgressBar progressBar;
    Activity activity;
    boolean isDismissDialog;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notes = getArguments().getString("notes");
        onlineChartId = getArguments().getString("onlineChartId");
        userId = getArguments().getString("userId");
        password = getArguments().getString("password");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dialog = getDialog();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.note_dialog_frag_layout, container);
        editText = (EditText) view.findViewById(R.id.edit_text);
        TextView textView = (TextView) view.findViewById(R.id.textquestiontextlimit);
        textView.setText(getResources().getString(R.string.astro_shop_user_question_250).replace("200", "4000"));
        textView.setTypeface(((BaseInputActivity) getActivity()).robotRegularTypeface);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        if (notes != null && !notes.equals("")) {
            editText.setText(notes);
            editText.setSelection(editText.getText().length());
            progressBar.setVisibility(View.GONE);
        } else {
            saveAndGetNotes(getActivity(), userId, password, "", onlineChartId, false);
        }
        TextView heading = (TextView) view.findViewById(R.id.heading);
        heading.setText(R.string.notes);
        heading.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        Button saveBtn = (Button) view.findViewById(R.id.saveBtn);
        Button cancelBtn = (Button) view.findViewById(R.id.cancelBtn);
        saveBtn.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        cancelBtn.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText.getText().toString().isEmpty()) {
                    saveNotes(false);
                }

            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotesDialogFragment.this.dismiss();
            }
        });
        CUtils.googleAnalyticSendWitPlayServie(getActivity(),
                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                "Notes Dialog", null);
        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_NOTES_DIALOG, CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int hight = metrics.heightPixels;
        Dialog dialog = getDialog();
        if (dialog != null) {
            WindowManager.LayoutParams wmlp = dialog.getWindow()
                    .getAttributes();
            wmlp.width = (int) width - 40;
            wmlp.height = (int) hight - 40;
            dialog.getWindow().setAttributes(wmlp);
        }
    }

    public static NotesDialogFragment getInstance(String onlineChartId, String notes, String userId, String password) {
        Bundle bundle = new Bundle();
        bundle.putString("notes", notes);
        bundle.putString("onlineChartId", onlineChartId);
        bundle.putString(CGlobalVariables.KEY_USER_ID,CUtils.replaceEmailChar(userId));
        bundle.putString(CGlobalVariables.KEY_PASSWORD, password);
        NotesDialogFragment notesDialogFragment = new NotesDialogFragment();
        notesDialogFragment.setArguments(bundle);
        return notesDialogFragment;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (!editText.getText().toString().isEmpty()) {
            saveNotes(true);
        }
    }

    public void editNotes(String notes, int pos) {
        isEdit = true;
        position = pos;
        editText.setText(notes);
        editText.setSelection(editText.getText().length());
    }

    private void saveNotes(boolean isDismissDialog) {
        String noteText = editText.getText().toString();
        if (noteText != null && !noteText.equals(notes)) {
            ((OutputMasterActivity) getActivity()).notes = noteText;
            noteText = validText(noteText);
            saveAndGetNotes(getActivity(), userId, password, noteText, onlineChartId, isDismissDialog);
            //Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveAndGetNotes(Context context, String userid, String password, String usercomment, String onlinechartid, boolean isDismissDialog) {
        //new SaveNotesOnServer(context, userid, password, usercomment, onlinechartid, isDismissDialog).execute();
        saveNotesOnServer(isDismissDialog);
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
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... arg0) {

            try {
                //result = new ControllerManager().saveNotesOnserver(context, userid, password, usercomment, onlinechartid);
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result1) {
            try {
                progressBar.setVisibility(View.GONE);
                if (isDismissDialog) {
                    dialog.dismiss();
                } else {
                    String comment = parseData(result);
                    if (!comment.equals("")) {
                        editText.setText(comment);
                        editText.setSelection(editText.getText().length());
                        ((OutputMasterActivity) getActivity()).notes = comment;
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
                    Toast.makeText(activity, getResources().getString(R.string.save_notes), Toast.LENGTH_SHORT).show();

                }
            } else if (resultCode.equals("5")) {
                Toast.makeText(activity, getResources().getString(R.string.not_save_notes), Toast.LENGTH_SHORT).show();
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


    /**
     * Save notes on server.
     */
    public void saveNotesOnServer(boolean isDismissDialog) {
        this.isDismissDialog = isDismissDialog;
        showProgressBar();
        RequestQueue queue = VolleySingleton.getInstance(activity).getRequestQueue();
        String url = CGlobalVariables.SAVE_NOTES_URL;
        StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url, this, true, getParamsNew(),0).getMyStringRequest();
        queue.add(stringRequest);
    }

    @Override
    public void onResponse(String response, int method) {
        try {
            progressBar.setVisibility(View.GONE);
            if (isDismissDialog) {
                dialog.dismiss();
            } else {
                String comment = parseData(response);
                if (!comment.equals("")) {
                    editText.setText(comment);
                    editText.setSelection(editText.getText().length());
                    ((OutputMasterActivity) getActivity()).notes = comment;
                    notes = comment;

                }
            }

        } catch (Exception e) {

        }
    }

    @Override
    public void onError(VolleyError error) {

        hideProgressBar();
    }

    public Map<String, String> getParamsNew() {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("key", CUtils.getApplicationSignatureHashCode(activity));
        params.put(CGlobalVariables.KEY_USER_ID,CUtils.replaceEmailChar( userId));
        params.put(CGlobalVariables.KEY_PASSWORD, password);
        params.put("usercomment", notes);
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
