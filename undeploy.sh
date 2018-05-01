#!/bin/bash

cf delete-service-broker postgres-sb -f
echo y | cf d -r postgres-broker