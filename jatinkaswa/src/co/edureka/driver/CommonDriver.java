package co.edureka.driver;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CommonDriver {

	public static WebDriver oDriver;
	private long lngPageLoadTimeOut;
	private long lngElementDetectionTimeOut;
	String firstChildWindow;

	public CommonDriver() {
		lngPageLoadTimeOut = 60L;
		lngElementDetectionTimeOut = 30L;
	}

	public void setPageLoadTimeOut(long lngPageLoadTimeOut) {
		this.lngPageLoadTimeOut = lngPageLoadTimeOut;
	}

	public void setElementDetectionTimeOut(long lngElementDetectionTimeOut) {
		this.lngElementDetectionTimeOut = lngElementDetectionTimeOut;
	}

	public void openBrowser(String sBrowserType, String sUrl) {
		try {

			switch (getBrowserTypeIndexed(sBrowserType)) {
			case 1:
				System.setProperty("webdriver.gecko.driver", "//Users/kaswaj02//Downloads//geckodriver");
				oDriver = new FirefoxDriver();
				break;
			case 2:
				// This is not verified as I am not a windows user.
				System.setProperty("webdriver.ie.driver", "//Users/kaswaj02//Downloads//IEDriverServer.exe");
				oDriver = new InternetExplorerDriver();
				break;
			case 3:
				System.setProperty("webdriver.chrome.driver", "//Users/kaswaj02//Downloads//chromedriver");
				oDriver = new ChromeDriver();
				break;
			default:
				throw new Exception("Unknow Browser Type = " + sBrowserType);
			}

			if (sUrl.isEmpty()) {

				sUrl = "about:blank";
			}
			oDriver.manage().window().maximize();
			oDriver.manage().deleteAllCookies();
			oDriver.manage().timeouts().pageLoadTimeout(lngPageLoadTimeOut, TimeUnit.SECONDS);
			oDriver.manage().timeouts().implicitlyWait(lngElementDetectionTimeOut, TimeUnit.SECONDS);
			oDriver.get(sUrl);
			Thread.sleep(2000);

		} catch (Throwable t) {
			t.getMessage();
		}
	}

	public void closeAllBrowser() {
		try {
			if (oDriver != null) {
				oDriver.quit();
			}

		} catch (Throwable t) {
			t.getMessage();
		}
	}

	public WebDriver getDriver() {
		return oDriver;
	}

	private int getBrowserTypeIndexed(String sBrowserType) {
		sBrowserType = sBrowserType.toLowerCase().trim();

		if (sBrowserType.isEmpty()) {
			return 1;
		}
		if (sBrowserType.equals("ff") || sBrowserType.equals("firefox") || sBrowserType.equals("mozilla")
				|| sBrowserType.equals("mozilla firefox")) {
			return 1;
		}
		if (sBrowserType.equals("ie") || sBrowserType.equals("explorer") || sBrowserType.equals("internet explorer")) {
			return 2;
		}
		if (sBrowserType.equals("chrome") || sBrowserType.equals("google") || sBrowserType.equals("google chrome")) {
			return 3;
		}
		return 1;
	}

	public void waitTillElementIsVisible(By oBy, long timeoutSeconds) {
		try {
			WebDriverWait oWait = new WebDriverWait(oDriver, timeoutSeconds);
			oWait.until(ExpectedConditions.visibilityOfElementLocated(oBy));
		} catch (Throwable t) {
			t.getMessage();
		}
	}

	public void scrollTo(By oBy) {
		JavascriptExecutor jse;
		jse = (JavascriptExecutor) oDriver;
		int xPoint = oDriver.findElement(oBy).getLocation().x;
		int yPoint = oDriver.findElement(oBy).getLocation().y;
		String command = String.format("window.scrollTo(%d, %d)", xPoint, yPoint);
		jse.executeScript(command);
	}

	public void takeScreenshot() throws Exception {
		try {
			// Convert web driver object to TakeScreenshot
			TakesScreenshot scrShot = ((TakesScreenshot) oDriver);
			// Call getScreenshotAs method to create image file
			File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);

			String img = (new SimpleDateFormat("ddmmyyyhhss")).format(new Date());
			String filepath = System.getProperty("user.dir") + "//Users//kaswaj02//screenshot" + img + ".jpg";
			System.out.println("Filepath: " + filepath);

			File DestFile = new File(filepath.trim());
			
			FileUtils.copyFile(SrcFile, DestFile);
		} catch (Exception e) {
			throw new Exception("Unable to take screenshot....");
		}
	}
}