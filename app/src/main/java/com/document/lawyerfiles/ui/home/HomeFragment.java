package com.document.lawyerfiles.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.document.lawyerfiles.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    Button btnscanear;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_home, container, false);

        btnscanear=(Button)vista.findViewById(R.id.id_btnscanear);
        btnscanear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanear();
            }
        });
        return vista;
    }
    private void scanear() {

        Toast.makeText(getContext(), "Scaneae we", Toast.LENGTH_SHORT).show();
    }

}