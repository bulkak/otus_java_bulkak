package ru.otus.l12;

public class BankNote {
    private final RatingEnum rating;

    public BankNote(RatingEnum rating)
    {
        this.rating = rating;
    }

    public RatingEnum getRating() {
        return rating;
    }
}
