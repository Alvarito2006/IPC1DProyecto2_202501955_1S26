package ipc1.gamezonepro.datastructures;

import ipc1.gamezonepro.model.Carta;

public class MatrizOrtogonal {

    private final int filas;
    private final int columnas;
    private final NodoMatriz inicio;

    public MatrizOrtogonal(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.inicio = construirMatriz();
    }

    private NodoMatriz construirMatriz() {
        NodoMatriz primerNodo = null;
        NodoMatriz filaAnteriorInicio = null;

        for (int fila = 0; fila < filas; fila++) {
            NodoMatriz actualFilaInicio = null;
            NodoMatriz actual = null;
            NodoMatriz superior = filaAnteriorInicio;

            for (int columna = 0; columna < columnas; columna++) {
                NodoMatriz nuevo = new NodoMatriz(fila, columna);
                if (primerNodo == null) {
                    primerNodo = nuevo;
                }
                if (actualFilaInicio == null) {
                    actualFilaInicio = nuevo;
                }
                if (actual != null) {
                    actual.setDerecha(nuevo);
                    nuevo.setIzquierda(actual);
                }
                if (superior != null) {
                    superior.setAbajo(nuevo);
                    nuevo.setArriba(superior);
                    superior = superior.getDerecha();
                }
                actual = nuevo;
            }

            filaAnteriorInicio = actualFilaInicio;
        }

        return primerNodo;
    }

    public NodoMatriz obtenerNodo(int fila, int columna) {
        if (fila < 0 || fila >= filas || columna < 0 || columna >= columnas) {
            return null;
        }
        NodoMatriz actual = inicio;
        int filaActual = 0;
        while (actual != null && filaActual < fila) {
            actual = actual.getAbajo();
            filaActual++;
        }
        int columnaActual = 0;
        while (actual != null && columnaActual < columna) {
            actual = actual.getDerecha();
            columnaActual++;
        }
        return actual;
    }

    public Carta obtenerCarta(int fila, int columna) {
        NodoMatriz nodo = obtenerNodo(fila, columna);
        return nodo != null ? nodo.getCarta() : null;
    }

    public void colocarCarta(int fila, int columna, Carta carta) {
        NodoMatriz nodo = obtenerNodo(fila, columna);
        if (nodo != null) {
            nodo.setCarta(carta);
        }
    }

    public boolean colocarEnPrimeraVacia(Carta carta) {
        NodoMatriz filaActual = inicio;
        while (filaActual != null) {
            NodoMatriz columnaActual = filaActual;
            while (columnaActual != null) {
                if (columnaActual.getCarta() == null) {
                    columnaActual.setCarta(carta);
                    return true;
                }
                columnaActual = columnaActual.getDerecha();
            }
            filaActual = filaActual.getAbajo();
        }
        return false;
    }

    public boolean intercambiar(int filaUno, int columnaUno, int filaDos, int columnaDos) {
        NodoMatriz primero = obtenerNodo(filaUno, columnaUno);
        NodoMatriz segundo = obtenerNodo(filaDos, columnaDos);
        if (primero == null || segundo == null) {
            return false;
        }
        Carta temporal = primero.getCarta();
        primero.setCarta(segundo.getCarta());
        segundo.setCarta(temporal);
        return true;
    }

    public int contarCartas() {
        int total = 0;
        NodoMatriz filaActual = inicio;
        while (filaActual != null) {
            NodoMatriz columnaActual = filaActual;
            while (columnaActual != null) {
                if (columnaActual.getCarta() != null) {
                    total++;
                }
                columnaActual = columnaActual.getDerecha();
            }
            filaActual = filaActual.getAbajo();
        }
        return total;
    }

    public boolean filaCompleta(int fila) {
        NodoMatriz inicioFila = obtenerNodo(fila, 0);
        NodoMatriz actual = inicioFila;
        while (actual != null) {
            if (actual.getCarta() == null) {
                return false;
            }
            actual = actual.getDerecha();
        }
        return true;
    }

    public boolean contieneCodigo(String codigoCarta) {
        NodoMatriz filaActual = inicio;
        while (filaActual != null) {
            NodoMatriz columnaActual = filaActual;
            while (columnaActual != null) {
                if (columnaActual.getCarta() != null && columnaActual.getCarta().getCodigo().equalsIgnoreCase(codigoCarta)) {
                    return true;
                }
                columnaActual = columnaActual.getDerecha();
            }
            filaActual = filaActual.getAbajo();
        }
        return false;
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public NodoMatriz getInicio() {
        return inicio;
    }
}
