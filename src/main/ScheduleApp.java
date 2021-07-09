package main;
/**
 * @author Dan Adams
 * @author dada141@wgu.edu
 * @version 1.0
 * @since 1.0
 *
 */

import dao.UserDao;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.User;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Application start - loads the login.fxml page
 */
public class ScheduleApp extends Application {

    public static void main(String[] args) {

        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
