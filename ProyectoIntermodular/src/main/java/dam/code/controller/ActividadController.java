package dam.code.controller;

import dam.code.exceptions.ActividadException;
import dam.code.models.Actividad;
import dam.code.models.Usuario;
import dam.code.service.ActividadService;
import dam.code.service.UsuarioService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ActividadController {

    private Usuario usuario;
    private ActividadService service;

    @FXML private Label lblUsuario;

    @FXML private TextField txtActividad;
    @FXML private TextField txtDuracion;

    @FXML private TableView<Actividad> tablaActividades;
    @FXML private TableColumn<Actividad, Integer> colId;
    @FXML private TableColumn<Actividad, String> colActividad;
    @FXML private TableColumn<Actividad, Integer> colDuracion;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        lblUsuario.setText("Usuario: " + usuario.getNombre());
    }

    public void setActividadService(ActividadService service) throws ActividadException {
        this.service = service;
        tablaActividades.setItems(service.obtenerActividad());
    }

    @FXML
    private void initialize() {
        prefWidthColumns();
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colActividad.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getActividad()));
        colDuracion.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getDuracion()).asObject());
    }


    private void prefWidthColumns() {
        tablaActividades.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        colId.prefWidthProperty().bind(tablaActividades.widthProperty().multiply(0.05));
        colActividad.prefWidthProperty().bind(tablaActividades.widthProperty().multiply(0.35));
        colDuracion.prefWidthProperty().bind(tablaActividades.widthProperty().multiply(0.15));

        setInteres();
    }

    private void setInteres() {
        tablaActividades.setRowFactory(tv -> {
            TableRow<Actividad> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    Actividad actividad = row.getItem();
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Interes en la actividad");
                    alert.setHeaderText("Añadir interes");
                    alert.setContentText("¿Quieres mostrar interes a " + actividad.getActividad() + "?");
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            try {
                                service.addInteres(usuario.getId(), actividad);
                                tablaActividades.setItems(service.obtenerActividad());
                            } catch (ActividadException e) {
                                mostrarError(e.getMessage());
                            }
                        }
                    });
                }
            });
            return row;
        });
    }

    @FXML
    public void addActividad() {
        try {
            if (!validarCampos()) throw new ActividadException("Todos los campos son obligatorios");
            Actividad actividad = new Actividad (
                    txtActividad.getText(),
                    Integer.parseInt(txtDuracion.getText())
            );
            service.agregarActividad(actividad);
            tablaActividades.setItems(service.obtenerActividad());
            limpiarCampos();
        } catch (ActividadException | DateTimeParseException e) {
            mostrarError(e.getMessage());
        } catch (NumberFormatException e) {
            mostrarError("La duración tiene que ser un número válido.");
        }
    }

    private void limpiarCampos() {
        txtActividad.clear();
        txtDuracion.clear();
    }

    private boolean validarCampos() {
        return !txtActividad.getText().isBlank()
                && !txtDuracion.getText().isBlank();
    }

    @FXML
    public void interes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/interes.fxml"));
            Parent root = loader.load();
            InteresController controller = loader.getController();
            controller.setUsuario(usuario);
            controller.setActividadService(service);

            Stage stage = (Stage) txtActividad.getScene().getWindow();
            stage.setResizable(false);
            stage.setWidth(400);
            stage.setHeight(600);
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    public void cerrarSesion() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cerrar Sesion");
        alert.setHeaderText("¿Seguro que desea cerrar sesión?");
        alert.setContentText("Se cerrará la sesión actual");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/inicio.fxml"));
                    Parent root = loader.load();
                    InicioController controller = loader.getController();
                    controller.setUsuarioService(new UsuarioService());

                    Stage stage = (Stage) txtActividad.getScene().getWindow();
                    stage.setResizable(false);
                    stage.setWidth(800);
                    stage.setHeight(600);
                    stage.setScene(new Scene(root));
                } catch (Exception e) {
                    mostrarError(e.getMessage());
                }
            }
        });
    }

    private void mostrarError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
