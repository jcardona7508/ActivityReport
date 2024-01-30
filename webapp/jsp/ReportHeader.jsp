<!--/*
    *    DATE         VERSION     AUTHOR      DESCRIPTION
    *   02/10/10      1.1           CAP       Refactor to fix Style issues
    *
    */
 */-->
<%@ page info="Report Header"
         import="java.text.SimpleDateFormat,
         activityrecord.ract.ActivityRecordCalendar,
	    activityrecord.ract.ActivityReportResource,
         activityrecord.ract.ActivityRecordCalendar,
         activityrecord.ract.WeekDetail,
         siscorp.system.DateSC,
         common.user.User,
         java.text.SimpleDateFormat,
         java.text.ParseException,
         siscorp.system.DateSC,
         java.util.logging.Level,
         java.util.logging.Logger,
         common.web.ClientInfo,
	    common.util.ResourceString,
         java.util.Locale,
         activityrecord.data.RactDAO"
         %>
<%!    ActivityReportResource resource;
    ActivityRecordCalendar calendar;
    ResourceString localeTexts;
    User user;
    String summaryImage;
    String fecha;
    int resolution,srcHeight;
    ClientInfo clientInfo;
    WeekDetail activities;
    int value;
    String summaryImageMap;
    Locale locale;
    String dateFormatStr;
    String currentDateStr;
    String inputValuesType;
    DateSC currTime;
    String contextPath;
    Logger logger = Logger.getLogger( "siscorp.registroactividades" );
%>

<%		
        response.addHeader( "Cache-Control", "no-cache" );
        response.addHeader( "Cache-Control", "no-store" );

        resource = ( ActivityReportResource ) session.getAttribute( "RESOURCE" );
        localeTexts = ( ResourceString ) session.getAttribute( "RESOURCESTRING" );
        user = ( User ) session.getAttribute( "USER" );
        logger.log( Level.INFO, "**** > ReportHeader.jsp "+user.getLoginName()+": "+RactDAO.getCurrentDateAndTime() );
        summaryImage = ( String ) session.getAttribute( "SUMMARYIMAGE" );
        summaryImageMap = ( String ) session.getAttribute( "SUMMARYMAP" );
        clientInfo = ( ClientInfo ) session.getAttribute( "CLIENTINFO" );
        activities = ( WeekDetail ) session.getAttribute( "ACTIVITIES" );
        String lastBigUnit = ( String ) session.getAttribute( "lastBigUnit" );
        String lastMediumUnit = ( String ) session.getAttribute( "lastMediumUnit" );
        String lastSmallUnit = ( String ) session.getAttribute( "lastSmallUnit" );
        String nextBigUnit = ( String ) session.getAttribute( "nextBigUnit" );
        String nextMediumUnit = ( String ) session.getAttribute( "nextMediumUnit" );
        String nextSmallUnit = ( String ) session.getAttribute( "nextSmallUnit" );

        locale = ( Locale ) session.getAttribute( "LOCALE" );

        resolution = Integer.parseInt( clientInfo.getScreenWidth());
        srcHeight = Integer.parseInt( clientInfo.getScreenHeight());
        DateSC date;

        try
        {
            value = resolution;
        } catch ( NumberFormatException e )
        {
            value = 800;
        }
        
        if ( value >= 1024 )
        {
            value = 1024;
        } else
        {
            value = 800;
        }

        dateFormatStr = ( String ) session.getAttribute( "DATEFORMAT" );

        if ( dateFormatStr == null )
        {
            dateFormatStr = "yyyy/MM/dd";
        }

        currentDateStr = ( String ) session.getAttribute( "CURRENTDATE" );
        if ( currentDateStr != null )
        {
            try
            {
                currTime = new DateSC( currentDateStr, dateFormatStr );
            } catch ( ParseException ex )
            {
                Logger.getLogger( "activityrecord.web.jsp" ).log( Level.SEVERE, null, ex );
                currTime = new DateSC();
                currTime.setFormat( dateFormatStr );
            }
        }

        calendar = new ActivityRecordCalendar( currTime );
        calendar.setDateFormat( new SimpleDateFormat( dateFormatStr ) );

        if ( calendar != null )
        {
            date = calendar.getCurrentTime();
        } else
        {
            date = new DateSC();
        }

        date.setFormat( "MMMMM yyyy" );
        date.setLocale( locale );
        fecha = date.toString();
        fecha = fecha.substring( 0, 1 ).toUpperCase() + fecha.substring( 1 );

        if ( resource.acceptPercentages() )
        {
            inputValuesType = localeTexts.getString( "htmlHeader.PercentInputMode" );
        } else
        {
            inputValuesType = localeTexts.getString( "htmlHeader.HoursInputMode" );
        }

        contextPath = request.getContextPath();
