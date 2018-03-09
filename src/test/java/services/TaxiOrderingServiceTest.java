package services;

import model.DAOException;
import model.dao.api.*;
import model.dao.impl.*;
import model.pojo.Car;
import model.pojo.Ride;
import model.pojo.TaxiService;
import model.pojo.User;
import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import services.api.TaxiOrderingService;
import services.impl.TaxiOrderingServiceImpl;

import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TaxiOrderingServiceTest {
    static {
        PropertyConfigurator.configure(UserServiceTest.class.getClassLoader().getResource("log4j.properties"));
    }

    private static final Logger LOGGER = LogManager.getLogger(UserServiceTest.class);

    @Mock private TaxiServiceDAO taxiServiceControllerMock;
    @Mock private CityDAO cityControllerMock;
    @Mock private RideDAO rideControllerMock;
    @Mock private CarDAO carControllerMock;
    @Mock private UserDAO userControllerMock;

    @InjectMocks
    private TaxiOrderingService taxiOrderingService;

    @Before
    public void setUp() {
        taxiServiceControllerMock = mock(TaxiServiceController.class);
        cityControllerMock = mock(CityController.class);
        rideControllerMock = mock(RideController.class);
        carControllerMock = mock(CarController.class);
        userControllerMock = mock(UserController.class);

        taxiOrderingService = new TaxiOrderingServiceImpl(
                taxiServiceControllerMock,
                cityControllerMock,
                rideControllerMock,
                carControllerMock,
                userControllerMock
        );
    }

    @After
    public void tearDown() {
        taxiOrderingService = null;
    }

    @Test
    public void placeValidOrder() throws DAOException, ServiceException {
        LOGGER.debug("Testing valid order placement");

        int userId = 2;
        int taxiServiceId = 1;
        int cityId = 1;
        String locationFrom = "Campus";
        String locationTo = "SportsComplex";
        String phoneNumber = "+79191234567";
        String orderComments = "";

        User user = new User(
                2, "test_user", "Test", "User", "",
                null, 0, null, false
        );
        doReturn(user).when(userControllerMock).getEntityById(userId);
        doReturn(1).when(userControllerMock).update(any(User.class));

        TaxiService taxiService = new TaxiService(
                taxiServiceId, cityId, "economy", 49, false
        );
        doReturn(taxiService).when(taxiServiceControllerMock).getEntityById(taxiServiceId);

        doReturn(35).when(rideControllerMock).insert(any(Ride.class));

        List<Car> allCars = new ArrayList<>();
        allCars.add(new Car(1, "C070MK16", "Hyundai Solaris",
                "white", 1, true, false));
        allCars.add(new Car(1, "E210PE116", "Renault Logan",
                "black", 2, false, false));
        allCars.add(new Car(1, "M123MM116", "Lada Granta",
                "white", 3, false, true));

        doReturn(allCars).when(carControllerMock).getAll();

        Ride ride = taxiOrderingService.placeOrder(
                userId, taxiServiceId, cityId, true, locationFrom, locationTo,
                phoneNumber, false, false, orderComments
        );

        /* correct identifiers */
        assertEquals(userId, ride.getUserId());
        assertEquals(taxiServiceId, ride.getTaxiServiceId());

        /* car is valid */
        assertTrue(ride.getCarId() > 0 && ride.getCarId() < 4);
        assertNotEquals("Car is not null", ride.getCar());

        /* price is valid */
        assertTrue(ride.getPrice() > taxiService.getBaseRate());

        /* driver was selected properly for given conditions */
        assertTrue(ride.getCar().getDriverId() > 0);

        /* checking if cityId was saved */
        assertEquals(cityId, user.getCityId());

        /* checking if phoneNumber was not saved */
        assertNotEquals(phoneNumber, user.getPhoneNumber());

        /* verifying method calls */
        verify(userControllerMock).getEntityById(userId);
        verify(userControllerMock).update(any(User.class));     // city info was saved
        verify(taxiServiceControllerMock).getEntityById(taxiServiceId);
        verify(rideControllerMock).insert(ride);

        /* another type of ride */
        Ride rideWithChildSeat = taxiOrderingService.placeOrder(
                userId, taxiServiceId, cityId, true, locationFrom, locationTo,
                phoneNumber, false, true, orderComments
        );
        /* only one driver is suitable */
        assertTrue(rideWithChildSeat.getCarId() == 1);

    }

    @Test(expected = ServiceException.class)
    public void placeInvalidOrder() throws DAOException, ServiceException {
        LOGGER.debug("Testing order placement with invalid taxi service id");

        int userId = 2;
        int taxiServiceId = 1;
        int cityId = 1;
        String locationFrom = "Campus";
        String locationTo = "SportsComplex";
        String phoneNumber = "+79191234567";
        String orderComments = "";

        User user = new User(
                2, "test_user", "Test", "User", "",
                null, 0, null, false
        );
        doReturn(user).when(userControllerMock).getEntityById(userId);
        doReturn(1).when(userControllerMock).update(any(User.class));

        doThrow(DAOException.class).when(taxiServiceControllerMock).getEntityById(taxiServiceId);

        Ride ride = taxiOrderingService.placeOrder(
                userId, taxiServiceId, cityId, true, locationFrom, locationTo,
                phoneNumber, false, false, orderComments
        );
    }

    @Test(expected = ServiceException.class)
    public void placeOrderWithoutCarsAvailable() throws DAOException, ServiceException {
        LOGGER.debug("Testing valid order placement with no cars available");

        int userId = 2;
        int taxiServiceId = 1;
        int cityId = 1;
        String locationFrom = "Campus";
        String locationTo = "SportsComplex";
        String phoneNumber = "+79191234567";
        String orderComments = "";

        User user = new User(
                2, "test_user", "Test", "User", "",
                null, 0, null, false
        );
        doReturn(user).when(userControllerMock).getEntityById(userId);
        doReturn(1).when(userControllerMock).update(any(User.class));

        TaxiService taxiService = new TaxiService(
                taxiServiceId, cityId, "economy", 49, false
        );
        doReturn(taxiService).when(taxiServiceControllerMock).getEntityById(taxiServiceId);

        doReturn(35).when(rideControllerMock).insert(any(Ride.class));

        doReturn(new ArrayList<>()).when(carControllerMock).getAll();

        Ride ride = taxiOrderingService.placeOrder(
                userId, taxiServiceId, cityId, true, locationFrom, locationTo,
                phoneNumber, false, false, orderComments
        );
    }

    @Test
    public void cancelOrder() throws DAOException, ServiceException {
        LOGGER.debug("Testing valid order cancelling");

        int rideId = 35;

        Ride ride = new Ride(
                rideId, 2, 1, 1, new Timestamp(0), "Campus", "SportsComplex",
                new Timestamp(0), new Timestamp(0), 66, 0, "", "ordered"
        );

        doReturn(ride).when(rideControllerMock).getEntityById(rideId);
        doReturn(1).when(rideControllerMock).update(ride);

        taxiOrderingService.cancelOrder(rideId);

        verify(rideControllerMock).getEntityById(rideId);
        verify(rideControllerMock).update(ride);
    }

    @Test(expected = ServiceException.class)
    public void cancelStartedOrder() throws DAOException, ServiceException {
        LOGGER.debug("Testing invalid order cancelling");

        int rideId = 35;

        Ride ride = new Ride(
                rideId, 2, 1, 1, new Timestamp(0), "Campus", "SportsComplex",
                new Timestamp(0), new Timestamp(0), 66, 0, "", "finished"
        );

        doReturn(ride).when(rideControllerMock).getEntityById(rideId);

        taxiOrderingService.cancelOrder(rideId);
    }
}
