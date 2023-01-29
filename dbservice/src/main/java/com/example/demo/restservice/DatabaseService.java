package com.example.demo.restservice;

import com.example.demo.db.DatabaseRepository;
import com.example.demo.db.Image;
import com.example.demo.db.Person;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;

@CrossOrigin(origins = "${FRONTEND_HOST:*}")
@RestController
public class DatabaseService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    DatabaseRepository dbRepo;

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public ResponseEntity<String> postForm(@RequestBody String data) throws JSONException {
        JSONObject jsonObject= new JSONObject(data);
        String name = jsonObject.getString("name");
        String email = jsonObject.getString("email");
        String base64 = jsonObject.getString("image").replace("data:image/png;base64,", "");
        byte[] decode = Base64.getDecoder().decode(base64);

        UUID uuid = UUID.randomUUID();
        String userId = uuid.toString();

        Person user = new Person(userId, name, email);
        Image image = new Image(userId, decode);
        
        dbRepo.savePerson(user);
        dbRepo.saveImage(image);

        ResponseEntity<String> result = new ResponseEntity<String>(HttpStatus.OK);

        return result;

    }

    @GetMapping("/image/{userId}")
    ResponseEntity<Image> getImage(@PathVariable String userId) throws IOException {

        Image image = dbRepo.getImage(userId);

        String imageName = image.getUserID() + ".png";

        saveImageToFile(image.getImage(), imageName);

        return new ResponseEntity<Image>(image, HttpStatus.OK);

    }

    private void saveImageToFile(byte[] image, String imageName) throws IOException {
        if (new File("/images/").exists()) {
            Files.write(new File("/images/" + imageName).toPath(), image);
        }
        else {
            new File("/images/").mkdir();
            Files.write(new File("/images/" + imageName).toPath(), image);
        }
    }

}