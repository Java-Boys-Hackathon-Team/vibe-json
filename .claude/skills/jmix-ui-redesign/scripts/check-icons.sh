#!/usr/bin/env bash
# Проверяет, что все иконки, упомянутые в исходниках, существуют в VaadinIcon.
#
# Jmix выдаёт ошибку про несуществующую иконку только при открытии экрана, поэтому
# опечатку легко пропустить. Скрипт ищет icon="ИМЯ" в XML и VaadinIcon.ИМЯ в Java,
# а список допустимых имён берёт из jar-файла vaadin-icons-flow в кэше Gradle
# (проект должен быть хотя бы раз собран).
#
# Запуск из корня проекта: bash <путь-к-скиллу>/scripts/check-icons.sh [src/main]
set -euo pipefail

SRC="${1:-src/main}"
JAR=$(find "${GRADLE_USER_HOME:-$HOME/.gradle}/caches" -name 'vaadin-icons-flow-*.jar' ! -name '*sources*' 2>/dev/null | sort | tail -1)
if [ -z "$JAR" ]; then
  echo "Не найден vaadin-icons-flow в кэше Gradle - соберите проект" >&2
  exit 2
fi

TMP=$(mktemp -d)
trap 'rm -rf "$TMP"' EXIT
unzip -q -o "$JAR" 'com/vaadin/flow/component/icon/VaadinIcon.class' -d "$TMP"
javap -p "$TMP/com/vaadin/flow/component/icon/VaadinIcon.class" 2>/dev/null \
  | grep -oE 'VaadinIcon [A-Z_0-9]+;' | awk '{print $2}' | tr -d ';' | sort -u > "$TMP/known.txt"

# icon="vaadin:magic" в menu.xml приводится к виду MAGIC.
USED=$(grep -rhoE 'icon="(vaadin:)?[A-Za-z_-]+"|VaadinIcon\.[A-Z_0-9]+' "$SRC" \
  | sed -E 's/icon="(vaadin:)?//; s/"$//; s/VaadinIcon\.//' \
  | tr 'a-z-' 'A-Z_' | sort -u)

MISSING=0
for icon in $USED; do
  if ! grep -qx "$icon" "$TMP/known.txt"; then
    echo "Нет такой иконки: $icon"
    MISSING=1
  fi
done

[ "$MISSING" -eq 0 ] && echo "Все иконки найдены ($(echo "$USED" | wc -w) шт.)"
exit "$MISSING"
