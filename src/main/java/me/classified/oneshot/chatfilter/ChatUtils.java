package me.classified.oneshot.chatfilter;

import com.google.common.collect.Lists;
import me.classified.oneshot.OneShotCore;
import me.classified.oneshot.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatUtils {

    protected static void clearChat(Player whoCleared) {
        String clearer = (whoCleared == null) ? "Console" : whoCleared.getName();
        for (Player player : OneShotCore.getInstance().getServer().getOnlinePlayers()) {
          for (int i = 0; i < 100; i++){
                Bukkit.broadcastMessage(" ");
            }
            String ccformat = Utils.color("&3&l(!) &3The chat has been cleared by &c&l" + clearer + "");
            player.sendMessage(Utils.color(ccformat));
            player.sendMessage(" ");
            player.sendMessage(" ");
        }
        whoCleared.sendMessage(Utils.color("&a&l(!) &aYou have cleared the chat."));
    }

    protected static void toggleChat(Player whoToggled) {
        String toggler = (whoToggled == null) ? "Console" : whoToggled.getName();
        if (!ChatFilter.isChatHalted()) {
            ChatFilter.setChatHalted(true);
            for (Player player : OneShotCore.getInstance().getServer().getOnlinePlayers()) {
                for (int i = 0; i < 75; i++)
                    player.sendMessage(" ");
                player.sendMessage(" ");
                player.sendMessage(" ");
                player.sendMessage(Utils.color("&3&l(!) &3Global chat mute has been &c&l&nDISABLED&3."));
                player.sendMessage(" ");
                player.sendMessage(" ");}
            if (whoToggled != null)
                whoToggled.sendMessage(Utils.color("&c&l(!) &7You have toggled the chat &c&lOFF&7."));
        } else {
            ChatFilter.setChatHalted(false);
            for (Player player : OneShotCore.getInstance().getServer().getOnlinePlayers()) {
                for (int i = 0; i < 75; i++)
                    player.sendMessage(" ");
                player.sendMessage(" ");
                player.sendMessage(" ");
                player.sendMessage(Utils.color("&3&l(!) &3Global chat mute has been &a&l&nENABLED&3."));
                player.sendMessage(" ");
                player.sendMessage(" ");}
            if (whoToggled != null)
            whoToggled.sendMessage(Utils.color("&c&l(!) &7You have toggled the chat &a&lON&7."));
        }
    }

    protected static boolean checkMessageForAdvertising(String message) {
        if (checkMessageForIP(message)) {
            Bukkit.getLogger().info("Matched IP to " + message);
            return true;
        }
        if (checkMessageForWebPattern(message)) {
            Bukkit.getLogger().info("Matched Web Pattern to " + message);
            return true;
        }
        return false;
    }

    public static boolean checkMessageForIP(String message) {
        message = message.replace(" ", "");
        Matcher regexMatcher = ChatFilter.ipPattern.matcher(message);
        while (regexMatcher.find()) {
            if (regexMatcher.group().length() != 0 &&
                    ChatFilter.ipPattern.matcher(message).find())
                return true;
        }
        return false;
    }

    public static boolean checkMessageForWebPattern(String message) {
        message = message.toLowerCase();
        message = message.replace("dt", ".").replace(" ", "").replace("<!>", ".").replace("[!]", ".").replace("(!)", ".").replace("0", "o").replace("3", "e").replace("1", "l").replace("4", "a").replace("7", "t").replace("(", "").replace(")", "").replace("{", "").replace("}", "").replace("[", "").replace("]", "").replace(",", ".").replace(">", "").replace("<", "").replace("*", ".").replace("^", "").replace("/", "").replace("\\", "").replace("-", "").replace("dot", ".").replace("d0t", ".");
        Matcher regexMatcherurl = ChatFilter.webpattern.matcher(message);
        String quoted = Pattern.quote("www.");
        while (regexMatcherurl.find()) {
            String text = regexMatcherurl.group().trim().replaceAll(quoted, "").replaceAll("http://", "").replaceAll("https://", "");
            if (regexMatcherurl.group().length() != 0 && text.length() != 0 &&
                    ChatFilter.webpattern.matcher(message).find() && (!message.toLowerCase().contains("oneshotmc.com")))
                return true;
        }
        return false;
    }

    private static String buildPlaceholders(int length, String placeHolderChar) {
        StringBuilder placeHolderBuilder = new StringBuilder();
        for (int i = 1; i <= length; i++)
            placeHolderBuilder.append(placeHolderChar);
        return placeHolderBuilder.toString();
    }

    private static int getUppercaseCount(String word) {
        int count = 0;
        for (int i = 0; i <= word.length() - 1; i++) {
            if (Character.isUpperCase(word.charAt(i)))
                count++;
        }
        return count;
    }

    public static String getFilteredMessage(String message, Player receiver, boolean filterUppercase) {
        if (receiver != null) return getFilteredSwearMessage(message, true);
        String msg = getFilteredSwearMessage(message, false);
        if (filterUppercase)
            msg = getFilteredUppercaseMessage(msg);
        return msg;
    }

    public static String getFilteredSwearMessage(String message) {
        return getFilteredSwearMessage(message, true);
    }

    public static String getFilteredSwearMessage(String message, boolean permanentFilter) {
        String oMessage = message.startsWith(" ") ? message.substring(1, message.length()) : message;
        StringBuilder newMessage = new StringBuilder();
        List<String> filters = permanentFilter ? ChatFilter.permanent_bad_words : Lists.newArrayList(ChatFilter.bad_words);
        if (!permanentFilter)
            filters.addAll(ChatFilter.permanent_bad_words);
        for (String s : filters) {
            if (message.toLowerCase().contains(s.toLowerCase()))
                try {
                    if (s.contains("{") || s.contains("}")) {
                        message = message.replace(s, buildPlaceholders(s.length(), "*"));
                        continue;
                    }
                    message = Pattern.compile(s, 2).matcher(message).replaceAll(buildPlaceholders(s.length(), "*"));
                } catch (Exception err) {
                    err.printStackTrace();
                }
        }
        for (String word : message.split(" ")) {
            if (newMessage.length() > 0)
                newMessage.append(" ");
            if (word.length() <= 1) {
                newMessage.append(word);
            } else if ((permanentFilter && isPermanentSwearWord(word.toLowerCase())) || (!permanentFilter && isSwearWord(word.toLowerCase()))) {
                newMessage.append(buildPlaceholders(word.length(), "*"));
            } else {
                newMessage.append(word);
            }
        }
        return newMessage.toString();
    }

    public static boolean isSwearMessage(String message) {
        StringBuilder newMessage = new StringBuilder();
        for (String s : ChatFilter.bad_words) {
            if (message.toLowerCase().contains(s.toLowerCase()))
                return true;
        }
        for (String s : ChatFilter.permanent_bad_words) {
            if (message.toLowerCase().contains(s.toLowerCase()))
                return true;
        }
        for (String word : message.split(" ")) {
            if (newMessage.length() > 0)
                newMessage.append(" ");
            if (word.length() <= 1) {
                newMessage.append(word);
            } else {
                if (isSwearWord(word.toLowerCase()))
                    return true;
                newMessage.append(word);
            }
        }
        return false;
    }

    protected static boolean isPermanentSwearWord(String word) {
        if (ChatFilter.permanent_bad_words.contains(word.toLowerCase()))
            return true;
        for (String sw : ChatFilter.permanent_bad_words) {
            if (word.contains(sw))
                return true;
        }
        List<Character> delimeters = Arrays.asList(new Character[] {
                Character.valueOf('.'), Character.valueOf(','), Character.valueOf(';'), Character.valueOf(':'), Character.valueOf('-'), Character.valueOf('_'), Character.valueOf('|'), Character.valueOf('/'), Character.valueOf('?'), Character.valueOf('('),
                Character.valueOf(')'), Character.valueOf('!'), Character.valueOf('@'), Character.valueOf('#'), Character.valueOf('4'), Character.valueOf('5'), Character.valueOf('6'), Character.valueOf('7'), Character.valueOf('8'), Character.valueOf('9'),
                Character.valueOf('0'), Character.valueOf('`'), Character.valueOf('~'), Character.valueOf('+'), Character.valueOf('<'), Character.valueOf('>') });
        String noDelimeterWord = "";
        for (char c : word.toCharArray()) {
            if (!delimeters.contains(Character.valueOf(c)))
                noDelimeterWord = noDelimeterWord + c;
        }
        for (String sw : ChatFilter.permanent_bad_words) {
            if (noDelimeterWord.contains(sw))
                return true;
        }
        return false;
    }

    protected static boolean isSwearWord(String word) {
        if (ChatFilter.bad_words.contains(word.toLowerCase()))
            return true;
        for (String sw : ChatFilter.bad_words) {
            if (word.contains(sw))
                return true;
        }
        List<Character> delimeters = Arrays.asList(new Character[] {
                Character.valueOf('.'), Character.valueOf(','), Character.valueOf(';'), Character.valueOf(':'), Character.valueOf('-'), Character.valueOf('_'), Character.valueOf('|'), Character.valueOf('/'), Character.valueOf('?'), Character.valueOf('('),
                Character.valueOf(')'), Character.valueOf('!'), Character.valueOf('@'), Character.valueOf('#'), Character.valueOf('4'), Character.valueOf('5'), Character.valueOf('6'), Character.valueOf('7'), Character.valueOf('8'), Character.valueOf('9'),
                Character.valueOf('0'), Character.valueOf('`'), Character.valueOf('~'), Character.valueOf('+'), Character.valueOf('<'), Character.valueOf('>') });
        String noDelimeterWord = "";
        for (char c : word.toCharArray()) {
            if (!delimeters.contains(Character.valueOf(c)))
                noDelimeterWord = noDelimeterWord + c;
        }
        for (String sw : ChatFilter.bad_words) {
            if (noDelimeterWord.contains(sw))
                return true;
        }
        return false;
    }

    public static String getFilteredUppercaseMessage(String message) {
        StringBuilder newMessage = new StringBuilder();
        for (String word : message.split(" ")) {
            if (newMessage.length() > 0)
                newMessage.append(" ");
            if (word.length() <= 1) {
                newMessage.append(word);
            } else if (getUppercaseCount(word) > 3) {
                newMessage.append(word.toLowerCase());
            } else {
                newMessage.append(word);
            }
        }
        return newMessage.toString();
    }
}
