
import java.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import HelloApll.Hello;
import HelloApll.HelloHelper;

public class Client 
{

	static Hello helloImpl;
	static Hello helloImpl1;
	static Hello helloImpl2;
	static Hello helloImpl3;
	static Hello helloImpl4;
	static Hello helloImpl5;
	static Hello helloImpl6;
	static Hello helloImpl7;
	
	public static void main(String args[]) throws NotBoundException, MalformedURLException, InvalidName, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName
  
	{
		Scanner a = new Scanner(System.in);
		String currentUser = null;
		Logger log=null;
		FileHandler file;
		ORB orb = ORB.init(args, null);
		org.omg.CORBA.Object obj = orb.resolve_initial_references("NameService");
        NamingContextExt ncRef = NamingContextExtHelper.narrow(obj);
		
		while(true) {
			System.out.println("Enter Client ID : ");
			currentUser = a.next();
			
			try
            {
                log=Logger.getLogger("Client");
                file=new FileHandler("D:/Concordia/COMP6231/log/"+currentUser+".log",true);
                log.addHandler(file);
                log.setUseParentHandlers(false);
                SimpleFormatter sf=new SimpleFormatter();
                file.setFormatter(sf);
            
            }catch(IOException e){e.printStackTrace();}
			
			if(currentUser.matches("...C....")) 
			{
				String r = null;
				if(currentUser.matches("MTL.....")) {
					String name = "MTL";
	    	        helloImpl = HelloHelper.narrow(ncRef.resolve_str(name));
				}else if(currentUser.matches("OTW.....")) {
					String name = "OTW";
	    	        helloImpl = HelloHelper.narrow(ncRef.resolve_str(name));
				}else if(currentUser.matches("TOR.....")) {
					String name = "TOR";
	    	        helloImpl = HelloHelper.narrow(ncRef.resolve_str(name));
				}
				int flag = 0;
				String customerID = null;
				String eventID = null;
				String eventType = null;
				do {
					System.out.println("Client");
					System.out.println("1. Book Event");
					System.out.println("2. Get Booking Schedule");
					System.out.println("3. Cancel Event");
					System.out.println("4. Swap");
					System.out.println("5. Multithreding");
					System.out.println("0. Exit");
					int option = a.nextInt();
					switch (option) {
						case 1:
							try {
								customerID = currentUser;
								System.out.println("Event ID : ");
								eventID = a.next();
								System.out.println("Event Type(C/S/T) : ");
								eventType = a.next();
								r = helloImpl.bookEvent(customerID, eventID, eventType);
								System.out.println(r);
								log.log(Level.INFO, "Customer ID: {0} {1}", new Object[]{customerID, r});
							}catch(Exception e){}
							//log
							break;
						case 2:
							try {
								customerID = currentUser;
								r = helloImpl.getBookingSchedule(customerID);
								System.out.println(r);
								//log.log(Level.INFO, "Customer ID: {0} {1}", new Object[]{customerID, r});
							}catch(Exception e) {}
							//log
							break;
						case 3:
							try {
								customerID = currentUser;
								System.out.println("Event ID : ");
								eventID = a.next();
								System.out.println("Event Type(C/S/T) : ");
								eventType = a.next();
								r = helloImpl.cancelEvent(customerID, eventID, eventType);
								System.out.println(r);
								log.log(Level.INFO, "Customer ID: {0} {1}", new Object[]{customerID, r});
							}catch(Exception e) {}
							//log
							break;
						case 4:
							try {
								customerID = currentUser;
								System.out.println("Old Event ID : ");
								String oldEventID = a.next();
								System.out.println("Old Event Type(C/S/T) : ");
								String oldEventType = a.next();
								System.out.println("New Event ID : ");
								String newEventID = a.next();
								System.out.println("New Event Type(C/S/T) : ");
								String newEventType = a.next();
								r = helloImpl.swapEvent(customerID, newEventID, newEventType, oldEventID, oldEventType);
								System.out.println(r);
								log.log(Level.INFO, "Customer ID: {0} {1}", new Object[]{customerID, r});
							}catch(Exception e) {}
							//log
							break;
						case 5:
							Runnable runnable =
					        () -> {
					        	String name = "MTL";
				    	        try {
									helloImpl1 = HelloHelper.narrow(ncRef.resolve_str(name));
								} catch (NotFound | CannotProceed
										| org.omg.CosNaming.NamingContextPackage.InvalidName e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
					        	String r1 = null;
								try {
									r1 = helloImpl1.addEvent("MTLM230520","T",1);
									System.out.println(r1);									
								} catch (Exception e) {
									e.printStackTrace();
								}
					        };
							
					        Runnable runnable1 =
					        () -> { 
					        	String name = "TOR";
				    	        try {
									helloImpl2 = HelloHelper.narrow(ncRef.resolve_str(name));
								} catch (NotFound | CannotProceed
										| org.omg.CosNaming.NamingContextPackage.InvalidName e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
				    	        String r2 = null;
								try {
									r2 = helloImpl2.addEvent("TORE210520","C",2);
									System.out.println(r2);
								} catch (Exception e) {
									e.printStackTrace();
								}
							};
									
					        Runnable runnable2 =
					        () -> { 
					        	String name = "OTW";
				    	        try {
									helloImpl3 = HelloHelper.narrow(ncRef.resolve_str(name));
								} catch (NotFound | CannotProceed
										| org.omg.CosNaming.NamingContextPackage.InvalidName e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
				    	        String r3 = null;
								try {
									r3 = helloImpl3.bookEvent("OTWC1234","TORE210520","C");
									System.out.println(r3);
								} catch (Exception e) {
									e.printStackTrace();
								}
							};
							
							Runnable runnable3 =
					        () -> { 
					        	String name = "TOR";
				    	        try {
									helloImpl4 = HelloHelper.narrow(ncRef.resolve_str(name));
								} catch (NotFound | CannotProceed
										| org.omg.CosNaming.NamingContextPackage.InvalidName e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
				    	        String r4 = null;
								try {
									r4 = helloImpl4.bookEvent("TORC1234","TORE210520","C");
									System.out.println(r4);
								} catch (Exception e) {
									e.printStackTrace();
								}
							};
							
							Runnable runnable4 =
					        () -> { 
					        	String name = "MTL";
				    	        try {
									helloImpl5 = HelloHelper.narrow(ncRef.resolve_str(name));
								} catch (NotFound | CannotProceed
										| org.omg.CosNaming.NamingContextPackage.InvalidName e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
				    	        String r5 = null;
								try {
									r5 = helloImpl5.bookEvent("MTLC1234","TORE210520","C");
									System.out.println(r5);
								} catch (Exception e) {
									e.printStackTrace();
								}
							};
							
							Runnable runnable5 =
					        () -> { 
					        	String name = "TOR";
				    	        try {
									helloImpl6 = HelloHelper.narrow(ncRef.resolve_str(name));
								} catch (NotFound | CannotProceed
										| org.omg.CosNaming.NamingContextPackage.InvalidName e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
				    	        String r6 = null;
								try {
									r6 = helloImpl6.swapEvent("TORC1234","MTLM230520","T","TORE210520","C");
									System.out.println(r6);
								} catch (Exception e) {
									e.printStackTrace();
								}
							};
							
							Thread t=new Thread(runnable);
							Thread t1=new Thread(runnable1);
							Thread t2=new Thread(runnable2);
							Thread t3=new Thread(runnable3);
							Thread t4=new Thread(runnable4);
							Thread t5=new Thread(runnable5);
									
							t.start();
							t1.start();
							t2.start();
							t3.start();
							t4.start();
							t5.start();
							break;
						case 0:
							flag = 1;
							continue;
						default:
							System.out.println("Choose correct option.");
					}
				}while(flag==0);
				
			}else if(currentUser.matches("...M....")) {
				helloImpl = null;
				if(currentUser.matches("MTL.....")){
					String name = "MTL";
	    	        helloImpl = HelloHelper.narrow(ncRef.resolve_str(name));
	    	        String r = helloImpl.validUser(currentUser);
					if(r.equalsIgnoreCase("N")) {
						System.out.println("You are not valid user.");
						continue;
					}
				}
				else if(currentUser.matches("OTW.....")) {
					String name = "OTW";
	    	        helloImpl = HelloHelper.narrow(ncRef.resolve_str(name));
					String r = helloImpl.validUser(currentUser);
					if(r.equalsIgnoreCase("N")) {
						System.out.println("You are not valid user.");
						continue;
					}
				}else if(currentUser.matches("TOR.....")) {
					String name = "TOR";
	    	        helloImpl = HelloHelper.narrow(ncRef.resolve_str(name));
					String r = helloImpl.validUser(currentUser);
					if(r.equalsIgnoreCase("N")) {
						System.out.println("You are not valid user.");
						continue;
					}
				}
				int flag=0;
				String reply = null;
				String eventID = null;
				String eventType = null;
				String customerID = null;
				int bookingCapacity = 0;
				do {
					System.out.println("Manager");
					System.out.println("1. Add Event");
					System.out.println("2. Remove Event");
					System.out.println("3. List Event Availability");
					System.out.println("4. Book event for Customer");
					System.out.println("5. Get Booking Schedule for Customer");
					System.out.println("6. Cancel Event for Customer");
					System.out.println("7. Swap Event for Customer");
					System.out.println("0. Exit");
					int option = a.nextInt();
					switch (option) {
						case 1:
							try {
								System.out.println("Event ID : ");
								eventID = a.next();
								System.out.println("Event Type(C/S/T) : ");
								eventType = a.next();
								System.out.println("Booking Capacity : ");
								bookingCapacity = a.nextInt();
								reply = helloImpl.addEvent(eventID, eventType, bookingCapacity);
								System.out.println(reply);
 								log.log(Level.INFO, "Manager ID: {0} {1}", new Object[]{currentUser, reply});
							}catch(Exception e) {}
							//log
							break;
						case 2:
							try {
								System.out.println("Event ID : ");
								eventID = a.next();
								System.out.println("Event Type(C/S/T) : ");
								eventType = a.next();
								reply = helloImpl.removeEvent(eventID, eventType);
								System.out.println(reply);
 								log.log(Level.INFO, "Manager ID: {0} {1}", new Object[]{currentUser, reply});
							}catch(Exception e) {}
							//log
							break;
						case 3:
							try {
								System.out.println("Event Type(C/S/T) : ");
								eventType = a.next();
								reply = helloImpl.listEventAvailability(eventType);
								System.out.println(reply);
 								//log.log(Level.INFO, "Manager ID: {0} {1}", new Object[]{currentUser, reply});
							}catch(Exception e) {}
							//log
							break;
						case 4:
							try {
								System.out.println("Customer ID : ");
								customerID = a.next();
								System.out.println("Event ID : ");
								eventID = a.next();
								System.out.println("Event Type(C/S/T) : ");
								eventType = a.next();
								reply = helloImpl.bookEvent(customerID, eventID, eventType);
								System.out.println(reply);
  								log.log(Level.INFO, "Manager ID: {0} {1} {2}", new Object[]{currentUser, customerID, reply});
							}catch(Exception e) {}
							//log
							break;
						case 5:
							try {
								System.out.println("Customer ID : ");
								customerID = a.next();
								reply = helloImpl.getBookingSchedule(customerID);
								System.out.println(reply);
  								//log.log(Level.INFO, "Manager ID: {0} {1} {2}", new Object[]{currentUser, customerID, reply});
							}catch(Exception e) {}
							//log
							break;
						case 6:
							try {
								System.out.println("Customer ID : ");
								customerID = a.next();
								System.out.println("Event ID : ");
								eventID = a.next();
								System.out.println("Event Type(C/S/T) : ");
								eventType = a.next();
								reply = helloImpl.cancelEvent(customerID, eventID, eventType);
								System.out.println(reply);
 								log.log(Level.INFO, "Manager ID: {0} {1} {2}", new Object[]{currentUser, customerID, reply});
							}catch(Exception e) {}
							//log
							break;
						case 7:
							try {
								System.out.println("Customer ID : ");
								customerID = a.next();
								System.out.println("Old Event ID : ");
								String oldEventID = a.next();
								System.out.println("Old Event Type(C/S/T) : ");
								String oldEventType = a.next();
								System.out.println("New Event ID : ");
								String newEventID = a.next();
								System.out.println("New Event Type(C/S/T) : ");
								String newEventType = a.next();
								reply = helloImpl.swapEvent(customerID, newEventID, newEventType, oldEventID, oldEventType);
								System.out.println(reply);
								log.log(Level.INFO, "Manager ID: {0} {1} {2}", new Object[]{currentUser, customerID, reply});
							}catch(Exception e) {}
							//log
							break;
						case 0:
							flag = 1;
							break;
						default:
							System.out.println("Choose correct option.");
					}
				}while(flag==0);
			}
		}
	}
}
