package org.iitrpr;

import org.w3c.dom.ls.LSOutput;

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

    public void startNewSemester(Connection connection) throws SQLException
    {
        System.out.println(year);
        System.out.println(sem);
        if(sem==1)
            sem++;
        else if(sem==2)
        {
            year++;
            sem=1;
        }
        String qry=String.format("DELETE FROM course_offering");
        Statement stmn=connection.createStatement();
        stmn.execute(qry);

    }
    public void setEvent(Connection connection) throws IOException, SQLException {
        System.out.println("1.Open Student course Add/Drop\n2.Open Faculty Course Add/Drop \n3.Close Student course Add/Drop\n4.Close faculty Course Add/Drop\n");
        BufferedReader bfr=new BufferedReader(new InputStreamReader(System.in));
        String op=bfr.readLine();
        if(op.equals("1"))
        {
            regDreg=1;
            floatInst=0;
            String qry=String.format("UPDATE event SET regDreg=1 ,floatInst=0");
            Statement stm=connection.createStatement();
            stm.execute(qry);
        }
        if(op.equals("2"))
        {
            regDreg=0;
            floatInst=1;
            String qry=String.format("UPDATE event SET regDreg=0 ,floatInst=1");
            Statement stm=connection.createStatement();
            stm.execute(qry);
        }
        if(op.equals("3"))
        {
            regDreg=0;
            String qry=String.format("UPDATE event SET regDreg=0");
            Statement stm=connection.createStatement();
            stm.execute(qry);
        }
        if(op.equals("4"))
        {
            floatInst=0;
            String qry=String.format("UPDATE event SET floatInst=0");
            Statement stm=connection.createStatement();
            stm.execute(qry);
        }

    }
    public String isGraduated(Connection connection) throws IOException, SQLException {
        System.out.println("Enter the entry no\n");
        BufferedReader bfr=new BufferedReader(new InputStreamReader(System.in));
        String entryNO=bfr.readLine();

        Integer countt=0;

        String qry=String.format("select count(*) from students where entry_no='%s'",entryNO);
        Statement stmte=connection.createStatement();
        ResultSet res=stmte.executeQuery(qry);
        while(res.next())
        {
            countt=res.getInt("count");
        }
        if(countt==0)
            return "this student doesn't exist!!";
        boolean isCapstoneDone=false;
        double coreCredits=0.0;
        double electiveCredits=0.0;

        String tabname='s'+entryNO;

        String credit="";
        String[] credt={};
        String credits="";
        String qry2 = String.format("SELECT credit_strct FROM course_catalog as cc,%s as st WHERE cc.course_code=st.course_code AND course_type='core' AND st.grade<>'F' AND st.grade<>'NA'",tabname);
        Statement stmt3 = connection.createStatement();
        ResultSet rst = stmt3.executeQuery(qry2);
        while (rst.next()) {

            credit = rst.getString("credit_strct");
            credt = credit.split("-");
            credits = credt[4];
            coreCredits+=Double.parseDouble(credits);

        }
        String qry3 = String.format("SELECT credit_strct FROM course_catalog as cc,%s as st WHERE cc.course_code=st.course_code AND course_type='elective' AND st.grade<>'F' AND st.grade<>'NA'",tabname);
        Statement stmt4 = connection.createStatement();
        ResultSet rst2 = stmt4.executeQuery(qry2);
        while (rst2.next()) {

            credit = rst2.getString("credit_strct");
            credt = credit.split("-");
            credits = credt[4];
            electiveCredits+=Double.parseDouble(credits);
        }

        Integer count=0;
        String query=String.format("SELECT count(*) FROM %s WHERE course_code='CP301' AND grade<>'F' AND grade<>'NA'",tabname);
        Statement stmt=connection.createStatement();
        ResultSet result=stmt.executeQuery(query);
        while(result.next())
        {
            count=result.getInt("count");
        }
        if(count>=1) {
            isCapstoneDone = true;
        }
        if(coreCredits>=4 && electiveCredits>=1.5 && isCapstoneDone)
        {
            System.out.println("Yes!!!     This Guy is able to be graduate\n");
            return "Yes!!!     This Guy is able to be graduate";
        }
        else {
            System.out.println("NO!!! This guy has not completed the required credits for graduation\n");
            return "NO!!! This guy has not completed the required credits for graduation";
        }

    }

    public boolean addNewCourse(Connection connection) throws SQLException, IOException {
        System.out.println("Enter course code\n");
        BufferedReader reader2=new BufferedReader(new InputStreamReader(System.in));
        String Coursecode=reader2.readLine();
        System.out.println("Enter course name\n");
        String courseName=reader2.readLine();
        System.out.println("Enter credit Structure\n");
        String creditStruct=reader2.readLine();
        System.out.println("Enter Prerequisites\n");
        String preReq=reader2.readLine();
        System.out.println("Enter minimum semester required to register the course\n");
        String minsemester= reader2.readLine();
        System.out.println("Enter the batch onwards\n");
        String batchS=reader2.readLine();
        System.out.println("Enter the branches for which this course would be program core\n");
        String core=reader2.readLine();
        System.out.println("Enter the branches for which this course is elective\n");
        String elective=reader2.readLine();

        Integer minsem=Integer.parseInt(minsemester);
        Integer batch=Integer.parseInt(batchS);
        preReq=preReq.replace("\"","");
        String[] pre = preReq.split(",");

        core=core.replace("\"","");
        String[] cores = core.split(",");

        elective=elective.replace("\"","");
        String[] electives = elective.split(",");

        List<String> preRequis = new ArrayList<>();
        if(!preReq.equals(""))
        {
            for (int i = 0; i < pre.length; i++) {
                preRequis.add(pre[i]);
            }
        }
        Array array = connection.createArrayOf("VARCHAR", preRequis.toArray());
        Integer count=0;
        String qryt=String.format("SELECT count(*) FROM course_catalog WHERE course_code='%s' AND batch_onward=%d",Coursecode,batch);
        Statement stmnt=connection.createStatement();
        ResultSet result=stmnt.executeQuery(qryt);
        while(result.next())
        {
            count=result.getInt("count");
        }
        if(count>=1)
            return false;
        String qry=String.format("INSERT INTO course_catalog VALUES(?,?,?,?,?)");
        PreparedStatement pstmt=connection.prepareStatement(qry);
        pstmt.setString(1,Coursecode);
        pstmt.setString(2,courseName);
        pstmt.setString(3,creditStruct);
        pstmt.setArray(4,array);
        pstmt.setInt(5,batch);
        pstmt.execute();
        pstmt.close();

        List<String> corel = new ArrayList<>();
        if(!core.equals(""))
        {
            for (int i = 0; i < cores.length; i++) {
                corel.add(cores[i]);
            }
        }
        Array arr1 = connection.createArrayOf("VARCHAR", corel.toArray());

        List<String> electivel = new ArrayList<>();
        if(!elective.equals(""))
        {
            for (int i = 0; i < electives.length; i++) {
                electivel.add(electives[i]);
            }
        }


        Array arr2 = connection.createArrayOf("VARCHAR", electivel.toArray());

        String qry2=String.format("INSERT INTO coreelective values(?,?,?,?)");
        PreparedStatement pstmt2=connection.prepareStatement(qry2);
        pstmt2.setString(1,Coursecode);
        pstmt2.setInt(2,minsem);
        pstmt2.setArray(3,arr1);
        pstmt2.setArray(4,arr2);
        pstmt2.execute();
        pstmt2.close();

        return true;

    }
    public String generateTranscripts(Connection connection) throws IOException, SQLException {
        generateTxtfiles gtxt=new generateTxtfiles();
        String Transcript="";
        System.out.println("Enter the Entry NO of the Student\n");
        BufferedReader bfr=new BufferedReader(new InputStreamReader(System.in));
        String entryNo=bfr.readLine();
        System.out.println("Enter the semester for which you want to generate transcript:\n");
        String semester= bfr.readLine();
        Integer sem=Integer.parseInt(semester);
        System.out.println(sem);

        Integer count=0;

        String qry=String.format("select count(*) from students where entry_no='%s'",entryNo);
        Statement stmte=connection.createStatement();
        ResultSet res=stmte.executeQuery(qry);
        while(res.next())
        {
            count=res.getInt("count");
        }
        if(count==0)
            return "this student doesn't exist!!";
        entryNo=entryNo.toLowerCase();
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
//
//        Integer maxsem=0;
//        String qryt=String.format("SELECT max(semester) FROM %s",tabname);
//        Statement stmtt=connection.createStatement();
//        ResultSet rest= stmtt.executeQuery(qryt);
//        while(rest.next())
//        {
//            maxsem=rest.getInt("max");
//        }
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
            String[] credt = {};
            double registeredCredits=0.0;
            String query = String.format("SELECT course_code,course_name,grade FROM %s where semester=%d", tabname,sem);
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
                    credits = credt[4];
                    registeredCredits+=Double. parseDouble(credits);

                }
                System.out.printf("%s              %s              %s              %s\n", code, coName, credits, grade);
                Transcript+=("              "+code+"              "+coName+"              "+credits+"              "+grade+"\n");
            }
        System.out.println("\n\n");
        gtxt.generateTranscript(entryNo,Transcript);

        return "Transcript generated Successfully";

    }

    public String viewGradesAll(Connection connection) throws IOException, SQLException {
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

                }
                System.out.println("\n");

            }
        }
        else {
            System.out.println("This Student doesn't exists .. Please Enter the correct entry no\n");
        }
        return "These are the grades";

    }

    public boolean addStudent(Connection connection,String name,String email,String entryno,String dept,Integer entryyr) throws SQLException {

        Integer count=0;
        String qryt=String.format("SELECT count(*) FROM students WHERE entry_no='%s'",entryno);
        Statement stmnt=connection.createStatement();
        ResultSet result=stmnt.executeQuery(qryt);
        while(result.next())
        {
            count=result.getInt("count");
        }
        if(count>=1)
            return false;

            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO students VALUES(?, ?, ?, ?, ?)");
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, entryno);
            pstmt.setString(4, dept);
            pstmt.setInt(5, entryyr);
            pstmt.execute();
            pstmt.close();
            System.out.printf("A new student has been added to the system with name %s\n",name);
            String name2='s'+entryno;
            String qry=String.format("CREATE TABLE %s (course_code VARCHAR(6),course_name VARCHAR(30),semester INTEGER,grade VARCHAR(2),course_type VARCHAR(20))",name2);
            PreparedStatement pstmt2=connection.prepareStatement(qry);
            pstmt2.execute();
            pstmt2.close();
            System.out.println("Student Record Created Successfully!!");


            return true;

        }

}