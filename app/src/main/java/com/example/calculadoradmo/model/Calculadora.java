package com.example.calculadoradmo.model;

import com.example.calculadoradmo.constants.Constantes;

public class Calculadora {

    private static Calculadora sCalculadora = null;
    private double memoria;
    private int operacao;

    private Calculadora() {
        c();
    }

    public static Calculadora getInstance() {
        if (sCalculadora == null) {
            sCalculadora = new Calculadora();
        }
        return sCalculadora;
    }

    public void c() {
        memoria = 0;
        operacao = Constantes.NULO;
    }

    public double calcular(int operacao, double valor) {
        if (this.operacao == Constantes.NULO) {
            this.operacao = operacao;
            this.memoria = valor;
        } else {
            switch (this.operacao) {
                case Constantes.ADICAO:
                    memoria += valor;
                    break;
                case Constantes.SUBTRACAO:
                    memoria -= valor;
                    break;
                case Constantes.MULTIPLICACAO:
                    memoria *= valor;
                    break;
                case Constantes.DIVISAO:
                    memoria /= valor;
                    break;
                case Constantes.POTENCIA:
                    memoria = Math.pow((memoria), (valor));
                default:
                    break;
            }
            this.operacao = operacao;
        }
        return memoria;
    }
}