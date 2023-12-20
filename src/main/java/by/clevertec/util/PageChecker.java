package by.clevertec.util;

import by.clevertec.exception.PageException;

public interface PageChecker {

    static Integer checkPage(Integer pageNumber, Integer pageSize) {

        if (pageNumber <= 0) {
            throw new PageException("Your page number is " + pageNumber
                    + "! Value must be greater than zero!");

        } else if (pageSize <= 0) {
            throw new PageException("Your page size is " + pageSize
                    + "! Value must be greater than zero!");
        }

        return pageNumber * pageSize - pageSize;
    }
}
