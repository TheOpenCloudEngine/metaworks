/*
 * Created on 2004-03-23
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.metaworks.component;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

import java.net.*;

public class URLComponentCenter extends MetaWorksComponentCenter{
		String codebase;
			
		public URLComponentCenter(String codebase){
			super();
			this.codebase = codebase;
			setInstance(this);
		}
			
		public Object getComponent(String name) throws Exception{
			String compFilePath = codebase + "/" + name.replace('.', '/') + ".xml";
			URL url = new URL(compFilePath);
				
			return deserialize(url.openStream());
		}
}
