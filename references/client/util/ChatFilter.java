package org.rscemulation.client.util;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.rscemulation.client.mudclient;

public class ChatFilter {
	static String badwords[] = {
		"fuck", "cunt", "fag", "niga", "nigger", "shit", "wank", "prick", "faggot", "cock", "penis", "dick", "dickhead", "bitch", "slut", "slag", "whore", "pussy", "knob"
	};
	
	static String spamwords[] = {
		"http", "www.", ".info", ".org", ".com", ".net", "hotmail", "msn", ".tk", ",tk", ",net", ",com", ",org", ",info", "www,", "www-", "-com", "-org", "-net", "-tk", "-info", ".info", ",info",
		"storkpk", "stork", "darkquest", "dq", "peeter", "kingpk", "rscaftermath", "rsca", "aftermath"
	};
	
	public static String censorChat(String in) {
		if (mudclient.chatFilter == false)
			return censorSpam(in);
		for (int i = 0; i < 2; i++) {
			String orig = in;
			db_ptr = 0;
			int j = 0;
			for (int charpos = 0; charpos < in.length(); charpos++) {
				char charry = in.charAt(charpos);
				if (charry >= 'A' && charry <= 'Z')
					charry = (char)((charry + 97) - 65);
				if (charry == '@' && charpos + 4 < in.length() && in.charAt(charpos + 4) == '@')
					charpos += 4;
				else {
					byte type;
					if (charry >= 'a' && charry <= 'z' || charry >= '0' && charry <= '9')
						type = 0;
					else if (charry == '\'')
						type = 1;
					else if (charry == '\r' || charry == ' ' || charry == '.' || charry == ',' || charry == '-' || charry == '(' || charry == ')' || charry == '?' || charry == '!')
						type = 2;
					else
						type = 3;
					int l = db_ptr;
					for (int i1 = 0; i1 < l; i1++)
						if (type == 3) {
							if (tdb[i1] > 0 && tdb[i1] < pdb[i1] + badwords_Db[i1].length() / 2) {
								tdb[db_ptr] = tdb[i1] + 1;
								sdb[db_ptr] = sdb[i1];
								qdb[db_ptr] = qdb[i1] + 1;
								pdb[db_ptr] = pdb[i1];
								badwords_Db[db_ptr++] = badwords_Db[i1];
								tdb[i1] = -tdb[i1];
							}
						} else {
							char c1 = badwords_Db[i1].charAt(qdb[i1]);
							if (leet(charry, c1)) {
								qdb[i1]++;
								if (tdb[i1] < 0)
									tdb[i1] = -tdb[i1];
							} else if ((charry == ' ' || charry == '\r') && pdb[i1] == 0)
								qdb[i1] = 0x1869f;
							else {
								char c2 = badwords_Db[i1].charAt(qdb[i1] - 1);
								if(type == 0 && !leet(charry, c2))
									qdb[i1] = 0x1869f;
							}
						}

					if (type >= 2)
						j = 1;
					if (type <= 2) {
						for (int bw1_ptr = 0; bw1_ptr < badwords.length; bw1_ptr++)
							if (leet(charry, badwords[bw1_ptr].charAt(0))) {
								tdb[db_ptr] = 1;
								sdb[db_ptr] = charpos;
								qdb[db_ptr] = 1;
								pdb[db_ptr] = 1;
								badwords_Db[db_ptr++] = badwords[bw1_ptr];
							}
					}
					for (int k1 = 0; k1 < db_ptr; k1++)
						if (qdb[k1] >= badwords_Db[k1].length()) {
							if (qdb[k1] < 0x1869f) {
								String s1 = "";
								for (int k2 = 0; k2 < in.length(); k2++)
									if (k2 < sdb[k1] || k2 > charpos)
										s1 = s1 + in.charAt(k2);
									else
										s1 = s1 + "*";

								in = s1;
							}
							db_ptr--;
							for (int i2 = k1; i2 < db_ptr; i2++) {
								pdb[i2] = pdb[i2 + 1];
								qdb[i2] = qdb[i2 + 1];
								badwords_Db[i2] = badwords_Db[i2 + 1];
								sdb[i2] = sdb[i2 + 1];
								tdb[i2] = tdb[i2 + 1];
							}

							k1--;
						}

				}
			}

			if (in.equalsIgnoreCase(orig))
				break;
		}

		return censorSpam(in);
	}
	
