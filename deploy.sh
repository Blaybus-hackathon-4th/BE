#!/bin/bash
set -e

echo ">>>>> Login to ECR"
aws ecr get-login-password --region ap-northeast-2 \
  | docker login --username AWS --password-stdin "$1"


echo ">>>>> Deploy"
docker compose -f docker-compose.prod.yml down
docker compose -f docker-compose.prod.yml pull
docker compose -f docker-compose.prod.yml up -d --remove-orphans

echo ">>>>> Done"
