<?php
define('HOST', 'localhost');
define('PORT', 8000);
define('SERVER_ROOT', __DIR__);

$host = HOST;
$port = PORT;
$root = SERVER_ROOT;

echo "===========================================\n";
echo "  Servidor PHP\n";
echo "===========================================\n";
echo "  URL:   http://{$host}:{$port}\n";
echo "  Root:  {$root}\n";
echo "  API:   http://{$host}:{$port}/api.php\n";
echo "===========================================\n";
echo "  Pulsa Ctrl+C para detener el servidor\n\n";

$comando = sprintf(
    'php -S %s:%d -t %s',
    $host,
    $port,
    escapeshellarg($root)
);

passthru($comando);