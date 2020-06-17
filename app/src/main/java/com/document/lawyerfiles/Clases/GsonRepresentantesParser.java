package com.document.lawyerfiles.Clases;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GsonRepresentantesParser {


    public List<ClsRepresentantes> leerFlujoJson(InputStream in) throws IOException {
        // Nueva instancia de la clase Gson

        Gson gson = new Gson();

        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        List<ClsRepresentantes> animales = new ArrayList<>();

        reader.beginArray();

        while (reader.hasNext()) {
            // Lectura de objetos
            ClsRepresentantes animal = gson.fromJson(reader, ClsRepresentantes.class);
            animales.add(animal);
        }
        reader.endArray();
        reader.close();


        return animales;
    }


}
