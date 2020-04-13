package com.company;

import java.sql.*;
import java.util.Scanner;

public class Main {
    String host = "mdcjava2.c9pjtvninqm1.us-west-2.rds.amazonaws.com";
    String db = "mdcjava2";
    String user = "mdcjava2";
    String pass = "password";
    Connection conMe;
    Main() throws Exception{
        driverTest();
        this.conMe = makeCon(this.host, this.db, this.user, this.pass);
    }
    public void printAll() throws Exception {
        /* now run a select query of the intended database */
        exeQuery(conMe, generatePrintAll());
    }

    protected void driverTest() throws Exception {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver Found");
        } catch(java.lang.ClassNotFoundException e){
            System.out.println("Drive Not Found");
            throw(e);
        }
    }

    public static String generatePrintAll(){
        return "SELECT * FROM Students";
    }

    public static String generateInsert(String name, int age, double gpa){
        return "INSERT INTO Students (studentName, age, gpa) " + "VALUES ('" + name + "','" + age + "','" + gpa + "')";
    }

    public static String generateUpdateById(int id, String newName, int newAge, double newGPA ){
        return "UPDATE Students SET studentName='" + newName + "',age='" + newAge + "',gpa='" + newGPA + "' WHERE idStudents='" + id + "'";
    }

    public static String generatePrintSelection(int id){
        return "SELECT * FROM Students WHERE idStudents='" + id + "'";
    }

    protected void exeQuery(Connection con, String sqlStatement)
            throws Exception {

        try {
            Statement cs = con.createStatement();
            ResultSet sqls = cs.executeQuery(sqlStatement);

            while (sqls.next()) {

                int id = Integer.parseInt(sqls.getObject("idStudents").toString());
                String studentName = (sqls.getObject("studentName").toString());
                int studentAge = Integer.parseInt(sqls.getObject("age").toString());
                double gpa = Double.parseDouble(sqls.getObject("gpa").toString());

                System.out.println("ID: " + id + ", "
                        + "Name: " + studentName + ", "
                        + "Age: " + studentAge + ", "
                        + "GPA: " + gpa);
            }

            sqls.close();

        } catch (SQLException e) {
            System.out.println ("Error executing sql statement");
            throw (e);
        }
    }
    protected void exeUpdate(Connection con, String sqlStatement)
            throws Exception {

        try {
            Statement cs = con.createStatement();
            cs.executeUpdate(sqlStatement);
        } catch (SQLException e) {
            System.out.println ("Error executing sql statement");
            throw (e);
        }
    }

    public void insert(String name, int age, double gpa) throws Exception{

        try{
            exeUpdate(conMe, generateInsert(name, age, gpa));
            System.out.println("Inserted");
        } catch(SQLException e){
            System.out.println(e);
        }
    }

    public void update(int id, String name, int age, double gpa) throws Exception{
        try{
            exeUpdate(conMe, generateUpdateById(id, name, age, gpa));
            System.out.println("Updated");
        } catch(SQLException e){
            System.out.println(e);
        }
    }

    public void select(int id) throws Exception{
        exeQuery(conMe, generatePrintSelection(id));
    }

    protected Connection makeCon (String host, String database, String user, String password)
            throws Exception {

        String url = "";
        try {
            url = "jdbc:mysql://" + host + ":3306/" + database;
            Connection con = DriverManager.getConnection(url, user, password);
            System.out.println("Connection established to " + url + "...");
            return con;
        } catch (java.sql.SQLException e) {
            System.out.println("Connection couldn't be established to " + url);
            throw (e);
        }
    }



    public static void main(String[] args) throws Exception{
       //Utils
        Main a = new Main();
        Scanner scanner = new Scanner(System.in);
        //a.runMe("mdcjava2.c9pjtvninqm1.us-west-2.rds.amazonaws.com", "mdcjava2", "mdcjava2", "password");

        //Menu
        boolean quit = false;
        int choice = 0;

        //Temporary
        String name, newName;
        int age, newAge, id;
        double gpa, newGPA;

        do {
            System.out.println("MENU:");
            System.out.println("1. Display Table\n" +
                    "2. Insert\n" +
                    "3. Update by ID\n" +
                    "4. Display by ID\n" +
                    "5. Quit");

            while(!scanner.hasNextInt()){
                scanner.nextLine();
            }
            choice = scanner.nextInt();

            if(choice == 1){
                System.out.println("Table: Students");
                a.printAll();
            } else if(choice == 2){
                //input
                System.out.print("Name: ");
                scanner.nextLine();
                name = scanner.nextLine();
                System.out.print("Age: ");
                age = scanner.nextInt();
                scanner.nextLine();
                System.out.print("GPA: ");
                gpa = scanner.nextDouble();

                a.insert(name, age, gpa);
            } else if(choice == 3){
                //input
                System.out.print("ID: ");
                scanner.nextLine();
                id = scanner.nextInt();
                scanner.nextLine();
                System.out.print("New Name: ");
                newName = scanner.nextLine();
                System.out.print("New Age: ");
                newAge = scanner.nextInt();
                scanner.nextLine();
                System.out.print("New GPA: ");
                newGPA = scanner.nextDouble();

                a.update(id, newName, newAge, newGPA);
            } else if(choice == 4){
                System.out.print("ID: ");
                scanner.nextLine();
                id = scanner.nextInt();
                scanner.nextLine();
                a.select(id);
            } else if(choice == 5){
                System.out.println("QUIT");
                quit = true;
            }
        } while (!quit);
    }
}
