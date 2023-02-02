package ru.netology.page;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

public class DebitPage extends CardPages {

    public void successOpenPage() {
        header.shouldBe(visible).shouldHave(text("Оплата по карте"));
    }
}