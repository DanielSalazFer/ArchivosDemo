package model;

import java.io.Serializable;

/**
 * Created by Daniel on 19/05/2018.
 */

public class Archivo implements Serializable {

    private boolean tipo; // true es directorio, false archivo normal
    private String nombre;
    private String path;

    public Archivo() {
    }

    public Archivo(boolean tipo, String nombre, String path) {
        this.tipo = tipo;
        this.nombre = nombre;
        this.path = path;
    }

    public boolean isTipo() {
        return tipo;
    }

    public void setTipo(boolean tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
