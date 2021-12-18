<?php
include 'conexion_doctor.php';

$usu_usuario=$_GET['usuario'];

$consulta = "SELECT * FROM usuarios where usuario='$usu_usuario'";
$resultado = $conexion ->query($consulta);

$producto = array();

if($fila =$resultado ->fetch_array()){
    $producto[]= $fila;
}

echo json_encode($producto, JSON_UNESCAPED_UNICODE);
$resultado ->close();
?>