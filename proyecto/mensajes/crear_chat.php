<?php
include '../cliente/conexion_cliente.php';

$nombreChat=$_POST['nombreChat'];

$cliente=$_POST['cliente'];
$doctor=$_POST['doctor'];
$claveDoctor= $_POST['claveDoctor'];
$claveCliente= $_POST['claveCliente'];

$verificador = false;

$consulta = "SELECT * FROM chats";
$resultado = $conexion ->query($consulta);


while($fila =$resultado ->fetch_array()){

    if($fila['nombreChat'] == $nombreChat){
		$verificador= true;
	}
	
}

if ($verificador == true){
	echo "no se creo tabla";
}else{
	echo "se creo tabla";
	$consulta ="INSERT INTO `chats`(`nombreChat`,`nombreUsuario`,`nombreDoctor`,`claveDoctor`,`claveUsuario`)
				VALUES('$nombreChat','$cliente','$doctor','$claveDoctor','$claveCliente');";//Sentencia sql a realizar

				
		mysqli_query($conexion,$consulta);


	/*$tabla ="create table if not exists $nombreChat
	  (
		id int auto_increment primary key,
		claveEnvia char(5),
		mensajes VARCHAR(32765),
		imagenes varchar(400),
		hora time,
		dia date,
		tipo int
	)";
	
	mysqli_query($conexion,$tabla);
	*/
	
	mkdir("../imagenes/chats/$nombreChat");

	  }


$resultado ->close();

mysqli_close($conexion);

?>