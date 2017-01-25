package com.example.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import ru.yandex.qatools.allure.annotations.Step;


/**
 * Sample page
 */
public class HomePage extends Page {

    @FindBy(how = How.TAG_NAME, using = "h1")
    @CacheLookup
    public WebElement header;

    @FindBy(xpath = ".//span[@id=\"gh-ug\"]//a[starts-with(@href,'https://signin.ebay.com/ws/eBayISAPI.dll?SignIn')]")
    private WebElement signInBtn;
    @FindBy(xpath = ".//div[@id=\"pri_signin\"]//div[4]//span[2]//input")
    private WebElement usernameField;
    @FindBy(xpath = ".//div[@id=\"pri_signin\"]//div[5]//span[2]//input")
    private WebElement passwordField;
    @FindBy(xpath = ".//input[@id=\"sgnBt\"]")
    private WebElement signBtn;
    @FindBy(xpath = ".//a[@id=\"gh-ug\"]")
    private WebElement succesfullogin;
    @FindBy(xpath = ".//a[@id=\"gh-ug\"]//b[1]")
    private WebElement userloginname;
    @FindBy(xpath = ".//iframe[@src='chrome://settings-frame/cookies']")
    private WebElement cleanCache;
    @FindBy(xpath = ".//*[@class='remove-all-cookies-button']")
    private WebElement removeAllCache;
    @FindBy(xpath = ".//a[@href=\"https://signin.ebay.com/ws/eBayISAPI.dll?SignIn&ru=\"]")
    private WebElement signOutName;
    @FindBy(xpath = ".//*[@id=\"clear-browsing-data\"]")
    private WebElement clearBrowsingData;
    @FindBy(xpath = ".//iframe[@src=\"chrome://settings-frame/clearBrowserData\"]")
    private WebElement frameBrowserDataOverlay;
    @FindBy(xpath = ".//button[@id=\"clear-browser-data-commit\"]")
    private WebElement clearHistory;
    @FindBy(xpath = ".//title")
    private WebElement title;

    public String loginname;

    public HomePage(WebDriver webDriver) {
        super(webDriver);
    }

    @Step("Open url")
    public void open(String url) {
        driver.get(url);
    }



    @Step("Sign in")
    public String signIn(String login, String password) {
        signInBtn.click();
        usernameField.sendKeys(login);
        passwordField.sendKeys(password);
        signBtn.click();
        loginname = userloginname.getText();
        return loginname;
    }

    @Step("delete cookie")
    public void clearChache() {
        driver.get("chrome://settings/cookies");
        driver.switchTo().frame(cleanCache);
        removeAllCache.click();
        driver.get("chrome://settings/clearBrowserData");
        driver.switchTo().frame(frameBrowserDataOverlay);
        clearHistory.click();

    }

    @Step("log out")
    public String logOut() {
        driver.get("https://signin.ebay.com/ws/eBayISAPI.dll?SignIn&lgout=1");
        return signOutName.getText();
    }
}
