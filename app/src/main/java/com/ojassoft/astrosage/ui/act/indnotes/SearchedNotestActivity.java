package com.ojassoft.astrosage.ui.act.indnotes;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.claudiodegio.msv.MaterialSearchView;
import com.claudiodegio.msv.OnSearchViewListener;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.customadapters.SearchHistoryAdapter;
import com.ojassoft.astrosage.customadapters.SearchedNotesAdapter;
import com.libojassoft.android.dao.NotesDBManager;
import com.ojassoft.astrosage.interfaces.RecyclerClickListner;
import com.libojassoft.android.models.NotesModel;
import com.ojassoft.astrosage.utils.indnotes.FontUtils;
import com.ojassoft.astrosage.utils.indnotes.SharedPreferenceUtils;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchedNotestActivity extends BaseActivity implements OnSearchViewListener {

    MaterialSearchView materialSearchView;
    private LinearLayout resultLL;
    private TextView noRecordTV;
    private TextView resultLblTV;
    private TextView resultCountTV;
    private RecyclerView recyclerView;
    private RecyclerView historyRecyclerView;
    private LinearLayout historyLL;
    private List<String> historyList;
    private List<String> filterHistoryList;
    private SearchHistoryAdapter historyAdapter;
    private SearchedNotesAdapter notesAdapter;
    private List<NotesModel> dbNotesModelList;
    private NotesDBManager notesDBManager;

    private String searchedString;
    private int month;
    private int year;
    private boolean fromGeneralNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_result);
    }

    @Override
    protected void initViews() {
        if (getIntent().getExtras() != null) {
            searchedString = getIntent().getStringExtra(KEY_SEARCHED_STR);
            month = getIntent().getIntExtra(KEY_MONTH, 0);
            year = getIntent().getIntExtra(KEY_YEAR, 0);
            fromGeneralNotes = getIntent().getBooleanExtra("fromGeneralNotes", false);
        }
        materialSearchView = findViewById(R.id.searchViewNotes);
        notesDBManager = new NotesDBManager(context);
        if (fromGeneralNotes) {
            dbNotesModelList = notesDBManager.getGenSearchedNotesList(searchedString);
        } else {
            dbNotesModelList = notesDBManager.getSearchedNotesList(month, year, searchedString);
        }
        toHideKeyboard();
        materialSearchView.setQuery(searchedString, false);

        historyLL = findViewById(R.id.historyLL);
        recyclerView = findViewById(R.id.recyclerView);
        historyRecyclerView = findViewById(R.id.historyRecyclerView);
        resultLL = findViewById(R.id.resultLL);
        noRecordTV = findViewById(R.id.noRecordTV);
        resultLblTV = findViewById(R.id.resultLblTV);
        resultCountTV = findViewById(R.id.resultCountTV);
        notesAdapter = new SearchedNotesAdapter(context, dbNotesModelList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(notesAdapter);

        historyList = new ArrayList<>();
        filterHistoryList = new ArrayList<>();
        historyAdapter = new SearchHistoryAdapter(context, filterHistoryList);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        historyRecyclerView.setItemAnimator(new DefaultItemAnimator());
        historyRecyclerView.setAdapter(historyAdapter);
        updateHistoryAdapter();

        FontUtils.changeFont(context, noRecordTV, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, resultLblTV, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, resultCountTV, FONT_ROBOTO_REGULAR);
        showMessage();
    }

    @Override
    protected void initContext() {
        context = SearchedNotestActivity.this;
        currentActivity = SearchedNotestActivity.this;
    }

    @Override
    protected void initListners() {
        initKeyBoardListener();
        materialSearchView.setOnSearchViewListener(this);

        notesAdapter.setOnClickListner(new RecyclerClickListner() {
            @Override
            public void onItemClick(int position, View v) {

                if (dbNotesModelList != null && dbNotesModelList.size() > position) {
                    toHideKeyboard();
                    NotesModel notesModel = dbNotesModelList.get(position);
                    if (notesModel == null) return;
                    if (bundle == null) {
                        bundle = new Bundle();
                    }
                    bundle.putParcelable(KEY_NOTES_MODEL, notesModel);
                    ((BaseActivity) currentActivity).startActivity(currentActivity, UserNotesActivity.class, bundle, true, REQUEST_TAG_NOTES, true, ANIMATION_SLIDE_UP);
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
        materialSearchView.showSearch(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            toHideKeyboard();
            finish();
        } else if (item.getItemId() == R.id.action_search) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //toHideKeyboard();
        super.onBackPressed();
    }

    @Override
    public void onSearchViewShown() {

    }

    @Override
    public void onSearchViewClosed() {
        finish();
    }

    @Override
    public boolean onQueryTextSubmit(String searchedString) {
        if (notesDBManager == null) return false;
        this.searchedString = searchedString;
        if (TextUtils.isEmpty(searchedString)) {
            dbNotesModelList.clear();
            notesAdapter.notifyDataSetChanged();
            return true;
        } else {

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
            filterHistoryList.clear();
            filterHistoryList.addAll(historyList);
            historyAdapter.notifyDataSetChanged();
            historyLL.setVisibility(View.GONE);
            List<NotesModel> notesModelList = null;
            if (fromGeneralNotes) {
                notesModelList = notesDBManager.getGenSearchedNotesList(searchedString);
            } else {
                notesModelList = notesDBManager.getSearchedNotesList(month, year, searchedString);
            }
            dbNotesModelList.clear();
            dbNotesModelList.addAll(notesModelList);
            notesAdapter.notifyDataSetChanged();
            showMessage();
            return true;
        }
    }

    @Override
    public void onQueryTextChange(String s) {
        if (TextUtils.isEmpty(s)) {
            toOpenKeyboard();
        }
    }

    private void initKeyBoardListener() {

        final int MIN_KEYBOARD_HEIGHT_PX = 150;

        final View decorView = getWindow().getDecorView();

        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            private final Rect windowVisibleDisplayFrame = new Rect();
            private int lastVisibleDecorViewHeight;

            @Override
            public void onGlobalLayout() {
                decorView.getWindowVisibleDisplayFrame(windowVisibleDisplayFrame);
                final int visibleDecorViewHeight = windowVisibleDisplayFrame.height();

                if (lastVisibleDecorViewHeight != 0) {
                    if (lastVisibleDecorViewHeight > visibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX) {
                        historyLL.setVisibility(View.VISIBLE);
                    } else if (lastVisibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX < visibleDecorViewHeight) {
                        historyLL.setVisibility(View.GONE);
                    }
                }

                lastVisibleDecorViewHeight = visibleDecorViewHeight;
            }
        });
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

    private void showMessage() {
        if (dbNotesModelList.isEmpty()) {
            noRecordTV.setText(getString(R.string.msg_no_notes));
            noRecordTV.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            resultLL.setVisibility(View.GONE);
        } else {
            noRecordTV.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            resultLL.setVisibility(View.VISIBLE);
            resultCountTV.setText(dbNotesModelList.size() + "");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        toHideKeyboard();
        List<NotesModel> notesModelList = null;
        if (fromGeneralNotes) {
            notesModelList = notesDBManager.getGenSearchedNotesList(searchedString);
        } else {
            notesModelList = notesDBManager.getSearchedNotesList(month, year, searchedString);
        }
        dbNotesModelList.clear();
        dbNotesModelList.addAll(notesModelList);
        notesAdapter.notifyDataSetChanged();
        showMessage();
    }
}
