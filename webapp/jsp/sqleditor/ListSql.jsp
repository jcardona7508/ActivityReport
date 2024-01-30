<%@ page import="java.io.PrintWriter,
		 java.util.*,
		 common.sql.*,
		 common.util.ResourceString" %>

<%
    response.addHeader("Cache-Control","no-cache");
    response.addHeader("Cache-Control","no-store");

    ArrayList sqlList;
    HTMLSqlList htmlList;
    ResourceString localeTexts;
    int i;

    localeTexts = (ResourceString) session.getAttribute("RESOURCESTRING");
    String contextPath = request.getContextPath();
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
         <td colspan="7" height="77" >
           <p>
                <h1><%=localeTexts.getString("sqlMode.SqlEditor")%></h1>
                <hr size="1" noshade color="#000066">
        </td>
 </tr>
</table>

<%
   try
   {
    sqlList = (ArrayList) request.getAttribute("sqlList");
	if( sqlList != null )
    {
	    htmlList = new HTMLSqlList( sqlList );	    
	    htmlList.getHTMLContent( new PrintWriter( out ) );
	}
   }
   catch(Exception anException)
   {
      System.out.println(localeTexts.getString("sqlMode.ErrorInJSP") + " ListSql.JSP: " + anException );
   }
%>
<table width="100%">
	<tr>
	<td width="33%">
	    <input type="hidden" name="group" value="<%=request.getParameter("group")%>">
	    <input name="addsql" type="button" onClick="javascript: doChangeAction( this.form , this.name )" value="<%=localeTexts.getString("sqlMode.AddButton")%>">
	</td>
	<td width="33%"><input name="deletesql" type="button" onClick="javascript: doChangeAction( this.form , this.name )" value="<%=localeTexts.getString("sqlMode.DeleteButton")%>"></td>
	<td width="33%"><input name="sqllistgroup" type="button" onClick="javascript: doChangeAction( this.form , this.name )" value="<%=localeTexts.getString("sqlMode.GoBackButton")%>"></td>
	</tr>	
</table>
<a href="<%=contextPath%>/do/home"><%=localeTexts.getString("sqlMode.HomeLinkCaption")%></a>
</form>
</body>