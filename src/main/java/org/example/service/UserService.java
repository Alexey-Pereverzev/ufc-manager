package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.JwtResponse;
import org.example.dto.UserAuthDto;
import org.example.dto.UserDto;
import org.example.entity.Role;
import org.example.entity.Status;
import org.example.entity.User;
import org.example.exceptions.*;
import org.example.jwt.JwtTokenUtil;
import org.example.repository.UserRepository;
import org.example.util.EnumUtil;
import org.example.util.InputValidationUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final InputValidationUtil validationService = new InputValidationUtil();

    public UserDto entityToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole().toString())
                .status(user.getStatus().toString())
                .build();
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(this::entityToDto).toList();
    }

    public Optional<UserDto> findById(Long id) {
        Optional<User> optUser = userRepository.findById(id);
        return optUser.map(this::entityToDto);
    }

    public String updateUserRole(Long id, String roleUpdate) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setRole(EnumUtil.parseRole(roleUpdate));
        userRepository.save(user);
        return user.getUsername();
    }

    public String updateUserStatus(Long id, String statusUpdate) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setStatus(EnumUtil.parseStatus(statusUpdate));
        userRepository.save(user);
        return user.getUsername();
    }


    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void register(UserAuthDto dto) throws InputDataErrorException {
        Optional<User> checkUser = findByUsername(dto.getUsername());
        System.out.println(2222);
        if (checkUser.isPresent()) {
            throw new UserAlreadyExistsException("Username already in use");
        } else {
            System.out.println(3333);
            User user = new User();

            String encryptedPassword = passwordEncoder.encode(dto.getPassword());
            String validationMessage = validateAndSaveFields(dto, encryptedPassword, user);
            if (validationMessage != null) {
                throw new InputDataErrorException(validationMessage);
            }
            user.setUsername(dto.getUsername());
            user.setRole(Role.GUEST);
            user.setStatus(Status.ACTIVE);
            userRepository.save(user);
        }
    }


    private String validateAndSaveFields(UserAuthDto dto, String encryptedPassword, User user) {
        String username = dto.getUsername();
        String validationMessage = "";

        validationMessage = validationService.acceptableLogin(username);
        if (validationMessage.isEmpty()) {
            user.setUsername(username);
        } else {
            return validationMessage;
        }

        validationMessage = validationService.acceptablePassword(dto.getPassword());
        // we take the password from the DTO, because we check the validity of the unencrypted password and save it
        // to the database
        if (validationMessage.isEmpty()) {
            user.setPassword(encryptedPassword);
        } else {
            return validationMessage;
        }

        return null;    //  successful validation
    }


    public JwtResponse auth(UserAuthDto dto) {
        String username = dto.getUsername();
        System.out.println(11111);
        User user = findByUsername(username).orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        } else {
            if (user.getStatus() == Status.BLOCKED) {
                throw new NoAccessRightsException("No access rights");
            } else {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, dto.getPassword()));
                // authorization, otherwise status 400
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(dto.getUsername());
                String token = jwtTokenUtil.generateToken(userDetails);
                return new JwtResponse(token, user.getUsername(), customUserDetailsService.getRole(username));
            }
        }
    }

}
