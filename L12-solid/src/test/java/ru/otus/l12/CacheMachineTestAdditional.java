package ru.otus.l12;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис CashMachine сложные сценарии")
public class CacheMachineTestAdditional {
    private static final String EOL = System.lineSeparator();

    private CashMachine cashMachineService;

    private PrintStream backup;
    private ByteArrayOutputStream bos;

    @BeforeEach
    public void setUp() {
        cashMachineService = new CashMachine(new CashStorage());
        backup = System.out;
        bos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bos));
    }

    @AfterEach
    void tearDown() {
        System.setOut(backup);
    }

    @DisplayName("сумма выдается минимальным числом банкнот, даже если банкнот максимального номинала не хватает")
    @Test
    void giveSumIfSumOfMaxAmountOfCellIsNotEnough() {
        var bankNotesToTake = new ArrayList<BankNote>();
        bankNotesToTake.add(new BankNote(RatingEnum.N200));
        bankNotesToTake.add(new BankNote(RatingEnum.N200));
        bankNotesToTake.add(new BankNote(RatingEnum.N100));
        bankNotesToTake.add(new BankNote(RatingEnum.N100));

        cashMachineService.takeMoney(bankNotesToTake);

        var bankNotes = cashMachineService.giveOut(600);
        var result = new HashMap<RatingEnum, Integer>(); // номинал, количество
        for (BankNote bankNote: bankNotes) {
            result.put(
                    bankNote.getRating(),
                    result.containsKey(bankNote.getRating()) ? result.get(bankNote.getRating()) + 1 : 1
            );
        }
        assertThat(result.get(RatingEnum.N200)).isEqualTo(2);
        assertThat(result.get(RatingEnum.N100)).isEqualTo(2);
    }

}
