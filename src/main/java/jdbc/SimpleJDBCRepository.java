package jdbc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleJDBCRepository {

    private Connection connection = null;
    private PreparedStatement ps = null;
    private Statement st = null;

    private static final String createUserSQL = "INSERT INTO myusers (id, firstname, lastname, age) VALUES (?, ?, ?, ?) RETURNING id";
    private static final String updateUserSQL = "UPDATE myusers SET firstname = ?, lastname = ?, age = ? WHERE id = ?";
    private static final String deleteUserSQL = "DELETE FROM myusers WHERE id = ?";
    private static final String findUserByIdSQL = "SELECT * FROM myusers WHERE id = ?";
    private static final String findUserByNameSQL = "SELECT * FROM myusers WHERE firstname = ?";
    private static final String findAllUserSQL = "SELECT * FROM myusers";

    public Long createUser() {
        Long generatedId = null;
        try {
            connection = CustomDataSource.getInstance().getConnection();
            ps = connection.prepareStatement(createUserSQL);
            ps.setLong(1, 1);
            ps.setString(2, "Adam");
            ps.setString(3, "Tobias");
            ps.setInt(4, 23);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                generatedId = rs.getLong(1);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return generatedId;
    }

    public User findUserById(Long userId) {
        User user = null;
        try {
            connection = CustomDataSource.getInstance().getConnection();
            ps = connection.prepareStatement(findUserByIdSQL);
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = new User(
                        rs.getLong("id"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getInt("age")
                );
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return user;
    }

    public User findUserByName(String userName) {
        User user = null;
        try {
            connection = CustomDataSource.getInstance().getConnection();
            ps = connection.prepareStatement(findUserByNameSQL);
            ps.setString(1, userName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = new User(
                        rs.getLong("id"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getInt("age")
                );
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return user;
    }

    public List<User> findAllUser() {
        List<User> users = new ArrayList<>();
        try {
            connection = CustomDataSource.getInstance().getConnection();
            st = connection.createStatement();
            ResultSet rs = st.executeQuery(findAllUserSQL);
            while (rs.next()) {
                User user = new User(
                        rs.getLong("id"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getInt("age")
                );
                users.add(user);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return users;
    }

    public User updateUser() {
        try {
            connection = CustomDataSource.getInstance().getConnection();
            ps = connection.prepareStatement(updateUserSQL);
            ps.setString(1, "User");
            ps.setString(2, "Userov");
            ps.setInt(3, 12);
            ps.setLong(4, 1L);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                return findUserById(1L);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return null;
    }

    public void deleteUser(Long userId) {
        try {
            connection = CustomDataSource.getInstance().getConnection();
            ps = connection.prepareStatement(deleteUserSQL);
            ps.setLong(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    private void closeResources() {
        try {
            if (ps != null) {
                ps.close();
            }
            if (st != null) {
                st.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
