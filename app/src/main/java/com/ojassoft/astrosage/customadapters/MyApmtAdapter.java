package com.ojassoft.astrosage.customadapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.constants.AppConstants;
import com.ojassoft.astrosage.interfaces.RecyclerClickListner;
import com.ojassoft.astrosage.model.AppointmentModel;
import com.ojassoft.astrosage.utils.indnotes.FontUtils;

import java.util.ArrayList;

public class MyApmtAdapter extends RecyclerView.Adapter<MyApmtAdapter.MyViewHolder> {

    private RecyclerClickListner recyclerClickListner;
    private Context context;
    private ArrayList<AppointmentModel> myAppointmentModelArrayList;
    private int dotPos;
    public MyApmtAdapter(Context context, ArrayList<AppointmentModel> myAppointmentModels) {
        this.myAppointmentModelArrayList = myAppointmentModels;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_my_apmt, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (myAppointmentModelArrayList != null && myAppointmentModelArrayList.size() > position) {
            AppointmentModel appointmentModel = myAppointmentModelArrayList.get(position);
            if (appointmentModel != null) {
                holder.nameTV.setText(appointmentModel.getName());
                holder.timeTV.setText("(" + appointmentModel.getAppointmentStartTimeIn12HR() + " to " + appointmentModel.getAppointmentEndTimeIn12HR() + ")");
            }
            if(dotPos > 3){
                dotPos = 0;
            }
            if(dotPos == 0){
                holder.iconDot.setImageResource(R.drawable.dot_circle_blue);
            }else if(dotPos == 1){
                holder.iconDot.setImageResource(R.drawable.dot_circle_purple);
            }else if(dotPos == 2){
                holder.iconDot.setImageResource(R.drawable.dot_circle_orange);
            }
            dotPos ++;
        }
    }

    @Override
    public int getItemCount() {
        if (myAppointmentModelArrayList == null) return 0;
        return myAppointmentModelArrayList.size();
    }

    public int getItemViewType(int position) {
        return 0;
    }

    public void setOnClickListner(RecyclerClickListner recyclerClickListner) {
        this.recyclerClickListner = recyclerClickListner;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nameTV;
        public TextView timeTV;
        private ImageView iconDot;

        public MyViewHolder(View view) {
            super(view);
            nameTV = view.findViewById(R.id.nameTV);
            timeTV = view.findViewById(R.id.timeTV);
            iconDot = view.findViewById(R.id.iconDot);

            FontUtils.changeFont(context, nameTV, AppConstants.FONT_ROBOTO_MEDIUM);
            FontUtils.changeFont(context, timeTV, AppConstants.FONT_ROBOTO_REGULAR);

        }

        @Override
        public void onClick(View v) {
            recyclerClickListner.onItemClick(getAdapterPosition(), v);
        }
    }
}
