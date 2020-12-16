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

public class MTL extends UnicastRemoteObject implements Server{

	HashMap<String, HashMap<String, Integer>> MTLEvents = null;
	HashMap<String, Integer> Conferences = null;
	HashMap<String, Integer> Seminars = null;
	HashMap<String, Integer> TradeShows = null;
	ArrayList<String> manager = null;
	
	HashMap<String,ArrayList<String>> MTL_Conferences = null;
	HashMap<String,ArrayList<String>> MTL_Seminars = null;
	HashMap<String,ArrayList<String>> MTL_TradeShows = null;
	
	HashMap<String,ArrayList<Integer>> Counter = null;
	
	Logger log = null;
	FileHandler file;
	
	MTL() throws RemoteException{
		
		try {
			log=Logger.getLogger("MTL");
            file=new FileHandler("D:/Concordia/COMP6231/log/MTL.log",true);
            log.addHandler(file);
            log.setUseParentHandlers(false);
            SimpleFormatter sf=new SimpleFormatter();
            file.setFormatter(sf);
		}catch(Exception e) {}
		
		MTLEvents = new HashMap<String, HashMap<String, Integer>>();
		
		Counter = new HashMap<>();
		Conferences = new HashMap<String, Integer>();
		
		Seminars = new HashMap<String, Integer>();
		
		TradeShows = new HashMap<String, Integer>();
		
		MTLEvents.put("C", Conferences);
		MTLEvents.put("S", Seminars);
		MTLEvents.put("T", TradeShows);
		
		manager = new ArrayList<String>();
		
		manager.add("MTLM1000");
		manager.add("MTLM2000");
		manager.add("MTLM3000");
		manager.add("MTLM9087");
		
		MTL_Conferences = new HashMap<>();
		
		MTL_Seminars = new HashMap<>();
		
		ArrayList<String> client2 = new ArrayList<String>();
		client2.add("MTLA210520");
		client2.add("MTLA250520");
		MTL_Seminars.put("MTLC1000", client2);
		
		MTL_TradeShows = new HashMap<>();
		
		Counter.putIfAbsent("MTLC1000", new ArrayList<>(Arrays.asList(0,0,0,0,2,0,0,0,0,0,0,0)));
			
	}
	
