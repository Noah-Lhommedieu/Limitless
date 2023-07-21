package soufix.client.other;

import soufix.client.Player;
import soufix.main.Constant;

import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;

public class Stats
{
  private Map<Long, Long> effects=new HashMap<Long, Long>();

  public Stats(boolean addBases, Player perso)
  {
    this.effects=new HashMap<Long, Long>();
    if(!addBases)
      return;
    this.effects.put(Constant.STATS_ADD_PA, (long)12);
    this.effects.put(Constant.STATS_ADD_PM, (long)6);
    this.effects.put(Constant.STATS_ADD_PROS,perso.getClasse()==Constant.CLASS_ENUTROF ? (long)120 : 100);
    this.effects.put(Constant.STATS_ADD_PODS,(long)20000);
    this.effects.put(Constant.STATS_ADD_SUM,perso.getClasse()==Constant.CLASS_OSAMODAS ? (long)25 : 15);
    this.effects.put(Constant.STATS_ADD_INIT,(long)1);
  }

  public Stats(Map<Long, Long> stats, boolean addBases, Player perso)
  {
    this.effects=stats;
    if(!addBases)
      return;
    this.effects.put(Constant.STATS_ADD_PA, (long)12);
    this.effects.put(Constant.STATS_ADD_PM, (long)6);
    this.effects.put(Constant.STATS_ADD_PROS,perso.getClasse()==Constant.CLASS_ENUTROF ? (long)120 : 100);
    this.effects.put(Constant.STATS_ADD_PODS,(long)20000);
    this.effects.put(Constant.STATS_ADD_SUM,perso.getClasse()==Constant.CLASS_OSAMODAS ? (long)25 : 15);
    this.effects.put(Constant.STATS_ADD_INIT,(long)1);
  }

  public Stats(boolean a)
  {
    this.effects.put(Constant.STATS_ADD_VITA,(long)0);
    this.effects.put(Constant.STATS_ADD_SAGE,(long)0);
    this.effects.put(Constant.STATS_ADD_INTE,(long)0);
    this.effects.put(Constant.STATS_ADD_FORC,(long)0);
    this.effects.put(Constant.STATS_ADD_CHAN,(long)0);
    this.effects.put(Constant.STATS_ADD_AGIL,(long)0);
  }

  public Stats(Map<Long, Long> stats)
  {
    this.effects=stats;
  }

  public Stats()
  {
    this.effects=new HashMap<Long, Long>();
  }

  public static Stats cumulStat(Stats s1, Stats s2, Player perso)
  {
    HashMap<Long, Long> effets=new HashMap<Long, Long>();
    for(long a=0;a<=Constant.MAX_EFFECTS_ID;a++)
    {
      if(s1.effects.get(a)==null&&s2.effects.get(a)==null)
        continue;

      long som=0;
      if(s1.effects.get(a)!=null)
        som+=s1.effects.get(a);
      if(s2.effects.get(a)!=null)
        som+=s2.effects.get(a);
      if(perso != null)
      if(perso.getFight() != null)
      if(perso.getFight().getType() == Constant.FIGHT_TYPE_AGRESSION || 
      perso.getFight().getType() == Constant.FIGHT_TYPE_KOLI || 
      perso.getFight().getType() == Constant.FIGHT_TYPE_PVT || 
      perso.getFight().getType() == Constant.FIGHT_TYPE_CONQUETE) {
          if(a==Constant.STATS_ADD_PA && som>16)som=16;
  		if(a==Constant.STATS_ADD_PM && som>8)som=8;
      }
    	  		if(a==Constant.STATS_ADD_RP_TER && som>50)som=50;
    	  		if(a==Constant.STATS_ADD_RP_EAU && som>50)som=50;
    	  		if(a==Constant.STATS_ADD_RP_AIR && som>50)som=50;
    	  		if(a==Constant.STATS_ADD_RP_FEU && som>50)som=50;
    	  		if(a==Constant.STATS_ADD_RP_NEU && som>50)som=50;
      effets.put(a,som);
    }
    return new Stats(effets,false,null);
  }
  public static Stats cumulStatfight2(Stats s1, Stats s2, Player perso)
  {
    HashMap<Long, Long> effets=new HashMap<Long, Long>();
    for(long a=0;a<=Constant.MAX_EFFECTS_ID;a++)
    {
      if(s1.effects.get(a)==null&&s2.effects.get(a)==null)
        continue;

      long som=0;
      if(s1.effects.get(a)!=null)
        som+=s1.effects.get(a);
      if(s2.effects.get(a)!=null)
        som+=s2.effects.get(a);
      effets.put(a,som);
    }
    return new Stats(effets,false,null);
  }

  public static Stats cumulStatFight(Stats s1, Stats s2)
  {
    HashMap<Long, Long> effets=new HashMap<Long, Long>();
    for(long a=0;a<=Constant.MAX_EFFECTS_ID;a++)
    {
      if((s1.effects.get(a)==null||s1.effects.get(a)==0)&&(s2.effects.get(a)==null||s2.effects.get(a)==0))
        continue;
      long som=0;
      if(s1.effects.get(a)!=null)
        som+=s1.effects.get(a);
      if(s2.effects.get(a)!=null)
        som+=s2.effects.get(a);
      effets.put(a,som);
    }
    return new Stats(effets,false,null);
  }

