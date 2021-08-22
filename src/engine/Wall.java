package engine;

public class Wall extends Obstacle {
  Wall() {
    super(ObstacleType.WALL, false, '=');
  }
}
