package Order.OrderAssessmentBugFix;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/* ****************************************************************************************
 
Please remove all bugs from the code below to get the following output:

<pre>

*******Order 1*******
1 book: 13.74
1 music CD: 16.49
1 chocolate bar: 0.94
Sales Tax: 2.84
Total: 28.33
*******Order 2*******
1 imported box of chocolate: 11.5
1 imported bottle of perfume: 54.62
Sales Tax: 8.62
Total: 57.5
*******Order 3*******
1 Imported bottle of perfume: 32.19
1 bottle of perfume: 20.89
1 packet of headache pills: 10.73
1 box of imported chocolates: 12.94
Sales Tax: 8.77
Total: 67.98
Sum of orders: 153.81
 
</pre>
 
******************************************************************************************** */

/*
 * represents an item, contains a price and a description.
 *
 */
class Item {

	private String description;
	private float price;

	public Item(String description, float price) {
		super();
		this.description = description;
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public float getPrice() {
		return price;
	}
}

/*
 * represents an order line which contains the @link Item and the quantity.
 *
 */
class OrderLine {

	private int quantity;
	private Item item;

	/*
	 * @param item Item of the order
	 * 
	 * @param quantity Quantity of the item
	 */
	public OrderLine(Item item, int quantity) throws Exception {
		if (item == null) {
			System.err.println("ERROR - Item is NULL");
			throw new Exception("Item is NULL");
		}
		assert quantity > 0;
		this.item = item;
		this.quantity = quantity;
	}

	public Item getItem() {
		return item;
	}

	public int getQuantity() {
		return quantity;
	}
}

class Order {

	private List<OrderLine> orderLines;

	public Order() {
		orderLines = new ArrayList<OrderLine>();
	}

	public void add(OrderLine o) throws Exception {
		if (o == null) {
			System.err.println("ERROR - Order is NULL");
			throw new IllegalArgumentException("Order is NULL");
		}
		orderLines.add(o);
	}

	public int size() {
		return orderLines.size();
	}

	public OrderLine get(int i) {
		return orderLines.get(i);
	}

	public void clear() {
		this.orderLines.clear();
	}
}

class calculator {

	public static BigDecimal rounding(BigDecimal value) {
		return value.setScale(2, RoundingMode.HALF_UP);
	}

	/**
	 * receives a collection of orders. For each order, iterates on the order lines 
	 * and calculate the total price which is the item's (price * quantity) + taxes.
	 * 
	 * For each order, print the total Sales Tax paid and Total price without taxes for 
	 * this order
	 */
	public void calculate(Map<String, Order> o) {

	    BigDecimal salesTaxPercent = new BigDecimal(".10");
	    BigDecimal importedSalesTaxPercent = new BigDecimal(".15");

		BigDecimal grandtotal = BigDecimal.ZERO;

		// Iterate through the orders
		for (Map.Entry<String, Order> entry : o.entrySet()) {
			System.out.println("*******" + entry.getKey() + "*******");

			Order r = entry.getValue();

			BigDecimal totalTax = BigDecimal.ZERO;
			BigDecimal total = BigDecimal.ZERO;

			// Iterate through the items in the order
			for (int i = 0; i < r.size(); i++) {

				// Calculate the taxes
			    BigDecimal tax;


				final BigDecimal price = rounding(new BigDecimal(Float.toString(r.get(i).getItem().getPrice())))
						.multiply(new BigDecimal(Integer.toString(r.get(i).getQuantity())));
				if (r.get(i).getItem().getDescription().toLowerCase().contains("imported")) {
					// Extra 5% tax on imported items
					tax = rounding(importedSalesTaxPercent.multiply(price));
				} else {
					tax = rounding(salesTaxPercent.multiply(price));
				}
				

				// Calculate the total price
				BigDecimal totalprice = price.add(tax);
				
				// Print out the item's total price
				System.out.println(r.get(i).getQuantity()+" "+r.get(i).getItem().getDescription() + ": " + totalprice.doubleValue());

				// Keep a running total
				totalTax = totalTax.add(tax);
				total = total.add(price);
			}

			// Print out the total taxes
			System.out.println("Sales Tax: " + totalTax.doubleValue());

			//NOTE: commented this to not include tax in the 'total'
//			total = total.add(totalTax);

			// Print out the total amount
			System.out.println("Total: " + total.doubleValue());
			grandtotal = grandtotal.add(total);
		}

		System.out.println("Sum of orders: " + grandtotal.doubleValue());
	}
}

public class Foo_BugFix {

	public static void main(String[] args) throws Exception {

		Map<String, Order> o = new LinkedHashMap<String, Order>();

		Order order1 = new Order();

		order1.add(new OrderLine(new Item("book", 12.49f), 1));
		order1.add(new OrderLine(new Item("music CD", 14.99f), 1));
		order1.add(new OrderLine(new Item("chocolate bar", 0.85f), 1));

		o.put("Order 1", order1);

		Order order2 = new Order();
		order2.add(new OrderLine(new Item("imported box of chocolate", 10f), 1));
		order2.add(new OrderLine(new Item("imported bottle of perfume", 47.50f), 1));

		o.put("Order 2", order2);

		Order order3 = new Order();
		order3.add(new OrderLine(new Item("Imported bottle of perfume", 27.99f), 1));
		order3.add(new OrderLine(new Item("bottle of perfume", 18.99f), 1));
		order3.add(new OrderLine(new Item("packet of headache pills", 9.75f), 1));
		order3.add(new OrderLine(new Item("box of imported chocolates", 11.25f), 1));

		o.put("Order 3", order3);

		new calculator().calculate(o);

	}
}
