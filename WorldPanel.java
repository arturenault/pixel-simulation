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
    Being[][] beings = world.land;  
    for (int i = 0; i < beings.length; i++) {
      for (int j = 0; j < beings[i].length; j++) {
        if(beings[i][j] != null) {
          drawBeing(beings[i][j], g);
        }  
      }
    }
    validate();
  }

  public void drawBeing(Being b, Graphics g) {
    g.setColor(new Color((float) b.strength, (float) b.fertility, (float) b.creativity));
    g.fillRect(adjust(b.x), adjust(b.y), pointSize, pointSize);
  }

  public int adjust(int location) {
    int x = (int) (location / (double) size * Simulator.FRAME_WIDTH);
    return x;
  }
}
