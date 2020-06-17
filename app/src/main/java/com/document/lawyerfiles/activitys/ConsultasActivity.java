package com.document.lawyerfiles.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.document.lawyerfiles.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ConsultasActivity extends AppCompatActivity {
    EditText etdni;
    Button btnbuscar;
    private ProgressDialog progressDialog;
    CardView card1;
    TextView txtnombres,txtapellidos,txtdni;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultas);

        etdni=(EditText)findViewById(R.id.id_etdnibuscar);
        btnbuscar=(Button)findViewById(R.id.btnbuscardni);
        txtdni=(TextView)findViewById(R.id.tvdni);
        txtnombres=(TextView)findViewById(R.id.tvdnnombres);
        txtapellidos=(TextView)findViewById(R.id.tvapellidos);
        card1=(CardView)findViewById(R.id.card1);
        card1.setVisibility(View.INVISIBLE);


        btnbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dni=etdni.getText().toString();
                buscardni(dni);
            }
        });
    }


    private void ocultar() {

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etdni.getWindowToken(), 0);
    }
    private void buscardni(final String dni) {


        if(TextUtils.isEmpty(dni)){
            etdni.setError("campo reqierodo");
        }
        else {


        progressDialog =new ProgressDialog(this);
        progressDialog.setTitle("Consultando");
        progressDialog.setMessage("Espera We ....");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        final String URL2="https://quertium.com/api/v1/reniec/dni/"+dni+"?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.MTM3Mw.x-jUgUBcJukD5qZgqvBGbQVMxJFUAIDroZEm4Y9uTyg";
        RequestQueue requestQueue2= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest1 =new StringRequest(Request.Method.GET,URL2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responses) {
                        try {
                            card1.setVisibility(View.VISIBLE);
                            progressDialog.dismiss();
                            JSONObject jsonObject2=new JSONObject(responses);
                            //  JSONObject nombre=jsonObject.getJSONObject("nombres");
                            txtdni.setText(dni);

                            String name=jsonObject2.getString("primerNombre");
                            String name2=jsonObject2.getString("segundoNombre");
                            String apellido_paterno=jsonObject2.getString("apellidoPaterno");
                            String apellido_materno=jsonObject2.getString("apellidoMaterno");
                            txtnombres.setText(name +" "+name2);
                            txtapellidos.setText(apellido_paterno +" " +apellido_materno);
                            ocultar();
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(ConsultasActivity.this, "Err "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
    }
}
