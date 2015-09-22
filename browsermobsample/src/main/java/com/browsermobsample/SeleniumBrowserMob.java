package com.browsermobsample;

import java.net.URL;

import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class SeleniumBrowserMob {
	public static void main(String args[]){
		try{
			BrowserMobProxyServer proxy = new BrowserMobProxyServer();
			proxy.start(0);

			// get the Selenium proxy object
			Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);

			// configure it as a desired capability
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities = DesiredCapabilities.chrome();
			capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);

			// start the browser up
			URL url =new URL("http://atubh3075p.prod.ch3.s.com:4444/wd/hub");
			WebDriver driver = new RemoteWebDriver(url,capabilities);

			// create a new HAR with the label "yahoo.com"
			proxy.newHar("sears.com");

			// open yahoo.com
			driver.get("http://www.sears.com");

			// get the HAR data
			Har har = proxy.getHar();
			driver.quit();
			proxy.stop();
		}catch(Exception e){

		}
	}
}
