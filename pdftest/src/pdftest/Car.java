package pdftest;
//general car class can be used as an object to store car information such as license plate, model, make, etc
public class Car {
private int price;
private String year;
private String make;
private String plate;
private String state;
private String vin;
private String model;
private int car_year;

//constructor with parameters 
public Car(String year2, String make,String plate, String state,String vin) 
{
	this.year=year2;
	this.make=make;
	this.plate=plate;
	this.state=state;
    this.vin =vin;
    this.model=null;
    // takes the year and copies it into an int in order to make 
    this.car_year=Integer.parseInt(year2);
}


public String getYear() {
	return year;
	
}

public void setYear(String year) {
	this.year=year;
	this.car_year=Integer.parseInt(year);
}

public int getPrice() {
	return price;
}

public void setPrice(int price) {
	this.price=price;
}

public void setMake(String make) {
	this.make=make;
}

public String getMake() {
	return make;
}

public String getState() {
	return state;
}

public void setState(String state) {
	this.state = state;
}

public String getPlate() {
	return plate;
}

public void setPlate(String plate) {
	this.plate=plate;
}

public void setVin(String vin) {
	this.vin=vin;
}
public String getVin() {
  return vin;
}

public void setModel(String model) {
	this.model=model;
}

public String getModel() {
	return model;
}


public int getCar_year() {
	return car_year;
}


public void setCar_year(int car_year) {
	this.car_year = car_year;
}



}
