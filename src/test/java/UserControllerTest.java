import java.util.Collections;
import java.util.List;
import ehome.backend.controllers.UserController;
import ehome.backend.dtos.ClientDTO;
import ehome.backend.services.UserService;
import ehome.backend.utils.exceptions.UserAlreadyExistsException;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;


    @Test
    public void testRegisterUserExists() throws UserAlreadyExistsException {
        ClientDTO dummy = ClientDTO.builder().name("Ion").address("Acasa").password("anaaremere").build();
        Mockito.doThrow(new UserAlreadyExistsException("")).when(userService).createClient(dummy);
        ResponseEntity<List<String>> response = userController.createClient(dummy);
        Assert.assertEquals(response, new ResponseEntity<>(Collections.singletonList(""), HttpStatus.CONFLICT));
    }
}
