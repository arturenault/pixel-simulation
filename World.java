import java.util.Random;

public class World {
  protected int size;
  protected int population;
  protected Being[][] land;
  private double[][] hostility;
  private Random gen;

  public World(int s, int initialPopulation) {
    gen = new Random();
    size = s;
    population = initialPopulation;

    land = new Being[size][size];
    hostility = new double[size][size];

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        hostility[i][j] = gen.nextDouble(); // TODO find good way of creating appropriately distributed hostilities
      }
    }

    for (int i = 0; i < initialPopulation; i++) {
      int x, y; 
      
      do {
          x = gen.nextInt(size);
          y = gen.nextInt(size);
      } while(land[x][y] != null);
     
      land[x][y] = new Being(i % 2, x, y); //TODO determine correct number of valences
    }
  }

  public String toString() {
    StringBuilder output = new StringBuilder();
    for (int i = 0; i < land.length; i++) {
      for (int j = 0; j < land[i].length; j++) {
         if (land[i][j] == null) {
           output.append("\t-\t");
         } else { 
           output.append(land[i][j]);
         }
         output.append("\t");
      }
      output.append("\n");
    }
    return output.toString();
  }
}
