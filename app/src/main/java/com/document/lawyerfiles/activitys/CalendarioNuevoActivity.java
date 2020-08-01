package com.document.lawyerfiles.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.document.lawyerfiles.Clases.Calendario;
import com.document.lawyerfiles.Clases.ClsCarpetas;
import com.document.lawyerfiles.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

public class CalendarioNuevoActivity extends AppCompatActivity {

    private static final String CERO = "0";
    private static final String BARRA = "/";

    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);


    private DatabaseReference referenceCalendario;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    EditText etPlannedDate;
    String user_id;
    EditText asunto,prioridad,descripcion;
    Button btnregistrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario_nuevo);


         etPlannedDate = (EditText) findViewById(R.id.etPlannedDate);
         asunto= (EditText) findViewById(R.id.inputAsunto);
        prioridad= (EditText) findViewById(R.id.inputPrioridad);
        descripcion= (EditText) findViewById(R.id.inputDescripcion);

        btnregistrar=(Button)findViewById(R.id.btnregistrar);


        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        referenceCalendario= FirebaseDatabase.getInstance().getReference("Calendario").child(user_id);

        etPlannedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerFecha();
            }
        });

        btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fecha=etPlannedDate.getText().toString();
                String  asu =asunto.getText().toString();
                String pri=prioridad.getText().toString();
                String descr=descripcion.getText().toString();
                registrar(fecha,asu,pri,descr);
            }
        });
    }

    private void registrar(String fecha, String asu, String pri, String descr) {

        if (TextUtils.isEmpty(fecha)){
            etPlannedDate.setError("Campo Requerido");
        }
        else if (TextUtils.isEmpty(asu)){
            asunto.setError("Campo Requerido");
        }
        else if (TextUtils.isEmpty(pri)){
            prioridad.setError("Campo Requerido");
        }

        else  if (TextUtils.isEmpty(descr)){
            descripcion.setError("Campo Requerido");
        }
        else{

            String key = referenceCalendario.push().getKey();
            Calendario o =new Calendario(key,fecha,asu,pri,descr);
            referenceCalendario.child(key).setValue(o).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Agregado", Toast.LENGTH_SHORT).show();
                        //progressDialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Error :" +e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    private void obtenerFecha(){
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                etPlannedDate.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);


            }

        },anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();

    }



}
