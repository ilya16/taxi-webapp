package ru.innopolis.services;

import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import ru.innopolis.model.DAOException;
import ru.innopolis.model.dao.api.UserDAO;
import ru.innopolis.model.pojo.User;
import ru.innopolis.services.api.UserService;
import ru.innopolis.services.impl.UserServiceImpl;

import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    static {
        PropertyConfigurator.configure(UserServiceTest.class.getClassLoader().getResource("log4j.properties"));
    }

    private static final Logger LOGGER = LogManager.getLogger(UserServiceTest.class);

    @Mock
    private UserDAO userControllerMock;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private UserService userService;

    @Before
    public void setUp() {
        userService = new UserServiceImpl(userControllerMock);
    }

    @After
    public void tearDown() {
        userService = null;
    }

    @Test
    public void signIn() throws ServiceException, DAOException {
        LOGGER.debug("Testing valid authorization");

        String login = "test_user";
        String password = "test_password";

        User testUser = new User(
                2, login, "Test", "User", "hashed_password",
                null, 1, new Timestamp(0), false
        );

        doReturn(testUser).when(userControllerMock).findUserByLoginAndPassword(login, password);

        User user = userService.auth(login, password);
        System.out.println(user.getPassword());
        assertEquals(login, user.getLogin());
        assertEquals("Test", user.getFirstName());
        assertEquals("User", user.getLastName());
        assertEquals("hashed_password", testUser.getPassword());
        assertEquals(false, user.isBlocked());

        verify(userControllerMock).findUserByLoginAndPassword(login, password);
    }

    @Test(expected = ServiceException.class)
    public void signInWrongPassword() throws ServiceException, DAOException {
        LOGGER.debug("Testing authorization with wrong password");

        String login = "test_user";
        String password = "wrong_password";

        doThrow(DAOException.class).when(userControllerMock).findUserByLoginAndPassword(login, password);

        User user = userService.auth(login, password);
    }

    @Test(expected = ServiceException.class)
    public void signInWrongLogin() throws ServiceException, DAOException {
        LOGGER.debug("Testing authorization with wrong login");

        String login = "test_use";
        String password = "test_password";

        doThrow(DAOException.class).when(userControllerMock).findUserByLoginAndPassword(login, password);

        User user = userService.auth(login, password);
    }

    @Test(expected = ServiceException.class)
    public void signInBlockedUser() throws ServiceException, DAOException {
        LOGGER.debug("Testing authorization of a blocked user");

        String login = "superuser";
        String password = "test_password";

        doThrow(DAOException.class).when(userControllerMock).findUserByLoginAndPassword(login, password);

        User user = userService.auth(login, password);
    }

    @Test
    public void register() throws ServiceException, DAOException {
        LOGGER.debug("Testing valid registration");

        String login = "bob";
        String firstName = "Bob";
        String lastName = "Smith";
        String password = "strong_password";

        doThrow(DAOException.class).when(userControllerMock).findUserByLoginAndPassword(login, password);
        doReturn(2).when(userControllerMock).insert(any(User.class));
        doReturn(new User(
                        2, login, firstName, lastName, "",
                        null, 0, null, false
        )).when(userControllerMock).getEntityById(2);

        User user = userService.register(login, firstName, lastName, password);
        assertEquals(login, user.getLogin());
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(false, user.isBlocked());

        verify(userControllerMock, times(1))
                .findUserByLogin(login);
    }

    @Test(expected = ServiceException.class)
    public void registerUserWithTakenLogin() throws ServiceException, DAOException {
        LOGGER.debug("Testing registration with already taken login");

        String login = "test_user";
        String password = "user_password";

        doThrow(DAOException.class).when(userControllerMock).findUserByLoginAndPassword(login, password);

        userService.auth(login, password);

        verify(userControllerMock, never()).getEntityById(anyInt());
    }

    @Test
    public void getUser() throws DAOException, ServiceException {
        LOGGER.debug("Testing user retrieval by identifier");

        Integer userId = 2;
        String login = "test_user";
        String firstName = "Test";
        String lastName = "User";

        doReturn(new User(
                2, login, firstName, lastName, "",
                null, 0, null, false
        )).when(userControllerMock).getEntityById(userId);

        User testUser = userService.getUser(userId);

        assertEquals(login, testUser.getLogin());
        assertEquals(firstName, testUser.getFirstName());
        assertEquals(lastName, testUser.getLastName());
        assertEquals(false, testUser.isBlocked());

        verify(userControllerMock).getEntityById(userId);
        verify(userControllerMock, never()).getEntityById(3);
    }

    @Test(expected = ServiceException.class)
    public void getUserWithInvalidId() throws DAOException, ServiceException {
        Integer userId = 0;

        doThrow(DAOException.class).when(userControllerMock).getEntityById(userId);

        User invalidUser = userService.getUser(userId);
    }
}
