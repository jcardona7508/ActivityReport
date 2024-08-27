<%@ page info="Daily Report"  buffer="60kb" 
         import="activityrecord.ract.ActivityRecordCalendar,
         activityrecord.ract.ActivityReportResource,
         activityrecord.ract.PeriodType,
         java.text.SimpleDateFormat,
         java.text.ParseException,
         siscorp.system.DateSC,
         java.util.logging.Level,
         java.util.logging.Logger,
         activityrecord.ract.WeekDetail,
         activityrecord.html.HTMLActivityDetail, 
         java.io.PrintWriter,
         java.util.Locale,
         java.util.HashMap,
         java.util.Map,
         common.util.ResourceString,
         common.web.ClientInfo,
         java.util.List,
         activityrecord.ract.ActivityChoiceTO,
         activityrecord.data.RactDAO"%> 
<%!    ActivityRecordCalendar calendar;
    ActivityReportResource resource;
    ResourceString localeTexts;
    String dateFormatString;
    DateSC currentDate;
    WeekDetail activities;
    HTMLActivityDetail htmlDetail;
    int resolution;
    int scrHeight;
    ClientInfo clientInfo;
    Locale locale;
    Map parameters;
    String maxHoras;
    String mode;
    DateSC minDate;
    DateSC maxDate;
    String level2Title, level3Title, level4Title;
    String dateFormatStr;
    String currentDateStr;
    DateSC currTime;
    String contextPath;    
    List<ActivityChoiceTO> proyectos;    
    Logger logger = Logger.getLogger( "siscorp.registroactividades" );    
%>

<%		
        response.addHeader( "Cache-Control", "no-cache" );
        response.addHeader( "Cache-Control", "no-store" );

        clientInfo = (ClientInfo) session.getAttribute( "CLIENTINFO" );
        localeTexts = (ResourceString) session.getAttribute( "RESOURCESTRING" );
        
        
        try
        {
            resolution = Integer.parseInt( clientInfo.getScreenWidth() );
            scrHeight = Integer.parseInt( clientInfo.getScreenHeight());
        }
        catch (NumberFormatException e)
        {
            resolution = clientInfo.getMaxSupportedScreenWidth();
        }
        resource = (ActivityReportResource) session.getAttribute( "RESOURCE" );
        logger.log( Level.INFO, "**** > DailyReportDetail.jsp "+resource.getResourceCode()+": "+RactDAO.getCurrentDateAndTime() );
        parameters = (HashMap) session.getAttribute( "RACT_PARAMETERS" );

        dateFormatStr = (String) session.getAttribute( "DATEFORMAT" );

        if ( dateFormatStr == null )
        {
            dateFormatStr = "yyyy/MM/dd";
        }

        currentDateStr = (String) session.getAttribute( "CURRENTDATE" );
        if ( currentDateStr != null )
        {
            try
            {
                currTime = new DateSC( currentDateStr, dateFormatStr );
            }
            catch (ParseException ex)
            {
                Logger.getLogger( "activityrecord.web.jsp" ).log( Level.SEVERE, null, ex );
                currTime = new DateSC();
                currTime.setFormat( dateFormatStr );
            }
        }

        calendar = new ActivityRecordCalendar( currTime );
        calendar.setDateFormat( new SimpleDateFormat( dateFormatStr ) );

        dateFormatString = (String) session.getAttribute( "DATEFORMAT" );
        if ( dateFormatString == null )
        {
            dateFormatString = "yyyy/MM/dd";
        }

        currentDate = calendar.getCurrentTime();
        currentDate.setFormat( dateFormatStr );
        activities = (WeekDetail) session.getAttribute( "ACTIVITIES" );
        locale = (Locale) session.getAttribute( "LOCALE" );

        parameters = (HashMap) session.getAttribute( "RACT_PARAMETERS" );

        minDate = DateSC.fromString( (String) parameters.get( "RFROM" ),
                                     "yyyy/MM/dd", new DateSC() );
        maxDate = DateSC.fromString( (String) parameters.get( "RTO" ),
                                     "yyyy/MM/dd", new DateSC() );

        maxHoras = (String) parameters.get( "TIME" );

        if ( maxHoras == null )
        {
            maxHoras = "0";
        }
        System.out.println( clientInfo.getScreenHeight());
        htmlDetail = new HTMLActivityDetail( resource, activities, locale,
                                             minDate, maxDate, localeTexts, parameters );
        switch (resource.getView())
        {
            case DAY:
                mode = "'D'";
                break;
            case WEEK:
                mode = "'W'";
                break;
            case MONTH:
                mode = "'M'";
                break;
            case QUARTER:
                mode = "'Q'";
                break;
            case SEMESTER:
                mode = "'S'";
                break;
            case YEAR:
                mode = "'Y'";
                break;
        }

        contextPath = request.getContextPath();
