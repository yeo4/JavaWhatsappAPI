package runner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bot.BotHandler;
import bot.Message;

public class SimpleBot{

	public static void main(String[] args) throws Exception {
		BotHandler bot = new BotHandler();
		HashMap<String,String> map = new HashMap<>();
		map.put("Hello", "😝");
    map.put("hii", "hii");
		map.put("home", "127.0.0.1");
		map.put("1", "😝");
		map.put("2", "😝");
		map.put("3", "😝");
		map.put("4", "😝");


		
		boolean botStop = false;
		while(true) {
			List<Message> messages = bot.GetMessages();
			for (Message m : messages) {
				System.out.println(m.getBody());
				if(m.getBody().equals("bot stop")) {
					System.out.println("STOP");
					botStop = true;
				}
				else if(m.getBody().equals("bot start")) {
					botStop = false;
				}
				else if(!botStop) {
					String res = map.get(m.getBody());
					if(res != null) {
						m.setBody(res);
						bot.SendMessage(m);
					}
				}
			}
			Thread.sleep(500);
		}
	}
	
}
