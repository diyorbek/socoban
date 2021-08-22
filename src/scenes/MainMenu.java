package scenes;

import components.MenuButton;
import components.SceneBackground;
import javafx.geometry.Pos;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainMenu extends SceneBackground {
  MainMenu(Stage primaryStage) {
    var mainMenu = new VBox(30);
    mainMenu.setAlignment(Pos.CENTER);

    var startBtn = new MenuButton("Start");
    startBtn.setOnAction(e -> {
      primaryStage.setScene(GameScene.getScene(primaryStage));
    });

    var about = new MenuButton("Instructions");
    about.setOnAction(e -> {
      primaryStage.getScene().setRoot(new About(primaryStage));
    });

    var title = new Text("SOCOBAN");
    title.setStyle("-fx-font-size: 100; -fx-font-weight: 800;-fx-text-fill: white");
    title.setLayoutY(200);
    title.setLayoutX(140);
    title.setFill(Color.WHITE);
    InnerShadow innerShadow = new InnerShadow();
    title.setEffect(innerShadow);

    mainMenu.getChildren().addAll(startBtn, about);

    this.getChildren().addAll(title);
    this.setCenter(mainMenu);
  }
}
