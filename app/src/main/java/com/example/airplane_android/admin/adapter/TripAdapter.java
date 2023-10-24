package com.example.airplane_android.admin.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airplane_android.R;
import com.example.airplane_android.admin.PlaneEditActivity;
import com.example.airplane_android.admin.TripEditActivity;
import com.example.airplane_android.admin.model.Plane;
import com.example.airplane_android.admin.model.Trip;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {
    private List<Trip> mListTrip;
    private Context context;

    public TripAdapter(List<Trip> mListTrip, Context context) {
        this.mListTrip = mListTrip;
        this.context = context;
    }

    public void release(){
        context = null;
    }
    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TripAdapter.TripViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trip trip = mListTrip.get(position);
        holder.fromVH.setText(trip.getFrom());
        holder.toVH.setText(trip.getTo());
        holder.startDateVH.setText(trip.getStart());
        holder.endDateVH.setText(trip.getEnd());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showOptionsDialog(trip);
                return true;
            }
        });
    }

    private void showOptionsDialog(Trip trip) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(new CharSequence[]{"Sửa", "Xóa"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Intent intent = new Intent(context, TripEditActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("trip",trip);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                } else if (which == 1) {
                    deleteData(trip);
                }
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void deleteData(Trip trip) {
        String id = trip.getId();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Trip").document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context,"Xóa Thành Công",Toast.LENGTH_SHORT).show();
                        mListTrip.remove(trip);
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
        if(mListTrip != null){
            return mListTrip.size();
        }
        return 0;
    }

    public class TripViewHolder extends RecyclerView.ViewHolder{

        private TextView fromVH,toVH,startDateVH,endDateVH,priceEconomy,priceBusiness,economyTicket,businessTicket;

        private FloatingActionButton flButton;
        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            fromVH = itemView.findViewById(R.id.textNoiDi);
            toVH = itemView.findViewById(R.id.textNoiDen);
            startDateVH = itemView.findViewById(R.id.textBatDau);
            endDateVH = itemView.findViewById(R.id.textKetThuc);
            flButton=itemView.findViewById(R.id.floatingActionButtonTrip);
        }

    }
}
