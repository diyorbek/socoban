package components;

import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class SceneBackground extends BorderPane {
  public SceneBackground() {
    super();

    this.setStyle("-fx-font-family: Arial");
    this.setBackground(
      new Background(
        new BackgroundImage(
          new Image("/sprites/background.png"),
          BackgroundRepeat.REPEAT,
          BackgroundRepeat.REPEAT,
          BackgroundPosition.DEFAULT,
          BackgroundSize.DEFAULT)
      )
    );
  }
}
