package com.group_1.usege.userInfo.dtos;

public class PaymentRequestDto {
    private String planName;
    private String cardNumber;
    private String cvv;
    private String expiredDate;

    public PaymentRequestDto(String planName, String cardNumber, String cvv, String expiredDate) {
        this.planName = planName;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expiredDate = expiredDate;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
    }
}