	boolean alreadyBooked(String customerID, String eventID, String eventType){
		if(eventType.matches("C")) {
			if(MTL_Conferences.containsKey(customerID)) {
				if(MTL_Conferences.get(customerID).contains(eventID)) {
					return true;
				}
			}
		}else if(eventType.matches("S")) {
			if(MTL_Seminars.containsKey(customerID)) {
				if(MTL_Seminars.get(customerID).contains(eventID)) {
					return true;
				}
			}
		}else if(eventType.matches("T")) {
			if(MTL_TradeShows.containsKey(customerID)) {
				if(MTL_TradeShows.get(customerID).contains(eventID)) {
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
			if(eventID.matches("MTL.......")) {
				if(eventType.equalsIgnoreCase("C")) {
					if(alreadyBooked(customerID, eventID, eventType)) {
						return "Already registered for "+eventID;
					}else {
						if(MTLEvents.get(eventType).containsKey(eventID)) {
							int availability = MTLEvents.get(eventType).get(eventID);
							if(availability!=0) {
								MTLEvents.get(eventType).put(eventID, MTLEvents.get(eventType).get(eventID)-1);
								System.out.println(MTLEvents);
								log.log(Level.INFO, "{0} booked {1} of Conferences", new Object[]{customerID, eventID});
								return "Yes";
							}else {
								return eventID+" is full.";
							}
						}else {
							return eventID+" is not available.";
						}
					}
				}else if(eventType.equalsIgnoreCase("S")) {
					if(alreadyBooked(customerID, eventID, eventType)) {
						return "Already registered for "+eventID;
					}else {
						if(MTLEvents.get(eventType).containsKey(eventID)) {
							int availability = MTLEvents.get(eventType).get(eventID);
							if(availability!=0) {
								MTLEvents.get(eventType).put(eventID, MTLEvents.get(eventType).get(eventID)-1);
								System.out.println(MTLEvents);
								log.log(Level.INFO, "{0} booked {1} of Seminars", new Object[]{customerID, eventID});
								return "Yes";
							}else {
								return eventID+" is full.";
							}
						}else {
							return eventID+" is not available.";
						}
					}
				}else if(eventType.equalsIgnoreCase("T")) {
					if(alreadyBooked(customerID, eventID, eventType)) {
						return "Already registered for "+eventID;
					}else {
						if(MTLEvents.get(eventType).containsKey(eventID)) {
							int availability = MTLEvents.get(eventType).get(eventID);
							if(availability!=0) {
								MTLEvents.get(eventType).put(eventID, MTLEvents.get(eventType).get(eventID)-1);
								System.out.println(MTLEvents);
								log.log(Level.INFO, "{0} booked {1} of TradeShows", new Object[]{customerID, eventID});
								return "Yes";
							}else {
								return eventID+" is full.";
							}
						}else {
							return eventID+" is not available.";
						}
					}
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
		try {
			if(eventType.equalsIgnoreCase("C")) {
				MTLEvents.get("C").put(eventID, MTLEvents.get("C").get(eventID)+1);
				System.out.println(MTLEvents);
				log.log(Level.INFO, "{0} canceled {1} of Conferences", new Object[]{customerID, eventID});
				return "yes";
			}else if(eventType.equalsIgnoreCase("S")) {
				MTLEvents.get("S").put(eventID, MTLEvents.get("S").get(eventID)+1);
				System.out.println(MTLEvents);
				log.log(Level.INFO, "{0} canceled {1} of Seminars", new Object[]{customerID, eventID});
				return "yes";
			}else if(eventType.equalsIgnoreCase("T")) {
				MTLEvents.get("T").put(eventID, MTLEvents.get("T").get(eventID)+1);
				System.out.println(MTLEvents);
				log.log(Level.INFO, "{0} canceled {1} of TradeShows", new Object[]{customerID, eventID});
				return "yes";
			}else {
				return eventID+" does not exist.";
			}
		}catch(Exception e) {}
		finally {
			//log
		}
		return null;
	}
	
	synchronized String removeOuterEvent(String eventID, String eventType){
		eventID=eventID.trim();
		eventType=eventType.trim();
		int m = Integer.parseInt(eventID.substring(6, 8));
		try {
			if(eventType.equalsIgnoreCase("C")) {
				for(HashMap.Entry<String,ArrayList<String>> pair: MTL_Conferences.entrySet()) {
					if(pair.getValue().contains(eventID)) {
						MTL_Conferences.get(pair.getKey()).remove(eventID);
						Counter.get(pair.getKey()).set(m-1, Counter.get(pair.getKey()).get(m-1)-1);
						System.out.println("MTL_Conferences : "+MTL_Conferences);
						System.out.println("Counter : "+Counter);
					}
				}
				log.log(Level.INFO, "{0} of Conferences has been removed for all Montreal customers", new Object[]{eventID});
				return "Removed "+eventID+" for Montreal customers";
			}else if(eventType.equalsIgnoreCase("S")) {
				for(HashMap.Entry<String,ArrayList<String>> pair: MTL_Seminars.entrySet()) {
					if(pair.getValue().contains(eventID)) {
						MTL_Seminars.get(pair.getKey()).remove(eventID);
						Counter.get(pair.getKey()).set(m-1, Counter.get(pair.getKey()).get(m-1)-1);
						System.out.println("MTL_Seminars : "+MTL_Seminars);
						System.out.println("Counter : "+Counter);
					}
				}
				log.log(Level.INFO, "{0} of Seminars has been removed for all Montreal customers", new Object[]{eventID});
				return "Removed "+eventID+" for Montreal customers";
			}else if(eventType.equalsIgnoreCase("T")) {
				for(HashMap.Entry<String,ArrayList<String>> pair: MTL_TradeShows.entrySet()) {
					if(pair.getValue().contains(eventID)) {
						MTL_TradeShows.get(pair.getKey()).remove(eventID);
						Counter.get(pair.getKey()).set(m-1, Counter.get(pair.getKey()).get(m-1)-1);
						System.out.println("MTL_TradeShows : "+MTL_TradeShows);
						System.out.println("Counter : "+Counter);
					}
				}
				log.log(Level.INFO, "{0} of TradeShows has been removed for all Montreal customers", new Object[]{eventID});
				return "Removed "+eventID+" for Montreal customers";
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
		s=s+"\nMontreal : ";
		if(eventType.equalsIgnoreCase("C")) {
			s=s+MTLEvents.get("C").toString();
		}else if(eventType.equalsIgnoreCase("S")) {
			s=s+MTLEvents.get("S").toString();
		}else if(eventType.equalsIgnoreCase("T")) {
			s=s+MTLEvents.get("T").toString();
		}
		return s;
	}
	
	public static void main(String args[])
	{
		try {
			
			MTL o_mtl = new MTL();
			Registry reg = LocateRegistry.createRegistry(6666);
			reg.bind("dg_mtl", o_mtl);
			System.out.println("Montreal server is Running.");
			Runnable task = () -> {
				DatagramSocket aSocket = null;
				while(true) {
					try {
						aSocket = new DatagramSocket(2223);
						byte[] b1 = new byte[10000];
						DatagramPacket Reply = new DatagramPacket(b1,b1.length);
						aSocket.receive(Reply);
						String str = new String(Reply.getData());
                        String[] splited = str.split("\\s+");
                        if(splited[0].equalsIgnoreCase("BOOKEVENT")) {
                        	String ret = o_mtl.bookOuterEvent(splited[1],splited[2],splited[3]);
                            byte[] temp = new byte[100000];
                            temp = ret.getBytes();
                            DatagramPacket se=new DatagramPacket(temp,temp.length,Reply.getAddress(),Reply.getPort());
                            aSocket.send(se);
                        }else if(splited[0].equalsIgnoreCase("CANCELEVENT")) {
                        	String ret = o_mtl.cancelOuterEvent(splited[1],splited[2],splited[3]);
                            byte[] temp = new byte[100000];
                            temp = ret.getBytes();
                            DatagramPacket se=new DatagramPacket(temp,temp.length,Reply.getAddress(),Reply.getPort());
                            aSocket.send(se);
                        }else if(splited[0].equalsIgnoreCase("REMOVEEVENT")) {
                        	String ret = o_mtl.removeOuterEvent(splited[1],splited[2]);
                            byte[] temp = new byte[100000];
                            temp = ret.getBytes();
                            DatagramPacket se=new DatagramPacket(temp,temp.length,Reply.getAddress(),Reply.getPort());
                            aSocket.send(se);
                        }else if(splited[0].equalsIgnoreCase("LIST")) {
                        	String ret = o_mtl.listOuterEvent(splited[1]);
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
							aSocket = new DatagramSocket(2224);
							byte[] b1 = new byte[10000];
							DatagramPacket Reply = new DatagramPacket(b1,b1.length);
							aSocket.receive(Reply);
							String str = new String(Reply.getData());
	                        String[] splited = str.split("\\s+");
	                        if(splited[0].equalsIgnoreCase("BOOKEVENT")) {
	                        	String ret = o_mtl.bookOuterEvent(splited[1],splited[2],splited[3]);
	                            byte[] temp = new byte[100000];
	                            temp = ret.getBytes();
	                            DatagramPacket se=new DatagramPacket(temp,temp.length,Reply.getAddress(),Reply.getPort());
	                            aSocket.send(se);
	                        }else if(splited[0].equalsIgnoreCase("CANCELEVENT")) {
	                        	String ret = o_mtl.cancelOuterEvent(splited[1],splited[2],splited[3]);
	                            byte[] temp = new byte[100000];
	                            temp = ret.getBytes();
	                            DatagramPacket se=new DatagramPacket(temp,temp.length,Reply.getAddress(),Reply.getPort());
	                            aSocket.send(se);
	                        }else if(splited[0].equalsIgnoreCase("REMOVEEVENT")) {
	                        	String ret = o_mtl.removeOuterEvent(splited[1],splited[2]);
	                            byte[] temp = new byte[100000];
	                            temp = ret.getBytes();
	                            DatagramPacket se=new DatagramPacket(temp,temp.length,Reply.getAddress(),Reply.getPort());
	                            aSocket.send(se);
	                        }else if(splited[0].equalsIgnoreCase("LIST")) {
	                        	String ret = o_mtl.listOuterEvent(splited[1]);
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
	           
	           
		}catch(Exception e) {}
	}
	
	@Override
	public synchronized String addEvent(String eventID, String eventType, int bookingCapacity) throws RemoteException {
		// TODO Auto-generated method stub
		String reply=null;
		HashMap<String, Integer> data = new HashMap<String, Integer>();
		try {
			if(eventID.substring(0, 3).equalsIgnoreCase("MTL")) {
				if(eventType.equals("C")) {
					if(MTLEvents.get("C")!=null) {
						data = MTLEvents.get("C");
					}
					data.put(eventID, bookingCapacity);
					MTLEvents.put("C", data);
					System.out.println(MTLEvents);
					log.log(Level.INFO, "Event {0} added in {1}", new Object[]{eventID, eventType});
					return eventID+" added";
				}else if(eventType.equals("S")) {
					if(MTLEvents.get("S")!=null) {
						data = MTLEvents.get("S");
					}
					data.put(eventID, bookingCapacity);
					MTLEvents.put("S", data);
					System.out.println(MTLEvents);
					log.log(Level.INFO, "Event {0} added in {1}", new Object[]{eventID, eventType});
					return eventID+" added";
				}else if(eventType.equals("T")) {
					if(MTLEvents.get("T")!=null) {
						data = MTLEvents.get("T");
					}
					data.put(eventID, bookingCapacity);
					MTLEvents.put("T", data);
					System.out.println(MTLEvents);
					log.log(Level.INFO, "Event {0} added in {1}", new Object[]{eventID, eventType});
					return eventID+" added";
				}
			}else {
				return "Not authorised";
			}
		}catch(Exception e) {}
		finally {
			//log
		}
		return reply;
	}

	@Override
	public String validUser(String currentUser) throws RemoteException {
		// TODO Auto-generated method stub
		if(manager.contains(currentUser)) {
			return "Y";
		}else {
			return "N";
		}
	}

	@Override
	public synchronized String bookEvent(String customerID, String eventID, String eventType) throws IOException {
		// TODO Auto-generated method stub
		eventType=eventType.trim();
		try {
			if(customerID.matches("MTLC....")) {
				if(eventID.matches("MTL.......")) {
					if(eventType.equalsIgnoreCase("C")) {
						if(alreadyBooked(customerID, eventID, eventType)) {
							return "Already registered "+eventID;
						}else {
							if(MTLEvents.get(eventType).containsKey(eventID)) {
								int availability = MTLEvents.get(eventType).get(eventID);
								if(availability!=0) {
									Counter.putIfAbsent(customerID, new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0)));
									MTL_Conferences.putIfAbsent(customerID, new ArrayList<>());
									MTL_Conferences.get(customerID).add(eventID);
									MTLEvents.get(eventType).put(eventID, MTLEvents.get(eventType).get(eventID)-1);
									System.out.println(MTLEvents);
									System.out.println(MTL_Conferences);
									log.log(Level.INFO, "Customer {0} booked {1} of {2}", new Object[]{customerID, eventID, eventType});
									return eventID+" booked";
								}else {
									return eventID+" is full";
								}
							}else {
								return eventID+" not available";
							}
						}
					}else if(eventType.equalsIgnoreCase("S")) {
						if(alreadyBooked(customerID, eventID, eventType)) {
							return "Already registered "+eventID;
						}else {
							if(MTLEvents.get(eventType).containsKey(eventID)) {
								int availability = MTLEvents.get(eventType).get(eventID);
								if(availability!=0) {
									Counter.putIfAbsent(customerID, new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0)));
									MTL_Seminars.putIfAbsent(customerID, new ArrayList<>());
									MTL_Seminars.get(customerID).add(eventID);
									MTLEvents.get(eventType).put(eventID, MTLEvents.get(eventType).get(eventID)-1);
									System.out.println(MTLEvents);
									System.out.println(MTL_Seminars);
									log.log(Level.INFO, "Customer {0} booked {1} of {2}", new Object[]{customerID, eventID, eventType});
									return eventID+" booked";
								}else {
									return eventID+" is full";
								}
							}else {
								return eventID+" not available";
							}
						}
					}else if(eventType.equalsIgnoreCase("T")) {
						if(alreadyBooked(customerID, eventID, eventType)) {
							return "Already registered "+eventID;
						}else {
							if(MTLEvents.get(eventType).containsKey(eventID)) {
								int availability = MTLEvents.get(eventType).get(eventID);
								if(availability!=0) {
									Counter.putIfAbsent(customerID, new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0)));
									MTL_TradeShows.putIfAbsent(customerID, new ArrayList<>());
									MTL_TradeShows.get(customerID).add(eventID);
									MTLEvents.get(eventType).put(eventID, MTLEvents.get(eventType).get(eventID)-1);
									System.out.println(MTLEvents);
									System.out.println(MTL_TradeShows);
									log.log(Level.INFO, "Customer {0} booked {1} of {2}", new Object[]{customerID, eventID, eventType});
									return eventID+" booked";
								}else {
									return eventID+" is full";
								}
							}else {
								return eventID+" not available";
							}
						}
					}
				}else if(eventID.matches("OTW.......")) {
					
					DatagramSocket asocket = new DatagramSocket();
	                InetAddress aHost = InetAddress.getByName("localhost");
					
					Counter.putIfAbsent(customerID, new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0)));
					int m = Integer.parseInt(eventID.substring(6, 8));
					int i = Counter.get(customerID).get(m-1);
					System.out.println(Counter);
					if(i<3) {
						String s = "BOOKEVENT"+" "+customerID+" "+eventID+" "+eventType;
	                    byte[] reply = s.getBytes();
	                    DatagramPacket req = new DatagramPacket(reply,reply.length,aHost,3332);
	                    asocket.send(req);
	                    System.out.println("Request sent to OTW server for booking event.");
	                    
	                    byte[] buffer = new byte[1000];
	                    DatagramPacket Rec = new DatagramPacket(buffer,buffer.length);
	                    asocket.receive(Rec);
	                    String temp = new String(Rec.getData());
	                    System.out.println(temp);
	                    if("yes".equalsIgnoreCase(temp.trim().toString())) {
	                    	Counter.putIfAbsent(customerID, new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0)));
	                    	if(eventType.equalsIgnoreCase("C")) {
	                    		MTL_Conferences.putIfAbsent(customerID, new ArrayList<>());
	                    		MTL_Conferences.get(customerID).add(eventID);
								Counter.get(customerID).set(m-1, Counter.get(customerID).get(m-1)+1);
								System.out.println(Counter);
								System.out.println(MTL_Conferences);
								log.log(Level.INFO, "Customer {0} booked {1} of {2}", new Object[]{customerID, eventID, eventType});
								return eventID+" booked";
	        				}else if(eventType.equalsIgnoreCase("S")) {
	        					MTL_Seminars.putIfAbsent(customerID, new ArrayList<>());
	        					MTL_Seminars.get(customerID).add(eventID);
								Counter.get(customerID).set(m-1, Counter.get(customerID).get(m-1)+1);
								System.out.println(Counter);
								System.out.println(MTL_Seminars);
								log.log(Level.INFO, "Customer {0} booked {1} of {2}", new Object[]{customerID, eventID, eventType});
								return eventID+" booked";
	        				}else if(eventType.equalsIgnoreCase("T")) {
								MTL_TradeShows.putIfAbsent(customerID, new ArrayList<>());
								MTL_TradeShows.get(customerID).add(eventID);
								Counter.get(customerID).set(m-1, Counter.get(customerID).get(m-1)+1);
								System.out.println(Counter);
								System.out.println(MTL_TradeShows);
								log.log(Level.INFO, "Customer {0} booked {1} of {2}", new Object[]{customerID, eventID, eventType});
								return eventID+" booked";
	        				}
	                    }else {
	                    	return "Failed to book event.";
	                    }
					}else {
						return "Reached limit of booking events in other cities.";
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
	                    DatagramPacket req = new DatagramPacket(reply,reply.length,aHost,4442);
	                    asocket.send(req);
	                    System.out.println("Request sent to TOR server for booking event.");
	                    
	                    byte[] buffer = new byte[1000];
	                    DatagramPacket Rec = new DatagramPacket(buffer,buffer.length);
	                    asocket.receive(Rec);
	                    String temp = new String(Rec.getData());
	                    System.out.println(temp);
	                    if("yes".equalsIgnoreCase(temp.trim().toString())) {
	                    	Counter.putIfAbsent(customerID, new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0)));
	                    	if(eventType.equalsIgnoreCase("C")) {
	                    		MTL_Conferences.putIfAbsent(customerID, new ArrayList<>());
	                    		MTL_Conferences.get(customerID).add(eventID);
								Counter.get(customerID).set(m-1, Counter.get(customerID).get(m-1)+1);
								System.out.println(Counter);
								System.out.println(MTL_Conferences);
								log.log(Level.INFO, "Customer {0} booked {1} of {2}", new Object[]{customerID, eventID, eventType});
								return eventID+" booked";
	        				}else if(eventType.equalsIgnoreCase("S")) {
	        					MTL_Seminars.putIfAbsent(customerID, new ArrayList<>());
	        					MTL_Seminars.get(customerID).add(eventID);
								Counter.get(customerID).set(m-1, Counter.get(customerID).get(m-1)+1);
								System.out.println(Counter);
								System.out.println(MTL_Seminars);
								log.log(Level.INFO, "Customer {0} booked {1} of {2}", new Object[]{customerID, eventID, eventType});
								return eventID+" booked";
	        				}else if(eventType.equalsIgnoreCase("T")) {
								MTL_TradeShows.putIfAbsent(customerID, new ArrayList<>());
								MTL_TradeShows.get(customerID).add(eventID);
								Counter.get(customerID).set(m-1, Counter.get(customerID).get(m-1)+1);
								System.out.println(Counter);
								System.out.println(MTL_TradeShows);
								log.log(Level.INFO, "Customer {0} booked {1} of {2}", new Object[]{customerID, eventID, eventType});
								return eventID+" booked";
	        				}
	                    }else {
	                    	return "Failed to book event.";
	                    }
					}else {
						return "Reached limit of booking events in other cities.";
					}
				}
			}else {
				return "Only Montreal Customers are allowed.";
			}
		}catch(Exception e) {}
		finally {
			//log
		}
		return null;
	}

