package org.metaworks;

import javax.persistence.Id;

import org.metaworks.annotation.Available;
import org.metaworks.annotation.Default;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dwr.MetaworksRemoteService;

@Face(ejsPath="genericfaces/CleanObjectFace.ejs")
public class Admin {

	public Admin() {
		setTargetClassName("*");
	}
	
	String targetClassName;
		@Id
		@Default(value="*")
		@Available(when=MetaworksContext.WHEN_EDIT)
		public String getTargetClassName() {
			return targetClassName;
		}
		public void setTargetClassName(String targetClassName) {
			this.targetClassName = targetClassName;
		}
	
	@ServiceMethod(keyBinding="Ctrl+F5@Global")
	public void refreshMetadata() throws Exception{
		System.out.println("targetClassName : " + getTargetClassName());
		
		MetaworksRemoteService.getInstance().clearMetaworksType(getTargetClassName());
	}
}
