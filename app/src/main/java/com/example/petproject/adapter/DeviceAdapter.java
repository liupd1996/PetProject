package com.example.petproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.petproject.R;

import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {
    private static final String TAG = "EncodeHistoryAdapter";
    private Context mContext;
    private List<DeviceItem> mList;
    private ItemClickListener mItemClickListener;

    public DeviceAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setList(List<DeviceItem> list) {
        this.mList = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.device_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DeviceItem DeviceItem = mList.get(position);
        if (DeviceItem == null) {
            return;
        }
        holder.title.setText(DeviceItem.deviceNum);
        //SimpleDateFormat formatter = new SimpleDateFormat("扫描时间: yyyy-MM-dd HH:mm", Locale.CHINA);
        //String dateString = formatter.format(new Date(dataBean.getTime()));
        holder.time.setText(DeviceItem.time);
        holder.itemView.setOnClickListener(v -> {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(DeviceItem, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView title;
        TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            title = itemView.findViewById(R.id.tv_title);
            //time = itemView.findViewById(R.id.tv_time);
        }
    }

    public interface ItemClickListener {
        void onItemClick(DeviceItem bean, int position);
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }
}
