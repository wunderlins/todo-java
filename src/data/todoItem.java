package data;

import java.util.Date;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author wus
 *
 */
@DatabaseTable(tableName = "todoItem")
class todoItem extends Commitable {
	/**
	 * Primary key. 
	 * <p>
	 * -1 means the item has not yet been commited. 
	 */
    @DatabaseField(generatedId = true, useGetSet = true)
	Integer id;
	
	/**
	 * name of the item 
	 */
    @DatabaseField(useGetSet = true)
	String name;
	
	/**
	 * Larger description, may contain HTML code
	 */
    @DatabaseField(useGetSet = true)
	String description;
	
	/**
	 * The parent's node id. -1 is root 
	 */
    @DatabaseField(useGetSet = true)
	Integer parent;
	
	/**
	 *	list of children 
	 */
    @ForeignCollectionField(eager = false) // do lazy loading
    ForeignCollection<Child> children;
	
	/**
	 * percent: 0-100
	 */
    @DatabaseField(useGetSet = true)
	int progress = 0; 
	
	/**
	 * Start date of the Task
	 */
    @DatabaseField(useGetSet = true)
	Date startDate = null;
	
	/**
	 * End Date of the Task
	 */
    @DatabaseField(useGetSet = true)
	Date endDate = null;
	
	// constructors
	public todoItem() {super();}
	
	public todoItem(int id) {
		super();
		this.id = id;
	}
	
	public todoItem(String name) {
		super();
		this.name = name;
	}

	// setters & getters
	public Integer getId() {
		return id;
	}

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

	public Integer getParent() {
		return parent;
	}

	public void setParent(Integer parentId) {
		this.parent = parentId;
		dirty = true;
	}
	
	/*
	public void setParent(todoItem item) {
		this.parent = item.getId();
		dirty = true;
	}
	*/

	public ForeignCollection<Child> getChildIds() {
		return children;
	}

	public void setChildIds(ForeignCollection<Child> items) {
		this.children = items;
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

	@Override
	public String toString() {
		return String.format("Item [{%s}, name=%s, dirty=%s]", id, name, dirty);
	}

}
