/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controler;

import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.apache.taglibs.standard.functions.Functions.trim;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author wndessy
 */
public class DbModules {

    private JSONObject json;

    public DbModules() {
    }

    int status = 0;
    static Connection con = null;

    /**
     * This code is for getting connection to the db
     *
     * @return connection string
     */
    public static Connection getConnection() {
        try {
            Common one = new Common();

            DriverManager.registerDriver(new com.mysql.jdbc.Driver());//remember this
            con = DriverManager.getConnection(one.getDb_url(), one.getUser(), one.getPassword());
            System.out.println("connected");
        } catch (SQLException sql) {
            System.out.println(sql.getMessage());
        }
        return con;
    }

    private boolean isComponent(String name, int qty, String price) {
        boolean isComponent = false;
        try {
            Connection conn = DbModules.getConnection();
            Statement stmt = conn.createStatement();

            String sql = "select * from component Where name=\"" + name + "\" && price_per_item=\"" + price + "\"";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                isComponent = true;
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return isComponent;
    }

    private boolean isItem(String name, String price) {
        boolean isItem = false;
        try {
            Connection conn = DbModules.getConnection();
            Statement stmt = conn.createStatement();

            String sql = "select product_id from product Where name=\"" + name + "\" && price=\"" + price + "\"";
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                isItem = true;
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return isItem;
    }

    private String getComponents(String name, String price) {
        String components = null;
        try {
            Connection conn = DbModules.getConnection();
            Statement stmt = conn.createStatement();
            String sql1 = "select * from product where name =\"" + name + "\" && price=\"" + price + "\"";
            ResultSet rs1 = stmt.executeQuery(sql1);

            if (rs1.next()) {
                String id = rs1.getString("product_id");

                String sql = "select DISTINCT name,price_per_item  from product_component natural join component Where product_id=\"" + id + "\"";
                ResultSet rs = stmt.executeQuery(sql);

                int i = 0;
                if (rs.next()) {
                    components="";
                    while (rs.next()) {
                        components += "{\"name\":\"" + rs.getString("name") + "\"," + "\"price\":\"" + rs.getString("price_per_item") + "\"};";
                    }
                    components = (components.substring(0, components.length() - 1)).trim();//delete the last semi colon
                }
                stmt.close();
            }
            rs1.close();

        } catch (SQLException e) {
            System.out.println("Error getting components");
            e.printStackTrace();
        }
        return components;
    }

    public boolean checkIfComponentExists(String ItemName, String itemPrice, int Quantity) {
        boolean present = false;
        try {
            Connection conn = DbModules.getConnection();
            Statement stmt = conn.createStatement();
            String sql1 = "SELECT * FROM `component` where name=\"" + ItemName + "\" and number_of_units>=\"" + Quantity + "\" and price_per_item =\"" + itemPrice + "\"";
            ResultSet rs1 = stmt.executeQuery(sql1);

            if (rs1.next()) {
                present = true;
                stmt.close();
            }
            rs1.close();

        } catch (SQLException e) {
            System.out.println("Error checking availablility of component");
            e.printStackTrace();
        }
        return present;
    }

    public String UpdatePurchase(String data[], String customerID) throws ParseException {
        String absentItems = "";
        boolean result = true;

        String status1 = null;
        for (String data1 : data) {
            String[] values = data1.split(",");
            for (String value : values) {
                
                System.out.println("Values: "+value);
                String[] individual = value.split(":");
                String name = individual[0];
                int quantity = Integer.parseInt(individual[1].trim());
                String price = individual[2].trim();

                //this is either an item or a component
                if (isItem(name, price)) {
                    System.out.println(name + ":" + quantity + ":" + price + "   : item");

                    //check if all components of this item exist
                    String components = getComponents(name, price);

                    //all Items must have components
                    if (components == null) {
                        if (status1 == null) {
                            result = false;
                            status1 = name + " is not available because it does not have any components the moment :)\n";
                        } else {
                            status1 += name + " is not available because it does not have any components the moment :)\n";
                        }
                    } else {
                        String[] split = components.split(";");

                        for (String split1 : split) {
                            
                            try {
                                json = (JSONObject) new JSONParser().parse(split1);
                                System.out.println("Checking for availability of "+(json.get("name").toString().trim()));
                                if (!checkIfComponentExists((json.get("name").toString().trim()), (json.get("price").toString().trim()), quantity)) {
                                    result = false;
                                    absentItems += (json.get("name").toString().trim()) + ": ksh" + (json.get("price").toString().trim()) + ",";
                                }
                            } catch (ParseException ex) {
                                Logger.getLogger(DbModules.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        if (result) {
                            //now buy them
                            for (String split1 : split) {
                                try {
                                    json = (JSONObject) new JSONParser().parse(split1);
                                    actualPerformPurchase((json.get("name").toString().trim()), quantity, (json.get("price").toString().trim()));

                                } catch (ParseException ex) {
                                    Logger.getLogger(DbModules.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }

                        } else {
                            absentItems = (absentItems.substring(0, absentItems.length() - 1)).trim();//delete the last semi colon
                            if (status1 == null) {
                                status1 = name + " is not available because its component \"" + absentItems + "\" is not available at the moment :)\n";
                            } else {
                                status1 += name + " is not available because its component \"" + absentItems + "\" is not available at the moment :)\n";
                            }
                        }
                    }

                } else if (isComponent(name, quantity, price)) {
                    System.out.println(name + ":" + quantity + ":" + price + "   : component");
                    if (!checkIfComponentExists(name, price, quantity)) {
                        result = false;
                        if (status1 == null) {
                            status1 = "Component \"" + name + "\": ksh. " + price + " is not available at the moment :)\n";
                        } else {
                            status1 += "Component \"" + name + "\": ksh. " + price + " is not available at the moment :)\n";
                        }
                    }
                }
            }
        }

        //all requsted components exist
        if (result) {
            for (String data1 : data) {
                String[] values = data1.split(",");
                for (String value : values) {
                    String[] individual = value.split(":");
                    String name = individual[0];
                    int quantity = Integer.parseInt(individual[1].trim());
                    String price = individual[2].trim();

                    //this is either an item or a component
                    if (isItem(name, price)) {
                        System.out.println(name + ":" + quantity + ":" + price + "   : item");

                        //check if all components of this item exist
                        String components = getComponents(name, price);
                        String[] split = components.split(";");

                        for (String split1 : split) {
                            try {
                                json = (JSONObject) new JSONParser().parse(split1);
                                actualPerformPurchase((json.get("name").toString().trim()), quantity, (json.get("price").toString().trim()));

                            } catch (ParseException ex) {
                                Logger.getLogger(DbModules.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                    } else if (isComponent(name, quantity, price)) {
                        System.out.println(name + ":" + quantity + ":" + price + "   : component");
                        actualPerformPurchase(name, quantity, price);
                    }
                }
            }

            //now update the order table
            String str1 = Arrays.toString(data);

            //replace starting "[" and ending "]" and ","
            str1 = str1.substring(1, str1.length() - 1).replaceAll(",", "");

            System.out.println("String 1: " + str1 + " and status = " + status1 + "cUST ID  = " + customerID);
            try {
                String sql = "insert into `order` (items,customer_id) values('" + str1 + "','" + customerID + "')";
                Connection conn = DbModules.getConnection();
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(sql);
                stmt.close();

            } catch (SQLException ex) {
                Logger.getLogger(DbModules.class.getName()).log(Level.SEVERE, null, ex);
            }
            status1 = "SUCCESSFUL PURCHASE."
                    + "You order is being proccessed and will be delivered as specified."
                    + "Thank you for shopping with us.";
        }
        //stmt.close();
        return status1;
    }

    public void actualPerformPurchase(String name, int quantity, String price) {
        try {
            Connection conn = this.getConnection();
            Statement stmt = conn.createStatement();

            String sql = "update component set number_of_units=number_of_units-" + quantity + " Where name=\"" + name + "\" and price_per_item=\"" + price + "\"";
            stmt.executeUpdate(sql);
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(DbModules.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int insertItemToDb(String itemName) throws SQLException {
        String usql = " insert into product (name) VALUES ('" + itemName + "')";
        try {
            Connection conn = this.getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(usql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public int getItemFromDb(String itemName) throws SQLException {
        String usql = " select *  from product where name = '" + itemName + "'";
        try {
            Connection conn = this.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(usql);
            while (rs.next()) {
                System.out.println(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    /**
     *
     * @return
     */
    public ArrayList<String> GetItemsInDb() throws SQLException {

        String usql = " select count *  from product ";

        Connection conn = this.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(usql);
        ArrayList<String> items = new ArrayList<String>();
        while (rs.next()) {
            items.add(rs.getString("name"));
        }

        ArrayList<String> myItems = items;
        return myItems;
    }

    public int updateItemOnDb(String itemName) throws SQLException {
        String usql = " update customers  set name = '" + itemName + "'";
        try {
            Connection conn = this.getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeQuery(usql);
            //    System.out.println(this.getItemFromDb(itemName));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    /**
     * This code is for getting connection to the db
     *
     * @return connection string
     * @throws java.sql.SQLException
     */
    public int InsertPurchaseDetails(String itemName, String customerName) throws SQLException {
        String usql = " select product_id from product where name = '" + itemName + "'";
        String usql2 = " select customer_id from product where name = '" + customerName + "'";
        try {
            Connection conn = this.getConnection();
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(usql);
            ResultSet rs2 = stmt.executeQuery(usql2);

            int itemId = rs.getInt("product_id");
            int customerId = rs2.getInt("customer_id");

            String usql1 = " Insert into order (order_date,product_id,customer_id)values ('" + date() + "','" + itemId + "','" + customerId + "')";
            stmt.executeUpdate(usql1);
            rs.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    /* for login*/
    public static Item validateLogin(Item user) {
        System.out.println("db modules validateLogin= email" + user.getEmail() + " pass" + user.getPassword() + "");
        String usql = "SELECT  * FROM customer WHERE email_adress = \"" + user.getEmail() + "\" and  password = \"" + user.getPassword() + "\"";
        try {
            Connection conn;
            conn = DbModules.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(usql);
            boolean test = rs.next();
            if (!test) {
                System.out.println("Failure login");
            } else if (test) {
                System.out.println("ssuucceessffuull login");
                user.setEmail(rs.getString("email_adress"));
                user.setPassword(rs.getString("password"));
                //call the findname method from within the class and setting  the return value to name 
                user.setName(DbModules.findName(rs.getString("email_adress")));//
                user.setIsValid(true);
            }
        } catch (SQLException e) {
            System.out.println("an exception occured" + e + "");
        }
        return user;
    }

    //for getting the user name
    public static String findName(String EmailAddress) {
        String usql = "SELECT name FROM customer WHERE email_adress=\"" + EmailAddress + "\"";
        Connection conn = DbModules.getConnection();
        String name = null;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(usql);
            if (rs.next()) {
                name = rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }

    public static int findCustomerId(String EmailAddress) {
        String usql = "SELECT cust_id FROM customer WHERE email_adress=\"" + EmailAddress + "\"";
        Connection conn = DbModules.getConnection();
        int id = 0;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(usql);
            if (rs.next()) {
                id = rs.getInt("cust_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    //for signing up a customer
    public void customerSIgnup(String name, String email, String phone, String password) {
        String usql = "INSERT INTO  customer (name,email_adress,phone,password)"
                + " VALUES ('" + name + "','" + email + "','" + phone + "','" + password + "')";
        try {
            Connection conn = DbModules.getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(usql);
            System.out.println(name + "  " + email + " " + phone + "  " + password);
        } catch (SQLException sql) {
            System.out.println(sql.getMessage());
        }
    }

    //for adding a component
    public void component_add(String val1, String val2, String val3) {
        String usql = "INSERT INTO into component (component_id,name,price_per_item,description)"
                + " VALUES ('','" + val1 + "','" + val2 + "','" + val3 + "')";
        try {
            Connection conn = DbModules.getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(usql);
            System.out.println("statemented");
            stmt.executeQuery(usql);

        } catch (SQLException sql) {
            System.out.println(sql.getMessage());
        }
    }

    //for adding a product
    public String product(String name, String[] components, int price) {
        String status1 = null;
        String myComponents = "";

        for (int i = 0; i < components.length; i++) {
            myComponents += components[i].trim() + ",";
        }
        myComponents = (myComponents.substring(0, myComponents.length() - 1)).trim();

        System.out.println(myComponents);
        //  String myComponents=Arrays.toString(components);

        String usql = "INSERT INTO product (name,components,price) VALUES ('" + name + "','" + myComponents + "','" + price + "')";
        try {
            Connection conn = DbModules.getConnection();
            Statement stmt = conn.createStatement();
            System.out.println(name + "  " + myComponents);
            stmt.executeUpdate(usql);

            //get the item id for the item just entered
            String sql = " Select product_id from product where name=\"" + name + "\"";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                int ItemId = rs.getInt("product_id");
                for (String component : components) {
                    //get the component id fom each component name
                    String sql2 = "Select compnt_id from component where name=\"" + component.trim() + "\"";
                    ResultSet rs1 = stmt.executeQuery(sql2);

                    if (rs1.next()) {
                        int ComponentId = rs1.getInt("compnt_id");
                        String sql3 = "Insert into product_component(product_id,compnt_id) values('" + ItemId + "','" + ComponentId + "')";
                        stmt.executeUpdate(sql3);
                    }
                } //insert the component and product ids into the product component table

            }
            status1 = "Component Added Successfully";

        } catch (SQLException sql) {
            System.out.println(sql.getMessage());
            sql.printStackTrace();
            status1 = "Component could not be added";
        }
        return status1;
    }

// for adding components
    public void AddComponent(String name, String Description, int price) {

        System.out.println("from db modules" + name + "  " + Description);
        String myComponents = "";

        String usql = "INSERT INTO component (name,price_per_item,description) VALUES ('" + name + "','" + price + "','" + Description + "')";
        try {
            Connection conn = DbModules.getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(usql);

        } catch (SQLException sql) {
            System.out.println(sql.getMessage());
            sql.printStackTrace();
        }
    }
// for adding Stock

    public String AddStock(String name, int quantity) {
        String status = null;
        System.out.println("From db" + name + quantity);
        String NewName = trim(name);
        
        String sqli ="Select number_of_units from component Where name=\""+name+"\"";
        String usql = "UPDATE component  Set number_of_units =  number_of_units + " + quantity + " WHERE name=\"" + NewName + "\"";
        try {
            
            Connection conn = DbModules.getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(usql);
            
            ResultSet rs= stmt.executeQuery(sqli);
            if(rs.next()){
                int quantity1=rs.getInt("number_of_units");
            
            status = "Update successful,the number of units is now "+quantity1;
        }
        } catch (SQLException sql) {
            System.out.println(sql.getMessage());
            sql.printStackTrace();
            status = "Update failed";
        }
        return status;
    }

    //for adding a purchase
    public void order(String val1, String val2, String val3) {
        String usql = "INSERT INTO  order (order_id,order_date,item_id,customer_id) "
                + "    VALUES ('','" + val1 + "','" + val2 + "','" + val3 + "')";
        try {
            Connection conn = DbModules.getConnection();
            Statement stmt = conn.createStatement();
            stmt = conn.createStatement();
            System.out.println("statemented");
            stmt.executeQuery(usql);

        } catch (SQLException sql) {
            System.out.println(sql.getMessage());

        }
    }

}
