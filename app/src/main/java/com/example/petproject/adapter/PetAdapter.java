package com.example.petproject.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petproject.R;
import com.example.petproject.bean.PetResponse;
import com.example.petproject.customview.CircularImageView;
import com.example.petproject.utils.Utils;

import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.ViewHolder> {
    private static final String TAG = "EncodeHistoryAdapter";
    private Context mContext;
    private List<PetResponse> mList;
    private ItemClickListener mItemClickListener;

    public PetAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setList(List<PetResponse> list) {
        this.mList = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pet_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PetResponse petResponse = mList.get(position);
        if (petResponse == null) {
            return;
        }
        holder.name.setText(petResponse.name);
        //holder.time.setText(petRequest.birth);
        if (petResponse.gender == 0) {
            holder.gender.setBackgroundResource(R.drawable.man);
        } else {
            holder.gender.setBackgroundResource(R.drawable.woman);
        }
        Bitmap bitmap = Utils.base64ToBitmap(petResponse.avatar);
        if (bitmap != null) {
            holder.avatar.setImageBitmap(bitmap);
        } else {
            if (petResponse.type == 0) {
                holder.avatar.setImageResource(R.drawable.cat_default);
                holder.time.setText("猫猫");
            } else {
                holder.avatar.setImageResource(R.drawable.dog_default);
                holder.time.setText("狗狗");
            }
        }
        holder.itemView.setOnClickListener(v -> {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(petResponse, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView name;
        TextView time;
        ImageView gender;
        CircularImageView avatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            name = itemView.findViewById(R.id.tv_name);
            time = itemView.findViewById(R.id.tv_birth);
            gender = itemView.findViewById(R.id.iv_gender);
            avatar = itemView.findViewById(R.id.iv_pet);
        }
    }

    public interface ItemClickListener {
        void onItemClick(PetResponse bean, int position);
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }
}