%>
<html>

    <head>
        <title><%=localeTexts.getString( "application.title" )%></title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <meta http-equiv="Cache-Control" content="no-cache">
        <link rel="stylesheet" href="/activityreportdoc/css/estilogauss.css" type="text/css">
        <% if ( resolution > 1024 && srcHeight >= 720)
                {%>
         <link rel="stylesheet" href="/activityreportdoc/css/estilogauss1024.css" type="text/css">
         <!--[if IE]><link rel="stylesheet" href="/activityreportdoc/css/estilogaussexp1024.css" type="text/css"/><![endif]-->
        <%}
         else
         {%>
        <link rel="stylesheet" href="/activityreportdoc/css/estilogauss800.css" type="text/css">
        <!--[if IE]><link rel="stylesheet" href="/activityreportdoc/css/estilogaussexp800.css" type="text/css"/><![endif]-->
        <%}%>
        <script type="text/javascript" language="JavaScript1.3" src="/activityreportdoc/js/languages.js"></script>
        <script type="text/javascript" language="JavaScript1.3">CurrentLang=<%=localeTexts.getString( "javascriptLocale" )%>;</script>
        <script type="text/javascript" language="JavaScript1.3" src="/activityreportdoc/js/window.js"></script>
        <script type="text/javascript" language="JavaScript1.3" src="/activityreportdoc/js/utils.js"></script>
        <script type="text/javascript" src="/activityreportdoc/js/jquery.js" ></script>
        <script type="text/javascript" src="/activityreportdoc/js/header.js"></script>
        <script type="text/javascript" src="/activityreportdoc/js/price.js" ></script>

	<script type="text/javascript" language="JavaScript1.3">
            function abrirVentana (pagina, alto, ancho, superior, iquierda)
            {
                var opciones;
                opciones = "toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=no,resizable=no,width="+ancho+",height="+alto+",top="+superior+",left="+iquierda;
	
                window.open(pagina,"",opciones);
            }

            function doSelectResource()
            {
                parentWindow = window.parent;
                parentWindow.frames['detail'].doSetResource();
                return false;
            }
            function saveOK(){
                removeLoader();
                $(".editedValue", top.frames["detail"].document).removeClass("editedValue");
            }
            function showLoader(){
                $("#loader", top.frames["detail"].document).removeClass("displayOff");
            }
            function removeLoader(){
                $("#loader", top.frames["detail"].document).addClass("displayOff");
            }
            function doUpdatePrice(){
                var data = {};
                var actividades, orderBy;

                if( $("#proyectCode", top.frames["detail"].document).length!=0 )
				{
                    data.proyecto=$("#proyectCode", top.frames["detail"].document)[0].value;
                    data.actividades=[];
                    actividades=$("#activitiesTable tr :input", top.frames["detail"].document).not("input[type='hidden']");
                    for( var i=0; i<actividades.length ;i+=2 )
					{
                        var activity={};
                        activity.actividad=actividades[i].id.substring(4);
                        activity.costo=actividades[i].value;
                        activity.qtty=actividades[i+1].value;
                        activity.contratado=actividades[i].value*actividades[i+1].value;
                        data.actividades.push(activity);
                    }                    
                    orderBy=$("#orderBy", top.frames["detail"].document)[0].value;
                    doRequest('/ActivityReport/do/saveProyectPriceActivities?orderBy='+orderBy, "POST", saveOK, data );
                    showLoader();
                }
            }
        </script>

    </head>

    <body  bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
        <table width="100%" cellspacing="0" cellpadding="0" class="p">
            <tr>
                <td>

                    <table width="100%" border="0" cellspacing="0" cellpadding="4" class="b">
                        <tr >
                            <td width="8%" class="t" valign="middle">
                                <%=localeTexts.getString( "object.ResourceCaption" )%>:
                            </td>
                            <td width="10%" valign="middle">
                                <input type="text" name="textfield" value="<%=resource.getCode()%>" class="by2" size="23" readonly="true">
                            </td>
                            <%
                                    if ( user.hasAccess( "PRACT" ) )
                                    {
                            %>
                            <td width="3%" valign="middle">
                                <a href=# onclick="javascript:doSelectResource()" class="bc">
                                    <img alt="" src="/activityreportdoc/images/search.png" width="13" height="12" border="0">
                                </a>
                            </td>
                            <%} else
                    {%>
                            <td width="3%" valign="middle">&nbsp;</td>
                            <%}%>
                            <td width="19%">
                                <input type="text" name="textfield2" size="40" value="<%=resource.getName()%>" class="bgg" readonly="true">
                            </td>
                            <td width="1%" valign="middle">&nbsp; </td>
                            <td width="9%" valign="middle">
                                <input type="text" style="text-align: center;" name="textfield4" size="8" value="<%=inputValuesType%>" class="bgg" readonly="true">
                            </td>
                            <td width="1%" valign="middle">&nbsp;</td>
                            <td width="9%" class="t"> <%=localeTexts.getString( "object.UserCaption" )%>:</td>
                            <td width="10%" valign="middle">
                                <input type="text" name="textfield3" class="by1" value="<%=user.getLoginName()%>" readonly="true">
                            </td>
                            <td width="16%">&nbsp;</td>
                            <% if( (resource.getCode().equals( "CONTRACTS" ))||(resource.getCode().equals( "PRICE_LIST" )) )    	    
    						{%>
                            	<td width="7%" align="center" class="vm" ><a id="updateRact" href="javascript:doUpdatePrice()" target="_top" class="bc"><img alt="" src="/activityreportdoc/images/check.png" align="middle" border="0"></a></td>
                            <%}
                            else{%>
                            	<td width="7%" align="center" class="vm" ><a id="updateRact" href="javascript:doUpdateRact()" target="_top" class="bc"><img alt="" src="/activityreportdoc/images/check.png" align="middle" border="0"></a></td>
                            <%}%>
                            <td width="7%" align="center" class="vm"> <a href=# onclick="javascript:abrirVentana('<%=contextPath%>/do/help?contexto=description',screen.availHeight-100,screen.availWidth-100,50,50)" class="bn">
                                    <img alt=""  src="/activityreportdoc/images/help.png" align="middle" border="0" ></a></td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td>
                    <table width="100%" border="0" cellspacing="0" cellpadding="4" class="b">
                        <tr>
                            <td width="2%" align="center">
                                <a href=# onclick="javascript:abrirVentana('<%=contextPath%>/do/calendar',250,266,150,200)"
                                   class="bn"><img alt="" src="/activityreportdoc/images/scheduler.png" width="16" height="15" border="0"></a>
                            </td>
                            <td width="20%" class="f" nowrap><%=fecha%></td>
                            <td width="10%" nowrap align="right">
                                <a name="backmonth" href="javascript:changeDate( 'backmonth' ) "
                                   target="_top" class="bday"><%=lastBigUnit%></a></td>
                            <td width="10%" nowrap align="left">
                                <a name="backweek" href="javascript:changeDate( 'backweek' ) "
                                   target="_top" class="bday"><%=lastMediumUnit%></a></td>

                            <% if ( activities.getDayCount() == 1 )
                                    {
                            %><td width="10%" nowrap align="left">
                                <a name="backday" href="javascript:changeDate( 'backday' ) "
                                   target="_top" class="bday"><%=lastSmallUnit%></a></td>
                                <% } else
                                 {
                                %><td width="10%" nowrap align="left">&nbsp;</td>
                            <%}%>

                            <td width="10%" nowrap align="left"></td>
                            <td width="1%" nowrap align="right">&nbsp;</td>
                            <td width="1%" nowrap align="right">&nbsp;</td>
                            <td width="2%" nowrap align="right">&nbsp;</td>
                            <% if ( activities.getDayCount() == 1 )
                                    {
                            %><td width="10%" nowrap align="right">
                                <a name="nextday" href="javascript:changeDate( 'nextday' ) "
                                   target="_top" class="bday"><%=nextSmallUnit%></a></td>
                                <%
                                 } else
                                 {
                                %><td width="10%" nowrap align="right">&nbsp;</td>
                            <%}%>
                            <td width="10%" nowrap align="right">
                                <a name="nextweek" href="javascript:changeDate( 'nextweek' ) "
                                   target="_top" class="bday"><%=nextMediumUnit%></a></td>
                            <td width="10%" nowrap align="left">
                                <a name="nextmonth" href="javascript:changeDate( 'nextmonth' ) "
                                   target="_top" class="bday"><%=nextBigUnit%></a></td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
        <table border="0" width="100%">
            <tr>
                <td>
                    <img alt="" src="<%=contextPath%>/do/getImage?Image=<%=summaryImage%>" usemap="#summaryMap" />
                    <%logger.log( Level.INFO, "**** < ReportHeader.jsp "+user.getLoginName()+": "+RactDAO.getCurrentDateAndTime() ); %>
                    <map id ="summaryMap" name="summaryMap"><%=summaryImageMap%></map>
                </td>
            </tr>
        </table>
        <div id="mask"></div>       
    </body>
</html>
