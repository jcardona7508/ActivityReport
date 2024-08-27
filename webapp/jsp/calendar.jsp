<%@ page import="common.web.ClientInfo,
		common.util.ResourceString"%> 
<%!    Integer resolution;
    ClientInfo clientInfo;
    ResourceString localeTexts;
%>
<%
        response.addHeader( "Cache-Control", "no-cache" );
        response.addHeader( "Cache-Control", "no-store" );

        localeTexts = (ResourceString) session.getAttribute( "RESOURCESTRING" );
        clientInfo = (ClientInfo) session.getAttribute( "CLIENTINFO" );
        resolution = clientInfo.getMaxSupportedScreenWidth();
%>
<html>
    <head>
        <title><%=localeTexts.getString( "calendar.title" )%></title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <meta http-equiv="Cache-Control" content="no-cache">
        <link rel="stylesheet" href="/activityreportdoc/css/calendario<%=resolution%>.css" type="text/css">

        <script type="text/javascript" src="/activityreportdoc/js/languages.js"></script>
        <script type="text/javascript">CurrentLang=<%=localeTexts.getString( "javascriptLocale" )%>;</script>
        <script type="text/javascript" src="/activityreportdoc/js/utils.js"></script>
        <script type="text/javascript" src="/activityreportdoc/js/jquery.js"></script>
        <script type="text/javascript" src="/activityreportdoc/js/calendar.js"></script>
        <script type="text/javascript" src="/activityreportdoc/js/estilo.js"></script>
    </head>


    <body onload="init()" bgcolor="#FFFFFF" text="#000000" >
        <form name="form1" method="post" action="">
            <table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="day" height="32" >
                        <table class="day" width="100%" cellspacing="0" cellpadding="0" height="40px" >
                            <tr>
                                <td rowspan="5" width="21%"></td>
                                <td width="8%" rowspan="5" >
                                    <input type="text" name="textfield2" class="t" size="5" maxlength="4" id="year" value="2004" readonly>
                                </td>
                                <td height="5%"/>
                                <td rowspan="5" width="32%"></td>
                                <td rowspan="5" width="7%" >
                                    <input type="text" name="textfield3" class="t" size="4" maxlength="2" id="month" value="07" readonly>
                                </td>
                                <td height="5%"></td>
                                <td rowspan="5" width="21%"></td>
                            </tr>
                            <tr>
                                <%-- NOTE: the following two lines must be left without spaces or line breaks, or they'll appear much higher  --%>
                                <td height="44%"><a href="#" ><img src="/activityreportdoc/images/arrow_up.png" name ="nextyear" width=8 height=8 alt=""></a></td>
                                <td height="44%"><a href="#" ><img src="/activityreportdoc/images/arrow_up.png" name ="nextmnt" width=8 height=8 alt=""></a></td>
                            </tr>
                            <tr>
                                <td height="2%"></td>
                                <td height="2%"></td>
                            </tr>
                            <tr>
                                <%-- NOTE: the following two lines must be left without spaces or line breaks, or they'll appear much higher  --%>
                                <td height="44%"><a href="#"><img src="/activityreportdoc/images/arrow_bottom.png" name="prevyear" width=8 height=8 alt=""></a></td>
                                <td height="44%"><a href="#"><img src="/activityreportdoc/images/arrow_bottom.png" name="prevmnt" width=8 height=8 alt=""></a></td>
                            </tr>
                            <tr>
                                <td height="5%"/>
                                <td height="5%"/>

                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td>
                        <table class="sch" width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td id="monthYear"colspan="7" height="26" class="tl">&nbsp;</td>
                            </tr>
                            <tr>
                                <td class="day" height="19" width="14%"><%=localeTexts.getString( "calendar.sun" )%></td>
                                <td class="day" height="19" width="14%"><%=localeTexts.getString( "calendar.mon" )%></td>
                                <td class="day" height="19" width="14%"><%=localeTexts.getString( "calendar.tue" )%></td>
                                <td class="day" height="19" width="15%"><%=localeTexts.getString( "calendar.wed" )%></td>
                                <td class="day" height="19" width="14%"><%=localeTexts.getString( "calendar.thu" )%></td>
                                <td class="day" height="19" width="14%"><%=localeTexts.getString( "calendar.fri" )%></td>
                                <td class="day" height="19" width="15%"><%=localeTexts.getString( "calendar.sat" )%></td>
                            </tr>
                            <tr>
                                <td id="day1" class="sch" height="19" width="14%">1</td>
                                <td id="day2" class="sch" height="19" width="14%">2</td>
                                <td id="day3" class="sch" height="19" width="14%">3</td>
                                <td id="day4" class="sch" height="19" width="15%">4</td>
                                <td id="day5" class="sch" height="19" width="14%">5</td>
                                <td id="day6" class="sch" height="19" width="14%">6</td>
                                <td id="day7" class="sch" height="19" width="15%">7</td>
                            </tr>
                            <tr>
                                <td id="day8" class="sch" height="19" width="14%">8</td>
                                <td id="day9" class="sch" height="19" width="14%">9</td>
                                <td id="day10" class="sch" height="19" width="14%">10</td>
                                <td id="day11" class="sch" height="19" width="15%">11</td>
                                <td id="day12" class="sch" height="19" width="14%">12</td>
                                <td id="day13" class="sch" height="19" width="14%">13</td>
                                <td id="day14" class="sch" height="19" width="15%">14</td>
                            </tr>
                            <tr>
                                <td id="day15" class="sch" height="19" width="14%">15</td>
                                <td id="day16" class="sch" height="19" width="14%">16</td>
                                <td id="day17" class="sch" height="19" width="14%">17</td>
                                <td id="day18" class="sch" height="19" width="15%">18</td>
                                <td id="day19" class="sch" height="19" width="14%">19</td>
                                <td id="day20" class="sch" height="19" width="14%">20</td>
                                <td id="day21" class="sch" height="19" width="15%">21</td>
                            </tr>
                            <tr>
                                <td id="day22" class="sch" height="19" width="14%">22</td>
                                <td id="day23" class="sch" height="19" width="14%">23</td>
                                <td id="day24" class="sch" height="19" width="14%">24</td>
                                <td id="day25" class="sch" height="19" width="15%">25</td>
                                <td id="day26" class="sch" height="19" width="14%">26</td>
                                <td id="day27" class="sch" height="19" width="14%">27</td>
                                <td id="day28" class="sch" height="19" width="15%">28</td>
                            </tr>
                            <tr>
                                <td id="day29" class="sch" height="19" width="14%">29</td>
                                <td id="day30" class="sch" height="19" width="14%">30</td>
                                <td id="day31" class="sch" height="19" width="14%">31</td>
                                <td id="day32" class="sch" height="19" width="15%">&nbsp;</td>
                                <td id="day33" class="sch" height="19" width="14%">&nbsp;</td>
                                <td id="day34" class="sch" height="19" width="14%">&nbsp;</td>
                                <td id="day35" class="sch" height="19" width="15%">&nbsp;</td>
                            </tr>
                            <tr>
                                <td id="day36" class="sch" height="19" width="14%">&nbsp;</td>
                                <td id="day37" class="sch" height="19" width="14%">&nbsp;</td>
                                <td id="day38" class="sch" height="19" width="14%">&nbsp;</td>
                                <td id="day39" class="sch" height="19" width="15%">&nbsp;</td>
                                <td id="day40" class="sch" height="19" width="14%">&nbsp;</td>
                                <td id="day41" class="sch" height="19" width="14%">&nbsp;</td>
                                <td id="day42" class="sch" height="19" width="215%">&nbsp;</td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td>
                        <table class="panel" width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td width="2%" height="28">&nbsp;</td>
                                <td height="28">
                                    <input type="text" name="textfield" size="15" maxlength="10" class="lbl" id="datefield">
                                </td>
                                <td width="25%" height="28">
                                    <input type="hidden" id="day">
                                </td>
                                <td width="17%" height="28"><a href="#" class="b" ><img alt="" src="/activityreportdoc/images/cancel.png"  name ="btnClose" align="bottom"></a></td>
                                <td width="17%" height="28"><a href="#" class="b" ><img alt="" src="/activityreportdoc/images/save.png" align="bottom" name="btnAccept"></a></td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </form>
    </body>

</html>
