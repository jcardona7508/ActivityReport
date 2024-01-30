package activityrecord.graph;

import activityrecord.graph.ActivityRecordBarSerieDrawer;
import activityrecord.graph.BottomWeekSummaryAxis;
import activityrecord.graph.SummaryGraphColorSelector;
import activityrecord.graph.TopWeekSummaryAxis;
import activityrecord.ract.Week;
import gausssoft.drawing.UserSpace;
import gausssoft.graph.CartesianGraph;
import gausssoft.graph.Graph;
import gausssoft.graph.GraphColorSelector;
import gausssoft.graph.SerieDrawer;
import gausssoft.graph.axis.Axis;
import gausssoft.graph.bar.AbstractBarFactory;
import gausssoft.graph.bar.GradientBarFactory;
import gausssoft.graph.serie.IDateDataSerie;
import gausssoft.graph.serie.INumericSerie;
import java.awt.Dimension;
import siscorp.system.DateSC;

public class WeekSummaryGraph extends CartesianGraph {
  private Week currentWeek;
  
  public WeekSummaryGraph(Week current, Dimension dimension, UserSpace userSpace, IDateDataSerie dateDataSerie, INumericSerie numericDataSerie, DateSC minDate, DateSC maxDate) {
    TopWeekSummaryAxis topWeekSummaryAxis = new TopWeekSummaryAxis((Graph)this, current, userSpace, dateDataSerie, minDate, maxDate);
    addAxis((Axis)topWeekSummaryAxis);
    ActivityRecordBarSerieDrawer activityRecordBarSerieDrawer = new ActivityRecordBarSerieDrawer((Axis)topWeekSummaryAxis, (AbstractBarFactory)new GradientBarFactory());
    activityRecordBarSerieDrawer.setShowDecorationWhenZero(false);
    activityRecordBarSerieDrawer.setColorSelector((GraphColorSelector)new SummaryGraphColorSelector());
    addDataSerie(numericDataSerie, (SerieDrawer)activityRecordBarSerieDrawer);
    addAxis((Axis)new BottomWeekSummaryAxis((Graph)this, userSpace, numericDataSerie));
    setTransform(userSpace);
    setCurrentWeek(current);
    setDimension(dimension);
  }
  
  public Week getCurrentWeek() {
    return this.currentWeek;
  }
  
  public void setCurrentWeek(Week week) {
    this.currentWeek = week;
  }
}
