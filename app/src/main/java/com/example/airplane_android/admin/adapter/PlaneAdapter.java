package com.example.airplane_android.admin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airplane_android.R;
import com.example.airplane_android.admin.baseInterface.IPlaneService;
import com.example.airplane_android.admin.model.Plane;

import java.util.List;

public class PlaneAdapter extends RecyclerView.Adapter<PlaneAdapter.PlaneViewHolder> {
    private List<Plane> mListPlane;
    public PlaneAdapter(List<Plane> mListPlane) {
        this.mListPlane = mListPlane;
    }

    @NonNull
    @Override
    public PlaneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlaneViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plane,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PlaneViewHolder holder, int position) {
        Plane plane = mListPlane.get(position);
        //boolean test = plane.isActive();
//        if(test == true){
//            holder.textTrangThai.setText("Kích Hoạt");
//        }else {
//            holder.textTrangThai.setText("Đã Tắt");
//        }
        holder.textTenHang.setText(plane.getBrand());
        holder.textTenLoai.setText(plane.getType());
        holder.textSucChua.setText(plane.getCapacity().toString());

    }

    @Override
    public int getItemCount() {
        if(mListPlane != null){
            return mListPlane.size();
        }
        return 0;
    }

    public class PlaneViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgAvatar;
        private TextView textTenHang;
        private TextView textTenLoai;
        private TextView textSucChua;
//        private TextView textTrangThai;
        public PlaneViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.img_avatar);
            textTenHang = itemView.findViewById(R.id.textTenHang);
            textTenLoai = itemView.findViewById(R.id.textTenLoai);
            textSucChua = itemView.findViewById(R.id.textSucChua);
//            textTrangThai = itemView.findViewById(R.id.textTrangThai);
        }
    }
}
