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

public class Consultas2Activity extends AppCompatActivity {
    EditText etbuscarruc;
    Button btnbuscar;
    private ProgressDialog progressDialog;

    CardView cardView;
    TextView txtruc,txtnombre,txttipo_contribuyente,txtnombre_comercial,txtestado,txtfecha_incripcion,txtfecha_inicio,txtdepartament,txtprovincia,txtdistrito,txtdireccion,txttelefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultas2);
        etbuscarruc=(EditText)findViewById(R.id.id_etdrucbuscar);

        cardView=(CardView)findViewById(R.id.card1);
        cardView.setVisibility(View.INVISIBLE);
        btnbuscar=(Button)findViewById(R.id.btnbuscaruc);


        txtruc=(TextView)findViewById(R.id.txtruc);
        txtnombre=(TextView)findViewById(R.id.tvdnnombres);
        txttipo_contribuyente=(TextView)findViewById(R.id.tipocontribuyente);
        txtnombre_comercial=(TextView)findViewById(R.id.nombrecomercial);
        txtestado=(TextView)findViewById(R.id.estado);
        txtfecha_incripcion=(TextView)findViewById(R.id.fecha);
        txtfecha_inicio =(TextView)findViewById(R.id.fechaincio);
        txtdepartament  =(TextView)findViewById(R.id.txtdeperatemetno);
        txtprovincia     =(TextView)findViewById(R.id.txtprovincia);
        txtdistrito     =(TextView)findViewById(R.id.txtdistrito);
        txtdireccion    =(TextView)findViewById(R.id.txtdireccion);
        txttelefono     =(TextView)findViewById(R.id.txttelefono);

        btnbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ruc=etbuscarruc.getText().toString();
                consultar(ruc);

            }
        });
    }

    private void ocultar() {

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etbuscarruc.getWindowToken(), 0);
    }
    private void consultar(final String ruc){
        if (TextUtils.isEmpty(ruc)){
            etbuscarruc.setError("campo Requerido");
        }
        else{

            String  URL2="https://quertium.com/api/v1/sunat/ruc/"+ruc+"?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.MTM3Mw.x-jUgUBcJukD5qZgqvBGbQVMxJFUAIDroZEm4Y9uTyg";


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
                                cardView.setVisibility(View.VISIBLE);
                                progressDialog.dismiss();
                                JSONObject jsonObject2=new JSONObject(responses);
                                //  JSONObject nombre=jsonObject.getJSONObject("nombres");
                                String nombre=jsonObject2.getString("nombre");
                                String tipoContribuyente=jsonObject2.getString("tipoContribuyente");
                                String nombreComercial=jsonObject2.getString("nombreComercial");
                                String estadoContribuyente=jsonObject2.getString("estadoContribuyente");
                                String fechaInscripcion=jsonObject2.getString("fechaInscripcion");
                                String fechaInicio=jsonObject2.getString("fechaInicio");
                                String departamento=jsonObject2.getString("departamento");
                                String provincia=jsonObject2.getString("provincia");
                                String distrito=jsonObject2.getString("distrito");
                                String direccion=jsonObject2.getString("direccion");
                                String telefono=jsonObject2.getString("telefono");

                                txtruc.setText(ruc);
                                txtnombre.setText(nombre);
                                txttipo_contribuyente.setText(tipoContribuyente);
                                txtnombre_comercial.setText(nombreComercial);
                                txtestado.setText(estadoContribuyente);
                                txtfecha_incripcion.setText(fechaInscripcion);
                                txtfecha_inicio.setText(fechaInicio);
                                txtdepartament.setText(departamento);
                                txtprovincia.setText(provincia);
                                txtdistrito.setText(distrito);
                                txtdireccion.setText(direccion);
                                txttelefono.setText(telefono);

                                ocultar();
                            }
                            catch (JSONException e){
                                e.printStackTrace();
                                progressDialog.dismiss();
                                Toast.makeText(Consultas2Activity.this, "Err "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
