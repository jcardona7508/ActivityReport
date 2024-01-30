package activityrecord.ract;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import java.util.Locale;
import siscorp.system.DateSC;

/**
 * @author azuluaga
 *
 * ActivityRecordCalendar
 * 
 * This class provides different functions to manipulate dates.  The clients of this class must 
 * to provide an initial date or  current date will be assumed.    
 */

public class ActivityRecordCalendar extends GregorianCalendar 
{	
	protected SimpleDateFormat dateFormat;
	
	public ActivityRecordCalendar()
	{
		super();
		this.set(this.get(Calendar.YEAR),this.get(Calendar.MONTH),this.get(Calendar.DATE),12,0); 
	}
	
	
	public ActivityRecordCalendar(DateSC date)
	{
		super();
		this.setTime(date);
	}
	
	
	/**
	 * informs what is the number of smaller time periods that can fit in a bigger time period
	 * NOTE: WEEK cannot be used as the smaller period
	 * NOTE: if DAY is the small period, the big period must be DAY, WEEK, MONTH
	 * NOTE: this method will not affect the calendar's currentTime
	 * @param smallPeriod the smaller of the time periods
	 * @param bigPeriod the bigger of the time periods
	 * @param currentDate the date to use when bigPeriod is MONTH.  if 
	 *		it's null then use the currentTime of this calendar
	 * @return the number of small periods that can fit in the big period
	 */
	public int getSmallPeriodsCount(PeriodType smallPeriod, PeriodType bigPeriod, DateSC currentDate)
	{
		int smallSize;
		int bigSize;
		
		switch (smallPeriod) {
			case DAY:
				if ((bigPeriod != PeriodType.MONTH) && (bigPeriod != PeriodType.WEEK) 
				&& (bigPeriod != PeriodType.DAY))
					// TODO: i18n: ErrMsg
					throw new RuntimeException("Program Error: DAY versus QUARTER/SEMESTER/YEAR not implemented");
				smallSize = 1;		// note: this is a size of one day
				break;
			case WEEK:
				// TODO: i18n: ErrMsg
				throw new RuntimeException("Program Error: Week not available as smaller period");
			case MONTH:
				smallSize = 1;	// note: this is a size of one month
				break;
			case QUARTER:
				smallSize = 3;	// three months
				break;
			case SEMESTER:
				smallSize = 6;
				break;
			case YEAR:
				smallSize = 12;
				break;
			default:
				// TODO: i18n: ErrMsg
				throw new RuntimeException("Program error in ActivityRecordCalendar.getSmallPeriodsCount().  smallPeriod not accepted");
		}
		
		switch (bigPeriod) {
			case DAY:
				bigSize = 1;
				break;
			case WEEK:
				bigSize = 7;
				break;
			case MONTH: {
				if (smallPeriod == PeriodType.DAY) {
					DateSC savedDate = this.getCurrentTime();
					if (currentDate != null)
						this.setTime(currentDate);
					this.add(Calendar.MONTH, 1);
					this.set(Calendar.DAY_OF_MONTH, 0);
					bigSize = this.get(Calendar.DAY_OF_MONTH);
					this.setTime(savedDate);
				} else
					bigSize = 1;	// one month
			} break;
			case QUARTER:
				bigSize = 3;
				break;
			case SEMESTER:
				bigSize = 6;
				break;
			case YEAR:
				bigSize = 12;
				break;
			default:
				// TODO: i18n: ErrMsg
				throw new RuntimeException(
					"Program error in ActivityRecordCalendar.getSmallPeriodsCount(). BigPeriod not accepted");
		}
		return bigSize/smallSize;
	}


