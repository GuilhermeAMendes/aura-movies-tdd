package br.ifsp.demo.model.movie;

import lombok.Getter;

@Getter
public enum Genre {
    ACTION("Action"),
    ADVENTURE("Adventure"),
    COMEDY("Comedy"),
    DRAMA("Drama"),
    FANTASY("Fantasy"),
    HORROR("Horror"),
    ROMANCE("Romance"),
    SCI_FI("Sci-Fi"),
    THRILLER("Thriller"),
    DOCUMENTARY("Documentary");
    private final String label;

    Genre(final String label) {
        this.label = label;
    }

    public static Genre fromLabel(final String label) {
        for (final Genre genre : Genre.values()) {
            if (genre.getLabel().equals(label)) {
                return genre;
            }
        }
        return null;
    }
}
