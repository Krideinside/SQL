package ru.netology.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.pages.LoginPage;

import static com.codeborne.selenide.Selenide.clearBrowserCookies;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.SQLHelper.cleanDatabase;


public class DeadlineTests {

    @AfterAll
    static void cleaner() {
        cleanDatabase();
    }

    @Test
    void successLogin() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authUser = DataHelper.getAuthInfoWithTestData();
        var verifyPage = loginPage.validLogin(authUser);
        verifyPage.verifyPageVisibility();
        var verifyCode = SQLHelper.getVerificationCode();
        verifyPage.validVerify(verifyCode.getCode());
    }

    @Test
    void randomUser() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getRandomAuthInfo();
        loginPage.validLogin(authInfo);
        loginPage.verifyErrorVisibility();
    }

    @Test
    void invalidLogin() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authUser = new DataHelper.AuthInfo(DataHelper.getRandomAuthInfo().getLogin(),
                DataHelper.getAuthInfoWithTestData().getPassword());
        loginPage.validLogin(authUser);
        loginPage.verifyErrorVisibility();
    }

    @Test
    void invalidPass() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authUser = new DataHelper.AuthInfo(DataHelper.getAuthInfoWithTestData().getLogin(),
                DataHelper.getRandomAuthInfo().getPassword());
        loginPage.validLogin(authUser);
        loginPage.verifyErrorVisibility();
    }

    @Test
    void invalidVerifyCode() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authUser = DataHelper.getAuthInfoWithTestData();
        var verifyPage = loginPage.validLogin(authUser);
        verifyPage.verifyPageVisibility();
        var verifyCode = DataHelper.getRandomVerificationCode().getCode();
        verifyPage.verify(verifyCode);
        verifyPage.errorVisibility();
    }
}