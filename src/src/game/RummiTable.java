package game;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class RummiTable {
  private Map<Coordinate, Stone> grid;
  private final static int height = 20;
  private final static int width = 20;

  public RummiTable(){
    grid = new HashMap<>();
  }

  public Map<Coordinate, Stone> getStones(){
    return grid;
  }

  public void setStone(Coordinate coordinate, Stone stone){
    grid.put(coordinate, stone);
  }

  public void clear(){
    grid.clear();
  }

  public boolean isConsistent(){
     for(Entry<Coordinate, Stone> entry : grid.entrySet()){
        if (isInRun(entry) == false){
          return false;
        }
     }
    return true;
  }


  // This has to be done easier. Cnsider this as a first idea.
  // This method wont work since it checks if it is a ColorGroup AND a NumberGroup...
  private boolean isInRun(Entry<Coordinate, Stone> entry){
    int colorCombo = 1;
    int numberCombo = 1;
    int x = entry.getKey().getCol();
    int y = entry.getKey().getRow();
    int stoneNumber = entry.getValue().getNumber();
    Stone.Color stoneColor = entry.getValue().getColor();

    //Number run to the left
    if (grid.containsKey(new Coordinate(x-1, y))){
      if (grid.get(new Coordinate(x-1, y)).getNumber() == stoneNumber -1 || grid.get(new Coordinate(x-1, y)).getColor() == Stone.Color.JOKER){
        numberCombo += 1;
        if (grid.containsKey(new Coordinate(x-2, y))) {
          if (grid.get(new Coordinate(x - 2, y)).getNumber() == stoneNumber - 2 || grid.get(new Coordinate(x-2, y)).getColor() == Stone.Color.JOKER) {
            numberCombo += 1;
          }
          else {
            return false;
          }
        }
      }
      else {
        return false;
      }
    }

    //Numercombo to the right.
    if (grid.containsKey(new Coordinate(x + 1, y))){
      if (grid.get(new Coordinate(x+1, y)).getNumber() == stoneNumber + 1 || grid.get(new Coordinate(x+1, y)).getColor() == Stone.Color.JOKER){
        numberCombo += 1;
        if (grid.containsKey(new Coordinate(x + 2, y))) {
          if (grid.get(new Coordinate(x + 2, y)).getNumber() == stoneNumber + 2 || grid.get(new Coordinate(x+2, y)).getColor() == Stone.Color.JOKER) {
            numberCombo += 1;
          }
          else {
            return false;
          }
        }
      }
      else {
        return false;
      }
    }

    //Colorcombo to the left
    if (grid.containsKey(new Coordinate(x-1, y))){
      if (grid.get(new Coordinate(x-1, y)).getNumber() == stoneNumber && grid.get(new Coordinate(x-1, y)).getColor() != stoneColor){
        colorCombo +=1;
        if (grid.containsKey(new Coordinate(x-2, y))){
          if (grid.get(new Coordinate(x-2, y)).getNumber() == stoneNumber && grid.get(new Coordinate(x-2, y)).getColor() != stoneColor && grid.get(new Coordinate(x-1, y)).getColor() != grid.get(new Coordinate(x-2, y)).getColor()){
            colorCombo +=1;
            if (grid.containsKey(new Coordinate(x-3, y))){
              if (grid.get(new Coordinate(x-3, y)).getNumber() == stoneNumber && grid.get(new Coordinate(x-3, y)).getColor() != stoneColor && grid.get(new Coordinate(x-3, y)).getColor() != grid.get(new Coordinate(x-2, y)).getColor() && grid.get(new Coordinate(x-3, y)).getColor() != grid.get(new Coordinate(x-1, y)).getColor()){
                colorCombo +=1;
                if (grid.containsKey(new Coordinate(x-4, y))){
                  return false;
                }
              } else {
                return false;
              }

            } else {
              return false;
            }
          } else {
            return false;
          }
        }
      } else {
        return false;
      }
    }

    //Colorcombo to the right
    if (grid.containsKey(new Coordinate(x+1, y))){
      if (grid.get(new Coordinate(x+1, y)).getNumber() == stoneNumber && grid.get(new Coordinate(x+1, y)).getColor() != stoneColor){
        colorCombo +=1;
        if (grid.containsKey(new Coordinate(x+2, y))){
          if (grid.get(new Coordinate(x+2, y)).getNumber() == stoneNumber && grid.get(new Coordinate(x+2, y)).getColor() != stoneColor && grid.get(new Coordinate(x+1, y)).getColor() != grid.get(new Coordinate(x+2, y)).getColor()){
            colorCombo +=1;
            if (grid.containsKey(new Coordinate(x+3, y))){
              if (grid.get(new Coordinate(x+3, y)).getNumber() == stoneNumber && grid.get(new Coordinate(x+3, y)).getColor() != stoneColor && grid.get(new Coordinate(x+3, y)).getColor() != grid.get(new Coordinate(x+2, y)).getColor() && grid.get(new Coordinate(x+2, y)).getColor() != grid.get(new Coordinate(x+1, y)).getColor()){
                colorCombo +=1;
                if (grid.containsKey(new Coordinate(x+4, y))){
                  return false;
                }
              } else {
                return false;
              }

            } else {
              return false;
            }
          } else {
            return false;
          }
        }
      } else {
        return false;
      }
    }

    if(colorCombo > 3 || numberCombo > 3){
      return true;
    } else {
      return false;
    }
  }


  public static int getHeight() {
    return height;
  }

  public static int getWidth() {
    return width;
  }



}
