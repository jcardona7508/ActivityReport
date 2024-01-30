package activityrecord.html;

import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Locale;

import activityrecord.ract.Activity;
import activityrecord.ract.ActivityRecordCalendar;
import activityrecord.ract.ActivityReportResource;
import activityrecord.ract.DailyDetail;
import activityrecord.ract.PeriodType;
import activityrecord.ract.WeekDetail;
import common.util.ResourceString;
import java.text.NumberFormat;
import java.util.ArrayList;
import com.gausssoft.GausssoftException;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import siscorp.system.DateSC;
import siscorp.webmanagement.HTMLBuffer;
import siscorp.webmanagement.HTMLContentSC;
import siscorp.webmanagement.HTMLPrintWriterBuffer;
import siscorp.webmanagement.HTMLStringBuffer;

/**
 * @author rbotero
 * Created on 11/09/2004
 * Heavily modified by RSarmiento in October/2008
 */
public class HTMLActivityDetail implements HTMLContentSC
{

    WeekDetail detail;
    Locale locale;
    private DateSC minDate;
    private DateSC maxDate;
    private ActivityReportResource resource;
	// the current system parameters for RACT (were taken from PARA table)
	private Map<String,String> parameters;
	// points to the set of locale texts, for i18n
	private ResourceString localeTexts;
	// number of custom columns to show. Don't remove this; it is DIFFERENT from customcolumns.size()
	private int customCount;
	ArrayList<CustomColumn> customColumns;


	// express a value as an amount or as an hour:minute
	private String expressValue(Float value)
	{
		if (this.resource.getView().equals( PeriodType.DAY ))
			return (new FormatHour( value ).toString() );
        else
			return ( new FormatValue( value ).toString() );
	}


	public int getCustomCount()
	{
		return this.customCount;
	}

	public int getVisibleCustomCount()
	{
		if (this.detail.getDayCount( ) == 1)
			return this.customCount;
		else
			// REMINDER: custom columns not allowed yet in 5day or 7day modes
			return 0;
	}


	// encapsulates a set of properties given a row level
	private class RowAttributes
	{
		public String styleLabel;
		public String styleSeparator;
		public String styleData;
		public String styleReadonly;
		public String styleTotal;
		public int level;
		public boolean isParent;
		public boolean isLastInItsGroup;
		public String activityAcuName;


		public RowAttributes(int dayCount, Activity activity, Activity nextActivity)
		{
			String rowTypeStyle;

			isParent = ( activity.getRegistros( ) > 0 );
			// row types (T: totals (0),  S: level 1, D: level 2, U: level 3)
			this.level = "TSDU".indexOf(activity.getType());
			if (isParent)
				rowTypeStyle = "P"+this.level;
			else
				rowTypeStyle = "L"+this.level;

			this.styleLabel = "la"+rowTypeStyle+"NO";
			this.styleSeparator = "la"+rowTypeStyle+"SE";
			this.styleData = "da"+rowTypeStyle+"ED";
			this.styleReadonly = "da"+rowTypeStyle+"RO";
			if (dayCount > 1)
				this.styleTotal = "da"+rowTypeStyle+"TO";
			else
				this.styleTotal = "da"+rowTypeStyle+"ED";

			switch (this.level) {
				case 1: {
					this.activityAcuName = activity.getAcua().trim();
					break;
				}
				case 2: {
					this.activityAcuName = activity.getAcuo().trim();
					break;
				}
				case 3: {
					this.activityAcuName = activity.getAcu4().trim();
					break;
				}
				default:
					this.activityAcuName = "?????";		// throw an exception instead ?
			}

			this.isLastInItsGroup = true;
			if ( (nextActivity != null) && ("TSDU".indexOf(nextActivity.getType()) == this.level))
				switch (this.level) {
					case 1: {
						this.isLastInItsGroup = ! this.activityAcuName.equals(
							nextActivity.getAcua().trim() );
						break;
					}
					case 2: {
						this.isLastInItsGroup = ! this.activityAcuName.equals(
							nextActivity.getAcuo().trim() );
						break;
					}
					case 3: {
						this.isLastInItsGroup = ! this.activityAcuName.equals(
							nextActivity.getAcu4().trim() );
						break;
					}
				}
		}
	}


	private class CustomColumn
	{
		public String title;
		public String type;
		public boolean nullable;
		public int count = 0;
		public String[] options;
		public String tdClassName;
		public int index;
        protected String maxLength;

