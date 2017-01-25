package com.example;

import com.example.pages.*;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.*;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Title;
import ru.yandex.qatools.allure.model.SeverityLevel;

import java.util.concurrent.TimeUnit;

/**
 * Created by User on 27.11.2016.
 */
public class ShopPageLogin extends TestNgTestBase {
    protected ShoppingCartPage shoppage;
    protected HomePage homepage;
    protected SearchFieldPage searchpage;
    protected SnippetPage snippettitle;
    protected ProductCardPage productcard;


    @BeforeMethod(groups = {"smoke","full"})
    public void initPageObjects() {
        shoppage = PageFactory.initElements(driver, ShoppingCartPage.class);
        homepage = PageFactory.initElements(driver, HomePage.class);
        searchpage = PageFactory.initElements(driver, SearchFieldPage.class);
        snippettitle = PageFactory.initElements(driver, SnippetPage.class);
        productcard = PageFactory.initElements(driver,ProductCardPage.class);
        homepage.open(baseUrl);
        homepage.signIn(login,password);
        searchpage.sendSearchRequest(searchpage.search);
        snippettitle.mapOfHeaders(snippettitle.snippetTitle);
        productcard.addToCart(snippettitle.hashmap);

    }


    @Severity(SeverityLevel.CRITICAL)
    @Title("Check has product added to cart")
    @Description("In this test is checked has product card add to cart or not")
    @Test(groups = {"smoke","full"})
    public void testAddToCart() {
        Assert.assertTrue(productcard.getCurrUrl(),"Product not added to cart");
    }


    @Severity(SeverityLevel.NORMAL)
    @Description("Test send search request and get hashmap of shippet titles. Then it iterate through " +
            "items and add it to shop cart. After adding to shop cart each item is removed from cart")
    @Test(description = "Checks removing items from Shop Cart",groups = {"full","smoke"})
    public void testCleanCart(){
        Assert.assertEquals(0,shoppage.cleanCart(),"Items not removed from shop cart");
    }


    @Severity(SeverityLevel.NORMAL)
    @Description("Test checks, redirected user to the page continue shopping after clicking to the btn continue shoppping or not")
    @Test(description = "Checks redirecting to the search page",groups = {"full"})
    public void testContinueShopping(){
        Assert.assertEquals(searchpage.search + " | eBay",shoppage.continueShopping(),"After clicking to the button continue" +
                "shopping the user isn't redirected to the page with search request");

    }


    @Severity(SeverityLevel.CRITICAL)
    @Description("Test checks is shipping free or not")
    @Test(description = "check free shipping", groups = {"full","smoke"})
    public void testFreeShipping(){
        Assert.assertTrue(shoppage.checkFreeShipping(), "Shipping is not free");
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("In test the sum of prices all products in shop cart is computing and then given sum compares" +
            "with total sum on the shop cart page")
    @Test(description = "Checks the total price for all products in cart shop",groups = {"smoke","full"})
    public void testTotalCount(){
        Assert.assertTrue(shoppage.totalCount(),"The total sum not equals the sum of product price");
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("Check saved product for later or not")
    @Test(groups = {"smoke","full"})
    public void testSaveForLater(){
        shoppage.setSaveForLater();
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("Check back product to cart or not")
    @Test(groups = {"smoke","full"})
    public void testBackToCart(){
        shoppage.setSaveForLater();
        shoppage.setBackToCart();
    }


    @Severity(SeverityLevel.CRITICAL)
    @Description("Test changing sum after clicking on the button save for later")
    @Test(groups = {"smoke","full"})
    public void checkSumAfterSaveForLater(){
        shoppage.changingTotalSumAfterSaveForLater();

    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Test changing sum after clicking on the button remove")
    @Test(groups = {"smoke","full"})
    public void checkSumAfterRemove(){
        shoppage.changingTotalSumAfterRemove();

    }


    @Severity(SeverityLevel.CRITICAL)
    @Description("Test changing sum after clicking on the button back to shop cart")
    @Test(groups = {"smoke","full"})
    public void checkSumBackToCart(){
        shoppage.setSaveForLater();
        shoppage.changingTotalSumAfterBackToCart();
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("Changing sum after count product cards changing")
    @Test(groups = {"smoke","full"})
    public void testSumAfterBoxCountChanging(){
        shoppage.changingSumAfterBoxCountChanging();
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("Changing total after box count changing")
    @Test(groups = {"smoke","full"})
    public  void testTotalSumAfterBoxCountChanging(){
        shoppage.changingTotalSumAfterCountChanging();
    }

    @AfterMethod(alwaysRun = true, groups = {"smoke","full"})
    public void clean(){
        shoppage.cleanCart();
        shoppage.cleanSaveForLater();
        homepage.clearChache();
        driver.close();
    }

}
