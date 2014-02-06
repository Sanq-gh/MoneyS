package com.sanq.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.sanq.moneys.R;
import com.sanq.utils.Utils;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: sanq
 * Date: 29.06.13
 * Time: 11:42
 * To change this template use File | Settings | File Templates.
 */
public class CalcDialog extends DialogFragment {
    Context context;
    CalcListener totalClickListener;
    LayoutInflater inflater ;
    //String formatNumber = "%" + Utils.getSystemDelimiter() + "2f";
    //String formatNumber = "%.2f";


   public static interface CalcListener {
        void onTotalClick(float result);
    }

    float totalValue;

    public CalcDialog(Context context, CalcListener totalClickListener) {
        this.context = context;
        this.totalClickListener = totalClickListener;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    EditText txtDisplay;
    Button cmdTotal;
    Button cmdClear;
    Button cmdSeven;
    Button cmdEight;
    Button cmdNine;
    Button cmdDivision;
    Button cmdFour;
    Button cmdFive;
    Button cmdSix;
    Button cmdMultiply;
    Button cmdOne;
    Button cmdTwo;
    Button cmdThree;
    Button cmdSubtract;
    Button cmdDecimal;
    Button cmdZzero;
    Button cmdEquals;
    Button cmdAddition;

    ArrayList<Float> mathVariables = new ArrayList<Float>();
    float mathVariable1;
    float mathVariable2;

    int currentOperation = 0;
    int nextOperation;

    private final static int ADD = 1;
    private final static int SUBTRACT = 2;
    private final static int MULTIPLY = 3;
    private final static int DIVISION = 4;
    private final static int EQUALS = 5;

    private final static int CLEAR = 1;
    private final static int DONT_CLEAR = 0;
    private int clearCalcDisplay = 0;



    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        totalValue  =  args.getFloat("TOTAL_VALUE");
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = inflater.inflate(R.layout.calc, null);

        //Window windowProperties = getWindow();
        //windowProperties.setBackgroundDrawable(null);


        txtDisplay = (EditText) v.findViewById(R.id.calc_dialog_display);
        if (totalValue != 0) {
            txtDisplay.setText(Utils.floatToString(totalValue));
        }

        cmdTotal = (Button) v.findViewById(R.id.calc_enter_total);
        cmdClear = (Button) v.findViewById(R.id.calc_all_clear);
        cmdSeven = (Button) v.findViewById(R.id.calc_seven);
        cmdEight = (Button) v.findViewById(R.id.calc_eight);
        cmdNine = (Button) v.findViewById(R.id.calc_nine);
        cmdDivision =(Button) v.findViewById(R.id.calc_division);
        cmdFour = (Button) v.findViewById(R.id.calc_four);
        cmdFive = (Button) v.findViewById(R.id.calc_five);
        cmdSix =(Button) v.findViewById(R.id.calc_six);
        cmdMultiply = (Button) v.findViewById(R.id.calc_multiply);
        cmdOne = (Button) v.findViewById(R.id.calc_one);
        cmdTwo = (Button) v.findViewById(R.id.calc_two);
        cmdThree = (Button) v.findViewById(R.id.calc_three);
        cmdSubtract = (Button) v.findViewById(R.id.calc_subtract);
        cmdDecimal = (Button) v.findViewById(R.id.calc_decimal);
        cmdZzero = (Button) v.findViewById(R.id.calc_zero);
        cmdEquals = (Button) v.findViewById(R.id.calc_equals);
        cmdAddition = (Button) v.findViewById(R.id.calc_addition);
        txtDisplay.setKeyListener(DigitsKeyListener.getInstance(true, true));
        registerListeners();


        builder.setView(v);

        return builder.create();
    }

