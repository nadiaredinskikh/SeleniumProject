package com.example.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.*;

public class SnippetPage extends Page {

    @FindBy(xpath = ".//*[@id='Results']//h3/a")
    public List<WebElement> snippetTitle;
    @FindBy(xpath = ".//h1[@id=\"itemTitle\"]")
    public WebElement header;
    public Map<String, String> hashmap = new LinkedHashMap<String, String>();
    List<Boolean> snippetValues = new ArrayList<Boolean>();

    public SnippetPage(WebDriver driver) {
        super(driver);
    }

    @Step("Create HashMap of snippet headers and links")
    public Map<String, String> mapOfHeaders(List<WebElement> snippetTitle) {
        for (WebElement title : snippetTitle) {
            String h3 = title.getText();
            if (h3.endsWith("...")) {
                int lastIndex = h3.lastIndexOf("...");
                h3 = h3.substring(0, lastIndex);
            }
            if (h3.startsWith("НОВОЕ ОБЪЯВЛЕНИЕ ")) {
                h3 = h3.replace("НОВОЕ ОБЪЯВЛЕНИЕ ", "");
            }
            hashmap.put(h3, title.getAttribute("href"));
        }
        return hashmap;
    }

    @Step("Check matching of header value on the search page and product card page")
    public List<Boolean> snippetHasHeader(Map<String, String> hashmap) {
        //Map.Entry<String, String> entry = hashmap.entrySet().iterator().next();
        //String key = entry.getKey();
        //String link = entry.getValue();
        Iterator it = hashmap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String link = (String) pair.getValue();
            driver.navigate().to(link);
            if (header.getText().toString().contains((String) pair.getKey())) {
                snippetValues.add(true);
            } else {
                snippetValues.add(false);
            }
        }
        return snippetValues;


    }

}
