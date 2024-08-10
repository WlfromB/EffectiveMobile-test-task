## EffectiveMobile-test-task
Тестовое задание для попадания на техническое собеседование.

## Описание

Сервис предоставляет возможность аутентификации и авторизации пользователей по email и паролю с использованием JWT токенов. Пользователи могут:

- Управлять своими задачами: создавать, редактировать, просматривать, удалять, менять статус и назначать исполнителей.
- Просматривать задачи других пользователей, а исполнители могут менять статус своих задач.
- Оставлять комментарии к задачам.
- Получать задачи конкретного автора или исполнителя, а также все комментарии к ним.

API поддерживает пагинацию и фильтрацию. Сервис обрабатывает ошибки, возвращает сообщения и валидирует входящие данные. Документация API доступна через Swagger.

**Технологии:**
- PostgreSQL
- Java 17
- Spring Boot
- Spring Security
- Mockito
- JUnit
- Spring Data
- Web
- AOP
- Hibernate ORM (миграции будут добавлены при дальнейшем развитии проекта)

Сейчас нет миграций, поскольку в техническом задании это не указано. Очевидно, что при дальнейшей разработке это будет проблемой и в случае признания жизнеспособности проекта будет произведен перенос на миграции (знаком с liquibase - opensource средство).

## Требования
- Docker >= 20.10
- Docker Compose >= 1.27.0
- Maven >= 3.6.3
- Java >= 17

## Сборка и запуск

1. **Клонирование репозитория**
    ```bash
    git clone https://github.com/WlfromB/EffectiveMobile-test-task
    cd EffectiveMobile-test-task
    ```

2. **Сборка проекта и создание исполняемого JAR файла**
    ```bash
    ./mvnw clean package -DskipTests
    ```
    Флаг `-DskipTests` позволяет избежать выполнения тестов при сборке.

3. **Настройка файла подключения**
    Создайте файл `.env` и укажите параметры подключения к базе данных. Пример файла `.env`:
    ```dotenv
    SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/effective-mobile-test-task
    SPRING_DATASOURCE_USERNAME=your_username
    SPRING_DATASOURCE_PASSWORD=your_password
    ```

4. **Сборка и запуск контейнеров**
    ```bash
    docker-compose up --build
    ```
    Приложение будет доступно по адресу [http://localhost:8080](http://localhost:8080).

5. **Остановка контейнеров**
    ```bash
    docker-compose down
    ```

6. **Запуск тестов**
    ```bash
    docker-compose run --rm app ./mvnw test
    ```


