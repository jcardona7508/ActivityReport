package activityrecord.graph;

import activityrecord.ract.ActivityRecordCalendar;
import activityrecord.ract.Week;
import gausssoft.drawing.Canvas;
import gausssoft.drawing.Drawable;
import gausssoft.drawing.DrawableFillShape;
import gausssoft.drawing.DrawableList;
import gausssoft.drawing.DrawableShape;
import gausssoft.drawing.DrawableText;
import gausssoft.drawing.DrawingException;
import gausssoft.drawing.UserSpace;
import gausssoft.graph.Graph;
import gausssoft.graph.axis.StandardHorizontalAxis;
import gausssoft.graph.serie.DateValue;
import gausssoft.graph.serie.DualValue;
import gausssoft.graph.serie.IDataSerie;
import gausssoft.graph.serie.IDateDataSerie;
import gausssoft.graph.serie.NumericSerieArrayListImplementation;
import gausssoft.graph.serie.Value;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Date;
import java.util.Iterator;
import siscorp.system.DateSC;

public class TopWeekSummaryAxis extends StandardHorizontalAxis {
  protected Week currentWeek;
  
  protected DateSC minDate;
  
  protected DateSC maxDate;
  
  public TopWeekSummaryAxis(Graph owner, Week currentWeek, UserSpace userSpace, IDateDataSerie dataSerie, DateSC minDate, DateSC maxDate) {
    this(owner, currentWeek, userSpace, dataSerie);
    setMaxDate(maxDate);
    setMinDate(minDate);
  }
  
  public TopWeekSummaryAxis(Graph owner, Week currentWeek, UserSpace userSpace, IDateDataSerie dataSerie) {
    super(owner, userSpace.getMinUserX(), userSpace.getMaxUserX());
    double step = (this.max - this.min) / dataSerie.size();
    this.axisData = (IDataSerie)new NumericSerieArrayListImplementation(dataSerie.size());
    Iterator<Value> iterator = dataSerie.iterator();
    double xValue = this.min;
    while (iterator.hasNext()) {
      this.axisData.add(new DualValue(new Double(xValue), 
            iterator.next()));
      xValue += step;
    } 
    this.currentWeek = currentWeek;
  }
  
  public void draw(Canvas canvas) throws DrawingException {
    Canvas.CanvasContext context = canvas.saveContext();
    double step = getSegmentSize().doubleValue();
    UserSpace userSpace = canvas.getDefaultTransform();
    double maxY = userSpace.getMaxUserY();
    double minX = userSpace.getMinUserX();
    DrawableList drawableList = new DrawableList();
    ActivityRecordCalendar calendar = new ActivityRecordCalendar();
    Font fontPlain = new Font("Arial", 0, 12);
    FontMetrics metrics = canvas.getFontMetrics(fontPlain);
    int height = metrics.getHeight();
    Font fontBold = new Font("Arial", 1, 12);
    metrics = canvas.getFontMetrics(fontBold);
    height = Math.max(height, metrics.getHeight());
    Color[] currentWeekColors = new Color[8];
    currentWeekColors[2] = new Color(192, 208, 211);
    currentWeekColors[3] = new Color(192, 208, 211);
    currentWeekColors[4] = new Color(192, 208, 211);
    currentWeekColors[5] = new Color(192, 208, 211);
    currentWeekColors[6] = new Color(192, 208, 211);
    currentWeekColors[7] = new Color(214, 226, 228);
    currentWeekColors[1] = new Color(217, 185, 183);
    Color[] weekColors = new Color[8];
    weekColors[2] = new Color(96, 123, 174);
    weekColors[3] = new Color(96, 123, 174);
    weekColors[4] = new Color(96, 123, 174);
    weekColors[5] = new Color(96, 123, 174);
    weekColors[6] = new Color(96, 123, 174);
    weekColors[7] = new Color(0, 0, 128);
    weekColors[1] = new Color(128, 0, 0);
    int textHeight = height;
    Point2D auxiliarPoint = new Point2D.Double(0.0D, textHeight);
    try {
      auxiliarPoint = userSpace.inverseTransform(auxiliarPoint, auxiliarPoint);
      textHeight = (int)Math.abs(maxY - auxiliarPoint.getY());
    } catch (NoninvertibleTransformException exception) {
      System.out.println("Exception: " + exception);
      height = 10;
    } 
    Iterator<DualValue> iterator = this.axisData.iterator();
    while (iterator.hasNext()) {
      Font font;
      Color textColor, dayColor, backColor, lineColor;
      DualValue value = iterator.next();
      DateValue dateValue = (DateValue)value.getDualValue();
      minX = value.getValue().doubleValue();
      calendar.setTime((Date)dateValue.getValue());
      if (!this.currentWeek.belongsTo(dateValue.getValue())) {
        font = fontPlain;
        textColor = Color.BLACK;
        dayColor = currentWeekColors[calendar.get(7)];
        backColor = Color.WHITE;
      } else {
        font = fontBold;
        textColor = Color.WHITE;
        dayColor = weekColors[calendar.get(7)];
        backColor = new Color(203, 215, 220);
      } 
      Point2D leftPoint = new Point2D.Double(minX, maxY - textHeight);
      Point2D textPoint = new Point2D.Double(minX + step / 2.0D, leftPoint.getY());
      DrawableFillShape rectangle = new DrawableFillShape(new Rectangle2D.Double(minX, 0.0D, 
            step - 0.3D, maxY - textHeight));
      rectangle.setColor(backColor);
      drawableList.add((Drawable)rectangle);
      drawableList.add(
          
          (Drawable)(rectangle = new DrawableFillShape(new Rectangle2D.Double(leftPoint.getX(), leftPoint.getY(), step, textHeight))));
      rectangle.setFill(dayColor);
      if (this.minDate != null && this.maxDate != null) {
        if (dateValue.getValue().compareTo((Date)this.minDate) >= 0 && 
          dateValue.getValue().compareTo((Date)this.maxDate) <= 0) {
          lineColor = Color.GREEN;
        } else {
          lineColor = Color.RED;
        } 
      } else {
        lineColor = Color.GREEN;
      } 
      DrawableText text = new DrawableText((float)textPoint.getX(), 
          (float)textPoint.getY(), 
          value.getDisplayValue());
      drawableList.add((Drawable)text);
      text.setHorizontalAlign(1);
      text.setVerticalAlign(0);
      text.setFont(font);
      text.setColor(textColor);
      text.setTransform(userSpace);
      Point2D leftLinePoint = new Point2D.Double(minX, maxY);
      Point2D rightLinePoint = new Point2D.Double(minX + step, maxY);
      DrawableShape line = new DrawableShape(new Line2D.Double(leftLinePoint, rightLinePoint));
      line.setColor(lineColor);
      line.setStroke(new BasicStroke((float)userSpace.getProportionalVerticalUserValue(1.2D)));
      drawableList.add((Drawable)line);
    } 
    drawableList.draw(canvas);
    canvas.restoreContext(context);
  }
  
  public DateSC getMaxDate() {
    return this.maxDate;
  }
  
  public void setMaxDate(DateSC maxDate) {
    this.maxDate = maxDate;
  }
  
  public DateSC getMinDate() {
    return this.minDate;
  }
  
  public void setMinDate(DateSC minDate) {
    this.minDate = minDate;
  }
}
