package org.iitrpr;
import java.sql.*;
import java.io.*;
import java.util.*;

public class academicSystem {
    public static void main(String[] args) throws IOException{
        try
        {
            databaseSeeder db=new databaseSeeder();
            academicSystem acsys =new academicSystem();
            Connection connection= db.connect();
            authentication auth = new authentication();
            auth.login(connection);
            academicOffice acoff=new academicOffice();
//            acoff.addStudent(connection,"Manoj Kumar","2020CSB1098","2020csb1098@iitrpr.ac.in","Computer Science and Engineering",2020);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }

    }
}