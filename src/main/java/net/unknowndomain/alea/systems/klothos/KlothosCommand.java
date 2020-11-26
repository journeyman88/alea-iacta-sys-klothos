/*
 * Copyright 2020 Marco Bignami.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.unknowndomain.alea.systems.klothos;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import net.unknowndomain.alea.command.HelpWrapper;
import net.unknowndomain.alea.messages.ReturnMsg;
import net.unknowndomain.alea.systems.RpgSystemCommand;
import net.unknowndomain.alea.systems.RpgSystemDescriptor;
import net.unknowndomain.alea.roll.GenericRoll;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author journeyman
 */
public class KlothosCommand extends RpgSystemCommand
{
    private static final Logger LOGGER = LoggerFactory.getLogger(KlothosCommand.class);
    private static final RpgSystemDescriptor DESC = new RpgSystemDescriptor("Klothos RPG", "kts", "klothos");
    
    private static final String APPROACH_PARAM = "approach";
    private static final String SPEC_PARAM = "specialization";
    private static final String ENEMY_PARAM = "enemy-spec";
    private static final String BOND_PARAM = "bond";
    
    private static final Options CMD_OPTIONS;
    
    static {
        
        CMD_OPTIONS = new Options();
        CMD_OPTIONS.addOption(
                Option.builder("a")
                        .longOpt(APPROACH_PARAM)
                        .desc("Approach modifier to the roll")
                        .hasArg()
                        .argName("approachModifier")
                        .build()
        );
        CMD_OPTIONS.addOption(
                Option.builder("s")
                        .longOpt(SPEC_PARAM)
                        .desc("Number of specialization applicable")
                        .hasArg()
                        .argName("specValue")
                        .build()
        );
        CMD_OPTIONS.addOption(
                Option.builder("e")
                        .longOpt(ENEMY_PARAM)
                        .desc("Set the enemy as specialized")
                        .build()
        );
        CMD_OPTIONS.addOption(
                Option.builder("b")
                        .longOpt(BOND_PARAM)
                        .desc("Set as been aided by an ally with whom shares a bond")
                        .build()
        );
        CMD_OPTIONS.addOption(
                Option.builder("h")
                        .longOpt( CMD_HELP )
                        .desc( "Print help")
                        .build()
        );
        CMD_OPTIONS.addOption(
                Option.builder("v")
                        .longOpt(CMD_VERBOSE)
                        .desc("Enable verbose output")
                        .build()
        );
    }
    
    public KlothosCommand()
    {
        
    }
    
    @Override
    public RpgSystemDescriptor getCommandDesc()
    {
        return DESC;
    }

    @Override
    protected Logger getLogger()
    {
        return LOGGER;
    }
    
    @Override
    protected Optional<GenericRoll> safeCommand(String cmdParams)
    {
        Optional<GenericRoll> retVal;
        try
        {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(CMD_OPTIONS, cmdParams.split(" "));

            if (
                    cmd.hasOption(CMD_HELP)
                )
            {
                return Optional.empty();
            }


            Set<KlothosModifiers> mods = new HashSet<>();

            int s = 0;
            if (cmd.hasOption(CMD_VERBOSE))
            {
                mods.add(KlothosModifiers.VERBOSE);
            }
            if (cmd.hasOption(BOND_PARAM))
            {
                mods.add(KlothosModifiers.BONDED_ALLY);
            }
            if (cmd.hasOption(ENEMY_PARAM))
            {
                mods.add(KlothosModifiers.ENEMY_SPEC);
            }
            if (cmd.hasOption(SPEC_PARAM))
            {
                s = Integer.parseInt(cmd.getOptionValue(SPEC_PARAM));
            }
            int a = Integer.parseInt(cmd.getOptionValue(APPROACH_PARAM));
            GenericRoll roll = new KlothosRoll(a, s, mods);
            retVal = Optional.of(roll);
        } 
        catch (ParseException | NumberFormatException ex)
        {
            retVal = Optional.empty();
        }
        return retVal;
    }
    
    @Override
    public ReturnMsg getHelpMessage(String cmdName)
    {
        return HelpWrapper.printHelp(cmdName, CMD_OPTIONS, true);
    }
    
}
