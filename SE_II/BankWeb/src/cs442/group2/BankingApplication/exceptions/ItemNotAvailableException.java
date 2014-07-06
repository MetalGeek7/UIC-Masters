package cs442.group2.BankingApplication.exceptions;

import cs442.group2.BankingApplication.Item;

@SuppressWarnings("serial")
public class ItemNotAvailableException extends RuntimeException {
	private String message;

	public ItemNotAvailableException(Item item) {
		super();
		this.message = String.format("Item(%d) : %s is no longer available.",
				item.getItemID(), item.getItemName());

	}

	@Override
	public String toString() {
		return message;

	}
}
