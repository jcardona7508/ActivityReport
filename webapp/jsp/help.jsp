<%@ page info="Help" import="common.util.ResourceString"%> 
<%!	
	String context;
	String referencia;
	String url;
	String language;
    String pdfurl;   // url for the users manual pdf
	ResourceString localeTexts;
%>
<%
    response.addHeader("Cache-Control","no-cache");
    response.addHeader("Cache-Control","no-store");

    localeTexts = (ResourceString) session.getAttribute("RESOURCESTRING");
    context=request.getParameter("contexto");
    referencia=request.getParameter("referencia");

    language = ((String) session.getAttribute("language")).toUpperCase();
    if ( language == null )
        language = "EN";
    // TODO: this is currently wrong, the file does not always exist.
    url="/activityreportdoc/"+language+"/html/help/"+context+"GeneralDetail.htm";

    if (referencia!=null)
    	url=url+"#"+referencia;
    if (language.equals("EN"))
        pdfurl = "/activityreportdoc/"+language+"/html/help/ActivityReport_UserManual.pdf";
    else
        pdfurl = "/activityreportdoc/"+language+"/html/help/Manual Activity Report User.pdf";
%>

<html>
<head>
<title><%=localeTexts.getString("application.title")%> - <%=context%> </title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>
<!-- <frameset rows="135,*" frameborder="NO" border="0" framespacing="0" cols="*">
  <frame name="topFrame" scrolling="NO" noresize src="/activityreportdoc/<%=language%>/html/help/<%=context%>General.htm" />
  <frame name="mainFrame" src="<%=url%>" />
</frameset>
<noframes>  -->
<!-- TODO: this is temporary, while the PDF file is replaced by a collection of HTML documents: -->
<frameset rows="*" frameborder="NO" border="0" framespacing="0" cols="*">
  <frame name="mainFrame" src="<%=pdfurl%>" />
</frameset>
<noframes>
<body bgcolor="#FFFFFF" text="#000000"  >

</body>
</noframes> 
</html>