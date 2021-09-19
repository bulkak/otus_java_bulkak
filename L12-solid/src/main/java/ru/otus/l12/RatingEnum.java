package ru.otus.l12;

public enum RatingEnum {
    N5000 (5000),
    N1000 (1000),
    N500 (500),
    N200 (200),
    N100 (100),
    N50 (50);

    private Integer rating;

    RatingEnum(Integer rating) {
        this.rating = rating;
    }

    public Integer getRating() {
        return rating;
    }
}
