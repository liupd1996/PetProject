package com.example.petproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petproject.R;
import com.example.petproject.bean.DeviceRequest;
import com.example.petproject.bean.DeviceResponse;

import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {
    private static final String TAG = "EncodeHistoryAdapter";
    private Context mContext;
    private List<DeviceResponse> mList;
    private ItemClickListener mItemClickListener;

    public DeviceAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setList(List<DeviceResponse> list) {
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
        DeviceResponse request = mList.get(position);
        if (request == null) {
            return;
        }
        holder.tv_num.setText(request.deviceName);
        //SimpleDateFormat formatter = new SimpleDateFormat("扫描时间: yyyy-MM-dd HH:mm", Locale.CHINA);
        //String dateString = formatter.format(new Date(dataBean.getTime()));
        //holder.time.setText(request.);
        holder.itemView.setOnClickListener(v -> {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(request, position);
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
        TextView tv_num;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            title = itemView.findViewById(R.id.tv_title);
            tv_num = itemView.findViewById(R.id.tv_num);
        }
    }

    public interface ItemClickListener {
        void onItemClick(DeviceResponse bean, int position);
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }
}
