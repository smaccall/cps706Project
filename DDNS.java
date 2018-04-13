package socketprogramming;

import java.util.ArrayList;

public class DDNS {
	/*
	 * DDNS Entry class
	 * Record hostname, value and type.
	 */
	public static class DDNSEntry{
		private String hostname="";
		private String value="";
		private String type="";
		
		public DDNSEntry(String hostname,String value,String type){
			this.hostname = hostname;
			this.value = value;
			this.type = type;
		}		
		public String getHostname() { return hostname; }
		public void setHostname(String hostname) { this.hostname = hostname; }
		public String getValue() { return value; }
		public void setValue(String value) { this.value = value; }
		public String getType() { return type; }
		public void setType(String type) { this.type = type; }
	}

	/*
	 * DDNS table
	 * Holds DDNS entries in arraylist
	 */
	public static class DDNSTable{
		private static ArrayList<DDNSEntry> DDNSTable;
		public DDNSTable(){
			DDNSTable = new ArrayList<DDNSEntry>();
		}
		public void addEntry(String hostname,String value,String type){			
			DDNSTable.add(new DDNSEntry(hostname,value,type));
		}
		public String findValue(String hostname)
		{
			for(DDNSEntry entry : DDNSTable){
				if(entry.getHostname().equals(hostname)) 
					return entry.getValue();
			}
			System.out.println("Hostname Not Found In DDNS Table");
			return null;
		}	
		public String findType(String hostname) {
			for(DDNSEntry entry : DDNSTable){
				if(entry.getHostname().equals(hostname)) 
					return entry.getType();
			}
			System.out.println("Hostname Not Found In DDNS Table");
			return null;
		}
		
	}	
}
