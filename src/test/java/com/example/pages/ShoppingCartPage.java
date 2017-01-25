package com.example.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static jdk.nashorn.internal.objects.NativeMath.round;

/**
 * Created by User on 06.11.2016.
 */
public class ShoppingCartPage extends Page{
    @FindBy(xpath = ".//a[@href=\"http://cart.payments.ebay.com/sc/view\"]")
    private WebElement shopCartBtn;
    @FindBy(xpath = ".//div[@id=\"empty-sc\"]")
    private WebElement emptyCart;
    @FindBy(xpath = ".//*[@id=\"ShopCart\"]")
    private WebElement shopCart;
    @FindBy(xpath = ".//div[@id=\"syncTotal\"]//following-sibling::span")
    private WebElement syncTotal;
    @FindBy(xpath = ".//div[@id=\"asyncTotal\"]")
    private WebElement asyncTotal;
    @FindBy(xpath = ".//a[@id=\"contShoppingBtn\"]")
    private WebElement contShoppingBtn;
    @FindBy(xpath = ".//a[@id=\"ptcBtnBottom\"]")
    private WebElement ptcBtnBottom;
    @FindBy(xpath = ".//a[starts-with(@id,'po_seller_')]")
    private List<WebElement> payOnlyThisSeller;
    @FindBy(xpath = ".//*[@id=\"ShopCart\"]//a[text()=\"Удалить\"]")
    private List<WebElement> remove;
    @FindBy(xpath = ".//a[text()=\"Сохранить на будущее\"]")
    private List<WebElement> saveForLater;
    @FindBy(xpath=".//div[@id=\"SFLSection\"]")
    private WebElement allSavedProducts;
    @FindBy(xpath = ".//div[@id=\"SFLSection\"]//a[text()=\"Удалить\"]")
    private List<WebElement> removeFromSaved;
    @FindBy(xpath = ".//div[starts-with(@class,\"fr pb20\")]//div[starts-with(@class,\"fw-b\")]")
    private List<WebElement> productPrices;

    @FindBy(xpath = ".//span[@class=\"g-hdn-ada\"]//parent::div")
    private WebElement shippingCost;

    @FindBy(xpath = ".//a[text()=\"Назад в корзину\"]")
    private List<WebElement> backToCart;
    @FindBy(xpath = ".//div[@id=\"ShopCart\"]//div[@class=\"ff-ds3 fs16 mb5 fw-n sci-itmttl\"]//a")
    private List<WebElement> listOfAllProductsinCart;
    @FindBy(xpath=".//div[@id=\"SFLSection\"]//div[@class=\"ff-ds3 fs16 mb5 fw-n sci-itmttl\"]//a")
    private List<WebElement>  allProductsInSaveForLater;
    @FindBy(xpath = ".//*[@id=\"asynccartsummary\"]//div[@class=\"tr nowrap\"]")
    private WebElement freeShipping;
    @FindBy(xpath = ".//input[starts-with(@id,\"qty_\")]")
    private List<WebElement> boxCountList;

    private double oldSum, newsum,newprice;
    private String id;
    private int i;


    public ShoppingCartPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Method chaecks is empty shop cart. It returns the size of shop cart list items .
     * @return int value
     */
    @Step("Check shopping cart is empty or not")
    public void shoppingCartIsEmpty (){
        shopCartBtn.click();
        emptyCart.isDisplayed();

    }
    @Step("Go to shopcart")
    public void goToShopCart(){
        driver.get("http://cart.payments.ebay.com/sc/view");
    }

    /**
     * Method removed each item from Shop Cart. If all items removed, method return 0
     * @return int value
     */
    @Step("Remove item from shop cart")
    public int cleanCart(){
        goToShopCart();
            for(int i = remove.size()-1; i !=-1 ; i--){
                remove.get(i).click();

            }
        return remove.size();
    }

    /**
     * Method checks, will be user redirected to the search page after clicking button
     * "Continue shopping"
     */
    @Step("Check is user redericted to the search page, after clicking to the button continue shopping")
    public String continueShopping(){
        driver.get(contShoppingBtn.getAttribute("href"));
        return driver.getTitle();
    }
    @Step("Check free shipping or not")
    public boolean checkFreeShipping(){
        goToShopCart();
        if(freeShipping.getText().equals("БЕСПЛАТНО")){
            return true;
        }
        return false;
    }