	public static String censorSpam(String in) {
		for (int i = 0; i < 2; i++) {
			String orig = in;
			db_ptr = 0;
			int j = 0;
			for (int charpos = 0; charpos < in.length(); charpos++) {
				char charry = in.charAt(charpos);
				if (charry >= 'A' && charry <= 'Z')
					charry = (char)((charry + 97) - 65);
				if (charry == '@' && charpos + 4 < in.length() && in.charAt(charpos + 4) == '@')
					charpos += 4;
				else {
					byte type;
					if (charry >= 'a' && charry <= 'z' || charry >= '0' && charry <= '9')
						type = 0;
					else if (charry == '\'')
						type = 1;
					else if (charry == '\r' || charry == ' ' || charry == '.' || charry == ',' || charry == '-' || charry == '(' || charry == ')' || charry == '?' || charry == '!')
						type = 2;
					else
						type = 3;
					int l = db_ptr;
					for (int i1 = 0; i1 < l; i1++)
						if (type == 3) {
							if (tdb[i1] > 0 && tdb[i1] < pdb[i1] + spamwords_Db[i1].length() / 2) {
								tdb[db_ptr] = tdb[i1] + 1;
								sdb[db_ptr] = sdb[i1];
								qdb[db_ptr] = qdb[i1] + 1;
								pdb[db_ptr] = pdb[i1];
								spamwords_Db[db_ptr++] = spamwords_Db[i1];
								tdb[i1] = -tdb[i1];
							}
						} else {
							char c1 = spamwords_Db[i1].charAt(qdb[i1]);
							if (leet(charry, c1)) {
								qdb[i1]++;
								if (tdb[i1] < 0)
									tdb[i1] = -tdb[i1];
							} else if ((charry == ' ' || charry == '\r') && pdb[i1] == 0)
								qdb[i1] = 0x1869f;
							else {
								char c2 = spamwords_Db[i1].charAt(qdb[i1] - 1);
								if(type == 0 && !leet(charry, c2))
									qdb[i1] = 0x1869f;
							}
						}

					if (type >= 2)
						j = 1;
					if (type <= 2) {
						for (int bw1_ptr = 0; bw1_ptr < spamwords.length; bw1_ptr++)
							if (leet(charry, spamwords[bw1_ptr].charAt(0))) {
								tdb[db_ptr] = 1;
								sdb[db_ptr] = charpos;
								qdb[db_ptr] = 1;
								pdb[db_ptr] = 1;
								spamwords_Db[db_ptr++] = spamwords[bw1_ptr];
							}
					}
					for (int k1 = 0; k1 < db_ptr; k1++)
						if (qdb[k1] >= spamwords_Db[k1].length()) {
							if (qdb[k1] < 0x1869f) {
								String s1 = "";
								for (int k2 = 0; k2 < in.length(); k2++)
									if (k2 < sdb[k1] || k2 > charpos)
										s1 = s1 + in.charAt(k2);
									else
										s1 = s1 + "*";

								in = s1;
							}
							db_ptr--;
							for (int i2 = k1; i2 < db_ptr; i2++) {
								pdb[i2] = pdb[i2 + 1];
								qdb[i2] = qdb[i2 + 1];
								spamwords_Db[i2] = spamwords_Db[i2 + 1];
								sdb[i2] = sdb[i2 + 1];
								tdb[i2] = tdb[i2 + 1];
							}

							k1--;
						}

				}
			}

			if (in.equalsIgnoreCase(orig))
				break;
		}

		return in;
	}	
	
	static boolean leet(char arg0, char arg1) {
		if (arg0 == arg1)
			return true;
		if (arg1 == 'i' && (arg0 == 'l' || arg0 == '1' || arg0 == '!' || arg0 == '|' || arg0 == '\246'))
			return true;
		if (arg1 == 'l' && (arg0 == 'i' || arg0 == '1' || arg0 == '!' || arg0 == '|' || arg0 == '\246'))
			return true;
		if (arg1 == 's' && (arg0 == '5' || arg0 == '$' || arg0 == 'z'))
			return true;
		if (arg1 == 'z' && (arg0 == '5' || arg0 == '$' || arg0 == 's'))
			return true;
		if (arg1 == 'a' && (arg0 == '4' || arg0 == '@'))
			return true;
		if (arg1 == 'c' && (arg0 == '(' || arg0 == '<' || arg0 == '['))
			return true;
		if (arg1 == 'o' && arg0 == '0')
			return true;
		if (arg1 == 'e' && arg0 == '3')
			return true;
		return arg1 == 'u' && arg0 == 'v';
	}
	
	static int db_ptr;
	static int pdb[] = new int[1000];
	static int qdb[] = new int[1000];
	static String badwords_Db[] = new String[1000];
	static String spamwords_Db[] = new String[1000];
	static int sdb[] = new int[1000];
	static int tdb[] = new int[1000];
}