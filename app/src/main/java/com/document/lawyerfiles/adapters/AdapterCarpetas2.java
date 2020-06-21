package com.document.lawyerfiles.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.document.lawyerfiles.Clases.ClsCarpetas;
import com.document.lawyerfiles.R;

import java.util.ArrayList;

public class AdapterCarpetas2 extends RecyclerView.Adapter<AdapterCarpetas2.ViewHolderDatos>{

    ArrayList<ClsCarpetas> listaCarpetas;
    Context context;
    public AdapterCarpetas2(ArrayList<ClsCarpetas> listaCarpetas) {

        this.listaCarpetas = listaCarpetas;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carpetas2,parent,false);

        return new ViewHolderDatos(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        if (holder instanceof ViewHolderDatos){
            final ViewHolderDatos items =(ViewHolderDatos)holder;
            items.tv_foldern.setText(listaCarpetas.get(position).getNombre_carpeta());


        }
    }

    @Override
    public int getItemCount() {
        return listaCarpetas.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView tv_foldern, tv_foldersize;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            tv_foldern=(TextView)itemView.findViewById(R.id.textView);
        }
    }
}
