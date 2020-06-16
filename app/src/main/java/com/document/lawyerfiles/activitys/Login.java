package com.document.lawyerfiles.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.document.lawyerfiles.Principal;
import com.document.lawyerfiles.R;
import com.document.lawyerfiles.Registro;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class Login extends AppCompatActivity {


    EditText et_correo,et_clave;
    Button btningresar;
    TextView tv_registrar;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference userDatabaseReference;
    DatabaseReference reference,reference2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        cargarcontroles();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userDatabaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");
        btningresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo=et_correo.getText().toString();
                String clave=et_clave.getText().toString();
                ingresar(correo,clave);
            }
        });
        tv_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarnuvo();
            }
        });
    }


    private void cargarcontroles() {

        et_correo=(EditText)findViewById(R.id.inputEmail);
        et_clave=(EditText)findViewById(R.id.inputPassword);
        btningresar=(Button)findViewById(R.id.loginButton);
        tv_registrar=(TextView)findViewById(R.id.registarse);

    }
    private void registrarnuvo() {
        startActivity(new Intent(Login.this, Registro.class));

    }

    private void ingresar(String correo, String clave) {
        progressDialog = new ProgressDialog(this);
        if (TextUtils.isEmpty(correo)){
            et_correo.setError("campo requerido");

        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            Toast.makeText(this, "Correo no valido", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(clave)){
            et_clave.setError("campo requerido");
        }
        else{

            progressDialog.setMessage("Cargando...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            mAuth.signInWithEmailAndPassword(correo,clave).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        String userUID = mAuth.getCurrentUser().getUid();
                        String userDeiceToken = FirebaseInstanceId.getInstance().getToken();

                        userDatabaseReference.child(userUID).child("device_token").setValue(userDeiceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                checkVerifiedEmail();
                            }
                        });
                    }
                    else{
                        Toast.makeText(Login.this, "Verifique su Email", Toast.LENGTH_SHORT).show();
                    }

                    progressDialog.dismiss();
                }
            });

        }


    }
    private void checkVerifiedEmail() {
        user = mAuth.getCurrentUser();
        boolean isVerified = false;
        if (user != null) {
            isVerified = user.isEmailVerified();
        }
        if (isVerified){

            final String UID = mAuth.getCurrentUser().getUid();
            userDatabaseReference.child(UID).child("verified").setValue("true");
            Intent intent = new Intent(Login.this, Principal.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            userDatabaseReference.child(UID).child("active_now").setValue("true");


            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Correo no verificado", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
        }
    }
}