%>
<html>
    <head>
        <title><%=localeTexts.getString( "application.title" )%></title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
        <meta http-equiv="Cache-Control" content="no-cache"/>
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <link rel="stylesheet" href="/activityreportdoc/css/estilogauss.css" type="text/css"/>
        <% if ( resolution >= 1024 && scrHeight >= 720 )
                {%>
        <link rel="stylesheet" href="/activityreportdoc/css/estilogauss1024.css" type="text/css">
            <link rel="stylesheet" href="/activityreportdoc/css/choicer1024.css" type="text/css">
        <!--[if IE]><link rel="stylesheet" href="/activityreportdoc/css/estilogaussexp1024.css" type="text/css"/><![endif]-->
        <%}
         else
         {%>
        	<link rel="stylesheet" href="/activityreportdoc/css/estilogauss800.css" type="text/css">
            <link rel="stylesheet" href="/activityreportdoc/css/choicer800.css" type="text/css">
        <!--[if IE]><link rel="stylesheet" href="/activityreportdoc/css/estilogaussexp800.css" type="text/css"/><![endif]-->
        <%}%>
        
        <style type="text/css">
            #comment_window
            {
                position: absolute;
                top:100px;
                left:100px;
                width:400px;
                height:65px;
                background-color:rgb(187, 197, 208);;
                color:gray;
                text-align:right;
                border-style: ridge ridge ridge ridge ;
                border-width: 2px;
                border-collapse: collapse;
                border-color: gray;
                display: none;
            }

            .in
            {
                width:375px;
                height:24px;
                border-style: ridge ridge ridge ridge ;
                border-width: 2px;
                border-collapse: collapse;
                border-color: gray;
                display: none;


            }
            .comment
            {
                width: 350px;
                background-color: #FFF;
                font-family: Arial, Helvetica, sans-serif;
                font-size: 11px;
            }
            #header
            {
                background-color:RGB(96,128,192);
                color:white;
                font-family: Arial, Helvetica, sans-serif;
                font-size: 11px;
                font-weight:bold;

            }
            .activity{
				border-left: 2px solid BLACK;
				border-top: 1px solid RGB(192,192,192);
				border-bottom: 1px solid WHITE;
				border-right: 2px solid RGB(192,164,164);
				padding-left: 15px;
			}

			#proyectTable tr:hover td{			
				background-color: #B0BED9;
				cursor:pointer;	
			}
			
			#proyectTable tr.clicked td{
				background-color: #FFFF00;
			}
			
			#activitiesTable tr:hover td{			
				background-color: #B0BED9;
				cursor:pointer;	
			}
			
			#activitiesTable tr.clicked td{
				background-color: #B0BED9;
			}
			
			.floatRight{
				float: right;
				padding-right: 5px;
			}
			.displayOff{
				visibility:hidden !important;
				display: none;
				height: 0px;
			}
			.editedValue{
				    background-color: #f0c4c4 !important;
			}
			.chxx{
			    background-color: #515C79;
			    border-color: #D6D6D6 #888888 #888888 #D6D6D6;
			    margin: 0px 0px 0px 0px;
			    padding: 0px 0px 0px 2px;
			    height: auto;
			    font-family: Arial, Helvetica, sans-serif;
			    font-size: 11px;
			    font-weight: bold;
			    color: #FFFFFF;    
    			border-width: 2px 2px 2px 2px;
			}
			/******************LOADER********************/
			.loader {
			  border: 16px solid #f3f3f3;
			  border-radius: 50%;
			  border-top: 16px solid #00caf2;
			  border-bottom: 16px solid #00caf2;
			  width: 40px;
			  height: 40px;
			  -webkit-animation: spin 2s linear infinite;
			  animation: spin 2s linear infinite;
			  margin-left: 50%;
			  margin-top: 30%;
			  z-index: inherit;
			  position: absolute;
			}
			@-webkit-keyframes spin 
			{
			  0% { -webkit-transform: rotate(0deg); }
			  100% { -webkit-transform: rotate(360deg); }
			}			
			@keyframes spin 
			{
			  0% { transform: rotate(0deg); }
			  100% { transform: rotate(360deg); }
			}
			/******************LOADER********************/
			body {font-family: Arial, Helvetica, sans-serif;}

