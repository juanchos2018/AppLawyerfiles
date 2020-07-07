package com.document.lawyerfiles.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.document.lawyerfiles.Clases.ClsImagenes;
import com.document.lawyerfiles.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ListaImagenesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference referenceImagenes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_imagenes);

        referenceImagenes= FirebaseDatabase.getInstance().getReference("Imagenes");
        recyclerView=findViewById(R.id.recyclerimagenes);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<ClsImagenes> recyclerOptions = new FirebaseRecyclerOptions.Builder<ClsImagenes>()
                .setQuery(referenceImagenes, ClsImagenes.class).build();
        FirebaseRecyclerAdapter<ClsImagenes,Items> adapter =new FirebaseRecyclerAdapter<ClsImagenes, Items>(recyclerOptions) {

            @Override
            protected void onBindViewHolder(@NonNull final Items items, final int i, @NonNull ClsImagenes tutores) {
                final String key = getRef(i).getKey();
                referenceImagenes.child(key).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()){

                            final String descripcion=dataSnapshot.child("descripcion_imagen").getValue().toString();
                            final String ruta_foto=dataSnapshot.child("ruta_imagen").getValue().toString();

                            items.txtdescripcion.setText(descripcion);
                            Glide.with(getApplicationContext())
                                    .load(ruta_foto)
                                    .placeholder(R.drawable.default_profile_image)
                                    .fitCenter()
                                    .centerCrop()
                                    .into(items.imgfoto);


                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(ListaImagenesActivity.this, "Error", Toast.LENGTH_SHORT).show();

                    }
                });

            }

            @NonNull
            @Override
            public Items onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_imagenes, parent, false);
                return new Items(vista);

            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
    public  static class Items extends RecyclerView.ViewHolder {
        TextView txtdescripcion;
        ImageView imgfoto;

        public Items(@NonNull View itemView) {
            super(itemView);
            txtdescripcion = (TextView) itemView.findViewById(R.id.tvdescripcion);
            imgfoto=(ImageView)itemView.findViewById(R.id.imgfoto);



        }
    }
}
