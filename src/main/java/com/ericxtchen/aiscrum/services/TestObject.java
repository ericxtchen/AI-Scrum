package com.ericxtchen.aiscrum.services;

public class TestObject {
    private String jql;
    private String[] fields;
    private int maxResults;

    public TestObject(String jql, String[] fields, int maxResults) {
        this.jql = jql;
        this.fields = fields;
        this.maxResults = maxResults;
    }

    public String getJql() {
        return jql;
    }
    public void setJql(String jql) {
        this.jql = jql;
    }
    public String[] getFields() {
        return fields;
    }
    public void setFields(String[] fields) {
        this.fields = fields;
    }
    public int getMaxResults() {
        return maxResults;
    }
    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }
}