		public CustomColumn(Map<String,String> parameters, String title, int index)
		{
			String tmpValue;
			int startIndex = 0;

			this.title = title;
			this.index = index;

            this.maxLength = parameters.get("CUSTOM_FIELD_MAX_LENGTH");
            try {
                Integer.parseInt(maxLength);
            }
            catch (NumberFormatException excep)
            {
                // this was the maximum field width on 21/aug/2009
                maxLength = "30";
            }
                
			tmpValue = parameters.get("CUST_"+index+"_TYP");
			if (tmpValue == null)
				this.type = "T";
			else
				this.type = tmpValue.trim();
			tmpValue = parameters.get("CUST_"+index+"_NUL");
			if (tmpValue == null)
				this.nullable = true;
			else
				this.nullable = tmpValue.trim().equals("T");
			if (this.type.equals("L")) {
				tmpValue = parameters.get("CUST_"+index+"_LST_CNT");
				if (tmpValue == null) {
					this.count = 0;
				} else {
					this.count = Integer.parseInt(tmpValue);
					if (this.nullable) {
						this.count++;
						startIndex = 1;
					}
					this.options = new String[this.count];
					if (this.nullable)
						this.options[0] = "";
					for (int j=startIndex; j < this.count; j++)
                    {
						this.options[j] = parameters.get("CUST_"+index+"_LST_"+(j+1-startIndex));
                        this.options[j] = this.options[j].substring(0, 
                            Math.min( Integer.parseInt(maxLength),this.options[j].length() ));
						if (this.options[j] == null)
							/* TODO: FUTURE - CORRECTNESS - should inform administrator about this:
							 * An option doesn't exist and LST_CNT says it does */
							this.options[j] = "<CFG ERROR>";
					}

				}
			}
		}


		// will generate the html contents for the custom column cell:
		private String getEditElement(Activity dailyActivity, boolean readOnlyCell, int j,
			int colIndex, RowAttributes row, int dayWidth, int rowIndex)
		{
			String contents;
			String value = "";
			String attributes;
			String htmlClassName;
			String idTag = "id=\"cust" + (rowIndex) + "-" + (this.index) + "\"";
			String idTagCheck = "id=\"" + idTag.substring(5);
			String nameTag = "name=\"cust"+(this.index)+"\"";
			String nameTagCheck = "name=\"" + nameTag.substring(7);
			String acceptNulls;

			if (this.nullable)
				acceptNulls = "true";
			else
				acceptNulls = "false";
			if (dailyActivity.hasCustomField(this.index-1))
				value = dailyActivity.getCustomFieldValue(this.index-1);
			else
				readOnlyCell = true;
			// NOTE: when type is L and is readonly, it will show an input element, is faster.
			if (this.type.equals("L") && !readOnlyCell) {
				contents = "<select size=1 "+idTag+" class=\"cussl\" "+nameTag+">";
				for (int p=0; p<this.options.length; p++)
					if (this.options[p].equalsIgnoreCase(value))
						contents += "<option selected>" + this.options[p] + "</option>";
					else
						contents += "<option>" + this.options[p] + "</option>";
				contents +="</select>";
			}
			else
			if (this.type.equals("B"))
			{
				String ronly;
				if (readOnlyCell)
					ronly = "class=\"cusckRO\" onclick=\"this.checked=!this.checked;\" ";
				else
					ronly = "class=\"cusck\" ";
				contents = "<input "+nameTagCheck+" "+idTagCheck+" "
					+ ronly+"type=\"checkbox\" value=\"T\" ";
				if (value.equals("T"))
					contents += " checked>";
				else {
					contents += ">";
					value = "F";
				}
				// hidden input for each checkbox
				contents +="<input "+nameTag+" "+idTag+" type=\"hidden\" value=\""+value+"\">";
			}
			else
			{
				if (readOnlyCell)
					htmlClassName = "class=\"cusRO\" readonly";
				else
					htmlClassName = "class=\"cus\"";
				if (this.type.equals("N"))
					attributes = "onBlur=\"validarDecimal(this, "+acceptNulls+")\" maxlength=15";
				else {
					if (! this.nullable)
						attributes = "onBlur=\"unselectCustom(this)\" maxlength=" + this.maxLength;
					else
						attributes = "maxlength=" + this.maxLength;
				}
				contents = "<input type=\"text\" " +htmlClassName+" "+nameTag
					+" "+idTag+" value=\""+value+"\" "+attributes
					+" size=\"" + (dayWidth - 1) + "\" />";
			}
			if (readOnlyCell)
				this.tdClassName = row.styleReadonly;
			else
				this.tdClassName = row.styleData;
			return (contents);
		}

	}


	/** constructor for the activity detail html table creator
	 * @param resource the current resource to whom/which the details belong
	 * @param activities
	 * @param locale current language settings
	 * @param minDate
	 * @param maxDate
	 * @param localeTexts set of locale strings
	 * @param parameters system parameters for RACT (these typically come from PARA table)
	 */
    public HTMLActivityDetail( ActivityReportResource resource, WeekDetail activities,
		Locale locale, DateSC minDate, DateSC maxDate, ResourceString localeTexts,
		Map parameters)
    {
        super( );
        this.detail = activities;
        this.locale = locale;
		this.localeTexts = localeTexts;

        this.minDate = new DateSC( minDate );
        this.maxDate = new DateSC( maxDate );
        this.resource = resource;
		this.parameters = parameters;
        
        Logger logger = Logger.getLogger( "siscorp.activityrecord.html" );
        logger.log (Level.FINER, "HTMLActivityDetail.minDate: " + minDate.toString() );
        logger.log (Level.FINER, "HTMLActivityDetail.maxDate: " + maxDate.toString() );

		this.customColumns = new ArrayList<CustomColumn>();

		// CAUTION: this allows to have custom columns only if the user is in 1-day mode:
//		if (this.detail.getDayCount( ) == 1) {
			DailyDetail weekSummary = this.detail.getWeekSummary( );
			for (int i=1; i <= 5; i++) {
				String title = this.parameters.get("CUST_" + i + "_TIT");
				if ((title == null) || (title.trim().equals("")))
					continue;
				boolean addColumn = false;
				for (int j = 1; j < this.detail.getWeekSummary( ).size( ); j++ ) {
					if ( weekSummary.getActivity( j ).hasCustomField(i-1) ) {
						addColumn = true;
						break;
					}
				}
				if ( addColumn )
					customColumns.add(new CustomColumn(parameters, title, i));
			}
			this.customCount = customColumns.size();
//		} else {
//			this.customCount = 0;
//		}
    }


