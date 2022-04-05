package ehome.backend.controllers;

import ehome.backend.dtos.AdministratorDTO;
import ehome.backend.services.UserService;
import ehome.backend.utils.exceptions.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/register")
@CrossOrigin("*")
public class RegisterController {

    private final UserService userService;

    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<List<String>> registerAdmin(@RequestBody AdministratorDTO administratorDTO) {
        try {
            userService.registerAdministrator(administratorDTO);
            return new ResponseEntity<>(Collections.singletonList("Admin registered successfully"), HttpStatus.OK);
        } catch (DataAccessException dataAccessException) {
            return new ResponseEntity<>(Collections.singletonList("Could not save user"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UserAlreadyExistsException userAlreadyExistsException) {
            return new ResponseEntity<>(Collections.singletonList("User already exists"), HttpStatus.CONFLICT);
        }
    }
}
