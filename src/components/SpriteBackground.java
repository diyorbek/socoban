package components;

import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class SpriteBackground {
  public static Background spriteBackground(Image image) {
    return new Background(
      new BackgroundImage(
        image,
        BackgroundRepeat.NO_REPEAT,
        BackgroundRepeat.NO_REPEAT,
        BackgroundPosition.DEFAULT,
        new BackgroundSize(1, 1, true, true, true, true)
      )
    );
  }
}
