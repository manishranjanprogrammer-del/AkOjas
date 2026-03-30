package com.ojassoft.astrosage.ui.fragments;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.BASE_INPUT_SCREEN;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.IS_FROM_SCREEN;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.libojassoft.android.misc.VolleyResponse;
import com.libojassoft.android.misc.VolleyServiceHandler;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanDateTime;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.controller.ControllerManager;
import com.ojassoft.astrosage.customexceptions.UIDataOperationException;
import com.ojassoft.astrosage.jinterface.ISearchBirthDeatils;
import com.ojassoft.astrosage.jinterface.ISearchBirthDetailsFragment;
import com.ojassoft.astrosage.jinterface.IUpdateMenus;
import com.ojassoft.astrosage.jinterface.OnLoadMoreListener;
import com.ojassoft.astrosage.misc.AutoCompleteTextviewAdapter;
import com.ojassoft.astrosage.misc.CalculateKundli;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.model.CDatabaseHelperOperations;
import com.ojassoft.astrosage.ui.JobServices.FindPlaceService;
import com.ojassoft.astrosage.ui.JobServices.MakeDefaultService;
import com.ojassoft.astrosage.ui.act.ActLogin;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.HomeInputScreen;
import com.ojassoft.astrosage.ui.act.MychartActivity;
import com.ojassoft.astrosage.ui.act.matching.HomeMatchMakingInputScreen;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.CXMLOperations;
import com.ojassoft.astrosage.utils.GetDefaultKundliDataService;
import com.ojassoft.astrosage.varta.ui.activity.LoginSignUpActivity;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class SearchBirthDetailsFragment extends Fragment implements ISearchBirthDeatils, VolleyResponse {
    public static int position;
    ProgressBar progressBar;
    int selectedButtonposition1;
    RequestQueue queue;
    String lastOnlineKundliId = "";
    CDatabaseHelperOperations databaseHelperOperations;
    int startIndex = 0;
    int localKundliLimit = 40;
    int serverKundliLimit = 20;

    //ArrayList<BeanHoroPersonalInfo> allSavedKundli;
    //AutoCompleteTextviewAdapter autoCompleteTextviewAdapter;
    ISearchBirthDetailsFragment _iSearchBirthDetailsFragment;
    ArrayList<BeanHoroPersonalInfo> onlineSavedKundli;
    RecyclerViewAdapter searchKundliListAdapterN;
    RecyclerViewAdapterForMyChart searchKundliListAdapterMyChart;
    public AutoCompleteTextView editTextSearchKundli;
    RecyclerView save_kundali_list_item_lay_mychart;
    CustomProgressDialog pd = null;
    // protected Map<String, String> onlineChartArray = new HashMap<String, String>();
    protected Map<String, String> mapHoroID = new HashMap<String, String>();
    public RecyclerView lstSearchKundli;
    //protected RecyclerView lstSearchMyKundli;
    protected SearchKundliListAdapter searchKundliListAdapter;
    RadioButton rbKundliSearchLocal, rbKundliSearchOnline;
    //rbKundliSearchAll;
    RadioGroup rbGroupKundliSearch;
    //TextView nameValueTextView;
    TextView nameValueOnly, noKundliError;
    TextView recentViewdKundliTextView, loadAllLocalKudli;
    ImageView myPersonalGenderImg;
    //public Typeface typeface, typeface2;
    int SELECTED_MODULE;
    // int LANGUAGE_CODE;
    final int NO_OF_RECENT_CHARTS = 5;
    // TextView textViewRecentCharts;

    // ADDED BY BIJENDRA ON 21-05-14
    List<KundliRecord> lstonlineChart = null;
    List<KundliRecord> lstofflineChart = null;
    List<KundliRecord> recentSearchChart = null;
    ArrayList<BeanHoroPersonalInfo> onlineChartArrayList = null;
    ArrayList<BeanHoroPersonalInfo> recentAndLocalKundli = null;
    private Sort sortList = Sort.ASCENDING;
    protected LinearLayout laySortKundliList;
    //TextView textViewLoadAll;
    Activity activity;
    Button create_kundli;
    CardView personKundaliDetail;
    LinearLayout select_from_below_text, recent_text_container;

    IUpdateMenus iUpdateMenus;

    String monthNames[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private TextView birthPlace;
    private TextView birthTime;
    private TextView birthDateTime;
    TextView viweForStatusBar;
    boolean isRecentTabChecked = true;
    //CDeleteOnlineKundliSync cDeleteOnlineKundliSync;
    Button loadMoreButton;
    //int totalOnlineCharts;
    LinearLayout errorContainer;
    int onlineKundliResultCode;
    int lastOnlineArraySize;
    Button signInButton;
    public static PopupMenu popup;

    // END
    public SearchBirthDetailsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SELECTED_MODULE = activity.getIntent()
                .getIntExtra(CGlobalVariables.MODULE_TYPE_KEY,
                        CGlobalVariables.MODULE_BASIC);
       /* LANGUAGE_CODE = ((AstrosageKundliApplication) activity
                .getApplication()).getLanguageCode();// ADDED BY HEVENDRA ON*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.birth_detail_search, container,
                false);

        rbKundliSearchLocal = (RadioButton) view
                .findViewById(R.id.rbKundliSearchLocal);
        rbKundliSearchOnline = (RadioButton) view
                .findViewById(R.id.rbKundliSearchOnline);
        rbGroupKundliSearch = (RadioGroup) view
                .findViewById(R.id.rbGroupKundliSearch);
        queue = VolleySingleton.getInstance(activity).getRequestQueue();
        recent_text_container = (LinearLayout) view.findViewById(R.id.recent_text_container);
        nameValueOnly = (TextView) view.findViewById(R.id.name_value_only);
        myPersonalGenderImg = (ImageView) view.findViewById(R.id.genderImagePersonal);
        viweForStatusBar = (TextView) view.findViewById(R.id.viweForStatusBar);
        nameValueOnly.setTypeface(((BaseInputActivity) activity).robotMediumTypeface);
        recentViewdKundliTextView = (TextView) view.findViewById(R.id.recent_view_kundli_text);
        ((TextView) view.findViewById(R.id.heading)).setTypeface(((BaseInputActivity) activity).mediumTypeface);
        ((TextView) view.findViewById(R.id.birthDate)).setTypeface(((BaseInputActivity) activity).mediumTypeface);
        ((TextView) view.findViewById(R.id.bTime)).setTypeface(((BaseInputActivity) activity).mediumTypeface);
        ((TextView) view.findViewById(R.id.bPlace)).setTypeface(((BaseInputActivity) activity).mediumTypeface);
        recentViewdKundliTextView.setTypeface(((BaseInputActivity) activity).regularTypeface);
        loadAllLocalKudli = (TextView) view.findViewById(R.id.load_local_kundli);
        loadAllLocalKudli.setTypeface(((BaseInputActivity) activity).regularTypeface);
        birthDateTime = (TextView) view.findViewById(R.id.birthDateTime);
        birthTime = (TextView) view.findViewById(R.id.birthTime);
        birthPlace = (TextView) view.findViewById(R.id.birthPlace);
        loadMoreButton = (Button) view.findViewById(R.id.load_more_btn);
        noKundliError = (TextView) view.findViewById(R.id.no_kundli_error);
        signInButton = (Button) view.findViewById(R.id.login_btn);
        signInButton.setTypeface(((BaseInputActivity) activity).mediumTypeface);
       /*
       //comment by neeraj 19/5/2016

       noKundliError.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ActLogin.class);
                intent.putExtra("callerActivity",
                        CGlobalVariables.HOME_INPUT_SCREEN);
                startActivityForResult(intent,
                        HomeInputScreen.SUB_ACTIVITY_USER_LOGIN);
                noKundliError.setVisibility(View.GONE);
            }
        });*/
        //added by neeraj 19/5/2016
        SpannableString ss = new SpannableString(getResources().getString(R.string.no_kundli_error));
        try {
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    //Intent intent = new Intent(activity, ActLogin.class);
                    //intent.putExtra("callerActivity", CGlobalVariables.HOME_INPUT_SCREEN);

                    Intent intent = new Intent(getActivity(), LoginSignUpActivity.class);
                    intent.putExtra(IS_FROM_SCREEN, BASE_INPUT_SCREEN);
                    activity.startActivityForResult(intent, HomeInputScreen.SUB_ACTIVITY_USER_LOGIN);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                }
            };
            if (((AstrosageKundliApplication) activity.getApplication())
                    .getLanguageCode() == CGlobalVariables.ENGLISH) {

                ss.setSpan(clickableSpan, 76, 89, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (((AstrosageKundliApplication) activity.getApplication())
                    .getLanguageCode() == CGlobalVariables.HINDI) {
                ss.setSpan(clickableSpan, 83, 96, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (((AstrosageKundliApplication) activity.getApplication())
                    .getLanguageCode() == CGlobalVariables.BANGALI) {
                ss.setSpan(clickableSpan, 88, 104, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (((AstrosageKundliApplication) activity.getApplication())
                    .getLanguageCode() == CGlobalVariables.TAMIL) {
                ss.setSpan(clickableSpan, 84, 114, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (((AstrosageKundliApplication) activity.getApplication())
                    .getLanguageCode() == CGlobalVariables.MARATHI) {
                ss.setSpan(clickableSpan, 82, 96, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (((AstrosageKundliApplication) activity.getApplication())
                    .getLanguageCode() == CGlobalVariables.GUJARATI) {
                ss.setSpan(clickableSpan, 85, 101, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (((AstrosageKundliApplication) activity.getApplication())
                    .getLanguageCode() == CGlobalVariables.TELUGU) {
                ss.setSpan(clickableSpan, 85, 101, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (((AstrosageKundliApplication) activity.getApplication())
                    .getLanguageCode() == CGlobalVariables.KANNADA) {
                ss.setSpan(clickableSpan, 75, 92, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } catch (Exception ex) {
            //
        }
        // TextView textView = (TextView) findViewById(R.id.hello);
        noKundliError.setText(ss);
        noKundliError.setMovementMethod(LinkMovementMethod.getInstance());
        noKundliError.setHighlightColor(Color.TRANSPARENT);
        lstSearchKundli = (RecyclerView) view.findViewById(R.id.lstSearchKundli);
        //lstSearchKundli.setNestedScrollingEnabled(false);
        ViewCompat.setNestedScrollingEnabled(lstSearchKundli, false);
        lstSearchKundli.setLayoutManager(new LinearLayoutManager(activity));
        editTextSearchKundli = (AutoCompleteTextView) view
                .findViewById(R.id.editTextSearchKundli);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        // editTextSearchKundli.setTypeface(((BaseInputActivity) activity).mediumTypeface);
        /*butKundliSearchOk = (ImageButton) view
                .findViewById(R.id.butKundliSearchOk);*/

        //added by Neeraj 5/4/16
        personKundaliDetail = (CardView) view.findViewById(R.id.personKundaliDetail);
        select_from_below_text = (LinearLayout) view.findViewById(R.id.select_from_below_text);
        create_kundli = (Button) view.findViewById(R.id.create_kundli);
        if (activity instanceof MychartActivity) {
            create_kundli.setTypeface(((MychartActivity) activity).mediumTypeface);
            if (((AstrosageKundliApplication) activity.getApplication())
                    .getLanguageCode() == CGlobalVariables.ENGLISH) {
                create_kundli.setText(getResources().getString(R.string.create_yourFirst_kundali).toUpperCase());
            }
        }
        rbGroupKundliSearch
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        lstSearchKundli.setAdapter(null);
                        //lstSearchMyKundli.setAdapter(null);
                        // ADDED BY BIJENDRA ON 20-05-15
                        if (checkedId == R.id.rbKundliSearchOnline) {
                        }
                        //textViewLoadAll.setVisibility(View.GONE);
                        // END

                    }
                });


       /* editTextSearchKundli.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(activity, onlineSavedKundli.get(position).getName(), Toast.LENGTH_SHORT).show();
                int selectedModule = 0;
                int selectedSubModule = 0;
                if (activity instanceof HomeInputScreen) {
                    selectedModule = ((HomeInputScreen) activity).SELECTED_MODULE;
                    selectedSubModule = ((HomeInputScreen) activity).SELECTED_SUB_SCREEN;
                } else {
                    // selectedModule = ((HomeMatchMakingInputScreen) context).SELECTED_MODULE;
                    selectedModule = 0;
                }
                editTextSearchKundli.showDropDown();
                AutoCompleteTextviewAdapter autoCompleteTextviewAdapter = ((AutoCompleteTextviewAdapter) editTextSearchKundli.getAdapter());
                BeanHoroPersonalInfo clickeItem = autoCompleteTextviewAdapter.getClickedItem(position);
                if (activity instanceof HomeMatchMakingInputScreen) {
                    editRecentKundli(clickeItem);
                } else {
                    CUtils.saveKundliInPreference(activity, clickeItem);
                    CalculateKundli kundli = new CalculateKundli(clickeItem, false, activity, ((BaseInputActivity) activity).regularTypeface, selectedModule, CGlobalVariables.HOME_INPUT_SCREEN, false, 0, 0, selectedSubModule);
                    kundli.calculate();
                }

            }
        });*/

        editTextSearchKundli.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchForAvailableKundli();
            }
        });

        loadAllLocalKudli = (TextView) view.findViewById(R.id.load_local_kundli);

        loadAllLocalKudli.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //hideRecentText(true);
                loadLocalAndRecentKundli();
                /*setListAdapter(null, loadAllLocalKundli(), 0);*/

            }
        });
        // ADDED BY BIJENDRA ON 21-05-14
       /* ((ImageButton) view.findViewById(R.id.butSortKundliList))
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if (rbKundliSearchLocal.isChecked())
                            sortOfflineKundli();
                        else
                            sortOnlineKundli();
                    }
                });*/
        // ADDED BY BIJENDRA ON 20-05-15
        /*textViewLoadAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewLoadAll.setVisibility(View.GONE);
                if (rbKundliSearchLocal.isChecked())
                    goToSearchKundli();
            }
        });*/

        // END
        applyLanguageFont(((BaseInputActivity) activity).mediumTypeface);
        errorContainer = (LinearLayout) view.findViewById(R.id.error_conteainer);
        loadMoreButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity == null) return;
                if (!isRecentTabChecked) {
                    getOnlineKundali();
                } else {
                    ArrayList<BeanHoroPersonalInfo> recentSearchKundli = (ArrayList) CUtils.getRecentSearchKundli(activity);
                    int startIndex = 0;
                    if (recentSearchKundli != null) {
                        startIndex = recentAndLocalKundli.size() - recentSearchKundli.size();
                    } else {
                        startIndex = recentAndLocalKundli.size();
                    }

                    ArrayList<BeanHoroPersonalInfo> localKundli = loadAllLocalKundli(startIndex);
                    recentAndLocalKundli.addAll(localKundli);
                    if (recentSearchKundli != null) {
                        setListAdapter(null, recentAndLocalKundli, recentSearchKundli.size());
                    } else {
                        setListAdapter(null, recentAndLocalKundli, 0);
                    }

                    int allLocalKundliSize = CUtils.getKundliCount(activity);
                    if (localKundli.size() >= localKundliLimit && recentAndLocalKundli.size() < allLocalKundliSize) {
                        loadMoreButton.setVisibility(View.VISIBLE);
                    } else {
                        loadMoreButton.setVisibility(View.GONE);
                    }
                }

            }
        });
        rbKundliSearchLocal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isRecentTabChecked = true;
                loadMoreButton.setVisibility(View.GONE);
                loadLocalAndRecentKundli();
            }
        });
        rbKundliSearchOnline.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity == null) return;
                isRecentTabChecked = false;
                TextView errorText = (TextView) view.findViewById(R.id.error_text);
                ImageView errorImage = (ImageView) view.findViewById(R.id.error_image);
                loadMoreButton.setVisibility(View.GONE);
                //when internet connection is not available
                if (!CUtils.isConnectedWithInternet(activity)) {
                    errorContainer.setVisibility(View.VISIBLE);
                    errorImage.setVisibility(View.VISIBLE);
                    signInButton.setVisibility(View.GONE);
                    errorText.setText(getResources().getString(R.string.no_internet_connection));
                    lstSearchKundli.setVisibility(View.GONE);
                }//When user is not logged in
                else if (!CUtils.isUserLogedIn(activity)) {
                    errorContainer.setVisibility(View.VISIBLE);
                    errorImage.setVisibility(View.GONE);
                    signInButton.setVisibility(View.VISIBLE);
                    errorText.setText(getResources().getString(R.string.not_logged_in_));
                    lstSearchKundli.setVisibility(View.GONE);
                } else {
                    errorContainer.setVisibility(View.GONE);
                    lstSearchKundli.setVisibility(View.VISIBLE);
                    if (onlineChartArrayList == null || (onlineChartArrayList != null && onlineChartArrayList.size() == 0)) {
                        getOnlineKundali();
                    } else {
                        if (lastOnlineArraySize >= serverKundliLimit && onlineKundliResultCode != 4) {
                            loadMoreButton.setVisibility(View.VISIBLE);
                        } else {
                            loadMoreButton.setVisibility(View.GONE);
                        }

                        setListAdapter(null, onlineChartArrayList, 0);
                    }
                }

            }
        });
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseInputActivity) activity).goToLogin(BaseInputActivity.TAG_SIGN_IN);
            }
        });
       /*rbKundliSearchOnline.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(activity, "Online", Toast.LENGTH_SHORT).show();
                // showLocalSearchedKundli("", false, NO_OF_RECENT_CHARTS);
                //setVisibalityOfSearchLayout(view, View.VISIBLE);
               *//* if (onlineSavedKundli != null && onlineSavedKundli.size() > 0) {
                    //setListAdapter(null, onlineSavedKundli);
                }*//*
                if (CUtils.isConnectedWithInternet(activity)) {
                    if (CUtils.isUserLogedIn(activity)) {
                        showLocalSearchedKundli("", false, NO_OF_RECENT_CHARTS);
                    } else {
                        Intent intent = new Intent(activity, ActLogin.class);
                        intent.putExtra("callerActivity",
                                CGlobalVariables.HOME_INPUT_SCREEN);
                        startActivityForResult(intent,
                                HomeInputScreen.SUB_ACTIVITY_USER_LOGIN);
                    }
                } else {
                    MyCustomToast mct = new MyCustomToast(activity,
                            activity.getLayoutInflater(), activity,
                            ((BaseInputActivity) activity).regularTypeface);
                    mct.show(getResources().getString(R.string.no_internet));
                }
                //goToSearchKundli();
            }
        });*/
        showPersonalKundliData();
        //visbility of button when no item on recyceler BY NEERAJ 12/4/16
        /*if (searchKundliListAdapterMyChart == null && activity instanceof MychartActivity) {
            create_kundli.setVisibility(View.VISIBLE);
        }*/


        create_kundli.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotocreatekundaliscreen = new Intent(activity.getApplicationContext(), HomeInputScreen.class);
                startActivity(gotocreatekundaliscreen);
                // Toast.makeText(activity.getApplicationContext(),"size "+searchKundliListAdapterMyChart+"    ",Toast.LENGTH_SHORT).show();
            }
        });

        final View activityRootView = view.findViewById(R.id.scrollView);


        return view;
    }

    private void loadLocalAndRecentKundli() {
        if (recentAndLocalKundli == null) {
            recentAndLocalKundli = new ArrayList<BeanHoroPersonalInfo>();
        } else {
            recentAndLocalKundli.clear();
        }

// executeOnExecutor execute asyctask parallel
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            new GetRecordsfromLocalDatabase().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new GetRecordsfromLocalDatabase().execute();
        }


    }

    public void onActivityResultResponce() {
        noKundliError.setVisibility(View.GONE);
        searchForAvailableKundli();
    }

    private void searchForAvailableKundli() {
        if (editTextSearchKundli.getText().length() >= 3) {

            AutoCompleteTextviewAdapter autoCompleteTextviewAdapter = (AutoCompleteTextviewAdapter) editTextSearchKundli.getAdapter();
            if (autoCompleteTextviewAdapter != null) {
                ArrayList<BeanHoroPersonalInfo> savedKundli = (ArrayList<BeanHoroPersonalInfo>) autoCompleteTextviewAdapter.getItems();
                if (savedKundli != null && savedKundli.size() > 0) {
                    editTextSearchKundli.setAdapter(null);
                }
            }
            getLocalSavedKundli(editTextSearchKundli.getText().toString());
        } else {
            noKundliError.setVisibility(View.GONE);
        }
    }

    private void hideRecentText(boolean isHide) {
        if (isHide) {
           /* loadAllLocalKudli.setVisibility(View.GONE);
            recentViewdKundliTextView.setVisibility(View.GONE);*/
            recent_text_container.setVisibility(View.GONE);
        } else {
           /* loadAllLocalKudli.setVisibility(View.VISIBLE);
            recentViewdKundliTextView.setVisibility(View.VISIBLE);*/
            recent_text_container.setVisibility(View.GONE);
        }

    }

    private void applyLanguageFont(Typeface typeface) {
        rbKundliSearchLocal.setTypeface(typeface);
        rbKundliSearchOnline.setTypeface(typeface);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        _iSearchBirthDetailsFragment = (ISearchBirthDetailsFragment) activity;
        if (activity instanceof HomeInputScreen) {
            iUpdateMenus = (IUpdateMenus) activity;
        }
        this.activity = activity;
    }


    @Override
    public void onResume() {
        super.onResume();
        if(activity == null) return;
        hideRecentText(false);
        ArrayList<BeanHoroPersonalInfo> recentSearchKundli = (ArrayList) CUtils.getRecentSearchKundli(activity);
        /*if (recentSearchKundli == null || recentSearchKundli.size() == 0) {
            rbGroupKundliSearch.setVisibility(View.GONE);
        } else {
            rbGroupKundliSearch.setVisibility(View.VISIBLE);
        }*/
        rbGroupKundliSearch.setVisibility(View.VISIBLE);
        if (rbKundliSearchLocal.isChecked()) {
            loadLocalAndRecentKundli();
        } else {
            //user return from login screen then get data from server
            if (CUtils.isUserLogedIn(activity)) {
                if (onlineChartArrayList == null ||
                        (onlineChartArrayList != null && onlineChartArrayList.size() == 0)) {
                    errorContainer.setVisibility(View.GONE);
                    lstSearchKundli.setVisibility(View.VISIBLE);
                    getOnlineKundali();

                } else {
                    errorContainer.setVisibility(View.GONE);
                    lstSearchKundli.setVisibility(View.VISIBLE);
                    onlineChartArrayList.clear();
                    getOnlineKundali();
                }
            }
        }

        //setListAdapter(null, recentSearchKundli, 0);
    }

    /**
     * @param beanHoroPersonalInfo
     * @param isOnlineChart
     * @param isboyOrGirlKundli    boy :0 , girl :1 kundli used only for MatchMaking
     */
    private void fireEventToSendHoroscopeDetail(
            BeanHoroPersonalInfo beanHoroPersonalInfo, boolean isOnlineChart, int isboyOrGirlKundli) {
        // Added by Amit Sharma
        // open dialog to select current kundli
        //CUtils.saveKundliInPreference(activity, beanHoroPersonalInfo);

        if (SELECTED_MODULE == CGlobalVariables.MODULE_MY_CHART) {
            openConfimationDialogForPersonalKundali(activity,
                    beanHoroPersonalInfo);
        } else {
            try {
                if (mapHoroID != null) {
                    mapHoroID.clear();
                }

              /*
              commented and below added by neeraj 28/06/2016

              _iSearchBirthDetailsFragment
                        .selectedKundli(beanHoroPersonalInfo, position);
                if (position == 0) {
                    position = 1;
                } else {
                    position = 0;
                }*/

                _iSearchBirthDetailsFragment
                        .selectedKundli(beanHoroPersonalInfo, isboyOrGirlKundli);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void showPersonalKundliData() {
//change by neeraj
        try {
            BeanHoroPersonalInfo beanHoroPersonalInfo = (BeanHoroPersonalInfo) CUtils
                    .getCustomObject(activity.getApplication());
            if (beanHoroPersonalInfo != null) {
//show nothing at state when state is agra 13/4/16

                String state = beanHoroPersonalInfo.getPlace().getState(), country = beanHoroPersonalInfo.getPlace().getCountryName();
                if (state.trim().equals("Agra") || country.trim().equals("not define")) {
                    state = "";
                    country = "";
                } else {
                    state = ", " + state;
                    country = ", " + country;

                }


                setTextonFields(beanHoroPersonalInfo.getName(),
                        beanHoroPersonalInfo.getDateTime().getDay() + "-"
                                + monthNames[beanHoroPersonalInfo.getDateTime().getMonth()]
                                + "-"
                                + beanHoroPersonalInfo.getDateTime().getYear(),
                        beanHoroPersonalInfo.getDateTime().getHour() + ":"
                                + beanHoroPersonalInfo.getDateTime().getMin() + ":"
                                + beanHoroPersonalInfo.getDateTime().getSecond(),
                        beanHoroPersonalInfo.getPlace().getCityName() + "" + state + "" + country);
                //added by Neeraj For Change gender image in personal details 8/4/16
                if (beanHoroPersonalInfo.getGender().equals("M") || beanHoroPersonalInfo.getGender().equals("Male")) {
                    // viewHolder.genderImage.
                    myPersonalGenderImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_male));
                } else {
                    myPersonalGenderImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_female));
                }

            } else {


                setTextonFields("", "", "", "");
            }
        }catch (Exception e){

        }
    }

    private void setTextonFields(String name, String dob, String time,
                                 String palace) {
        // TextView nameValueTextView = (TextView) findViewById(R.id.name_value_textview);
        /*
         * TextView dobValueTextView = (TextView)
         * findViewById(R.id.dob_value_textview); TextView timeValueTextView =
         * (TextView) findViewById(R.id.time_value_textview); TextView
         * palaceValueTextView = (TextView)
         * findViewById(R.id.palace_value_textview);
         */
        //modified by NEERAJ 07/04/2016

        if (name.equals("")) {
            nameValueOnly.setText("None");
//            nameValueTextView.setText("None");
        } else {


            nameValueOnly.setText(name);
            //nameValueTextView.setText("Birth Date:" + dob + ", Time:" + time + "\n" + "Place:" + palace);
            birthDateTime.setText(": " + dob);
            birthTime.setText(": " + time);
            birthPlace.setText(": " + palace);
        }

        /*
         * dobValueTextView.setText(dob); timeValueTextView.setText(time);
         * palaceValueTextView.setText(palace);
         */
    }

    /*  private void goToSearchKundli(String kundliName) {
          String kundliNameToSearch = kundliName;
          kundliNameToSearch = editTextSearchKundli.getText().toString().trim();
          //lstSearchKundli.setAdapter(null);
          //lstSearchMyKundli.setAdapter(null);
          CUtils.hideMyKeyboard(activity);
          //textViewRecentCharts.setText(R.string.kundli_list);// ADDED BY BIJENDRA
          // ON 21-05-14
          if (rbKundliSearchLocal.isChecked()) {
              showLocalSearchedKundli(kundliNameToSearch, true, -1);
          } else {
              if (CUtils.isConnectedWithInternet(activity)) {
                  if (CUtils.isUserLogedIn(activity)) {
                      *//*
     * CUtils.googleAnalyticSend(null,
     * CGlobalVariables.GOOGLE_ANALYTIC_EVENT, CGlobalVariables.
     * GOOGLE_ANALYTIC_ACTION_ASTROSAGE_CLOUD_KUNDLI_SEARCH,
     * null, 0l);
     *//*
                    CUtils.googleAnalyticSendWitPlayServie(
                            activity,
                            CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                            CGlobalVariables.GOOGLE_ANALYTIC_ACTION_ASTROSAGE_CLOUD_KUNDLI_SEARCH,
                            null);
                    try {
                        pd = new CustomProgressDialog(activity, ((BaseInputActivity) activity).regularTypeface);
                        pd.show();
                        String key = CUtils.getApplicationSignatureHashCode(activity);
                        new ControllerManager().getOnlineChartList(activity,
                                kundliNameToSearch, CUtils.getUserName(activity),
                                CUtils.getUserPassword(activity), key, "1");
                    } catch (UICOnlineChartOperationException e) {
                        //msg = e.getMessage();
                        //Log.i("Exception", e.getMessage().toString());
                    }
                    //new CSearchOnlineKundliSync(kundliNameToSearch).execute();
                } else {
                    MyCustomToast mct = new MyCustomToast(activity,
                            activity.getLayoutInflater(), activity,
                            ((BaseInputActivity) activity).regularTypeface);
                    mct.show(getResources().getString(R.string.login_first));

                    // Intent intent = new Intent(activity
                    // ,ActWizardScreens.class);
                    Intent intent = new Intent(activity, ActLogin.class);
                    intent.putExtra("callerActivity",
                            CGlobalVariables.HOME_INPUT_SCREEN);
                    startActivityForResult(intent,
                            HomeInputScreen.SUB_ACTIVITY_USER_LOGIN);
                }
            } else {
                MyCustomToast mct = new MyCustomToast(activity,
                        activity.getLayoutInflater(), activity,
                        ((BaseInputActivity) activity).regularTypeface);
                mct.show(getResources().getString(R.string.no_internet));
            }
        }
    }
*/
    // LOCAL KUNDLI SEARECH
    private void openLocalKundli(long kundliId) {
        /*
         * SQLiteDatabase sqLiteDatabase = null; CDatabaseHelper
         * cDatabaseHelper=null; BeanHoroPersonalInfo beanHoroPersonalInfo=null;
         * try { cDatabaseHelper=new
         * ControllerManager().getDatabaseHelperObject(activity);
         * sqLiteDatabase = cDatabaseHelper.getReadableDatabase();
         * beanHoroPersonalInfo=new
         * ControllerManager().getLocalKundliDetail(sqLiteDatabase, kundliId);
         *
         * } catch (UIDataOperationException e) { MyCustomToast mct = new
         * MyCustomToast(activity, activity.getLayoutInflater(),
         * activity, Typeface.DEFAULT); mct.show(e.getMessage()); } finally
         * { if(cDatabaseHelper!=null)cDatabaseHelper.close(); }
         */

        BeanHoroPersonalInfo beanHoroPersonalInfo = null;
        try {

            beanHoroPersonalInfo = new ControllerManager()
                    .getLocalKundliDetailOperation(activity
                            .getApplicationContext(), kundliId);

        } catch (UIDataOperationException e) {
            MyCustomToast mct = new MyCustomToast(activity, activity
                    .getLayoutInflater(), activity, Typeface.DEFAULT);
            mct.show("Error" + e.getMessage());
        } catch (Exception e) {

        }

        fireEventToSendHoroscopeDetail(beanHoroPersonalInfo, false, 0);
    }

    public void deleteLocalKundli(BeanHoroPersonalInfo beanHoroPersonalInfo, boolean isLocal) {
        /*
         * SQLiteDatabase sqLiteDatabase = null; CDatabaseHelper
         * cDatabaseHelper=null; boolean isDeleted=false; try {
         * cDatabaseHelper=new
         * ControllerManager().getDatabaseHelperObject(activity);
         * sqLiteDatabase = cDatabaseHelper.getWritableDatabase(); isDeleted =
         * new ControllerManager().deleteLocalKundli(sqLiteDatabase, kundliId);
         *
         * } catch (UIDataOperationException e) { MyCustomToast mct = new
         * MyCustomToast(activity, activity.getLayoutInflater(),
         * activity, Typeface.DEFAULT); mct.show(e.getMessage()); isDeleted
         * = false; } finally {
         * if(cDatabaseHelper!=null)cDatabaseHelper.close(); }
         */
        if(activity == null) return;
        boolean isDeleted = false;
        try {

            isDeleted = new ControllerManager().deleteLocalKundliOperation(
                    activity.getApplicationContext(), beanHoroPersonalInfo.getLocalChartId());

        } catch (UIDataOperationException e) {
            MyCustomToast mct = new MyCustomToast(activity, activity
                    .getLayoutInflater(), activity, Typeface.DEFAULT);
            mct.show("Error" + e.getMessage());
            isDeleted = false;
        } catch (Exception e) {

        }

        if (isDeleted) {
            if (editTextSearchKundli.getAdapter() != null) {
                ((AutoCompleteTextviewAdapter) editTextSearchKundli.getAdapter()).deleteKundli(beanHoroPersonalInfo.getLocalChartId(), true);
                editTextSearchKundli.showDropDown();
                editTextSearchKundli.setThreshold(1);
            }

            MyCustomToast mct = new MyCustomToast(activity, activity
                    .getLayoutInflater(), activity, ((BaseInputActivity) activity).regularTypeface);
            mct.show(activity.getResources().getString(
                    R.string.kundli_deleted));
            if (mapHoroID != null) {
                mapHoroID.remove(String.valueOf(beanHoroPersonalInfo.getLocalChartId()));
                //showOfflineSearchedKundli();
                ArrayList<BeanHoroPersonalInfo> recentAndLocalKundli = new ArrayList<BeanHoroPersonalInfo>();
                ArrayList<BeanHoroPersonalInfo> recentSearchKundli = (ArrayList) CUtils.getRecentSearchKundli(activity);
                recentAndLocalKundli.addAll(recentSearchKundli);
                recentAndLocalKundli.addAll(loadAllLocalKundli(0));
                setListAdapter(null, recentAndLocalKundli, recentSearchKundli.size());
                //loadAllLocalKundli();
                _iSearchBirthDetailsFragment.oneChartDeleted(beanHoroPersonalInfo.getLocalChartId(), false);
            }
            deleteRecentKundli(beanHoroPersonalInfo.getLocalChartId(), isLocal);
            /*
             * else laySortKundliList.setVisibility(View.GONE);//ADDED BY
             * BIJENDRA ON 21-05-14
             */
        }

    }

    private void showLocalSearchedKundli(String kundliName,
                                         boolean showMessage, int noOfRecords) {

        /*
         * SQLiteDatabase sqLiteDatabase = null; CDatabaseHelper
         * cDatabaseHelper=null;
         *
         * try { cDatabaseHelper = new
         * ControllerManager().getDatabaseHelperObject(activity);
         * sqLiteDatabase = cDatabaseHelper.getReadableDatabase(); mapHoroID =
         * new
         * ControllerManager().searchLocalKundliList(sqLiteDatabase,kundliName
         * ,CGlobalVariables.BOTH_GENDER,noOfRecords);
         * lstSearchKundli.setAdapter(null); if(mapHoroID!=null) {
         * //laySortKundliList.setVisibility(View.VISIBLE);//ADDED BY BIJENDRA
         * ON 21-05-14 showOfflineSearchedKundli(); } else { if(showMessage) {
         * //laySortKundliList.setVisibility(View.GONE);//ADDED BY BIJENDRA ON
         * 21-05-14 MyCustomToast mct = new MyCustomToast(activity,
         * activity.getLayoutInflater(), activity, typeface2);
         * if(editTextSearchKundli.getText().toString().trim().length()>0) {
         * mct.
         * show(activity.getResources().getString(R.string.kundli_not_found
         * )); } else {
         * mct.show(activity.getResources().getString(R.string.
         * no_kundli_found)); } }else {
         * textViewRecentCharts.setVisibility(View.GONE);
         * //laySortKundliList.setVisibility(View.VISIBLE);//ADDED BY BIJENDRA
         * ON 21-05-14 } } } catch (UIDataOperationException e) { MyCustomToast
         * mct = new MyCustomToast(activity,
         * activity.getLayoutInflater(), activity, Typeface.DEFAULT);
         * mct.show(e.getMessage()); } finally {
         * if(cDatabaseHelper!=null)cDatabaseHelper.close(); }
         */

        try {

            mapHoroID = new ControllerManager().searchLocalKundliListOperation(
                    activity.getApplicationContext(), kundliName,
                    CGlobalVariables.BOTH_GENDER, noOfRecords);
            lstSearchKundli.setAdapter(null);
            //lstSearchMyKundli.setAdapter(null);
            if (mapHoroID != null) {
                // laySortKundliList.setVisibility(View.VISIBLE);//ADDED BY
                // BIJENDRA ON 21-05-14
                showOfflineSearchedKundli();
                setMakeKundliVisibility(false);
            } else {
                if (showMessage) {
                    // laySortKundliList.setVisibility(View.GONE);//ADDED BY
                    // BIJENDRA ON 21-05-14
                    MyCustomToast mct = new MyCustomToast(activity,
                            activity.getLayoutInflater(), activity,
                            ((BaseInputActivity) activity).regularTypeface);
                    if (editTextSearchKundli.getText().toString().trim()
                            .length() > 0) {
                        mct.show(activity.getResources().getString(
                                R.string.kundli_not_found));
                    } else {
                        mct.show(activity.getResources().getString(
                                R.string.no_kundli_found));
                    }
                } else {
                    //textViewRecentCharts.setVisibility(View.GONE);
                    // laySortKundliList.setVisibility(View.VISIBLE);//ADDED BY
                    // BIJENDRA ON 21-05-14
                }

                setMakeKundliVisibility(true);
            }
        } catch (UIDataOperationException e) {
            MyCustomToast mct = new MyCustomToast(activity, activity
                    .getLayoutInflater(), activity, Typeface.DEFAULT);
            mct.show("Error" + e.getMessage());
        } catch (Exception e) {

        }

    }

    private void setMakeKundliVisibility(boolean isVisible) {

        if (activity instanceof MychartActivity) {

            if (isVisible) {
                create_kundli.setVisibility(View.VISIBLE);
                select_from_below_text.setVisibility(View.GONE);
            } else {
                create_kundli.setVisibility(View.GONE);
                select_from_below_text.setVisibility(View.VISIBLE);
            }
        }

    }


    /**
     * This function is used to format kundlies list after online search
     *
     * @author Bijendra(21 - 05 - 14)
     */
    private void formatOnlineSearchedKundlies() {
        lstonlineChart = new ArrayList<KundliRecord>();

        /*Collection<String> chartNames1 = onlineChartArray.values();
        Collection<String> chartKey1 = onlineChartArray.keySet();
        /*Iterator<String> itr = chartNames1.iterator();
        Iterator<String> iti = chartKey1.iterator();
        for (int i = 0; i < onlineChartArray.size(); i++) {
            lstonlineChart.add(new KundliRecord(iti.next().trim(), itr.next()
                    .trim()));
        }*/

    }

    /*
     * //ONLINE KUNDLI FUNCTION private List<String> formatOnlineChartNameList()
     * { List<String> list = new ArrayList<String>(); Collection<String>
     * chartNames = onlineChartArray.values(); Iterator<String> itr =
     * chartNames.iterator(); for(int i=0;i<onlineChartArray.size();i++) {
     * list.add(itr.next().trim()); }
     *
     *
     *
     * return list; } //ONLINE KUNDLI FUNCTION private List<String>
     * formatOnlineChartIdList() { List<String> list = new ArrayList<String>();
     * Collection<String> chartNames = onlineChartArray.keySet();
     * Iterator<String> itr = chartNames.iterator(); for(int
     * i=0;i<onlineChartArray.size();i++) { list.add(itr.next()); }
     * return list; }
     */

    // OFFLINE KUNDLI FUNCTION

    /**
     * This function is used to format kundlies list after offline search
     *
     * @author Bijendra(21 - 05 - 14)
     */
    private void formatOfflineSearchedKundlies() {
        lstofflineChart = new ArrayList<KundliRecord>();

        Collection<String> chartNames1 = mapHoroID.values();
        Collection<String> chartKey1 = mapHoroID.keySet();
        Iterator<String> itr = chartNames1.iterator();
        Iterator<String> iti = chartKey1.iterator();
        for (int i = 0; i < mapHoroID.size(); i++) {
            lstofflineChart.add(new KundliRecord(iti.next().trim(), itr.next()
                    .trim()));
        }

    }

    /* private ArrayList<BeanHoroPersonalInfo> loadAllLocalKundli() {
         ArrayList<BeanHoroPersonalInfo> localSavedKundli = new ArrayList<BeanHoroPersonalInfo>();
         try {


             Map<String, String> mapHoroID = new ControllerManager().searchLocalKundliListOperation(
                     activity.getApplicationContext(), "",
                     CGlobalVariables.BOTH_GENDER, -1);
             if (mapHoroID != null) {
                 Collection<String> chartKey1 = mapHoroID.keySet();
                 Iterator<String> iti = chartKey1.iterator();
                 while (iti.hasNext()) {
                     localSavedKundli.add(getBeanPersonalInfo(Long.parseLong(iti.next().trim())));
                     Collections.sort(localSavedKundli, new MySalaryComp());

                 }
             }
             //setListAdapter(null, localSavedKundli);
         } catch (Exception e) {

         }
         return localSavedKundli;
     }*/
    private ArrayList<BeanHoroPersonalInfo> loadAllLocalKundli(int startIndex) {
        ArrayList<BeanHoroPersonalInfo> localSavedKundli = null;
        try {


            localSavedKundli = new ControllerManager().searchLocalKundliListOperation(
                    activity.getApplicationContext(),
                    startIndex, localKundliLimit);
           /* if (mapHoroID != null) {
                Collection<String> chartKey1 = mapHoroID.keySet();
                Iterator<String> iti = chartKey1.iterator();
                while (iti.hasNext()) {
                    localSavedKundli.add(getBeanPersonalInfo(Long.parseLong(iti.next().trim())));
                    Collections.sort(localSavedKundli, new MySalaryComp());

                }
            }*/
            //setListAdapter(null, localSavedKundli);
        } catch (Exception e) {

        }
        if(localSavedKundli == null){
            localSavedKundli = new ArrayList<BeanHoroPersonalInfo>();
        }
        return localSavedKundli;
    }


    class MySalaryComp implements Comparator<BeanHoroPersonalInfo> {

        @Override
        public int compare(BeanHoroPersonalInfo e1, BeanHoroPersonalInfo e2) {
            if (e1.getName().compareTo(e2.getName()) >= 0) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    private void getLocalSavedKundli(String kundliName) {
        if(activity == null) return;
        try {
           /* if (allSavedKundli == null) {
                allSavedKundli = new ArrayList<>();
            }*/
            ArrayList<BeanHoroPersonalInfo> localSavedKundli = new ArrayList<BeanHoroPersonalInfo>();
            Map<String, String> mapHoroID = new ControllerManager().searchLocalKundliListOperation(
                    activity.getApplicationContext(), kundliName,
                    CGlobalVariables.BOTH_GENDER, -1);
            if (mapHoroID != null) {
                Collection<String> chartKey1 = mapHoroID.keySet();
                Iterator<String> iti = chartKey1.iterator();
                while (iti.hasNext()) {
                    localSavedKundli.add(getBeanPersonalInfo(Long.parseLong(iti.next().trim())));
                }
                if (editTextSearchKundli.getAdapter() == null) {
                    if(activity != null) {
                        AutoCompleteTextviewAdapter autoCompleteTextviewAdapter = new AutoCompleteTextviewAdapter(activity, R.id.birth_detail_serch_layout, localSavedKundli, true);
                        editTextSearchKundli.setAdapter(autoCompleteTextviewAdapter);
                    }
                } else {
                    ((AutoCompleteTextviewAdapter) editTextSearchKundli.getAdapter()).replaceItem(localSavedKundli);
                }
                editTextSearchKundli.showDropDown();
                editTextSearchKundli.setThreshold(1);
            } else {
                ArrayList<BeanHoroPersonalInfo> recentSearchKundli = (ArrayList) CUtils.getRecentSearchKundli(activity);
                if (CUtils.isConnectedWithInternet(activity)) {
                    if (CUtils.isUserLogedIn(activity)) {
                        progressBar.setVisibility(View.VISIBLE);
                    } else if (recentSearchKundli == null || recentSearchKundli.size() == 0) {
                        noKundliError.setVisibility(View.VISIBLE);
                    }
                }
            }

            if (CUtils.isConnectedWithInternet(activity)) {
                if (CUtils.isUserLogedIn(activity)) {

                    CUtils.googleAnalyticSendWitPlayServie(
                            activity,
                            CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                            CGlobalVariables.GOOGLE_ANALYTIC_ACTION_ASTROSAGE_CLOUD_KUNDLI_SEARCH,
                            null);
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ACTION_ASTROSAGE_CLOUD_KUNDLI_SEARCH, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                    String key = CUtils.getApplicationSignatureHashCode(activity);
                    new ControllerManager().getOnlineChartList(activity,
                            kundliName, CUtils.getUserName(activity),
                            CUtils.getUserPassword(activity), key, "1");
                }
            }


        } catch (Exception e) {
            //Log.e("Error", "" + e.getMessage());
        }
    }

    /**
     * This function is used to show online searched kundlies list
     *
     * @author Bijendra(21 - 05 - 14)
     */
    public void showOnlineSearchedKundli(ArrayList<BeanHoroPersonalInfo> onlineSavedKundli) {
        progressBar.setVisibility(View.INVISIBLE);
        if (editTextSearchKundli.getAdapter() == null) {
            if(activity != null) {
                AutoCompleteTextviewAdapter autoCompleteTextviewAdapter = new AutoCompleteTextviewAdapter(activity, R.id.birth_detail_serch_layout, onlineSavedKundli, false);
                //Set the number of characters the user must type before the drop down list is shown
                editTextSearchKundli.setThreshold(1);
                editTextSearchKundli.setAdapter(autoCompleteTextviewAdapter);
                editTextSearchKundli.showDropDown();
                editTextSearchKundli.setThreshold(1);
            }
        } else {
            ((AutoCompleteTextviewAdapter) editTextSearchKundli.getAdapter()).addItem(onlineSavedKundli);
        }
    }

    /**
     * This function is used to show offline searched kundlies list
     *
     * @author Bijendra(21 - 05 - 14)
     */
    private void showOfflineSearchedKundli() {
        /*
         * searchKundliListAdapter = new SearchKundliListAdapter(activity,
         * android.R.layout.simple_list_item_1,
         * formatOfflineChartNameList(),formatOfflineChartIdList());
         * lstSearchKundli.setAdapter(searchKundliListAdapter);
         */

        // laySortKundliList.setVisibility(View.VISIBLE);//ADDED BY BIJENDRA ON
        // 21-05-14
        formatOfflineSearchedKundlies();
        SortKundlies rc = new SortKundlies(sortList, Field.S);
        Collections.sort(lstofflineChart, rc);

        setListAdapter(lstofflineChart, null, 0);
        //setListAdapterMyChart(lstofflineChart, null);

        /*
         * if((lstofflineChart.size()<1) || (lstofflineChart==null))
         * laySortKundliList.setVisibility(View.GONE);//ADDED BY BIJENDRA ON
         * 21-05-14
         */
    }

    /**
     * This function is used to sort offline kundli list
     *
     * @author Bijendra(21 - 05 - 14)
     */
    private void sortOfflineKundli() {

        if (sortList == Sort.ASCENDING)
            sortList = Sort.DESCENDING;
        else
            sortList = Sort.ASCENDING;

        SortKundlies rc = new SortKundlies(sortList, Field.S);
        Collections.sort(lstofflineChart, rc);

        setListAdapter(lstofflineChart, null, 0);
        //setListAdapterMyChart(lstofflineChart, null);

    }

    /**
     * This function is used to sort online kundli list
     *
     * @author Bijendra(21 - 05 - 14)
     */
    private void sortOnlineKundli() {

        if (sortList == Sort.ASCENDING)
            sortList = Sort.DESCENDING;
        else
            sortList = Sort.ASCENDING;

        SortKundlies rc = new SortKundlies(sortList, Field.S);
        Collections.sort(lstonlineChart, rc);

        setListAdapter(null, onlineSavedKundli, 0);
        //setListAdapterMyChart(null, onlineSavedKundli);


    }


    // THIS CLASS IS WRITTEN BY BIJENDRA ON 2 APRIL 2012
    /*class CSearchOnlineKundliSync extends AsyncTask<String, Long, Void> {
        String _kundliName = "", msg = "";
        CustomProgressDialog pd = null;

        public CSearchOnlineKundliSync(String kundliName) {
            _kundliName = kundliName;
        }

        @Override
        protected Void doInBackground(String... arg0) {
           *//* try {
                onlineChartArray = new ControllerManager().getOnlineChartList(
                        _kundliName, CUtils.getUserName(activity),
                        CUtils.getUserPassword(activity));
            } catch (UICOnlineChartOperationException e) {
                msg = e.getMessage();
            }*//*
            return null;
        }

        protected void onPreExecute() {
            lstSearchKundli.setAdapter(null);
            //lstSearchMyKundli.setAdapter(null);
            //onlineChartArray = null;
            *//*
     * pd = ProgressDialog.show(activity, null, getResources()
     * .getString(R.string.msg_please_wait), true, false); TextView
     * tvMsg = (TextView) pd.findViewById(android.R.id.message);
     * tvMsg.setTypeface(typeface); tvMsg.setTextSize(20);
     *//*

            pd = new CustomProgressDialog(activity, ((BaseInputActivity) activity).regularTypeface);
            pd.show();
        }

        protected void onPostExecute(final Void unused) {
            try {
                if (pd != null & pd.isShowing())
                    pd.dismiss();
            } catch (Exception e) {

            }
            if (true) {
                //(onlineChartArray != null) && (onlineChartArray.size() > 0)showOnlineSearchedKundli();
            } else {
                // laySortKundliList.setVisibility(View.GONE);//ADDED BY
                // BIJENDRA ON 21-05-14
                if ((msg != null) && (msg.length() > 0)) {
                    MyCustomToast mct2 = new MyCustomToast(activity,
                            activity.getLayoutInflater(), activity,
                            Typeface.DEFAULT);
                    mct2.show(msg);
                } else {
                    MyCustomToast mct = new MyCustomToast(activity,
                            activity.getLayoutInflater(), activity,
                            ((BaseInputActivity) activity).regularTypeface);
                    if (editTextSearchKundli.getText().toString().trim()
                            .length() > 0) {
                        mct.show(activity.getResources().getString(
                                R.string.kundli_not_found));
                    } else {
                        mct.show(activity.getResources().getString(
                                R.string.no_kundli_found));
                    }
                }
            }
        }
    }*/

    /*// DELETE ONLINE KUNDLI
    class CDeleteOnlineKundliSync extends AsyncTask<String, Long, Void> {
        BeanHoroPersonalInfo beanHoroPersonalInfo;
        long onlineKundliId = -1;
        long localKundliId = -1;
        CustomProgressDialog pd = null;
        String msg = "";
        int _deleteReturnValue = 0;
        // CDatabaseHelper cDatabaseHelper=null;
        // SQLiteDatabase sQLiteDatabase=null;
        boolean isDeletedOnlineIdFromLocalDb = false;


        public CDeleteOnlineKundliSync(BeanHoroPersonalInfo beanHoroPersonalInfo) {
            this.beanHoroPersonalInfo = beanHoroPersonalInfo;
            onlineKundliId = Long.parseLong(beanHoroPersonalInfo.getOnlineChartId());
            localKundliId = beanHoroPersonalInfo.getLocalChartId();
        }

        @Override
        protected Void doInBackground(String... paramArrayOfParams) {

            try {
                _deleteReturnValue = new ControllerManager().deleteOnlineKundli(onlineKundliId,
                        CUtils.getUserName(activity), CUtils.getUserPassword(activity));
            } catch (UICOnlineChartOperationException e) {
                msg = e.getMessage();
            } catch (Exception e) {
                msg = "" + e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            // lstSearchKundli.setAdapter(null);
            *//*
     * pd = ProgressDialog.show(activity, null, getResources()
     * .getString(R.string.msg_please_wait), true, false); TextView
     * tvMsg = (TextView) pd.findViewById(android.R.id.message);
     * tvMsg.setTypeface(typeface); tvMsg.setTextSize(20);
     *//*

            pd = new CustomProgressDialog(activity, ((BaseInputActivity) activity).regularTypeface);
            pd.show();
        }

        protected void onPostExecute(final Void unused) {
            try {
                if (pd != null & pd.isShowing())
                    pd.dismiss();
            } catch (Exception e) {

            }
            if (activity != null) {
                if (_deleteReturnValue == 1) {
                    MyCustomToast mct = new MyCustomToast(activity,
                            activity.getLayoutInflater(), activity,
                            ((BaseInputActivity) activity).regularTypeface);
                    mct.show(activity.getResources().getString(R.string.kundli_deleted));
                    // delete online chart id from local database

                } else {
                    if (msg.length() > 0) {
                        MyCustomToast mct = new MyCustomToast(activity,
                                activity.getLayoutInflater(), activity,
                                Typeface.DEFAULT);
                        mct.show(msg);
                    }

                }
            }

            try {
                isDeletedOnlineIdFromLocalDb = new ControllerManager().deleteLocalKundliOperation(activity, localKundliId, onlineKundliId);
                //Log.i("isDeletemLocalDb >>", isDeletedOnlineIdFromLocalDb + "");
            } catch (Exception e) {
                e.printStackTrace();
            }
            *//*
     * finally { if(cDatabaseHelper!=null)cDatabaseHelper.close(); }
     *//*
            if (editTextSearchKundli.getAdapter() != null) {
                ((AutoCompleteTextviewAdapter) editTextSearchKundli.getAdapter()).deleteKundli(onlineKundliId, false);
                editTextSearchKundli.showDropDown();
                editTextSearchKundli.setThreshold(1);
                showOnlineKundliAfterDelete(onlineKundliId);
            }

            _iSearchBirthDetailsFragment.oneChartDeleted(onlineKundliId, true);
            deleteRecentKundli(onlineKundliId, false);

        }

    }*/

  /*  // GET ONLINE kundli DETAIL
    class CFetchOnlineKundliDetailSync extends AsyncTask<String, Long, Void> {
        BeanHoroPersonalInfo _horoscope = null;
        long _kundliId = -1;
        CustomProgressDialog pd = null;
        String msg = "";
        // SQLiteDatabase sQLiteDatabase = null;
        // CDatabaseHelper cDatabaseHelper=null;
        boolean isFetchOnlineKundliDetail = true;

        public CFetchOnlineKundliDetailSync(long kundliId) {
            _kundliId = kundliId;
        }

        @Override
        protected Void doInBackground(String... paramArrayOfParams) {
            // TODO Auto-generated method stub

            try {

				*//*
     * cDatabaseHelper=new
     * ControllerManager().getDatabaseHelperObject(activity);
     *
     * sQLiteDatabase = cDatabaseHelper.getWritableDatabase();
     * _horoscope=new
     * ControllerManager().getOnlineKundliDetail(sQLiteDatabase
     * ,_kundliId,
     * CUtils.getUserName(activity),CUtils.getUserPassword
     * (activity));
     *//*
                _horoscope = new ControllerManager().getOnlineKundliDetailNew(
                        activity.getApplicationContext(), _kundliId,
                        CUtils.getUserName(activity),
                        CUtils.getUserPassword(activity));
            } catch (UICOnlineChartOperationException e) {
                // TODO Auto-generated catch block
                msg = e.getMessage();
                isFetchOnlineKundliDetail = false;
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            *//*
     * pd = ProgressDialog.show(activity, null, getResources()
     * .getString(R.string.msg_please_wait), true, false); TextView
     * tvMsg = (TextView) pd.findViewById(android.R.id.message);
     * tvMsg.setTypeface(typeface); tvMsg.setTextSize(20);
     *//*
            pd = new CustomProgressDialog(activity, ((HomeInputScreen) activity).regularTypeface);
            pd.show();
        }

        protected void onPostExecute(final Void unused) {
            try {
                if (pd != null & pd.isShowing())
                    pd.dismiss();
            } catch (Exception e) {

            }
            // if(cDatabaseHelper!=null)cDatabaseHelper.close();
            if (isFetchOnlineKundliDetail)
                fireEventToSendHoroscopeDetail(_horoscope, true);
        }

    }*/

    private void showOnlineKundliAfterDelete(long _kundliId) {
        if (onlineSavedKundli != null) {
            int removePosition = 0;
            for (int i = 0; i < onlineSavedKundli.size(); i++) {
                if (Long.parseLong(onlineSavedKundli.get(i).getOnlineChartId()) == _kundliId) {
                    removePosition = i;
                    break;
                }
            }
            onlineSavedKundli.remove(removePosition);
            //searchKundliListAdapterN.deleteItemFromOnlineList(onlineSavedKundli);

            showOnlineSearchedKundli(onlineSavedKundli);
        }
    }

    void openLocalKundli(String kundlId) {
        openLocalKundli(Long.valueOf(kundlId));
    }

    /**
     * @param beanHoroPersonalInfo
     * @param isboyOrGirlKundli    boy:0 girl:1 default value used only for MatchMaking
     */
    public void editRecentKundli(BeanHoroPersonalInfo beanHoroPersonalInfo, int isboyOrGirlKundli) {

        fireEventToSendHoroscopeDetail(beanHoroPersonalInfo, true, isboyOrGirlKundli);
    }

    void openKundli(String kundlId) {
        long kundliIdToOpen = -1;
        if (rbKundliSearchLocal.isChecked()) {
            openLocalKundli(Long.valueOf(kundlId));
        } else {
            //kundliIdToOpen = Integer.parseInt(kundlId);
            fireEventToSendHoroscopeDetail(onlineSavedKundli.get(Integer.parseInt(kundlId)), true, 0);
            //new CFetchOnlineKundliDetailSync(kundliIdToOpen).execute();
        }
    }

    void deleteKundli(String kundliId) {

        long kundliIdToDelete = -1;

        if (rbKundliSearchLocal.isChecked()) {
            //deleteLocalKundli(Long.valueOf(kundliId));
        } else {
            kundliIdToDelete = Long.valueOf(kundliId);
            /*
             * CUtils.googleAnalyticSend(null,
             * CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
             * CGlobalVariables.GOOGLE_ANALYTIC_ACTION_ASTROSAGE_CLOUD_KUNDLI_DELETE
             * , null, 0l);
             */
            CUtils.googleAnalyticSendWitPlayServie(
                    activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_ACTION_ASTROSAGE_CLOUD_KUNDLI_DELETE,
                    null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ACTION_ASTROSAGE_CLOUD_KUNDLI_DELETE, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            // new CDeleteOnlineKundliSync(kundliIdToDelete).execute();
        }
    }

    // ADAPTER

    class SearchKundliListAdapter extends ArrayAdapter<String> {
        private static final int CHILD_ITEM_RESOURCE = R.layout.lay_search_kundli_list_item_row;
        LayoutInflater vi;
        Context _context;
        List<String> _nameList;
        List<String> _idList;
        Activity _activity;

        public SearchKundliListAdapter(Context context, int textViewResourceId,
                                       List<String> list, List<String> idList) {
            super(context, textViewResourceId, list);
            _context = context;
            _nameList = list;
            _idList = idList;
            vi = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return _nameList.size();
        }

        public String getItem(int position) {
            return _nameList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            v = vi.inflate(CHILD_ITEM_RESOURCE, null);
            ExpendedSearchKundliViewHolder holder = new ExpendedSearchKundliViewHolder(
                    v);
            holder.textKundliTitle.setText(_nameList.get(position).toString());
            holder.textKundliTitle.setTag(_idList.get(position));
            holder.textKundliTitle.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View paramView) {
                    String kundlId = paramView.getTag().toString();
                    openKundli(kundlId);
                }
            });
            holder.imageview.setTag(_idList.get(position));
            holder.imageview.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View paramView) {
                    String kundlId = paramView.getTag().toString();
                    //confirmDeleteToKundli(kundlId,false);
                }
            });

            return v;

        }


        class ExpendedSearchKundliViewHolder {

            public TextView textKundliTitle;
            public ImageView imageview;

            public ExpendedSearchKundliViewHolder(View v) {
                this.textKundliTitle = (TextView) v
                        .findViewById(R.id.tvSearchKundliTitle);
                this.imageview = (ImageView) v
                        .findViewById(R.id.imageViewDeleteKundliFromList);
            }

        }
    }

    public void clearOnlineChartsListAfterSignOut() {
        if (rbKundliSearchOnline.isChecked()) {
            lstSearchKundli.setAdapter(null);
            //lstSearchMyKundli.setAdapter(null);
        }
    }

    /*
     * This adapter is used to show kundli list
     *
     * @author Bijendra(21-05-14)
     */
   /* class SearchKundliListAdapterKundliRecord extends ArrayAdapter<String> {
        private static final int CHILD_ITEM_RESOURCE = R.layout.lay_search_kundli_list_item_row;
        LayoutInflater vi;
        Context _context;
        List<KundliRecord> _nameList;
        Activity _activity;

        public SearchKundliListAdapterKundliRecord(Context context,
                                                   int textViewResourceId, List<KundliRecord> list) {
            super(context, textViewResourceId);
            _context = context;
            _nameList = list;
            vi = (LayoutInflater) _context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        public int getCount() {
            return _nameList.size();
        }

        public String getItem(int position) {
            // return _nameList.get(position);
            return _nameList.get(position).name;
        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            View v = convertView;
            v = vi.inflate(CHILD_ITEM_RESOURCE, null);
            ExpendedSearchKundliViewHolder holder = new ExpendedSearchKundliViewHolder(
                    v);
            holder.textKundliTitle.setText(_nameList.get(position).name
                    .toString());
            holder.textKundliTitle.setTag(_nameList.get(position).id);
            holder.textKundliTitle.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View paramView) {
                    String kundlId = paramView.getTag().toString();
                    openKundli(kundlId);
                }
            });
            holder.imageview.setTag(_nameList.get(position).id);
            holder.imageview.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View paramView) {
                    String kundlId = paramView.getTag().toString();
                    //   confirmDeleteToKundli(kundlId, false);
                }
            });

            return v;

        }


        class ExpendedSearchKundliViewHolder {

            public TextView textKundliTitle;
            public ImageView imageview;

            public ExpendedSearchKundliViewHolder(View v) {
                this.textKundliTitle = (TextView) v
                        .findViewById(R.id.tvSearchKundliTitle);
                this.imageview = (ImageView) v
                        .findViewById(R.id.imageViewDeleteKundliFromList);
            }

        }
    }*/

    // ADDED BY BIJENDRA ON 20-05-14

    /*
     * This class is used to store kundli detail
     *
     * @author Bijendra(21-05-14)
     */
    class KundliRecord {

        private String name;
        private String id;
        private String place;
        private String birthDate;
        private String birthTime;

        public KundliRecord(String number, String name) {
            this.name = name;
            this.id = number;
        }

        public KundliRecord(String number, String name, String place,
                            String birthDate, String birthTime) {
            this.name = name;
            this.id = number;
            this.place = place;
            this.birthDate = birthDate;
            this.birthTime = birthTime;
        }

        @Override
        public String toString() {
            return name + " " + id;
        }

        public int compareTo(Field field, KundliRecord record) {
            switch (field) {
                case S:
                    return this.name.toLowerCase().compareTo(
                            record.name.toLowerCase());
                // case D: return this.id.compareTo(record.id);
                default:
                    throw new IllegalArgumentException("Unable to sort Records by "
                            + field.getType());
            }
        }
    }

    enum Sort {
        ASCENDING, DESCENDING;
    }

    enum Field {

        S(String.class), D(Double.class);

        private Class type;

        Field(Class<? extends Comparable> type) {
            this.type = type;
        }

        public Class getType() {
            return type;
        }
    }

    /*
     * This class is used to sort kundli record
     *
     * @author Bijendra(21-05-14)
     */
    class SortKundlies implements Comparator<KundliRecord> {

        private Field field;
        private Sort sort;

        public SortKundlies(Sort sort, Field field) {
            this.sort = sort;
            this.field = field;
        }

        public final int compare(KundliRecord a, KundliRecord b) {
            int result = a.compareTo(field, b);
            if (sort == Sort.ASCENDING)
                return result;
            else
                return -result;
        }
    }

    private void savePersonalKundaliData(
            BeanHoroPersonalInfo beanHoroPersonalInfo) {
        if(activity == null) return;
        CUtils.saveCustomObject(activity, beanHoroPersonalInfo);
//        CUtils.saveStringData(activity,
//                CGlobalVariables.SHOWDIALOGFORPERSONALKUNDALI, "notshow");
//        CUtils.BirthdayNotifications(activity,
//                CGlobalVariables.BIRTHDAY_INTENT_ACTION, 50);
    }


    private void adjustDefaultKundliAfterDeletion(List<KundliRecord> arrayList,
                                                  int postion) {
        if (arrayList != null && arrayList.size() > postion
                && arrayList.get(postion) != null) {
            BeanHoroPersonalInfo beanHoroPersonalInfo = getBeanPersonalInfo(Long
                    .parseLong(arrayList.get(postion).id));
            savePersonalKundaliData(beanHoroPersonalInfo);
            // ((MychartActivity) activity).showPersonalKundliData();
            showPersonalKundliData();
            Toast.makeText(activity, beanHoroPersonalInfo.getName(), Toast.LENGTH_SHORT)
                    .show();

        } else {
            Toast.makeText(activity, "else", Toast.LENGTH_SHORT).show();

        }

    }

    private BeanHoroPersonalInfo getBeanPersonalInfo(long kundliId) {
        BeanHoroPersonalInfo beanHoroPersonalInfo = null;
        try {

            beanHoroPersonalInfo = new ControllerManager()
                    .getLocalKundliDetailOperation(activity
                            .getApplicationContext(), kundliId);

        } catch (UIDataOperationException e) {

        } catch (Exception e) {

        }
        return beanHoroPersonalInfo;

    }

    private void setListAdapter(List<KundliRecord> offlineSavedKundli, List<BeanHoroPersonalInfo> onlineSavedKundli, int recentKundliSize) {

        if (activity instanceof MychartActivity) {
            //set adapter for MyChartActivity (To choose default chart)
            searchKundliListAdapterMyChart = new RecyclerViewAdapterForMyChart(
                    activity,
                    onlineSavedKundli, recentKundliSize);
           /* searchKundliListAdapterMyChart = new RecyclerViewAdapterForMyChart(
                    activity,
                    onlineSavedKundli);*/
            lstSearchKundli.setAdapter(searchKundliListAdapterMyChart);
            try {
                if (onlineSavedKundli == null || onlineSavedKundli.size() == 0) {
                    setMakeKundliVisibility(true);
                } else {
                    setMakeKundliVisibility(false);
                }
                BeanHoroPersonalInfo beanHoroPersonalInfo = (BeanHoroPersonalInfo) CUtils
                        .getCustomObject(activity.getApplication());
                if (beanHoroPersonalInfo == null) {
                    personKundaliDetail.setVisibility(View.GONE);
                } else {
                    personKundaliDetail.setVisibility(View.VISIBLE);
                    create_kundli.setVisibility(View.GONE);//added by neeraj 17/6/16
                }
            } catch (Exception ex) {
                //
            }


        } else {
            searchKundliListAdapterN = new RecyclerViewAdapter(
                    activity, onlineSavedKundli, recentKundliSize);
            lstSearchKundli.setAdapter(searchKundliListAdapterN);
        }

    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<BeanHoroPersonalInfo> recentViewedKundli;
        Context context;
        int recentListSize = 0;
        private final int VIEW_TYPE_ITEM = 0;
        private final int VIEW_TYPE_LOADING = 1;
        private boolean isLoading = true;
        private int visibleThreshold = 5;
        private int lastVisibleItem, totalItemCount;
        private OnLoadMoreListener onLoadMoreListener;


        public RecyclerViewAdapter(Context context, List<BeanHoroPersonalInfo> recentViewedKundli) {
            this.recentViewedKundli = recentViewedKundli;
            this.context = context;
            totalItemCount = recentViewedKundli.size();
        }

        public RecyclerViewAdapter(Context context, List<BeanHoroPersonalInfo> recentViewedKundli, int recentListSize) {
            this.recentViewedKundli = recentViewedKundli;
            this.context = context;
            this.recentListSize = recentListSize;


        }

        @Override
        public int getItemViewType(int position) {
            return recentViewedKundli.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
        }

        public void deleteItemFromOnlineList(ArrayList<BeanHoroPersonalInfo> recentViewedKundli) {
            this.recentViewedKundli = recentViewedKundli;
            notifyDataSetChanged();
        }

        public void deleteOnlineKundli() {
            ((BaseInputActivity) context).deleteOnlineKundliFromList((ArrayList) this.recentViewedKundli);
            notifyDataSetChanged();
        }

        // Create new views (invoked by the layout manager)
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.save_kundali_list_item_lay, parent, false);
            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;


        }

       /* @Override
        public int getItemViewType(int position) {
            return recentViewedKundli.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
        }*/

        @Override
        public int getItemCount() {
            return recentViewedKundli == null ? 0 : recentViewedKundli.size();
        }


        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

            // - get data from your itemsData at this position
            // - replace the contents of the view with that itemsData

            try {
                if (recentListSize > 0) {
                    if (position == 0) {
                        ((ViewHolder) viewHolder).heading.setText(getResources().getString(R.string.lastfivecharts));
                        ((ViewHolder) viewHolder).heading.setVisibility(View.VISIBLE);
                    } else if (position == recentListSize) {
                        ((ViewHolder) viewHolder).heading.setText(getResources().getString(R.string.Load_All_Local_Kundli));
                        ((ViewHolder) viewHolder).heading.setVisibility(View.VISIBLE);
                    } else {
                        ((ViewHolder) viewHolder).heading.setVisibility(View.GONE);
                    }
                    ((ViewHolder) viewHolder).heading.setTypeface(((BaseInputActivity) activity).robotMediumTypeface);
                }

                BeanHoroPersonalInfo beanHoroPersonalInfo = recentViewedKundli.get(position);
                BeanDateTime beanDateTime = beanHoroPersonalInfo.getDateTime();
                BeanPlace beanPlace = beanHoroPersonalInfo.getPlace();
                String name = beanHoroPersonalInfo.getName();
                //added by neeraj for defalt state agra not to show 8/4/16
                String state = beanPlace.getState(), country = beanPlace.getCountryName();
                if (state.trim().equals("Agra") || country.trim().equals("not define")) {
                    state = "";
                    country = "";
                }
                String dob = beanDateTime.getDay() + "-" + monthNames[beanDateTime.getMonth()] + "-" + beanDateTime.getYear();
                String dot = beanDateTime.getHour() + ":" + beanDateTime.getMin() + ":" + beanDateTime.getSecond();
                String dop = beanPlace.getCityName();
                if (!state.equals("")) {
                    dop = dop + ", " + state;
                }
                if (!country.equals("")) {
                    dop = dop + ", " + country;
                }
                ((ViewHolder) viewHolder).personName.setText(name);
               /* viewHolder.birthDate.setText(": " + dob);
                viewHolder.birthTime.setText(": " + dot);

                viewHolder.birthPlace.setText(": " + dop);*/
                ((ViewHolder) viewHolder).birthDate.setText(dob + ", " + dot + ", " + dop);
               /* viewHolder.birthTime.setText(", " + dot);

                viewHolder.birthPlace.setText(", " + dop);*/
                ((ViewHolder) viewHolder).birthDateLabel.setTypeface(((BaseInputActivity) activity).mediumTypeface);
                ((ViewHolder) viewHolder).birthTimeLabel.setTypeface(((BaseInputActivity) activity).mediumTypeface);
                ((ViewHolder) viewHolder).birthPlacelabel.setTypeface(((BaseInputActivity) activity).mediumTypeface);
                ((ViewHolder) viewHolder).personName.setTypeface(((BaseInputActivity) activity).robotMediumTypeface);
                ((ViewHolder) viewHolder).birthPlace.setTypeface(((BaseInputActivity) activity).robotRegularTypeface);
                ((ViewHolder) viewHolder).birthPlace.setTypeface(((BaseInputActivity) activity).robotRegularTypeface);
                ((ViewHolder) viewHolder).birthPlace.setTypeface(((BaseInputActivity) activity).robotRegularTypeface);
                ((ViewHolder) viewHolder).heading.setTypeface(((BaseInputActivity) activity).mediumTypeface);

                if (beanHoroPersonalInfo.getGender().equals("M") || beanHoroPersonalInfo.getGender().equals("Male")) {
                    ((ViewHolder) viewHolder).genderImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_male));
                } else {
                    ((ViewHolder) viewHolder).genderImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_female));
                }
                if (beanHoroPersonalInfo.getOnlineChartId().equals("") || beanHoroPersonalInfo.getOnlineChartId().equals("-1") || beanHoroPersonalInfo.getOnlineChartId().equals("0")) {
                    ((ViewHolder) viewHolder).cloudIcon.setVisibility(View.GONE);
                } else {
                    ((ViewHolder) viewHolder).cloudIcon.setVisibility(View.VISIBLE);
                }
                /*

                viewHolder.editButton.setImageResource(itemsData[position].getImageUrl());
                viewHolder.viewButton.setText(itemsData[position].getTitle());
                viewHolder.cancelButton.setImageResource(itemsData[position].getImageUrl());*/


            } catch (Exception e) {
                MyCustomToast mct = new MyCustomToast(activity, activity
                        .getLayoutInflater(), activity, Typeface.DEFAULT);
                mct.show("Error" + e.getMessage());
            }

            //viewHolder.genderImage.setText(itemsData[position].getTitle());
        }


        // inner class to hold a reference to each item of RecyclerView
        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView cloudIcon;
            public ImageView genderImage;
            public TextView personName;
            public TextView birthDateLabel;
            public TextView birthTimeLabel;
            public TextView birthPlacelabel;
            public TextView birthDate;
            public TextView birthTime;
            public TextView birthPlace;
            public Button editButton;
            public Button viewButton;
            public Button btnSelect, btnSelectgirl;
            public TextView cancelButton;
            public TextView heading;
            public ImageView moreImage;
            public LinearLayout container;

            public ViewHolder(View itemLayoutView) {
                super(itemLayoutView);
                container = (LinearLayout) itemLayoutView.findViewById(R.id.list_item_container);
                moreImage = (ImageView) itemLayoutView.findViewById(R.id.more_icon);
                cloudIcon = (ImageView) itemLayoutView.findViewById(R.id.cloud_icon);
                genderImage = (ImageView) itemLayoutView.findViewById(R.id.genderImage);
                personName = (TextView) itemLayoutView.findViewById(R.id.personName);
                birthDate = (TextView) itemLayoutView.findViewById(R.id.birthDate);
                birthTime = (TextView) itemLayoutView.findViewById(R.id.birthTime);
                birthPlace = (TextView) itemLayoutView.findViewById(R.id.birthPlace);
                birthDateLabel = (TextView) itemLayoutView.findViewById(R.id.birthDate_label);
                birthTimeLabel = (TextView) itemLayoutView.findViewById(R.id.birthTime_label);
                birthPlacelabel = (TextView) itemLayoutView.findViewById(R.id.birthPlace_label);
                heading = (TextView) itemLayoutView.findViewById(R.id.heading);
                editButton = (Button) itemLayoutView.findViewById(R.id.editButton);
                viewButton = (Button) itemLayoutView.findViewById(R.id.viewButton);
                btnSelect = (Button) itemLayoutView.findViewById(R.id.btnSelect);
                btnSelectgirl = (Button) itemLayoutView.findViewById(R.id.btnSelectgirl);
                cancelButton = (TextView) itemLayoutView.findViewById(R.id.cancelButton);
                cancelButton.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View paramView) {
                        if (recentViewedKundli != null && recentViewedKundli.size() > getLayoutPosition()) {
                            BeanHoroPersonalInfo beanHoroPersonalInfo = recentViewedKundli.get(getLayoutPosition());
                            if (!beanHoroPersonalInfo.getOnlineChartId().equals("") && !beanHoroPersonalInfo.getOnlineChartId().equals("-1")) {
                                confirmDeleteToKundli(beanHoroPersonalInfo, true, false, false);
                            } else if (beanHoroPersonalInfo.getOnlineChartId().equals("") && beanHoroPersonalInfo.getLocalChartId() == -1) {
                                confirmDeleteToKundli(beanHoroPersonalInfo, true, true, true);
                                //deleteRecentKundli(beanHoroPersonalInfo);
                            } else {
                                confirmDeleteToKundli(beanHoroPersonalInfo, true, true, false);
                            }
                        }
                    }
                });
                moreImage.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopup(moreImage, getLayoutPosition());
                    }
                });

                if (activity instanceof HomeMatchMakingInputScreen) {
                    btnSelect.setVisibility(View.VISIBLE);
                    btnSelectgirl.setVisibility(View.VISIBLE);
                    btnSelect.setTypeface(((BaseInputActivity) activity).mediumTypeface);
                    btnSelectgirl.setTypeface(((BaseInputActivity) activity).mediumTypeface);
                    editButton.setVisibility(View.GONE);
                    viewButton.setVisibility(View.GONE);
                    container.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (recentViewedKundli != null && recentViewedKundli.size() > getLayoutPosition()) {
                                if (recentViewedKundli.get(getLayoutPosition()).getGender().equals("M") || recentViewedKundli.get(getLayoutPosition()).getGender().equals("Male")) {
                                    editRecentKundli(recentViewedKundli.get(getLayoutPosition()), 0);
                                } else {
                                    editRecentKundli(recentViewedKundli.get(getLayoutPosition()), 1);
                                }
                                //openDialogForKundliSelection();
                            }
                        }
                    });
                    btnSelect.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View paramView) {
                            //editKundli(getLayoutPosition());
                            selectedButtonposition1 = 0;
                            if (recentViewedKundli != null && recentViewedKundli.size() > getLayoutPosition()) {
                                editRecentKundli(recentViewedKundli.get(getLayoutPosition()), 0);
                            }

                        }
                    });
                    btnSelectgirl.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View paramView) {
                            //editKundli(getLayoutPosition());
                            selectedButtonposition1 = 1;
                            if (recentViewedKundli != null && recentViewedKundli.size() > getLayoutPosition()) {
                                editRecentKundli(recentViewedKundli.get(getLayoutPosition()), 1);
                            }
                        }
                    });

                } else {
                    btnSelect.setVisibility(View.GONE);
                    btnSelectgirl.setVisibility(View.GONE);

                    editButton.setVisibility(View.VISIBLE);
                    viewButton.setVisibility(View.VISIBLE);
                    viewButton.setTypeface(((BaseInputActivity) activity).mediumTypeface);

                    if ((activity instanceof HomeInputScreen) && ((((HomeInputScreen) activity).ASK_QUESTION_QUERY_DATA) || (((HomeInputScreen) activity).ASTROSAGE_CHAT_QUERY_DATA)) || (((HomeInputScreen) activity).ASTRO_SERVICE_QUERY_DATA) || (((HomeInputScreen) activity).ASTRO_PRODUCT_DATA)) {
                        if (((AstrosageKundliApplication) activity.getApplication())
                                .getLanguageCode() == CGlobalVariables.ENGLISH) {
                            viewButton.setText(activity.getResources().getString(R.string.select_profile_text).toUpperCase());
                        } else {
                            viewButton.setText(activity.getResources().getString(R.string.select_profile_text));

                        }
                    }
                    container.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewKundli(getLayoutPosition());
                        }
                    });
                    editButton.setTypeface(((BaseInputActivity) activity).mediumTypeface);
                    editButton.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View paramView) {
                            /* editKundli(getLayoutPosition());*/
                            if (recentViewedKundli != null && recentViewedKundli.size() > getLayoutPosition()) {
                                editRecentKundli(recentViewedKundli.get(getLayoutPosition()), 0);
                            }
                        }
                    });
                    viewButton.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View paramView) {
                            viewKundli(getLayoutPosition());
                        }
                    });
                }
            }
        }

        @TargetApi(11)
        public void showPopup(View v, final int position) {
            try {
                popup = new PopupMenu(context, v);
                MenuInflater inflater = popup.getMenuInflater();
                if (activity instanceof HomeMatchMakingInputScreen) {
                    inflater.inflate(R.menu.matchmakingbuttonmenu, popup.getMenu());
                    popup.getMenu().findItem(R.id.action_match_making_delete).getIcon().setTint(getResources().getColor(R.color.text_color_black, activity.getTheme()));

                } else if ((activity instanceof HomeInputScreen) && ((((HomeInputScreen) activity).ASK_QUESTION_QUERY_DATA) || (((HomeInputScreen) activity).ASTROSAGE_CHAT_QUERY_DATA)) || (((HomeInputScreen) activity).ASTRO_SERVICE_QUERY_DATA) || (((HomeInputScreen) activity).ASTRO_PRODUCT_DATA)) {
                    inflater.inflate(R.menu.askaquestionbuttonmenu, popup.getMenu());
                    popup.getMenu().findItem(R.id.action_edit).getIcon().setTint(getResources().getColor(R.color.text_color_black, activity.getTheme()));
                    popup.getMenu().findItem(R.id.action_view).getIcon().setTint(getResources().getColor(R.color.text_color_black, activity.getTheme()));
                    popup.getMenu().findItem(R.id.action_delete).getIcon().setTint(getResources().getColor(R.color.text_color_black, activity.getTheme()));
                } else if (activity instanceof HomeInputScreen) {
                    inflater.inflate(R.menu.kundlieditanddeletemenu, popup.getMenu());
                    popup.getMenu().findItem(R.id.action_edit).getIcon().setTint(getResources().getColor(R.color.text_color_black, activity.getTheme()));
                    popup.getMenu().findItem(R.id.action_view).getIcon().setTint(getResources().getColor(R.color.text_color_black, activity.getTheme()));
                    popup.getMenu().findItem(R.id.action_delete).getIcon().setTint(getResources().getColor(R.color.text_color_black, activity.getTheme()));
                }
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_edit:
                            case R.id.action_ask_a_question_edit:
                                if (recentViewedKundli != null && recentViewedKundli.size() > position) {
                                    editRecentKundli(recentViewedKundli.get(position), 0);
                                }
                                break;
                            case R.id.action_view:
                                viewKundli(position);
                                break;
                            case R.id.action_delete:
                            case R.id.action_match_making_delete:
                            case R.id.action_ask_a_question_delete:
                                if (recentViewedKundli != null && recentViewedKundli.size() > position) {
                                    BeanHoroPersonalInfo beanHoroPersonalInfo = recentViewedKundli.get(position);
                                    if (!beanHoroPersonalInfo.getOnlineChartId().equals("") && !beanHoroPersonalInfo.getOnlineChartId().equals("-1")) {
                                        confirmDeleteToKundli(beanHoroPersonalInfo, true, false, false);
                                    } else if (beanHoroPersonalInfo.getOnlineChartId().equals("") && beanHoroPersonalInfo.getLocalChartId() == -1) {
                                        confirmDeleteToKundli(beanHoroPersonalInfo, true, true, true);
                                        //deleteRecentKundli(beanHoroPersonalInfo);
                                    } else {
                                        confirmDeleteToKundli(beanHoroPersonalInfo, true, true, false);
                                    }
                                }
                                break;
                            default:
                                break;

                        }
                        return false;
                    }
                });
                popup.show();
            } catch (Exception e){
                //
            }
        }

        public void viewKundli(int layPosition) {
            Log.e("OpneKundli","viewKundli()" );
            if(activity == null) return;
            if (recentViewedKundli != null && recentViewedKundli.size() > layPosition) {

                BeanHoroPersonalInfo beanHoroPersonalInfo = recentViewedKundli.get(layPosition);
                int selectedModule = 0;
                int selectedSubModule = 0;
                boolean isKundliSaved = (!TextUtils.isEmpty(beanHoroPersonalInfo.getOnlineChartId()) && !beanHoroPersonalInfo.getOnlineChartId().equalsIgnoreCase("-1"));
                if (context instanceof HomeInputScreen) {
                    selectedModule = ((HomeInputScreen) context).SELECTED_MODULE;
                    selectedSubModule = ((HomeInputScreen) context).SELECTED_SUB_SCREEN;
                } else {
                    // selectedModule = ((HomeMatchMakingInputScreen) context).SELECTED_MODULE;
                    selectedModule = 0;
                }

                try {
                    if (beanHoroPersonalInfo.getGender().equals("Male")) {
                        beanHoroPersonalInfo.setGender("M");
                    } else if (beanHoroPersonalInfo.getGender().equals("Female")) {
                        beanHoroPersonalInfo.setGender("F");
                    }
                } catch (Exception ex) {
                    //Log.i("Exception", "-" + ex.getMessage());
                }
           /* if (beanHoroPersonalInfo.getLocalChartId() == -1 || beanHoroPersonalInfo.getOnlineChartId().equals("")) {
                CUtils.saveRecentCharts(context, beanHoroPersonalInfo);
            }else{*/
                CUtils.saveKundliInPreference(activity, beanHoroPersonalInfo);
                // }

                if ((activity instanceof HomeInputScreen) && ((HomeInputScreen) activity).ASK_QUESTION_QUERY_DATA) {
                    CalculateKundli kundli = new CalculateKundli(beanHoroPersonalInfo, false, activity, ((BaseInputActivity) context).regularTypeface, selectedModule, CGlobalVariables.HOME_INPUT_SCREEN, false, 0, 0, selectedSubModule);
                    kundli.calculate();
                } else {
                    CalculateKundli kundli = new CalculateKundli(beanHoroPersonalInfo, isKundliSaved, activity, ((BaseInputActivity) context).regularTypeface, selectedModule, CGlobalVariables.HOME_INPUT_SCREEN, false, 0, 0, selectedSubModule);
                    kundli.calculate();
                }

                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_ACTION_VIEW_KUNDLI, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ACTION_VIEW_KUNDLI, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                //((HomeInputScreen) context).calculateKundli(beanHoroPersonalInfo, false);
            }
        }


        // Return the size of your itemsData (invoked by the layout manager)
       /* @Override
        public int getItemCount() {

            if (recentViewedKundli != null) {
                return recentViewedKundli.size();
            } else {
                return 0;
            }
        }*/
    }

    class RecyclerViewAdapterForMyChart extends RecyclerView.Adapter<RecyclerViewAdapterForMyChart.ViewHolderMyChart> {
        // List<KundliRecord> offlineSavedKundli;
        List<BeanHoroPersonalInfo> recentViewedKundli;
        Context context;
        int recentListSize = 0;


        public RecyclerViewAdapterForMyChart(Context context, List<BeanHoroPersonalInfo> recentViewedKundli) {

            this.recentViewedKundli = recentViewedKundli;
            this.context = context;
        }

        public RecyclerViewAdapterForMyChart(Context context, List<BeanHoroPersonalInfo> recentViewedKundli, int recentListSize) {

            this.recentViewedKundli = recentViewedKundli;
            this.context = context;
            this.recentListSize = recentListSize;
          /*  for (int i = 0; i < recentListSize; recentListSize--) {
                this.recentViewedKundli.remove(i);
            }*/
        }

        public void deleteItemFromOnlineList(ArrayList<BeanHoroPersonalInfo> recentViewedKundli) {
            this.recentViewedKundli = recentViewedKundli;
            notifyDataSetChanged();
        }

        public void deleteOnlineKundli() {
            ((BaseInputActivity) context).deleteOnlineKundliFromList((ArrayList) this.recentViewedKundli);
            notifyDataSetChanged();
        }
        // Create new views (invoked by the layout manager)

        @Override
        public RecyclerViewAdapterForMyChart.ViewHolderMyChart onCreateViewHolder(ViewGroup parent,
                                                                                  int viewType) {
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.save_kundali_list_item_lay_mychart, parent, false);
            ViewHolderMyChart viewHolder = new ViewHolderMyChart(itemLayoutView);


            return viewHolder;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolderMyChart viewHolder, int position) {

            // - get data from your itemsData at this position
            // - replace the contents of the view with that itemsData
            try {
                try {
                    if (recentListSize > 0) {
                        if (position == 0) {
                            viewHolder.heading.setText(getResources().getString(R.string.lastfivecharts));
                            viewHolder.heading.setVisibility(View.VISIBLE);
                        } else if (position == recentListSize) {
                            viewHolder.heading.setText(getResources().getString(R.string.Load_All_Local_Kundli));
                            viewHolder.heading.setVisibility(View.VISIBLE);
                        } else {
                            viewHolder.heading.setVisibility(View.GONE);
                        }
                        viewHolder.heading.setTypeface(((BaseInputActivity) activity).robotMediumTypeface);
                    }
                    BeanHoroPersonalInfo beanHoroPersonalInfo = recentViewedKundli.get(position);
                   /* if (onlineSavedKundli != null) {
                        beanHoroPersonalInfo = onlineSavedKundli.get(position);
                    } else {
                        beanHoroPersonalInfo = getBeanPersonalInfo(Long.parseLong(offlineSavedKundli.get(position).id));
                    }*/
                    BeanDateTime beanDateTime = beanHoroPersonalInfo.getDateTime();
                    BeanPlace beanPlace = beanHoroPersonalInfo.getPlace();
                    String name = beanHoroPersonalInfo.getName();
                    //added by neeraj for defalt state agra not to show 8/4/16
                    String state = beanPlace.getState(), country = beanPlace.getCountryName();


                    if (state.trim().equals("Agra") || country.trim().equals("not define")) {
                        state = "";
                        country = "";
                    }/* else {
                        state =  state;
                        country =  country;

                    }*/

                    String dob = beanDateTime.getDay() + "-" + monthNames[beanDateTime.getMonth()] + "-" + beanDateTime.getYear();
                    String dot = beanDateTime.getHour() + ":" + beanDateTime.getMin() + ":" + beanDateTime.getSecond();
                    String dop = beanPlace.getCityName() + ", " + state + ", " + country;
                    if (dop.endsWith(",")) {
                        dop = dop.substring(0, dop.length() - 1);
                    }
                    viewHolder.personName.setText(name);
                   /*cmnt by neeraj
                    viewHolder.birthDate.setText("Birth Date:" + dob );
                    viewHolder.birthTime.setText("Time:" + dot);*/
                    /*viewHolder.birthDateTime.setText(": " + dob);
                    viewHolder.birthTime.setText(": " + dot);
                    viewHolder.birthPlace.setText(": " + dop);*/
                    viewHolder.birthDateTime.setText(dob);
                    viewHolder.birthTime.setText(dot);
                    viewHolder.birthPlace.setText(dop);
                    viewHolder.personName.setTypeface(((BaseInputActivity) activity).robotMediumTypeface);
                    viewHolder.personName.setTypeface(((BaseInputActivity) activity).robotRegularTypeface);
                    viewHolder.birthPlace.setTypeface(((BaseInputActivity) activity).robotRegularTypeface);
                    viewHolder.birthPlace.setTypeface(((BaseInputActivity) activity).robotRegularTypeface);
                    viewHolder.birthPlace.setTypeface(((BaseInputActivity) activity).robotRegularTypeface);
                    if (beanHoroPersonalInfo.getGender().equals("M") || beanHoroPersonalInfo.getGender().equals("Male")) {
                        viewHolder.genderImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_male));
                    } else {
                        viewHolder.genderImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_female));
                    }
                    if (beanHoroPersonalInfo.getOnlineChartId().equals("") || beanHoroPersonalInfo.getOnlineChartId().equals("-1")) {
                        viewHolder.cloudIcon.setVisibility(View.GONE);
                    } else {
                        viewHolder.cloudIcon.setVisibility(View.VISIBLE);
                    }
                    //*//**//*

                    // viewHolder.editButton.setImageResource(itemsData[position].getImageUrl());
                    //viewHolder.viewButton.setText(itemsData[position].getTitle());
                    //viewHolder.cancelButton.setImageResource(itemsData[position].getImageUrl());*//**//*

                } catch (Exception e) {
                    MyCustomToast mct = new MyCustomToast(activity, activity
                            .getLayoutInflater(), activity, Typeface.DEFAULT);
                    mct.show("Error" + e.getMessage());
                }

            } catch (Exception e) {
                MyCustomToast mct = new MyCustomToast(activity, activity
                        .getLayoutInflater(), activity, Typeface.DEFAULT);
                mct.show("Error" + e.getMessage());
            }
            // viewHolder.genderImage.setText(itemsData[position].getTitle());
        }

        // inner class to hold a reference to each item of RecyclerView
        public class ViewHolderMyChart extends RecyclerView.ViewHolder {

            public TextView personName;
            /*  public TextView birthDate;
              public TextView birthTime;*/
            public ImageView cloudIcon;
            public TextView birthDateTime;
            public TextView birthTime;
            public TextView birthPlace;
            public TextView heading;
            public Button editButton;
            public Button viewButton;
            // public TextView cancelButton;
            LinearLayout llMyChart;
            public ImageView genderImage;


            public ViewHolderMyChart(View itemLayoutView) {
                super(itemLayoutView);
                genderImage = (ImageView) itemLayoutView.findViewById(R.id.genderImage);
                personName = (TextView) itemLayoutView.findViewById(R.id.personName);
                birthDateTime = (TextView) itemLayoutView.findViewById(R.id.birthDateTime);
                birthTime = (TextView) itemLayoutView.findViewById(R.id.birthTime);
                birthPlace = (TextView) itemLayoutView.findViewById(R.id.birthPlace);
                editButton = (Button) itemLayoutView.findViewById(R.id.editButton);
                viewButton = (Button) itemLayoutView.findViewById(R.id.viewButton);
                llMyChart = (LinearLayout) itemLayoutView.findViewById(R.id.llMyChart);
                cloudIcon = (ImageView) itemLayoutView.findViewById(R.id.cloud_icon);
                heading = (TextView) itemLayoutView.findViewById(R.id.heading);
                //((TextView) itemLayoutView.findViewById(R.id.pName)).setTypeface(((BaseInputActivity) activity).regularTypeface);
                ((TextView) itemLayoutView.findViewById(R.id.birthDate)).setTypeface(((BaseInputActivity) activity).mediumTypeface);
                ((TextView) itemLayoutView.findViewById(R.id.bTime)).setTypeface(((BaseInputActivity) activity).mediumTypeface);
                ((TextView) itemLayoutView.findViewById(R.id.bPlace)).setTypeface(((BaseInputActivity) activity).mediumTypeface);

                if (searchKundliListAdapterMyChart.getItemCount() <= 1) {
                    BeanHoroPersonalInfo beanHoroPersonalInfo = (BeanHoroPersonalInfo) CUtils
                            .getCustomObject(activity.getApplication());
                    if (beanHoroPersonalInfo != null) {
                        //personKundaliDetail.setVisibility(View.VISIBLE);
                    } else {
                        if (activity instanceof MychartActivity) {
                            select_from_below_text.setVisibility(View.VISIBLE);
                        }
                    }

                } else {

                    BeanHoroPersonalInfo beanHoroPersonalInfo = (BeanHoroPersonalInfo) CUtils
                            .getCustomObject(activity.getApplication());
                    if (beanHoroPersonalInfo != null) {
                        // personKundaliDetail.setVisibility(View.VISIBLE);
                    }
                    if (activity instanceof MychartActivity) {
                        select_from_below_text.setVisibility(View.VISIBLE);
                    }
                }

                llMyChart.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectPersonalKundali(context, recentViewedKundli.get(getLayoutPosition()));
                    }
                });
            }
        }


        // Return the size of your itemsData (invoked by the layout manager)
        @Override
        public int getItemCount() {
            if (recentViewedKundli != null) {
                return recentViewedKundli.size();
            } else {
                return 0;
            }
        }
    }


    private BeanHoroPersonalInfo checkIfAlreadyExistsInLocalDatabase(BeanHoroPersonalInfo beanHoroPersonalInfo) {

        try {
            long kundliId = new ControllerManager()
                    .addEditHoroPersonalInfoOperation(activity, beanHoroPersonalInfo);
            beanHoroPersonalInfo.setLocalChartId(kundliId);
        } catch (Exception e) {
            //Log.i("Exception", "-" + e.getMessage());
        } finally {
            //
        }
        return beanHoroPersonalInfo;
    }


    private void setVisibalityOfSearchLayout(View view, int boolVal) {
        CardView search_layout = (CardView) view.findViewById(R.id.search_layout);
        search_layout.setVisibility(boolVal);
    }

    // open Dialog to select current dialog
    private void openConfimationDialogForPersonalKundali(final Context context,
                                                         final BeanHoroPersonalInfo beanHoroPersonalInfo) {
        FragmentManager fm = ((AppCompatActivity) activity).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("OpenConfimationDialogForPersonalKundali");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        OpenConfimationDialogForPersonalKundali openConfimationDialogForPersonalKundali = OpenConfimationDialogForPersonalKundali.getInstance(beanHoroPersonalInfo);
        openConfimationDialogForPersonalKundali.setTargetFragment(SearchBirthDetailsFragment.this, 0);
        openConfimationDialogForPersonalKundali.show(fm, "OpenConfimationDialogForPersonalKundali");
        ft.commit();
    }

    @Override
    public void openConfirmationDialogForPersonalKundaliCallBack(BeanHoroPersonalInfo beanHoroPersonalInfo) {
        savePersonalKundaliData(beanHoroPersonalInfo);
        showPersonalKundliData();
    }

    public void selectPersonalKundali(final Context context, final BeanHoroPersonalInfo beanHoroPersonalInfo) {
        FragmentManager fm = ((AppCompatActivity) activity).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("MyKundliSelectPersonalKundliDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        // ft.addToBackStack(null);
        MyKundliSelectPersonalKundliDialog myKundliSelectPersonalKundliDialog = MyKundliSelectPersonalKundliDialog.getInstance(beanHoroPersonalInfo);
        myKundliSelectPersonalKundliDialog.setTargetFragment(SearchBirthDetailsFragment.this, 0);
        myKundliSelectPersonalKundliDialog.show(fm, "MyKundliSelectPersonalKundliDialog");
        ft.commit();
    }

    @Override
    public void selectPersonalMyKundali(BeanHoroPersonalInfo beanHoroPersonalInfo) {

        if(activity == null) return;
        CUtils.saveCustomObject(activity, beanHoroPersonalInfo);
        CUtils.BirthdayNotifications(activity, CGlobalVariables.BIRTHDAY_INTENT_ACTION, 50);
       // Log.e("trackCall", "flashLoginActivity on response" );
        showPersonalKundliData();
        makeDefaultKundli(beanHoroPersonalInfo.getOnlineChartId());
        personKundaliDetail.setVisibility(View.VISIBLE);//addde by neeraj for changing the personal kundali firstime 13/4/16
        create_kundli.setVisibility(View.GONE);//added by neeraj 17/6/16
        Intent intent = new Intent(activity, GetDefaultKundliDataService.class);
        intent.putExtra("beanHoroPersonalInfo", beanHoroPersonalInfo);
        activity.startService(intent);

    }

    private void makeDefaultKundli(String chartId){

        try {
            Data input = new Data.Builder()
                    .putString("chartid", chartId).build();

            Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();

            OneTimeWorkRequest requestFindLocation = new OneTimeWorkRequest.Builder(MakeDefaultService.class)
                    .setInputData(input)
                    .setInitialDelay(1, TimeUnit.SECONDS)
                    .setBackoffCriteria(BackoffPolicy.LINEAR,
                            OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                            TimeUnit.MILLISECONDS)
                    .setConstraints(constraints)
                    .addTag("MakeKundliDefault")
                    .build();

            WorkManager.getInstance(activity)
                    .enqueueUniqueWork("MakeKundliDefault", ExistingWorkPolicy.KEEP, requestFindLocation);
        } catch (Exception e) {
            //Log.e("SAN ", " MKD SBDF makeDefaultKundli() exp " + e.toString() );
        }

    }


    public void confirmDeleteToKundli(final BeanHoroPersonalInfo beanHoroPersonalInfo, final boolean isCheckForDeletePersonalKundli, final boolean isLocal, boolean isRecentKundli) {

        FragmentManager fm = ((AppCompatActivity) activity).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("ConfirmDeleteKundliDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        //ft.addToBackStack(null);
        ConfirmDeleteKundliDialog confirmDeleteKundliDialog = ConfirmDeleteKundliDialog.getInstance(beanHoroPersonalInfo, isCheckForDeletePersonalKundli, isLocal, isRecentKundli);
        confirmDeleteKundliDialog.setTargetFragment(SearchBirthDetailsFragment.this, 0);
        confirmDeleteKundliDialog.show(fm, "ConfirmDeleteKundliDialog");
        ft.commit();
    }

    @Override
    public void confirmDeleteKundli(BeanHoroPersonalInfo beanHoroPersonalInfo, boolean isCheckForDeletePersonalKundli, boolean isLocal) {
        if(activity == null) return;
        if (isCheckForDeletePersonalKundli) {
            deletePersonalKundli(String.valueOf(beanHoroPersonalInfo.getLocalChartId()));
        }
        // deleteKundli(kundlId);

        if (isLocal) {
            deleteLocalKundli(beanHoroPersonalInfo, isLocal);
        } else {
            CUtils.googleAnalyticSendWitPlayServie(
                    activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_ACTION_ASTROSAGE_CLOUD_KUNDLI_DELETE,
                    null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ACTION_ASTROSAGE_CLOUD_KUNDLI_DELETE, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            /*cDeleteOnlineKundliSync = new CDeleteOnlineKundliSync(beanHoroPersonalInfo);
            cDeleteOnlineKundliSync.execute();*/
            deleteOnlineKundliSync(beanHoroPersonalInfo);
        }
    }

    /*public void confirmDeleteKundli(BeanHoroPersonalInfo beanHoroPersonalInfo, boolean isCheckForDeletePersonalKundli) {
        if (isCheckForDeletePersonalKundli) {
            deletePersonalKundli(String.valueOf(beanHoroPersonalInfo.getLocalChartId()));
        }
        if (beanHoroPersonalInfo.getOnlineChartId().equals("") ||
                beanHoroPersonalInfo.getOnlineChartId().equals("-1")) {
            if (CUtils.isUserLogedIn(activity)) {
                CUtils.googleAnalyticSendWitPlayServie(
                        activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_ACTION_ASTROSAGE_CLOUD_KUNDLI_DELETE,
                        null);
                cDeleteOnlineKundliSync = new CDeleteOnlineKundliSync(beanHoroPersonalInfo);
                cDeleteOnlineKundliSync.execute();
            } else {
                deleteLocalKundli(beanHoroPersonalInfo, true);
            }
        } else {
            deleteLocalKundli(beanHoroPersonalInfo, true);
        }
    }*/

    public void deleteRecentKundli(BeanHoroPersonalInfo beanHoroPersonalInfo) {
        ArrayList<BeanHoroPersonalInfo> recentKundli = (ArrayList<BeanHoroPersonalInfo>) CUtils.getRecentSearchKundli(activity);

        if (recentKundli != null) {
            for (int i = 0; i < recentKundli.size(); i++) {

                if (recentKundli.get(i).getRecentId() == beanHoroPersonalInfo.getRecentId() ||
                        recentKundli.get(i).getLocalChartId() == beanHoroPersonalInfo.getLocalChartId()) {
                    recentKundli.remove(i);
                }
            }
            //setListAdapter(null, recentKundli, 0);
            CUtils.saveRecentSearchKundli(activity, recentKundli);
            MyCustomToast mct = new MyCustomToast(activity, activity
                    .getLayoutInflater(), activity, ((BaseInputActivity) activity).regularTypeface);
            mct.show(activity.getResources().getString(
                    R.string.kundli_deleted));
        }
    }

    private void deletePersonalKundli(final String kundlId) {
        if(activity == null) return;
        try {
            BeanHoroPersonalInfo beanHoroPersonalInfo = (BeanHoroPersonalInfo) CUtils
                    .getCustomObject(activity.getApplication());
            if (beanHoroPersonalInfo != null) {
                if (String.valueOf(beanHoroPersonalInfo.getLocalChartId()).equals(kundlId)) {
                    CUtils.saveCustomObject(activity, null);
                    personKundaliDetail.setVisibility(View.GONE);
                    iUpdateMenus.updateMenus();
                }
            }
        } catch (Exception ex) {
            //Log.i("Exception", "--" + ex.getMessage());
        }
    }

    private void deleteRecentKundli(long kundliId, boolean isLocal) {
        if(activity == null) return;
        ArrayList<BeanHoroPersonalInfo> recentKundli = (ArrayList<BeanHoroPersonalInfo>) CUtils.getRecentSearchKundli(activity);

        if (recentKundli != null) {
            for (int i = 0; i < recentKundli.size(); i++) {
                if (isLocal) {
                    if (recentKundli.get(i).getLocalChartId() == kundliId) {
                        recentKundli.remove(i);
                    }
                } else {
                    if (!recentKundli.get(i).getOnlineChartId().equals("") && Long.parseLong(recentKundli.get(i).getOnlineChartId()) == kundliId) {
                        recentKundli.remove(i);
                    }
                }
            }
            CUtils.saveRecentSearchKundli(activity, recentKundli);
            if (onlineChartArrayList != null && onlineChartArrayList.size() > 0)
                for (int i = 0; i < onlineChartArrayList.size(); i++) {
                    if (!onlineChartArrayList.get(i).getOnlineChartId().equals("") && Long.parseLong(onlineChartArrayList.get(i).getOnlineChartId()) == kundliId) {
                        onlineChartArrayList.remove(i);
                    }
                }
            if (isRecentTabChecked) {
                loadLocalAndRecentKundli();
                //setListAdapter(null, recentKundli, 0);
            } else {

                setListAdapter(null, onlineChartArrayList, 0);
            }

        }

    }


    public void headerVisibility(boolean b) {
        try {
            if (activity != null) {
                ViewPager viewPager = null;
                View appbarAppModule = activity.findViewById(R.id.appbarAppModule);
                if (activity instanceof HomeInputScreen) {
                    viewPager = ((HomeInputScreen) activity).viewPager;
                } else if (activity instanceof HomeMatchMakingInputScreen) {
                    viewPager = ((HomeMatchMakingInputScreen) activity).viewPager;
                }
                if (activity instanceof MychartActivity || viewPager.getCurrentItem() == 0) {
                    if (b && editTextSearchKundli.hasFocus()) {
                        appbarAppModule.setVisibility(View.GONE);
                        viweForStatusBar.setVisibility(View.VISIBLE);
                        //  activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    } else {
                        appbarAppModule.setVisibility(View.VISIBLE);
                        viweForStatusBar.setVisibility(View.GONE);
               /* activity.getWindow().clearFlags(
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
                    }
                }

            }
        } catch (Exception e) {

        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(activity == null) return;
        if (activity != null && isVisibleToUser) {
            CUtils.hideMyKeyboard(activity);
        } else {
            if (editTextSearchKundli != null) {
                editTextSearchKundli.setText("");
                editTextSearchKundli.setAdapter(null);
            }
            if (noKundliError != null) {
                noKundliError.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
       /* if (cDeleteOnlineKundliSync != null && cDeleteOnlineKundliSync.getStatus() == AsyncTask.Status.RUNNING)
            cDeleteOnlineKundliSync.cancel(true);*/
        iUpdateMenus = null;
        activity = null;
    }

    @Override
    public void onPause() {

        super.onPause();
        //noKundliError.setVisibility(View.GONE);
    }

    /*  public void doActionOnLogout() {

        RecyclerView.Adapter adapter = (RecyclerView.Adapter) lstSearchKundli.getAdapter();
        if (adapter instanceof RecyclerViewAdapter) {
            ((RecyclerViewAdapter) adapter).deleteOnlineKundli();
        } else {
            ((RecyclerViewAdapterForMyChart) adapter).deleteOnlineKundli();
        }
        editTextSearchKundli.setAdapter(null);
    }*/
    /*private void setAlphabetList(View view) {
        RecyclerView list = (RecyclerView) view.findViewById(R.id.sideIndexlistview);
        String[] alphabetList = {
                "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
                "T", "U", "V", "W", "X", "Y", "Z",
        };
        MoviesAdapter alphabetListAdapter = new MoviesAdapter(alphabetList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        list.setLayoutManager(mLayoutManager);
        list.setItemAnimator(new DefaultItemAnimator());
        list.setAdapter(alphabetListAdapter);
    }*/

    private void openDialogForKundliSelection() {
        /*FragmentManager fm = ((AppCompatActivity) activity).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("MatchMakingSelectKundliDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        MatchMakingSelectKundliDialog matchMakingSelectKundliDialog = MatchMakingSelectKundliDialog.getInstance();
        matchMakingSelectKundliDialog.setTargetFragment(SearchBirthDetailsFragment.this, 0);
        matchMakingSelectKundliDialog.show(fm, "MatchMakingSelectKundliDialog");
        ft.commit();*/
    }

    //get online kundlis
    public void getOnlineKundali() {
        final String url = CGlobalVariables.GET_ONLINE_KUNDLI_URL;

        if (pd == null)
            pd = new CustomProgressDialog(activity, ((BaseInputActivity) activity).regularTypeface);
        pd.show();
        pd.setCancelable(false);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideProgressBar();
                        if (response != null && !response.isEmpty()) {
                            parseOnlineKundliData(response);
                        }

                    }
                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (activity != null && error.getMessage() != null) {
                    hideProgressBar();
                    MyCustomToast mct = new MyCustomToast(activity, activity
                            .getLayoutInflater(), activity, ((BaseInputActivity) activity).regularTypeface);
                    mct.show(error.getMessage());
                }

                if (error instanceof TimeoutError) {
                    VolleyLog.d("TimeoutError: " + error.getMessage());
                    //If timeout occur it will again hit the request for get the product list
                    //      loadAstroShopData();
                } else if (error instanceof NoConnectionError) {
                    VolleyLog.d("NoConnectionError: " + error.getMessage());
                } else if (error instanceof AuthFailureError) {
                    VolleyLog.d("AuthFailureError: " + error.getMessage());
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                    VolleyLog.d("ServerError: " + error.getMessage());
                } else if (error instanceof NetworkError) {
                    //TODO
                    VolleyLog.d("NetworkError: " + error.getMessage());
                } else if (error instanceof ParseError) {
                    //TODO
                    VolleyLog.d("ParseError: " + error.getMessage());

                }

            }


        }


        ) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }


            @Override
            public Map<String, String> getParams() {
                HashMap<String, String> headers = new HashMap<String, String>();
                if(activity == null) return headers;
                headers.put("key", CUtils.getApplicationSignatureHashCode(activity));
                headers.put(CGlobalVariables.KEY_USER_ID, CUtils.replaceEmailChar(CUtils.getUserName(activity)));
                headers.put(CGlobalVariables.KEY_PASSWORD, CUtils.getUserPassword(activity));
                //get last chartid for pagination
                if (onlineChartArrayList != null && onlineChartArrayList.size() > 0) {
                    lastOnlineKundliId = onlineChartArrayList.
                            get(onlineChartArrayList.size() - 1).getOnlineChartId();
                } else {
                    lastOnlineKundliId = "";
                }
                headers.put("chartid", lastOnlineKundliId);

                return headers;
            }

        };


        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

        queue.add(stringRequest);
    }

    //parse online kundlis data
    private void parseOnlineKundliData(String response) {
        if (onlineChartArrayList == null) {
            onlineChartArrayList = new ArrayList<>();
        }

        try {
            JSONObject mainJsonObject = new JSONObject(response);
            String resultVal = mainJsonObject.getString("result");
            //totalOnlineCharts = Integer.parseInt(mainJsonObject.getString("totalcharts"));
            //if result is sucess
            if (resultVal.equals("1")) {
                JSONArray mainJsonArray = mainJsonObject.getJSONArray("chartdata");
                lastOnlineArraySize = mainJsonArray.length();
                JSONObject jsonObject;
                BeanHoroPersonalInfo beanHoroPersonalInfo;
                BeanDateTime beanDateTime;
                BeanPlace beanPlace;
                for (int i = 0; i < mainJsonArray.length(); i++) {
                    jsonObject = mainJsonArray.getJSONObject(i);
                    beanHoroPersonalInfo = new BeanHoroPersonalInfo();
                    beanDateTime = new BeanDateTime();
                    beanPlace = new BeanPlace();
                    beanDateTime.setDay(Integer.parseInt(jsonObject.getString("Day")));
                    beanDateTime.setMonth(Integer.parseInt(jsonObject.getString("Month")) - 1);
                    beanDateTime.setYear(Integer.parseInt(jsonObject.getString("Year")));
                    beanDateTime.setHour(Integer.parseInt(jsonObject.getString("Hour")));
                    beanDateTime.setMin(Integer.parseInt(jsonObject.getString("Min")));
                    beanDateTime.setSecond(Integer.parseInt(jsonObject.getString("Sec")));

                    beanPlace.setCityName(jsonObject.getString("Place"));
                    //beanPlace.setCityId(Integer.parseInt(jsonObject.getString("")));
                    //beanPlace.setCountryId(Integer.parseInt(jsonObject.getString("")));
                    beanPlace.setCountryName("");
                    beanPlace.setState("");

                    //beanPlace.setLatitude(jsonObject.getString(""));
                    beanPlace.setLatDeg(jsonObject.getString("LatDeg"));
                    beanPlace.setLatDir(jsonObject.getString("LatNS"));
                    //beanPlace.setLatSec(jsonObject.getString(""));
                    beanPlace.setLatMin(jsonObject.getString("LatMin"));

                    //beanPlace.setLongitude(jsonObject.getString(""));
                    beanPlace.setLongDeg(jsonObject.getString("LongDeg"));
                    beanPlace.setLongDir(jsonObject.getString("LongEW"));
                    //beanPlace.setLongSec(jsonObject.getString(""));
                    beanPlace.setLongMin(jsonObject.getString("LongMin"));

                    //beanPlace.setTimeZone(jsonObject.getString(""));
                    //beanPlace.setTimeZoneId(Integer.parseInt(jsonObject.getString("")));
                    //beanPlace.setTimeZoneName(jsonObject.getString(""));
                    //beanPlace.setTimeZoneString(jsonObject.getString(""));
                    beanPlace.setTimeZoneValue(Float.parseFloat(jsonObject.getString("TimeZone")));
                    beanPlace.setTimeZone(jsonObject.getString("TimeZone"));
                    beanHoroPersonalInfo.setOnlineChartId(jsonObject.getString("ChartId"));
                    beanHoroPersonalInfo.setName(jsonObject.getString("Name"));
                    beanHoroPersonalInfo.setGender(jsonObject.getString("Sex"));
                    beanHoroPersonalInfo.setAyanIndex(Integer.parseInt(jsonObject.getString("Ayanamsa")));
                    beanHoroPersonalInfo.setDST(Integer.parseInt(jsonObject.getString("DST")));
                    beanHoroPersonalInfo.setHoraryNumber(Integer.parseInt(jsonObject.getString("kphn")));
                    beanHoroPersonalInfo.setDateTime(beanDateTime);
                    beanHoroPersonalInfo.setPlace(beanPlace);

                    onlineChartArrayList.add(beanHoroPersonalInfo);
                }
                //set list data
                setListAdapter(null, onlineChartArrayList, 0);
                //show loadmore button if more data are available on server
                if (lastOnlineArraySize >= serverKundliLimit && onlineKundliResultCode != 4) {
                    loadMoreButton.setVisibility(View.VISIBLE);
                } else {
                    loadMoreButton.setVisibility(View.GONE);
                }
                //insert online kundlis on local database
                insertRecordsInDatabase();
                onlineKundliResultCode = 1;
            } else if (resultVal.equals("0")) {
                onlineKundliResultCode = 0;
                showToast("All parameters are required");
            } else if (resultVal.equals("2")) {
                onlineKundliResultCode = 2;
                showToast(getResources().getString(R.string.sign_up_validation_authentication_failed));
            } else if (resultVal.equals("3")) {
                onlineKundliResultCode = 3;
                showToast("Invalid userId and password");
            } else if (resultVal.equals("4")) {
                onlineKundliResultCode = 4;
                showToast(getResources().getString(R.string.no_more_chart_on_server));
                loadMoreButton.setVisibility(View.GONE);
            }


        } catch (Exception e) {
            Log.i("Exception>>", e.getMessage());
        }
    }

    // insert online chartsinlocal database
    private synchronized void insertRecordsInDatabase() {
        if (databaseHelperOperations == null) {
            databaseHelperOperations = new CDatabaseHelperOperations(activity);
        }
        // executeOnExecutor execute asyctask parallel
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            new InsertRecordsInLocalDatabase().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new InsertRecordsInLocalDatabase().execute();
        }

    }

    //insert kundlis in local database
    private class InsertRecordsInLocalDatabase extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = startIndex; i < onlineChartArrayList.size(); i++) {
                databaseHelperOperations.synchDataFromServerToLocal(onlineChartArrayList.get(i));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            startIndex = onlineChartArrayList.size();
        }
    }

    //get local kundlis
    private class GetRecordsfromLocalDatabase extends AsyncTask<Void, Void, Void> {
        int recentListsize = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            lstSearchKundli.setVisibility(View.VISIBLE);
            errorContainer.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(activity == null) return null;
            //get recent kundlis
            ArrayList<BeanHoroPersonalInfo> recentSearchKundli = (ArrayList) CUtils.getRecentSearchKundli(activity);
            //get local kundlis form database
            ArrayList<BeanHoroPersonalInfo> localKundli = loadAllLocalKundli(0);
            if (recentSearchKundli != null) {
                recentAndLocalKundli.addAll(recentSearchKundli);
                recentListsize = recentSearchKundli.size();
            }
            if (localKundli != null) {
                recentAndLocalKundli.addAll(localKundli);
            }
            updateLocalKundliByRemoveHashFromOnlineChartId(recentSearchKundli, localKundli);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setListAdapter(null, recentAndLocalKundli, recentListsize);
            //ArrayList<BeanHoroPersonalInfo> recentSearchKundli = (ArrayList) CUtils.getRecentSearchKundli(activity);
            int size = recentAndLocalKundli.size() - recentListsize;
            if (size >= localKundliLimit) {
                loadMoreButton.setVisibility(View.VISIBLE);
            } else {
                loadMoreButton.setVisibility(View.GONE);
            }

        }
    }

    /** remove hash from online chart id if exists
     * @param recentSearchKundli
     * @param localKundli
     */
    private void updateLocalKundliByRemoveHashFromOnlineChartId(ArrayList<BeanHoroPersonalInfo> recentSearchKundli, ArrayList<BeanHoroPersonalInfo> localKundli){
        try {
            for (int i = 0; i < recentSearchKundli.size(); i++) {
                BeanHoroPersonalInfo beanHoroPersonalInfo = recentSearchKundli.get(i);
                if (beanHoroPersonalInfo == null) {
                    continue;
                }
                String onlineChartId = beanHoroPersonalInfo.getOnlineChartId();
                if(TextUtils.isEmpty(onlineChartId)) {
                    continue;
                }
                //Log.e("onlineChartId", "1= "+onlineChartId);
                if(onlineChartId.contains("#")) {
                    beanHoroPersonalInfo.setOnlineChartId(onlineChartId.split("#")[0]);
                    //Log.e("onlineChartId", "2= "+onlineChartId.split("#")[0]);
                }
            }
            //update all recent kundlis
            CUtils.saveRecentSearchKundli(activity, recentSearchKundli);

            for (int i = 0; i < localKundli.size(); i++) {
                BeanHoroPersonalInfo beanHoroPersonalInfo = localKundli.get(i);
                if (beanHoroPersonalInfo == null) {
                    continue;
                }
                String onlineChartId = beanHoroPersonalInfo.getOnlineChartId();
                if(TextUtils.isEmpty(onlineChartId)) {
                    continue;
                }
                //Log.e("onlineChartId", "3= "+onlineChartId);
                if(onlineChartId.contains("#")) {
                    beanHoroPersonalInfo.setOnlineChartId(onlineChartId.split("#")[0]);
                    try{
                        //update one by one
                        new ControllerManager().addEditHoroPersonalInfoOperation(activity, beanHoroPersonalInfo);
                        //Log.e("onlineChartId", "4= "+onlineChartId.split("#")[0]);
                    }catch (Exception e){
                        Log.e("onlineChartId", "Exception= "+e);
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    private void showToast(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    long onlineKundliId = -1;
    long localKundliId = -1;

    private void deleteOnlineKundliSync(BeanHoroPersonalInfo beanHoroPersonalInfo) {
        showProgressBar();
        onlineKundliId = Long.parseLong(beanHoroPersonalInfo.getOnlineChartId());
        localKundliId = beanHoroPersonalInfo.getLocalChartId();
        String url = CGlobalVariables.ONLINE_CHART_DELETE_URL;
        StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url, this, true, getParams(onlineKundliId), 0).getMyStringRequest();
        queue.add(stringRequest);
    }

    public Map<String, String> getParams(long onlineKundliId) {
        HashMap<String, String> params = new HashMap<String, String>();
        if(activity == null) return params;
        params.put(CGlobalVariables.KEY_USER_ID, CUtils.replaceEmailChar(CUtils.getUserName(activity)));
        params.put(CGlobalVariables.KEY_PASSWORD, CUtils.getUserPassword(activity));
        params.put("chartId", String.valueOf(onlineKundliId));

        return params;
    }

    @Override
    public void onResponse(String response, int method) {
        hideProgressBar();
        String msg = "";
        int _deleteReturnValue = 0;
        boolean isDeletedOnlineIdFromLocalDb = false;
        if (activity != null) {
            try {
                _deleteReturnValue = getChartDeleteStatus(response);
            } catch (Exception e) {
                msg = msg + e.getMessage();
            }

            if (_deleteReturnValue == 1) {
                MyCustomToast mct = new MyCustomToast(activity,
                        activity.getLayoutInflater(), activity,
                        ((BaseInputActivity) activity).regularTypeface);
                mct.show(activity.getResources().getString(R.string.kundli_deleted));
                // delete online chart id from local database

            } else {
                if (msg.length() > 0) {
                    MyCustomToast mct = new MyCustomToast(activity,
                            activity.getLayoutInflater(), activity,
                            Typeface.DEFAULT);
                    mct.show(msg);
                }

            }
        }

        try {
            isDeletedOnlineIdFromLocalDb = new ControllerManager().deleteLocalKundliOperation(activity, localKundliId, onlineKundliId);
            //Log.i("isDeletemLocalDb >>", isDeletedOnlineIdFromLocalDb + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
         * finally { if(cDatabaseHelper!=null)cDatabaseHelper.close(); }
         */
        if (editTextSearchKundli.getAdapter() != null) {
            ((AutoCompleteTextviewAdapter) editTextSearchKundli.getAdapter()).deleteKundli(onlineKundliId, false);
            editTextSearchKundli.showDropDown();
            editTextSearchKundli.setThreshold(1);
            showOnlineKundliAfterDelete(onlineKundliId);
        }

        _iSearchBirthDetailsFragment.oneChartDeleted(onlineKundliId, true);
        deleteRecentKundli(onlineKundliId, false);

    }

    /**
     * This function is used to extract the status after deleting
     * chart on AstroSage server.
     *
     * @param _status
     * @return int(success / fail)
     */
    private int getChartDeleteStatus(String _status) {
        int _result = -1;
        Document doc = CXMLOperations.XMLfromString(_status);
        int numResults = doc.getChildNodes().getLength();
        NodeList nodes = doc.getElementsByTagName("product");
        if (numResults > 0) {
            try {

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD) {
                    XPath xpathDeleteStatus = XPathFactory.newInstance().newXPath();
                    String msgCodeExp = "/product/msgcode"; // chart id
                    NodeList msgCodeNodes;
                    try {
                        msgCodeNodes = (NodeList) xpathDeleteStatus.evaluate(msgCodeExp, doc, XPathConstants.NODESET);
                        _result = Integer.valueOf(msgCodeNodes.item(0).getTextContent());
                    } catch (XPathExpressionException e1) {
                        e1.printStackTrace();
                    }

                } else {
                    for (int i = 0; i < nodes.getLength(); i++) {
                        Element e = (Element) nodes.item(i);
                        _result = Integer.valueOf(CXMLOperations.getValue(e, "msgcode"));
                    }
                }

            } catch (Exception e) {

            }
        }
        return _result;

    }

    @Override
    public void onError(VolleyError error) {

    }

    /**
     * show Progress Bar
     */
    private void showProgressBar() {
        if (pd == null) {
            pd = new CustomProgressDialog(activity, ((BaseInputActivity) activity).regularTypeface);
            pd.setCanceledOnTouchOutside(false);
            if (!pd.isShowing()) {
                pd.show();
            }
        }
    }

    /**
     * hide Progress Bar
     */
    private void hideProgressBar() {
        try {
            if(activity == null || getActivity() == null) return;
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (final IllegalArgumentException e) {
            // Do nothing.
        } catch (final Exception e) {
            // Do nothing.
        } finally {
            pd = null;
        }
    }
}
