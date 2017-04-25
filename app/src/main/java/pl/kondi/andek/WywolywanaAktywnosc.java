package pl.kondi.andek;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by ATyKondziu on 08.03.2017.
 */

public class WywolywanaAktywnosc extends AppCompatActivity {

    private int il_ocen;
    private float srednia;
    ListView rozbudowana_lista;
    private Context context;
    private List<ModelOceny> listaOcen;
    private static final String TAG = WywolywanaAktywnosc.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wywolywana);
        context = getApplicationContext();

        Bundle tobolek = getIntent().getExtras();
        il_ocen = tobolek.getInt("iloscOcen");
        rozbudowana_lista = (ListView) findViewById(R.id.listaOcen);
        listaOcen = new ArrayList<>();
        for (int i = 0; i < il_ocen; i++) {
            listaOcen.add(new ModelOceny("", 0));
        }
        if (savedInstanceState != null) {
            //przechwycenie tablicy z ocenami zachowanej na wypadek gdyby została wykonana metoda onStop()
            int[] lista = savedInstanceState.getIntArray("ocenki");
            int i = 0;
            //przepisywanie ocen z saveInstanceState do naszej listy ocen
            for (ModelOceny ocena : listaOcen) {
                ocena.setOcena(lista[i]);
                i++;
            }
        }


        InteraktywnyAdapterTablicy adapter = new InteraktywnyAdapterTablicy(this, listaOcen);
        rozbudowana_lista.setAdapter(adapter);
    }

    //Aktywność za chwilę stanie się widoczna
    @Override
    protected void onStart() {
        super.onStart();
        //Tworzenie elementów niezbędnych do uaktualniania interfejsu użytkownika
    }        //Aktywność jest na pierwszym planie

    @Override
    protected void onResume() {
        super.onResume();
    }

    //Aktywność traci "focus". Zostanie zapauzowana.
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        int[] lista = new int[listaOcen.size()];

        for (int i = 0; i < listaOcen.size(); i++) {
            lista[i] = listaOcen.get(i).getOcena();
        }

        outState.putIntArray("ocenki", lista);

        super.onSaveInstanceState(outState);
        //tutaj należy zwolnić zasoby i ew. zapisać istotne elementy stanu
        //ponieważ aktywność może zostać "zabita" przez system
    }


    //Aktywność nie jest widoczna. Została wstrzymana.
    @Override
    protected void onStop() {
        super.onStop();
        //tutaj należy zwolnić zasoby i ew. zapisać istotne elementy stanu
        //ponieważ aktywność może zostać "zabita" przez system

    }

    //Za chwilę aktywność zostanie zniszczona
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //tutaj należy zwolnić zasoby i ew. zapisać istotne elementy stanu
        //ponieważ aktywność może zostać "zabita" przez system
    }

    //przycisk zatwierdzenia ocen
    public void Gotowe(View view) {
        //czy wszystkie oceny zostały uzupełnione
        if (checkWhetherAll()) {
            int i = 0, suma = 0;
            for (ModelOceny ocena : listaOcen) {
                suma += ocena.getOcena();
                i++;
            }
            srednia = (float) suma / i;
            // Log.e(TAG,String.format("%.2f",srednia));
            Bundle tobolek = new Bundle();
            tobolek.putString("srednia", String.format("%.2f", srednia).toString()); //można przekazać więcej niż 1 element

            Intent zamiar = new Intent();
            zamiar.putExtras(tobolek);
            setResult(1, zamiar);
            finish();
        } else {
            Toast toast = Toast.makeText(context, //lub MainActivity.this, //kontekst-zazwyczaj referencja do Activity
                    "Uzupełnij oceny", //napis do wyświetlenia
                    Toast.LENGTH_SHORT); //długość wyświetlania napisu
            toast.show();
        }
    }

    //metoda sprawdzająca czy wszystkie oceny zostały wypełnione
    public boolean checkWhetherAll() {
        boolean isAll = true;
        for (ModelOceny ocena : listaOcen) {
            if (ocena.getOcena() < 2 || ocena.getOcena() > 5) {
                isAll = false;
                break;
            }
        }
        return isAll;
    }
}
