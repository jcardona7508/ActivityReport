package activityrecord.graph;

import gausssoft.graph.GraphColorSelector;
import gausssoft.graph.serie.NumericValue;
import java.awt.Color;
import java.util.List;

public class SummaryGraphColorSelector implements GraphColorSelector {
  List list;
  
  public Color getColor(int color) {
    return null;
  }
  
  public Color getColor(int sequence, int subSequence, int color) {
    return getColor(sequence, subSequence, new NumericValue(Integer.valueOf(color)));
  }
  
  public Color getColor(int sequence, int subSequence, NumericValue value) {
    Color color;
    if (value.getValue().doubleValue() >= 16.0D) {
      color = new Color(234, 174, 149);
    } else if (value.getValue().doubleValue() >= 10.0D) {
      color = new Color(103, 149, 186);
    } else if (value.getValue().doubleValue() >= 8.0D) {
      color = new Color(100, 174, 174);
    } else {
      color = new Color(214, 221, 164);
    } 
    return color;
  }
  
  public void setColorList(List colorList) {
    this.list = colorList;
  }
}
