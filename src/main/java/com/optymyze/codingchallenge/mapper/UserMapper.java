package com.optymyze.codingchallenge.mapper;

import com.optymyze.codingchallenge.dto.UserDto;
import com.optymyze.codingchallenge.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);
    User toUser(UserDto userDto);
    List<UserDto> toUserDtos(List<User> users);
}
