<!--/*
    *    DATE         VERSION     AUTHOR      DESCRIPTION
    *   10/11/10      1.1           CAP       Refactor to fix Style issues
    *
    */
 */-->
<%@ page info="Choicer"
         import="java.io.PrintWriter,
	    siscorp.webmanagement.HTMLSelectSC, 
	    common.web.ClientInfo,
	    common.util.ResourceString"%> 
<%! String action;
    String position;
    String search;
    HTMLSelectSC select;
    Integer resolution;
    ClientInfo clientInfo;
    String multi;
    ResourceString localeTexts;
    int frWidth, frHeight, percent, srcHeight;
    String contextPath;
%>
<%
        response.addHeader( "Cache-Control", "no-cache" );
        response.addHeader( "Cache-Control", "no-store" );

        action = "add" + request.getParameter( "choicesAction" );
        search = "search" + request.getParameter( "choicesAction" );
        position = request.getParameter( "name" );
        select = (HTMLSelectSC) session.getAttribute( "COLUMNS" );
        clientInfo = (ClientInfo) session.getAttribute( "CLIENTINFO" );
        try
        {
            resolution = Integer.parseInt( clientInfo.getScreenWidth() );
            srcHeight = Integer.parseInt( clientInfo.getScreenHeight() );
        }
        catch (NumberFormatException e)
        {
            resolution = clientInfo.getMaxSupportedScreenWidth();
        }
        multi = request.getParameter( "multi" );
        multi = ( multi == null ? "true" : multi );
        localeTexts = (ResourceString) session.getAttribute( "RESOURCESTRING" );
        contextPath = request.getContextPath();
