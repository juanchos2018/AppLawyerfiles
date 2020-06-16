package com.document.lawyerfiles.ui.clientes;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.document.lawyerfiles.Clases.ClsClientes;
import com.document.lawyerfiles.R;
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
import static android.Manifest.permission.READ_CONTACTS;

public class ClientesFragment extends Fragment {

    private ClientesViewModel mViewModel;
    private final int MIS_PERMISOS = 100;
    public static final int PICK_CONTACT_REQUEST = 1 ;
    private Uri contactUri;

    private DatabaseReference referenceclientes;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;

    TextView tvnombre;
    String user_id;
    public static ClientesFragment newInstance() {
        return new ClientesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View vista =inflater.inflate(R.layout.clientes_fragment, container, false);

        FloatingActionButton fab = vista.findViewById(R.id.fab2);


        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        referenceclientes= FirebaseDatabase.getInstance().getReference("Clientes").child(user_id);
        recyclerView=vista.findViewById(R.id.recuclerclientes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mostrarcontactos();
            }
        });

        if (solicitaPermisosVersionesSuperiores()){

        }
        return vista;
    }
    private void mostrarcontactos() {

        Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(i, PICK_CONTACT_REQUEST);
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<ClsClientes> recyclerOptions = new FirebaseRecyclerOptions.Builder<ClsClientes>()
                .setQuery(referenceclientes, ClsClientes.class).build();
        FirebaseRecyclerAdapter<ClsClientes,Items> adapter =new FirebaseRecyclerAdapter<ClsClientes, Items>(recyclerOptions) {

            @Override
            protected void onBindViewHolder(@NonNull final Items items, final int i, @NonNull ClsClientes tutores) {
                final String key = getRef(i).getKey();
                referenceclientes.child(key).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()){
                            final String nombre_cliente=dataSnapshot.child("nombre_cliente").getValue().toString();
                            final String celular=dataSnapshot.child("celular_cliente").getValue().toString();
                            //    final String cantidad=dataSnapshot.child("cantidad_archivos").getValue().toString();
                            items.txtnomcliente.setText(nombre_cliente);
                            items.txtcelular.setText(celular);

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

                View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_clientes, parent, false);
                return new Items(vista);

            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    public  static class Items extends RecyclerView.ViewHolder{
        TextView txtnomcliente,txtcelular,txtcantidad;
        ImageView imgfoto;

        public Items(@NonNull View itemView) {
            super(itemView);
            txtnomcliente=(TextView)itemView.findViewById(R.id.id_tvnombrecliente);
            txtcelular=(TextView)itemView.findViewById(R.id.id_tvcelularcliente);



        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                contactUri = data.getData();
                // renderContact(contactUri);
                saveContacto(contactUri);
            }
        }

    }

    private void renderContact(Uri uri) {
        tvnombre.setText(getName(uri));
        String asd=getPhone(uri);

    }
    private  void saveContacto(Uri uri){
        if (uri==null){
            Toast.makeText(getContext(), "no hay cliente we", Toast.LENGTH_SHORT).show();
        }
        else{
            progressDialog=new ProgressDialog(getContext());
            progressDialog.setTitle("Agregajdo Carpeta");
            progressDialog.setMessage("cargando");
            progressDialog.show();
            progressDialog.setCancelable(false);
            String key = referenceclientes.push().getKey();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            Date date = new Date();
            String fecha = dateFormat.format(date);
            ClsClientes o =new ClsClientes(key,getName(uri),getPhone(uri),"","");
            referenceclientes.child(key).setValue(o).addOnCompleteListener(new OnCompleteListener<Void>() {
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
    private String getName(Uri uri) {

        String name = null;
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor c = contentResolver.query(
                uri,
                new String[]{ContactsContract.Contacts.DISPLAY_NAME},
                null,
                null,
                null);

        if(c.moveToFirst()){
            name = c.getString(0);
        }
        c.close();
        return name;
    }
    private String getPhone(Uri uri) {
        /*
        Variables temporales para el id y el teléfono
         */
        String id = null;
        String phone = null;

        /************* PRIMERA CONSULTA ************/
        /*
        Obtener el _ID del contacto
         */
        Cursor contactCursor = getActivity().getContentResolver().query(
                uri,
                new String[]{ContactsContract.Contacts._ID},
                null,
                null,
                null);


        if (contactCursor.moveToFirst()) {
            id = contactCursor.getString(0);
        }
        contactCursor.close();

        /************* SEGUNDA CONSULTA ************/
        /*
        Sentencia WHERE para especificar que solo deseamos
        números de telefonía móvil
         */
        String selectionArgs =
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE+"= " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;

        /*
        Obtener el número telefónico
         */
        Cursor phoneCursor = getActivity().getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER },
                selectionArgs,
                new String[] { id },
                null
        );
        if (phoneCursor.moveToFirst()) {
            phone = phoneCursor.getString(0);
        }
        phoneCursor.close();

        return phone;
    }
    private boolean solicitaPermisosVersionesSuperiores() {
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M){//validamos si estamos en android menor a 6 para no buscar los permisos
            return true;
        }

        //validamos si los permisos ya fueron aceptados
        if((getActivity().checkSelfPermission(READ_CONTACTS)== PackageManager.PERMISSION_GRANTED)&&getActivity().checkSelfPermission(CAMERA)==PackageManager.PERMISSION_GRANTED){
            return true;
        }

        if ((shouldShowRequestPermissionRationale(READ_CONTACTS)||(shouldShowRequestPermissionRationale(READ_CONTACTS)))){
            cargarDialogoRecomendacion();
        }else{
            requestPermissions(new String[]{READ_CONTACTS, READ_CONTACTS}, MIS_PERMISOS);
        }

        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i("Mensaje", "No se tiene permiso para enviar SMS.");
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, 225);
        } else {
            Log.i("Mensaje", "Se tiene permiso para enviar SMS!");
        }
        return false;//implementamos el que procesa el evento dependiendo de lo que se defina aqui AAsdsd
    }
    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(getContext());
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe conceder los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{READ_CONTACTS,READ_CONTACTS},100);
            }
        });
        dialogo.show();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ClientesViewModel.class);
        // TODO: Use the ViewModel
    }

}
