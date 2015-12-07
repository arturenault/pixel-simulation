import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class WorldPanel extends JPanel {
  World world;
  int size;
  int pointSize;

  public WorldPanel(World w) {
    world = w;
    size = world.size;
    pointSize = (int) ((float)Simulator.FRAME_WIDTH / size);
  }

  @Override
  protected void paintComponent(Graphics g) {
    //*
    setBackground(Color.black);
    g.setColor(getBackground());
    g.fillRect(0,0, Simulator.FRAME_WIDTH, Simulator.FRAME_WIDTH);
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
    return (int) (location / (double) size * Simulator.FRAME_WIDTH);
  }
}
