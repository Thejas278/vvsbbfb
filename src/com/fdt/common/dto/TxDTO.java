package com.fdt.common.dto;

import com.fdt.ecom.entity.Merchant;

public class TxDTO {

	private String txRefNumber = null;
    
    private Merchant merchant = null;
    
    public String getTxRefNumber() {
		return txRefNumber;
	}

	public void setTxRefNumber(String txRefNumber) {
		this.txRefNumber = txRefNumber;
	}

	public Merchant getMerchant() {
		return merchant;
	}

	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}
	
}
