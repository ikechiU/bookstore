package org.example.bookstore.enums;

import lombok.Getter;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-25(Fri)-2024
 */

@Getter
public enum BookGenre {
    MYSTERY("Mystery"),
    FANTASY("Fantasy"),
    ROMANCE("Romance"),
    HORROR("Horror"),
    ADVENTURE("Adventure");

    private final String displayName;

    BookGenre(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

}
