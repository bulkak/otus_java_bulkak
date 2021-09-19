package ru.otus.l12;

import java.util.*;

public class CashStorage implements CashStorageInterface {
    private final TreeMap<RatingEnum, CashStorageCell> storageMap = new TreeMap<>(Enum::compareTo);
    private final Map<RatingEnum, Integer>givePlan = new HashMap<>();

    public CashStorage() {
        for (RatingEnum availableCellRating : RatingEnum.values()) {
            storageMap.put(availableCellRating, new CashStorageCell(availableCellRating));
        }
    }

    @Override
    public int getTotal() {
        var total = 0;
        Collection<CashStorageCell> values = storageMap.values();
        for (CashStorageCell value : values) {
            total += value.getTotalSum();
        }
        return total;
    }

    @Override
    public boolean putBankNote(BankNote bankNote) {
        if (storageMap.containsKey(bankNote.getRating())) {
            return storageMap.get(bankNote.getRating()).putOne(bankNote);
        }
        throw new RuntimeException("this rating of banknote is not supported to put in");
    }

    @Override
    public Collection<BankNote> giveSum(int sum) {
        if (getTotal() < sum) {
            throw new RuntimeException("can't give this sum");
        }
        makeGivePlan(sum);
        if (givePlan.isEmpty()) {
            throw new RuntimeException("can't give this sum");
        }
        var result =  applyPlan();
        givePlan.clear();
        return result;
    }

    private Collection<BankNote> applyPlan() {
        var result = new ArrayList<BankNote>();
        for (Map.Entry<RatingEnum, Integer> planItem : givePlan.entrySet()) {
            result.addAll(
                    storageMap
                            .get(planItem.getKey())
                            .giveAmount(planItem.getValue())
            );
        }
        return result;
    }

    private void makeGivePlan(int remainder) {
        for (Map.Entry<RatingEnum, CashStorageCell> entry : storageMap.entrySet()) {
            remainder = getMaxCountOfAmountFromCell(entry, remainder);
        }
        // если есть остаток значит не может выдать эту сумму
        if (remainder > 0) {
            givePlan.clear();
        }
    }

    private int getMaxCountOfAmountFromCell(Map.Entry<RatingEnum, CashStorageCell>entry, int remainder)
    {
        var currentNeededAmount = remainder / entry.getKey().getRating();
        if (canGetFromCell(currentNeededAmount, entry.getValue())) {
            givePlan.put(entry.getKey(), currentNeededAmount);
            return remainder % entry.getKey().getRating();
        } else {
            var newRemainder = remainder - entry.getKey().getRating();
            if (newRemainder > 0 && currentNeededAmount > 1) {
                if (getMaxCountOfAmountFromCell(entry, newRemainder) == 0) {
                    return remainder - newRemainder;
                }
            }
        }
        return remainder;
    }

    private boolean canGetFromCell(int amount, CashStorageCell cell) {
        var canGive = cell.canGiveAmount(amount);
        return amount > 0 && canGive;
    }
}
