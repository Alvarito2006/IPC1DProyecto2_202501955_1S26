package ipc1.gamezonepro.datastructures;

public class Cola<T> {

    private NodoCola<T> frente;
    private NodoCola<T> fin;
    private int tamanio;

    public synchronized void encolar(T dato) {
        NodoCola<T> nuevo = new NodoCola<T>(dato);
        if (frente == null) {
            frente = nuevo;
            fin = nuevo;
        } else {
            fin.setSiguiente(nuevo);
            fin = nuevo;
        }
        tamanio++;
    }

    public synchronized T desencolar() {
        if (frente == null) {
            return null;
        }
        T dato = frente.getDato();
        frente = frente.getSiguiente();
        if (frente == null) {
            fin = null;
        }
        tamanio--;
        return dato;
    }

    public synchronized T peek() {
        return frente != null ? frente.getDato() : null;
    }

    public synchronized boolean estaVacia() {
        return tamanio == 0;
    }

    public synchronized int tamanio() {
        return tamanio;
    }

    public synchronized String representarComoTexto() {
        StringBuilder builder = new StringBuilder();
        NodoCola<T> actual = frente;
        while (actual != null) {
            if (builder.length() > 0) {
                builder.append(" -> ");
            }
            builder.append(actual.getDato());
            actual = actual.getSiguiente();
        }
        if (builder.length() == 0) {
            builder.append("Sin usuarios en espera");
        }
        return builder.toString();
    }
}
