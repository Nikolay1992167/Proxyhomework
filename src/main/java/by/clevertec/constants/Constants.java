package by.clevertec.constants;

public class Constants {

    public static final String PATH_TO_FILE_XML = "src/main/java/by/clevertec/reportXML/report";
    public static final String NAME_REGEX ="[а-яА-Я ]{5,10}";
    public static final String DESCRIPTION_REGEX ="^[а-яА-Я\\s]{10,30}$";
    public static final String INVALID_NAME_ERROR = "Неверное имя car!";
    public static final String EMPTY_NAME_ERROR = "Не введено имя!";
    public static final String INVALID_DESCRIPTION_ERROR = "Неверное описание car!";
    public static final String NULL_PRICE_ERROR = "Цена не может быть null";
    public static final String INVALID_PRICE_ERROR = "Корректно укажите цену, она не может быть меньше либо равна 0";
    public static final String FONT = "src/main/resources/lato-light.ttf";
    public static final String VALUE_GET_FROM_CACHE = "Объект получен из кэш";
    public static final String VALUE_ADD_IN_CACHE = "Объект добавлен в кэш";
    public static final String VALUE_GET_FROM_BD = "Объект получен из базы данных";
    public static final String VALUE_UPDATE_IN_CACHE = "Объект обновлён в кэш";
    public static final String VALUE_DELETE_IN_CACHE = "Объект удалён из кэш";
}
