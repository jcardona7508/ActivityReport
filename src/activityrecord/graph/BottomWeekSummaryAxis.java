package activityrecord.graph;

import activityrecord.ract.Week;
import gausssoft.drawing.Canvas;
import gausssoft.drawing.Drawable;
import gausssoft.drawing.DrawableList;
import gausssoft.drawing.DrawableShape;
import gausssoft.drawing.DrawableText;
import gausssoft.drawing.DrawingException;
import gausssoft.drawing.UserSpace;
import gausssoft.graph.Graph;
import gausssoft.graph.axis.StandardHorizontalAxis;
import gausssoft.graph.serie.IDataSerie;
import gausssoft.graph.serie.INumericSerie;
import gausssoft.graph.serie.NumericSerieArrayListImplementation;
import gausssoft.graph.serie.NumericValue;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.Iterator;

public class BottomWeekSummaryAxis extends StandardHorizontalAxis {
  protected Week currentWeek;
  
  public BottomWeekSummaryAxis(Graph owner, UserSpace userSpace, INumericSerie dataSerie) {
    super(owner, userSpace.getMinUserX(), userSpace.getMaxUserX(), (IDataSerie)dataSerie);
    this.axisData.clear();
    Iterator iterator = dataSerie.iterator();
    while (iterator.hasNext())
      this.axisData.add(iterator.next()); 
    ((NumericSerieArrayListImplementation)this.axisData).resetBorderValues(null);
  }
  
  public void draw(Canvas canvas) throws DrawingException {
    double step = getSegmentSize().doubleValue();
    UserSpace userSpace = canvas.getDefaultTransform();
    double minX = userSpace.getMinUserX();
    double maxX = userSpace.getMaxUserX();
    double minY = userSpace.getMinUserY();
    double maxY = userSpace.getMaxUserY();
    DrawableList drawableList = new DrawableList();
    Font font = new Font("Arial", 0, 12);
    FontMetrics metrics = canvas.getFontMetrics(font);
    int height = metrics.getHeight();
    int textHeight = height;
    Point2D auxiliarPoint = new Point2D.Double(0.0D, textHeight);
    try {
      auxiliarPoint = userSpace.inverseTransform(auxiliarPoint, auxiliarPoint);
      textHeight = (int)Math.abs(maxY - auxiliarPoint.getY());
    } catch (NoninvertibleTransformException exception) {
      System.out.println("Exception in BottomWeekSummaryAxis: " + exception);
      height = 10;
    } 
    Iterator<NumericValue> iterator = this.axisData.iterator();
    while (iterator.hasNext()) {
      NumericValue value = iterator.next();
      if (value.getValue().doubleValue() > 0.0D) {
        Point2D textPoint = new Point2D.Double(minX + step / 2.0D, 
            minY + (textHeight / 2));
        long valor = (long)Math.floor(value.getValue().doubleValue());
        double resto = value.getValue().doubleValue() - valor;
        DrawableText text = new DrawableText((float)textPoint.getX(), 
            (float)textPoint.getY(), 
            String.valueOf(Long.toString(valor)) + ((resto == 0.0D) ? 
            "" : 
            "+"));
        text.setHorizontalAlign(1);
        text.setVerticalAlign(0);
        drawableList.add((Drawable)text);
      } 
      minX += step;
    } 
    int margin = metrics.getHeight() + 4;
    maxY = ((INumericSerie)this.axisData).getMaxValue().getValue().doubleValue();
    if (maxY < 8.0D)
      maxY = 8.0D; 
    userSpace = new UserSpace(userSpace.getDevice(), 
        0.0D, 
        0.0D, 
        maxX, 
        maxY, 
        0.0D, 
        margin, 
        0.0D, 
        margin);
    BasicStroke stroke = new BasicStroke((float)userSpace.getProportionalVerticalUserValue(0.5D));
    for (int i = 4; i < maxY; i += 4) {
      DrawableShape drawableShape = new DrawableShape(new Line2D.Double(new Point2D.Double(0.0D, i), 
            new Point2D.Double(maxX, i)));
      drawableShape.setColor(new Color(155, 155, 155));
      drawableShape.setStroke(stroke);
      drawableShape.setTransform(userSpace);
      drawableList.add((Drawable)drawableShape);
    } 
    double moveY = -userSpace.getProportionalVerticalUserValue(1.0D);
    DrawableShape line = new DrawableShape(new Line2D.Double(new Point2D.Double(0.0D, moveY), 
          new Point2D.Double(maxX, moveY)));
    line.setColor(new Color(100, 115, 155));
    line.setStroke(new BasicStroke((float)userSpace.getProportionalVerticalUserValue(2.0D)));
    line.setUseOriginalTransform(false);
    line.setTransform(userSpace);
    drawableList.add((Drawable)line);
    drawableList.setFont(font);
    drawableList.setColor(Color.BLACK);
    drawableList.draw(canvas);
  }
}
