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

public class TOR extends UnicastRemoteObject implements Server{

	HashMap<String, HashMap<String, Integer>> TOREvents = null;
	HashMap<String, Integer> Conferences = null;
	HashMap<String, Integer> Seminars = null;
	HashMap<String, Integer> TradeShows = null;
	ArrayList<String> manager = null;
	
	HashMap<String,ArrayList<String>> TOR_Conferences = null;
	HashMap<String,ArrayList<String>> TOR_Seminars = null;
	HashMap<String,ArrayList<String>> TOR_TradeShows = null;
	
	HashMap<String,ArrayList<Integer>> Counter = null;
	
	Logger log = null;
	FileHandler file;
	
	TOR() throws RemoteException{
		
		try {
			log=Logger.getLogger("TOR");
            file=new FileHandler("D:/Concordia/COMP6231/log/TOR.log",true);
            log.addHandler(file);
            log.setUseParentHandlers(false);
            SimpleFormatter sf=new SimpleFormatter();
            file.setFormatter(sf);
		}catch(Exception e) {}
		
		TOREvents = new HashMap<String, HashMap<String, Integer>>();
		/*
		TOREvents.put("C", null);
		TOREvents.put("S", null);
		TOREvents.put("T", null);
		*/
		Counter = new HashMap<>();
		Conferences = new HashMap<String, Integer>();
		/*
		Conferences.put("TORM210520", 5);
		Conferences.put("TORM220520", 5);
		Conferences.put("TORM230520", 5);
		Conferences.put("TORM240520", 5);
		Conferences.put("TORM250520", 5);
		*/
		Seminars = new HashMap<String, Integer>();
		/*
		Seminars.put("TORA210520", 5);
		Seminars.put("TORA220520", 5);
		Seminars.put("TORA230520", 5);
		Seminars.put("TORA240520", 5);
		Seminars.put("TORA250520", 5);
		*/
		TradeShows = new HashMap<String, Integer>();
		
		/*TradeShows.put("TORE210520", 5);
		TradeShows.put("TORE220520", 5);
		TradeShows.put("TORE230520", 5);
		TradeShows.put("TORE240520", 5);
		TradeShows.put("TORE250520", 5);
		*/
		TOREvents.put("C", Conferences);
		TOREvents.put("S", Seminars);
		TOREvents.put("T", TradeShows);
		
		manager = new ArrayList<String>();
		
		manager.add("TORM1110");
		manager.add("TORM1120");
		manager.add("TORM1130");
		manager.add("TORM6785");
		
		TOR_Conferences = new HashMap<>();
		
		TOR_Seminars = new HashMap<>();
		TOR_TradeShows = new HashMap<>();
	}
	
