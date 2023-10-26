package com.example.airplane_android.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.airplane_android.R;
import com.example.airplane_android.SearchActivity;
import com.example.airplane_android.utils.ValidateUtils;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainHomeFragment extends Fragment {

  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;

  public MainHomeFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment MainHomeFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static MainHomeFragment newInstance(String param1, String param2) {
    MainHomeFragment fragment = new MainHomeFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_main_home, container, false);
  }


  TextInputEditText fromInput, toInput, dateInput, typeChairInput, amountInput;
  CircleImageView btnSwitch;
  Button btnSubmit;
  FrameLayout mainHome;

  private int typeChairSelectedIndex = 0;
  private final String[] typeChairOptions = new String[]{"Economy", "Business"};

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    fromInput = requireView().findViewById(R.id.fromDesInput);
    toInput = requireView().findViewById(R.id.toDesInput);
    dateInput = requireView().findViewById(R.id.dateFlightInput);
    amountInput = requireView().findViewById(R.id.amountPassengerInput);
    typeChairInput = requireView().findViewById(R.id.typeChairInput);
    btnSwitch = requireView().findViewById(R.id.switchFromDes);
    btnSubmit = requireView().findViewById(R.id.submit);
    mainHome = requireView().findViewById(R.id.mainHome);

    final Date currentDate = new Date();
    SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    dateInput.setText(formatDate.format(currentDate));

    amountInput.setText("1");

    typeChairInput.setText(typeChairOptions[0]);
  }

  @Override
  public void onStart() {
    super.onStart();

    btnSwitch.setOnClickListener(v -> {
      final String fromValue = fromInput.getText().toString();
      final String toValue = toInput.getText().toString();

      fromInput.setText(toValue);
      toInput.setText(fromValue);
    });

    dateInput.setOnClickListener(v -> {
      MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
          .setTitleText("Chọn ngày")
          .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
          .setTextInputFormat(new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH))
          .build();

      datePicker.show(getChildFragmentManager(), "birthCalendar");
      datePicker.addOnPositiveButtonClickListener(selection -> {
        Date selectedDate = new Date(selection);
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        dateInput.setText(formatDate.format(selectedDate));
      });
    });

    typeChairInput.setOnClickListener(v -> {
      MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext());
      dialogBuilder
          .setTitle("Chọn hạng ghế ngồi")
          .setSingleChoiceItems(typeChairOptions, typeChairSelectedIndex, (dialog, which) -> {
            typeChairInput.setText(typeChairOptions[which]);
            typeChairSelectedIndex = which;
            dialog.dismiss();
          })
          .show();
    });

    btnSubmit.setOnClickListener(v -> {
      final String fromValue = fromInput.getText().toString();
      final String toValue = toInput.getText().toString();
      final String dateValue = dateInput.getText().toString();
      final String amountValue = amountInput.getText().toString();
      final String typeChairValue = typeChairInput.getText().toString();

      final String validateBasicRes = validateBasicField(fromValue, toValue, dateValue, amountValue, typeChairValue);
      if (!validateBasicRes.equals("")) {
        Snackbar.make(mainHome, validateBasicRes, Snackbar.LENGTH_SHORT).show();
        return;
      }

      Intent intent = new Intent(getActivity(), SearchActivity.class);
      intent.putExtra("fromDes", fromValue);
      intent.putExtra("toDes", toValue);
      intent.putExtra("date", dateValue);
      intent.putExtra("amount", amountValue);
      intent.putExtra("typeChair", typeChairValue);
      startActivity(intent);
    });
  }

  private boolean checkIsEmptyField(String fromValue, String toValue, String dateValue, String amountValue, String typeChairValue) {
    return fromValue.equals("") || toValue.equals("") || dateValue.equals("") || amountValue.equals("") || typeChairValue.equals("");
  }

  private boolean checkIsPreviousDate(String dateValue) {
    @SuppressLint("SimpleDateFormat")
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    try {
      LocalDate currentDate = LocalDate.now();
      LocalDate selectedDate = LocalDate.parse(dateValue, dateFormat);

        return selectedDate.isBefore(currentDate);
    } catch (Exception e) {
      Log.e("Parsing to date", "Failed to parsing string date to type date, log: " + e);
      return false;
    }
  }

  private boolean checkDesAtLeast3Characters(String des) {
    return des.length() >= 3;
  }

  private String validateBasicField(String fromValue, String toValue, String dateValue, String amountValue, String typeChairValue) {
    if (checkIsEmptyField(fromValue, toValue, dateValue, amountValue, typeChairValue))
      return "Không được để trống";
    if (ValidateUtils.checkContainsNumeric(fromValue) || ValidateUtils.checkContainsNumeric(toValue)) return "Nơi đi hoặc nơi đến không được chứa chữ số";
    if (!checkDesAtLeast3Characters(fromValue) || !checkDesAtLeast3Characters(toValue)) return "Vui lòng nhập ít nhất 3 ký tự";
    if (fromValue.equals(toValue)) return "Điểm đi trùng với điểm đến";
    if (checkIsPreviousDate(dateValue)) return "Vui lòng chọn từ ngày hiện tại trở đi";
    if (Integer.parseInt(amountValue) < 1) return "Vui lòng nhập số lượng hợp lệ";
    if (Integer.parseInt(amountValue) > 7) return "Số lượng vé tối đa là 7";

    return "";
  }
}
