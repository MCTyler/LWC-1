/**
 * This file is part of LWC (https://github.com/Hidendra/LWC)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.griefcraft.commands;

import static com.griefcraft.util.StringUtils.capitalizeFirstLetter;
import static com.griefcraft.util.StringUtils.hasFlag;
import static com.griefcraft.util.StringUtils.join;
import static com.griefcraft.util.StringUtils.transform;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.griefcraft.lwc.LWC;
import com.griefcraft.util.Colors;

public class Create implements ICommand {

	@Override
	public String getName() {
		return "creation";
	}
	
	@Override
	public boolean supportsConsole() {
		return false;
	}

	@Override
	public void execute(LWC lwc, CommandSender sender, String[] args) {
		if (args.length == 1) {
			sendHelp(sender);
			return;
		}

		String type = args[1].toLowerCase();
		String full = join(args, 1);
		Player player = (Player) sender;

		if (type.equals("trap")) {
			if (!lwc.isAdmin(sender)) {
				sender.sendMessage(Colors.Blue + "[lwc] " + Colors.Red + "Permission denied. ");
				return;
			}

			if (args.length < 3) {
				lwc.sendSimpleUsage(sender, "/lwc -c trap <kick/ban> [reason]");
				return;
			}
		}

		else if (type.equals("password")) {
			if (args.length < 3) {
				lwc.sendSimpleUsage(sender, "/lwc -c password <Password>");
				return;
			}

			String password = join(args, 2);
			String hiddenPass = transform(password, '*');

			sender.sendMessage(Colors.Blue + "Using password: " + Colors.Yellow + hiddenPass);
		}

		else if (!type.equals("public") && !type.equals("private")) {
			sendHelp(sender);
			return;
		}

		lwc.getMemoryDatabase().unregisterAllActions(player.getName());
		lwc.getMemoryDatabase().registerAction("create", player.getName(), full);

		sender.sendMessage(Colors.Blue + "Lock type: " + Colors.Green + capitalizeFirstLetter(type));
		sender.sendMessage(Colors.Green + "Please left click your block to lock it.");
	}

	@Override
	public boolean validate(LWC lwc, CommandSender player, String[] args) {
		return hasFlag(args, "c") || hasFlag(args, "create");
	}

	public void sendHelp(CommandSender player) {
		player.sendMessage(" ");
		player.sendMessage(Colors.Green + "LWC Protection");
		player.sendMessage(" ");

		player.sendMessage("/lwc -c public " + Colors.Gold + "Create a public protection");
		player.sendMessage(Colors.Blue + "Anyone can use a Public protection, but no one can protect it");
		player.sendMessage(" ");

		player.sendMessage("/lwc -c password <password> " + Colors.Gold + "Create a passworded protection");
		player.sendMessage(Colors.Blue + "Each time you login you need to enter the password to access");
		player.sendMessage(Colors.Blue + "it (if someone knows the pass, they can use it too!)");
		player.sendMessage(" ");

		player.sendMessage("/lwc -c private " + Colors.Gold + "Create a private protection");
		player.sendMessage(Colors.Blue + "Private means private. You can also allow other users or");
		player.sendMessage(Colors.Blue + "groups to access the chest or furnace. This is done by");
		player.sendMessage(Colors.Blue + "adding them after \"private\".");
		player.sendMessage(" ");
		player.sendMessage("Example:");
		player.sendMessage(Colors.Blue + "/lwc -c private UserName g:GroupName OtherGuy");
		player.sendMessage(" ");
		player.sendMessage(Colors.Blue + "You can specify more than 1 group and/or user per command!");
	}

}