    public String getHTMLContent( ) throws Exception
    {
        StringBuffer buffer = new StringBuffer( 1000 );

        this.getHTMLContent( new HTMLStringBuffer( buffer ) );

        return buffer.toString( );
    }


    public void getHTMLContent( PrintWriter out) throws Exception
    {
        this.getHTMLContent( new HTMLPrintWriterBuffer( out ) );
    }


	// inner method called from getHTMLContent
	void appendByDayType(HTMLBuffer buffer, int dayOfWeek, String color, String classType)
	{
		if (dayOfWeek == Calendar.SATURDAY)
				classType +="SA";
		else
			if (dayOfWeek == Calendar.SUNDAY)
				classType +="SU";
			else
				classType +="DT";
		buffer.append( "<td style=\"color:" + color + "\" class=\""+classType+"\">" );
	}


	void appendLineTD(HTMLBuffer buffer, String text, String className, String extraAttributes)
	{
		if (text == null)
			text = "";
		if (extraAttributes == null)
			extraAttributes = "";
		if ((className != null) && (className.length() > 0))
			buffer.appendLine("<td class=\""+className+"\" "+extraAttributes+">"
				+ text + "</td>");
		else
			buffer.appendLine("<td "+extraAttributes+">" + text + "</td>" );
	}

	/** will create the Html text for a hidden input control
	 * @param name name of the html hidden input control
	 * @param value the value for the html control, must be applied verbatim (no trim(), etc.)
	 * @param id the id attribute for the html hidden input control
	 * @return the html text that represents the hidden input control
	 */
	private String getHiddenInput(String name, String value, String id)
	{
		if (id == null)
			return ("<input type=\"hidden\" name=\""+name+"\" value=\""+value+"\">");
		else
			return("<input type=\"hidden\" name=\""+name+"\" value=\""+value+"\" id=\""+id+"\">");
	}


