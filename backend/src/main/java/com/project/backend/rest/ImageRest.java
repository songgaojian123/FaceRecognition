package com.project.backend.rest;

import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.azure.storage.blob.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Base64;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "${FRONTEND_HOST:*}")
public class ImageRest {

    @GetMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hello World";
    }

    private void saveImageToFile(byte[] image, String imageName) throws IOException {
        File path = new File("./images/");
        if (!path.exists()) {
            path.mkdir();
        }
        Files.write(new File("./images/" + imageName).toPath(), image);

    }

    @RequestMapping(value = "/images", method = RequestMethod.POST)
    public ResponseEntity<Object> upload(@RequestBody String data) throws IOException, JSONException {
        String base64 = data.replace("data:image/png;base64,", "");
        byte[] decode = Base64.getDecoder().decode(base64);
        String imageName = UUID.randomUUID() + ".png";
        // saveImageToFile(decode, imageName);
        // saveToCloud(decode, imageName);
        CustomVision.uploadImage(CustomVision.tagIdJay, decode);
        return new ResponseEntity<Object>("Image saved sucessfully", HttpStatus.OK);
    }

    private void saveToCloud(byte[] image, String imageName) {
        // Retrieve the connection string for use with the application.
        String connectStr = "DefaultEndpointsProtocol=https;AccountName=cs21003200156dc0219;AccountKey=1RrtGD2CZHFxg65up71hyg5e0AjsWMwgLXiFnKzhqxH6XIvdYU5b4svz4IL5U5WaVDOPEBiHHTnv+AStMcy+ww==;EndpointSuffix=core.windows.net";
        // Create a BlobServiceClient object using a connection string
        BlobServiceClient client = new BlobServiceClientBuilder().connectionString(connectStr).buildClient();

        // Create a unique name for the container
        String containerName = "images";

        // Create the container and return a container client object
        BlobContainerClient blobContainerClient = client.createBlobContainerIfNotExists(containerName);

        // Get a reference to a blob
        BlobClient blobClient = blobContainerClient.getBlobClient(imageName);

        // Upload the blob
        // blobClient.uploadFromFile(localPath + fileName);
        InputStream targetStream = new ByteArrayInputStream(image);
        blobClient.upload(targetStream);
    }

    @RequestMapping(value = "/validate", method = RequestMethod.POST)
    public ResponseEntity<String> validate(@RequestBody String data) throws IOException, JSONException {
        String base64 = data.replace("data:image/png;base64,", "");
        byte[] decode = Base64.getDecoder().decode(base64);
        return CustomVision.validate(decode);
    }

}
