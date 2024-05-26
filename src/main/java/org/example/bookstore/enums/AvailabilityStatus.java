package org.example.bookstore.enums;

import lombok.Getter;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-25(Fri)-2024
 */

@Getter
public enum AvailabilityStatus {
    IN_STOCK("IN_STOCK"),
    OUT_OF_STOCK("OUT_OF_STOCK"),
    PRE_ORDER("PRE_ORDER"),
    DISCONTINUED("DISCONTINUED");

    private final String displayName;

    AvailabilityStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

}
