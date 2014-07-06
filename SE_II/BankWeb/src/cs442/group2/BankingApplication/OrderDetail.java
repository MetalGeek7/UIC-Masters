package cs442.group2.BankingApplication;


public class OrderDetail {
	private Item item;
	private int quantityPurchased;

	@Override
	public String toString() {
		return String.format("OrderDetail %s having quantityPurchased = %d", getItem().toString(), getQuantityPurchased());
	}
	
	/****************************************
	 *			Getters of the Class		*
	 ****************************************/
	public Item getItem() {
		return item;
	}

	public int getQuantityPurchased() {
		return quantityPurchased;
	}

	// Constructor
	public OrderDetail(Item item, int quantityPurchased) {

		this.item = item;
		this.quantityPurchased = quantityPurchased;
	}
	
	

}
