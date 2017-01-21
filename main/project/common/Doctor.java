package common;

import java.io.Serializable;

public class Doctor extends Person implements Serializable {
    private String branch;
    private String role;

    public Doctor(Integer id, String name, String branch, String role) {
        super(id, name);
        this.branch = branch;
        this.role = role;
    }

    public String getBranch() {
        return branch;
    }

    public String getRole() {
        return role;
    }
}
