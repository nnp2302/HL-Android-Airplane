package com.example.airplane_android.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.airplane_android.ChangePassActivity;
import com.example.airplane_android.LoginActivity;
import com.example.airplane_android.R;
import com.example.airplane_android.UpdateUserInfoActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainUserFragment extends Fragment {

  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  public MainUserFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment MainUserFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static MainUserFragment newInstance(String param1, String param2) {
    MainUserFragment fragment = new MainUserFragment();
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
      // TODO: Rename and change types of parameters
      String mParam1 = getArguments().getString(ARG_PARAM1);
      String mParam2 = getArguments().getString(ARG_PARAM2);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_main_user, container, false);
  }

  TextView txtNoLogin, txtHasLogin;
  Button btnNoLoginLink;
  CircleImageView avatarHasLogin;
  LinearLayout hasLoginContainer, userBtn, changePassBtn, logoutBtn;
  FirebaseAuth firebaseAuth;

  @SuppressLint("SetTextI18n")
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    txtNoLogin = requireView().findViewById(R.id.sloganNoLogin);
    btnNoLoginLink = requireView().findViewById(R.id.noLoginLink);

    hasLoginContainer = requireView().findViewById(R.id.hasLoginContainer);
    avatarHasLogin = requireView().findViewById(R.id.hasLoginAvatar);
    txtHasLogin = requireView().findViewById(R.id.hasLoginName);

    userBtn = requireView().findViewById(R.id.userSetting);
    changePassBtn = requireView().findViewById(R.id.changePassSetting);
    logoutBtn = requireView().findViewById(R.id.logoutSetting);

    firebaseAuth = FirebaseAuth.getInstance();

    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    if (currentUser == null) {
      txtNoLogin.setVisibility(View.VISIBLE);
      btnNoLoginLink.setVisibility(View.VISIBLE);

      hasLoginContainer.setVisibility(View.INVISIBLE);
      avatarHasLogin.setVisibility(View.INVISIBLE);
      txtHasLogin.setVisibility(View.INVISIBLE);
      userBtn.setVisibility(View.INVISIBLE);
      logoutBtn.setVisibility(View.INVISIBLE);
    } else {
      txtNoLogin.setVisibility(View.INVISIBLE);
      btnNoLoginLink.setVisibility(View.INVISIBLE);

      hasLoginContainer.setVisibility(View.VISIBLE);
      avatarHasLogin.setVisibility(View.VISIBLE);
      txtHasLogin.setVisibility(View.VISIBLE);
      userBtn.setVisibility(View.VISIBLE);
      logoutBtn.setVisibility(View.VISIBLE);

      handleUserIsLogin(currentUser.getUid());
    }
  }

  @Override
  public void onStart() {
    super.onStart();

    btnNoLoginLink.setOnClickListener(v -> startActivity(new Intent(getActivity(), LoginActivity.class)));

    userBtn.setOnClickListener(v -> startActivity(new Intent(getActivity(), UpdateUserInfoActivity.class)));

    changePassBtn.setOnClickListener(v -> startActivity(new Intent(getActivity(), ChangePassActivity.class)));

    logoutBtn.setOnClickListener(v -> {
      MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext());
      dialogBuilder.setTitle("Cảnh báo").setMessage("Bạn có thật sự muốn đăng xuất?")
          .setNegativeButton("Không", (dialog, which) -> {
            dialog.dismiss();
          })
          .setPositiveButton("Có", (dialog, which) -> {
            firebaseAuth.signOut();
            dialog.dismiss();

            Toast.makeText(getActivity(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), LoginActivity.class));
          });
    });
  }

  @SuppressLint("SetTextI18n")
  private void handleUserIsLogin(String uid) {
    FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    fireStore.collection("Users").document(uid).get()
        .addOnSuccessListener(res -> {
          String username = res.getString("name");
          if (username.equals("")) username = "Unknown";

          final String avatarUrl = res.getString("photoUrl");
          if (!avatarUrl.equals("")) Picasso.get().load(avatarUrl).into(avatarHasLogin);

          txtHasLogin.setText("Xin chào, " + username);
        }).addOnFailureListener(e -> {
          Toast.makeText(getActivity(), "Có lỗi trong quá trình xử lý", Toast.LENGTH_SHORT).show();
          Log.e("Load data FireStore", "Failed to load user data's document in FireStore, log: " + e);
        });
  }
}