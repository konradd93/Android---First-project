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

public class InteractiveArrayAdapter extends ArrayAdapter<MarkModel> {
    //przechowujemy referencję do listy ocen
    private List<MarkModel> marksList;
    private Activity context;

    private TextView tvMark;
    private TextView tvNumberOfMark;


    public InteractiveArrayAdapter(Activity context, List<MarkModel> marksList) {
        super(context, R.layout.mark, marksList);
        //ustawienie wartości pól
        this.context = context;
        this.marksList = marksList;
    }

    //tworzenie nowego wiersza
    @Override
    public View getView(int rowNumber, View viewForRecycling, ViewGroup parent) {

        View view = null;
        //tworzenie nowego wiersza
        if (viewForRecycling == null) {
            //utworzenie layout na podstawie pliku XML
            LayoutInflater inflater = (LayoutInflater) context.getLayoutInflater();
            view = inflater.inflate(R.layout.mark, null);
            //wybranie konkretnego przycisku radiowego musi zmieniać dane w modelu
            RadioGroup grupaOceny = (RadioGroup) view.findViewById(R.id.marksGroup);
            grupaOceny.setOnCheckedChangeListener(
                    new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, //referencja do grupy
                                                     //przycisków
                                                     int checkedId)    //identyfikator wybranego
                        //przycisku
                        {
                            updateMarkModel(group,
                                    checkedId);
                            //odczytanie z etykiety, który obiekt modelu przechowuje dane o
                            //zmienionej ocenie
                            //zapisanie zmienionej oceny
                        }
                    }
            );
            //powiązanie grupy przycisków z obiektem w modelu
            grupaOceny.setTag(marksList.get(rowNumber));
        }
        //aktualizacja istniejącego wiersza
        else {
            view = viewForRecycling;
            RadioGroup markGroup = (RadioGroup) view.findViewById(R.id.marksGroup);
            //powiązanie grupy przycisków z obiektem w modelu
            markGroup.setTag(marksList.get(rowNumber));
        }

        TextView label = (TextView) view.findViewById(R.id.tvNumberOfMark);
        //ustawienie tekstu etykiety na podstawie modelu
        label.setText(
                marksList.get(rowNumber).getName());
        RadioGroup markGroup = (RadioGroup) view.findViewById(R.id.marksGroup);
        //zaznaczenie odpowiedniego przycisku na podtawie modelu
        //zwrócenie nowego lub zaktualizowanego wiersza listy
        setMark(markGroup, rowNumber);
        rowNumber++;
        tvNumberOfMark = (TextView) view.findViewById(R.id.tvNumberOfMark);
        tvNumberOfMark.setText("" + rowNumber);
        return view;
    }

    //ustawianie ocen - zaznaczanie odpowiednich radio
    private void setMark(RadioGroup markGroup,
                         int rowNumber) {
        switch (marksList.get(rowNumber).getMark()) {
            case 2:
                markGroup.check(R.id.radio2);
                break;
            case 3:
                markGroup.check(R.id.radio3);
                break;
            case 4:
                markGroup.check(R.id.radio4);
                break;
            case 5:
                markGroup.check(R.id.radio5);
                break;
            default:
                markGroup.clearCheck();
        }

    }

    private void updateMarkModel(
            RadioGroup marksGroup, int selectedButtonID) {
        MarkModel element = (MarkModel) marksGroup.getTag();
        switch (selectedButtonID) {
            case R.id.radio2:
                element.setMark(2);
                break;
            case R.id.radio3:
                element.setMark(3);
                break;
            case R.id.radio4:
                element.setMark(4);
                break;
            case R.id.radio5:
                element.setMark(5);
                break;
            default:
                element.setMark(0);
        }
    }

}
