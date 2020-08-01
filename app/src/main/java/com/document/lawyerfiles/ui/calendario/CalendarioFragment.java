package com.document.lawyerfiles.ui.calendario;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.document.lawyerfiles.Clases.Calendario;
import com.document.lawyerfiles.R;
import com.document.lawyerfiles.activitys.CalendarioNuevoActivity;
import com.document.lawyerfiles.adapters.AdapterCalendario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.vo.DateData;

public class CalendarioFragment extends Fragment {


    private DatabaseReference reference1;
    private FirebaseAuth mAuth;
    MCalendarView calendarView;
    private RecyclerView recycler;
    String user_id,tiposusu;

    AdapterCalendario adapter;
    ArrayList<Calendario> listaMisclases;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.calendario_fragment, container, false);

        FloatingActionButton fab = vista.findViewById(R.id.fab4);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AgregarReunion();
            }
        });

        recycler = vista.findViewById(R.id.idrecyclerfechastareas);
        calendarView = (MCalendarView)vista.findViewById(R.id.calendar);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));


        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        reference1 = FirebaseDatabase.getInstance().getReference().child("Calendario").child(user_id);
        listaMisclases = new ArrayList<>();



        return vista;
    }

    private void AgregarReunion() {

        startActivity(new Intent(getContext(), CalendarioNuevoActivity.class));


    }

    @Override
    public void onStart() {
        super.onStart();

        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                for (final DataSnapshot data : dataSnapshot.getChildren()) {

                    Calendario artist = data.getValue(Calendario.class);
                    String[] fechas= artist.getFecha().split("/");

                    calendarView.markDate(Integer.parseInt(fechas[0]), Integer.parseInt(fechas[1]), Integer.parseInt(fechas[2]));
                    calendarView.setOnDateClickListener(new OnDateClickListener() {

                        @Override
                        public void onDateClick(View view, DateData date) {

                            Integer dia=Integer.parseInt(date.getDayString());
                            String mes=date.getMonthString();
                            Integer ano=date.getYear();

                            String fechareu=dia+"/"+mes+"/"+String.valueOf(ano);
                            Query q=reference1.orderByChild("fecha").equalTo(fechareu);
                            q.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    listaMisclases.clear();

                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                                        Calendario artist = postSnapshot.getValue(Calendario.class);
                                        listaMisclases.add(artist);
                                    }

                                    adapter = new AdapterCalendario(listaMisclases);
                                    recycler.setAdapter(adapter);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {


            }
        });



    }



}
