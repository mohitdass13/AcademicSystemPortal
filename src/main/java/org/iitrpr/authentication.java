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
        students student = new students();

        boolean exit=false;
//        while (true) {
            System.out.println("Enter Username:");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String name = reader.readLine();
            System.out.println("\n");
            System.out.println("Enter Password: ");
            String pass = reader.readLine();

            Integer count = 0;
            String role = "";
            Statement stmt3 = connection.createStatement();
            String query = String.format("SELECT role,count(*) AS count FROM users WHERE users.username='%s' AND users.password='%s' GROUP BY users.role ", name, pass);
            ResultSet rs = stmt3.executeQuery(query);
            while (rs.next()) {
                count = rs.getInt("count");
                role = rs.getString("role");
            }

            if (count == 1) {
                System.out.println("Logged IN Successfully!!\n");

                if (role.equals("student")) {
                    name = name.toLowerCase();
                    while (true) {

                        System.out.println("------------------------------------------------------------------------------------------------------------------------\n");
                        System.out.println("Choose the Appropriate Action:\n1. Register a course\n2.Deregister a Course\n3.View Grades\n4.Calculate CGPA\n5.Logout");
                        System.out.println("------------------------------------------------------------------------------------------------------------------------\n");
//                    BufferedReader option = new BufferedReader(new InputStreamReader(System.in));
                        String opt = reader.readLine();
                        if (opt.equals("1")) {
                            student.enroll_course(connection, name);
                        } else if (opt.equals("2")) {
//                            students student = new students();
                            student.deregisterCourse(connection, name);
                        } else if (opt.equals("3")) {
//                            students st = new students();
                            student.view_grades(connection, name);
                        } else if (opt.equals("4")) {
//                            students st = new students();
                            System.out.println("------------------------------------------\n");
                            System.out.println("Your current CGPA is :  ");
                            System.out.println(student.cgpa_calculate(connection, name));
                            System.out.println("------------------------------------------\n");
                        } else if (opt.equals("5"))
                            break;
                    }

                } else if (role.equals("instructor")) {
                    while (true) {
                        instructor inst = new instructor();
                        System.out.println("------------------------------------------------------------------------------------------------------------------------\n");
                        System.out.println("Choose the Appropriate Action:\n1. Register a course\n2.Deregister a Course\n3.View Grades of students\n4.Courses Offered\n5.upload Grades\n6.Logout");
                        System.out.println("------------------------------------------------------------------------------------------------------------------------\n");
//                    BufferedReader option = new BufferedReader(new InputStreamReader(System.in));
                        String opt = reader.readLine();
                        if (opt.equals("6"))
                            break;
                        else if (opt.equals("1")) {
                            inst.registerCourse(connection, name);

                        } else if (opt.equals("2")) {

                            inst.dRegisterCourse(connection, name);
                        } else if (opt.equals("4")) {
                            inst.coursesOffered(connection, name);
                        } else if (opt.equals("3")) {
                            inst.viewGrades(connection, name);
                        } else if (opt.equals("5")) {
                            inst.updateGrades(connection, name);
                        }
                    }
                } else if (role.equals("academics")) {
                    while (true) {
                        System.out.println("------------------------------------------------------------------------------------------------------------------------\n");
                        System.out.println("Choose the Appropriate Action:\n1.Edit Course Catalog\n2.View grades of students\n3.Generate Transcripts\n4.Open/close an event\n5.Start New Acdemic Semester\n6.Check Graduation of a student\n7.Logout");
                        System.out.println("------------------------------------------------------------------------------------------------------------------------\n");
//                        BufferedReader reader1 = new BufferedReader(new InputStreamReader(System.in));
                        String option = reader.readLine();
                        if (option.equals("1")) {
                            System.out.println("Choose Activity\n1.Add a new course\n2.Edit an Existing course\n");
//                            BufferedReader rdr = new BufferedReader(new InputStreamReader(System.in));
                            String option2 = reader.readLine();
                            if (option2.equals("1")) {
                                boolean res = acoff.addNewCourse(connection);
                            } else if (option2.equals("2")) {
                                acoff.addNewCourse(connection);
                            }
                        } else if (option.equals("2")) {

                            acoff.viewGradesAll(connection);
                        } else if (option.equals("3")) {
                            acoff.generateTranscripts(connection);
                        } else if (option.equals("4")) {
                            acoff.setEvent(connection);
                        } else if (option.equals("5")) {
                            acoff.startNewSemester(connection);
                        } else if (option.equals("6")) {
                            String out = acoff.isGraduated(connection);
                        } else if (option.equals("7")) {
                            exit=true;
                            System.out.println(exit);
                            break;

                        }
                    }
                }
            } else {
                System.out.println("User Not Found!!!!!\n");
            }
//            if(exit)
//                break;
//        }
    }
}