/* The Modal (background) */
.modal {
  display: none; /* Hidden by default */
  position: fixed; /* Stay in place */
  z-index: 1; /* Sit on top */
  padding-top: 100px; /* Location of the box */
  left: 0;
  top: 0;
  width: 100%; /* Full width */
  height: 100%; /* Full height */
  overflow: auto; /* Enable scroll if needed */
  background-color: rgb(0,0,0); /* Fallback color */
  background-color: rgba(0,0,0,0.4); /* Black w/ opacity */
}

/* Modal Content */
.modal-content {
  position: relative;
  background-color: #fefefe;
  margin: auto;
  padding: 0;
  border: 1px solid #888;
  width: 80%;
  box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2),0 6px 20px 0 rgba(0,0,0,0.19);
  -webkit-animation-name: animatetop;
  -webkit-animation-duration: 0.4s;
  animation-name: animatetop;
  animation-duration: 0.4s
}

/* Add Animation */
@-webkit-keyframes animatetop {
  from {top:-300px; opacity:0} 
  to {top:0; opacity:1}
}

@keyframes animatetop {
  from {top:-300px; opacity:0}
  to {top:0; opacity:1}
}

/* The Close Button */
.close {
  color: white;
  float: right;
  font-size: 28px;
  font-weight: bold;
}

.close:hover,
.close:focus {
  color: #000;
  text-decoration: none;
  cursor: pointer;
}

.modal-header {
  padding: 2px 16px;
  background-color: rgb(187,197,208);
  color: white;
}

.modal-body {padding: 2px 16px;}

.modal-footer {
  padding: 2px 16px;
  background-color: rgb(187,197,208);
  color: white;
  height: 25px;
}	

.choiceimg{
	background-image: url(/activityreportdoc/images/unbutton.png) !important;
	background-repeat: no-repeat;
}

