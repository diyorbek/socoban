package components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class DialogBox extends VBox {
  public VBox content = new VBox(10);

  public DialogBox(String title, Color borderColor) {
    content.setAlignment(Pos.CENTER);
    content.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), null)));
    content.setPadding(new Insets(30));
    content.setBorder(
      new Border(new BorderStroke(borderColor, BorderStrokeStyle.SOLID, new CornerRadii(10), BorderStroke.MEDIUM)));
    content.getChildren().add(new Typography(title, 50, 800));

    this.getChildren().add(content);
    this.setAlignment(Pos.CENTER);
  }
}
