package com.example.decode.controller;

import com.example.decode.Response.ResponseBO;
import com.example.decode.entity.UserEntity;
import com.example.decode.service.ImageUtils;
import com.example.decode.service.Interface.UserInterface;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/decode")
public class UserController {

    @Autowired
    private UserInterface userInterface;

    @Value("${image.width}")
    private int imageWidth;

    @Value("${image.height}")
    private int imageHeight;

    @PostMapping("/save")
    public ResponseBO registerUser(
            @RequestParam("user") String userJson,
            @RequestParam("profileImage") MultipartFile profileImage) throws IOException {

        // Deserialize user from JSON
        UserEntity user = new ObjectMapper().readValue(userJson, UserEntity.class);

        // Convert MultipartFile to byte array
        byte[] profileImageBytes = profileImage.getBytes();
        user.setProfileImage(profileImageBytes);  // Save image as a byte array

        UserEntity createdUser = userInterface.saveUser(user);
        return new ResponseBO(HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase(), createdUser, "User Created Successfully");
    }

    @GetMapping("/view/{userName}")
    public ResponseBO viewUser(@PathVariable String userName) throws IOException {
        if (userInterface.isUsernameExists(userName)) {
            UserEntity viewUser = userInterface.viewUser(userName);

            // Resize the image before returning if necessary
            byte[] resizedImage = ImageUtils.resizeImage(viewUser.getProfileImage(), imageWidth, imageHeight);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            // Set the resized image back to the user entity (optional, depending on use case)
            viewUser.setProfileImage(resizedImage);

            return new ResponseBO(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), viewUser, "Retrieved Successfully");
        }
        return new ResponseBO(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), null, "Username: '" + userName + "' Not Found");
    }

    @PutMapping("/update/{id}")
    public ResponseBO updateUser(@PathVariable Long id, @RequestParam("user") String userJson, @RequestParam("profileImage") MultipartFile profileImage) throws JsonProcessingException, IOException {
        // Deserialize user from JSON
        UserEntity userDetails = new ObjectMapper().readValue(userJson, UserEntity.class);

        // Convert MultipartFile to byte array
        byte[] profileImageBytes = profileImage.getBytes();
        userDetails.setProfileImage(profileImageBytes);  // Save image as a byte array

        // Update user
        UserEntity updatedUser = userInterface.updateUser(id, userDetails);
        return new ResponseBO(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), updatedUser, "Updated Successfully");
    }

    @GetMapping("/image/{userName}")
    public ResponseEntity<byte[]> getUserProfileImage(@PathVariable("userName") String userName) {
        try {
            UserEntity user = userInterface.viewUser(userName);
            if (user == null || user.getProfileImage() == null) {
                return ResponseEntity.notFound().build();
            }

            // Resize the image using ImageUtils
            byte[] resizedImage = ImageUtils.resizeImage(user.getProfileImage(), imageWidth, imageHeight);

            // Set headers and return the resized image
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Adjust based on your image type
            return new ResponseEntity<>(resizedImage, headers, HttpStatus.OK);
        } catch (RuntimeException | IOException e) {
            // Log the exception and return an appropriate error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage().getBytes());
        }
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userInterface.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