    /**
     * Method checks total sum for all products in prices. If total sum equals the sum of prices for products in shop cart
     * method return true, otherwise false
     * @return true
     */
    @Step("Compute the sum of prices of all products in shop cart")
    public boolean totalCount(){
        String totalSum = syncTotal.getText().toString().replaceAll("[US $]","").replace(",",".");
        double s = 0;
        for(i = productPrices.size()-1; i != -1; i--){
            s = s + Double.parseDouble(productPrices.get(i).getText().replaceAll("[US $]","").replace(",","."));
        }

        if(round(Double.parseDouble(totalSum),2) == round(s,2)){
            return true;
        }
        return false;

    }
    @Step("Check work button pay only for this product")
    public void payOnlyThisProduct(){
        goToShopCart();
        for(i = payOnlyThisSeller.size()-1; i != -1 ; i--){
            String payLink = payOnlyThisSeller.get(i).getAttribute("href");
            driver.get(payLink);
            Assert.assertTrue(driver.getCurrentUrl().startsWith("https://checkout.ebay.com/"), "Btn pay only this seller not work");
            goToShopCart();
        }
    }
    @Step("Save product for later")
    public void setSaveForLater(){
        goToShopCart();
        for(i = saveForLater.size()-1; i != -1 ; i--){
            String saveLink = saveForLater.get(i).getAttribute("href");
            id = saveForLater.get(i).getAttribute("aria-describedby");
            String headerOfProduct = driver.findElement(By.id(id)).getText();
            driver.get(saveLink);
            driver.manage().timeouts().pageLoadTimeout(5,TimeUnit.SECONDS);
            if(allSavedProducts.isDisplayed()){
                String headerofSavedProduct = allSavedProducts.findElement(By.id(id)).getText();
                Assert.assertEquals(headerOfProduct,headerofSavedProduct, "Product not added to save for later block");
            }
        }
    }
    @Step("Back product to shop cart, that was added to save for later ")
    public void setBackToCart(){
        goToShopCart();
        boolean shopcartvivsible = true;
        for (i = backToCart.size()-1; i !=-1 ; i--){
            String backId = backToCart.get(i).getAttribute("aria-describedby");
            String titleOfProduct = driver.findElement(By.id(backId)).getText();
            backToCart.get(i).click();
            try{
                shopcartvivsible = true;
                if(shopCart.isDisplayed()){
                    String titleOfBackProducts = shopCart.findElement(By.id(backId)).getText();
                    Assert.assertEquals(titleOfProduct,titleOfBackProducts, "Product not added back to shop cart");
                }
            }catch (ElementNotVisibleException e){
                shopcartvivsible = false;
            }

        }

    }
    @Step("Clean save for later block")
    public int cleanSaveForLater(){
        goToShopCart();
            for(i = removeFromSaved.size()-1; i != -1 ; i--){
                removeFromSaved.get(i).click();

            }

        return removeFromSaved.size();
    }


    @Step("Check sum changing after clicking on save for later button")
    public void changingTotalSumAfterRemove(){
        goToShopCart();
        String titleId;
        if(listOfAllProductsinCart.size() != 0){
            for(i = listOfAllProductsinCart.size()-1; i != -1; i--){
                oldSum = Double.parseDouble(syncTotal.getText().replaceAll("[US $]","").replace(",","."));
                if(listOfAllProductsinCart.get(i).getAttribute("id").equals("")){
                    id = listOfAllProductsinCart.get(i).getAttribute("href").replace("http://www.ebay.com/itm/","")+"_title";
                    titleId = listOfAllProductsinCart.get(i).getAttribute("href").replace("http://www.ebay.com/itm/","");
                }else{
                    id = listOfAllProductsinCart.get(i).getAttribute("id");
                    titleId = listOfAllProductsinCart.get(i).getAttribute("id").replace("_title","");
                }
                WebElement price = driver.findElement(By.xpath(".//div[@data-itemid=\""+titleId+"\"]//div[starts-with(@class,\"fw-b\")]"));
                newprice = Double.parseDouble(price.getText().replace("US $","").replace(",",".").replace(" ",""));
                WebElement removeId = driver.findElement(By.xpath(".//div[@id=\"ShopCart\"]//a[1][@aria-describedby=\""+id+"\"]"));
                removeId.click();
                newsum = Double.parseDouble(syncTotal.getText().replaceAll("[US $]","").replace(",","."));
                Assert.assertEquals(round(newsum,2),round(oldSum - newprice,2));
            }
        }

    }