    public void getHTMLContent( HTMLBuffer buffer ) throws GausssoftException
    {
		int[][] cellWidths = {{10,10,10,9,9,9,8},{},{},{},{8,8,8,7,7,7,6},{},{7,7,7,7,8,8,8}};
        Logger logger = Logger.getLogger("siscorp.activityrecord.html");

        Activity activity;
        Activity dailyActivity;
        DateSC date;
        int dayCount = this.detail.getDayCount( );
        int objetos = 0;
        Float valor;
        DateSC currentDate;
        int dayWidth = 0;
        int detailWidth = 0;
        String comentario;
		String contents;
		ActivityRecordCalendar calendar;
		int prevRowLevel = 0;
		int lastColumn = 0;
		String level1Title, level2Title, level3Title;
		boolean showItemCodes;
		// indicates if the parameter RACT-DOBL is T (true) or F (false)
		String doubleValues;
        
        logger.log (Level.FINER, "HTMLActivityDetail.getHTMLContent().resource.View: " + resource.getView().toString() );
        logger.log (Level.FINER, "HTMLActivityDetail.getHTMLContent().dayCount: " + dayCount );

		level1Title = this.parameters.get("LEV2");
		if (level1Title == null)
			level1Title = "<LEV2 ERROR>";
	    level2Title = this.parameters.get("LEV3");
		if (level2Title == null)
			level2Title = "<LEV3 ERROR>";
	    level3Title = this.parameters.get("LEV4");
		if (level3Title == null)
			level3Title = "<LEV4 ERROR>";
		if ( (this.parameters.get("DOBL") != null)
		&& (this.parameters.get("DOBL").equalsIgnoreCase("T")) )
			doubleValues = "T";
		else
			doubleValues = "F";

		String level_codes = this.parameters.get("LEV_CODES");
		if (level_codes == null)
			showItemCodes = true;
		else
			showItemCodes = level_codes.equalsIgnoreCase("T");

		if (dayCount == 1) {
			dayWidth = cellWidths[dayCount-1][this.getCustomCount()];
			detailWidth = 100-14-dayWidth*(dayCount+this.getCustomCount());
			lastColumn = detailWidth / 2;
			if (lastColumn*2 < detailWidth )
				detailWidth = lastColumn +1;
			else
				detailWidth = lastColumn;
		}
		else
		{
			dayWidth = cellWidths[dayCount-1][0];
			lastColumn = dayWidth;
			detailWidth = 100-14-dayWidth*(dayCount)-lastColumn;
		}

		// <editor-fold desc="COMMENTS BLOCK, FOR WEEKLY MODE (5day/7day) ">
        if ( dayCount > 1 )
        {
//            buffer.appendLine( "<table width=\"98%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">" );
//            buffer.appendLine( "<tr>" );
//
//			appendLineTD(buffer, this.localeTexts.getString("htmlDetail.Comment"), "ct", "");
//
//			appendLineTD(buffer, "&nbsp;", null, "");
//            buffer.appendLine( "<td width=\"67%\" class=\"c\">" );
//            buffer.appendLine( "<input type=\"text\" id=\"comewk\" name=\"comentario\" class=\"comewk\" size=\"100\" maxlength=\"250\">" );
            buffer.appendLine( " <div id=\"comment_window\"><div id=\"header\"><table width=\"100%\"><tr><td><b><p align=\"left\" style=\"color:white\">Comment</b></p></td>"+
            "<td align=\"right\"><img src=\"/activityreportdoc/images/close_comment.PNG\" id=\"cl_com\" alt=\"Close comment window\"/></td></tr></table></div></div>" );
            buffer.appendLine( "<input class=\"in\" style=\"display:none\" id=\"float_input\" class=\"comment\" type=\"text\"/>");
//            buffer.appendLine( "</td>" );
//            buffer.appendLine( "</tr>" );
//            buffer.appendLine( "</table>" );
//            buffer.appendLine( "<br>" );
        }
		// </editor-fold>

        // START OF THE ACTIVITY DETAIL TABLE:
		buffer.appendLine( getHiddenInput("doubleValues", doubleValues, "doubleValues") );
        buffer.appendLine( "<table id=\"tabledetail\" width=\"100%\" border=\"0\" "
			+"cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse: separate\" >" );

		// column widths row:
		int[] labelCols = {2,1,1,1,1,1,2,2,2,detailWidth};
		buffer.appendLine(getWidthsRow(labelCols, this.getVisibleCustomCount(), dayCount,
			dayWidth, lastColumn, 1));

        // LAS ACTIVIDADES

		// <editor-fold desc="FIRST TITLE LINE OF DETAIL TABLE">
        buffer.appendLine( "<tr>" );
		appendLineTD(buffer, "&nbsp;", "tiNB", "colspan=7");
		appendLineTD(buffer, level1Title, "tiLVL1", "colspan=3");

        calendar = new ActivityRecordCalendar( );
        calendar.setDateFormat( new SimpleDateFormat( DateSC.GAUSS_DATE_FORMAT ) );

		// Extra columns:
		for ( int j = 0; j < this.getVisibleCustomCount(); j++) {
			appendLineTD(buffer, "&nbsp;", "tiNB", "");
		}

		// Days columns:
        for ( int j = 0; j < dayCount; j++ )
        {
            date = new DateSC( this.detail.getDailyDetail( j ).getDate( ) );
            date.setFormat( "EEE" );
            date.setLocale( this.locale );
            calendar.setTimeCalendar( date );

            if ( ! ( this.resource.getView().equals( PeriodType.DAY )) ) {
                calendar.gotoFirstDayOfPeriod( this.resource.getView() );

				// Use a work day, so that the background color for big periods is always the same
				this.appendByDayType(buffer, Calendar.WEDNESDAY, "WHITE", "ti");
			} else {
				this.appendByDayType(buffer, calendar.get( Calendar.DAY_OF_WEEK ), "WHITE", "ti");
			}
			
            if( dayCount > 1 )
                buffer.append( "<a onfocus=\"this.hideFocus=true;\" id=\"l-" + j + "\" class=\"day\" href=\"#\">" );

			if ( this.resource.getView().equals( PeriodType.YEAR ) )
				buffer.append( "&nbsp;");
			else
				if ( ! ( this.resource.getView().equals( PeriodType.DAY )) )
					buffer.append( calendar.getTimePeriod( this.resource.getView(), this.locale ) );
				else
					buffer.append( date.toString( ) );

            if( dayCount > 1 )
                buffer.append( "</a>" );

            buffer.appendLine( "</td>" );
        }

        if ( dayCount > 1 )
			// si hay mas de 1 dia entonces se imprime el resumen semanal
			appendLineTD(buffer, this.localeTexts.getString("htmlDetail.week"), "tiTO", "");
        else
			// si solo es un dia se imprime el comentario
			appendLineTD(buffer, "&nbsp;", "tiCO", "");

		appendLineTD(buffer, "&nbsp;", "tiNB", "");
        // fin 1 Linea
        buffer.appendLine( "</tr>" );
		// </editor-fold>

        // <editor-fold desc="SECOND TITLE LINE OF DETAIL TABLE">
        buffer.appendLine( "<tr>" );
		appendLineTD(buffer, "&nbsp;", "tiNB", "colspan=8");
		appendLineTD(buffer, level2Title, "tiLVL2", "colspan=2");

		// Extra columns:
		for ( int j = 0; j < getVisibleCustomCount(); j++) {
			appendLineTD(buffer, this.customColumns.get(j).title, "tiEXTRA", "rowspan=2");
		}

        for ( int j = 0; j < dayCount; j++ )
        {
            date = new DateSC( this.detail.getDailyDetail( j ).getDate( ) );
            date.setFormat( "dd-MMM");
            date.setLocale( this.locale );
            calendar.setTimeCalendar( date );

            if ( ! ( this.resource.getView().equals( PeriodType.DAY )) )
            {
                calendar.gotoFirstDayOfPeriod( this.resource.getView() );
                date = calendar.getCurrentTime();
                date.setFormat( "yyyy" );
				this.appendByDayType(buffer, Calendar.WEDNESDAY, "WHITE", "ti");
            } else {
				this.appendByDayType(buffer, calendar.get( Calendar.DAY_OF_WEEK ), "WHITE", "ti");
			}

            if( dayCount > 1 )
                buffer.append( "<a onfocus=\"this.hideFocus=true;\" id=\"l-" + j + "\" class=\"day\" href=\"#\">" );

            buffer.append( date.toString( ) );

            if( dayCount > 1 )
                buffer.append( "</a>" );

            buffer.appendLine( "</td>" );
        }

        if ( dayCount > 1 )
			appendLineTD(buffer, "&nbsp;", "tiTO", "");
        else
			// si solo es un dia se imprime el titulo de comentario
			appendLineTD(buffer, this.localeTexts.getString("htmlDetail.Comments"), "tiCO", "");
		appendLineTD(buffer, "&nbsp;", "tiNB", "");
        buffer.appendLine( "</tr>" );
		// </editor-fold>

        // <editor-fold desc="THIRD TITLE LINE OF DETAIL TABLE">
        buffer.appendLine( "<tr> " );
		contents = "<img class=\"getChoiceA\" name=\" " + 1 +
					"\" src=\"/activityreportdoc/images/button.png\" width=\"10\" height=\"10\">";
		appendLineTD(buffer, contents, "tiNB", "align=\"center\"");
		appendLineTD(buffer, "&nbsp;", "tiNB", "colspan=8");
		appendLineTD(buffer, level3Title, "tiLVL3", "");

		// Escribe los totales por dia
        for ( int j = 0; j < dayCount; j++ )
        {
            date = new DateSC( this.detail.getDailyDetail( j ).getDate( ) );

            calendar.setTimeCalendar( date );

            if ( ! ( this.resource.getView().equals( PeriodType.DAY )) )
            {
                calendar.gotoFirstDayOfPeriod( this.resource.getView() );
                date = calendar.getCurrentTime();
            }

            date.setFormat(  "yyyy/MM/dd" );

			if ( ! this.resource.getView().equals( PeriodType.DAY) )
				this.appendByDayType(buffer, Calendar.WEDNESDAY, "WHITE", "ti");
			else
				this.appendByDayType(buffer, calendar.get( Calendar.DAY_OF_WEEK ),
					"RGB(248,248,32)", "ti");

            activity = this.detail.getDailyDetail( j ).getActivity( 0 );

            if ( this.resource.getView().equals( PeriodType.DAY) )
                buffer.append( new FormatHour( activity.getCana( ) ).toString( ) );
            else
                buffer.append(  new FormatPercentage( activity.getCana() ).toString() );
			if (doubleValues.equals("T")) {
				buffer.append( " - " + Integer.toString( (int)Math.floor( activity.getCano() ) ) );
			}

			buffer.appendLine(getHiddenInput("currentdate", date.toString( ), null));
            buffer.appendLine( "</td>" );
        }

        if ( dayCount > 1 )
        {
			activity = this.detail.getWeekSummary( ).getActivity( 0 );
			if (this.resource.getView().equals( PeriodType.DAY ))
				contents = new FormatHour( activity.getCana() ).toString();
			else
				contents = new FormatValue( activity.getCana() ).toString();
			appendLineTD(buffer, contents, "tiTO", "");
        }
        else
			appendLineTD(buffer, "&nbsp;", "tiCO", "");

		appendLineTD(buffer, "&nbsp;", "tiNB", "");
        buffer.appendLine( "</tr>" );
		// </editor-fold>

        // <editor-fold desc="DATA LINES OF DETAIL TABLE">
        for (int i = 1; i < this.detail.getWeekSummary( ).size( ); i++ )
        {
			RowAttributes row;
			int colspan;		// used in description column
			String tdClassName;
			boolean readOnlyCell = false;
			Activity nextActivity;

            logger.log (Level.FINER, "HTMLActivityDetail.getHTMLContent().row index (i): " + i );

            activity = this.detail.getWeekSummary( ).getActivity( i );
			if (i < this.detail.getWeekSummary( ).size()-1 )
				nextActivity = this.detail.getWeekSummary( ).getActivity( i+1 );
			else
				nextActivity = null;
			row = new RowAttributes(dayCount, activity, nextActivity);
			// NOTE: this ignores the Total Row type, if by any accident there is one (shouldn't happen)
			if (row.level < 1)
				continue;

			// start new html row:
			buffer.appendLine( "<tr>" );

			// <editor-fold desc="LABEL SECTION OF ROW (LEFT PART)">

			// line number - zeroth column
			appendLineTD(buffer, Integer.toString(i), "laNUM", "");

			if (row.level == 1)
				// Primera columna: borde izquierdo de la fila:
				appendLineTD(buffer, "&nbsp;", "tiRG", "");
			else {
				// primera y segunda columna: borde izquierdo:
				if (row.level == 2)
					appendLineTD(buffer, "&nbsp;", "tiNB", "");
				else
					appendLineTD(buffer, "&nbsp;", "tiNB", "colspan=2");
				if (prevRowLevel == row.level-1)
					appendLineTD(buffer, "&nbsp;", "tiTRG", "");
				else
					appendLineTD(buffer, "&nbsp;", "tiRG", "");
			}

			// add Buttons and hidden inputs
			if (row.isParent) {
				// display "add children" button:
				if (row.level == 1) {
					contents = "<img class=\"getChoiceO\" name=\"" + activity.getAcua()
						+ "\" src=\"/activityreportdoc/images/button.png\" width=\"10\" height=\"10\">";
				} else {	// then is level 2:
					contents = "<img class=\"getChoiceD\" name=\"" + activity.getAcua() + "[|]" + activity.getAcuo()
						+ "\" src=\"/activityreportdoc/images/button.png\" width=\"10\" height=\"10\">";
				}
				appendLineTD(buffer, contents, row.styleLabel, "align=\"center\" colspan=2");
				appendLineTD(buffer, "&nbsp;", row.styleLabel, "colspan=3");
			} else {		// is an editable row, add "Duplicate row" and "Lock" buttons, and hidden inputs
				// TODO: this && false hides the duplicate button, it is not implemented right now:
				if (row.isLastInItsGroup && false)
					contents = "<img class=\"duplicateRow\" name=\"" + row.level + row.activityAcuName
						+ "\" src=\"/activityreportdoc/images/duplicate.png\" width=\"11\" height=\"12\">";
				else
					contents = "&nbsp;";
				if (row.level == 1) {
					appendLineTD(buffer, "&nbsp;", row.styleLabel, "colspan=2");
					contents += getHiddenInput("idactividad", activity.getAcua( ), null) +
							getHiddenInput("idobjeto", activity.getAcua( ), null) +
							getHiddenInput("idobjetoaux", activity.getAcua( ), null);
				} else
					if (row.level == 2) {
						appendLineTD(buffer, "&nbsp;", row.styleLabel, "");
						contents += getHiddenInput("idactividad", activity.getAcua( ), null) +
							getHiddenInput("idobjeto", activity.getAcuo( ), null) +
							getHiddenInput("idobjetoaux", activity.getAcuo( ), null);
					} else {
						contents += getHiddenInput("idactividad", activity.getAcua( ), null) +
							getHiddenInput("idobjeto", activity.getAcuo( ), null) +
							getHiddenInput("idobjetoaux", activity.getAcu4( ), null);
					}
				appendLineTD(buffer, contents, row.styleLabel, "colspan=2");

				if (row.isLastInItsGroup) {
					if ( activity.isFixed( ) )
						contents = "<img src=\"/activityreportdoc/images/rbchk.png\" name=\"imgchk\" class=\"ch\">";
					else
						contents = "<img src=\"/activityreportdoc/images/rbuchk.png\" name=\"imgchk\" class=\"ch\">";
					contents += getHiddenInput("fixed", activity.isFixed( ) ? "T" : "F", "1");
				} else
					contents = "&nbsp;";
				appendLineTD(buffer, contents, row.styleLabel, "align=\"center\"");

				if (row.level == 2)
					appendLineTD(buffer, "&nbsp;", row.styleLabel, "");
				else
					if (row.level == 3)
						appendLineTD(buffer, "&nbsp;", row.styleLabel, "colspan=2");
			}

			// columna de descripción:
			if (showItemCodes)
				contents = row.activityAcuName;
			else
				contents = "";
			if (row.level == 1) {
				contents = contents + " " + activity.getActividad( );
				colspan=3;
			} else
			if (row.level == 2) {
				contents = contents + " " + activity.getObjeto( );
				colspan=2;
			} else {
				contents = contents + " " + activity.getDetail( );
				colspan=1;
			}
			if (colspan > 1)
				appendLineTD(buffer, contents, row.styleSeparator, "colspan="+colspan);
			else
				appendLineTD(buffer, contents, row.styleSeparator, "");

			// </editor-fold>

			// <editor-fold desc="DAY DETAIL COLUMNS">
			dailyActivity = null;
			currentDate = null;
            logger.log ( Level.FINEST, "HTMLActiviyDetail getHTMLContent dayCount " + dayCount );
            for ( int j = 0; j < dayCount; j++ )
            {
                logger.log ( Level.FINEST, "HTMLActiviyDetail getHTMLContent column index (j) " + j );
                logger.log ( Level.FINEST, "HTMLActiviyDetail getHTMLContent minDate: " + this.minDate );
                logger.log ( Level.FINEST, "HTMLActiviyDetail getHTMLContent maxDate: " + this.maxDate );
                dailyActivity = this.detail.getDailyDetail( j ).getActivity( i );
                currentDate = new DateSC( this.detail.getDailyDetail( j ).getDate( ) );
                calendar.setTimeCalendar( currentDate );

                logger.log (Level.FINER, "HTMLActivityDetail.getHTMLContent().currDate(a): " + currentDate.toString() );
                if ( !( this.resource.getView().equals( PeriodType.DAY )) )
                {
                    calendar.gotoFirstDayOfPeriod( this.resource.getView() );
                    currentDate = calendar.getCurrentTime();
                    logger.log ( Level.FINEST, "HTMLActiviyDetail getHTMLContent currenDate moved to currDate(b): " + currentDate);
                }
                logger.log (Level.FINER, "HTMLActivityDetail.getHTMLContent().currDate(c): " + currentDate.toString() );

				readOnlyCell = ( ( currentDate.compareTo( this.minDate ) < 0 ) ||
                    ( currentDate.compareTo( this.maxDate ) > 0 ) );
				
				// <editor-fold desc="EXTRA COLUMNS DETAIL">
				// NOTE: this is done inside the daily details on purpose.
				// NOTE: can be modified when AJAX enters the scene.
				for ( int k = 0; k < this.getVisibleCustomCount(); k++ )
				{
					CustomColumn custom = this.customColumns.get(k);
					if (row.isParent) {
						contents = "&nbsp;";
						tdClassName = row.styleData;
					} else {
						contents = custom.getEditElement(dailyActivity, readOnlyCell,
							j, k, row, dayWidth, i);
						tdClassName = custom.tdClassName;
					}
					appendLineTD(buffer, contents, tdClassName, "");
				}
				// </editor-fold>

				valor = dailyActivity.getCana( );
				if (row.isParent)
				{
					tdClassName = row.styleData;

                    if ( !valor.equals( new Float( 0f ) ) )
                        contents = expressValue(valor);
					else
						contents = "&nbsp;";
                }
                else
                {
					if ( !valor.equals( new Float( 0f ) ) )
                        contents = expressValue(valor);
					else
						contents = "";  // NOTE: it must be exactly "", look at next use:
					if (readOnlyCell)
						tdClassName = "qtyRO";
					else
						tdClassName = "qty";
                    logger.log ( Level.FINEST, "HTMLActiviyDetail getHTMLContent before print horas: " + currentDate);
                    contents = "<input type=\"text\" "
						+ "class=\""+tdClassName+"\" name=\"horas" + currentDate.toString( )
						+ "\" id=\"horas" + i + "-" + (j+this.getVisibleCustomCount()) + "\""
						+ " onkeypress=\"return testForNumericValue(event);\" value=\"" + contents + "\"";
                    logger.log ( Level.FINEST, "HTMLActiviyDetail getHTMLContent after print horas: " + currentDate);

                    if (readOnlyCell)
					{
                        contents += " readonly ";
						tdClassName = row.styleReadonly;
					} else
						tdClassName = row.styleData;
                    contents += " maxlength=\"10\" size=\"" + (dayWidth - 1) + "\" />";

					// add the cano value, even when "double values" is false
					if (doubleValues.equals("T"))
						valor = dailyActivity.getCano( );
					else
						valor = new Float(0);
					String tmpValue = "";
					if ( !valor.equals( new Float( 0f ) ) )
						// NOTE: the space before the value is absolutely necessary
						tmpValue = " " + Integer.toString( ( int ) Math.floor( valor.floatValue( ) ) );
                    logger.log ( Level.FINEST, "HTMLActiviyDetail getHTMLContent before print cano: " + currentDate);
					contents +=getHiddenInput("cano"+ currentDate.toString( ), tmpValue,
						"cano" + i + "-" + (j+this.getVisibleCustomCount()) );
                    logger.log ( Level.FINEST, "HTMLActiviyDetail getHTMLContent after print cano: " + currentDate);
                }

                if ( dayCount > 1 )
                {

                    // TODO what the hell is this code for ?
                    if ( !( ( dailyActivity.getType( ).equals( "S" ) )
						&& ( dailyActivity.getRegistros( ) > 0 ) ) )
                    {
                        logger.log ( Level.FINEST, "HTMLActiviyDetail getHTMLContent before print comentario(a): " + currentDate);
						comentario = dailyActivity.getCome( );
						contents += getHiddenInput("comentario"+currentDate.toString( ),
							comentario == null ? "" : comentario, "come" + i + "-" + j);
                        logger.log ( Level.FINEST, "HTMLActiviyDetail getHTMLContent after print comentario(a): " + currentDate);
                    }
                }
				appendLineTD(buffer, contents, tdClassName, "");
				prevRowLevel = row.level;

                logger.log (Level.FINER, "HTMLActivityDetail.getHTMLContent().currDate(d): " + currentDate.toString() );
            }
			// </editor-fold>

			// <editor-fold desc="WEEK TOTALS OR COMMENTS LAST COLUMN">
			tdClassName = row.styleTotal;
            if ( dayCount > 1 )
            {
                valor = activity.getCana( );

                if ( !valor.equals( new Float( 0f ) ) )
                    contents = expressValue(valor);
                else
                    contents = "&nbsp;";
            } else {
				if (row.isParent)
					contents = "&nbsp;";
				else {
					comentario = dailyActivity.getCome( );
					comentario = ( comentario == null ? "" : comentario );
					// this is the readonly state for the only day in "one day" display mode:
					if (readOnlyCell)
					{
						tdClassName = "comeRO";
						contents = " readonly ";
					} else {
						tdClassName = "come";
						contents = "";
					}

                    logger.log ( Level.FINEST, "HTMLActiviyDetail getHTMLContent after print comentario(b): " + currentDate);
                    contents = "<input"+contents +" type=\"text\" name=\"comentario"
							+ currentDate.toString( ) + "\" id=\"come" + i + "-" + 0 + "\""
							+ " value=\"" + comentario + "\""
							+ " maxlength=\"250\" class=\""+tdClassName+"\">";

                    logger.log ( Level.FINEST, "HTMLActiviyDetail getHTMLContent after print comentario(b): " + currentDate);
					// if last cell is a comment line, and only day is readonly:
					if (readOnlyCell)
						tdClassName = row.styleReadonly;
					else
						tdClassName = row.styleTotal;
				}
                logger.log (Level.FINER, "HTMLActivityDetail.getHTMLContent().currDate(e): " + currentDate.toString() );
			}
			appendLineTD(buffer, contents, tdClassName, "");
			appendLineTD(buffer, "&nbsp;", "tiLG", "");
			// </editor-fold>

            buffer.appendLine( "</tr>" );
            objetos--;
        }
		// </editor-fold>

		// <editor-fold desc="ENDING SECTION OF DETAIL TABLE">
        buffer.appendLine( "<tr>" );
        contents = "<img class=\"getChoiceA\" name=\" " + this.detail.getWeekSummary( ).size( ) + "\""
			+ " src=\"/activityreportdoc/images/button.png\" width=\"10\" height=\"10\">";
        appendLineTD(buffer, contents, "tiNB", "align=\"center\"");
		if (prevRowLevel == 1) {
			appendLineTD(buffer, "&nbsp;", "tiNB", "");
			appendLineTD(buffer, "&nbsp;", "tiTG", "colspan=2");
		} else
			if (prevRowLevel == 2) {
				appendLineTD(buffer, "&nbsp;", "tiNB", "colspan=2");
				appendLineTD(buffer, "&nbsp;", "tiTG", "");
			} else
				appendLineTD(buffer, "&nbsp;", "tiNB", "colspan=3");
		appendLineTD(buffer, "&nbsp;", "tiTG", "colspan="+(7+this.getVisibleCustomCount()+dayCount));
        buffer.appendLine( "</tr>" );
		// </editor-fold>

        buffer.appendLine( "</table>" );
    }


