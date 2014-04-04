/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var General = {
    buttonClicked: function(id, name, price) {
        //alert(id+ ""+ name + "" + price);
        var tableName = "Cart";

        if (General.getNumberOfSuchItemsSelectedAlready(tableName, name, price) === 0) {
            var newRowContent = "<tr><td>" + name + "</td>" + "<td>" + price + "</td><td>1</td><td><button name ='remove_from_cart_button' class='remove_from_cart_button' type='button' onclick='General.removeItemFromCart(\"" + tableName + "\",\"" + name + "\",\"" + price + "\")'>Remove</button></td></tr>";
            $(newRowContent).appendTo($("#" + tableName));
            document.getElementById("buy_button").disabled = false;
        }
        else {
            //just increment the number of them available
            General.incrementItem(tableName, name, price);
        }

        General.showSumTotal(tableName);
    },
    addItem: function(opt) {
        var components = new Array();
        var ItemPrice = document.getElementById("price").value;
        var itemName = document.getElementById("ItemName").value;

        var select1 = document.getElementById(opt);
        for (var i = 0; i < select1.length; i++) {
            if (select1.options[i].selected) {
                components [i]=(select1.options[i].innerHTML);
            }
        }

        if ((components !== "") && (ItemPrice !== "") && itemName !== "") {
            var values = components + ":" + itemName + ":" + ItemPrice;
            //send these values to manageItem

            var params = 'myValues=' + values;
            //console.log(params);

            $.ajax({
                type: "POST", url: 'ManageItems', async: false, data: params,
                error: function() {
                    alert("Error Adding new Item");
                    return false;
                },
                success: function(data) {
                    alert(data);
                }
            });

        }
        else {
            alert("Please fill all the fields")
        }
    },
    buttonBuyItems: function() {
        var tableName = "Cart";
        var table = $("#" + tableName)[0];
        var myArray = new Array();
        var k = 0;
        for (var r = 0; r < table.rows.length; r++) {

            var name = $(table.rows[r].cells[0]).text();
            var price = $(table.rows[r].cells[1]).text();
            var quantity = $(table.rows[r].cells[2]).text();

            if (!(isNaN(parseInt(quantity)))) {
                myArray[k++] = name + ":" + quantity + ":" + price;
            }
        }
        //send thyese values to the servlet
        //all is well, lets send this data to the database
        var params = 'myValues=' + myArray;
        //console.log(params);

        $.ajax({
            type: "POST", url: 'purchaseServlet', async: false, data: params,
            error: function() {
                alert("Error buying");
                return false;
            },
            success: function(data) {
                alert(data);

            }
        });

    },
    removeItemFromCart: function(tableName, itemName, itemPrice) {
        //alert("Removing: "+itemName+","+itemPrice+" from "+tableName);
        General.decrementItem(tableName, itemName, itemPrice);
        General.showSumTotal(tableName);
    },
    getNumberOfSuchItemsSelectedAlready: function(tableName, itemName, itemCost) {
        var number = 0;
        var table = $("#" + tableName)[0];

        for (var r = 0; r < table.rows.length; r++) {
            var cell = table.rows[r].cells[0];
            var desc = $(cell).text();
            var cost = $(table.rows[r].cells[1]).text();

            if (desc === itemName && cost === itemCost) {
                number = parseInt($(table.rows[r].cells[2]).text());
                break;
            }
        }
        return number;
    }
    , incrementItem: function(tableName, itemName, itemCost) {
        var table = $("#" + tableName)[0];

        for (var r = 0; r < table.rows.length; r++) {

            var desc = $(table.rows[r].cells[0]).text();
            var cost = $(table.rows[r].cells[1]).text();
            if (desc === itemName && cost === itemCost) {
                $(table.rows[r].cells[2]).text(parseInt($(table.rows[r].cells[2]).text()) + 1);
                break;
            }
        }
    }
    , decrementItem: function(tableName, itemName, itemCost) {
        var table = $("#" + tableName)[0];

        for (var r = 0; r < table.rows.length; r++) {

            var desc = $(table.rows[r].cells[0]).text();
            var cost = $(table.rows[r].cells[1]).text();

            if ((desc === itemName) && (cost === itemCost)) {
                if (parseInt($(table.rows[r].cells[2]).text()) - 1 > 0) {
                    //jusr decrease the number
                    $(table.rows[r].cells[2]).text(parseInt($(table.rows[r].cells[2]).text()) - 1);
                    break;
                }
                else if (parseInt($(table.rows[r].cells[2]).text()) === 1) {
                    //remove the row from the table
                    table.deleteRow(r);
                    if (r === 2) {
                        document.getElementById("buy_button").disabled = true;
                        $("#totalCost").text("0");
                    }
                }
            }
        }
    }
    , outputSelected: function(opt) {
        var select1 = document.getElementById(opt);
        for (var i = 0; i < select1.length; i++) {
            if (select1.options[i].selected) {
                var res = (select1.options[i].value).split(":");
                General.buttonClicked("1", res[0], res[1]);
            }
        }
    },
    update_components: function() {
        var name = document.getElementById("itemToAdd").options[document.getElementById("itemToAdd").options.selectedIndex].value;
        var quantity = $("#quantity").val();

        if (quantity !== "") {
            var myArray = "name=" + name + "&quantity=" + quantity;
            var params = myArray;

            $.ajax({
                type: "POST", url: 'Addstock', async: false, data: params,
                error: function() {
                    alert("Error adding data");
                    return false;
                },
                success: function(data) {
                    alert(data);
                }
            });
        }
        else {
            alert("Please enter the quantity");
        }
    }
    , showSumTotal: function(tableName) {
        var table = $("#" + tableName)[0];
        var sum = 0;
        for (var r = 0; r < table.rows.length; r++) {
            var cost = parseFloat($(table.rows[r].cells[1]).text());
            var numberOfItems = parseInt($(table.rows[r].cells[2]).text());

            //some cells do not contain numbers . Ignore them
            if (!(isNaN(cost) || isNaN(numberOfItems))) {
                sum += (cost * numberOfItems);
                $("#totalCost").text(sum);
            }
        }
    }
};
