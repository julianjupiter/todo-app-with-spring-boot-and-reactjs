package com.julianjupiter.todo.service;

import com.julianjupiter.todo.dto.CreateUserDto;
import com.julianjupiter.todo.dto.UserDto;
import com.julianjupiter.todo.exception.ResourceNotFoundException;
import com.julianjupiter.todo.exception.ValidationException;
import com.julianjupiter.todo.mapper.UserMapper;
import com.julianjupiter.todo.repository.RoleRepository;
import com.julianjupiter.todo.repository.UserRepository;
import com.julianjupiter.todo.util.Applications;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::fromEntityToDto)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Optional<UserDto> findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::fromEntityToDto);
    }

    @Override
    public UserDto create(CreateUserDto createUserDto) {
        var userOptional = userRepository.findByUsername(createUserDto.getUsername());
        if (userOptional.isEmpty()) {
            if (createUserDto.getCreatedAt() == null) {
                var now = OffsetDateTime.now();
                createUserDto.setCreatedAt(now);
                createUserDto.setUpdatedAt(now);
            }
            createUserDto.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
            var newUser = userMapper.toNewEntity(createUserDto);
            var role = roleRepository.findById(createUserDto.getRoleId()).orElse(null);
            if (role != null) {
                newUser.setRoles(Set.of(role));
            }
            var createUser = userRepository.save(newUser);

            return userMapper.fromEntityToDto(createUser);
        }

        throw new ValidationException("User already exists.", List.of("User already exists."), Applications.createUri());
    }

    @Override
    public UserDto update(UserDto userDto) {
        var userId = userDto.getId();
        return userRepository.findById(userId)
                .map(user -> {
                    var userUpdate = userMapper.fromDtoToEntity(userDto);
                    var updatedUser = userRepository.save(userUpdate);
                    return userMapper.fromEntityToDto(updatedUser);
                })
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + userId + " does not exist", Applications.createUri()));
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
