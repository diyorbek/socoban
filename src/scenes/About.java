package scenes;

import components.MenuButton;
import components.SceneBackground;
import components.Typography;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class About extends SceneBackground {
  About(Stage primaryStage) {
    var container = new VBox(30);
    var backBtn = new MenuButton("< Go Back");

    backBtn.setOnAction(e -> {
      primaryStage.getScene().setRoot(new MainMenu(primaryStage));
    });

    var TaskContainer = new VBox(10);
    var Task = new Typography("TASK", 30, Color.WHITE, 400);
    var TaskContent = new Typography("Your task in the game is to move the boxes on the screen and\n" +
      "put them in the way that is show before each level.\n" +
      "There is a time limit for each level.\n" +
      "You should finish the task before time is over!", 20, Color.BLACK, 400);
    TaskContent.setTextAlignment(TextAlignment.CENTER);
    TaskContainer.getChildren().addAll(Task, TaskContent);
    TaskContainer.setAlignment(Pos.CENTER);

    var ControlsContainer = new VBox(10);
    var Controls = new Typography("CONTROLS", 30, Color.WHITE, 400);
    var ControlsContent = new Typography(
      "Use ARROW KEYS to move around or:\n\n" +
        "w - move up\n" +
        "s - move left\n" +
        "a - move down\n" +
        "d - move right\n" +
        "e - see target board\n\n" +
        "To push or pull a box hold SHIFT key and use controls.",
      20, Color.BLACK, 400);
    ControlsContainer.getChildren().addAll(Controls, ControlsContent);
    ControlsContainer.setAlignment(Pos.CENTER);

    container.getChildren().addAll(backBtn, TaskContainer, ControlsContainer);
    container.setAlignment(Pos.CENTER);
    this.setCenter(container);
  }
}
