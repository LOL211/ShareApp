#! /usr/bin/bash

echo "Stopping share api"
bash stop.sh
echo "Starting share api"
nohup java -jar share_api-0.0.1.jar > /dev/null 2>&1 &