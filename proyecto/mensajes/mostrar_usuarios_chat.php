<?php
include 'conexion_mensaje.php';
$nombreU=$_GET['userCliente'];


$consultaMensaje = "SELECT * FROM chats where userCliente = '$nombreU'";
$resultadoMensaje = $conexion ->query($consultaMensaje);

$mensajes = array();

while($filaMensaje =$resultadoMensaje->fetch_array()){
    $mensajes[]= $filaMensaje;
}

echo json_encode($mensajes, JSON_UNESCAPED_UNICODE);
$resultadoMensaje ->close();

mysqli_close($conexion);
?>