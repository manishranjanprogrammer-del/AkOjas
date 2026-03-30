package com.ojassoft.astrosage.customadapters;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.constants.AppConstants;
import com.ojassoft.astrosage.model.AppointmentModel;
import com.ojassoft.astrosage.ui.act.NewAppointmentActivity;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.indnotes.FontUtils;

import java.util.ArrayList;

public class ApmtAdapter extends RecyclerView.Adapter<ApmtAdapter.MyViewHolder> {

    private Context context;
    private String[] arrStartTime;
    private ArrayList<AppointmentModel> myAppointmentModelArrayList;

    public ApmtAdapter(Context context, String[] arrStartTime, ArrayList<AppointmentModel> myAppointmentModels) {
        this.arrStartTime = arrStartTime;
        this.myAppointmentModelArrayList = myAppointmentModels;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_apmt, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (arrStartTime != null && arrStartTime.length > position) {
            String apmtTime = CUtils.convertTimeToAmPm(arrStartTime[position]);
            if (apmtTime.contains("00:00")) {
                apmtTime = "12:00 AM";
            }
            apmtTime = apmtTime.replace(" ", "\n");
            holder.dateText.setText(apmtTime);
            setApmtView(holder.apmtLL, position);
            if (holder.lineViewLL != null) {
                if (position == arrStartTime.length - 1) {
                    holder.lineViewLL.setVisibility(View.GONE);
                } else {
                    holder.lineViewLL.setVisibility(View.VISIBLE);
                }
            }
            final int pos = position;
            holder.iconAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, NewAppointmentActivity.class);
                    intent.putExtra("apmtStartTime", arrStartTime[pos]);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (arrStartTime == null) return 0;
        return arrStartTime.length;
    }

    public int getItemViewType(int position) {
        return 0;
    }

    private void setApmtView(LinearLayout apmtLL, int position) {
        if (myAppointmentModelArrayList == null) return;
        try {
            apmtLL.removeAllViews();
            int startHr = Integer.parseInt(arrStartTime[position].split(":")[0]);
            for (int i = 0; i < myAppointmentModelArrayList.size(); i++) {
                AppointmentModel appointmentModel = myAppointmentModelArrayList.get(i);
                if (appointmentModel == null) continue;
                int apmtStartHr = appointmentModel.getAppointmentStartHour();
                if (apmtStartHr >= startHr && apmtStartHr < (startHr + 1)) {
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View view = inflater.inflate(R.layout.items_my_apmt_below, null);
                    LinearLayout mainLL = view.findViewById(R.id.mainLL);
                    final TextView nameTV = mainLL.findViewById(R.id.nameTV);
                    final TextView timeTV = mainLL.findViewById(R.id.timeTV);
                    FontUtils.changeFont(context, nameTV, AppConstants.FONT_ROBOTO_MEDIUM);
                    FontUtils.changeFont(context, timeTV, AppConstants.FONT_ROBOTO_REGULAR);
                    nameTV.setText(appointmentModel.getName());
                    timeTV.setText("(" + appointmentModel.getAppointmentStartTimeIn12HR() + " to " + appointmentModel.getAppointmentEndTimeIn12HR() + ")");
                    apmtLL.addView(view);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout lineViewLL;
        public TextView dateText;
        private LinearLayout apmtLL;
        private ImageView iconAdd;

        public MyViewHolder(View view) {
            super(view);

            lineViewLL = view.findViewById(R.id.lineViewLL);
            apmtLL = view.findViewById(R.id.apmtLL);
            dateText = view.findViewById(R.id.date_text);
            iconAdd = view.findViewById(R.id.iconAdd);

            FontUtils.changeFont(context, dateText, AppConstants.FONT_ROBOTO_REGULAR);
        }
    }
}
