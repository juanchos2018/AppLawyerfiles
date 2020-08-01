package com.document.lawyerfiles.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.document.lawyerfiles.Clases.Calendario;
import com.document.lawyerfiles.R;

import java.util.ArrayList;

public class AdapterCalendario   extends RecyclerView.Adapter<AdapterCalendario.ViewHolderDatos> {

    ArrayList<Calendario> listaPersonaje;

    public AdapterCalendario(ArrayList<Calendario> listaPersonaje) {
        this.listaPersonaje = listaPersonaje;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendario,parent,false);
        //  vista.setOnClickListener(this);
        return new ViewHolderDatos(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        if (holder instanceof ViewHolderDatos){

            final ViewHolderDatos datgolder =(ViewHolderDatos)holder;
            datgolder.nombre.setText(listaPersonaje.get(position).getAsunto());
            datgolder.tvndescriocion.setText(listaPersonaje.get(position).getPrioridad());

        }
    }

    @Override
    public int getItemCount() {
        return listaPersonaje.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView nombre,tvndescriocion;
        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            nombre=(TextView)itemView.findViewById(R.id.idtvnombreclase2);
            tvndescriocion=(TextView)itemView.findViewById(R.id.idtvdescripcioncalendario);

        }
    }
}
