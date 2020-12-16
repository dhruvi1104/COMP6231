import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import HelloApll.Hello;
import HelloApll.HelloHelper;
import HelloApll.HelloPOA;

public class OTW extends HelloPOA{

	private ORB orb;
	HashMap<String, HashMap<String, Integer>> OTWEvents = null;
	HashMap<String, Integer> Conferences = null;
	HashMap<String, Integer> Seminars = null;
	HashMap<String, Integer> TradeShows = null;
	ArrayList<String> manager = null;
	
	HashMap<String,ArrayList<String>> OTW_Conferences = null;
	HashMap<String,ArrayList<String>> OTW_Seminars = null;
	HashMap<String,ArrayList<String>> OTW_TradeShows = null;
	
	HashMap<String,ArrayList<Integer>> Counter = null;
	
	Logger log = null;
	FileHandler file;
	
	OTW() throws RemoteException{
		
		try {
			log=Logger.getLogger("OTW");
            file=new FileHandler("D:/Concordia/COMP6231/log/OTW.log",true);
            log.addHandler(file);
            log.setUseParentHandlers(false);
            SimpleFormatter sf=new SimpleFormatter();
            file.setFormatter(sf);
		}catch(Exception e) {}
		
		OTWEvents = new HashMap<String, HashMap<String, Integer>>();
		
		Conferences = new HashMap<String, Integer>();
		/*
		Conferences.put("OTWM210520", 5);
		Conferences.put("OTWM220520", 5);
		Conferences.put("OTWM230520", 5);
		Conferences.put("OTWM240520", 5);
		Conferences.put("OTWM250520", 5);
		*/
		Seminars = new HashMap<String, Integer>();
		/*
		Seminars.put("OTWA210520", 5);
		Seminars.put("OTWA220520", 5);
		Seminars.put("OTWA230520", 5);
		Seminars.put("OTWA240520", 5);
		Seminars.put("OTWA250520", 5);
		*/
		TradeShows = new HashMap<String, Integer>();
		/*
		TradeShows.put("OTWE210520", 5);
		TradeShows.put("OTWE220520", 5);
		TradeShows.put("OTWE230520", 5);
		TradeShows.put("OTWE240520", 5);
		TradeShows.put("OTWE250520", 5);
		*/
		OTWEvents.put("C", Conferences);
		OTWEvents.put("S", Seminars);
		OTWEvents.put("T", TradeShows);
		
		manager = new ArrayList<String>();
		
		manager.add("OTWM1100");
		manager.add("OTWM1200");
		manager.add("OTWM1300");
		manager.add("OTWM9000");
		Counter = new HashMap<>();
		OTW_Conferences = new HashMap<>();
		
		OTW_Seminars = new HashMap<>();
		
		OTW_TradeShows = new HashMap<>();
		
		
	}
	
	public void setORB(ORB orb_val)
  	{
  		orb = orb_val;
    }
	
	boolean alreadyBooked(String customerID, String eventID, String eventType){
		if(eventType.matches("C")) {
			if(OTW_Conferences.containsKey(customerID)) {
				if(OTW_Conferences.get(customerID).contains(eventID)) {
					return true;
				}
			}
		}else if(eventType.matches("S")) {
			if(OTW_Seminars.containsKey(customerID)) {
				if(OTW_Seminars.get(customerID).contains(eventID)) {
					return true;
				}
			}
		}else if(eventType.matches("T")) {
			if(OTW_TradeShows.containsKey(customerID)) {
				if(OTW_TradeShows.get(customerID).contains(eventID)) {
					return true;
				}
			}
		}
		return false;
	}
	
	synchronized String bookOuterEvent(String customerID, String eventID, String eventType) throws IOException {
		eventType=eventType.trim();
		customerID=customerID.trim();
		eventID=eventID.trim();
		try {
			if(eventID.matches("OTW.......")) {
				if(eventType.equalsIgnoreCase("C")) {
					if(alreadyBooked(customerID, eventID, eventType)) {
						return "YOU HAVE ALREADY BOOKED THIS EVENT.";
					}else {
						if(OTWEvents.get(eventType).containsKey(eventID)) {
							int availability = OTWEvents.get(eventType).get(eventID);
							if(availability!=0) {
								OTWEvents.get(eventType).put(eventID, OTWEvents.get(eventType).get(eventID)-1);
								System.out.println(OTWEvents);
								log.log(Level.INFO, "{0} booked {1} of Conferences", new Object[]{customerID, eventID});
								return "Yes";
							}else {
								return "THIS EVENT IS FULL.";
							}
						}else {
							return "ENTER THE VALID EVENTID.";
						}
					}
				}else if(eventType.equalsIgnoreCase("S")) {
					if(alreadyBooked(customerID, eventID, eventType)) {
						return "YOU HAVE ALREADY BOOKED THIS EVENT.";
					}else {
						if(OTWEvents.get(eventType).containsKey(eventID)) {
							int availability = OTWEvents.get(eventType).get(eventID);
							if(availability!=0) {
								OTWEvents.get(eventType).put(eventID, OTWEvents.get(eventType).get(eventID)-1);
								System.out.println(OTWEvents);
								log.log(Level.INFO, "{0} booked {1} of Seminars", new Object[]{customerID, eventID});
								return "Yes";
							}else {
								return "THIS EVENT IS FULL.";
							}
						}else {
							return "ENTER THE VALID EVENTID.";
						}
					}
				}else if(eventType.equalsIgnoreCase("T")) {
					if(alreadyBooked(customerID, eventID, eventType)) {
						return "YOU HAVE ALREADY BOOKED THIS EVENT.";
					}else {
						if(OTWEvents.get(eventType).containsKey(eventID)) {
							int availability = OTWEvents.get(eventType).get(eventID);
							if(availability!=0) {
								OTWEvents.get(eventType).put(eventID, OTWEvents.get(eventType).get(eventID)-1);
								System.out.println(OTWEvents);
								log.log(Level.INFO, "{0} booked {1} of TradeShows", new Object[]{customerID, eventID});
								return "Yes";
							}else {
								return "THIS EVENT IS FULL.";
							}
						}else {
							return "ENTER THE VALID EVENTID.";
						}
					}
				}else {
					return "ENTER THE VALID EVENTID.";
				}
			}
		}catch(Exception e) {}
		finally {
			//log
		}
		return null;
	}
	
