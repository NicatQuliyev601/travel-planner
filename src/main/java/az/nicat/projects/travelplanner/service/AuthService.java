package az.nicat.projects.travelplanner.service;

import az.nicat.projects.travelplanner.dto.request.ChangePasswordRequest;
import az.nicat.projects.travelplanner.dto.request.LoginRequest;
import az.nicat.projects.travelplanner.dto.request.RegisterRequest;
import az.nicat.projects.travelplanner.dto.response.JwtResponse;
import az.nicat.projects.travelplanner.dto.response.UserResponse;
import az.nicat.projects.travelplanner.entity.Authority;
import az.nicat.projects.travelplanner.entity.User;
import az.nicat.projects.travelplanner.repository.AuthorityRepository;
import az.nicat.projects.travelplanner.repository.UserRepository;
import az.nicat.projects.travelplanner.security.jwt.JwtService;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;


    public AuthService(AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder, UserRepository userRepository, JwtService jwtService, JwtService jwtService1, ModelMapper modelMapper) {
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtService = jwtService1;
        this.modelMapper = modelMapper;
    }

    public List<UserResponse> findAll() {
        return userRepository
                .findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserResponse.class))
                .collect(Collectors.toList());
    }

    public UserResponse findById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("User Not found")
        );

        return modelMapper.map(user, UserResponse.class);
    }

    public String register(RegisterRequest request) {

//        if (userRepository.existsByEmail(request.getEmail())) {
//            throw new UserAlreadyExistException(ErrorCodes.USER_ALREADY_EXIST);
//        }

        Authority userAuthority = authorityRepository.findByAuthority(request.getAuthority()).orElseGet(() -> {
            Authority authority = new Authority();
            authority.setAuthority(request.getAuthority());
            return authorityRepository.save(authority);
        });


        User user = new User(
                request.getFirstName(),
                request.getLastName(),
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                List.of(userAuthority)
        );

//        user.setActive(false);

        userRepository.save(user);

        return "User created";
    }


    public JwtResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("InValid password");
        }

//        if (!user.isActive()) {
//            throw new RuntimeException("Please confirm your email before logging in.");
//        }

        return new JwtResponse(user.getId(), jwtService.issueToken(user), user.getAuthorities());
    }


    public String changePassword(Long userId, ChangePasswordRequest changePasswordRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("InValid password");
        }

        String encodedNewPassword = passwordEncoder.encode(changePasswordRequest.getNewPassword());
        user.setPassword(encodedNewPassword);
        userRepository.save(user);

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());

        return "Password changed successfully";
    }

//    public ResponseEntity<?> confirmation(String confirmationToken) {
//        Optional<User> userOptional = userRepository.findByConfirmationToken(confirmationToken);
//
//        if (userOptional.isEmpty()) {
//            return ResponseEntity.badRequest().body("Invalid confirmation token");
//        }
//
//        User user = userOptional.get();
//        user.setActive(true);
//        userRepository.save(user);
//
//        return ResponseEntity.status(302)
//                .header("Location", "http://localhost:5173/login")
//                .build();
//    }


    private String getConfirmationToken() {
        UUID gfg = UUID.randomUUID();
        return gfg.toString();
    }


}
