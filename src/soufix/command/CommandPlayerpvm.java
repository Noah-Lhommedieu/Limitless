package soufix.command;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import soufix.fight.spells.Spell;
import soufix.main.Constant;
import soufix.other.ItemEvolution;
import soufix.other.Ornements;
import soufix.other.OverPower;
import soufix.other.Titre;
import soufix.Hdv.Hdv;
import soufix.client.Player;
import soufix.client.other.Party;
import soufix.command.administration.Command;
import soufix.common.SocketManager;
import soufix.database.Database;
//import soufix.database.Database;
import soufix.game.GameClient;
import soufix.game.World;
import soufix.game.action.ExchangeAction;
import soufix.main.Config;
import soufix.main.Main;
import soufix.object.GameObject;

public class CommandPlayerpvm {
	private static String canal;

	static {
		CommandPlayerpvm.canal = "Limitless";
	}
	static int i = 0;
	public static boolean analyse(final Player perso, final String msg) {
		if (msg.charAt(0) != '.' || msg.charAt(1) == '.') {
			return false;
		}
		if(perso.getGameClient() == null)
		return true;	
		if(msg.length() > 6 && msg.substring(1, 7).equalsIgnoreCase("points")) {
			perso.sendMessage("Vous avez <b>" + perso.getAccount().getPoints() + "</b> points boutique");
			return true;
		} 
		if (msg.length() > 3 && msg.substring(1, 4).equalsIgnoreCase("all") || msg.length() > 5 && msg.substring(1, 6).equalsIgnoreCase("monde")) {
			if (perso.noall) {
				SocketManager.GAME_SEND_MESSAGE(perso,
						"Votre canal " + CommandPlayerpvm.canal + " est d\u00e9sactiv\u00e9.", "C35617");
				return true;
			}
            if (perso.getGroupe() == null && System.currentTimeMillis() < perso.getGameClient().getTimeLastTaverne()) {
                perso.sendMessage("Tu dois attendre encore "+(System.currentTimeMillis() - perso.getGameClient().getTimeLastTaverne()) / 1000+" seconde(s)");
                return true;
            }
            if(msg.substring(5).compareTo("") == 0) {
            	 perso.sendMessage("Message vide");
            	return true;
            }
            World.get_Succes(perso.getId()).msg_add(perso);
			perso.getGameClient().setTimeLastTaverne(System.currentTimeMillis()+5000);
			SocketManager.GAME_SEND_cMK_PACKET_TO_ALL_commande_all(perso,"~",perso.getId(),perso.getName(),msg.substring(5));
			
			return true;
		} else {
			if (msg.length() > 5 && msg.substring(1, 6).equalsIgnoreCase("noall")) {
				if (perso.noall) {
					perso.noall = false;
					SocketManager.GAME_SEND_MESSAGE(perso,
							"Vous avez activ\u00e9 le canal " + CommandPlayerpvm.canal + ".", "C35617");
				} else {
					perso.noall = true;
					SocketManager.GAME_SEND_MESSAGE(perso,
							"Vous avez d\u00e9sactiv\u00e9 le canal " + CommandPlayerpvm.canal + ".", "C35617");
				}
				return true;
			}
			if (msg.length() > 9
					&& msg.substring(1, 10).equalsIgnoreCase("celldeblo"))
			{//180min
				if (perso.isInPrison())
					return true;
				if (perso.cantTP())
					return true;
				if (perso.getFight() != null)
					return true;
				boolean autorised = true;
				switch (perso.getCurMap().getId())
				{
				case 8911:
				case 8916:
				case 8917:
				case 9827:
				case 8930:
				case 8932:
				case 8933:
				case 8934:
				case 8935:
				case 8936:
				case 8938:
				case 8939:
				case 9230:
				case 9646:
				case 9589:
				case 9590:
					autorised = false;
					break;
				}
				if (!autorised)
					return true;
				if (System.currentTimeMillis() < perso.getGameClient().getTimeLastTaverne()) {
	                perso.sendMessage("Tu dois attendre encore "+(System.currentTimeMillis() - perso.getGameClient().getTimeLastTaverne()) / 1000+" seconde(s)");
	                return true;
	            }
				perso.getGameClient().setTimeLastTaverne(System.currentTimeMillis()+15000);
				perso.teleport(perso.getCurMap(), perso.getCurMap().getRandomFreeCellId());
				return true;
			}
			if (msg.length() > 5 && msg.substring(1, 6).equalsIgnoreCase("staff")) {
				String message = "Liste des membres du staff connect\u00e9s :";
				boolean vide = true;
				 for(Player player : Main.world.getOnlinePlayers())
				 {
					if (player == null) {
						continue;
					}
					if (player.getGroupe() == null) {
						continue;
					}
					if (player.isInvisible()) {
						continue;
					}
					message = String.valueOf(message) + "\n- <b><a href='asfunction:onHref,ShowPlayerPopupMenu,"
							+ player.getName() + "'>[" + player.getGroupe().getName() + "] " + player.getName()
							+ "</a></b>";
					vide = false;
				}
				if (vide) {
					message = "Il n'y a aucun membre du staff connect\u00e9. Vous pouvez tout de m\u00eame allez voir sur notre Discord.";
				}
				SocketManager.GAME_SEND_MESSAGE(perso, message);
				return true;
			}
			if (msg.length() > 4 && msg.substring(1, 5).equalsIgnoreCase("pass")) {
				if(perso.getAutoSkip()== true){
					perso.setAutoSkip(false);
				SocketManager.GAME_SEND_MESSAGE(perso,"Auto pass Off,","008000");
				}else{
					perso.setAutoSkip(true);
					SocketManager.GAME_SEND_MESSAGE(perso,"Auto pass On", "008000");
				}
				return true;
			}
			if (msg.length() > 5 && msg.substring(1, 6).equalsIgnoreCase("krala")) {
				 SocketManager.GAME_SEND_MESSAGE(perso,"La relique du jour est <b>["+Main.relique_donjon+"]</b> pour <b>Antre du kralamour </b>.","257C38");
				return true;
			}
//			if (msg.length() > 7 && msg.substring(1, 8).equalsIgnoreCase("addorna"))
//			{
//				String[] mots = msg.split(" "); // Diviser la cha�ne en mots
//
//		        if (mots.length > 1) {
//	            String valeurOrnementStr = mots[1]; // R�cup�rer le deuxi�me mot apr�s "geto"
//		        int valeurOrnement = Integer.parseInt(valeurOrnementStr);
//				perso.addOrnements(valeurOrnement);
//				perso.setOrnement(valeurOrnement);
//		    	perso.sendMessage(perso.getName() + ": Ornement re�u -> " + perso.getOrnement()); // Affichage r�sultat d'obtention d'ornement
//				return true;
//			}
//		        }
			
			

			if (msg.length() > 5 && msg.substring(1, 6).equalsIgnoreCase("parse"))
			{
				perso.sendMessage(perso.parseToGM());
				return true;
			}
			/*if (msg.length() > 8 && msg.substring(1, 9).equalsIgnoreCase("auraPrice"))
			{
				perso.sendMessage("auraList = "+perso.auraList);
				perso.sendMessage("auraList.ToString() = " + perso.auraList.toString());
			}*/
			if (msg.length() > 4 && msg.substring(1, 5).equalsIgnoreCase("aura"))
			{	
				try {
			        Map<Integer, Integer> PriceAura = new HashMap<>();
			        int j = 0;
			        int price = 0;
			        
			        for (int i = 0; i < 33; i++) 
			        {	switch (j)
			        	{
			        case 10:
			        	price = 150;
			        	break;
			        case 20:
			        	price = 300;
			        	break;
			        case 30:
			        	price = 450;
			        	break;
			        	}
			        	PriceAura.put(i, price);
			        	j++;
					}
			        
			        int aura =  Integer.parseInt(msg.substring(6, msg.length() - 1));
			        if(perso.getAuraList().contains(aura)) 
			        {
			        	perso.setAura(aura);
			        }
			        /*
			          else
			          {
						SocketManager.PACKET_POPUP_DEPART(perso, "Tu ne possède pas cette aura, attend de la débloquer");

			          }
			        */
			        else
			        {
			        	int pointPlayer = perso.getAccount().getPoints();
			        	if(pointPlayer >= PriceAura.get(aura))
			        	{
				        	perso.setAura(aura);
							perso.addAura(aura);
							perso.getAccount().setPoints(pointPlayer - PriceAura.get(aura));
			        	}
			        
			        	else 
			        	{
			        		SocketManager.PACKET_POPUP_DEPART(perso, "Tu n'as pas assez de points pour acheter cette aura, il t'en faut " + PriceAura.get(aura));
			        	}
			        }
					
				Database.getStatics().getPlayerData().updateAura(perso);
				Database.getStatics().getPlayerData().update(perso);
				//perso.sendMessage("parseAuraToDB = " + perso.parseAuraToDB());
				
				
				
				}
				catch (Exception e) 
				{
					perso.sendMessage("Exception AURA");
				}
				SocketManager.PACKET_POPUP_DEPART(perso, "Aura "+ perso.auraC +" équipé");
				
				
				//CommandPlayerpvm.analyse(perso,".parse");
				SocketManager.GAME_SEND_ALTER_GM_PACKET(perso.getCurMap(), perso);
				SocketManager.GAME_SEND_ASK(perso.getAccount().getGameClient(), perso); // ask le client
            	SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(perso.getCurMap(), perso.getId()); // delete perso de la map
            	SocketManager.GAME_SEND_ADD_PLAYER_TO_MAP(perso.getCurMap(), perso); // rajoute le perso 
			}
			if (msg.length() > 7 && msg.substring(1, 8).equalsIgnoreCase("ItemEvo")) 
				{
					ItemEvolution itemEvo = new ItemEvolution(perso);
					itemEvo.OneItemEvoWithObject(perso, Constant.ITEM_POS_ANNEAU1, Constant.STATS_ADD_CHAN, perso.getObjetByPos(Constant.ITEM_POS_ANNEAU1).getTemplate());		
						
							
				}
			
			
			/*if (msg.length() > 9 && msg.substring(1, 10).equalsIgnoreCase("auraClear")) 
			{
				perso.getAuraList().clear();
			}*/
			if (msg.length() > 4 && msg.substring(1, 5).equalsIgnoreCase("FEURVIP")) {
//				if(perso.getAccount().getSubscribeRemaining() == 0L){
//	               	 SocketManager.GAME_SEND_MESSAGE(perso,"R�serv� au V.I.P.","008000");	 
//	                return true;	 
//	                }
				if(perso.couleur== true){
					perso.couleur = false;
				SocketManager.GAME_SEND_MESSAGE(perso,"Mode aura Off","008000");
				SocketManager.GAME_SEND_ALTER_GM_PACKET(perso.getCurMap(),perso);
				}else{
					perso.couleur = true;
					SocketManager.GAME_SEND_MESSAGE(perso,"Mode aura On", "008000");
					SocketManager.GAME_SEND_ALTER_GM_PACKET(perso.getCurMap(),perso);
				}
				return true;
			}
			if(msg.length() > 6 && msg.substring(1, 7).equalsIgnoreCase("setvip")) {
				perso.getAccount().setVip(1);
			}
			if (msg.length() > 3 && msg.substring(1, 4).equalsIgnoreCase("vip")) {
				if(Config.singleton.serverId == 1) {
				SocketManager.PACKET_POPUP_DEPART(perso, 
						 "\n- Vos points acquis par vote sur Serveur-Prive passent � 45 PB par vote."
						+ "\n- Avoir acc�s � la <b>banque</b>. gratuitement pour les VIP."
						//+ "\n- L'acc�l�ration <b>*3</b> de votre temps de craft."
						+ "\n+ <b>10.000</b> pods de plus."
						+ "\n<b>.ipdrop</b> - Permets de r�cup�rer le drop de vos mules."
						+ "\n<b>.ipkamas</b> - Permets de r�cup�rer les kamas de vos mules."
						+ "\n<b>.nodropitem</b> - Vous emp�che de recevoir des items de monstres."
						+ "\n<b>.nodropressources</b> - Vous emp�che de recevoir des ressources de monstres."
						+ "\n Vous obtenez un bonus de 25% d'exp�rience � chaque combat pour toute la team"
						+ "\n Vous obtenez un bonus de 25% d'exp�rience m�tier "
						+ "\n Vous obtenez un bonus de 25% de drop � chaque combat pour toute la team"
						+ "\n<b>.aura</b> - Aura exlusive multicolore"
						+ "\nAugmente la chance de reussite d'un <b>Exo</b> 1/4 vip 1/10 non vip.");
				}else {
					SocketManager.PACKET_POPUP_DEPART(perso, 
							 "\n- Vos points acquis par vote sur Serveur-Prive passent � 45 PB par vote."
							+ "\n- Avoir acc�s � la <b>banque</b>. gratuitement pour les VIP."
							//+ "\n- L'acc�l�ration <b>*3</b> de votre temps de craft."
							+ "\n+ <b>10.000</b> pods de plus."
							+ "\n<b>.ipdrop</b> - Permets de r�cup�rer le drop de vos mules."
							+ "\n<b>.ipkamas</b> - Permets de r�cup�rer les kamas de vos mules."
							+ "\n<b>.nodropitem</b> - Vous emp�che de recevoir des items de monstres."
							+ "\n<b>.nodropressources</b> - Vous emp�che de recevoir des ressources de monstres."
							+ "\n Vous obtenez un bonus de 25% d'exp�rience � chaque combat pour toute la team"
							+ "\n Vous obtenez un bonus de 25% d'exp�rience m�tier "
							+ "\n Vous obtenez un bonus de 25% de drop � chaque combat pour toute la team"
							+ "\n<b>.aura</b> - Aura exlusive multicolore"
							+ "\nAugmente la chance de reussite d'un <b>Exo</b> 1/10 vip 1/20 non vip.");	
				}
				return true;
			}
			if (msg.length() > 6 && msg.substring(1, 7).equalsIgnoreCase("maitre")) {
			
				if (System.currentTimeMillis() < perso.getGameClient().timeLasttpcommande) {
	                perso.sendMessage("Tu dois attendre encore "+(System.currentTimeMillis() -  perso.getGameClient().timeLasttpcommande) / 1000+" seconde(s)");
	                return true;
	            }
				 perso.getGameClient().timeLasttpcommande =(System.currentTimeMillis()+700);
				    if(perso.getParty()!=null)
				    {
				    	if(perso.getParty().getMaster() != null)
				    	if(perso.getParty().getMaster().getId() == perso.getId()) {
				    		 perso.getParty().setMaster(null);
				    		 perso.getParty().clear_groupe();
				    		 perso.setOne_windows(false);
				    		 perso.sendMessage("Mode Maitre off.");
				    		 return true;
				    	}
				      perso.sendMessage("Vos etes d�j� dans un groupe.");
				      return true;
				    }
				    int nbr = 0;
				    for (final Player z : perso.getCurMap().getPlayers()) {
				    	if(z == null)
				    		continue;
    					if (z.getGameClient() == null) {
    						continue;
    					}
    					if(z.getAccount().getId_web() != perso.getAccount().getId_web()) 
    					if(!z.getAccount().getCurrentIp().equals(perso.getAccount().getCurrentIp()))
    					continue;
    					if(perso.getId() == z.getId())continue;
    					if(z.getParty() != null)
        					continue;
    					if (perso.getParty() != null && nbr == 8)
    						continue;
    					nbr++;
    					perso.getGameClient().inviteParty("zz"+z.getName(),true);
    					z.getGameClient().acceptInvitation();
    					SocketManager.GAME_SEND_PR_PACKET(z);
    					SocketManager.GAME_SEND_MESSAGE(z,"Vous suivez maintenant "+perso.getName()+"");
    				}
				    if(nbr == 0 || perso.getParty() == null) {
				    	SocketManager.GAME_SEND_MESSAGE(perso,"Aucune mule n'est sur la map");	
				    	return true;
				    }
				    final Party party=perso.getParty();
				    party.setMaster(perso);
				    party.moveAllPlayersToMaster(null);
				    SocketManager.GAME_SEND_MESSAGE(perso,"Vous �tes d�sormais le maitre de votre groupe");
				    return true;
			}
			if (msg.length() > 6 && msg.substring(1, 7).equalsIgnoreCase("banque")) {
				if (perso.getFight() != null) {
					return true;
				}
				if(perso.getGameClient().show_cell_BANK) {
					GameClient.leaveExchange(perso);
					perso.getGameClient().show_cell_BANK = false;
				}
				else {
				GameClient.leaveExchange(perso);
				final int cost = perso.getBankCost();
				if(perso.getAccount().getSubscribeRemaining() == 0L)
				if (cost > 0) {
					final long playerKamas = perso.getKamas();
					final long kamasRemaining = playerKamas - cost;
					final long bankKamas = perso.getAccount().getBankKamas();
					long totalKamas = bankKamas + playerKamas;
					if (kamasRemaining < 0L) {
						if (bankKamas >= cost) {
							Main.world.kamas_total -= cost;
							perso.setBankKamas(bankKamas - cost);
						} else {
							if (totalKamas < cost) {
								SocketManager.GAME_SEND_MESSAGE_SERVER(perso, "10|" + cost);
								return true;
							}
							Main.world.kamas_total -= cost;
							perso.setKamas(0L);
							perso.setBankKamas(totalKamas - cost);
							SocketManager.GAME_SEND_STATS_PACKET(perso);
							SocketManager.GAME_SEND_Im_PACKET(perso, "020;" + playerKamas);
						}
					} else {
						Main.world.kamas_total -= cost;
						perso.setKamas(kamasRemaining);
						SocketManager.GAME_SEND_STATS_PACKET(perso);
						SocketManager.GAME_SEND_Im_PACKET(perso, "020;" + cost);
					}
				}
				SocketManager.GAME_SEND_ECK_PACKET(perso.getGameClient(), 5, "");
				SocketManager.GAME_SEND_EL_BANK_PACKET(perso);
				perso.setAway(true);
				perso.setExchangeAction(new ExchangeAction<>(ExchangeAction.IN_BANK,0));
				perso.getGameClient().show_cell_BANK = true;
				}
				return true;
				
			}
			if (msg.length() > 9 && msg.substring(1, 10).equalsIgnoreCase("ornements")) {
                StringBuilder ornements = new StringBuilder();
                for (Ornements o : World.getOrnements().values()) {

                    if(perso.getOrnementsList().contains(o.getId())) {
                        if (ornements.length() > 0) {
                            ornements.append(";");
                        }
                        ornements.append(o.getId() + ","+ o.getPrice() +",T");

                    } else if (!o.isCanbuy()) {
                        if (ornements.length() > 0) {
                            ornements.append(";");
                        }
                        //ornements.append(o.getId() + ","+o.getName()+"," + o.getPrice());
                    }
                    ornements.append(o.getId() + ","+o.getPrice() +"," + o.getName());
                }


                perso.send("wl"+ornements.toString());
                //perso.sendMessage("wl"+ornements.toString());
                // DEBUG perso.sendMessage("wl"+ornements.toString());
                // DEBUG perso.sendMessage(World.getOrnements().toString());
                return true;
            }
			
			if (msg.length() > 8 && msg.substring(1, 9).equalsIgnoreCase("boutique")) {
				GameClient.leaveExchange(perso);
				soufix.main.Boutique.open(perso);
				return true;
			}
			if (msg.length() > 6 && msg.substring(1, 7).equalsIgnoreCase("window")) {
				if(perso.getFight()!=null) {
					SocketManager.GAME_SEND_MESSAGE(perso,"Error Combat", "008000");
					return true;
				}
				if(perso.getParty() == null || perso.getParty().getMaster() == null ||
						perso.getParty().getMaster().getId() != perso.getId())
				{
				SocketManager.GAME_SEND_MESSAGE(perso,"Mets toi Ma�tre avant");	
				return true;
				}
				if(perso.isOne_windows())
				{
					 SocketManager.GAME_SEND_MESSAGE(perso,"One Window Off");
				 perso.setOne_windows(false);
				}else
				{
					 SocketManager.GAME_SEND_MESSAGE(perso,"One Window On");
				perso.setOne_windows(true);	
				}
				return true;
			} 
			if (msg.length() > 3 && msg.substring(1, 4).equalsIgnoreCase("hdv")) {
				if(perso.getFight()!=null)
				SocketManager.GAME_SEND_MESSAGE(perso,"Vous ne pouvez pas ouvrir le march� pendant le combat.");
		        else
		        {
		        	GameClient.leaveExchange(perso);
		          Hdv hdv=Main.world.getWorldMarket();
		          if(hdv!=null)
		          {
		            String info="1,10,100;"+hdv.getStrCategory()+";"+hdv.parseTaxe()+";"+hdv.getLvlMax()+";"+hdv.getMaxAccountItem()+";-1;"+hdv.getSellTime();
		            SocketManager.GAME_SEND_ECK_PACKET(perso,11,info);
		            ExchangeAction<Integer> exchangeAction=new ExchangeAction<>(ExchangeAction.AUCTION_HOUSE_BUYING,-perso.getCurMap().getId()); //R�cup�re l'ID de la map et rend cette valeur n�gative
		            perso.setExchangeAction(exchangeAction);
		            perso.setWorldMarket(true);
		          }
		        }
				return true;
			} 
			if (msg.length() > 8 && msg.substring(1, 9).equalsIgnoreCase("movemobs")) {
				perso.getCurMap().onMapMonsterDeplacement();
			      perso.sendMessage("Vous avez deplace un groupe de monstres.");
				return true;
			} 
			if (msg.length() > 5 && msg.substring(1, 6).equalsIgnoreCase("debug")) {
				GameClient.leaveExchange(perso);
				if(perso.getFight() != null) {
					perso.sendMessage("Combat !!!!.");
				return true;	
				}
				if (System.currentTimeMillis() <  perso.getAccount().timeLastdebug) {
					 perso.sendMessage("Tu dois attendre encore "+(System.currentTimeMillis() -  perso.getAccount().timeLastdebug) / 1000+" seconde(s)");
	                return true;
	            }
				perso.getAccount().timeLastdebug =(System.currentTimeMillis()+60000);
				
				try
			      {
			        for(GameObject object : new ArrayList<>(perso.getObjects().values()))
			        {
			          if(object==null)
			            continue;
			          if(object.modification==0)
			            Database.getDynamics().getObjectData().insert(object);
			          else if(object.modification==1)
			            Database.getDynamics().getObjectData().update(object);
			          object.modification=-1;
			        }
			      }
			      catch(Exception e)
			      {
			        e.printStackTrace();
			      }
				Database.getStatics().getPlayerData().update(perso);
			      perso.getCurCell().removePlayer(perso);
			      if(perso.getGameClient()!=null)
			        perso.getGameClient().disconnect();
			      perso.setOnline(false);
			      perso.resetVars();
			      SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(perso.getCurMap(),perso.getId());
			     Main.world.unloadPerso(perso);
			      Database.getStatics().getPlayerData().load(perso.getId());
			    //  Main.world.ReassignAccountToChar(perso.getAccount().getId());;
			      if(perso.get_guild() != null)
				      perso.get_guild().getMember(perso.getId()).setPlayer(Main.world.getPlayer(perso.getId()));
				return true;
			}
			/*if (msg.length() > 5 && msg.substring(1, 6).equalsIgnoreCase("debug")) {
				if (perso.getFight() != null){
					SocketManager.GAME_SEND_MESSAGE(perso,"Error Combat", "008000");
					return true;
				}
				perso.getCurCell().removePlayer(perso);
			      if(perso.getGameClient()!=null)
			        perso.getGameClient().disconnect();
			      perso.setOnline(false);
			      perso.resetVars();
			      Database.getStatics().getPlayerData().update(perso);
			      SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(perso.getCurMap(),perso.getId());
			      Main.world.unloadPerso(perso);
			      Database.getStatics().getPlayerData().load(perso.getId());
			      Main.world.ReassignAccountToChar(perso.getAccount().getId());
				return true;
			} */
			if (msg.length() > 8 && msg.substring(1, 9).equalsIgnoreCase("tpgroupe")) {
				if(perso.getFight() != null)return true;
				if (System.currentTimeMillis() <  perso.getGameClient().timeLasttpcommande) {
					 perso.sendMessage("Tu dois attendre encore "+(System.currentTimeMillis() -  perso.getGameClient().timeLasttpcommande) / 1000+" seconde(s)");
	                return true;
	            }
				 perso.getGameClient().timeLasttpcommande =(System.currentTimeMillis()+1000);
				for (final String s : Main.world.maps_dj) {
					if (Integer.parseInt(s) == perso.getCurMap().getId()) {
						SocketManager.GAME_SEND_MESSAGE(perso,"Error TP Zone DJ", "008000");	
						return true;
					}
				}
				if (perso.isInPrison()) {
					SocketManager.GAME_SEND_MESSAGE(perso,"Error TP Prison", "008000");
					return true;
				}
				if (perso.cantTP()) {
					SocketManager.GAME_SEND_MESSAGE(perso,"Error TP Zone DJ", "008000");
					return true;
				}
				if (perso.getFight() != null){
					SocketManager.GAME_SEND_MESSAGE(perso,"Error Combat", "008000");
					return true;
				}
				if(perso.getCurMap().getId() == 9877 ){
					SocketManager.GAME_SEND_MESSAGE(perso,"Error TP Zone DJ", "008000");	
					return true;
				}
				if (perso.getCurMap().getSong() == 1)
				{
					SocketManager.GAME_SEND_MESSAGE(perso,"Error TP Zone Songe", "008000");	
					return true;
				}
				boolean autorised = true;
				switch (perso.getCurMap().getId())
				{
				case 9646:
				case 8905:
				case 8911:
				case 8916:
				case 8917:
				case 9827:
				case 8930:
				case 8932:
				case 8933:
				case 8934:
				case 8935:
				case 8936:
				case 8938:
				case 8939:
				case 9230:
				case 9589:
				case 9590:
					autorised = false;
					break;
				}
				if (!autorised)
				{
					SocketManager.GAME_SEND_MESSAGE(perso,"Error TP Zone DJ", "008000");	
					return true;
				}
				if(perso.getParty() != null){
					for (final Player z :perso.getParty().getPlayers()) {
						if (z.getGameClient() == null) {
							continue;
						}
						if(z.getFight() != null)continue;
						if(z.getId() == perso.getId())
	    					continue;
						z.teleport(perso.getCurMap(), perso.getCurCell().getId());
					}
				}else{
					SocketManager.GAME_SEND_MESSAGE(perso,"Mets toi en groupe avant", "008000");	
				}
				return true;
			}
			if (msg.length() > 2 && msg.substring(1, 3).equalsIgnoreCase("tp")) {
				if(perso.getFight() != null)return true;
				if (System.currentTimeMillis() <  perso.getGameClient().timeLasttpcommande) {
					 perso.sendMessage("Tu dois attendre encore "+(System.currentTimeMillis() -  perso.getGameClient().timeLasttpcommande) / 1000+" seconde(s)");
	                return true;
	            }
				 perso.getGameClient().timeLasttpcommande =(System.currentTimeMillis()+1000);
				for (final String s : Main.world.maps_dj) {
					if (Integer.parseInt(s) == perso.getCurMap().getId()) {
						SocketManager.GAME_SEND_MESSAGE(perso,"Error TP Zone DJ", "008000");	
						return true;
					}
				}
				if (perso.isInPrison()) {
					SocketManager.GAME_SEND_MESSAGE(perso,"Error TP Prison", "008000");
					return true;
				}
				if (perso.cantTP()) {
					SocketManager.GAME_SEND_MESSAGE(perso,"Error TP Zone DJ", "008000");
					return true;
				}
				if (perso.getFight() != null){
					SocketManager.GAME_SEND_MESSAGE(perso,"Error Combat", "008000");
					return true;
				}
				if(perso.getCurMap().getId() == 9877 ){
					SocketManager.GAME_SEND_MESSAGE(perso,"Error TP Zone DJ", "008000");	
					return true;
				}
				if (perso.getCurMap().getSong() == 1)
				{
					SocketManager.GAME_SEND_MESSAGE(perso,"Error TP Zone Songe", "008000");	
					return true;
				}
				boolean autorised = true;
				switch (perso.getCurMap().getId())
				{
				case 9646:
				case 8911:
				case 8916:
				case 8917:
				case 9827:
				case 8930:
				case 8932:
				case 8933:
				case 8934:
				case 8935:
				case 8936:
				case 8938:
				case 8939:
				case 9230:
				case 9589:
				case 9590:
					autorised = false;
					break;
				}
				if (!autorised){
					SocketManager.GAME_SEND_MESSAGE(perso,"Error TP Zone DJ", "008000");	
					return true;
				}
				if(perso.getParty() != null && perso.getParty().isChief(perso.getId())){
					for (final Player z :perso.getParty().getPlayers()) {
						if (z.getGameClient() == null) {
							continue;
						}
						if(z.getFight() != null)continue;
						if(z.getAccount().getId_web() != perso.getAccount().getId_web()) 
	    					if(!z.getAccount().getCurrentIp().equals(perso.getAccount().getCurrentIp()))
	    					continue;
						if(z.getId() == perso.getId())
	    					continue;
						z.teleport(perso.getCurMap(), perso.getCurCell().getId());
					}
				}else{
					SocketManager.GAME_SEND_MESSAGE(perso,"Mets toi Ma�tre avant", "008000");	
				}
				return true;
			}
			if (msg.length() > 16 && msg.substring(1, 17).equalsIgnoreCase("nodropressources")) {
				if(perso.getAccount().getSubscribeRemaining() == 0L){
	               	 SocketManager.GAME_SEND_MESSAGE(perso,"R�serv� au V.I.P.","008000");	 
	                return true;	 
	                }
				if(perso.isCanDrop_ressources())
			    {
					perso.setCanDrop(true);
				      perso.setCanDrop_items(true);
				      perso.setCanDrop_ressources(false);
			      SocketManager.GAME_SEND_MESSAGE(perso,"No drop ressources on.");
			    }
			    else
			    {
			    	perso.setCanDrop(true);
				      perso.setCanDrop_items(true);
				      perso.setCanDrop_ressources(true);
			      SocketManager.GAME_SEND_MESSAGE(perso,"No drop ressources off.");
			    }
			    return true;
				
			}
			if (msg.length() > 10 && msg.substring(1, 11).equalsIgnoreCase("nodropitem")) {
				if(perso.getAccount().getSubscribeRemaining() == 0L){
	               	 SocketManager.GAME_SEND_MESSAGE(perso,"R�serv� au V.I.P.","008000");	 
	                return true;	 
	                }
				if(perso.isCanDrop_items())
			    {
					perso.setCanDrop(true);
				      perso.setCanDrop_items(false);
				      perso.setCanDrop_ressources(true);
			      SocketManager.GAME_SEND_MESSAGE(perso,"No drop items on.");
			    }
			    else
			    {
			    	perso.setCanDrop(true);
				      perso.setCanDrop_items(true);
				      perso.setCanDrop_ressources(true);
			      SocketManager.GAME_SEND_MESSAGE(perso,"No drop items off.");
			    }
			    return true;
				
			}
			if (msg.length() > 6 && msg.substring(1, 7).equalsIgnoreCase("nodrop")) {
				if(perso.getCanDrop())
			    {
			      perso.setCanDrop(false);
			      perso.setCanDrop_items(true);
			      perso.setCanDrop_ressources(true);
			      SocketManager.GAME_SEND_MESSAGE(perso,"No drop on.");
			    }
			    else
			    {
			      perso.setCanDrop(true);
			      perso.setCanDrop_items(true);
			      perso.setCanDrop_ressources(true);
			      SocketManager.GAME_SEND_MESSAGE(perso,"No drop off.");
			    }
			    return true;
				
			}
			
			
			if (msg.length() > 8 && msg.substring(1, 9).equalsIgnoreCase("prestige")) {
				int max = 100, lvlmax = 8000;
            	if(perso.prestige < 0 || perso.prestige >= max ) {
            		perso.sendMessage("Action impossible : vous avez d�j� atteint le prestige maximal.");
            		return true;
            	}
            	if(perso.getLevel() != lvlmax) {
            		perso.sendMessage("Vous devez �tre niveau "+lvlmax+" pour pouvoir b�n�ficier de ce service.");
            		return true;
            	}
            	
            	perso.unequipedObjetPrestige();
            	Restat(perso);
            	resetPlayer(perso);
            	perso.setPrestige(perso.getPrestige() + 1);
            	SocketManager.GAME_SEND_ALTER_GM_PACKET(perso.curMap,perso);
            	Database.getStatics().getPlayerData().updatePrestige(perso);
            	SocketManager.GAME_SEND_ASK(perso.getAccount().getGameClient(), perso); // ask le client
            	SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(perso.getCurMap(), perso.getId()); // delete perso de la map
            	SocketManager.GAME_SEND_ADD_PLAYER_TO_MAP(perso.getCurMap(), perso); // rajoute le perso 
            	setRewardPrestige(perso);
            	SocketManager.PACKET_POPUP_DEPART(perso, "Vous �tes Prestige " + perso.getPrestige() + ".");
            	SocketManager.GAME_SEND_MESSAGE_TO_ALL("Le joueur " + perso.getName() + "est pass� prestige " + perso.getPrestige() + ".", "32A852");
            	Constant.onLevelUpSpells(perso, perso.getLevel());// le poss�de Player.SpellMax(perso);
            	
            	return true;
			    
				
			}
			if (msg.length() > 12 && msg.substring(1, 13).equalsIgnoreCase("infoprestige"))
			{
    			if (perso.getPrestige() == 0) 
    			{
    				SocketManager.PACKET_POPUP_DEPART(perso, "Vous n'�tes pas prestige");
    			}
    			else 
    			{
    				SocketManager.PACKET_POPUP_DEPART(perso, "Tu es <b>Prestige " + perso.prestige + " !</b>");
    			}
				return true;
			}
			if (msg.length() > 7 && msg.substring(1, 8).equalsIgnoreCase("omgpass"))
			{
				
				if (perso.getLevel() == 8000 && perso.getPrestige() == 100) // Si lvl max + prestige max passage au lvl omega
				{
					if (World.getExpLevel(perso.getLevel()).lvlomega >= 1)
					{
						if (perso.getxpOmega() >= World.getExpLevel(perso.getLevel()).expomega)
						{
							
							if (perso.getOmega() <= 10)
							{	
								perso.setLevel(perso.getLevel() + 1); 
								perso.setOmega(perso.getOmega() + 1);
								Database.getStatics().getPlayerData().updateOmega(perso);
								SocketManager.GAME_SEND_ASK(perso.getAccount().getGameClient(), perso); // ask le client
				            	SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(perso.getCurMap(), perso.getId()); // delete perso de la map
				            	SocketManager.GAME_SEND_ADD_PLAYER_TO_MAP(perso.getCurMap(), perso); // rajoute le perso
				            	SocketManager.GAME_SEND_ALTER_GM_PACKET(perso.curMap,perso);
								setRewardOmega(perso);
								SocketManager.PACKET_POPUP_DEPART(perso, "Vous �tes Omega " + perso.getOmega() + ".");
								SocketManager.GAME_SEND_MESSAGE_TO_ALL("Le joueur " + perso.getName() + "est pass� Omega " + perso.getOmega() + ".", "34E5EB");
							}
							else
								SocketManager.PACKET_POPUP_DEPART(perso, "Vous avez atteint le niveau Omega maximum.");
								return true;
						}
						else
							SocketManager.PACKET_POPUP_DEPART(perso, "Vous n'avez pas assez d'exp Omega pour passer au rang Omega sup�rieur.");
							return true;
					}
					else
						
						return true;
				}
				else 
					SocketManager.PACKET_POPUP_DEPART(perso, "Vous devez �tre <b>Niveau 8000 et Prestige 100</b> pour acc�der aux niveaux Omega.");
					return true;	
	            }
			if (msg.length() > 8 && msg.substring(1, 9).equalsIgnoreCase("infomega")) 
			{
				if (perso.getLevel() == 8000 && perso.getPrestige() == 100)
				{
					double expomega = World.getExpLevel(perso.getLevel()).expomega;
		            int lvlomega = World.getExpLevel(perso.getLevel()).lvlomega;
		            double xppomega = perso.getxpOmega();
		            SocketManager.PACKET_POPUP_DEPART(perso, "Tu es Omega: " + perso.getOmega() +
		            		"\n- Votre Exp Omega -> " + perso.getxpOmega() +
		            		"\n- Exp du niveau Omega " + (perso.getOmega() + 1) + " -> " + Main.world.getPersoXpOmegaMax(perso.getOmega()+1));
		            //perso.sendMessage("XP Omega (table player): " + xppomega);
//		            perso.sendMessage("XP Omega (table experience): " + expomega);
//		            perso.sendMessage("LVL Omega (table experience): " + lvlomega);
		            //player.sendMessage("Pour �tre niveau omega " + lvlomega + " il te faut " + expomega + " d'experience en plus.");
				} 
			else 
				{
				SocketManager.PACKET_POPUP_DEPART(perso, "Vous n'avez pas de niveau Omega.");
				}
				return true;
			}
			
			if (msg.length() > 2 && msg.substring(1, 3).equalsIgnoreCase("OP"))
			{	
				
				try 
				{
					//Si en dessous de 0 ou au dessus de 10
					if(Integer.parseInt(msg.substring(4, msg.length() - 1)) < 0 || Integer.parseInt(msg.substring(4, msg.length() - 1)) > 10)
					{
						SocketManager.PACKET_POPUP_DEPART(perso, "Tu peux pas aller en dessous de 0 ni au dessus de 10");
					}
					//Sinon on fais la commande
					else 
					{
						int OP =  Integer.parseInt(msg.substring(4, msg.length() - 1));
						perso.getOverPower().setOpLevel(OP);
						Database.getStatics().getPlayerData().updateOverPower(perso);
						SocketManager.PACKET_POPUP_DEPART(perso, "Tu es maintenant OverPower " + OP);
						
					}
					
				}
				//Si pas de valeur donné ou valeur incorrecte 
				catch (Exception e)
				{
					SocketManager.PACKET_POPUP_DEPART(perso, "Tu n'as pas indiqué de niveau OverPower");

				}
				
				
			}
			if (msg.length() > 13 && msg.substring(1, 14).equalsIgnoreCase("OverPowerInfo"))
			{
				OverPower op = perso.getOverPower();
				try 
				{
					String stringOverPower = "Le système OverPower augmente grandement les stats globales des mobs \n";
					
					for(int i = 0; i<=10; i++)
					{
						stringOverPower += op.InfoOpLvlDifficulty(i);
					}
					
					SocketManager.PACKET_POPUP_DEPART(perso,stringOverPower);
				}
				catch(Exception e)
				{
					SocketManager.PACKET_POPUP_DEPART(perso, "Commande bug : " + msg);
				}
			}
			if (msg.length() > 5 && msg.substring(1, 6).equalsIgnoreCase("MonOP"))
			{
				try 
				{
					SocketManager.PACKET_POPUP_DEPART(perso, "Tu es actuellement OverPower " + perso.getOverPower().getOpLevel());	
				}
				catch(Exception e)
				{
					SocketManager.PACKET_POPUP_DEPART(perso, "Commande bug" + msg);
				}
				
			}
			if (msg.length() > 4 && msg.substring(1, 5).equalsIgnoreCase("size"))
			{
				if(perso.getAccount().getPoints() >= 10)
				{
					perso.send("ka");
				}
				else
				{
					SocketManager.PACKET_POPUP_DEPART(perso, "Pas assez de Points Boutique pour juste ouvrir le panel (tu as moins de 10 PB)");
				}
				
			}
			if (msg.length() > 5 && msg.substring(1, 6).equalsIgnoreCase("monte"))
			{
				if(perso.getMount() == null) 
				{
					perso.sendMessage("Monture null");
				}
				else 
				{
					perso.toogleOnMount();
				}
			}
			if (msg.length() > 5 && msg.substring(1, 6).equalsIgnoreCase("start"))
			{
				if (perso.isInPrison()) 
					return true;
				if (perso.cantTP()) 
					return true;
				if (perso.getFight() != null) 
					return true;
				
		        if (perso.getFight() != null) return true;
		        perso.teleport((short) 25002, 298);
		        SocketManager.GAME_SEND_MESSAGE(perso, "Vous avez été téléporté au <b>start</b>." , "009900");
		        return true;
			}
			if (msg.length() > 6 && msg.substring(1, 7).equalsIgnoreCase("poutch"))
			{
				if (perso.isInPrison()) 
					return true;
				if (perso.cantTP()) 
					return true;
				if (perso.getFight() != null) 
					return true;
				
		        if (perso.getFight() != null) return true;
		        perso.teleport((short) 534, 372);
		        SocketManager.GAME_SEND_MESSAGE(perso, "Vous avez été téléporté au <b>poutch</b>." , "009900");
		        return true;
			}
			if (msg.length() > 6 && msg.substring(1, 7).equalsIgnoreCase("enclos"))
			{
				if (perso.isInPrison()) 
					return true;
				if (perso.cantTP()) 
					return true;
				if (perso.getFight() != null) 
					return true;
				
		        if (perso.getFight() != null) return true;
		        perso.teleport((short) 8747, 633);
		        SocketManager.GAME_SEND_MESSAGE(perso, "Vous avez été téléporté au <b>enclos</b>." , "009900");
		        return true;
			}
			if (msg.length() > 4 && msg.substring(1, 5).equalsIgnoreCase("pvm1"))
			{
				if (perso.isInPrison()) 
					return true;
				if (perso.cantTP()) 
					return true;
				if (perso.getFight() != null) 
					return true;
				
		        if (perso.getFight() != null) return true;
		        perso.teleport((short) 10134, 360);
		        SocketManager.GAME_SEND_MESSAGE(perso, "Vous avez été téléporté au <b>pvm1</b>." , "009900");
		        return true;
			}
			if (msg.length() > 4 && msg.substring(1, 5).equalsIgnoreCase("pvm2"))
			{
				if (perso.isInPrison()) 
					return true;
				if (perso.cantTP()) 
					return true;
				if (perso.getFight() != null) 
					return true;
				
		        if (perso.getFight() != null) return true;
		        perso.teleport((short) 10137, 360);
		        SocketManager.GAME_SEND_MESSAGE(perso, "Vous avez été téléporté au <b>pvm2</b>." , "009900");
		        return true;
			}
			
			if (msg.length() > 3 && msg.substring(1, 4).equalsIgnoreCase("vie"))
			{
				perso.fullPDV();
				if(perso.isOnline())
				{
				SocketManager.GAME_SEND_STATS_PACKET(perso);
				}
				SocketManager.GAME_SEND_MESSAGE(perso, "Vous avez récupérer Tous vos points de vie." , "009900");
				return true;
			}
			if (msg.length() > 6 && msg.substring(1, 7).equalsIgnoreCase("nodrop")) {
				if(perso.getCanDrop())
			    {
			      perso.setCanDrop(false);
			      perso.setCanDrop_items(true);
			      perso.setCanDrop_ressources(true);
			      SocketManager.GAME_SEND_MESSAGE(perso,"No drop on.");
			    }
			    else
			    {
			      perso.setCanDrop(true);
			      perso.setCanDrop_items(true);
			      perso.setCanDrop_ressources(true);
			      SocketManager.GAME_SEND_MESSAGE(perso,"No drop off.");
			    }
			    return true;
				
			}
		
			if (msg.length() > 7 && msg.substring(1, 8).equalsIgnoreCase("ipkamas")) {
				if(perso.getParty() == null || perso.getParty().getMaster() == null ||
						perso.getParty().getMaster().getId() != perso.getId())
				{
				SocketManager.GAME_SEND_MESSAGE(perso,"Mets toi Maître avant");	
				return true;
				}
				if(perso.getAccount().getSubscribeRemaining() == 0L){
	               	 SocketManager.GAME_SEND_MESSAGE(perso,"Réservé au V.I.P.","008000");	 
	                return true;	 
	                }
				 if(perso.ipKamas)
				    {
				      perso.ipKamas=false;
				      SocketManager.GAME_SEND_MESSAGE(perso,"Vous ne gagnerez plus tous les kamas de cette IP.");
				    }
				    else
				    {
				    	
				      perso.ipKamas=true;
				      SocketManager.GAME_SEND_MESSAGE(perso,"Vous allez maintenant gagner tous les kamas de cette IP.");
				      for(Player z : Main.world.getOnlinePlayers())
				      {
				        if(z==null)
				          continue;
				        if(z.getAccount().getCurrentIp().equals(perso.getAccount().getCurrentIp()))
				        {
				          if(z.ipKamas && z.getId() != perso.getId()) {
				        	  z.ipKamas=false;
						      SocketManager.GAME_SEND_MESSAGE(z,"Vous ne gagnerez plus tous les kamas de cette IP.");  
				          }
				        }
				      }
				    }
				    return true;
			} 
			if (msg.length() > 6 && msg.substring(1, 7).equalsIgnoreCase("ipdrop")) {
				if(perso.getParty() == null || perso.getParty().getMaster() == null ||
						perso.getParty().getMaster().getId() != perso.getId())
				{
				SocketManager.GAME_SEND_MESSAGE(perso,"Mets toi Ma�tre avant");	
				return true;
				}
				if(perso.getAccount().getSubscribeRemaining() == 0L){
	               	 SocketManager.GAME_SEND_MESSAGE(perso,"R�serv� au V.I.P.","008000");	 
	                return true;	 
	                }
				 if(perso.ipDrop)
				    {
				      perso.ipDrop=false;
				      SocketManager.GAME_SEND_MESSAGE(perso,"Vous ne gagnerez plus tous les drops de cette IP.");
				    }
				    else
				    {
				    	
				      perso.ipDrop=true;
				      SocketManager.GAME_SEND_MESSAGE(perso,"Vous allez maintenant gagner tous les drops de cette IP.");
				      for(Player z : Main.world.getOnlinePlayers())
				      {
				        if(z==null)
				          continue;
				        if(z.getAccount().getCurrentIp().equals(perso.getAccount().getCurrentIp()))
				        {
				          if(z.ipDrop && z.getId() != perso.getId()) {
				        	  z.ipDrop=false;
						      SocketManager.GAME_SEND_MESSAGE(z,"Vous ne gagnerez plus tous les drops de cette IP.");  
				          }
				        }
				      }
				    }
				    return true;
			}
			
			if (msg.length() > 6 && msg.substring(1, 7).equalsIgnoreCase("restat")) {
				if (perso.isInPrison()) {
					return true;
				}
				if (perso.getFight() != null) {
					return true;
				}
				Restat(perso);
			
			}
			
			if(Config.singleton.serverId == 1)
			if (msg.length() > 3 && msg.substring(1, 4).equalsIgnoreCase("pvp")) {
				if (perso.isInPrison()) {
					return true;
				}
				if (perso.getFight() != null) {
					return true;
				}
				if (System.currentTimeMillis() <  perso.getGameClient().timeLasttpcommande) {
	                perso.sendMessage("Tu dois attendre encore "+(System.currentTimeMillis() -  perso.getGameClient().timeLasttpcommande) / 1000+" seconde(s)");
	                return true;
	            }
				 perso.getGameClient().timeLasttpcommande =(System.currentTimeMillis()+1000);
				perso.teleport((short) 952, 297);
				return true;
			}
			if (msg.length() > 8 && msg.substring(1, 9).equalsIgnoreCase("marchand")) {
				if (perso.isInPrison()) {
					return true;
				}
				if (perso.getFight() != null) {
					return true;
				}
				if (System.currentTimeMillis() <  perso.getGameClient().timeLasttpcommande) {
	                perso.sendMessage("Tu dois attendre encore "+(System.currentTimeMillis() -  perso.getGameClient().timeLasttpcommande) / 1000+" seconde(s)");
	                return true;
	            }
				 perso.getGameClient().timeLasttpcommande =(System.currentTimeMillis()+1000);
				perso.teleport((short) 33, 283);
				return true;
			}
			if (msg.length() > 9 && msg.substring(1, 10).equalsIgnoreCase("joindelay")) {
                try {
                	int join = Integer.parseInt(msg.substring(11).replace("|", ""));
                	if(join < 1)
                	join = 1;
                	if(join > 10)
                    	join = 10;
                	perso.setJoindelay(join);
                	SocketManager.GAME_SEND_MESSAGE(perso,"joindelay "+perso.getJoindelay()+" seconde(s)"); 
                }catch(Exception e)
                {
                	SocketManager.GAME_SEND_MESSAGE(perso,".joindelay 1 � 10 en seconde");   
                  }
				return true;
			}
			if (msg.length() > 5 && msg.substring(1, 6).equalsIgnoreCase("event")) {
				if (perso.isInPrison()) {
					return true;
				}
				if (perso.getFight() != null) {
					return true;
				}
				if (System.currentTimeMillis() <  perso.getGameClient().timeLasttpcommande) {
	                perso.sendMessage("Tu dois attendre encore "+(System.currentTimeMillis() -  perso.getGameClient().timeLasttpcommande) / 1000+" seconde(s)");
	                return true;
	            }
				 perso.getGameClient().timeLasttpcommande =(System.currentTimeMillis()+1000);
				perso.teleport((short) 3336, 183);
				return true;
			}
			if(msg.length() > 9 && msg.substring(1, 10).equalsIgnoreCase("commandes"))
			{
				SocketManager.PACKET_POPUP_DEPART(perso, "<b>.start</b> - Permet de se t�l�porter au zaap."
						+ "\n<b>.marchand</b> - Permets de se t�l�porter � la map marchande."
						+ "\n<b>.staff</b> - Permet de voir les membres du staff connect\u00e9s."
						+ "\n<b>.boutique</b> - Permet d'acc�der � la boutique."
						+ "\n<b>.points</b> - Affiche ses points boutique."
						+ "\n<b>.all</b> - <b>.noall</b> - Permet d'envoyer un message \u00e0 tous les joueurs."
						+ "\n<b>.celldeblo</b> - Permet de t�l�porter � une cellule libre si vous �tes bloqu�s."
						+ "\n<b>.movemobs</b> - Permet de deplace un groupe de monstres."
						+ "\n<b>.banque</b> - Ouvrir la banque n�importe o�."
						+ "\n<b>.maitre</b> - Permet de cr�er une escouade et d'inviter toutes tes mules dans ton groupe."
						+ "\n<b>.window</b> - Permet de g�rer toutes vos mules en combat via la fen�tre du maitre."
						+ "\n<b>.tp</b> - Permet de t�l�porter tes personnages sur ta map actuelle."
						+ "\n<b>.tpgroupe</b> - Permets de t�l�porter ton groupe sur ta map actuelle."
						+ "\n<b>.pass</b> - Permet au joueur de passer automatiquement ses tours."
						+ "\n<b>.nodrop</b> - Vous emp�che de recevoir des items de monstres."
						+ "\n<b>.joindelay</b> - Permet de d�finir le delai d'attente avant de rejoindre le combat pour vos mules en groupe."
						+ "\n<b>.debug</b> - permet de debug le personnage lors d'un freeze."
						+ "\n<b>.hdv</b> - Permet d'acc�der au HDV."
						+ "\n<b>.vip</b> - Affiche les privil�ges VIP."
						+ "\n<b>.pvm1</b> - Permet de se t�l�porter au pvm1."
						+ "\n<b>.pvm2</b> - Permet de se t�l�porter au pvm2."
						+ "\n<b>.parse</b> - Permet d'afficher son ParseToGM."
						+ "\n<b>.ornements</b> - Permet d'afficher le panel d'ornement."
						+ "\n<b>.prestige</b> - Permet de monter en prestige."
						+ "\n<b>.infoprestige</b> - Permet de s'informer sur son prestige."
						+ "\n<b>.omgpass</b> - Permet de monter son niveau Omega."
						+ "\n<b>.infomega</b> - Permet de s'informer sur ses Omega."
						+ "\n<b>.size</b> - Permet d'afficher le panel de size.");
				return true;
			}
			
			else {
				if (msg.length() > 5 && msg.substring(1, 6).equalsIgnoreCase("infos")) {
					long uptime = System.currentTimeMillis() - Config.getInstance().startTime;
					final int jour = (int) (uptime / 86400000L);
					uptime %= 86400000L;
					final int hour = (int) (uptime / 3600000L);
					uptime %= 3600000L;
					final int min = (int) (uptime / 60000L);
					uptime %= 60000L;
					final int sec = (int) (uptime / 1000L);
					int fake = 0;
					 long calcul=System.currentTimeMillis()-Config.getInstance().startTime;
					 if(calcul>1200000) {
						 if(Config.getInstance().serverId == 1 )
				        	fake = 100;
						 if(Config.getInstance().serverId == 22|| Config.getInstance().serverId == 7)
					        	fake = 20;
					 }
					final int nbPlayer = Main.world.getOnlinePlayers().size()+fake;
					//final int nbPlayerIp = Main.gameServer.getPlayersNumberByIp();
					final int maxPlayer = Main.Max_players+fake;
					String mess = "<b>" + Config.getInstance().name + "</b>\n" + "Uptime : " + jour + "j " + hour + "h "
							+ min + "m " + sec + "s.";
					if (nbPlayer > 0) {
						mess = String.valueOf(mess) + "\nJoueurs en ligne : " + nbPlayer;
					}
				//	if (nbPlayerIp > 0)  mess = String.valueOf(mess) + "\nJoueurs uniques en ligne : " + nbPlayerIp;
					
					if (maxPlayer > 0) {
						mess = String.valueOf(mess) + "\nRecord de connexion : " + maxPlayer;
					}
					SocketManager.GAME_SEND_MESSAGE(perso, mess);
					return true;
					
				}
				i ++;
				if(Config.singleton.serverId == 1 && i == 8) {
				SocketManager.GAME_SEND_MESSAGE(perso,
						"Les commandes disponibles sont  :\n<b>.infos</b> - Permet d'obtenir des informations sur le serveur."
						);
				i = 0;
				}/*else
				{
					SocketManager.GAME_SEND_MESSAGE(perso,
							"Les commandes disponibles sont  :\n<b>.infos</b> - Permet d'obtenir des informations sur le serveur."
							//+ "\n<b>.start</b> - Permet de se t�l�porter au zaap."
							+ "\n<b>.marchand</b> - Permets de se t�l�porter � la map marchande."
							+ "\n<b>.staff</b> - Permet de voir les membres du staff connect\u00e9s."
							+ "\n<b>.boutique</b> - Permet d'acc�der � la boutique."
							+ "\n<b>.points</b> - Affiche ses points boutique."
							+ "\n<b>.all</b> - <b>.noall</b> - Permet d'envoyer un message \u00e0 tous les joueurs."
							+ "\n<b>.celldeblo</b> - Permet de t�l�porter � une cellule libre si vous �tes bloqu�s."
							+ "\n<b>.movemobs</b> - Permet de deplace un groupe de monstres."
							+ "\n<b>.banque</b> - Ouvrir la banque n�importe o�."
							+ "\n<b>.maitre</b> - Permet de cr�er une escouade et d'inviter toutes tes mules dans ton groupe."
							+ "\n<b>.window</b> - Permet de g�rer toutes vos mules en combat via la fen�tre du maitre."
							+ "\n<b>.tp</b> - Permet de t�l�porter tes personnages sur ta map actuelle."
							+ "\n<b>.tpgroupe</b> - Permets de t�l�porter ton groupe sur ta map actuelle."
							+ "\n<b>.pass</b> - Permet au joueur de passer automatiquement ses tours."
							+ "\n<b>.nodrop</b> - Vous emp�che de recevoir des items de monstres."
							+ "\n<b>.debug</b> - permet de debug le personnage lors d'un freeze."
							+ "\n<b>.hdv</b> - Permet d'acc�der au HDV."
							+ "\n<b>.vip</b> - Affiche les privil�ges VIP."
							);	
				}*/
				return true;
			
			}
			}
		}
			
		
	
