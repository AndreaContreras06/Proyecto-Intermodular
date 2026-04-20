package dam.code.controller;

import dam.code.exceptions.EmpleadoException;
import dam.code.exceptions.UsuarioException;
import dam.code.models.Empleado;
import dam.code.models.Usuario;
import dam.code.models.utils.Plan;
import dam.code.models.utils.Rol;
import dam.code.service.EmpleadoService;
import dam.code.service.UsuarioService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RegistroController {

    @FXML private TextField txtDni;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtPasswordRepit;

    @FXML private ComboBox<Plan> comboPlan;
    @FXML private ComboBox<Rol> comboRol;

    @FXML private Label lblMensaje;

    private UsuarioService service;
    private EmpleadoService empleadoService;

    public void setUsuarioService(UsuarioService service) {
        this.service = service;
    }

    public void setEmpleadoService(UsuarioService empleadoService) {
        this.service = empleadoService;
    }

    @FXML
    private void initialize() {
        comboPlan.setItems(FXCollections.observableArrayList(Plan.values()));
        comboRol.setItems(FXCollections.observableArrayList(Rol.values()));
    }

    @FXML
    private void registrarUsuario() {
        if (validarPassword()) {
            try {
                if(!validarCampos()) {
                    lblMensaje.setText("Todos los campos son obligatorios");
                    lblMensaje.setStyle("-fx-text-fill: red");
                    return;
                }
                Usuario usuario = new Usuario(
                        txtDni.getText(),
                        txtNombre.getText(),
                        txtApellido.getText(),
                        txtTelefono.getText(),
                        txtEmail.getText(),
                        comboPlan.getValue()
                );
                service.registrar(usuario, txtPassword.getText());
                lblMensaje.setText("Usuario registrado con éxito");
                lblMensaje.setStyle("-fx-text-fill: lightgreen;");
                limpiarCampos();
            } catch (UsuarioException e) {
                mostrarError(e.getMessage());
            }
        } else {
            lblMensaje.setText("Las contraseñas no coinciden");
            lblMensaje.setStyle("-fx-text-fill: red");
        }
    }

    @FXML
    private void registrarEmpleado() {
        if (validarPassword()) {
            try {
                if(!validarCamposEmpleados()) {
                    lblMensaje.setText("Todos los campos son obligatorios");
                    lblMensaje.setStyle("-fx-text-fill: red");
                    return;
                }
                Empleado empleado = new Empleado(
                        txtDni.getText(),
                        txtNombre.getText(),
                        txtApellido.getText(),
                        txtTelefono.getText(),
                        txtEmail.getText(),
                        comboRol.getValue()
                );
                service.registrar(empleado, txtPassword.getText());
                lblMensaje.setText("Empleado registrado con éxito");
                lblMensaje.setStyle("-fx-text-fill: lightgreen;");
                limpiarCampos();
            } catch (EmpleadoException e) {
                mostrarError(e.getMessage());
            }
        } else {
            lblMensaje.setText("Las contraseñas no coinciden");
            lblMensaje.setStyle("-fx-text-fill: red");
        }
    }

    private void limpiarCampos() {
        txtDni.clear();
        txtNombre.clear();
        txtApellido.clear();
        txtTelefono.clear();
        txtEmail.clear();
        txtPassword.clear();
        txtPasswordRepit.clear();
        comboPlan.setValue(null);
    }

    @FXML
    public void inicio() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/inicio.fxml"));

            Parent root = loader.load();

            InicioController controller = loader.getController();
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

    private boolean validarCampos() {
        return !txtDni.getText().isEmpty()
                && !txtNombre.getText().isEmpty()
                && !txtApellido.getText().isEmpty()
                && !txtTelefono.getText().isEmpty()
                && !txtEmail.getText().isEmpty()
                && !txtPassword.getText().isEmpty()
                && !txtPasswordRepit.getText().isEmpty()
                && comboPlan.getValue() != null;
    }

    private boolean validarCamposEmpleados() {
        return !txtDni.getText().isEmpty()
                && !txtNombre.getText().isEmpty()
                && !txtApellido.getText().isEmpty()
                && !txtTelefono.getText().isEmpty()
                && !txtEmail.getText().isEmpty()
                && !txtPassword.getText().isEmpty()
                && !txtPasswordRepit.getText().isEmpty()
                && comboRol.getValue() != null;
    }


    private boolean validarPassword() {
        return txtPasswordRepit.getText().equals(txtPassword.getText());
    }

    private void mostrarError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
