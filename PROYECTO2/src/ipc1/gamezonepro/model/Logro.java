package ipc1.gamezonepro.model;

public class Logro {

    private final String nombre;
    private final String descripcion;
    private boolean desbloqueado;
    private String fechaDesbloqueo;

    public Logro(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean isDesbloqueado() {
        return desbloqueado;
    }

    public void setDesbloqueado(boolean desbloqueado) {
        this.desbloqueado = desbloqueado;
    }

    public String getFechaDesbloqueo() {
        return fechaDesbloqueo;
    }

    public void setFechaDesbloqueo(String fechaDesbloqueo) {
        this.fechaDesbloqueo = fechaDesbloqueo;
    }
}
