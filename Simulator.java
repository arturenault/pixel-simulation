import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Stack;
import java.awt.Color;
import javax.swing.*;
import java.awt.Graphics;

public class Simulator extends JFrame {

  // Declare elements
  public static final int FRAME_WIDTH = 800;
  public static final int FRAME_HEIGHT = 850;

  private int size;
  private int population;
  private World world;

  private JLabel sizeLabel;
  private JLabel populationLabel;
  private JTextField sizeField;
  private JTextField populationField;

  private JButton start;
  private WorldPanel canvas;
  private Timer timer;

  public Simulator() {
    createElements();
    createFrame();
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setSize(FRAME_WIDTH, FRAME_HEIGHT);
    setResizable(false);
  }

  private void createElements() { // Build elements within the frame
    sizeLabel = new JLabel("Size");
    sizeField = new JTextField("100", 5);
    populationLabel = new JLabel("Population");
    populationField = new JTextField("20", 5);
    world = new World(100, 20);
    canvas = new WorldPanel(world);


    ActionListener a1 = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        world.next();
        canvas.repaint();
      }
    };
    timer = new Timer(100, a1);

    start = new JButton("Start");
    ActionListener a2 = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          size = Integer.parseInt(sizeField.getText());
          population = Integer.parseInt(populationField.getText());
          getContentPane().remove(canvas);
          world = new World(size, population);
          canvas = new WorldPanel(world);
          getContentPane().add(canvas, BorderLayout.CENTER);
          revalidate();
          repaint();
          timer.start();
        } catch (NumberFormatException e1) {
          System.out.println("Please input a number!");
        }
      }
    };
    start.addActionListener(a2);
  }

  private void createFrame() { // Puts frame together
    Container pane = getContentPane();
    pane.setLayout(new BorderLayout());

    JPanel controlPanel = new JPanel();
    controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

    JPanel fieldPane = new JPanel();
    fieldPane.setLayout(new BoxLayout(fieldPane, BoxLayout.LINE_AXIS));

    fieldPane.add(sizeLabel);
    fieldPane.add(sizeField);
    fieldPane.add(populationLabel);
    fieldPane.add(populationField);

    JPanel buttonPane = new JPanel();
    buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));

    buttonPane.add(start);

    controlPanel.add(fieldPane);
    controlPanel.add(buttonPane);

    pane.add(controlPanel, BorderLayout.PAGE_START);
    pane.add(canvas, BorderLayout.CENTER);
  }

  void start() {
    boolean repeat = true;
    while(repeat) {
      getContentPane().remove(canvas);
      repeat = world.next();
      canvas = new WorldPanel(world);
      getContentPane().add(canvas, BorderLayout.CENTER);
      revalidate();
      repaint();
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
      }
    }
  }

  public static void main(String args[]) {
    (new Simulator()).setVisible(true);
  }
}
