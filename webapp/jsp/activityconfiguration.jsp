<%@ page import="siscorp.framework.application.EventResponse"%> 

<%
EventResponse eventResponse; 
String message=null;
if ( ( eventResponse = (EventResponse)  request.getAttribute("response") ) != null )
{
    message = ( String ) eventResponse.getAttribute("message");    
}
%>
<html>
	<head>
    	<meta name="viewport" content="width=device-width, initial-scale=1">
		<title>
		Activity Report Configuration Manager
		</title>
		<link rel="stylesheet" type="text/css" href="/activityreportdoc/css/bootstrap.css">
		<link rel="stylesheet" type="text/css" href="/activityreportdoc/css/bootstrap-gauss.css">
		<link rel="stylesheet" href="/activityreportdoc/css/dbmcssbase.css">	
		<link rel="stylesheet" href="/activityreportdoc/css/configuration.css">
		<link rel="stylesheet" href="/activityreportdoc/css/main.css">
		<link rel="stylesheet" media="only screen" href="/activityreportdoc/css/dbmcss.css">
		<link rel="stylesheet" href="/activityreportdoc/css/control.css">
		<link href="/activityreportdoc/css/css-Basic.css" rel="stylesheet" title="Basic" type="text/css"/>		 
		<script>
			function doOk( form )
			{					
				submitForm( form );
			}
			function submitForm( form )
			{ 
				form.submit();
			}		
		</script>
	</head>
	<body>
		<header>            
        </header>
        <div id="main" class="displayOff">
			<img alt="" src="/activityreportdoc/images/activitylogo.png" />
			<div id="adminTille">Activity Report Configuration Manager</div>
			<hr>
			<div id="client"></div>
        </div>
        <div id="login-form">        	
					<form id="login" method="post" action="/ActivityReport/do/checkConfigLogin" class="loginForm">
						<fieldset>
							<img src="/activityreportdoc/images/activitylogo.png" alt="Configuration">
							
							<label for="password">Contrase&ntilde;a:</label>
							<input type="password" name="password" id="idPassword" AUTOCOMPLETE="off">							
							<!--<input type="submit" name="submit" class="button" id="loginConfButton" value="Iniciar Sesi&oacute;n">-->
							<a href="Iniciar Sesi&oacute;n" class="button" target="_self" onclick="doOk(document.forms['login']);return false;">
								Iniciar Sesi&oacute;n
		    				</a>
		    				<%
		    				if ( message != null )
		    					out.print("<p class=\"errorConfig\">" + message + "</p>");
		    				else
		    					out.print("<p class=\"errorConfig\"></p>");
		    				%>
						</fieldset>
					</form>			
		</div>
	</body>
</html>