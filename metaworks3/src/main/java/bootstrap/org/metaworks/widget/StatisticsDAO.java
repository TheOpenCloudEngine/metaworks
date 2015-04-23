package bootstrap.org.metaworks.widget;

import java.sql.Timestamp;

import org.metaworks.dao.Database;


public class StatisticsDAO extends Database<IStatisticsDAO> implements IStatisticsDAO {
	
	String empName;
	int completeWorklistCountByPerson;
	
	public String getEmpName() {return empName;}
	public void setEmpName(String empName) {this.empName = empName;}
	public int getCompleteWorklistCountByPerson() {return completeWorklistCountByPerson;}
	public void setCompleteWorklistCountByPerson(int completeWorklistCountByPerson) {this.completeWorklistCountByPerson = completeWorklistCountByPerson;}
	
	public IStatisticsDAO getWorklistCountPerPerson() throws Exception {
		StringBuffer sb = new StringBuffer();
		
		sb.append("	select EMP.empName, BW.ENDPOINT, COUNT(BW.ENDPOINT) completeWorklistCountByPerson 		");
		sb.append("	from bpm_worklist bw                                                    	");
		sb.append("		INNER JOIN bpm_procinst bp on bp.INSTID = bw.ROOTINSTID             	");
		sb.append("			AND bp.ISDELETED = 0                                            	");
		sb.append("			AND bp.STATUS = 'Completed'                                     	");
		sb.append("		INNER JOIN EMPTABLE EMP ON BW.ENDPOINT = EMP.empcode                	");
		sb.append("			AND EMP.ISDELETED = 0 												");
		if(getEndpoint() != null && !"".equals(getEndpoint())){
			sb.append("		AND EMP.EMPCODE LIKE ?ENDPOINT                                      "); 
		}
		if(getEmpName() != null && !"".equals(getEmpName())){
			sb.append("		AND EMP.EMPNAME LIKE ?EMPNAME                                       "); 
		}
		sb.append("	WHERE bw.trctag is not null                                             	");
		sb.append("	GROUP BY EMP.EMPNAME                                                    	");
		
		IStatisticsDAO ci = sql(sb.toString());
		if(getEndpoint() != null && !"".equals(getEndpoint())){
			ci.setEndpoint("%" + getEndpoint() +  "%");
		}
		if(getEmpName() != null && !"".equals(getEmpName())){
			ci.setEmpName("%" + getEmpName() +  "%");
		}
		ci.select();
		
		return ci;
	}
	
	
	String endpoint;
	String status;
	String title;
	Timestamp startdate;
	Timestamp enddate;
	Timestamp duedate;
	
	
	public String getStatus() {return status;}
	public void setStatus(String status) {this.status = status;}
	public String getTitle() {return title;}
	public void setTitle(String title) {this.title = title;}
	public Timestamp getStartdate() {return startdate;}
	public void setStartdate(Timestamp startdate) {this.startdate = startdate;}
	public Timestamp getEnddate() {return enddate;}
	public void setEnddate(Timestamp enddate) {this.enddate = enddate;}
	public Timestamp getDuedate() {return duedate;}
	public void setDuedate(Timestamp duedate) {this.duedate = duedate;}
	public String getEndpoint() {return endpoint;}
	public void setEndpoint(String endpoint) {this.endpoint = endpoint;}
	
	public IStatisticsDAO getDetailWorklistCountPerPerson() throws Exception {
		StringBuffer sb = new StringBuffer();
		
		sb.append("	select BW.TITLE, BW.STATUS, Bw.startdate, BW.ENDDATE, BW.DUEDATE   	"); 
		sb.append("	from bpm_worklist bw                                               	");      	
		sb.append("		INNER JOIN bpm_procinst bp on bp.INSTID = bw.ROOTINSTID        	");      	
		sb.append("			AND bp.ISDELETED = 0                                       	");      	
		sb.append("			AND bp.STATUS = 'Completed'                                	");      	
		sb.append("		INNER JOIN EMPTABLE EMP ON BW.ENDPOINT = EMP.empcode           	");      	
		sb.append("			AND EMP.ISDELETED = 0 										"); 		
		sb.append("	WHERE bw.trctag is not null                                        	"); 
		sb.append("	AND BW.ENDPOINT = ?ENDPOINT                                        	"); 
		sb.append("	ORDER BY BW.STARTDATE                                              	"); 
		
		IStatisticsDAO ci = sql(sb.toString());
		ci.setEndpoint(getEndpoint());
		ci.select();
		
		return ci;
	}
	
	
	String defId;
	String defName;
	int completeInstanceCount;
	
	public String getDefId() {return defId;}
	public void setDefId(String defId) {this.defId = defId;}
	public String getDefName() {return defName;}
	public void setDefName(String defName) {this.defName = defName;}
	public int getCompleteInstanceCount() {return completeInstanceCount;}
	public void setCompleteInstanceCount(int completeInstanceCount) {this.completeInstanceCount = completeInstanceCount;}
	
