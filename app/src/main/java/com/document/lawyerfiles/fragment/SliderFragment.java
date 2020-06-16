package com.document.lawyerfiles.fragment;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.document.lawyerfiles.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SliderFragment extends Fragment {



    View view;
    ImageView imagen;
    TextView titulo, contenido;

    public SliderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_slider,container,false);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        imagen = view.findViewById(R.id.image);
        titulo = view.findViewById(R.id.txtTitulo);
        contenido=view.findViewById(R.id.txtcontenido);

        RelativeLayout background = view.findViewById(R.id.background);
        //si esta enviando informacion con la llave title
        if(getArguments() != null){
            titulo.setText(getArguments().getString("titulo"));
            contenido.setText(getArguments().getString("contenido"));
            imagen.setImageResource(getArguments().getInt("imagen"));
            background.setBackgroundColor(getArguments().getInt("color"));
        }
        //por defecto
        return view;
    }

}
