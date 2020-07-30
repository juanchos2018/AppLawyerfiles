package com.document.lawyerfiles.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.document.lawyerfiles.R;

public class NoticiasActivity extends AppCompatActivity {


    WebView browser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticias);

        browser=(WebView)findViewById(R.id.webviewnoticias);

        browser.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        // Cargamos la web http://radiouno.pe/
        browser.loadUrl("http://elperuano.pe/");

    }
}
