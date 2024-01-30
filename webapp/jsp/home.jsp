<%@ page info="Home Page" import="common.util.ResourceString,common.user.User,activityrecord.ract.ActivityReportResource" %> 
<%!
	ResourceString localeTexts;
    String  contextPath = "/ActivityReport";
%>
<%
    response.addHeader("Cache-Control","no-cache");
    response.addHeader("Cache-Control","no-store");

    localeTexts = (ResourceString) session.getAttribute("RESOURCESTRING");
    contextPath = request.getContextPath();
    ActivityReportResource resource = ( ActivityReportResource ) session.getAttribute( "RESOURCE" );
	User user = ( User ) session.getAttribute( "USER" );
%>
<html>
<head>
    <title><%=localeTexts.getString("application.title")%></title>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <meta http-equiv="Cache-Control" content="no-cache">
    <script type="text/javascript" language="JavaScript1.3" src="/activityreportdoc/js/languages.js"></script>
    <script type="text/javascript" language="JavaScript1.3">CurrentLang=<%=localeTexts.getString("javascriptLocale")%>;</script>
    <script type="text/javascript" language="javascript" src="/activityreportdoc/js/header.js"></script>
</head>
<% if( (resource.getCode().equals( "CONTRACTS" ))||(resource.getCode().equals( "PRICE_LIST" )) )
    {%>
 		<frameset id="frameset" rows="30,*" frameborder="NO" border="0" framespacing="0" cols="*"> 
  <%}
  else
  {%>
  		<frameset id="frameset" rows="150,*" frameborder="NO" border="0" framespacing="0" cols="*">
  <%}%>
  <frame name="header" scrolling="NO" noresize src="<%=contextPath%>/do/getHeader" frameborder="yes" marginwidth="0" marginheight="0" />
  <frame name="detail" scrolling="YES" noresize src="<%=contextPath%>/do/getActivityDetail" marginwidth="0" marginheight="0" frameborder="NO"/>
</frameset>
<noframes> 
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
</body>
</noframes> 
</html>
