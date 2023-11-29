package by.clevertec.proxy.factory;

import lombok.Getter;

@Getter
public enum DescriptionAction {

    VALUE_GET_FROM_CACHE ("Объект получен из кэш"),
    VALUE_ADD_IN_CACHE ("Объект добавлен в кэш"),
    VALUE_ADD_IN_BD ("Объект добавлен в базу данных"),
    VALUE_GET_FROM_BD ("Объект получен из базы данных"),
    VALUE_UPDATE_IN_CACHE ("Объект обновлён в кэш"),
    VALUE_DELETE_IN_CACHE ("Объект удалён из кэш");

    final private String value;

    DescriptionAction(String value) {
        this.value = value;
    }

}