	private String getWidthsRow(int[] labelCols, int extraCount, int dayCount,
		int dayWidth, int lastColumn, int borderCol)
	{
		String st = "%\"/> <td width=\"";
		String edits="";

		for (int i=0; i< extraCount+dayCount; i++)
			edits += dayWidth+st;

		return ("<tr><td width=\"" +labelCols[0]+st +labelCols[1]+st +labelCols[2]+st
			+labelCols[3]+st +labelCols[4]+st +labelCols[5]+st +labelCols[6]+st
			+labelCols[7]+st +labelCols[8]+st +labelCols[9]+st
			+edits +lastColumn+st +borderCol+"\"/></tr>");
	}


	protected class FormatValue
    {
        Float value;

        public FormatValue( Float value )
        {
            this.value = value;
        }

		//@Override
        public String toString()
        {
            DecimalFormat percentageFormat;
			DecimalFormatSymbols symbols;

            percentageFormat = new DecimalFormat("#0.00");
			symbols = percentageFormat.getDecimalFormatSymbols();
			// NOTE: this will force to display DOT as the decimal separator, always
			symbols.setDecimalSeparator('.');
			percentageFormat.setDecimalFormatSymbols(symbols);

            return percentageFormat.format( this.value  );
        }
    }


    protected class FormatPercentage
    {
        Float percentage;

