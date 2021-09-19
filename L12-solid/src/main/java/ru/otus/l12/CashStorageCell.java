package ru.otus.l12;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

public class CashStorageCell {
    private Deque<BankNote> total = new ArrayDeque<>();
    private final RatingEnum rating;

    public CashStorageCell(RatingEnum rating) {
        this.rating = rating;
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
                    "Cache cell with rating " + rating.getRating().toString() + " has no asked amount!"
            );
        }
    }

    public boolean canGiveAmount(int amount)
    {
        return amount <= total.size();
    }

    public int getTotalSum()
    {
        return total.size() * rating.getRating();
    }
}
