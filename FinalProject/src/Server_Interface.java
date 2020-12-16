
public interface Server_Interface {
	public String addEvent(String eventID, String eventType, int bookingCapacity);
	public String removeEvent(String eventID, String eventType);
	public String listEventAvailability(String eventType);
	
	public String validUser(String currentUser);
	
	public String bookEvent(String customerID, String eventID, String eventType);
	public String getBookingSchedule (String customerID);
	public String cancelEvent (String customerID, String eventID, String eventType);
	public String swapEvent(String customerID, String newEventID, String newEventType, String oldEventID, String oldEventType);

}