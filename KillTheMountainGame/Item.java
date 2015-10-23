package Game;

public class Item {
	
	String name = "";
	boolean onGround;
	int attack;
	double speed;
	int mana;
	int health;
	int armor;
	int xID;
	int yID;
	
	public Item(String name, boolean onGround) {
		
		this.name = name;
		this.onGround = onGround;
		
	}
}
