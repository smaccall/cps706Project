package socketprogramming;

import java.util.ArrayList;

public class DDNS {
	
	//Creates records for ddns table
	public static class DDNSEntry{
		private String hostName="";
		private String value="";
		private String type="";
		
		public DDNSEntry(String h,String v,String t){
			this.hostName = h;
			this.value = v;
			this.type = t;
		}		
		public String getHostname() { return hostName; }
		public void setHostname(String h) { this.hostName = h; }
		public String getValue() { return value; }
		public void setValue(String value) { this.value = value; }
		public String getType() { return type; }
		public void setType(String type) { this.type = type; }
	}

	//contains DDNS entries
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