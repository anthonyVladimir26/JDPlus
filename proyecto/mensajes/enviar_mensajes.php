<?php
include 'conexion_mensaje.php';

ini_set('date.timezone','America/Mexico_City'); 

$nombreChat=$_POST['nombreChat'];
$usuarioEnvia=$_POST['usuarioEnvia'];
$mensajes=$_POST['mensajes'];
$tipo=$_POST['tipo'];

$hora=date("H:i");
$dia=date("Y-m-d");

$consulta = "insert into $nombreChat (usuarioEnvia,mensajes,tipo,hora,dia) values ('$usuarioEnvia','$mensajes','$tipo','$hora','$dia')";//Sentencia sql a realizar
mysqli_query($conexion,$consulta);

?>