package com.gazim.calculator.stable;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b0, c, P, M, E, m, d, point;
    EditText eT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = findViewById(R.id.btnOne);
        b2 = findViewById(R.id.btnTwo);
        b3 = findViewById(R.id.btnThree);
        b4 = findViewById(R.id.btnFour);
        b5 = findViewById(R.id.btnFive);
        b6 = findViewById(R.id.btnSix);
        b7 = findViewById(R.id.btnSeven);
        b8 = findViewById(R.id.btnEight);
        b9 = findViewById(R.id.btnNine);
        b0 = findViewById(R.id.btnZero);
        c = findViewById(R.id.btnClear);
        P = findViewById(R.id.btnAdd);
        M = findViewById(R.id.btnSubtract);
        E = findViewById(R.id.btnEquals);
        m = findViewById(R.id.btnMultiply);
        d = findViewById(R.id.btnDivision);
        point = findViewById(R.id.btnPoint);
        eT = findViewById(R.id.mathValuesInput);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            eT.setShowSoftInputOnFocus(false);
        }
        eT.requestFocus();

        b1.setOnClickListener(this::oC);
        b2.setOnClickListener(this::oC);
        b3.setOnClickListener(this::oC);
        b4.setOnClickListener(this::oC);
        b5.setOnClickListener(this::oC);
        b6.setOnClickListener(this::oC);
        b7.setOnClickListener(this::oC);
        b8.setOnClickListener(this::oC);
        b9.setOnClickListener(this::oC);
        b0.setOnClickListener(this::oC);
        P.setOnClickListener(this::oC);
        M.setOnClickListener(this::oC);
        E.setOnClickListener(this::oC);
        m.setOnClickListener(this::oC);
        d.setOnClickListener(this::oC);
        point.setOnClickListener(this::oC);

        c.setOnClickListener(v -> {
            int iS = eT.getSelectionStart(), iE = eT.getSelectionEnd();
            if (iS>0 && iS==iE) {
                eT.setText(eT.getText().delete(iS-1, iS));
                eT.setSelection(iS-1);
            } else {
                eT.setText(eT.getText().delete(iS, iE));
                eT.setSelection(iS);
            }
            if (findEqually(eT)>-1 && iS>findEqually(eT)+1) eT.setSelection(findEqually(eT)+1);
        });
        c.setOnLongClickListener(v -> {
            eT.setText("");
            return false;
        });

        eT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }


            @Override
            public void afterTextChanged(Editable s) {
                if (getCurrentFocus() == eT) {
                    eT.clearFocus();
                    eT.setText(eT.getText().toString().replaceAll("\\*", "×").replaceAll("/", "÷").replaceAll(",", "."));
                    while (findEqually(eT)==0) eT.getText().delete(0, 1);
                    int i;
                    if (findEqually(eT)==-1) i = eT.getText().length()-1;
                    else i = findEqually(eT)-1;
                    while (i>=0) {
                        boolean noDigit = !Character.isDigit(eT.getText().charAt(i)), noDigitPlusOne = false;
                        if (!(i+1==eT.getText().length())) noDigitPlusOne = !Character.isDigit(eT.getText().charAt(i+1));
                        int divi = eT.getText().toString().indexOf("÷", i), mult = eT.getText().toString().indexOf("×", i), point = eT.getText().toString().indexOf(".", i), minus = eT.getText().toString().indexOf("-", i), plus = eT.getText().toString().indexOf("+", i);
                        if (noDigit && (!(divi == i || mult == i || minus == i || plus == i || point == i) || noDigitPlusOne)) {
                            eT.getText().delete(i, i + 1);
                        }
                        if (!Character.isDigit(eT.getText().charAt(0)) && !((eT.getText().toString().indexOf("-"))==0)) eT.getText().delete(0, 1);
                        i--;
                    }
                    if (findEqually(eT)>-1) {
                        eT.getText().delete(findEqually(eT)+1, eT.getText().length());
                        eT.append(evel(eT));
                    }
                    eT.requestFocus();
                }
            }
        });
    }

    private void oC(View view) {
        int iS = eT.getSelectionStart(), iE = eT.getSelectionEnd();
        eT.setText(eT.getText().delete(iS, iE));
        eT.setText(eT.getText().insert(iS, ((Button) view).getText()));
        if ((iS<=findEqually(eT) || findEqually(eT)==-1) && iS<eT.getText().length()) eT.setSelection(iS+1);
        else if (findEqually(eT)==-1) eT.setSelection(eT.getText().length());
        else eT.setSelection(findEqually(eT)+1);
    }

    public static int findEqually(EditText EditText) {
        return EditText.getText().toString().indexOf("=");
    }


    public strictfp String evel(EditText equally) {
        int plus, mult, divi, start, end, i = -1, stop = 0;
        String s, example = "";
        double double0, double1, result;

        if (equally.getText().toString().contains("=")) {
            example = equally.getText().toString().substring(0, equally.getText().toString().indexOf("="));
            i = example.indexOf("-", 1);
        }
        while (i > -1) {
            example = example.substring(0, i) + "+" + example.substring(i);
            i = example.indexOf("-", i + 2);
        }

        while (stop == 0 && example.length() > 0) {
            start = 0;
            end = 0;
            divi = 0;
            mult = 0;
            plus = 0;
            result = 0;
            s = "s";

            if (example.contains("÷")) {
                s = "÷";
                divi = 1;
            } else if (example.contains("×")) {
                s = "×";
                mult = 1;
            } else if (example.contains("+")) {
                s = "+";
                plus = 1;
            } else stop = 1;
            i = example.indexOf(s);
            if (i > 0) {
                start = i - 1;
                end = i + 1;
            }
            while (start > -1 && (example.indexOf("-", start) == start || example.indexOf(".", start) == start || Character.isDigit(example.charAt(start)))) {
                start = start - 1;
            }
            while (end < example.length() && (example.indexOf("-", end) == end || example.indexOf(".", end) == end || Character.isDigit(example.charAt(end)))) {
                end = end + 1;
            }

            if (i > 0) {
                double0 = Double.parseDouble(example.substring(start + 1, i));
                double1 = Double.parseDouble(example.substring(i + 1, end));
                if (divi == 1) {
                    if (double1 == 0) {
                        example = "error";
                        break;
                    } else result = double0 / double1;
                }
                if (mult == 1) result = double0 * double1;
                if (plus == 1) result = double0 + double1;
                example = example.substring(0, start + 1) + result + example.substring(end);
            }
        }
        int point = example.indexOf(".", example.length() - 2), zero = example.indexOf("0", example.length() - 1);
        if (zero > -1 & point > -1) example = example.substring(0, point);
        return example;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}