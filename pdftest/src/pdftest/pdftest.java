package pdftest;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.Collections;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

import net.sourceforge.htmlunit.corejs.javascript.ast.FunctionNode.Form;
public class pdftest {
//programmer: Matthew Delaguila
/*Description: a program i developed to take the data from the pdfs of nyc car auctions and find out more information 
	           on the cars value and model using a webscraper. It extracts all data from a pdf saves it as a string and stores each car's data in 
	           an array of car objects. In order to extract the text from the pdf i used the pdfbox java library. Then i used 
	           the htmlunit library in order to scrap and submit the cars year and vin to the national highway traffic administration 
	           website so i can scrap the model and more information from that website. Once it has the model it uses that information 
	           in order to submit it to edmunds website using the jsoup library to scrape the results which then gives you the final 
	           value of the car based on condition and whether a private party is selling it.Currently it only works on staten island 
	           car auctions because of the formatting of the pdf documents.But it can be easily modified based on a pdf's formatting
	           This program is highly dependent on both the national highway traffic administration and edmunds websites.
	           */
	           
	           
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
          int car_number;
       
          String year;
          String make;
          String liplate;
          String state;
          String vin;
          String lein;
          ArrayList<String>prices = new ArrayList<String>();
          VinClient client= new VinClient();
          ArrayList<Double>car_prices = new ArrayList<Double>();
          ArrayList<Car> cars = new ArrayList <Car>();
          
          String[] condition = {"Outstanding","Clean      ","Average    ","Rough      "};
          String [] carvariables;
          //where you put name of pdf file 
	      File file = new File("C:/pdf_examples/auction.pdf");
	      PDDocument document2 = PDDocument.load(file);
	     //Instantiate PDFTextStripper class
	      PDFTextStripper pdfStripper = new PDFTextStripper();

	      //Retrieving text from PDF document
	      String text = pdfStripper.getText(document2);
	     
	      //System.out.println(text);//prints all text from pdf
	      
	      // split text into 2 strings 1 for car info the other for general info 
	      //this is based on the # that starts off the car list in the staten island auction 
	      String [] arrofstring = text.split("#",2);
	      String info= arrofstring[0];
	      String carinfo= arrofstring[1];
	      //System.out.print(arrofstring[1]);
	      // separate all cars line by line 
	      String lines[] = carinfo.split("\\r?\\n",100);
	      //skip the first 2 lines since its heading and an empty line
	      System.out.println("The size of lines is "+lines.length);
	      for (int i = 17; i < lines.length -3; i++) 
	      {
	    	  //System.out.println(lines[i]);
	    	  carvariables=lines[i].split("\\s+");
	    	  // if car number is 10 or aboves theres no space between the first number in the pdf document
	    	  // 
	    	  if (i>=11) {
	    		  year=carvariables[1];
		    	  make=carvariables[2];
		    	  //makes all lowercase in order to search on website
		    	  make = make.toLowerCase();
		    	  liplate=carvariables[3];
		    	  state= carvariables[4];
	    		  vin = carvariables[5];
	    	  }
	    	  else {
	    	  year=carvariables[2];
	    	  make=carvariables[3];
	    	  
	    	  make = make.toLowerCase();
	    	  liplate=carvariables[4];
	    	  state= carvariables[5];
	    	  vin = carvariables[6];
	    	  }
	    	
	    	 
	    	  cars.add(new Car(year,make,liplate,state,vin));
	    	  //System.out.println("array length is "+carvariables.length);
	    	  
	    	  
	      }
	      System.out.println("Finding all models of vehicles in Pdf");
	      for (Car item:cars) {
	    	 
	    	  client.Getmodel(item);
	      }
	     
