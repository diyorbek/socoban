package scenes;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Socoban");
    primaryStage.setScene(new Scene(new MainMenu(primaryStage), 800, 800));
    primaryStage.show();
    primaryStage.setResizable(false);
  }


  public static void main(String[] args) {
    launch(args);
  }
}