	synchronized String cancelOuterEvent(String customerID, String eventID, String eventType){
		eventID=eventID.trim();
		customerID=customerID.trim();
		eventType=eventType.trim();
		if(eventType.equalsIgnoreCase("C")) {
			if(OTWEvents.get("C").containsKey(eventID)) {
				OTWEvents.get("C").put(eventID, OTWEvents.get("C").get(eventID)+1);
				System.out.println(OTWEvents);
				log.log(Level.INFO, "{0} canceled {1} of Conferences", new Object[]{customerID, eventID});
				return "yes";
			}else {
				return "EVENT DOES NOT EXIST.";
			}
		}else if(eventType.equalsIgnoreCase("S")) {
			if(OTWEvents.get("S").containsKey(eventID)) {
				OTWEvents.get("S").put(eventID, OTWEvents.get("S").get(eventID)+1);
				System.out.println(OTWEvents);
				log.log(Level.INFO, "{0} canceled {1} of Seminars", new Object[]{customerID, eventID});
				return "yes";
			}else {
				return "EVENT DOES NOT EXIST.";
			}
		}else if(eventType.equalsIgnoreCase("T")) {
			if(OTWEvents.get("T").containsKey(eventID)) {
				OTWEvents.get("T").put(eventID, OTWEvents.get("T").get(eventID)+1);
				System.out.println(OTWEvents);
				log.log(Level.INFO, "{0} canceled {1} of TradeShows", new Object[]{customerID, eventID});
				return "yes";
			}else {
				return "EVENT DOES NOT EXIST.";
			}
		}else {
			return "EVENT DOES NOT EXIST.";
		}
	}
	
	synchronized String removeOuterEvent(String eventID, String eventType){
		eventID=eventID.trim();
		eventType=eventType.trim();
		int m = Integer.parseInt(eventID.substring(6, 8));
		try {
			if(eventType.equalsIgnoreCase("C")) {
				for(HashMap.Entry<String,ArrayList<String>> pair: OTW_Conferences.entrySet()) {
					if(pair.getValue().contains(eventID)) {
						OTW_Conferences.get(pair.getKey()).remove(eventID);
						Counter.get(pair.getKey()).set(m-1, Counter.get(pair.getKey()).get(m-1)-1);
						System.out.println("OTW_Conferences : "+OTW_Conferences);
						System.out.println("Counter : "+Counter);
					}
				}
				log.log(Level.INFO, "{0} of Conferences has been removed for all Ottawa customers", new Object[]{eventID});
				return "EVENT HAS BEEN REMOVED.";
			}else if(eventType.equalsIgnoreCase("S")) {
				for(HashMap.Entry<String,ArrayList<String>> pair: OTW_Seminars.entrySet()) {
					if(pair.getValue().contains(eventID)) {
						OTW_Seminars.get(pair.getKey()).remove(eventID);
						Counter.get(pair.getKey()).set(m-1, Counter.get(pair.getKey()).get(m-1)-1);
						System.out.println("OTW_Seminars : "+OTW_Seminars);
						System.out.println("Counter : "+Counter);
					}
				}
				log.log(Level.INFO, "{0} of Seminars has been removed for all Ottawa customers", new Object[]{eventID});
				return "EVENT HAS BEEN REMOVED.";
			}else if(eventType.equalsIgnoreCase("T")) {
				for(HashMap.Entry<String,ArrayList<String>> pair: OTW_TradeShows.entrySet()) {
					if(pair.getValue().contains(eventID)) {
						OTW_TradeShows.get(pair.getKey()).remove(eventID);
						Counter.get(pair.getKey()).set(m-1, Counter.get(pair.getKey()).get(m-1)-1);
						System.out.println("OTW_TradeShows : "+OTW_TradeShows);
						System.out.println("Counter : "+Counter);
					}
				}
				log.log(Level.INFO, "{0} of TradeShows has been removed for all Ottawa customers", new Object[]{eventID});
				return "EVENT HAS BEEN REMOVED.";
			}else {
				return "ENTER THE VALID EVENTTYPE.";
			}
		}catch(Exception e) {}
		finally {
			//log
		}
		return null;
	}
	
