package ru.otus.l12;

import java.util.Collection;
import java.util.List;

public interface CashMachineInterface {
    void takeMoney(List<BankNote> banknotes);

    Collection<BankNote> giveOut(int sum);

    int getTotal();
}
