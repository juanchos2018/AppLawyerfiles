package com.document.lawyerfiles.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.document.lawyerfiles.Clases.ClsColegas;
import com.document.lawyerfiles.Clases.ClsEnvios;
import com.document.lawyerfiles.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Enviar_archivosActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    public FirebaseUser currentUser;
    String id_usuario,nombre_usuario,foto_usuario;
    private DatabaseReference reference,reference2,referenceBandeja;
    RecyclerView recyclerView;

    String nombre_archivo;
    String ruta_archivo,tipo_docu,tipo_archi;
    String peso_archivo;
    ImageView imgg;
    TextView txtnombre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_archivos);
        imgg=(ImageView)findViewById(R.id.id_imgearchivo);
        txtnombre=(TextView)findViewById(R.id.id_etnombrearchhivo);

        nombre_archivo=getIntent().getStringExtra("name");
        ruta_archivo  =getIntent().getStringExtra("ruta");
        tipo_docu =getIntent().getStringExtra("tipo_doc");
        tipo_archi =getIntent().getStringExtra("tipo_arc");
        peso_archivo=getIntent().getStringExtra("peso_arc");

        txtnombre.setText(nombre_archivo);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        id_usuario= mAuth.getCurrentUser().getUid();
        reference= FirebaseDatabase.getInstance().getReference("MisColegas").child(id_usuario);
        switch (tipo_archi){
            case "doc":
                imgg.setImageResource(R.drawable.logow1);
                break;
            case "docx":
                imgg.setImageResource(R.drawable.logow1);
                break;
            case "pdf":
                imgg.setImageResource(R.drawable.logopdf);
                break;
            case "ppt":
                imgg.setImageResource(R.drawable.logoppt);
                break;
            case "pptx":
                imgg.setImageResource(R.drawable.logoppt);
                break;

            case "xls":
                imgg.setImageResource(R.drawable.ic_excel);
                break;
            case "xlsx":
                imgg.setImageResource(R.drawable.ic_excel);
                break;
            case "img":
                imgg.setImageResource(R.drawable.ic_foto);
                break;

        }

        reference2= FirebaseDatabase.getInstance().getReference("Usuarios").child(id_usuario);
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    nombre_usuario=dataSnapshot.child("nombre_usuario").getValue().toString();
                    foto_usuario=dataSnapshot.child("image_usuario").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        recyclerView=(RecyclerView)findViewById(R.id.recuclercolegasenviar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<ClsColegas> recyclerOptions = new FirebaseRecyclerOptions.Builder<ClsColegas>()
                .setQuery(reference, ClsColegas.class).build();
        FirebaseRecyclerAdapter<ClsColegas,Items> adapter =new FirebaseRecyclerAdapter<ClsColegas, Items>(recyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final Items items, final int i, @NonNull ClsColegas claseesprofe) {
                final String key = getRef(i).getKey();
                reference.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            final String id_usu=dataSnapshot.child("id_usuario").getValue().toString();
                            final String correo=dataSnapshot.child("correo_usuario").getValue().toString();
                            final String nombre=dataSnapshot.child("nombre_usuario").getValue().toString();
                            final String image_usuario=dataSnapshot.child("image_usuario").getValue().toString();
                            items.tvcorreo.setText(correo);
                            items.tvtnombre.setText(nombre);

                            Glide.with(getApplicationContext())
                                    .load(image_usuario)
                                    .placeholder(R.drawable.default_profile_image)
                                    .fitCenter()
                                    .centerCrop()
                                    .into(items.imgcam);
                            items.btnenviar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                   // ClipboardManager
                                            referenceBandeja=FirebaseDatabase.getInstance().getReference("Bandeja").child(id_usu);
                                    String key =referenceBandeja.push().getKey();
                                    ClsEnvios o=new ClsEnvios(key,id_usu,nombre_archivo,ruta_archivo,peso_archivo,tipo_docu,tipo_archi,"23-23-23");
                                    referenceBandeja.child(key).setValue(o).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            items.btnenviar.setText("Enviado");
                                        }
                                    });



                                //    Toast.makeText(Enviar_archivosActivity.this, "listo para enviar we", Toast.LENGTH_SHORT).show();
                                }
                            });

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
                View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_colegas_compartir, parent, false);
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
        Button btnenviar;

        public Items(final View itemView) {
            super(itemView);
            tvcorreo=(TextView)itemView.findViewById(R.id.id_tvcorreo2);
            tvtnombre=(TextView)itemView.findViewById(R.id.id_nombre2);
            imgcam=(ImageView)itemView.findViewById(R.id.id_imgperfil2);
            btnenviar=(Button)itemView.findViewById(R.id.id_btnenviar);


        }
    }
}
