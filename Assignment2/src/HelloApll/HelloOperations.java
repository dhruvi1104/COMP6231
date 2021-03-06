package HelloApll;


/**
* HelloApll/HelloOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from C:/Users/Dhruvi/eclipse-workspace/Assignment2/src/Hello.idl
* Thursday, June 13, 2019 8:03:53 PM EDT
*/

public interface HelloOperations 
{
  String bookEvent (String customerID, String eventID, String eventType);
  String getBookingSchedule (String customerID);
  String cancelEvent (String customerID, String eventID, String eventType);
  String swapEvent (String customerID, String newEventID, String newEventType, String oldEventID, String oldEventType);
  String addEvent (String eventID, String eventType, int bookingCapacity);
  String removeEvent (String eventID, String eventType);
  String listEventAvailability (String eventType);
  String validUser (String currentUser);
} // interface HelloOperations
