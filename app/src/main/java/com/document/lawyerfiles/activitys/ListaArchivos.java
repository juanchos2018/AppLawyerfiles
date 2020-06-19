package com.document.lawyerfiles.activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.SimpleLottieValueCallback;
import com.document.lawyerfiles.Clases.ClsArchivos;
import com.document.lawyerfiles.R;
import com.document.lawyerfiles.fragment.DialogoFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ListaArchivos extends AppCompatActivity  implements DialogoFragment.BootonClickLisntener{

    private static final short REQUEST_CODE = 6545;
    FirebaseStorage storage;
    private DatabaseReference referencearchivos;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private ImageButton imgbutton;
    private final int MIS_PERMISOS = 100;
    android.app.AlertDialog.Builder builder1;
    AlertDialog alert;
    String user_id,keycarpeta;
    Uri uri;
    Uri pdfurl;
    String tipodocumento,tipoarchivo,correousuario;
    TextView txtprueba;
    String ruta_archivo_descargar;

    private final int frames = 9;
    private int currentAnimationFrame = 0;
    private LottieAnimationView animationView;


    android.app.AlertDialog.Builder builder2;
    AlertDialog aler2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_archivos);

        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        correousuario=mAuth.getCurrentUser().getEmail();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        keycarpeta=getIntent().getStringExtra("key");
        Log.e("ke",keycarpeta);

        txtprueba=(TextView)findViewById(R.id.idtextotpueba);

        referencearchivos= FirebaseDatabase.getInstance().getReference("Archivos2").child(keycarpeta);
        recyclerView=findViewById(R.id.recylcercarchivos);
        imgbutton=(ImageButton)findViewById(R.id.id_imgbutton);
        imgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirgaleria();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    private void abrirgaleria() {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent,86);
    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<ClsArchivos> recyclerOptions = new FirebaseRecyclerOptions.Builder<ClsArchivos>()
                .setQuery(referencearchivos, ClsArchivos.class).build();
        FirebaseRecyclerAdapter<ClsArchivos,Items> adapter =new FirebaseRecyclerAdapter<ClsArchivos, Items>(recyclerOptions) {

            @Override
            protected void onBindViewHolder(@NonNull final Items items, final int i, @NonNull ClsArchivos tutores) {
                final String key = getRef(i).getKey();
                referencearchivos.child(key).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()){
                            final String nombre_carpeta=dataSnapshot.child("nombre_archivo").getValue().toString();

                            final String fecha=dataSnapshot.child("fecha_archivo").getValue().toString();
                            final String ruta=dataSnapshot.child("ruta_archivo").getValue().toString();
                            final String tipo_documento=dataSnapshot.child("tipo_documento").getValue().toString();
                            final String tipo_archivo=dataSnapshot.child("tipo_archivo").getValue().toString();
                            final String peso_archivo=dataSnapshot.child("peso_archivo").getValue().toString();
                            items.txtnombrefile.setText(nombre_carpeta);
                            items.txtfecha.setText(fecha);
                            items.txtpeso.setText(peso_archivo);


                            if (tipo_archivo.equals("ppt")){
                                items.imgfoto.setImageResource(R.drawable.logoppt);
                            }
                            if (tipo_archivo.equals("pptx")) {

                                items.imgfoto.setImageResource(R.drawable.logoppt);
                            }
                            if (tipo_archivo.equals("doc")){
                                items.imgfoto.setImageResource(R.drawable.logow1);
                            }
                            if (tipo_archivo.equals("docx")){
                                items.imgfoto.setImageResource(R.drawable.logow1);
                            }
                            if (tipo_archivo.equals("pdf")){
                                items.imgfoto.setImageResource(R.drawable.logopdf);
                            }
                            if (tipo_archivo.equals("img")){
                                items.imgfoto.setImageResource(R.drawable.ic_foto);
                            }

                            items.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //   otromensaje();
                                    DialogoFragment bottomSheetDialog = DialogoFragment.newInstance();
                                    bottomSheetDialog.nombredearchivo=nombre_carpeta;
                                    bottomSheetDialog.ruta_archivo=ruta;
                                    bottomSheetDialog.tipo_documento=tipo_documento;
                                    bottomSheetDialog.tipo_archivo=tipo_archivo;
                                    bottomSheetDialog.pes_archivo=peso_archivo;

//
                                    bottomSheetDialog.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(ListaArchivos.this, "Error :"+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }

            @NonNull
            @Override
            public Items onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_archivos, parent, false);
                return new Items(vista);

            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
    public  static class Items extends RecyclerView.ViewHolder {
        TextView txtnombrefile, txtfecha, txtcantidad,txtpeso;
        ImageView imgfoto;

        public Items(@NonNull View itemView) {
            super(itemView);
            txtnombrefile = (TextView) itemView.findViewById(R.id.id_tv_nombrearchivo);
            txtfecha = (TextView) itemView.findViewById(R.id.id_tvfecha);
            imgfoto = (ImageView) itemView.findViewById(R.id.id_imgtipofoto);
            txtpeso=(TextView)itemView.findViewById(R.id.id_tvpeso);


        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==86 && resultCode==RESULT_OK && data!=null){
            String nombrearchivo;
            uri=data.getData(); //return the uir of selected file

            String mimeType = getContentResolver().getType(uri);
            Cursor returnCursor = getContentResolver().query(uri, null, null, null, null);
            returnCursor.moveToFirst();

            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);

            String peso=Long.toString(returnCursor.getLong(sizeIndex));
            nombrearchivo=returnCursor.getString(nameIndex);

            long fileSizeInKB = returnCursor.getLong(sizeIndex) / 1024;

            Log.e("peso bytes ",peso);
            Log.e("peso Kb ",String.valueOf(fileSizeInKB));
            String size=String.valueOf(fileSizeInKB);

            String tipo= getExtension(nombrearchivo);

            switch (tipo){
                case "mp4":
                    Toast.makeText(this, "Tipo de archivo no admitido", Toast.LENGTH_SHORT).show();
                    uri=null;

                    break;
                case "rar":
                    Toast.makeText(this, "Tipo de archivo no admitido", Toast.LENGTH_SHORT).show();
                    uri=null;

                    break;
                case "mp3":
                    Toast.makeText(this, "Tipo de archivo no admitido", Toast.LENGTH_SHORT).show();
                    uri=null;

                    break;
                case "doc":
                    tipodocumento="file";
                    tipoarchivo="doc";
                case "xls":
                    tipodocumento="file";
                    tipoarchivo="xls";
                    break;
                case "xlsx":
                    tipodocumento="file";
                    tipoarchivo="xls";
                    break;
                case "docx":
                    tipodocumento="file";
                    tipoarchivo="docx";
                    break;
                case "pdf":
                    tipodocumento="file";
                    tipoarchivo="pdf";;
                    break;
                case "ppt":
                    tipodocumento="file";
                    tipoarchivo="ppt";
                    break;
                case "pptx":
                    tipodocumento="file";
                    tipoarchivo="pptx";
                    break;
                case "img":
                    tipoarchivo="img";
                    tipodocumento="img";
                    break;
                case "jpg":
                    tipodocumento="img";
                    tipoarchivo="img";
                    break;
                case "jpeg":
                    tipodocumento="img";
                    tipoarchivo="img";
                    break;
                case "png":
                    tipodocumento="img";
                    tipoarchivo="img";
                    break;


                default:
                    tipodocumento="desconocido";
                    tipoarchivo="desconociddo";
                    break;


            }

            previus(tipodocumento,uri,nombrearchivo,tipoarchivo,size);
            // if (tipoarchivo.equals("img")){
            //    // guardarimagen(tipodocumento);
            // }
            // else if (tipoarchivo.equals("file")){
            //    // guardararchivo(tipodocumento,uri,nombrearchivo);
            // }


        }
        else{
            Toast.makeText(this, "No seleciono un archivo", Toast.LENGTH_SHORT).show();
        }
    }
    private void previus(final String tipodocumento, final Uri uri, final String nombrearchivo, final String tipoarchivo ,String peso){
        builder1 = new AlertDialog.Builder(this);
        Button btcerrrar,btnsaves;
        final ImageView imgprevia;

        builder1.setTitle("Archivo");
        final TextView etnombre;
        final TextView txtpeso;

        View v = LayoutInflater.from(this).inflate(R.layout.dialogo_archivo, null);

        builder1.setView(v);
        btcerrrar=(Button)v.findViewById(R.id.id_btncancel);

        btnsaves=(Button)v.findViewById(R.id.id_btnsave) ;
        imgprevia=(ImageView)v.findViewById(R.id.id_imgprevia);
        etnombre=(TextView)v.findViewById(R.id.id_tv_nombrearchivo1);
        txtpeso=(TextView)v.findViewById(R.id.id_sizefile);
        txtpeso.setText(peso+" Kb");

        switch (tipoarchivo){
            case "doc":
                imgprevia.setImageResource(R.drawable.logow1);
                break;
            case "docx":
                imgprevia.setImageResource(R.drawable.logow1);
                break;
            case "ppt":
                imgprevia.setImageResource(R.drawable.logoppt);
                break;
            case "pptx":
                imgprevia.setImageResource(R.drawable.logoppt);
                break;
            case "pdf":
                imgprevia.setImageResource(R.drawable.logopdf);
                break;
            case "xls":
                imgprevia.setImageResource(R.drawable.ic_excel);
                break;
            case "xlsx":
                imgprevia.setImageResource(R.drawable.ic_excel);
                break;
            case "img":
                imgprevia.setImageURI(uri);
                break;

        }
        etnombre.setText(nombrearchivo);
        btcerrrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alert.dismiss();
            }
        });
        btnsaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tipodocumento.equals("file")){
                    guardararchivo(tipodocumento,uri,nombrearchivo);
                }
                else if (tipodocumento.equals("img")){
                    imgprevia.setDrawingCacheEnabled(true);
                    imgprevia.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) imgprevia.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
                    byte[] path = baos.toByteArray();
                    guardarimagen(tipodocumento,nombrearchivo,path,txtpeso.getText().toString());
                }
                else {
                    Toast.makeText(ListaArchivos.this, "Nohay  nigun archivo we", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alert  = builder1.create();
        // alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        alert.show();
    }
    private void guardararchivo(final String tipodocumento, Uri uri, final String nombrearchivo) {
        if (uri==null){
            Toast.makeText(this, "no hay archivo we", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(nombrearchivo)){
            Toast.makeText(this, "falta nombre archivowe", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(tipodocumento)){
            Toast.makeText(this, "no hay archivo we", Toast.LENGTH_SHORT).show();
        }
        else {
            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setTitle("Subiendo..");
            progressDialog.setProgress(0);
            progressDialog.show();
            progressDialog.setCancelable(false);

            final StorageReference mountainsRef=mStorageRef.child(correousuario).child(nombrearchivo);
            final UploadTask uploadTask = mountainsRef.putFile(uri);
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
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                Date date = new Date();
                                String fecha = dateFormat.format(date);
                                Uri dowloand=task.getResult();
                                //    progressDialog.dismiss(); tipoarchivo="file";
                                //                    tipodocumento="doc";
                                //   String tipo_documento;   // fole   o img
                                //String tipo_archivo; //ffile  o img  ppt jpt  png  xecl
                                String key  = referencearchivos.push().getKey();
                                ClsArchivos obj= new ClsArchivos(key,nombrearchivo,tipodocumento,tipoarchivo,"12",fecha,dowloand.toString(),"");
                                referencearchivos.child(key).setValue(obj);



                            } else {
                                Toast.makeText(ListaArchivos.this, "Error al subir", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ListaArchivos.this, "Error " +e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    //track the progress of = our upload
                    int currentprogress= (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setProgress(currentprogress);
                    if (currentprogress==100){
                        progressDialog.dismiss();
                        alert.dismiss();
                        Toast.makeText(ListaArchivos.this, "Agregado We", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
    private void guardarimagen(final String tipodocumento, final String nombrearhivo, byte[] path, final String peso) {

        if (TextUtils.isEmpty(tipodocumento)){
            Toast.makeText(this, "falta docuemnto we", Toast.LENGTH_SHORT).show();
        }
        else  if (TextUtils.isEmpty(nombrearhivo)){
            Toast.makeText(this, "falta docuemnto ", Toast.LENGTH_SHORT).show();
        }
        else  if (path==null){
            Toast.makeText(this, "falta algo we", Toast.LENGTH_SHORT).show();
        }else{
            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setTitle("Subiendo..");
            progressDialog.setProgress(0);
            progressDialog.show();
            progressDialog.setCancelable(false);
            final StorageReference mountainsRef=mStorageRef.child(correousuario).child(nombrearhivo);
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
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                Date date = new Date();
                                String fecha = dateFormat.format(date);
                                Uri dowloand=task.getResult();

                                String key  = referencearchivos.push().getKey();

                                ClsArchivos obj= new ClsArchivos(key,nombrearhivo,tipodocumento,tipoarchivo,peso,fecha,dowloand.toString(),"");
                                referencearchivos.child(key).setValue(obj);
                            } else {
                                Toast.makeText(ListaArchivos.this, "Error ", Toast.LENGTH_SHORT).show();
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
                        //  Toast.makeText(ListaArchivos.this, "Subido we", Toast.LENGTH_SHORT).show();
                        String  msg="Subido con Exito";

                        alert.dismiss();
                        menssaje3(msg);
                    }

                }
            });

        }

    }
    private void menssaje3(String estado){

        builder2 = new AlertDialog.Builder(ListaArchivos.this);
        Button btcerrrar;
        TextView tvestado;
        View v = LayoutInflater.from(ListaArchivos.this).inflate(R.layout.dialogo_check, null);
        animationView = v.findViewById(R.id.animation_viewcheck);
        resetAnimationView();
        animationView.playAnimation();
        builder2.setView(v);
        btcerrrar=(Button)v.findViewById(R.id.idbtncerrardialogo);
        tvestado=(TextView)v.findViewById(R.id.idestado);
        tvestado.setText(estado);
        btcerrrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aler2.dismiss();
            }
        });

        aler2  = builder2.create();
        aler2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        aler2.show();


    }
    private void resetAnimationView() {
        currentAnimationFrame = 0;
        animationView.addValueCallback(new KeyPath("**"), LottieProperty.COLOR_FILTER,
                new SimpleLottieValueCallback<ColorFilter>() {
                    @Override
                    public ColorFilter getValue(LottieFrameInfo<ColorFilter> frameInfo) {
                        return null;
                    }
                }
        );
    }
    public static String getExtension(String filename) {
        //TODO : ESTO ES PARA SACAR LA EXTENSION DEL TIPO DE ARCHIVO
        int index = filename.lastIndexOf('.');
        if (index == -1) {
            return "";
        } else {
            return filename.substring(index + 1);
        }
    }
    @Override
    public void onButtonclick(String texto) {
        DescargarFoto(texto);
    }

    private void DescargarFoto(String ruta) {

        if (TextUtils.isEmpty(ruta)){

            Toast.makeText(this, "No hay link", Toast.LENGTH_SHORT).show();

        }else{
            ruta_archivo_descargar=ruta;
            if (isDownloadManagerAvailable()) {
                checkSelfPermission();
            } else {
                Toast.makeText(this, "El administrador de descargas no está disponible", Toast.LENGTH_LONG).show();
            }

        }
    }
    private static boolean isDownloadManagerAvailable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return true;
        }
        return false;
    }
    private void checkSelfPermission() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE);

        } else {

            executeDownload(ruta_archivo_descargar);

        }
    }
    private void executeDownload(String urlfoto) {

        // registrer receiver in order to verify when download is complete
        registerReceiver(new DonwloadCompleteReceiver(), new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urlfoto));
        Uri uri = Uri.parse(urlfoto);
        String idruta= getExtension2(uri.getLastPathSegment());
        request.setDescription("Descarga archivo " + idruta);
        request.setTitle("Material");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,idruta);

        // get download service and enqueue file
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

    }
    public class DonwloadCompleteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)){
                Toast.makeText(context,"Descarga Completa", Toast.LENGTH_LONG).show();
                // DO SOMETHING WITH THIS FILE
            }
        }
    }
    public static String getExtension2(String filename) {
        int index = filename.lastIndexOf('/');
        if (index == -1) {
            return "";
        } else {
            return filename.substring(index + 1);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==MIS_PERMISOS){
            if(grantResults.length==2 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){//el dos representa los 2 permisos
                Toast.makeText(getApplicationContext(),"Permisos concedidos",Toast.LENGTH_SHORT);
                // imgFoto.setEnabled(true);
            }
        }else{
            solicitarPermisosManual();
        }

        if (requestCode==REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted! Do the work
                executeDownload(ruta_archivo_descargar);
            } else {
                // permission denied!
                Toast.makeText(this, "Please give permissions ", Toast.LENGTH_LONG).show();
            }
            return;
        }
    }
    private void solicitarPermisosManual() {
        //TODO es para otrad casaoas
        final CharSequence[] opciones={"si","no"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(getBaseContext());//estamos en fragment
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("si")){
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package",getBaseContext().getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(getBaseContext(),"Los permisos no fueron concedidos",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }
    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(getBaseContext());
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
            }
        });
        dialogo.show();
    }
}
