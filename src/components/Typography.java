package components;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Typography extends Text {
  public Typography(String label, int fontSize) {
    super(label);
    this.setFont(Font.font("Arial", FontWeight.findByWeight(400), fontSize));
  }

  public Typography(String label, int fontSize, Color color) {
    super(label);
    this.setFont(Font.font("Arial", FontWeight.findByWeight(400), fontSize));
    this.setFill(color);
  }

  public Typography(String label, int fontSize, int fontWeight) {
    super(label);
    this.setFont(Font.font("Arial", FontWeight.findByWeight(fontWeight), fontSize));
  }

  public Typography(String label, int fontSize, Color color, int fontWeight) {
    super(label);
    this.setFont(Font.font("Arial", FontWeight.findByWeight(fontWeight), fontSize));
    this.setFill(color);
  }
}
