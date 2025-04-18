#!/bin/bash

# Проверка наличия аргумента
if [ -z "$1" ]; then
  echo "❗ Укажи путь к каталогу с Java-файлами (от корня репозитория)."
  echo "Пример: ./merge_java.sh src/main/java/com/example"
  exit 1
fi

# Корень репозитория
REPO_ROOT=$(git rev-parse --show-toplevel 2>/dev/null)
if [ -z "$REPO_ROOT" ]; then
  echo "❗ Не удалось определить корень репозитория. Убедись, что ты внутри git-репозитория."
  exit 1
fi

# Полный путь к директории с java-файлами
JAVA_DIR="$REPO_ROOT/$1"

if [ ! -d "$JAVA_DIR" ]; then
  echo "❗ Указанный каталог не существует: $JAVA_DIR"
  exit 1
fi

# Путь к выходному файлу
OUTPUT_FILE="$REPO_ROOT/all_java_code.txt"

# Объединение и фильтрация
find "$JAVA_DIR" -name "*.java" -type f \
    -exec cat {} + | \
    grep -v '^\s*$' | \
    grep -v '^\s*import\s' | \
    grep -v '^\s*package\s' > "$OUTPUT_FILE"

echo "✅ Готово! Содержимое всех Java-файлов сохранено в: $OUTPUT_FILE"
