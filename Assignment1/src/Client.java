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


public class Client {	
		
	public static void main(String args[]) throws  NotBoundException, MalformedURLException, RemoteException, IOException{
		Scanner a = new Scanner(System.in);
		String currentUser = null;
		Logger log=null;
		FileHandler file;
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
			
			if(currentUser.matches("...C....")) {
				Server o_server = null;
				String r = null;
				if(currentUser.matches("MTL.....")) {
					o_server = (Server)Naming.lookup("rmi://localhost:6666/dg_mtl");
				}else if(currentUser.matches("OTW.....")) {
					o_server = (Server)Naming.lookup("rmi://localhost:7777/dg_otw");
				}else if(currentUser.matches("TOR.....")) {
					o_server = (Server)Naming.lookup("rmi://localhost:8888/dg_tor");
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
					System.out.println("4. MultiBooking");
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
								r = o_server.bookEvent(customerID, eventID, eventType);
								System.out.println(r);
								log.log(Level.INFO, "Customer ID: {0} {1}", new Object[]{customerID, r});
							}catch(Exception e){}
							//log
							break;
						case 2:
							try {
								customerID = currentUser;
								r = o_server.getBookingSchedule(customerID);
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
								r = o_server.cancelEvent(customerID, eventID, eventType);
								System.out.println(r);
								log.log(Level.INFO, "Customer ID: {0} {1}", new Object[]{customerID, r});
							}catch(Exception e) {}
							//log
							break;
						case 4:
							Runnable runnable =
					        () -> {
					        	Server o_server1 = null;
					        	String r1 = null;
								try {
									o_server1 = (Server)Naming.lookup("rmi://localhost:6666/dg_mtl");
									r1 = o_server1.addEvent("MTLM230520","T",1);
									System.out.println(r1);									
								} catch (Exception e) {
									e.printStackTrace();
								}
					        };
							
					        Runnable runnable1 =
					        () -> { 
					        	Server o_server2 = null;
					        	String r2 = null;
								try {
									o_server2 = (Server)Naming.lookup("rmi://localhost:8888/dg_tor");
									r2 = o_server2.bookEvent("TORC1234","MTLM230520","T");
									System.out.println(r2);
								} catch (Exception e) {
									e.printStackTrace();
								}
							};
									
					        Runnable runnable2 =
					        () -> { 
					        	Server o_server3 = null;
					        	String r3 = null;
								try {
									o_server3 = (Server)Naming.lookup("rmi://localhost:7777/dg_otw");
									r3 = o_server3.bookEvent("OTWC1234","MTLM230520","T");
									System.out.println(r3);
								} catch (Exception e) {
									e.printStackTrace();
								}
							};
							
							Runnable runnable3 =
					        () -> { 
					        	Server o_server4 = null;
					        	String r4 = null;
								try {
									o_server4 = (Server)Naming.lookup("rmi://localhost:8888/dg_tor");
									r4 = o_server4.cancelEvent("TORC1234","MTLM230520","T");
									System.out.println(r4);
								} catch (Exception e) {
									e.printStackTrace();
								}
							};
							Thread t=new Thread(runnable);
							Thread t1=new Thread(runnable1);
							Thread t2=new Thread(runnable2);
							Thread t3=new Thread(runnable3);
									
							t.start();
							t1.start();
							t2.start();
							t3.start();
					        break;
						case 0:
							flag = 1;
							continue;
						default:
							System.out.println("Choose correct option.");
					}
				}while(flag==0);
				
			}else if(currentUser.matches("...M....")) {
				Server o_server = null;
				if(currentUser.matches("MTL.....")) {
					o_server = (Server)Naming.lookup("rmi://localhost:6666/dg_mtl");
					String r = o_server.validUser(currentUser);
					if(r.equalsIgnoreCase("N")) {
						System.out.println("You are not valid user.");
						continue;
					}
				}else if(currentUser.matches("OTW.....")) {
					o_server = (Server)Naming.lookup("rmi://localhost:7777/dg_otw");
					String r = o_server.validUser(currentUser);
					if(r.equalsIgnoreCase("N")) {
						System.out.println("You are not valid user.");
						continue;
					}
				}else if(currentUser.matches("TOR.....")) {
					o_server = (Server)Naming.lookup("rmi://localhost:8888/dg_tor");
					String r = o_server.validUser(currentUser);
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
								reply = o_server.addEvent(eventID, eventType, bookingCapacity);
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
								reply = o_server.removeEvent(eventID, eventType);
								System.out.println(reply);
 								log.log(Level.INFO, "Manager ID: {0} {1}", new Object[]{currentUser, reply});
							}catch(Exception e) {}
							//log
							break;
						case 3:
							try {
								System.out.println("Event Type(C/S/T) : ");
								eventType = a.next();
								reply = o_server.listEventAvailability(eventType);
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
								reply = o_server.bookEvent(customerID, eventID, eventType);
								System.out.println(reply);
  								log.log(Level.INFO, "Manager ID: {0} {1} {2}", new Object[]{currentUser, customerID, reply});
							}catch(Exception e) {}
							//log
							break;
						case 5:
							try {
								System.out.println("Customer ID : ");
								customerID = a.next();
								reply = o_server.getBookingSchedule(customerID);
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
								reply = o_server.cancelEvent(customerID, eventID, eventType);
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
