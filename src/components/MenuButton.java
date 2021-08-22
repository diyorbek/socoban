package components;

import javafx.scene.control.Button;

public class MenuButton extends Button {
  public MenuButton(String label) {
    super(label);
    this.setPrefHeight(60);
    this.setPrefWidth(200);
    this.setStyle("-fx-font-size: 24");
  }
}
