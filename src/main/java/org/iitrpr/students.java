package org.iitrpr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Scanner;

public class students {
    public boolean idNotAreadyCompleted(Connection connection,String username,String code,Integer currsem) throws SQLException {
        String tabname='s'+username;
        Integer count=0;
        String qry=String .format("SELECT count(*) FROM %s WHERE course_code='%s' AND grade<>'F' AND grade<>'NA' AND semester<> %d",tabname,code,currsem);
        Statement stmt=connection.createStatement();
        ResultSet result=stmt.executeQuery(qry);
        while(result.next())
        {
            count=result.getInt("count");
        }
        if(count>=1)
            return false;
        else
        {
            Integer count2=0;
            String qry2=String .format("SELECT count(*) FROM %s WHERE course_code='%s' AND grade='NA' AND semester=%d",tabname,code,currsem);
            Statement stmt2=connection.createStatement();
            ResultSet result2=stmt.executeQuery(qry);
            while(result2.next())
            {
                count2=result2.getInt("count");
            }
            if(count2>0)
                return false;
            else
                return true;
        }

    }
    public void view_grades(Connection connection,String username) throws SQLException, IOException {
        System.out.println("Enter the Semester you wanna see grades\n");
        Scanner s = new Scanner(System.in);
       int sem = s.nextInt();

//        System.out.println(sem);
        String tabname = 's' + username;
        String temp = "";
        String qry = String.format("SELECT course_code,grade FROM %s WHERE semester=%d ", tabname, sem);
        Statement stmt = connection.createStatement();
        ResultSet res = stmt.executeQuery(qry);
        ResultSetMetaData rsmd = res.getMetaData();

        int columnsNumber = rsmd.getColumnCount();

        while (res.next()) {
            for (int i = 1; i <= columnsNumber; i++) {

                System.out.print(res.getString(i) + " ");

            }
            System.out.println("\n\n");//Move to the next line to print the next row.
        }
    }
    public void deregisterCourse(Connection connection,String username) throws IOException, SQLException {

        System.out.println("Enter the Course code and instructor You want to drop\n");
        BufferedReader cc=new BufferedReader(new InputStreamReader(System.in));
        String code=cc.readLine();
        String inst=cc.readLine();
        String tabname='s'+username;
        int count=0;
        if(isCourseFloated(connection,code,inst))
        {
            String qry=String.format("SELECT count(*) FROM %s WHERE course_code='%s' AND grade='NA'",tabname,code);
            Statement stmt=connection.createStatement();
            ResultSet rst=stmt.executeQuery(qry);

            while(rst.next())
            {
                count= rst.getInt("count");
            }
            if(count==0)
            {
                System.out.println("You are not enrolled in this course\n");
            }
            else if(count==1)
            {
                academicOffice acoff=new academicOffice();
                if(acoff.regDreg==1)
                {
                    String query=String.format("DELETE FROM %s WHERE course_code='%s'",tabname,code);
                    PreparedStatement pstmt=connection.prepareStatement(query);
                    pstmt.execute();
                }
            }
        }
        else
        {
            System.out.println("This course is not floated\n");
        }

    }

