#!/usr/bin/env bash

# Остановка при первой ошибке
set -e

# Вывод каждой команды перед выполнением
set -x

# Проверка наличия аргумента с названием ветки
if [ "$#" -ne 1 ]; then
  echo "Usage: $0 <branch_name>"
  exit 1
fi

# Сохраняем название ветки в переменную
BRANCH_NAME=$1

# Шаг 1: Получение последних изменений из репозитория
git fetch

# Шаг 2: Переключение на основную ветку
git checkout "$BRANCH_NAME"

# Шаг 3: Слияние последних изменений
git pull origin "$BRANCH_NAME"

# На сервере приложение запускается отдельным compose-файлом: базу даёт
# common-infra, а docker-compose.yml с её локальной копией используется только
# на машине разработчика.
COMPOSE="docker compose -f docker-compose.prod.yml"

[ -f .env.prod ] || { echo "Нет .env.prod с параметрами подключения к common-infra"; exit 1; }
docker network inspect common-infra >/dev/null 2>&1 || { echo "Нет сети common-infra - не поднята общая инфраструктура"; exit 1; }

# Шаг 4: Остановка всех работающих контейнеров
$COMPOSE down

rm -rf build
rm -rf node_modules
rm -rf frontend/generated
rm -rf src/main/bundles

# Шаг 5: Удаление прежнего образа
# На чистом сервере образа ещё нет, а set -e превратил бы это в падение деплоя.
docker rmi -f vibe-json-vibe-json:latest || true

$COMPOSE rm -f
docker image prune -f --filter "label=com.docker.compose.project=vibe-json"

# Шаг 6: Сборка проекта vibe-json
# Проект собирается под Java 17: на сервере версия по умолчанию другая, поэтому
# JDK выбирается явно, иначе сборка падает на несовместимом байт-коде.
JAVA_17_HOME=${JAVA_17_HOME:-/usr/lib/jvm/temurin-17-jdk-arm64}
if [ -x "$JAVA_17_HOME/bin/javac" ]; then
  export JAVA_HOME="$JAVA_17_HOME"
fi
./gradlew -Dorg.gradle.jvmargs='-Xms1g -Xmx4g' -Pvaadin.productionMode=true bootJar -x test

# Шаг 7: Запуск приложения
$COMPOSE up -d --build

echo "Deployment completed successfully on branch $BRANCH_NAME"
