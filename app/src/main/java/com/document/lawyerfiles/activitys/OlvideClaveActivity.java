package com.document.lawyerfiles.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.document.lawyerfiles.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class OlvideClaveActivity extends AppCompatActivity {


    EditText etcorreo;
    Button btnenviar;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvide_clave);

        etcorreo=(EditText)findViewById(R.id.correoenviarmensaje);
        btnenviar=(Button)findViewById(R.id.idbotonenviarmensaje);
        mAuth=FirebaseAuth.getInstance();
        btnenviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo=etcorreo.getText().toString().trim();
                enviarmensaje(correo);
            }
        });
    }
    private void enviarmensaje(String correo) {
        progressDialog=new ProgressDialog(this);
        if (TextUtils.isEmpty(correo)){
            etcorreo.setError("campo requerido");
        }else{

            progressDialog.setTitle("verficando");
            progressDialog.setMessage("cargando..");
            progressDialog.show();

            mAuth.setLanguageCode("es");
            mAuth.sendPasswordResetEmail(correo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        progressDialog.dismiss();
                        Toast.makeText(OlvideClaveActivity.this, "Mensaje enviado a su correo", Toast.LENGTH_SHORT).show();
                        limpiarcajas();
                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(OlvideClaveActivity.this, "El correo no existe", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    private void limpiarcajas(){
        etcorreo.setText("");
    }
}
