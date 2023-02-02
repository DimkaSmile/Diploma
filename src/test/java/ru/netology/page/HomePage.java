package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class HomePage {

    SelenideElement header = $x(".//h2[contains(text(), 'Путешествие')]");
    SelenideElement debitForm = $x(".//span[text()='Купить']");
    SelenideElement creditForm = $x(".//span[text()='Купить в кредит']");

    public void checkHomePageIsOpened() {header.shouldBe(visible).shouldHave(text("Путешествие дня"));
    }

    public DebitPage openDebitForm() {
        debitForm.click();
        return new DebitPage();
    }

    public CreditPage openCreditForm() {
        creditForm.click();
        return new CreditPage();
    }
}