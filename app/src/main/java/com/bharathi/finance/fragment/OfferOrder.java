package com.bharathi.finance.fragment;

import java.io.Serializable;

/**
 * Created by ravi on 21/12/17.
 */

public class OfferOrder implements Serializable {
    String companyName;
    String principal;
    String amount;
    String interest;
    String duration;
    String monthlyInstallement;
    String startDate;
    String endDate;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getMonthlyInstallement() {
        return monthlyInstallement;
    }

    public void setMonthlyInstallement(String monthlyInstallement) {
        this.monthlyInstallement = monthlyInstallement;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
