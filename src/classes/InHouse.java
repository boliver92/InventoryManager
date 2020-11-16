package classes;

public class InHouse extends Part{
    private int machineId;

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId){
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * Sets machineId
     * @param   machineId   the ID of the InHouse object.
     *
     * Logical/Runtime Error: None
     * Compatible Feature on Updates: None
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    /**
     * Returns machineID
     * @return  the ID of the InHouse object
     *
     * Logical/Runtime Error: None
     * Compatible feature on Updates: None
     */
    public int getMachineId() {
        return machineId;
    }
}
