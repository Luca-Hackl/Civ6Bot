package com.github.lucahackl.civ6bot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class Main extends ListenerAdapter {

    public static void main(String[] args) throws LoginException {
        String token = System.getenv("BOT_TOKEN");
        if(token != null && token.length()>0){
            if(args.length == 0){
                args = new String[]{token};

            }else{
                args[0] = token;
            }
        }

        if (args.length < 1) {
            System.out.println("You have to provide a token as first argument!");
            System.exit(1);
        }
        System.out.println("Using Token:" + token);

        // args[0] should be the token
        // We only need 2 intents in this bot. We only respond to messages in guilds and private channels.
        // All other events will be disabled.
        JDABuilder.createLight(args[0], GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                .addEventListeners(new Main())
                .setActivity(Activity.playing("Always ready to choose your civ"))
                .build();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        String substring = msg.getContentRaw().substring(0, 3);
        if (substring.equals("civ")) {
            if (msg.getContentRaw().substring(7).contains("a")) {
                int numb = Integer.parseInt(msg.getContentRaw().substring(msg.getContentRaw().length() - 1)) + 1;
                for (int y = 1; y < numb; y++) {
                    String SendingMessage = SQLStart.Start(msg.getContentRaw(), y);
                    MessageChannel channel = event.getChannel();
                    channel.sendMessage(SendingMessage).queue();
                }
            } else {
                int numb = Integer.parseInt(msg.getContentRaw().substring(msg.getContentRaw().length() - 1));
                String SendingMessage = SQLStart.Start(msg.getContentRaw(), numb);
                MessageChannel channel = event.getChannel();
                channel.sendMessage(SendingMessage).queue();
            }

        }
    }
}
