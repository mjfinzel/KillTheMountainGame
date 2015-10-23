package Game;

public class Entity {
	//stat related
	double baseHealth;
	double baseMana;

	double currentHealth;
	double currentMana;
	double currentStrength;
	double currentEvasion;
	double currentAccuracy;
	double intelligence;
	double magic;
	double armor;
	double luck;

	double maxHealth;
	double maxMana;
	double maxStrength;
	double maxEvasion;
	double maxAccuracy;
	double maxSpeed;
	double maxMagic;

	double minStrength;
	double minSpeed;

	double magicResistance;
	double physicalResistance;
	double fireResistance;
	double iceResistance;

	boolean isAlive = true;
	boolean armorOn;

	//position related
	double xpos;
	double ypos;
	double movementSpeed;

	public Entity(){

	}

	public double getBaseHealth() {
		return baseHealth;
	}

	public double getHealth() {
		return currentHealth;
	}

	public double getMaxHealth() {
		//loop through inventory and add how they effect maxhealth
		return maxHealth;
	}

	public double getBaseMana() {
		return baseMana;
	}

	public double getMana () {
		return currentMana;
	}

	public double getMaxMana() { 
		return maxMana;
	}

	public double getStrength() {
		return currentStrength;
	}

	public double getMaxStrength() {
		return maxStrength;
	}

	public double getEvasion() {
		return currentEvasion;
	}

	public double getMaxEvasion() {
		return maxEvasion;
	}

	public double getAccuracy() {
		return currentAccuracy;
	}

	public double getMaxAccuracy() {
		return maxAccuracy;
	}

	public double getMagic() {
		return magic;
	}

	public double getMaxMagic() {
		return maxMagic;
	}

	public double getSpeed() {
		return movementSpeed;
	}

	public double getMaxSpeed() {
		return maxSpeed;
	}

	public double getIntelligence() {
		return intelligence;
	}
	
	/*
	 * Decreases health and possibly kills player dependent on armor and currentHealth
	 * @param double damage amount
	 */
	public void takeDamage(double damage) {
		if (armorOn) {
			currentHealth-=damage/2; // will change to damage/armorResistance
		}

		if (currentHealth - damage <= 0) {
			currentHealth = 0;
			isAlive = false;
		}

		else {
			currentHealth-=damage;
		}

	}

	/*
	 * Adds health to currenthealth; can't go over max.
	 * @param double amount of health added
	 */
	public void healthUp(double amount) {

		if (currentHealth+amount > maxHealth) {
			currentHealth = maxHealth;
		}
		else {
			currentHealth+=amount;
		}
	}

	/*
	 * Sets health to a certain amount
	 * @param double amount health to be set to
	 */
	public void setHealth(double amount) {
		if (amount >= maxHealth) {
			currentHealth = maxHealth;
		}
		if (amount <= 0) {
			currentHealth = 0;
			isAlive = false;
		}
		else {
			currentHealth = amount;
		}
	}

	/*
	 * Decreases or increases currentMana; can't go over max.
	 * @param double amount of mana (positive or negative)
	 */
	public void manaChange(double amount) {
		if (currentMana + amount >= maxMana) {
			currentMana = maxMana;
		}
		else if (currentMana + amount <= 0) {
			currentMana = 0;
		}
		else {
			currentMana+=amount;
		}
	}

	/*
	 * Changes the movementSpeed of the player on the map; can't go over max.
	 * @param double amount change, positive or negative
	 */
	public void speedChange(double amount) {

		if (movementSpeed*amount > maxSpeed) {
			movementSpeed = maxSpeed;
		}
		else if (movementSpeed*amount < minSpeed) {
			movementSpeed = minSpeed;
		}
		else{
			movementSpeed*=amount;
		}

	}

	/*
	 * Changes the currentStrength of the player; can't go over max.
	 * @param double amount change, positive or negative
	 */
	public void strengthChange(double amount) {

		if (currentStrength+amount > maxStrength) {
			currentStrength = maxStrength;
		}
		else if (currentStrength+amount < minStrength) {
			currentStrength = minStrength;
		}
		else {
			currentStrength+=amount;
		}

	}

	/*
	 * Sets strength to a certain amount; can't go over max.
	 * @param double amount health to be set to
	 */
	public void setStrength(double amount) {
		if (amount >= maxStrength) {
			currentStrength = amount;
		}

		if (amount <= minStrength) {
			currentStrength = minStrength;
		}

		else {
			currentStrength = amount;
		}
	}

	/*
	 * Sets evasion to a certain amount; can't go over max.
	 * @param double amount evasion to be set to
	 */
	public void setEvasion(double amount) {
		if (amount >= maxEvasion) {
			currentEvasion = amount;
		}

		if (amount <= 0) {
			currentEvasion = 0;
		}
		else {
			currentEvasion = amount;
		}

	}

	/*
	 * Sets mana to a certain amount; can't go over max.
	 * @param double amount mana to be set to
	 */
	public void setMana(double mana) {
		if (mana >= maxMana) {
			currentMana = maxMana;
		}

		if (mana <= 0) {
			mana = 0;
		}

		else {
			currentMana = mana;
		}

	}

	public void setMaxHealth(double amount) {
		maxHealth = amount;
	}

	public void setMaxMana(double amount) {
		maxMana = amount;
	}

	public void setMaxStrength(double amount) {
		maxStrength = amount;
	}

	public void setMaxEvasion(double amount){
		maxEvasion = amount;
	}

	public void setMaxAccuracy(double amount) {
		maxAccuracy = amount;
	}

	public void setMaxSpeed(double amount) {
		maxSpeed = amount;
	}

	public void setMaxMagic(double amount) {
		maxMagic = amount;
	}

}
