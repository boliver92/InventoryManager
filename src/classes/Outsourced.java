package classes;

public class Outsourced extends Part{

    // Class Variables
    private String companyName;


    // Methods
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName){
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * Returns Outsourced company name
     *
     * Logical/Runtime Error: None
     * Compatible Features for Updates: None
     *
     * @return      The part's Company Name
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Sets the Outsourced company name
     *
     Logical/Runtime Error: None
     * Compatible Features for Updates: None
     *
     * @param   companyName     The Company Name of the part.
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