	/** will return a list of periods according to the number of smallPeriods that can
	 * fit in bigPeriod.  Note: not all combinations are supported  (weeks in months)
	 * @param bigPeriod the containing period
	 * @param smallPeriod the contained period
	 * @return
	 */
	public List datesOfPeriod(PeriodType bigPeriod, PeriodType smallPeriod)
	{
		ArrayList periods;
		DateSC current;
		int size;			// size in days or months for the small Period
		int number;			// number of days or months that fin in the big period

		current = this.getCurrentTime();
		this.gotoFirstDayOfPeriod(bigPeriod);

		periods = new ArrayList();
		// TODO: it should use getSmallPeriodCount in here:
		switch (smallPeriod) {
			case DAY: size = 1;		// note: this is a size of one day
				break;
			case MONTH: size = 1;	// note: this is a size of one month
				break;
			case QUARTER: size = 3;	// three months
				break;
			case SEMESTER: size = 6;
				break;
			case YEAR: size = 12;
				break;
			default:
				// TODO: i18n: ErrMsg
				throw new RuntimeException("Program error in ActivityRecordCalendar.datesOfPeriod().  smallPeriod not accepted");
		}		
		switch (bigPeriod) {
			case WEEK: number = 7;
				break;
			case MONTH: {
				if (smallPeriod == PeriodType.DAY) {
					this.add(Calendar.MONTH, 1);
					this.set(Calendar.DAY_OF_MONTH, 0);
					number = this.get(Calendar.DAY_OF_MONTH);
					this.gotoFirstDayOfPeriod(bigPeriod);
				} else
					number = 1;
			} break;
			case QUARTER: number = 3;
				break;
			case SEMESTER: number = 6;
				break;
			case YEAR: number = 12;
				break;
			default:
				// TODO: i18n: ErrMsg
				throw new RuntimeException("Program error in ActivityRecordCalendar.datesOfPeriod(). BigPeriod not accepted");
		}
		
		for (int i = 0; i < number/size; i++) {
			periods.add(this.getCurrentTime());
			if (smallPeriod == PeriodType.DAY)
				this.add(Calendar.DAY_OF_MONTH, size);
			else
				this.add(Calendar.MONTH, size);
		}
		this.setTime(current);

		return periods;
	}


	/** return the text value for the given time Period type for the current date
	 * @param period the period type
	 * @return the text value for the given period type at the current date
	 */
	public String getTimePeriod(PeriodType period, Locale locale)
	{
	    String semester;
		switch (period) {
			case DAY:
				return Integer.toString(this.get(Calendar.DAY_OF_MONTH));
			case WEEK:
				// TODO: FUTURE: this doesn't work for spanish
				return "W" + Integer.toString(this.get(Calendar.WEEK_OF_MONTH));
			case MONTH: {
				DateSC tmpDate = new DateSC(this.getTime());
				tmpDate.setFormat("MMM");
            tmpDate.setLocale( locale );
				return tmpDate.toString();
			} case QUARTER:
				// TODO: i18n: ask rafael: IMPORTANT: this doesn't work for spanish
				return "Q" + Integer.toString((this.get(Calendar.MONTH)+1) / 3+1);
			case SEMESTER:
			    if((this.get(Calendar.MONTH)+1)<=5)
				return "S1";
			    else
				return "S2";
				/*return "S" + Integer.toString((this.get(Calendar.MONTH)+1) / 2+1);*/
			case YEAR:
				return Integer.toString(this.get(Calendar.YEAR));
			default:
				// TODO: i18n: ErrMsg
				throw new RuntimeException("Program error:"
					+" PeriodType not recognized in ActivityRecordCalendar.getTimePeriod()");
		}
	}

	
	/**
	* Setea el calendario con la fecha dada
	*/   		
	public void setTimeCalendar(DateSC date)
	{	  
	  this.setTime(date);	   	  	  
 	}
 	
	
	/**
	* Adelanta el calendario un numero especifico de dias 			 
	* @return retorna una variable de tipo DateSC la cual encapsula la nueva fecha
	*/   	
	public DateSC getCurrentTime()
	{
	  DateSC current;	 
	  SimpleDateFormat format;
	  
	  current = new DateSC( this.getTime() );
	  
	  if ( (format = this.getDateFormat() ) != null )
	    current.setFormat( format.toPattern() );
	    	  	  	   
	  return current;  
	} 

	
	/**
	* Se ubica en el primer dia de la semana LUNES รณ domingo
	* @return retorna una variable de tipo DateSC la cual encapsula el primer dia de la semana
	*/   		
	public DateSC goWeekStartDate()
	{	  
	  DateSC result;	  	  
	  this.set(Calendar.DAY_OF_WEEK,this.getFirstDayOfWeek());
	  result = new DateSC(this.getTime());	  
	  return result;  
	}
	
	
	/**
	* Se ubica en el ultimo dia de la semana DOMINGO รณ Lunes	 
	* @return retorna una variable de tipo DateSC la cual encapsula el ultimo dia de la semana
	*/   		
	public DateSC goWeekLastDate()
	{	  
	  DateSC result;
	  // TODO: RAFAEL: URGENT: shouldn't this be 7 ??
	  this.set(Calendar.DAY_OF_WEEK, 8 );
	  result = new DateSC(this.getTime());	  	  
	  return result;  
	}
	
	
	/**
	* Adelanta el calendario un dia 			 
	* @return retorna una variable de tipo DateSC la cual encapsula el dia siguiente del calendario
	*/   		
	public DateSC goNextDate()
	{	  
	  return this.goNextTimePeriod(PeriodType.DAY);
	}
	
