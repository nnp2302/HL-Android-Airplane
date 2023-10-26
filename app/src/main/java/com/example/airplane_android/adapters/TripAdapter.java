package com.example.airplane_android.adapters;

import android.content.Context;
import android.icu.util.LocaleData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.airplane_android.R;
import com.example.airplane_android.models.UserTrip;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class TripAdapter extends BaseAdapter {
  private List<UserTrip> tripList;
  private Context context;

  public TripAdapter(Context context, List<UserTrip> tripList) {
    this.context = context;
    this.tripList = tripList;
  }

  @Override
  public int getCount() {
    return tripList.size();
  }

  @Override
  public Object getItem(int position) {
    return tripList.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      LayoutInflater inflater = LayoutInflater.from(context);
      convertView = inflater.inflate(R.layout.search_list_item, parent, false);
    }

    TextView planeId = convertView.findViewById(R.id.planeId);
    TextView fromDes = convertView.findViewById(R.id.fromDes);
    TextView toDes = convertView.findViewById(R.id.toDes);
    TextView fromTime = convertView.findViewById(R.id.fromTime);
    TextView toTime = convertView.findViewById(R.id.toTime);
    TextView estimateTime = convertView.findViewById(R.id.estimateTime);
    TextView price = convertView.findViewById(R.id.price);

    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    UserTrip trip = tripList.get(position);
    planeId.setText(trip.getPlaneId());
    fromDes.setText(trip.getFrom());
    toDes.setText(trip.getTo());
    fromTime.setText(trip.getStart().split(" ")[1]);
    toTime.setText(trip.getEnd().split(" ")[1]);
    estimateTime.setText(trip.getEstimateTime());
    price.setText(currencyFormat.format(trip.getPrice()));

    return convertView;
  }
}
