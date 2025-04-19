#!/bin/bash

# Проверка, существует ли venv
if [ ! -d "venv" ]; then
  echo "Создание виртуального окружения..."
  python3.10 -m venv venv
fi

# Активация окружения
source venv/bin/activate

# Установка зависимостей
pip install -r requirements.txt

# Запуск сервера
uvicorn main:app --reload
