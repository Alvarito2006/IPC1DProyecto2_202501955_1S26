package ipc1.gamezonepro.datastructures;

import ipc1.gamezonepro.model.Carta;

public class NodoMatriz {

    private final int fila;
    private final int columna;
    private Carta carta;
    private NodoMatriz arriba;
    private NodoMatriz abajo;
    private NodoMatriz izquierda;
    private NodoMatriz derecha;

    public NodoMatriz(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public Carta getCarta() {
        return carta;
    }

    public void setCarta(Carta carta) {
        this.carta = carta;
    }

    public NodoMatriz getArriba() {
        return arriba;
    }

    public void setArriba(NodoMatriz arriba) {
        this.arriba = arriba;
    }

    public NodoMatriz getAbajo() {
        return abajo;
    }

    public void setAbajo(NodoMatriz abajo) {
        this.abajo = abajo;
    }

    public NodoMatriz getIzquierda() {
        return izquierda;
    }

    public void setIzquierda(NodoMatriz izquierda) {
        this.izquierda = izquierda;
    }

    public NodoMatriz getDerecha() {
        return derecha;
    }

    public void setDerecha(NodoMatriz derecha) {
        this.derecha = derecha;
    }
}
