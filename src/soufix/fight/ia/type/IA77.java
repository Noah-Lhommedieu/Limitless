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
public class IA77 extends AbstractNeedSpell
{

  private int attack=0;
  private boolean boost=false;

  public IA77(Fight fight, Fighter fighter, byte count)
  {
    super(fight,fighter,count);
  }

  @Override
  public void apply()
  {
    if(!this.stop&&this.fighter.canPlay()&&this.count>0)
    {
      Fighter ennemy=Function.getInstance().getNearestEnnemy(this.fight,this.fighter);
      int PA=(int) this.fighter.getCurPa(this.fight),
          PM=(int) this.fighter.getCurPm(this.fight),time=100,maxPo=1;
      boolean action=false;

      if(this.fighter.getMob().getPa()<PA)
        this.boost=true;

      for(SortStats spellStats : this.highests)
        if(spellStats!=null&&spellStats.getMaxPO()>maxPo)
          maxPo=spellStats.getMaxPO();

      Fighter target=Function.getInstance().getNearestEnnemynbrcasemax(this.fight,this.fighter,0,3);

      if(target!=null)
        if(target.isHide())
          target=null;

      PA=(int) this.fighter.getCurPa(this.fight);
      PM=(int) this.fighter.getCurPm(this.fight);

      if(this.fighter.getCurPa(this.fight)>0&&!action)
      {
        if(Function.getInstance().buffIfPossible(this.fight,this.fighter,this.fighter,this.buffs))
        {
          time=400;
          action=true;
        }
      }
      
      if(PM>0&&target==null&&this.attack==0||PM>0&&target==null&&this.attack==1&&this.boost)
      {
        int num=Function.getInstance().moveautourIfPossible(this.fight,this.fighter,ennemy);
        if(num!=0)
        {
          time=num;
          action=true;
          target=Function.getInstance().getNearestEnnemynbrcasemax(this.fight,this.fighter,0,3);
        }
      }

      if(PA>0&&target!=null&&!action)
      {
        int beforeAP=(int) this.fighter.getCurPa(this.fight);
        int num=Function.getInstance().attackIfPossible(this.fight,this.fighter,this.cacs);
        int afterAP=(int) this.fighter.getCurPa(this.fight);
        if(beforeAP>afterAP)
        {
          time=num;
          action=true;
          this.attack++;
        }
      }

      if(PM>0&&!action&&this.attack>0)
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