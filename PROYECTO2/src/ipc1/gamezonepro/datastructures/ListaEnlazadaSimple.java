package ipc1.gamezonepro.datastructures;

public class ListaEnlazadaSimple<T> {

    private NodoSimple<T> cabeza;
    private NodoSimple<T> cola;
    private int tamanio;

    public void agregar(T dato) {
        NodoSimple<T> nuevo = new NodoSimple<T>(dato);
        if (cabeza == null) {
            cabeza = nuevo;
            cola = nuevo;
        } else {
            cola.setSiguiente(nuevo);
            cola = nuevo;
        }
        tamanio++;
    }

    public void agregarAlInicio(T dato) {
        NodoSimple<T> nuevo = new NodoSimple<T>(dato);
        if (cabeza == null) {
            cabeza = nuevo;
            cola = nuevo;
        } else {
            nuevo.setSiguiente(cabeza);
            cabeza = nuevo;
        }
        tamanio++;
    }

    public T obtener(int indice) {
        validarIndice(indice);
        NodoSimple<T> actual = cabeza;
        int posicion = 0;
        while (actual != null) {
            if (posicion == indice) {
                return actual.getDato();
            }
            posicion++;
            actual = actual.getSiguiente();
        }
        return null;
    }

    public void establecer(int indice, T dato) {
        validarIndice(indice);
        NodoSimple<T> actual = cabeza;
        int posicion = 0;
        while (actual != null) {
            if (posicion == indice) {
                actual.setDato(dato);
                return;
            }
            posicion++;
            actual = actual.getSiguiente();
        }
    }

    public boolean contiene(T dato) {
        NodoSimple<T> actual = cabeza;
        while (actual != null) {
            if (actual.getDato() == dato || (actual.getDato() != null && actual.getDato().equals(dato))) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    public T eliminarEn(int indice) {
        validarIndice(indice);
        if (indice == 0) {
            T dato = cabeza.getDato();
            cabeza = cabeza.getSiguiente();
            if (cabeza == null) {
                cola = null;
            }
            tamanio--;
            return dato;
        }

        NodoSimple<T> anterior = cabeza;
        int posicion = 0;
        while (anterior != null && posicion < indice - 1) {
            anterior = anterior.getSiguiente();
            posicion++;
        }

        NodoSimple<T> objetivo = anterior != null ? anterior.getSiguiente() : null;
        if (objetivo == null) {
            return null;
        }
        anterior.setSiguiente(objetivo.getSiguiente());
        if (objetivo == cola) {
            cola = anterior;
        }
        tamanio--;
        return objetivo.getDato();
    }

    public boolean eliminar(T dato) {
        if (cabeza == null) {
            return false;
        }
        if (cabeza.getDato() == dato || (cabeza.getDato() != null && cabeza.getDato().equals(dato))) {
            eliminarEn(0);
            return true;
        }
        NodoSimple<T> actual = cabeza;
        while (actual.getSiguiente() != null) {
            T actualDato = actual.getSiguiente().getDato();
            if (actualDato == dato || (actualDato != null && actualDato.equals(dato))) {
                if (actual.getSiguiente() == cola) {
                    cola = actual;
                }
                actual.setSiguiente(actual.getSiguiente().getSiguiente());
                tamanio--;
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    public void limpiar() {
        cabeza = null;
        cola = null;
        tamanio = 0;
    }

    public boolean estaVacia() {
        return tamanio == 0;
    }

    public int tamanio() {
        return tamanio;
    }

    public NodoSimple<T> getCabeza() {
        return cabeza;
    }

    private void validarIndice(int indice) {
        if (indice < 0 || indice >= tamanio) {
            throw new IllegalArgumentException("Indice fuera de rango: " + indice);
        }
    }
}
