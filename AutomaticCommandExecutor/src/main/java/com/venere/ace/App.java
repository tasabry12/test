package com.venere.ace;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) throws FileNotFoundException, IOException, Exception {
        ClassPathXmlApplicationContext oClass = new ClassPathXmlApplicationContext(new String[]{"spring/spring-config.xml"});
         WebDriver oWebDriver =(WebDriver) oClass.getBean("firefoxBrowser");

//        WebDriver oWebDriver = new FirefoxDriver();

        try {
            // open yahoo.com
            oWebDriver.get("http://www.venere.com/italy/rome/");
            Thread.sleep(3000);
            oWebDriver.findElement(By.id("checkin")).sendKeys("30/08/2013");
            oWebDriver.findElement(By.id("checkout")).sendKeys("31/08/2013");
            oWebDriver.findElement(By.id("find")).click();
            Thread.sleep(7000);

            oWebDriver.quit();

        } catch (RuntimeException ex) {
            System.out.println("------------->>>> " + ex.getMessage());
            oWebDriver.quit();

        }

    }
}
