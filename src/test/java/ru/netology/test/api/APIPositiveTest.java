package ru.netology.test.api;

import ru.netology.data.DBHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static ru.netology.data.APIHelper.payFromCard;
import static ru.netology.data.DBHelper.deleteAllDB;
import static ru.netology.data.DataHelper.*;
import static org.junit.jupiter.api.Assertions.*;

public class APIPositiveTest {

    private static final int amount = 45_000_00;
    private final String debitPay = "/pay";
    private final String creditPay = "/credit";

    @AfterEach
    void setDownDB() {
        DBHelper.deleteAllDB();
    }

    @Test
    @DisplayName("Должно быть поле CreditId при оплате одобренной кредитной картой")
    void shouldHaveCreditIdWithCreditApprovedCard() {
        APIHelper.payFromCard(DataHelper.getApprovedCard(), creditPay, 200);
        assertNotNull(DBHelper.getCreditId());
    }

    @Test
    @DisplayName("Должен быть статус APPROVED при оплате одобренной дебетовой картой")
    void shouldHaveStatusWithDebitApprovedCard() {
        APIHelper.payFromCard(DataHelper.getApprovedCard(), debitPay, 200);
        assertEquals("APPROVED", DBHelper.getStatusDebitCard());
    }

    @Test
    @DisplayName("Должен быть статус DECLINED при оплате одобренной дебетовой картой")
    void shouldHaveStatusWithDebitDeclinedCard() {
        APIHelper.payFromCard(DataHelper.getDeclinedCard(), debitPay, 200);
        assertEquals("DECLINED", DBHelper.getStatusDebitCard());
    }

    @Test
    @DisplayName("Должна вернуться ошибка при оплате неизвестной дебетовой картой")
    void shouldReturnFailWithDebitUnknownCard() {
        APIHelper.payFromCard(DataHelper.getUnknownCard(), debitPay, 500);
    }

    @Test
    @DisplayName("Должно быть поле Amount при оплате одобренной дебетовой картой")
    void shouldHaveAmountWithDebitApprovedCard() {
        APIHelper.payFromCard(DataHelper.getApprovedCard(), debitPay, 200);
        assertEquals(amount, DBHelper.getAmountDebitCard());
    }

    @Test
    @DisplayName("Должно быть поле BankId при оплате одобренной кредитной картой")
    void shouldHaveBankIdWithCreditApprovedCard() {
        APIHelper.payFromCard(DataHelper.getApprovedCard(), creditPay, 200);
        assertNotNull(DBHelper.getBankIdCreditCard());
    }

    @Test
    @DisplayName("Должно быть поле PaymentId при оплате одобренной дебетовой картой")
    void shouldHavePaymentIdWithDebitApprovedCard() {
        APIHelper.payFromCard(DataHelper.getApprovedCard(), debitPay, 200);
        assertNotNull(DBHelper.getPaymentId());
    }

    @Test
    @DisplayName("Должно быть поле TransactionId при оплате одобренной дебетовой картой")
    void shouldHaveTransactionIdWithDebitApprovedCard() {
        APIHelper.payFromCard(DataHelper.getApprovedCard(), debitPay, 200);
        assertNotNull(DBHelper.getTransactionIdDebitCard());
    }

    @Test
    @DisplayName("Должно отсутствовать поле CreditId при оплате одобренной дебетовой картой")
    void shouldNotHaveCreditIdWithDebitApprovedCard() {
        APIHelper.payFromCard(DataHelper.getApprovedCard(), debitPay, 200);
        assertNull(DBHelper.getCreditId());
    }

    @Test
    @DisplayName("Поля PaymentId и TransactionId равны при оплате одобренной дебетовой картой")
    void shouldEqualPaymentAndTransactionIdWithDebitApprovedCard() {
        APIHelper.payFromCard(DataHelper.getApprovedCard(), debitPay, 200);
        assertEquals(DBHelper.getPaymentId(), DBHelper.getTransactionIdDebitCard());
    }

    @Test
    @DisplayName("Должен быть статус APPROVED при оплате одобренной кредитной картой")
    void shouldHaveStatusWithCreditApprovedCard() {
        APIHelper.payFromCard(DataHelper.getApprovedCard(), creditPay, 200);
        assertEquals("APPROVED", DBHelper.getStatusCreditCard());
    }

    @Test
    @DisplayName("Должно отсутствовать поле PaymentId при оплате одобренной кредитной картой")
    void shouldNotHavePaymentIdWithCreditApprovedCard() {
        APIHelper.payFromCard(DataHelper.getApprovedCard(), creditPay, 200);
        assertNull(DBHelper.getPaymentId());
    }
    @Test
    @DisplayName("Должна вернуться ошибка при оплате неизвестной кредитной картой")
    void shouldReturnFailWithCreditUnknownCard() {
        APIHelper.payFromCard(DataHelper.getUnknownCard(), creditPay, 500);
    }
}
