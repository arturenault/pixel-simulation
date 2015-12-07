import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Simulator extends JFrame {

  // Declare elements
  public static final int FRAME_WIDTH = 800;
  public static final int FRAME_HEIGHT = 850;

  private int size;
  private int population;
  private float infertility;
  private float hostility;
  private int valences;
  private World world;
  private int fps;

  private JLabel sizeLabel;
  private JLabel populationLabel;
  private JLabel infertilityLabel;
  private JLabel hostilityLabel;
  private JLabel fpsLabel;
  private JLabel valencesLabel;
  private JTextField sizeField;
  private JTextField populationField;
  private JTextField infertilityField;
  private JTextField hostilityField;
  private JTextField fpsField;
  private JTextField valencesField;

  private JButton start;
  private JButton pause;
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
    populationField = new JTextField("1000", 5);
    infertilityLabel = new JLabel("Fertility");
    infertilityField = new JTextField("1", 5);
    hostilityLabel = new JLabel("Hostility");
    hostilityField = new JTextField("2", 5);
    valencesLabel = new JLabel("Types");
    valencesField = new JTextField("3", 5);
    fpsLabel = new JLabel("FPS");
    fpsField = new JTextField("20", 5);
    world = new World();
    canvas = new WorldPanel(world);
    ActionListener a1 = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        world.next();
        canvas.repaint();
      }
    };
    timer = new Timer(0,a1);

    start = new JButton("Start");
    ActionListener a2 = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          size = Integer.parseInt(sizeField.getText());
          population = Integer.parseInt(populationField.getText());
          valences = Integer.parseInt(valencesField.getText());
          infertility = Float.parseFloat(infertilityField.getText());
          hostility = Float.parseFloat(hostilityField.getText());
          fps = Integer.parseInt(fpsField.getText());
          getContentPane().remove(canvas);
          world = new World(size, population, infertility, hostility,
                  valences);
          canvas = new WorldPanel(world);
          getContentPane().add(canvas, BorderLayout.CENTER);
          revalidate();
          repaint();
          timer.setDelay((int) (1000.0/fps));
          timer.start();
        } catch (NumberFormatException e1) {
          System.out.println("Please input a number!");
        }
      }
    };
    start.addActionListener(a2);

    pause = new JButton("Pause");
    ActionListener a3 = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (timer.isRunning()) {
          timer.stop();
        } else {
          fps = Integer.parseInt(fpsField.getText());
          timer.setDelay((int) (1000.0 / fps));
          timer.start();
        }
      }
    };
    pause.addActionListener(a3);
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
    fieldPane.add(infertilityLabel);
    fieldPane.add(infertilityField);
    fieldPane.add(hostilityLabel);
    fieldPane.add(hostilityField);
    fieldPane.add(valencesLabel);
    fieldPane.add(valencesField);

    JPanel buttonPane = new JPanel();
    buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));

    buttonPane.add(Box.createHorizontalGlue());
    buttonPane.add(start);
    buttonPane.add(pause);
    buttonPane.add(fpsLabel);
    buttonPane.add(fpsField);
    buttonPane.add(Box.createHorizontalGlue());

    controlPanel.add(fieldPane);
    controlPanel.add(buttonPane);

    pane.add(controlPanel, BorderLayout.PAGE_START);
    pane.add(canvas, BorderLayout.CENTER);
  }

  public static void main(String args[]) {
    (new Simulator()).setVisible(true);
  }
}
