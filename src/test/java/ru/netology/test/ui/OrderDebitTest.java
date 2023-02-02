package ru.netology.test.ui;

import com.codeborne.selenide.logevents.SelenideLogger;
import ru.netology.data.DBHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.page.CreditPage;
import ru.netology.page.DebitPage;
import ru.netology.page.HomePage;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.DBHelper.deleteAllDB;
import static ru.netology.data.DataHelper.*;
import static org.junit.jupiter.api.Assertions.*;

public class OrderDebitTest {

    private static final int amount = 45_000_00;

    @BeforeAll
    static void addListenerAndHeadless() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(false));
    }

    @AfterAll
    static void removeListener() {
        SelenideLogger.removeListener("AllureSelenide");
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:8080/");
        new HomePage().checkHomePageIsOpened();
    }

    @AfterEach
    void setDown() {
        DBHelper.deleteAllDB();
    }

    @Test
    @DisplayName("Должен успешно оплатить с одобренной дебетовой картой в форме оплаты")
    void shouldSuccessPayWithApprovedDebitCard() {
        successDebitPage().enterValidUserWithApprovedCard();
        assertEquals(amount, DBHelper.getAmountDebitCard());
        assertEquals("APPROVED", DBHelper.getStatusDebitCard());
        assertNotNull(DBHelper.getPaymentId());
        assertNotNull(DBHelper.getTransactionIdDebitCard());
        assertEquals(DBHelper.getPaymentId(), DBHelper.getTransactionIdDebitCard());
        assertNull(DBHelper.getCreditId());
    }

    @Test
    @DisplayName("Должен вернуть ошибку с отклоненной дебетовой картой в форме оплаты")
    void shouldReturnFailWithDeclinedDebitCard() {
        successDebitPage()
                .enterValidUserWithIncorrectCard(DataHelper.validUser(DataHelper.getDeclinedCard()));
        assertEquals("DECLINED", DBHelper.getStatusDebitCard());
        assertNull(DBHelper.getPaymentId());
        assertNull(DBHelper.getTransactionIdDebitCard());
    }

    @Test
    @DisplayName("Должен вернуть ошибку с неизвестной дебетовой картой в форме оплаты")
    void shouldReturnFailWithUnknownDebitCard() {
        successDebitPage()
                .enterValidUserWithIncorrectCard(DataHelper.validUser(DataHelper.getUnknownCard()));
        assertNull(DBHelper.getPaymentId());
        assertNull(DBHelper.getTransactionIdDebitCard());
    }

    @Test
    @DisplayName("Должен вернуть ошибку с пустой дебетовой картой в форме оплаты")
    void shouldReturnErrorWithEmptyDebitCard() {
        successDebitPage()
                .enterIncorrectCardInput(DataHelper.emptyCardUser(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку с пустым полем месяца в форме оплаты")
    void shouldReturnErrorWithEmptyMonthDebit() {
        successDebitPage()
                .enterIncorrectMonthInput(DataHelper.emptyMonthUser(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку с пустым полем года в форме оплаты")
    void shouldReturnErrorWithEmptyYearDebit() {
        successDebitPage()
                .enterIncorrectYearInput(DataHelper.emptyYearUser(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку с пустым полем имени в форме оплаты")
    void shouldReturnErrorWithEmptyNameDebit() {
        successDebitPage()
                .enterIncorrectNameInput(DataHelper.emptyNameUser(), "Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("Должен вернуть ошибку с пустым полем CVC в форме оплаты")
    void shouldReturnErrorWithEmptyCodeDebit() {
        successDebitPage()
                .enterIncorrectCodeInput(DataHelper.emptyCodeUser(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку со всеми пустыми полями в форме оплаты")
    void shouldReturnErrorsWithEmptyAllDebit() {
        successDebitPage()
                .enterInputs(DataHelper.emptyUser());
        errorsDisplayDebit(
                "Неверный формат",
                "Неверный формат",
                "Неверный формат",
                "Поле обязательно для заполнения",
                "Неверный формат"
        );
    }

    @Test
    @DisplayName("Должен вернуть ошибку с некорректным полем карты в форме оплаты")
    void shouldReturnErrorWithInvalidDebitCard() {
        successDebitPage()
                .enterIncorrectCardInput(DataHelper.invalidCardUser(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку с некорректным полем месяца в форме оплаты")
    void shouldReturnErrorWithInvalidMonthDebit() {
        successDebitPage()
                .enterIncorrectMonthInput(DataHelper.invalidMonthUser(), "Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("Должен вернуть ошибку с некорректным полем года в форме оплаты")
    void shouldReturnErrorWithInvalidYearDebit() {
        successDebitPage()
                .enterIncorrectYearInput(DataHelper.invalidYearUser(), "Истёк срок действия карты");
    }

    @Test
    @DisplayName("Должен вернуть ошибку с некорректным полем имени в форме оплаты")
    void shouldReturnErrorWithInvalidNameDebit() {
        successDebitPage()
                .enterIncorrectNameInput(DataHelper.invalidNameUser(), "Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("Должен вернуть ошибку с некорректным полем CVC в форме оплаты")
    void shouldReturnErrorWithInvalidCodeDebit() {
        successDebitPage()
                .enterIncorrectCodeInput(DataHelper.invalidCodeUser(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку со всеми некорректными полями в форме оплаты")
    void shouldReturnErrorsWithInvalidAllDebit() {
        successDebitPage()
                .enterInputs(DataHelper.invalidUser());
        errorsDisplayDebit(
                "Неверный формат",
                "Неверно указан срок действия карты",
                "Истёк срок действия карты",
                "Неверный формат",
                "Неверный формат"
        );
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением '0' поля Карты в форме оплаты")
    void shouldReturnErrorsWithCardZeroInputsDebit() {
        successDebitPage()
                .enterIncorrectCardInput(DataHelper.userWithCardZero(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением '0' поля Года в форме оплаты")
    void shouldReturnErrorsWithYearZeroInputsDebit() {
        successDebitPage()
                .enterIncorrectYearInput(DataHelper.userWithYearZero(), "Истёк срок действия карты");
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением '0' поля Месяца в форме оплаты")
    void shouldReturnErrorsWithMonthZeroInputsDebit() {
        successDebitPage()
                .enterIncorrectMonthInput(DataHelper.userWithMonthZero(), "Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением '0' поля Имени в форме оплаты")
    void shouldReturnErrorsWithNameZeroInputsDebit() {
        successDebitPage()
                .enterIncorrectNameInput(DataHelper.userWithNameZero(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением '0' поля CVC в форме оплаты")
    void shouldReturnErrorsWithCodeZeroInputsDebit() {
        successDebitPage()
                .enterIncorrectCodeInput(DataHelper.userWithCodeZero(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку со всеми полями со значением '0' в форме оплаты")
    void shouldReturnErrorsWithAllZeroInputsDebit() {
        successDebitPage()
                .enterInputs(DataHelper.userWithAllZero());
        errorsDisplayDebit(
                "Неверный формат",
                "Неверно указан срок действия карты",
                "Истёк срок действия карты",
                "Неверный формат",
                "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением поля Карты до лимита в форме оплаты")
    void shouldReturnErrorWithCardUnderLimitDebit() {
        successDebitPage()
                .enterIncorrectCardInput(DataHelper.userWithCardUnderLimit(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением поля Года до лимита в форме оплаты")
    void shouldReturnErrorWithYearUnderLimitDebit() {
        successDebitPage()
                .enterIncorrectYearInput(DataHelper.userWithYearUnderLimit(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением поля Месяца до лимита в форме оплаты")
    void shouldReturnErrorWithMonthUnderLimitDebit() {
        successDebitPage()
                .enterIncorrectMonthInput(DataHelper.userWithMonthUnderLimit(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением поля Имени до лимита в форме оплаты")
    void shouldReturnErrorWithNameUnderLimitDebit() {
        successDebitPage()
                .enterIncorrectNameInput(DataHelper.userWithNameUnderLimit(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением поля CVC до лимита в форме оплаты")
    void shouldReturnErrorWithCodeUnderLimitDebit() {
        successDebitPage()
                .enterIncorrectCodeInput(DataHelper.userWithCodeUnderLimit(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку со всеми полями до лимита в форме оплаты")
    void shouldReturnErrorsWithAllUnderLimitsDebit() {
        successDebitPage()
                .enterInputs(DataHelper.userWithAllUnderLimits());
        errorsDisplayDebit(
                "Неверный формат",
                "Неверный формат",
                "Неверный формат",
                "Неверный формат",
                "Неверный формат");
    }

    @Test
    @DisplayName("Должен успешно внести валидное значение поля Карты сверх лимита в форме оплаты")
    void shouldReturnErrorWithCardAfterLimitDebit() {
        successDebitPage()
                .enterInputs(DataHelper.userWithCardAfterLimit());
    }

    @Test
    @DisplayName("Должен успешно внести валидное значение поля Года сверх лимита в форме оплаты")
    void shouldReturnErrorWithYearAfterLimitDebit() {
        successDebitPage()
                .enterInputs(DataHelper.userWithYearAfterLimit());
    }

    @Test
    @DisplayName("Должен успешно внести валидное значение поля Месяца сверх лимита в форме оплаты")
    void shouldReturnErrorWithMonthAfterLimitDebit() {
        successDebitPage()
                .enterInputs(DataHelper.userWithMonthAfterLimit());
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением поля Имени сверх лимита в форме оплаты")
    void shouldReturnErrorWithNameAfterLimitDebit() {
        successDebitPage()
                .enterIncorrectNameInput(DataHelper.userWithNameAfterLimit(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен успешно внести валидное значение поля CVC сверх лимита в форме оплаты")
    void shouldReturnErrorWithCodeAfterLimitDebit() {
        successDebitPage()
                .enterInputs(DataHelper.userWithCodeAfterLimit());
    }

    @Test
    @DisplayName("Должен вернуть ошибку у поля Имени при вводе всех полей сверх лимита в форме оплаты")
    void shouldReturnErrorsWithAllAfterLimitsDebit() {
        successDebitPage()
                .enterInputs(DataHelper.userWithAfterLimits());
        errorNameDisplayDebit(
                "Неверный формат"
        );
    }

    @Test
    @DisplayName("Должен вернуть ошибку в поле Карты с некорректными символами в форме оплаты")
    void shouldReturnFailWithCardIncorrectSymbolsDebit() {
        successDebitPage()
                .enterIncorrectCardInput(DataHelper.userWithCardIncorrectSymbols(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку в поле Года с некорректными символами в форме оплаты")
    void shouldReturnFailWithYearIncorrectSymbolsDebit() {
        successDebitPage()
                .enterIncorrectYearInput(DataHelper.userWithYearIncorrectSymbols(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку в поле Месяца с некорректными символами в форме оплаты")
    void shouldReturnFailWithMonthIncorrectSymbolsDebit() {
        successDebitPage()
                .enterIncorrectMonthInput(DataHelper.userWithMonthIncorrectSymbols(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку в поле Имени с некорректными символами в форме оплаты")
    void shouldReturnFailWithNameIncorrectSymbolsDebit() {
        successDebitPage()
                .enterIncorrectNameInput(DataHelper.userWithNameIncorrectSymbols(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку в поле CVC с некорректными символами в форме оплаты")
    void shouldReturnFailWithCodeIncorrectSymbolsDebit() {
        successDebitPage()
                .enterIncorrectCodeInput(DataHelper.userWithCodeIncorrectSymbols(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку в полях с некорректными символами в форме оплаты")
    void shouldReturnFailWithAllIncorrectSymbolsDebit() {
        successDebitPage()
                .enterInputs(DataHelper.userWithAllIncorrectSymbols());
        errorsDisplayDebit(
                "Неверный формат",
                "Неверный формат",
                "Неверный формат",
                "Неверный формат",
                "Неверный формат"
        );
    }

    @Test
    @DisplayName("Должен вернуть ошибку в поле Карты с символьным значением в форме оплаты")
    void shouldReturnFailWithCardSymbolicValueDebit() {
        successDebitPage()
                .enterIncorrectCardInput(DataHelper.userWithCardSymbolicValue(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку в поле Года с символьным значением в форме оплаты")
    void shouldReturnFailWithYearSymbolicValueDebit() {
        successDebitPage()
                .enterIncorrectYearInput(DataHelper.userWithYearSymbolicValue(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку в поле Месяца с символьным значением в форме оплаты")
    void shouldReturnFailWithMonthSymbolicValueDebit() {
        successDebitPage()
                .enterIncorrectMonthInput(DataHelper.userWithMonthSymbolicValue(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку в поле Имени с символьным значением в форме оплаты")
    void shouldReturnFailWithNameSymbolicValueDebit() {
        successDebitPage()
                .enterIncorrectNameInput(DataHelper.userWithNameSymbolicValue(), "Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("Должен вернуть ошибку в поле CVC с символьным значением в форме оплаты")
    void shouldReturnFailWithCodeSymbolicValueDebit() {
        successDebitPage()
                .enterIncorrectCodeInput(DataHelper.userWithCodeSymbolicValue(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку в полях с символьными значениями в форме оплаты")
    void shouldReturnFailWithSymbolicValuesDebit() {
        successDebitPage()
                .enterInputs(DataHelper.userWithSymbolicValues());
        errorsDisplayDebit(
                "Неверный формат",
                "Неверный формат",
                "Неверный формат",
                "Поле обязательно для заполнения",
                "Неверный формат"
        );
    }
    private DebitPage successDebitPage() {
        new HomePage().openDebitForm().successOpenPage();
        return new DebitPage();
    }

    private void errorsDisplayDebit(String errorCard, String errorMonth, String errorYear, String errorName, String errorCode) {
        new DebitPage()
                .errorsDisplay(errorCard, errorMonth, errorYear, errorName, errorCode);
    }

    private void errorNameDisplayDebit(String errorName) {
        new DebitPage()
                .errorNameDisplay(errorName);
    }

}