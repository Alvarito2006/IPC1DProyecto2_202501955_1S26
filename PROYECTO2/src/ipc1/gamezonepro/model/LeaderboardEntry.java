package ipc1.gamezonepro.model;

public class LeaderboardEntry {

    private String nombre;
    private int xp;
    private boolean usuarioActual;

    public LeaderboardEntry(String nombre, int xp, boolean usuarioActual) {
        this.nombre = nombre;
        this.xp = xp;
        this.usuarioActual = usuarioActual;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public boolean isUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(boolean usuarioActual) {
        this.usuarioActual = usuarioActual;
    }
}