    public boolean ispreRequisites(Connection connection,String code,String username,Integer currsem) throws SQLException {
        String table='s'+username;
        int count=0;
        String[] prereq={};
        String regex=",";
        String qry=String.format("SELECT prerequisites FROM course_catalog where course_code='%s'",code);
        Statement stmt=connection.createStatement();
        ResultSet res= stmt.executeQuery(qry);

        while(res.next())
        {
            String temp=res.getString("prerequisites");
            temp=temp.replace("{","");
            temp=temp.replace("}","");

            prereq=temp.split(regex);
        }
        String temp="";
        if(prereq.length==1 && prereq[0].equals(""))
            return true;
//        System.out.println(prereq.length);
//        for(int i=0;i< prereq.length;i++)
//        {
//            System.out.println(prereq[i]);
//        }
        for(int i=0;i< prereq.length;i++)
        {
            int count2=0;
            temp=prereq[i];
            String qry2=String.format("SELECT count(*) as count FROM %s WHERE course_code='%s' AND grade<>'F' AND semester<=%d",table,temp,currsem);
            Statement stmt2=connection.createStatement();
            ResultSet rst2=stmt2.executeQuery(qry2);
            while(rst2.next())
            {
                count2=rst2.getInt("count");
            }
            if(count2==0)
                return false;

        }
        return true;
    }
    public boolean isCreditCriteria(Connection connection,String username,int currsem,String code) throws SQLException {
        String tabname='s'+username;
        if(currsem<=2)
        {
            String credit_strct="";
            double totalCredit=0.0;
            String qry1=String.format("SELECT credit_strct FROM course_catalog as cg,%s as sr WHERE sr.course_code=cg.course_code AND sr.semester=%d",tabname,currsem);
            Statement stmt1=connection.createStatement();
            ResultSet rst1= stmt1.executeQuery(qry1);
            while(rst1.next())
            {

                credit_strct= rst1.getString("credit_strct");
                totalCredit+=Integer.parseInt(String.valueOf(credit_strct.charAt(6)));
            }
            String qry4=String.format("SELECT credit_strct FROM course_catalog as cg WHERE cg.course_code='%s'",code);
            Statement stmt4=connection.createStatement();
            ResultSet rst4= stmt4.executeQuery(qry4);
            while(rst4.next())
            {

                credit_strct= rst4.getString("credit_strct");
                totalCredit+=Integer.parseInt(String.valueOf(credit_strct.charAt(6)));
            }

            if(totalCredit<=18)
                return true;
            else if(totalCredit>18)
                return false;
        }
        else
        {
            double sum = 0.0;
            int curr = currsem - 1;
            String credit = "";

            String qry = String.format("SELECT credit_strct FROM course_catalog as cg,%s as sr WHERE sr.course_code=cg.course_code AND sr.semester=%d", tabname, curr);
            Statement stmt = connection.createStatement();
            ResultSet rst = stmt.executeQuery(qry);
            while (rst.next()) {

                credit = rst.getString("credit_strct");
                sum += Integer.parseInt(String.valueOf(credit.charAt(6)));
            }
            int curr2 = currsem - 2;
            String qry2 = String.format("SELECT credit_strct FROM course_catalog as cg,%s as sr WHERE sr.course_code=cg.course_code AND sr.semester=%d", tabname, curr2);
            Statement stmt2 = connection.createStatement();
            ResultSet rst2 = stmt.executeQuery(qry2);
            while (rst2.next()) {

                credit = rst2.getString("credit_strct");
                sum += Integer.parseInt(String.valueOf(credit.charAt(6)));
            }
            String credit_strct2="";
            double totalCredit2=0.0;
            String qry3=String.format("SELECT credit_strct FROM course_catalog as cg,%s as sr WHERE sr.course_code=cg.course_code AND sr.semester=%d",tabname,currsem);
            Statement stmt3=connection.createStatement();
            ResultSet rst3= stmt3.executeQuery(qry3);
            while(rst3.next())
            {

                credit_strct2= rst3.getString("credit_strct");
                totalCredit2+=Integer.parseInt(String.valueOf(credit_strct2.charAt(6)));
            }
            String qry5=String.format("SELECT credit_strct FROM course_catalog as cg WHERE cg.course_code='%s'",code);
            Statement stmt5=connection.createStatement();
            ResultSet rst5= stmt5.executeQuery(qry5);
            while(rst5.next())
            {

                credit_strct2= rst5.getString("credit_strct");
                totalCredit2+=Integer.parseInt(String.valueOf(credit_strct2.charAt(6)));
            }
            if(totalCredit2<=(1.25*(sum/2.0)))
                return true;
            else
                return false;
        }
        return false;

    }
    public double cgpa_calculate(Connection connection,String username) throws SQLException {
         double sgpa=0.0;
         double sum=0.0;
         String tabname='s'+username;
         String credit="";
         double totalcred=0.0;
         String grade="";
         double cred=0.0;

         String qry=String.format("SELECT credit_strct,grade FROM course_catalog as cg,%s st WHERE cg.course_code=st.course_code AND st.grade <> 'NA'",tabname);
         Statement stmt=connection.createStatement();
         ResultSet rst=stmt.executeQuery(qry);
         while(rst.next())
         {
            credit= rst.getString(1);
            grade= rst.getString(2);
            String[] cre=credit.split("-");
            cred=Double.parseDouble(cre[4]);
            totalcred+=cred;

            if(grade.equals("A"))
            {
                sgpa+=(cred*10);
            }
             else if(grade.equals("A-"))
             {
                 sgpa+=(cred*9);
             }
             else if(grade.equals("B"))
             {
                 sgpa+=(cred*8);
             }
            else if(grade.equals("B-"))
             {
                 sgpa+=(cred*7);
             }
             else if(grade.equals("C"))
             {
                 sgpa+=(cred*6);
             }
             else if(grade.equals("C-"))
             {
                 sgpa+=(cred*5);
             }
             else if(grade.equals("D"))
             {
                 sgpa+=(cred*4);
             }
             else if(grade.equals("F"))
             {
                 sgpa+=(cred*0.0);
             }

         }
         if(totalcred==0.0)
             return 0.0;
         double result=sgpa/totalcred;

         return result;
    }
    public boolean isminSemCompleted(Connection connection,String username,String code) throws SQLException {
        Integer syr=0;
        Integer currsem=0;
        String srecord='s'+username;
        academicOffice acaoff=new academicOffice();
        Statement sts=connection.createStatement();
        String q=String.format("SELECT entryyr FROM students WHERE entry_no='%s'",username);
        ResultSet rst=sts.executeQuery(q);
        while(rst.next()) {
            syr = rst.getInt("entryyr");
        }
//                System.out.println(syr);

        currsem=(acaoff.year-syr)*2+acaoff.sem;
//        System.out.println(currsem);
        int minsem=0;
        Statement sts2=connection.createStatement();
        String q2=String.format("SELECT minsem_req from course_offering where course_code='%s'",code);
        ResultSet rst2=sts.executeQuery(q2);
        while(rst2.next()) {
            minsem = rst2.getInt("minsem_req");
        }
        System.out.println(minsem);
        if(currsem>minsem) {
            return true;
        }
        else {
            return false;
        }
    }
    public boolean isCourseFloated(Connection connection,String code,String inst) throws SQLException {
        Integer count=0;

        Statement stmt=connection.createStatement();
        String query=String.format("select count(*) as count from course_offering where course_code='%s' AND instructor='%s'",code,inst);
        ResultSet result=stmt.executeQuery(query);
        while(result.next())
        {
            count=result.getInt("count");
        }
        if(count==1)
            return true;

        return false;
    }
    public void enroll_course(Connection connection,String username)throws IOException, SQLException
    {

        academicOffice acaoff=new academicOffice();

        String srecord='s'+username;
        System.out.println("Select the course from below list ot register\n");
        System.out.println("----------------------------------------------------------------------------------------------------------\n");
//        System.out.println("CODE    INSTRUCTOR  CGPA_REQUIRED    MINSEM_REQUIRED    CORE    ELECTIVES\n");
        String qry="SELECT course_code,instructor,cgpa_const,minsem_req,core,elective FROM course_offering ;";
        Statement st = connection.createStatement();
        ResultSet res = st.executeQuery(qry);
        ResultSetMetaData rsmd = res.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while (res.next()) {
            for(int i = 1 ; i <= columnsNumber; i++){

                System.out.print(res.getString(i) + "   ");

            }
            System.out.println("\n");
        }
        System.out.println("----------------------------------------------------------------------------------------------------------\n");

        System.out.println("Enter the Course code and instructor name\n");
        BufferedReader cc=new BufferedReader(new InputStreamReader(System.in));
        String code=cc.readLine();
        String inst=cc.readLine();


        Integer syr=0;
        Integer currsem=0;
        String department="";
        String studentName="";
        Statement sts=connection.createStatement();
        String q=String.format("SELECT entryyr,name,dept FROM students WHERE entry_no='%s'",username);
        ResultSet rst=sts.executeQuery(q);
        while(rst.next()) {
            syr = rst.getInt("entryyr");
            studentName=rst.getString("name");
            department=rst.getString("dept");
        }
//                System.out.println(syr);

        currsem=(acaoff.year-syr)*2+acaoff.sem;
//        System.out.println(currsem);

        double cgpa_req=0.0;
        String instabname="";
        Statement sts2=connection.createStatement();
        String q2=String.format("SELECT cgpa_const,username FROM course_offering WHERE course_code='%s' AND instructor='%s'",code,inst);
        ResultSet rst2=sts.executeQuery(q2);
        while(rst2.next()) {
            cgpa_req = rst2.getDouble("cgpa_const");
            instabname=rst2.getString("username");
        }

        String core="";
        String elective="";
        String qery=String.format("SELECT core,elective FROM coreelective as ce WHERE ce.course_code='%s'",code);
        Statement state=connection.createStatement();
        ResultSet result=state.executeQuery(qery);
        while(result.next())
        {
            core=result.getString("core");
            elective=result.getString("elective");
        }
        core=core.replace("}","");
        core=core.replace("{","");

        String[] coreBranches=core.split(",");
        elective=elective.replace("}","");
        elective=elective.replace("{","");

        String[] elecBranches=elective.split(",");

        boolean iscore=false;
        boolean iselective=false;

        for(int i=0;i<coreBranches.length;i++)
        {
            if(coreBranches[i].equals(department)) {
                iscore = true;
                break;
            }
        }
        for(int i=0;i<elecBranches.length;i++)
        {
            if(elecBranches[i].equals(department)) {
                iselective = true;
                break;
            }
        }

        if(isCourseFloated(connection,code,inst)) {
            if (idNotAreadyCompleted(connection, username, code,currsem)) {
                if (acaoff.regDreg == 1) {
                    if (isminSemCompleted(connection, username, code)) {
                        if (currsem == 1 || (currsem != 1 && cgpa_calculate(connection, username) >= cgpa_req)) {
                            if (ispreRequisites(connection, code, username, currsem)) {
                                if (isCreditCriteria(connection, username, currsem, code)) {

                                    if (iscore || iselective) {
                                        String course_name = "";
                                        Statement sts3 = connection.createStatement();
                                        String q4 = String.format("SELECT course_name from course_catalog where course_code='%s'", code);
                                        ResultSet rst3 = sts3.executeQuery(q4);
                                        while (rst3.next()) {
                                            course_name = rst3.getString("course_name");
                                        }

                                        String q3 = String.format("insert into %s values(?,?,?,?,?)", srecord);
                                        PreparedStatement pstmt = connection.prepareStatement(q3);
                                        pstmt.setString(1, code);
                                        pstmt.setString(2, course_name);
                                        pstmt.setInt(3, currsem);
                                        pstmt.setString(4, "NA");
                                        if (iscore)
                                            pstmt.setString(5, "core");
                                        else if (iselective)
                                            pstmt.setString(5, "elective");
                                        pstmt.execute();
                                        pstmt.close();

                                        String qinst = String.format("INSERT INTO %s VALUES(?,?,?)", instabname);
                                        PreparedStatement pstmt2 = connection.prepareStatement(qinst);
                                        pstmt2.setString(1, code);
                                        pstmt2.setString(2, studentName);
                                        pstmt2.setString(3, username);
                                        pstmt2.execute();
                                        pstmt2.close();

                                    } else {
                                        System.out.println("This course is not floated for your Branch\n");
                                    }
                                } else {
                                    System.out.println("You can't enroll this course.. Credit limit Exceeded\n");
                                }
                            } else {
                                System.out.println("You haven't completed the preRequisites for this course\n");
                            }
                        } else {
                            System.out.println("CGPA criteria is not fulfilled\n");
                        }
                    } else {
                        System.out.println("You haven't completed the minimum semester required for this course\n");
                    }

                } else {
                    System.out.println("Course enroll and drop window is not open\n");
                }
            }
            else {
                System.out.println("you have either passed this course or already registered for this course\n");
            }
        }
        else
        {
            System.out.println("This course is not floated\n");
        }

    }
//    public static void main(String[] args) throws SQLException {
//        databaseSeeder ds=new databaseSeeder();
//        Connection connection=ds.connect();
//        students st=new students();
////        System.out.println(st.ispreRequisites(connection,"HS104","2020CSB1098",1));
////        System.out.println(st.cgpa_calculate(connection,"2020CSB1098"));
////        System.out.println(st.isminSemCompleted(connection,"2020CSB1098","HS104"));
////        System.out.println(st.ispreRequisites(connection,"HS104","2020CSB1098"));
////        System.out.println(st.sumofcredits(connection,"2020CSB1098",3));
//    }
}
