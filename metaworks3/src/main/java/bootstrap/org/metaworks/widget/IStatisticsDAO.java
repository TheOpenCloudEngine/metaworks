package bootstrap.org.metaworks.widget;

import java.sql.Timestamp;

import org.metaworks.dao.IDAO;

public interface IStatisticsDAO extends IDAO {
	
	public String getDefId();
	public void setDefId(String defId);
	
	public String getDefName();
	public void setDefName(String defName);
	
	public int getCompleteInstanceCount();
	public void setCompleteInstanceCount(int completeInstanceCount);
	
	public String getEmpName();
	public void setEmpName(String empName);
	
	public String getEndpoint();
	public void setEndpoint(String endpoint);
	
	public int getCompleteWorklistCountByPerson();
	public void setCompleteWorklistCountByPerson(int completeWorklistCountByPerson);
	
	public int getDelayWorklistCount();
	public void setDelayWorklistCount(int delayWorklistCount);
	
	public String getPartName();
	public void setPartName(String partName);
	
	public int getTodayCompleteWorklistCount();
	public void setTodayCompleteWorklistCount(int todayCompleteWorklistCount);
	
	public String getTitle();
	public void setTitle(String title);
	public String getStatus();
	public void setStatus(String status);
	public Timestamp getStartdate();
	public void setStartdate(Timestamp startdate);
	public Timestamp getEnddate();
	public void setEnddate(Timestamp enddate);
	public Timestamp getDuedate();
	public void setDuedate(Timestamp duedate);
	
}
