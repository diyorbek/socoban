package level;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Level {
  private final char[][] map;
  private final int timeLimit;

  Level(String sourceFilePath) throws IOException {
    FileInputStream fileInputStream = new FileInputStream(sourceFilePath);
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

    // Read first line to get map dimensions and initialize map
    String lineString = bufferedReader.readLine();
    String[] mapInfo = lineString.split(" ");

    int mapWidth = Integer.parseInt(mapInfo[0]);
    int mapHeight = Integer.parseInt(mapInfo[1]);
    timeLimit = Integer.parseInt(mapInfo[2]);

    map = new char[mapHeight][mapWidth];

    // Copy rest of the text file to map
    int currentLine = 0;
    while ((lineString = bufferedReader.readLine()) != null) {
      map[currentLine++] = lineString.toCharArray();
    }

    fileInputStream.close();
  }

  public char[][] getMap() {
    return map;
  }

  public int getTimeLimit() {
    return timeLimit;
  }
}
