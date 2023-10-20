package com.example.airplane_android.admin.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airplane_android.R;
import com.example.airplane_android.admin.PlaneAddActivity;
import com.example.airplane_android.admin.PlaneEditActivity;
import com.example.airplane_android.admin.baseInterface.IPlaneService;
import com.example.airplane_android.admin.model.Plane;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class PlaneAdapter extends RecyclerView.Adapter<PlaneAdapter.PlaneViewHolder> {
    private List<Plane> mListPlane;
    private Context context;

    public PlaneAdapter(List<Plane> mListPlane, Context context) {
        this.mListPlane = mListPlane;
        this.context = context;
    }

    public void release(){
        context = null;
    }
    @NonNull
    @Override
    public PlaneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlaneViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plane,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PlaneViewHolder holder, int position) {
        final Plane plane = mListPlane.get(position);
        //boolean test = plane.isActive();
//        if(test == true){
//            holder.textTrangThai.setText("Kích Hoạt");
//        }else {
//            holder.textTrangThai.setText("Đã Tắt");
//        }
        holder.textTenHang.setText(plane.getBrand());
        holder.textTenLoai.setText(plane.getType());
        holder.textSucChua.setText(plane.getCapacity().toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToEditPlane = new Intent(context, PlaneEditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("plane",plane);
                intentToEditPlane.putExtras(bundle);
                context.startActivity(intentToEditPlane);
            }
        });
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
        private FloatingActionButton flButton;
//        private TextView textTrangThai;
        public PlaneViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.img_avatar);
            textTenHang = itemView.findViewById(R.id.textTenHang);
            textTenLoai = itemView.findViewById(R.id.textTenLoai);
            textSucChua = itemView.findViewById(R.id.textSucChua);
            flButton=itemView.findViewById(R.id.floatingActionButtonPlane);

//            textTrangThai = itemView.findViewById(R.id.textTrangThai);
        }
    }
}
