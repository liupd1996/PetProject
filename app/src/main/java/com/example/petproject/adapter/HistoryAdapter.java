package com.example.petproject.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petproject.R;
import com.example.petproject.bean.DeviceResponse;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private static final String TAG = "EncodeHistoryAdapter";
    private Context mContext;
    private List<HistoryItem> mList;
    private ItemClickListener mItemClickListener;

    public HistoryAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setList(List<HistoryItem> list) {
        this.mList = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.history_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistoryItem item = mList.get(position);
        if (item == null) {
            return;
        }
        holder.tv_date.setText(item.time);
        holder.tv_data.setText(item.data);
        holder.itemView.setOnClickListener(v -> {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(null, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView tv_date;
        TextView tv_data;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_data = itemView.findViewById(R.id.tv_data);
        }
    }

    public interface ItemClickListener {
        void onItemClick(HistoryItem bean, int position);
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }
}
