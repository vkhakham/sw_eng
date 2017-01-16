package common;

import java.io.Serializable;

public class Doctor implements Serializable {
    public Integer Id;
    public String name;
    public String branch;

    public Doctor(Integer id, String name, String branch) {
        Id = id;
        this.name = name;
        this.branch = branch;
    }
}
