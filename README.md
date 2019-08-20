
# Hypixel-Autoguild
This is the code for the bot that I used to run the Lostshard Hypixel guild. 
Hypixel has limited guild invites to 10 every 5 minutes, so this code no longer works.
However, I have provided it for reference.

This written in the spaghettiest if spaghetti code. I did not originally intend to make this code public. I also did not do any planning. My goal at the time was to make the mod a fast as possible. To implement additional functionality, I would just write ontop of the spaghetti code. However, I was still able to achieve what I wanted to do.

The provided jar does not contain the automated guild messages. To enable those, you will have to compile the code yourself.
To enable, goto line 288 of AutoGuildTickHandler.java

### MC-version
1.8.9

### Features
1. Invites people from games and lobbies to your guild. (now limited to 10 every 5 minutes).
2. Kicks offline players when the guild fills up.
3. Promotes specified players to the "Supporter" guild rank when they join the guild. People with this rank get kicked last.
4. Mutes players who spam chat. 
5. Kicks blacklisted players when they join the guild. This is really useful if you want to ban someone from multiple guilds.
