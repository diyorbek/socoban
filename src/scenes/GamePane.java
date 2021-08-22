package scenes;

import components.*;
import engine.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GamePane extends SceneBackground implements IEventPortal {
  final Stage primaryStage;
  final Game game;
  final Background grassBackground = SpriteBackground.spriteBackground(new Image("/sprites/grass.png"));
  final Background boxBackground = SpriteBackground.spriteBackground(new Image("/sprites/box.png"));
  final Background wallBackground = SpriteBackground.spriteBackground(new Image("/sprites/wall.png"));
  final Background playerBackground = SpriteBackground.spriteBackground(new Image("/sprites/man.png"));
  final StackPane playerSprite = createplayerSprite();
  boolean isBoardInitialized = false;

  GridPane boardGrid = new GridPane();
  StackPane canvasStack = new StackPane();
  Typography levelText = new Typography("Level", 50, Color.WHITE);
  Typography timerText = new Typography("00:00", 50, Color.WHITE);
  Typography previewTimerText = new Typography("", 200, Color.rgb(255, 0, 0, .5), 800);

  private StackPane createplayerSprite() {
    var playerSprite = new StackPane();
    var background = new Pane();
    var player = new Pane();

    background.setBackground(grassBackground);
    player.setBackground(playerBackground);
    playerSprite.getChildren().addAll(background, player);

    return playerSprite;
  }

  GamePane(Stage primaryStage) {
    this.primaryStage = primaryStage;
    game = new Game(this);


    levelText.setText("Level " + game.getCurrentLevelNum());

    var StatusBar = new BorderPane();
    StatusBar.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, .2), null, null)));
    StatusBar.setPadding(new Insets(15));
    StatusBar.setMaxHeight(60);
    StatusBar.setLeft(levelText);
    StatusBar.setRight(timerText);

//        boardGrid.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, .2), null, null)));

    canvasStack.setAlignment(Pos.CENTER);
    canvasStack.getChildren().addAll(boardGrid, previewTimerText);
    this.setTop(StatusBar);
    this.setCenter(canvasStack);

    game.initGame(1);
  }

  public void executeUserCommand(char command) {
    game.executeUserCommand(command);
  }

  private void initializeBoardGrid(int rows, int columns) {
    boardGrid.getChildren().clear();

    for (int row = 0; row < rows; row++) {
      for (int column = 0; column < columns; column++) {
        var cell = new Pane();

        if (rows == columns) {
          cell.prefWidthProperty().bind(this.widthProperty().divide(columns));
          cell.prefHeightProperty().bind(this.widthProperty().divide(row));
        } else if (rows < columns) {
          cell.prefWidthProperty().bind(this.widthProperty().divide(columns));
          cell.prefHeightProperty().bind(this.widthProperty().divide(columns));
        } else {
          cell.prefWidthProperty().bind(this.heightProperty().divide(rows));
          cell.prefHeightProperty().bind(this.heightProperty().divide(rows));
        }

        boardGrid.add(cell, column, row);
      }
    }
  }

  @Override
  public void onBoardChange(Board updatedBoard) {
    var matrix = updatedBoard.getBoard();
    int rows = matrix.length;
    int columns = matrix[0].length;

    int columnMargin = rows - columns > 1 ? (rows - columns) / 2 : 0;
    int rowMargin = columns - rows > 1 ? (columns - rows) / 2 : 0;

    if (!isBoardInitialized) {
      // Create bigger grid to align the board
      initializeBoardGrid(rows + rowMargin, columns + columnMargin);
    }

    var len = boardGrid.getChildren().size();
    for (int i = 0; i < len; i++) {
      var cell = (Pane) (boardGrid.getChildren().get(i));
      int col = GridPane.getColumnIndex(cell) - columnMargin;
      int row = GridPane.getRowIndex(cell) - rowMargin;

      if (col < 0 || row < 0) continue;

      EntityType type = matrix[row][col].getType();

      switch (type) {
        case PLAYER -> {
          cell.getChildren().clear();
          playerSprite.prefWidthProperty().bind(cell.widthProperty());
          playerSprite.prefHeightProperty().bind(cell.widthProperty());
          cell.getChildren().add(playerSprite);
        }
        case OBSTACLE -> {
          var obstacle = (Obstacle) (matrix[row][col]);
          var obstacleType = obstacle.getObstacleType();

          if (obstacleType == ObstacleType.BOX) {
            cell.setBackground(boxBackground);
          } else {
            cell.setBackground(wallBackground);
          }
        }
        case EMPTY_SPOT -> {
          cell.setBackground(grassBackground);
        }
      }
    }
  }

  @Override
  public void onPreviewTimeTick(int milliseconds) {
    if (milliseconds > 0) {
      previewTimerText.setVisible(true);
      previewTimerText.setText(String.valueOf(milliseconds / 1000));
    } else {
      previewTimerText.setText("");
      previewTimerText.setVisible(false);
    }
  }

  @Override
  public void onLevelTimeTick(int currentLevelTimeLimit) {
    timerText.setFill(currentLevelTimeLimit <= 10_000 ? Color.RED : Color.WHITE);

    if (currentLevelTimeLimit < 0) return;

    int minutes = currentLevelTimeLimit / 1000 / 60;
    int seconds = (currentLevelTimeLimit / 1000) % 60;
    String formattedMinutes = minutes / 10 > 0 ? String.valueOf(minutes) : "0" + minutes;
    String formattedSeconds = seconds / 10 > 0 ? String.valueOf(seconds) : "0" + seconds;

    timerText.setText(formattedMinutes + ":" + formattedSeconds);
  }

  @Override
  public void onLoseLevel() {
    var dialogBox = new DialogBox("TIME IS OUT!", Color.RED);

    var restartBtn = new MenuButton("Restart");
    restartBtn.setOnAction(e -> {
      game.initGame(1);
      canvasStack.getChildren().removeAll(dialogBox);
    });

    var menuBtn = new MenuButton("Main Menu");
    menuBtn.setOnAction(e -> {
      primaryStage.setScene(new Scene(new MainMenu(primaryStage), 800, 800));
    });

    dialogBox.content.getChildren().addAll(
      restartBtn,
      menuBtn
    );

    canvasStack.getChildren().add(dialogBox);
  }

  @Override
  public void onFinishLevel(int levelNum) {
    isBoardInitialized = false;

    var dialogBox = new DialogBox("Well Done!", Color.GREENYELLOW);

    var nextLevelBtn = new MenuButton("Next Level");
    nextLevelBtn.setOnAction(e -> {
      game.initGame(levelNum + 1);
      levelText.setText("Level " + game.getCurrentLevelNum());
      canvasStack.getChildren().removeAll(dialogBox);
    });

    var menuBtn = new MenuButton("Main Menu");
    menuBtn.setOnAction(e -> {
      primaryStage.setScene(new Scene(new MainMenu(primaryStage), 800, 800));
    });

    dialogBox.content.getChildren().addAll(nextLevelBtn, menuBtn);
    canvasStack.getChildren().add(dialogBox);
  }

  @Override
  public void onFinishGame() {
    var dialogBox = new DialogBox("Congratulations!", Color.LIGHTSKYBLUE);
    var text = new Typography("You have completed all the levels!", 20);
    var menuBtn = new MenuButton("Main Menu");

    menuBtn.setOnAction(e -> {
      primaryStage.setScene(new Scene(new MainMenu(primaryStage), 800, 800));
    });

    dialogBox.content.getChildren().addAll(text, menuBtn);
    canvasStack.getChildren().add(dialogBox);
  }
}
