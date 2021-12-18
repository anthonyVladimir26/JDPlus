<?php
include 'conexion_doctor.php';

$usu_id=$_GET['id'];

$consulta = "SELECT * FROM especialidad where id ='$usu_id'";
$resultado = $conexion ->query($consulta);

$producto = array();

if($fila =$resultado ->fetch_array()){
    $producto[]= $fila;
}

echo json_encode($producto, JSON_UNESCAPED_UNICODE);
$resultado ->close();
?>