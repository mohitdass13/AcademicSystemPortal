package org.iitrpr;
import java.sql.*;
import java.io.*;
import java.util.*;

public class academicSystem {
    public static void main(String[] args) throws IOException, SQLException {


            databaseSeeder db=new databaseSeeder();
            academicSystem acsys =new academicSystem();
            Connection connection= db.connect();
            authentication auth = new authentication();
            auth.login(connection);
        }


}