	      try {
    	  //waits 6 seconds before each connection 
    	  //sets useragent to broweser 
	      System.out.println("Finding all car's value");
    	  for (Car item:cars) {
	    	  
	      if(item.getCar_year()>=1990) {
		Thread.sleep(6000);
		Document document = Jsoup.connect("https://www.edmunds.com/"+item.getMake()+"/"+item.getModel()+"/"+item.getYear()+"/appraisal-value/?expressPath")
	    		  .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36")
	    		  .timeout(10 * 1000)
	    	      .referrer("http://www.google.com")
	    		  .get();
		
		//String title = document.title(); //Get title
		//System.out.println("  Title: " + title); //Print title.
		  System.out.println();
		 
		  System.out.println();
	      System.out.println(item.getYear()+" "+item.getMake()+" "+item.getModel()+" "+item.getPlate()+" "+item.getState()+" "+item.getVin());
		//Elements price = document.select(".result-price:contains($)"); //Get price
				Element table = document.body().getElementsByTag("table").first();

		Element thead = table.getElementsByTag("thead").first();

		StringBuilder headBuilder = new StringBuilder();

		for (Element th : thead.getElementsByTag("th")) {
			
		    headBuilder.append(th.text());
		    headBuilder.append("    ");
		}
         
		System.out.println(headBuilder.toString());
		
		
		Element tbody = table.getElementsByTag("tbody").first();
        
		for (Element tr : tbody.getElementsByTag("tr")) {
			
		    StringBuilder rowBuilder = new StringBuilder();
            
		    for (Element td : tr.getElementsByTag("td")) {
		    	
		        rowBuilder.append(td.text());
		        rowBuilder.append(" ");
		    }
		    
		    System.out.println(condition[tr.elementSiblingIndex()]+"     "+rowBuilder.toString());
		   
		}
	      }// end of if statement only if car year is geater than 1990 can we get its value from edmunds
		
		
    	  }// end of every car loop
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	      
	      //Closing the document
	      document2.close();
	    
	      // Below is an unfinished cragislist web scraper that finds all prices of a car on craigslist then removes outliers and gets average   
	      /*try {
	    	  //waits 9 seconds before each connection 
	    	  //sets useragent to broweser 
	    	  for (Car item:cars) {
		    	  
		      
			Thread.sleep(9000);
			Document document = Jsoup.connect("https://newyork.craigslist.org/d/cars-trucks/search/cto?query="+item.getYear()+"+"+item.getMake()+"+"+item.getModel())
		    		  .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36")
		    		  .timeout(10 * 1000)
		    	      .referrer("http://www.google.com")
		    		  .get();
			
			String title = document.title(); //Get title
			System.out.println("  Title: " + title); //Print title.
			Elements price = document.select(".result-price:contains($)"); //Get price
			//cragislist has 2 prices for every object in row which y we increase i by 2
			//converts elements into string arraylist to get the prices 
			for (int i=0; i < price.size(); i+=2) {
			    prices.add(price.get(i).text());
				System.out.println(price.get(i).text());
				System.out.println(prices.get(i));
				}
			//removes duplicates in string arraylist prices so it can get a more accurate average price
			//prices=removeDuplicates(prices);
			System.out.println("this is in prices string array");
			//creates a double arraylist so we can remove outliers and get the average price
			for (String t:prices) {
				
				  t = t.replace("$",""); 
			      System.out.println(t);
			      car_prices.add(Double.parseDouble(t)); 
	    	     }
			
			//getOutliers(car_prices);
			for (double d:car_prices){
			System.out.println(d);
			}
			
			System.out.println(item.getYear()+"  "+item.getMake()+" "+item.getModel()+"  "+item.getPlate()+"  "+item.getState());
			
			
			
			
	    	  }// end of every car loop
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}// main end braket
	      
	      
	    /*  public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list) 
	      { 
	    
	          // Create a new ArrayList 
	          ArrayList<T> newList = new ArrayList<T>(); 
	    
	          // Traverse through the first list 
	          for (T element : list) { 
	    
	              // If this element is not present in newList 
	              // then add it 
	              if (!newList.contains(element)) { 
	    
	                  newList.add(element); 
	              } 
	          } 
	          return newList; 
	      } */
	      

	      
	    /*  public static List<Double> getOutliers(List<Double> input) {
	          List<Double> output = new ArrayList<Double>();
	          List<Double> data1 = new ArrayList<Double>();
	          List<Double> data2 = new ArrayList<Double>();
	          if (input.size() % 2 == 0) {
	              data1 = input.subList(0, input.size() / 2);
	              data2 = input.subList(input.size() / 2, input.size());
	          } else {
	              data1 = input.subList(0, input.size() / 2);
	              data2 = input.subList(input.size() / 2 + 1, input.size());
	          }
	          double q1 = getMedian(data1);
	          double q3 = getMedian(data2);
	          double iqr = q3 - q1;
	          double lowerFence = q1 - 1.5 * iqr;
	          double upperFence = q3 + 1.5 * iqr;
	          for (int i = 0; i < input.size(); i++) {
	              if (input.get(i) < lowerFence || input.get(i) > upperFence)
	                  output.add(input.get(i));
	                  input.remove(i);
	          }
	          return output;
	      }*/
	
	      
	      
	      
	      /*private static double getMedian(List<Double> data) {
	          if (data.size() % 2 == 0)
	              return (data.get(data.size() / 2) + data.get(data.size() / 2 - 1)) / 2;
	          else
	              return data.get(data.size() / 2);
	      }
	      
	      public static double getAverage(List<Double>car_prices) {
	    	  double average = 0;
	    	  double sum = 0;
	    	  if(!car_prices.isEmpty()) {
	    		    for (double mark : car_prices) {
	    		        sum += mark;
	    		    }
	    		    average = sum/car_prices.size();
	    	  }
	    	  return average;
	      }*/
			
	      
	
	

}// end of class




/*output

The size of lines is 51
Finding all models of vehicles in Pdf

2004 jeep Grand-Cherokee 1J4GW48S74C229817
2005 cadillac CTS 1G6DP567750110987
2003 dodge Grand-Caravan 1D4GP24313B281599
1995 toyota Camry 4T1GK12E7SU857754
2007 mercury Mountaineer 4M2EU48877UJ13033
2003 ford Ranger 1FTYR10E03PA16916
2002 chevrolet Tahoe 1GNEK13Z32R222120
2001 saturn l-series 1G8JU52F91Y583432
2007 toyota Camry 4T1BE46K57U649169
2010 ford Escape 1FMCU0DG3AKD09651
2002 chrysler Town-and-Country 2C4GP44342R700697
2000 ford F-250-Super-Duty 1FTNX21L4YEC75584
2009 chrysler Town-and-Country 2A8HR44E49R521496
2014 infiniti Q50 JN1BV7AR9EM702613
1997 ford Explorer 1FMDU34E3VUB08508
1994 toyota Camry 4T1SK13E8RU389858
2012 jeep Grand-Cherokee 1C4RJFCG6CC170480
2008 honda Accord 1HGCP36828A040479Finding all car's value


2004 jeep Grand-Cherokee JHD3704 NY 1J4GW48S74C229817
Condition    Trade-In    Private Party    Dealer Retail    
Outstanding     $921 $1,721 $2,144 
Clean           $840 $1,568 $1,955 
Average         $677 $1,261 $1,576 
Rough           $514 $954 $1,197 


2005 cadillac CTS JAK5138 NY 1G6DP567750110987
Condition    Trade-In    Private Party    Dealer Retail    
Outstanding     $1,278 $2,215 $2,737 
Clean           $1,141 $1,978 $2,441 
Average         $867 $1,504 $1,848 
Rough           $593 $1,029 $1,255 


2003 dodge Grand-Caravan HNE5556 NY 1D4GP24313B281599
Condition    Trade-In    Private Party    Dealer Retail    
Outstanding     $694 $1,420 $1,809 
Clean           $640 $1,310 $1,670 
Average         $533 $1,090 $1,390 
Rough           $425 $869 $1,111 


1995 toyota Camry JGL8776 NY 4T1GK12E7SU857754
Condition    Trade-In    Private Party    Dealer Retail    
Outstanding     $1,004 $1,958 $2,478 
Clean           $883 $1,727 $2,185 
Average         $642 $1,264 $1,599 
Rough           $400 $801 $1,014 


2007 mercury Mountaineer AMA6301 NY 4M2EU48877UJ13033
Condition    Trade-In    Private Party    Dealer Retail    
Outstanding     $1,369 $2,671 $3,377 
Clean           $1,272 $2,479 $3,133 
Average         $1,079 $2,096 $2,644 
Rough           $886 $1,713 $2,154 


2003 ford Ranger N598PE FL 1FTYR10E03PA16916
Condition    Trade-In    Private Party    Dealer Retail    
Outstanding     $1,822 $3,515 $4,440 
Clean           $1,646 $3,173 $4,004 
Average         $1,293 $2,488 $3,133 
Rough           $941 $1,804 $2,262 


2002 chevrolet Tahoe JCM4758 NY 1GNEK13Z32R222120
Condition    Trade-In    Private Party    Dealer Retail    
Outstanding     $1,291 $2,277 $2,810 
Clean           $1,162 $2,050 $2,530 
Average         $904 $1,595 $1,969 
Rough           $646 $1,141 $1,408 


2001 saturn l-series JKN3636 NY 1G8JU52F91Y583432
Condition    Trade-In    Private Party    Dealer Retail    
Outstanding     $780 $1,605 $2,053 
Clean           $688 $1,419 $1,816 
Average         $505 $1,048 $1,341 
Rough           $321 $677 $865 


2007 toyota Camry HGJ5976 NY 4T1BE46K57U649169
Condition    Trade-In    Private Party    Dealer Retail    
Outstanding     $2,499 $4,532 $5,637 
Clean           $2,262 $4,108 $5,109 
Average         $1,788 $3,261 $4,054 
Rough           $1,314 $2,413 $2,998 


2010 ford Escape HCU2512 NY 1FMCU0DG3AKD09651
Condition    Trade-In    Private Party    Dealer Retail    
Outstanding     $1,828 $3,326 $4,321 
Clean           $1,729 $3,141 $4,068 
Average         $1,531 $2,770 $3,563 
Rough           $1,334 $2,400 $3,058 


2002 chrysler Town-and-Country VTP3444 VA 2C4GP44342R700697
Condition    Trade-In    Private Party    Dealer Retail    
Outstanding     $929 $1,787 $2,240 
Clean           $854 $1,644 $2,063 
Average         $706 $1,357 $1,709 
Rough           $557 $1,071 $1,355 


2000 ford F-250-Super-Duty ZGK4141 PA 1FTNX21L4YEC75584
Condition    Trade-In    Private Party    Dealer Retail    
Outstanding     $2,220 $4,070 $5,019 
Clean           $1,986 $3,642 $4,501 
Average         $1,519 $2,785 $3,467 
Rough           $1,052 $1,927 $2,432 


2009 chrysler Town-and-Country JFC5627 NY 2A8HR44E49R521496
Condition    Trade-In    Private Party    Dealer Retail    
Outstanding     $1,767 $3,163 $4,046 
Clean           $1,690 $3,023 $3,850 
Average         $1,536 $2,745 $3,457 
Rough           $1,382 $2,466 $3,064 


2014 infiniti Q50 HTP7362 NY JN1BV7AR9EM702613
Condition    Trade-In    Private Party    Dealer Retail    
Outstanding     $11,108 $13,312 $15,311 
Clean           $10,545 $12,625 $14,499 
Average         $9,420 $11,251 $12,873 
Rough           $8,295 $9,876 $11,248 


1997 ford Explorer HRD9924 NY 1FMDU34E3VUB08508
Condition    Trade-In    Private Party    Dealer Retail    
Outstanding     $501 $1,229 $1,602 
Clean           $449 $1,101 $1,441 
Average         $345 $847 $1,118 
Rough           $241 $592 $795 


1994 toyota Camry HNE3418 NY 4T1SK13E8RU389858
Condition    Trade-In    Private Party    Dealer Retail    
Outstanding     $731 $1,653 $2,155 
Clean           $643 $1,457 $1,900 
Average         $467 $1,067 $1,391 
Rough           $291 $676 $881 


2012 jeep Grand-Cherokee JEH9546 NY 1C4RJFCG6CC170480
Condition    Trade-In    Private Party    Dealer Retail    
Outstanding     $5,735 $7,937 $9,628 
Clean           $5,466 $7,556 $9,153 
Average         $4,928 $6,793 $8,201 
Rough           $4,390 $6,030 $7,249 


2008 honda Accord HAM7134 NY 1HGCP36828A040479
Condition    Trade-In    Private Party    Dealer Retail    
Outstanding     $3,965 $6,573 $7,995 
Clean           $3,611 $5,994 $7,290 
Average         $2,903 $4,837 $5,879 
Rough           $2,195 $3,679 $4,468 
*/
