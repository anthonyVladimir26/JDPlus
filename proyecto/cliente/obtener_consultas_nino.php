<?php
include 'conexion_cliente.php';
$usu_id = $_GET['id'];

$consulta = "SELECT * FROM consul_nino where id ='$usu_id'";
$resultado = $conexion ->query($consulta);
 
$producto = array();

while($fila =$resultado ->fetch_array()){
   
    $producto[]= $fila;
}

echo json_encode($producto, JSON_UNESCAPED_UNICODE);
$resultado ->close();
?>