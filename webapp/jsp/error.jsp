<%@ page import="siscorp.framework.application.EventResponse,
    common.util.ResourceString"
%> 
<%!
    EventResponse eventResponse; 
    String message;
    String pageTitle;
    ResourceString localeTexts;
%>
<%
    response.addHeader("Cache-Control","no-cache");
    response.addHeader("Cache-Control","no-store");

    localeTexts = (ResourceString) session.getAttribute("RESOURCESTRING");

    if ( localeTexts != null )
        pageTitle = localeTexts.getString("error.errorPageTitle");
    else
        pageTitle = "General Error";

%>
<script type="text/javascript">
    function toggleDisplay(name)
    {
       doc = document.getElementById(name);
       if (doc.style.display=="none")
           doc.style.display="block";
       else
           doc.style.display="none";
    }
</script>
<html>
    <head>
        <title><%=pageTitle%></title>
    </head>
    <body bgColor=#8090a0 text=#F5F5F0 style="margin:0px; padding:0px;" >
        <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 style="border: white 0px solid;" >
            <TR>
                <TD>
                    <img src="/activityreportdoc/images/stripesimage.png" />
                </TD>
                <TD vAlign=top height=21>
                    <font face="Comic Sans MS" size=4>
                    <CENTER><H1><%=pageTitle%></H1></CENTER><p/>
                    </font>
<%
            Throwable e = ( Throwable ) request.getAttribute("exception");
            if (e != null) {
                StackTraceElement element = e.getStackTrace()[ 0];
                // TODO: i18n: urgent
                out.println( "El aplicativo ha sufrido el siguiente problema:<br/>");
                out.println( "<BLOCKQUOTE dir=ltr style=\"MARGIN-RIGHT:0px\" />");
                out.println( e.getMessage());
                out.println( "Recomendaciones generales para recuperarse del error.<p/>");
                out.println( "<INPUT type=\"button\" value=\"Mostrar detalles\" onClick=\"toggleDisplay(\'showdetails\')\" ><br/>");
                out.println( "<DIV STYLE=\"display:none\" id=\"showdetails\" >");
                out.println( "<TEXTAREA style=\"width:30%; height:100px\">");

                out.println( "Error details: ");
                out.println( e);
                out.println( "Class: " + element.getClassName());
                out.println( "Method: " + element.getMethodName());
                out.println( "Line: " + element.getLineNumber());
                out.println( );
                out.println( "Root Cause: " + e.getCause());
                out.println( "</TEXTAREA><p/>");
                out.println( "</DIV>");
                out.println( "<p/>");
                out.println( "Desea enviar información sobre el error al equipo de soporte?<br/>");
                out.println( "<INPUT type=\"submit\" value=\"E-Mail support team\" />");
            } else {
                if ((eventResponse = (EventResponse) request.getAttribute("response")) == null) {
                    if (localeTexts != null )
                        out.println("<p>" + localeTexts.getString("error.PageNotFound") +"</p>");
                    else
                        out.println("<p> Page Not Found </p>");
                } else {
                    if ((message = (String) eventResponse.getAttribute("message")) != null) {
                        out.println("<P>" + message + "</P>");
                    } else {
                        if (localeTexts != null )
                            out.println(localeTexts.getString("error.NoResponseReceived"));
                        else
                            out.println("<p> Response not received </p>");
                    }
                }
        }
        %>
                </TD>
            </TR>
        </TABLE>
    </body>
</html>
