# Cache application

В приложении выполнена реализация кэша, используя алгоритмы на выбор LRU или LFU. Алгоритм для работы приложения
и размер коллекции указываются в файле resources/application.yaml. 
В приложении реализованы слои service, dao согласно условиям задания. В сервисе реализованы CRUD операции для
работы с entity Car. В качестве id используется тип UUID.  Данные для подключения к БД также считываются из файла 
application.yaml. Создание таблиц в БД и вставка данных осуществляется чтением файла create_sql_V1.sql. Взаимодействие
с БД осуществлено посредствам JDBC Template. Взаимодействие dao слоя с cache реализовано с помощью кастомной аннотации,
которую отслеживает InvocationHandler и выполняет функциональность работы с cache согласно типу метода. В приложении 
выполнена валидация объекта CarDto c применением регулярных выражений(Validation.class). В приложении реализована 
функциональность добавления объектов в файл report+UUID.xml для каждого объекта, куда записываются объекты добавленные 
в cache. Для этого была применена библиотека jackson.

### Технологии применённые в проекте

* Java 17
* Gradle 8.1.1
* Reflections 0.10.2
* Postgresql 42.6.0
* Logback 1.4.11
* Snakeyaml 2.1
* Mapstruct 1.5.5.Final
* Commons-configuration2 2.9.0
* Jackson 2.16.0
* Mockito-junit-jupiter 5.6.0
* Junit-bom 5.9.2
* Junit-jupiter
* Assertj-core:3.24.2

### Unit тесты

Модульные тесты выполнены с использованием библиотеки AssertJ. 
Написано 53 тест для cache, dao, mapper, proxy, validation. 
Вы можете запустить тесты для этого проекта, выполнив в корне проекта:
```
./gradlew test
```
Один тест в валидации не смог реализовать.

### Функциональность

В приложении используется БД Postgres. Первоначально необходимо создать самостоятельно базу данных cars и настроить
application.yaml. 
Старт приложения осуществляется из класса Main: 
1. Выполнить метод extracted(). В результате в БД будет создана таблица cars. В таблицу будут внесены 2 записи.
2. Закомментировать метод extracted().
3. Раскомментировать метод startApplication().
4. Из таблицы cars необходимо скопировать значения id и использовать их как аргументы метода startApplication().
Например: В таблице 1. id = 0a9498a7-14b1-41b7-8cd0-5ff1eaaa1c22, 2. id = e43791ac-e10b-4daa-9d2f-ad5dfb4410cb. 
startApplication("0a9498a7-14b1-41b7-8cd0-5ff1eaaa1c22", "0a9498a7-14b1-41b7-8cd0-5ff1eaaa1c22",
"e43791ac-e10b-4daa-9d2f-ad5dfb4410cb")
5. Выполнить метод startApplication.
6. В результате в консоли мы получим результат работы:
```
Проверка выполнения программы
19:47:24.631 [main] INFO by.clevertec.proxy.pattern.ActionFactory -- Добавлено в кэш: Car(id=null, name=Ладушка, description=Почти хороший автомобиль!, price=105000, created=null)
[InfoCarDto[id=0a9498a7-14b1-41b7-8cd0-5ff1eaaa1c22, name=Audi, description=Good car!, price=50000.0, created=2023-11-14T14:00], InfoCarDto[id=e43791ac-e10b-4daa-9d2f-ad5dfb4410cb, name=BMW, description=Fast car!, price=80000.0, created=2023-11-13T12:00], InfoCarDto[id=bce2fd15-95c8-44ae-a4cd-ca945cca45e2, name=Ладушка, description=Почти хороший автомобиль!, price=105000, created=2023-11-19T19:47:24.444651]]
19:47:24.659 [main] INFO by.clevertec.proxy.pattern.ActionFactory -- Добавлено в кэш: Car(id=0a9498a7-14b1-41b7-8cd0-5ff1eaaa1c22, name=Audi, description=Good car!, price=50000.0, created=2023-11-14T14:00)
19:47:24.660 [main] INFO by.clevertec.proxy.pattern.ActionFactory -- Взято из кэша: Car(id=0a9498a7-14b1-41b7-8cd0-5ff1eaaa1c22, name=Audi, description=Good car!, price=50000.0, created=2023-11-14T14:00)
19:47:24.674 [main] INFO by.clevertec.proxy.pattern.ActionFactory -- Обновлено в кэше: Car(id=0a9498a7-14b1-41b7-8cd0-5ff1eaaa1c22, name=Москвич, description=Был хорошим авто наверное!, price=105000, created=2023-11-14T14:00)
19:47:24.674 [main] INFO by.clevertec.proxy.pattern.ActionFactory -- Удалено из кэша: e43791ac-e10b-4daa-9d2f-ad5dfb4410cb
```
Благодарю за уделённое время! Буду рад детальному ревью и хотел бы услышать мнение о README.md.