	@Override
	public synchronized String removeEvent(String eventID, String eventType) throws RemoteException {
		// TODO Auto-generated method stub
		eventType=eventType.trim();
		eventID=eventID.trim();
		String reply=null;
		try {
			if(eventID.substring(0, 3).equalsIgnoreCase("MTL")) {
				if(MTLEvents.get(eventType).containsKey(eventID)) {
					DatagramSocket asocket = null;
					try {
						asocket = new DatagramSocket();
		                InetAddress aHost = InetAddress.getByName("localhost");
		                
		                String s="REMOVEEVENT"+" "+eventID+" "+eventType;
	                    byte[] reply1 = s.getBytes();
	                    //********************OTW*************************
	                    DatagramPacket req_to_otw = new DatagramPacket(reply1,reply1.length,aHost,3332);
	                    asocket.send(req_to_otw);
	                    System.out.println("Request sent to OTW server for removing event.");
	                    
	                    byte[] buffer = new byte[1000];
	                    DatagramPacket Rec_from_otw = new DatagramPacket(buffer,buffer.length);
	                    asocket.receive(Rec_from_otw);
	                    String temp = new String(Rec_from_otw.getData());
	                    System.out.println(temp);
						//**********************TOR*******************
	                    DatagramPacket req_to_tor = new DatagramPacket(reply1,reply1.length,aHost,4442);
	                    asocket.send(req_to_tor);
	                    System.out.println("Request sent to TOR server for removing event.");
	                    
	                    byte[] buffer1 = new byte[1000];
	                    DatagramPacket Rec_from_tor = new DatagramPacket(buffer,buffer.length);
	                    asocket.receive(Rec_from_tor);
	                    String temp1 = new String(Rec_from_tor.getData());
	                    System.out.println(temp1);
					}
					catch(Exception e) {}
					finally {
						if (asocket != null)
                        {
                             	asocket.close();
                        }
					}
					MTLEvents.get(eventType).remove(eventID);
					System.out.println("MTLEvents : "+MTLEvents);
					if(eventType.equals("C")) {
						for(HashMap.Entry<String,ArrayList<String>> pair: MTL_Conferences.entrySet()) {
							System.out.println(pair.getValue());
							if(pair.getValue().contains(eventID)) {
								MTL_Conferences.get(pair.getKey()).remove(eventID);
								System.out.println("MTL_Conferences : "+MTL_Conferences);
							}
						}
						log.log(Level.INFO, "Event {0} of Conferences has been removed", new Object[]{eventID});
						return eventID+" Removed.";
					}else if(eventType.equals("S")) {
						for(HashMap.Entry<String,ArrayList<String>> pair: MTL_Seminars.entrySet()) {
							System.out.println(pair.getValue());
							if(pair.getValue().contains(eventID)) {
								MTL_Seminars.get(pair.getKey()).remove(eventID);
								System.out.println("MTL_Seminars : "+MTL_Seminars);
							}
						}
						log.log(Level.INFO, "Event {0} of Seminars has been removed", new Object[]{eventID});
						return eventID+" Removed.";
					}else if(eventType.equals("T")) {
						for(HashMap.Entry<String,ArrayList<String>> pair: MTL_TradeShows.entrySet()) {
							System.out.println(pair.getValue());
							if(pair.getValue().contains(eventID)) {
								MTL_TradeShows.get(pair.getKey()).remove(eventID);
								System.out.println("MTL_TradeShows : "+MTL_TradeShows);
							}
						}
						log.log(Level.INFO, "Event {0} of TradeShows has been removed", new Object[]{eventID});
						return eventID+" Removed.";
					}
				}else {
					return eventID+" does not exist.";
				}
			}else {
				return "Not authorised";
			}
		}catch(Exception e) {}
		finally {
			//log
		}
		return null;
	}

