import common.Message;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class StaffInterfaceStart {
    public static void main(String[] args) {
        StaffInterfaceClient staffInterfaceClient = new StaffInterfaceClient("localhost", 5555);
        try {
            BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
            String message;

            while (true)
            {
                message = fromConsole.readLine();
                staffInterfaceClient.openConnection();
                staffInterfaceClient.sendToServer(new Message("hello/das", "dadas"));
            }
        }
        catch (Exception ex) {
            System.out.println("Unexpected error while reading from console!");
        }
    }
}

