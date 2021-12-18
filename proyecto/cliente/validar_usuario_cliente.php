<?php
include 'conexion_cliente.php';
$usu_usuario=$_POST['usuario'];
$usu_password=$_POST['password'];

//$usu_usuario="anyel";
//$usu_password="123456";

$sentencia=$conexion->prepare("SELECT * FROM usuario WHERE usuario=? AND contra=?");
$sentencia->bind_param('ss',$usu_usuario,$usu_password);
$sentencia->execute();

$resultado = $sentencia->get_result();
if ($fila = $resultado->fetch_assoc()) {
         echo json_encode($fila,JSON_UNESCAPED_UNICODE);     
}

$sentencia->close();
$conexion->close();
?>