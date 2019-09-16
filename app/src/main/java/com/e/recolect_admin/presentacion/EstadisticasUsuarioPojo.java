package com.e.recolect_admin.presentacion;

public class EstadisticasUsuarioPojo {
    private int logins, registrados, registros, usos;

    public EstadisticasUsuarioPojo() {
    }

    public int getLogins() {
        return logins;
    }

    public void setLogins(int logins) {
        this.logins = logins;
    }

    public int getRegistrados() {
        return registrados;
    }

    public void setRegistrados(int registrados) {
        this.registrados = registrados;
    }

    public int getRegistros() {
        return registros;
    }

    public void setRegistros(int registros) {
        this.registros = registros;
    }

    public int getUsos() {
        return usos;
    }

    public void setUsos(int usos) {
        this.usos = usos;
    }

    public int[] datos() {
        int[] vectorDatos = new int[4];
        vectorDatos[0] = getLogins();
        vectorDatos[1] = getRegistrados();
        vectorDatos[2] = getRegistros();
        vectorDatos[3] = getUsos();
        return vectorDatos;
    }
}
