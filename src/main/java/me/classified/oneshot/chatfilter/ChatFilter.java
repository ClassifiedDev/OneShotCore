package me.classified.oneshot.chatfilter;

import me.classified.oneshot.OneShotCore;
import org.bukkit.Bukkit;

import java.util.*;
import java.util.regex.Pattern;

public class ChatFilter {

    protected static List<String> bad_words = new ArrayList<>();
    protected static List<String> permanent_bad_words = new ArrayList<>();
    protected static List<UUID> hasntMoved = new ArrayList<>();
    protected static HashMap<UUID, String> lastMessage = new HashMap<>();
    protected static HashMap<UUID, Long> lastMessage_time = new HashMap<>();
    protected static boolean block_chat_until_move = true;
    protected static boolean block_duplicate_messages = true;
    protected static boolean remove_chat_colors = false;
    protected static final Pattern ipPattern = Pattern.compile("((?<![0-9])(?:(?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})[ ]?[.,-:; ][ ]?(?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})[ ]?[., ][ ]?(?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})[ ]?[., ][ ]?(?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2}))(?![0-9]))");
    protected static final Pattern webpattern = Pattern.compile("[-a-zA-Z0-9@:%_\\+.~#?&//=]{2,256}\\.[a-z]{2,4}\\b(\\/[-a-zA-Z0-9@:%_\\+.~#?&//=]*)?");

    private static boolean chat_haulted = false;

    public ChatFilter() {
        enable();
    }

    public void enable() {
        bad_words = OneShotCore.getConfigFile().getStringList("chat_filter.bad_words");
        block_chat_until_move = OneShotCore.getConfigFile().getBoolean("chat_filter.block_chat_until_move");
        block_duplicate_messages = OneShotCore.getConfigFile().getBoolean("chat_filter.no_duplicates");
        remove_chat_colors = OneShotCore.getConfigFile().getBoolean("chat_filter.remove_chat_colors");
        for (String s : OneShotCore.getConfigFile().getStringList("chat_filter.bad_words")) {
            if (!bad_words.contains(s)) {
                bad_words.add(s);
                Bukkit.getLogger().info("(CommandBlocker) Added command '" + s + "' to blocked commands list (default).");
            }
        }
    }

    public void disable() {
        chat_haulted = false;
    }

    protected static boolean isChatHalted() {
        return chat_haulted;
    }

    protected static void setChatHalted(boolean b) {
        chat_haulted = b;
    }
}

