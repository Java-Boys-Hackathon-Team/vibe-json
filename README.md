# Vibe JSON - AI based инструмент построения интеграционных бизнес-процессов

## Сборка и запуск проекта из IntelliJ IDEA

- Установить IntelliJ IDEA, Docker
- Плагины для IDEA: Jmix

#### Сборка проекта:
- при первом запуске и пересборке после добавления новой функциональности: выполнить `./clear.sh`, чтобы jmix не кешировал лишнего
- сделать `gradle clean build`
- запустить контейнер с БД в docker-compose.yml: сервис `db`
- запустить проект через Run/Debug Configuration (сохранена в файле `.run/Vibe-json Jmix Application.run.xml`)
- UI доступен по ссылке [http://localhost:8080](http://localhost:8080). Креды `admin:admin`