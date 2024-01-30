package activityrecord.graph;

import activityrecord.ract.ActivityRecordCalendar;
import activityrecord.ract.PeriodType;
import activityrecord.ract.TimePeriod;
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
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import siscorp.system.DateSC;

public class TopPeriodSummaryAxis extends StandardHorizontalAxis {
  private TimePeriod currentBigPeriod;
  
  private TimePeriod currentPeriod;
  
  protected DateSC minDate;
  
  protected DateSC maxDate;
  
  private double maxQuantityPerWorkPeriod;
  
  public Locale locale;
  
  Color normalTextColor = new Color(0, 0, 0);
  
  Color highMonthTextColor = new Color(255, 255, 255);
  
  Color highYearTextColor = new Color(255, 255, 255);
  
  Color backMonthColor = new Color(190, 210, 210);
  
  Color backHighMonthColor = new Color(110, 135, 170);
  
  Color backYearColor = new Color(165, 185, 205);
  
  Color backHighYearColor = new Color(110, 135, 170);
  
  Color backHighBarColor = new Color(190, 230, 240);
  
  Color backGraphColor = new Color(255, 255, 255);
  
  Color backHighGraphColor = new Color(230, 230, 240);
  
  public TopPeriodSummaryAxis(Graph owner, TimePeriod currentPeriod, UserSpace userSpace, IDateDataSerie dataSerie, DateSC minDate, DateSC maxDate, TimePeriod currentBigPeriod, double maxQuantityPerWorkPeriod, Locale locale) {
    super(owner, userSpace.getMinUserX(), userSpace.getMaxUserX());
    double step = (this.max - this.min) / dataSerie.size();
    this.axisData = (IDataSerie)new NumericSerieArrayListImplementation(dataSerie.size());
    Iterator<Value> iterator = dataSerie.iterator();
    double xValue = this.min;
    while (iterator.hasNext()) {
      this.axisData.add(new DualValue(new Double(xValue), iterator.next()));
      xValue += step;
    } 
    setCurrentPeriod(currentPeriod);
    setCurrentBigPeriod(currentBigPeriod);
    setMinDate(minDate);
    setMaxDate(maxDate);
    this.locale = locale;
    this.maxQuantityPerWorkPeriod = maxQuantityPerWorkPeriod;
  }
  
