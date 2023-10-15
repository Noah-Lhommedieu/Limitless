package soufix.other;

import java.util.ArrayList;
import java.util.List;

import soufix.client.Player;
import soufix.common.SocketManager;
import soufix.database.Database;
import soufix.game.World;
import soufix.main.Constant;
import soufix.object.GameObject;
import soufix.object.ObjectTemplate;

public class ItemEvolution {

	int prestigePlayer;
	
	
	double[] tabCoef;

    // Coefficient de base initial
    double coefficientDeBase = 1.01;

    List<ObjectTemplate> listItemEvo; 
    
    
    
	public ItemEvolution(Player perso)
	{
		prestigePlayer = perso.getPrestige();
		tabCoef = new double[100];
		listItemEvo = new ArrayList<ObjectTemplate>();
		
		
		for (int i = 0; i < 100; i++) 
		{
	    	tabCoef[i] = coefficientDeBase;
	        coefficientDeBase *= 1.13; // Multiplie le coefficient par 1.13 pour la prochaine itération
	    }
		
		listItemEvo.add(World.getObjTemplate(112));
		
		
		
	}
	
	/*public void ItemEvo(Player perso) 
	  {
			//R�cup�ration du facteur principal des futurs stats, les niveaux.
			int lvl = perso.getLevel();

			//Boucle pour modifi� unitairement les items sur la bonne liste de de GameObject.
			for (GameObject gameObject : getAllObjetByPos(perso)) 
			{
				//R�cup�ration des stats a modifier
				double force = gameObject.getStats().getEffect(Constant.STATS_ADD_FORC);
				double intel = gameObject.getStats().getEffect(Constant.STATS_ADD_INTE);
				double agi = gameObject.getStats().getEffect(Constant.STATS_ADD_AGIL);
				double chance = gameObject.getStats().getEffect(Constant.STATS_ADD_CHAN);
				
				
	;			
				int thisPrestige = perso.getPrestige();
				if(gameObject.getTemplate() == World.getObjTemplate(7114)) // Ciblage d'un item pr�cis, et donc am�lioration pr�cise 
				{
					gameObject.getStats().addOneStat(Constant.STATS_ADD_FORC, -force +  (tabCoef[thisPrestige] * lvl * thisPrestige));
				}
				else if (gameObject.getTemplate() == World.getObjTemplate(739))
				{
					gameObject.getStats().addOneStat(Constant.STATS_ADD_CHAN, -chance + (long) (1000*(lvl)));

				}
				else 
				{
					//Modification des stats a am�liorer par level (ici c'est le cas) et pour TOUS les items si ce n'est pas un item rechercher
					gameObject.getStats().addOneStat(Constant.STATS_ADD_AGIL, -agi + (long) (1000*(lvl)));
				}
				
				
				//Actualisation de la database des items modifi� de mani�re individuel
				Database.getDynamics().getObjectData().update(gameObject);			

			}
				//Refresh le perso pour voir et utiliser les modifications sans d�co reco
				SocketManager.GAME_SEND_ALTER_GM_PACKET(perso.curMap,perso);
				//SocketManager.GAME_SEND_ASK(this.getAccount().getGameClient(), this); / ask le client / bug le tableau score
				SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(perso.getCurMap(), perso.getId()); // delete this de la map
				SocketManager.GAME_SEND_ADD_PLAYER_TO_MAP(perso.getCurMap(), perso); // rajoute le this
	  }*/
	
