package ru.netology.page;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

public class CreditPage extends CardPages {

    public void successOpenPage() {
        header.shouldBe(visible).shouldHave(text("Кредит по данным карты"));
    }
}
