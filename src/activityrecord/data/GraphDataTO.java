package activityrecord.data;

import activityrecord.ract.ActivityRecordCalendar;
import activityrecord.ract.PercentsPerTimePeriod;
import activityrecord.ract.PeriodType;
import activityrecord.ract.TimePeriod;
import activityrecord.ract.Week;
import gausssoft.drawing.UserSpace;
import gausssoft.graph.serie.DateSerieArrayListImplementation;
import gausssoft.graph.serie.NumericSerieArrayListImplementation;
import gausssoft.graph.serie.NumericValue;
import java.awt.Dimension;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import siscorp.system.DateSC;

public class GraphDataTO implements Serializable {
  public boolean isWeeklyGraph;
  
  public Week week;
  
  public TimePeriod currentPeriod;
  
  public TimePeriod bigCurrentPeriod;
  
  public Dimension dimension;
  
  public UserSpace userSpace;
  
  public DateSerieArrayListImplementation dateDataSerie;
  
  public ArrayList<String> dateDataSerieTO;
  
  public NumericSerieArrayListImplementation numericDataSerie;
  
  public ArrayList<Number> numericDataSerieTO;
  
  public PercentsPerTimePeriod summary;
  
  public Locale locale;
  
  public DateSC minDate;
  
  public DateSC maxDate;
  
  public String htmlMap;
  
  public ArrayList<String> fullDateDataSerieTO;
  
  public GraphDataTO(Week week, Dimension dimension, UserSpace userSpace, DateSerieArrayListImplementation dateDataSerie, NumericSerieArrayListImplementation numericDataSerie, DateSC minDate, DateSC maxDate) {
    this.isWeeklyGraph = true;
    this.week = week;
    this.dimension = dimension;
    this.userSpace = userSpace;
    this.dateDataSerie = dateDataSerie;
    this.numericDataSerie = numericDataSerie;
    this.minDate = minDate;
    this.maxDate = maxDate;
    this.dateDataSerieTO = new ArrayList<>();
    this.numericDataSerieTO = new ArrayList<>();
    this.fullDateDataSerieTO = new ArrayList<>();
    setDataSeriesTO();
  }
  
  public GraphDataTO(TimePeriod currentPeriod, Dimension dimension, UserSpace userSpace, PercentsPerTimePeriod summary, DateSC minDate, DateSC maxDate, TimePeriod bigCurrentPeriod, Locale locale) {
    this.isWeeklyGraph = false;
    this.currentPeriod = currentPeriod;
    this.dimension = dimension;
    this.userSpace = userSpace;
    this.summary = summary;
    this.minDate = minDate;
    this.maxDate = maxDate;
    this.bigCurrentPeriod = bigCurrentPeriod;
    this.locale = locale;
    this.dateDataSerieTO = new ArrayList<>();
    this.numericDataSerieTO = new ArrayList<>();
    this.fullDateDataSerieTO = new ArrayList<>();
  }
  
  public void setDataSeriesTO() {
    int i;
    for (i = 0; i < this.dateDataSerie.size(); i++)
      this.dateDataSerieTO.add(this.dateDataSerie.get(i).toString()); 
    for (i = 0; i < this.numericDataSerie.size(); i++)
      this.numericDataSerieTO.add(((NumericValue)this.numericDataSerie.get(i)).getValue()); 
  }
  
  public void setDateSeriesTO(int periods, DateSC startDate, PeriodType periodType) {
    ActivityRecordCalendar cal = new ActivityRecordCalendar(startDate);
    cal.setDateFormat(new SimpleDateFormat("yyyy/MM/dd"));
    for (int i = 0; i < periods; i++) {
      this.fullDateDataSerieTO.add(cal.getCurrentTime().toString());
      cal.goNextTimePeriod(periodType);
    } 
  }
}
