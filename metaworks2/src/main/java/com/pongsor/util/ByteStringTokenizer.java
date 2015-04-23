package com.pongsor.util;

import java.util.*;
/**
 * 한/영 혼용된 문자열을 지정한 길이만큼 잘라서 배열에 넣는다.<p>
 * 사용법)<br><pre>
 *	public static void main(String args[]){
 *		try{
 *
 *			System.out.println("====");
 *			
 *			String out [] = getToken("가 나 다라마바abcd 아 차dddd카타 파rr 하", 11);	
 *			
 *			for(int i=0; i<out.length; i++){
 *				System.out.println("["+out[i]+"]");
 *			}
 *			
 *		}catch(Exception e){
 *			e.printStackTrace();
 *		}
 *	}
 * </pre>
 * @author 장진영
 */

public class ByteStringTokenizer{

	public static String[] getToken(String string, int size) throws Exception{
	
		if(size<1) throw new Exception("Out of cutting size");
		
	
		byte [] src = string.getBytes("KSC5601");
		
		int starting =0;
		boolean hangul;
		boolean ending = false;
		
		Vector result = new Vector();
		
		while(!ending){
			hangul = false;
			byte [] token = null;
			
			int j=starting + size;			
			if(j < src.length){
				for(; j>starting && src[j-1]<0; j--)//{
					//System.out.println("do");
					hangul = !hangul;
				//}
				
				token = new byte[size-(hangul ? 1:0)];
				
			}else{
				ending = true;

				token = new byte[src.length-starting];
			}
				
			//System.out.println(j+":"+hangul);			
			//System.out.println(token.length);
			
			// copy string in bytes
			for(int i=0; i<token.length; i++){
				token [i]= src[i+starting];
				
			//	System.out.println(token[i]);
			}
			
			//System.out.println(new String (token, "KSC5601"));
			
			if(hangul && size==1)	// 이 경우 무한 루프가 발생하므로 조정
				size=2;
			else 
				starting = Math.min(starting + token.length, src.length);
			
			//System.out.println(starting);
			
			result.add(new String (token, "KSC5601"));
			
		}
			

		return PrimitiveConverter.vectorToStringArray(result);
	}
	


		
	public static void main(String args[]){
		try{

			System.out.println("====");
			
			String out [] = getToken("kk", 1);//가나 다라마바abcd 아 차dddd카타 파rr 하", 11);	
			
			for(int i=0; i<out.length; i++){
				System.out.println("["+out[i]+"]");
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
		