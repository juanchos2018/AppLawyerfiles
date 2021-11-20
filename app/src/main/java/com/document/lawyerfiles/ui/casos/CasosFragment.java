package com.document.lawyerfiles.ui.casos;

import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.document.lawyerfiles.Clases.Casos;
import com.document.lawyerfiles.Clases.ClsCarpetas;
import com.document.lawyerfiles.Clases.ClsClientes;
import com.document.lawyerfiles.Clases.ClsUsuarios;
import com.document.lawyerfiles.R;
import com.document.lawyerfiles.activitys.BuscarClienteActivity;
import com.document.lawyerfiles.activitys.CasosActivity;
import com.document.lawyerfiles.ui.colegas.ColegasFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.microedition.khronos.egl.EGLDisplay;

public class CasosFragment extends Fragment {

    private CasosViewModel mViewModel;

    public static CasosFragment newInstance() {
        return new CasosFragment();
    }


       EditText extcliente,etfecha,etjuez,etresumen;
    Spinner spinnercaso,spinneretapa;
    Button btnregistar;
    private ProgressDialog progressDialog;
    ImageView imgbuscar;
    RecyclerView recyclerView;
    private DatabaseReference reference;

    ArrayList<String> listtiprocespo;
    ArrayAdapter<String> adapterproceso;


    ArrayList<String> listtipocaso;
    ArrayAdapter<String> adapterspiner;
    private FirebaseAuth mAuth;

    String id_user;

    private EditText etbuscarnombre;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.casos_fragment, container, false);

        spinnercaso=(Spinner)vista.findViewById(R.id.spiner1);
        extcliente=(EditText)vista.findViewById(R.id.txtcliente);
        imgbuscar=(ImageView)vista.findViewById(R.id.imgbuscar);
        etjuez=(EditText)vista.findViewById(R.id.txtjuez);
        etfecha=(EditText)vista.findViewById(R.id.txtfecha);
        spinneretapa=(Spinner)vista.findViewById(R.id.spineretapa);
        //etresumen=(EditText)vista.findViewById(R.id.txtresumen);
        btnregistar=(Button)vista.findViewById(R.id.btnregistar);

        FloatingActionButton fab = vista.findViewById(R.id.fab4);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                agregaacasos();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        id_user = mAuth.getCurrentUser().getUid();
        reference= FirebaseDatabase.getInstance().getReference("MisCasos").child(id_user);

        recyclerView=(RecyclerView)vista.findViewById(R.id.recylcercasos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        etbuscarnombre = vista.findViewById(R.id.idetbuscarclase6);
        etbuscarnombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchPeopleProfile(etbuscarnombre.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return  vista;
    }


    private void searchPeopleProfile(String searchString) {

        final Query searchQuery = reference.orderByChild("cliente")
                .startAt(searchString.toUpperCase()).endAt(searchString.toUpperCase() + "\uf8ff");
        //final Query searchQuery = peoplesDatabaseReference.orderByChild("search_name").equalTo(searchString);

        FirebaseRecyclerOptions<Casos> recyclerOptions = new FirebaseRecyclerOptions.Builder<Casos>()
                .setQuery(searchQuery, Casos.class)
                .build();
        final Context context = getContext();
        FirebaseRecyclerAdapter<Casos, Items> adapter = new FirebaseRecyclerAdapter<Casos, Items>(recyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final Items holder, final int position, @NonNull final Casos model) {

                      holder.tvtipo.setText(model.getTipoCAso());
                       holder.tvcliente.setText(model.getCliente());
                       holder.tvfecha.setText(model.getFecha());
                        holder.tvsteado.setText(model.getEtapa());


            }

            @NonNull
            @Override
            public Items onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_casos, viewGroup, false);
                return new Items(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }



    private void agregaacasos() {

        startActivity( new Intent(getContext(), CasosActivity.class));

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Casos> recyclerOptions = new FirebaseRecyclerOptions.Builder<Casos>()
                .setQuery(reference, Casos.class).build();
        FirebaseRecyclerAdapter<Casos, Items> adapter =new FirebaseRecyclerAdapter<Casos, Items>(recyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final Items items, final int i, @NonNull Casos claseesprofe) {
                final String key = getRef(i).getKey();
                reference.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final Context context = getContext();
                        if (dataSnapshot.exists()){
                            final String tipoCAso=dataSnapshot.child("tipoCAso").getValue().toString();
                            final String nombre=dataSnapshot.child("cliente").getValue().toString();
                            final String fecha=dataSnapshot.child("fecha").getValue().toString();
                            final String estado=dataSnapshot.child("etapa").getValue().toString();

                            items.tvtipo.setText(tipoCAso);
                            items.tvcliente.setText(nombre);
                            items.tvfecha.setText(fecha);
                            items.tvsteado.setText(estado);

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
                View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_casos, parent, false);
                return new  Items(vista);

            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }

    public   class Items extends RecyclerView.ViewHolder{
        TextView tvtipo,tvcliente,tvfecha,tvsteado;
        ImageView imgcam;
        String id_usario,nombreclase;


        public Items(final View itemView) {
            super(itemView);
            tvtipo=(TextView)itemView.findViewById(R.id.txtcaso);
            tvcliente=(TextView)itemView.findViewById(R.id.txtcliente);
            tvfecha=(TextView)itemView.findViewById(R.id.txtfecha);
            tvsteado=(TextView)itemView.findViewById(R.id.txtestado);
        }
    }

    private void RegistarCaso(String tipoCAso, String cliente, String juex, String fecha, String etapa, String resumen) {


        progressDialog=new ProgressDialog(getContext());
        progressDialog.setTitle("Agregajdo Carpeta");
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
                    Toast.makeText(getContext(), "Registrado", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error :" +e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CasosViewModel.class);
        // TODO: Use the ViewModel
    }

}