package staff.src;

import common.Message;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class StaffInterfaceStart {
    public static void main(String[] args) {
        StaffInterfaceClient staffInterfaceClient = new StaffInterfaceClient("localhost", 5555);
        staffInterfaceClient.login(1, 1234);
    }
}

