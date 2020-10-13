package com.document.lawyerfiles.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.document.lawyerfiles.Clases.Casos;
import com.document.lawyerfiles.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        etresumen=(EditText)findViewById(R.id.txtresumen);
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
                String resumen =etresumen.getText().toString();
                RegistarCaso(tipoCAso,cliente,juex,fecha,etapa,resumen);
            }
        });
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
                    Toast.makeText(CasosActivity.this, "Registrado", Toast.LENGTH_SHORT).show();
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

}