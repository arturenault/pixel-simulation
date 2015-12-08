import javax.swing.*;
import java.awt.*;

public class WorldPanel extends JPanel {
  World world;
  int size;
  int pointSize;
  int canvasWidth;

  public WorldPanel(World w) {
    world = w;
    size = world.size;
    pointSize =(int)((double) Simulator.FRAME_WIDTH / size);
    canvasWidth = size * pointSize;
    setPreferredSize(new Dimension(canvasWidth, canvasWidth));
  }

  @Override
  protected void paintComponent(Graphics g) {
    //*
    setBackground(Color.black);
    for (Being b : world.beings) {
      if (b == null || b.dead) continue;

      drawBeing(b, g);
    }/*/
    testDrawHostility(g);
    //*/
    validate();
  }

  public void drawBeing(Being b, Graphics g) {
    try {
      g.setColor(new Color((float) b.strength, (float) b.fertility, (float) b.intelligence));
    } catch(IllegalArgumentException e) {
      System.out.println(b);
    }
    g.fillRect(adjust(b.x), adjust(b.y), pointSize, pointSize);
  }



  public void testDrawHostility(Graphics g) {
    for (int i = 0; i < world.hostility.length; i++) {
      for (int j = 0; j < world.hostility[i].length; j++) {
        g.setColor(new Color(world.hostility[i][j],world.hostility[i][j],
                world.hostility[i][j]));
        g.fillRect(adjust(i), adjust(j), pointSize, pointSize);
      }
    }
  }

  public int adjust(int location) {
    return location * pointSize;
  }
}
