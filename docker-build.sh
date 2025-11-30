#!/bin/bash

echo "🔨 Building JAR files..."

# 1. JAR 파일 빌드
./gradlew clean
./gradlew :eureka:bootJar
./gradlew :user-service:bootJar
./gradlew :traffic-service:bootJar

echo "✅ JAR files built successfully!"
echo ""
echo "🐳 Building Docker images..."

# 2. Docker 이미지 빌드
docker build -t imjuyongp/capstone2-eureka:latest ./eureka
docker build -t imjuyongp/capstone2-user:latest ./user-service
docker build -t imjuyongp/capstone2-traffic:latest ./traffic-service

echo "✅ Docker images built successfully!"
echo ""
echo "📋 Built images:"
docker images | grep imjuyongp/capstone2

echo ""
echo "🚀 To run: docker-compose up -d"
echo "📤 To push: docker-compose push"