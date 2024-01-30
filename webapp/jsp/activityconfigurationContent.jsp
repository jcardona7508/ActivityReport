<%@ page import="siscorp.framework.application.EventResponse, java.util.Properties,com.gausssoft.configuration.SettingTO"%> 
<%
EventResponse eventResponse;
Properties properties=null;
String message=null, click=null;
SettingTO settingTO=null; 
int i=0;
if ( ( eventResponse = (EventResponse)  request.getAttribute("response") ) != null )
{
    properties = ( Properties ) eventResponse.getAttribute("authProperties");
    settingTO = ( SettingTO ) eventResponse.getAttribute("appSettings"); 
    message = ( String ) eventResponse.getAttribute("message");    
    click = ( String ) eventResponse.getAttribute("clickldap");    											   
}
%>
<html class=" js no-flexbox canvas canvastext webgl no-touch geolocation postmessage websqldatabase indexeddb hashchange history draganddrop websockets rgba hsla multiplebgs backgroundsize borderimage borderradius boxshadow textshadow opacity cssanimations csscolumns cssgradients cssreflections csstransforms csstransforms3d csstransitions fontface generatedcontent video audio localstorage sessionstorage webworkers applicationcache svg inlinesvg smil svgclippaths" lang="en">
    <head>
		<meta charset="utf-8">

		<!-- Use the .htaccess and remove these lines to avoid edge case issues.
		More info: h5bp.com/i/378 -->
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		
		<title>Activity Report</title>
		<meta name="description" content="Activity Report">

		<meta name="viewport" content="width=device-width">

        <link rel="stylesheet" type="text/css" href="/activityreportdoc/css/configuration.css">
		<link rel="stylesheet" type="text/css" href="/activityreportdoc/css/style.css">
		<link rel="stylesheet" type="text/css" href="/activityreportdoc/css/base.css">
		<link rel="stylesheet" type="text/css" href="/activityreportdoc/css/modal.css">
		<link rel="stylesheet" type="text/css" href="/activityreportdoc/css/main.css">
		<script type="text/javascript">
			function doExit( form )
			{
			    form.action = "/ActivityReport/do/configLogin";
				submitForm( form );
			}
			function doSave( form )
			{
			    document.getElementById("pass").value=' ';
			    document.getElementById("user").value=' ';
			    if(validateDataInput(form.id))
		        {
			        form.action = "/ActivityReport/do/checkConfigLogin";
			        submitForm( form );
		        }
			}
			function doSaveConf( form )
			{
			    document.getElementById("error").innerHTML="";
			    if(validateDataAll(form.id))
		        {
			        form.action = "/ActivityReport/do/installApp";
					submitForm( form );		        
		        }
			}
			function doTest( form )
			{
			    document.getElementById("error").innerHTML="";
			    form.action = "/ActivityReport/do/testAuthConfig";
				submitForm( form );
			}
			function doTestDB( form )
			{
			    document.getElementById("error").innerHTML="";
			    form.action = "/ActivityReport/do/testDB";
				submitForm( form );
			}
			function submitForm( form )
			{ 
				form.submit();
			}
			function buttonSelectedApp()
			{
			    var tabContentApp = document.getElementById("tabContentApp");
			    var tabContentLDAP = document.getElementById("tabContentLDAP");
			    tabContentApp.classList.add("displayOn");			    
			    tabContentApp.classList.remove("displayOff");
			    tabContentLDAP.classList.add("displayOff");
			    tabContentLDAP.classList.remove("displayOn");
			    document.getElementById("buttonTabConfig").classList.add("buttonTabSelected");
			    document.getElementById("buttonTabLDAP").classList.remove("buttonTabSelected");
			    document.getElementById("error").innerHTML="";
			}
			function buttonSelectedLDAP()
			{
			    var tabContentApp = document.getElementById("tabContentApp");
			    var tabContentLDAP = document.getElementById("tabContentLDAP");
			    tabContentApp.classList.add("displayOff");			    
			    tabContentApp.classList.remove("displayOn");			    
			    tabContentLDAP.classList.add("displayOn");
			    tabContentLDAP.classList.remove("displayOff");
			    document.getElementById("buttonTabConfig").classList.remove("buttonTabSelected");
			    document.getElementById("buttonTabLDAP").classList.add("buttonTabSelected");
			    document.getElementById("pass").value='';
			}			
			function clickElement(elementId)
			{
			    let el;
			    el = document.getElementById(elementId);    
			    if (el.fireEvent) {
			        el.fireEvent('on' + etype);
			    } 
			    else
			    {
			        let evObj = document.createEvent('Events');
			        evObj.initEvent('click', true, false);
			        el.dispatchEvent(evObj);    
			    }
			}
			function doChangeDriver()
			{
			    var driver = document.getElementById("selectedDriver").value;			    
			    
			    if(driver.includes("sqlserver"))
			    {
			        document.getElementById("Driver").value ="com.microsoft.sqlserver.jdbc.SQLServerDriver";
			        document.getElementById("DSN").value ="jdbc:sqlserver://[HOST];instanceName=[INSTANCE_NAME];databaseName=[DATABASE_NAME]";
			    }
			    if(driver.includes("mysql"))
		        {
			        document.getElementById("Driver").value ="com.mysql.jdbc.Driver";
			        document.getElementById("DSN").value ="jdbc:mysql://[HOST]/[DATABASE_NAME]?autoReconnect=true";
		        }
			    if(driver.includes("oracle"))
		        {
			        document.getElementById("Driver").value ="oracle.jdbc.driver.OracleDriver";
			        document.getElementById("DSN").value ="jdbc:oracle:thin:@[HOST]:[PORT]:[SID]";
		        }
			    if(driver.includes("postgresql"))
		        {
			        document.getElementById("Driver").value ="org.postgresql.Driver";
			        document.getElementById("DSN").value ="jdbc:postgresql://[HOST]/[DATABASE_NAME]";
		        }
			    if(driver.includes("sybase"))
		        {
			        document.getElementById("Driver").value ="com.sybase.jdbc2.jdbc.SybDriver";
			        document.getElementById("DSN").value ="jdbc:sybase:Tds:[HOST]:[PORT]/[DATABASE_NAME]";
		        }			    
			    if(driver.includes("-otro-"))
		        {
			        document.getElementById("Driver").value ="";
			        document.getElementById("DSN").value ="";
		        }
			}
			function validateDataInput(container_id)
			{
			    var nodes = document.querySelectorAll("#"+container_id+" input[type=text]");
			    var resp = true;
			    for (var i=0; i<nodes.length; i++)
				{
			        nodes[i].style.background = '';
			        if (nodes[i].value == "")
			        {   nodes[i].style.background = 'red';
			            resp = resp&&false;
			        }
				}
			    return resp;
			}
			function validateDataAll(container_id)
			{
			    var nodes = document.querySelectorAll("#"+container_id+" input[type=text]");
			    var resp;
			    resp = validateDataInput(container_id);
			    nodes = document.querySelectorAll("#"+container_id+" input[type=password]");
			    for (var i=0; i<nodes.length; i++)
				{
			        nodes[i].style.background = '';
			        if (nodes[i].value == "")
					{
			            nodes[i].style.background = 'red';
			            resp = resp&&false;
			        }
				}
			    return resp;
			}
			function showUninstallMessage()
			{
			    document.getElementById("error").innerHTML="";
			    document.getElementById("modalWindow").classList.add("displayOn");			    
			}
			function closeUninstallMessage()
			{
			    document.getElementById("modalWindow").classList.remove("displayOn");			    
			}
			function doUninstall( form )
			{
			    document.getElementById("error").innerHTML="";
			    form.action = "/ActivityReport/do/unInstallApp";
				submitForm( form );
			}
			function load()
			{
			    document.getElementById("pass").value='';
			}
			function doChangeDefaultLogo()
			{
			    document.getElementById("upload").classList.toggle("displayOff");			    
			}
			
			function doUpload()
			{
			    document.getElementById("error").innerHTML="";
			    if( document.getElementById("fileToUpload").files[0]!==undefined )
			    {    
				    document.getElementById("error").innerHTML="";
				    var formData = new FormData();
				    formData.append("defaultLogo", document.getElementById("fileToUpload").files[0]);
				    var request = new XMLHttpRequest();				    
				    request.open("POST", "/ActivityReport/go/uploadLogo", true);
				    request.setRequestHeader('appName', 'ActivityRecordWeb');
				    /*request.addEventListener("load", successUpload, false);
				    request.addEventListener("error", errorUpload, false);*/
				    request.onreadystatechange = function (aEvt) {
				        if (request.readyState == 4) {
				           if(request.status == 200&&request.responseText.indexOf("Success")>0)
				               successUpload();
				           else
				               errorUpload();
				        }
				      };
				    request.send(formData);
			    }
			    else
			        document.getElementById("error").innerHTML="Debe seleccionar un archivo";
			}
			function successUpload()
			{
			    document.getElementById("error").innerHTML="Logo actualizado";
			}
			function errorUpload()
			{
			    document.getElementById("error").innerHTML="Error cargando archivo";
			}
		</script>
		
	</head>
	<body id="body" onload="load()" style="overflow: auto;">		
		<div id="header">
			<div class="floatL"><img src="/activityreportdoc/images/activitylogo.png">
			</div>
			<div class="floatR">
				<div id="options"><button id="startApp" class="button_out" onClick="doExit(document.forms['confForm']);return false;"></button> </div>
				<div id="toolbar">
				</div>
			</div>
		</div>
		<div id="configuration_content">
			<hr>
			<div id="content_block" class="content_block ">				
			<div id="configurationBody" class="content header">
				<div class="">
				<p id="buttonTabConfig" class="buttonTab buttonTabSelected" onClick="buttonSelectedApp()">CONFIGURACI&Oacute;N RACT</p>
			  <%if(settingTO!=null){%>	
				<p id="buttonTabLDAP" class="buttonTab" onClick="buttonSelectedLDAP()">AUTENTICACI&Oacute;N LDAP</p>
			  <%} %>
				<div id="modalWindow" class="modal">
					<div id="modalContent">
						<div class="formoid-solid-blue">
					    <div class="title">						
					            <h2 id="messageTitle">ADVERTENCIA</h2>
					        </div>					
					        <div class="element-input">
					            <img id="imgINFO" class="imageMessage displayOff" src="/activityreportdoc/images/Valid.png">
					            <img id="imgERROR" class="imageMessage displayOff" src="/activityreportdoc/images/error.png">
					            <img id="imgWARNING" class="imageMessage" src="/activityreportdoc/images/warning.png">
					            <label class="lblMassage" for="nombre" id="lblMessage">¿Desea desinstalar la aplicación?</label>
					        </div>
					        <div class="submit submitMessage">
					            <input id="yes" type="submit" value="Aceptar" onClick="doUninstall(document.forms['appConfForm']);return false;">
					            <input id="cancel" type="submit" value="Cancelar" onClick="closeUninstallMessage();return false;">
					        </div>
						</div>
					</div>
				</div>	
				<div id="tabContentApp" class="tab_container displayOn">
						<div id="tabBody" class="">
							<div id="AppConf" class="tab_content" style="display:block;">
								<form method="post" id="appConfForm" action="">									
									<button id="btnSaveConf" class="button_save" onClick="doSaveConf(document.forms['appConfForm']);return false;"></button>
									<%
									if(settingTO!=null)
									{
									%>								
									<button id="btnUninstall" class="button_uninstall" onClick="showUninstallMessage();return false;"></button>
									<%}%>								
									<fieldset class="parameters" id="confParameters">																			
										<label>Directorio de instalaci&oacute;n:</label>										
										<input type="Text" name="applicationPath" id="applicationPath" value="<%=settingTO==null?"":settingTO.settings.get("applicationPath")%>" <%=settingTO!=null?" readonly":""%>>
										<label>Driver:</label>
										<select id="selectedDriver" name="type" class="selectedDriver" onChange="doChangeDriver()">
											<option value="-otro-">OTRO</option>
											<option value="com.microsoft.sqlserver.jdbc.SQLServerDriver" <%=settingTO!=null&&settingTO.settings.get("activityrecord.Driver").contains( "sqlserver" )?" selected":""%> >SQL Server</option>
											<option value="com.mysql.jdbc.Driver" <%=settingTO!=null&&settingTO.settings.get("activityrecord.Driver").contains( "mysql" )?" selected":""%>>MySQL</option>
											<option value="oracle.jdbc.driver.OracleDriver" <%=settingTO!=null&&settingTO.settings.get("activityrecord.Driver").contains( "oracle" )?" selected":""%>>Oracle</option>
											<option value="org.postgresql.Driver" <%=settingTO!=null&&settingTO.settings.get("activityrecord.Driver").contains( "postgresql" )?" selected":""%>>PostgresSQL</option>
											<option value="com.sybase.jdbc2.jdbc.SybDriver" <%=settingTO!=null&&settingTO.settings.get("activityrecord.Driver").contains( "sybase" )?" selected":""%>>Sybase</option>																						
										</select>
										<input type="Text" name="Driver" id="Driver" value="<%=settingTO==null?"":settingTO.settings.get("activityrecord.Driver")%>" style="width: 246px;">
                                        <label>DSN:</label>										
										<input type="Text" name="DSN" id="DSN" value="<%=settingTO==null?"":settingTO.settings.get("activityrecord.DSN")%>">
										<label>Usuario DB:</label>										
										<input type="Text" name="User" id="User" value="<%=settingTO==null?"":settingTO.settings.get("activityrecord.User")%>">
										<label>Contrase&ntilde;a DB:</label>										
										<input type="Password" name="Password" id="Password" value="<%=settingTO==null?"":settingTO.settings.get("activityrecord.Password")%>">
										<%if(settingTO!=null)
										{
										%>
											<button id="btnTestDB" class="button_test" onClick="doTestDB(document.forms['appConfForm']);return false;"></button>
										<%}%>
										<p>Uppercase:<input style="width: 30px" type="checkbox" id="upperCase" <%=settingTO==null?"":settingTO.settings.get("activityrecord.UpperCase")!=null&&settingTO.settings.get("activityrecord.UpperCase").equals("true")?(String)"checked":""%> name="upperCase">
										Recordar ultimo usuario (Login):<input style="width: 30px" type="checkbox" id="rememberUser" <%=settingTO==null?"":settingTO.settings.get("activityrecord.RememberUser")!=null&&settingTO.settings.get("activityrecord.RememberUser").equals("true")?(String)"checked":""%> name="rememberUser">
										</p>
										<p>
										Mostrar códigos de actividades:<input style="width: 30px" type="checkbox" id="showActivityCode" <%=settingTO==null?"":settingTO.settings.get("activityrecord.ShowActivityCode")!=null&&settingTO.settings.get("activityrecord.ShowActivityCode").equals("true")?(String)"checked":""%> name="showActivityCode">
										</p>
										<p>
										Debug log:<input style="width: 30px" type="checkbox" id="logLevel" <%=settingTO==null?"":settingTO.settings.get("LogLevel")!=null&&settingTO.settings.get("LogLevel").equals("DEBUG")?(String)"checked":""%> name="logLevel"> 
										</p>
										<label>Contrase&ntilde;a de Administraci&oacute;n</label>										
										<input type="Password" name="adminPassword" id="adminPassword" value="<%=settingTO==null?"":settingTO.settings.get("adminPassword")%>">		
										<label></label>
										<%
										String logo ="";
										String classDisplay="";
										if(settingTO!=null&&(settingTO.settings.get("activityrecord.DefaultLogo")==null||settingTO.settings.get("activityrecord.DefaultLogo").equals("true")))
										{   
										    logo="checked";
										    classDisplay="displayOff";
										}										
										%>
										<p>Logo por defecto:<input style="width: 30px" type="checkbox" id="defaultLogo" <%=logo%> name="defaultLogo" onChange="doChangeDefaultLogo()"></p>
										<div id="upload" class="<%=classDisplay%>">
											<form enctype="multipart/form-data" method="post" name="fileinfo">
												<input type="file" name="fileToUpload" id="fileToUpload">
											</form>
											<button id="btnUploadLogo" class="uploadLogo" onclick="doUpload();return false;"></button>
										</div>
										<label>Pie de página:</label>
										<input id="pie" name="pie" type="Text" value="<%=(settingTO==null||settingTO.settings.get("activityrecord.Footer")==null)?"":settingTO.settings.get("activityrecord.Footer")%>" >
									</fieldset>
								</form>								
							</div>						
					   </div>
					</div>
						<div id="tabContentLDAP" class="tab_container displayOff">
								<div id="tabBody" class="">
									<div id="LDAPConf" class="tab_content" style="display:block;">
										<form method="post" id="confForm" action="">									
											<button id="btnSaveEtl" class="button_save" onClick="doSave(document.forms['confForm']);return false;"></button>									
											<fieldset class="parameters" id="etlParameters">										
												<p>Activar:<input style="width: 30px" type="checkbox" id="active" <%=properties!=null&&properties.getProperty("active")!=null&&properties.getProperty("active").equals("true")?(String)"checked":""%> name="active"></p>																														
												<label>Nombre o IP del servidor LDAP:</label>										
												<input type="Text" name="ipServer" id="ipServer" value="<%=(properties==null||properties.getProperty("server")==null)?(String)"":properties.getProperty("server")%>">
												<label>Puerto:</label>										
												<input type="Text" name="port" id="port" value="<%=(properties==null||properties.getProperty("port")==null)?(String)"":properties.getProperty("port")%>">
		                                        <label>Dominio:</label>										
												<input type="Text" name="domain" id="domain" value="<%=(properties==null||properties.getProperty("domain")==null)?(String)"":properties.getProperty("domain")%>">										
												<label></label>									
												<p class="titleConfig">TEST</p>
												<label>Usuario:</label>										
												<input type="Text" name="user" id="user">
												<label>Contrase&ntilde;a:</label>
												<input type="Password" name="pass" id="pass" autocomplete="off">
												<button id="btnTest" class="button_test" onClick="doTest(document.forms['confForm']);return false;"></button>
											</fieldset>
										</form>								
									</div>						
							   </div>
							</div>			
						</div>					
			</div> <!-- configuration -->
				
			</div>
			<!-- content_block -->
		</div>
		<hr>
		<% 
			if(message!=null)
			{
			    out.print("<p id=\"error\" class=\"errorConfig\">" + message + "</p>");
			}
			else
			    out.print("<p id=\"error\" class=\"errorConfig\"></p>");
		    if(click!=null)
		    {
			%>
			  <script type="text/javascript">
					clickElement("buttonTabLDAP");
			  </script>
			<%
		    }
			%>		
		<div id="mask" style="display: none;"></div>
		<div id="modal" style="display: none; position: absolute; min-width: 370px; min-height: 100px; border: solid rgb(174, 174, 174); overflow: auto; text-align: center; top: 416.5px; left: 452px;"></div></body></html>