        public FormatPercentage( Float percentage )
        {
            this.percentage =  percentage;
        }

		//@Override
        public String toString()
        {
            NumberFormat percentageFormat;

            percentageFormat = DecimalFormat.getPercentInstance();

            return percentageFormat.format( this.percentage / 100 );
        }

    }


    protected class FormatHour
    {
        Float hour;

        public FormatHour( Float hour )
        {
            this.hour = hour;
        }

		//@Override
        public String toString( )
        {
            double hours;
            double minutes;

            DecimalFormat hourFormat = new DecimalFormat( "0" );
            DecimalFormat minFormat = new DecimalFormat( "00" );

            hours = Math.floor( this.hour.doubleValue( ) );
            minutes = this.hour.doubleValue( ) - hours;

            hours += Math.floor( minutes / 60 );
            minutes -= Math.floor( minutes / 60 );

            return hourFormat.format( hours )
                   + ":"
                   + minFormat.format( 60 * minutes );
            /*double h, m, temp1;
            h = Math.floor( this.hour );
            m = this.hour - h;
            h += Math.floor( m/60 );
            m -= Math.floor( m/60 );
            temp1 = (m*60)-Math.floor(m*60);// Toma la parte decimal
            if(temp1>0.5)
              m = Math.ceil(m*60); //Toma el numero superior
            else
              m = Math.floor(m*60); //Toma el numero inferior
            DecimalFormat hourFormat = new DecimalFormat( "0" );
            DecimalFormat minFormat = new DecimalFormat( "00" );

            return (hourFormat.format(h) + ":" + minFormat.format(m));*/
        }
    }


}
