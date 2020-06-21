package com.document.lawyerfiles.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.document.lawyerfiles.Clases.ClsCountries;
import com.document.lawyerfiles.Clases.VolleySingleton;
import com.document.lawyerfiles.R;
import com.document.lawyerfiles.adapters.AdapterPaises2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Consultas4Activity extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener {



    EditText etruc;
    TextView tvtipoDoc, tvnroDoc, tvnombres, tvcargo;
    Button btnconsultar;

    private ProgressDialog progressDialog;
    JsonObjectRequest jsonObjectRequest;

    CardView cardView;
    ListView lista;
    ArrayAdapter adaptador;
    HttpURLConnection con;
    RecyclerView recyclerView;
    //recyclerpaises
    ArrayList<ClsCountries> listaAlumnos;
    private static final String TAG = "Consultas4Activity";
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultas4);

        listaAlumnos=new ArrayList<>();
        recyclerView= (RecyclerView) findViewById(R.id.recyclerpaises);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

       // lis();
        //listar3();

        cargarWebService();
    }

    private void cargarWebService() {
        progress=new ProgressDialog(this);
        progress.setMessage("Consultando...");
        progress.show();


        String url="https://api.covid19api.com/summary";


        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);

        VolleySingleton.getIntanciaVolley(this).addToRequestQueue(jsonObjectRequest);
    }

    private void listar3() {


    }


    private void buscarsi(String ruc) {
        /*
        lista= (ListView) findViewById(R.id.listaAnimales);

            try {
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    new JsonTask(). execute(
                            new URL("https://api.covid19api.com/summary"));
                } else {
                    Toast.makeText(this, "Error de conexión", Toast.LENGTH_LONG).show();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

         */


    }

    private  void listar(){
       // String  URL2="https://quertium.com/api/v1/sunat/ruc/"+ruc+"?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.MTM3Mw.x-jUgUBcJukD5qZgqvBGbQVMxJFUAIDroZEm4Y9uTyg";
       String URL2="https://api.covid19api.com/summary";
        progressDialog =new ProgressDialog(this);
        progressDialog.setTitle("Consultando");
        progressDialog.setMessage("Espera We ....");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        //   final String URL2="https://quertium.com/api/v1/reniec/dni/"+dni+"?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.MTM3Mw.x-jUgUBcJukD5qZgqvBGbQVMxJFUAIDroZEm4Y9uTyg";
        RequestQueue requestQueue2= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest1 =new StringRequest(Request.Method.GET,URL2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responses) {
                        try {

                            progressDialog.dismiss();
                            JSONObject jsonObject2=new JSONObject(responses);
                            //  JSONObject nombre=jsonObject.getJSONObject("nombres");
                            String nombre=jsonObject2.getString("nombre");

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(Consultas4Activity.this, "Err "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest1.setRetryPolicy(policy);
        requestQueue2.add(stringRequest1);
    }
    private  void a(){

    }

    private void lis(){
        String url = "https://api.covid19api.com/summary";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray jsonArray = response.getJSONArray("Countries");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    String a =jsonObject.getString("Country");
                    Log.e(TAG, a );

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TAG", "Error Respuesta en JSON: " + error.getMessage());

                    }
                });
        requestQueue.add(jsObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
        System.out.println();
        Log.d("ERROR: ", error.toString());
        progress.hide();
    }

    @Override
    public void onResponse(JSONObject response) {
        ClsCountries alumno=null;
        JSONArray json=response.optJSONArray("Countries");
        try {

            for (int i=0;i<json.length();i++){
                alumno=new ClsCountries();
                JSONObject jsonObject=null;
                jsonObject=json.getJSONObject(i);

                alumno.setCountry(jsonObject.optString("Country"));
                alumno.setNewConfirmed(jsonObject.optString("NewConfirmed"));


                listaAlumnos.add(alumno);
            }
            progress.hide();
            AdapterPaises2 adapter=new AdapterPaises2(listaAlumnos);
            recyclerView.setAdapter(adapter);


        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "No se ha podido establecer conexión con el servidor" +
                    " "+response, Toast.LENGTH_LONG).show();
            progress.hide();
        }
    }

    private  void copy(){
        String da="asdas";
        ClipboardManager clipboardManager=(ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        ClipData clip=ClipData.newPlainText("Editext",da);
        clipboardManager.setPrimaryClip(clip);

        //clip.getDescription();
    }
}
