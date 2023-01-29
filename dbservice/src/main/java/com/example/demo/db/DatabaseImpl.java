package com.example.demo.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class DatabaseImpl implements DatabaseRepository{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int savePerson(Person person) {

        return jdbcTemplate.update(
                "insert into Persons (userID, fullName, email) values(?,?,?)",
                person.getUserID(), person.getFullName(), person.getEmail());

    }

    @Override
    public int saveImage(Image image) {

        return jdbcTemplate.update(
                "insert into Images (userID, image) values(?,?)",
                image.getUserID(), image.getImage());

    }

    @Override
    public Person getPerson(String userId) {
        String sql = "SELECT * FROM Persons WHERE userId = ?";
        Person target = jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new Person(
                        rs.getString("userId"),
                        rs.getString("fullName"),
                        rs.getString("age")
                ), new Object[] { userId });

                /*
                new RowMapper<Person>() {
                    @Override
                    public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Person target = new Person();
                        target.setUserID(rs.getInt("userId"));
                        target.setFullName(rs.getString("fullName"));
                        target.setEmail(rs.getString("email"));
                        return target;
                    }
                });*/

        return target;
    }

    @Override
    public Image getImage(String userId) {
        String sql = "SELECT * FROM Images WHERE userId = ?";
        Image target = jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new Image(
                        rs.getString("userId"),
                        rs.getBytes("image")
                ),new Object[]{userId});
        /*
                new RowMapper<Image>() {
                    @Override
                    public Image mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Image target = new Image();
                        target.setUserID(rs.getInt("userId"));
                        target.setImage(rs.getBytes("image"));
                        return target;
                    }
                });*/

        return target;

    }
}