	//Améliore qu'importe l'item donné
	public void OneItemEvo(Player perso, int ITEM_POS, long ConstantStat) //use Method getOneObjetByPos()
	{
		//On prend l'item du joueur via l'item pos donné, on ajoute une stats genre intel, l'intel de base de l'item est suppr
		//donc intel = 0 histoire de reset la stats de l'item sur la stats demandé, on rajoute l'effet multiplicatif via le calcul :
		//coefficient de prestige * niveau du joueur * le prestige
		//coefficient de prestige = coefficient qui se multiplie lui-même par 1.13 par prestige
		if(perso.getObjetByPos(ITEM_POS) != null)
		{
			if(prestigePlayer > 0) 
			{
				perso.getObjetByPos(ITEM_POS).getStats().addOneStat(ConstantStat, -perso.getObjetByPos(ITEM_POS).getStats().get(ConstantStat)+ tabCoef[prestigePlayer-1] * perso.getLevel() * prestigePlayer);
			}
			else if(prestigePlayer == 0)
			{
				SocketManager.PACKET_POPUP_DEPART(perso,"Tu ne peux pas a ton prestige.");
			}
		}
		Database.getDynamics().getObjectData().update(perso.getObjetByPos(ITEM_POS));			

		
		//Refresh le perso pour voir et utiliser les modifications sans d�co reco
		SocketManager.GAME_SEND_ALTER_GM_PACKET(perso.curMap,perso);
		//SocketManager.GAME_SEND_ASK(perso.getAccount().getGameClient(), perso); // ask le client / bug le tableau score
		SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(perso.getCurMap(), perso.getId()); // delete this de la map
		SocketManager.GAME_SEND_ADD_PLAYER_TO_MAP(perso.getCurMap(), perso); // rajoute le this
		
	}
	//Améliore en vérifiant que l'item donné fait parti des items qui sont acceptable
	public void OneItemEvoWithObject(Player perso, int ITEM_POS, long ConstantStat, ObjectTemplate objtemplate) //objtemplate via méthode
	{
		boolean test = false;
		if(perso.getObjetByPos(ITEM_POS) != null)
		{
			
		
			for (ObjectTemplate objectTemplate : listItemEvo) 
			{
				//Si l'item de la liste qu'on regarde, est égal au template de l'item qu'on a demandé a vérifier...
				if(objectTemplate == objtemplate)
				{
					test = true;
				}
				//Si effectivement l'item qu'on a demander apparait 1 fois dans la liste des items OK...
					if(test && prestigePlayer > 0)
					{
						perso.getObjetByPos(ITEM_POS).getStats().addOneStat(ConstantStat, -perso.getObjetByPos(ITEM_POS).getStats().get(ConstantStat)  +  
								(tabCoef[prestigePlayer-1] * perso.getLevel() * prestigePlayer));
					}
					else if(prestigePlayer == 0)
					{
						SocketManager.PACKET_POPUP_DEPART(perso,"Tu ne peux pas a ton prestige.");
						
					}
				
			}
			if(!test)
			{
				SocketManager.PACKET_POPUP_DEPART(perso, "Tu as aucun objet qui est de type Evolutif");
			}
		}
		Database.getDynamics().getObjectData().update(perso.getObjetByPos(ITEM_POS));			

	
		//Refresh le perso pour voir et utiliser les modifications sans d�co reco
		SocketManager.GAME_SEND_ALTER_GM_PACKET(perso.curMap,perso);
		//SocketManager.GAME_SEND_ASK(perso.getAccount().getGameClient(), perso); // ask le client / bug le tableau score
		SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(perso.getCurMap(), perso.getId()); // delete this de la map
		SocketManager.GAME_SEND_ADD_PLAYER_TO_MAP(perso.getCurMap(), perso); // rajoute le this
		
	}
	
