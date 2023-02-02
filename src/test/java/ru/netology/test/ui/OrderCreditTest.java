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


public class OrderCreditTest {

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
    @DisplayName("Должен успешно оплатить с одобренной кредитной картой в форме кредита")
    void shouldSuccessPayWithApprovedCreditCard() {
        successCreditPage()
                .enterValidUserWithApprovedCard();
        assertEquals("APPROVED", DBHelper.getStatusCreditCard());
        assertNotNull(DBHelper.getBankIdCreditCard());
        assertNotNull(DBHelper.getCreditId());
        assertNull(DBHelper.getPaymentId());
    }

    @Test
    @DisplayName("Должен вернуть ошибку с отклоненной кредитной картой в форме кредита")
    void shouldReturnFailWithDeclinedCreditCard() {
        successCreditPage()
                .enterValidUserWithIncorrectCard(DataHelper.validUser(DataHelper.getDeclinedCard()));
        assertEquals("DECLINED", DBHelper.getStatusCreditCard());
        assertNull(DBHelper.getBankIdCreditCard());
        assertNull(DBHelper.getCreditId());
    }

    @Test
    @DisplayName("Должен вернуть ошибку с неизвестной кредитной картой в форме кредита")
    void shouldReturnFailWithUnknownCreditCard() {
        successCreditPage()
                .enterValidUserWithIncorrectCard(DataHelper.validUser(DataHelper.getUnknownCard()));
        assertNull(DBHelper.getBankIdCreditCard());
        assertNull(DBHelper.getCreditId());
    }

