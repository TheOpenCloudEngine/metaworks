/*
 * Created on 2004. 12. 14.
 */
package org.metaworks.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.metaworks.MetaworksContext;


/**
 * @author Jinyoung Jang
 */
public class MySQLDAOFactory extends OracleDAOFactory{
	
	static Hashtable currKeys = new Hashtable();
	
	
	public KeyGeneratorDAO createKeyGenerator(final String forWhat, final Map options) throws Exception {
		boolean option_useTableNameHeader = false;
		if(options!=null && options.containsKey("useTableNameHeader")){
			option_useTableNameHeader = !"false".equals(options.get("useTableNameHeader"));
		}
		
		boolean option_onlySequenceTable = false;
		if(options!=null && options.containsKey("onlySequenceTable")){
			option_onlySequenceTable = !"false".equals(options.get("onlySequenceTable"));
		}
		
		final boolean useTableNameHeader = option_useTableNameHeader;
		final boolean onlySequenceTable = option_onlySequenceTable;

		return new KeyGeneratorDAO(){

			public Number getKeyNumber() {
				
				String forTableName = new String(forWhat);
				String forColumnName = new String(((useTableNameHeader) ? forWhat : "") + "id");
				forColumnName = forColumnName.replaceFirst("Proc", "");
				forTableName = forTableName.toLowerCase();
//				forTableName = forTableName.toUpperCase();
				if (forTableName.equals("workitem")) {
					forColumnName = "taskid";
				}
				
				Connection conn = null;
				Statement stmt_select_seq = null;
				ResultSet rs_select_seq = null;
				
				Statement stmt_select_table_max_key = null;
				ResultSet rs_select_table_max_key = null;
				
				PreparedStatement pstmt_update_seq = null;
				PreparedStatement pstmt_insert_seq = null;
				
				try {
					//conn = DefaultConnectionFactory.create().getConnection(); //may cause connection leak and undesired sequence number increment.
					conn = getConnectionFactory().getConnection();
					conn.setAutoCommit(false);
					
					Long seq_key  = null;
						stmt_select_seq = conn.createStatement();
						rs_select_seq = stmt_select_seq.executeQuery("select ifnull(max(seq),0) + 1 as lastkey from bpm_seq where tbname = '" + forTableName + "'");
						if (rs_select_seq.next()) {
							seq_key = rs_select_seq.getLong("lastkey");
						} else {
							seq_key = new Long(1);
						}
					
					Long key = null;
					if(onlySequenceTable){
						key = seq_key;
					}else{
						Long id_key  = null;
						stmt_select_table_max_key = conn.createStatement();
						rs_select_table_max_key = stmt_select_table_max_key.executeQuery("select ifnull(max("+forColumnName+"),0) as lastkey from " +(forTableName.equals("workitem")? "bpm_worklist" : ((useTableNameHeader)?"bpm_":"") + forTableName));
						if (rs_select_table_max_key.next()) {
							id_key = rs_select_table_max_key.getLong("lastkey");
						}
										
						if (seq_key.longValue() > id_key.longValue()) {
							key = seq_key;
						} else {
							key = new Long(id_key.longValue() + 1);
						}
					}
					
					pstmt_update_seq = conn.prepareStatement("update bpm_seq set seq = ? , moddate = now() where  tbname = ?");
					pstmt_update_seq.setLong(1, key);
					pstmt_update_seq.setString(2, forTableName);
					int modcount = pstmt_update_seq.executeUpdate();
					
					if(modcount == 0) {
						pstmt_insert_seq = conn.prepareStatement("insert into bpm_seq (tbname, seq, description, moddate) values(?, ?, ?, now())");
						pstmt_insert_seq.setString(1, forTableName);
						pstmt_insert_seq.setLong(2, key);
						pstmt_insert_seq.setString(3, forTableName);
						pstmt_insert_seq.executeUpdate();
					}
					
					conn.commit();
					
					return key;
					
				} catch (Exception e1) {
					try {
						if(conn != null)
							conn.rollback();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					throw new RuntimeException(e1);
				} finally {
					if (stmt_select_seq != null) try { stmt_select_seq.close(); } catch (SQLException e1) {}
					if (rs_select_seq != null) try { rs_select_seq.close(); } catch (SQLException e1) {}
					
					if (stmt_select_table_max_key != null) try { stmt_select_table_max_key.close(); } catch (SQLException e1) {}
					if (rs_select_table_max_key != null) try { rs_select_table_max_key.close(); } catch (SQLException e1) {}
					
					if (pstmt_update_seq != null) try { pstmt_update_seq.close(); } catch (SQLException e1) {}
					if (pstmt_insert_seq != null) try { pstmt_insert_seq.close(); } catch (SQLException e1) {}
					
					if (conn != null) try { conn.setAutoCommit(true); } catch (SQLException e1) {}
					if (conn != null) try { conn.close(); } catch (SQLException e) {}
				}
			}

			public void setKeyNumber(Number id) {
				// TODO Auto-generated method stub
				
			}

			public void select() throws Exception {
				// TODO Auto-generated method stub
				
			}

			public int insert() throws Exception {
				// TODO Auto-generated method stub
				return 0;
			}

			public int update() throws Exception {
				// TODO Auto-generated method stub
				return 0;
			}

			public int call() throws Exception {
				// TODO Auto-generated method stub
				return 0;
			}

			public void beforeFirst() throws Exception {
				// TODO Auto-generated method stub
				
			}

			public boolean previous() throws Exception {
				// TODO Auto-generated method stub
				return false;
			}

			public boolean next() throws Exception {
				// TODO Auto-generated method stub
				return false;
			}

			public boolean first() throws Exception {
				// TODO Auto-generated method stub
				return false;
			}
			
			public void afterLast() throws Exception {
			}
			
			public boolean last() throws Exception {
				return false;
			}

			public int size() {
				// TODO Auto-generated method stub
				return 0;
			}

			public Object get(String key) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}

			public Object set(String key, Object value) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}

			public int update(String stmt) throws Exception {
				// TODO Auto-generated method stub
				return 0;
			}

			public void addBatch() throws Exception {
				// TODO Auto-generated method stub
				
			}

			public int[] updateBatch() throws Exception {
				// TODO Auto-generated method stub
				return null;
			}

			public String getString(String key) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}

			public Integer getInt(String key) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}

			public Long getLong(String key) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}

			public Boolean getBoolean(String key) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}

			public Date getDate(String key) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}

			public boolean absolute(int pos) throws Exception {
				// TODO Auto-generated method stub
				return false;
			}

			public AbstractGenericDAO getImplementationObject() {
				// TODO Auto-generated method stub
				return null;
			}

			public void releaseResource() throws Exception {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void moveToInsertRow() throws Exception {
				// TODO Auto-generated method stub
				
			}

			@Override
			public MetaworksContext getMetaworksContext() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void setMetaworksContext(MetaworksContext context) {
				// TODO Auto-generated method stub
				
			}
		};
	}
	
	public String getSequenceSql(String seqName) throws Exception {
		// TODO Auto-generated method stub
		return "";
	}

	public String getDBMSProductName() throws Exception {
		return "MySQL";
	}
	
	
}