	public static void setRewardPrestige(Player player) {
    	// Ici n'importe quel prestige //
    	player.setCapital(player.prestige * 1000000);
    	
    	
    	// Le switch player prestige = quel num�ro de prestige, le case = le num�ro de prestige, player additem = l'ajout d'un item par ID et quantit�
    	switch(player.prestige) {
    	
    		
    	case 10: 
   
    		player.AddItem(10688,1);
    		break;
    	case 20:
    		player.AddItem(100092,1);
    		break;
    	case 25: 
    		player.AddItem(10687,1);
    		break;
    	case 30:
    		player.AddItem(100093,1);
    		break;
    	case 50:
    		player.AddItem(10686,1);
    		player.AddItem(200923, 1);
    		player.AddItem(200922, 1);
    		break;
    	case 75 : 
    		player.AddItem(10685,1);    
    		break;
    	case 100: 
    		player.AddItem(15035, 1);	
    		break;
    	
    	}
        SocketManager.GAME_SEND_STATS_PACKET(player);
    }
    
    public static void setRewardOmega(Player player)
    {
    	//player.set_capital(player.omega * 1000000);
    	
    	switch(player.omega)
    	{	
	    	case 1: 
	    		player.AddItem(10688,1);
	    	case 2:
	    		player.AddItem(100092,1);
	    	case 3: 
	    		player.AddItem(10687,1);
	    	case 4:
	    		player.AddItem(100093,1);
	    	case 5:
	    		player.AddItem(10686,1);
	    	case 6 : 
	    		player.AddItem(10685,1);    		
	    	case 7: 
	    		player.AddItem(15035, 1);	
    	break;
    	}
        	SocketManager.GAME_SEND_STATS_PACKET(player);
    }
 
