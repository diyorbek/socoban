package scenes;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameScene {
  public static Scene getScene(Stage primaryStage) {
    var gamePane = new GamePane(primaryStage);
    var scene = new Scene(gamePane, 800, 860);

    scene.setOnKeyPressed(event -> {
      switch (event.getCode()) {
        case W, UP -> gamePane.executeUserCommand(event.isShiftDown() ? 'W' : 'w');
        case S, DOWN -> gamePane.executeUserCommand(event.isShiftDown() ? 'S' : 's');
        case A, LEFT -> gamePane.executeUserCommand(event.isShiftDown() ? 'A' : 'a');
        case D, RIGHT -> gamePane.executeUserCommand(event.isShiftDown() ? 'D' : 'd');
        case E -> gamePane.executeUserCommand('e');
      }
    });

    scene.setOnKeyReleased(event -> {
      switch (event.getCode()) {
        case E -> gamePane.executeUserCommand('E');
      }
    });

    return scene;
  }
}
