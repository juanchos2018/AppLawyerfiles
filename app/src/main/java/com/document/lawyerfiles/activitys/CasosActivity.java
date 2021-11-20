package com.document.lawyerfiles.activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.SimpleLottieValueCallback;
import com.document.lawyerfiles.Clases.Casos;
import com.document.lawyerfiles.Clases.ClsClientes;
import com.document.lawyerfiles.R;
import com.document.lawyerfiles.Registro;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CasosActivity extends AppCompatActivity {

    EditText extcliente,etfecha,etjuez,etresumen;
    Spinner spinnercaso,spinneretapa;
    Button btnregistar;
    private ProgressDialog progressDialog;
    ImageView imgbuscar;
    private DatabaseReference reference;

    ArrayList<String> listtiprocespo;
    ArrayAdapter<String> adapterproceso;


    ArrayList<String> listtipocaso;
    ArrayAdapter<String> adapterspiner;
    private FirebaseAuth mAuth;

    String id_user;


    Locale id = new Locale("es", "ID");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", id);
    Date tgl_daftar_date;
    public final Calendar c = Calendar.getInstance();
    private static final String CERO = "0";
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);


    private int currentAnimationFrame = 0;
    private LottieAnimationView animationView;
    android.app.AlertDialog.Builder builder1;
    AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_casos);


        spinnercaso=(Spinner)findViewById(R.id.spiner1);
        extcliente=(EditText)findViewById(R.id.txtcliente);
        imgbuscar=(ImageView)findViewById(R.id.imgbuscar);
        etjuez=(EditText)findViewById(R.id.txtjuez);
        etfecha=(EditText)findViewById(R.id.txtfecha);
        spinneretapa=(Spinner)findViewById(R.id.spineretapa);
    //    etresumen=(EditText)findViewById(R.id.txtresumen);
        btnregistar=(Button)findViewById(R.id.btnregistar);



        mAuth = FirebaseAuth.getInstance();
        id_user = mAuth.getCurrentUser().getUid();
        reference= FirebaseDatabase.getInstance().getReference("MisCasos").child(id_user);



        listtipocaso = new ArrayList<>();
        listtipocaso.add("Indenmizacion");
        listtipocaso.add("Divorcio");
        listtipocaso.add("Custodia");
        listtipocaso.add("Pension");
        listtipocaso.add("Herencia");

        listtiprocespo = new ArrayList<>();
        listtiprocespo.add("Inicio");
        listtiprocespo.add("EnProceso");
        listtiprocespo.add("Suspendido");


        adapterspiner= new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,listtipocaso);
        spinnercaso.setAdapter(adapterspiner);

        adapterproceso= new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,listtiprocespo);
        spinneretapa.setAdapter(adapterproceso);

        // adapterspiner.seta(adapterspiner);

        btnregistar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  tipoCAso=spinnercaso.getSelectedItem().toString();
                String cliente =extcliente.getText().toString();
                String  juex =etjuez.getText().toString();
                String fecha =etfecha.getText().toString();
                String etapa =spinneretapa.getSelectedItem().toString();
                String resumen ="resumen";
                RegistarCaso(tipoCAso,cliente,juex,fecha,etapa,resumen);
            }
        });

        imgbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BucarCliente();
            }
        });

        etfecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog recogerFecha = new DatePickerDialog(CasosActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        final int mesActual = month + 1;
                        c.set(year, month, dayOfMonth);
                        String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                        String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                        etfecha.setText(simpleDateFormat.format(c.getTime()));
                        tgl_daftar_date = c.getTime();
                        //    etfecha.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
                    }
                },anio, mes, dia);
                recogerFecha.show();
            }
        });
    }

    private void BucarCliente() {
        startActivityForResult(new Intent(this,BuscarClienteActivity.class),1);


    }
    private void mensajevOk(){

        builder1 = new AlertDialog.Builder(CasosActivity.this);
        Button btcerrrar;

        View v = LayoutInflater.from(CasosActivity.this).inflate(R.layout.dialogo_check, null);
        animationView = v.findViewById(R.id.animation_viewcheck);
        resetAnimationView();
        animationView.playAnimation();
        builder1.setView(v);
        btcerrrar=(Button)v.findViewById(R.id.idbtncerrardialogo);
        btcerrrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert  = builder1.create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        alert.show();
    }

    private void resetAnimationView() {
        currentAnimationFrame = 0;
        animationView.addValueCallback(new KeyPath("**"), LottieProperty.COLOR_FILTER,
                new SimpleLottieValueCallback<ColorFilter>() {
                    @Override
                    public ColorFilter getValue(LottieFrameInfo<ColorFilter> frameInfo) {
                        return null;
                    }
                }
        );
    }
    private void RegistarCaso(String tipoCAso, String cliente, String juex, String fecha, String etapa, String resumen) {

        progressDialog=new ProgressDialog(CasosActivity.this);
        progressDialog.setTitle("Agregajdo Caso");
        progressDialog.setMessage("cargando");
        progressDialog.show();
        progressDialog.setCancelable(false);

        String key = reference.push().getKey();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();
        String fechas = dateFormat.format(date);
        Casos o =new Casos(tipoCAso,cliente,juex,fecha,etapa,resumen,key);
        reference.child(key).setValue(o).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    mensajevOk();
                    //Toast.makeText(CasosActivity.this, "Registrado", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CasosActivity.this, "Error :" +e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String nombrecliente =data.getStringExtra("nombre");
                String idcliente =data.getStringExtra("id_cliente");
                extcliente.setText(nombrecliente);

            }
        }
    }
}