package soufix.other;

import java.util.HashMap;
import java.util.Map;

import soufix.fight.Fighter;

public class OverPower {

	private int opLevel;
	private Map<Integer, Double> pvDifficulty; // Map pour stocker les facteurs de multiplication pour les PV
    private Map<Integer, Double> damageDifficulty; // Map pour stocker les facteurs de multiplication pour les dégâts
    private Map<Integer, Double> xpDifficulty; // Map pour stocker les facteurs de multiplication pour l'xp
    
    public OverPower() {
        pvDifficulty = new HashMap<>();
        damageDifficulty = new HashMap<>();
        xpDifficulty= new HashMap<>();
        
        // Initialisation des valeurs de difficulté pour les PV
        pvDifficulty.put(1, 2.0); // Key = OpLevel d'OverPower
        pvDifficulty.put(2, 4.0); //Value = multiplication de Difficulté
        pvDifficulty.put(3, 6.0);
        pvDifficulty.put(4, 8.0);
        pvDifficulty.put(5, 10.0);
        pvDifficulty.put(6, 12.0);
        pvDifficulty.put(7, 14.0);
        pvDifficulty.put(8, 18.0);
        pvDifficulty.put(9, 24.0);
        pvDifficulty.put(10, 30.0);
        
        // Initialisation des valeurs de difficulté pour les dégâts
        damageDifficulty.put(1, 1.2); //Pareil ici mais pour les dégâts
        damageDifficulty.put(2, 1.4);
        damageDifficulty.put(3, 1.6);
        damageDifficulty.put(4, 1.8);
        damageDifficulty.put(5, 2.0);
        damageDifficulty.put(6, 2.4);
        damageDifficulty.put(7, 2.8);
        damageDifficulty.put(8, 3.4);
        damageDifficulty.put(9, 5.0);
        damageDifficulty.put(10, 8.0);
        
        // Initialisation des valeurs de difficulté pour l'xp
        xpDifficulty.put(1, 1.8); //Pareil ici mais pour l'xp
        xpDifficulty.put(2, 3.0);
        xpDifficulty.put(3, 7.0);
        xpDifficulty.put(4, 12.0);
        xpDifficulty.put(5, 25.0);
        xpDifficulty.put(6, 35.8);
        xpDifficulty.put(7, 45.8);
        xpDifficulty.put(8, 55.0);
        xpDifficulty.put(9, 70.0);
        xpDifficulty.put(10, 100.0);
	}
    
	public int getOpLevel()
	{
		return this.opLevel;
	}
	
	public void setOpLevel(int opLevel)
	{
		this.opLevel = opLevel;
	}
	
	public Map<Integer, Double> getPvDifficulty()
    {
    	return pvDifficulty;
    }
	
    public void setPvDifficulty(Map<Integer, Double> pvDifficulty)
    {
    	this.pvDifficulty = pvDifficulty;
    }
    
    public Map<Integer, Double> getDamageDifficulty()
    {
    	return damageDifficulty;
    }
    
    public void setDamageDifficulty(Map<Integer, Double> damageDifficulty)
    {
    	this.damageDifficulty = damageDifficulty;
    }
    
    public Map<Integer, Double> getXpDifficulty()
    {
    	return damageDifficulty;
    }
    
    public void setXpDifficulty(Map<Integer, Double> xpDifficulty)
    {
    	this.xpDifficulty = xpDifficulty;
    }
	
	public void OverPowerDifficultyPV(Fighter f)
	{
		int op = this.getOpLevel();
		double multiplier = this.getPvDifficulty().get(op);

		f.setPdvMax(f.getPdvMaxOutFight() * multiplier);
		f.fullPdv();
	}
	
	public long OverPowerDifficultyDamage(long finaldamage)
	{
		int op = this.getOpLevel();
		finaldamage *= this.getDamageDifficulty().get(op);
		return finaldamage;
	}
	
	public double OverPowerDifficultyXP(double xp)
	{
		int op = this.getOpLevel();
		if(op >= 1 && op <= 10)
		xp *= this.getXpDifficulty().get(op);
		return xp;
	}
	
	public String InfoOpLvlDifficulty(int opLevel)
	{
		String res = "";
		if(opLevel != 0 && opLevel <= 10)
		{
			double pvMultiplier = pvDifficulty.get(opLevel);
            double damageMultiplier = damageDifficulty.get(opLevel);
            double xpMultiplier = xpDifficulty.get(opLevel);
            res = String.format("OP %d -> PV %.1fx / Dégâts %.1fx / XP %.1fx \n"
            		, opLevel, pvMultiplier, damageMultiplier, xpMultiplier);
		}
        return res;
	}
}
