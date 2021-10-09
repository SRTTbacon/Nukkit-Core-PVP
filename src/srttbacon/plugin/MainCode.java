package srttbacon.plugin;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockStairs;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.entity.passive.EntityVillager;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause;
import cn.nukkit.event.entity.EntityShootBowEvent;
import cn.nukkit.event.entity.ProjectileHitEvent;
import cn.nukkit.event.inventory.CraftItemEvent;
import cn.nukkit.event.inventory.EnchantItemEvent;
import cn.nukkit.event.inventory.InventoryClickEvent;
import cn.nukkit.event.inventory.InventoryCloseEvent;
import cn.nukkit.event.inventory.InventoryOpenEvent;
import cn.nukkit.event.level.WeatherChangeEvent;
import cn.nukkit.event.player.PlayerDeathEvent;
import cn.nukkit.event.player.PlayerDropItemEvent;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.event.player.PlayerInteractEntityEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerInteractEvent.Action;
import cn.nukkit.event.player.PlayerItemHeldEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.player.PlayerRespawnEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.inventory.EnchantInventory;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.item.ItemDye;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.InventoryTransactionPacket;
import cn.nukkit.network.protocol.MoveEntityAbsolutePacket;
import cn.nukkit.network.protocol.PlayerActionPacket;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import cn.nukkit.network.protocol.SetEntityLinkPacket;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.DyeColor;
import cn.nukkit.utils.TextFormat;
import srttbacon.plugin.Achievement.Achievement_Main;
import srttbacon.plugin.Achievement.Battle_Achievement;
import srttbacon.plugin.Class.Block_Replace;
import srttbacon.plugin.Class.Image_To_Pixel;
import srttbacon.plugin.Class.PlaySound;
import srttbacon.plugin.Class.Player_Data;
import srttbacon.plugin.Class.ScoreBoard_Show;
import srttbacon.plugin.Class.System_Command;
import srttbacon.plugin.Class.Window;
import srttbacon.plugin.Map.Map_Not_Break_Pos;
import srttbacon.plugin.Map.Map_Setting;
import srttbacon.plugin.Map.Ore_Restore;
import srttbacon.plugin.Skills.Acrobat;
import srttbacon.plugin.Skills.Archer;
import srttbacon.plugin.Skills.Scout_Skill;
import srttbacon.plugin.Skills.Skill_Main;

