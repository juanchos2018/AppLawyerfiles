package com.document.lawyerfiles.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.document.lawyerfiles.Clases.ClsArchivos;
import com.document.lawyerfiles.Clases.ClsCarpetas;
import com.document.lawyerfiles.R;
import com.document.lawyerfiles.adapters.AdapterCarpetas;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MoverCarpetaActivity extends AppCompatActivity {

    FirebaseStorage storage;
    private final int MIS_PERMISOS = 100;
    private DatabaseReference referencecarpetas,referencearchivos,reference2;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;

    android.app.AlertDialog.Builder builder1;
    AlertDialog alert;
    String user_id;

    GridView simpleList;
    ArrayList<ClsCarpetas> birdList=new ArrayList<>();
    TextView txt1,txt2;
    String idcarpeta;

    String nombre_archivo;
    String ruta_archivo,tipo_docu,tipo_archi,id_archivo;
    String peso_archivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mover_carpeta);
        setTitle("Seleccionar Destino");
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        referencecarpetas= FirebaseDatabase.getInstance().getReference("Archivos").child(user_id);
        simpleList=(GridView)findViewById(R.id.simpleGridView1);
        txt1=(TextView)findViewById(R.id.txtcancelar);
        txt2=(TextView)findViewById(R.id.txtmover);

        id_archivo=getIntent().getStringExtra("id_archivo");

        nombre_archivo=getIntent().getStringExtra("name");
        ruta_archivo  =getIntent().getStringExtra("ruta");
        tipo_docu =getIntent().getStringExtra("tipo_doc");
        tipo_archi =getIntent().getStringExtra("tipo_arc");
        peso_archivo=getIntent().getStringExtra("peso_arc");
        txt2.setEnabled(false);

        birdList = new ArrayList<>();

        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(idcarpeta))
                {
                    Toast.makeText(MoverCarpetaActivity.this, "no elijgio carpeta", Toast.LENGTH_SHORT).show();
                    return;
                }
                mover(idcarpeta);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Query q=referencecarpetas;
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                birdList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ClsCarpetas artist = postSnapshot.getValue(ClsCarpetas.class);
                    birdList.add(artist);
                }
                //AdapterCarpetas2 myAdapter=new AdapterCarpetas2(ListaArchivos3Activity.this,R.layout.grid_view_items,birdList);
                AdapterCarpetas myAdapter=new AdapterCarpetas(getApplicationContext(),birdList);
                //   AdapterCarpetas2 adapterCarpetas2=new AdapterCarpetas2(birdList);
                simpleList.setAdapter(myAdapter);
                simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        txt2.setEnabled(true);
                        txt2.setTextColor(getColor( R.color.bgAzul));


                        idcarpeta= birdList.get(position).getId_carpeta();


                       // Toast.makeText(MoverCarpetaActivity.this, birdList.get(position).getId_carpeta(), Toast.LENGTH_SHORT).show();
                     //    Toast.makeText(ListaArchivos3Activity.this,  + position + " " + birdList.get(position).getId_carpeta(), Toast.LENGTH_SHORT).show();
                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void  mover(String keycarpeta){

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando..");
        progressDialog.setTitle("Moviendo");

        progressDialog.show();
        progressDialog.setCancelable(false);

        referencearchivos= FirebaseDatabase.getInstance().getReference("Archivos2").child(keycarpeta);
        String key  = referencearchivos.push().getKey();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);
         ClsArchivos obj= new ClsArchivos(key,nombre_archivo,tipo_docu,tipo_archi,"12",fecha,ruta_archivo,"");
        referencearchivos.child(key).setValue(obj);

         //
        reference2= FirebaseDatabase.getInstance().getReference("Archivos2").child(keycarpeta).child(id_archivo);
        reference2.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if ( task.isSuccessful()){
                    Toast.makeText(MoverCarpetaActivity.this, "Movido we", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                     finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MoverCarpetaActivity.this, "ERror", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
    private void  Eliminar(){
        // ClsArchivos obj= new ClsArchivos(key,nombrearchivo,tipodocumento,tipoarchivo,"12",fecha,dowloand.toString(),"");
    }
}
