package activityrecord.graph;

import gausssoft.drawing.Canvas;
import gausssoft.drawing.DrawingException;
import gausssoft.drawing.UserSpace;
import gausssoft.graph.Graph;
import gausssoft.graph.axis.Axis;
import gausssoft.graph.bar.AbstractBarFactory;
import gausssoft.graph.bar.VerticalBarSerieDrawer;
import gausssoft.graph.serie.IDataSerie;
import gausssoft.graph.serie.INumericSerie;
import java.awt.Font;
import java.awt.FontMetrics;

public class ActivityRecordBarSerieDrawer extends VerticalBarSerieDrawer {
  public ActivityRecordBarSerieDrawer(Axis axis) {
    super(axis);
  }
  
  public ActivityRecordBarSerieDrawer(Axis axis, AbstractBarFactory barFactory) {
    super(axis, barFactory);
    setBarWidthPercentage(0.8D);
    setShowValues(false);
  }
  
  public void draw(Canvas canvas, Graph graph, IDataSerie dataSerie, int sequence) throws DrawingException {
    UserSpace currentUserSpace = canvas.getDefaultTransform();
    double maxY = ((INumericSerie)dataSerie).getMaxValue()
      .getValue()
      .doubleValue();
    if (maxY < 8.0D)
      maxY = 8.0D; 
    Font localFont = getFont();
    if (localFont == null)
      localFont = new Font("Arial", 0, 12); 
    FontMetrics metrics = canvas.getFontMetrics(localFont);
    double margin = metrics.getHeight() + 4.0D;
    UserSpace userSpace = canvas.getDefaultTransform();
    userSpace = new UserSpace(currentUserSpace.getDevice(), 
        0.0D, 
        0.0D, 
        currentUserSpace.getMaxUserX(), 
        maxY, 
        0.0D, 
        margin, 
        0.0D, 
        margin);
    canvas.setDefaultTransform(userSpace);
    super.draw(canvas, graph, dataSerie, sequence);
  }
}
