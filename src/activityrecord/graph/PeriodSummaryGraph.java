package activityrecord.graph;

import activityrecord.graph.PercentGraphColorSelector;
import activityrecord.graph.PeriodLegendDrawer;
import activityrecord.graph.TopPeriodSummaryAxis;
import activityrecord.ract.PercentsPerTimePeriod;
import activityrecord.ract.TimePeriod;
import activityrecord.ract.WorkPerTimePeriod;
import gausssoft.drawing.UserSpace;
import gausssoft.graph.Graph;
import gausssoft.graph.GraphColorSelector;
import gausssoft.graph.SerieDrawer;
import gausssoft.graph.StringValue;
import gausssoft.graph.axis.Axis;
import gausssoft.graph.bar.AbstractBarFactory;
import gausssoft.graph.bar.GradientBarFactory;
import gausssoft.graph.bar.StackBarGraph;
import gausssoft.graph.bar.StackBarSerieDrawer;
import gausssoft.graph.serie.DateSerieArrayListImplementation;
import gausssoft.graph.serie.DateValue;
import gausssoft.graph.serie.DualValue;
import gausssoft.graph.serie.IDataSerie;
import gausssoft.graph.serie.IDateDataSerie;
import gausssoft.graph.serie.INumericSerie;
import gausssoft.graph.serie.NumericSerieArrayListImplementation;
import gausssoft.graph.serie.NumericValue;
import gausssoft.graph.serie.StringSerieArrayListImplementation;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import siscorp.system.DateSC;

public class PeriodSummaryGraph extends StackBarGraph {
  private TimePeriod currentPeriod;
  
  private double maxWorkQuantity;
  
  public PeriodSummaryGraph(TimePeriod currentPeriod, Dimension dimension, UserSpace userSpace, PercentsPerTimePeriod originData, DateSC minDate, DateSC maxDate, TimePeriod currentBigPeriod, Locale locale) {
    super("A");
    PercentGraphColorSelector colorSelector = new PercentGraphColorSelector();
    DateSerieArrayListImplementation dateDataSeries = new DateSerieArrayListImplementation();
    StringSerieArrayListImplementation uniqueConventionsDataSeries = new StringSerieArrayListImplementation();
    ArrayList<IDataSerie> series = new ArrayList();
    List<WorkPerTimePeriod> workPeriods = originData.getWorkPeriods();
    this.maxWorkQuantity = 0.0D;
    int i;
    for (i = 0; i < workPeriods.size(); i++) {
      WorkPerTimePeriod item = workPeriods.get(i);
      DateSC date = new DateSC((Date)item.getDate());
      dateDataSeries.add(new DateValue(date));
      this.maxWorkQuantity = Math.max(this.maxWorkQuantity, item.calculateTotalWork());
      for (int j = 0; j < item.getWorkItems().size(); j++) {
        DualValue amount = (DualValue) item.getWorkItems().get(j);
        String type = amount.getDisplayValue();
        for (int k = 0; k < series.size(); k++) {
          if (type.equals(((IDataSerie)series.get(k)).getName())) {
            type = null;
            break;
          } 
        } 
        if (type != null) {
          NumericSerieArrayListImplementation valuesSeries = new NumericSerieArrayListImplementation();
          valuesSeries.setName(type);
          series.add(valuesSeries);
        } 
      } 
    } 
    for (i = 0; i < workPeriods.size(); i++) {
      WorkPerTimePeriod item = workPeriods.get(i);
      int j;
      for (j = 0; j < item.getWorkItems().size(); j++) {
        DualValue amount = (DualValue) item.getWorkItems().get(j);
        String type = amount.getDisplayValue();
        double value = amount.getValue().doubleValue() * 76.0D / this.maxWorkQuantity;
        for (int k = 0; k < series.size(); k++) {
          if (type.equals(((IDataSerie)series.get(k)).getName())) {
            ((List<NumericValue>)series.get(k)).add(new NumericValue(Double.valueOf(value)));
            type = null;
            break;
          } 
        } 
        if (type != null)
          throw new RuntimeException("Program error in WorkPerTimePeriod constructor, series was not found."); 
      } 
      for (j = 0; j < series.size(); j++) {
        if (((List)series.get(j)).size() < i + 1)
          ((List<NumericValue>)series.get(j)).add(new NumericValue(Double.valueOf(0.0D))); 
      } 
    } 
    TopPeriodSummaryAxis topPeriodSummaryAxis = new TopPeriodSummaryAxis((Graph)this, currentPeriod, userSpace, (IDateDataSerie)dateDataSeries, 
        minDate, maxDate, currentBigPeriod, this.maxWorkQuantity, locale);
    addAxis((Axis)topPeriodSummaryAxis);
    StackBarSerieDrawer drawer = new StackBarSerieDrawer();
    drawer.setAxis((Axis)topPeriodSummaryAxis);
    drawer.setBarFactory((AbstractBarFactory)new GradientBarFactory());
    drawer.setBarWidthPercentage(0.85D);
    drawer.setShowValues(false);
    drawer.setColorSelector((GraphColorSelector)colorSelector);
    for (i = 0; i < series.size(); i++) {
      uniqueConventionsDataSeries.add(
          new StringValue(((IDataSerie)series.get(i)).getName()));
      addDataSerie((INumericSerie)series.get(i), (SerieDrawer)drawer);
    } 
    addCategorySerie((IDataSerie)uniqueConventionsDataSeries, (SerieDrawer)new PeriodLegendDrawer((GraphColorSelector)colorSelector));
    setCurrentPeriod(currentBigPeriod);
    setDimension(dimension);
    setTransform(userSpace);
  }
  
  protected TimePeriod getCurrentPeriod() {
    return this.currentPeriod;
  }
  
  protected void setCurrentPeriod(TimePeriod currentPeriod) {
    this.currentPeriod = currentPeriod;
  }
}