	String listOuterEvent(String eventType) {
		eventType=eventType.trim();
		String s = "";
		if(eventType.equalsIgnoreCase("C")) {
			s=s+OTWEvents.get("C").toString();
		}else if(eventType.equalsIgnoreCase("S")) {
			s=s+OTWEvents.get("S").toString();
		}else if(eventType.equalsIgnoreCase("T")) {
			s=s+OTWEvents.get("T").toString();
		}
		return s;
	}
	
	@Override
	public synchronized String swapEvent(String customerID,String newEventID,String newEventType,String oldEventID,String oldEventType)
	{
		String drop=cancelEvent(customerID, oldEventID, oldEventType);
		String compare_drop="EVENT HAS BEEN CANCELED.";
		if(drop.equalsIgnoreCase(compare_drop)){
			String result= bookEvent(customerID, newEventID, newEventType);
			String compare="BOOKING IS SUCCESSFUL.";
			if(result.equalsIgnoreCase(compare)){
				//log.log(Level.INFO, "Student with ID: {0} Swapped Course {1} to {2}", new Object[]{StudentID, CourseID, NewCourseID});
				return "SWAPPING SUCCESSFUL.";
			}
			else{
				bookEvent(customerID, oldEventID, oldEventType);
				return "SWAP FAILED.";
			}
		}
		else{
			//enrollCourse(StudentID, CourseID, Semester);
			return "SWAP FAILED.";	
		}
	}
		
