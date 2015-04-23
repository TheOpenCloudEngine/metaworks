package org.metaworks;

/**
 * FieldDescriptor의 attribute 들을 FieldDescriptor의 생성자에서 바로 입력하기 위한 용도로 사용되는 Class 
 * 
 * 
 * @version 1.0 2000/2/14 
 * @example
 * <pre>
 *  	public static void main(String args[]) throws Exception{
 * 
 * 		// table을 생성한다. db의 'create'문을 연상
 * 		Table px_part_newparts = new Table(
 * 			"px_part_newparts",	//  table명
 * 						//  column		title		type		iskey
 * 			new FieldDescriptor[]{
 * 				new FieldDescriptor("NAME", 		"이름", 	Types.VARCHAR,	true,
 * 					new FieldAttribute[]{
 * 						new FieldAttribute("default", new String('pongsor')),
 *  						new FieldAttribute("size", new Integer(10))
 *  					},
 *  				),
 * 			},
 * 		);
 * 	}		
 * </pre>
 *  
 * @author 장진영 
 * @see com.cwpdm.util.db.Tuple
 */

public class FieldAttribute{
	
	String keyStr;
	Object obj;
	
	public FieldAttribute(String keyStr, Object obj){
		this.keyStr=keyStr;
		this.obj=obj;
	}
	
	public String getKey(){
		return keyStr;
	}
	
	public Object getObject(){
		return obj;
	}
}
	