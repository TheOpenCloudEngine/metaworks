package org.metaworks.widget;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.metaworks.annotation.Id;
import org.metaworks.dao.IDAO;
import org.metaworks.dao.MetaworksDAO;
import org.metaworks.dao.TransactionContext;

public class DatabaseChoice extends Choice

{
	public DatabaseChoice()
	{
	}

	public DatabaseChoice(String sql) throws Exception {
		this();
		load(sql);
	}

	public DatabaseChoice(String tableName, 
			              String codeColumn, 
			              String nameColumn, 
			              String conditionColumn, 
			              String conditionValue,
			              String orderBy) throws Exception {
		this();
		load(tableName, codeColumn, nameColumn, conditionColumn, conditionValue, orderBy);
	}

	public DatabaseChoice(String tableName, 
					      String codeColumn,
					      String nameColumn, 
					      String conditionClause,
					      String orderBy) throws Exception {
		this();
		load(tableName, codeColumn, nameColumn, conditionClause, orderBy);
	}

	String codeId;
		@Id
		public String getCodeId() {
			return codeId;
		}
		public void setCodeId(String codeId) {
			this.codeId = codeId;
		}

	public void load(String sql) throws Exception {
		IDAO dao = (IDAO) MetaworksDAO.createDAOImpl(TransactionContext.getThreadLocalInstance(), sql,IDAO. class);
		dao.select();

		while (dao.next()) {
			Object optionValue = dao.get("id");
			String value;
			String name;
			
			if (optionValue instanceof Integer) {
				value = optionValue.toString();
			} else if (optionValue instanceof BigDecimal) {
				value = optionValue.toString();
			} else {
				value = (String) dao.get("id");
			}

			name = (String) dao.get("name");
			
			this.add(name, value);
		}
	}

	public void load(String tableName, String codeColumn, String nameColumn, String conditionClause, String orderBy) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		sql.append(codeColumn).append(" id, ");
		sql.append(nameColumn).append(" name ");
		sql.append("from ").append(tableName);
		if (!"".equals(conditionClause))
			sql.append(" where ").append(conditionClause);
		
		if(orderBy != null)
			sql.append(" order by " + orderBy);

		load(sql.toString());
	}

	public void load(String tableName, String codeColumn, String nameColumn, String conditionColumn, String conditionValue, String orderBy) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		sql.append(codeColumn).append(" id, ");
		sql.append(nameColumn).append(" name ");
		sql.append("from ").append(tableName);
		sql.append(" where ");
		sql.append(conditionColumn).append(" = '").append(conditionValue).append("' ");
		
		if(orderBy != null)
			sql.append(" order by " + orderBy);

		load(sql.toString());
	}
	
	public void copyFromOptionValue(DatabaseChoice object) {
		setOptionNames((ArrayList)object.getOptionNames().clone());
		setOptionValues((ArrayList)object.getOptionValues().clone());
	}
	
	@Override
	public void onLoad() throws Exception {
		
	}
}