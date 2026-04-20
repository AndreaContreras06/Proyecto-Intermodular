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

public class InteresController {

    private Usuario usuario;
    private ActividadService service;

    @FXML
    private TableView<Actividad> tablaInteres;
    @FXML
    private TableColumn<Actividad, Integer> colId;
    @FXML
    private TableColumn<Actividad, String> colActividad;
    @FXML
    private TableColumn<Actividad, Integer> colDuracion;
    @FXML
    private TableColumn<Actividad, Integer> colInteres;

    @FXML
    private Label lblUsuario;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        lblUsuario.setText("Usuario: " + usuario.getNombre());
    }

    public void setActividadService(ActividadService service) throws ActividadException {
        this.service = service;

        tablaInteres.setItems(service.obtenerActividadPorUsuario(usuario.getId()));
    }

    @FXML
    private void initialize() {
        prefWidthColumns();

        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colActividad.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getActividad()));
        colDuracion.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getDuracion()).asObject());
        colInteres.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getInteres()).asObject());

    }

    private void prefWidthColumns() {
        tablaInteres.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        colId.prefWidthProperty().bind(tablaInteres.widthProperty().multiply(0.05));
        colActividad.prefWidthProperty().bind(tablaInteres.widthProperty().multiply(0.35));
        colDuracion.prefWidthProperty().bind(tablaInteres.widthProperty().multiply(0.10));
        colInteres.prefWidthProperty().bind(tablaInteres.widthProperty().multiply(0.05));
    }

    private void setInteres() {
        tablaInteres.setRowFactory(tv -> {
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
                                tablaInteres.setItems(service.obtenerActividadPorUsuario(usuario.getId()));
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
    public void actividad() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/actividades.fxml"));

            Parent root = loader.load();

            ActividadController controller = loader.getController();
            controller.setUsuario(usuario);
            controller.setActividadService(service);

            Stage stage = (Stage) lblUsuario.getScene().getWindow();
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

                    Stage stage = (Stage) tablaInteres.getScene().getWindow();
                    stage.setResizable(false);
                    stage.setWidth(400);
                    stage.setHeight(600);
                    stage.setScene(new Scene(root));
                } catch (Exception e) {
                    mostrarError(e.getMessage());
                }
            }
        });
    }
}
