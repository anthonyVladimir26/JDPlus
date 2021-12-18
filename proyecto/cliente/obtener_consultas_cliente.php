<?php
include 'conexion_cliente.php';

$clave=$_GET['clave'];

$consulta ="SELECT * FROM consultas where claveUsuario = '$clave'";
$resultado = $conexion ->query($consulta);

$producto = array();

while($fila =$resultado ->fetch_array()){
   
    $producto[]= $fila;
}

echo json_encode($producto, JSON_UNESCAPED_UNICODE);
$resultado ->close();
?>