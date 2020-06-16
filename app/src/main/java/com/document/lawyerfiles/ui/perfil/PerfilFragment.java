package com.document.lawyerfiles.ui.perfil;

import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.document.lawyerfiles.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class PerfilFragment extends Fragment {

    private PerfilViewModel mViewModel;
    Button btnregistrrar,btnabrigaleria;
    ImageView imgfoto;
    private String path;//almacena la ruta de la imagen
    private final static int GALLERY_PICK_CODE = 1;
    Bitmap thumb_Bitmap = null;
    private ProgressDialog progressDialog;
    private static final int COD_SELECCIONA = 10;
    Uri uri;

    private DatabaseReference referenceUsuarios;
    private FirebaseAuth mAuth;
    String correoprofe;
    public FirebaseUser currentUser;
    private StorageReference mStorageRef;

    private StorageReference storageReference;
    private static final int COD_FOTO = 20;
    private final int MIS_PERMISOS = 100;
    public static PerfilFragment newInstance() {
        return new PerfilFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.perfil_fragment, container, false);
        imgfoto=(ImageView)vista.findViewById(R.id.idimgfotoperfil);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        String user_id = mAuth.getCurrentUser().getUid();
        correoprofe=mAuth.getCurrentUser().getEmail();
        referenceUsuarios = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(user_id);
        referenceUsuarios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String img_usuario = dataSnapshot.child("image_usuario").getValue().toString();
                if (img_usuario.equals("default_image")){
                    imgfoto.setImageResource(R.drawable.default_profile_image);

                }
                else{
                    Glide.with(getActivity().getApplicationContext())
                            .load(img_usuario)
                            .placeholder(R.drawable.default_profile_image)
                            .fitCenter()
                            .centerCrop()
                            .error(R.drawable.default_profile_image)
                            .into(imgfoto);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        imgfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/");
                startActivityForResult(intent.createChooser(intent,"Seleccione"),COD_SELECCIONA);// 10
            }
        });
        return vista;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            //TODO : ESTO SELECCIONA DE LA GALERIA
            case COD_SELECCIONA:
                // Uri miPath=data.getData();

                if (data==null){
                    Toast.makeText(getContext(), "No selecciono una imagen", Toast.LENGTH_SHORT).show();
                    return;
                }

                uri=data.getData();

                imgfoto.setImageURI(uri);

                try {
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    progressDialog = new ProgressDialog(getContext());
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setTitle("Subiendo..");
                    progressDialog.setProgress(0);
                    progressDialog.show();
                    final StorageReference mountainsRef=mStorageRef.child(correoprofe).child(uri.getLastPathSegment());
                    imgfoto.setDrawingCacheEnabled(true);
                    imgfoto.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) imgfoto.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
                    byte[] path = baos.toByteArray();


                    final UploadTask uploadTask = mountainsRef.putBytes(path);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }
                                    return mountainsRef.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri downloadUri = task.getResult();
                                        referenceUsuarios.child("image_usuario").setValue(downloadUri.toString());

                                    } else {
                                        Toast.makeText(getContext(), "Error al subir", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            int currentprogress= (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setProgress(currentprogress);
                            if (currentprogress==100){
                                progressDialog.dismiss();
                                String mensaje="Foto subida";
                                Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
                                //   dialogomensaje(mensaje);
                            }

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                Toast.makeText(getContext(), "No existe la foto", Toast.LENGTH_SHORT).show();
                break;
        }

        //    bitmap=redimensionarImagen(bitmap,600,800);

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PerfilViewModel.class);
        // TODO: Use the ViewModel
    }

}
