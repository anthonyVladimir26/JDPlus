<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname   = "junodoctor";
$dbServerPort = "3306";

// Create conexionection
$conexion = new mysqli($servername, $username, $password, $dbname, $dbServerPort) or die('Unable to conexionect');

// Check conexionection
if ($conexion->connect_error) {
    die("conexionection failed: " . $conexion->connect_error);
} 

?>