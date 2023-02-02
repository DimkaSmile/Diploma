package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataHelper {

    private static final Faker faker = new Faker();

    private static final String approvedCard = "4444 4444 4444 4441";
    private static final String declinedCard = "4444 4444 4444 4442";
    private static final String unknownCard = "4444 4444 4444 4444";

    @Value
    public static class AuthInfo {
        String number;
        String year;
        String month;
        String holder;
        String cvc;
    }

    public static AuthInfo validUser(String cardType) {
        return new AuthInfo(
                cardType, // Номер карты: approvedCard, declinedCard или unknownCard
                generateDate(1, "yy"), // Год (следующего после текущего месяца)
                generateDate(1, "MM"), // Следующий месяц после текущего
                generateHolder("EN"), // Имя пользователя (Имя + Фамилия) на английском
                String.valueOf(faker.number().numberBetween(100, 999)) // Любой CVC-код из 3 цифр
        );
    }

    // Пользователь с нулевыми значениями полей.

    public static AuthInfo userWithAllZero() {
        return new AuthInfo(
                "0000 0000 0000 0000",
                "00",
                "00",
                "000 000",
                "000"
        );
    }

    public static AuthInfo userWithCardZero() {
        return new AuthInfo(
                "0000 0000 0000 0000",
                generateDate(2, "yy"),
                generateDate(2, "MM"),
                generateHolder("EN"),
                String.valueOf(faker.number().numberBetween(100, 999))
        );
    }

    public static AuthInfo userWithYearZero() {
        return new AuthInfo(
                getApprovedCard(),
                "00",
                generateDate(1, "MM"),
                generateHolder("EN"),
                String.valueOf(faker.number().numberBetween(100, 999))
        );
    }

    public static AuthInfo userWithMonthZero() {
        return new AuthInfo(
                getApprovedCard(),
                generateDate(1, "yy"),
                "00",
                generateHolder("EN"),
                String.valueOf(faker.number().numberBetween(100, 999))
        );
    }

    public static AuthInfo userWithNameZero() {
        return new AuthInfo(
                getApprovedCard(),
                generateDate(1, "yy"),
                generateDate(1, "MM"),
                "000",
                String.valueOf(faker.number().numberBetween(100, 999))
        );
    }

    public static AuthInfo userWithCodeZero() {
        return new AuthInfo(
                getApprovedCard(),
                generateDate(1, "yy"),
                generateDate(1, "MM"),
                generateHolder("EN"),
                "000"
        );
    }

    // Пользователь со значением полей ниже лимита.

    public static AuthInfo userWithAllUnderLimits() {
        return new AuthInfo(
                "4444 4444 4444 444",
                String.valueOf(faker.number().numberBetween(1, 9)),
                String.valueOf(faker.number().numberBetween(1, 9)),
                "f",
                String.valueOf(faker.number().numberBetween(1, 99))
        );
    }

    public static AuthInfo userWithCardUnderLimit() {
        return new AuthInfo(
                "4444 4444 4444 444",
                generateDate(1, "yy"),
                generateDate(1, "MM"),
                generateHolder("EN"),
                String.valueOf(faker.number().numberBetween(100, 999))
        );
    }

    public static AuthInfo userWithYearUnderLimit() {
        return new AuthInfo(
                getApprovedCard(),
                String.valueOf(faker.number().numberBetween(1, 9)),
                generateDate(1, "MM"),
                generateHolder("EN"),
                String.valueOf(faker.number().numberBetween(100, 999))
        );
    }

    public static AuthInfo userWithMonthUnderLimit() {
        return new AuthInfo(
                getApprovedCard(),
                generateDate(1, "yy"),
                String.valueOf(faker.number().numberBetween(1, 9)),
                generateHolder("EN"),
                String.valueOf(faker.number().numberBetween(100, 999))
        );
    }

    public static AuthInfo userWithNameUnderLimit() {
        return new AuthInfo(
                getApprovedCard(),
                generateDate(1, "yy"),
                generateDate(1, "MM"),
                "f",
                String.valueOf(faker.number().numberBetween(100, 999))
        );
    }

    public static AuthInfo userWithCodeUnderLimit() {
        return new AuthInfo(
                getApprovedCard(),
                generateDate(1, "yy"),
                generateDate(1, "MM"),
                generateHolder("EN"),
                String.valueOf(faker.number().numberBetween(1, 99))
        );
    }

    // Пользователь со значением полей сверх лимита.

    public static AuthInfo userWithAfterLimits() {
        return new AuthInfo(
                "44444444444444444",
                "234",
                "123",
                "SAJDKFJDSFHSDKASDJNSAJDJSANDJAKNDJANDJKADNJANDJKANDJADNKJAJDNAJKDNSJJSJ",
                "1234"
        );
    }

    public static AuthInfo userWithCardAfterLimit() {
        return new AuthInfo(
                "44444444444444444",
                generateDate(1, "yy"),
                generateDate(1, "MM"),
                generateHolder("EN"),
                String.valueOf(faker.number().numberBetween(100, 999))
        );
    }

    public static AuthInfo userWithYearAfterLimit() {
        return new AuthInfo(
                getApprovedCard(),
                "234",
                generateDate(1, "MM"),
                generateHolder("EN"),
                String.valueOf(faker.number().numberBetween(100, 999))
        );
    }

    public static AuthInfo userWithMonthAfterLimit() {
        return new AuthInfo(
                getApprovedCard(),
                generateDate(1, "yy"),
                "123",
                generateHolder("EN"),
                String.valueOf(faker.number().numberBetween(100, 999))
        );
    }

    public static AuthInfo userWithNameAfterLimit() {
        return new AuthInfo(
                getApprovedCard(),
                generateDate(1, "yy"),
                generateDate(1, "MM"),
                "SAJDKFJDSFHSDKASDJNSAJDJSANDJAKNDJANDJKADNJANDJKANDJADNKJAJDNAJKDNSJJSJ",
                String.valueOf(faker.number().numberBetween(100, 999))
        );
    }

    public static AuthInfo userWithCodeAfterLimit() {
        return new AuthInfo(
                getApprovedCard(),
                generateDate(1, "yy"),
                generateDate(1, "MM"),
                generateHolder("EN"),
                "1234"
        );
    }

    // Пользователь с некорректными значениями полей.

    public static AuthInfo userWithAllIncorrectSymbols() {
        return new AuthInfo(
                "abcd fcds",
                "ab",
                "ab",
                "123456 1234",
                "abc"
        );
    }

    public static AuthInfo userWithCardIncorrectSymbols() {
        return new AuthInfo(
                "abcd fcds",
                generateDate(1, "yy"),
                generateDate(1, "MM"),
                generateHolder("EN"),
                String.valueOf(faker.number().numberBetween(100, 999))
        );
    }

    public static AuthInfo userWithYearIncorrectSymbols() {
        return new AuthInfo(
                getApprovedCard(),
                "ab",
                generateDate(1, "MM"),
                generateHolder("EN"),
                String.valueOf(faker.number().numberBetween(100, 999))
        );
    }

    public static AuthInfo userWithMonthIncorrectSymbols() {
        return new AuthInfo(
                getApprovedCard(),
                generateDate(1, "yy"),
                "ab",
                generateHolder("EN"),
                String.valueOf(faker.number().numberBetween(100, 999))
        );
    }

    public static AuthInfo userWithNameIncorrectSymbols() {
        return new AuthInfo(
                getApprovedCard(),
                generateDate(1, "yy"),
                generateDate(1, "MM"),
                "123456 1234",
                String.valueOf(faker.number().numberBetween(100, 999))
        );
    }

    public static AuthInfo userWithCodeIncorrectSymbols() {
        return new AuthInfo(
                getApprovedCard(),
                generateDate(1, "yy"),
                generateDate(1, "MM"),
                generateHolder("EN"),
                "abc"
        );
    }

    // Пользователь со значением полей из символов.

    public static AuthInfo userWithSymbolicValues() {
        return new AuthInfo(
                "%&$# &@^!",
                "*&",
                "?@",
                ")*^%@ @!#*(",
                "*^$"
        );
    }

    public static AuthInfo userWithCardSymbolicValue() {
        return new AuthInfo(
                "%&$# &@^!",
                generateDate(1, "yy"),
                generateDate(1, "MM"),
                generateHolder("EN"),
                String.valueOf(faker.number().numberBetween(100, 999))
        );
    }

    public static AuthInfo userWithYearSymbolicValue() {
        return new AuthInfo(
                getApprovedCard(),
                "*&",
                generateDate(1, "MM"),
                generateHolder("EN"),
                String.valueOf(faker.number().numberBetween(100, 999))
        );
    }

    public static AuthInfo userWithMonthSymbolicValue() {
        return new AuthInfo(
                getApprovedCard(),
                generateDate(1, "yy"),
                "?@",
                generateHolder("EN"),
                String.valueOf(faker.number().numberBetween(100, 999))
        );
    }

    public static AuthInfo userWithNameSymbolicValue() {
        return new AuthInfo(
                getApprovedCard(),
                generateDate(1, "yy"),
                generateDate(1, "MM"),
                ")*^%@ @!#*(",
                String.valueOf(faker.number().numberBetween(100, 999))
        );
    }

    public static AuthInfo userWithCodeSymbolicValue() {
        return new AuthInfo(
                getApprovedCard(),
                generateDate(1, "yy"),
                generateDate(1, "MM"),
                generateHolder("EN"),
                "*^$"
        );
    }

    // Невалидный пользователь с неподходящими значениями полей.

    public static AuthInfo invalidUser() {
        return new AuthInfo(
                "4444",
                String.valueOf(faker.number().numberBetween(10, 20)),
                String.valueOf(faker.number().numberBetween(13, 99)),
                generateHolder("RU"),
                String.valueOf(faker.number().numberBetween(0, 99))
        );
    }

    public static AuthInfo invalidCardUser() {
        return new AuthInfo(
                "4444",
                generateDate(1, "yy"),
                generateDate(1, "MM"),
                generateHolder("EN"),
                String.valueOf(faker.number().numberBetween(100, 999))
        );
    }

    public static AuthInfo invalidYearUser() {
        return new AuthInfo(
                getApprovedCard(),
                String.valueOf(faker.number().numberBetween(10, 20)),
                generateDate(1, "MM"),
                generateHolder("EN"),
                String.valueOf(faker.number().numberBetween(100, 999))
        );
    }

    public static AuthInfo invalidMonthUser() {
        return new AuthInfo(
                getApprovedCard(),
                generateDate(1, "yy"),
                String.valueOf(faker.number().numberBetween(13, 99)),
                generateHolder("EN"),
                String.valueOf(faker.number().numberBetween(100, 999))
        );
    }

    public static AuthInfo invalidNameUser() {
        return new AuthInfo(
                getApprovedCard(),
                generateDate(1, "yy"),
                generateDate(1, "MM"),
                generateHolder("RU"),
                String.valueOf(faker.number().numberBetween(100, 999))
        );
    }

    public static AuthInfo invalidCodeUser() {
        return new AuthInfo(
                getApprovedCard(),
                generateDate(1, "yy"),
                generateDate(1, "MM"),
                generateHolder("EN"),
                String.valueOf(faker.number().numberBetween(0, 99))
        );
    }

    // Пользователь с пустыми значениями полей.

    public static AuthInfo emptyUser() {
        return new AuthInfo(
                "",
                "",
                "",
                "",
                ""
        );
    }

    public static AuthInfo emptyCardUser() {
        return new AuthInfo(
                "",
                generateDate(1, "yy"),
                generateDate(1, "MM"),
                generateHolder("EN"),
                String.valueOf(faker.number().numberBetween(100, 999))
        );
    }

    public static AuthInfo emptyYearUser() {
        return new AuthInfo(
                getApprovedCard(),
                "",
                generateDate(1, "MM"),
                generateHolder("EN"),
                String.valueOf(faker.number().numberBetween(100, 999))
        );
    }

    public static AuthInfo emptyMonthUser() {
        return new AuthInfo(
                getApprovedCard(),
                generateDate(1, "yy"),
                "",
                generateHolder("EN"),
                String.valueOf(faker.number().numberBetween(100, 999))
        );
    }

    public static AuthInfo emptyNameUser() {
        return new AuthInfo(
                getApprovedCard(),
                generateDate(1, "yy"),
                generateDate(1, "MM"),
                "",
                String.valueOf(faker.number().numberBetween(100, 999))
        );
    }

    public static AuthInfo emptyCodeUser() {
        return new AuthInfo(
                getApprovedCard(),
                generateDate(1, "yy"),
                generateDate(1, "MM"),
                generateHolder("EN"),
                ""
        );
    }


    private static String generateDate(int monthToAdd, String formatPattern) {
        return LocalDate.now().plusMonths(monthToAdd).format(DateTimeFormatter.ofPattern(formatPattern));
    }

    private static String generateHolder(String fakerLocale) {
        Faker faker = new Faker(new Locale(fakerLocale));
        return faker.name().firstName() + " " + faker.name().lastName();
    }

    public static String getApprovedCard() {
        return approvedCard;
    }

    public static String getDeclinedCard() {
        return declinedCard;
    }

    public static String getUnknownCard() {
        return unknownCard;
    }
}
