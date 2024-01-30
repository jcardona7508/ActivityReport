<%@ page info="Choices"
    import="java.io.PrintWriter,siscorp.webmanagement.HTMLContentSC,
	    common.web.ClientInfo,
	    common.util.ResourceString"%>

<%! 
    HTMLContentSC content;
    Integer resolution;
    ClientInfo clientInfo; 
    String multi;
    ResourceString localeTexts;
%>
<%
    response.addHeader("Cache-Control","no-cache");
    response.addHeader("Cache-Control","no-store");

    localeTexts = (ResourceString) session.getAttribute("RESOURCESTRING");
    content = (HTMLContentSC) session.getAttribute("CHOICES");
    clientInfo =  (ClientInfo) session.getAttribute("CLIENTINFO");
    resolution = clientInfo.getMaxSupportedScreenWidth();
    multi = request.getParameter( "multi" );
    multi = ( multi == null ? "true" : multi );
%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="Cache-Control" content="no-cache">

<link rel="stylesheet" href="/activityreportdoc/css/choicer<%=resolution%>.css" type="text/css">
<script language="JavaScript1.3" src="/activityreportdoc/js/languages.js"></script>
<script language="JavaScript1.3">CurrentLang=<%=localeTexts.getString("javascriptLocale")%>;</script>
<script type="text/javascript" src="/activityreportdoc/js/jquery.js"></script>
<script language="JavaScript1.3" src="/activityreportdoc/js/utils.js"></script>
<script language="JavaScript1.3" src="/activityreportdoc/js/general.js"></script>
<script language="JavaScript1.3" src="/activityreportdoc/js/event.js"></script>

<script language="JavaScript1.3" src="/activityreportdoc/js/choices.js"></script>
<script language="JavaScript1.3">
    var multi=<%=multi%>;
	 fija= new Image();
	 fija.src = "/activityreportdoc/images/unbutton.png";
	 
	 nfija = new Image();
	 nfija.src = "/activityreportdoc/images/button.png";
	 
	 var isMozilla = isMozilla();
	 var isIE = isExplorer();
</script>
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onload="init()" bgcolor="#F3F4F2">
<form name="frmchoices">
  <table  cellspacing="0" cellpadding="0" >
  <%
    if ( content != null )
       content.getHTMLContent( new PrintWriter( out ) );
    else
    	out.println("<tr><TD>"+localeTexts.getString("choices.NotFoundMessage")+"</TD></tr>");
  %>
</table>
</form>
</body>
</html>
