package pl.kondi.andek;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ATyKondziu on 09.03.2017.
 */

public class InteraktywnyAdapterTablicy extends ArrayAdapter<ModelOceny> {
    //przechowujemy referencję do listy ocen
    private List<ModelOceny> listaOcen;
    private Activity kontekst;

    private TextView ocenaTextView;
    private TextView nrOcenyTextView;


    public InteraktywnyAdapterTablicy(Activity kontekst, List<ModelOceny> listaOcen) {
        super(kontekst, R.layout.ocena, listaOcen);
        //ustawienie wartości pól
        this.kontekst = kontekst;
        this.listaOcen = listaOcen;
    }

    //tworzenie nowego wiersza
    @Override
    public View getView(int numerWiersza, View widokDoRecyklingu, ViewGroup parent) {

        View widok = null;
        //tworzenie nowego wiersza
        if (widokDoRecyklingu == null) {
            //utworzenie layout na podstawie pliku XML
            LayoutInflater pompka = (LayoutInflater) kontekst.getLayoutInflater();
            widok = pompka.inflate(R.layout.ocena, null);
            //wybranie konkretnego przycisku radiowego musi zmieniać dane w modelu
            RadioGroup grupaOceny = (RadioGroup) widok.findViewById(R.id.grupaOceny);
            grupaOceny.setOnCheckedChangeListener(
                    new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, //referencja do grupy
                                                     //przycisków
                                                     int checkedId)    //identyfikator wybranego
                        //przycisku
                        {
                            aktualizujModelOceny(group,
                                    checkedId);
                            //odczytanie z etykiety, który obiekt modelu przechowuje dane o
                            //zmienionej ocenie
                            //zapisanie zmienionej oceny
                        }
                    }
            );
            //powiązanie grupy przycisków z obiektem w modelu
            grupaOceny.setTag(listaOcen.get(numerWiersza));
        }
        //aktualizacja istniejącego wiersza
        else {
            widok = widokDoRecyklingu;
            RadioGroup grupaOceny = (RadioGroup) widok.findViewById(R.id.grupaOceny);
            //powiązanie grupy przycisków z obiektem w modelu
            grupaOceny.setTag(listaOcen.get(numerWiersza));
        }

        TextView etykieta = (TextView) widok.findViewById(R.id.nrOcenyTextView);
        //ustawienie tekstu etykiety na podstawie modelu
        etykieta.setText(
                listaOcen.get(numerWiersza).getNazwa());
        RadioGroup grupaOceny = (RadioGroup) widok.findViewById(R.id.grupaOceny);
        //zaznaczenie odpowiedniego przycisku na podtawie modelu
        //zwrócenie nowego lub zaktualizowanego wiersza listy
        ustawOcene(grupaOceny, numerWiersza);
        numerWiersza++;
        nrOcenyTextView = (TextView) widok.findViewById(R.id.nrOcenyTextView);
        nrOcenyTextView.setText("" + numerWiersza);
        return widok;
    }

    //ustawianie ocen - zaznaczanie odpowiednich radio
    private void ustawOcene(RadioGroup grupaOceny,
                            int numerWiersza) {
        switch (listaOcen.get(numerWiersza).getOcena()) {
            case 2:
                grupaOceny.check(R.id.radio2);
                break;
            case 3:
                grupaOceny.check(R.id.radio3);
                break;
            case 4:
                grupaOceny.check(R.id.radio4);
                break;
            case 5:
                grupaOceny.check(R.id.radio5);
                break;
            default:
                grupaOceny.clearCheck();
        }

    }

    private void aktualizujModelOceny(
            RadioGroup grupaOceny, int idWybranegoButtona) {
        ModelOceny element = (ModelOceny) grupaOceny.getTag();
        switch (idWybranegoButtona) {
            case R.id.radio2:
                element.setOcena(2);
                break;
            case R.id.radio3:
                element.setOcena(3);
                break;
            case R.id.radio4:
                element.setOcena(4);
                break;
            case R.id.radio5:
                element.setOcena(5);
                break;
            default:
                element.setOcena(0);
        }
    }

}
