public class ServerStart {
    public static void main(String[] args) {
        GoodHealthServer sv = new GoodHealthServer(5555);
        try {
            sv.listen(); //Start listening for connections
        }
        catch (Exception ex) {
            System.out.println("ERROR - Could not listen for clients!");
        }
    }
}
