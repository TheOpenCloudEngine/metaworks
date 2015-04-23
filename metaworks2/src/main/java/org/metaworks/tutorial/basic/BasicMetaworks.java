package org.metaworks.tutorial.basic;

import java.awt.BorderLayout;
import java.sql.Types;
import java.util.Date;

import javax.swing.JFrame;

import org.metaworks.FieldDescriptor;
import org.metaworks.InputDialog;
import org.metaworks.InputForm;
import org.metaworks.Instance;
import org.metaworks.Type;
import org.metaworks.validator.Validator;

public class BasicMetaworks {

	public static void  main(String[] args){
		
		
		Type px_part_newparts = new Type(
				"px_part_newparts",     //  table명
							//  column              title           type            iskey
				new FieldDescriptor[]{
					new FieldDescriptor("SEQNO",            "순번",         Types.INTEGER,  true),
					new FieldDescriptor("DESCRIPTION",      "설명"),
					new FieldDescriptor("DEVELOPMENTDATE",  "개발일정",     Types.DATE, false, null, null, 
							new Validator[]{
								new Validator(){
		                                public String validate(Object data, Instance instance){
		                                	if (((Date)data).before(new Date())){
		                                		return "due date should be after than today";
		                                	}else
		                                		return null;
		                                }
		                        }
							}
					),
					new FieldDescriptor("DIVISION",         "사업부")
				}
		);
		
		
		InputDialog newForm = new InputDialog(px_part_newparts){

			public void onSaveOK(Instance rec) {
				System.out.println(rec.get("DEVELOPMENTDATE"));

				super.onSaveOK(rec);
			}
			
		};
        
		newForm.show();
		
	}
}
