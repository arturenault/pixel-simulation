import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Simulator extends JFrame {

  // Declare elements
  public static final int FRAME_WIDTH = 800;
  public static final int FRAME_HEIGHT = 850;

  private int size;
  private int population;
  private double infertility;
  private double hostility;
  private double intelligence;
  private int valences;
  private World world;
  private int fps;

  private JLabel sizeLabel;
  private JLabel populationLabel;
  private JLabel infertilityLabel;
  private JLabel hostilityLabel;
  private JLabel fpsLabel;
  private JLabel valencesLabel;
  private JLabel messageLabel;
  private JLabel intelligenceLabel;
  private JTextField sizeField;
  private JFileChooser pathChooser;
  private JTextField populationField;
  private JTextField infertilityField;
  private JTextField hostilityField;
  private JTextField fpsField;
  private JTextField valencesField;
  private JTextField intelligenceField;

  private JButton start;
  private JButton pause;
  private JButton save;
  private JPanel buttonPane;
  private JPanel controlPanel;
  private WorldPanel canvas;
  private Timer timer;

  public Simulator() {
    createElements();
    createFrame();
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setSize(FRAME_WIDTH, FRAME_HEIGHT);
    setResizable(false);
  }

  public static void main(String args[]) {
    (new Simulator()).setVisible(true);
  }

  private void createElements() { // Build elements within the frame
    messageLabel = new JLabel("");
    sizeLabel = new JLabel("Size", SwingConstants.CENTER);
    sizeField = new JTextField("100", 5);
    populationLabel = new JLabel("Population", SwingConstants.CENTER);
    populationField = new JTextField("1000", 5);
    infertilityLabel = new JLabel("Fertility", SwingConstants.CENTER);
    infertilityField = new JTextField("1", 5);
    hostilityLabel = new JLabel("Hostility", SwingConstants.CENTER);
    hostilityField = new JTextField("2", 5);
    intelligenceLabel = new JLabel("Intelligence", SwingConstants.CENTER);
    intelligenceField = new JTextField("0.5", 5);
    valencesLabel = new JLabel("Types", SwingConstants.CENTER);
    valencesField = new JTextField("3", 5);
    fpsLabel = new JLabel("FPS");
    fpsField = new JTextField("20", 5);
    pathChooser = new JFileChooser("Location");
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
          messageLabel.setText("");
          size = Integer.parseInt(sizeField.getText());

          if (size > Simulator.FRAME_WIDTH) {
            messageLabel.setText("Please pick a size between 1 and " +
                    Simulator.FRAME_WIDTH);
            return;
          }
          population = Integer.parseInt(populationField.getText());
          valences = Integer.parseInt(valencesField.getText());
          infertility = Double.parseDouble(infertilityField.getText());
          hostility = Double.parseDouble(hostilityField.getText());
          intelligence = Double.parseDouble(intelligenceField.getText());
          fps = Integer.parseInt(fpsField.getText());
          getContentPane().remove(canvas);
          world = new World(size, population, infertility, hostility,
                  intelligence, valences);
          canvas = new WorldPanel(world);
          getContentPane().add(canvas, BorderLayout.CENTER);
          revalidate();
          setSize(canvas.getPreferredSize().width, canvas.getPreferredSize()
                  .height + controlPanel.getHeight());
          timer.setDelay((int) (1000.0 / fps));
          timer.start();
        } catch (NumberFormatException e1) {
          messageLabel.setText("Please input a number greater than 0!");
        } catch (ArithmeticException e2) {
          messageLabel.setText("Please input a number greater than 0!");
        } catch (IllegalArgumentException e3) {
          messageLabel.setText("Please input a number greater than 0!");
        } catch (NegativeArraySizeException e4) {
          messageLabel.setText("Please input a number greater than 0!");
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

    save = new JButton("Save as...");
    ActionListener a4 = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (pathChooser.showSaveDialog(buttonPane) != JFileChooser
                .APPROVE_OPTION) {
          return;
        }
        File file = pathChooser.getSelectedFile();
        if (file.isDirectory()) {
          messageLabel.setText("Please select an output file");
          return;
        }
        if (file.exists()) {
          int reply = JOptionPane.showConfirmDialog(null, "Overwrite?", "File" +
                  " " +
                  "already exists", JOptionPane.YES_NO_OPTION);
          if (reply == JOptionPane.NO_OPTION) {
            return;
          }
        }
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        BufferedImage bi = new BufferedImage(width, height, BufferedImage
                .TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        canvas.paint(g);
        try {
          ImageIO.write(bi, "png", file);
        } catch (IOException e1) {
          messageLabel.setText(e1.getMessage());
        }
      }
    };
    save.addActionListener(a4);
  }

  private void createFrame() { // Puts frame together
    Container pane = getContentPane();
    pane.setLayout(new BorderLayout());

    controlPanel = new JPanel();
    controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

    JPanel labelPane = new JPanel();
    labelPane.setLayout(new GridLayout(2, 6));

    labelPane.add(sizeLabel);
    labelPane.add(populationLabel);
    labelPane.add(infertilityLabel);
    labelPane.add(hostilityLabel);
    labelPane.add(intelligenceLabel);
    labelPane.add(valencesLabel);

    labelPane.add(sizeField);
    labelPane.add(populationField);
    labelPane.add(infertilityField);
    labelPane.add(hostilityField);
    labelPane.add(intelligenceField);
    labelPane.add(valencesField);

    JPanel messagePane = new JPanel();
    messagePane.add(messageLabel);

    buttonPane = new JPanel();
    buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));

    buttonPane.add(start);
    buttonPane.add(pause);
    buttonPane.add(fpsLabel);
    buttonPane.add(fpsField);
    buttonPane.add(save);

    controlPanel.add(labelPane);
    controlPanel.add(labelPane);
    controlPanel.add(buttonPane);
    controlPanel.add(messagePane);
    controlPanel.setSize(getWidth(), 50);

    JPanel wrapper = new JPanel();
    wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.LINE_AXIS));
    wrapper.add(canvas);

    pane.add(controlPanel, BorderLayout.PAGE_START);
    pane.add(wrapper, BorderLayout.CENTER);
  }
}