public class MainCode extends PluginBase implements Listener
{
	public static Random r = new Random();
	private Map<String, Long> onChair = new HashMap<>();
	private Map<String, Long> doubleTap = new HashMap<>();
	private Map<String, Long> tagblock = new HashMap<>();
	private List<String> disabled = new ArrayList<>();
	private static final int[] faces = new int[]{90, 270, 180, 0, 90, 270, 180, 0};
	Server Main_Server;
	//起動時必要な処理
	@Override
	public void onEnable()
	{
		this.getServer().getPluginManager().registerEvents(this, this);
        Main_Server = this.getServer();
        Sub_Code.Server_Main = Main_Server;
        Image_To_Pixel.Init();
        Skill_Main.Init();
        Battle_Achievement.Init();
        new Loop().runTaskTimer(this.getServer().getPluginManager().getPlugin(getFullName()), 0, 20);
		Sub_Code.Plugin_Dir = getDataFolder().getParent();
		getLogger().info("メインプラグインは、正常に起動されました。");
		Save_Load.Load_All_Player_Data();
        ScoreBoard_Show.Show_All();
        Save_Load.Load_All_Maps();
        Save_Load.Load_Main();
		Sub_Code.Voice_Type_List.add("Hanai_Risa");
		Sub_Code.Voice_Type_List.add("Mashiro_Miho");
		Sub_Code.Voice_Type_List.add("Ochiai_Natsuki");
		Sub_Code.Voice_Type_List.add("Yakata_Miki");
	}
	//プラグインを無効にしたとき実行
	@Override
	public void onDisable()
	{
		Save_Load.Save_All_Player_Data();
		ScoreBoard_Show.Hide_All();
		//Save_Load.Save_All_Maps();
		Save_Load.Save_Main();
	}
	//プレイヤーが参加したら実行
	@EventHandler
	public void Join(PlayerJoinEvent e)
	{
		Player p = e.getPlayer();
		String name = p.getName();
		String Message = "";
		int a = r.nextInt(10);
		if (a == 0)
			Message = "いらっしゃい！";
		else if (a == 1)
			Message = "こんにちは！！！";
		else if (a == 2)
			Message = "゜*。(*´Д`)。*°";
		else if (a == 3)
			Message = "v(｡･ω･｡)ｨｪｨ♪";
		else if (a == 4)
			Message = "ようこそ！";
		else if (a == 5)
			Message = "！！！";
		else if (a == 6)
			Message = "ご参加頂きありがとうございます！";
		else if (a == 7)
			Message = "おかえりなさい。";
		else if (a == 8)
			Message = "";
		else if (a == 9)
			Message = "";
		if (name.equalsIgnoreCase("GuminoAme") || name.equalsIgnoreCase("SRTTbacon7839"))
			Message = TextFormat.AQUA + "【OP】";
		e.setJoinMessage(TextFormat.GREEN + name + "さんが参加しました。" + Message);
		//プレイヤーデータをロードし、スコアボードに表示
		Save_Load.Load_Player_Data(p);
		ScoreBoard_Show.Show_Player(Sub_Code.Get_Player_Data(p));
		Sub_Code.AddAllText(p);
		if (!p.isOp())
		{
			p.getInventory().clearAll();
			p.removeAllEffects();
			Lobby.Teleport(p);
		}
     }
	 //手の持つアイテムが変更されたら実行
	 @EventHandler
	 public void onItemHeld(PlayerItemHeldEvent event)
	 {
		 Player_Data p_data = Sub_Code.Get_Player_Data(event.getPlayer());
		 if (p_data.Skill_Now == "Archer" && p_data.IsStartupSkill >= 0)
		 {
			 p_data.player.getInventory().setItem(p_data.IsStartupSkill, p_data.Before_Skill_Item);
			 p_data.IsStartupSkill = -1;
			 p_data.Before_Skill_Item = null;
		 }
	     Scout_Skill.onItemHeld(event);
	 }
	 //ショップのボタンをクリックしたら実行
	 @EventHandler
	 public void onFormResponse(PlayerFormRespondedEvent e)
	 {
		 //処理内容は別クラスで指定
		 Window.onFormResponse(e);
	 }
	 //エンティティを右クリックしたら実行
	 @EventHandler
	 public void Entity_Right_Click(PlayerInteractEntityEvent e)
	 {
		 //処理は別クラスに記述
		 Player_Data Click_Player = Sub_Code.Get_Player_Data(e.getPlayer());
		 Window.Entity_Right_Click(e, Click_Player);
		 Skill_Main.Player_Interact(e, Click_Player);
	 }
	 @EventHandler
	 public void onPacket(DataPacketReceiveEvent e)
	 {
		 if (e instanceof DataPacketReceiveEvent)
		 {
		     if (((DataPacketReceiveEvent)e).getPacket() instanceof InventoryTransactionPacket)
		     {
		         InventoryTransactionPacket pk = (InventoryTransactionPacket)((DataPacketReceiveEvent)e).getPacket();
		         if (pk.transactionType == 3)
		         {
		             Player_Data p_data = Sub_Code.Get_Player_Data(e.getPlayer());
		             p_data.CPS++;
		         }
		     }
		 }
	 }
	 //右クリック
	 @EventHandler
	 public void Interact(PlayerInteractEvent e)
	 {
		 if (e.getItem() == null)
			 return;
		 //スキルの使用
		 Skill_Main.Interact(e);
		 Player p = e.getPlayer();
		 //ロビーの看板
		 Lobby.Sign_Board_Click(e);
		 Item item = e.getItem();
		 //マップに変更を与えるコマンドを使用可能に
		 Map_Setting.Right_Click(e);
		 //称号の看板
		 Achievement_Main.onInteract(e);
		 if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR))
		 {
			 Player_Data p_data = Sub_Code.Get_Player_Data(p);
			 if (p.getInventory().getItemInHand().getId() == Item.BOW && p_data.Skill_Now == "Archer" && p_data.CT <= 0)
			 {
				 if (p_data.Archer_Type == Archer.Archer_Type.テレポート && !p.isSneaking())
					 return;
				 //ItemBow item_back = (ItemBow)p.getInventory().getItemInHand().clone();
	             p_data.Before_Skill_Item = p.getInventory().getItemInHand().clone();
				 p_data.IsStartupSkill = p.getInventory().getHeldItemIndex();
				 PlaySound.PlaySoundAll(p_data, "system.skill_active", p.getPosition());
				 /*Enchantment enc = Enchantment.get(Enchantment.ID_BOW_INFINITY);
				 item_back.addEnchantment(enc);
				 p_data.player.getInventory().setItem(p_data.player.getInventory().getHeldItemIndex(), item_back);*/
			 }
		     else if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			 {
				 if (Game.Map_Now ==null && p.isOp() && p.isSneaking() && item.getId() == Item.AIR)
				 {
					 //OPのみ、アイテムをシフトしながら右クリックでそのブロックをアイテム化させる
					 PlayerInventory Invent = p.getInventory();
					 Invent.setItem(Invent.getHeldItemIndex(), e.getBlock().toItem());
			     }
				 else if (Game.Map_Now != null)
				 {
					 Sub_Code.Team Touch_Team = Sub_Code.Team.None;
					 Block block = e.getBlock();
					 //自軍のコアを右クリック
					 if (p.isSneaking() && block.getId() == Block.END_STONE)
					 {
						 if (Game.Map_Now.Blue_Team_Core_Position != null)
						 {
							 if (Game.Map_Now.Blue_Team_Core_Position.x == (int)block.x && Game.Map_Now.Blue_Team_Core_Position.y == (int)block.y && Game.Map_Now.Blue_Team_Core_Position.z == (int)block.z)
								 Touch_Team = Sub_Code.Team.Blue;
						 }
						 if (Game.Map_Now.Red_Team_Core_Position != null)
						 {
							 if (Game.Map_Now.Red_Team_Core_Position.x == (int)block.x && Game.Map_Now.Red_Team_Core_Position.y == (int)block.y && Game.Map_Now.Red_Team_Core_Position.z == (int)block.z)
								 Touch_Team = Sub_Code.Team.Red;
						 }
						 if (Touch_Team == Sub_Code.Team.None)
							 return;
						 if (p_data.Team_Color == Touch_Team)
						 {
							 FormWindowSimple gui = new FormWindowSimple("職業ショップ", "所持PT:" + p_data.PT);
							 for (int Number = 0; Number < Skill_Main.Skill_Name.size(); Number++)
							 {
							     if (Skill_Main.Skill_IsEnable.get(Number))
								 {
									 if (p_data.Has_Skills.contains(Skill_Main.Skill_Name.get(Number)))
									     gui.addButton(new ElementButton(Skill_Main.Skill_Name.get(Number) + " ： " + Skill_Main.Skill_Cost.get(Number) + "PT(購入済み)"));
								     else
									     gui.addButton(new ElementButton(Skill_Main.Skill_Name.get(Number) + " ： " + Skill_Main.Skill_Cost.get(Number) + "PT"));
								 }
							 }
							 PlaySound.PlaySoundOne(p_data, "system.menu", p.getPosition());
							 p.showFormWindow(gui);
						 }
					 }
				 }
			 }
		 }
	 }
	 @EventHandler
	 public void Place(BlockPlaceEvent e) 
	 {
		 Player p = e.getPlayer();
		 Block block = e.getBlock();
		 if (Skill_Main.Not_Put_Item(e.getItem()))
		    e.setCancelled();
		 Map_Setting.Block_Place(p, block);
		 if (p.isOp())
		 {
			 Player_Data p_data = Sub_Code.Get_Player_Data(p);
			 //Builderの称号処理
			 if (p_data.Skill_Now == "Builder" && !p_data.Has_Achievements.contains(Battle_Achievement.Get_Skill_Achievement_Name("Builder")))
			 {
				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							Thread.sleep(100);
						}
						catch (Exception e1)
						{
							//例外処理なし
						}
						boolean IsExist = false;
						for (Item item: p.getInventory().getContents().values())
						{
							if (item.getCustomName().contains("Builder") && !item.getCustomName().contains("3x3"))
							{
								IsExist = true;
								return;
							}
						}
						if (!IsExist)
							Sub_Code.Skill_Achievement(p_data, "Builder");
					}
				}).start();
			 }
			 for (int Number = 0; Number < Sub_Code.Block_Replace_List.size(); Number++)
			 {
				 //OPのみ、マップの変更
				 if (Sub_Code.Block_Replace_List.get(Number).Player_Name.getName() == p.getName())
				 {
					 Block_Replace Temp = Sub_Code.Block_Replace_List.get(Number);
					 Temp.End_Position = new Vector3(block.x, block.y, block.z);
					 Temp.Block_Index = block.getId();
					 Sub_Code.Block_Replace_List.set(Number, Temp);
					 p.sendMessage("終了地点を設定しました。/Set_Replaceコマンドを実行してください。");
					 break;
				 }
			 }
		 }
		 if (Game.Map_Now != null)
		 {
			Player_Data p_data = Sub_Code.Get_Player_Data(p);
			if (p_data.IsLobbyMode)
			{
			    e.setCancelled();
				return;
			}
			 //破壊不可能な領域の場合はキャンセル
			 for (Map_Not_Break_Pos NotBreakPos: Game.Map_Now.IsNotBreakPos)
			 {
				 if (block.x >= NotBreakPos.Start_Pos.x && block.x <= NotBreakPos.End_Pos.x && block.z >= NotBreakPos.Start_Pos.z && block.z <= NotBreakPos.End_Pos.z)
				 {
					 e.setCancelled();
					 return;
				 }
			 }
			 //プレイヤーが置いたブロックは復活しないように設定
			 if (block.getId() == Block.QUARTZ_STAIRS || block.getId() == Block.FENCE || block.getId() == Block.END_STONE || block.getId() == Block.CACTUS || block.getId() == Block.STONE || block.getId() == Block.PACKED_ICE)
			 {
				 block.setDamage(1);
			 }
			 if (p_data.Skill_Now == "Builder" && !p_data.Has_Achievements.contains(Battle_Achievement.Skill_All_Achievement.get(3).Name))
			 {
				 boolean IsExist = false;
				 for (Item item: p.getInventory().getContents().values())
				 {
					 if (item == e.getItem())
						 continue;
					 if (item.getCustomName().contains("Builder") && !item.getCustomName().contains("3x3"))
					 {
					     IsExist = true;
						 return;
					 }
				 }
				 if (!IsExist)
				     Sub_Code.Skill_Achievement(p_data, "Builder");
			 }
		 }
		 else if (!p.isOp())
			 e.setCancelled();
	 }
	 //クラフト時に発生
	 @EventHandler
	 public void CraftEvent(CraftItemEvent e)
	 {
		 Item item = e.getRecipe().getResult();
		 Player p = e.getPlayer();
		 //以下のアイテムはクラフト不可能
		 if (item.getId() == Item.BUCKET)
		 {
			 e.setCancelled();
			 p.sendMessage(TextFormat.RED + "バケツを作ることはできません。");
		 }
     }
	 //プレイヤーが移動したら実行
	 @EventHandler
	 public void Move(PlayerMoveEvent e)
	 {
		 Skill_Main.Move(e);
	 }
	 //Qボタンでアイテムを投げたら実行
	 @EventHandler
	 public static void ItemDrop(PlayerDropItemEvent e)
	 {
		Skill_Main.ItemDrop(e);	
	 }
	 @EventHandler
	 public void onWeatherChanger(WeatherChangeEvent e)
	 {
		 //e.getLevel().setRaining(false);
	 }
	 //コマンドを実行
	 @Override
	 public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	 {
		   Player p = (cn.nukkit.Player)sender;
		   //各クラスのコマンドも実行
	       Image_To_Pixel.onCommand(sender, cmd, commandLabel, args);
	       Skill_Main.Command(cmd.getName(), p);
	       Map_Setting.Map_Commands(p, cmd, args);
	       Achievement_Main.Commands(p, cmd, args);
	       Lobby.Commands(p, cmd, args);
		   System_Command.onCommand(p, cmd, args);
	       //所持TPのランキングを表示
	       if (cmd.getName().equalsIgnoreCase("Top-PT"))
	       {
	    	   File file = new File(Sub_Code.Plugin_Dir + "/Configs");
	           File files[] = file.listFiles();
	           ArrayList<String> Top_Name = new ArrayList<String>();
	           ArrayList<Integer> Top_PT = new ArrayList<Integer>();
	           for (int i = 0; i < files.length; i++)
	           {
	        	   if (files[i].isFile())
	        	   {
		        	   String basename = files[i].getName();
		        	   String name_only = basename.substring(0, basename.lastIndexOf('.'));
		        	   try 
		        	   {
						   BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(files[i]), "UTF-8"));
						   String line;
						   while ((line = br.readLine()) != null)
						   {
							   String Setting_Name = line.substring(0, line.indexOf(':'));
				               String Value = line.substring(line.indexOf(':') + 1);
				               if (Setting_Name.contains("Player_PT"))
				               {
				                   int PT = Integer.valueOf(Value);
					               boolean Add_between = false;
					               for (int Index = 0; Index < Top_PT.size(); Index++)
					               {
					            	   if (Top_PT.get(Index) < PT)
					            	   {
					            		   Top_Name.add(Index, name_only);
					            		   Top_PT.add(Index, PT);
					            		   Add_between = true;
					            		   break;
					            	   }
					               }
					               if (!Add_between)
					               {
					            	   Top_Name.add(name_only);
					            	   Top_PT.add(PT);
					               }
					               break;
				               }
						   }
						   br.close();
					   }
		        	   catch (Exception e)
		        	   {
						   Sub_Code.Server_Main.getLogger().info(e.getMessage());
		        	   }
	        	   }
	           }
	           if (Top_PT.size() > 0)
	           {
	        	   p.sendMessage(TextFormat.GREEN + "---所持TPトップ5---");
		           for (int Number = 0; Number < Top_PT.size(); Number++)
		        	   if (Number < 5)
		        		   p.sendMessage(TextFormat.GREEN + "" + (Number + 1) + ":" + TextFormat.WHITE + Top_Name.get(Number) + TextFormat.GREEN + " - " + Top_PT.get(Number) + "PT");
	           }
	           else
	        	   p.sendMessage(TextFormat.GREEN + "プレイヤーを取得できませんでした。");
	       }
	       //プレイヤーの持ち物をすべて消去
	       else if (cmd.getName().equalsIgnoreCase("Clear_All"))
	       {
	    	   for (Player player: Server.getInstance().getOnlinePlayers().values())
	    	   {
	    		   player.getInventory().clearAll();
	    		   player.removeAllEffects();
	    	   }
	       }
		   //スペクターモードに移行
		   else if (cmd.getName().equalsIgnoreCase("Set_Spectator") && p.isOp())
		   {
		       p.setGamemode(Player.SPECTATOR);
			   p.sendMessage(TextFormat.GREEN + "スペクテイターモードになりました。");
		   }
	       //マップレベルを変更(大変危険な行為なので管理者以外は実行不可)
	       else if (cmd.getName().equalsIgnoreCase("Change_Level"))
	       {
	    	   if (!p.getName().contains("SRTTbacon"))
	    	   {
	    		   p.sendMessage(TextFormat.RED + "管理者以外はこのコマンドを実行できません。危険過ぎます。");
	    		   return true;
	    	   }
	    	   if (p.level.getName().contains("main_back"))
	    	   {
	    		   Sub_Code.Server_Main.loadLevel("main");
				   Sub_Code.Server_Main.setDefaultLevel(Sub_Code.Server_Main.getLevelByName("main"));
				   for (Player player: Server.getInstance().getOnlinePlayers().values())
				   {
					   player.setLevel(Sub_Code.Server_Main.getLevelByName("main"));
					   player.switchLevel(Sub_Code.Server_Main.getLevelByName("main"));
				   }
				   Sub_Code.Server_Main.unloadLevel(Sub_Code.Server_Main.getLevelByName("main_back"));
				   p.sendMessage("マップを変更しました。 main_back -> main");
	    	   }
	    	   else
	    	   {
	    		   Sub_Code.Server_Main.loadLevel("main_back");
				   Sub_Code.Server_Main.setDefaultLevel(Sub_Code.Server_Main.getLevelByName("main_back"));
				   for (Player player: Server.getInstance().getOnlinePlayers().values())
				   {
					   player.setLevel(Sub_Code.Server_Main.getLevelByName("main_back"));
					   player.switchLevel(Sub_Code.Server_Main.getLevelByName("main_back"));
				   }
				   Sub_Code.Server_Main.unloadLevel(Sub_Code.Server_Main.getLevelByName("main"));
				   p.sendMessage("マップを変更しました。main -> main_back");
	    	   }
	       }
	       else if (cmd.getName().equalsIgnoreCase("PlaySound"))
	       {
	    	   if (args.length == 0)
	    	   {
	    		   p.sendMessage(TextFormat.GREEN + "再生するイベント名を引数1に入力してください。イベント名はコマンド一覧ページに記載しています。");
	    		   return true;
	    	   }
	    	   Player_Data p_data = Sub_Code.Get_Player_Data(p);
	    	   PlaySound.PlaySoundOne(p_data, args[0], p.getPosition());
	       }
	       else if (cmd.getName().equalsIgnoreCase("StopSound"))
	       {
	    	   PlaySound.StopSoundOne(p);
	       }
		 return true;
	 }
	 //矢を発射
	 @EventHandler
	 public void Bow_Shot(EntityShootBowEvent e)
	 {
		 Archer.Bow_Shot(e);
	 }
	 //エンティティに矢や雪玉が当たったら実行
	 @EventHandler
	 public static void Entity_Hit(ProjectileHitEvent e)
	 {
	     Archer.Bow_Hit(e);
	 }
	 //エンティティがダメージを受けたとき
	 @EventHandler
	 public void Entity_Damage(EntityDamageByEntityEvent e)
	 {
		 if (e.getEntity() instanceof Player)
		 {
			 Player_Data Data = Sub_Code.Get_Player_Data((Player)e.getEntity());
			 if (e.getDamager() instanceof Player)
			 {
				 //同じチームには攻撃できないように
				 if (Sub_Code.Get_Player_Data((Player)e.getDamager()).Team_Color == Data.Team_Color || Data.Team_Color == Sub_Code.Team.None)
				 {
					 e.setCancelled();
					 return;
				 }
				 //ロビーまたは戦闘が終了したら攻撃できないように
				 else if (Game.Map_Now == null || Game.Blue_Team_HP <= 0 || Game.Red_Team_HP <= 0)
				 {
					 e.setCancelled();
					 return;
				 }
				 //釣り竿のダメージを無効
				 else if (((Player)e.getDamager()).getInventory().getItemInHand().getId() == Item.FISHING_ROD)
					 e.setCancelled();
				 Data.Damage_Time = Main_Server.getTick();
			 }
		     Skill_Main.Entity_Damage(e);
		 }
		 //村人へのダメージを無効
		 else if (e.getEntity() instanceof EntityVillager)
		 {
			 if (e.getDamager() instanceof Player && !((Player)e.getDamager()).isOp())
				 e.setCancelled();
		 }
		 else if (e.getDamager() instanceof Player && !((Player)e.getDamager()).isOp())
			 e.setCancelled();
	 }
	 //落下時、特定の職業であれば軽減
	 @EventHandler
	 public void onFallDamage(EntityDamageEvent event)
	 {
		 if(event.getEntity() instanceof Player)
		 {
			 Player_Data data = Sub_Code.Get_Player_Data((Player)event.getEntity());
			 if (event.getCause() == DamageCause.FALL)
			 {
				 if (!Acrobat.OnFallDamage(data) || data.IsLobbyMode)
	            	 event.setCancelled();
				 else if (data.Skill_Now == "Scout" || data.Skill_Now == "Ninja")
					 event.setDamage(event.getDamage() / 2);
			 }
		 }
	 }
	//プレイヤーが死んでしまったとき
	@EventHandler
	public void Death(PlayerDeathEvent e)
	{
		Player p_death = e.getEntity();
		ArrayList<Item> items = new ArrayList<Item>(Arrays.asList(e.getDrops()));
		Player_Data p_data = Sub_Code.Get_Player_Data(p_death);
		p_data.Death_Count++;
		p_data.Kill_Continue_Count = 0;
		//スキル使用中であれば無効化
		if (p_data.IsStartupSkill >= 0)
		{
			p_data.player.getInventory().setItem(p_data.IsStartupSkill, p_data.Before_Skill_Item);
			p_data.IsStartupSkill = -1;
			p_data.Before_Skill_Item = null;
		}
		//プレイヤーにキルされた場合
		if (e.getEntity().getKiller() instanceof Player)
		{
			//キルカウントを増加
			Player_Data p_killer_data =  Sub_Code.Get_Player_Data((Player)e.getEntity().getKiller());
			p_killer_data.Kill_Count++;
			p_killer_data.Kill_Continue_Count++;
			//音声
			PlaySound.PlaySoundAll(p_killer_data, "voice." + p_killer_data.Voice_Name + ".enemy_kill", p_killer_data.player.getPosition());
			//職業がバーサーカーだった場合キルカウントに応じて効果を発動
			if (p_killer_data.Skill_Now == "Berserker")
			{
				int Strength_Value = -1;
				int Speed_Value = -1;
				for (Effect effect: p_killer_data.player.getEffects().values())
				{
					if (effect.getId() == Effect.STRENGTH)
						Strength_Value = effect.getAmplifier();
					else if (effect.getId() == Effect.SPEED)
						Speed_Value = effect.getAmplifier();
				}
				if (Strength_Value < 2)
					Strength_Value++;
				if (Speed_Value < 1)
					Speed_Value++;
				if (Strength_Value >= 2)
				    Sub_Code.Skill_Achievement(p_killer_data, "Berserker");
				Effect effect_st = Effect.getEffect(Effect.STRENGTH);
				effect_st.setAmplifier(Strength_Value);
				effect_st.setDuration(200);
				Effect effect_sp = Effect.getEffect(Effect.SPEED);
				effect_sp.setAmplifier(Speed_Value);
				effect_sp.setDuration(200);
				p_killer_data.player.addEffect(effect_st);
				p_killer_data.player.addEffect(effect_sp);
				p_killer_data.player.setHealth(p_killer_data.player.getHealth() + 4.0f);
			}
			//キルカウントによる称号を付与
			Battle_Achievement.Get_Battle_Achievement(p_killer_data);
		}
		//敵プレイヤーが消失する効果音
		PlaySound.PlaySoundAll(p_data, "system.enemy_kill", p_death.getPosition());
		//特定のアイテムはドロップしないように
		for (int Number = 0; Number < items.size(); Number++)
		{
			Item item_now = items.get(Number);
			int aa = item_now.getId();
			String a = item_now.getCustomName();
			boolean IsCancel = false;
			if (aa == Item.LEATHER_CAP || aa == Item.LEATHER_TUNIC || aa == Item.LEATHER_PANTS || aa == Item.LEATHER_BOOTS || aa == Item.WOODEN_SWORD || aa == Item.WOODEN_AXE || aa == Item.WOODEN_PICKAXE)
				IsCancel = true;
			else if (a != "" && a.contains(p_data.No_Drop_Item))
				IsCancel = true;
			else if (a.contains("Builder 石レンガ") || a.contains("Builder レンガ") || a.contains("Builder 木材") || a.contains("Builder 羊毛") || a.contains("日本刀"))
				IsCancel = true;
			if (IsCancel)
			{
				items.remove(item_now);
				Number--;
			}
		}
		e.setDrops(items.toArray(new Item[0]));
    }
	//リスポーン時
	@EventHandler
	public void Player_Respawn(PlayerRespawnEvent e)
	{
		Player p = e.getPlayer();
		Player_Data p_data = Sub_Code.Get_Player_Data(p);
		if (p_data == null)
			return;
		p_data.IsBerserkerUsing = false;
		if (p_data.Team_Color == Sub_Code.Team.Blue)
			e.setRespawnPosition(Game.Map_Now.Blue_Team_Spawn_Pos);
		else if (p_data.Team_Color == Sub_Code.Team.Red)
			e.setRespawnPosition(Game.Map_Now.Red_Team_Spawn_Pos);
		else if (p_data.Team_Color == Sub_Code.Team.None)
			e.setRespawnPosition(Lobby.Lobby_Teleport_Position);
		if (!p_data.IsLobbyMode)
			Skill_Main.Get_Skill_Item(p_data);
		p.setMaxHealth(20);
		//透明化が持続されている可能性があるので実行
		for (Player player2: Server.getInstance().getOnlinePlayers().values())
		    player2.showPlayer(p);
	}
	//プレイヤーがログアウトしたとき
	@EventHandler
	public void Quit(PlayerQuitEvent e)
	{
		Player p = e.getPlayer();
		//プレイヤーデータをセーブ
		Save_Load.Save_Player_Data(Sub_Code.Get_Player_Data(p));
		String name = p.getDisplayName();
		String Message = "";
		int a = r.nextInt(10);
		if (a == 0)
			Message = "じゃあね！";
		else if (a == 1)
			Message = "またね！";
		else if (a == 2)
			Message = "また...入ってくれるよね...？";
		else if (a == 3)
			Message = "さらば！";
		else if (a == 4)
			Message = "（ _ _ ）......o";
		else if (a == 5)
			Message = "(´･ω･`)";
		else if (a == 6)
			Message = "さようなら。";
		else if (a == 7)
			Message = "バイバイ！";
		else if (a == 8)
			Message = "おつ(っ･ω･)っ旦";
		else if (a == 9)
			Message = "(｡･ω･)ﾉﾞ おつ〜";
		e.setQuitMessage(name + TextFormat.GREEN + "さんが世界を去りました。" + Message);
		ScoreBoard_Show.Hide_Player(p);
		Scout_Skill.onQuit(e);
	}
	//ブロックを破壊したときに実行
	@EventHandler
	public void Break(BlockBreakEvent e)
	{
		if (Game.Block_Break(e))
			return;
		Player p = e.getPlayer();
		if (Game.Map_Now != null)
		{
			Block b = e.getBlock();
			int x = (int)b.x;
			//int y = (int)b.y;
			int z = (int)b.z;
			//特定のブロックであればn秒後に再生させる
			if (b.getId() == Block.QUARTZ_STAIRS || b.getId() == Block.FENCE || b.getId() == Block.END_STONE || b.getId() == Block.CACTUS || b.getId() == Block.PACKED_ICE || b.getId() == Block.GOLD_ORE || b.getId() == Block.IRON_ORE 
					|| b.getId() == Block.DIAMOND_ORE || b.getId() == Block.COAL_ORE || b.getId() == Block.EMERALD_ORE || b.getId() == Block.STONE || b.getId() == Block.WOOD)
			{
				int Time = 10;
				if (b.getId() == Block.IRON_ORE)
				{
					Time = 20;
					p.addExperience(20);
					p.getInventory().addItem(Item.get(Item.IRON_INGOT));
					Player_Data p_data = Sub_Code.Get_Player_Data(p);
					//Minerの称号
					if (p_data.Skill_Now == "Miner" && !p_data.Has_Achievements.contains(Battle_Achievement.Get_Skill_Achievement_Name("Miner")))
					{
						p_data.Get_Iron_Count++;
						if (p_data.Get_Iron_Count >= 128)
						    Sub_Code.Skill_Achievement(p_data, "Miner");
					}
				}
				else if (b.getId() == Block.COAL_ORE)
				{
					Time = 15;
					p.addExperience(40);
					p.getInventory().addItem(Item.get(Item.COAL));
				}
				else if (b.getId() == Block.GOLD_ORE)
				{
					Time = 25;
					p.addExperience(30);
					p.getInventory().addItem(Item.get(Item.GOLD_INGOT));
				}
				else if (b.getId() == Block.DIAMOND_ORE)
				{
					if (Game.Phase < 3)
					{
						e.setCancelled();
						return;
					}
					Time = 30;
					p.addExperience(60);
					p.getInventory().addItem(Item.get(Item.DIAMOND));
				}
				else if (b.getId() == Block.EMERALD_ORE)
				{
					Time = 30;
					p.addExperience(175);
					p.getInventory().addItem(Item.get(Item.EMERALD));
				}
				else if (b.getId() == Block.STONE)
				{
					ItemBlock item = new ItemBlock(Block.get(Block.COBBLESTONE));
					item.setCount(1);
					p.getInventory().addItem(item);
				}
				else if (b.getId() == Block.WOOD)
				{
					ItemBlock block = new ItemBlock(Block.get(Block.WOOD));
					block.setCount(1);
					p.addExperience(5);
					p.getInventory().addItem(block);
				}
				else
					p.getInventory().addItem(new ItemBlock(b));
				if (b.getDamage() != 1)
				{
				    Ore_Restore or = new Ore_Restore();
				    or.block = b.clone();
				    or.Time = Time;
				    Map_Setting.Map_Ore_Restore_List.add(or);
				    e.setDrops(new Item[] {});
				    e.setDropExp(0);
				}
				return;
			}
			//保護範囲内であれば食料以外すぐに復活
			for (Map_Not_Break_Pos Pos: Game.Map_Now.IsNotBreakPos)
			{
				if (x >= Pos.Start_Pos.x && x <= Pos.End_Pos.x && z >= Pos.Start_Pos.z && z <= Pos.End_Pos.z)
				{
					if (b.getId() == Block.MELON_BLOCK || b.getId() == Block.CARROT_BLOCK || b.getId() == Block.POTATO_BLOCK || b.getId() == Block.WHEAT_BLOCK)
					{
						Ore_Restore or = new Ore_Restore();
						or.block = b.clone();
						or.Time = 25;
						Map_Setting.Map_Ore_Restore_List.add(or);
						e.setDrops(new Item[] {});
						e.setDropExp(0);
						if (b.getId() == Block.CARROT_BLOCK)
						    p.getInventory().addItem(Item.get(Item.CARROT, 0, 4));
						else if (b.getId() == Block.MELON_BLOCK)
						    p.getInventory().addItem(Item.get(Item.MELON, 0, 7));
						else if (b.getId() == Block.POTATO_BLOCK)
						    p.getInventory().addItem(Item.get(Item.POTATO, 0, 5));
						else if (b.getId() == Block.WHEAT_BLOCK)
						    p.getInventory().addItem(Item.get(Item.BREAD, 0, 1));
					}
					else
					    e.setCancelled();
					break;
				}
			}
			if (Sub_Code.Get_Player_Data(p).IsLobbyMode)
				e.setCancelled();
		}
		else if (!p.isOp())
			e.setCancelled();
	}
	//エンチャントテーブルを開いたとき
	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent e)
	{
        if (e.getInventory() instanceof EnchantInventory)
        {
            ItemDye id = new ItemDye(DyeColor.BLUE);
            id.setCount(64);
            e.getInventory().setItem(1, id);
            e.getInventory().sendSlot(1, e.getPlayer());
        }
    }
	//エンチャント時
	@EventHandler
	public void EnchantItem(EnchantItemEvent e)
	{
		ItemDye id = new ItemDye(DyeColor.BLUE);
        id.setCount(64);
        e.getInventory().setItem(1, id);
        e.getInventory().sendSlot(1, e.getEnchanter());
    }
	//エンチャントテーブルを閉じたとき
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event)
	{
	    if (event.getInventory() instanceof EnchantInventory)
	    {
	    	event.getInventory().setItem(1, Item.get(Item.AIR));
	    }
    }
	//インベントリのアイテムをクリックしたら実行
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event)
	{
		//スキル使用中はアイテムを移動できないように
		 Player_Data p_data = Sub_Code.Get_Player_Data(event.getPlayer());
		 if (p_data.IsStartupSkill >= 0)
		 {
			 event.setCancelled();
			 return;
		 }
		if (event.getInventory() instanceof EnchantInventory)
		{
			if (event.getSlot() == 1)
                event.setCancelled();
		}
	}
	//イスに座る
    @EventHandler
    public void onTouch(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        if (player.isSneaking() || event.getAction() != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK || disabled.contains(player.getName())) {
            return;
        }
        if (!Sub_Code.Get_Player_Data(player).IsLobbyMode)
        	return;
        String name = player.getName().toLowerCase();
        Block block = event.getBlock();

        if (!this.onChair.containsKey(name)) {
            if (block instanceof BlockStairs) {
                if ((block.getDamage() & 4) != 0 || block.up().isSolid()) return;
                if (!this.doubleTap.containsKey(name)) {
                    this.doubleTap.put(name, System.currentTimeMillis());
                    return;
                }

                if (System.currentTimeMillis() - this.doubleTap.get(name) < 500) {
                    AddEntityPacket addTagblockPacket = new AddEntityPacket();
                    long eid = Entity.entityCount++;
                    this.tagblock.put(name, eid);
                    addTagblockPacket.entityRuntimeId = eid;
                    addTagblockPacket.entityUniqueId = eid;
                    addTagblockPacket.speedX = 0;
                    addTagblockPacket.speedY = 0;
                    addTagblockPacket.speedZ = 0;
                    addTagblockPacket.pitch = 0;
                    addTagblockPacket.yaw = 0;
                    addTagblockPacket.x = (float) (block.getX() + 0.5);
                    addTagblockPacket.y = (float) (block.getY() + 0.3);
                    addTagblockPacket.z = (float) (block.getZ() + 0.5);
                    addTagblockPacket.type = 84;

                    long flags = 0;
                    flags |= 1 << Entity.DATA_FLAG_CAN_SHOW_NAMETAG;
                    flags |= 1 << Entity.DATA_FLAG_INVISIBLE;
                    flags |= 1 << Entity.DATA_FLAG_NO_AI;
                    flags |= 1 << Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG;

                    addTagblockPacket.metadata = new EntityMetadata()
                            .putLong(Entity.DATA_FLAGS, flags)
                            .putShort(Entity.DATA_AIR, 400)
                            .putShort(Entity.DATA_MAX_AIR, 400)
                            .putString(Entity.DATA_NAMETAG, TextFormat.AQUA + "")
                            .putLong(Entity.DATA_LEAD_HOLDER_EID, -1)
                            .putFloat(Entity.DATA_SCALE, 0.0001f);

                    MoveEntityAbsolutePacket moveTagblockPacket = new MoveEntityAbsolutePacket();
                    moveTagblockPacket.eid = eid;
                    moveTagblockPacket.x = (float) (block.getX() + 0.5);
                    moveTagblockPacket.y = (float) (block.getY() + 0.7);
                    moveTagblockPacket.z = (float) (block.getZ() + 0.5);

                    AddEntityPacket addEntityPacket = new AddEntityPacket();
                    eid = Entity.entityCount++;
                    this.onChair.put(name, eid);
                    addEntityPacket.entityRuntimeId = eid;
                    addEntityPacket.entityUniqueId = eid;
                    addEntityPacket.speedX = 0;
                    addEntityPacket.speedY = 0;
                    addEntityPacket.speedZ = 0;
                    addEntityPacket.pitch = 0;
                    addEntityPacket.yaw = faces[event.getBlock().getDamage()];
                    addEntityPacket.x = (float) (block.getX() + 0.5);
                    addEntityPacket.y = (float) (block.getY() + 1.6);
                    addEntityPacket.z = (float) (block.getZ() + 0.5);
                    addEntityPacket.type = 84;

                    flags = 0;
                    flags |= 1 << Entity.DATA_FLAG_CAN_SHOW_NAMETAG;
                    flags |= 1 << Entity.DATA_FLAG_INVISIBLE;
                    flags |= 1 << Entity.DATA_FLAG_NO_AI;
                    flags |= 1 << Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG;

                    addEntityPacket.metadata = new EntityMetadata()
                            .putLong(Entity.DATA_FLAGS, flags)
                            .putShort(Entity.DATA_AIR, 400)
                            .putShort(Entity.DATA_MAX_AIR, 400)
                            .putLong(Entity.DATA_LEAD_HOLDER_EID, -1)
                            .putFloat(Entity.DATA_SCALE, 0.0001f);

                    MoveEntityAbsolutePacket moveEntityPacket = new MoveEntityAbsolutePacket();
                    moveEntityPacket.eid = eid;
                    moveEntityPacket.x = (float) (block.getX() + 0.5);
                    moveEntityPacket.y = (float) (block.getY() + 1.6);
                    moveEntityPacket.z = (float) (block.getZ() + 0.5);
                    moveEntityPacket.yaw = faces[event.getBlock().getDamage()];
                    moveEntityPacket.headYaw = faces[event.getBlock().getDamage()];
                    moveEntityPacket.pitch = 0;

                    SetEntityLinkPacket setEntityLinkPacket = new SetEntityLinkPacket();
                    setEntityLinkPacket.vehicleUniqueId = eid;
                    setEntityLinkPacket.riderUniqueId = player.getId();
                    setEntityLinkPacket.type = SetEntityLinkPacket.TYPE_PASSENGER;

                    Main_Server.getOnlinePlayers().values().forEach((target) -> {
                        target.dataPacket(addEntityPacket);
                        target.dataPacket(moveEntityPacket);
                        target.dataPacket(addTagblockPacket);
                        target.dataPacket(moveTagblockPacket);
                        target.dataPacket(setEntityLinkPacket);
                    });

                    player.setDataFlag(Entity.DATA_FLAGS, Entity.DATA_FLAG_RIDING, true);
                    this.doubleTap.remove(name);
                } else {
                    this.doubleTap.put(name, System.currentTimeMillis());
                }
            }
        } else {
            RemoveEntityPacket removeEntityPacket = new RemoveEntityPacket();
            removeEntityPacket.eid = this.onChair.remove(name);
            RemoveEntityPacket removeTagblockPacket = new RemoveEntityPacket();
            removeTagblockPacket.eid = this.tagblock.remove(name);
            Main_Server.getOnlinePlayers().values().forEach((p) -> {
                p.dataPacket(removeEntityPacket);
                p.dataPacket(removeTagblockPacket);
            });
        }
    }

    @EventHandler
    public void onJump(DataPacketReceiveEvent event) {
        if (event.getPacket().pid() == ProtocolInfo.PLAYER_ACTION_PACKET) {
            PlayerActionPacket packet = (PlayerActionPacket) event.getPacket();
            String name = event.getPlayer().getName().toLowerCase();
            if (packet.action == PlayerActionPacket.ACTION_JUMP && this.onChair.containsKey(name)) {
                RemoveEntityPacket removeEntityPacket = new RemoveEntityPacket();
                removeEntityPacket.eid = this.onChair.remove(name);
                RemoveEntityPacket removeTagblockPacket = new RemoveEntityPacket();
                removeTagblockPacket.eid = this.tagblock.remove(name);
                Main_Server.getOnlinePlayers().values().forEach((p) -> {
                    p.dataPacket(removeEntityPacket);
                    p.dataPacket(removeTagblockPacket);
                });
            }
        }
    }
}