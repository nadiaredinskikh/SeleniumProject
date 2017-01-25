package com.example;

import com.example.pages.HomePage;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.model.SeverityLevel;

/**
 * Created by User on 27.11.2016.
 */
public class HomePageTest extends TestNgTestBase {
    protected HomePage homepage;

    //private static final Logger LOG = LoggerFactory.getLogger(HomePage.class);

    @BeforeMethod(groups = {"full", "smoke"})
    public void initPageObjects() {
        homepage = PageFactory.initElements(driver, HomePage.class);
        homepage.open(baseUrl);
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Log in")
    @Test(description = "log in on site", groups = {"smoke", "full"})
    public void testHomePageLogin() {
        Assert.assertEquals("name", homepage.signIn(login, password), "User not logged");


    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Log out")
    @Test(description = "login to site", groups = {"smoke", "full"})
    public void homePageLogOut() {
        homepage.signIn(login, password);
        Assert.assertEquals("Войдите", homepage.logOut(), "User not log out");
    }

    @AfterMethod
    public void clean() {
        homepage.clearChache();
    }
}
