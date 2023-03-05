package org.iitrpr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class academicOffice {
    public int regDreg=1;
    public int floatInst=1;
    public int year=2020;
    public int sem=1;
    public static void generateTranscript(String entryNo, String Transcript) {
        String home = System.getProperty("user.home");
        String os = System.getProperty("os.name").toLowerCase();
        String dDir;
        if (os.contains("win")) {
            dDir = "\\Documents\\AcademicSystem\\";
        } else if (os.contains("mac")) {
            dDir = "/Documents/AcademicSystem/";
        } else {
            dDir = "/Documents/AcademicSystem/";
        }

        String fullPath = home + dDir + "Transcripts/";
        File folder = new File(fullPath);
        folder.mkdirs();
        String fileName = String.format("%s_Transcript", entryNo);
        File file = new File(fullPath + fileName + ".txt");

        if (file.exists()) {
            int i = 1;
            do {
                file = new File(fullPath + fileName+"(" + i + ").txt");
                i++;
            } while (file.exists());
        }



        try {
            boolean success = file.createNewFile();
            if (success) {
            } else {
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating the file.");
            e.printStackTrace();
        }

        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            writer.write(Transcript);

            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
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
        String Transcript="";
        System.out.println("Enter the Entry NO of the Student\n");
        BufferedReader bfr=new BufferedReader(new InputStreamReader(System.in));
        String entryNo=bfr.readLine();

        String tabname='s'+entryNo;
        String code="";
        String coName="";
        String grade="";
        String credit="";
        String credits="";
        String department="";
        String name="";
        String dept="";
        String qury=String.format("SELECT name,dept FROM students WHERE entry_no='%s'",entryNo);
        Statement stmt=connection.createStatement();
        ResultSet result=stmt.executeQuery(qury);
        while(result.next())
        {
            name=result.getString("name");
            dept=result.getString("dept");
        }

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
        System.out.println("                         Grade Report\n");
        System.out.printf("Name:            %s\n",name);
        System.out.printf("Entry No:        %s\n",entryNo);
        System.out.printf("Programme:      B.TECH in %s\n\n",department);
        System.out.println("----------------------------------------------------------------------------------------------------------\n");
        System.out.println("Course Code             Course Name             Credits             Grade\n");
        System.out.println("----------------------------------------------------------------------------------------------------------\n");
        Transcript+=("                                                  INDIAN INSTITUTE of TECHNOLOGY ROPAR\n"+"                                                  Semester Grade Report\n\n"+"Name:            "+name+"\n"+
                "Entry No:        "+entryNo+"Programme:      B.TECH in "+department+"\n----------------------------------------------------------------------------------------------------------\n"+
                "Course Code             Course Name             Credits             Grade\n"+"\n----------------------------------------------------------------------------------------------------------\n");
        while(maxsem>=1) {
            String[] credt = {};
            double registeredCredits=0.0;
//            System.out.println("----------------------------------------------------------------------------------------------------------\n");
//            System.out.printf("Registered Credits: %f\n",registeredCredits);
//            System.out.println("----------------------------------------------------------------------------------------------------------\n");
            String query = String.format("SELECT course_code,course_name,grade FROM %s where semester=%d", tabname,maxsem);
            Statement stmt2 = connection.createStatement();
            ResultSet result2 = stmt2.executeQuery(query);
            while (result2.next()) {
                code = result2.getString("course_code");
                coName = result2.getString("course_name");
                grade = result2.getString("grade");
                String qry2 = String.format("SELECT credit_strct FROM course_catalog WHERE course_code='%s'", code);
                Statement stmt3 = connection.createStatement();
                ResultSet rst = stmt3.executeQuery(qry2);
                while (rst.next()) {

                    credit = rst.getString("credit_strct");
                    credt = credit.split("-");
                    credits = credt[3];
                    registeredCredits+=Double. parseDouble(credits);

                }
                System.out.printf("%s              %s              %s              %s\n", code, coName, credits, grade);
                Transcript+=("              "+code+"              "+coName+"              "+credits+"              "+grade+"\n");
            }

            maxsem--;
        }
        System.out.println("\n\n");
        generateTranscript(entryNo,Transcript);

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