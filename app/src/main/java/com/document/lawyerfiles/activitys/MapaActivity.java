package com.document.lawyerfiles.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.document.lawyerfiles.R;
import com.document.lawyerfiles.fragment.MapaFragment;

public class MapaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        MapaFragment fr = new MapaFragment();
      //  Bundle args = new Bundle();
      //  String texto="hola we";


        // Colocamos el String
      //  args.putString("textFromActivityB", texto);
      //  fr.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.contenerdor,fr).commit();

    }
}
