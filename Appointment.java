public class Appointment {
    private String date;
    private int appointmentsPerDate;

    public Appointment(String date, int appointmentsPerDate) {
        this.date = date;
        this.appointmentsPerDate = appointmentsPerDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAppointmentsPerDate() {
        return appointmentsPerDate;
    }

    public void setAppointmentsPerDate(int appointmentsPerDate) {
        this.appointmentsPerDate = appointmentsPerDate;
    }
}
