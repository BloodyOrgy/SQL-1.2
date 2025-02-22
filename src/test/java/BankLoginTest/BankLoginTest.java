package BankLoginTest;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import data.DataHelper;
import data.SQLHelper;
import page.LoginPage;

import static data.SQLHelper.cleanDatabase;
import static com.codeborne.selenide.Selenide.open;
import static data.SQLHelper.getVerificationCode;

public class BankLoginTest {

    @AfterAll
    static void teardown() {
        cleanDatabase();
    }

    @Test
    void shouldSuccessfulLogin() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verifyVerificationPageVisiblity();
        var verificationCode = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode.getCode());
    }

    @Test
    void shouldGetErrorNotificationIfLoginWithRandomUserWithoutAddingToBase() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.generateRandomUser();
        loginPage.validLogin(authInfo);
        loginPage.verifyErrorNotificationVisiblity("Ошибка! \nНеверно указан логин или пароль");
    }

    @Test
    void shouldGetErrorNotificationIfLoginWithExistUserAndRandomVerificationCode() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.generateRandomVerificationCode();
        verificationPage.verify(verificationCode.getCode());
        verificationPage.verifyErrorNotificationVisiblity("Ошибка! \nНеверно указан код! Попробуйте ещё раз.");

    }
}
