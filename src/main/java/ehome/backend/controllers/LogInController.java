package ehome.backend.controllers;

import ehome.backend.dtos.CredentialsDTO;
import ehome.backend.services.UserService;
import ehome.backend.utils.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/authenticate")
@CrossOrigin("*")
public class LogInController {

    private final AuthenticationManager authenticationManager;

    private final TokenProvider jwtTokenUtil;
    private final UserService userService;


    @Autowired
    public LogInController(AuthenticationManager authenticationManager, TokenProvider tokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = tokenProvider;
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<List<String>> generateToken(@RequestBody CredentialsDTO credentials) throws AuthenticationException {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credentials.getUsername(),
                        credentials.getPassword()
                )
        );
        List<String> responseList = new ArrayList<String>();
        UserDetails userDetails = userService.loadUserByUsername(credentials.getUsername());
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                responseList.add("ADMIN");
            } else {
                responseList.add("CLIENT");
            }
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateToken(authentication);
        responseList.add(token);
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }


}