	public void AllItemEvo(Player perso, int ITEM_POS, long ConstantStat, boolean allElem) //use Method getOneObjetByPos()
	{
		if(ConstantStat == 0) //Si on met 0 pour indiquer AllElem
		{
			
		
			if(allElem) //Si effectivement on veut tous les element
			{
				//Pour chaque item dans les objets équipé du joueur
				for (GameObject objectTemplate : getAllObjetByPos(perso)) 
				{
					if(objectTemplate != null)
					{
						//On met a jour les stats via l'évolution
					
						objectTemplate.getStats().addOneStat(Constant.STATS_ADD_AGIL, -objectTemplate.getStats().get(Constant.STATS_ADD_AGIL)  +  
							(tabCoef[prestigePlayer-1] * perso.getLevel() * prestigePlayer));
						objectTemplate.getStats().addOneStat(Constant.STATS_ADD_CHAN, -objectTemplate.getStats().get(Constant.STATS_ADD_CHAN)  +  
							(tabCoef[prestigePlayer-1] * perso.getLevel() * prestigePlayer));
						objectTemplate.getStats().addOneStat(Constant.STATS_ADD_FORC, -objectTemplate.getStats().get(Constant.STATS_ADD_FORC)  +  
							(tabCoef[prestigePlayer-1] * perso.getLevel() * prestigePlayer));
						objectTemplate.getStats().addOneStat(Constant.STATS_ADD_INTE, -objectTemplate.getStats().get(Constant.STATS_ADD_INTE)  +  
							(tabCoef[prestigePlayer-1] * perso.getLevel() * prestigePlayer));
					}
				}
				
			
			}
		}
		else // si on veut juste ajouter 1 stats a tous les objets équipé du joueur 
		{
			for (GameObject objectTemplate : getAllObjetByPos(perso)) 
			{
				//Pour chaque item, on met a jour via l'évolution la stats voulu
				objectTemplate.getStats().addOneStat(ConstantStat, -perso.getObjetByPos(ITEM_POS).getStats().get(ConstantStat)  +  
					(tabCoef[prestigePlayer-1] * perso.getLevel() * prestigePlayer));
			}
		}
		Database.getDynamics().getObjectData().update(perso.getObjetByPos(ITEM_POS));			

		
		//Refresh le perso pour voir et utiliser les modifications sans d�co reco
		SocketManager.GAME_SEND_ALTER_GM_PACKET(perso.curMap,perso);
		//SocketManager.GAME_SEND_ASK(perso.getAccount().getGameClient(), perso); // ask le client / bug le tableau score
		SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(perso.getCurMap(), perso.getId()); // delete this de la map
		SocketManager.GAME_SEND_ADD_PLAYER_TO_MAP(perso.getCurMap(), perso); // rajoute le this
		
	}
	public void AllItemEvoWithVerif(Player perso, int ITEM_POS, long ConstantStat, boolean allElem) //use Method getOneObjetByPos()
	{
		if(ConstantStat == 0) //Si on met 0 pour indiquer AllElem
		{
			
		
			if(allElem) //Si effectivement on veut tous les element
			{
				//Pour chaque item dans les objets équipé du joueur
				for (GameObject objectTemplateDuJoueur : getAllObjetByPos(perso)) 
				{
					if(objectTemplateDuJoueur != null)
					{
						for (ObjectTemplate objectTemplateOk : listItemEvo) 
						{
							if(objectTemplateOk == objectTemplateDuJoueur.getTemplate())
							{
								
									//On met a jour les stats via l'évolution
								
								objectTemplateDuJoueur.getStats().addOneStat(Constant.STATS_ADD_AGIL, -objectTemplateDuJoueur.getStats().get(Constant.STATS_ADD_AGIL)  +  
										(tabCoef[prestigePlayer-1] * perso.getLevel() * prestigePlayer));
									objectTemplateDuJoueur.getStats().addOneStat(Constant.STATS_ADD_CHAN, -objectTemplateDuJoueur.getStats().get(Constant.STATS_ADD_CHAN)  +  
										(tabCoef[prestigePlayer-1] * perso.getLevel() * prestigePlayer));
									objectTemplateDuJoueur.getStats().addOneStat(Constant.STATS_ADD_FORC, -objectTemplateDuJoueur.getStats().get(Constant.STATS_ADD_FORC)  +  
										(tabCoef[prestigePlayer-1] * perso.getLevel() * prestigePlayer));
									objectTemplateDuJoueur.getStats().addOneStat(Constant.STATS_ADD_INTE, -objectTemplateDuJoueur.getStats().get(Constant.STATS_ADD_INTE)  +  
										(tabCoef[prestigePlayer-1] * perso.getLevel() * prestigePlayer));
								}
							}
						}
					
				}
				
			
			}
		}
		else // si on veut juste ajouter 1 stats a tous les objets équipé du joueur 
		{
			//Pour chaque item dans les objets équipé du joueur
			for (GameObject objectTemplateDuJoueur : getAllObjetByPos(perso)) 
			{
				if(objectTemplateDuJoueur != null)
				{
					for (ObjectTemplate objectTemplateOk : listItemEvo) 
					{
						if(objectTemplateOk == objectTemplateDuJoueur.getTemplate())
						{
							//On met a jour les stats via l'évolution	
							objectTemplateDuJoueur.getStats().addOneStat(ConstantStat, -objectTemplateDuJoueur.getStats().get(ConstantStat)  +  
									(tabCoef[prestigePlayer-1] * perso.getLevel() * prestigePlayer));
						}
					}
				}
				
			}
			
		}
		Database.getDynamics().getObjectData().update(perso.getObjetByPos(ITEM_POS));			

		
		//Refresh le perso pour voir et utiliser les modifications sans d�co reco
		SocketManager.GAME_SEND_ALTER_GM_PACKET(perso.curMap,perso);
		//SocketManager.GAME_SEND_ASK(perso.getAccount().getGameClient(), perso); // ask le client / bug le tableau score
		SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(perso.getCurMap(), perso.getId()); // delete this de la map
		SocketManager.GAME_SEND_ADD_PLAYER_TO_MAP(perso.getCurMap(), perso); // rajoute le this
		
	}


