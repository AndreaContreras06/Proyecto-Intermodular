package dam.code.service;

import dam.code.dao.ActividadDAO;
import dam.code.dao.impl.ActividadDAOImpl;
import dam.code.exceptions.ActividadException;
import dam.code.models.Actividad;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class ActividadService {

    private final ActividadDAO actividadDAO = new ActividadDAOImpl();

    public ObservableList<Actividad> obtenerActividad() throws ActividadException {
        return FXCollections.observableArrayList(actividadDAO.listar());
    }

    public void addInteres(int idUsuario, Actividad actividad) throws ActividadException {
        actividadDAO.interes(idUsuario, actividad.getId());
    }

    public void agregarActividad(Actividad actividad) throws ActividadException {
        validarActividad(actividad);

        actividadDAO.registrar(actividad);
    }

    private void validarActividad(Actividad actividad) throws ActividadException {
        if (actividad.getActividad().length() < 3) {
            throw new ActividadException("El nombre de la actividad es muy corto");
        }
        if (actividad.getDuracion() > 180) {
            throw new ActividadException("Eso es mucho tiempo, descansa");
        }
    }

    public ObservableList<Actividad> obtenerActividadPorUsuario(int idUsuario) throws ActividadException {
        return FXCollections.observableArrayList(actividadDAO.obtenerActividadPorUsuario(idUsuario));
    }
}
