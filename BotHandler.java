package bot;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.RuntimeErrorException;

public class BotHandler {
	private final String PASSWORD = "PASSWORD";
	private final String URL = "http://XXXXXXX.XXX";

	public List<Message> GetMessages() throws UnsupportedEncodingException, IOException {
		return JsonToList(sendGet());
	}

	public void SendMessage(Message m) throws IOException {
		String s = PostMessageToServer(m);
		if (!s.equals("message send")) {
			throw new RuntimeErrorException(null, "Message not sended");
		}
	}

	// HTTP POST request
	private String PostMessageToServer(Message m) throws IOException {

		String url = URL + "/SendMessage";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("POST");

		// add request header
		con.setRequestProperty("password", PASSWORD);

		String tmp = "@s.whatsapp.net";
		if (m.getContact().contains("-")) {
			tmp = "@g.us";
		}

		String urlParameters = "to=" + m.getContact() + tmp + "&message=" + URLEncoder.encode(m.getBody(), "UTF-8");
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return (response.toString());
	}

	// HTTP GET request
	private String sendGet() throws IOException {

		String url = URL + "/ReciveMessage";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("password", PASSWORD);
		con.setRequestProperty("Accept-Charset", "UTF-8");

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		String str = response.toString();
		return str;
	}

	private List<Message> JsonToList(String s) throws UnsupportedEncodingException {
		s = s.substring(1, s.length() - 1);
		List<Message> Part = new ArrayList<>();
		if (s.length() < 3) {
			return Part;
		}
		boolean go = true;
		while (go) {
			String id = s.substring(1);
			id = id.substring(0, id.charAt('"'));
			s = s.substring(id.length() + 16);
			String num = s.substring(0, s.indexOf('"'));
			s = s.substring(num.length() + 12);
			String body = s.substring(0, s.indexOf('"'));
			if (s.length() - body.length() == 2)
				go = false;
			else
				s = s.substring(body.length() + 4);
			Message m = new Message();

			m.setBody(Decode(body));
			m.setId(id);
			m.setContact(num);
			Part.add(m);
		}
		return Part;

	}

	private String Decode(String raw) {
		final Pattern UNICODE_CHARACTER_PATTERN = Pattern.compile("\\\\u([0-9A-Fa-f]{2,4})");

		StringBuilder sb = new StringBuilder(raw.length() / 7);

		Matcher matcher = UNICODE_CHARACTER_PATTERN.matcher(raw);

		boolean RTL = false;
		while (raw.length() != 0) {
			if (raw.charAt(0) == '\\') {
				matcher = UNICODE_CHARACTER_PATTERN.matcher(raw);
				String hexCode = "";
				char[] decodedChars = null;
				if (matcher.find()) {
					hexCode = matcher.group(1);
					decodedChars = Character.toChars(Integer.valueOf(hexCode, 16));
					sb.append(decodedChars);
				}
				raw = raw.substring(matcher.group(0).length());
			} else {
				sb.append(raw.charAt(0));
				raw = raw.substring(1);
			}
		}		
		
		return sb.toString();
	}
	private boolean RTL(char c) {
		if(c>'א' && c<'ת') {
			return true;
		}
		return false;
	}

}
