<?php
include '../cliente/conexion_cliente.php';

ini_set('date.timezone','America/Mexico_City'); 

$tipo_consulta=$_POST['tipo_consulta'];
$doctora=$_POST['doctora'];
$fehca_cita=$_POST['fehca_cita'];
$hora_cita=$_POST['hora_cita'];
$genero=$_POST['genero'];
$nombre=$_POST['nombre'];
$fecha_nac=$_POST['fecha_nac'];
$edad=$_POST['edad'];
$tipo_sangre=$_POST['tipo_sangre'];
$telefono=$_POST['telefono'];
$correo=$_POST['correo'];
$enfermedad=$_POST['enfermedad'];
$enfermedad_info=$_POST['enfermedad_info'];
$medicacion=$_POST['medicacion'];
$info_medicacion=$_POST['info_medicacion'];
$pa=$_POST['pa'];
$cantidad=$_POST['cantidad'];
$claveC=$_POST['claveC'];
$claveD = $_POST['claveD'];

$hora=date("H:i:s");
$dia=date("Y-m-d");

$horaYDia = $dia." ".$hora;

echo($horaYDia);

$consulta = "INSERT INTO consul_adulto
(`fecha_hora`,`tipo_consulta`,`doctora`,`fehca_cita`,`hora_cita`,`genero`,`nombre`,`fecha_nac`,`edad`,`tipo_sangre`,`telefono`,`correo`,`enfermedad`,`enfermedad_info`,`medicacion`,`info_medicacion`,`pago`,`cantidad`,`clave_cliente`,`clave_doctor`)
VALUES
('$horaYDia','$tipo_consulta','$doctora','$fehca_cita','$hora_cita','$genero','$nombre','$fecha_nac','$edad','$tipo_sangre','$telefono','$correo','$enfermedad','$enfermedad_info','$medicacion','$info_medicacion','$pa','$cantidad','$claveC','$claveD');";//Sentencia sql a realizar


mysqli_query($conexion,$consulta);


?>