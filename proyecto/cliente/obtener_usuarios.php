<?php
include 'conexion_cliente.php';


$consulta = "SELECT * FROM usuario ";
$resultado = $conexion ->query($consulta);

$usuarios = array();

while($fila =$resultado ->fetch_array()){
   
    $usuarios[]= $fila;
}

echo json_encode($usuarios, JSON_UNESCAPED_UNICODE);
$resultado ->close();
?>

