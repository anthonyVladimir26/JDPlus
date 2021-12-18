<?php
include 'conexion_mensaje.php';
$nombreU=$_GET['userDoctor'];


$consultaMensaje = "SELECT * FROM chats where userDoctor = '$nombreU'";
$resultadoMensaje = $conexion ->query($consultaMensaje);

$mensajes = array();

while($filaMensaje =$resultadoMensaje->fetch_array()){
    $mensajes[]= $filaMensaje;
}

echo json_encode($mensajes, JSON_UNESCAPED_UNICODE);
$resultadoMensaje ->close();

mysqli_close($conexion);
?> 