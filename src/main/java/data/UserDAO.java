package data;

import java.sql.*;

/**
 * User Data Access Object.
 * @author godhand@ayohandgod.com
 */
public class UserDAO {

    public UserDAO() {

    }

    public User checkLogin(String email, String password) throws SQLException, ClassNotFoundException {

        Connection connection = DBConnector.getInstance().getConnection();
        String sql = "SELECT * FROM users WHERE email = ? and password = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, email);
        statement.setString(2, password);

        ResultSet result = statement.executeQuery();


        User user = null;

        if (result.next()) {
            user = new User();
            user.setName(result.getString("fullname"));
            user.setEmail(email);
        }

        connection.close();

        return user;
    }

    /**
     * Insert a user into the User database table.
     * @param user - A User Class instance to be inserted into the table.
     * @throws SQLException - Thrown if Database access problems encountered.
     */
    public void addUser(User user) throws SQLException {
        Connection connection = DBConnector.getInstance().getConnection();

        String insertSql = "INSERT INTO users(email, password, fullname) values (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(insertSql);

        statement.setString(1, user.getEmail());
        statement.setString(2, user.getPassword());
        statement.setString(3, user.getName());

        int result = statement.executeUpdate();

        System.out.println(result);

        connection.close();
    }

    public void addUser(String email, String password, String name) throws SQLException {
        User newUser = new User.UserBuilder().setEmail(email).setPassword(password).setName(name).build();
        addUser(newUser);
    }

    public static void main(String[] args) {
        try {
            UserDAO userDAO = new UserDAO();
            User dante = userDAO.checkLogin("calikidd86@gmail.com", "September");

            System.out.println(dante.getName());
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

    }

}