    public static void resetPlayer(Player player) {
    	
		if (player.getLevel() >= 100)
			player.getStats().addOneStat(Constant.STATS_ADD_PA, -1);
		player.setExp(0);
		player.setLevel(1);	
		player.set_spellPts(0);
		player.setCapital(0);
        player.setSpells(Constant.getStartSorts(player.getClasse()));
	
		
		SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(player.getCurMap(), player.getId());
		SocketManager.GAME_SEND_ADD_PLAYER_TO_MAP(player.getCurMap(), player);
		SocketManager.GAME_SEND_STATS_PACKET(player);
		SocketManager.GAME_SEND_ASK(player.getGameClient(), player);
		SocketManager.GAME_SEND_SPELL_LIST(player);
        Database.getStatics().getPlayerData().update(player);

    }
    
    
    public static void Restat(Player player) {
    	double capitalTotalActuel = (player.getStats().get(Constant.STATS_ADD_SAGE)) + player.getStats().get(Constant.STATS_ADD_AGIL) + player.getStats().get(Constant.STATS_ADD_CHAN) + player.getStats().get(Constant.STATS_ADD_FORC) +player.getStats().get(Constant.STATS_ADD_INTE) + player.getStats().get(Constant.STATS_ADD_VITA);
    	double curPer = player.get_pdvper();

          if (player.getStatsParcho().getEffect(125) != 0 || player.getStatsParcho().getEffect(124) != 0 || player.getStatsParcho().getEffect(118) != 0
                  || player.getStatsParcho().getEffect(119) != 0 || player.getStatsParcho().getEffect(126) != 0 || player.getStatsParcho().getEffect(123) != 0) {
              player.getStats().addOneStat(125, -player.getStats().getEffect(125) + player.getStatsParcho().getEffect(125));
              player.getStats().addOneStat(124, -player.getStats().getEffect(124) + player.getStatsParcho().getEffect(124));
              player.getStats().addOneStat(118, -player.getStats().getEffect(118) + player.getStatsParcho().getEffect(118));
              player.getStats().addOneStat(123, -player.getStats().getEffect(123) + player.getStatsParcho().getEffect(123));
              player.getStats().addOneStat(119, -player.getStats().getEffect(119) + player.getStatsParcho().getEffect(119));
              player.getStats().addOneStat(126, -player.getStats().getEffect(126) + player.getStatsParcho().getEffect(126));
              player.addCapital((player.getLevel() - 1) * 5 - player.get_capital());
              SocketManager.GAME_SEND_STATS_PACKET(player);
          } else if (player.getStats().getEffect(125) == 101 && player.getStats().getEffect(124) == 101 && player.getStats().getEffect(118) == 101
                  && player.getStats().getEffect(123) == 101 && player.getStats().getEffect(119) == 101 && player.getStats().getEffect(126) == 101) {
              player.getStats().addOneStat(125, -player.getStats().getEffect(125) + 101);
              player.getStats().addOneStat(124, -player.getStats().getEffect(124) + 101);
              player.getStats().addOneStat(118, -player.getStats().getEffect(118) + 101);
              player.getStats().addOneStat(123, -player.getStats().getEffect(123) + 101);
              player.getStats().addOneStat(119, -player.getStats().getEffect(119) + 101);
              player.getStats().addOneStat(126, -player.getStats().getEffect(126) + 101);
              player.getStatsParcho().getMap().clear();
              player.getStatsParcho().addOneStat(125, 101);
              player.getStatsParcho().addOneStat(124, 101);
              player.getStatsParcho().addOneStat(118, 101);
              player.getStatsParcho().addOneStat(123, 101);
              player.getStatsParcho().addOneStat(119, 101);
              player.getStatsParcho().addOneStat(126, 101);
              player.addCapital((player.getLevel() - 1) * 5 - player.get_capital());
              SocketManager.GAME_SEND_STATS_PACKET(player);
          } else {
              player.getStats().addOneStat(125, -player.getStats().getEffect(125));
              player.getStats().addOneStat(124, -player.getStats().getEffect(124));
              player.getStats().addOneStat(118, -player.getStats().getEffect(118));
              player.getStats().addOneStat(123, -player.getStats().getEffect(123));
              player.getStats().addOneStat(119, -player.getStats().getEffect(119));
              player.getStats().addOneStat(126, -player.getStats().getEffect(126));
              player.setCapital(capitalTotalActuel + player.get_capital()); // Ici on set exactement ce que la personne avait (m�me si y'a un give de capital)
              //player.addCapital((player.getLevel() - 1) * 5 - player.get_capital()); Ancienne m�thode
              player.getStatsParcho().getMap().clear();
              SocketManager.GAME_SEND_STATS_PACKET(player);
          }
          double newPDV = player.getMaxPdv() * curPer / 100;
          player.setPdv(newPDV);
          SocketManager.GAME_SEND_STATS_PACKET(player);
          player.sendMessage("Reset des caract�ristiques.");
          player.verifEquiped();
    	
    }
    public static void RestatByLevel(Player player) {
    	//long capitalTotalActuel = (player.getBaseStats().get(Constant.STATS_ADD_SAGE) * 100) + player.getBaseStats().get(Constant.STATS_ADD_AGIL) + player.getBaseStats().get(Constant.STATS_ADD_CHAN) + player.getBaseStats().get(Constant.STATS_ADD_FORC) +player.getBaseStats().get(Constant.STATS_ADD_INTE) + player.getBaseStats().get(Constant.STATS_ADD_VITA);
    	double curPer = player.get_pdvper();

          if (player.getStatsParcho().getEffect(125) != 0 || player.getStatsParcho().getEffect(124) != 0 || player.getStatsParcho().getEffect(118) != 0
                  || player.getStatsParcho().getEffect(119) != 0 || player.getStatsParcho().getEffect(126) != 0 || player.getStatsParcho().getEffect(123) != 0) {
              player.getStats().addOneStat(125, -player.getStats().getEffect(125) + player.getStatsParcho().getEffect(125));
              player.getStats().addOneStat(124, -player.getStats().getEffect(124) + player.getStatsParcho().getEffect(124));
              player.getStats().addOneStat(118, -player.getStats().getEffect(118) + player.getStatsParcho().getEffect(118));
              player.getStats().addOneStat(123, -player.getStats().getEffect(123) + player.getStatsParcho().getEffect(123));
              player.getStats().addOneStat(119, -player.getStats().getEffect(119) + player.getStatsParcho().getEffect(119));
              player.getStats().addOneStat(126, -player.getStats().getEffect(126) + player.getStatsParcho().getEffect(126));
              player.addCapital((player.getLevel() - 1) * 5 - player.get_capital());
              SocketManager.GAME_SEND_STATS_PACKET(player);
          } else if (player.getStats().getEffect(125) == 101 && player.getStats().getEffect(124) == 101 && player.getStats().getEffect(118) == 101
                  && player.getStats().getEffect(123) == 101 && player.getStats().getEffect(119) == 101 && player.getStats().getEffect(126) == 101) {
              player.getStats().addOneStat(125, -player.getStats().getEffect(125) + 101);
              player.getStats().addOneStat(124, -player.getStats().getEffect(124) + 101);
              player.getStats().addOneStat(118, -player.getStats().getEffect(118) + 101);
              player.getStats().addOneStat(123, -player.getStats().getEffect(123) + 101);
              player.getStats().addOneStat(119, -player.getStats().getEffect(119) + 101);
              player.getStats().addOneStat(126, -player.getStats().getEffect(126) + 101);
              player.getStatsParcho().getMap().clear();
              player.getStatsParcho().addOneStat(125, 101);
              player.getStatsParcho().addOneStat(124, 101);
              player.getStatsParcho().addOneStat(118, 101);
              player.getStatsParcho().addOneStat(123, 101);
              player.getStatsParcho().addOneStat(119, 101);
              player.getStatsParcho().addOneStat(126, 101);
              player.addCapital((player.getLevel() - 1) * 5 - player.get_capital());
              SocketManager.GAME_SEND_STATS_PACKET(player);
          } else {
              player.getStats().addOneStat(125, -player.getStats().getEffect(125));
              player.getStats().addOneStat(124, -player.getStats().getEffect(124));
              player.getStats().addOneStat(118, -player.getStats().getEffect(118));
              player.getStats().addOneStat(123, -player.getStats().getEffect(123));
              player.getStats().addOneStat(119, -player.getStats().getEffect(119));
              player.getStats().addOneStat(126, -player.getStats().getEffect(126));
              //player.setCapital(capitalTotalActuel + player.get_capital()); // Ici on set exactement ce que la personne avait (m�me si y'a un give de capital)
              player.addCapital((player.getLevel() - 1) * 5 - player.get_capital());
              player.getStatsParcho().getMap().clear();
              SocketManager.GAME_SEND_STATS_PACKET(player);
          }
          double newPDV = player.getMaxPdv() * curPer / 100;
          player.setPdv(newPDV);
          SocketManager.GAME_SEND_STATS_PACKET(player);
          player.sendMessage("Reset des caract�ristiques.");
          player.verifEquiped();
    	
    }
   }


