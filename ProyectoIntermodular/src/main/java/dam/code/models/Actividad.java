package dam.code.models;

import java.time.LocalDate;

public class Actividad {

    private int id;
    private String actividad;
    private int duracion;
    private int interes;

    public Actividad(String actividad, int duracion) {
        this.actividad = actividad;
        this.duracion = duracion;
        this.interes = 0;
    }

    public Actividad(int id, String actividad, int duracion) {
        this.id = id;
        this.actividad = actividad;
        this.duracion = duracion;
        this.interes = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }


    public int getInteres() {
        return interes;
    }

    public void setInteres(int interes) {
        this.interes = interes;
    }
}