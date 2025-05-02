# Инструкция для запуска автотестирования
## ПО для запуска автотестов:

* IntelliJ IDEA
* Google Chrome
* Docker Desktop
* GitHub

## Шаги:
Открыть скопированный проект в IDEA, выполнить команды:

1.	Запустить контейнеры СУБД MySQl, PostgerSQL и Node.js командой в терминале:
      docker-compose up

2.	Запустить SUT в терминале при помощи команды:
* для MySQL:
java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar artifacts/aqa-shop.jar

* для PostgreSQL:
java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar artifacts/aqa-shop.jar

3. Проверить работоспособность системы. Приложение должно быть доступно по адресу:
http://localhost:8080/

4. Для запуска автотестов:

* Для БД MySQL в командной строке выполнить команду 

./gradlew test -Ddb.url=jdbc:mysql://localhost:3306/app

* Для БД PostgreSQL в командной строке выполнить команду  

./gradlew test -Ddb.url=jdbc:postgresql://localhost:5432/app


5. Для получения отчета по результатам прогона автотестов дважды нажать ctrl выполнить команду gradle allureServe, отчет откроется в браузере.