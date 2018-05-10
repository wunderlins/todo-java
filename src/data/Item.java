package data;

import java.util.Date;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

/**
 * @author wus
 *
 */
abstract class Item extends Commitable {
	/**
	 * Primary key. 
	 * <p>
	 * -1 means the item has not yet been commited. 
	 */
    @DatabaseField(generatedId = true)
	Integer id;
	
	/**
	 * name of the item 
	 */
    @DatabaseField
	String name;
	
	/**
	 * Larger description, may contain HTML code
	 */
    @DatabaseField
	String description;
	
	/**
	 * The parent's node id. -1 is root 
	 */
    @DatabaseField
	Integer parentId;
	
	/**
	 *	list of children 
	 */
    @ForeignCollectionField(eager = false) // do lazy loading
    ForeignCollection<Item> childIds;
	
	/**
	 * percent: 0-100
	 */
    @DatabaseField
	int progress = 0; 
	
	/**
	 * Start date of the Task
	 */
    @DatabaseField
	Date startDate = null;
	
	/**
	 * End Date of the Task
	 */
    @DatabaseField
	Date endDate = null;
	
	/**
	 * list of person Ids who are responsible for this Task
	 */
    @ForeignCollectionField(eager = false) // do lazy loading
    ForeignCollection<Person> responsible;
	
	/**
	 * List of Person IDs who are accountable for this task
	 */
    @ForeignCollectionField(eager = false) // do lazy loading
    ForeignCollection<Person> accountable;
	
	/**
	 * people to inform about the task
	 */
    @ForeignCollectionField(eager = false) // do lazy loading
    ForeignCollection<Person> informed;
	
	/**
	 * List of person IDs which need to be consulted 
	 */
    @ForeignCollectionField(eager = false) // do lazy loading
    ForeignCollection<Person> consulted;
	
	// constructors
	public Item() {super();}
	
	public Item(int id) {
		super();
		this.id = id;
	}
	
	public Item(String name) {
		super();
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	// setters & getters
	public void setId(Integer id) {
		this.id = id;
		dirty = true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		dirty = true;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		dirty = true;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
		dirty = true;
	}

	public ForeignCollection<Item> getChildIds() {
		return childIds;
	}

	public void setChildIds(ForeignCollection<Item> childIds) {
		this.childIds = childIds;
		dirty = true;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
		dirty = true;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
		dirty = true;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
		dirty = true;
	}

	public ForeignCollection<Person> getResponsible() {
		return responsible;
	}

	public void setResponsible(ForeignCollection<Person> responsible) {
		this.responsible = responsible;
		dirty = true;
	}

	public ForeignCollection<Person> getAccountable() {
		return accountable;
	}

	public void setAccountable(ForeignCollection<Person> accountable) {
		this.accountable = accountable;
		dirty = true;
	}

	public ForeignCollection<Person> getInformed() {
		return informed;
	}

	public void setInformed(ForeignCollection<Person> informed) {
		this.informed = informed;
		dirty = true;
	}

	public ForeignCollection<Person> getConsulted() {
		return consulted;
	}

	public void setConsulted(ForeignCollection<Person> consulted) {
		this.consulted = consulted;
		dirty = true;
	}
	
	// add methods
	/*
	public ForeignCollection<Person> addChildIds(int id) {
		this.childIds.add(id);
		dirty = true;
		return this.childIds;
	}

	public ForeignCollection<Person> addResponsible(int id) {
		this.responsible.add(id);
		dirty = true;
		return this.responsible;
	}

	public ForeignCollection<Person> addAccountable(int id) {
		this.accountable.add(id);
		dirty = true;
		return this.accountable;
	}

	public ForeignCollection<Person> addInformed(int id) {
		this.informed.add(id);
		dirty = true;
		return this.informed;
	}

	public ForeignCollection<Person> addConsulted(int id) {
		this.consulted.add(id);
		dirty = true;
		return this.consulted;
	}
	*/

	@Override
	public String toString() {
		return String.format("Item [{%s}, name=%s, responsible=%s, dirty=%s]", id, name, responsible, dirty);
	}

}
