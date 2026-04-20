package dam.code;

import dam.code.controller.InicioController;
import dam.code.dao.UsuarioDAO;
import dam.code.dao.impl.UsuarioDAOImpl;
import dam.code.service.UsuarioService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppGym extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        UsuarioService service = new UsuarioService();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/inicio.fxml"));

        Parent root = loader.load();

        InicioController controller = loader.getController();
        controller.setUsuarioService(service);

        stage.setScene(new Scene(root));
        stage.setTitle("GymPrometeo");
        stage.setResizable(false);
        stage.setWidth(400);
        stage.setHeight(600);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

