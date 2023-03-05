package org.iitrpr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class authentication {
    public void login(Connection connection) throws IOException, SQLException {
        academicOffice acoff = new academicOffice();
        System.out.println("Enter Username:");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String name = reader.readLine();
//        System.out.println(name);
        System.out.println("\n");
        System.out.println("Enter Password: ");
        String pass= reader.readLine();
//        System.out.println(pass);
//        String te="count";
        Integer count=0;
        String role="";
        Statement stmt3 = connection.createStatement();
        String query=String.format("SELECT role,count(*) AS count FROM users WHERE users.username='%s' AND users.password='%s' GROUP BY users.role ",name,pass);
        ResultSet rs=stmt3.executeQuery(query);
        while(rs.next()){
            count = rs.getInt("count");
            role=rs.getString("role");
        }

        if(count==1)
        {
            System.out.println("Logged IN Successfully!!\n");

            if(role.equals("student"))
            {
                while(true) {
                    System.out.println("Choose the Appropriate Action:\n1. Register a course\n2.Deregister a Course\n3.View Grades\n4.Calculate CGPA\n5.Logout");
                    System.out.println("--------------------------------------------------------------------------------");
                    BufferedReader option = new BufferedReader(new InputStreamReader(System.in));
                    String opt = option.readLine();
                    if(opt.equals("5"))
                        break;
                    else if(opt.equals("1"))
                    {
                       students student=new students();
                       student.enroll_course(connection,name);
                    }
                    else if(opt.equals("2"))
                    {
                        students student=new students();
                        student.deregisterCourse(connection,name);
                    }
                    else if(opt.equals("4"))
                    {
//                        BufferedReader rdr=new BufferedReader(new InputStreamReader(System.in));
                        students st=new students();
                        System.out.println(st.cgpa_calculate(connection,name));
                    }
                    else if(opt.equals("3"))
                    {
                        students st=new students();
                        st.view_grades(connection,name);
                    }
                }

            }
            else if(role.equals("instructor"))
            {
                while(true)
                {
                    System.out.println("Choose the Appropriate Action:\n1. Register a course\n2.Deregister a Course\n3.View Grades of students\n4.Courses Offered\n5.Logout");
                    System.out.println("--------------------------------------------------------------------------------");
                    BufferedReader option = new BufferedReader(new InputStreamReader(System.in));
                    String opt = option.readLine();
                    if(opt.equals("5"))
                        break;
                    else if(opt.equals("1"))
                    {
                       instructor inst=new instructor();
                       inst.registerCourse(connection,name);

                    }
                    else if(opt.equals("2"))
                    {
                        instructor inst=new instructor();
                        inst.dRegisterCourse(connection,name);
                    }
                    else if(opt.equals("4"))
                    {
                        instructor inst=new instructor();
                        inst.coursesOffered(connection,name);
                    }
                    else if(opt.equals("3"))
                    {
                        instructor inst=new instructor();
                        inst.viewGrades(connection,name);
                    }
                }
            }
            else if(role.equals("academics")) {
                while (true) {
                    System.out.println("Choose the Appropriate Action:\n1.Edit Course Catalog\n2.View grades of students\n3.Generate Transcripts\n4.Logout");
                    System.out.println("--------------------------------------------------------------------------------");
                    BufferedReader reader1 = new BufferedReader(new InputStreamReader(System.in));
                    String option = reader1.readLine();
                    if (option.equals("1")) {
                        System.out.println("Choose Activity\n1.Add a new course\n2.Edit an Existing course\n");
                        BufferedReader rdr = new BufferedReader(new InputStreamReader(System.in));
                        String option2 = rdr.readLine();
                        if (option2.equals("1")) {
                            acoff.addNewCourse(connection);
                        }
                    }
                    else if(option.equals("4")) {
                        break;
                    }
                    else if(option.equals("2")) {

                     acoff.viewGradesAll(connection);
                    }
                    else if(option.equals("3"))
                    {
                        acoff.generateTranscripts(connection);
                    }
                }
            }
        }
    }
}
