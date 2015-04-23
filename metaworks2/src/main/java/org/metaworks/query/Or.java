package org.metaworks.query;

import java.util.*;

import org.metaworks.*;

public class Or extends QuerySet{
	
	public Or(Query [] q){
		super(q);
	}
	
	public String getSeparator(){
		return " or ";
	}
	
	//// tester ////
	
	public static void main(String args[]){
		
		Query q =
		new And(new Query[]{
			new Or(new Eval[]{				
				new Eval("=", "PROJECT"),
				new Eval(">", "MODEL")
			}),
			new Eval("<", "SEQNO")
		});
		
		Instance rec = new Instance(
			new Type("test",
				new FieldDescriptor[]{
					new FieldDescriptor("PROJECT", "", 2),
					new FieldDescriptor("MODEL"),
					new FieldDescriptor("SEQNO")
				}
			)
		);
	//	rec.put("PROJECT", "prj");
		rec.put("MODEL", "model");
		rec.put("SEQNO", "seq");
		
		System.out.println(q.toSQLExp(rec));		
	}		
}