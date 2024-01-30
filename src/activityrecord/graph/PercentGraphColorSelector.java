package activityrecord.graph;

import gausssoft.graph.GraphColorSelector;
import gausssoft.graph.serie.NumericValue;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class PercentGraphColorSelector implements GraphColorSelector {
  private List colors;
  
  public PercentGraphColorSelector() {
    this.colors = new ArrayList();
    this.colors.add(Color.RED);
    this.colors.add(new Color(152, 249, 147));
    this.colors.add(Color.YELLOW);
    this.colors.add(new Color(166, 166, 255));
    this.colors.add(new Color(255, 128, 128));
    this.colors.add(new Color(196, 208, 242));
    this.colors.add(new Color(0, 183, 183));
    this.colors.add(new Color(255, 159, 113));
    this.colors.add(new Color(194, 190, 141));
    this.colors.add(new Color(255, 125, 255));
  }
  
  public Color getColor(int color) {
    return (Color) this.colors.get(color % this.colors.size());
  }
  
  public void setColorList(List colorList) {
    this.colors = colorList;
  }
  
  public Color getColor(int sequence, int subSequence, NumericValue value) {
    if (sequence == 0)
      return (Color) this.colors.get((subSequence + 1) % this.colors.size()); 
    return (Color) this.colors.get(sequence % this.colors.size());
  }
  
  public Color getColor(int sequence, int subSequence, int color) {
    if (sequence == 0)
      return (Color) this.colors.get((subSequence + 1) % this.colors.size()); 
    return (Color) this.colors.get(sequence % this.colors.size());
  }
}
