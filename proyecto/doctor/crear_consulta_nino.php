<?php
include '../cliente/conexion_cliente.php';

ini_set('date.timezone','America/Mexico_City'); 

$tipo_consulta=$_POST['tipo_consulta'];
$doctora=$_POST['doctora'];
$fehca_cita=$_POST['fehca_cita'];
$hora_cita=$_POST['hora_cita'];


$tutor=$_POST['tutor'];
$nombre_tt=$_POST['nombre_tt'];
$fecha_nac=$_POST['fecha_nac'];
$edad=$_POST['edad'];
$tipo_sangre=$_POST['tipo_sangre'];
$telefono=$_POST['telefono'];
$correo=$_POST['correo'];
$enfermedad=$_POST['enfermedad'];
$enfermedad_info=$_POST['enfermedad_info'];
$medicacion=$_POST['medicacion'];
$info_medicacion=$_POST['info_medicacion'];

$nombre_nino =$_POST['nombre_nino'];
$fecha_nac_nino =$_POST['fecha_nac_nino'];
$tipo_sangre_nino =$_POST['tipo_sangre_nino'];
$edad_nino =$_POST['edad_nino'];
$enfermedad_nino =$_POST['enfermedad_nino'];
$enfermedad_nino_info =$_POST['enfermedad_nino_info'];
$medicacion_nino =$_POST['medicacion_nino'];
$medicacion_nino_info =$_POST['medicacion_nino_info'];
$hermano_nino =$_POST['hermano_nino'];
$hermano_nino_info =$_POST['hermano_nino_info'];


$pa=$_POST['pa'];
$cantidad=$_POST['cantidad'];
$claveC=$_POST['claveC'];
$claveD = $_POST['claveD'];

$hora=date("H:i:s");
$dia=date("Y-m-d");

$horaYDia = $dia." ".$hora;

echo($horaYDia);

$consulta = "INSERT INTO `junodoctor_cliente`.`consul_nino`
(`fecha_hora`,`tipo_consulta`,`doctora`,`fehca_cita`,`hora_cita`,`tutor`,`nombre_tt`,`fehca_na`,`tipo_san`,`telefono`,`correo`,`enfermedad`,`info_enfermedad`,`medicacion`,`infi_medic`,`nombre_nino`,`fecha_nino`,`tipo_sangre`,`edad`,`enfer_nino`,`info_enfer_nino`,`medicacion_nino`,`info_medicacion_nino`,`hermano`,`info_hermano`,`pa;`,`cantidad`,`claveCliente`,`clave_doctor`)
VALUES
('$horaYDia','$tipo_consulta','$doctora','$fehca_cita','$hora_cita','$tutor','$nombre_tt','$fecha_nac','$tipo_sangre','$telefono','$correo','$enfermedad','$enfermedad_info','$medicacion','$info_medicacion','$nombre_nino','$fecha_nac_nino','$tipo_sangre_nino','$edad_nino','$enfermedad_nino','$enfermedad_nino_info','$medicacion_nino','$medicacion_nino_info','$hermano_nino','$hermano_nino_info','$pa','$cantidad','$claveD','$claveD');";//Sentencia sql a realizar


mysqli_query($conexion,$consulta);


?>