  public void draw(Canvas canvas) throws DrawingException {
    Canvas.CanvasContext context = canvas.saveContext();
    double stepSize = getSegmentSize().doubleValue();
    UserSpace userSpace = canvas.getDefaultTransform();
    double maxY = userSpace.getMaxUserY();
    double minX = userSpace.getMinUserX();
    DrawableList drawableList = new DrawableList();
    ActivityRecordCalendar calendar = new ActivityRecordCalendar();
    Font fontPlain = new Font("Arial", 0, 10);
    Font fontBold = new Font("Arial", 1, 10);
    double pixelWidth = userSpace.getProportionalHorizontalUserValue(1.0D);
    double pixelHeight = userSpace.getProportionalVerticalUserValue(1.0D);
    double textHeight = 11.0D * pixelHeight;
    double yLinePos = userSpace.getMinUserY();
    DrawableShape line = new DrawableShape(new Line2D.Double(new Point2D.Double(minX, yLinePos), 
          new Point2D.Double(userSpace.getMaxUserX(), yLinePos)));
    line.setColor(new Color(64, 64, 96));
    line.setStroke(new BasicStroke((float)(pixelHeight * 1.5D)));
    drawableList.add((Drawable)line);
    DrawableFillShape rectangle = new DrawableFillShape(
        new Rectangle2D.Double(minX, maxY, 
          userSpace.getMaxUserX(), userSpace.getTopMargin() * pixelHeight));
    rectangle.setFill(this.backMonthColor);
    drawableList.add((Drawable)rectangle);
    Stroke stroke = new BasicStroke((float)(0.5D * pixelHeight));
    double quarterPercentStep = 1900.0D / this.maxQuantityPerWorkPeriod;
    for (double i = quarterPercentStep; i < maxY; i += quarterPercentStep) {
      line = new DrawableShape(new Line2D.Double(new Point2D.Double(minX, i), 
            new Point2D.Double(userSpace.getMaxUserX(), i)));
      line.setColor(new Color(200, 200, 200));
      line.setStroke(stroke);
      drawableList.add((Drawable)line);
    } 
    Iterator<DualValue> iterator = this.axisData.iterator();
    int currentYear = -1;
    int axisDataIndex = -1;
    while (iterator.hasNext()) {
      Color textColor, yearTextColor, yearBackColor, lineColor;
      Font font;
      double startDelta = 0.0D;
      double endDelta = 0.0D;
      axisDataIndex++;
      DualValue value = iterator.next();
      DateValue dateValue = (DateValue)value.getDualValue();
      minX = value.getValue().doubleValue();
      calendar.setTime((Date)dateValue.getValue());
      String periodDisplayText = calendar.getTimePeriod(this.currentPeriod.getPeriodType(), this.locale);
      if (!this.currentBigPeriod.contains(dateValue.getValue())) {
        font = fontPlain;
        yearTextColor = this.normalTextColor;
        yearBackColor = this.backYearColor;
      } else {
        font = fontBold;
        yearTextColor = this.highYearTextColor;
        yearBackColor = this.backHighYearColor;
        if (this.currentPeriod.getPeriodType() != PeriodType.YEAR) {
          int count = calendar.getSmallPeriodsCount(this.currentPeriod.getPeriodType(), 
              PeriodType.YEAR, null);
          if (axisDataIndex / count * count == axisDataIndex)
            startDelta = pixelWidth * 2.0D; 
          if ((axisDataIndex + 1) / count * count == axisDataIndex + 1)
            endDelta = pixelWidth * 4.0D; 
        } 
      } 
      yLinePos = maxY + userSpace.getTopMargin() * pixelHeight;
      if (this.minDate != null && calendar.getCurrentTime().compareTo((Date)this.minDate) >= 0 && 
        this.maxDate != null && calendar.getCurrentTime().compareTo((Date)this.maxDate) <= 0) {
        lineColor = Color.GREEN;
      } else {
        lineColor = Color.RED;
      } 
      line = new DrawableShape(new Line2D.Double(new Point2D.Double(minX, yLinePos), 
            new Point2D.Double(minX + stepSize - 1.0D, yLinePos)));
      line.setColor(lineColor);
      line.setStroke(new BasicStroke((float)(pixelHeight * 2.0D)));
      drawableList.add((Drawable)line);
      if (currentYear != calendar.get(1)) {
        double yearWidth;
        currentYear = calendar.get(1);
        switch (this.currentPeriod.getPeriodType()) {
          case MONTH:
            yearWidth = 12.0D * stepSize;
            break;
          case QUARTER:
            yearWidth = 4.0D * stepSize;
            break;
          case SEMESTER:
            yearWidth = 2.0D * stepSize;
            break;
          case YEAR:
            yearWidth = stepSize;
            break;
          default:
            throw new RuntimeException("Period is not accepted in TopPeriodSummaryAxis.draw()");
        } 
        if (this.currentPeriod.getPeriodType() != PeriodType.YEAR) {
          rectangle = new DrawableFillShape(new Rectangle2D.Double(
                minX + pixelWidth * 2.0D, maxY + textHeight, 
                yearWidth - pixelWidth * 6.0D, textHeight - pixelHeight * 2.0D));
          rectangle.setFill(yearBackColor);
          drawableList.add((Drawable)rectangle);
          DrawableText drawableText = new DrawableText((float)(minX + yearWidth / 2.0D), 
              (float)(maxY + textHeight * 2.0D + pixelHeight), Integer.toString(currentYear));
          drawableText.setHorizontalAlign(1);
          drawableText.setVerticalAlign(2);
          drawableText.setFont(font);
          drawableText.setColor(yearTextColor);
          drawableList.add((Drawable)drawableText);
        } 
      } 
      if (this.currentPeriod.getStartDate().equals(dateValue.getValue())) {
        rectangle = new DrawableFillShape(
            new Rectangle2D.Double(minX + startDelta, userSpace.getMinUserY() + pixelHeight, 
              stepSize - 0.3D - endDelta, maxY - userSpace.getMinUserY()));
        rectangle.setColor(this.backHighBarColor);
        drawableList.add((Drawable)rectangle);
        rectangle = new DrawableFillShape(new Rectangle2D.Double(minX + startDelta, 
              maxY, stepSize - endDelta, textHeight - pixelHeight));
        rectangle.setFill(this.backHighYearColor);
        drawableList.add((Drawable)rectangle);
        textColor = this.highYearTextColor;
      } else {
        textColor = this.normalTextColor;
      } 
      DrawableText text = new DrawableText((float)(minX + stepSize / 2.0D), 
          (float)(maxY + textHeight + 2.0D * pixelHeight), 
          periodDisplayText.toUpperCase());
      text.setHorizontalAlign(1);
      text.setVerticalAlign(2);
      text.setFont(font);
      text.setColor(textColor);
      drawableList.add((Drawable)text);
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
  
  public TimePeriod getCurrentPeriod() {
    return this.currentPeriod;
  }
  
  public void setCurrentPeriod(TimePeriod currentPeriod) {
    this.currentPeriod = currentPeriod;
  }
  
  public TimePeriod getCurrentBigPeriod() {
    return this.currentBigPeriod;
  }
  
  public void setCurrentBigPeriod(TimePeriod currentBigPeriod) {
    this.currentBigPeriod = currentBigPeriod;
  }
  
  public double getMaxQuantityPerWorkPeriod() {
    return this.maxQuantityPerWorkPeriod;
  }
  
  public void setMaxQuantityPerWorkPeriod(double maxQuantityPerWorkPeriod) {
    this.maxQuantityPerWorkPeriod = maxQuantityPerWorkPeriod;
  }
}
