package ru.otus.l12;

import java.util.*;

public class CashStorage {
    private int[] availableCellRatings = {5000, 1000, 500, 200, 100, 50};

    private final TreeMap<Integer, CashStorageCell> storageMap = new TreeMap<>(
            (o1, o2) -> {
                if (o1 > o2) {
                    return -1;
                } else if (o2.equals(o1)) {
                    return 0;
                }
                return 1;
            }
    );

    public CashStorage() {
        for (int availableCellRating : availableCellRatings) {
            storageMap.put(availableCellRating, new CashStorageCell(availableCellRating));
        }
    }

    public int getTotal() {
        var total = 0;
        Collection<CashStorageCell> values = storageMap.values();
        for (CashStorageCell value : values) {
            total += value.getTotalSum();
        }
        return total;
    }

    public boolean putBankNote(BankNote bankNote) {
        if (storageMap.containsKey(bankNote.getRating())) {
            return storageMap.get(bankNote.getRating()).putOne(bankNote);
        }
        throw new RuntimeException("this rating of banknote is not supported to put in");
    }

    public Collection<BankNote> giveSum(int sum) {
        var plan = makeGivePlan(sum);
        if (plan.isEmpty()) {
            throw new RuntimeException("can't give this sum");
        }
        return applyPlan(plan);
    }

    private Collection<BankNote> applyPlan(Map<Integer, Integer> plan) {
        var result = new ArrayList<BankNote>();
        for (Map.Entry<Integer, Integer> planItem : plan.entrySet()) {
            result.addAll(
                    storageMap
                            .get(planItem.getKey())
                            .giveAmount(planItem.getValue())
            );
        }
        return result;
    }

    private Map<Integer, Integer> makeGivePlan(int remainder) {
        var result = new HashMap<Integer, Integer>();
        for (Map.Entry<Integer, CashStorageCell> entry : storageMap.entrySet()) {
            var currentNeededAmount = remainder / entry.getKey();
            if (canGetFromCell(currentNeededAmount, entry.getValue())) {
                result.put(entry.getKey(), currentNeededAmount);
                remainder = remainder % entry.getKey();
            }
        }
        // если есть остаток значит не может выдать эту сумму
        if (remainder > 0) {
            result.clear();
        }
        return result;
    }

    private boolean canGetFromCell(int amount, CashStorageCell cell) {
        if (amount > 0 && cell.canGiveAmount(amount)) {
            return cell.canGiveAmount(amount);
        } else {
            return false;
        }
    }
}
