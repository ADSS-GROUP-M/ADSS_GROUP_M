package Backend;

public class BankAccount {
    private String bank;
    private String branch;
    private String accountNumber;

    public BankAccount(String bank, String branch, String accountNumber) {
        this.bank = bank;
        this.branch = branch;
        this.accountNumber = accountNumber;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
