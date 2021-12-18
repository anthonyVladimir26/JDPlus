<?php
include 'conexion_doctor.php';


$consulta = "SELECT * FROM doctores";
$resultado = $conexion ->query($consulta);

$usuarios = array();

while($fila =$resultado ->fetch_array()){
   
    $usuarios[]= $fila;
    
}

echo json_encode($usuarios, JSON_UNESCAPED_UNICODE);
$resultado ->close();
?>

