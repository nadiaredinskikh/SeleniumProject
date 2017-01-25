package com.example.pages;

import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by User on 06.11.2016.
 */
public class ProductCardPage extends Page {

    @FindBy(xpath = ".//h1[@id=\"itemTitle\"]")
    private WebElement cardHeader;
    @FindBy(xpath = ".//a[@id=\"isCartBtn_btn\"]")
    private WebElement addToCartBtn;
    @FindBy(xpath = ".//*[@id=\"prcIsum\"]")
    private WebElement productPrice;
    @FindBy(xpath = ".//select[@id=\"msku-sel-1\"]")
    private WebElement color;
    @FindBy(xpath = "./*//*[@id=\"msku-sel-2\"]")
    private WebElement storageCapacity;
    @FindBy(xpath = "./*//*[@id=\"msku-sel-3\"]")
    private WebElement model;
    @FindBy(xpath = ".//div[@id=\"ShopCart\"]//div[@class=\"ff-ds3 fs16 mb5 fw-n sci-itmttl\"]//a")
    private List<WebElement> cartCapacity;
    @FindBy(xpath = ".//*[@id=\"atcLnk\"]//a")
    private WebElement productYetAdded;
    @FindBy(xpath = ".//input[@id=\"qtyTextBox\"]")
    private WebElement countBox;
    @FindBy(xpath = ".//div[@id=\"syncTotal\"]//following-sibling::span")
    private WebElement syncTotal;

    public ProductCardPage(WebDriver driver) {
        super(driver);
    }


    /**
     * Method get current url and check starts this method from "http://cart.payments.ebay.com"
     *
     * @return true if current url starts with "http://cart.payments.ebay.com"
     */
    public boolean getCurrUrl() {
        return driver.getCurrentUrl().startsWith("http://cart.payments.ebay.com");
    }

    /**
     * Method checks, has product card page "Add to Cart" button
     *
     * @return boolean
     */
    private boolean аddToCarBtn() {
        boolean addBtnexists = true;
        try {
            addToCartBtn.click();
        } catch (NoSuchElementException e) {
            addBtnexists = false;
        } catch(ElementNotVisibleException e){
            addBtnexists = false;
        }
        return addBtnexists;
    }

    /**
     * Method checks, is field for color choosing displayed on product card page
     *
     * @return boolean
     */
    private boolean colorDisplayed() {
        boolean colorExists = true;
        try {
            color.click();
            System.out.println(color);
            Select dropdownColor = new Select(color);
            List<WebElement> colorOptions = dropdownColor.getOptions();
            for (WebElement coloroption : colorOptions) {
                if (coloroption.getAttribute("disabled") == null) {
                    if (Integer.valueOf(coloroption.getAttribute("value")) != -1) {
                        coloroption.click();
                        break;
                    }
                }
            }
        } catch (NoSuchElementException e) {
            colorExists = false;
        }
        return colorExists;
    }

    /**
     * Method checks, is field for model choosing displayed on product card page
     *
     * @return boolean
     */
    private boolean modelDisplayed() {
        boolean modelExists = true;
        try {
            model.click();
            Select dropdownModel = new Select(model);
            List<WebElement> modelOptions = dropdownModel.getOptions();
            for (WebElement modeloption : modelOptions) {
                if (modeloption.getAttribute("disabled") == null) {
                    if (Integer.valueOf(modeloption.getAttribute("value")) != -1) {
                        modeloption.click();
                        break;
                    }
                }
            }
        } catch (org.openqa.selenium.NoSuchElementException e) {
            modelExists = false;
        }
        return modelExists;

    }

    /**
     * Method checks, is field for storage capacity choosing displayed on product card page
     *
     * @return boolean
     */
    private boolean storageDisplayed() {
        boolean storageExists = true;
        try {
            storageCapacity.click();
            Select dropdownCapacity = new Select(storageCapacity);
            List<WebElement> storageOptions = dropdownCapacity.getOptions();
            for (WebElement storageoption : storageOptions) {
                if (storageoption.getAttribute("disabled") == null) {
                    if (Integer.valueOf(storageoption.getAttribute("value")) != -1) {
                        storageoption.click();
                        break;
                    }
                }
            }
        } catch (NoSuchElementException e) {
            storageExists = false;
        }
        return storageExists;
    }

    private boolean isCountBox() {
        boolean existsCountBox = true;
        try {
            countBox.isDisplayed();
            existsCountBox = existsCountBox;
        } catch (NoSuchElementException e) {
            existsCountBox = false;
        }
        return existsCountBox;
    }

    @Step("Add product to cart")
    public void addToCart(Map<String, String> hashmap) {
        Iterator it = hashmap.entrySet().iterator();
        while (it.hasNext()) {
            boolean flag = true;
            Map.Entry pair = (Map.Entry) it.next();
            driver.navigate().to((String) pair.getValue());
            try {
                if (productYetAdded.getText().equals("вашу корзину")) {
                    flag = flag;
                    continue;
                }
            } catch (NoSuchElementException e) {
                flag = false;
            }

            if (isCountBox() == true) {
                colorDisplayed();
                modelDisplayed();
                storageDisplayed();
                if(аddToCarBtn() == true){
                    getCurrUrl();
                    if (cartCapacity.size() > 2 || cartCapacity.size() == 2) {
                        break;
                    }
                }else{
                    continue;
                }

            } else {
                continue;
            }



        }
    }



}
