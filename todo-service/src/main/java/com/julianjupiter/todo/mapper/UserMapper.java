package com.julianjupiter.todo.mapper;

import com.julianjupiter.todo.dto.CreateUserDto;
import com.julianjupiter.todo.dto.UserDetailsDto;
import com.julianjupiter.todo.dto.UserDto;
import com.julianjupiter.todo.entity.User;
import com.julianjupiter.todo.entity.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto fromEntityToDto(User user) {
        var userDetails = user.getUserDetails();

        var userDto = new UserDto(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );

        if (userDetails != null) {
            userDto.setUserDetailsDto(
                    new UserDetailsDto(
                            userDetails.getId(),
                            userDetails.getFirstName(),
                            userDetails.getLastName(),
                            userDetails.getMobileNumber(),
                            userDetails.getEmailAddress(),
                            userDetails.getCreatedAt(),
                            userDetails.getUpdatedAt()
                    )
            );
        }

        return userDto;
    }

    public User fromDtoToEntity(UserDto userDto) {
        var user  = new User()
                .setId(userDto.getId())
                .setUsername(userDto.getUsername())
                .setPassword(userDto.getPassword())
                .setCreatedAt(userDto.getCreatedAt())
                .setUpdatedAt(userDto.getUpdatedAt());

        var userDetailsDto = userDto.getUserDetailsDto();

        if (userDetailsDto != null) {
            var userDetails = new UserDetails()
                    .setFirstName(userDetailsDto.getFirstName())
                    .setLastName(userDetailsDto.getLastName())
                    .setMobileNumber(userDetailsDto.getMobileNumber())
                    .setEmailAddress(userDetailsDto.getMobileNumber())
                    .setCreatedAt(userDetailsDto.getCreatedAt())
                    .setUpdatedAt(userDetailsDto.getUpdatedAt())
                    .setUser(user);
            user.setUserDetails(userDetails);
        }

        return user;
    }

    public User toNewEntity(CreateUserDto createUserDto) {
        var user  = new User()
                .setUsername(createUserDto.getUsername())
                .setPassword(createUserDto.getPassword())
                .setCreatedAt(createUserDto.getCreatedAt())
                .setUpdatedAt(createUserDto.getUpdatedAt());
        var userDetails = new UserDetails()
                .setFirstName(createUserDto.getFirstName())
                .setLastName(createUserDto.getLastName())
                .setMobileNumber(createUserDto.getMobileNumber())
                .setEmailAddress(createUserDto.getEmailAddress())
                .setCreatedAt(createUserDto.getCreatedAt())
                .setUpdatedAt(createUserDto.getUpdatedAt())
                .setUser(user);
        user.setUserDetails(userDetails);

        return user;
    }
}