    public void registerListeners () {
       // cmdTotal.setOnClickListener(totalClickListener);
        cmdTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strRes = txtDisplay.getText().toString();//.replaceAll("," ,Utils.getSystemDelimiter()).replaceAll(".",Utils.getSystemDelimiter());
                float dRes  =  strRes.isEmpty() ? 0 : Utils.stringToFloat(context, strRes);
                totalClickListener.onTotalClick(dRes);
                CalcDialog.this.dismiss();
            }
        });

        cmdClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                txtDisplay.setText("");
                mathVariable1 = 0;
                mathVariable2 = 0;
                mathVariables.removeAll(mathVariables);
                currentOperation = 0;
                nextOperation = 0;
            }
        });

        cmdSeven.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (clearCalcDisplay == CLEAR) {
                    txtDisplay.setText("");
                }
                clearCalcDisplay = DONT_CLEAR;
                txtDisplay.append("7");

            }
        });

        cmdEight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (clearCalcDisplay == CLEAR) {
                    txtDisplay.setText("");
                }
                clearCalcDisplay = DONT_CLEAR;
                txtDisplay.append("8");

            }
        });

        cmdNine.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (clearCalcDisplay == CLEAR) {
                    txtDisplay.setText("");
                }
                clearCalcDisplay = DONT_CLEAR;
                txtDisplay.append("9");

            }
        });

        cmdDivision.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                calcLogic(DIVISION);
            }
        });

        cmdFour.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (clearCalcDisplay == CLEAR) {
                    txtDisplay.setText("");
                }
                clearCalcDisplay = DONT_CLEAR;
                txtDisplay.append("4");

            }
        });

        cmdFive.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (clearCalcDisplay == CLEAR) {
                    txtDisplay.setText("");
                }
                clearCalcDisplay = DONT_CLEAR;
                txtDisplay.append("5");

            }
        });

        cmdSix.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (clearCalcDisplay == CLEAR) {
                    txtDisplay.setText("");
                }
                clearCalcDisplay = DONT_CLEAR;
                txtDisplay.append("6");

            }
        });

        cmdMultiply.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                calcLogic(MULTIPLY);

            }
        });

        cmdOne.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (clearCalcDisplay == CLEAR) {
                    txtDisplay.setText("");
                }
                clearCalcDisplay = DONT_CLEAR;
                txtDisplay.append("1");

            }
        });

        cmdTwo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (clearCalcDisplay == CLEAR) {
                    txtDisplay.setText("");
                }
                clearCalcDisplay = DONT_CLEAR;
                txtDisplay.append("2");

            }
        });

        cmdThree.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (clearCalcDisplay == CLEAR) {
                    txtDisplay.setText("");
                }
                clearCalcDisplay = DONT_CLEAR;
                txtDisplay.append("3");

            }
        });

        cmdSubtract.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                calcLogic(SUBTRACT);
            }
        });

        cmdDecimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clearCalcDisplay == CLEAR) {
                    txtDisplay.setText("");
                }
                clearCalcDisplay = DONT_CLEAR;
                txtDisplay.append(".");
            }
        });

        cmdZzero.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (clearCalcDisplay == CLEAR) {
                    txtDisplay.setText("");
                }
                clearCalcDisplay = DONT_CLEAR;
                txtDisplay.append("0");

            }
        });

        cmdEquals.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                calcLogic(EQUALS);

            }
        });

        cmdAddition.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                calcLogic(ADD);
            }
        });
    }

    private void calcLogic(int operator) {

        float displayVal = 0.0f;
        try {
            displayVal = Float.parseFloat(txtDisplay.getText().toString());
        } catch (NumberFormatException e) {
           txtDisplay.setText("0");
        }

        mathVariables.add(displayVal);

        if (operator != EQUALS) {
            nextOperation = operator;
        }else if (operator == EQUALS){
            nextOperation = 0;
        }

        switch (currentOperation) {
            case ADD:
                mathVariable1 = mathVariables.get(0);
                mathVariable2 = mathVariables.get(1);

                mathVariables.removeAll(mathVariables);

                mathVariables.add(mathVariable1 + mathVariable2);

                //txtDisplay.setText(String.format(formatNumber, mathVariables.get(0)));
                writeResult(mathVariables.get(0));
                break;
            case SUBTRACT:
                mathVariable1 = mathVariables.get(0);
                mathVariable2 = mathVariables.get(1);

                mathVariables.removeAll(mathVariables);

                mathVariables.add(mathVariable1 - mathVariable2);

                //txtDisplay.setText(String.format(formatNumber, mathVariables.get(0)));
                writeResult(mathVariables.get(0));
                break;
            case MULTIPLY:
                mathVariable1 = mathVariables.get(0);
                mathVariable2 = mathVariables.get(1);

                mathVariables.removeAll(mathVariables);

                mathVariables.add(mathVariable1 * mathVariable2);

                //txtDisplay.setText(String.format(formatNumber, mathVariables.get(0)));
                writeResult(mathVariables.get(0));
                break;
            case DIVISION:
                mathVariable1 = mathVariables.get(0);
                mathVariable2 = mathVariables.get(1);

                mathVariables.removeAll(mathVariables);

                mathVariables.add(mathVariable1 / mathVariable2);

                //txtDisplay.setText(String.format(formatNumber, mathVariables.get(0)));
                writeResult(mathVariables.get(0));
                break;
        }

        clearCalcDisplay = CLEAR;
        currentOperation = nextOperation;
        if (operator == EQUALS) {
            mathVariable1 = 0;
            mathVariable2 = 0;
            mathVariables.removeAll(mathVariables);
        }
    }


    private void writeResult(Float res){
        //txtDisplay.setText(String.format(formatNumber, mathVariables.get(0)));
        txtDisplay.setText(res.toString());
    }


}
