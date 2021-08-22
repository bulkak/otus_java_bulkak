package ru.otus.l12;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class CashStorageCell {
    private ArrayDeque<BankNote> total = new ArrayDeque<>();
    private final int rating;

    public CashStorageCell(int rating) {
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }

    public boolean putOne(BankNote bankNote)
    {
        return total.add(bankNote);
    }

    public ArrayList<BankNote> giveAmount(int amount)
    {
        var result = new ArrayList<BankNote>();
        if (canGiveAmount(amount)) {
            for (; amount > 0; amount--) {
                result.add(total.pollLast());
            }
            return result;
        } else {
            throw new RuntimeException(
                    "Cache cell with rating " + Integer.valueOf(rating).toString() + " has no asked amount!"
            );
        }
    }

    public boolean canGiveAmount(int amount)
    {
        return amount <= total.size();
    }

    public int getTotalSum()
    {
        return total.size() * rating;
    }
}
