package com.browsermobsample;

import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarLog;
import net.lightbody.bmp.core.har.HarNameValuePair;
import net.lightbody.bmp.core.har.HarPage;

import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class SeleniumBrowserMob {
	public static void main(String args[]){
		WebDriver driver = null;
		try{
			System.out.println("In");
			BrowserMobProxyServer proxyServer = new BrowserMobProxyServer();
			proxyServer.start(0);
			System.out.println("start proxy");

			// get the Selenium proxy object
			Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxyServer);

			// configure it as a desired capability
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities = DesiredCapabilities.chrome();
			capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);

			// start the browser up
			URL url =new URL("http://atubh3072p.prod.ch3.s.com:4444/wd/hub");
			driver = new RemoteWebDriver(url,capabilities);
			System.out.println("start driver");

			// create a new HAR with the label "yahoo.com"
			proxyServer.newHar("sears.com");

			// open yahoo.com
			driver.get("http://m.sears.com");
			/*driver.get("http://www.sears.com/crsp/api/cart/v1/item/add?offerId=00850259000&quantity=1");
			Thread.sleep(5000);
			driver.navigate().to("http://www.sears.com/crsp/mx/cart#/cart");
			Thread.sleep(10000);
			driver.findElement(By.xpath("(//div[contains(@class,'go-to-checkout')]//button[contains(.,'Proceed to Checkout')])[1]")).click();*/
			Thread.sleep(3000);
			// get the HAR data
			Har har = proxyServer.getHar();
			readHarFile(har);
			File harFile = new File("traffic1.har");
			har.writeTo(harFile);
			proxyServer.stop();
		}catch(Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}finally{
			if(null!=driver)
				driver.quit();

		}
	}
	public static void readHarFile(Har har){

		try
		{
			// Access all elements as objects
			HarLog log=har.getLog();
			//HarBrowser browser =log.getBrowser();

			// Used for loops
			List<HarPage> pages = log.getPages();
			List<HarEntry> hentry = log.getEntries(); 

			for (HarPage page : pages)
			{
				System.out.println("page start time: "+ page.getStartedDateTime());
				System.out.println("page id: " + page.getId());
				System.out.println("page title: " + page.getTitle());
			}

			//Output "response" code of entries.
			for (HarEntry entry : hentry)
			{
				//System.out.println("request code: " + entry.getRequest().getMethod()); //Output request type
				//System.out.println("response code: " + entry.getRequest().getUrl()); //Output url of request
				if(entry.getRequest().getUrl().contains("som.sears.com")){
					List<HarNameValuePair> list=entry.getRequest().getQueryString();
					for (Iterator iterator = list.iterator(); iterator
							.hasNext();) {
						HarNameValuePair harNameValuePair = (HarNameValuePair) iterator
								.next();
						System.out.println(harNameValuePair.getName()+"-->"+harNameValuePair.getValue());
						
					}

				}
				if(entry.getRequest().getUrl().contains("http://om.sears.com")){
					List<HarNameValuePair> list=entry.getRequest().getQueryString();
					for (Iterator iterator = list.iterator(); iterator
							.hasNext();) {
						HarNameValuePair harNameValuePair = (HarNameValuePair) iterator
								.next();
						System.out.println(harNameValuePair.getName()+"-->"+harNameValuePair.getValue());
						
					}

				}
				
				//System.out.println("response code: " + entry.getResponse().getStatus()); // Output the 
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			//fail("IO exception during test");
		}

	}
}
