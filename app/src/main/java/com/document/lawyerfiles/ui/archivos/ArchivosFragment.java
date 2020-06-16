package com.document.lawyerfiles.ui.archivos;

import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.document.lawyerfiles.Clases.ClsCarpetas;
import com.document.lawyerfiles.R;
import com.document.lawyerfiles.activitys.ListaArchivos;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ArchivosFragment extends Fragment {

    private ArchivosViewModel mViewModel;
    FirebaseStorage storage;
    private final int MIS_PERMISOS = 100;
    private DatabaseReference referencecarpetas;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;

    android.app.AlertDialog.Builder builder1;
    AlertDialog alert;
    String user_id;
    public static ArchivosFragment newInstance() {
        return new ArchivosFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.archivos_fragment, container, false);

        FloatingActionButton fab = vista.findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogo();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        referencecarpetas= FirebaseDatabase.getInstance().getReference("Archivos").child(user_id);

        recyclerView=vista.findViewById(R.id.recylcercarpetas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        if(solicitaPermisosVersionesSuperiores()){

        }

        return vista;
    }

    private void agregarCarpeta(String nombrecarpeta) {
        if (TextUtils.isEmpty(nombrecarpeta)){
            Toast.makeText(getContext(), "no hay nombre carpeta", Toast.LENGTH_SHORT).show();
        }
        else{
            progressDialog=new ProgressDialog(getContext());
            progressDialog.setTitle("Agregajdo Carpeta");
            progressDialog.setMessage("cargando");
            progressDialog.show();
            progressDialog.setCancelable(false);
            String key = referencecarpetas.push().getKey();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            Date date = new Date();
            String fecha = dateFormat.format(date);
            ClsCarpetas o =new ClsCarpetas(key,nombrecarpeta,fecha,"0");
            referencecarpetas.child(key).setValue(o).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getContext(), "Agregado", Toast.LENGTH_SHORT).show();
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
    }
    private void dialogo(){
        builder1 = new AlertDialog.Builder(getContext());
        Button btcerrrar;
        final EditText etnombre;
        View v = LayoutInflater.from(getContext()).inflate(R.layout.dialogo_crear_carpeta, null);

        builder1.setView(v);
        btcerrrar=(Button)v.findViewById(R.id.id_btnagregarcarpeta);
        etnombre=(EditText)v.findViewById(R.id.id_etnombrecarpeta);

        btcerrrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nonbrecarpeta =etnombre.getText().toString();
                agregarCarpeta(nonbrecarpeta);
                alert.dismiss();
            }
        });
        alert  = builder1.create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        alert.show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ArchivosViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<ClsCarpetas> recyclerOptions = new FirebaseRecyclerOptions.Builder<ClsCarpetas>()
                .setQuery(referencecarpetas, ClsCarpetas.class).build();
        FirebaseRecyclerAdapter<ClsCarpetas,Items> adapter =new FirebaseRecyclerAdapter<ClsCarpetas, Items>(recyclerOptions) {

            @Override
            protected void onBindViewHolder(@NonNull final Items items, final int i, @NonNull ClsCarpetas tutores) {
                final String key = getRef(i).getKey();
                referencecarpetas.child(key).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()){
                            final String nombre_carpeta=dataSnapshot.child("nombre_carpeta").getValue().toString();
                            final String fecha=dataSnapshot.child("fecha_creacion").getValue().toString();
                            //    final String cantidad=dataSnapshot.child("cantidad_archivos").getValue().toString();
                            items.txtnomnbrcarpeta.setText(nombre_carpeta);
                            items.txtfecha.setText(fecha);

                            items.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent intent=new Intent(getContext(), ListaArchivos.class);
                                    Bundle bundle= new Bundle();
                                    bundle.putString("key",key);
                                    intent.putExtras(bundle);
                                    startActivity(intent);

                                }
                            });

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }

            @NonNull
            @Override
            public Items onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carpetas, parent, false);
                return new Items(vista);

            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }


    public  static class Items extends RecyclerView.ViewHolder{
        TextView txtnomnbrcarpeta,txtfecha,txtcantidad;
        ImageView imgfoto;

        public Items(@NonNull View itemView) {
            super(itemView);
            txtnomnbrcarpeta=(TextView)itemView.findViewById(R.id.id_nombrecarpeta);
            txtfecha=(TextView)itemView.findViewById(R.id.id_fecha);
            txtcantidad=(TextView)itemView.findViewById(R.id.id_cantida);


        }
    }

    private boolean solicitaPermisosVersionesSuperiores() {
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M){//validamos si estamos en android menor a 6 para no buscar los permisos
            return true;
        }

        //validamos si los permisos ya fueron aceptados
        if((getActivity().checkSelfPermission(WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)&&getActivity().checkSelfPermission(CAMERA)==PackageManager.PERMISSION_GRANTED){
            return true;
        }

        if ((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)||(shouldShowRequestPermissionRationale(CAMERA)))){
            cargarDialogoRecomendacion();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MIS_PERMISOS);
        }

        return false;//implementamos el que procesa el evento dependiendo de lo que se defina aqui
    }
    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(getContext());
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe conceder los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
            }
        });
        dialogo.show();
    }

}
