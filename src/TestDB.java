import java.sql.Connection;
import java.sql.DriverManager;

public class TestDB {
    public static void main(String[] args) {
        try {
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/mysql",
                "root",
                "amresh123@456$"
            );
            System.out.println("Connected Successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
