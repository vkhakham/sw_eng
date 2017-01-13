package server.src;

import java.io.*;
import ocsf.*;

public class GoodHealthServer extends AbstractServer {
    SqlConnection sqlConnection;

    GoodHealthServer() {
        sqlConnection = new SqlConnection();
    }

}