	public static void main(String args[]) {
		try {
//			OTW o_otw = new OTW();
//			Registry reg = LocateRegistry.createRegistry(7777);
//			reg.bind("dg_otw", o_otw);
			
			ORB orb = ORB.init(args, null);
	          POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
	   	      rootpoa.the_POAManager().activate();
	   	      

	   	      OTW o_otw = new OTW();
	   	      o_otw.setORB(orb); 
	   	      
	   	      // get object reference from the servant
	   	      org.omg.CORBA.Object ref = rootpoa.servant_to_reference(o_otw);
	   	      
	   	      Hello href = HelloHelper.narrow(ref);
	   	      // get the root naming context
	   	      // NameService invokes the name service
	   	      
	   	      org.omg.CORBA.Object objRef =
	   	      orb.resolve_initial_references("NameService");
	   	      // Use NamingContextExt which is part of the Interoperable
	   	      // Naming Service (INS) specification.
	   	      
	   	      NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
	   	      // bind the Object Reference in Naming
	   	      
	   	      String name = "OTW";
	   	      NameComponent path[] = ncRef.to_name( name );
	   	      ncRef.rebind(path, href);
			
			System.out.println("Ottawa server is Running.");
			Runnable task = () -> {
				DatagramSocket aSocket = null;
				while(true) {
					try {
						aSocket = new DatagramSocket(3332);
						byte[] b1 = new byte[10000];
						DatagramPacket Reply = new DatagramPacket(b1,b1.length);
						aSocket.receive(Reply);
						String str = new String(Reply.getData());
						System.out.println("String: "+str);
						String[] splited = str.split("\\s+");
                        if(splited[0].equalsIgnoreCase("BOOKEVENT")) {
                        	System.out.println("Received request from MTL for booking event.");
                        	String ret = o_otw.bookOuterEvent(splited[1],splited[2],splited[3]);
                        	System.out.println(ret);
                            byte[] temp = new byte[100000];
                            temp = ret.getBytes();
                            DatagramPacket se=new DatagramPacket(temp,temp.length,Reply.getAddress(),Reply.getPort());
                            aSocket.send(se);
                        }else if(splited[0].equalsIgnoreCase("CANCELEVENT")) {
                        	String ret = o_otw.cancelOuterEvent(splited[1],splited[2],splited[3]);
                            byte[] temp = new byte[100000];
                            temp = ret.getBytes();
                            DatagramPacket se=new DatagramPacket(temp,temp.length,Reply.getAddress(),Reply.getPort());
                            aSocket.send(se);
                        }else if(splited[0].equalsIgnoreCase("REMOVEEVENT")) {
                        	System.out.println("Recived request from MTL server for removing event.");
                        	String ret = o_otw.removeOuterEvent(splited[1],splited[2]);
                            byte[] temp = new byte[100000];
                            temp = ret.getBytes();
                            DatagramPacket se=new DatagramPacket(temp,temp.length,Reply.getAddress(),Reply.getPort());
                            aSocket.send(se);
                        }else if(splited[0].equalsIgnoreCase("LIST")) {
                        	String ret = o_otw.listOuterEvent(splited[1]);
                            byte[] temp = new byte[100000];
                            temp = ret.getBytes();
                            DatagramPacket se=new DatagramPacket(temp,temp.length,Reply.getAddress(),Reply.getPort());
                            aSocket.send(se);
                        }
						
					}catch(Exception e) {
						System.out.println(e.getMessage());
					}finally {
						if(aSocket != null) {
							aSocket.close();
						}
					}
				}
			};
			 Thread t=new Thread(task);
	           t.start(); 
	           
	           Runnable task1 = () -> {
					DatagramSocket aSocket = null;
					while(true) {
						try {
							aSocket = new DatagramSocket(3334);
							byte[] b1 = new byte[10000];
							DatagramPacket Reply = new DatagramPacket(b1,b1.length);
							aSocket.receive(Reply);
							String str = new String(Reply.getData());
							System.out.println("String: "+str);
							String[] splited = str.split("\\s+");
	                        if(splited[0].equalsIgnoreCase("BOOKEVENT")) {
	                        	System.out.println("Received request for booking event.");
	                        	String ret = o_otw.bookOuterEvent(splited[1],splited[2],splited[3]);
	                        	System.out.println(ret);
	                            byte[] temp = new byte[100000];
	                            temp = ret.getBytes();
	                            DatagramPacket se=new DatagramPacket(temp,temp.length,Reply.getAddress(),Reply.getPort());
	                            aSocket.send(se);
	                        }else if(splited[0].equalsIgnoreCase("CANCELEVENT")) {
	                        	String ret = o_otw.cancelOuterEvent(splited[1],splited[2],splited[3]);
	                            byte[] temp = new byte[100000];
	                            temp = ret.getBytes();
	                            DatagramPacket se=new DatagramPacket(temp,temp.length,Reply.getAddress(),Reply.getPort());
	                            aSocket.send(se);
	                        }else if(splited[0].equalsIgnoreCase("REMOVEEVENT")) {
	                        	String ret = o_otw.removeOuterEvent(splited[1],splited[2]);
	                            byte[] temp = new byte[100000];
	                            temp = ret.getBytes();
	                            DatagramPacket se=new DatagramPacket(temp,temp.length,Reply.getAddress(),Reply.getPort());
	                            aSocket.send(se);
	                        }else if(splited[0].equalsIgnoreCase("LIST")) {
	                        	String ret = o_otw.listOuterEvent(splited[1]);
	                            byte[] temp = new byte[100000];
	                            temp = ret.getBytes();
	                            DatagramPacket se=new DatagramPacket(temp,temp.length,Reply.getAddress(),Reply.getPort());
	                            aSocket.send(se);
	                        }
							
						}catch(Exception e) {
							System.out.println(e.getMessage());
						}finally {
							if(aSocket != null) {
								aSocket.close();
							}
						}
					}
				};
				 Thread t1=new Thread(task1);
		           t1.start(); 
		           orb.run();  
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	
	@Override
	public synchronized String addEvent(String eventID, String eventType, int bookingCapacity) {
		// TODO Auto-generated method stub
		String reply=null;
		HashMap<String, Integer> data = new HashMap<String, Integer>();
		try {
			if(eventID.substring(0, 3).equalsIgnoreCase("OTW")) {
				if(eventType.equals("C")) {
					if(OTWEvents.get("C")!=null) {
						data = OTWEvents.get("C");
					}
					data.put(eventID, bookingCapacity);
					OTWEvents.put("C", data);
					System.out.println(OTWEvents);
					log.log(Level.INFO, "Event {0} added in {1}", new Object[]{eventID, eventType});
					return "EVENT ADDED SUCCESSFULLY.";
				}else if(eventType.equals("S")) {
					if(OTWEvents.get("S")!=null) {
						data = OTWEvents.get("S");
					}
					data.put(eventID, bookingCapacity);
					OTWEvents.put("S", data);
					System.out.println(OTWEvents);
					log.log(Level.INFO, "Event {0} added in {1}", new Object[]{eventID, eventType});
					return "EVENT ADDED SUCCESSFULLY.";
				}else if(eventType.equals("T")) {
					if(OTWEvents.get("T")!=null) {
						data = OTWEvents.get("T");
					}
					data.put(eventID, bookingCapacity);
					OTWEvents.put("T", data);
					System.out.println(OTWEvents);
					log.log(Level.INFO, "Event {0} added in {1}", new Object[]{eventID, eventType});
					return "EVENT ADDED SUCCESSFULLY.";
				}
			}else {
				return "ENTER VALID EVENTID.";
			}
		}catch(Exception e) {}
		return reply;
	}

	@Override
	public String validUser(String currentUser) {
		// TODO Auto-generated method stub
		if(manager.contains(currentUser)) {
			return "Y";
		}else {
			return "N";
		}
	}

	@Override
	public synchronized String bookEvent(String customerID, String eventID, String eventType) {
		// TODO Auto-generated method stub
		eventType=eventType.trim();
		try {
			if(customerID.matches("OTWC....")) {
				if(eventID.matches("OTW.......")) {
					if(eventType.equalsIgnoreCase("c")) {
						if(alreadyBooked(customerID, eventID, eventType)) {
							return "YOU HAVE ALREADY BOOKED THIS EVENT.";
						}else {
							if(OTWEvents.get(eventType).containsKey(eventID)) {
								int availability = OTWEvents.get(eventType).get(eventID);
								if(availability!=0) {
									Counter.putIfAbsent(customerID, new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0)));
									OTW_Conferences.putIfAbsent(customerID, new ArrayList<>());
									OTW_Conferences.get(customerID).add(eventID);
									OTWEvents.get(eventType).put(eventID, OTWEvents.get(eventType).get(eventID)-1);
									System.out.println(OTWEvents);
									System.out.println(OTW_Conferences);
									log.log(Level.INFO, "Customer {0} booked {1} of {2}", new Object[]{customerID, eventID, eventType});
									return "BOOKING IS SUCCESSFUL.";
								}else {
									return "THIS EVENT IS FULL.";
								}
							}else {
								return "ENTER THE VALID EVENTID.";
							}
						}
					}else if(eventType.equalsIgnoreCase("S")) {
						if(alreadyBooked(customerID, eventID, eventType)) {
							return "YOU HAVE ALREADY BOOKED THIS EVENT.";
						}else {
							if(OTWEvents.get(eventType).containsKey(eventID)) {
								int availability = OTWEvents.get(eventType).get(eventID);
								if(availability!=0) {
									Counter.putIfAbsent(customerID, new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0)));
									OTW_Seminars.putIfAbsent(customerID, new ArrayList<>());
									OTW_Seminars.get(customerID).add(eventID);
									OTWEvents.get(eventType).put(eventID, OTWEvents.get(eventType).get(eventID)-1);
									System.out.println(OTWEvents);
									System.out.println(OTW_Seminars);
									log.log(Level.INFO, "Customer {0} booked {1} of {2}", new Object[]{customerID, eventID, eventType});
									return "BOOKING IS SUCCESSFUL.";
								}else {
									return "THIS EVENT IS FULL.";
								}
							}else {
								return "ENTER THE VALID EVENTID.";
							}
						}
					}else if(eventType.equalsIgnoreCase("T")) {
						if(alreadyBooked(customerID, eventID, eventType)) {
							return "YOU HAVE ALREADY BOOKED THIS EVENT.";
						}else {
							if(OTWEvents.get(eventType).containsKey(eventID)) {
								int availability = OTWEvents.get(eventType).get(eventID);
								if(availability!=0) {
									Counter.putIfAbsent(customerID, new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0)));
									OTW_TradeShows.putIfAbsent(customerID, new ArrayList<>());
									OTW_TradeShows.get(customerID).add(eventID);
									OTWEvents.get(eventType).put(eventID, OTWEvents.get(eventType).get(eventID)-1);
									System.out.println(OTWEvents);
									System.out.println(OTW_TradeShows);
									log.log(Level.INFO, "Customer {0} booked {1} of {2}", new Object[]{customerID, eventID, eventType});
									return "BOOKING IS SUCCESSFUL.";
								}else {
									return "THIS EVENT IS FULL.";
								}
							}else {
								return "ENTER THE VALID EVENTID.";
							}
						}
					}else {
						return "ENTER THE VALID EVENTID.";
					}
				}else if(eventID.matches("MTL.......")) {
					DatagramSocket asocket = new DatagramSocket();
	                InetAddress aHost = InetAddress.getByName("localhost");
	                
					Counter.putIfAbsent(customerID, new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0)));
					int m = Integer.parseInt(eventID.substring(6, 8));
					int i = Counter.get(customerID).get(m-1);
					if(i<3) {
						String s="BOOKEVENT"+" "+customerID+" "+eventID+" "+eventType;
	                    byte[] reply = s.getBytes();
	                    DatagramPacket req = new DatagramPacket(reply,reply.length,aHost,2223);
	                    asocket.send(req);
	                    System.out.println("Request sent to MTL server for booking event.");
	                    
	                    byte[] buffer = new byte[1000];
	                    DatagramPacket Rec = new DatagramPacket(buffer,buffer.length);
	                    asocket.receive(Rec);
	                    String temp = new String(Rec.getData());
	                    System.out.print("Result from MTL: "+temp);
	                    if("yes".equalsIgnoreCase(temp.trim().toString())) {
	                    	Counter.putIfAbsent(customerID, new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0)));
	                    	if(eventType.matches("C")) {
	                    		OTW_Conferences.putIfAbsent(customerID, new ArrayList<>());
	                    		OTW_Conferences.get(customerID).add(eventID);
								Counter.get(customerID).set(m-1, Counter.get(customerID).get(m-1)+1);
								System.out.println(Counter);
								System.out.println(OTW_Conferences);
								log.log(Level.INFO, "Customer {0} booked {1} of {2}", new Object[]{customerID, eventID, eventType});
								return "BOOKING IS SUCCESSFUL.";
	        				}else if(eventType.matches("S")) {
	        					OTW_Seminars.putIfAbsent(customerID, new ArrayList<>());
	        					OTW_Seminars.get(customerID).add(eventID);
								Counter.get(customerID).set(m-1, Counter.get(customerID).get(m-1)+1);
								System.out.println(Counter);
								System.out.println(OTW_Seminars);
								log.log(Level.INFO, "Customer {0} booked {1} of {2}", new Object[]{customerID, eventID, eventType});
								return "BOOKING IS SUCCESSFUL.";
	        				}else if(eventType.matches("T")) {
								OTW_TradeShows.putIfAbsent(customerID, new ArrayList<>());
								OTW_TradeShows.get(customerID).add(eventID);
								Counter.get(customerID).set(m-1, Counter.get(customerID).get(m-1)+1);
								System.out.println(Counter);
								System.out.println(OTW_TradeShows);
								log.log(Level.INFO, "Customer {0} booked {1} of {2}", new Object[]{customerID, eventID, eventType});
								return "BOOKING IS SUCCESSFUL.";
	        				}
	                    }else {
	                    	return "FAILED TO BOOK THE EVENT.";
	                    }
					}else {
						return "YOU HAVE ALREADY REGISTERED 3 EVENTS OUTSIDE TORONTO.";
					}
				}else if(eventID.matches("TOR.......")) {
					DatagramSocket asocket = new DatagramSocket();
	                InetAddress aHost = InetAddress.getByName("localhost");
	                
					Counter.putIfAbsent(customerID, new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0)));
					int m = Integer.parseInt(eventID.substring(6, 8));
					int i = Counter.get(customerID).get(m-1);
					if(i<3) {
						String s="BOOKEVENT"+" "+customerID+" "+eventID+" "+eventType;
	                    byte[] reply = s.getBytes();
	                    DatagramPacket req = new DatagramPacket(reply,reply.length,aHost,4443);
	                    asocket.send(req);
	                    System.out.println("Request sent to TOR server for booking event.");
	                    
	                    byte[] buffer = new byte[1000];
	                    DatagramPacket Rec = new DatagramPacket(buffer,buffer.length);
	                    asocket.receive(Rec);
	                    String temp = new String(Rec.getData());
	                    
	                    if("yes".equalsIgnoreCase(temp.trim().toString())) {
	                    	Counter.putIfAbsent(customerID, new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0)));
	                    	if(eventType.matches("C")) {
	                    		OTW_Conferences.putIfAbsent(customerID, new ArrayList<>());
	                    		OTW_Conferences.get(customerID).add(eventID);
								Counter.get(customerID).set(m-1, Counter.get(customerID).get(m-1)+1);
								System.out.println(Counter);
								System.out.println(OTW_Conferences);
								log.log(Level.INFO, "Customer {0} booked {1} of {2}", new Object[]{customerID, eventID, eventType});
								return "BOOKING IS SUCCESSFUL.";
	        				}else if(eventType.matches("S")) {
	        					OTW_Seminars.putIfAbsent(customerID, new ArrayList<>());
	        					OTW_Seminars.get(customerID).add(eventID);
								Counter.get(customerID).set(m-1, Counter.get(customerID).get(m-1)+1);
								System.out.println(Counter);
								System.out.println(OTW_Seminars);
								log.log(Level.INFO, "Customer {0} booked {1} of {2}", new Object[]{customerID, eventID, eventType});
								return "BOOKING IS SUCCESSFUL.";
	        				}else if(eventType.matches("T")) {
								OTW_TradeShows.putIfAbsent(customerID, new ArrayList<>());
								OTW_TradeShows.get(customerID).add(eventID);
								Counter.get(customerID).set(m-1, Counter.get(customerID).get(m-1)+1);
								System.out.println(Counter);
								System.out.println(OTW_TradeShows);
								log.log(Level.INFO, "Customer {0} booked {1} of {2}", new Object[]{customerID, eventID, eventType});
								return "BOOKING IS SUCCESSFUL.";
	        				}
	                    }else {
	                    	return "FAILED TO BOOK THE EVENT.";
	                    }
					}else {
						return "YOU HAVE ALREADY REGISTERED 3 EVENTS OUTSIDE OTTAWA.";
					}
				}else{
					return "ENTER THE VALID EVENTID.";
				}
			}else {
				return "ENTER THE VALID CUSTOMERID.";
			}
		}catch(Exception e) {}
		finally {
			//log
		}
		
		return null;
	}

	@Override
	public synchronized String removeEvent(String eventID, String eventType) {
		// TODO Auto-generated method stub
		eventType=eventType.trim();
		eventID=eventID.trim();
		String reply=null;
		try {
			if(eventID.substring(0, 3).equalsIgnoreCase("OTW")) {
				if(OTWEvents.get(eventType).containsKey(eventID)) {
					
					DatagramSocket asocket = new DatagramSocket();
	                InetAddress aHost = InetAddress.getByName("localhost");
	                
	                String s="REMOVEEVENT"+" "+eventID+" "+eventType;
                    byte[] reply1 = s.getBytes();
                    //********************OTW*************************
                    DatagramPacket req_to_mtl = new DatagramPacket(reply1,reply1.length,aHost,2223);
                    asocket.send(req_to_mtl);
                    System.out.println("Request sent to MTL server for removing event.");
                    
                    byte[] buffer = new byte[1000];
                    DatagramPacket Rec_from_mtl = new DatagramPacket(buffer,buffer.length);
                    asocket.receive(Rec_from_mtl);
                    String temp = new String(Rec_from_mtl.getData());
                    System.out.println(temp);
					//**********************TOR*******************
                    DatagramPacket req_to_tor = new DatagramPacket(reply1,reply1.length,aHost,4443);
                    asocket.send(req_to_tor);
                    System.out.println("Request sent to TOR server for removing event.");
                    
                    byte[] buffer1 = new byte[1000];
                    DatagramPacket Rec_from_tor = new DatagramPacket(buffer,buffer.length);
                    asocket.receive(Rec_from_tor);
                    String temp1 = new String(Rec_from_tor.getData());
                    System.out.println(temp1);
                    
					OTWEvents.get(eventType).remove(eventID);
					System.out.println("OTWEvents : "+OTWEvents);
					if(eventType.equals("C")) {
						for(HashMap.Entry<String,ArrayList<String>> pair: OTW_Conferences.entrySet()) {
							if(pair.getValue().contains(eventID)) {
								OTW_Conferences.get(pair.getKey()).remove(eventID);
								System.out.println("OTW_Conferences : "+OTW_Conferences);
							}
						}
						log.log(Level.INFO, "Event {0} of Conferences has been removed", new Object[]{eventID});
						return "EVENT HAS BEEN REMOVED.";
					}else if(eventType.equals("S")) {
						for(HashMap.Entry<String,ArrayList<String>> pair: OTW_Seminars.entrySet()) {
							if(pair.getValue().contains(eventID)) {
								OTW_Seminars.get(pair.getKey()).remove(eventID);
								System.out.println("OTW_Seminars : "+OTW_Seminars);
							}
						}
						log.log(Level.INFO, "Event {0} of Seminars has been removed", new Object[]{eventID});
						return "EVENT HAS BEEN REMOVED.";
					}else if(eventType.equals("T")) {
						for(HashMap.Entry<String,ArrayList<String>> pair: OTW_TradeShows.entrySet()) {
							if(pair.getValue().contains(eventID)) {
								OTW_TradeShows.get(pair.getKey()).remove(eventID);
								System.out.println("OTW_TradeShows : "+OTW_TradeShows);
							}
						}
						log.log(Level.INFO, "Event {0} of TradeShows has been removed", new Object[]{eventID});
						return "EVENT HAS BEEN REMOVED.";
					}
					else
					{
						return "ENTER THE VALID EVENTID.";
					}
				}
				else 
				{
					return "THIS EVENT IS NOT REGISTERED.";
				}
			}
			else
			{
				return "ENTER THE VALID EVENTID.";
			}
		}catch(Exception e) {}
		return null;
	}

	@Override
	public String listEventAvailability(String eventType) {
		// TODO Auto-generated method stub
				String s = listOuterEvent(eventType);
							
				DatagramSocket asocket = null;
				try {
					asocket = new DatagramSocket();
		            InetAddress aHost = InetAddress.getByName("localhost");
		            
		            String str="LIST"+" "+eventType;
		            byte[] reply1 = str.getBytes();
		            //********************OTW*************************
		            DatagramPacket req_to_mtl = new DatagramPacket(reply1,reply1.length,aHost,2223);
		            asocket.send(req_to_mtl);
		            System.out.println("Request sent to MTL server for listing event.");
		            
		            byte[] buffer = new byte[1000];
		            DatagramPacket Rec_from_mtl = new DatagramPacket(buffer,buffer.length);
		            asocket.receive(Rec_from_mtl);
		            String temp = new String(Rec_from_mtl.getData());
		            System.out.println(temp);
		            s=s+"\n"+temp;
		            //********************TOR*************************
		            DatagramPacket req_to_tor = new DatagramPacket(reply1,reply1.length,aHost,4443);
		            asocket.send(req_to_tor);
		            System.out.println("Request sent to TOR server for listing event.");
		            
		            byte[] buffer1 = new byte[1000];
		            DatagramPacket Rec_from_tor = new DatagramPacket(buffer1,buffer1.length);
		            asocket.receive(Rec_from_tor);
		            String temp1 = new String(Rec_from_tor.getData());
		            System.out.println(temp1);
		            s=s+"\n"+temp1;
				}catch(Exception e) {}
				finally {
					if (asocket != null){
						asocket.close();
		            }
				}
				
				return s;
	}

	@Override
	public String getBookingSchedule(String customerID) {
		// TODO Auto-generated method stub
		String s = "EVENTS BOOKED : ";
		if(OTW_Conferences.containsKey(customerID)) {
			s = s+"\nConferences: "+OTW_Conferences.get(customerID);
		}
		if(OTW_Seminars.containsKey(customerID)) {
			s = s+"\nSeminars: "+OTW_Seminars.get(customerID);
		}
		if(OTW_TradeShows.containsKey(customerID)) {
			s = s+"\nTradeShows: "+OTW_TradeShows.get(customerID);
		}
		return s;
	}

	@Override
	public synchronized String cancelEvent(String customerID, String eventID, String eventType) {
		// TODO Auto-generated method stub
		try {
			if(eventID.matches("OTW.......")) {
				if(OTWEvents.get(eventType).containsKey(eventID)) {
					if(eventType.equalsIgnoreCase("C")) {
						if(OTW_Conferences.containsKey(customerID)) {
							if(OTW_Conferences.get(customerID).contains(eventID)) {
								OTW_Conferences.get(customerID).remove(eventID);
								OTWEvents.get("C").put(eventID, OTWEvents.get("C").get(eventID)+1);
								System.out.println(OTWEvents);
								System.out.println(OTW_Conferences);
								log.log(Level.INFO, "Event {0} of Conferences has been canceled for {1}", new Object[]{eventID, customerID});
								return "EVENT HAS BEEN CANCELED.";
							}else {
								return "CUSTOMER NOT REGISTERED FOR THIS EVENT.";
							}
						}else {
							return "CUSTOMER NOT REGISTERED FOR THIS EVENT.";
						}	
					}else if(eventType.equalsIgnoreCase("S")) {
						if(OTW_Seminars.containsKey(customerID)) {
							if(OTW_Seminars.get(customerID).contains(eventID)) {
								OTW_Seminars.get(customerID).remove(eventID);
								OTWEvents.get("S").put(eventID, OTWEvents.get("S").get(eventID)+1);
								System.out.println(OTWEvents);
								System.out.println(OTW_Seminars);
								log.log(Level.INFO, "Event {0} of Seminars has been canceled for {1}", new Object[]{eventID, customerID});
								return "EVENT HAS BEEN CANCELED.";
							}else {
								return "CUSTOMER NOT REGISTERED FOR THIS EVENT.";
							}
						}else {
							return "CUSTOMER NOT REGISTERED FOR THIS EVENT.";
						}
					}else if(eventType.equalsIgnoreCase("T")) {
						if(OTW_TradeShows.containsKey(customerID)) {
							if(OTW_TradeShows.get(customerID).contains(eventID)) {
								OTW_TradeShows.get(customerID).remove(eventID);
								OTWEvents.get("T").put(eventID, OTWEvents.get("T").get(eventID)+1);
								System.out.println(OTWEvents);
								System.out.println(OTW_TradeShows);
								log.log(Level.INFO, "Event {0} of TradeShows has been canceled for {1}", new Object[]{eventID, customerID});
								return "EVENT HAS BEEN CANCELED.";
							}else {
								return "CUSTOMER NOT REGISTERED FOR THIS EVENT.";
							}
						}else {
							return "CUSTOMER NOT REGISTERED FOR THIS EVENT.";
						}
					}else {
						return "ENTER THE VALID EVENTTYPE.";
					}
				}else {
					return "EVENT DOES NOT EXIST.";
				}
			}else if(eventID.matches("MTL.......")||eventID.matches("TOR.......")){
				DatagramSocket asocket = new DatagramSocket();
	            InetAddress aHost = InetAddress.getByName("localhost");
	            String s="CANCELEVENT"+" "+customerID+" "+eventID+" "+eventType;
	            byte[] reply = s.getBytes();
	            if(eventID.matches("MTL.......")) {
	            	DatagramPacket req = new DatagramPacket(reply,reply.length,aHost,2223);
	            	asocket.send(req);
	                System.out.println("Request sent to MTL server for canceling event.");
	            }else if(eventID.matches("TOR.......")) {
	            	DatagramPacket req = new DatagramPacket(reply,reply.length,aHost,4443);
	            	asocket.send(req);
	                System.out.println("Request sent to TOR server for canceling event.");
	            }
	            
	            byte[] buffer = new byte[1000];
	            DatagramPacket Rec = new DatagramPacket(buffer,buffer.length);
	            asocket.receive(Rec);
	            String temp = new String(Rec.getData());
	            System.out.println(temp);
	            
	            int m = Integer.parseInt(eventID.substring(6, 8));
				int i = Counter.get(customerID).get(m-1);
	            
	            if("yes".equalsIgnoreCase(temp.trim().toString())) {
	            	if(eventType.equalsIgnoreCase("C")) {
	            		OTW_Conferences.get(customerID).remove(eventID);
	            		Counter.get(customerID).set(m-1, Counter.get(customerID).get(m-1)-1);
	                	System.out.println(Counter);
						System.out.println(OTW_Conferences);
						log.log(Level.INFO, "Event {0} of Conferences has been canceled for {1}", new Object[]{eventID, customerID});
						return "EVENT HAS BEEN CANCELED.";
					}else if(eventType.equalsIgnoreCase("S")) {
						OTW_Seminars.get(customerID).remove(eventID);
						Counter.get(customerID).set(m-1, Counter.get(customerID).get(m-1)-1);
		            	System.out.println(Counter);
		            	System.out.println(OTW_Seminars);
		            	log.log(Level.INFO, "Event {0} of Seminars has been canceled for {1}", new Object[]{eventID, customerID});
		            	return "EVENT HAS BEEN CANCELED.";
					}else if(eventType.equalsIgnoreCase("T")) {
						OTW_TradeShows.get(customerID).remove(eventID);
						Counter.get(customerID).set(m-1, Counter.get(customerID).get(m-1)-1);
		            	System.out.println(Counter);
		            	System.out.println(OTW_TradeShows);
		            	log.log(Level.INFO, "Event {0} of TradeShows has been canceled for {1}", new Object[]{eventID, customerID});
		            	return "EVENT HAS BEEN CANCELED.";
					}
	            }else {
	            	return temp;
	            }
	            
			}else {
				return "ENTER THE VALID EVENTID.";
			}
		}catch(Exception e) {}
		return null;
	}
}
