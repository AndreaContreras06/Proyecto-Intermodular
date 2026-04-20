package dam.code.dao;

import dam.code.exceptions.EmpleadoException;
import dam.code.models.Empleado;

import java.util.List;
import java.util.Optional;

public interface EmpleadoDAO {
    void registrar(Empleado empleado, String password) throws EmpleadoException;
    Empleado login(String dni, String password) throws EmpleadoException;
}