	public IStatisticsDAO getInstanceCount() throws Exception {
		StringBuffer sb = new StringBuffer();
		
		sb.append("	select defId, defName, COUNT(DEFID) completeInstanceCount      		");
		sb.append("	from bpm_procinst                                                	");
		sb.append("	where isdeleted = 0                                              	");
		sb.append("	AND STATUS = 'Completed'                                         	");
		sb.append("	AND DEFID IS NOT NULL                                            	");
		sb.append("	GROUP BY DEFID, DEFNAME;                                         	");
		
		IStatisticsDAO ci = sql(sb.toString());
		ci.select();
		
		return ci;
	}
	
	public IStatisticsDAO getDetailInstanceCount() throws Exception {
		StringBuffer sb = new StringBuffer();
		
		sb.append("	select defId, defName, COUNT(DEFID) completeInstanceCount      		");
		sb.append("	from bpm_procinst                                                	");
		sb.append("	where isdeleted = 0                                              	");
		sb.append("	AND STATUS = 'Completed'                                         	");
		sb.append("	AND DEFID IS NOT NULL                                            	");
		sb.append("	GROUP BY DEFID, DEFNAME;                                         	");
		
		IStatisticsDAO ci = sql(sb.toString());
		ci.select();
		
		return ci;
	}
	
	
	int delayWorklistCount;

	public int getDelayWorklistCount() {return delayWorklistCount;}
	public void setDelayWorklistCount(int delayWorklistCount) {this.delayWorklistCount = delayWorklistCount;}
	
	public IStatisticsDAO getDelayWorklistCountByPerson() throws Exception {
		StringBuffer sb = new StringBuffer();

		sb.append(" SELECT EMP.empName, COUNT(BW.ENDPOINT) delayWorklistCount    	");
		sb.append(" from bpm_worklist bw                                           	");
		sb.append(" 	INNER JOIN bpm_procinst bp on bp.INSTID = bw.ROOTINSTID    	");
		sb.append(" 		AND bp.ISDELETED = 0                                   	");
		sb.append(" 		AND bp.STATUS IN ('Running')              	");
		sb.append(" 	INNER JOIN EMPTABLE EMP ON BW.ENDPOINT = EMP.empcode       	");
		sb.append("			AND EMP.ISDELETED = 0 									");
		sb.append(" WHERE BW.DUEDATE < NOW()                                       	");
		sb.append(" AND BW.ENDDATE IS NULL 												");
		sb.append(" GROUP BY EMP.EMPNAME                                           	");
		sb.append(" ORDER BY COUNT(BW.ENDPOINT) DESC                               	");
		sb.append(" LIMIT 10                                                       	");
		
		IStatisticsDAO ci = sql(sb.toString());
		ci.select();
		
		return ci;
	}
	
	public IStatisticsDAO getDetailDelayWorklistCountByPerson() throws Exception {
		StringBuffer sb = new StringBuffer();

		sb.append(" SELECT EMP.empName, COUNT(BW.ENDPOINT) delayWorklistCount    	");
		sb.append(" from bpm_worklist bw                                           	");
		sb.append(" 	INNER JOIN bpm_procinst bp on bp.INSTID = bw.ROOTINSTID    	");
		sb.append(" 		AND bp.ISDELETED = 0                                   	");
		sb.append(" 		AND bp.STATUS IN ('Running')              	");
		sb.append(" 	INNER JOIN EMPTABLE EMP ON BW.ENDPOINT = EMP.empcode       	");
		sb.append("			AND EMP.ISDELETED = 0 									");
		sb.append(" WHERE BW.DUEDATE < NOW()                                       	");
		sb.append(" AND BW.ENDDATE IS NULL 												");
		sb.append(" GROUP BY EMP.EMPNAME                                           	");
		sb.append(" ORDER BY COUNT(BW.ENDPOINT) DESC                               	");
		sb.append(" LIMIT 10                                                       	");
		
		IStatisticsDAO ci = sql(sb.toString());
		ci.select();
		
		return ci;
	}
	
	
	String partName;
	
	public String getPartName() {return partName;}
	public void setPartName(String partName) {this.partName = partName;}
	
	public IStatisticsDAO getDelayWorklistCountByPart() throws Exception {
		StringBuffer sb = new StringBuffer();

		sb.append(" select DEPT.partName, COUNT(BW.ENDPOINT) delayWorklistCount   		");
		sb.append(" from bpm_worklist bw                                            	");
		sb.append(" 	INNER JOIN bpm_procinst bp on bp.INSTID = bw.ROOTINSTID     	");
		sb.append(" 		AND bp.ISDELETED = 0                                    	");
		sb.append(" 		AND bp.STATUS IN ('Running')                            	");
		sb.append(" 	INNER JOIN EMPTABLE EMP ON BW.ENDPOINT = EMP.empcode        	");
		sb.append(" 		AND EMP.ISDELETED = 0                                   	");
		sb.append(" 	INNER JOIN PARTTABLE DEPT ON DEPT.PARTCODE = EMP.PARTCODE   	");
		sb.append(" 		AND DEPT.ISDELETED = 0                                   	");
		sb.append(" WHERE BW.DUEDATE < NOW()                                        	");
		sb.append(" AND BW.ENDDATE IS NULL 												");
		sb.append(" GROUP BY DEPT.PARTNAME                                          	");
		sb.append(" ORDER BY COUNT(BW.ENDPOINT) DESC                                	");
		sb.append(" LIMIT 10                                                        	");
		
		IStatisticsDAO ci = sql(sb.toString());
		ci.select();
		
		return ci;
	}
	
