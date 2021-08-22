package engine;

public class Board {
  // Properties of board where entities are kept
  public int width;
  public int height;
  private Entity[][] board;

  // Properties of display matrix used for printing
  private int matrixWidth;
  private int matrixHeight;
  private char[][] matrix;

  private final char BLANK_CELL = ' ';
  private final char HORIZONTAL_BORDER = '─';
  private final char VERTICAL_BORDER = '│';
  private final char TOP_LEFT_BORDER = '┌';
  private final char TOP_RIGHT_BORDER = '┐';
  private final char BOTTOM_LEFT_BORDER = '└';
  private final char BOTTOM_RIGHT_BORDER = '┘';

  Board(int width, int height) {
    this.width = width;
    this.height = height;

    initializeBoard();
    clearMatrix();
  }

  private void initializeBoard() {
    board = new Entity[height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        board[i][j] = new EmptySpot();
      }
    }
  }

  public boolean isPositionEmpty(int x, int y) {
    return isInsideBoard(x, y) && board[y][x].getType() == EntityType.EMPTY_SPOT;
  }

  public boolean isInsideBoard(int x, int y) {
    return x >= 0 && y >= 0 && x < width && y < height;
  }

  public Entity select(int x, int y) {
    if (isInsideBoard(x, y)) {
      return board[y][x];
    }
    return null;
  }

  public boolean insert(int x, int y, Entity entity) {
    if (isPositionEmpty(x, y)) {
      board[y][x] = entity;
      return true;
    }
    System.out.println("Error setting entity.");
    return false;
  }

  public boolean erase(int x, int y) {
    if (isInsideBoard(x, y)) {
      board[y][x] = new EmptySpot();
      return true;
    }
    System.out.println("Error erasing entity.");
    return false;
  }

  private boolean canMoveEntity(Entity entity) {
    return entity != null && !(entity instanceof Obstacle);
  }

  // Moves entity by swapping
  public boolean moveEntityTo(int originX, int originY, int targetX, int targetY) {
    Entity origin = select(originX, originY);
    Entity target = select(targetX, targetY);

    if ((origin != null) && canMoveEntity(target)) {
      board[originY][originX] = target;
      board[targetY][targetX] = origin;

      return true;
    }

    return false;
  }

  public boolean moveEntityRight(int x, int y) {
    return moveEntityTo(x, y, x + 1, y);
  }

  public boolean moveEntityLeft(int x, int y) {
    return moveEntityTo(x, y, x - 1, y);
  }

  public boolean moveEntityUp(int x, int y) {
    return moveEntityTo(x, y, x, y - 1);
  }

  public boolean moveEntityDown(int x, int y) {
    return moveEntityTo(x, y, x, y + 1);
  }

  private void clearMatrix() {
    initializeMatrix();
    drawBorders();
  }

  private void initializeMatrix() {
    matrixWidth = width + 2;
    matrixHeight = height + 2;
    matrix = new char[matrixHeight][matrixWidth];

    for (int i = 0; i < matrixHeight; i++) {
      for (int j = 0; j < matrixWidth; j++) {
        matrix[i][j] = BLANK_CELL;
      }
    }
  }

  private void drawBorders() {
    matrix[0][0] = TOP_LEFT_BORDER;
    matrix[0][matrixWidth - 1] = TOP_RIGHT_BORDER;

    for (int i = 1; i < matrixWidth - 1; i++) {
      matrix[0][i] = HORIZONTAL_BORDER;
      matrix[matrixHeight - 1][i] = HORIZONTAL_BORDER;
    }

    matrix[matrixHeight - 1][0] = BOTTOM_LEFT_BORDER;
    matrix[matrixHeight - 1][matrixWidth - 1] = BOTTOM_RIGHT_BORDER;

    for (int i = 1; i < matrixHeight - 1; i++) {
      matrix[i][0] = VERTICAL_BORDER;
      matrix[i][matrixWidth - 1] = VERTICAL_BORDER;
    }
  }

  // Extract display values of entities to matrix
  private void mirrorBoardToMatrix() {
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        matrix[i + 1][j + 1] = board[i][j].toChar();
      }
    }
  }

  public Entity[][] getBoard() {
    return board;
  }

  public void display() {
    mirrorBoardToMatrix();

    for (int i = 0; i < matrixHeight; i++) {
      for (int j = 0; j < matrixWidth; j++) {
        System.out.print(matrix[i][j]);
      }
      System.out.println();
    }
  }

  // Creates board with given map(matrix) of entities
  static public Board createBoard(char[][] map) {
    Board board = new Board(map[0].length, map.length);

    for (int y = 0; y < board.height; y++) {
      for (int x = 0; x < board.width; x++) {
        if (map[y][x] == '#') {
          board.insert(x, y, new Box());
        } else if (map[y][x] == '=') {
          board.insert(x, y, new Wall());
        }
      }
    }

    return board;
  }

  // Randomize the places of exiting obstacles in the board
  static public void randomizeObstacles(Board board) {
    int boxCount = 0;

    for (int y = 0; y < board.height; y++) {
      for (int x = 0; x < board.width; x++) {
        if (board.select(x, y) instanceof Box) {
          boxCount++;
          board.erase(x, y);
        }
      }
    }

    Box.randomlyPlaceOnBoard(board, boxCount);
  }

  // Compare matrices of 2 boards
  static public boolean matches(Board board1, Board board2, char exclude) {
    board1.mirrorBoardToMatrix();
    board2.mirrorBoardToMatrix();

    char[][] boardMap1 = board1.matrix;
    char[][] boardMap2 = board2.matrix;

    for (int i = 1; i <= board1.height; i++) {
      for (int j = 1; j <= board1.width; j++) {
        if (boardMap1[i][j] == exclude || boardMap2[i][j] == exclude) {
          continue;
        }

        if (boardMap1[i][j] != boardMap2[i][j]) {
          return false;
        }
      }
    }

    return true;
  }
}
