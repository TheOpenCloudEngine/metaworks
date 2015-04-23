package org.metaworks;

import java.sql.Connection;

// Referenced classes of package com.pongsor.dosql:
//            Table, InterfaceAppliedTable, FieldDescriptor

public abstract class AbstractAppliedTable extends Type implements InterfaceAppliedTable
{

    public AbstractAppliedTable()
    {
    }

    public AbstractAppliedTable(String s)
    {
        super(s);
    }

    public AbstractAppliedTable(String s, FieldDescriptor afielddescriptor[])
    {
        super(s, afielddescriptor);
    }

    public AbstractAppliedTable(String s, FieldDescriptor afielddescriptor[], Connection connection)
    {
        super(s, afielddescriptor, connection);
    }
    
    

    public abstract void newRecord();
    public abstract void cut();

    public abstract void paste();

    public abstract void delete();

    public void saveSheet()
    {
    }
}