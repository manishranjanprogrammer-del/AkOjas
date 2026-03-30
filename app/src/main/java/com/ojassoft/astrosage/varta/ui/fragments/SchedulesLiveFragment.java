package com.ojassoft.astrosage.varta.ui.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.varta.adapters.SchedulesLiveAstroAdapter;
import com.ojassoft.astrosage.varta.model.ScheduleLiveAstroModel;
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


public class SchedulesLiveFragment extends Fragment implements VolleyResponse {
    private RecyclerView liveAstroRecyclerView;
    private ArrayList<ScheduleLiveAstroModel> scheduleLiveAstroModelArrayList;
    private SchedulesLiveAstroAdapter schedulesLiveAstroAdapter;
    private Activity activity;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RequestQueue queue;
    Dialog emptyListDialog;
    private boolean spacingSet = false;
    private RelativeLayout rl_not_schedule;
    private int FETCH_SCHEDULES_LIVE_ASTROLOGER = 5;
    private Button btnNoLiveAstro;
    public SchedulesLiveFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_schedules_live, container, false);
        activity = getActivity();
        init(view);
        return view;
    }

    private void init(View view) {
        queue = VolleySingleton.getInstance(activity).getRequestQueue();
        rl_not_schedule = view.findViewById(R.id.rl_not_schedule);
        btnNoLiveAstro = view.findViewById(R.id.btnNoLiveAstro);
        liveAstroRecyclerView = view.findViewById(R.id.scheduleAstroRecyclerView);
        liveAstroRecyclerView.setItemAnimator(null);
        scheduleLiveAstroModelArrayList = new ArrayList<>();
        schedulesLiveAstroAdapter = new SchedulesLiveAstroAdapter(getContext(), scheduleLiveAstroModelArrayList, 1);
        liveAstroRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        liveAstroRecyclerView.setAdapter(schedulesLiveAstroAdapter);

        mSwipeRefreshLayout = view.findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getScheduledLiveAstrologerList();
                    }
                }, 500);
            }
        });
        btnNoLiveAstro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
                Intent dashboardIntent = new Intent(activity, DashBoardActivity.class);
                startActivity(dashboardIntent);
            }
        });
        getScheduledLiveAstrologerList();
    }

    private void getScheduledLiveAstrologerList() {
        if (!CUtils.isConnectedWithInternet(activity)) {
            if (mSwipeRefreshLayout != null) {
                mSwipeRefreshLayout.setRefreshing(false);
                mSwipeRefreshLayout.setEnabled(true);
            }
            Toast.makeText(activity, getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        } else {
            // Log.e("SAN LiveAstro URL2=>", CGlobalVariables.GET_LIVE_ASTRO_URL);
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.GET_SCHEDULES_LIVE_ASTRO_URL,
//                    this, false, CUtils.getSchedulesLiveAstroParams(activity), FETCH_SCHEDULES_LIVE_ASTROLOGER).getMyStringRequest();
//            queue.add(stringRequest);
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.getScheduleLiveAstrologerList(CUtils.getSchedulesLiveAstroParams(getActivity()));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (mSwipeRefreshLayout != null) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mSwipeRefreshLayout.setEnabled(true);
                    }
                    try {
                        if (response.body() != null) {
                            String myResponse = response.body().string();
                            parseScheduleLiveAstrologerList(myResponse);

                        }
                    } catch (Exception e) {
                        //
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (mSwipeRefreshLayout != null) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mSwipeRefreshLayout.setEnabled(true);
                    }
                }
            });

        }


    }

    @Override
    public void onResponse(String response, int method) {

//        if (mSwipeRefreshLayout != null) {
//            mSwipeRefreshLayout.setRefreshing(false);
//            mSwipeRefreshLayout.setEnabled(true);
//        }
//
//        if (response != null && response.length() > 0) {
//
//            if (method == FETCH_SCHEDULES_LIVE_ASTROLOGER) {
//                try {
//                    parseScheduleLiveAstrologerList(response);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

    private void parseScheduleLiveAstrologerList(String response) {
        if(TextUtils.isEmpty(response)){
            return;
        }
        try {

            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("scheduleList");
            if(scheduleLiveAstroModelArrayList!=null)
            scheduleLiveAstroModelArrayList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                ScheduleLiveAstroModel scheduleLiveAstroModel = CUtils.parseScheduleLiveAstrologerObject(object);
                if(scheduleLiveAstroModel == null) continue;
                scheduleLiveAstroModelArrayList.add(scheduleLiveAstroModel);
            }

            if (scheduleLiveAstroModelArrayList.isEmpty()){
                rl_not_schedule.setVisibility(View.VISIBLE);
                liveAstroRecyclerView.setVisibility(View.GONE);
            }
            else{
                rl_not_schedule.setVisibility(View.GONE);
                liveAstroRecyclerView.setVisibility(View.VISIBLE);
                schedulesLiveAstroAdapter.notifyDataSetChanged();
            }

        }catch (Exception e){
            if(scheduleLiveAstroModelArrayList!=null && schedulesLiveAstroAdapter!=null)
            scheduleLiveAstroModelArrayList.clear();
            schedulesLiveAstroAdapter.notifyDataSetChanged();
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onError(VolleyError error) {

    }
}