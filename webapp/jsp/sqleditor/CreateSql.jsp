<%@ page import="java.io.PrintWriter,common.sql.*,
		 siscorp.dbmanagement.DatabaseSC,
		 common.util.ResourceString"%>

<%!
    SQLEntry sqlEditor = null;
    HTMLSqlResult sqlResult;
    ResourceString localeTexts;
%>

<%
    response.addHeader("Cache-Control","no-cache");
    response.addHeader("Cache-Control","no-store");

    localeTexts = (ResourceString) session.getAttribute("RESOURCESTRING");
    try
    {
        sqlEditor = (SQLEntry)request.getAttribute("sql");
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
      <% if((sqlEditor!=null)&&(sqlEditor.getId()!=-1))
	 {	
	 %>
	 <input type="hidden" name="idsql" value="<%=sqlEditor.getId()%>">
	 <input type="hidden" name="group" value="<%=request.getParameter("group")%>">
     <td colspan="7" height="77" >
           <p>
                <h1><%=localeTexts.getString("sqlMode.ModifySQL")%></h1>
                <hr size="1" noshade color="#000066">
        </td>
	<%}
	else{
	%>
	 <td colspan="7" height="77" >
           <p>
                <h1><%=localeTexts.getString("sqlMode.NewSQL")%></h1>
                <hr size="1" noshade color="#000066">
        </td>
	<%}%>
   </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="desc">
  <tbody>
    <tr>
      <th width="5%" height="20" class="verticalgrid"><%=localeTexts.getString("sqlMode.NameCaption")%></th>
      <td width="95%" colspan="7" height="20" class="verticalgrid">
	<input helptext="<%=localeTexts.getString("sqlMode.NameHelp")%>" req="SI" tipo="alfanumerico" type="text" name="nombre" class="field" size="80" maxlength="20" value="<%=(sqlEditor==null)?(String)"":sqlEditor.getName()%>">
      </td>
    </tr>
    <tr>
      <th width="5%" height="20" class="verticalgrid"><%=localeTexts.getString("sqlMode.DescriptionCaption")%></th>
      <td width="95%" colspan="7" class="verticalgrid"><br>
      <textarea  helptext="<%=localeTexts.getString("sqlMode.DescriptionHelp")%>" req="SI" tipo="alfanumerico" name="descripcion" cols="80" class="field" size="83" maxlength="100" rows="1"><%=(sqlEditor==null)?(String)"":(sqlEditor.getDecription()==null?"":sqlEditor.getDecription())%></textarea></td>
    </tr>
    <tr>
      <th width="5%" height="20" class="verticalgrid"><%=localeTexts.getString("sqlMode.GroupCaption")%></th>
      <td width="95%" colspan="7" height="20" class="verticalgrid">
	<input type="text" name="grupo" class="field" size="80" maxlength="20" value="<%=(sqlEditor==null)?(String)request.getParameter("group"):(sqlEditor.getGroup()==null?"":sqlEditor.getGroup())%>">
      </td>
    </tr>
    <tr>
      <th width="5%" height="20" class="verticalgrid"><%=localeTexts.getString("sqlMode.SqlCaption")%></th>
      <td width="95%" colspan="7" class="verticalgrid"><br>
      <textarea  helptext="<%=localeTexts.getString("sqlMode.SqlHelp")%>" req="SI" tipo="alfanumerico" name="sql" cols="80" class="field" rows="10" size="83"><%=(sqlEditor==null)?(String)"":sqlEditor.getSqlStatement()%></textarea></td>
    </tr>
    <tr>
      <th width="5%" height="20" class="verticalgrid"><%=localeTexts.getString("sqlMode.TypeCaption")%></th>
      <td width="95%" colspan="7" class="verticalgrid">
      <select name="typesql" class="field">
	      <option value="4" <%=( sqlEditor!=null?(sqlEditor.getType() ==4?"selected":""):"")%>>COMMAND</option>
	      <option value="1" <%=( sqlEditor!=null?(sqlEditor.getType() ==1?"selected":""):"")%>>DELETE</option>
	      <option value="3" <%=( sqlEditor!=null?(sqlEditor.getType() ==3?"selected":""):"")%>>INSERT</option>
	      <option value="0" <%=( sqlEditor!=null?(sqlEditor.getType() ==0?"selected":""):"")%>>SELECT</option>
	      <option value="2" <%=( sqlEditor!=null?(sqlEditor.getType() ==2?"selected":""):"")%>>UPDATE</option>
      </select>
	</td>
     </tr>
    <tr>
      <th width="5%" height="20" class="verticalgrid"><%=localeTexts.getString("sqlMode.ParametersCaption")%></th>
      <td width="95%" colspan="7" class="verticalgrid"><br>
      <textarea helptext="<%=localeTexts.getString("sqlMode.ParametersHelp")%>" name="parametros" cols="80" class="field" rows="1" size="83" maxlength="100">
          <%=((String)request.getParameter("parametros")==null)?(String)"":(String)request.getParameter("parametros")%>
      </textarea>
      </td>
    </tr>
    <tr>
      <th width="5%" height="20">&nbsp;</th>
         <td width="95%"><input value="<%=localeTexts.getString("sqlMode.TestButton")%>" type="button" onclick="javascript:doChangeAction( this.form ,this.name)" name="probesql"></td>
    </tr>
 </tbody>
</table>

<% if(request.getAttribute("action")!=null)
   {%>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tbody>
     <tr>
	<td width="100%"> <% 	try
                                {
				    sqlResult = new HTMLSqlResult((DatabaseSC)request.getAttribute("db") ,sqlEditor, (String)request.getAttribute("parametros") );
                                    sqlResult.getHTMLContent( new PrintWriter( out ) );
                                }
                                catch(Exception e){%>
                                <%=e%>
				<%}%>
	</td>	
    </tr>
  </tbody>
</table>
<%}%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tbody>
    <tr>
     <%
	if((sqlEditor!=null)&&(sqlEditor.getId()!=-1))
	{ %>
	<td width="25%"><input value="<%=localeTexts.getString("sqlMode.UpdateButton")%>" type="button" onclick="javascript:submitAndValidateForm( this.form ,this.name)" name="updatesql"></td>	<%}
	else{  %>
     <td width="25%"><input value="<%=localeTexts.getString("sqlMode.AddButton")%>" type="button" onclick="javascript:submitAndValidateForm( this.form ,this.name)" name="savesql"></td>
	<%}%>
      <td width="25%"><input value="<%=localeTexts.getString("sqlMode.CancelButton")%>" type="button" onclick="javascript:doChangeAction( this.form ,this.name)" name="sqllist"></td>
      <td width="25%">&nbsp; </td>
      <td width="25%">&nbsp; </td>
    </tr>
  </tbody>
</table>
<%
 }
   catch(Exception anException)
   {
	String text = localeTexts.getString("sqlMode.ErrorInJSP")+" CreateSql.JSP: " + anException;
	System.out.println(text);
	out.println(text);
   }
%>
</form>
</body>