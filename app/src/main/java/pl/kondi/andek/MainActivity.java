package pl.kondi.andek;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private Button wyslij;
    private Button result;
    private EditText imie;
    private EditText nazwisko;
    private EditText ocen;
    private Context contex;
    private boolean powrotWywolana = false;
    private boolean whetherDialogCreated = false;
    private String srednia = "";
    private TextView tvSrednia;
    private double sr;
    private final Pattern sPattern
            = Pattern.compile("^[A-Z]+[a-z]{1,15}$");

    private boolean isValid(CharSequence s) {
        return sPattern.matcher(s).matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forOnCreate(savedInstanceState);
    }

    //wyodrębniona metoda z onCreate, dla akcji przycisku od nowa w końcowym oknie dialogowym aplikacji
    public void forOnCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        contex = getApplicationContext();


        wyslij = (Button) findViewById(R.id.send);
        wyslij.setVisibility(View.INVISIBLE);
        result = (Button) findViewById(R.id.result);
        tvSrednia = (TextView) findViewById(R.id.srednia);
        tvSrednia.setVisibility(View.INVISIBLE);

        imie = (EditText) findViewById(R.id.editText1);
        nazwisko = (EditText) findViewById(R.id.editText2);
        ocen = (EditText) findViewById(R.id.editText3);
        //gdy w saveInstanceState są dane:
        if (savedInstanceState != null)
            powrotWywolana = savedInstanceState.getBoolean("czyPowrot");
        //sprawdzenie czy wróciliśmy juz z wywoływanej aktywności
        if (powrotWywolana) {

            srednia = savedInstanceState.getString("przechowanaSrednia");
            tvSrednia.setText("średnia: " + srednia);
            tvSrednia.setVisibility(View.VISIBLE);
            wyslij.setVisibility(View.GONE);
            sr = Double.parseDouble(srednia);
            if (sr >= 3.00)
                result.setText("SUPER! ;)");
            else
                result.setText("PRZYKRO MI... ;(");
            result.setVisibility(View.VISIBLE);
            whetherDialogCreated = savedInstanceState.getBoolean("dialog");
            if (whetherDialogCreated) dialogCreator();
        }
        /** Listenery **/
        imie.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus && imie.getText().toString().equals("")) {
                            Toast toast = Toast.makeText(contex, //lub MainActivity.this, //kontekst-zazwyczaj referencja do Activity
                                    "Pole imię nie może być puste", //napis do wyświetlenia
                                    Toast.LENGTH_SHORT); //długość wyświetlania napisu
                            toast.show();
                            //zmiana na małe litery, pierwsza duża
                        } else if (!hasFocus && !isValid(imie.getText())) {
                            imie.setText(imie.getText().toString().toLowerCase());
                            String a = new String(imie.getText().toString().charAt(0) + "");
                            a = a.toUpperCase();
                            imie.setText(imie.getText().replace(0, 1, a));
                        }
                    }
                }
        );
        nazwisko.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {

                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus && nazwisko.getText().toString().equals("")) {
                            Toast toast = Toast.makeText(MainActivity.this, //kontekst-zazwyczaj referencja do Activity
                                    "Pole nazwisko nie może być puste", //napis do wyświetlenia
                                    Toast.LENGTH_SHORT); //długość wyświetlania napisu
                            toast.show();
                        }
                        //zmiana na małe litery, pierwsza duża
                        else if (!hasFocus && !isValid(nazwisko.getText())) {
                            nazwisko.setText(nazwisko.getText().toString().toLowerCase());
                            String a = new String(nazwisko.getText().toString().charAt(0) + "");
                            a = a.toUpperCase();
                            nazwisko.setText(nazwisko.getText().replace(0, 1, a));
                        }
                    }
                }
        );
        ocen.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {

                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        int oceny;
                        if (ocen.getText().toString().equals("")) oceny = 0;
                        else {
                            oceny = Integer.parseInt(ocen.getText().toString());
                        }

                        if (!hasFocus) {
                            String komunikat = "";
                            if (ocen.getText().toString().equals("")) {
                                komunikat = "Pole Liczba ocen nie może być puste";
                            } else {
                                if (oceny < 5) komunikat = "Za mała wartość (Podaj >=5)";
                                if (oceny > 15) komunikat = "Za duża wartość (Podaj <=15)";
                            }

                            if (!(oceny >= 5 && oceny <= 15)) {
                                Toast toast = Toast.makeText(MainActivity.this, //kontekst-zazwyczaj referencja do Activity
                                        komunikat, //napis do wyświetlenia
                                        Toast.LENGTH_SHORT); //długość wyświetlania napisu
                                toast.show();
                            }
                        }
                    }
                }
        );


        imie.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        check(imie, nazwisko, ocen, wyslij);
                    }
                }
        );

        nazwisko.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        check(imie, nazwisko, ocen, wyslij);
                    }
                }
        );
        ocen.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        check(imie, nazwisko, ocen, wyslij);
                    }
                }
        );
    }

    //metoda pomocnicza sprawdzająca czy button może już się pojawić
    public void check(EditText imie, EditText nazwisko, EditText ocen, Button wyslij) {
        int oceny;
        if (ocen.getText().toString().equals("")) oceny = 0;
        else {
            oceny = Integer.parseInt(ocen.getText().toString());
        }

        if (!imie.getText().toString().equals("")
                && !nazwisko.getText().toString().equals("")
                && oceny >= 5 && oceny <= 15) wyslij.setVisibility(View.VISIBLE);
        else wyslij.setVisibility(View.INVISIBLE);
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

    //pobranie ilośći ocen i przekazanie wartośći do wywoływanej activity
    public void wpiszOceny(View view) {
        powrotWywolana = false;
        Intent zamiar = new Intent(contex, //kontekst – zazwyczaj aktywność
                WywolywanaAktywnosc.class);
        int oceny = Integer.parseInt(ocen.getText().toString());
        zamiar.putExtra("iloscOcen", oceny);
        startActivityForResult(zamiar, 1);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putBoolean("czyPowrot", powrotWywolana);
        outState.putString("przechowanaSrednia", srednia);
        outState.putBoolean("dialog", whetherDialogCreated);
        super.onSaveInstanceState(outState);
        //tutaj należy zwolnić zasoby i ew. zapisać istotne elementy stanu
        //ponieważ aktywność może zostać "zabita" przez system
    }

    protected void onActivityResult(int kodZadania, int kodWyniku, Intent dane) {
        super.onActivityResult(kodZadania, kodWyniku, dane);
        if (kodWyniku == 1) {
            powrotWywolana = true;
            Bundle tobolek = dane.getExtras();
            srednia = tobolek.getString("srednia");
            tvSrednia.setText("średnia: " + srednia);
            tvSrednia.setVisibility(View.VISIBLE);
            wyslij.setVisibility(View.GONE);
            sr = Double.parseDouble(srednia);
            if (sr >= 3.00)
                result.setText("SUPER! ;)");
            else
                result.setText("PRZYKRO MI... ;(");
            result.setVisibility(View.VISIBLE);

        }
    }

    public void getResult(View view) {
        whetherDialogCreated = true;
        dialogCreator();
    }

    //Końcowy AlertDialog
    public void dialogCreator() {
        String title = "", message = "";
        if (sr >= 3.00) {
            title = "Gratulacje!";
            message = "Brawo masz zaliczenie.";
        } else {
            title = "Przykro mi";
            message = "Spotkamy się za rok.";
        }
        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Wyjdź",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Wróć",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        whetherDialogCreated = false;
                        alertDialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Od nowa!",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        forOnCreate(new Bundle());
                    }
                });

        alertDialog.setOnKeyListener(new AlertDialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    whetherDialogCreated = false;
                    alertDialog.dismiss();
                }
                return true;
            }
        });
        alertDialog.show();
    }
}
