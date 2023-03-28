public abstract class Person {
    private String ID;
    private String name;
    private String phone;
    private int age;
    
    public Person() {
    }

    public Person(String ID, String name, String phone, int age) {
        this.ID = ID;
        this.name = name;
        this.phone = phone;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String searchString() {
        return name + "," + phone;
    }

    public abstract String toCSVString();
}