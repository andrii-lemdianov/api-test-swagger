package com.alemdianov.api.services;

import com.alemdianov.api.utils.FileMessageResource;
import io.qameta.allure.Step;
import io.swagger.client.model.ModelApiResponse;
import io.swagger.client.model.Pet;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static com.alemdianov.api.utils.Utils.buildUrl;

public class PetService extends BaseService {
    public PetService() {
        super();
    }

    @Step("Get all pests")
    public ResponseEntity<Pet> getById(Long id) {
        String url = buildUrl("pet", id);
        ResponseEntity<Pet> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                getDefaultEntity(),
                Pet.class);
        return response;
    }

    @Step("Find pets by status")
    public ResponseEntity<Pet[]> findByStatus(Pet.StatusEnum status) {
        String url = buildUrl("pet", "findByStatus?status=" + status.toString());
        ResponseEntity<Pet[]> response = restTemplate.exchange(
                url, HttpMethod.GET, getDefaultEntity(), Pet[].class);

        return response;
    }

    @Step("Create a single pet")
    public ResponseEntity<Pet> create(Pet pet) {
        String url = buildUrl("pet");

        HttpEntity<Pet> entity = new HttpEntity<>(pet, defaultHeaders());
        ResponseEntity<Pet> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, Pet.class);

        return response;
    }

    @Step("Update a single pet")
    public ResponseEntity<Pet> update(Pet pet) {
        String url = buildUrl("pet");

        HttpEntity<Pet> entity = new HttpEntity<>(pet, defaultHeaders());
        ResponseEntity<Pet> response = restTemplate.exchange(
                url, HttpMethod.PUT, entity, Pet.class);

        return response;
    }

    @Step("Update a single pet with form")
    public ResponseEntity<ModelApiResponse> updateWithForm(Long petId, MultiValueMap<String, String> parametersMap) {
        String url = buildUrl("pet", petId);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(parametersMap, formHeaders());

        ResponseEntity<ModelApiResponse> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, ModelApiResponse.class);

        return response;
    }

    @Step("Delete a single pet")
    public ResponseEntity<ModelApiResponse> delete(Long petId) {
        String url = buildUrl("pet", petId);

        ResponseEntity<ModelApiResponse> response = restTemplate.exchange(
                url, HttpMethod.DELETE, null, ModelApiResponse.class);

        return response;
    }

    @Step("Upload file to the existing pet")
    public ResponseEntity<ModelApiResponse> uploadFile(Long id, String fileName) {
        String url = buildUrl("pet", id, "uploadImage");
        File uploadFile = null;
        try {
            uploadFile = new ClassPathResource(fileName).getFile();
        } catch (IOException e) {
            e.printStackTrace();
        }


        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        byte[] fileAsBytes = null;
        try {
            fileAsBytes = Files.readAllBytes(uploadFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        body.add("file", new FileMessageResource(fileAsBytes, fileName));

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, mediaHeaders());
        ResponseEntity<ModelApiResponse> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, ModelApiResponse.class);

        return response;
    }
}