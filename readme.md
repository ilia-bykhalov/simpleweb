1. Postgres
<br>`docker pull postgres`
<br>`mkdir -p $HOME/docker/volumes/postgres`
<br>`docker run --rm   --name pg-docker -e POSTGRES_PASSWORD=docker -d -p 5400:5432 -v $HOME/docker/volumes/postgres:/var/lib/postgresql/data  postgres`

1. Инициализация базы
<br> выполнить `./prepare_sql_server.sh`

1. Сборка
<br> Без тестов
<br> `mvn clean install -DskipTests`
<br> С тестами, но они хотят postgres на 5400 порту, с проинициализированной базой
<br> `mvn clean install`

1. Запуск
<br> При желании можно указать свой файл конфига
<br> `java -jar target/simpleweb-1.0-SNAPSHOT.jar custom_employee_server.conf`
<br> Либо не указывать вовсе, тогда он подтянет `default_employee_server.conf`
<br> `java -jar target/simpleweb-1.0-SNAPSHOT.jar`

1. При желании можно переконфигрурировать сервер
<br> `workersCount` = количество тредов обрабатывающих входящие соединения
<br> `connectionPoolSize` = размер пула к базе
