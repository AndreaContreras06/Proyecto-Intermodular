package dam.code.models;

import dam.code.models.utils.Plan;
import dam.code.models.utils.Rol;

public class Sesion {
    private static Usuario usuarioActual;

    public static void setUsuario(Usuario usuario) {
        usuarioActual = usuario;
    }

    public static Usuario getUsuario() {
        return usuarioActual;
    }

    public static Plan getPlan()  {
        return usuarioActual.getPlan();
    }

    private static Empleado empleadoActual;

    public static void setEmpleado(Empleado empleado) {
        empleadoActual = empleado;
    }

    public static Empleado getEmpleado() {
        return empleadoActual;
    }

    public static Rol getRol()  {
        return empleadoActual.getRol();
    }
}

