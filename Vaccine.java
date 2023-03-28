public class Vaccine {
    private int batchNumber;
    private String assignedVC;

    public Vaccine(int batchNumber, String assignedVC) {
        this.batchNumber = batchNumber;
        this.assignedVC = assignedVC;
    }

    public int getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(int batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getAssignedVC() {
        return assignedVC;
    }
    
    public void setAssignedVC(String assignedVC) {
        this.assignedVC = assignedVC;
    }

    public String toCSVString() {
        return batchNumber + "," + assignedVC;
    }
}