	boolean alreadyBooked(String customerID, String eventID, String eventType){
		if(eventType.matches("C")) {
			if(TOR_Conferences.containsKey(customerID)) {
				if(TOR_Conferences.get(customerID).contains(eventID)) {
					return true;
				}
			}
		}else if(eventType.matches("S")) {
			if(TOR_Seminars.containsKey(customerID)) {
				if(TOR_Seminars.get(customerID).contains(eventID)) {
					return true;
				}
			}
		}else if(eventType.matches("T")) {
			if(TOR_TradeShows.containsKey(customerID)) {
				if(TOR_TradeShows.get(customerID).contains(eventID)) {
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
			if(eventID.matches("TOR.......")) {
				if(eventType.equalsIgnoreCase("C")) {
					if(alreadyBooked(customerID, eventID, eventType)) {
						return "Already registered for "+eventID;
					}else {
						if(TOREvents.get(eventType).containsKey(eventID)) {
							int availability = TOREvents.get(eventType).get(eventID);
							if(availability!=0) {
								TOREvents.get(eventType).put(eventID, TOREvents.get(eventType).get(eventID)-1);
								System.out.println(TOREvents);
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
						if(TOREvents.get(eventType).containsKey(eventID)) {
							int availability = TOREvents.get(eventType).get(eventID);
							if(availability!=0) {
								TOREvents.get(eventType).put(eventID, TOREvents.get(eventType).get(eventID)-1);
								System.out.println(TOREvents);
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
						if(TOREvents.get(eventType).containsKey(eventID)) {
							int availability = TOREvents.get(eventType).get(eventID);
							if(availability!=0) {
								TOREvents.get(eventType).put(eventID, TOREvents.get(eventType).get(eventID)-1);
								System.out.println(TOREvents);
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
		
		return null;
	}
	
	synchronized String cancelOuterEvent(String customerID, String eventID, String eventType){
		eventID=eventID.trim();
		customerID=customerID.trim();
		eventType=eventType.trim();
		if(eventType.equalsIgnoreCase("C")) {
			TOREvents.get("C").put(eventID, TOREvents.get("C").get(eventID)+1);
			System.out.println(TOREvents);
			log.log(Level.INFO, "{0} canceled {1} of Conferences", new Object[]{customerID, eventID});
			return "yes";
		}else if(eventType.equalsIgnoreCase("S")) {
			TOREvents.get("S").put(eventID, TOREvents.get("S").get(eventID)+1);
			System.out.println(TOREvents);
			log.log(Level.INFO, "{0} canceled {1} of Seminars", new Object[]{customerID, eventID});
			return "yes";
		}else if(eventType.equalsIgnoreCase("T")) {
			TOREvents.get("T").put(eventID, TOREvents.get("T").get(eventID)+1);
			System.out.println(TOREvents);
			log.log(Level.INFO, "{0} canceled {1} of TradeShows", new Object[]{customerID, eventID});
			return "yes";
		}else {
			return eventID+" does not exist.";
		}
	}
	
	synchronized String removeOuterEvent(String eventID, String eventType){
		eventID=eventID.trim();
		eventType=eventType.trim();
		int m = Integer.parseInt(eventID.substring(6, 8));
		if(eventType.equalsIgnoreCase("C")) {
			for(HashMap.Entry<String,ArrayList<String>> pair: TOR_Conferences.entrySet()) {
				if(pair.getValue().contains(eventID)) {
					TOR_Conferences.get(pair.getKey()).remove(eventID);
					Counter.get(pair.getKey()).set(m-1, Counter.get(pair.getKey()).get(m-1)-1);
					System.out.println("TOR_Conferences : "+TOR_Conferences);
					System.out.println("Counter : "+Counter);
				}
			}
			log.log(Level.INFO, "{0} of Conferences has been removed for all Torronto customers", new Object[]{eventID});
			return "Removed "+eventID+" for Torronto customers";
		}else if(eventType.equalsIgnoreCase("S")) {
			for(HashMap.Entry<String,ArrayList<String>> pair: TOR_Seminars.entrySet()) {
				if(pair.getValue().contains(eventID)) {
					TOR_Seminars.get(pair.getKey()).remove(eventID);
					Counter.get(pair.getKey()).set(m-1, Counter.get(pair.getKey()).get(m-1)-1);
					System.out.println("TOR_Seminars : "+TOR_Seminars);
					System.out.println("Counter : "+Counter);
				}
			}
			log.log(Level.INFO, "{0} of Seminars has been removed for all Torronto customers", new Object[]{eventID});
			return "Removed "+eventID+" for Torronto customers";
		}else if(eventType.equalsIgnoreCase("T")) {
			for(HashMap.Entry<String,ArrayList<String>> pair: TOR_TradeShows.entrySet()) {
				if(pair.getValue().contains(eventID)) {
					TOR_TradeShows.get(pair.getKey()).remove(eventID);
					Counter.get(pair.getKey()).set(m-1, Counter.get(pair.getKey()).get(m-1)-1);
					System.out.println("TOR_TradeShows : "+TOR_TradeShows);
					System.out.println("Counter : "+Counter);
				}
			}
			log.log(Level.INFO, "{0} of TradeShows has been removed for all Torronto customers", new Object[]{eventID});
			return "Removed "+eventID+" for Torronto customers";
		}
		return null;
	}
	
	String listOuterEvent(String eventType) {
		eventType=eventType.trim();
		String s = "";
		s=s+"\nTorronto : ";
		if(eventType.equalsIgnoreCase("C")) {
			s=s+TOREvents.get("C").toString();
		}else if(eventType.equalsIgnoreCase("S")) {
			s=s+TOREvents.get("S").toString();
		}else if(eventType.equalsIgnoreCase("T")) {
			s=s+TOREvents.get("T").toString();
		}
		return s;
	}
	
	public static void main(String args[]) {
		try {
			
			TOR o_tor = new TOR();
			Registry reg = LocateRegistry.createRegistry(8888);
			reg.bind("dg_tor", o_tor);
			System.out.println("Torronto server is Running.");
			Runnable task = () -> {
				DatagramSocket aSocket = null;
				while(true) {
					try {
						aSocket = new DatagramSocket(4442);
						byte[] b1 = new byte[10000];
						DatagramPacket Reply = new DatagramPacket(b1,b1.length);
						aSocket.receive(Reply);
						String str = new String(Reply.getData());
						String[] splited = str.split("\\s+");
                        if(splited[0].equalsIgnoreCase("BOOKEVENT")) {
                        	String ret = o_tor.bookOuterEvent(splited[1],splited[2],splited[3]);
                            byte[] temp = new byte[100000];
                            temp = ret.getBytes();
                            DatagramPacket se=new DatagramPacket(temp,temp.length,Reply.getAddress(),Reply.getPort());
                            aSocket.send(se);
                        }else if(splited[0].equalsIgnoreCase("CANCELEVENT")) {
                        	String ret = o_tor.cancelOuterEvent(splited[1],splited[2],splited[3]);
                            byte[] temp = new byte[100000];
                            temp = ret.getBytes();
                            DatagramPacket se=new DatagramPacket(temp,temp.length,Reply.getAddress(),Reply.getPort());
                            aSocket.send(se);
                        }else if(splited[0].equalsIgnoreCase("REMOVEEVENT")) {
                        	String ret = o_tor.removeOuterEvent(splited[1],splited[2]);
                            byte[] temp = new byte[100000];
                            temp = ret.getBytes();
                            DatagramPacket se=new DatagramPacket(temp,temp.length,Reply.getAddress(),Reply.getPort());
                            aSocket.send(se);
                        }else if(splited[0].equalsIgnoreCase("LIST")) {
                        	String ret = o_tor.listOuterEvent(splited[1]);
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
							aSocket = new DatagramSocket(4443);
							byte[] b1 = new byte[10000];
							DatagramPacket Reply = new DatagramPacket(b1,b1.length);
							aSocket.receive(Reply);
							String str = new String(Reply.getData());
							String[] splited = str.split("\\s+");
	                        if(splited[0].equalsIgnoreCase("BOOKEVENT")) {
	                        	String ret = o_tor.bookOuterEvent(splited[1],splited[2],splited[3]);
	                            byte[] temp = new byte[100000];
	                            temp = ret.getBytes();
	                            DatagramPacket se=new DatagramPacket(temp,temp.length,Reply.getAddress(),Reply.getPort());
	                            aSocket.send(se);
	                        }else if(splited[0].equalsIgnoreCase("CANCELEVENT")) {
	                        	String ret = o_tor.cancelOuterEvent(splited[1],splited[2],splited[3]);
	                            byte[] temp = new byte[100000];
	                            temp = ret.getBytes();
	                            DatagramPacket se=new DatagramPacket(temp,temp.length,Reply.getAddress(),Reply.getPort());
	                            aSocket.send(se);
	                        }else if(splited[0].equalsIgnoreCase("REMOVEEVENT")) {
	                        	String ret = o_tor.removeOuterEvent(splited[1],splited[2]);
	                            byte[] temp = new byte[100000];
	                            temp = ret.getBytes();
	                            DatagramPacket se=new DatagramPacket(temp,temp.length,Reply.getAddress(),Reply.getPort());
	                            aSocket.send(se);
	                        }else if(splited[0].equalsIgnoreCase("LIST")) {
	                        	String ret = o_tor.listOuterEvent(splited[1]);
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
		}catch(Exception e) {
			System.out.println(e);
		}
	}

	@Override
	public synchronized String addEvent(String eventID, String eventType, int bookingCapacity) throws RemoteException {
		// TODO Auto-generated method stub
		String reply=null;
		HashMap<String, Integer> data = new HashMap<String, Integer>();
		try {
			if(eventID.substring(0, 3).equalsIgnoreCase("TOR")) {
				if(eventType.equals("C")) {
					if(TOREvents.get("C")!=null) {
						data = TOREvents.get("C");
					}
					data.put(eventID, bookingCapacity);
					TOREvents.put("C", data);
					System.out.println(TOREvents);
					log.log(Level.INFO, "Event {0} added in {1}", new Object[]{eventID, eventType});
					return eventID+" added";
				}else if(eventType.equals("S")) {
					if(TOREvents.get("S")!=null) {
						data = TOREvents.get("S");
					}
					data.put(eventID, bookingCapacity);
					TOREvents.put("S", data);
					System.out.println(TOREvents);
					log.log(Level.INFO, "Event {0} added in {1}", new Object[]{eventID, eventType});
					return eventID+" added";
				}else if(eventType.equals("T")) {
					if(TOREvents.get("T")!=null) {
						data = TOREvents.get("T");
					}
					data.put(eventID, bookingCapacity);
					TOREvents.put("T", data);
					System.out.println(TOREvents);
					log.log(Level.INFO, "Event {0} added in {1}", new Object[]{eventID, eventType});
					return eventID+" added";
				}
			}else {
				return "Not authorised";
			}
		}catch(Exception e) {}
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
			if(customerID.matches("TORC....")) {
				if(eventID.matches("TOR.......")) {
					if(eventType.equalsIgnoreCase("C")) {
						if(alreadyBooked(customerID, eventID, eventType)) {
							return "Already registered "+eventID;
						}else {
							if(TOREvents.get(eventType).containsKey(eventID)) {
								int availability = TOREvents.get(eventType).get(eventID);
								if(availability!=0) {
									Counter.putIfAbsent(customerID, new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0)));
									TOR_Conferences.putIfAbsent(customerID, new ArrayList<>());
									TOR_Conferences.get(customerID).add(eventID);
									TOREvents.get(eventType).put(eventID, TOREvents.get(eventType).get(eventID)-1);
									System.out.println(TOREvents);
									System.out.println(TOR_Conferences);
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
							if(TOREvents.get(eventType).containsKey(eventID)) {
								int availability = TOREvents.get(eventType).get(eventID);
								if(availability!=0) {
									Counter.putIfAbsent(customerID, new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0)));
									TOR_Seminars.putIfAbsent(customerID, new ArrayList<>());
									TOR_Seminars.get(customerID).add(eventID);
									TOREvents.get(eventType).put(eventID, TOREvents.get(eventType).get(eventID)-1);
									System.out.println(TOREvents);
									System.out.println(TOR_Seminars);
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
							if(TOREvents.get(eventType).containsKey(eventID)) {
								int availability = TOREvents.get(eventType).get(eventID);
								if(availability!=0) {
									Counter.putIfAbsent(customerID, new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0)));
									TOR_TradeShows.putIfAbsent(customerID, new ArrayList<>());
									TOR_TradeShows.get(customerID).add(eventID);
									TOREvents.get(eventType).put(eventID, TOREvents.get(eventType).get(eventID)-1);
									System.out.println(TOREvents);
									System.out.println(TOR_TradeShows);
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
				}else if(eventID.matches("MTL.......")) {
					DatagramSocket asocket = new DatagramSocket();
	                InetAddress aHost = InetAddress.getByName("localhost");
	                
					Counter.putIfAbsent(customerID, new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0)));
					int m = Integer.parseInt(eventID.substring(6, 8));
					int i = Counter.get(customerID).get(m-1);
					if(i<3) {
						String s="BOOKEVENT"+" "+customerID+" "+eventID+" "+eventType;
	                    byte[] reply = s.getBytes();
	                    DatagramPacket req = new DatagramPacket(reply,reply.length,aHost,2224);
	                    asocket.send(req);
	                    System.out.println("Request sent to MTL server for booking event.");
	                    
	                    byte[] buffer = new byte[1000];
	                    DatagramPacket Rec = new DatagramPacket(buffer,buffer.length);
	                    asocket.receive(Rec);
	                    String temp = new String(Rec.getData());
	                    
	                    if("yes".equalsIgnoreCase(temp.trim().toString())) {
	                    	Counter.putIfAbsent(customerID, new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0)));
	                    	if(eventType.equalsIgnoreCase("C")) {
	                    		TOR_Conferences.putIfAbsent(customerID, new ArrayList<>());
	                    		TOR_Conferences.get(customerID).add(eventID);
								Counter.get(customerID).set(m-1, Counter.get(customerID).get(m-1)+1);
								System.out.println(Counter);
								System.out.println(TOR_Conferences);
								log.log(Level.INFO, "Customer {0} booked {1} of {2}", new Object[]{customerID, eventID, eventType});
								return eventID+" booked";
	        				}else if(eventType.equalsIgnoreCase("S")) {
	        					TOR_Seminars.putIfAbsent(customerID, new ArrayList<>());
	        					TOR_Seminars.get(customerID).add(eventID);
								Counter.get(customerID).set(m-1, Counter.get(customerID).get(m-1)+1);
								System.out.println(Counter);
								System.out.println(TOR_Seminars);
								log.log(Level.INFO, "Customer {0} booked {1} of {2}", new Object[]{customerID, eventID, eventType});
								return eventID+" booked";
	        				}else if(eventType.equalsIgnoreCase("T")) {
								TOR_TradeShows.putIfAbsent(customerID, new ArrayList<>());
								TOR_TradeShows.get(customerID).add(eventID);
								Counter.get(customerID).set(m-1, Counter.get(customerID).get(m-1)+1);
								System.out.println(Counter);
								System.out.println(TOR_TradeShows);
								log.log(Level.INFO, "Customer {0} booked {1} of {2}", new Object[]{customerID, eventID, eventType});
								return eventID+" booked";
	        				}
	                    }else {
	                    	return "Failed to book event.";
	                    }
					}else {
						return "Reached limit of booking events in other cities.";
					}
				}else if(eventID.matches("OTW.......")) {
					DatagramSocket asocket = new DatagramSocket();
	                InetAddress aHost = InetAddress.getByName("localhost");
	                
					Counter.putIfAbsent(customerID, new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0)));
					int m = Integer.parseInt(eventID.substring(6, 8));
					int i = Counter.get(customerID).get(m-1);
					if(i<3) {
						String s="BOOKEVENT"+" "+customerID+" "+eventID+" "+eventType;
	                    byte[] reply = s.getBytes();
	                    DatagramPacket req = new DatagramPacket(reply,reply.length,aHost,3334);
	                    asocket.send(req);
	                    System.out.println("Request sent to OTW server for booking event.");
	                    
	                    byte[] buffer = new byte[1000];
	                    DatagramPacket Rec = new DatagramPacket(buffer,buffer.length);
	                    asocket.receive(Rec);
	                    String temp = new String(Rec.getData());
	                    
	                    if("yes".equalsIgnoreCase(temp.trim().toString())) {
	                    	Counter.putIfAbsent(customerID, new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0)));
	                    	if(eventType.equalsIgnoreCase("C")) {
	                    		TOR_Conferences.putIfAbsent(customerID, new ArrayList<>());
	                    		TOR_Conferences.get(customerID).add(eventID);
								Counter.get(customerID).set(m-1, Counter.get(customerID).get(m-1)+1);
								System.out.println(Counter);
								System.out.println(TOR_Conferences);
								log.log(Level.INFO, "Customer {0} booked {1} of {2}", new Object[]{customerID, eventID, eventType});
								return eventID+" booked";
	        				}else if(eventType.equalsIgnoreCase("S")) {
	        					TOR_Seminars.putIfAbsent(customerID, new ArrayList<>());
	        					TOR_Seminars.get(customerID).add(eventID);
								Counter.get(customerID).set(m-1, Counter.get(customerID).get(m-1)+1);
								System.out.println(Counter);
								System.out.println(TOR_Seminars);
								log.log(Level.INFO, "Customer {0} booked {1} of {2}", new Object[]{customerID, eventID, eventType});
								return eventID+" booked";
	        				}else if(eventType.equalsIgnoreCase("T")) {
								TOR_TradeShows.putIfAbsent(customerID, new ArrayList<>());
								TOR_TradeShows.get(customerID).add(eventID);
								Counter.get(customerID).set(m-1, Counter.get(customerID).get(m-1)+1);
								System.out.println(Counter);
								System.out.println(TOR_TradeShows);
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
				return "Only Toronto customers are allowed.";
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
			if(eventID.substring(0, 3).equalsIgnoreCase("TOR")) {
				if(TOREvents.get(eventType).containsKey(eventID)) {
					
					DatagramSocket asocket = new DatagramSocket();
	                InetAddress aHost = InetAddress.getByName("localhost");
	                
	                String s="REMOVEEVENT"+" "+eventID+" "+eventType;
                    byte[] reply1 = s.getBytes();
                    //********************OTW*************************
                    DatagramPacket req_to_mtl = new DatagramPacket(reply1,reply1.length,aHost,2224);
                    asocket.send(req_to_mtl);
                    System.out.println("Request sent to MTL server for removing event.");
                    
                    byte[] buffer = new byte[1000];
                    DatagramPacket Rec_from_mtl = new DatagramPacket(buffer,buffer.length);
                    asocket.receive(Rec_from_mtl);
                    String temp = new String(Rec_from_mtl.getData());
                    System.out.println(temp);
					//**********************TOR*******************
                    DatagramPacket req_to_otw = new DatagramPacket(reply1,reply1.length,aHost,3334);
                    asocket.send(req_to_otw);
                    System.out.println("Request sent to OTW server for removing event.");
                    
                    byte[] buffer1 = new byte[1000];
                    DatagramPacket Rec_from_otw = new DatagramPacket(buffer,buffer.length);
                    asocket.receive(Rec_from_otw);
                    String temp1 = new String(Rec_from_otw.getData());
                    System.out.println(temp1);
                    
					TOREvents.get(eventType).remove(eventID);
					System.out.println("TOREvents : "+TOREvents);
					if(eventType.equals("C")) {
						for(HashMap.Entry<String,ArrayList<String>> pair: TOR_Conferences.entrySet()) {
							System.out.println(pair.getValue());
							if(pair.getValue().contains(eventID)) {
								TOR_Conferences.get(pair.getKey()).remove(eventID);
								System.out.println("TOR_Conferences : "+TOR_Conferences);
							}
						}
						log.log(Level.INFO, "Event {0} of Conferences has been removed", new Object[]{eventID});
						return eventID+" Removed.";
					}else if(eventType.equals("S")) {
						for(HashMap.Entry<String,ArrayList<String>> pair: TOR_Seminars.entrySet()) {
							System.out.println(pair.getValue());
							if(pair.getValue().contains(eventID)) {
								TOR_Seminars.get(pair.getKey()).remove(eventID);
								System.out.println("TOR_Seminars : "+TOR_Seminars);
							}
						}
						log.log(Level.INFO, "Event {0} of Seminars has been removed", new Object[]{eventID});
						return eventID+" Removed.";
					}else if(eventType.equals("T")) {
						for(HashMap.Entry<String,ArrayList<String>> pair: TOR_TradeShows.entrySet()) {
							System.out.println(pair.getValue());
							if(pair.getValue().contains(eventID)) {
								TOR_TradeShows.get(pair.getKey()).remove(eventID);
								System.out.println("TOR_TradeShows : "+TOR_TradeShows);
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
		            DatagramPacket req_to_mtl = new DatagramPacket(reply1,reply1.length,aHost,2224);
		            asocket.send(req_to_mtl);
		            System.out.println("Request sent to MTL server for listing event.");
		            
		            byte[] buffer = new byte[1000];
		            DatagramPacket Rec_from_mtl = new DatagramPacket(buffer,buffer.length);
		            asocket.receive(Rec_from_mtl);
		            String temp = new String(Rec_from_mtl.getData());
		            System.out.println(temp);
		            s=s+temp;
		            //********************TOR*************************
		            DatagramPacket req_to_otw = new DatagramPacket(reply1,reply1.length,aHost,3334);
		            asocket.send(req_to_otw);
		            System.out.println("Request sent to OTW server for listing event.");
		            
		            byte[] buffer1 = new byte[1000];
		            DatagramPacket Rec_from_otw = new DatagramPacket(buffer,buffer.length);
		            asocket.receive(Rec_from_otw);
		            String temp1 = new String(Rec_from_otw.getData());
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
	public String getBookingSchedule(String customerID) throws RemoteException {
		// TODO Auto-generated method stub
		String s = "";
		if(TOR_Conferences.containsKey(customerID)) {
			s = s+"Conferences: "+TOR_Conferences.get(customerID);
		}
		if(TOR_Seminars.containsKey(customerID)) {
			s = s+"Seminars: "+TOR_Seminars.get(customerID);
		}
		if(TOR_TradeShows.containsKey(customerID)) {
			s = s+"TradeShows: "+TOR_TradeShows.get(customerID);
		}
		return s;
	}

	@Override
	public synchronized String cancelEvent(String customerID, String eventID, String eventType) throws IOException {
		// TODO Auto-generated method stub
		if(eventID.matches("MTL.......")) {
			if(eventType.equalsIgnoreCase("C")) {
				if(TOR_Conferences.containsKey(customerID)) {
					if(TOR_Conferences.get(customerID).contains(eventID)) {
						TOR_Conferences.get(customerID).remove(eventID);
						//TOREvents.get("C").put(eventID, TOREvents.get("C").get(eventID)+1);
						System.out.println(TOREvents);
						System.out.println(TOR_Conferences);
						log.log(Level.INFO, "Event {0} of Conferences has been canceled for {1}", new Object[]{eventID, customerID});
						return eventID+" canceled for "+customerID;
					}else {
						return customerID+" not registered for "+eventID;
					}
				}else {
					return "No record for "+customerID;
				}
			}else if(eventType.equalsIgnoreCase("S")) {
				if(TOR_Seminars.containsKey(customerID)) {
					if(TOR_Seminars.get(customerID).contains(eventID)) {
						TOR_Seminars.get(customerID).remove(eventID);
						//TOREvents.get("S").put(eventID, TOREvents.get("S").get(eventID)+1);
						System.out.println(TOREvents);
						System.out.println(TOR_Seminars);
						log.log(Level.INFO, "Event {0} of Seminars has been canceled for {1}", new Object[]{eventID, customerID});
						return eventID+" canceled for "+customerID;
					}else {
						return customerID+" not registered for "+eventID;
					}
				}else {
					return "No record for "+customerID;
				}
			}else if(eventType.equalsIgnoreCase("T")) {
				if(TOR_TradeShows.containsKey(customerID)) {
					if(TOR_TradeShows.get(customerID).contains(eventID)) {
						TOR_TradeShows.get(customerID).remove(eventID);
						//TOREvents.get("T").put(eventID, TOREvents.get("T").get(eventID)+1);
						System.out.println(TOREvents);
						System.out.println(TOR_TradeShows);
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
            String s="CANCELEVENT"+" "+customerID+" "+eventID;
            byte[] reply = s.getBytes();
            if(eventID.matches("MTL.......")) {
            	DatagramPacket req = new DatagramPacket(reply,reply.length,aHost,2224);
            	asocket.send(req);
                System.out.println("Request sent to MTL server for booking event.");
            }else if(eventID.matches("OTW.......")) {
            	DatagramPacket req = new DatagramPacket(reply,reply.length,aHost,3334);
            	asocket.send(req);
                System.out.println("Request sent to OTW server for booking event.");
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
            		TOR_Conferences.get(customerID).remove(eventID);
            		Counter.get(customerID).set(m-1, Counter.get(customerID).get(m-1)-1);
                	System.out.println(Counter);
                	System.out.println(TOR_Conferences);
                	log.log(Level.INFO, "Event {0} of Conferences has been canceled for {1}", new Object[]{eventID, customerID});
                	return eventID+" canceled for "+customerID;
				}else if(eventType.equalsIgnoreCase("S")) {
					TOR_Seminars.get(customerID).remove(eventID);
					Counter.get(customerID).set(m-1, Counter.get(customerID).get(m-1)-1);
	            	System.out.println(Counter);
	            	System.out.println(TOR_Seminars);
	            	log.log(Level.INFO, "Event {0} of Seminars has been canceled for {1}", new Object[]{eventID, customerID});
	            	return eventID+" canceled for "+customerID;
				}else if(eventType.equalsIgnoreCase("T")) {
					TOR_TradeShows.get(customerID).remove(eventID);
					Counter.get(customerID).set(m-1, Counter.get(customerID).get(m-1)-1);
	            	System.out.println(Counter);
	            	System.out.println(TOR_TradeShows);
	            	log.log(Level.INFO, "Event {0} of TradeShows has been canceled for {1}", new Object[]{eventID, customerID});
	            	return eventID+" canceled for "+customerID;
				}
            }else {
            	return "Failed to cancel "+eventID;
            }
            
		}
		return null;
	}
}
