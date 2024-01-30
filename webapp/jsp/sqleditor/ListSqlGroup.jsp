<%@ page import="java.io.PrintWriter,java.util.*,
		common.sql.*,
		common.util.ResourceString"%>
<%
    String contextPath = request.getContextPath();
    response.addHeader("Cache-Control","no-cache");
    response.addHeader("Cache-Control","no-store");

	ArrayList sqlList;
	int i;
	HTMLSqlListGroup htmlList;
	ResourceString localeTexts;
	
	localeTexts = (ResourceString) session.getAttribute("RESOURCESTRING");
%>

<html>
    <head>
	<title><%=localeTexts.getString("application.title")%></title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	<link rel="stylesheet" href="/activityreportdoc/css/estilogauss800.css" type="text/css">
        <script language="JavaScript1.3" src="/activityreportdoc/js/languages.js"></script>	
	<script language="JavaScript1.3">CurrentLang=<%=localeTexts.getString("javascriptLocale")%>;</script>
	<script language="JavaScript1.3" src="/activityreportdoc/js/utils.js"></script>
    </head>
    
    <body>
	<form name="form" method="post" action="">
	    <input type="hidden" name="action" value="">
	    <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
		<tr>
		    <td colspan="7" height="77" ><p>
			<h1><%=localeTexts.getString("sqlMode.SqlEditor")%></h1>
			<hr size="1" noshade color="#000066">
		    </td>
		</tr>
	    </table>

<%
	try {
		sqlList = (ArrayList) request.getAttribute("sqlList");
		if (sqlList != null) {
			htmlList = new HTMLSqlListGroup(sqlList, (String) session.getAttribute("language"));
			htmlList.getHTMLContent(new PrintWriter(out));
		}
	} catch (Exception anException) {
		System.out.println(localeTexts.getString("sqlMode.ErrorInJSP") 
			+ " ListSqlGroup.JSP: " + anException);
	}
%>

	    <a href="<%=contextPath%>/do/home"><%=localeTexts.getString("sqlMode.HomeLinkCaption")%></a>
	</form>
    </body>
</html>
