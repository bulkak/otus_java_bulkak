package ru.otus.l12;

import java.util.Collection;
import java.util.List;

public class CashMachine implements CashMachineInterface {

    private final CashStorageInterface cashStorage;

    public CashMachine(CashStorageInterface cashStorage)
    {
        this.cashStorage = cashStorage;
    }

    @Override
    public void takeMoney(List<BankNote> banknotes)
    {
        banknotes.forEach(cashStorage::putBankNote);
    }

    @Override
    public Collection<BankNote> giveOut(int sum)
    {
        try {
            return cashStorage.giveSum(sum);
        } catch (RuntimeException e) {
            System.out.println("do not have enough money");
        }
        return null;
    }

    @Override
    public int getTotal()
    {
        return cashStorage.getTotal();
    }
}
