package com.example.decode.dto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class UserDto {
    private Long Id;

    private String userName;

    private String emailId;

    private String address;

}
