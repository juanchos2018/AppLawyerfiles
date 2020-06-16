package com.document.lawyerfiles;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.document.lawyerfiles.adapters.MyViewPageAdapter;
import com.document.lawyerfiles.fragment.SliderFragment;

public class SliderActivity extends AppCompatActivity {


    private ViewPager viewPager;
    private MyViewPageAdapter adapter;
    private LinearLayout dotsLayout;
    private Button btnAtras,btnSiguiente;

    private int[] image = {R.drawable.ic_1, R.drawable.ic_2};
    private int[] colorsBackground, colorsDot;
    private String[] content = {
            "Lorem  impsun  lorem ipson lorem ipson.",
            "Lorem  impsun  lorem ipson lorem ipson"};

    private String[] title = {"Lorem","Loren"};
    private TextView[] dots;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);

        SharedPreferences sharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE);
        if(sharedPreferences.getInt("INTRO", 0) == 1){
            startActivity(new Intent(SliderActivity.this, Principal.class));
            finish();
        }

        colorsBackground=getResources().getIntArray(R.array.array_background);
        colorsDot=getResources().getIntArray(R.array.array_dot);

        viewPager = findViewById(R.id.ViewPager);
        btnAtras = findViewById(R.id.btnBack);
        btnSiguiente = findViewById(R.id.btnNext);
        dotsLayout = findViewById(R.id.layoutDots);

        addDots(0);

        loadViewPager();

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int back=getItem(-1);
                if(viewPager.getCurrentItem()==title.length-1){
                    viewPager.setCurrentItem(back);
                }
                else {
                    viewPager.setCurrentItem(back);
                }
            }
        });
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int next=getItem(+1);
                if(next<title.length){
                    viewPager.setCurrentItem(next);
                }
                else{

                    //TODO AQUI PUSE LA FUNCION PANZON
                    launchHomeScreen();
                    Intent intent = new Intent(SliderActivity.this,Principal.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
    private int getItem(int i){
        return viewPager.getCurrentItem()+i;
    }

    private void addDots(int currentPage){
        dots=new TextView[title.length];

        dotsLayout.removeAllViews();

        for (int i=0; i<dots.length;i++){
            dots[i]= new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            if (i==currentPage){
                dots[i].setTextColor(colorsDot[currentPage]);
            }
            else{
                dots[i].setTextColor(Color.LTGRAY);
            }
            dotsLayout.addView(dots[i]);
        }
    }


    private void loadViewPager(){
        adapter = new MyViewPageAdapter(getSupportFragmentManager());
        //crear un for para listar las paginas para ver el tamano usamos title
        for(int i=0; i<title.length;i++){
            adapter.addFragment(newInstance(title[i],content[i],image[i],colorsBackground[i]));
        }
        //color de background
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(pagerListener);

    }
    private void launchHomeScreen() {

        SharedPreferences sharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = sharedPreferences.edit();
        editor.putInt("INTRO", 1);
        editor.apply();
        startActivity(new Intent(this, Principal.class));
        finish();
    }
    private SliderFragment newInstance(String title, String content, int image, int color){
        Bundle bundle = new Bundle();
        bundle.putString("titulo",title);
        bundle.putString("contenido",content);
        bundle.putInt("imagen",image);
        bundle.putInt("color",color);
        SliderFragment fragment = new SliderFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    ViewPager.OnPageChangeListener pagerListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);

            if(position==title.length-1){
                btnSiguiente.setText("Empezar");
                btnAtras.setText("Atras");
            }
            else{
                btnSiguiente.setText("Siguiente");
                btnAtras.setText("Atras");
                //   Toast.makeText(SliderActivity.this, "final", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
