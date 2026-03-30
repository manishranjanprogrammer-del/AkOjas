package com.ojassoft.astrosage.ui.act.indnotes;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.claudiodegio.msv.MaterialSearchView;
import com.claudiodegio.msv.OnSearchViewListener;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.customadapters.NotesAdapter;
import com.ojassoft.astrosage.customadapters.SearchHistoryAdapter;
import com.libojassoft.android.dao.NotesDBManager;
import com.ojassoft.astrosage.interfaces.RecyclerClickListner;
import com.libojassoft.android.models.NotesModel;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.indnotes.AnalyticsUtils;
import com.ojassoft.astrosage.utils.indnotes.DateTimeUtils;
import com.ojassoft.astrosage.utils.indnotes.FontUtils;
import com.ojassoft.astrosage.utils.indnotes.SharedPreferenceUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class NotesActivity extends BaseActivity implements OnSearchViewListener {

    private MaterialSearchView materialSearchView;
    private LinearLayout nextLL;
    private LinearLayout prevLL;
    private TextView monthTV;
    private RecyclerView recyclerView;
    private RecyclerView historyRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout historyLL;
    private ArrayList<NotesModel> notesModels;
    private List<String> historyList;
    private List<String> filterHistoryList;
    private NotesAdapter notesAdapter;
    private SearchHistoryAdapter historyAdapter;
    private NotesDBManager notesDBManager;
    private List<NotesModel> dbNotesModelList;
    private int currentYear;
    private int currentMonth;
    private int scrollPossition;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cal_notes);
    }

    @Override
    protected void initViews() {

        int LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication()).getLanguageCode();
        CUtils.getRobotoFont(NotesActivity.this, LANGUAGE_CODE, CGlobalVariables.regular);
        materialSearchView = findViewById(R.id.searchViewNotes);
        Calendar calendar = Calendar.getInstance();
        currentMonth = (calendar.get(Calendar.MONTH) + 1);
        currentYear = calendar.get(Calendar.YEAR);

        nextLL = findViewById(R.id.nextLL);
        prevLL = findViewById(R.id.prevLL);
        monthTV = findViewById(R.id.monthTV);
        historyLL = findViewById(R.id.historyLL);
        recyclerView = findViewById(R.id.recyclerView);
        historyRecyclerView = findViewById(R.id.historyRecyclerView);

        notesDBManager = new NotesDBManager(context);
        notesModels = new ArrayList<>();
        dbNotesModelList = new ArrayList<>();
        notesAdapter = new NotesAdapter(context, notesModels);
        mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(notesAdapter);

        historyList = new ArrayList<>();
        filterHistoryList = new ArrayList<>();
        historyAdapter = new SearchHistoryAdapter(context, filterHistoryList);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        historyRecyclerView.setItemAnimator(new DefaultItemAnimator());
        historyRecyclerView.setAdapter(historyAdapter);

        FontUtils.changeFont(context, monthTV, FONT_ROBOTO_MEDIUM);
        updateHistoryAdapter();
        updateMonthView();
    }

    @Override
    protected void initContext() {
        context = NotesActivity.this;
        currentActivity = NotesActivity.this;
    }

    @Override
    protected void initListners() {
        prevLL.setOnClickListener(this);
        nextLL.setOnClickListener(this);
        materialSearchView.setOnSearchViewListener(this);

        notesAdapter.setOnClickListner(new RecyclerClickListner() {
            @Override
            public void onItemClick(int position, View v) {
                 if (notesModels != null && notesModels.size() > position) {
                    NotesModel notesModel = notesModels.get(position);
                    if (notesModel == null) return;
                    if (bundle == null) {
                        bundle = new Bundle();
                    }
                    bundle.putParcelable(KEY_NOTES_MODEL, notesModel);
                    AnalyticsUtils.setAnalytics(currentActivity, "Note clicked from date-wise");
                    startActivity(currentActivity, UserNotesActivity.class, bundle, true, REQUEST_TAG_NOTES, true, ANIMATION_SLIDE_UP);
                }
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });

        historyAdapter.setOnClickListner(new RecyclerClickListner() {
            @Override
            public void onItemClick(int position, View v) {
                if (filterHistoryList != null && filterHistoryList.size() > position) {
                    String historyStr = filterHistoryList.get(position);
                    if (historyStr == null) return;
                    materialSearchView.setQuery(historyStr, true);
                }
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });
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
        int i = v.getId();
        if (i == R.id.prevLL) {
            currentMonth--;
            if (currentMonth < January) {
                currentMonth = December;
                currentYear--;
            }
            updateMonthView();
        } else if (i == R.id.nextLL) {
            currentMonth++;
            if (currentMonth > December) {
                currentMonth = January;
                currentYear++;
            }
            updateMonthView();
        }
    }

    @Override
    public void onAlertClicked(int alertType) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_notes, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        materialSearchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            toHideKeyboard();
            toOpenShareDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onSearchViewShown() {
        historyLL.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSearchViewClosed() {
        historyLL.setVisibility(View.GONE);
    }

    @Override
    public boolean onQueryTextSubmit(String searchedString) {
        if (TextUtils.isEmpty(searchedString)) {
            return true;
        }
        toHideKeyboard();
        if (historyList.size() < 10) {
            if (historyList.contains(searchedString)) {
                historyList.remove(searchedString);
            }
            historyList.add(0, searchedString);
        } else {
            historyList.add(0, searchedString);
            historyList.remove(10);
        }
        String joinedStr = TextUtils.join(",", historyList);
        SharedPreferenceUtils.getInstance(context).putString(HISTORY_LIST, joinedStr);

        historyLL.setVisibility(View.GONE);
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putString(KEY_SEARCHED_STR, searchedString);
        bundle.putInt(KEY_MONTH, currentMonth);
        bundle.putInt(KEY_YEAR, currentYear);
        AnalyticsUtils.setAnalytics(currentActivity, "Notes Searched");
        startActivity(currentActivity, SearchedNotestActivity.class, bundle, true, REQUEST_TAG_NOTES, true, ANIMATION_SLIDE_LEFT);
        return false;
    }

    @Override
    public void onQueryTextChange(String s) {
        if (TextUtils.isEmpty(s)) {
            filterHistoryList.clear();
            filterHistoryList.addAll(historyList);
            historyAdapter.notifyDataSetChanged();
            return;
        }
        filterHistoryList.clear();
        for (int i = 0; i < historyList.size(); i++) {
            String historyStr = historyList.get(i);
            if (historyStr == null) continue;
            if (historyStr.toLowerCase().contains(s.toLowerCase())) {
                filterHistoryList.add(historyStr);
            }
        }
        historyAdapter.notifyDataSetChanged();
    }

    private void prepareNotesData() {
        Calendar calendar = new GregorianCalendar(currentYear, currentMonth - 1, 1);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        notesModels.clear();
        dbNotesModelList.clear();
        dbNotesModelList.addAll(notesDBManager.getNotesList(currentMonth, currentYear));

        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        scrollPossition = 0;
        for (int i = 0; i < daysInMonth; i++) {
            int day = i + 1;
            NotesModel notesModel = getNotesModel(day);
            if (notesModel == null) {
                notesModel = new NotesModel();
                notesModel.setMonth(currentMonth);
                notesModel.setYear(currentYear);
                notesModel.setDay(day);
            }
            notesModel.setDate(DateTimeUtils.getMonthString(context, currentMonth, day));
            if (year == currentYear && month == currentMonth & day == currentDay) {
                notesModel.setCurrentDay(true);
                scrollPossition = day - 1;
            }
            notesModels.add(notesModel);
        }
    }

    private NotesModel getNotesModel(int day) {
        NotesModel notesModel = null;

        for (int i = 0; i < dbNotesModelList.size(); i++) {
            NotesModel dbNotesModel = dbNotesModelList.get(i);
            if (dbNotesModel == null) continue;
            if (dbNotesModel.getMonth() == currentMonth && dbNotesModel.getDay() == day
                    && dbNotesModel.getYear() == currentYear) {
                notesModel = dbNotesModel;
                break;
            }
        }
        return notesModel;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        toHideKeyboard();
        updateHistoryAdapter();
        updateMonthView();
    }

    private void updateHistoryAdapter() {
        String joinedStr = SharedPreferenceUtils.getInstance(context).getString(HISTORY_LIST);
        String[] strArray = TextUtils.split(joinedStr, ",");
        historyList.clear();
        historyList.addAll(Arrays.asList(strArray));
        filterHistoryList.clear();
        filterHistoryList.addAll(historyList);
        historyAdapter.notifyDataSetChanged();
    }

    private void updateMonthView() {
        settingTitle(getString(R.string.text_notes));
        monthTV.setText(DateTimeUtils.getMonthTitle(context, currentMonth) + " " + currentYear);
        prepareNotesData();
        notesAdapter.notifyDataSetChanged();

        RecyclerView.SmoothScroller smoothScroller = new
                LinearSmoothScroller(context) {
                    @Override
                    protected int getVerticalSnapPreference() {
                        return LinearSmoothScroller.SNAP_TO_START;
                    }
                };

        if (scrollPossition > 0) {
            smoothScroller.setTargetPosition(scrollPossition - 1);
        } else {
            smoothScroller.setTargetPosition(scrollPossition);
        }

        mLayoutManager.startSmoothScroll(smoothScroller);
    }

    public void toOpenShareDialog() {

        StringBuilder textToShare = new StringBuilder();
        for (int i = 0; i < notesModels.size(); i++) {
            NotesModel notesModel = notesModels.get(i);
            if (notesModel == null) continue;
            if (TextUtils.isEmpty(notesModel.getNotes())) {
                continue;
            }
            textToShare.append(getDate(notesModel) + "\n" + notesModel.getNotes() + "\n");
        }
        if (TextUtils.isEmpty(textToShare.toString().trim())) {
            showSnackbar(recyclerView, getString(R.string.msg_no_notes));
            //toast(getString(R.string.msg_no_notes), false);
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, textToShare.toString());
        try {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            AnalyticsUtils.setAnalytics(currentActivity, "Share Notes clicked");
            startActivity(Intent.createChooser(intent, getResources().getString(R.string.text_share_it)));
        } catch (android.content.ActivityNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private String getDate(NotesModel notesModel) {
        String date = "";
        if (notesModel == null) return date;
        Calendar cal = Calendar.getInstance();
        int month = notesModel.getMonth() - 1;
        cal.set(notesModel.getYear(), month, notesModel.getDay());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        date = dateFormat.format(cal.getTime());
        return date;
    }
}
