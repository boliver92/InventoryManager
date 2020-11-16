import javafx.application.Application;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class MainForm extends Application {

    /**
     * Required method to launch the MainForm.fxml. Called by launch(args) in main.
     * @param   stage       Stage passed by main method.
     * @throws  IOException if MainForm.fxml can not be loaded
     */
    public void start(Stage stage) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("MainForm.fxml"));

        Scene scene = new Scene(parent);

        stage.setTitle("Inventory Manager");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * main function
     * @param args no args provided
     */
    public static void main(String[] args){
        launch(args);
    }

}
