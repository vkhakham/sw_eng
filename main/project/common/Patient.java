package common;

public class Patient {
    private int id;
    private String name;

    public Patient(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Patient setId(int id) {
        this.id = id;
        return this;
    }

    public Patient setName(String name) {
        this.name = name;
        return this;
    }
}
