#! /usr/bin/bash

pid=$(ps aux | grep share_api | grep -v grep | head -1 | awk '{print $2}')

if [ -n "$pid" ]; then
  echo "Stopping share api with PID $pid"
  kill "$pid"
else
 echo "Share api process not found"
fi