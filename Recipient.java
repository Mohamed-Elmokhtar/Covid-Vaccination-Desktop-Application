public class Recipient extends Person {
    private int dose;
    private String dose1Batch;
    private String dose2Batch;
    private String status;
    private String VCName;
    private String appointment1;
    private String appointment2;

    /**
     * Constructs default recipient with all data fields set to their deafult value
     */
    public Recipient() {}

    /**
     * Constucts a recipeint with the given ID, name, phone, age, sets dose to 0,
     * sets status to Pending and sets all the remaining data fields to None
     * 
     * @param ID    recipient ID
     * @param name  recipient name
     * @param phone recipient phone
     * @param age   recipient age
     */
    public Recipient(String ID, String name, String phone, int age) {
        super(ID, name, phone, age);
        dose = 0;
        status = "Pending";
        VCName = "None";
        appointment1 = "None";
        appointment2 = "None";
        dose1Batch = "None";
        dose2Batch = "None";
    }

    /**
     * Constucts a recipeint with the given ID, name, phone, age, dose, dose 1 batch, dose 2 batch
     * status, VC name, appointment 1, and appointment 2
     * 
     * @param ID            recipient ID
     * @param name          recipient name
     * @param phone         recipient phone
     * @param age           recipient age
     * @param dose          recipient dose
     * @param dose1Batch    recipient dose 1 batch
     * @param dose2Batch    recipient dose 2 batch
     * @param status        recipient status
     * @param VCName        recipient assigned VC name
     * @param appointment1  recipient dose 1 appointment 
     * @param appointment2  recipient dose 2 appointment
     */
    public Recipient(String ID, String name, String phone, int age, int dose, String dose1Batch, String dose2Batch,String status, 
                     String VCName, String appointment1, String appointment2) {
        super(ID, name, phone, age);
        this.dose = dose;
        this.dose1Batch = dose1Batch;
        this.dose2Batch = dose2Batch;
        this.status = status;
        this.VCName = VCName;
        this.appointment1 = appointment1;
        this.appointment2 = appointment2;
    }

    /**
     * Accessor to get the dose of the recipient
     * 
     * @return The recipient dose
     */
    public int getDose() {
        return dose;
    }

    /**
     * Mutator to alter recipient dose
     * 
     * @param dose The new dose of the recipient
     */
    public void setDose(int dose) {
        this.dose = dose;
    }

    /**
     * Accessor to get the dose 1 batch number of the recipient
     * 
     * @return The recipient dose 1 batch number
     */
    public String getDose1Batch() {
        return dose1Batch;
    }

    /**
     * Mutator to alter recipient dose 1 batch number
     * 
     * @param dose1Batch The batch number of the recipient first dose
     */
    public void setDose1Batch(String dose1Batch) {
        this.dose1Batch = dose1Batch;
    }

    /**
     * Accessor to get the dose 2 batch number of the recipient
     * 
     * @return The recipient dose 2 batch number
     */
    public String getDose2Batch() {
        return dose2Batch;
    }

    /**
     * Mutator to alter recipient dose 2 batch number
     * 
     * @param dose1Batch The batch number of the recipient second dose
     */
    public void setDose2Batch(String dose2Batch) {
        this.dose2Batch = dose2Batch;
    }

    /**
     * Accessor to get the status of the recipient
     * 
     * @return The recipient status
     */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    /**
     * Accessor to get the vaccine center name the recipient is assigned to
     * 
     * @return The vaccine center name the recipient is assigned to
     */
    public String getVCName() {
        return VCName;
    }

    /**
     * Mutator to alter name of the vaccine center the recipient is assigned to
     * 
     * @param VCName The name of the vaccine center
     */
    public void setVCName(String VCName) {
        this.VCName = VCName;
    }

    /**
     * Accessor to get the appointment of 1st dose of the recipient
     * 
     * @return The recipient 1st dose appointment
     */
    public String getAppointment1() {
        return appointment1;
    }

    /**
     * Mutator to alter recipient 1st dose appointment
     * 
     * @param appointment1 Recipient 1st dose appointment
     */
    public void setAppointment1(String appointment1) {
        this.appointment1 = appointment1;
    }

    /**
     * Accessor to get the appointment of 1st dose of the recipient
     * 
     * @return The recipient 1st dose appointment
     */
    public String getAppointment2() {
        return appointment2;
    }

    /**
     * Mutator to alter recipient 2nd dose appointment
     * 
     * @param appointment1 Recipient 2nd dose appointment
     */
    public void setAppointment2(String appointment2) {
        this.appointment2 = appointment2;
    }

    /**
     * Returs a string consisting of recipeint details where each derail is 
     * separated by a comma
     * 
     * @return A string representaion of the recipients details
     */
    public String toCSVString() {
        return super.getID() + "," + super.getName() + "," + super.getPhone() + "," + super.getAge() + "," + dose + "," + 
            dose1Batch + "," + dose2Batch + "," + status + "," + VCName + "," + appointment1 + "," + appointment2;
    }
}
