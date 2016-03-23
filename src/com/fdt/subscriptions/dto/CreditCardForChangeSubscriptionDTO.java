package com.fdt.subscriptions.dto;

import com.fdt.ecom.entity.CreditCard;

public class CreditCardForChangeSubscriptionDTO {

	public CreditCardForChangeSubscriptionDTO() {
		super();
	}

	private CreditCard creditCardForOldSubscription = null;
	
	private CreditCard creditCardForNewSubscription = null;

	public CreditCard getCreditCardForOldSubscription() {
		return creditCardForOldSubscription;
	}

	public void setCreditCardForOldSubscription(
			CreditCard creditCardForOldSubscription) {
		this.creditCardForOldSubscription = creditCardForOldSubscription;
	}

	public CreditCard getCreditCardForNewSubscription() {
		return creditCardForNewSubscription;
	}

	public void setCreditCardForNewSubscription(
			CreditCard creditCardForNewSubscription) {
		this.creditCardForNewSubscription = creditCardForNewSubscription;
	}

	@Override
	public String toString() {
		return "CreditCardForChangeSubscriptionDTO [creditCardForOldSubscription="
				+ creditCardForOldSubscription
				+ ", creditCardForNewSubscription="
				+ creditCardForNewSubscription + "]";
	}
	
	
}
