package data;

abstract class Commitable {
	public Boolean dirty = true;
	public Commitable() {}
	public Boolean commit() {
		dirty = false;
		return true;
	}
}