    public void changingTotalSumAfterSaveForLater(){
        goToShopCart();
        String titleId;
        WebElement saveForLaterTitle;
        if(listOfAllProductsinCart.size() != 0){
            for(i = listOfAllProductsinCart.size()-1; i != -1; i--){
                oldSum = Double.parseDouble(syncTotal.getText().replaceAll("[US $]","").replace(",","."));
                if(listOfAllProductsinCart.get(i).getAttribute("id").equals("")){
                    id = listOfAllProductsinCart.get(i).getAttribute("href").replace("http://www.ebay.com/itm/","")+"_title";
                    titleId = listOfAllProductsinCart.get(i).getAttribute("href").replace("http://www.ebay.com/itm/","");
                }else{
                    id = listOfAllProductsinCart.get(i).getAttribute("id");
                    titleId = listOfAllProductsinCart.get(i).getAttribute("id").replace("_title","");
                }
                String title = listOfAllProductsinCart.get(i).getText();
                WebElement saveForLaterId = driver.findElement(By.xpath(".//*[@aria-describedby=\""+id+"\"]//following-sibling::a"));

                WebElement price = driver.findElement(By.xpath(".//div[@data-itemid=\""+titleId+"\"]//div[starts-with(@class,\"fw-b\")]"));
                newprice = Double.parseDouble(price.getText().replaceAll("[US $]","").replace(",","."));
                saveForLaterId.click();
                try{
                    saveForLaterTitle = driver.findElement(By.xpath(".//*[@id=\"SFLSection\"]//a[@id=\""+id+"\"]"));
                }catch(NoSuchElementException e){
                    saveForLaterTitle = driver.findElement(By.xpath(".//*[@id=\"SFLSection\"]//span[@id=\""+id+"\"]"));
                }

                if(title.equals(saveForLaterTitle.getText())){
                    newsum = Double.parseDouble(syncTotal.getText().replaceAll("[US $]","").replace(",","."));
                    Assert.assertEquals(round(newsum,2),round(oldSum - newprice,2));
                }



            }
        }


    }
    @Step("Check changing sum after clicking on back to cart btn")
    public void changingTotalSumAfterBackToCart(){
        goToShopCart();
        WebElement backedElmTitle;
        if(allProductsInSaveForLater.size() != 0){
            for(i = allProductsInSaveForLater.size()-1; i != -1; i--){
                oldSum = Double.parseDouble(syncTotal.getText().replaceAll("[US $]","").replace(",","."));
                String title = allProductsInSaveForLater.get(i).getText();
                if(allProductsInSaveForLater.get(i).getAttribute("id").equals("")){
                    id = allProductsInSaveForLater.get(i).getAttribute("href").replace("http://www.ebay.com/itm/","").replace("_title","");
                }else{
                    id = allProductsInSaveForLater.get(i).getAttribute("id").replace("_title","");
                }

                WebElement price = driver.findElement(By.xpath(".//div[@data-itemid=\""+id+"\"]//div[starts-with(@class,\"fw-b\")]"));
                newprice = Double.parseDouble(price.getText().replaceAll("[US $]","").replace(",","."));
                WebElement backToCart = driver.findElement(By.xpath(".//*[@id=\"SFLSection\"]//a[@aria-describedby=\""+id+"_title\"]//following-sibling::a"));
                backToCart.click();
                try{
                    backedElmTitle= driver.findElement(By.xpath(".//*[@id=\"ShopCart\"]//a[@id=\""+id+"_title\"]"));
                }catch(NoSuchElementException e){
                    backedElmTitle= driver.findElement(By.xpath(".//*[@id=\"ShopCart\"]//span[@id=\""+id+"_title\"]//a"));
                }

                if(title.equals(backedElmTitle.getText())){
                    newsum = Double.parseDouble(syncTotal.getText().replaceAll("[US $]","").replace(",","."));
                    Assert.assertEquals(round(newsum,2),round(oldSum + newprice,2));
                }

            }
        }
    }

    public void changingSumAfterBoxCountChanging(){
        goToShopCart();
        String count = "2";
        for (i = boxCountList.size()-1; i != -1; i--){
            id = boxCountList.get(i).getAttribute("id").replace("qty_","");
            WebElement oldPrice = driver.findElement(By.xpath(".//div[@id=\""+id+"\"]//div[starts-with(@class,\"fw-b\")]"));
            double oldprice = Double.parseDouble(oldPrice.getText().replaceAll("[US $]","").replace(",","."));
            boxCountList.get(i).sendKeys(Keys.chord(Keys.CONTROL, "a"), count);
            driver.findElement(By.xpath(".//a[@id=\"ul_" + id + "\"]")).click();
            WebElement newPrice = driver.findElement(By.xpath(".//div[@id=\""+id+"\"]//div[starts-with(@class,\"fw-b\")]"));
            double newprice = Double.parseDouble(newPrice.getText().replaceAll("[US $]","").replace(",","."));
            Assert.assertEquals(round(oldprice*Double.parseDouble(count),2),round(newprice,2));
        }
    }

    public void changingTotalSumAfterCountChanging(){
        goToShopCart();
        String count = "2";
        for (i = boxCountList.size()-1; i != -1; i--){
            id = boxCountList.get(i).getAttribute("id").replace("qty_","");
            oldSum = Double.parseDouble(syncTotal.getText().replaceAll("[US $]","").replace(",","."));
            WebElement oldPrice = driver.findElement(By.xpath(".//div[@id=\""+id+"\"]//div[starts-with(@class,\"fw-b\")]"));
            double oldprice = Double.parseDouble(oldPrice.getText().replaceAll("[US $]","").replace(",","."));
            boxCountList.get(i).sendKeys(Keys.chord(Keys.CONTROL, "a"), count);
            driver.findElement(By.xpath(".//a[@id=\"ul_" + id + "\"]")).click();
            WebElement newPrice = driver.findElement(By.xpath(".//div[@id=\""+id+"\"]//div[starts-with(@class,\"fw-b\")]"));
            double newprice = Double.parseDouble(newPrice.getText().replaceAll("[US $]","").replace(",","."));
            newsum = Double.parseDouble(syncTotal.getText().replaceAll("[US $]","").replace(",","."));
            Assert.assertEquals(round(newsum,2),round(oldSum+(newprice - oldprice),2));
        }

    }




}
