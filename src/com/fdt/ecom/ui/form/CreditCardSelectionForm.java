package com.fdt.ecom.ui.form;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import com.fdt.ecom.entity.CreditCard;

public class CreditCardSelectionForm {

    private List<CreditCard> creditCardList;
    private Long selectedCardId;
    private Boolean useFirmAccount;

    public CreditCardSelectionForm(List<CreditCard> creditCardList) {
        this.creditCardList = creditCardList;
    }

    public CreditCardSelectionForm() {

    }

    public List<CreditCard> getCreditCardList() {
        return creditCardList;
    }

    public void setCreditCardList(List<CreditCard> creditCardList) {
        this.creditCardList = creditCardList;
    }

    public Long getSelectedCardId() {
        return selectedCardId;
    }

    public void setSelectedCardId(Long selectedCardId) {
        this.selectedCardId = selectedCardId;
    }

    public Boolean getUseFirmAccount() {
        return useFirmAccount;
    }

    public void setUseFirmAccount(Boolean useFirmAccount) {
        this.useFirmAccount = useFirmAccount;
    }

    public Boolean isCardExpired(CreditCard creditCard) {
        Integer expiryYear = creditCard.getExpiryYear();
        Integer expiryMonth = creditCard.getExpiryMonth();
        LocalDate expiryDate = LocalDate.of(expiryYear, expiryMonth, 1).with(TemporalAdjusters.lastDayOfMonth());
        return expiryDate.compareTo(LocalDate.now()) < 0;
    }

    public Boolean isCardExpired(Integer index) {
        CreditCard creditCard = creditCardList.get(index);
        return isCardExpired(creditCard);
    }

    public Boolean areAllCardsExpired() {
        return creditCardList.stream().allMatch(c -> isCardExpired(c));
    }

}