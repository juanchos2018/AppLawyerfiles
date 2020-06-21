package com.document.lawyerfiles.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.document.lawyerfiles.Clases.ClsCountries;
import com.document.lawyerfiles.R;

import java.util.ArrayList;

public class AdapterPaises2 extends RecyclerView.Adapter<AdapterPaises2.ViewHolderDatos> {

    ArrayList<ClsCountries> listaAlumnos;

    public AdapterPaises2(ArrayList<ClsCountries> listaAlumnos) {
        this.listaAlumnos = listaAlumnos;
    }
    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_paises,parent,false);

        return new  ViewHolderDatos(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        if (holder instanceof ViewHolderDatos){
            final ViewHolderDatos items =(ViewHolderDatos)holder;
            items.txtnombres.setText(listaAlumnos.get(position).getCountry());
            items.txtapellidos.setText(listaAlumnos.get(position).getNewConfirmed());

        }
    }

    @Override
    public int getItemCount() {
        return listaAlumnos.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        TextView txtnombres,txttelefono,txtcorreo,txtapellidos,txtedad;
        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            txtnombres=(TextView)itemView.findViewById(R.id.id_tvpais);
            txtapellidos=(TextView)itemView.findViewById(R.id.id_NewConfirmed);
        }
    }
}