.alert{
        position: relative;
    padding: .75rem 1.25rem;
    margin-bottom: 1rem;
    border: 1px solid transparent;
    border-radius: .25rem;
    margin: 20px;
    text-align: center;
}
.alert-info {
    color: #336573;
    background-color: #e0f3f8;
    border-color: #d3eef6;
}
.alert-error {
    color: #813838;
    background-color: #fee2e1;
    border-color: #fdd6d6;
}
			/********************************************/			

        </style>
        <script type="text/javascript" src="/activityreportdoc/js/languages.js"></script>
        <script type="text/javascript">
            CurrentLang=<%=localeTexts.getString( "javascriptLocale" )%>;
            CurrentContext="<%=contextPath%>";
        </script>
        <script type="text/javascript" src="/activityreportdoc/js/utils.js"></script>
        <script type="text/javascript" src="/activityreportdoc/js/estilo.js"></script>
        <script type="text/javascript" src="/activityreportdoc/js/header.js"></script>
        <script type="text/javascript" src="/activityreportdoc/js/jquery.js" ></script>
        <script type="text/javascript" src="/activityreportdoc/js/act.js" ></script>        
        <script type="text/javascript" src="/activityreportdoc/js/price.js" ></script>
       
        
        <script type="text/javascript">
            fija= new Image();
            fija.src = "/activityreportdoc/images/rbchk.png";

            nfija = new Image();
            nfija.src = "/activityreportdoc/images/rbuchk.png";

            var isMozilla = isMozilla();
            var isIE = isExplorer();
        </script>
        <script type="text/javascript">
            $(function(){ $(init); });
            
            var dayCount=<%=activities.getDayCount()%>;
            var extraCount=<%=htmlDetail.getVisibleCustomCount()%>;
            var maxHoras=<%=maxHoras%>;
            var mode=<%=mode %>
        </script>
    </head>

    <body bgcolor="#FFFFFF" text="#000000" >
     <div id="loader" class="loader displayOff"></div>    
            <form name="frmDetail" id="frmDetail" action="<%=contextPath%>/do/" method="post" style="margin:0;padding: 0" autocomplete="off">
	            <input TYPE=HIDDEN id="fecha" NAME="fecha" value="<%=currentDate.toString()%>"/>
	            <input type=HIDDEN NAME="operation" value="updateract"/>
	            <input TYPE=HIDDEN NAME="mode" value="day"/>
	            <input TYPE=HIDDEN NAME="go" value=""/>
	            <input TYPE=HIDDEN NAME="type" value=""/>
	            <input TYPE=HIDDEN NAME="date" value=""/>
	            <input TYPE=HIDDEN NAME="list" value=""/>
	            <input TYPE=HIDDEN NAME="position" value=""/>            
            <br>
    	<% if( (resource.getCode().equals( "CONTRACTS" ))||(resource.getCode().equals( "PRICE_LIST" )) )    	    
    	{
    		proyectos=(List<ActivityChoiceTO>)session.getAttribute( "PROJECTS" );
    	%>
    	 <div id="allPriceList">   
       <!--NEW -------------------------->
        <input tabindex="-1" type="text" id="searchProject" onkeyup="searchProyectName()" placeholder="<%=localeTexts.getString( "price.searchName" )%>" title="Type in a name" style="width: fit-content!important;display: block;margin: 0 auto;background-image: url(/activityreportdoc/images/search4.svg);background-repeat: no-repeat;padding-left: 20px;">
		<br>
    	<div id="proyectsHeader" style="width: fit-content!important;margin-top: 10px;margin: 0 auto;width: 675px;overflow: auto;">
			<table cellspacing="0" cellpadding="0" class="fixed_headers">
				<thead>
					<tr class="table_thead_tr">
						<td width="30" class="chxx">&nbsp;</td> 
						<td width="12" class="chxx">&nbsp;</td>
						<td width="96" class="chxx"><%=localeTexts.getString( "price.code" )%></td>
						<td width="313" class="chxx"><%=localeTexts.getString( "price.name" )%></td>
						<td width="99" class="chxx"><%=localeTexts.getString( "price.class" )%></td>
						<td width="105" class="chxx"><%=localeTexts.getString( "price.unity" )%></td>
					</tr>
				</thead>
			</table>
		</div>

	<div id="proyects" style="height:211px;width: fit-content!important; margin-top: 10px;margin: 0 auto; width: 675px;overflow: auto;">
		<table id="proyectTable">
  			<tbody class="table_tbody">
  			<%
  			int i=1;
  			for(ActivityChoiceTO proyecto: proyectos)
  			{%>   
  				<tr class="proyectRow">
				    <input type="hidden" name="<%=proyecto.nombre%>" id="<%=proyecto.codigo%>" value="">
					<td width="28" class="chtv"><%=i%></td>					
					<td width="99" class="ch"><%=proyecto.codigo%></td>
					<td width="313" class="ch"><%=proyecto.nombre%></td>
					<td width="100" class="ch"><%=proyecto.tipo%></td>
					<td width="100" class="ch"><%=proyecto.unidad%>	</td>
				</tr>
			<% i++;
			}%>			
    	    </tbody>
    	 </table>
	</div> <!-- Fin div proyects-->
	        <br>         
            <br>
            <!-- The Modal -->
<div id="myModal" class="modal">

  <!-- Modal content -->
  <div class="modal-content" style="width:fit-content!important;">
    <div class="modal-header">
      <span id="closeModal" class="close">&times;</span>
      <h2><%=localeTexts.getString( "price.copyfrom" )%><span id="copyFrom" style="color: yellow;"></span></h2>
    </div>    
    <div class="modal-body" style="height:211px;width:fit-content!important;margin: 0 auto;overflow: auto;">
    	<input tabindex="-1" type="text" id="searchCopyProject" onkeyup="searchCopyProyectName()" placeholder="<%=localeTexts.getString( "price.searchName" )%>" title="Type in a name" style="width: fit-content!important;display: block;margin: 0 auto;background-image: url(/activityreportdoc/images/search4.svg);background-repeat: no-repeat;padding-left: 20px;">
      <table cellspacing="0" cellpadding="0">
	      <thead>
		      	<tr>
					<td width="28" class="cht">&nbsp;</td> 
					<td width="12" class="cht">&nbsp;</td>
					<td width="99" class="cht"><%=localeTexts.getString( "price.destination" )%></td>
					<td width="313" class="cht"><%=localeTexts.getString( "price.name" )%></td>
				</tr>
	      </thead>
		  <tbody id="copyTable">
			  
		  </tbody>
 	 </table> 	 	
    </div>
    <div class="modal-footer">
         <a href="javascript:doOk()" class="button floatRight">
			<img src="/activityreportdoc/images/save.gif" height="15px" border="0">
         </a>
		<a href="javascript:doCancel()" class="button floatRight">
			<img src="/activityreportdoc/images/cancel.gif" height="15px" border="0">
		</a>
		<div id="ok" class="alert alert-info displayOff" style="margin-top: 40px;"> INFORMACI&#211;N ACTUALIZADA</div> 
		<div id="error" class="alert alert-error displayOff" style="margin-top: 40px;"> DEBE SELECCIONAR UN DESTINO</div>                                 
		<div id="error2" class="alert alert-error displayOff" style="margin-top: 40px;"> DEBE SELECCIONAR UNA ACTIVIDAD</div>
    </div>
    </div>
  </div>
</div>
	<div id="divPriceList" style="margin-top: 10px;margin: 0 auto;overflow: scroll;padding: 0px 5%;height: 70%;">
   <form name="frmPriceList" id="frmPriceList" action="/ActivityReport/do/" method="post" style="margin: 10px auto;padding: 0px;/* display: none; */" autocomplete="off">          
                  
            <input type="hidden" name="doubleValues" value="F" id="doubleValues">
            <input type="hidden" value="1" id="orderBy">
            
<table id="tabledetail" border="0" cellpadding="0" cellspacing="0" style="border-collapse: separate;width: 100%;border: 1px;">
    <thead>
<tr>
<td style="width: 51%;padding-left: 10px;" class="tiLVL3"><%=localeTexts.getString( "price.activities" )%>
<input tabindex="-1" class="floatRight" type="text" id="searchActivity" onkeyup="searchActivityCode()" placeholder="<%=localeTexts.getString( "price.searchCode" )%>" title="Type in a name" style="background-image: url(/activityreportdoc/images/search4.svg);background-repeat: no-repeat;padding-left: 20px;margin-right: 200px;">
</td>
<td style="width: 16.3%;" class="tiEXTRA"><%=localeTexts.getString( "price.price" )%></td>
<td style="width: 16.3%;" class="tiEXTRA"><%=localeTexts.getString( "price.quantity" )%></td>
<td style="
    width: 16.3%;
    border-top: 2px solid BLACK;
    border-left: 2px solid BLACK;
    color: BLACK;
    background-color: WHITE;
    padding: 0px 0px 2px 0px;
    text-align: center;
    border-right: 2px solid BLACK;
"><%=localeTexts.getString( "price.contracted" )%></td>
</tr>
</thead>
<tbody id="activitiesTable">
<tr>
<!--<td>&nbsp;</td>-->
<td id="proyectName" class="laP1SE" style="border-left: 2px solid BLACK;">&nbsp;</td>
<td class="daP1ED">&nbsp;</td>
<td class="daP1ED">&nbsp;</td>
<td class="laP1SE" style="border-right: 2px solid BLACK;">&nbsp;</td>
</tr>
<tr>
<!-- <td>&nbsp;</td>-->
<td class="tiTG" colspan="2">&nbsp;</td>
<td class="tiTG">&nbsp;</td>
<td class="tiTG">&nbsp;</td>
</tr>
</tbody>
</table>

</form>
</div> <!-- divPriceList -->
</div> <!-- Fin div allPriceList -->
    	<%}
    	else
    	{
                    if ( resource.getView().equals( PeriodType.DAY ) )
                    {
            %>
            
            <table width="37%" border="0" cellspacing="0" cellpadding="3">
                <tr> 
                    <td nowrap align="left"><%

                                    if ( activities.getDayCount() != 1 )
                                    {
                        %><a name="1day" href="javascript:changeMode( '1day' )" target="_top" class="bday">
                            <img src="/activityreportdoc/images/1day.png" width="16" 
                                 height="15" border="0" alt=""/><%=localeTexts.getString( "dates.oneDay" )%></a><%
                                                 }
                                                 if ( ( activities.getDayCount() != 5 ) && ( htmlDetail.
                                                         getCustomCount() == 0 ) )
                                                 {

                            %><a name="5day" href="javascript:changeMode( '5day' )" target="_top" class="bday">
                            <img src="/activityreportdoc/images/5day.png" width="16" 
                                 height="15" border="0" alt=""/><%=localeTexts.getString( "dates.workWeek" )%></a><%
                                                 }
                                                 if ( ( activities.getDayCount() != 7 ) && ( htmlDetail.
                                                         getCustomCount() == 0 ) )
                                                 {
                            %><a name="7day" href="javascript:changeMode( '7day' )" target="_top" class="bday">
                            <img src="/activityreportdoc/images/7day.png" width="16" 
                                 height="15" border="0" alt=""/><%=localeTexts.getString( "dates.fullWeek" )%></a></td><%
                                                 }
                            %>
                </tr>
            </table>
            <br>
            <%
                    }
            %>
            <%
			htmlDetail.getHTMLContent( new PrintWriter( out ) );            
            logger.log( Level.INFO, "**** < DailyReportDetail.jsp:"+resource.getResourceCode()+" "+RactDAO.getCurrentDateAndTime() );
    	}
            %>
        
        
        </form>
        <div class="window"><div align="left" class="title">Select an option</div><iframe class="link"></iframe></div>
        <div id="mask"></div>
            
