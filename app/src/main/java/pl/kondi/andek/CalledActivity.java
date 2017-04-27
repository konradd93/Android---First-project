package pl.kondi.andek;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ATyKondziu on 08.03.2017.
 */

public class CalledActivity extends AppCompatActivity {

    private int numberOfMarks;
    private float average;
    ListView lvExtensiveList;
    private Context context;
    private List<MarkModel> marksList;
    private static final String TAG = CalledActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.called_activity);
        context = getApplicationContext();

        Bundle bundle = getIntent().getExtras();
        numberOfMarks = bundle.getInt("iloscOcen");
        lvExtensiveList = (ListView) findViewById(R.id.marksList);
        marksList = new ArrayList<>();
        for (int i = 0; i < numberOfMarks; i++) {
            marksList.add(new MarkModel("", 0));
        }
        if (savedInstanceState != null) {
            //przechwycenie tablicy z ocenami zachowanej na wypadek gdyby została wykonana metoda onStop()
            int[] list = savedInstanceState.getIntArray("ocenki");
            int i = 0;
            //przepisywanie ocen z saveInstanceState do naszej listy ocen
            for (MarkModel mark : marksList) {
                mark.setMark(list[i]);
                i++;
            }
        }


        InteractiveArrayAdapter adapter = new InteractiveArrayAdapter(this, marksList);
        lvExtensiveList.setAdapter(adapter);
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
        int[] lista = new int[marksList.size()];

        for (int i = 0; i < marksList.size(); i++) {
            lista[i] = marksList.get(i).getMark();
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
    public void finished(View view) {
        //czy wszystkie oceny zostały uzupełnione
        if (checkWhetherAll()) {
            int i = 0, sum = 0;
            for (MarkModel mark : marksList) {
                sum += mark.getMark();
                i++;
            }
            average = (float) sum / i;
            // Log.e(TAG,String.format("%.2f",average));
            Bundle bundle = new Bundle();
            bundle.putString("average", String.format("%.2f", average).toString()); //można przekazać więcej niż 1 element

            Intent intent = new Intent();
            intent.putExtras(bundle);
            setResult(1, intent);
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
        for (MarkModel mark : marksList) {
            if (mark.getMark() < 2 || mark.getMark() > 5) {
                isAll = false;
                break;
            }
        }
        return isAll;
    }
}