  public Map<Long, Long> getMap()
  {
    return this.effects;
  }

  public long addOneStat(long id, long l)
  {
    if(this.effects.get(id)==null||this.effects.get(id)==0)
    {
      this.effects.put((long)id, l);
    } else
    {
      long newVal=(this.effects.get(id)+l);
      if(newVal<=0)
      {
        this.effects.remove(id);
        return 0;
      } else
        this.effects.put((long)id, newVal);
    }
    return this.effects.get(id);
  }

  public boolean isSameStats(Stats other)
  {
    for(Entry<Long, Long> entry : this.effects.entrySet())
    {
      //Si la stat n'existe pas dans l'autre map
      if(other.getMap().get(entry.getKey())==null)
        return false;
      //Si la stat existe mais n'a pas la m�me valeur
      if(other.getMap().get(entry.getKey()).compareTo(entry.getValue())!=0)
        return false;
    }
    for(Entry<Long, Long> entry : other.getMap().entrySet())
    {
      //Si la stat n'existe pas dans l'autre map
      if(this.effects.get(entry.getKey())==null)
        return false;
      //Si la stat existe mais n'a pas la m�me valeur
      if(this.effects.get(entry.getKey()).compareTo(entry.getValue())!=0)
        return false;
    }
    return true;
  }

  public String parseToItemSetStats()
  {
    StringBuilder str=new StringBuilder();
    if(this.effects.isEmpty())
      return "";
    for(Entry<Long, Long> entry : this.effects.entrySet())
    {
      if(str.length()>0)
        str.append(",");
      str.append(Long.toHexString(entry.getKey())).append("#").append(Long.toHexString(entry.getValue())).append("#0#0");
    }
    return str.toString();
  }

  public long get(long statsAddSage)
  {
    return this.effects.get(statsAddSage)==null ? 0 : this.effects.get(statsAddSage);
  }

