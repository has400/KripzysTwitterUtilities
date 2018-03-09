package RemoveCSGOPosts;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import twitter4j.IDs;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class KripzyTwitterBotGui {

	private JFrame frame;

	static Twitter twitter;

	static String filename = "config.txt";

	static String path = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "KripTech"
			+ File.separator + "TwitterContestBot";

	static String ConsumerKey = null;
	static String ConsumerSecret = null;
	static String AccessToken = null;
	static String AccessSecret = null;
static String zipfilename = "";
	public static void main(String[] args) throws InterruptedException, TwitterException {

		if (args.length == 2) {
			String bot = args[0];
			load(bot);
			zipfilename = args[1];
			try {
				startBot();
			} catch (FileNotFoundException | InterruptedException e) {
				
				e.printStackTrace();
			}
		} else {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						KripzyTwitterBotGui window = new KripzyTwitterBotGui();
						window.frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	static String inputOfButton;

	private static void load(String input2) {

		try {
			Scanner input = new Scanner(
					new File(path + File.separator + input2.toLowerCase() + File.separator + filename));
			try {
				ConsumerKey = input.nextLine().toString();
				ConsumerSecret = input.nextLine().toString();
				AccessToken = input.nextLine().toString();
				AccessSecret = input.nextLine().toString();
			} catch (NoSuchElementException e) {
				String[] tokens = ConsumerKey.split("\\|\\|\\|");
				ConsumerKey = tokens[0];
				ConsumerSecret = tokens[1];
				AccessToken = tokens[2];
				AccessSecret = tokens[3];
			}

			inputOfButton = input2;
			input.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void getNewKeys(String bot) {
		String[] keys = getBot(bot.toLowerCase(), "kripzy", 3245);

		Writer wr;
		try {
			wr = new FileWriter(path + File.separator + bot.toLowerCase() + File.separator + filename);

			wr.write(keys[0]);
			wr.write(System.getProperty("line.separator"));
			wr.write(keys[1]);
			wr.write(System.getProperty("line.separator"));
			wr.write(keys[2]);
			wr.write(System.getProperty("line.separator"));
			wr.write(keys[3]);

			wr.flush();
			wr.close();

		} catch (IOException e1) {
		}
	}

	private static String[] getBot(String bot, String botid, int PW) {
		String https_url = "https://kripzy.com/getbot.php?botid=" + botid + bot + "&PW=" + PW;

		URL url;

		try {

			url = new URL(https_url);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

			// dump all the content
			return getBotText(con);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String[] getBotText(HttpsURLConnection con) {
		if (con != null) {

			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

				String input;
				while ((input = br.readLine()) != null) {
					String[] keys = input.split("<br>");
					return keys;
				}
				br.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	static List<Long> longIDList = new ArrayList<Long>();

	private static void startBot() throws FileNotFoundException, InterruptedException {

		System.out.println("Launching the KripzyBot");

		File file = new File(path + File.separator + inputOfButton.toLowerCase() + File.separator + "config.txt");

	
		try {
			file.createNewFile();
		} catch (IOException e) {
		}
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey(ConsumerKey).setOAuthConsumerSecret(ConsumerSecret)
				.setOAuthAccessToken(AccessToken).setOAuthAccessTokenSecret(AccessSecret);
		cb.setTweetModeExtended(true);
		twitter = new TwitterFactory(cb.build()).getInstance();
		
		String prefix = returnPrefix(inputOfButton.toLowerCase());
		
		 File dir = new File(path);
		    File[] files = dir.listFiles(new FilenameFilter() {
		        @Override
		        public boolean accept(File dir, String name) {
		            return name.matches("^"+prefix+"_[a-zA-Z0-9_]*.zip$");
		        }
		    });		
		JSONArray jsonObject = (JSONArray) readZipFiles(files[0].getPath());
		for (int i = 0; i < jsonObject.size(); i++) {
			JSONObject obj2 = (JSONObject) jsonObject.get(i);
			// JSONObject ob3 = (JSONObject)obj2.get("id_str");

			Date date2 = new Date();
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.DATE, -1);
			date2.setTime(c.getTime().getTime());

			Date date1 = null;
			try {
				date1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
						.parse(((String) obj2.get("created_at")).replaceAll("\\+0000", ""));
			} catch (java.text.ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if (date1.before(date2)) {
				Date currentdate = new Date();
				String[] hr2 = new String[] { "15 min", "10 min", "25 min", "20 min", "30 min", "60 min", "9 min" };
				String[] day1 = new String[] { "12 hour", "1 hour", "2 hour", "3 hour", "4 hour", "5 hour", "6 hour",
						"7 hour", "8 hour", "9 hour", "10 hour", "11 hour", "12 hour", "15 hrs", "speed giveaway",
						"12h", "a few hours" };
				String[] day2 = new String[] { "must be in live stream", "24 hrs", "33 hours", "be in stream",
						"win tonight", "keyword", "22 hour", "24 hour", "24-hour" };
				String[] day3 = new String[] { "48 hours", "48hrs", "48hours", "48-hours", "48h" };
				String[] day4 = new String[] { "72hrs", "75H", "72 hours", "3 days", "days", "60 min", "9 min" };
				Calendar c2 = Calendar.getInstance();

				Date datehr2 = new Date();
				c2.setTime(date1);
				c2.add(Calendar.HOUR, +2);
				datehr2.setTime(c2.getTime().getTime());

				Date dateday1 = new Date();
				c2.setTime(date1);
				c2.add(Calendar.DATE, +1);
				dateday1.setTime(c2.getTime().getTime());

				Date dateday2 = new Date();
				c2.setTime(date1);
				c2.add(Calendar.DATE, +2);
				dateday2.setTime(c2.getTime().getTime());

				Date dateday3 = new Date();
				c2.setTime(date1);
				c2.add(Calendar.DATE, +3);
				dateday3.setTime(c2.getTime().getTime());

				Date dateday4 = new Date();
				c2.setTime(date1);
				c2.add(Calendar.DATE, +4);
				dateday4.setTime(c2.getTime().getTime());

				System.out.println(obj2.get("id_str"));
				String text = (String) obj2.get("text");
				System.out.println(obj2.get("created_at"));
				
				// 2hr delete
				if (currentdate.after(datehr2)) {
					for (String s : hr2) {
						if (text.toLowerCase().contains(s)) {
							delete(obj2);
						}
					}
				}

				// 1day delete
				if (currentdate.after(dateday1)) {
					for (String s : day1) {
						if (text.toLowerCase().contains(s)) {
							delete(obj2);
						}
					}
				}
				// 2day delete
				if (currentdate.after(dateday2)) {
					for (String s : day2) {
						if (text.toLowerCase().contains(s)) {
							delete(obj2);
						}
					}
				}
				// 3day delete
				if (currentdate.after(dateday3)) {
					for (String s : day3) {
						if (text.toLowerCase().contains(s)) {
							delete(obj2);
						}
					}
				}

				// 4day delete
				if (currentdate.after(dateday4)) {
					for (String s : day4) {
						if (text.toLowerCase().contains(s)) {
							delete(obj2);
						}
					}
				}
			}
		}

		System.out.println("done");

	}

	public static void delete(JSONObject obj2) {

		try {
			twitter.destroyStatus(Long.valueOf((String) obj2.get("id_str")));
		} catch (NumberFormatException | TwitterException e) {
			e.printStackTrace();
		}

		try {
			twitter.destroyFavorite(Long.valueOf((String) obj2.get("id_str")));
		} catch (NumberFormatException | TwitterException e) {

		}

	}
	
	  private static String returnPrefix(String lowerCase) {
	
		  String[] prefix = AccessToken.split("-");
		  System.out.println(prefix[0]);
		return prefix[0];
	}

	public static JSONArray readZipFiles(String filename)
	    {
		  ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(filename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		    Enumeration<? extends ZipEntry> entries = zipFile.entries();
		    
		    
		    while(entries.hasMoreElements()){
		        ZipEntry entry = entries.nextElement();
		       // System.out.println(entry.getName());
		        if (entry.getName().equals("data/js/tweets/"+zipfilename)) {

		        try {
					InputStream stream = zipFile.getInputStream(entry);
					//String result = IOUtils.toString(stream);
					BufferedReader streamReader = new BufferedReader(new InputStreamReader(stream, "UTF-8")); 
					StringBuilder responseStrBuilder = new StringBuilder();

					String inputStr;
					while ((inputStr = streamReader.readLine()) != null)
					    responseStrBuilder.append(inputStr);
					
					
	

					JSONParser jsonParser = new JSONParser();
			
						JSONArray obj = (JSONArray)jsonParser.parse(responseStrBuilder.toString());
						
						return obj;
			
			
				} catch (IOException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        }
		    }
		    return null;
	    }
	
	public KripzyTwitterBotGui() throws IOException {
		initialize();
	}

	private void initialize() throws IOException {

		frame = new JFrame();
		frame.setBounds(100, 100, 415, 240);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("KripTech - Remove CSGO Posts");
		frame.getContentPane().setLayout(null);

		JButton btnNewButton_1 = new JButton("Bot1");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run() {
						load(btnNewButton_1.getText());

						try {
							startBot();
						} catch (FileNotFoundException | InterruptedException e) {
							
							e.printStackTrace();
						}

					}
				}.start();

			}
		});
		btnNewButton_1.setBounds(10, 11, 89, 23);
		frame.getContentPane().add(btnNewButton_1);

		JButton btnNewButton = new JButton("Bot2");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run() {

						load(btnNewButton.getText());
						try {
							startBot();
						} catch (FileNotFoundException | InterruptedException e) {
							
							e.printStackTrace();
						}
					}
				}.start();

			}
		});
		btnNewButton.setBounds(10, 45, 89, 23);
		frame.getContentPane().add(btnNewButton);

		JButton btnNewButton_2 = new JButton("Bot3");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run() {

						load(btnNewButton_2.getText());
						try {
							startBot();
						} catch (FileNotFoundException | InterruptedException e) {
							
							e.printStackTrace();
						}

					}
				}.start();

			}
		});
		btnNewButton_2.setBounds(10, 79, 89, 23);
		frame.getContentPane().add(btnNewButton_2);

		JButton btnNewButton_3 = new JButton("Bot4");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run() {
						load(btnNewButton_3.getText());
						try {
							startBot();
						} catch (FileNotFoundException | InterruptedException e) {
							
							e.printStackTrace();
						}
					}
				}.start();

			}
		});
		btnNewButton_3.setBounds(10, 113, 89, 23);
		frame.getContentPane().add(btnNewButton_3);

		JButton btnNewButton_4 = new JButton("Bot6");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run() {
						load(btnNewButton_4.getText());
						try {
							startBot();
						} catch (FileNotFoundException | InterruptedException e) {
							
							e.printStackTrace();
						}
					}
				}.start();
			}
		});
		btnNewButton_4.setBounds(109, 11, 89, 23);
		frame.getContentPane().add(btnNewButton_4);

		JButton btnBot = new JButton("Bot5");
		btnBot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run() {

						load(btnBot.getText());
						try {
							startBot();
						} catch (FileNotFoundException | InterruptedException e) {
							
							e.printStackTrace();
						}

					}
				}.start();
			}
		});
		btnBot.setBounds(10, 147, 89, 23);
		frame.getContentPane().add(btnBot);

		JButton btnNewButton_5 = new JButton("Bot7");
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run() {

						load(btnNewButton_5.getText());
						try {
							startBot();
						} catch (FileNotFoundException | InterruptedException e) {
							
							e.printStackTrace();
						}

					}
				}.start();

			}
		});
		btnNewButton_5.setBounds(109, 45, 89, 23);
		frame.getContentPane().add(btnNewButton_5);

		JButton btnNewButton_6 = new JButton("Bot8");
		btnNewButton_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run() {

						load(btnNewButton_6.getText());
						try {
							startBot();
						} catch (FileNotFoundException | InterruptedException e) {
							
							e.printStackTrace();
						}

					}
				}.start();
			}
		});
		btnNewButton_6.setBounds(109, 79, 89, 23);
		frame.getContentPane().add(btnNewButton_6);

		JButton btnNewButton_7 = new JButton("Bot9");
		btnNewButton_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run() {

						load(btnNewButton_7.getText());
						try {
							startBot();
						} catch (FileNotFoundException | InterruptedException e) {
							
							e.printStackTrace();
						}

					}
				}.start();

			}
		});
		btnNewButton_7.setBounds(109, 113, 89, 23);
		frame.getContentPane().add(btnNewButton_7);

		JButton btnNewButton_8 = new JButton("Bot10");
		btnNewButton_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run() {

						load(btnNewButton_8.getText());
						try {
							startBot();
						} catch (FileNotFoundException | InterruptedException e) {
							
							e.printStackTrace();
						}

					}
				}.start();
			}
		});
		btnNewButton_8.setBounds(109, 147, 89, 23);
		frame.getContentPane().add(btnNewButton_8);

		JButton button = new JButton("Bot11");
		button.setBounds(208, 11, 89, 23);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run() {

						load(button.getText());
						try {
							startBot();
						} catch (FileNotFoundException | InterruptedException e) {
							
							e.printStackTrace();
						}

					}
				}.start();

			}
		});
		frame.getContentPane().add(button);

		JButton button_1 = new JButton("Bot12");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run() {

						load(button_1.getText());
						try {
							startBot();
						} catch (FileNotFoundException | InterruptedException e) {
							
							e.printStackTrace();
						}

					}
				}.start();

			}
		});
		button_1.setBounds(208, 45, 89, 23);
		frame.getContentPane().add(button_1);

		JButton button_2 = new JButton("Bot13");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run() {

						load(button_2.getText());
						try {
							startBot();
						} catch (FileNotFoundException | InterruptedException e) {

							e.printStackTrace();
						}

					}
				}.start();

			}
		});
		button_2.setBounds(208, 79, 89, 23);
		frame.getContentPane().add(button_2);

		JButton button_3 = new JButton("Bot14");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run() {

						load(button_3.getText());
						try {
							startBot();
						} catch (FileNotFoundException | InterruptedException e) {
							
							e.printStackTrace();
						}

					}
				}.start();

			}
		});
		button_3.setBounds(208, 113, 89, 23);
		frame.getContentPane().add(button_3);

		JButton button_4 = new JButton("Bot15");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run() {

						load(button_4.getText());
						try {
							startBot();
						} catch (FileNotFoundException | InterruptedException e) {
							
							e.printStackTrace();
						}

					}
				}.start();

			}
		});
		button_4.setBounds(208, 147, 89, 23);
		frame.getContentPane().add(button_4);

		JButton button_5 = new JButton("Bot16");
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run() {

						load(button_5.getText());
						try {
							startBot();
						} catch (FileNotFoundException | InterruptedException e) {
							
							e.printStackTrace();
						}

					}
				}.start();

			}
		});
		button_5.setBounds(307, 11, 89, 23);
		frame.getContentPane().add(button_5);

		JButton button_6 = new JButton("Bot17");
		button_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run() {

						load(button_6.getText());
						try {
							startBot();
						} catch (FileNotFoundException | InterruptedException e) {
							
							e.printStackTrace();
						}

					}
				}.start();

			}
		});
		button_6.setBounds(307, 45, 89, 23);
		frame.getContentPane().add(button_6);

		JButton button_7 = new JButton("Bot18");
		button_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run() {

						load(button_7.getText());
						try {
							startBot();
						} catch (FileNotFoundException | InterruptedException e) {
							
							e.printStackTrace();
						}

					}
				}.start();

			}
		});
		button_7.setBounds(307, 79, 89, 23);
		frame.getContentPane().add(button_7);

		JButton button_8 = new JButton("Bot19");
		button_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run() {

						load(button_8.getText());
						try {
							startBot();
						} catch (FileNotFoundException | InterruptedException e) {
							e.printStackTrace();
						}

					}
				}.start();

			}
		});
		button_8.setBounds(307, 113, 89, 23);
		frame.getContentPane().add(button_8);

		JButton button_9 = new JButton("Bot20");
		button_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run() {

						load(button_9.getText());
						try {
							startBot();
						} catch (FileNotFoundException | InterruptedException e) {
							e.printStackTrace();
						}

					}
				}.start();

			}
		});
		button_9.setBounds(307, 147, 89, 23);
		frame.getContentPane().add(button_9);

		ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("/images/KripzySketch2.png"));
		Image image = imageIcon.getImage(); // transform it
		imageIcon = new ImageIcon(image);

		frame.setResizable(false);
	}

}
