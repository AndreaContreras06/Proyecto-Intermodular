<?php

require_once 'db_config.php';

header('Content-Type: application/json');

// Permitir lanzar la api en todos los navegadores
header('Access-Control-Allow-Origin: *');

$pdo = conectarBBDD();

$metodo = $_SERVER['REQUEST_METHOD'];

if($metodo === 'GET'){

    $stmt = $pdo -> query("SELECT * FROM tabla WHERE ...")

    //EJEMPLO 
    $usuarios = $stmt->fetchAll();

    echo json_encode($usuarios);

}elseif ($metodo === 'POST'){

    $datos = json_decode(file_get_contents('php://input'), true);

    $stmt = $pdo -> prepare("INSERT INTO tabla (campos) VALUES (valores)");

    $stmt -> execute([

        ':valor' => $datos['campo'],
        ':valor' => $datos['campo'],
    ]);

    echo json_encode([
        'ok' => true,
        'message' => 'mensaje de confirmacion'
    ]);

}