	/**
	* Va al dia anterior del calendario  			 
	* @return retorna una variable de tipo DateSC la cual encapsula el dia anterior del calendario
	*/   	
	public DateSC goPreviousDate()
	{	  
	  return this.goPreviousTimePeriod(PeriodType.DAY);
	}

	/**
	* Adelanta el calendario un mes 			 
	* @return retorna una variable de tipo DateSC la cual encapsula el mes siguiente del calendario
	*/   	
	public DateSC goNextMonth()
	{	  
	  return this.goNextTimePeriod(PeriodType.MONTH);
	}
	
	/**
	* Va al mes anterior del calendario  			 
	* @return retorna una variable de tipo DateSC la cual encapsula el mes anterior del calendario
	*/   		
	public DateSC goPreviousMonth()
	{	  
	  return this.goPreviousTimePeriod(PeriodType.MONTH);
	}
	
	/**
	* Adelanta el calendario una semana 			 
	* @return retorna una variable de tipo DateSC la cual encapsula la fecha de la semana siguiente del calendario
	*/   	
	public DateSC goNextWeek()
	{	  
	  return this.goNextTimePeriod(PeriodType.WEEK);
	}
	
	/**
	* Va a la semana anterior 			 
	* @return retorna una variable de tipo DateSC la cual encapsula la semana anterior del calendario
	*/   	
	public DateSC goPreviousWeek()
	{	  
	  return this.goPreviousTimePeriod(PeriodType.WEEK);
	}
	
	
	/**
	 * move back by one unit of a period type
	 * @param periodType the period type.  i.e. day, month, quarter
	 * @return the new current amount after the move
	 */
	public DateSC goPreviousTimePeriod(PeriodType periodType)
	{
		this.addTimePeriod(periodType, -1);
		return new DateSC(this.getTime());
	}
	
	
	/**
	 * move forward by one unit of a period type
	 * @param periodType the period type.  i.e. day, month, quarter
	 * @return the new current amount after the move
	 */
	public DateSC goNextTimePeriod(PeriodType periodType)
	{
		this.addTimePeriod(periodType, 1);
		return new DateSC(this.getTime());
	}
	
	
	/**
	 * add positive or negative amount of an specified period type to current amount
	 * @param periodType type of period, i.e. DAY, or QUARTER
	 * @param amount number of units, can be a negative value
	 */
	public void addTimePeriod(PeriodType periodType, int amount)
	{
		switch (periodType) {
			case DAY: {
				this.add(Calendar.DAY_OF_MONTH, amount);
			} break;
			case WEEK: {
				this.add(Calendar.WEEK_OF_MONTH, amount);
			} break;
			case MONTH: {
				this.add(Calendar.MONTH, amount);
			} break;
			case QUARTER: {
				this.add(Calendar.MONTH, amount*3);
			} break;
			case SEMESTER: {
				this.add(Calendar.MONTH, amount*6);
			} break;
			case YEAR: {
				this.add(Calendar.YEAR, amount);
			} break;
		}		
	}

	
	/**
	* Adelanta el calendario un numero especifico de dias 			 
	* @return retorna una variable de tipo DateSC la cual encapsula la nueva fecha
	*/   	
	public DateSC goToDate(int amount)
	{
		DateSC result;
		SimpleDateFormat format = this.getDateFormat();
		this.addTimePeriod(PeriodType.DAY, amount);
		result = new DateSC(this.getTime());

      if (format != null )
		result.setFormat( format.toPattern());
		return result;
	}


