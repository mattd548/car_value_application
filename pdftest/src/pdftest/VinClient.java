package pdftest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
// vin client used for connecting to a website using the htmlunit library
// 
public class VinClient {
	 private final WebClient webclient = new WebClient(BrowserVersion.CHROME);
     private HtmlPage page;
     private HtmlForm form;
     private DomElement button;
     private DomElement element;
// constructor automatically sets the environment on call 
public VinClient() {
	setEnvironment();
}


public void setEnvironment() {
	//makes sure to turn javascript on since the website im scraping uses javascript
	//turns warnings off 
	java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 
	
	webclient.getOptions().setJavaScriptEnabled(true);
	
}

public void connectToUrl(String url) {
	
}

public void setPage(HtmlPage page) {
	this.page=page;
	
}
public HtmlPage getPage() {
	
	return page;
	
}

public DomElement getButton() {
	return button;
}

public void setButton(DomElement button) {
	this.button = button;
}

public void Getmodel(Car item) throws FailingHttpStatusCodeException, MalformedURLException, IOException
{
	// find out make of cars based on vin using html unit
    
	
    HtmlPage page = webclient.getPage("https://vpic.nhtsa.dot.gov/decoder/");
    //finds form passed on name and takes it from the page
    HtmlForm form = page.getFirstByXPath("//form[@action='/decoder/Decoder']");
    //System.out.print(page.asText());  //prints out whole page as text
    form.getInputByName("VIN").setValueAttribute(item.getVin());
    DomElement button = page.getFirstByXPath("//input[@type='submit'and @value='Decode VIN']");
   page= button.click();
    try {
		Thread.sleep(3000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
    DomElement element = page.getFirstByXPath("//span[@id='decodedModel']");
    //String model = element.getNodeValue();
    //System.out.print(page.asText()); //prints out whole page as text
    
    
    String model = element.getTextContent();
    // have to not only find the model of the car but make sure its the same as edmunds website since thats where iam going to scrap for the cars value 
    if (model.equals("Caravan/Grand Caravan")) {
    	model="Grand Caravan";
    }
    else if (item.getMake().equals("saturn")&& model.startsWith("L")) {
    	   model="l series";
    }
    else if (item.getMake().equals("saturn")&& model.startsWith("S")) {
    	model="S series";
    }
    else if (item.getMake().equals("ford")&& model.equals("F-250")) {
    	model="F 250 Super Duty";
    }
    else if (item.getMake().equals("ford")&& model.equals("F-350")) {
    	model="F 350 Super Duty";
    }
    else if (item.getMake().equals("ford")&& model.equals("F-450")) {
    	model="F 450 Super Duty";
    }
    else if (item.getMake().equals("bmw")&& model.startsWith("1")) {
    	model="1 series";
    }
    else if (item.getMake().equals("bmw")&& model.startsWith("3")) {
    	model="3 series";
    }
    else if (item.getMake().equals("bmw")&& model.startsWith("5")) {
    	model="5 series";
    }
    else if (item.getMake().equals("bmw")&& model.startsWith("6")) {
    	model="6 series";
    }
    else if (item.getMake().equals("bmw")&& model.startsWith("7")) {
    	model="7 series";
    }
    //replaces every space in a string with - because thats what edmund uses on their website url 
    model=model.replaceAll(" ","-");
    item.setModel(model);
    System.out.println();
    System.out.print(item.getYear()+" "+item.getMake()+" "+item.getModel()+" "+item.getVin());
	
	
	
	
}




}
