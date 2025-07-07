package com.bytebites.auth_service.services;

import com.bytebites.auth_service.dto.*;
import com.bytebites.auth_service.exceptions.ResourceExistsException;
import com.bytebites.auth_service.exceptions.ResourceNotFound;
import com.bytebites.auth_service.infrastructure.RoleRepository;
import com.bytebites.auth_service.infrastructure.UserRepository;
import com.bytebites.auth_service.mappers.AuthMapper;
import com.bytebites.auth_service.mappers.UserMapper;
import com.bytebites.auth_service.models.AppUser;
import com.bytebites.auth_service.models.Role;
import com.bytebites.auth_service.models.User;
import com.bytebites.auth_service.utilities.JWTUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final  UserMapper userMapper;
    private final AuthMapper authMapper;


    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository, AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserMapper userMapper, AuthMapper authMapper) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userMapper = userMapper;
        this.authMapper = authMapper;
    }

    @Override
    public UserResponse register(RegisterRequest registerRequest) {
        checkExistingUser(registerRequest.email());
        checkExistingRole(registerRequest.role());
        return userMapper.toUserResponse(createUser(registerRequest));
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        AuthResult authenticatedUser = authenticateUser(loginRequest);
        SecurityContextHolder.getContext().setAuthentication(authenticatedUser.authentication());
        AppUser authentication = (AppUser)authenticatedUser.authentication().getPrincipal();
        String accessToken = jwtUtil.generateAccessToken(
                authentication.getId(),
                authenticatedUser.roles()
        );
        return authMapper.toAuthResponse(new AuthResponse(
                authenticatedUser.authentication().getName(),
                accessToken
        ));
    }

    private void checkExistingUser(String email){
        boolean userExists = userRepository.existsByEmail(email);
        if(userExists){
            throw new ResourceExistsException("User with this email already exists");
        }
    }

    private AuthResult authenticateUser(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
        );
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return new AuthResult(
                authentication,
                roles
        );
    }

    private String createHashedPassword(String password){
        return bCryptPasswordEncoder.encode(password);
    }

    private Role checkExistingRole(String roleName){
        return roleRepository.findByName(roleName).orElseThrow(()->
                new ResourceNotFound("Role not found"));
    }

    private User createUser(RegisterRequest registerRequest){
        String password = createHashedPassword(registerRequest.password());
        User user = new User(registerRequest.email(), password);
        Role role = checkExistingRole(registerRequest.role());
        user.getRoles().add(role);
        return userRepository.save(user);
    }
}
