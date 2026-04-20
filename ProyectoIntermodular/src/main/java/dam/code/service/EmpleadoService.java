package dam.code.service;

import dam.code.dao.EmpleadoDAO;
import dam.code.dao.impl.EmpleadoDAOImpl;
import dam.code.exceptions.EmpleadoException;
import dam.code.models.Empleado;

public class EmpleadoService {
    private final EmpleadoDAO empleadoDAO = new EmpleadoDAOImpl();

    public Empleado loginEmpleado(String dni, String password) throws EmpleadoException {
        return empleadoDAO.login(dni, password);
    }

    public void registrarEmpleado(Empleado empleado, String password) throws EmpleadoException {
        validarPasswordEmpleado(password);
        validarEmailEmpleado(empleado.getEmail());
        empleadoDAO.registrar(empleado, password);
    }

    private void validarEmailEmpleado(String email) throws EmpleadoException {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!email.matches(regex)) {
            throw new EmpleadoException("El formato del email no es válido");
        }
    }

    private void validarPasswordEmpleado(String password) throws EmpleadoException {
        if (password.length() < 6) {
            throw new EmpleadoException("La contraseña tiene que ser mínimo de 6 caracteres!");
        }
        if (!password.matches(".*[a-z].*")) {
            throw new EmpleadoException("La contraseña debe contener al menos una minúscula!");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new EmpleadoException("La contraseña debe contener al menos una mayúscula");
        }
        if (!password.matches(".*[@$!%*?&].*")) {
            throw new EmpleadoException("La contraseña debe contener al menos uno de estos símbolos @$!%*?&");
        }
        if (!password.matches(".*\\d.*")) {
            throw new EmpleadoException("La contraseña debe contener al menos un número");
        }
    }
}
