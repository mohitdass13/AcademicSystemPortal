package org.iitrpr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class academicOffice {
    public int regDreg=1;
    public int floatInst=1;
    public int year=2020;
    public int sem=1;
    public void addNewCourse(Connection connection) throws SQLException, IOException {
        System.out.println("Enter course code\n");
        BufferedReader reader2=new BufferedReader(new InputStreamReader(System.in));
        String Coursecode=reader2.readLine();
        System.out.println("Enter course name\n");
        String courseName=reader2.readLine();
        System.out.println("Enter credit Structure\n");
        String creditStruct=reader2.readLine();
        System.out.println("Enter Prerequisites\n");
        String preReq=reader2.readLine();
        System.out.println("Enter minsem required\n");
        Scanner scan=new Scanner(new InputStreamReader(System.in));
        Integer minsem= scan.nextInt();
        preReq=preReq.replace("\"","");
        String[] pre = preReq.split(",");

        List<String> preRequis = new ArrayList<>();
        if(!preReq.equals(""))
        {
            for (int i = 0; i < pre.length; i++) {
                preRequis.add(pre[i]);
            }
        }
        Array array = connection.createArrayOf("VARCHAR", preRequis.toArray());

        String qry=String.format("INSERT INTO course_catalog VALUES(?,?,?,?)");
        PreparedStatement pstmt=connection.prepareStatement(qry);
        pstmt.setString(1,Coursecode);
        pstmt.setString(2,courseName);
        pstmt.setString(3,creditStruct);
        pstmt.setArray(4,array);
        pstmt.execute();
        pstmt.close();

    }
    public void generateTranscripts(Connection connection) throws IOException, SQLException {
        System.out.println("Enter the Entry NO of the Student\n");
        BufferedReader bfr=new BufferedReader(new InputStreamReader(System.in));
        String entryNo=bfr.readLine();

        String tabname='s'+entryNo;
        String name="";
        String dept="";
        String code="";
        String coName="";
        String grade="";
        String credit="";
        String credits="";
        String department="";

        String qry=String.format("SELECT name,dept FROM students WHERE entry_no='%s'",entryNo);
        Statement stmt=connection.createStatement();
        ResultSet result=stmt.executeQuery(qry);
        while(result.next())
        {
            name=result.getString("name");
            dept= result.getString("dept");
        }
        System.out.println(dept);
        if(dept.equals("CIV"))
        {
            department="CIVIL ENGINEERING";
        }
        else if(dept.equals("CSE"))
        {
            department="COMPUTER SCIENCE AND ENGINEERING";
        }
        else if(dept.equals("MEC"))
        {
            department="MECHANICAL ENGINEERING";
        }
        else if(dept.equals("MTH"))
        {
            department="MATHEMATICS AND COMPUTING";
        }
        else if(dept.equals("CHE"))
        {
            department="CHEMICAL ENGINEERING";
        }
        else if(dept.equals("ELE"))
        {
            department="ELECTRICAL ENGINEERING";
        }
        Integer maxsem=0;
        String qryt=String.format("SELECT max(semester) FROM %s",tabname);
        Statement stmtt=connection.createStatement();
        ResultSet rest= stmtt.executeQuery(qryt);
        while(rest.next())
        {
            maxsem=rest.getInt("max");
        }
        System.out.println(department);
        System.out.println("\n\n");
        System.out.println("                INDIAN INSTITUTE of TECHNOLOGY ROPAR\n");
        System.out.println("                        Semester Grade Report\n");
        System.out.printf("Name:            %s\n",name);
        System.out.printf("Entry No:        %s\n",entryNo);
        System.out.printf("Programme:      B.TECH in %s\n\n",department);
        System.out.println("----------------------------------------------------------------------------------------------------------\n");
        System.out.println("Course Code             Course Name             Credits             Grade\n");
        System.out.println("----------------------------------------------------------------------------------------------------------\n");

        String[] credt={};
        String query=String.format("SELECT course_code,course_name,grade FROM %s",tabname);
        Statement stmt2=connection.createStatement();
        ResultSet result2=stmt2.executeQuery(query);
        while(result2.next())
        {
            code=result2.getString("course_code");
            coName=result2.getString("course_name");
            grade=result2.getString("grade");
            String qry2 = String.format("SELECT credit_strct FROM course_catalog WHERE course_code='%s'",code);
            Statement stmt3 = connection.createStatement();
            ResultSet rst = stmt3.executeQuery(qry2);
            while (rst.next()) {

                credit = rst.getString("credit_strct");
                credt=credit.split("-");
                credits=credt[3];
            }
            System.out.printf("%s              %s              %s              %s\n",code,coName,credits,grade);
        }
        System.out.println("\n\n");
    }

    public void viewGradesAll(Connection connection) throws IOException, SQLException {
        System.out.println("Enter the Entry No. of the Student You wanna see the grades\n");
        BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
        String entryno=reader.readLine();

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

                }


                System.out.println("\n");//Move to the next line to print the next row.

            }
        }
        else {
            System.out.println("This Student doesn't exists .. Please Enter the correct entry no\n");
        }

    }

    public void addStudent(Connection connection,String name,String email,String entryno,String dept,Integer entryyr)
    {
        try{
            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO students VALUES(?, ?, ?, ?, ?)");
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, entryno);
            pstmt.setString(4, dept);
            pstmt.setInt(5, entryyr);
            pstmt.execute();
            pstmt.close();
            System.out.printf("A new student has been added to the system with name %s\n",name);
//            Character c=name.charAt(0);
            String name2='s'+entryno;
            String qry=String.format("CREATE TABLE %s (course_code VARCHAR(6),course_name VARCHAR(30),semester INTEGER,grade VARCHAR(2),course_type VARCHAR(20))",name2);
            PreparedStatement pstmt2=connection.prepareStatement(qry);
            pstmt2.execute();
            pstmt2.close();
            System.out.println("Student Record Created Successfully!!");

        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }


    }
}