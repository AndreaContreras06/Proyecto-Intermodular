package dam.code.dao;

import dam.code.exceptions.ActividadException;
import dam.code.models.Actividad;

import java.util.List;
import java.util.Optional;

public interface ActividadDAO {
    void registrar(Actividad actividad) throws ActividadException;
    List<Actividad> listar()  throws ActividadException;
    List<Actividad> obtenerActividadPorUsuario (int idUsuario) throws ActividadException;
    void interes (int idUsuario, int idActividad) throws ActividadException;
}
