<?php
include '../cliente/conexion_cliente.php';


$consulta = "SELECT * FROM consultas";
$resultado = $conexion ->query($consulta);

$producto = array();

while($fila =$resultado ->fetch_array()){
   
    $producto[]= $fila;
}

echo json_encode($producto, JSON_UNESCAPED_UNICODE);
$resultado ->close();
?>