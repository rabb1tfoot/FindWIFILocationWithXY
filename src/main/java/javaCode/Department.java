package javaCode;
import java.util.Arrays;

public class Department {
	 private int id;
	 private String name;
	 private User[] users;
	 
	  public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User[] getUsers() {
		return users;
	}

	public void setUsers(User[] users) {
		this.users = users;
	}
	 
	  //Getters and Setters
	 
	  public String toString() {
	    return "Department [id=" + id + ", name=" + name + ", users=" + Arrays.toString(users) + "]";
	  }
}
