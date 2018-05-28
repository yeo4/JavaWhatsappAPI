# JavaWhatsappAPI
Simple Whatsapp Java API using SimpleWhatsappRestAPI

## Setup
1. install this: https://github.com/yeo4/SimpleWhatsappRestAPI
2. edit PASSWORD and URL in BotHandler.java (line 19,20)
```
private final String PASSWORD = "PASSWORD";
private final String URL = "http://XXXXXXX.XXX";
```
3. Write a bot :)

## API:
1. GetMessages() ==> Recive List of Messages
2. SendMessage(Message m) ==> Send m to Whatsapp

# SimpleBot:

```
import java.util.List;
import bot.BotHandler;
import bot.Message;

public class EchoBot{

	public static void main(String[] args) throws Exception {
		BotHandler bot = new BotHandler();
		
		while(true) {
			List<Message> messages = bot.GetMessages();
			for (Message m : messages) {
				bot.SendMessage(m);
			}
			Thread.sleep(500);
		}
	}
	
}

```
