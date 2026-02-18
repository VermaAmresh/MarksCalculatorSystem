import java.sql.*;
import java.util.*;
import java.io.*;

public class Main {

    static String URL;
    static String USER;
    static String PASS;
    static final String FILE_NAME = "student_info.txt";

    // Static block to load DB credentials
    static {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream("config.properties"));
            URL = props.getProperty("db.url");
            USER = props.getProperty("db.user");
            PASS = props.getProperty("db.pass");
        } catch (Exception e) {
            System.out.println("Error loading config: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             Scanner sc = new Scanner(System.in)) {

            while (true) {
                System.out.println("\n--- Marks Calculator System ---");
                System.out.println("1. NEW STUDENT");
                System.out.println("2. STUDENT INFORMATION");
                System.out.println("3. MODIFICATION");
                System.out.println("4. RESULT");
                System.out.println("5. DATA");
                System.out.println("6. EXIT");
                System.out.print("Choose option: ");

                int option = sc.nextInt();
                sc.nextLine(); // consume newline

                switch (option) {
                    case 1:
                        addNewStudent(con, sc);
                        break;
                    case 2:
                        viewStudentInfo(con, sc);
                        break;
                    case 3:
                        modifyStudent(con, sc);
                        break;
                    case 4:
                        viewResult(con, sc);
                        break;
                    case 5:
                       viewAllNames(con);
                        break;
                    case 6:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid option!");
                }
            }

        } catch (Exception e) {
            System.out.println("DB Connection Error: " + e.getMessage());
        }
    }

    // --- Add new student ---
    static void addNewStudent(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Student ID: ");
            int id = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Phone: ");
            String phone = sc.nextLine();

            System.out.print("Enter Math marks: ");
            int math = sc.nextInt();
            System.out.print("Enter Physics marks: ");
            int physics = sc.nextInt();
            System.out.print("Enter Chemistry marks: ");
            int chemistry = sc.nextInt();
            sc.nextLine();

            String sql = "INSERT INTO students(student_id,name,phone,math,physics,chemistry) VALUES (?,?,?,?,?,?)";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setInt(1, id);
                pst.setString(2, name);
                pst.setString(3, phone);
                pst.setInt(4, math);
                pst.setInt(5, physics);
                pst.setInt(6, chemistry);
                pst.executeUpdate();
            }

            // Save to file
            try (FileWriter fw = new FileWriter(FILE_NAME, true);
                 PrintWriter pw = new PrintWriter(fw)) {
                pw.println(id + "," + name + "," + phone + "," + math + "," + physics + "," + chemistry);
            } catch (Exception e) {
                System.out.println("Error writing to file: " + e.getMessage());
            }

            System.out.println("Student added successfully!");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // --- View student info ---
    static void viewStudentInfo(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Student ID: ");
            int id = sc.nextInt();
            sc.nextLine();

            String sql = "SELECT * FROM students WHERE student_id=?";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setInt(1, id);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    System.out.println("\n--- STUDENT INFO ---");
                    System.out.println("ID: " + rs.getInt("student_id"));
                    System.out.println("Name: " + rs.getString("name"));
                    System.out.println("Phone: " + rs.getString("phone"));
                } else {
                    System.out.println("NO DATA FOUND");
                }
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // --- Modify student ---
    static void modifyStudent(Connection con, Scanner sc) 
    {
        try {
        System.out.print("Enter Student ID to modify: ");
        int id = sc.nextInt();
        sc.nextLine();

        String checkSql = "SELECT * FROM students WHERE student_id=?";
        try (PreparedStatement checkPst = con.prepareStatement(checkSql)) {
            checkPst.setInt(1, id);
            ResultSet rs = checkPst.executeQuery();

            if (!rs.next()) {
                System.out.println("NO DATA FOUND");
                return;
            }

            System.out.println("\nChoose info to modify:");
            System.out.println("1. PERSONAL INFORMATION");
            System.out.println("2. ACADEMIC INFORMATION");
            System.out.print("Option: ");
            int choice = sc.nextInt();
            sc.nextLine();

            boolean updated = false;

            switch (choice) {
                case 1: // Personal info
                    System.out.print("Enter new Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter new Phone: ");
                    String phone = sc.nextLine();

                    String updatePersonalSql = "UPDATE students SET name=?, phone=? WHERE student_id=?";
                    try (PreparedStatement pst = con.prepareStatement(updatePersonalSql)) {
                        pst.setString(1, name);
                        pst.setString(2, phone);
                        pst.setInt(3, id);
                        int rows = pst.executeUpdate();
                        if (rows > 0) {
                            System.out.println("Personal information updated successfully!");
                            updated = true;
                        } else {
                            System.out.println("Update failed!");
                        }
                    }
                    break;

                case 2: // Academic info
                    System.out.print("Enter new Math marks: ");
                    int math = sc.nextInt();
                    System.out.print("Enter new Physics marks: ");
                    int physics = sc.nextInt();
                    System.out.print("Enter new Chemistry marks: ");
                    int chemistry = sc.nextInt();
                    sc.nextLine();

                    String updateAcademicSql = "UPDATE students SET math=?, physics=?, chemistry=? WHERE student_id=?";
                    try (PreparedStatement pst = con.prepareStatement(updateAcademicSql)) {
                        pst.setInt(1, math);
                        pst.setInt(2, physics);
                        pst.setInt(3, chemistry);
                        pst.setInt(4, id);
                        int rows = pst.executeUpdate();
                        if (rows > 0) {
                            System.out.println("Academic information updated successfully!");
                            updated = true;
                        } else {
                            System.out.println("Update failed!");
                        }
                    }
                    break;

                default:
                    System.out.println("Invalid option!");
                    break;
            }

            //  Update the file if DB was updated
            if (updated) {
                try (FileWriter fw = new FileWriter(FILE_NAME);
                     PrintWriter pw = new PrintWriter(fw);
                     Statement stmt = con.createStatement();
                     ResultSet allRs = stmt.executeQuery("SELECT * FROM students")) {

                    while (allRs.next()) {
                        int sid = allRs.getInt("student_id");
                        String sname = allRs.getString("name");
                        String sphone = allRs.getString("phone");
                        int smath = allRs.getInt("math");
                        int sphy = allRs.getInt("physics");
                        int schem = allRs.getInt("chemistry");
                        pw.println(sid + "," + sname + "," + sphone + "," + smath + "," + sphy + "," + schem);
                    }

                } catch (Exception e) {
                    System.out.println("Error updating file: " + e.getMessage());
                }
            }

        }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }   


    // --- View result ---
    static void viewResult(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Student ID to view result: ");
            int id = sc.nextInt();
            sc.nextLine();

            String sql = "SELECT * FROM students WHERE student_id=?";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setInt(1, id);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    String name = rs.getString("name");
                    int math = rs.getInt("math");
                    int physics = rs.getInt("physics");
                    int chemistry = rs.getInt("chemistry");

                    int total = math + physics + chemistry;
                    double percentage = total / 3.0;

                    System.out.println("\n--- RESULT ---");
                    System.out.println("ID: " + id);
                    System.out.println("Name: " + name);
                    System.out.println("Math: " + math);
                    System.out.println("Physics: " + physics);
                    System.out.println("Chemistry: " + chemistry);
                    System.out.println("Total: " + total);
                    System.out.printf("Percentage: %.2f\n", percentage);

                    String grade;
                    if (percentage >= 90) grade = "A+";
                    else if (percentage >= 80) grade = "A";
                    else if (percentage >= 70) grade = "B+";
                    else if (percentage >= 60) grade = "B";
                    else if (percentage >= 50) grade = "C";
                    else grade = "F";

                    System.out.println("Grade: " + grade);

                } else {
                    System.out.println("NO DATA FOUND");
                }
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // --- View all student names (DATA) ---
    static void viewAllNames(Connection con) {
        try {
            String sql = "SELECT student_id, name FROM students ORDER BY student_id";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                ResultSet rs = pst.executeQuery();
                System.out.println("\n--- ALL STUDENT NAMES ---");
                boolean found = false;
                while (rs.next()) {
                    int id = rs.getInt("student_id");
                    String name = rs.getString("name");
                    System.out.println(id + " : " + name);
                    found = true;
                }
                if (!found) {
                    System.out.println("No student data found!");
                }
            }
        } catch (Exception e) {
            System.out.println("Error fetching data: " + e.getMessage());
        }
    }
}