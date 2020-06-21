package com.document.lawyerfiles.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.document.lawyerfiles.R;
import com.document.lawyerfiles.activitys.Enviar_archivosActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import net.glxn.qrgen.android.QRCode;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DialogoFragment2 extends BottomSheetDialogFragment {

    public String nombredearchivo;
    public String ruta_archivo;
    public String tipo_documento;
    public String tipo_archivo;
    private static final int CODIGO_PERMISO_ESCRIBIR_ALMACENAMIENTO = 1;
    private static final int ALTURA_CODIGO = 500, ANCHURA_CODIGO = 500;

    android.app.AlertDialog.Builder builder1;
    AlertDialog alert;


    private BootonClickLisntener mListener;

    public DialogoFragment2() {
        // Required empty public constructor
    }
    public static DialogoFragment2 newInstance() {
        DialogoFragment2 fragment = new DialogoFragment2();
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void setupDialog(Dialog dialog, int style) {
        final View contentView = View.inflate(getContext(), R.layout.fragment_dialogo2, null);
        TextView txtdescargar,txtcompartir,txtmover,txtcompiarviculo,txtqr,txteliminar;
      //  Button btn=(Button)contentView.findViewById(R.id.btonmensaje);
        //Button btn2 =(Button)contentView.findViewById(R.id.btncompartir);
        //Button btn3 =(Button)contentView.findViewById(R.id.btdesscarga);

        // Toast.makeText(getContext(), ruta_archivo, Toast.LENGTH_SHORT).show();

        txtdescargar=(TextView)contentView.findViewById(R.id.id_tvdescargar);
        txtcompartir=(TextView)contentView.findViewById(R.id.id_tvcompartir);
        txtqr=(TextView)contentView.findViewById(R.id.id_tvqr);
        txtcompiarviculo=(TextView)contentView.findViewById(R.id.id_tvcopiar);
        txtmover=(TextView)contentView.findViewById(R.id.id_tvmover);
        txteliminar=(TextView)contentView.findViewById(R.id.tv_eliminar);

        TextView nombrearchivo;
        ImageView imgg;
        imgg=(ImageView)contentView.findViewById(R.id.imgprevia);

        switch (tipo_archivo){
            case "doc":
                imgg.setImageResource(R.drawable.logow1);
                break;
            case "docx":
                imgg.setImageResource(R.drawable.logow1);
                break;
            case "pdf":
                imgg.setImageResource(R.drawable.logopdf);
                break;
            case "ppt":
                imgg.setImageResource(R.drawable.logoppt);
                break;
            case "pptx":
                imgg.setImageResource(R.drawable.logoppt);
                break;
        }

        nombrearchivo=(TextView)contentView.findViewById(R.id.idnombrearchivo);
        nombrearchivo.setText(nombredearchivo);

        txtqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mensaje(nombredearchivo);
            }
        });

        txtcompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   mListener.onButtonclick("desde el framentedshet");

                Intent intent=new Intent(getContext(), Enviar_archivosActivity.class);

                Bundle bundle= new Bundle();
                bundle.putString("name",nombredearchivo);
                bundle.putString("ruta",ruta_archivo);
                bundle.putString("tipo_doc",tipo_documento);
                bundle.putString("tipo_arc",tipo_archivo);
                intent.putExtras(bundle);
                startActivity(intent);
                dismiss();
            }
        });

        txtdescargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Descargar(ruta_archivo);

            }
        });
        dialog.setContentView(contentView);
        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        // loadAddNotesFragments();
    }
    private void mensaje(String nombrearchivo){
        builder1 = new AlertDialog.Builder(getContext());
        Button btcerrrar;
        TextView tvnombre;
        final EditText etnombre;
        ImageView imgqr;
        View v = LayoutInflater.from(getContext()).inflate(R.layout.dialogo_qr, null);

        builder1.setView(v);
        tvnombre=(TextView)v.findViewById(R.id.id_tvnombrearchivo2);
        imgqr=(ImageView)v.findViewById(R.id.id_imgqr);
        //  ByteArrayOutputStream byteArrayOutputStream = QRCode.from(ruta_archivo).withSize(ANCHURA_CODIGO, ALTURA_CODIGO).stream();
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(Environment.getExternalStorageDirectory() + "/codigo.png");

            Bitmap bitmap = QRCode.from(ruta_archivo).bitmap();
// Suponiendo que tienes un ImageView con el id ivCodigoGenerado
            imgqr.setImageBitmap(bitmap);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        tvnombre.setText(nombrearchivo);
        alert  = builder1.create();
        // alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        alert.show();
    }

    public  interface  BootonClickLisntener{
        void  onButtonclick(String texto);
    }


    private void Descargar(String ruta) {

        if (TextUtils.isEmpty(ruta)){
            //    Toast.makeText(getContext(), "no hay atchivo", Toast.LENGTH_SHORT).show();

        }else{

            Uri uri = Uri.parse(ruta);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            if (context instanceof BootonClickLisntener){
                mListener = (BootonClickLisntener) context;
            }else{

                throw  new ClassCastException(context.toString()+"musimpemet");
            }
            // mListener=(BootonClickLisntener)context;
        }catch (ClassCastException e){

        }

    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
