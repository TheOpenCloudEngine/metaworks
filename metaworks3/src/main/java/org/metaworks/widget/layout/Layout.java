package org.metaworks.widget.layout;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Id;


@Face(
		ejsPathMappingByContext=
	{
		"{when: 'design', face: 'dwr/metaworks/org/metaworks/widget/layout/Layout_design.ejs'}",
	}		
)
public class Layout implements ContextAware {

	public Layout() {
		setMetaworksContext(new MetaworksContext());		
		getMetaworksContext().setWhen(MetaworksContext.WHEN_NEW);
	}
	
	String id;
		@Id
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}

	Object north;
		public Object getNorth() {
			return north;
		}	
		public void setNorth(Object north) {
			this.north = north;
		}
		
	Object west;
		public Object getWest() {
			return west;
		}	
		public void setWest(Object west) {
			this.west = west;
		}
	
	Object center;
		public Object getCenter() {
			return center;
		}	
		public void setCenter(Object center) {
			this.center = center;
		}
		
	Object east;
		public Object getEast() {
			return east;
		}	
		public void setEast(Object east) {
			this.east = east;
		}	
		
	Object south;
		public Object getSouth() {
			return south;
		}	
		public void setSouth(Object south) {
			this.south = south;
		}
		
	String options;
		public String getOptions() {
			return options;
		}
		public void setOptions(String options) {
			this.options = options;
		}
		
	String name;
	
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
	MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}
	
	boolean useHideBar;
		public boolean isUseHideBar() {
			return useHideBar;
		}
		public void setUseHideBar(boolean useHideBar) {
			this.useHideBar = useHideBar;
		}
}
