package com.example.FitTribe.service;

import com.example.FitTribe.dto.AuthResponseDto;
import com.example.FitTribe.dto.LoginUserDto;
import com.example.FitTribe.dto.RegisterUserDto;
import com.example.FitTribe.entity.User;
import com.example.FitTribe.repository.UserRepository;
import com.example.FitTribe.utils.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;

  private final JwtService jwtService;

  private final PasswordEncoder passwordEncoder;

  private final AuthenticationManager authenticationManager;

  public AuthResponseDto register(RegisterUserDto userDto) {

    Optional<User> userOptional = userRepository.findByUserName(userDto.getUserName());
    if(userOptional.isPresent()){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userName already exists");
    }
    userOptional = userRepository.findByEmail(userDto.getEmail());
    if(userOptional.isPresent()){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email already exists");
    }

    User user =
        User.builder()
            .userName(userDto.getUserName())
            .firstName(userDto.getFirstName())
            .lastName(userDto.getLastName())
            .password(passwordEncoder.encode(userDto.getPassword()))
            .email(userDto.getEmail())
            .contact(userDto.getContact())
            .roles(List.of(Role.ROLE_USER))
            .followedBy(new HashSet<>())
            .following(new HashSet<>())
            .build();

    user = userRepository.save(user);
    String token = jwtService.getToken(user.getUsername());
    return AuthResponseDto.builder().token(token).build();
  }

  public AuthResponseDto login(LoginUserDto userDto) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(userDto.getUserName(), userDto.getPassword()));

    User user = userRepository.findByUserName(userDto.getUserName()).orElseThrow();
    String token = jwtService.getToken(user.getUsername());
    return AuthResponseDto.builder().token(token).build();
  }
}
