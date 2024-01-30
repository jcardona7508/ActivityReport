package activityrecord.graph;

import gausssoft.drawing.Canvas;
import gausssoft.drawing.Drawable;
import gausssoft.drawing.DrawableFillShape;
import gausssoft.drawing.DrawableList;
import gausssoft.drawing.DrawableShape;
import gausssoft.drawing.DrawableText;
import gausssoft.drawing.DrawingException;
import gausssoft.drawing.UserSpace;
import gausssoft.graph.ConventionsSerieDrawer;
import gausssoft.graph.Graph;
import gausssoft.graph.GraphColorSelector;
import gausssoft.graph.serie.IDataSerie;
import gausssoft.graph.serie.Value;
import gausssoft.graph.style.GraphStyle;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

public class PeriodLegendDrawer extends ConventionsSerieDrawer {
  public PeriodLegendDrawer(GraphColorSelector colorSelector) {
    super(colorSelector);
  }
  
  public void draw(Canvas canvas, Graph graph, IDataSerie dataSerie, int sequence) throws DrawingException {
    GraphStyle style = graph.getStyle();
    DrawableList drawableList = new DrawableList();
    UserSpace userSpace = canvas.getDefaultTransform();
    UserSpace tempUserSpace = new UserSpace(userSpace.getDevice(), 
        userSpace.getMinUserX(), 
        userSpace.getMinUserY(), 
        userSpace.getMaxUserX(), 
        userSpace.getMaxUserY());
    double oneHPixelValue = tempUserSpace.getProportionalHorizontalUserValue(1.0D);
    double oneVPixelValue = tempUserSpace.getProportionalVerticalUserValue(1.0D);
    canvas.setDefaultTransform(tempUserSpace);
    Point2D position = new Point2D.Double(userSpace.getMinUserX() + (userSpace.getLeftMargin() + 12.0D) * oneHPixelValue, 
        userSpace.getMinUserY() + 7.0D * oneVPixelValue);
    Iterator<Value> iterator = dataSerie.iterator();
    Rectangle2D smallRectangle = new Rectangle2D.Double(0.0D, 0.0D, 
        oneHPixelValue * 6.0D, oneVPixelValue * 7.0D);
    int index = 0;
    Font font = new Font("Arial", 0, 10);
    FontMetrics metrics = canvas.getFontMetrics(font);
    while (iterator.hasNext()) {
      String value = ((Value)iterator.next()).getDisplayValue();
      Point2D finalPosition = (Point2D.Double)position.clone();
      finalPosition.setLocation(finalPosition.getX() - smallRectangle.getWidth(), 
          finalPosition.getY() - smallRectangle.getHeight());
      Rectangle2D smallRectangle2 = new Rectangle2D.Double(finalPosition.getX(), 
          finalPosition.getY(), 
          smallRectangle.getWidth(), 
          smallRectangle.getHeight());
      DrawableFillShape drawableFillShape = new DrawableFillShape(smallRectangle2);
      drawableFillShape.setFill(style.getLegendTextColor());
      drawableList.add((Drawable)drawableFillShape);
      smallRectangle2 = new Rectangle2D.Double(finalPosition.getX() - oneHPixelValue, 
          finalPosition.getY() + oneVPixelValue, 
          smallRectangle.getWidth(), 
          smallRectangle.getHeight());
      drawableFillShape = new DrawableFillShape(smallRectangle2);
      drawableFillShape.setFill(getColorSelector().getColor(index + 1));
      drawableList.add((Drawable)drawableFillShape);
      DrawableShape drawableShape = new DrawableShape(smallRectangle2);
      drawableShape.setStroke(new BasicStroke((float)oneHPixelValue));
      drawableShape.setColor(style.getLegendTextColor());
      drawableList.add((Drawable)drawableShape);
      index++;
      finalPosition.setLocation(finalPosition.getX() + smallRectangle.getWidth() * 1.5D, 
          finalPosition.getY());
      DrawableText textShape = new DrawableText(finalPosition, value);
      textShape.setColor(style.getLegendTextColor());
      textShape.setFont(font);
      textShape.setHorizontalAlign(0);
      drawableList.add((Drawable)textShape);
      position.setLocation(position.getX() + metrics.stringWidth(value) + smallRectangle.getWidth() * 3.0D, 
          position.getY());
    } 
    drawableList.draw(canvas);
    canvas.setDefaultTransform(userSpace);
  }
}
