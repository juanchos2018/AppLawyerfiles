package com.document.lawyerfiles.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.document.lawyerfiles.Clases.ClsRepresentantes;
import com.document.lawyerfiles.Clases.GsonRepresentantesParser;
import com.document.lawyerfiles.R;
import com.document.lawyerfiles.adapters.AdapterRepresentantes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Consultas3Activity extends AppCompatActivity {

    EditText etruc;
    TextView tvtipoDoc, tvnroDoc, tvnombres, tvcargo;
    Button btnconsultar;

    private ProgressDialog progressDialog;
    JsonObjectRequest jsonObjectRequest;

    CardView cardView;
    ListView lista;
    ArrayAdapter adaptador;
    HttpURLConnection con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultas3);

        etruc = (EditText) findViewById(R.id.id_etdrucbuscar);
        btnconsultar=(Button)findViewById(R.id.btnbuscaruc);

        btnconsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ruc = etruc.getText().toString();
                buscarsi(ruc);
            }
        });
    }

    private void buscarsi(String ruc) {
        lista= (ListView) findViewById(R.id.listaAnimales);
        if (TextUtils.isEmpty(ruc)){
            etruc.setError("campo requerido we");
        }
        else{
            try {
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    new JsonTask(). execute(
                            new URL("https://quertium.com/api/v1/sunat/legals/"+ruc+"?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.MTM3Mw.x-jUgUBcJukD5qZgqvBGbQVMxJFUAIDroZEm4Y9uTyg"));
                } else {
                    Toast.makeText(this, "Error de conexión", Toast.LENGTH_LONG).show();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }
    }
    public class JsonTask extends AsyncTask<URL, Void, List<ClsRepresentantes>> {

        @Override
        protected List<ClsRepresentantes> doInBackground(URL... urls) {
            List<ClsRepresentantes> animales = null;

            try {
                // Establecer la conexión
                con = (HttpURLConnection)urls[0].openConnection();
                con.setConnectTimeout(15000);
                con.setReadTimeout(10000);

                // Obtener el estado del recurso
                int statusCode = con.getResponseCode();

                if(statusCode!=200) {
                    animales = new ArrayList<>();
                    animales.add(new ClsRepresentantes("Error",null,null,null));

                } else {

                    // Parsear el flujo con formato JSON
                    InputStream in = new BufferedInputStream(con.getInputStream());

                    // JsonAnimalParser parser = new JsonAnimalParser();
                    GsonRepresentantesParser parser = new GsonRepresentantesParser();
                    animales = parser.leerFlujoJson(in);
                }

            } catch (Exception e) {
                e.printStackTrace();

            }finally {
                con.disconnect();
            }
            return animales;
        }

        @Override
        protected void onPostExecute(List<ClsRepresentantes> representantes) {
            /*
            Asignar los objetos de Json parseados al adaptador
             */
            if(representantes!=null) {
                adaptador = new AdapterRepresentantes(getBaseContext(), representantes);
                lista.setAdapter(adaptador);
            }else{
                Toast.makeText(
                        getBaseContext(),
                        "Ocurrió un error de Parsing Json",
                        Toast.LENGTH_SHORT)
                        .show();
            }

        }
    }


}

