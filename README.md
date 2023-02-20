## Сервис заказа путешествия

## Дипломный проект профессии «ПО Тестировщик»

**Описание приложения**

*Бизнес часть*

Приложение предлагает купить тур по определённой цене с помощью двух способов:

1. Обычная оплата по дебетовой карте.
2. Выдача кредита по данным банковской карты.

С помощью сервисов:

+ сервису платежей (далее - Payment Gate)
+ кредитному сервису (далее - Credit Gate)

Данные карт сохранять не допускается.

**Техническая часть**

Само приложение расположено в файле *aqa-shop.jar* и запускается на порту 8080.

**Добавляем обработку параметров логина, пароля командами:**
+ systemProperty 'db.user', System.getProperty('db.user', 'app')
+ systemProperty 'db.password', System.getProperty('db.password', 'pass')

+ В файле *application.properties* приведён ряд типовых настроек:

+ учётные данные и url для подключения к СУБД
+ url-адреса банковских сервисов

**СУБД**

Поддержка двух СУБД:

+ MySQL
+ PostgreSQL


## Начало работы

**Предусловия**

При выполнении дипломной работы необходимо установить: IntelliJ IDEA, Google Chrome.

**Установка и запуск**

*Примечание:* по-умолчанию SUT работает с PostgreSQL.

1.Выполнить команду в консоли:

*docker-compose up*

2.После развертывания контейнера для запуска SUT в зависимости от выбранной для работы СУБД выполнить команду в консоли:

PostgreSQL
+ java -Dspring.datasource.url=jdbc:postgresql://localhost:5432/app -jar aqa-shop.jar

MySQL
+ java -Dspring.datasource.url=jdbc:mysql://localhost:3306/app -jar aqa-shop.jar

3. Запустить тесты командой в консоли:

PostgreSQL
+ ./gradlew clean test -Ddb.url=jdbc:postgresql://localhost:5432/app allureServe

MySQL
+ ./gradlew clean test -Ddb.url=jdbc:mysql://localhost:3306/app allureServe

**Дополнительные сведения:**

Для просмотра отчета Allure Report после выполнения тестов ввести в консоли:

*./gradlew allureServe*

Установка headless-режима Selenide осуществляется через изменение свойств gradle.properties:

*systemProp.selenide.headless=true*

**После окончания работы:**

+ Завершить работу SUT сочетанием клавиш CTRL + C.
+ Завершить работу контейнеров командой в консоли:
docker-compose down


[Issues](https://github.com/DimkaSmile/Diploma/issues)