	@Override
	public String listEventAvailability(String eventType) throws RemoteException {
		// TODO Auto-generated method stub
		String s = listOuterEvent(eventType);
		DatagramSocket asocket = null;
		try {
			asocket = new DatagramSocket();
            InetAddress aHost = InetAddress.getByName("localhost");
            
            String str="LIST"+" "+eventType;
            byte[] reply1 = str.getBytes();
            //********************OTW*************************
            DatagramPacket req_to_otw = new DatagramPacket(reply1,reply1.length,aHost,3332);
            asocket.send(req_to_otw);
            System.out.println("Request sent to OTW server for listing event.");
            
            byte[] buffer = new byte[1000];
            DatagramPacket Rec_from_otw = new DatagramPacket(buffer,buffer.length);
            asocket.receive(Rec_from_otw);
            String temp = new String(Rec_from_otw.getData());
            System.out.println(temp);
            s=s+temp;
            //********************TOR*************************
            DatagramPacket req_to_tor = new DatagramPacket(reply1,reply1.length,aHost,4442);
            asocket.send(req_to_tor);
            System.out.println("Request sent to TOR server for listing event.");
            
            byte[] buffer1 = new byte[1000];
            DatagramPacket Rec_from_tor = new DatagramPacket(buffer,buffer.length);
            asocket.receive(Rec_from_tor);
            String temp1 = new String(Rec_from_tor.getData());
            System.out.println(temp1);
            s=s+temp1;
		}catch(Exception e) {}
		finally {
			if (asocket != null){
				asocket.close();
            }
		}
		
		return s;
	}

