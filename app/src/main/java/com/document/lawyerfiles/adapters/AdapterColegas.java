package com.document.lawyerfiles.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.document.lawyerfiles.Clases.ClsColegas;

import java.util.ArrayList;

public class AdapterColegas  extends RecyclerView.Adapter<AdapterColegas.ViewHolderDatos> {

    ArrayList<ClsColegas> listaColegas;

    public AdapterColegas(ArrayList<ClsColegas> listaColegas) {
        this.listaColegas = listaColegas;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        if (holder instanceof AdapterColegas.ViewHolderDatos){
            final AdapterColegas.ViewHolderDatos datgolder =(AdapterColegas.ViewHolderDatos)holder;
            datgolder.id_colega=listaColegas.get(position).getId_usuario();
            datgolder.correo_colega=listaColegas.get(position).getCorreo_usuario();
        }
    }

    @Override
    public int getItemCount() {
        return listaColegas.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        String id_colega,correo_colega;
        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
        }
    }
}
