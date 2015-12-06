import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import javax.swing.JPanel;

public class WorldPanel extends JPanel {
  World world;
  int size;
  int pointSize;

  public WorldPanel(World w) {
    world = w;
    size = world.size;
    pointSize = Simulator.FRAME_WIDTH / size;
  }

  @Override
  protected void paintComponent(Graphics g) {
    setBackground(Color.black);
    g.setColor(getBackground());
    g.fillRect(0,0, Simulator.FRAME_WIDTH, Simulator.FRAME_WIDTH);
    for (Being b : world.beings) {
      if (b.dead) continue;

      drawBeing(b, g);
    }
    validate();
  }

  public void drawBeing(Being b, Graphics g) {
    try {
      g.setColor(new Color((float) b.strength, (float) b.fertility, (float) b.creativity));
    } catch(IllegalArgumentException e) {
      System.out.println(b);
    }
    g.fillRect(adjust(b.x), adjust(b.y), pointSize, pointSize);
  }

  public void testBlue(Graphics g) {
    g.setColor(Color.blue);
    g.fillRect(0,0, 100, 100);
  }

  public int adjust(int location) {
    return (int) (location / (double) size * Simulator.FRAME_WIDTH);
  }
}
