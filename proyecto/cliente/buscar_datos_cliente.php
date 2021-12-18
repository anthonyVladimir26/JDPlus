<?php
include 'conexion_cliente.php';

$usu_usuario=$_GET['usuario'];

$consulta = "SELECT * FROM usuario where tipo= '$usu_usuario'";
$resultado = $conexion ->query($consulta);
$producto = array();

if($fila =$resultado ->fetch_array()){
    $producto[]= $fila;
}

echo json_encode($producto, JSON_UNESCAPED_UNICODE);
    $resultado ->close();
?>
