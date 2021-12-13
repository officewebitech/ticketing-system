package os.ticketingsystem.login.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import os.ticketingsystem.login.dto.PartialUserDTO;
import os.ticketingsystem.login.dto.UserDTO;
import os.ticketingsystem.login.mapper.UserMapper;
import os.ticketingsystem.login.payload.response.JWTResponse;
import os.ticketingsystem.login.repository.UserRepository;
import os.ticketingsystem.login.security.jwt.JWTUtils;
import os.ticketingsystem.login.security.jwt.UserDetailsImplement;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoginService {

    private AuthenticationManager authenticationManager;
    private JWTUtils jwtUtils;

    private UserRepository userRepository;
    private UserMapper userMapper;

    @Autowired
    public LoginService(AuthenticationManager authenticationManager,
                        UserRepository userRepository,
                        UserMapper userMapper,
                        JWTUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.jwtUtils = jwtUtils;
    }

    public JWTResponse login(UserDTO userDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImplement userDetailsImplement = (UserDetailsImplement) authentication.getPrincipal();

        List<String> roles = userDetailsImplement.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new JWTResponse(userDetailsImplement.getId(),
                jwt,
                userDetailsImplement.getFirstName(),
                userDetailsImplement.getLastName(),
                userDetailsImplement.getEmail(),
                roles);
    }

    public List<PartialUserDTO> getAll() {
        return userRepository.findAll()
                .stream()
                .map(user -> userMapper.mapToPartialUser(user))
                .collect(Collectors.toList());
    }
}
