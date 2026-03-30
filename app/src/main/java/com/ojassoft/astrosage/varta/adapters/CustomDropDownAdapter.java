package com.ojassoft.astrosage.varta.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ojassoft.astrosage.R;

import java.util.List;

public class CustomDropDownAdapter extends RecyclerView.Adapter<CustomDropDownAdapter.MyViewHolder> {

    private final String[] mList;
    private final DropDownInterface dropDownInterface;
    private final int type;

    public CustomDropDownAdapter(String[] mList, DropDownInterface dropDownInterface, int type) {
        this.mList = mList;
        this.dropDownInterface = dropDownInterface;
        this.type = type;
    }

    @NonNull
    @Override
    public CustomDropDownAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_dropdown_item, parent, false);
        return new CustomDropDownAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomDropDownAdapter.MyViewHolder holder, int position) {
        holder.textView.setText(mList[position]);

        holder.textView.setOnClickListener(v->{
            dropDownInterface.onItemSelected(position, type);
        });
    }

    @Override
    public int getItemCount() {
        return mList.length;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tvDropDown);
        }
    }

}

