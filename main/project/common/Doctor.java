package common;

import java.io.Serializable;

public class Doctor implements Serializable {
    private Integer Id;
    private String name;
    private String branch;
    private String role;

    public Doctor(Integer id, String name, String branch, String role) {
        Id = id;
        this.name = name;
        this.branch = branch;
        this.role = role;
    }

    public Doctor setId(Integer id) {
        Id = id;
        return this;
    }

    public Doctor setName(String name) {
        this.name = name;
        return this;
    }

    public Doctor setBranch(String branch) {
        this.branch = branch;
        return this;
    }

    public Doctor setRole(String role) {
        this.role = role;
        return this;
    }

    public Integer getId() {
        return Id;
    }

    public String getName() {
        return name;
    }

    public String getBranch() {
        return branch;
    }

    public String getRole() {
        return role;
    }
}
