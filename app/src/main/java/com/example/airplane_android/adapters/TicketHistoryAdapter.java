package com.example.airplane_android.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airplane_android.R;
import com.example.airplane_android.TicketDetailActivity;
import com.example.airplane_android.models.TicketModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class TicketHistoryAdapter extends RecyclerView.Adapter<TicketHistoryAdapter.ViewHolder>{
    private List<TicketModel> dataList;
    private Context context;
    FirebaseFirestore firestore;
    private final String UID = FirebaseAuth.getInstance().getUid();
    public TicketHistoryAdapter(List<TicketModel> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        firestore = FirebaseFirestore.getInstance();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TicketModel data = dataList.get(position);

        // Populate the TextViews with data from the data model
        holder.from.setText(data.getFrom());
        holder.start.setText(data.getStart());
        holder.to.setText(data.getTo());
        holder.end.setText(data.getEnd());
        holder.estimateTime.setText(data.getEstimateTime());
        holder.id.setText("Mã vé: "+data.getTicketCode());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, TicketDetailActivity.class);
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
        public TextView estimateTime;
        public TextView id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the TextViews
            from = itemView.findViewById(R.id.history_item_from);
            to = itemView.findViewById(R.id.history_item_to);
            start = itemView.findViewById(R.id.history_item_start);
            end = itemView.findViewById(R.id.history_item_end);
            estimateTime = itemView.findViewById(R.id.history_item_estimateTime);
            id = itemView.findViewById(R.id.history_item_id);
        }
    }
}
