package ehome.backend.controllers;

import ehome.backend.dtos.DeviceConsumptionDTO;
import ehome.backend.dtos.DeviceDTO;
import ehome.backend.dtos.MeasurementDTO;
import ehome.backend.services.DeviceService;
import ehome.backend.utils.TokenProvider;
import ehome.backend.utils.exceptions.DeviceNotFoundException;
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
@RequestMapping(("/devices"))
@CrossOrigin
@Validated
public class DeviceController {

    private final DeviceService deviceService;

    private final TokenProvider tokenProvider;

    @Autowired
    public DeviceController(DeviceService deviceService, TokenProvider tokenProvider) {
        this.deviceService = deviceService;
        this.tokenProvider = tokenProvider;
    }

    @GetMapping("/all")
    public ResponseEntity<List<DeviceDTO>> getDevices() {
        try {
            List<DeviceDTO> devices = deviceService.findAll();
            return new ResponseEntity<>(devices, HttpStatus.OK);
        } catch (DataAccessException exception) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<List<String>> createDevice(@Valid @RequestBody DeviceDTO deviceDTO) {
        try {
            deviceService.createDevice(deviceDTO);
            return new ResponseEntity<>(Collections.singletonList("Device successfully created"), HttpStatus.OK);
        } catch (DataAccessException exception) {
            return new ResponseEntity<>(Collections.singletonList("Something went wrong. Please try again"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<List<String>> updateDevice(@Valid @RequestBody DeviceDTO deviceDTO) {
        try {
            deviceService.updateDevice(deviceDTO);
            return new ResponseEntity<>(Collections.singletonList("Device successfully updated"), HttpStatus.OK);
        } catch (DeviceNotFoundException deviceNotFoundException) {
            return new ResponseEntity<>(Collections.singletonList(deviceNotFoundException.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (ConstraintViolationException constraintViolationException) {
            return new ResponseEntity<>(Collections.singletonList("Invalid device data"), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<String>> deleteDevice(@PathVariable("id") Long id) {
        try {
            deviceService.deleteDevice(id);
            return new ResponseEntity<>(Collections.singletonList("Device successfully deleted"), HttpStatus.OK);
        } catch (DataAccessException exception) {
            return new ResponseEntity<>(Collections.singletonList("Something went wrong. Please try again"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (DeviceNotFoundException deviceNotFoundException) {
            return new ResponseEntity<>(Collections.singletonList(deviceNotFoundException.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (ConstraintViolationException constraintViolationException) {
            return new ResponseEntity<>(Collections.singletonList("Invalid device data"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/sensorless")
    public ResponseEntity<List<DeviceDTO>> getSensorlessDevices() {
        try {
            List<DeviceDTO> devices = deviceService.findAllSensorless();
            return new ResponseEntity<>(devices, HttpStatus.OK);
        } catch (DataAccessException exception) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/clientless")
    public ResponseEntity<List<DeviceDTO>> getClientlessDevices() {
        try {
            List<DeviceDTO> devices = deviceService.findAllClientless();
            return new ResponseEntity<>(devices, HttpStatus.OK);
        } catch (DataAccessException exception) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/client/{token}")
    public ResponseEntity<List<DeviceConsumptionDTO>> getDevicesOfClient(@PathVariable("token") String token) {
        try {
            String username = tokenProvider.getUsernameFromToken(token);
            List<DeviceConsumptionDTO> devices = deviceService.getDevicesOfClient(username);
            return new ResponseEntity<>(devices, HttpStatus.OK);
        } catch (DataAccessException exception) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/consumption_history/{device_id}")
    public ResponseEntity<List<MeasurementDTO>> getHistoricalConsumption(@PathVariable("device_id") Long deviceId) {
        try {
            List<MeasurementDTO> historicalConsumption = deviceService.getHistoricalConsumption(deviceId);
            return new ResponseEntity<>(historicalConsumption, HttpStatus.OK);
        } catch (DataAccessException exception) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
