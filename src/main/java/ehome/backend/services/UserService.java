package ehome.backend.services;

import ehome.backend.dtos.AdministratorDTO;
import ehome.backend.dtos.ClientDTO;
import ehome.backend.entities.Administrator;
import ehome.backend.entities.Client;
import ehome.backend.entities.Device;
import ehome.backend.entities.Measurement;
import ehome.backend.repositories.AdministratorRepository;
import ehome.backend.repositories.ClientRepository;
import ehome.backend.repositories.DeviceRepository;
import ehome.backend.repositories.MeasurementRepository;
import ehome.backend.utils.exceptions.DeviceAlreadyAssignedException;
import ehome.backend.utils.exceptions.DeviceNotFoundException;
import ehome.backend.utils.exceptions.UserAlreadyExistsException;
import ehome.backend.utils.exceptions.UserNotFoundException;
import ehome.backend.utils.mappers.DtoToEntityMappers;
import ehome.backend.utils.mappers.EntityToDtoMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {


    private final AdministratorRepository administratorRepository;
    private final ClientRepository clientRepository;
    private final DeviceRepository deviceRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MeasurementRepository measurementRepository;

    @Autowired
    public UserService(ClientRepository clientRepository, DeviceRepository deviceRepository,
                       AdministratorRepository administratorRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                       MeasurementRepository measurementRepository) {
        this.deviceRepository = deviceRepository;
        this.clientRepository = clientRepository;
        this.administratorRepository = administratorRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.measurementRepository = measurementRepository;
    }

    public List<ClientDTO> findAll() {
        return clientRepository.findAll().stream().map(EntityToDtoMappers::fromEntity).collect(Collectors.toList());
    }

    public void createClient(ClientDTO clientDTO) throws UserAlreadyExistsException {
        Client client = DtoToEntityMappers.toEntity(clientDTO);
        client.setPassword(bCryptPasswordEncoder.encode(client.getPassword()));
        if (clientRepository.findByUsername(clientDTO.getUsername()) != null) {
            throw new UserAlreadyExistsException("User already exists");
        } else {
            clientRepository.save(client);
        }
    }

    public void updateClient(ClientDTO clientDTO) throws UserNotFoundException {
        Client updated = DtoToEntityMappers.toEntity(clientDTO);
        Client toBeEdited = clientRepository.findById(clientDTO.getId()).orElse(null);

        if (toBeEdited == null) {
            throw new UserNotFoundException("User does not exist");
        }
        updated.setPassword(toBeEdited.getPassword());

        updated.setDevices(toBeEdited.getDevices());
        clientRepository.save(updated);

    }

    public void deleteClient(Long id) throws UserNotFoundException {
        Client toBeDeleted = clientRepository.findById(id).orElse(null);
        if (toBeDeleted == null) {
            throw new UserNotFoundException("User does not exist");
        } else {
            List<Device> toBeDeletedDevices = deviceRepository.findAllByClient(toBeDeleted);
            for (Device device : toBeDeletedDevices) {
                List<Measurement> measurements = measurementRepository.findAllByDevice(device);
                for (Measurement measurement : measurements) {
                    measurementRepository.delete(measurement);
                }
                deviceRepository.delete(device);
            }
            clientRepository.delete(toBeDeleted);
        }
    }

    public void assignDeviceToClient(Long clientId, Long deviceId) throws UserNotFoundException, DeviceNotFoundException, DeviceAlreadyAssignedException {
        Client client = clientRepository.findById(clientId).orElse(null);
        Device toBeAssigned = deviceRepository.findById(deviceId).orElse(null);

        if (client == null) {
            throw new UserNotFoundException("User does not exist");
        } else if (toBeAssigned == null) {
            throw new DeviceNotFoundException("Device does not exist");
        } else if (toBeAssigned.getClient() != null) {
            throw new DeviceAlreadyAssignedException("Device is already assigned to a client");
        } else {
            toBeAssigned.setClient(client);
            deviceRepository.save(toBeAssigned);
        }
    }

    public void registerAdministrator(AdministratorDTO administratorDTO) throws UserAlreadyExistsException {
        Administrator administrator = DtoToEntityMappers.toEntity(administratorDTO);
        administrator.setPassword(bCryptPasswordEncoder.encode(administrator.getPassword()));
        if (administratorRepository.findByUsername(administratorDTO.getUsername()) != null) {
            throw new UserAlreadyExistsException("User already exists");
        } else {
            administratorRepository.save(administrator);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = clientRepository.findByUsername(username);
        Administrator administrator = administratorRepository.findByUsername(username);
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        if (client == null && administrator == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        } else if (administrator == null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_CLIENT"));
            return new org.springframework.security.core.userdetails.User(client.getUsername(), client.getPassword(), authorities);
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            return new org.springframework.security.core.userdetails.User(administrator.getUsername(), administrator.getPassword(), authorities);
        }
    }


}
