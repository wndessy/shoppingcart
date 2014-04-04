/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controler;

/**
 *
 * @author wndessy
 */
public class Item {
public Item(){
    
} //for shoping
    String Name;
    String Price;
    String Image;
    //for login
    String email;
    String password;
    boolean isValid;
    //for signup variables
    String customerName;
    String Phone;
    
    //for sesssion
    String title;
    int CustomerID;

    public int getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(int CustomerID) {
        this.CustomerID = CustomerID;
    }
int count = 0;
    
    public int getCount(){
        return count;
    }
    public void setCount(int Count){
        this.count = Count;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }
    
    

    public boolean isIsValid() {
        return isValid;
    }

    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
     
    //getter methods
    public String getName() {
        return Name;
    }

    public String getPrice() {
        return Price;
    }

    public String getImage() {
        return Image;
    }

    //setter methods
    public void setName(String Name) {
        this.Name = Name;
    }

    public void setPrice(String Price) {
        this.Price = Price;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }

    //constructor
    public Item(String Name) {
        this.Name = Name;
    }
}
