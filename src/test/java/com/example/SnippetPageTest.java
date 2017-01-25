package com.example;

import com.example.pages.*;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Title;
import ru.yandex.qatools.allure.model.SeverityLevel;


public class SnippetPageTest extends TestNgTestBase {
    protected ShoppingCartPage shoppage;
    protected HomePage homepage;
    protected SearchFieldPage searchpage;
    protected SnippetPage snippettitle;
    protected ProductCardPage productcard;

    @BeforeMethod(groups = {"smoke","full"})
    public void initPageObjects() {
        homepage = PageFactory.initElements(driver, HomePage.class);
        searchpage = PageFactory.initElements(driver, SearchFieldPage.class);
        snippettitle = PageFactory.initElements(driver, SnippetPage.class);
        productcard = PageFactory.initElements(driver,ProductCardPage.class);
        homepage.open(baseUrl);

    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("In this test we \n 1.use method sendSearchRequest() for sending serch request to ebay.com" +
            "\n 2. check title of page")
    @Test(description = "homepage is loaded",groups = {"full","smoke"})
    public void testHomePageIsLoaded() {
        searchpage.sendSearchRequest(searchpage.search);
        Assert.assertEquals(searchpage.search + " | eBay", searchpage.checkTitle());

    }

    @Severity(SeverityLevel.NORMAL)
    @Title("This test checks, has homapage headers or not")
    @Description("In this test we submit search request to searchfield and compare headers of " +
            "snippet on the search page and cart page")
    @Test(description = "has homepage headers",groups = {"smoke","full"})
    public void testHomePageHasAHeader() {
        searchpage.sendSearchRequest(searchpage.search);
        snippettitle.mapOfHeaders(snippettitle.snippetTitle);
        for (Boolean item : snippettitle.snippetHasHeader(snippettitle.hashmap)) {
            Assert.assertTrue(item);
        }
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("In this test we \n 1.use method sendSearchRequest() for sending serch request to ebay.com" +
            "\n 2. check title of page")
    @Test(description = "homepage is loaded",groups = {"full","smoke"})
    public void testHomePageIsLoadedLogin() {
        homepage.signIn(login,password);
        searchpage.sendSearchRequest(searchpage.search);
        Assert.assertEquals(searchpage.search + " | eBay", searchpage.checkTitle());

    }
    @Severity(SeverityLevel.NORMAL)
    @Title("This test checks, has homapage headers or not")
    @Description("In this test we submit search request to searchfield and compare headers of " +
            "snippet on the search page and cart page")
    @Test(description = "has homepage headers",groups = {"smoke","full"})
    public void testHomePageHasAHeaderLogin() {
        homepage.signIn(login,password);
        searchpage.sendSearchRequest(searchpage.search);
        snippettitle.mapOfHeaders(snippettitle.snippetTitle);
        for (Boolean item : snippettitle.snippetHasHeader(snippettitle.hashmap)) {
            Assert.assertTrue(item);
        }
    }


    /*@DataProvider(name = "data")
    public Object[][] createData() {
        return new Object[][]{
                {false, "vfibyf"},
                {true, "car"},
                {false, "ipad"},
                {true, "wewwe"}
        };
    }
    @Severity(SeverityLevel.MINOR)
    @Title("Check search results with typos")
    @Description("In this test we check how ebay.com reacts on the typos")
    @Test(dataProvider = "data",groups = {"full"})
    public void verifyData(Boolean val, String str) {
        searchpage.sendSearchRequest(str);
        Assert.assertTrue(snippettitle.snippetTitle.size() > 0, "Incorrect search query");
    }*/

    @AfterMethod(alwaysRun = true, groups = {"smoke","full"})
    public void clean(){
        homepage.clearChache();
        driver.close();
    }
}
