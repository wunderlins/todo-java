package data;

import java.util.ArrayList;
import java.util.Date;

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
	Integer id = -1;
	
	/**
	 * name of the item 
	 */
	String name = "";
	
	/**
	 * Larger description, may contain HTML code
	 */
	String description = "";
	
	/**
	 * The parent's node id. -1 is root 
	 */
	Integer parentId = -1;
	
	/**
	 *	list of children 
	 */
	ArrayList<Integer> childIds = new ArrayList<Integer>();
	
	/**
	 * percent: 0-100
	 */
	int progress = 0; 
	
	/**
	 * Start date of the Task
	 */
	Date startDate = null;
	
	/**
	 * End Date of the Task
	 */
	Date endDate = null;
	
	/**
	 * list of person Ids who are responsible for this Task
	 */
	ArrayList<Integer> responsible = new ArrayList<Integer>();
	
	/**
	 * List of Person IDs who are accountable for this task
	 */
	ArrayList<Integer> accountable = new ArrayList<Integer>();
	
	/**
	 * people to inform about the task
	 */
	ArrayList<Integer> informed = new ArrayList<Integer>();
	
	/**
	 * List of person IDs which need to be consulted 
	 */
	ArrayList<Integer> consulted = new ArrayList<Integer>();
	
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

	public ArrayList<Integer> getChildIds() {
		return childIds;
	}

	public void setChildIds(ArrayList<Integer> childIds) {
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

	public ArrayList<Integer> getResponsible() {
		return responsible;
	}

	public void setResponsible(ArrayList<Integer> responsible) {
		this.responsible = responsible;
		dirty = true;
	}

	public ArrayList<Integer> getAccountable() {
		return accountable;
	}

	public void setAccountable(ArrayList<Integer> accountable) {
		this.accountable = accountable;
		dirty = true;
	}

	public ArrayList<Integer> getInformed() {
		return informed;
	}

	public void setInformed(ArrayList<Integer> informed) {
		this.informed = informed;
		dirty = true;
	}

	public ArrayList<Integer> getConsulted() {
		return consulted;
	}

	public void setConsulted(ArrayList<Integer> consulted) {
		this.consulted = consulted;
		dirty = true;
	}
	
	// add methods
	public ArrayList<Integer> addChildIds(int id) {
		this.childIds.add(id);
		dirty = true;
		return this.childIds;
	}

	public ArrayList<Integer> addResponsible(int id) {
		this.responsible.add(id);
		dirty = true;
		return this.responsible;
	}

	public ArrayList<Integer> addAccountable(int id) {
		this.accountable.add(id);
		dirty = true;
		return this.accountable;
	}

	public ArrayList<Integer> addInformed(int id) {
		this.informed.add(id);
		dirty = true;
		return this.informed;
	}

	public ArrayList<Integer> addConsulted(int id) {
		this.consulted.add(id);
		dirty = true;
		return this.consulted;
	}

	@Override
	public String toString() {
		return String.format("Item [{%s}, name=%s, responsible=%s, dirty=%s]", id, name, responsible, dirty);
	}

}
