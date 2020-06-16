package com.document.lawyerfiles.ui.consultas;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.document.lawyerfiles.R;
import com.document.lawyerfiles.activitys.Consultas2Activity;
import com.document.lawyerfiles.activitys.ConsultasActivity;

public class ConsultasFragment extends Fragment {

    private ConsultasViewModel mViewModel;
    Button btn1,btn2,btn3;
    CardView card1,card2,card3;

    public static ConsultasFragment newInstance() {
        return new ConsultasFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.consultas_fragment, container, false);

        card1=(CardView)vista.findViewById(R.id.idcar1);
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consulta1();
            }
        });
        card2=(CardView)vista.findViewById(R.id.idcar2);
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consulta2();
            }
        });

        return vista;
    }

    private void consulta2() {

        startActivity(new Intent(getContext(), Consultas2Activity.class));

    }

    private void consulta1() {

        startActivity(new Intent(getContext(), ConsultasActivity.class));

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ConsultasViewModel.class);
        // TODO: Use the ViewModel
    }

}
