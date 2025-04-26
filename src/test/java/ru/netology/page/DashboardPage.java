package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.DisplayName;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {

    private final SelenideElement tourName = $(byText("Путешествие дня"));
    private final SelenideElement bayButton = $$("button.button").findBy(text("Купить"));
    private final SelenideElement creditButton = $$("button.button").findBy(text("Купить в кредит"));
    private final SelenideElement card = $(byText("Оплата по карте"));
    private final SelenideElement creditCard = $(byText("Кредит по данным карты"));
    private final SelenideElement numberFieldFilledIncorrectly = $$(".form-field").findBy(text("Номер карты")).find(".input__control");
    private final SelenideElement numderFieldMonth = $$(".form-field .input-group__input-case").findBy(text("Месяц")).find(".input__control");
    private final SelenideElement yearField = $$(".form-field .input-group__input-case").findBy(text("Год")).find(".input__control");
    private final SelenideElement nameField = $$(".form-field .input-group__input-case").findBy(text("Владелец")).find(".input__control");
    private final SelenideElement securityCodesField = $$(".form-field .input-group__input-case").findBy(text("CVC/CVV")).find(".input__control");
    private final SelenideElement sendButton = $$(".button__text").findBy(text("Продолжить"));
    private final SelenideElement iconTextGood = $(".notification_status_ok .notification__content");
    private final SelenideElement iconTitleBad = $(".notification_status_error .notification__content");
    public final SelenideElement invalidFormat = $$(".form-field .input__sub").findBy(text("Неверный формат"));
    public final SelenideElement fieldIsRequired = $$(".form-field .input__sub").findBy(text("Поле обязательно для заполнения"));
    public final SelenideElement incorrectExpirationDate = $$(".form-field .input__sub").findBy(text("Неверно указан срок действия карты"));
    public final SelenideElement theCardIsExpired = $$(".form-field .input__sub").findBy(text("Истёк срок действия карты"));



    public void bayButtonPage() {
        tourName.shouldHave(visible);
        bayButton.click();
        card.shouldHave(visible);
    }

    public void CreditPage() {
        tourName.shouldHave(visible);
        creditButton.click();
        creditCard.shouldHave(visible);
    }

    public void cardNumber(String getCardNumber) {
        numberFieldFilledIncorrectly.setValue(String.valueOf(getCardNumber));
    }

    public void monthNumber(String getMonthNumber) {
        numderFieldMonth.setValue(String.valueOf(getMonthNumber));
    }

    public void yearNumber(String getYears) {
        yearField.setValue(String.valueOf(getYears));
    }

    public void fullName(String getName) {
        nameField.setValue(String.valueOf(getName));
    }

    public void CVC(String getCVC) {
        securityCodesField.setValue(String.valueOf(getCVC));
    }

    public void buttonContinue() {
        sendButton.click();
    }


    @DisplayName("Операция одобрена банком.")
    public void iconTextGood(String expectedText) {
        iconTextGood
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(exactText(expectedText)).shouldBe(visible);
    }

    @DisplayName("Ошибка! Банк отказал в проведении операции.")
    public void iconTitleBad(String expectedText) {
        iconTitleBad
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(exactText(expectedText)).shouldBe(visible);
    }
}