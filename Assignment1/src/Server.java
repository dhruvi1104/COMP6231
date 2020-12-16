import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public interface Server extends Remote{
	public String addEvent(String eventID, String eventType, int bookingCapacity) throws RemoteException;
	public String removeEvent(String eventID, String eventType) throws RemoteException;
	public String listEventAvailability(String eventType) throws RemoteException;
	
	public String validUser(String currentUser) throws RemoteException;
	
	public String bookEvent(String customerID, String eventID, String eventType) throws RemoteException, SocketException, UnknownHostException, IOException;
	public String getBookingSchedule (String customerID) throws RemoteException, SocketException, UnknownHostException, IOException;
	public String cancelEvent (String customerID, String eventID, String eventType) throws RemoteException, UnknownHostException, SocketException, IOException;
}
