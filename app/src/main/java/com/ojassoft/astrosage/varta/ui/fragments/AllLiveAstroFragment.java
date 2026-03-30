package com.ojassoft.astrosage.varta.ui.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.varta.adapters.LiveAstrologerAdapter;
import com.ojassoft.astrosage.varta.model.LiveAstrologerModel;
import com.ojassoft.astrosage.varta.ui.activity.AllLiveAstrologerActivity;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.GridViewSpacing;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllLiveAstroFragment extends Fragment implements VolleyResponse {
    private RecyclerView liveAstroRecyclerView;
    private ArrayList<LiveAstrologerModel> liveAstrologerModelArrayList;
    private LiveAstrologerAdapter liveAstrologerAdapter;
    private Activity activity;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RequestQueue queue;
    Dialog emptyListDialog;
    private String astroProfileUrl;
    private boolean spacingSet = false;
    private static final int PERMISSION_REQ_CODE = 1;
    private String[] PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };
    private BroadcastReceiver liveAstroReceiver;
    private RelativeLayout rl_not_schedule;
    private int FETCH_LIVE_ASTROLOGER = 4;
    private Button btnNoLiveAstro;
    public AllLiveAstroFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_live_astro, container, false);
        activity = getActivity();
        inti(view);
        return view;
    }
    /**
     * @param view
     */
    private void inti(View view) {
        queue = VolleySingleton.getInstance(activity).getRequestQueue();
        liveAstroRecyclerView = view.findViewById(R.id.liveAstroRecyclerView);
        rl_not_schedule = view.findViewById(R.id.rl_not_schedule);
        btnNoLiveAstro = view.findViewById(R.id.btnNoLiveAstro);
        liveAstroRecyclerView.setItemAnimator(null);
        liveAstrologerModelArrayList = new ArrayList<>();
        liveAstrologerAdapter = new LiveAstrologerAdapter(getContext(), liveAstrologerModelArrayList, 1);
        liveAstroRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        liveAstroRecyclerView.setAdapter(liveAstrologerAdapter);

        mSwipeRefreshLayout = view.findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            getLiveAstrologerDataFromServer();
                        } catch (Exception e) {
                            //
                        }
                    }
                }, 500);
            }
        });
        //initLiveAstroReceiver();
        getLiveAstrologerList();
        btnNoLiveAstro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
                Intent dashboardIntent = new Intent(activity, DashBoardActivity.class);
                startActivity(dashboardIntent);
            }
        });
    }
    private void initLiveAstroReceiver(){
        try {
            liveAstroReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                   // Log.d("cdasnCJAD","called broadcaster");
                    getLiveAstrologerDataFromServer();
                }
            };
            LocalBroadcastManager.getInstance(activity).registerReceiver((liveAstroReceiver),
                    new IntentFilter(CGlobalVariables.LIVE_ASTRO_BROAD_ACTION)
            );
        }catch (Exception e){
            //
        }
    }
    private void getLiveAstrologerList() {
        String liveAstroData = CUtils.getLiveAstroList();
        if (!TextUtils.isEmpty(liveAstroData)) {
            parseLiveAstrologerList(liveAstroData);
        }else {
            getLiveAstrologerDataFromServer();

        }
    }

    private void getLiveAstrologerDataFromServer(){
       // Log.d("cdasnCJAD","called getLiveAstrologerDataFromServer");
        if (!CUtils.isConnectedWithInternet(activity)) {
            if (mSwipeRefreshLayout != null) {
                mSwipeRefreshLayout.setRefreshing(false);
                mSwipeRefreshLayout.setEnabled(true);
            }
            Toast.makeText(activity, getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            //CUtils.showSnackbar(navView, getResources().getString(R.string.no_internet), activity);
        } else {
            //if ( ( CUtils.getCurrentTimeStamp() - CUtils.getApiLastHitTime() ) > ( 60 * 1000 ) ) {
                // Log.e("SAN LiveAstro URL2=>", CGlobalVariables.GET_LIVE_ASTRO_URL);
//                StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.GET_LIVE_ASTRO_URL,
//                        this, false, CUtils.getLiveAstroParams(activity,CUtils.getActivityName(activity)), FETCH_LIVE_ASTROLOGER).getMyStringRequest();
//                queue.add(stringRequest);
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.getLiveAstrologerList(CUtils.getLiveAstroParams(getActivity(), CUtils.getActivityName(getActivity())));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.body() != null) {
                            String myResponse = response.body().string();
                            if (mSwipeRefreshLayout != null) {
                                mSwipeRefreshLayout.setRefreshing(false);
                                mSwipeRefreshLayout.setEnabled(true);
                            }
                            CUtils.setApiLastHitTime();
                            CUtils.saveLiveAstroList(myResponse);
                            parseLiveAstrologerList(myResponse);
                        }
                    } catch (Exception e) {
                        //
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    try {
                        if (mSwipeRefreshLayout != null) {
                            mSwipeRefreshLayout.setRefreshing(false);
                            mSwipeRefreshLayout.setEnabled(true);
                        }
                    } catch (Exception e) {
                        //
                    }
                }
            });


        }
    }
    private void parseLiveAstrologerList(String liveAstroData){
        if(TextUtils.isEmpty(liveAstroData)){
            return;
        }
        try {

            JSONObject jsonObject = new JSONObject(liveAstroData);
            JSONArray jsonArray = jsonObject.getJSONArray("astrologers");
            liveAstrologerModelArrayList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                LiveAstrologerModel liveAstrologerModel = CUtils.parseLiveAstrologerObject(object);
                if(liveAstrologerModel == null) continue;
                liveAstrologerModelArrayList.add(liveAstrologerModel);
            }

            if (liveAstrologerModelArrayList.isEmpty()){
                rl_not_schedule.setVisibility(View.VISIBLE);
                liveAstroRecyclerView.setVisibility(View.GONE);
            } else if(liveAstrologerModelArrayList.size()>4){
                rl_not_schedule.setVisibility(View.GONE);
                liveAstroRecyclerView.setVisibility(View.VISIBLE);
                liveAstrologerAdapter = new LiveAstrologerAdapter(getContext(), liveAstrologerModelArrayList,1);
                liveAstroRecyclerView.setLayoutManager(new GridLayoutManager(activity,3));
                liveAstroRecyclerView.setAdapter(liveAstrologerAdapter);
                if(!spacingSet){
                    liveAstroRecyclerView.addItemDecoration(new GridViewSpacing(20));
                    spacingSet = true;
                }
            }else{
                rl_not_schedule.setVisibility(View.GONE);
                liveAstroRecyclerView.setVisibility(View.VISIBLE);
                liveAstrologerAdapter.notifyDataSetChanged();
            }

            CUtils.parseGiftList(liveAstroData);
        }catch (Exception e){
            //Log.e("redirectToLink", e.toString());
            liveAstrologerModelArrayList.clear();
            liveAstrologerAdapter.notifyDataSetChanged();
        }
        mSwipeRefreshLayout.setRefreshing(false);

    }


    @Override
    public void onResponse(String response, int method) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(true);
        }

//        if (response != null && response.length() > 0) {
//
//            if (method == FETCH_LIVE_ASTROLOGER) {
//                try {
//                    CUtils.setApiLastHitTime();
////                    Log.e("SAN HF AStro res ", " Time=> " + System.currentTimeMillis() + " res=> " + response);
//                    CUtils.saveLiveAstroList(response);
//                    parseLiveAstrologerList(response);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

    @Override
    public void onError(VolleyError error) {

    }
}