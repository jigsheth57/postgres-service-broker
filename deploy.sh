#!/bin/bash

mvn clean install package -DskipTests=true
cf t
echo -n "Validate the space & org, you are currently logged in before continuing!"
read

cf p

cf create-service-broker postgres-sb admin pivotal https://postgres-broker.cfapps.gcp.pcfapps.org
cf enable-service-access postgres -p standard