package org.metaworks.inputter;

/**
 * Field 값을 입력받기 위한 Swing Component와의 일관적인 interface를 제공하기 위한 base class 이 클래스는 InputForm Class가 다음과 같은 경로로 사용한다. 
 * <br>
 * <br>
 * <br>
 * 	1. Field Descriptor에서 attribute 들을 얻는다.<br>
 * 	2. 얻은 attrs들로 initialize(attrs)를 call해 컴포넌트를 초기화한다.<br>
 * 	3. 폼 JPanel에 add시킨다.<br>
 * 	4. 사용자가 폼에 입력된 Record값을 얻으려 할때(InputForm.getRecord()) 'getValue()'를 통해 값을 넘겨준다.<br>
 * 
 * @version 1.0 2000/2/14 
 * @author 장진영 
*/

import java.awt.Component;
import java.awt.event.*;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Iterator;

import org.metaworks.*;

public abstract class AbstractComponentInputter extends AbstractInputter{

	transient Component uiComp=null;

	public AbstractComponentInputter(){
		// do nothing
	}

	public AbstractComponentInputter(Hashtable attrs){
		initialize(attrs);
	}
	
////////////////// implements //////////////////////

	public void initialize(Hashtable attrs){
		super.initialize(attrs);
		
//		if(DoSQL.isRunningInWeb()) return;
		
		getComponent();
		getValueComponent().addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent me){
				onValueChanged();
			}
		});
		
		if(attrs.containsKey(FieldDescriptor.ATTR_DEFAULT))
			setValue(attrs.get(FieldDescriptor.ATTR_DEFAULT));
			
		if(attrs.containsKey(FieldDescriptor.ATTR_FIXED)){
			setValue(attrs.get(FieldDescriptor.ATTR_FIXED));
			getComponent().setEnabled(false);
		}
				
		if(attrs.containsKey("mandatory")){
			bMandatory=true;
		}
	}
			
	/**
	 * 활성화된 입력 컴포넌트를 얻는다. 현재 활성화된 컴포넌트가 없으면 getNewComponent()를 통해 새 인스턴트를 생성해서 pooling하고 있으면 그대로 리턴한다.
	 */
	public Component getComponent(){
		if(uiComp==null)
			uiComp=getNewComponent();
			
		return uiComp;
	}
	
	public void dispose(){
		uiComp = null;
	}
	
////////////////// abstracts /////////////////////

	/**
	 * <b>Description copied from interface:</b> @link com.cwpdm.util.db.inputter
	 * 현재 활성화된 컴포넌트에서 값을 얻음
	 */
	abstract public Object getValue();
	
	/**
	 * <b>Description copied from interface:</b> @link com.cwpdm.util.db.inputter
	 * 현재 활성화된 컴포넌트에 값을 세팅하고 display 시킴
	 */
	abstract public void setValue(Object data);
	abstract public Component getNewComponent();

	public Component getValueComponent(){
		return getComponent();
	}
	
	public void onValueChanged(){
		fireActionEvent(new ActionEvent(this, 1, "valueChanged"));
	}
	
	transient Vector actionListeners = new Vector();
		public void addActionListener(ActionListener al){
			actionListeners.add(al);		
		}
		public void fireActionEvent(ActionEvent ae){
			for(Iterator iter = actionListeners.iterator(); iter.hasNext(); ){
				ActionListener al = (ActionListener)iter.next();
				al.actionPerformed(ae);
			}
		}
}
