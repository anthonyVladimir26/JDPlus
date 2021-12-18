<?php
include 'conexion_mensaje.php';
$nombreChat=$_GET['nombreChat'];


$consultaMensaje = "SELECT * FROM $nombreChat";
$resultadoMensaje = $conexion ->query($consultaMensaje);

$mensajes = array();

while($filaMensaje =$resultadoMensaje->fetch_array()){
    $mensajes[]= $filaMensaje;
}

echo json_encode($mensajes, JSON_UNESCAPED_UNICODE);
$resultadoMensaje ->close();

mysqli_close($conexion);
?>