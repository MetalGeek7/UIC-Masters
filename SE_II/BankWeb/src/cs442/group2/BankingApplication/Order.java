package cs442.group2.BankingApplication;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Order {
	private int orderID;
	private int customerID;
	private Date dateTime;
	private List<OrderPayment> paymentOptions;
	private List<OrderDetail> orderDetails;
	private double totalAmount;

	@Override
	public String toString() {
		String s = "";
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		s = format.format(getDateTime());
		
		
		s =  String.format("Order(orderID=%d) of (customerID=%d) on %s of total: %4.2f with payments:", getOrderID(), getCustomerID(), s, getTotalAmount());
		for(OrderPayment payment : getPaymentOptions()){
			s += "\n\t" + payment.toString();
		}
		s += "\n\t";
		for(OrderDetail detail : getOrderDetails()){
			s += "\n\t" + detail.toString();
		}
		
		return s;
	}

	/****************************************
	 *			Getters of the Class		*
	 ****************************************/
	
	public int getOrderID() {
		return orderID;
	}

	public int getCustomerID() {
		return customerID;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public List<OrderDetail> getOrderDetails() {
		return orderDetails;
	}
	
	public List<OrderPayment> getPaymentOptions() {
		return paymentOptions;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	// Constructor
	Order(int orderID, int customerID, Date dateTime,
			List<OrderPayment> paymentOptions, List<OrderDetail> orderDetails, double totalAmount) {
		this.orderID = orderID;
		this.customerID = customerID;
		this.dateTime = dateTime;
		this.paymentOptions = paymentOptions;
		this.orderDetails = orderDetails;
		this.totalAmount = totalAmount;
		
	}

	
}
