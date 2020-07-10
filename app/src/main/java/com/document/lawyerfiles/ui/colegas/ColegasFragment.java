package com.document.lawyerfiles.ui.colegas;

import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.document.lawyerfiles.Clases.ClsColegas;
import com.document.lawyerfiles.Clases.ClsUsuarios;
import com.document.lawyerfiles.R;
import com.document.lawyerfiles.activitys.BuscarColegasActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ColegasFragment extends Fragment {

    private ColegasViewModel mViewModel;

    private FirebaseAuth mAuth;
    public FirebaseUser currentUser;
    String id_usuario,nombre_usuario,foto_usuario;
    private DatabaseReference reference,reference2;
    RecyclerView recyclerView;

    android.app.AlertDialog.Builder builder1;
    AlertDialog alert;

    public static ColegasFragment newInstance() {
        return new ColegasFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.colegas_fragment, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        id_usuario= mAuth.getCurrentUser().getUid();
        reference= FirebaseDatabase.getInstance().getReference("MisColegas").child(id_usuario);

        reference2= FirebaseDatabase.getInstance().getReference("Usuarios").child(id_usuario);
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    nombre_usuario = dataSnapshot.child("nombre_usuario").getValue().toString();
                    foto_usuario = dataSnapshot.child("image_usuario").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FloatingActionButton fab = vista.findViewById(R.id.fab3);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                buscarcolegas();
            }
        });

        recyclerView=(RecyclerView)vista.findViewById(R.id.recylcercolegas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        return vista;
    }

    private void buscarcolegas() {
        Intent intent=new Intent(getContext(), BuscarColegasActivity.class);

        Bundle bundle=new Bundle();
        bundle.putString("name",nombre_usuario);
        bundle.putString("foto",foto_usuario);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    @Override
    public void onStart() {
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
                        final Context context = getContext();
                        if (dataSnapshot.exists()){
                            final String correo=dataSnapshot.child("correo_usuario").getValue().toString();
                            final String nombre=dataSnapshot.child("nombre_usuario").getValue().toString();
                         //   final String cargo_usuario=dataSnapshot.child("cargo_usuario").getValue().toString();
                            items.tvcorreo.setText(correo);
                            items.tvtnombre.setText(nombre);

                            String  rutafoto=dataSnapshot.child("image_usuario").getValue().toString();

                            if (rutafoto.equals("default_image")){
                                items.imgcam.setImageResource(R.drawable.default_profile_image);
                            }else {
                                if (isValidContextForGlide(context)){
                                    Glide.with(getContext().getApplicationContext())
                                            .load(rutafoto)
                                            .placeholder(R.drawable.default_profile_image)
                                            .fitCenter()
                                            .centerCrop()
                                            .into(items.imgcam);
                                }

                            }

                            items.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    items.imgcam.setDrawingCacheEnabled(true);
                                    items.imgcam.buildDrawingCache();
                                    Bitmap bitmap = Bitmap.createBitmap(items.imgcam.getDrawingCache());
                                    mostrarcolega(correo,nombre,"cargo",bitmap);
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
                View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_colegas, parent, false);
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


        public Items(final View itemView) {
            super(itemView);
            tvcorreo=(TextView)itemView.findViewById(R.id.id_tvcorreo1);
            tvtnombre=(TextView)itemView.findViewById(R.id.id_nombre1);
            imgcam=(ImageView)itemView.findViewById(R.id.id_imgperfil1);
        }
    }

    private  void  mostrarcolega(final String correo, final String nombre,String cargo,  Bitmap bitmap) {
        builder1 = new AlertDialog.Builder(getContext());
        Button btagregar;
        TextView tvnombre,tvvcorreo,tvcargo;
        ImageView imperfil;
        View v = LayoutInflater.from(getContext()).inflate(R.layout.alert_detalle_colega, null);
        builder1.setView(v);
        tvvcorreo=(TextView)v.findViewById(R.id.id_tvcorreocolega);
        tvnombre=(TextView)v.findViewById(R.id.id_tvnombreocolega);
        tvcargo=(TextView)v.findViewById(R.id.id_tvcargoocolega);
        imperfil=(ImageView)v.findViewById(R.id.id_imgperfil3);
        imperfil.setImageBitmap(bitmap);

       tvcargo.setText(cargo);
       tvnombre.setText(nombre);
       tvvcorreo.setText(correo);


        alert  = builder1.create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.show();
    }


    public static boolean isValidContextForGlide(final Context context) {
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isDestroyed() || activity.isFinishing()) {
                return false;
            }
        }
        return true;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ColegasViewModel.class);
        // TODO: Use the ViewModel
    }

}
