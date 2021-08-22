package ru.otus.l12;

import java.util.Collection;
import java.util.List;

public class CashMachine {

    private final CashStorage cashStorage = new CashStorage();

    public void takeMoney(List<BankNote> banknotes)
    {
        banknotes.forEach(cashStorage::putBankNote);
    }

    public Collection<BankNote> giveOut(int sum)
    {
        try {
            return cashStorage.giveSum(sum);
        } catch (RuntimeException e) {
            System.out.println("do not have enough money");
        }
        return null;
    }

    public int getTotal()
    {
        return cashStorage.getTotal();
    }
}
