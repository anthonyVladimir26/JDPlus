<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname   = "junodoctor_cliente";
$dbServerPort = "3306";

// Create conexionection
$conexion = new mysqli($servername, $username, $password, $dbname, $dbServerPort) or die('Unable to conexionect');
mysqli_set_charset($conexion,"utf8");

// Check conexionection
if ($conexion->connect_error) {
    die("conexionection failed: " . $conexion->connect_error);
} 

?>