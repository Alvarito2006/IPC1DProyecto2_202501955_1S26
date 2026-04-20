package ipc1.gamezonepro.model;

public class EstudianteInfo {

    private final String nombreCompleto;
    private final String carnet;
    private final String cui;
    private final String correo;
    private final String seccion;
    private final String semestre;
    private final String descripcionProyecto;

    public EstudianteInfo(String nombreCompleto, String carnet, String cui, String correo, String seccion, String semestre, String descripcionProyecto) {
        this.nombreCompleto = nombreCompleto;
        this.carnet = carnet;
        this.cui = cui;
        this.correo = correo;
        this.seccion = seccion;
        this.semestre = semestre;
        this.descripcionProyecto = descripcionProyecto;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getCarnet() {
        return carnet;
    }

    public String getCui() {
        return cui;
    }

    public String getCorreo() {
        return correo;
    }

    public String getSeccion() {
        return seccion;
    }

    public String getSemestre() {
        return semestre;
    }

    public String getDescripcionProyecto() {
        return descripcionProyecto;
    }
}
