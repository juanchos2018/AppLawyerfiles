package com.document.lawyerfiles.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.renderscript.ScriptGroup;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.document.lawyerfiles.Clases.ClsClientes;
import com.document.lawyerfiles.R;
import com.document.lawyerfiles.ui.clientes.ClientesFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class BuscarClienteActivity extends AppCompatActivity {

    //ScriptGroup.Binding BuscaBinding
    Button btnenviar;
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
    private EditText etbuscarnombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_cliente);

        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        referenceclientes= FirebaseDatabase.getInstance().getReference("Clientes").child(user_id);
        recyclerView=findViewById(R.id.recyclerclientes2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        etbuscarnombre = findViewById(R.id.id_etbuscarcliente);
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


    }

    private void searchPeopleProfile(String searchString) {

        final Query searchQuery = referenceclientes.orderByChild("nombre_cliente")
                .startAt(searchString.toUpperCase()).endAt(searchString.toUpperCase() + "\uf8ff");
        //final Query searchQuery = peoplesDatabaseReference.orderByChild("search_name").equalTo(searchString);

        FirebaseRecyclerOptions<ClsClientes> recyclerOptions = new FirebaseRecyclerOptions.Builder<ClsClientes>()
                .setQuery(searchQuery, ClsClientes.class)
                .build();
        final Context context = this;
        FirebaseRecyclerAdapter<ClsClientes, Items> adapter = new FirebaseRecyclerAdapter<ClsClientes, Items>(recyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final Items holder, final int position, @NonNull final ClsClientes model) {

                  holder.txtnomcliente.setText(model.getNombre_cliente().toLowerCase());
                  holder.txtcelular.setText(model.getCelular_cliente());
                  holder.txtcaracter.setText(model.getCaracter());
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent data = new Intent();
                            Bundle bundle= new Bundle();
                            bundle.putString("nombre", model.getNombre_cliente());
                            bundle.putString("id_cliente", model.getId_cliente());
                            data.putExtras(bundle);
                            setResult(RESULT_OK, data);
                            finish();
                        }
                    });

            }

            @NonNull
            @Override
            public Items onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_clientes, viewGroup, false);
                return new Items(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<ClsClientes> recyclerOptions = new FirebaseRecyclerOptions.Builder<ClsClientes>()
                .setQuery(referenceclientes, ClsClientes.class).build();
        FirebaseRecyclerAdapter<ClsClientes, Items> adapter =new FirebaseRecyclerAdapter<ClsClientes, Items>(recyclerOptions) {

            @Override
            protected void onBindViewHolder(@NonNull final Items items, final int i, @NonNull ClsClientes tutores) {
                final String key = getRef(i).getKey();
                referenceclientes.child(key).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()){
                            final String nombre_cliente=dataSnapshot.child("nombre_cliente").getValue().toString();
                            final String celular=dataSnapshot.child("celular_cliente").getValue().toString();
                            final String caracter=dataSnapshot.child("caracter").getValue().toString();
                            final String id_cliente=dataSnapshot.child("id_cliente").getValue().toString();
                            items.txtnomcliente.setText(nombre_cliente);
                            items.txtcelular.setText(celular);
                            items.txtcaracter.setText(caracter);

                            items.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent data = new Intent();
                                    ClsClientes cli = new ClsClientes();
                                    cli.setNombre_cliente(nombre_cliente);
                                    cli.setId_cliente(id_cliente);


                                    Bundle bundle= new Bundle();
                                    bundle.putString("nombre", nombre_cliente);
                                    bundle.putString("id_cliente", id_cliente);
                                    data.putExtras(bundle);
                                    setResult(RESULT_OK, data);
                                    finish();

                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(BuscarClienteActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

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



    public   class Items extends RecyclerView.ViewHolder{
        
        TextView txtnomcliente,txtcelular,txtcaracter;
        ImageView imgfoto;

        public Items(@NonNull View itemView) {
            super(itemView);
            txtnomcliente=(TextView)itemView.findViewById(R.id.id_tvnombrecliente);
            txtcelular=(TextView)itemView.findViewById(R.id.id_tvcelularcliente);
            txtcaracter=(TextView)itemView.findViewById(R.id.idcaracter);
        }
    }
}

