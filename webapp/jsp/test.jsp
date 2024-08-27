<!doctype html>
<html><head>
</head>

	<body>
    <button id="boton-usuarios">Obtener usuarios</button>
    
    <div id="lista-usuarios"></div>

	</body>	
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
	<script>
	$("#boton-usuarios").on("click", getUsers);
	function getUsers() {
	  $.ajax({	      
		url: '/ActivityReport/do/getActivityDetail',
		success: function(respuesta) {
			console.log(respuesta);      
		},
		error: function() {
		  console.log("No se ha podido obtener la información");
			}
			});
			}	
	</script>
</html>