	// récupérer tous les items équipé d'un joueur sous format tableau
	public GameObject[] getAllObjetByPos(Player perso)
	{
		GameObject[] itemsPosOfPlayer = 
			{
				perso.getObjetByPos(Constant.ITEM_POS_AMULETTE), 
				perso.getObjetByPos(Constant.ITEM_POS_ANNEAU1),
				perso.getObjetByPos(Constant.ITEM_POS_ANNEAU2),
				perso.getObjetByPos(Constant.ITEM_POS_ARME),
				perso.getObjetByPos(Constant.ITEM_POS_BOTTES),
				perso.getObjetByPos(Constant.ITEM_POS_CAPE),
				perso.getObjetByPos(Constant.ITEM_POS_CEINTURE),
				perso.getObjetByPos(Constant.ITEM_POS_COIFFE),
				perso.getObjetByPos(Constant.ITEM_POS_DOFUS1),
				perso.getObjetByPos(Constant.ITEM_POS_DOFUS2),
				perso.getObjetByPos(Constant.ITEM_POS_DOFUS3),
				perso.getObjetByPos(Constant.ITEM_POS_DOFUS4),
				perso.getObjetByPos(Constant.ITEM_POS_DOFUS5),
				perso.getObjetByPos(Constant.ITEM_POS_DOFUS6),
				perso.getObjetByPos(Constant.ITEM_POS_DOFUS7),
				perso.getObjetByPos(Constant.ITEM_POS_DOFUS8),
				perso.getObjetByPos(Constant.ITEM_POS_DOFUS9),
				perso.getObjetByPos(Constant.ITEM_POS_DOFUS10),
				perso.getObjetByPos(Constant.ITEM_POS_DOFUS11),
				perso.getObjetByPos(Constant.ITEM_POS_DOFUS12),
				perso.getObjetByPos(Constant.ITEM_POS_BOUCLIER),
				perso.getObjetByPos(Constant.ITEM_POS_DOFUS_ULTIME),
				perso.getObjetByPos(Constant.ITEM_POS_FAMILIER),
				perso.getObjetByPos(Constant.ITEM_POS_RELIQUE),
				perso.getObjetByPos(Constant.ITEM_POS_FIGURINE),
				perso.getObjetByPos(Constant.ITEM_POS_DRAGODINDE)
			};
		//Création d'une liste, qui va récupérer tous les items que le joueur a d'équipé // non null
		List<GameObject> itemsPosWithItem = new ArrayList<>();
		for(GameObject gameObject : itemsPosOfPlayer)
		{
			if(gameObject != null && gameObject.getPosition()!= Constant.ITEM_POS_NO_EQUIPED)
			{
				itemsPosWithItem.add(gameObject);
			}
		}
		return itemsPosOfPlayer;
	}
	public int getPrestigePlayer()
	{
		return prestigePlayer;
	}
	public void setPrestigePlayer(int prestigeOfPlayer)
	{
		prestigePlayer = prestigeOfPlayer;
	}
	
	
	
}
