package org.metaworks.wizard.reverse;

import org.metaworks.*;
import org.metaworks.component.MetaWorksComponentCenter;
import java.util.*;
import java.io.*;

public class ApplicationGenerator{

	public static void main(String[] args) throws Exception{	
		if(args.length<1){
			System.out.println(" usage:	ApplicationGenerator <instructionfile>");
			System.out.println(" 		ApplicationGenerator <projName> <tableName>");
			return;
		}
		
		ApplicationGenerationContext appGenCtx;
		String tableNames[];
		
		if(args[0].endsWith(".xml")){
			String instructionFileName = args[0];		
			appGenCtx = (ApplicationGenerationContext)MetaWorksComponentCenter.deserialize(new FileInputStream(instructionFileName));
			generateApplication(appGenCtx);
		}else{
			String projectName = args[0];
			String tableName=args[1];
System.out.println("projectName:"+projectName);
System.out.println("tableName:"+tableName);
			Type table = new Type();
			
			if(tableName.equals("-all")){
				tableNames = MetaWorksComponentCenter.getInstance().getComponentNames("tables." + projectName);
			}else{
				tableNames = new String[]{tableName};
			}
			
			for(int i=0; i<tableNames.length; i++){
				tableName = tableNames[i];
System.out.println("tableName:"+tableName);
				table.setComponentName("tables." + projectName +"." + tableName);

				appGenCtx = new ApplicationGenerationContext();
				appGenCtx.setTable(table);
				appGenCtx.setApplicationName(tableName);
				appGenCtx.setProjectName(projectName);
				appGenCtx.setApplicationName(tableName);
				appGenCtx.setApplicationClass(Thread.currentThread().getContextClassLoader().loadClass("org.metaworks.DefaultApplication"));
				
				generateApplication(appGenCtx);
			}				
		}			
	}
	
	public static void generateApplication(ApplicationGenerationContext appGenCtx) throws Exception{
		Application app = (Application)appGenCtx.getApplicationClass().newInstance();
		
		app.setType(appGenCtx.getTable());
		
		Map propMap = appGenCtx.getPropertyMap();
		if(propMap!=null && propMap.keySet()!=null)		
		for(Iterator iter = propMap.keySet().iterator(); iter.hasNext(); ){
			String keyStr = (String)iter.next();
			Object val = propMap.get(keyStr);
															//may occur casting problem
			app.getClass().getMethod("set" + keyStr.substring(0,1).toUpperCase() + keyStr.substring(1), new Class[]{val.getClass()}).invoke(app, new Object[]{val});				
		}
		
		MetaWorksComponentCenter.getInstance().saveComponent("applications." + appGenCtx.getProjectName() + "." + appGenCtx.getApplicationName(), app);
	}
}