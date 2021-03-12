package com.example.convertion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Spinner type, outputUnit, inputUnit;
    Button convert_btn;
    EditText inputValue;
    TextView result;
    // user's choices for type of measurements
    String[] types = {"Mass", "Length", "Volume"};
    //units array for each type of measurements
    String[] massunits = {"milligram (mg)", "gram (g)", "ounce (oz)", "pound (lb)", "kilogram (kg)", "ton (t)"};
    String[] lengthunits = {"millimeter (mm)", "centimeter (cm)", "inch (in)", "feet (ft)", "yard (yd)", "meter (m)", "kilometer (km)", "mile (mi)"};
    String[] volumeunits = {"milliliter (ml)", "ounce (oz)", "cup (c)", "quart (qt)", "liter (l)"};
    //arrays for conversions value between units
    Double[] massConversions = {1000.0, 28.35, 16.0, 2.205, 1000.0};
    Double[] lengthConversions = {10.0, 2.54, 12.0, 3.0, 1.094, 1000.0, 1.609};
    Double[] volumeConversions = {29.574, 8.0, 4.0, 1.057};
    //2d arrays for easier accessing in loops
    String[][] units = {massunits, lengthunits, volumeunits};
    Double[][] conversions = {massConversions, lengthConversions, volumeConversions};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //dropdown list for type of measurement(mass, length or volume)
        type = findViewById(R.id.unit);
        //dropdown list for unit of output
        outputUnit = findViewById(R.id.output_unit);
        //dropdown list for unit of input
        inputUnit = findViewById(R.id.input_unit);
        //editText for the input from user
        inputValue = findViewById(R.id.input);
        //textView for the result
        result = findViewById(R.id.output);
        //button of conversion
        convert_btn = findViewById(R.id.convert_btn);
        //array adapter for filling the dropdown lists
        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(type.getContext(),
                android.R.layout.simple_spinner_dropdown_item, types);
        type.setAdapter(adapterType);
        //updateSpinners function for changing the values of dropdown lists depending
        //on value of the chosen type pf measurements(by default mass is chosen)
        updateSpinners();
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateSpinners();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void updateSpinners() {
        //index of selected item in dropdown list of type of measurements
        //0->mass, 1->length, 2->volume
        int typeIndex = type.getSelectedItemPosition();
        //updating the dropdown list input and output units
        ArrayAdapter<String> inputUnitAdapter = new ArrayAdapter<String>(inputUnit.getContext(),
                android.R.layout.simple_spinner_dropdown_item, units[typeIndex]);
        inputUnit.setAdapter(inputUnitAdapter);
        ArrayAdapter<String> outputUnitAdapter = new ArrayAdapter<String>(outputUnit.getContext(),
                android.R.layout.simple_spinner_dropdown_item, units[typeIndex]);
        outputUnit.setAdapter(outputUnitAdapter);
        outputUnit.setSelection(1);

    }


    public void convertInput(View view) {
        //checking if input is empty
        if (inputValue.getText().toString().trim().length() == 0 || inputValue.getText().toString().equals(".")) {
            Toast.makeText(this, String.format("Enter a number to convert"), Toast.LENGTH_SHORT).show();
            return;
        }
        //getting index of units selected in dropdown lists
        int inputUnitIndex = inputUnit.getSelectedItemPosition();
        int outputUnitIndex = outputUnit.getSelectedItemPosition();
        int typeIndex = type.getSelectedItemPosition();
        //stroing the value entered by user
        double resultValue = Double.parseDouble(inputValue.getText().toString());
        //incase the output and the input units are the same=>input from user = to the result
        if (outputUnitIndex == inputUnitIndex) {
            result.setText(inputValue.getText().toString());
            return;
        } else {
            //incase converting from smaller unit to bigger unit(ex: from g->kg)
            if (outputUnitIndex > inputUnitIndex) {
                for (int i = inputUnitIndex; i < outputUnitIndex; i++) {
                    //input is divided by the conversion values between units
                    resultValue = resultValue / conversions[typeIndex][i];
                }
            } else {
                //incase converting from bigger to smaller unit(ex: from kg->g)
                for (int i = outputUnitIndex; i < inputUnitIndex; i++) {
                    //input is multiplied by the conversion values between units
                    resultValue = resultValue * conversions[typeIndex][i];

                }
            }
        }
        //rounding the result value;
        resultValue = (int) (Math.round(resultValue * 100)) / 100.0;
        //setting the result value in the output textview
        result.setText(Double.toString(resultValue));
    }
    //switch between units of measurements
    public void switchvalues(View view) {
        int inputUnitIndex = inputUnit.getSelectedItemPosition();
        int outputUnitIndex = outputUnit.getSelectedItemPosition();
        inputUnit.setSelection(outputUnitIndex);
        outputUnit.setSelection(inputUnitIndex);
        convert_btn.performClick();


    }
}