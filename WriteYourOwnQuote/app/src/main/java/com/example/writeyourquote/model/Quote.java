package com.example.writeyourquote.model;

public class Quote {
    private int id=0;
    private String authorName="",quotation="",quotationDate="",quoteAddedBy="";

    public Quote(int id, String authorName, String quotation, String quotationDate, String quoteAddedBy) {
        this.id = id;
        this.authorName = authorName;
        this.quotation = quotation;
        this.quotationDate = quotationDate;
        this.quoteAddedBy = quoteAddedBy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getQuotation() {
        return quotation;
    }

    public void setQuotation(String quotation) {
        this.quotation = quotation;
    }

    public String getQuotationDate() {
        return quotationDate;
    }

    public void setQuotationDate(String quotationDate) {
        this.quotationDate = quotationDate;
    }

    public String getQuoteAddedBy() {
        return quoteAddedBy;
    }

    public void setQuoteAddedBy(String quoteAddedBy) {
        this.quoteAddedBy = quoteAddedBy;
    }
}
