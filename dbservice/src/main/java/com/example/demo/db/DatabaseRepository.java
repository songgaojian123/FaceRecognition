package com.example.demo.db;

public interface DatabaseRepository {

    int savePerson(Person person);

    int saveImage(Image image);

    Person getPerson(String userId);

    Image getImage(String userId);

}
