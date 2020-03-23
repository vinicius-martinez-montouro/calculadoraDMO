package com.example.calculadoradmo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.calculadoradmo.R;
import com.example.calculadoradmo.constants.Constantes;
import com.example.calculadoradmo.model.Calculadora;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Calculadora calculadora;
    private TextView resultEditText;
    private boolean hasDot;

    private String value;
    private String operation;
    private String afterOperationValue;
    private boolean showResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultEditText = findViewById(R.id.text_view_result);

        findViewById(R.id.button_0).setOnClickListener(this);
        findViewById(R.id.button_1).setOnClickListener(this);
        findViewById(R.id.button_2).setOnClickListener(this);
        findViewById(R.id.button_3).setOnClickListener(this);
        findViewById(R.id.button_4).setOnClickListener(this);
        findViewById(R.id.button_5).setOnClickListener(this);
        findViewById(R.id.button_6).setOnClickListener(this);
        findViewById(R.id.button_7).setOnClickListener(this);
        findViewById(R.id.button_8).setOnClickListener(this);
        findViewById(R.id.button_9).setOnClickListener(this);
        findViewById(R.id.button_dot).setOnClickListener(this);
        findViewById(R.id.button_equal).setOnClickListener(this);
        findViewById(R.id.button_erase).setOnClickListener(this);
        findViewById(R.id.button_erase_last).setOnClickListener(this);

        findViewById(R.id.button_pow).setOnClickListener(this);
        findViewById(R.id.button_division).setOnClickListener(this);
        findViewById(R.id.button_multiply).setOnClickListener(this);
        findViewById(R.id.button_minus).setOnClickListener(this);
        findViewById(R.id.button_plus).setOnClickListener(this);

        calculadora = Calculadora.getInstance();

        this.value = "";
        this.afterOperationValue = "";
        this.showResult = false;
    }


    @Override
    public void onClick(View v) {
        String text = ((Button) v).getText().toString();
        if(isNumber(text)){
            addNumber(text);
        }else{
            switch (text.toLowerCase()){
                case ".":{
                    if(!hasDot){
                        addDot();
                    }
                    break;
                }
                case "c": {
                    erase("",false);
                    break;
                }
                case "ce"{
                    cleanLastOperation();
                    break;
                }
                case "=":{
                    if(!this.value.matches("[a-zA-Z]*")){
                        erase("",false);
                    }else{
                        getResult();
                    }
                    break;
                }
                default:{
                    addOperation(text);
                    break;
                }
            }
        }
        updateValue(getValueShow());
    }

    private String getValueShow(){
        return this.afterOperationValue.equals("") ? this.value : this.afterOperationValue;
    }

    private int getConstants(String operation){
        switch (operation){
            case "+":
                return Constantes.ADICAO;
            case "-":
                return Constantes.SUBTRACAO;
            case "/":
                return Constantes.DIVISAO;
            case "^":
                return Constantes.POTENCIA;
            default:
                return Constantes.MULTIPLICACAO;
        }
    }

    private void addOperation(String operationValue){
        if(afterOperationValue.equals("")){
            operation = operationValue;
            Double result = calculadora.calcular(getConstants(operation), Float.parseFloat(this.value));
            hasDot = false;
            value = getFormatedValue(result);
        }else{
            getResult();
        }

    }

    private void getResult(){
        Double result = getFinalResult();
        erase(getFormatedValue(result), result % 1 != 0);
    }

    private Double getFinalResult(){
        if(operation == null){
            return Double.parseDouble(value);
        }
        return calculadora
                .calcular(Constantes.RESULTADO,Float.parseFloat(this.afterOperationValue));
    }

    private boolean verifyEmptyString(){
        if(operation == null){
            return this.value.equals("") || showResult;
        }
        return this.afterOperationValue.equals("");
    }

    private String getFormatedValue(Double value){
        if(value % 1 == 0){
            return new DecimalFormat("#").format(value);
        }
        return String.valueOf(value);
    }

    private void erase(String value, boolean hasDot){
        this.value = value;
        this.operation = null;
        this.afterOperationValue = "";
        this.hasDot = hasDot;
        this.showResult = true;
        calculadora.c();
    }

    private boolean isNumber(String value){
        if(value.matches("[\\d]")){
            return true;
        }
        return false;
    }

    private void addNumber(String value){
        if(showResult){
            this.value = "";
            this.showResult = false;
        }
        if(this.operation == null){
            this.value = this.value.concat(value);
        }else{
            this.afterOperationValue = this.afterOperationValue.concat(value);
        }
    }

    private void addDot(){
        if(verifyEmptyString()){
            addNumber("0");
        }
        addNumber(".");
        this.hasDot = true;
    }

    private void updateValue(String value){
        resultEditText.setText(value);
    }

    private void cleanLastOperation(){
        if(operation == null){
            erase("",false);
        }else{
            if(afterOperationValue.equals("")){
                operation = null;
            }else{
                operation = "";
            }
        }
    }
}