  public long getEffect(long statsAddSage)
  {
    long val;
    if(this.effects.get(statsAddSage)==null)
      val=0;
    else
      val=this.effects.get(statsAddSage);

    switch((int)statsAddSage)
    {
      case (int) Constant.STATS_ADD_PA:
        if(this.effects.get((long)Constant.STATS_ADD_PA2)!=null)
          val+=this.effects.get((long)Constant.STATS_ADD_PA2);
        if(this.effects.get((long)Constant.STATS_REM_PA)!=null)
          val-=this.effects.get((long)Constant.STATS_REM_PA);
        if(this.effects.get((long)Constant.STATS_REM_PA2)!=null) //Non esquivable
          val-=this.effects.get((long)Constant.STATS_REM_PA2);
        break;
      case (int) Constant.STATS_ADD_PM:
          if(this.effects.get((long)Constant.STATS_ADD_PM2)!=null)
            val+=this.effects.get((long)Constant.STATS_ADD_PM2);
          if(this.effects.get((long)Constant.STATS_REM_PM)!=null)
            val-=this.effects.get((long)Constant.STATS_REM_PM);
          if(this.effects.get((long)Constant.STATS_REM_PM2)!=null)//Non esquivable
            val-=this.effects.get((long)Constant.STATS_REM_PM2);
          break;
        case (int) Constant.STATS_ADD_PO:
          if(this.effects.get((long)Constant.STATS_REM_PO)!=null)
            val-=this.effects.get((long)Constant.STATS_REM_PO);
          break;
        case (int) Constant.STATS_ADD_SUM:
          if(this.effects.get((long)Constant.STATS_REM_SUM)!=null)
            val-=this.effects.get((long)Constant.STATS_REM_SUM);
          break;
        case (int) Constant.STATS_ADD_DOMA:
          if(this.effects.get((long)Constant.STATS_REM_DOMA)!=null)
            val-=this.effects.get((long)Constant.STATS_REM_DOMA);
          break;
        case Constant.STATS_ADD_CC:
          if(this.effects.get((long)Constant.STATS_REM_CC)!=null)
            val-=this.effects.get((long)Constant.STATS_REM_CC);
          break;
        case Constant.STATS_ADD_SOIN:
          if(this.effects.get((long)Constant.STATS_REM_SOIN)!=null)
            val-=this.effects.get((long)Constant.STATS_REM_SOIN);
          break;
        case Constant.STATS_ADD_RP_TER:
          if(this.effects.get((long)Constant.STATS_REM_RP_TER)!=null)
            val-=this.effects.get((long)Constant.STATS_REM_RP_TER);
          break;
        case Constant.STATS_ADD_RP_EAU:
          if(this.effects.get((long)Constant.STATS_REM_RP_EAU)!=null)
            val-=this.effects.get((long)Constant.STATS_REM_RP_EAU);
          break;
        case Constant.STATS_ADD_RP_AIR:
          if(this.effects.get((long)Constant.STATS_REM_RP_AIR)!=null)
            val-=this.effects.get((long)Constant.STATS_REM_RP_AIR);
          break;
        case Constant.STATS_ADD_RP_FEU:
          if(this.effects.get((long)Constant.STATS_REM_RP_FEU)!=null)
            val-=this.effects.get((long)Constant.STATS_REM_RP_FEU);
          break;
        case Constant.STATS_ADD_RP_NEU:
          if(this.effects.get((long)Constant.STATS_REM_RP_NEU)!=null)
            val-=this.effects.get((long)Constant.STATS_REM_RP_NEU);
          break;
        case (int) Constant.STATS_ADD_PROS:
          if(this.effects.get((long)Constant.STATS_REM_PROS)!=null)
            val-=this.effects.get((long)Constant.STATS_REM_PROS);
          break;
        case (int) Constant.STATS_ADD_SAGE:
          if(this.effects.get((long)Constant.STATS_REM_SAGE)!=null)
            val-=this.effects.get((long)Constant.STATS_REM_SAGE);
          break;
        case Constant.STATS_ADD_R_FEU:
          if(this.effects.get((long)Constant.STATS_REM_R_FEU)!=null)
            val-=this.effects.get((long)Constant.STATS_REM_R_FEU);
          break;
        case Constant.STATS_ADD_R_NEU:
          if(this.effects.get((long)Constant.STATS_REM_R_NEU)!=null)
            val-=this.effects.get((long)Constant.STATS_REM_R_NEU);
          break;
        case Constant.STATS_ADD_R_TER:
          if(this.effects.get((long)Constant.STATS_REM_R_TER)!=null)
            val-=this.effects.get((long)Constant.STATS_REM_R_TER);
          break;
        case Constant.STATS_ADD_R_EAU:
          if(this.effects.get((long)Constant.STATS_REM_R_EAU)!=null)
            val-=this.effects.get((long)Constant.STATS_REM_R_EAU);
          break;
        case Constant.STATS_ADD_R_AIR:
          if(this.effects.get((long)Constant.STATS_REM_R_AIR)!=null)
            val-=this.effects.get((long)Constant.STATS_REM_R_AIR);
          break;
        case Constant.STATS_ADD_PERDOM:
          if(this.effects.get((long)Constant.STATS_REM_PERDOM)!=null)
            val-=this.effects.get((long)Constant.STATS_REM_PERDOM);
          break;
        case (int) Constant.STATS_ADD_FORC:
          if(this.effects.get((long)Constant.STATS_REM_FORC)!=null)
            val-=this.effects.get((long)Constant.STATS_REM_FORC);
          break;
        case (int) Constant.STATS_ADD_INTE:
          if(this.effects.get((long)Constant.STATS_REM_INTE)!=null)
            val-=this.effects.get((long)Constant.STATS_REM_INTE);
          break;
        case (int) Constant.STATS_ADD_CHAN:
          if(this.effects.get((long)Constant.STATS_REM_CHAN)!=null)
            val-=this.effects.get((long)Constant.STATS_REM_CHAN);
          break;
        case (int) Constant.STATS_ADD_AGIL:
          if(this.effects.get((long)Constant.STATS_REM_AGIL)!=null)
            val-=this.effects.get((long)Constant.STATS_REM_AGIL);
          break;
        case (int) Constant.STATS_ADD_PODS:
          if(this.effects.get((long)Constant.STATS_REM_PODS)!=null)
            val-=this.effects.get((long)Constant.STATS_REM_PODS);
          break;
        case (int) Constant.STATS_ADD_VITA:
          if(this.effects.get((long)Constant.STATS_REM_VITA)!=null)
            val-=this.effects.get((long)Constant.STATS_REM_VITA);
          break;
        case (int) Constant.STATS_ADD_VIE:
          val=Constant.STATS_ADD_VIE;
          break;
        case (int) Constant.STATS_ADD_INIT:
          if(this.effects.get((long)Constant.STATS_REM_INIT)!=null)
            val-=this.effects.get((long)Constant.STATS_REM_INIT);
          break;
          
        case Constant.STATS_ADD_AFLEE:
          if(this.effects.get((long)Constant.STATS_REM_AFLEE)!=null)
            val-=getEffect((long)Constant.STATS_REM_AFLEE);
          if(this.effects.get((long)Constant.STATS_ADD_SAGE)!=null)
            val+=getEffect((long)Constant.STATS_ADD_SAGE)/4;
          break;
        case Constant.STATS_ADD_MFLEE:
          if(this.effects.get((long)Constant.STATS_REM_MFLEE)!=null)
            val-=getEffect((long)Constant.STATS_REM_MFLEE);
          if(this.effects.get((long)Constant.STATS_ADD_SAGE)!=null)
            val+=getEffect((long)Constant.STATS_ADD_SAGE)/4;
          break;
        case Constant.STATS_ADD_MAITRISE:
          if(this.effects.get((long)Constant.STATS_ADD_MAITRISE)!=null)
            val=this.effects.get((long)Constant.STATS_ADD_MAITRISE);
        break;
    }
    return val;
  }

}
