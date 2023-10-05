package com.example.mortgagepayment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MortgageInput extends AppCompatActivity {

    private static final DecimalFormat df = new DecimalFormat("0.00");
    Button calcit;
    EditText princtxt, intratetxt, loandurtxt;
    RadioGroup intratetype;
    RadioGroup durationtype;
    RadioButton interestratebtn;
    int radioBtnIDinterest;
    int radioBtnIDduration;
    RadioButton durationbtn;
    String intrateBtntxt, durationBtntxt;

    public double convertInterestToMonthly(double intrate)
    {
        //converts annual interest rate to monthly interest rate
        double interest = intrate/12;
        return interest;
    }

    public double convertInterestToFloat(double intrate)
    {
        //converts annual interest rate to monthly interest rate
        double interest = intrate/100;
        return interest;
    }

    public double convertDuration(double duration)
    {
        //converts years into equivalent months
        double months = (duration * 12);
        return months;
    }

    public static Double stringToDouble(String str) {
        return Double.valueOf(str);
    }

    public static double calculateEMI(double interest, double duration, double principal)
    {
        Log.w("int", Double.toString(interest));
        Log.w("dur", Double.toString(duration));
        Log.w("princ", Double.toString(principal));
        //formula i am implementing: EMI = P * [ (i * ((1 + i)^n)) / (((1 + i)^n)-1)]
        double numerator = Math.pow((1 + interest), duration); // (1 + i)^n)
        Log.w("num1", Double.toString(numerator));
        numerator =  interest * numerator;
        Log.w("num", Double.toString(numerator));

        double denominator = Math.pow((1 + interest), duration); // (1 + i)^n)
        Log.w("denom1", Double.toString(denominator));
        denominator = denominator - 1;
        Log.w("denom", Double.toString(denominator));

        double division = numerator / denominator;

        double result = (principal * (division));
        Log.w("result", Double.toString(result));

        return result;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mortgage_input);

        ArrayList<String> emptyMsgs = new ArrayList<String>();
        ArrayList<String> errorInput = new ArrayList<String>();

        calcit = findViewById(R.id.calit);
        princtxt = findViewById(R.id.principalamt);
        intratetxt = findViewById(R.id.interestrate);
        loandurtxt = findViewById(R.id.loanduration);

        //get radio buttons
        //get interest type radio button, specifically which text is selected so it can be converted if needed
        intratetype = findViewById(R.id.intratetype);

        durationtype = findViewById(R.id.loandurationtype);

        calcit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //clear lists to make sure error status is new.
                emptyMsgs.clear();
                errorInput.clear();

                double actualprincipal = 0;
                double actualintrate = 0;
                double actualduration = 0;

                radioBtnIDinterest = intratetype.getCheckedRadioButtonId();
                if (radioBtnIDinterest != -1) {
                    interestratebtn = intratetype.findViewById(radioBtnIDinterest);
                    intrateBtntxt = interestratebtn.getText().toString(); //string of selected interest radio button
                }
                else {
                    //trigger some sort of msg that btn needs to be selected
                    emptyMsgs.add("interest rate type");
                }

                radioBtnIDduration = durationtype.getCheckedRadioButtonId();
                if (radioBtnIDduration != -1) {
                    durationbtn = durationtype.findViewById(radioBtnIDduration);
                    durationBtntxt = durationbtn.getText().toString(); //string of selected duration radio button
                }
                else{
                    //trigger some sort of msg that btn needs to be selected
                    emptyMsgs.add("duration type");
                }


                String principal = princtxt.getText().toString();
                if (!principal.isEmpty())
                {
                    //check if can be converted to float
                    try {
                        actualprincipal = stringToDouble(principal);
                    }
                    catch (Exception e) {
                        //please enter a number for principal amount
                        errorInput.add("principal amount");
                    }
                }
                else
                {
                    //create popup that field not filled out correctly
                    emptyMsgs.add("principal amount");
                }


                String intrate = intratetxt.getText().toString();
                if (!intrate.isEmpty())
                {
                    //check if can be converted to float
                    try {
                        Log.w("ir", intrate.toString());
                        actualintrate = stringToDouble(intrate);
                        Log.w("ir2", Double.toString(actualintrate));
                    }
                    catch (Exception e) {
                        errorInput.add("interest rate");
                    }
                }
                else
                {
                    //create popup that field not filled out correctly
                    emptyMsgs.add("interest rate");
                }

                String loanlength = loandurtxt.getText().toString();
                if (!loanlength.isEmpty())
                {
                    //check if can be converted to float
                    try {
                        actualduration = stringToDouble(loanlength);
                    }
                    catch (Exception e) {
                        errorInput.add("loan duration");
                    }
                }
                else
                {
                    //create popup that field not filled out correctly
                    emptyMsgs.add("loan duration");
                }

                //now that all fields have been filled in and error lists added to where necessary, check those lists
                //first ask for all fields to be filled in
                if (!emptyMsgs.isEmpty())
                {
                    CharSequence text = "Please fill in all fields and make all selections.";
                    Toast toast = Toast.makeText(MortgageInput.this, text, Toast.LENGTH_LONG);
                    toast.show();
                }
                else{

                    //now if there is any invalid output, ask them to correct the first issue :)
                    //by the time it is done the list should be empty.
                    if (!errorInput.isEmpty())
                    {
                        CharSequence text = "There was an issue with the format of the " + errorInput.get(0) +". Please correct the input and try again.";
                        Toast toast = Toast.makeText(MortgageInput.this, text, Toast.LENGTH_LONG);
                        toast.show();
                    }
                    else
                    {
                        //can FINALLY calculate the interest and pass it to the activity
                        if ((intrateBtntxt.toUpperCase()).equals("ANNUAL")){
                            //convert interest rate to monthly rate if necessary
                            actualintrate = convertInterestToMonthly(actualintrate);
                            Log.w("ir", "convert");
                        }

                        if ((durationBtntxt.toUpperCase()).equals("YEARS")){

                            actualduration = convertDuration(actualduration);
                        }
                        else
                        {
                            actualduration = actualduration;
                        }

                        actualintrate = convertInterestToFloat(actualintrate);

                        double emi = calculateEMI(actualintrate, actualduration, actualprincipal);
                        String tosend = df.format(emi);
                        //with calculated EMI, pass it to the new activity for display
                        Intent intent = new Intent(MortgageInput.this, MortgageDisplay.class);
                        intent.putExtra("emi", tosend);
                        startActivity(intent);

                    }
                }
            }
        });
    }


}