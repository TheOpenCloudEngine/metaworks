/**
 * Tuple을 처리하는 method들이 든 interface
 * @see com.pongsor.dosql.Tuple
 * @author 장진영
 */
 
 
package org.metaworks;

public interface TupleType{
	/**
	 * 이 Type의 Tuple을 생성하여 리턴	 
	 */
	public Tuple create();
//	public Tuple[] find(TupleProperty keyProperty);

	/**
	 * tuple을 저장할 때의 operation
	 * @param	tuple 저장할 tuple instance
	 */
	public void save(Tuple tuple) throws Exception;
	public void load(Tuple tuple, Object keyObject) throws Exception;
		
	public AbstractPropertyDescriptor getKeyPropertyDescriptor();
		
		
}