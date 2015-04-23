package org.metaworks;

	public class TestClass{
	
		String name;
		
			public String getName(){
				return name;
			}
				
			public void setName(String value){
				name = value;
			}
			
		String address;
		
			public String getAddress(){
				return address;
			}
				
			public void setAddress(String value){
				address = value;
			}
			
		int age;
			public int getAge() {
				return age;
			}
			public void setAge(int i) {
				age = i;
			}
								
			
		public String toString(){
			
			return "name: "  + getName() + "\n" + "address: "+getAddress();
		}

	}
		
