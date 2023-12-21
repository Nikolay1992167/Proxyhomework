# Задание

1. Взять за основу проект из лекции паттернов.
2. Придерживаться GitFlow: master->develop->feature/fix.
3. Написать CRUD для всех таблиц.
4. Для метода .findAll() сделать пагинацию (по умолчанию 20 элементов
на странице, если pagesize не задан).
5. Сделать GET метод, для генерации описания объекта в формате pdf (если 
товара не существует, тогда генерируем ошибку).
6. Прикрутить возможность инициализации бд и наполнения её данными с помощью 
параметра в application.yaml файле, т.е. чтобы при подъёме приложения, приложение 
создавало схему, таблицы и наполняло таблицы данными.
7. Фильтры.
8. UI не нужен.

# Servlet

Согласно условиям задачи был применён проект, в котором выполнена реализация кэша, используя алгоритмы на выбор LRU или LFU,
а также применён паттерн проектирования Фабрика.
В пакете controller для реализации пунктов 3 и 5 созданы классы CarServlet и GenerationFilePdfServlet. Так как эти два
сервлета работают с CarServiceImpl, то общая функциональность была вынесена в AbstractCarServlet. 
4 пункт задания выполнен с использованием интерфейса PageChecker и sql запроса в CarDAOImpl 
"SELECT * FROM public_car.cars ORDER BY id LIMIT ? OFFSET ?".
При реализации генерации файла pdf, содержащего информацию об объекте, была предусмотрена функциональность работы из 
развёрнутого архива и все генерируемые файлы размещаются в директории build/libs/exploded/ProxyHW-1.0-
SNAPSHOT.war/WEB-INF/classes/pdf. 
Также при использовании сервлетов сохранена функциональность работы прокси и генерации отчета в формате pdf. Файлы
создаются в директории указанной выше.
Для выполнения условий 6 пункта задания создан класс ContextListener, который помечен аннотацией @WebListener. В данном
классе организована функциональность создания схемы бд, таблиц и заполнения данными. Также организовано удаление схемы
и таблиц при остановке приложения. 
В приложении реализовано два фильтра: 1. CodingFilter, который проверяет валидность данных на соответствие системе 
кодирования UTF-8; 2. CheckNullFilter, который проверяет объекты CarDto на null.
Хотел реализовать класс ApplicationConfig, в котором была бы выполнена инициализация маппера, сервисов, подключения к бд, 
но упёрся в ошибку при работе с прокси 'Method threw 'java.lang.reflect.UndeclaredThrowableException' exception.', пока 
устранить не получается.

### Технологии применённые в проекте

* Java 17
* Gradle 8.1.1
* Reflections 0.10.2
* Postgresql 42.6.0
* Logback 1.4.11
* Snakeyaml 2.1
* Mapstruct 1.5.5.Final
* Jackson 2.16.0
* Mockito-junit-jupiter 5.6.0
* Junit-bom 5.9.2
* Junit-jupiter
* Assertj-core:3.24.2
* Javax.validation: 2.0.1.Final
* ITextPdf: 5.5.13.3
* Gson: 2.10.1
* Servlet-api: 4.0.1

### Unit тесты

Модульные тесты выполнены с использованием библиотеки AssertJ. 
Написано 61 тест для cache, dao, mapper, pdf, proxy, service. 
Вы можете запустить тесты для этого проекта, выполнив в корне проекта:
```
./gradlew test
```

### Функциональность

В приложении используется БД Postgres. Для проверки приложения использую Postman. При выполнении запросов 
значения id необходимо вносить валидные из БД.

### CarServlet
* **GET | Finds car by id**
* http://localhost:8080/cars?id=bd114dde-0f0c-4139-85a0-1011389a6508
* Response example:
````json
[
  {
    "id":"bd114dde-0f0c-4139-85a0-1011389a6508",
    "name":"Волат","description":"Большая машина!",
    "price":80000.0,"created":"2023-11-13T12:00"
  }
]
````
* **GET | Finds cars by pageNumber & pageSize**
* http://localhost:8080/cars?pageNumber=1&pageSize=3
* Response example:
````json
[
  {
    "id":"0157b055-9112-4b81-a599-0b9a58a94991",
    "name":"Лада","description":"Бюджетная машина!",
    "price":10000.0,
    "created":"2023-11-14T11:00"
  },
  {
    "id":"085b83bc-a3fe-420d-b949-288dcf99280b",
    "name":"Заз",
    "description":"Прикольная машина!",
    "price":12000.0,"created":"1800-10-13T12:00"
  },
  {
    "id":"165acca9-0307-482d-b553-2f9ad3a680a6",
    "name":"ЗИС",
    "description":"Премиум машина!",
    "price":150000.0,
    "created":"1965-06-08T12:00"
  }
]
````
* **POST | Creates new car**
* http://localhost:8080/cars
* Request example:
````json
[
  {
    "name": "Джили",
    "description": "Неплохой авто!",
    "price": 43000.00
  }
]
````
* **PUT | Updates car with id**
* http://localhost:8080/cars?id=4aa0ee07-0708-4749-8cc8-c7fdded52f83
* Request example:
````json
[
  {
    "name":"Форд",
    "description":"Изумительный автомобиль!",
    "price": 25000.0
  }
]
````
* **DELETE | Deletes car with id**
* http://localhost:8080/cars?id=6b7cfef8-4bb5-4b0b-9eac-36e2e1484308

### GenerationFilePdfServlet
* **GET | Generates file pdf about car by id**
* http://localhost:8080/cars?id=bd114dde-0f0c-4139-85a0-1011389a6508

Благодарю за уделённое время! Буду рад детальному ревью и хотел бы услышать мнение о README.md.