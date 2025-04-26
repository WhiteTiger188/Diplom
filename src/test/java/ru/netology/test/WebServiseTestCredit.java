package ru.netology.test;

import org.junit.jupiter.api.*;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import ru.netology.data.DataDB;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;

import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;
import static ru.netology.data.DataHelper.cleanDB;

public class WebServiseTestCredit {
    DashboardPage dashboardPage;
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void stopData() {

        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    @DisplayName("Переход к покупки тура в кредит")
    void setup() {
        dashboardPage = open("http://localhost:8080", DashboardPage.class);
        dashboardPage.CreditPage();
    }
    @AfterEach
    void tearDownAllSQL() {
        cleanDB();
    }
    @Test
    @DisplayName("Заполнение валидными данными")
    void filledInCompletelyCredit() {
        dashboardPage.cardNumber(DataDB.getNumberApprovedCard());
        dashboardPage.monthNumber(DataDB.generateValidDate(0, 0, "MM"));
        dashboardPage.yearNumber(DataDB.generateValidDate(0, 0, "yy"));
        dashboardPage.fullName(DataDB.generateRandomSurname());
        dashboardPage.CVC(DataDB.generateCVC_CVV());
        dashboardPage.buttonContinue();
        dashboardPage.iconTextGood("Операция одобрена банком.");
        var actualStatusPayment = DataHelper.getStatusCredit();
        var actualStatusOrder_entityPayment = DataHelper.getStatusOrderPayment();
        String expectedStatus = DataDB.APPROVED().getStatus();
        String expectedId = null;
        assertAll(() -> assertEquals(expectedStatus, actualStatusPayment),
                () -> assertEquals(expectedId, actualStatusOrder_entityPayment));
    }
    @Test
    @DisplayName("Ввод номера карты со статусом DECLINED")
    void cardStatusСredit() {
        dashboardPage.cardNumber(DataDB.getNumberDeclinedCard());
        dashboardPage.monthNumber(DataDB.generateValidDate(0, 0, "MM"));
        dashboardPage.yearNumber(DataDB.generateValidDate(0, 0, "yy"));
        dashboardPage.fullName(DataDB.generateRandomSurname());
        dashboardPage.CVC(DataDB.generateCVC_CVV());
        dashboardPage.buttonContinue();
        dashboardPage.iconTitleBad("Ошибка! Банк отказал в проведении операции.");
        var actualStatusPayment = DataHelper.getStatusCredit();
        var actualStatusOrder_entityPayment = DataHelper.getStatusOrderPayment();
        String expectedStatus = DataDB.DECLINED().getStatus();
        String expectedId = null;
        assertAll(() -> assertEquals(expectedStatus, actualStatusPayment),
                () -> assertEquals(expectedId, actualStatusOrder_entityPayment));
    }
    @Test
    @DisplayName("Ввод 16-значного рандомного номера карты")
    void randomNumberСredit() {
        dashboardPage.cardNumber(DataDB.generateRandomCard());
        dashboardPage.monthNumber(DataDB.generateValidDate(0, 0, "MM"));
        dashboardPage.yearNumber(DataDB.generateValidDate(0, 0, "yy"));
        dashboardPage.fullName(DataDB.generateRandomSurname());
        dashboardPage.CVC(DataDB.generateCVC_CVV());
        dashboardPage.buttonContinue();
        dashboardPage.iconTitleBad("Ошибка! Банк отказал в проведении операции.");
        var actualStatusPayment = DataHelper.getStatusCredit();
        var actualStatusOrder_entityPayment = DataHelper.getStatusOrderPayment();
        String expectedStatus = null;
        String expectedId = null;
        assertAll(() -> assertEquals(expectedStatus, actualStatusPayment),
                () -> assertEquals(expectedId, actualStatusOrder_entityPayment));
    }
    @Test
    @DisplayName("Ввод 15-значного рандомного номера карты")
    void randomNumber15Сredit() {
        dashboardPage.cardNumber(DataDB.generateNotValidCard());
        dashboardPage.monthNumber(DataDB.generateValidDate(0, 0, "MM"));
        dashboardPage.yearNumber(DataDB.generateValidDate(0, 0, "yy"));
        dashboardPage.fullName(DataDB.generateRandomSurname());
        dashboardPage.CVC(DataDB.generateCVC_CVV());
        dashboardPage.buttonContinue();
        dashboardPage.invalidFormat.shouldBe(visible);
        var actualStatusPayment = DataHelper.getStatusCredit();
        var actualStatusOrder_entityPayment = DataHelper.getStatusOrderPayment();
        String expectedStatus = null;
        String expectedId = null;
        assertAll(() -> assertEquals(expectedStatus, actualStatusPayment),
                () -> assertEquals(expectedId, actualStatusOrder_entityPayment));
    }

    @Test
    @DisplayName("Поле \"Номер карты\" оставить не заполненным")
    void emptyNumberCardСredit() {
        dashboardPage.cardNumber("");
        dashboardPage.monthNumber(DataDB.generateValidDate(0, 0, "MM"));
        dashboardPage.yearNumber(DataDB.generateValidDate(0, 0, "yy"));
        dashboardPage.fullName(DataDB.generateRandomSurname());
        dashboardPage.CVC(DataDB.generateCVC_CVV());
        dashboardPage.buttonContinue();
        dashboardPage.fieldIsRequired.shouldBe(visible);
        var actualStatusPayment = DataHelper.getStatusCredit();
        var actualStatusOrder_entityPayment = DataHelper.getStatusOrderPayment();
        String expectedStatus = null;
        String expectedId = null;
        assertAll(() -> assertEquals(expectedStatus, actualStatusPayment),
                () -> assertEquals(expectedId, actualStatusOrder_entityPayment));
    }

    @Test
    @DisplayName("Заполнение поля \"Месяц\" значением 00")
    void month00Сredit() {
        dashboardPage.cardNumber(DataDB.getNumberApprovedCard());
        dashboardPage.monthNumber("00");
        dashboardPage.yearNumber(DataDB.generateValidDate(0, 1, "yy"));
        dashboardPage.fullName(DataDB.generateRandomSurname());
        dashboardPage.CVC(DataDB.generateCVC_CVV());
        dashboardPage.buttonContinue();
        dashboardPage.incorrectExpirationDate.shouldBe(visible);
        var actualStatusPayment = DataHelper.getStatusCredit();
        var actualStatusOrder_entityPayment = DataHelper.getStatusOrderPayment();
        String expectedStatus = null;
        String expectedId = null;
        assertAll(() -> assertEquals(expectedStatus, actualStatusPayment),
                () -> assertEquals(expectedId, actualStatusOrder_entityPayment));
    }

    @Test
    @DisplayName("Заполнение поля \"Месяц\" значением 14")
    void month14Сredit() {
        dashboardPage.cardNumber(DataDB.getNumberApprovedCard());
        dashboardPage.monthNumber("14");
        dashboardPage.yearNumber(DataDB.generateValidDate(0, 0, "yy"));
        dashboardPage.fullName(DataDB.generateRandomSurname());
        dashboardPage.CVC(DataDB.generateCVC_CVV());
        dashboardPage.buttonContinue();
        dashboardPage.incorrectExpirationDate.shouldBe(visible);
        var actualStatusPayment = DataHelper.getStatusCredit();
        var actualStatusOrder_entityPayment = DataHelper.getStatusOrderPayment();
        String expectedStatus = null;
        String expectedId = null;
        assertAll(() -> assertEquals(expectedStatus, actualStatusPayment),
                () -> assertEquals(expectedId, actualStatusOrder_entityPayment));
    }

    @Test
    @DisplayName("Поле \"Месяц\" оставить не заполненным")
    void emptyMonthСredit() {
        dashboardPage.cardNumber(DataDB.getNumberApprovedCard());
        dashboardPage.monthNumber("");
        dashboardPage.yearNumber(DataDB.generateValidDate(0, 0, "yy"));
        dashboardPage.fullName(DataDB.generateRandomSurname());
        dashboardPage.CVC(DataDB.generateCVC_CVV());
        dashboardPage.buttonContinue();
        dashboardPage.fieldIsRequired.shouldBe(visible);
        var actualStatusPayment = DataHelper.getStatusCredit();
        var actualStatusOrder_entityPayment = DataHelper.getStatusOrderPayment();
        String expectedStatus = null;
        String expectedId = null;
        assertAll(() -> assertEquals(expectedStatus, actualStatusPayment),
                () -> assertEquals(expectedId, actualStatusOrder_entityPayment));
    }

    @Test
    @DisplayName("Заполнение поле \"Месяц\" на один меньше текущего и поле \"Год\" текущий")
    void expiredСardСredit() {
        dashboardPage.cardNumber(DataDB.getNumberApprovedCard());
        dashboardPage.monthNumber(DataDB.generateValidDate(11, 0, "MM"));
        dashboardPage.yearNumber(DataDB.generateValidDate(0, 0, "yy"));
        dashboardPage.fullName(DataDB.generateRandomSurname());
        dashboardPage.CVC(DataDB.generateCVC_CVV());
        dashboardPage.buttonContinue();
        dashboardPage.theCardIsExpired.shouldBe(visible);
        var actualStatusPayment = DataHelper.getStatusCredit();
        var actualStatusOrder_entityPayment = DataHelper.getStatusOrderPayment();
        String expectedStatus = null;
        String expectedId = null;
        assertAll(() -> assertEquals(expectedStatus, actualStatusPayment),
                () -> assertEquals(expectedId, actualStatusOrder_entityPayment));
    }

    @Test
    @DisplayName("Заполнение поля \"Год\" прибавив к текущему году 6 лет")
    void moreYear6Сredit() {
        dashboardPage.cardNumber(DataDB.getNumberApprovedCard());
        dashboardPage.monthNumber(DataDB.generateValidDate(0, 0, "MM"));
        dashboardPage.yearNumber(DataDB.generateValidDate(0, 6, "yy"));
        dashboardPage.fullName(DataDB.generateRandomSurname());
        dashboardPage.CVC(DataDB.generateCVC_CVV());
        dashboardPage.buttonContinue();
        dashboardPage.incorrectExpirationDate.shouldBe(visible);
        var actualStatusPayment = DataHelper.getStatusCredit();
        var actualStatusOrder_entityPayment = DataHelper.getStatusOrderPayment();
        String expectedStatus = null;
        String expectedId = null;
        assertAll(() -> assertEquals(expectedStatus, actualStatusPayment),
                () -> assertEquals(expectedId, actualStatusOrder_entityPayment));
    }

    @Test
    @DisplayName("Заполнение поля \"Год\" на один меньше текущего")
    void lessYear1Сredit() {
        dashboardPage.cardNumber(DataDB.getNumberApprovedCard());
        dashboardPage.monthNumber(DataDB.generateValidDate(0, 0, "MM"));
        dashboardPage.yearNumber(DataDB.generateLastYear(1, "yy"));
        dashboardPage.fullName(DataDB.generateRandomSurname());
        dashboardPage.CVC(DataDB.generateCVC_CVV());
        dashboardPage.buttonContinue();
        dashboardPage.theCardIsExpired.shouldBe(visible);
        var actualStatusPayment = DataHelper.getStatusCredit();
        var actualStatusOrder_entityPayment = DataHelper.getStatusOrderPayment();
        String expectedStatus = null;
        String expectedId = null;
        assertAll(() -> assertEquals(expectedStatus, actualStatusPayment),
                () -> assertEquals(expectedId, actualStatusOrder_entityPayment));
    }

    @Test
    @DisplayName("Поле \"Год\" оставить не заполненным")
    void emptyYearСredit() {
        dashboardPage.cardNumber(DataDB.getNumberApprovedCard());
        dashboardPage.monthNumber(DataDB.generateValidDate(0, 0, "MM"));
        dashboardPage.yearNumber("");
        dashboardPage.fullName(DataDB.generateRandomSurname());
        dashboardPage.CVC(DataDB.generateCVC_CVV());
        dashboardPage.buttonContinue();
        dashboardPage.fieldIsRequired.shouldBe(visible);
        var actualStatusPayment = DataHelper.getStatusCredit();
        var actualStatusOrder_entityPayment = DataHelper.getStatusOrderPayment();
        String expectedStatus = null;
        String expectedId = null;
        assertAll(() -> assertEquals(expectedStatus, actualStatusPayment),
                () -> assertEquals(expectedId, actualStatusOrder_entityPayment));
    }

    @Test
    @DisplayName("Поле \"Владелец\" оставить не заполненным")
    void emptyNameСredit() {
        dashboardPage.cardNumber(DataDB.getNumberApprovedCard());
        dashboardPage.monthNumber(DataDB.generateValidDate(0, 0, "MM"));
        dashboardPage.yearNumber(DataDB.generateValidDate(0, 0, "yy"));
        dashboardPage.fullName("");
        dashboardPage.CVC(DataDB.generateCVC_CVV());
        dashboardPage.buttonContinue();
        dashboardPage.fieldIsRequired.shouldBe(visible);
        var actualStatusPayment = DataHelper.getStatusCredit();
        var actualStatusOrder_entityPayment = DataHelper.getStatusOrderPayment();
        String expectedStatus = null;
        String expectedId = null;
        assertAll(() -> assertEquals(expectedStatus, actualStatusPayment),
                () -> assertEquals(expectedId, actualStatusOrder_entityPayment));
    }

    @Test
    @DisplayName("Поле \"CVC/CVV\" оставить не заполненным")
    void emptyCVCСredit() {
        dashboardPage.cardNumber(DataDB.getNumberApprovedCard());
        dashboardPage.monthNumber(DataDB.generateValidDate(0, 0, "MM"));
        dashboardPage.yearNumber(DataDB.generateValidDate(0, 0, "yy"));
        dashboardPage.fullName(DataDB.generateRandomSurname());
        dashboardPage.CVC("");
        dashboardPage.buttonContinue();
        dashboardPage.fieldIsRequired.shouldBe(visible);
        dashboardPage.invalidFormat.shouldBe(empty);
        var actualStatusPayment = DataHelper.getStatusCredit();
        var actualStatusOrder_entityPayment = DataHelper.getStatusOrderPayment();
        String expectedStatus = null;
        String expectedId = null;
        assertAll(() -> assertEquals(expectedStatus, actualStatusPayment),
                () -> assertEquals(expectedId, actualStatusOrder_entityPayment));
    }

    @Test
    @DisplayName("Ввод в поле \"CVC/CVV\" две цифры")
    void cvc2Сredit() {
        dashboardPage.cardNumber(DataDB.getNumberApprovedCard());
        dashboardPage.monthNumber(DataDB.generateValidDate(0, 0, "MM"));
        dashboardPage.yearNumber(DataDB.generateValidDate(0, 0, "yy"));
        dashboardPage.fullName(DataDB.generateRandomSurname());
        dashboardPage.CVC(DataDB.generateShortCVC_CVV());
        dashboardPage.buttonContinue();
        dashboardPage.invalidFormat.shouldBe(visible);
        var actualStatusPayment = DataHelper.getStatusCredit();
        var actualStatusOrder_entityPayment = DataHelper.getStatusOrderPayment();
        String expectedStatus = null;
        String expectedId = null;
        assertAll(() -> assertEquals(expectedStatus, actualStatusPayment),
                () -> assertEquals(expectedId, actualStatusOrder_entityPayment));
    }

    @Test
    @DisplayName("Ввод в поле \"Владелец\" не валидным значением-кириллицей")
    void nameCyrillicСredit() {
        dashboardPage.cardNumber(DataDB.getNumberApprovedCard());
        dashboardPage.monthNumber(DataDB.generateValidDate(0, 0, "MM"));
        dashboardPage.yearNumber(DataDB.generateValidDate(0, 0, "yy"));
        dashboardPage.fullName(DataDB.generateRandomSurnameCyrillic());
        dashboardPage.CVC(DataDB.generateCVC_CVV());
        dashboardPage.buttonContinue();
        dashboardPage.invalidFormat.shouldBe(visible);
        var actualStatusPayment = DataHelper.getStatusCredit();
        var actualStatusOrder_entityPayment = DataHelper.getStatusOrderPayment();
        String expectedStatus = null;
        String expectedId = null;
        assertAll(() -> assertEquals(expectedStatus, actualStatusPayment),
                () -> assertEquals(expectedId, actualStatusOrder_entityPayment));
    }

    @Test
    @DisplayName("Ввод в поле \"Владелец\" не валидным значением-цифрами")
    void nameNumbersСredit() {
        dashboardPage.cardNumber(DataDB.getNumberApprovedCard());
        dashboardPage.monthNumber(DataDB.generateValidDate(0, 0, "MM"));
        dashboardPage.yearNumber(DataDB.generateValidDate(0, 0, "yy"));
        dashboardPage.fullName(DataDB.generateNotValidCard());
        dashboardPage.CVC(DataDB.generateCVC_CVV());
        dashboardPage.buttonContinue();
        dashboardPage.invalidFormat.shouldBe(visible);
        var actualStatusPayment = DataHelper.getStatusCredit();
        var actualStatusOrder_entityPayment = DataHelper.getStatusOrderPayment();
        String expectedStatus = null;
        String expectedId = null;
        assertAll(() -> assertEquals(expectedStatus, actualStatusPayment),
                () -> assertEquals(expectedId, actualStatusOrder_entityPayment));
    }

    @Test
    @DisplayName("Ввод в поле \"Владелец\" не валидным значением-спецсимволами")
    void nameSpacesСredit() {
        dashboardPage.cardNumber(DataDB.getNumberApprovedCard());
        dashboardPage.monthNumber(DataDB.generateValidDate(0, 0, "MM"));
        dashboardPage.yearNumber(DataDB.generateValidDate(0, 0, "yy"));
        dashboardPage.fullName(DataDB.randomSymbol());
        dashboardPage.CVC(DataDB.generateCVC_CVV());
        dashboardPage.buttonContinue();
        dashboardPage.invalidFormat.shouldBe(visible);
        var actualStatusPayment = DataHelper.getStatusCredit();
        var actualStatusOrder_entityPayment = DataHelper.getStatusOrderPayment();
        String expectedStatus = null;
        String expectedId = null;
        assertAll(() -> assertEquals(expectedStatus, actualStatusPayment),
                () -> assertEquals(expectedId, actualStatusOrder_entityPayment));
    }

    @Test
    @DisplayName("Ввод в поле \"Владелец\" не валидным значением-пробелами")
    void nameWithSpacesСredit() {
        dashboardPage.cardNumber(DataDB.getNumberApprovedCard());
        dashboardPage.monthNumber(DataDB.generateValidDate(0, 0, "MM"));
        dashboardPage.yearNumber(DataDB.generateValidDate(0, 0, "yy"));
        dashboardPage.fullName("        ");
        dashboardPage.CVC(DataDB.generateCVC_CVV());
        dashboardPage.buttonContinue();
        dashboardPage.fieldIsRequired.shouldBe(visible);
        var actualStatusPayment = DataHelper.getStatusCredit();
        var actualStatusOrder_entityPayment = DataHelper.getStatusOrderPayment();
        String expectedStatus = null;
        String expectedId = null;
        assertAll(() -> assertEquals(expectedStatus, actualStatusPayment),
                () -> assertEquals(expectedId, actualStatusOrder_entityPayment));
    }
}
