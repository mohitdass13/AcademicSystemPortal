package org.iitrpr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Locale;
import java.util.Scanner;

public class databaseSeeder {
    private final String url = "jdbc:postgresql://localhost:5432/academicsystem";
    private final String user="postgres";
    private final String password="12345";

    public Connection connect()
    {
        Connection cnct =null;
        try{
            cnct = DriverManager.getConnection(url,user,password);
            if(cnct!=null)
                System.out.println("Connected to Postgres Server Successfully !!");
            else
                System.out.println("Sorry.......Failed to make Connection");
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return cnct;
    }
    public void generateSchema(Connection connection)
    {
        try
        {
            String query="";
            try{
                File file = new File("./SQL/acadSQL.sql");
                Scanner scan = new Scanner(file);
                while(scan.hasNextLine())
                {
                    query=query.concat(scan.nextLine()+" ");
                }
                scan.close();
            }
            catch(IOException e)
            {
                System.out.println(e.getLocalizedMessage());
            }
            Statement stmt= connection.createStatement();
            stmt.execute(query);
            stmt.close();
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
   public void dataseeder(Connection connection)
   {
        String file="./src/datafeed/studentssss.csv";
       BufferedReader read = null;
       String line="";
       try{
           read=new BufferedReader(new FileReader(file));
           while((line= read.readLine())!=null)
           {
               String[] row=line.split(",");
               String name=row[0];
               String entry_no=row[2].toLowerCase();
               String emailid=row[1];
               String dept=row[3];
               Integer entryyr=Integer.parseInt(row[4]);
               academicOffice acaoff=new academicOffice();
               acaoff.addStudent(connection,name,emailid,entry_no,dept,entryyr);

           }
       }catch(Exception e)
       {
            e.printStackTrace();
       }finally {
           try{
                      read.close();
           }catch (IOException e)
           {
               e.printStackTrace();
           }
       }
   }
    public void userseeder(Connection connection)
    {
        String file="./src/datafeed/users.csv";
        BufferedReader read = null;
        String line="";
        try{
            read=new BufferedReader(new FileReader(file));
            while((line= read.readLine())!=null)
            {
                String[] row=line.split(",");
                String username=row[0];
                String pass=row[1];
                String name=row[2];
                String role=row[3];

                PreparedStatement pstmt=connection.prepareStatement("INSERT INTO users VALUES(?,?,?,?)");
                pstmt.setString(1,username);
                pstmt.setString(2,pass);
                pstmt.setString(3,name);
                pstmt.setString(4,role);
                pstmt.execute();
                pstmt.close();

                if(role.equals("instructor"))
                {
                    String qry=String.format("CREATE TABLE %s (course_code VARCHAR(6),student_name VARCHAR(50),entry_no VARCHAR(12))",username);
                    PreparedStatement pstmt2=connection.prepareStatement(qry);
                    pstmt2.execute();
                    pstmt2.close();
                }
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }finally {
            try{
                read.close();
            }catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {

        databaseSeeder dbs=new databaseSeeder();
        Connection connection= dbs.connect();
        dbs.generateSchema(connection);
        dbs.dataseeder(connection);
        dbs.userseeder(connection);
    }
}
