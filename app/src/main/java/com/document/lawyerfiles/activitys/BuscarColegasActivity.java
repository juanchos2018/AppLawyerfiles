package com.document.lawyerfiles.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.document.lawyerfiles.Clases.ClsColegas;
import com.document.lawyerfiles.Clases.ClsUsuarios;
import com.document.lawyerfiles.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BuscarColegasActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    public FirebaseUser currentUser;
    String id_usuario,correo_local,nombre_local,foto_local;
    private DatabaseReference reference,reference2,reference3,reference4;
    RecyclerView recyclerView;

    android.app.AlertDialog.Builder builder1;
    AlertDialog alert;
    private EditText et_buscar;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_colegas);


        nombre_local=getIntent().getStringExtra("name");
        foto_local=getIntent().getStringExtra("foto");

       // Toast.makeText(this, nombre_local, Toast.LENGTH_SHORT).show();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        id_usuario= mAuth.getCurrentUser().getUid();
        correo_local=mAuth.getCurrentUser().getEmail();
        reference= FirebaseDatabase.getInstance().getReference("Usuarios");

        reference4= FirebaseDatabase.getInstance().getReference("Usuarios").child(id_usuario);
        reference4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference2=FirebaseDatabase.getInstance().getReference("MisColegas").child(id_usuario);

        recyclerView=(RecyclerView)findViewById(R.id.recyclercorreos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        et_buscar=(EditText)findViewById(R.id.id_etbuscarcorreo);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<ClsUsuarios> recyclerOptions = new FirebaseRecyclerOptions.Builder<ClsUsuarios>()
                .setQuery(reference, ClsUsuarios.class).build();
        FirebaseRecyclerAdapter<ClsUsuarios,Items> adapter =new FirebaseRecyclerAdapter<ClsUsuarios, Items>(recyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final Items items, final int i, @NonNull ClsUsuarios claseesprofe) {
                final String key = getRef(i).getKey();
                reference.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            final String correo=dataSnapshot.child("correo_usuario").getValue().toString();
                            final String nombre=dataSnapshot.child("nombre_usuario").getValue().toString();
                            final String id_colega=dataSnapshot.child("id_usuario").getValue().toString();
                            final String image_usuario=dataSnapshot.child("image_usuario").getValue().toString();
                            items.tvcorreo.setText(correo);
                            items.tvtnombre.setText(nombre);


                            items.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    items.imgcam.setDrawingCacheEnabled(true);
                                    items.imgcam.buildDrawingCache();
                                    Bitmap bitmap = Bitmap.createBitmap(items.imgcam.getDrawingCache());
                                    mostrarcolega(id_colega,correo,nombre,image_usuario,bitmap);

                                }
                            });

                            Glide.with(getBaseContext())
                                    .load(image_usuario)
                                    .placeholder(R.drawable.default_profile_image)
                                    .fitCenter()
                                    .centerCrop()
                                    .into(items.imgcam);



                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public Items onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_correos, parent, false);
                return new Items(vista);

            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
    public   class Items extends RecyclerView.ViewHolder{
        TextView tvtnombre,tvcorreo,tvcargo;
        ImageView imgcam;
        String id_usario,nombreclase;
        Button button;


        public Items(final View itemView) {
            super(itemView);
            tvcorreo=(TextView)itemView.findViewById(R.id.id_tvcorreo);
            tvtnombre=(TextView)itemView.findViewById(R.id.id_nombre);
            imgcam=(ImageView)itemView.findViewById(R.id.id_imgperfil);


        }
    }
    private  void  mostrarcolega(final String id_colega, final String correo, final String nombre, final String image_usuario,Bitmap bitmap) {
        builder1 = new AlertDialog.Builder(BuscarColegasActivity.this);
        Button btagregar;
        TextView tvestado;
        ImageView imperfil;
        View v = LayoutInflater.from(BuscarColegasActivity.this).inflate(R.layout.dialogo_agregar_colega, null);
        builder1.setView(v);
        btagregar=(Button)v.findViewById(R.id.idbtncerrardialogo);
        tvestado=(TextView)v.findViewById(R.id.idestado);
        imperfil=(ImageView)v.findViewById(R.id.id_imgperfil2);
        imperfil.setImageBitmap(bitmap);

        tvestado.setText("Foto Perfil");
        btagregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // String key=reference2.push().getKey();
                progressDialog=new ProgressDialog(BuscarColegasActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.setTitle("Agregando Colega");
                progressDialog.setMessage("Cargando..");
                progressDialog.show();
                ClsColegas o =new ClsColegas(id_colega,id_colega,nombre,correo,image_usuario);// cambie esto porid_colega estaba key
                reference2.child(id_colega).setValue(o).addOnCompleteListener(new OnCompleteListener<Void>() { // cambie esto porid_colega estaba key
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            reference3=FirebaseDatabase.getInstance().getReference("MisColegas").child(id_colega);
                            String key2=reference3.push().getKey();
                            ClsColegas o2=new ClsColegas(id_usuario,id_usuario,nombre_local,correo_local,foto_local);
                            reference3.child(id_usuario).setValue(o2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if ( task.isSuccessful()){
                                        progressDialog.dismiss();
                                        alert.dismiss();
                                        Toast.makeText(BuscarColegasActivity.this, "Agregado We", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(BuscarColegasActivity.this, "Errror", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        alert  = builder1.create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.show();
    }

}
