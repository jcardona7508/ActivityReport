<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="common.util.ResourceString"%>

<%!
    ResourceString localeTexts;
    String contextPath;
%>

<%
    response.addHeader("Cache-Control","no-cache");
    response.addHeader("Cache-Control","no-store");

    localeTexts = (ResourceString) session.getAttribute("RESOURCESTRING");
    contextPath = request.getContextPath();
%>
<html>
    <head>
	<meta http-equiv="content-type" content="text/html; charset=ISO-8859-1" />
	<title><%=localeTexts.getString("application.title")%></title>
    </head>
    <script type="text/javascript" language="JavaScript1.3" src="/activityreportdoc/js/languages.js"></script>
    <script type="text/javascript" language="JavaScript1.3">CurrentLang=<%=localeTexts.getString("javascriptLocale")%>;</script>
    <script type="text/javascript" language="JavaScript1.3" src="/activityreportdoc/js/utils.js"></script>
    <body>
	<%=localeTexts.getString("start.description")%><br>
	<a href="<%=contextPath%>do/sqllistgroup">
	    <%=localeTexts.getString("start.SqlEditorLink")%>
        </a>
    </body>
</html>
