package com.document.lawyerfiles;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.document.lawyerfiles.activitys.Login;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class Principal extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private FirebaseAuth mAuth;
    public FirebaseUser currentUser;
    private DatabaseReference userDatabaseReference;

    TextView txtnombre,tvtorrreo;
    ImageView imgfoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,R.id.nav_archivos,R.id.nav_clientes,R.id.nav_colegas,R.id.nav_bandeja,R.id.nav_consultas,R.id.nav_perfil).setDrawerLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        View navHeader = navigationView.getHeaderView(0);


        txtnombre = navHeader.findViewById(R.id.id_tvnombre);
        tvtorrreo = navHeader.findViewById(R.id.textView);
        imgfoto=(ImageView)navHeader.findViewById(R.id.imageView);


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null){
            final String  user_uID = mAuth.getCurrentUser().getUid();
            userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(user_uID);
            String correo=mAuth.getCurrentUser().getEmail();
            tvtorrreo.setText(correo);
            userDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String nombre=dataSnapshot.child("nombre_usuario").getValue().toString();
                    String urlimageperfil=dataSnapshot.child("image_usuario").getValue().toString();
                    txtnombre.setText(nombre);
                    if (urlimageperfil.equals("default_image")){
                        imgfoto.setImageDrawable(getDrawable(R.drawable.default_profile_image));

                    }else{
                        Glide.with(getApplicationContext())
                                .load(urlimageperfil)
                                .placeholder(R.drawable.default_profile_image)
                                .fitCenter()
                                .thumbnail(0.1f)
                                .error(R.drawable.default_profile_image)
                                .centerCrop()
                                .into(imgfoto);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        //checking logging, if not login redirect to Login ACTIVITY
        if (currentUser == null ){
            mAuth.signOut();
            logOutUser(); // Return to Login activity

        }
        if (currentUser != null ){
            userDatabaseReference.child("active_now").setValue("true");

        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId()==R.id.action_settings){

            if (currentUser != null){
                // userDatabaseReference.child("active_now").setValue(ServerValue.TIMESTAMP);
            }
            mAuth.signOut();
            logOutUser();
        }
        return super.onOptionsItemSelected(item);
    }
    private void logOutUser() {

        Intent loginIntent =  new Intent(Principal.this, Login.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

}
