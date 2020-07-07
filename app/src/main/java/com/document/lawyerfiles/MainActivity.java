package com.document.lawyerfiles;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    private void ir(){
        new IntentIntegrator(MainActivity.this).initiateScan(); //integrar una nueva intencion a nuesta actividad
    }
}