	/**
	* Crea una lista con los dias de la semana de una fecha			 
	* @return retorna una variable de tipo List con cada uno de los dias de la semana 
	*/   					
	public List daysOfWeek()
	{		   	
		return this.datesOfPeriod(PeriodType.WEEK, PeriodType.DAY);
	}
	
	
	/** move current date to the last day of the given period, based on the current date
	 * @param period the period type
	 */
	public void gotoLastDayOfPeriod(PeriodType period)
	{
		switch (period) {
			case DAY: break;
			case WEEK: {
				this.set(Calendar.DAY_OF_WEEK, this.getFirstDayOfWeek());
				this.add(Calendar.DAY_OF_WEEK, 6);
			} break;
			case MONTH: {
				this.add(Calendar.MONTH, 1);
				this.set(Calendar.DAY_OF_MONTH, 0);
			} break;
			case QUARTER: {
				int month = this.get(Calendar.MONTH);
				this.set(Calendar.MONTH, (month / 3+1) * 3);
				this.set(Calendar.DAY_OF_MONTH, 0);
			} break;
			case SEMESTER: {
				int month = this.get(Calendar.MONTH);
				this.set(Calendar.MONTH, (month / 6+1) * 6);
				this.set(Calendar.DAY_OF_MONTH, 0);
			} break;
			case YEAR: {
				this.add(Calendar.YEAR,1);
				this.set(Calendar.MONTH, 0);
				this.set(Calendar.DAY_OF_MONTH, 0);
			} break;
			default:
				// TODO: i18n: ErrMsg
				throw new RuntimeException("Program error in gotoLastDayOfPeriod()");
		}
	}
	
	
	/** move current amount to the first day of the given period, based on the current amount
	 * @param period the period type
	 */
	public void gotoFirstDayOfPeriod(PeriodType period)
	{
		switch (period) {
			case DAY: break;
			case WEEK: {
				this.set(Calendar.DAY_OF_WEEK, this.getFirstDayOfWeek());
			} break;
			case MONTH: {
				this.set(Calendar.DAY_OF_MONTH, 1);
			} break;
			case QUARTER: {
				int month = this.get(Calendar.MONTH);
				this.set(Calendar.MONTH, (month / 3) * 3);
				this.set(Calendar.DAY_OF_MONTH, 1);
			} break;
			case SEMESTER: {
				int month = this.get(Calendar.MONTH);
				this.set(Calendar.MONTH, (month / 6) * 6);
				this.set(Calendar.DAY_OF_MONTH, 1);
			} break;
			case YEAR: {
				this.set(Calendar.MONTH, 0);
				this.set(Calendar.DAY_OF_MONTH, 1);
			} break;
			default:
				// TODO: i18n: ErrMsg
				throw new RuntimeException("Program error in gotoFirstDayOfPeriod()");
		}
	}
	
	
	/**
	 * @return
	 */
	public SimpleDateFormat getDateFormat()
	{
		return this.dateFormat; 
	}

	/**
	 * @param format
	 */
	public void setDateFormat(SimpleDateFormat format)
	{
		this.dateFormat = format;
	}
    
	
	@Override
    public Object clone()
    {
        ActivityRecordCalendar copy;
        
        copy = (ActivityRecordCalendar) super.clone();
        
        if ( this.dateFormat != null )
            copy.dateFormat = (SimpleDateFormat) this.dateFormat.clone();
        
        return copy;
    }

}
