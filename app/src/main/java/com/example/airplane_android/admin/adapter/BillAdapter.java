package com.example.airplane_android.admin.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airplane_android.R;
import com.example.airplane_android.TicketDetailActivity;
import com.example.airplane_android.admin.BillDetailActivity;
import com.example.airplane_android.admin.model.BillModel;
import com.example.airplane_android.models.TicketModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder>{
    private List<BillModel> dataList;
    private Context context;
    FirebaseFirestore firestore;
    private final String UID = FirebaseAuth.getInstance().getUid();
    public BillAdapter(List<BillModel> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        firestore = FirebaseFirestore.getInstance();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BillModel data = dataList.get(position);

        // Populate the TextViews with data from the data model
        holder.from.setText(data.getFrom());
        holder.start.setText(data.getStart());
        holder.to.setText(data.getTo());
        holder.end.setText(data.getEnd());
        holder.code.setText(data.getTicketCode());
        holder.email.setText("Người mua: "+data.getEmail());
        if(data.getPurchaseStatus()){
            holder.status.setImageResource(R.drawable.baseline_check_circle_24);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, BillDetailActivity.class);
                intent.putExtra("code",data.getTicketCode());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView from;
        public TextView to;
        public TextView start;
        public TextView end;
        public TextView code;
        public TextView email;
        public ImageView status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the TextViews
            from = itemView.findViewById(R.id.bill_item_from);
            to = itemView.findViewById(R.id.bill_item_to);
            start = itemView.findViewById(R.id.bill_item_start);
            end = itemView.findViewById(R.id.bill_item_end);
            code = itemView.findViewById(R.id.bill_item_code);
            email = itemView.findViewById(R.id.bill_item_email);
            status = itemView.findViewById(R.id.bill_item_status);
        }
    }
}
