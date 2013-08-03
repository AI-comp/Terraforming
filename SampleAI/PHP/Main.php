#!/usr/bin/php -q
<?php
echo "SamplePHP\n";
$fp = fopen('php://stdin', 'r');
do {
    $line = trim(fgets($fp));
    if ($line === "EOS") {
        echo "finish", "\n";
    }
} while (!feof($fp));
