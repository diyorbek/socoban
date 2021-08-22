package level;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LevelCollection {
  private final Level[] levels;
  private final int length;

  public LevelCollection() {
    String projectFolderPath = LevelCollection.class.getProtectionDomain().getCodeSource().getLocation().getFile();
    File folder = new File(projectFolderPath + "/level");
    File[] files = folder.listFiles();

    // Count .txt files
    int filesCount = 0;
    for (int i = 0; i < Objects.requireNonNull(files).length; i++) {
      File file = files[i];
      if (file.getName().endsWith(".txt")) {
        filesCount++;
      }
    }

    levels = new Level[filesCount];

    for (int i = 0; i < files.length; i++) {
      File file = files[i];

      if (file.getName().endsWith(".txt")) {
        Pattern p = Pattern.compile("\\d+"); // Matches files with digits
        Matcher m = p.matcher(file.getName());

        // Sava level with its corresponding index number
        if (m.find()) {
          int j = Integer.parseInt(m.group()) - 1;

          try {
            levels[j] = new Level(file.getPath());
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }

    length = levels.length;
  }

  // Level number starts from 1
  public Level getLevel(int n) {
    return levels[n - 1];
  }

  public int getLength() {
    return length;
  }
}
