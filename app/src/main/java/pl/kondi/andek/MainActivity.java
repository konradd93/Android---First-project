package pl.kondi.andek;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private Button send;
    private Button result;
    private EditText name;
    private EditText surname;
    private EditText mark;
    private Context contex;
    private boolean backFromTheCalled = false;
    private boolean whetherDialogCreated = false;
    private String average = "";
    private TextView tvAverage;
    private double avg;
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


        send = (Button) findViewById(R.id.send);
        send.setVisibility(View.INVISIBLE);
        result = (Button) findViewById(R.id.result);
        tvAverage = (TextView) findViewById(R.id.srednia);
        tvAverage.setVisibility(View.INVISIBLE);

        name = (EditText) findViewById(R.id.editText1);
        surname = (EditText) findViewById(R.id.editText2);
        mark = (EditText) findViewById(R.id.editText3);
        //gdy w saveInstanceState są dane:
        if (savedInstanceState != null)
            backFromTheCalled = savedInstanceState.getBoolean("czyPowrot");
        //sprawdzenie czy wróciliśmy juz z wywoływanej aktywności
        if (backFromTheCalled) {

            average = savedInstanceState.getString("przechowanaSrednia");
            tvAverage.setText("średnia: " + average);
            tvAverage.setVisibility(View.VISIBLE);
            send.setVisibility(View.GONE);
            avg = Double.parseDouble(average);
            if (avg >= 3.00)
                result.setText("SUPER! ;)");
            else
                result.setText("PRZYKRO MI... ;(");
            result.setVisibility(View.VISIBLE);
            whetherDialogCreated = savedInstanceState.getBoolean("dialog");
            if (whetherDialogCreated) dialogCreator();
        }
        /** Listenery **/
        name.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus && name.getText().toString().equals("")) {
                            Toast toast = Toast.makeText(contex, //lub MainActivity.this, //kontekst-zazwyczaj referencja do Activity
                                    "Pole imię nie może być puste", //napis do wyświetlenia
                                    Toast.LENGTH_SHORT); //długość wyświetlania napisu
                            toast.show();
                            //zmiana na małe litery, pierwsza duża
                        } else if (!hasFocus && !isValid(name.getText())) {
                            name.setText(name.getText().toString().toLowerCase());
                            String a = new String(name.getText().toString().charAt(0) + "");
                            a = a.toUpperCase();
                            name.setText(name.getText().replace(0, 1, a));
                        }
                    }
                }
        );
        surname.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {

                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus && surname.getText().toString().equals("")) {
                            Toast toast = Toast.makeText(MainActivity.this, //kontekst-zazwyczaj referencja do Activity
                                    "Pole surname nie może być puste", //napis do wyświetlenia
                                    Toast.LENGTH_SHORT); //długość wyświetlania napisu
                            toast.show();
                        }
                        //zmiana na małe litery, pierwsza duża
                        else if (!hasFocus && !isValid(surname.getText())) {
                            surname.setText(surname.getText().toString().toLowerCase());
                            String a = new String(surname.getText().toString().charAt(0) + "");
                            a = a.toUpperCase();
                            surname.setText(surname.getText().replace(0, 1, a));
                        }
                    }
                }
        );
        mark.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {

                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        int marks;
                        if (mark.getText().toString().equals("")) marks = 0;
                        else {
                            marks = Integer.parseInt(mark.getText().toString());
                        }

                        if (!hasFocus) {
                            String message = "";
                            if (mark.getText().toString().equals("")) {
                                message = "Pole Liczba mark nie może być puste";
                            } else {
                                if (marks < 5) message = "Za mała wartość (Podaj >=5)";
                                if (marks > 15) message = "Za duża wartość (Podaj <=15)";
                            }

                            if (!(marks >= 5 && marks <= 15)) {
                                Toast toast = Toast.makeText(MainActivity.this, //kontekst-zazwyczaj referencja do Activity
                                        message, //napis do wyświetlenia
                                        Toast.LENGTH_SHORT); //długość wyświetlania napisu
                                toast.show();
                            }
                        }
                    }
                }
        );


        name.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        check(name, surname, mark, send);
                    }
                }
        );

        surname.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        check(name, surname, mark, send);
                    }
                }
        );
        mark.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        check(name, surname, mark, send);
                    }
                }
        );
    }

    //metoda pomocnicza sprawdzająca czy button może już się pojawić
    public void check(EditText name, EditText surname, EditText mark, Button send) {
        int marks;
        if (mark.getText().toString().equals("")) marks = 0;
        else {
            marks = Integer.parseInt(mark.getText().toString());
        }

        if (!name.getText().toString().equals("")
                && !surname.getText().toString().equals("")
                && marks >= 5 && marks <= 15) send.setVisibility(View.VISIBLE);
        else send.setVisibility(View.INVISIBLE);
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

    //pobranie ilośći mark i przekazanie wartośći do wywoływanej activity
    public void printMarks(View view) {
        backFromTheCalled = false;
        Intent intent = new Intent(contex, //kontekst – zazwyczaj aktywność
                CalledActivity.class);
        int marks = Integer.parseInt(mark.getText().toString());
        intent.putExtra("iloscOcen", marks);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putBoolean("czyPowrot", backFromTheCalled);
        outState.putString("przechowanaSrednia", average);
        outState.putBoolean("dialog", whetherDialogCreated);
        super.onSaveInstanceState(outState);
        //tutaj należy zwolnić zasoby i ew. zapisać istotne elementy stanu
        //ponieważ aktywność może zostać "zabita" przez system
    }

    protected void onActivityResult(int taskCode, int resultCode, Intent data) {
        super.onActivityResult(taskCode, resultCode, data);
        if (resultCode == 1) {
            backFromTheCalled = true;
            Bundle bundle = data.getExtras();
            average = bundle.getString("average");
            tvAverage.setText("średnia: " + average);
            tvAverage.setVisibility(View.VISIBLE);
            send.setVisibility(View.GONE);
            avg = Double.parseDouble(average);
            if (avg >= 3.00)
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
        if (avg >= 3.00) {
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
