package engine;

public interface IEventPortal {
  void onBoardChange(Board updatedBoard);

  void onPreviewTimeTick(int milliseconds);

  void onLevelTimeTick(int milliseconds);

  void onLoseLevel();

  void onFinishLevel(int levelNum);

  void onFinishGame();
}
