package com.project.backend.rest;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class CustomVision {
    final static String trainingEndpoint = "https://facerecongition2022.cognitiveservices.azure.com";
    final static String trainingApiKey = "3aca9c9596e241309c53c661351552df";
    static RestTemplate restTemplate = new RestTemplate();
    final static String projectID = "5bfb1459-e248-468e-b566-3af30cd9aa45";
    public final static String tagIdJay = "1bc90c21-6917-4b75-8376-d0cfa4caf7d7";
    public final static String tagIdOthers = "2fe001b1-0f82-4e88-866a-ea6430c36e6a";

    public static void createProject(String projectName) throws JSONException {
        String url = "{endpoint}/customvision/v3.3/Training/projects?name={name}";
        Map<String, String> params = new HashMap<>();
        params.put("endpoint", trainingEndpoint);
        params.put("name", projectName);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        // System.out.println(builder.buildAndExpand(params).toUri());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Training-key", trainingApiKey);

        HttpEntity<String> request = new HttpEntity<String>(headers);

        ResponseEntity<String> response = restTemplate.postForEntity(builder.buildAndExpand(params).toUri(), request,
                String.class);
        // System.out.println(response.getBody())

        JSONObject jsonObject = new JSONObject(response.getBody());
        System.out.println(jsonObject.getString("id"));
    }

    public static void createTag(String tagName) throws JSONException {
        String url = "{endpoint}/customvision/v3.3/Training/projects/{projectId}/tags?name={name}";

        Map<String, String> params = new HashMap<>();
        params.put("endpoint", trainingEndpoint);
        params.put("projectId", projectID);
        params.put("name", tagName);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        URI uri = builder.buildAndExpand(params).toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Training-key", trainingApiKey);

        HttpEntity<String> request = new HttpEntity<String>(headers);

        ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);

        JSONObject jsonObject = new JSONObject(response.getBody());
        System.out.println(jsonObject.getString("id") + ": " + tagName);
    }

    public static void uploadImage(String tagId, byte[] fileData) throws JSONException, IOException {
        String url = "{endpoint}/customvision/v3.3/training/projects/{projectId}/images?tagIds={tagIds}";

        Map<String, String> params = new HashMap<>();
        params.put("endpoint", trainingEndpoint);
        params.put("projectId", projectID);
        params.put("tagIds", tagId);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        URI uri = builder.buildAndExpand(params).toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set("Training-key", trainingApiKey);

        HttpEntity<byte[]> request = new HttpEntity<>(fileData, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);
        System.out.println(response.getBody());
    }

    public static ResponseEntity<String> validate(byte[] data) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/octet-stream");
        headers.add("Prediction-Key", "3aca9c9596e241309c53c661351552df");

        HttpEntity<byte[]> entity = new HttpEntity<>(data, headers);
        String URL = "https://eastus.api.cognitive.microsoft.com/customvision/v3.0/Prediction/5bfb1459-e248-468e-b566-3af30cd9aa45/classify/iterations/Iteration1/image";
        ResponseEntity<String> result = restTemplate.postForEntity(URL, entity, String.class);
        return result;
    }

    public static void main(String args[]) throws JSONException, IOException {

        createTag("Jay");
        createTag("Others");
        

    }

}
