package voiceapp.shop.model;

public class BenefitTransaction {

    private int benefitTransactionID;

    private int userID;

    private int selection;

    private String benefitName;

    private int value;

    public BenefitTransaction(int benefitTransactionID, int userID, int selection, String benefitName, int value) {
        this.benefitTransactionID = benefitTransactionID;
        this.userID = userID;
        this.selection = selection;
        this.benefitName = benefitName;
        this.value = value;
    }

    public int getBenefitTransactionID() {
        return benefitTransactionID;
    }

    public void setBenefitTransactionID(int benefitTransactionID) {
        this.benefitTransactionID = benefitTransactionID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getSelection() {
        return selection;
    }

    public void setSelection(int selection) {
        this.selection = selection;
    }

    public String getBenefitName() {
        return benefitName;
    }

    public void setBenefitName(String name) {
        this.benefitName = benefitName;
    }
}