    @Test
    @DisplayName("Должен вернуть ошибку с пустой кредитной картой в форме кредита")
    void shouldReturnErrorWithEmptyCreditCard() {
        successCreditPage()
                .enterIncorrectCardInput(DataHelper.emptyCardUser(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку с пустым полем месяца в форме кредита")
    void shouldReturnErrorWithEmptyMonthCredit() {
        successCreditPage()
                .enterIncorrectMonthInput(DataHelper.emptyMonthUser(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку с пустым полем года в форме кредита")
    void shouldReturnErrorWithEmptyYearCredit() {
        successCreditPage()
                .enterIncorrectYearInput(DataHelper.emptyYearUser(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку с пустым полем имени в форме кредита")
    void shouldReturnErrorWithEmptyNameCredit() {
        successCreditPage()
                .enterIncorrectNameInput(DataHelper.emptyNameUser(), "Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("Должен вернуть ошибку с пустым полем CVC в форме кредита")
    void shouldReturnErrorWithEmptyCodeCredit() {
        successCreditPage()
                .enterIncorrectCodeInput(DataHelper.emptyCodeUser(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку со всеми пустыми полями в форме кредита")
    void shouldReturnErrorsWithEmptyAllCredit() {
        successCreditPage()
                .enterInputs(DataHelper.emptyUser());
        errorsDisplayCredit(
                "Неверный формат",
                "Неверный формат",
                "Неверный формат",
                "Поле обязательно для заполнения",
                "Неверный формат"
        );
    }

    @Test
    @DisplayName("Должен вернуть ошибку с некорректным полем карты в форме кредита")
    void shouldReturnErrorWithInvalidCreditCard() {
        successCreditPage()
                .enterIncorrectCardInput(DataHelper.invalidCardUser(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку с некорректным полем месяца в форме кредита")
    void shouldReturnErrorWithInvalidMonthCredit() {
        successCreditPage()
                .enterIncorrectMonthInput(DataHelper.invalidMonthUser(), "Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("Должен вернуть ошибку с некорректным полем года в форме кредита")
    void shouldReturnErrorWithInvalidYearCredit() {
        successCreditPage()
                .enterIncorrectYearInput(DataHelper.invalidYearUser(), "Истёк срок действия карты");
    }

    @Test
    @DisplayName("Должен вернуть ошибку с некорректным полем имени в форме кредита")
    void shouldReturnErrorWithInvalidNameCredit() {
        successCreditPage()
                .enterIncorrectNameInput(DataHelper.invalidNameUser(), "Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("Должен вернуть ошибку с некорректным полем CVC в форме кредита")
    void shouldReturnErrorWithInvalidCodeCredit() {
        successCreditPage()
                .enterIncorrectCodeInput(DataHelper.invalidCodeUser(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку со всеми некорректными полями в форме кредита")
    void shouldReturnErrorsWithInvalidAllCredit() {
        successCreditPage()
                .enterInputs(DataHelper.invalidUser());
        errorsDisplayCredit(
                "Неверный формат",
                "Неверно указан срок действия карты",
                "Истёк срок действия карты",
                "Неверный формат",
                "Неверный формат"
        );
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением '0' поля Карты в форме кредита")
    void shouldReturnErrorsWithCardZeroInputsCredit() {
        successCreditPage()
                .enterIncorrectCardInput(DataHelper.userWithCardZero(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением '0' поля Года в форме кредита")
    void shouldReturnErrorsWithYearZeroInputsCredit() {
        successCreditPage()
                .enterIncorrectYearInput(DataHelper.userWithYearZero(), "Истёк срок действия карты");
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением '0' поля Месяца в форме кредита")
    void shouldReturnErrorsWithMonthZeroInputsCredit() {
        successCreditPage()
                .enterIncorrectMonthInput(DataHelper.userWithMonthZero(), "Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением '0' поля Имени в форме кредита")
    void shouldReturnErrorsWithNameZeroInputsCredit() {
        successCreditPage()
                .enterIncorrectNameInput(DataHelper.userWithNameZero(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением '0' поля CVC в форме кредита")
    void shouldReturnErrorsWithCodeZeroInputsCredit() {
        successCreditPage()
                .enterIncorrectCodeInput(DataHelper.userWithCodeZero(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку со всеми полями со значением '0' в форме кредита")
    void shouldReturnErrorsWithAllZeroInputsCredit() {
        successCreditPage()
                .enterInputs(DataHelper.userWithAllZero());
        errorsDisplayCredit(
                "Неверный формат",
                "Неверно указан срок действия карты",
                "Истёк срок действия карты",
                "Неверный формат",
                "Неверный формат"
        );
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением поля Карты до лимита в форме кредита")
    void shouldReturnErrorWithCardUnderLimitCredit() {
        successCreditPage()
                .enterIncorrectCardInput(DataHelper.userWithCardUnderLimit(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением поля Года до лимита в форме кредита")
    void shouldReturnErrorWithYearUnderLimitCredit() {
        successCreditPage()
                .enterIncorrectYearInput(DataHelper.userWithYearUnderLimit(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением поля Месяца до лимита в форме кредита")
    void shouldReturnErrorWithMonthUnderLimitCredit() {
        successCreditPage()
                .enterIncorrectMonthInput(DataHelper.userWithMonthUnderLimit(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением поля Имени до лимита в форме кредита")
    void shouldReturnErrorWithNameUnderLimitCredit() {
        successCreditPage()
                .enterIncorrectNameInput(DataHelper.userWithNameUnderLimit(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением поля CVC до лимита в форме кредита")
    void shouldReturnErrorWithCodeUnderLimitCredit() {
        successCreditPage()
                .enterIncorrectCodeInput(DataHelper.userWithCodeUnderLimit(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку со всеми полями до лимита в форме кредита")
    void shouldReturnErrorsWithAllUnderLimitsCredit() {
        successCreditPage()
                .enterInputs(DataHelper.userWithAllUnderLimits());
        errorsDisplayCredit(
                "Неверный формат",
                "Неверный формат",
                "Неверный формат",
                "Неверный формат",
                "Неверный формат"
        );
    }

    @Test
    @DisplayName("Должен вернуть успешно внести валидное значение поля Карты сверх лимита в форме кредита")
    void shouldReturnErrorWithCardAfterLimitCredit() {
        successCreditPage()
                .enterInputs(DataHelper.userWithCardAfterLimit());
    }

    @Test
    @DisplayName("Должен вернуть успешно внести валидное значение поля Года сверх лимита в форме кредита")
    void shouldReturnErrorWithYearAfterLimitCredit() {
        successCreditPage()
                .enterInputs(DataHelper.userWithYearAfterLimit());
    }

    @Test
    @DisplayName("Должен вернуть успешно внести валидное значение поля Месяца сверх лимита в форме кредита")
    void shouldReturnErrorWithMonthAfterLimitCredit() {
        successCreditPage()
                .enterInputs(DataHelper.userWithMonthAfterLimit());
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением поля Имени сверх лимита в форме кредита")
    void shouldReturnErrorWithNameAfterLimitCredit() {
        successCreditPage()
                .enterIncorrectNameInput(DataHelper.userWithNameAfterLimit(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть успешно внести валидное значение поля CVC сверх лимита в форме кредита")
    void shouldReturnErrorWithCodeAfterLimitCredit() {
        successCreditPage()
                .enterInputs(DataHelper.userWithCodeAfterLimit());
    }

    @Test
    @DisplayName("Должен вернуть ошибку у поля Имени при вводе всех полей сверх лимита в форме кредита")
    void shouldReturnErrorsWithAllAfterLimitsCredit() {
        successCreditPage()
                .enterInputs(DataHelper.userWithAfterLimits());
        errorNameDisplayCredit(
                "Неверный формат"
        );
    }

    @Test
    @DisplayName("Должен вернуть ошибку в поле Карты с некорректными символами в форме кредита")
    void shouldReturnFailWithCardIncorrectSymbolsCredit() {
        successCreditPage()
                .enterIncorrectCardInput(DataHelper.userWithCardIncorrectSymbols(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку в поле Года с некорректными символами в форме кредита")
    void shouldReturnFailWithYearIncorrectSymbolsCredit() {
        successCreditPage()
                .enterIncorrectYearInput(DataHelper.userWithYearIncorrectSymbols(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку в поле Месяца с некорректными символами в форме кредита")
    void shouldReturnFailWithMonthIncorrectSymbolsCredit() {
        successCreditPage()
                .enterIncorrectMonthInput(DataHelper.userWithMonthIncorrectSymbols(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку в поле Имени с некорректными символами в форме кредита")
    void shouldReturnFailWithNameIncorrectSymbolsCredit() {
        successCreditPage()
                .enterIncorrectNameInput(DataHelper.userWithNameIncorrectSymbols(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку в поле CVC с некорректными символами в форме кредита")
    void shouldReturnFailWithCodeIncorrectSymbolsCredit() {
        successCreditPage()
                .enterIncorrectCodeInput(DataHelper.userWithCodeIncorrectSymbols(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку в полях с некорректными символами в форме кредита")
    void shouldReturnFailWithIncorrectSymbolsCredit() {
        successCreditPage().enterInputs(DataHelper.userWithAllIncorrectSymbols());
        errorsDisplayCredit(
                "Неверный формат",
                "Неверный формат",
                "Неверный формат",
                "Неверный формат",
                "Неверный формат"
        );
    }

    @Test
    @DisplayName("Должен вернуть ошибку в поле Карты с символьным значением в форме кредита")
    void shouldReturnFailWithCardSymbolicValueCredit() {
        successCreditPage()
                .enterIncorrectCardInput(DataHelper.userWithCardSymbolicValue(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку в поле Года с символьным значением в форме кредита")
    void shouldReturnFailWithYearSymbolicValueCredit() {
        successCreditPage()
                .enterIncorrectYearInput(DataHelper.userWithYearSymbolicValue(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку в поле Месяца с символьным значением в форме кредита")
    void shouldReturnFailWithMonthSymbolicValueCredit() {
        successCreditPage()
                .enterIncorrectMonthInput(DataHelper.userWithMonthSymbolicValue(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку в поле Имени с символьным значением в форме кредита")
    void shouldReturnFailWithNameSymbolicValueCredit() {
        successCreditPage()
                .enterIncorrectNameInput(DataHelper.userWithNameSymbolicValue(), "Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("Должен вернуть ошибку в поле CVC с символьным значением в форме кредита")
    void shouldReturnFailWithCodeSymbolicValueCredit() {
        successCreditPage()
                .enterIncorrectCodeInput(DataHelper.userWithCodeSymbolicValue(), "Неверный формат");
    }

    @Test
    @DisplayName("Должен вернуть ошибку в полях с символьными значениями в форме кредита")
    void shouldReturnFailWithSymbolicValuesCredit() {
        successCreditPage().enterInputs(DataHelper.userWithSymbolicValues());
        errorsDisplayCredit(
                "Неверный формат",
                "Неверный формат",
                "Неверный формат",
                "Поле обязательно для заполнения",
                "Неверный формат"
        );
    }

    private CreditPage successCreditPage() {
        new HomePage().openCreditForm().successOpenPage();
        return new CreditPage();
    }

    private void errorsDisplayCredit(String errorCard, String errorMonth, String errorYear, String errorName, String errorCode) {
        new CreditPage()
                .errorsDisplay(errorCard, errorMonth, errorYear, errorName, errorCode);
    }

    private void errorNameDisplayCredit(String errorName) {
        new CreditPage()
                .errorNameDisplay(errorName);
    }
}
