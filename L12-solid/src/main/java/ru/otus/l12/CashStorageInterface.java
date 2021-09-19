package ru.otus.l12;

import java.util.Collection;

public interface CashStorageInterface {
    int getTotal();

    boolean putBankNote(BankNote bankNote);

    Collection<BankNote> giveSum(int sum);
}
