package data;

abstract class Person extends Commitable {
	
	protected int id = -1;
	protected String name = "";
	protected String firstname = "";
	protected String nick = "";
	protected String email = null;
	
	// constructors
	
	public Person() {super();}
	
	public Person(String name, String firstname) {
		super();
		this.name      = name;
		this.firstname = firstname;
	}
	
	public Person(String name, String firstname, String email) {
		super();
		this.name      = name;
		this.firstname = firstname;
		this.email     = email;
	}
	
	@Override
	public String toString() {
		return String.format("Person [{%s}, %s %s, <%s>, dirty=%s]", 
				id, name, firstname, email, dirty);
	}

	// getters and setters. no setter for ID, this is handled by the storage backend
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
		dirty = true;
	}
	
	public String getFirstname() {
		return firstname;
	}
	
	public void setFirstname(String firstname) {
		this.firstname = firstname;
		dirty = true;
	}
	
	public String getNick() {
		return nick;
	}
	
	public void setNick(String nick) {
		this.nick = nick;
		dirty = true;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
		dirty = true;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
		dirty = true;
	}
}
