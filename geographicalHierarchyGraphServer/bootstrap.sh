#!/usr/bin/env bash

apt-get update
apt-get upgrade -y
apt-get install docker.io -y
sudo usermod -a -G docker vagrant
mkdir -p /app/logs /app/config /app/neo4j/data /app/neo4j/logs
usermod -aG docker vagrant