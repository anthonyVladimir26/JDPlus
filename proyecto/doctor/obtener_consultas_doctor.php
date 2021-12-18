<?php
include '../cliente/conexion_cliente.php';

$doctor=$_GET['doctor'];

$consulta = "SELECT * FROM consultas where claveDoctor = '$doctor'";
$resultado = $conexion ->query($consulta);

$producto = array();

while($fila =$resultado ->fetch_array()){
   
    $producto[]= $fila;
}

echo json_encode($producto, JSON_UNESCAPED_UNICODE);
$resultado ->close();
?>