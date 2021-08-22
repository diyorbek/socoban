package engine;

import javafx.application.Platform;
import level.Level;
import level.LevelCollection;

import java.util.Timer;
import java.util.TimerTask;

public class Game {
  private Board board;
  private Board activeBoard;
  private Board targetBoard;
  private Player player;
  private boolean isTargetVisible = false;
  private final char PLAYER_AVATAR = '█';

  private final LevelCollection LEVEL_COLLECTION = new LevelCollection();
  private int currentLevelNum = 1;

  private Timer CURRENT_LEVEL_TIMER;
  private final int CURRENT_LEVEL_TIMER_PERIOD = 1000;
  private int currentLevelTimeLimit; // milliseconds

  private Timer MAIN_LOOP_TIMER;
  private final int MAIN_LOOP_TIMER_DELAY = 3000; // milliseconds

  private final IEventPortal Events;

  public Game(IEventPortal events) {
    Events = events;
  }

  public int getCurrentLevelNum() {
    return currentLevelNum;
  }

  public void initGame(int levelNum) {
    currentLevelNum = levelNum;
    Level currentLevel = LEVEL_COLLECTION.getLevel(levelNum);

    targetBoard = Board.createBoard(currentLevel.getMap());
    board = Board.createBoard(currentLevel.getMap());
    activeBoard = board;

    player = new Player(PLAYER_AVATAR, activeBoard);

    Board.randomizeObstacles(activeBoard);

    // Displays preview until `MAIN_LOOP_TIMER_DELAY` ends
    previewCurrentLevelTarget();

    // Start level timer earlier
    // with margin of MAIN_LOOP_TIMER_DELAY
    // to initialize timer info
    currentLevelTimeLimit = currentLevel.getTimeLimit() * 1000 + MAIN_LOOP_TIMER_DELAY;
    CURRENT_LEVEL_TIMER = new Timer();
    CURRENT_LEVEL_TIMER.schedule(new CurrentLevelTimerLoop(), 0, CURRENT_LEVEL_TIMER_PERIOD);

    // Start displaying level with randomized board after delay
    // Add 10ms extra margin to catch up with CURRENT_LEVEL_TIMER's latest updates
    MAIN_LOOP_TIMER = new Timer();
    MAIN_LOOP_TIMER.schedule(new TimerTask() {
      public void run() {
        displayBoard(activeBoard);
      }
    }, MAIN_LOOP_TIMER_DELAY + 10);
  }

  public void previewCurrentLevelTarget() {
    Events.onBoardChange(targetBoard);
  }

  public void finishCurrentLevel() {
    stopCurrentLevelTimer();
    MAIN_LOOP_TIMER.cancel();
    MAIN_LOOP_TIMER.purge();

    // Finish game after completing last level
    if (currentLevelNum + 1 > LEVEL_COLLECTION.getLength()) {
      Events.onFinishGame();
      return;
    }

    Events.onFinishLevel(currentLevelNum);
  }

  public void loseCurrentLevel() {
    Events.onLoseLevel();
  }

  public void stopCurrentLevelTimer() {
    CURRENT_LEVEL_TIMER.cancel();
    CURRENT_LEVEL_TIMER.purge();
  }

  public void displayBoard(Board currentBoard) {
    Platform.runLater(() -> Events.onBoardChange(currentBoard));

    if (Board.matches(activeBoard, targetBoard, PLAYER_AVATAR)) {
      finishCurrentLevel();
    }
  }

  public void executeUserCommand(char input) {
    if (currentLevelTimeLimit < 0) return;

    switch (input) {
      case 'd':
        player.moveRight();
        break;

      case 'a':
        player.moveLeft();
        break;

      case 'w':
        player.moveUp();
        break;

      case 's':
        player.moveDown();
        break;

      case 'D':
        player.pushRight();
        break;

      case 'A':
        player.pushLeft();
        break;

      case 'W':
        player.pushUp();
        break;

      case 'S':
        player.pushDown();
        break;

      case 'e':
        isTargetVisible = true;
        break;

      case 'E':
        isTargetVisible = false;
        break;

      default:
        break;
    }

    displayBoard(isTargetVisible ? targetBoard : activeBoard);
  }

  class CurrentLevelTimerLoop extends TimerTask {
    private int previewThreshold = MAIN_LOOP_TIMER_DELAY;

    public void run() {
      if (previewThreshold >= 0) {
        Events.onPreviewTimeTick(previewThreshold);
        previewThreshold -= CURRENT_LEVEL_TIMER_PERIOD;
      }

      if (previewThreshold < 0) {
        Events.onLevelTimeTick(currentLevelTimeLimit);
      }

      if (currentLevelTimeLimit < 0) {
        stopCurrentLevelTimer();
        Platform.runLater(Game.this::loseCurrentLevel);

        return;
      }

      currentLevelTimeLimit -= CURRENT_LEVEL_TIMER_PERIOD;
    }
  }
}