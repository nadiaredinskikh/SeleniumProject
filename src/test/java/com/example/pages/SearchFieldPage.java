package com.example.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.allure.annotations.Step;


public class SearchFieldPage extends Page {
    public static String search = "iphone 7";
    @FindBy(xpath = ".//input[@name=\"_nkw\"]")
    private WebElement searchfield;
    @FindBy(xpath = ".//input[@id=\"gh-btn\"]")
    private WebElement searchbtn;
    @FindBy(xpath = ".//div[@class=\"pnl-b frmt\"]/a[2]")
    private WebElement buyItNow;



    public SearchFieldPage(WebDriver driver) {
        super(driver);
    }



    @Step("Open main page and send search request")
    public void sendSearchRequest(String search) {
        searchfield.sendKeys(search);
        searchbtn.click();
        driver.navigate().to("http://www.ebay.com/sch/i.html?_from=R40&_sacat=0&LH_BIN=1&_nkw="+search+"&rt=nc&LH_FS=1");
        makeScreenshot();
    }

    @Step("Check title of search page results")
    public String checkTitle() {
        return driver.getTitle();

    }
}
