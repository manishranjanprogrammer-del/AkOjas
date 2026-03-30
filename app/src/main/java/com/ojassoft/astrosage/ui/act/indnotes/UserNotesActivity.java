package com.ojassoft.astrosage.ui.act.indnotes;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.libojassoft.android.dao.NotesDBManager;
import com.libojassoft.android.models.NotesModel;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.constants.AppConstants;
import com.ojassoft.astrosage.model.CategoryModel;
import com.ojassoft.astrosage.ui.fragments.ChooseCategoryFragmentDailog;
import com.ojassoft.astrosage.utils.indnotes.AnalyticsUtils;
import com.ojassoft.astrosage.utils.indnotes.CategoryUtils;
import com.ojassoft.astrosage.utils.indnotes.DateTimeUtils;
import com.ojassoft.astrosage.utils.indnotes.FontUtils;
import com.ojassoft.astrosage.utils.indnotes.SharedPreferenceUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class UserNotesActivity extends BaseActivity {

    private LinearLayout categoryLL;
    private TextView textCategory;
    private EditText notesETV;
    private TextView msgNotesTV;
    private NotesDBManager notesDBManager;
    private NotesModel notesModel;
    CategoryModel selectedCategoryModel;
    private int selectedCategoryId;
    Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_notes);
    }

    @Override
    protected void initViews() {
        try {
        toChangeActionBar();
        if (getIntent().getExtras() != null) {
            notesModel = getIntent().getParcelableExtra(KEY_NOTES_MODEL);
            if (notesModel != null) {
                selectedCategoryId = notesModel.getCategoryId();
            }
        }
        notesDBManager = new NotesDBManager(context);
        categoryLL = findViewById(R.id.categoryLL);
        textCategory = findViewById(R.id.textCategory);
        notesETV = findViewById(R.id.notesETV);
        msgNotesTV = findViewById(R.id.msgNotesTV);

        toSetTitle();
        getCategory();

        FontUtils.changeFont(context, notesETV, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, msgNotesTV, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, textCategory, FONT_ROBOTO_REGULAR);
        if (notesModel != null) {
            notesETV.setText(notesModel.getNotes());
            notesETV.setSelection(notesETV.getText().length());
            setRemindarView();
        }
        if (selectedCategoryModel != null) {
            textCategory.setText(selectedCategoryModel.getName());
        }
        }catch (Exception e){
            //
        }
    }

    @Override
    protected void initContext() {
        currentActivity = UserNotesActivity.this;
        context = UserNotesActivity.this;
    }

    @Override
    protected void initListners() {
        categoryLL.setOnClickListener(this);
    }

    @Override
    protected boolean isActionBar() {
        return true;
    }

    @Override
    protected boolean isHomeButton() {
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.categoryLL: {
                openCategorySelectDialog();
                break;
            }
        }
    }

    @Override
    public void onAlertClicked(int alertType) {

    }

    private void setRemindarView() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.YEAR, notesModel.getYear());
        calendar1.set(Calendar.MONTH, notesModel.getMonth() - 1);
        calendar1.set(Calendar.DAY_OF_MONTH, notesModel.getDay());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_user_notes, menu);
        boolean isHelpScreenShow = SharedPreferenceUtils.getInstance(context).getBoolean(AppConstants.HELP_SCREEN_CATEGORY_CLICK);
        if (!isHelpScreenShow) {
            customeDialogHelp();
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            toHideKeyboard();
        } else if (item.getItemId() == R.id.action_save) {
            saveNotesIntoDb(true);
            return true;
        } else if (item.getItemId() == R.id.action_category) {
            openCategorySelectDialog();
            return true;
        } else if (item.getItemId() == R.id.action_share) {
            toOpenShareDialog();
            return true;
        } else if (item.getItemId() == R.id.action_calendar) {
            toOpenCalendar();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        try {
            FragmentManager fm = getSupportFragmentManager();
            if (fm != null) {
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        saveNotesIntoDb(false);
        super.onPause();
    }

    private void toSetTitle() {
        if (notesModel == null) {
            settingTitle(getString(R.string.title_user_notes));
        } else {
            StringBuilder sb = new StringBuilder();
            if (notesModel.getDay() < 10) {
                sb.append("0" + notesModel.getDay());
            } else {
                sb.append(notesModel.getDay());
            }
            String title = DateTimeUtils.getMonthTitle(context, notesModel.getMonth());
            sb.append(" " + title);
            if (notesModel.getDay() <= 0) {
                settingTitle(getString(R.string.title_user_notes));
            } else {
                String textStr = "";
                int langCode = SharedPreferenceUtils.getInstance(currentActivity).getInteger(AppConstants.app_language_key);
                if (langCode == 0 || langCode == HINDI) {
                    textStr = sb.toString() + " को याद दिलाएं।";
                } else {
                    textStr = "Remind me on " + sb.toString() + ".";
                }
                settingTitle(getString(R.string.title_user_notes) + " (" + sb + ")");
            }
        }
    }

    private void toChangeActionBar() {
        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setBackgroundDrawable(new ColorDrawable(getColor(R.color.backgroundColorView)));
            mActionBar.setDisplayShowTitleEnabled(false);
            mActionBar.setDisplayShowTitleEnabled(true);
        }
        Drawable drawable = toolbar.getNavigationIcon();
        drawable.setColorFilter(ContextCompat.getColor(currentActivity, R.color.black), PorterDuff.Mode.SRC_ATOP);
        toolbar.setTitleTextColor(getColor(R.color.black));
    }

    private void saveNotesIntoDb(boolean isMsg) {

        if (notesDBManager == null) return;

        if (notesModel == null) {
            notesModel = new NotesModel();
        }

        notesModel.setNotes(notesETV.getText().toString().trim());
        notesModel.setTimestamp(System.currentTimeMillis() + "");
        if (selectedCategoryModel != null) {
            notesModel.setCategoryId(selectedCategoryModel.getId());
            notesModel.setTags(selectedCategoryModel.getName());
        }

        if (TextUtils.isEmpty(notesModel.getNotes())) {
            notesModel.setCategoryId(0);
            notesModel.setTags("");
        }
        int result = 0;
        NotesModel dbNotesModel = notesDBManager.getNotesModelById(notesModel.getId());
        if (dbNotesModel == null) {
            result = notesDBManager.addNotes(notesModel);
        } else {
            result = notesDBManager.updateNotesById(notesModel);
        }
        if (!TextUtils.isEmpty(notesModel.getNotes())) {
            AnalyticsUtils.setAnalytics(currentActivity, "Note Saved by User");
        }

        AnalyticsUtils.setAnalytics(currentActivity, "Note Saved");
    }

    public void openCategorySelectDialog() {
        AnalyticsUtils.setAnalytics(context, "ChooseCategory Clicked");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        FragmentManager fm = getSupportFragmentManager();
        Fragment prev = fm.findFragmentByTag("ChooseCategoryFragmentDailog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        ChooseCategoryFragmentDailog fragmentDailog = new ChooseCategoryFragmentDailog();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_CATEGORY_ID, selectedCategoryId);
        fragmentDailog.setArguments(bundle);
        fragmentDailog.show(fm, "ChooseCategoryFragmentDailog");
        ft.commit();
    }

    public void setSelectedCategory(CategoryModel categoryModel) {
        if (categoryModel == null) return;
        selectedCategoryId = categoryModel.getId();
        selectedCategoryModel = categoryModel;
        textCategory.setText(selectedCategoryModel.getName());
    }


    private void getCategory() {
        List<CategoryModel> categoryModels = CategoryUtils.getCategoryList(context);
        if (categoryModels == null || selectedCategoryId == 0) {
            return;
        }

        for (int i = 0; i < categoryModels.size(); i++) {
            CategoryModel categoryModel = categoryModels.get(i);
            if (categoryModel == null) continue;
            if (categoryModel.getId() == selectedCategoryId) {
                selectedCategoryModel = categoryModel;
                selectedCategoryModel.setSelected(true);
                break;
            }
        }
    }

    public void toOpenShareDialog() {
        String textToShare = null;
        String date = getDate();
        if (TextUtils.isEmpty(date)) {
            textToShare = notesETV.getText().toString().trim();
        } else {
            textToShare = getDate() + "\n" + notesETV.getText().toString().trim();
        }
        if (TextUtils.isEmpty(textToShare.trim()) || TextUtils.isEmpty(notesETV.getText().toString().trim())) {
            toast(getString(R.string.msg_no_notes), false);
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, textToShare);
        try {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            AnalyticsUtils.setAnalytics(context, "Share Note Clicked");
            startActivity(Intent.createChooser(intent, getResources().getString(R.string.text_share_it)));
        } catch (android.content.ActivityNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private String getDate() {
        String date = "";
        if (notesModel == null) return date;
        if (notesModel.getDay() == 0) {
            return date;
        }
        Calendar cal = Calendar.getInstance();
        int month = notesModel.getMonth() - 1;
        cal.set(notesModel.getYear(), month, notesModel.getDay());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        date = dateFormat.format(cal.getTime());
        return date;
    }

    private boolean isGeneralNotes() {
        if (getIntent().getExtras() != null) {
            return getIntent().getExtras().getBoolean("isGeneralNotes", false);
        }
        return false;
    }

    private void toOpenCalendar() {

        if (notesModel != null && notesModel.getDay() != 0 && notesModel.getMonth() != 0
                && notesModel.getYear() != 0) {
            myCalendar.set(Calendar.YEAR, notesModel.getYear());
            myCalendar.set(Calendar.MONTH, notesModel.getMonth() - 1);
            myCalendar.set(Calendar.DAY_OF_MONTH, notesModel.getDay());
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            if (notesModel == null) {
                notesModel = new NotesModel();
            }
            notesModel.setDay(dayOfMonth);
            notesModel.setMonth(monthOfYear + 1);
            notesModel.setYear(year);
        }

    };

    protected void customeDialogHelp() {

        final Animation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        final Animation ScaleAnimation = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        ScaleAnimation.setDuration(500);
        alphaAnimation.setDuration(500);

        dialog = new Dialog(this);
        LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflator.inflate(R.layout.layout_help_category, null, false);

        final LinearLayout llHelp = view.findViewById(R.id.llHelp);
        Button btnClick = view.findViewById(R.id.btnClick);
        TextView textViewClickHelp = view.findViewById(R.id.textViewClickHelp);

        FontUtils.changeFont(context, textViewClickHelp, FONT_ROBOTO_MEDIUM);
        FontUtils.changeFont(context, btnClick, FONT_ROBOTO_MEDIUM);

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        view.startAnimation(alphaAnimation);
        view.startAnimation(ScaleAnimation);

        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
        addOptionHelp(view);

        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openHintAlert();
                SharedPreferenceUtils.getInstance(context).putBoolean(AppConstants.HELP_SCREEN_CATEGORY_CLICK, true);
            }
        });

        llHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openHintAlert();
                SharedPreferenceUtils.getInstance(context).putBoolean(AppConstants.HELP_SCREEN_CATEGORY_CLICK, true);
            }
        });

    }

    private void addOptionHelp(View view) {

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.llHelp1);

        int additionalValue = 0;

        double density = getResources().getDisplayMetrics().density;
        if (density >= 4.0) {
            additionalValue = additionalValue + 100;
            //xxxhdpi
        } else if (density >= 3.5 && density < 4.0) {
            additionalValue = additionalValue + 100;
        } else if (density >= 3.0 && density < 3.5) {
            additionalValue = additionalValue + 15;
        } else if (density >= 2.0 && density < 3.0) {
            additionalValue = additionalValue + 5;
            //xhdpi
        } else if (density >= 1.5 && density < 2.0) {
            additionalValue = additionalValue + 5;
            //hdpi
        } else if (density >= 1.0 && density < 1.5) {
            additionalValue = additionalValue + 5;
            //mdpi
        }

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
        layoutParams
                .setMargins(
                        0,
                        additionalValue,
                        0,
                        0);

        layout.setLayoutParams(layoutParams);
    }

    private void openHintAlert() {
        alert(context, "", getString(R.string.text_dialog_msg), getString(R.string.ok), getString(R.string.cancel), false, false, ALERT_TYPE_NO_NETWORK);
    }

}
