package ehome.backend.controllers;

import ehome.backend.dtos.ClientDTO;
import ehome.backend.services.UserService;
import ehome.backend.utils.exceptions.DeviceAlreadyAssignedException;
import ehome.backend.utils.exceptions.DeviceNotFoundException;
import ehome.backend.utils.exceptions.UserAlreadyExistsException;
import ehome.backend.utils.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/clients")
@CrossOrigin
@Validated
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ClientDTO>> getClients() {
        try {
            List<ClientDTO> clients = userService.findAll();
            return new ResponseEntity<>(clients, HttpStatus.OK);
        } catch (DataAccessException exception) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<List<String>> createClient(@Valid @RequestBody ClientDTO clientDTO) {
        try {
            userService.createClient(clientDTO);
            return new ResponseEntity<>(Collections.singletonList("Client successfully created"), HttpStatus.OK);
        } catch (DataAccessException exception) {
            return new ResponseEntity<>(Collections.singletonList("Something went wrong. Please try again"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UserAlreadyExistsException userAlreadyExistsException) {
            return new ResponseEntity<>(Collections.singletonList(userAlreadyExistsException.getMessage()), HttpStatus.CONFLICT);
        } catch (ConstraintViolationException constraintViolationException) {
            return new ResponseEntity<>(Collections.singletonList("Invalid user data"), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<List<String>> updateClient(@Valid @RequestBody ClientDTO clientDTO) {
        try {
            userService.updateClient(clientDTO);
            return new ResponseEntity<>(Collections.singletonList("Client successfully updated"), HttpStatus.OK);
        } catch (DataAccessException exception) {
            return new ResponseEntity<>(Collections.singletonList("Something went wrong. Please try again"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UserNotFoundException userNotFoundException) {
            return new ResponseEntity<>(Collections.singletonList(userNotFoundException.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (ConstraintViolationException constraintViolationException) {
            return new ResponseEntity<>(Collections.singletonList("Invalid user data"), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<String>> deleteClient(@PathVariable("id") Long id) {
        try {
            userService.deleteClient(id);
            return new ResponseEntity<>(Collections.singletonList("Client successfully deleted"), HttpStatus.OK);
        } catch (DataAccessException exception) {
            return new ResponseEntity<>(Collections.singletonList("Something went wrong. Please try again"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UserNotFoundException userNotFoundException) {
            return new ResponseEntity<>(Collections.singletonList(userNotFoundException.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{client_id}/{device_id}")
    public ResponseEntity<List<String>> assignDeviceToClient(@PathVariable("client_id") Long clientId, @PathVariable("device_id") Long deviceId) {
        try {
            userService.assignDeviceToClient(clientId, deviceId);
            return new ResponseEntity<>(Collections.singletonList("Device successfully assigned"), HttpStatus.OK);
        } catch (DataAccessException exception) {
            return new ResponseEntity<>(Collections.singletonList("Something went wrong. Please try again"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UserNotFoundException | DeviceNotFoundException | DeviceAlreadyAssignedException notFoundException) {
            return new ResponseEntity<>(Collections.singletonList(notFoundException.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

}