	public IStatisticsDAO getDetailDelayWorklistCountByPart() throws Exception {
		StringBuffer sb = new StringBuffer();

		sb.append(" select DEPT.partName, COUNT(BW.ENDPOINT) delayWorklistCount   		");
		sb.append(" from bpm_worklist bw                                            	");
		sb.append(" 	INNER JOIN bpm_procinst bp on bp.INSTID = bw.ROOTINSTID     	");
		sb.append(" 		AND bp.ISDELETED = 0                                    	");
		sb.append(" 		AND bp.STATUS IN ('Running')                            	");
		sb.append(" 	INNER JOIN EMPTABLE EMP ON BW.ENDPOINT = EMP.empcode        	");
		sb.append(" 		AND EMP.ISDELETED = 0                                   	");
		sb.append(" 	INNER JOIN PARTTABLE DEPT ON DEPT.PARTCODE = EMP.PARTCODE   	");
		sb.append(" 		AND DEPT.ISDELETED = 0                                   	");
		sb.append(" WHERE BW.DUEDATE < NOW()                                        	");
		sb.append(" AND BW.ENDDATE IS NULL 												");
		sb.append(" GROUP BY DEPT.PARTNAME                                          	");
		sb.append(" ORDER BY COUNT(BW.ENDPOINT) DESC                                	");
		sb.append(" LIMIT 10                                                        	");
		
		IStatisticsDAO ci = sql(sb.toString());
		ci.select();
		
		return ci;
	}
	
	
	int todayCompleteWorklistCount;
	
	public int getTodayCompleteWorklistCount() {return todayCompleteWorklistCount;}
	public void setTodayCompleteWorklistCount(int todayCompleteWorklistCount) {this.todayCompleteWorklistCount = todayCompleteWorklistCount;}
	
	public IStatisticsDAO getTodayCompleteWorklistCountByPerson() throws Exception {
		StringBuffer sb = new StringBuffer();

		sb.append(" SELECT EMP.EMPNAME, COUNT(BW.ENDPOINT) todayCompleteWorklistCount          	");
		sb.append(" from bpm_worklist bw                                                      	");
		sb.append(" 	INNER JOIN bpm_procinst bp on bp.INSTID = bw.ROOTINSTID               	");
		sb.append(" 		AND bp.ISDELETED = 0                                              	");
		sb.append(" 		AND bp.STATUS IN ('Running')                                      	");
		sb.append(" 	INNER JOIN EMPTABLE EMP ON BW.ENDPOINT = EMP.empcode                  	");
		sb.append(" 		AND EMP.ISDELETED = 0                                             	");
		sb.append(" WHERE date_format(BW.DUEDATE, '%Y%m%d') = date_format(now(), '%Y%m%d')    	");
		sb.append(" AND BW.ENDDATE IS NULL                                                    	");
		sb.append(" GROUP BY EMP.EMPNAME                                                      	");
		sb.append(" ORDER BY COUNT(BW.ENDPOINT) DESC                                          	");
		
		IStatisticsDAO ci = sql(sb.toString());
		ci.select();
		
		return ci;
	}
	
	public IStatisticsDAO getDetailTodayCompleteWorklistCountByPerson() throws Exception {
		StringBuffer sb = new StringBuffer();

		sb.append(" SELECT EMP.EMPNAME, COUNT(BW.ENDPOINT) todayCompleteWorklistCount          	");
		sb.append(" from bpm_worklist bw                                                      	");
		sb.append(" 	INNER JOIN bpm_procinst bp on bp.INSTID = bw.ROOTINSTID               	");
		sb.append(" 		AND bp.ISDELETED = 0                                              	");
		sb.append(" 		AND bp.STATUS IN ('Running')                                      	");
		sb.append(" 	INNER JOIN EMPTABLE EMP ON BW.ENDPOINT = EMP.empcode                  	");
		sb.append(" 		AND EMP.ISDELETED = 0                                             	");
		sb.append(" WHERE date_format(BW.DUEDATE, '%Y%m%d') = date_format(now(), '%Y%m%d')    	");
		sb.append(" AND BW.ENDDATE IS NULL                                                    	");
		sb.append(" GROUP BY EMP.EMPNAME                                                      	");
		sb.append(" ORDER BY COUNT(BW.ENDPOINT) DESC                                          	");
		
		IStatisticsDAO ci = sql(sb.toString());
		ci.select();
		
		return ci;
	}
}
