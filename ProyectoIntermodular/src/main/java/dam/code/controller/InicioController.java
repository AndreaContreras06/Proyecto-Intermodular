package dam.code.controller;

import dam.code.exceptions.ActividadException;
import dam.code.exceptions.UsuarioException;
import dam.code.models.Sesion;
import dam.code.models.Usuario;
    import dam.code.service.ActividadService;
import dam.code.service.EmpleadoService;
import dam.code.service.UsuarioService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class InicioController {

    @FXML private TextField txtDni;
    @FXML private PasswordField txtPassword;

    @FXML private Label lblMensaje;

    private UsuarioService service;
    private EmpleadoService empleadoService;

    public void setUsuarioService(UsuarioService service) {
        this.service = service;
    }

    public void setEmpleadoService(EmpleadoService service) {
        this.service = empleadoService;
    }

    @FXML
    public void iniciarSesion() {
        if(!validarCampos()) {
            lblMensaje.setText("Todos los campos son obligatorios");
            lblMensaje.setStyle("-fx-text-fill: red");
            return;
        }
        String dni = txtDni.getText();
        String password = txtPassword.getText();
        try {
            Usuario usuario = service.login(dni, password);

            Sesion.setUsuario(usuario);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/interes.fxml"));
            Parent root = loader.load();
            InteresController controller = loader.getController();
            controller.setUsuario(usuario);
            controller.setActividadService(new ActividadService());

            Stage stage = (Stage) txtDni.getScene().getWindow();
            stage.setWidth(800);
            stage.setHeight(600);
            stage.setScene(new Scene(root));
        } catch (ActividadException | IOException | UsuarioException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    public void registro() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/registro.fxml"));
            Parent root = loader.load();
            RegistroController controller = loader.getController();
            controller.setUsuarioService(service);
            Stage stage = (Stage) txtDni.getScene().getWindow();
            stage.setResizable(false);
            stage.setWidth(800);
            stage.setHeight(600);
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    public void registroEmpleados() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/registro.fxml"));
            Parent root = loader.load();
            RegistroController controller = loader.getController();
            controller.setEmpleadoService(service);
            Stage stage = (Stage) txtDni.getScene().getWindow();
            stage.setResizable(false);
            stage.setWidth(800);
            stage.setHeight(600);
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    private void mostrarError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean validarCampos() {
        return !txtDni.getText().isEmpty() && !txtPassword.getText().isEmpty();
    }
}

