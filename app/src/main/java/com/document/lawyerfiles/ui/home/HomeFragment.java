package com.document.lawyerfiles.ui.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.document.lawyerfiles.Clases.ClsClientes;
import com.document.lawyerfiles.Clases.ClsColegas;
import com.document.lawyerfiles.R;
import com.document.lawyerfiles.activitys.MapaActivity;
import com.document.lawyerfiles.activitys.NoticiasActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    Button btnscanear;
    private DatabaseReference referenceclientes,referencecolegas,reference2,getReferenceclases2;
    private FirebaseAuth mAuth;
    public FirebaseUser currentUser;
    private ProgressDialog progressDialog;

    TextView txt1,txt2;

    public  static  int cantcolegas;
    TextView txtclientes,txtcolegas;
    CardView carNoticias;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_home, container, false);

        btnscanear=(Button)vista.findViewById(R.id.id_btnscanear);
        txtclientes=(TextView)vista.findViewById(R.id.idcantidaclases);
        txtcolegas=(TextView)vista.findViewById(R.id.idcantidcolegas);
        carNoticias =(CardView)vista.findViewById(R.id.carNoticias);

        txt1=(TextView)vista.findViewById(R.id.tvFormat);
        txt2=(TextView)vista.findViewById(R.id.tvresult);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Espere un momento");
        progressDialog.setCancelable(false);
        progressDialog.show();
        if (currentUser != null) {
            final String user_id = mAuth.getCurrentUser().getUid();
            referenceclientes = FirebaseDatabase.getInstance().getReference("Clientes").child(user_id);
            referencecolegas = FirebaseDatabase.getInstance().getReference("MisColegas").child(user_id);


        }
         btnscanear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanear();
            }
        });
        carNoticias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( getContext(), NoticiasActivity.class));

            }
        });
        return vista;
    }
    private void scanear() {

        new IntentIntegrator(getActivity()).initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        final IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,intent);
        manipularResultado(intentResult);
    }

    private  void actualiarUITextViews(String resultadoScaneo,String formatoResultado){

        txt1.setText(formatoResultado);
        txt2.setText(resultadoScaneo);

    }
    private  void manipularResultado(IntentResult intentResult){
        if(intentResult != null){

            actualiarUITextViews(intentResult.getContents(),intentResult.getFormatName());
        }else{
            Toast.makeText(getContext(),"No se leyó nada",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onStart() {
        super.onStart();

        if (currentUser != null) {
            referenceclientes.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        int contador=0;
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            ClsClientes obj = postSnapshot.getValue(ClsClientes.class);
                            contador++;
                        }
                        txtclientes.setText(String.valueOf( contador));
                    }
                    else{
                        txtclientes.setText("0");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            referencecolegas.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        int contador=0;
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            ClsColegas obj = postSnapshot.getValue(ClsColegas.class);
                            contador++;
                        }
                        txtcolegas.setText(String.valueOf( contador));
                        cantcolegas=contador;
                        progressDialog.dismiss();
                    }
                    else{
                        txtcolegas.setText("0");
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

    }

}