	@Override
	public String getBookingSchedule(String customerID) throws IOException {
		// TODO Auto-generated method stub
		//City:Event type:Event ID
		String s = "";
		if(MTL_Conferences.containsKey(customerID)) {
			s = s+"Conferences: "+MTL_Conferences.get(customerID);
		}
		if(MTL_Seminars.containsKey(customerID)) {
			s = s+"\nSeminars: "+MTL_Seminars.get(customerID);
		}
		if(MTL_TradeShows.containsKey(customerID)) {
			s = s+"\nTradeShows: "+MTL_TradeShows.get(customerID);
		}
		return s;
	}

	@Override
	public synchronized String cancelEvent(String customerID, String eventID, String eventType) throws IOException {
		// TODO Auto-generated method stub
		try {
			if(eventID.matches("MTL.......")) {
				if(eventType.equalsIgnoreCase("C")) {
					if(MTL_Conferences.containsKey(customerID)) {
						if(MTL_Conferences.get(customerID).contains(eventID)) {
							MTL_Conferences.get(customerID).remove(eventID);
							MTLEvents.get("C").put(eventID, MTLEvents.get("C").get(eventID)+1);
							System.out.println(MTLEvents);
							System.out.println(MTL_Conferences);
							log.log(Level.INFO, "Event {0} of Conferences has been canceled for {1}", new Object[]{eventID, customerID});
							return eventID+" canceled for "+customerID;
						}else {
							return customerID+" not registered for "+eventID;
						}
					}else {
						return "No record for "+customerID;
					}
				}else if(eventType.equalsIgnoreCase("S")) {
					if(MTL_Seminars.containsKey(customerID)) {
						if(MTL_Seminars.get(customerID).contains(eventID)) {
							MTL_Seminars.get(customerID).remove(eventID);
							MTLEvents.get("S").put(eventID, MTLEvents.get("S").get(eventID)+1);
							System.out.println(MTLEvents);
							System.out.println(MTL_Seminars);
							log.log(Level.INFO, "Event {0} of Seminars has been canceled for {1}", new Object[]{eventID, customerID});
							return eventID+" canceled for "+customerID;
						}else {
							return customerID+" not registered for "+eventID;
						}
					}else {
						return "No record for "+customerID;
					}
				}else if(eventType.equalsIgnoreCase("T")) {
					if(MTL_TradeShows.containsKey(customerID)) {
						if(MTL_TradeShows.get(customerID).contains(eventID)) {
							MTL_TradeShows.get(customerID).remove(eventID);
							MTLEvents.get("T").put(eventID, MTLEvents.get("T").get(eventID)+1);
							System.out.println(MTLEvents);
							System.out.println(MTL_TradeShows);
							log.log(Level.INFO, "Event {0} of TradeShows has been canceled for {1}", new Object[]{eventID, customerID});
							return eventID+" canceled for "+customerID;
						}else {
							return customerID+" not registered for "+eventID;
						}
					}else {
						return "No record for "+customerID;
					}
				}else {
					return eventID+" does not exist";
				}
			}else {
				DatagramSocket asocket = new DatagramSocket();
	            InetAddress aHost = InetAddress.getByName("localhost");
	            String s="CANCELEVENT"+" "+customerID+" "+eventID+" "+eventType;
	            byte[] reply = s.getBytes();
	            if(eventID.matches("OTW.......")) {
	            	DatagramPacket req = new DatagramPacket(reply,reply.length,aHost,3332);
	            	asocket.send(req);
	                System.out.println("Request sent to OTW server for canceling event.");
	            }else if(eventID.matches("TOR.......")) {
	            	DatagramPacket req = new DatagramPacket(reply,reply.length,aHost,4442);
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
	            		MTL_Conferences.get(customerID).remove(eventID);
	            		Counter.get(customerID).set(m-1, Counter.get(customerID).get(m-1)-1);
	                	System.out.println(Counter);
	                	System.out.println(MTL_Conferences);
	                	log.log(Level.INFO, "Event {0} of Conferences has been canceled for {1}", new Object[]{eventID, customerID});
	                	return eventID+" canceled for "+customerID;
					}else if(eventType.equalsIgnoreCase("S")) {
						MTL_Seminars.get(customerID).remove(eventID);
						Counter.get(customerID).set(m-1, Counter.get(customerID).get(m-1)-1);
		            	System.out.println(Counter);
		            	System.out.println(MTL_Seminars);
		            	log.log(Level.INFO, "Event {0} of Seminars has been canceled for {1}", new Object[]{eventID, customerID});
		            	return eventID+" canceled for "+customerID;
					}else if(eventType.equalsIgnoreCase("T")) {
						MTL_TradeShows.get(customerID).remove(eventID);
						Counter.get(customerID).set(m-1, Counter.get(customerID).get(m-1)-1);
		            	System.out.println(Counter);
		            	System.out.println(MTL_TradeShows);
		            	log.log(Level.INFO, "Event {0} of TradeShows has been canceled for {1}", new Object[]{eventID, customerID});
		            	return eventID+" canceled for "+customerID;
					}
	            }else {
	            	return "Failed to cancel "+eventID;
	            }
	            
			}
		}catch(Exception e) {}
		finally {
			//log
		}
		return null;
	}
}