<script type="text/javascript">
   $('#proyectTable tr').click(function() 
	{
       var proyectCode, orderBy;
      $(this).toggleClass("clicked").siblings().removeClass("clicked"); //Selecciona una fila de la tabla de proyectos y deselecciona los demás
	  if ($('#proyectTable tr.proyectRow.clicked input')[0]!=undefined)
	   {
	      if($('.editedValue').length!==0)
	          top.frames["header"].doUpdatePrice();
	  	  proyectCode=$('#proyectTable tr.proyectRow.clicked input')[0].id;
	  	  orderBy=$("#orderBy", top.frames["detail"].document)[0].value;	  	 
	  	  doRequest('/ActivityReport/do/getProyectActivities?proyectCode='+proyectCode+"&orderBy="+orderBy, "POST", activityTable );
	   }
    });
   $('#proyectTable tr').dblclick(function() 
   	{
   	  if ( ($('#proyectTable tr.proyectRow.clicked input')[0]!=undefined)&&($(this).find('input')[0].id===$('#proyectTable tr.proyectRow.clicked input')[0].id) )
   	   {   	      
   	  	  proyectCode=$('#proyectTable tr.proyectRow.clicked input')[0].id;
   	  	  orderBy=$("#orderBy", top.frames["detail"].document)[0].value;
   	  	  $("#copyFrom").text($('#proyectTable tr.proyectRow.clicked input')[0].name);
   	  	  var modal = document.getElementById('myModal');
   	  	  modal.style.display = "block";
   	  	  showCopyTable();   	  	  
   	   }
	});   
   
   function doCancel(){
       	 var modal = document.getElementById('myModal');
	  	 modal.style.display = "none";
   }
   
   $('#closeModal').click(function(){
       var modal = document.getElementById('myModal');
	  	  modal.style.display = "none";
   });
   
   $('#activitiesTable').delegate('tr','click', function(){
       $(this).toggleClass("clicked").siblings().removeClass("clicked"); //Selecciona una fila de la tabla de actividades y deselecciona los demás
   			});
   
   $('#activitiesTable').delegate('tr','keydown', function(e){		//Cuando usan el TAB
       var code = e.keyCode || e.which;
       if (code === 9) 
       {
           if($(this).next().hasClass( "actrow" ))
       		$(this).next().toggleClass("clicked").siblings().removeClass("clicked"); //Selecciona una fila de la tabla de actividades y deselecciona los demás
       	   else
		   {
       	       $("#proyectName").parent().next().toggleClass("clicked").siblings().removeClass("clicked"); //Vuelve a la primera fila
			   if($(document.activeElement)[0].id.startsWith("qtty"))
			   { 
			       $("[TabIndex='1']").select();
			       e.preventDefault(); // Let's stop this event.
			       e.stopPropagation(); // Really this time.			   
			   }
       	   }
		}
   		});
	
</script>
</body>
</html>
