package soufix.fight.ia.type;

import soufix.fight.Fight;
import soufix.fight.Fighter;
import soufix.fight.ia.AbstractNeedSpell;
import soufix.fight.ia.util.Function;
import soufix.fight.spells.Spell.SortStats;
import soufix.main.Config;

/**
 * Created by Locos on 04/10/2015.
 */
public class IA79 extends AbstractNeedSpell
{
  public IA79(Fight fight, Fighter fighter, byte count)
  {
    super(fight,fighter,count);
  }

  @Override
  public void apply()
  {
    if(!this.stop&&this.fighter.canPlay()&&this.count>0)
    {
      int PM=(int) this.fighter.getCurPm(this.fight),time=100,maxPo=1;
      boolean action=false;

      for(SortStats spellStats : this.highests)
        if(spellStats!=null&&spellStats.getMaxPO()>maxPo)
          maxPo=spellStats.getMaxPO();

      if(this.fighter.getCurPa(this.fight)>0&&!action)
      {
        if(Function.getInstance().invocIfPossible(this.fight,this.fighter,this.invocations))
        {
          time=600;
          action=true;
        }
      }

      if(this.fighter.getCurPa(this.fight)>0&&!action)
      {
        if(Function.getInstance().buffIfPossible(this.fight,this.fighter,this.fighter,this.buffs))
        {
          time=400;
          action=true;
        }
      }

      if(PM>0&&!action)
      {
        int num=Function.getInstance().moveFarIfPossible(this.fight,this.fighter);
        if(num!=0)
          time=num;
      }

      if(this.fighter.getCurPa(this.fight)==0&&this.fighter.getCurPm(this.fight)==0)
        this.stop=true;

      addNext(this::decrementCount,time+Config.getInstance().AIDelay);
    } else
    {
      this.stop=true;
    }
  }
}