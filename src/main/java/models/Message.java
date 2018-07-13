package models;

public class Message {

    private int id;
    private String value;
    
    public Message() {
    	
    }

    public Message(int id, String value) {
    	this.id = id;
    	this.value = value;
    }

    public int getId() {
    	return id;
    }
    
    public String getValue() {
    	return value;
    }
    
    @Override
    public String toString() {
        return String.format(
            "Message is: '%id', value: '%s'",
            id, value);
    }

}