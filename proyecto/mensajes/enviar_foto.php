<?php

ini_set('date.timezone','America/Mexico_City'); 


$nombreChat=$_POST['nombreChat'];

$imagenes=$_POST['imagenes'];
$nombreImagen=$_POST['nombreImagen'];

$path = "imagenes/chats/$nombreChat/$nombreImagen";
$path2 ="../$path";

file_put_contents($path2,base64_decode($imagenes));
$bytesArchivo = file_get_contents($path2);


echo "registra";

?>