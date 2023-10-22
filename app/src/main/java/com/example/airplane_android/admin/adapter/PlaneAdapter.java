package com.example.airplane_android.admin.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airplane_android.R;
import com.example.airplane_android.admin.PlaneActivity;
import com.example.airplane_android.admin.PlaneAddActivity;
import com.example.airplane_android.admin.PlaneEditActivity;
import com.example.airplane_android.admin.model.Plane;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

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
        boolean isActive = plane.isActive();
        ImageView iconCheckActive = holder.itemView.findViewById(R.id.iconCheckActive);
        if(isActive == true){

            iconCheckActive.setVisibility(View.VISIBLE);

//            holder.textTrangThai.setText("Kích Hoạt");
        }else {
            iconCheckActive.setVisibility(View.GONE);
//            holder.textTrangThai.setText("Đã Tắt");
        }
        holder.textTenHang.setText(plane.getBrand());
        holder.textTenLoai.setText(plane.getType());
        holder.textSucChua.setText(plane.getCapacity().toString());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showOptionsDialog(plane);
//                Toast.makeText(context, "Long Clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intentToEditPlane = new Intent(context, PlaneEditActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("plane",plane);
//                intentToEditPlane.putExtras(bundle);
//                context.startActivity(intentToEditPlane);
            }
        });
    }

    private void showOptionsDialog(Plane plane) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("Options");
        builder.setItems(new CharSequence[]{"Sửa", "Xóa"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Intent intent = new Intent(context, PlaneEditActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("plane",plane);
                    intent.putExtras(bundle);

                    context.startActivity(intent);

                } else if (which == 1) {
                    deleteData(plane);
                }
                dialog.dismiss();
            }
        });
        builder.show();
    }
    private void deleteData(Plane plane) {
        String id = plane.getId();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Plane").document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context,"Xóa Thành Công",Toast.LENGTH_SHORT).show();
                        mListPlane.remove(plane);
                        notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

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
        private TextView textNoItems,textTenHang;
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
