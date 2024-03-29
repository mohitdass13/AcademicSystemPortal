package org.iitrpr;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


//INSERT INTO course_catalog VALUES ('CS301','DATABASE SYSTEMS','6-1-2-3','{"CS202","CS201"}');
//INSERT INTO coreElective VALUES('CS301',1,'{"CSE,MNC"}','{"CIV"}');

public class instructor {
    public boolean updateGrades(Connection connection,String username) throws IOException, SQLException {
        System.out.println("enter the course code :\n");
        BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
        String code=reader.readLine();
        String file="";
        System.out.println("Enter the file path to upload the grades");
        file= reader.readLine();
        file="./src/datafeed/grades.csv";
        BufferedReader read = null;
        String line="";

        String  name="";
        String q2=String.format("SELeCT name from users where username='%s'",username);
        Statement stmt=connection.createStatement();
        ResultSet result=stmt.executeQuery(q2);
        while (result.next())
        {
            name=result.getString("name");

        }
       Integer count=0;
        String qery=String.format("select count(*) from course_offering where course_code='%s' and instructor='%s'",code,name);
        Statement stmnt=connection.createStatement();
        ResultSet res=stmnt.executeQuery(qery);
        while(res.next())
        {
            count=res.getInt("count");
        }
        if(count==0)
            return false;

        read=new BufferedReader(new FileReader(file));
        while((line= read.readLine())!=null)
        {
            String[] row=line.split(",");
            String entryNO=row[0];
            String grade=row[1];
            String tabname='s'+entryNO;
            String qry=String.format("UPDATE %s SET grade = '%s' WHERE course_code='%s'",tabname,grade,code);
            PreparedStatement stmt2=connection.prepareStatement(qry);
            stmt2.execute();
            stmt2.close();
        }
        return true;

    }
    public boolean coursesOffered(Connection connection,String username) throws SQLException {
        String toreturn="";
        String name = "";
        String query = String.format("SELECT name FROM users WHERE username='%s'", username);
        Statement stmt = connection.createStatement();
        ResultSet result = stmt.executeQuery(query);
        while (result.next()) {
            name = result.getString("name");
        }
        String qry = String.format("SELECT * FROM course_offering WHERE instructor='%s'",name);
        Statement st = connection.createStatement();
        ResultSet res = st.executeQuery(qry);
        ResultSetMetaData rsmd = res.getMetaData();

        int columnsNumber = rsmd.getColumnCount();

        while (res.next()) {
            for (int i = 1; i <= columnsNumber; i++) {

                System.out.print(res.getString(i) + " ");
                toreturn+=res.getString(i)+" ";

            }
            System.out.println("\n");//Move to the next line to print the next row.
            toreturn+="\n";
        }
       return true;

    }
    public boolean  viewGrades(Connection connection,String username) throws IOException, SQLException {
        String toreturn="";
        System.out.println("Enter the Entry No. of the Student You wanna see the grades\n");
        BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
        String entryno=reader.readLine();
        entryno=entryno.toLowerCase();
        String tabname='s'+entryno;
        boolean isStudentExists=true;

        int count=0;
        String stuqry=String.format("SELECT count(*) FROM students WHERE entry_no='%s'",entryno);
        Statement stmt=connection.createStatement();
        ResultSet result=stmt.executeQuery(stuqry);
        while(result.next())
        {
            count=result.getInt("count");
        }
        if(count==0)
            isStudentExists=false;

        if(isStudentExists) {
            String qry = String.format("SELECT * FROM %s ;", tabname);
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery(qry);
            ResultSetMetaData rsmd = res.getMetaData();

            int columnsNumber = rsmd.getColumnCount();

            while (res.next()) {
                for (int i = 1; i <= columnsNumber; i++) {

                    System.out.print(res.getString(i) + " ");
                    toreturn+=res.getString(i)+" ";

                }


                System.out.println("\n");//Move to the next line to print the next row.
                toreturn+="\n";

            }
            return true;
        }
        else {
            System.out.println("This Student doesn't exists .. Please Enter the correct entry no\n");
            return false;
        }

    }
    public boolean dRegisterCourse(Connection connection,String username) throws IOException, SQLException {
        Integer regDreg=0;
        Integer floatInst=0;
        Integer year=0;
        Integer sem=0;

        String qryt=String.format("SELECT regDreg ,floatInst,year,sem from event");
        Statement stmn=connection.createStatement();
        ResultSet ress=stmn.executeQuery(qryt);
        while(ress.next())
        {
            regDreg=ress.getInt("regDreg");
            floatInst=ress.getInt("floatInst");
            year=ress.getInt("year");
            sem=ress.getInt("sem");

        }
        System.out.println("Enter the course You wanna Deregister\n");
        BufferedReader rdr=new BufferedReader(new InputStreamReader(System.in));
        String code=rdr.readLine();

        String name = "";
        String query = String.format("SELECT name FROM users WHERE username='%s'", username);
        Statement stmt = connection.createStatement();
        ResultSet result = stmt.executeQuery(query);
        while (result.next()) {
            name = result.getString("name");
        }

        boolean isAlready = false;
        int count = 0;
        String qry2 = String.format("SELECT count(*) FROM course_offering WHERE course_code='%s' AND instructor='%s'", code, name);
        Statement stmt3 = connection.createStatement();
        ResultSet rst = stmt3.executeQuery(qry2);
        while (rst.next()) {
            count = rst.getInt("count");
        }
        if (count == 1)
            isAlready = true;
        academicOffice acoff=new academicOffice();
        if(floatInst==1)
        {
            if(isAlready)
            {
                String qry=String.format("DELETE FROM course_offering WHERE course_code='%s' and instructor='%s'",code,name);
                PreparedStatement pstmt=connection.prepareStatement(qry);
                pstmt.execute();
                pstmt.close();
                String qry4=String.format("DELETE FROM %s WHERE course_code='%s'",username,code);
                PreparedStatement pstmt4=connection.prepareStatement(qry4);
                pstmt4.execute();
                pstmt4.close();
                System.out.println("Course DeRegistered Successfully\n");
            }
            else{
                System.out.println("You have not floated this course\n");
            }
        }
        else{
            System.out.println("Course DeRegistration window is not open\n");
        }

return true;

    }
    public void registerCourse(Connection connection,String username) throws IOException, SQLException {
        System.out.println("Enter the course code you want to register\n");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String code = reader.readLine();
        System.out.println("enter cgpa criteria\n");
        String cgp=reader.readLine();
        double cgpa_const =Double.parseDouble(cgp);

        boolean isexist = false;
        Integer count2 = 0;
        String qrye = String.format("SELECT count(*) FROM course_catalog WHERE course_code='%s'", code);
        Statement stmnt = connection.createStatement();
        ResultSet res = stmnt.executeQuery(qrye);
        while (res.next()) {
            count2 = res.getInt("count");
        }
        if (count2 >= 1)
            isexist = true;

        String name = "";
        String query = String.format("SELECT name FROM users WHERE username='%s'", username);
        Statement stmt = connection.createStatement();
        ResultSet result = stmt.executeQuery(query);
        while (result.next()) {
            name = result.getString("name");
        }
        boolean isnotAlready = true;

        int count = 0;
        String qry2 = String.format("SELECT count(*) FROM course_offering WHERE course_code='%s' AND instructor='%s'", code, name);
        Statement stmt3 = connection.createStatement();
        ResultSet rst = stmt3.executeQuery(qry2);
        while (rst.next()) {
            count = rst.getInt("count");
        }
        if (count >= 1)
            isnotAlready = false;
        academicOffice acaoff = new academicOffice();
        if (isexist) {
            if (acaoff.floatInst == 1) {
                if (isnotAlready) {

                    String qry = String.format("SELECT minsem_req,core,elective FROM coreElective WHERE course_code='%s'", code);
                    Statement stmt2 = connection.createStatement();
                    ResultSet result2 = stmt2.executeQuery(qry);
                    while (result2.next()) {
                        int minsem = result2.getInt(1);
                        String core = result2.getString(2);
                        String elective = result2.getString(3);

                        core = core.replace("{", "");
                        core = core.replace("}", "");
                        elective = elective.replace("{", "");
                        elective = elective.replace("}", "");
                        core = core.replace("\"", "");
                        elective = elective.replace("\"", "");

                        String[] cor = core.split(",");
                        String[] elect = elective.split(",");

                        List<String> co = new ArrayList<>();
                        List<String> elec = new ArrayList<>();
                        for (int i = 0; i < cor.length; i++) {
                            co.add(cor[i]);
                        }
                        for (int i = 0; i < elect.length; i++) {
                            elec.add(elect[i]);
                        }


                        Array array = connection.createArrayOf("VARCHAR", co.toArray());
                        Array array2 = connection.createArrayOf("VARCHAR", elec.toArray());

                        PreparedStatement pstmt = connection.prepareStatement("INSERT INTO course_offering VALUES(?,?,?,?,?,?,?)");
                        pstmt.setString(1, code);
                        pstmt.setString(2, name);
                        pstmt.setString(3, username);
                        pstmt.setDouble(4, cgpa_const);
                        pstmt.setInt(5, minsem);
                        pstmt.setArray(6, array);
                        pstmt.setArray(7, array2);

                        pstmt.execute();
                        pstmt.close();
                    }
                } else {
                    System.out.println("You have already floated this course\n");
                }

            } else {
                System.out.println("Course Registration is not open\n");
            }
        }
        else {
            System.out.println("This course does not exist int course catalog\n");
        }
    }

}
