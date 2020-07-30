package com.document.lawyerfiles.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.document.lawyerfiles.Clases.ClsCarpetas;
import com.document.lawyerfiles.R;

import java.util.ArrayList;

public class AdapterCarpetas   extends ArrayAdapter<ClsCarpetas> {

    Context context;
    //   RecyclerView.ViewHolder viewHolder;
    ViewHolder viewHolder;
    ArrayList<ClsCarpetas> al_menu = new ArrayList<>();
    public AdapterCarpetas(Context context, ArrayList<ClsCarpetas> al_menu) {
        super(context, R.layout.item_carpetas2, al_menu);
        this.al_menu = al_menu;
        this.context = context;
    }

    @Override
    public int getCount() {
        return al_menu.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        if (al_menu.size() > 0) {
            return al_menu.size();
        } else {
            return 1;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView== null) {

            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_carpetas2, parent, false);
            viewHolder.tv_foldern = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_foldern.setText(al_menu.get(position).getNombre_carpeta());
        return convertView;

    }
    public  void filtrar(ArrayList<ClsCarpetas> filtro){
        this.al_menu=filtro;
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        TextView tv_foldern, tv_foldersize;

    }
}
