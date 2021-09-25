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

@DisplayName("Сервис CashMachine должен мне")
public class CacheMachineTest {
    private static final String EOL = System.lineSeparator();

    private CashMachine cashMachineService;

    private PrintStream backup;
    private ByteArrayOutputStream bos;

    @BeforeEach
    public void setUp() {
        cashMachineService = new CashMachine(new CashStorage());
        var bankNotes = new ArrayList<BankNote>();
        RatingEnum[] availableBankNotesRatings = {
                RatingEnum.N5000, RatingEnum.N1000,
                RatingEnum.N500, RatingEnum.N200,
                RatingEnum.N100, RatingEnum.N50
        };
        for (int i = 100; i > 0; i--) {
            int a = (int) (Math.random() * 6);
            bankNotes.add(new BankNote(availableBankNotesRatings[a]));
        }
        cashMachineService.takeMoney(bankNotes);

        backup = System.out;
        bos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bos));
    }

    @AfterEach
    void tearDown() {
        System.setOut(backup);
    }

    @DisplayName("сумма вычисляется правильно после выдачи денег")
    @Test
    void canSumAllKindOfCashRating() {
        var total = cashMachineService.getTotal();
        assertThat(total).isGreaterThan(0);

        cashMachineService.giveOut(650);
        var total2 = cashMachineService.getTotal();
        assertThat(total2).isEqualTo(total - 650);

        cashMachineService.giveOut(6250);
        var total3 = cashMachineService.getTotal();
        assertThat(total3).isEqualTo(total - 650 - 6250);
    }

    @DisplayName("сумма выдается минимальным числом банкнот")
    @Test
    void giveSumWithMinimalCountOfBanknotes() {
        var bankNotes = cashMachineService.giveOut(650);
        var result = new HashMap<RatingEnum, Integer>(); // номинал, количество
        for (BankNote bankNote: bankNotes) {
            result.put(
                    bankNote.getRating(),
                    result.containsKey(bankNote.getRating()) ? result.get(bankNote.getRating()) + 1 : 1
            );
        }
        assertThat(result.get(RatingEnum.N50)).isEqualTo(1);
        assertThat(result.get(RatingEnum.N500)).isEqualTo(1);
        assertThat(result.get(RatingEnum.N100)).isEqualTo(1);

        var bankNotes1 = cashMachineService.giveOut(12300);
        var result1 = new HashMap<RatingEnum, Integer>(); // номинал, количество
        for (BankNote bankNote: bankNotes1) {
            result1.put(
                    bankNote.getRating(),
                    result1.containsKey(bankNote.getRating()) ? result1.get(bankNote.getRating()) + 1 : 1
            );
        }
        assertThat(result1.get(RatingEnum.N5000)).isEqualTo(2);
        assertThat(result1.get(RatingEnum.N1000)).isEqualTo(2);
        assertThat(result1.get(RatingEnum.N200)).isEqualTo(1);
        assertThat(result1.get(RatingEnum.N100)).isEqualTo(1);
    }

    @DisplayName("выдает ошибку если сумму выдать нельзя")
    @Test
    void checkErrorMessageIfHasNoEnough() {
        cashMachineService.giveOut(650000000);
        assertThat(bos.toString()).isEqualTo("do not have enough money" + EOL);
    }

    @DisplayName("выдает ошибку если сумма выдать нельзя, т.к. нет подходящих номиналов")
    @Test
    void checkErrorMessageIfHasNotSuitableBanknotes() {
        cashMachineService.giveOut(330);
        assertThat(bos.toString()).isEqualTo("do not have enough money" + EOL);
    }

}