%>
<html>
    <head>
        <title><%=localeTexts.getString( "choicer.find" )%></title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <meta http-equiv="Cache-Control" content="no-cache">

        <% if ( resolution > 1024 && srcHeight >= 768 )
                {%>
        <link rel="stylesheet" href="/activityreportdoc/css/choicer1024.css" type="text/css">
        <%}
         else
         {%>
        <link rel="stylesheet" href="/activityreportdoc/css/choicer800.css" type="text/css">
        <%}%>
        <script type=""  language="JavaScript1.3" src="/activityreportdoc/js/languages.js"></script>
        <script type="text/javascript"> CurrentLang=<%=localeTexts.getString( "javascriptLocale" )%>;</script>
        <script type="text/javascript" src="/activityreportdoc/js/jquery.js"></script>
        <script type="text/javascript" src="/activityreportdoc/js/choicer.js"></script>
    </head>
    <script type="text/javascript">
        var choicerAction="<%=action%>";
        var position="<%=position%>";
        function abrirVentana (pagina, alto, ancho, superior, iquierda)
        {
            var opciones;
            opciones = "toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=no,resizable=no,width="+ancho+",height="+alto+",,top="+superior+",left="+iquierda;
	
            window.open(pagina,"",opciones);
        }
    </script>

    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
        <form name="browse" method="post" action="<%=contextPath%>/do/" target="options" style="margin: 0px;padding:0px">
            <input type="hidden" name="operation" value="<%=search%>">
            <input type="hidden" name="name" value="<%=position%>">
            <%if ( resolution >= 1024 )
                    {
                        frHeight = 200;
                        frWidth = 600;
                        percent = 100;
                    }
                    else
                    {
                        frHeight = 200;
                        frWidth = 600;
                        percent = 25;
                    }
            %>
            <table width="<%=frWidth + "px"%>" border="0" cellspacing="0" cellpadding="0" height="<%=frHeight + "px"%>">
                <tr>
                    <td align="center" valign="top" height="5%">
                        <table width="<%=frWidth + "px"%>" border="0" cellspacing="0" cellpadding="0" height="<%=percent + "%"%>">
                            <tr>
                                <td class="head" colspan="3">
                                    <table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
                                        <tr>
                                            <td width="39" class="head" height="27">
                                                <div align="right"><%=localeTexts.getString( "choicer.findCaption" )%></div>
                                            </td>
                                            <td width="190" class="head" height="27">
                                                <div align="left">
                                                    <input type="text" name="filter" size="35" tabindex="1" class="filter">
                                                </div>
                                            </td>
                                            <td width="44" class="button" height="27" >
                                                <a href="javascript:doSubmit()" tabindex="2" class="button">
                                                    <img src="/activityreportdoc/images/search.gif" width="16" height="16" border="0">
                                                </a>
                                            </td>
                                            <td width="92" class="head" height="27">
                                                <div align="right"><%=localeTexts.getString( "choicer.inColumnCaption" )%></div>
                                            </td>
                                            <td class="head" height="27">
                                                <div align="left">
                                                    <%if ( select != null )
                                                            {
                                                                select.getHTMLContent( new PrintWriter(
                                                                        out ) );
                                                            }%>
                                                </div>
                                            </td>
                                            <td width="44" class="button" height="27">
                                                <div align="right">
                                                    <a href=# tabindex="3" onclick="javascript:abrirVentana('<%=contextPath%>/do/help?contexto=workingscreen&referencia=actividad',screen.availHeight-100,screen.availWidth-100,50,50)" class="button">
                                                        <img src="/activityreportdoc/images/help.png" width="20" height="18" border="0">
                                                    </a>
                                                </div>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td valign="middle" align="center" height="5%" class="head" colspan="3">
                                    <table width="95%" border="0" cellspacing="0" cellpadding="0" height="30" align="center">
                                        <tr>
                                            <td class="sep">
                                                <input type="radio" name="option" tabindex="4" value="0">
                                                <%=localeTexts.getString( "choicer.searchFromTextBeginning" )%>
                                            </td>
                                            <td class="sep">
                                                <input type="radio" name="option" tabindex="5" value="1">
                                                <%=localeTexts.getString( "choicer.exactMatchOnly" )%></td>
                                            <td class="sep">
                                                <input type="radio" name="option" tabindex="6" value="2" checked>
                                                <%=localeTexts.getString( "choicer.anyTextPosition" )%></td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td height="85%" colspan="3">
                                    <iframe name="options" tabindex="7" src="<%=contextPath%>/do/getChoices?multi=<%=multi%>" width="<%=frWidth + "px"%>" height="<%=frHeight + "px"%>" frameborder="0" scrolling="yes" style="margin: 0px;padding: 0px;">
                                    </iframe>
                                </td>
                            </tr>
                            <tr>
                                <td height="5%" class="foot" width="650" align="center" >
                                    <table width="95%" border="0" cellspacing="0" cellpadding="0">
                                        <tr>
                                            <td width="13%" class="darkbutton">
                                                <input id="total" type="text" name="textfield" readonly tabindex="8" class="plane" value="0/0">
                                            </td>
                                            <td width="19%" align="center" class="darkbutton">
                                                <a href="javascript:doSelectAll()" tabindex="9" class="darkbutton">
                                                    <%=localeTexts.getString( "choicer.SelectAll" )%></a></td>
                                            <td width="19%" align="center" class="darkbutton">
                                                <a href="javascript:doUnSelectAll()" tabindex="10" class="darkbutton">
                                                    <%=localeTexts.getString( "choicer.DeselectAll" )%></a></td>
                                            <td width="10%">&nbsp;</td>
                                            <td width="10%">&nbsp;</td>
                                            <td width="10%">&nbsp;</td>
                                            <td width="10%">&nbsp;</td>
                                        </tr>
                                    </table>
                                </td>
                                <td height="5%" class="button" width="25">
                                    <a href="javascript:doCancel()" tabindex="11" class="button">
                                        <img src="/activityreportdoc/images/cancel.gif" height="15px" border="0">
                                    </a></td>
                                <td height="5%" class="button" width="25">
                                    <a href="javascript:doOk()" tabindex="12" class="button">
                                        <img src="/activityreportdoc/images/save.gif" height="15px" border="0">

                                    </a>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>
