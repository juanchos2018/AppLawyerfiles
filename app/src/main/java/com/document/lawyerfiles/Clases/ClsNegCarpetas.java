package com.document.lawyerfiles.Clases;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executor;

import javax.xml.transform.Result;

public class ClsNegCarpetas {

    private DatabaseReference referencecarpetas;
     boolean subido=false;
    public void Registrar(ClsCarpetas o){

     //   final boolean subido=false;
        String key = referencecarpetas.push().getKey();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);
        ClsCarpetas o1 =new ClsCarpetas(key,o.nombre_carpeta,o.fecha_creacion,"0");
        referencecarpetas.child(key).setValue(o1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                   // Toast.makeText(getContext(), "Agregado", Toast.LENGTH_SHORT).show();
                  //  progressDialog.dismiss();
                    subido=true;
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                subido=false;
               // Toast.makeText(getContext(), "Error :" +e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public  Task<Result> guardar(){

        Task<Result>tarea =new Task<Result>() {

            @Override
            public boolean isComplete() {
                return false;
            }

            @Override
            public boolean isSuccessful() {
                return false;
            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Nullable
            @Override
            public Result getResult() {
                return null;
            }

            @Nullable
            @Override
            public <X extends Throwable> Result getResult(@NonNull Class<X> aClass) throws X {
                return null;
            }

            @Nullable
            @Override
            public Exception getException() {
                return null;
            }

            @NonNull
            @Override
            public Task<Result> addOnSuccessListener(@NonNull OnSuccessListener<? super Result> onSuccessListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<Result> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super Result> onSuccessListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<Result> addOnSuccessListener(@NonNull Activity activity, @NonNull OnSuccessListener<? super Result> onSuccessListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<Result> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<Result> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<Result> addOnFailureListener(@NonNull Activity activity, @NonNull OnFailureListener onFailureListener) {
                return null;
            }
        };


        return  tarea;

    }

}
