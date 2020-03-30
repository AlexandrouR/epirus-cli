/*
 * Copyright 2019 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package io.epirus.console;

import io.epirus.console.account.AccountManager;
import io.epirus.console.config.CliConfig;
import io.epirus.console.deploy.DeployRunner;
import io.epirus.console.project.ProjectCreator;
import io.epirus.console.project.ProjectImporter;
import io.epirus.console.project.UnitTestCreator;
import io.epirus.console.update.Updater;
import io.epirus.console.utils.Version;

import org.web3j.codegen.Console;
import org.web3j.codegen.SolidityFunctionWrapperGenerator;
import org.web3j.codegen.TruffleJsonFunctionWrapperGenerator;

import static io.epirus.console.project.ProjectCreator.COMMAND_NEW;
import static io.epirus.console.project.ProjectImporter.COMMAND_IMPORT;
import static io.epirus.console.project.UnitTestCreator.COMMAND_GENERATE_TESTS;
import static org.web3j.codegen.SolidityFunctionWrapperGenerator.COMMAND_SOLIDITY;
import static org.web3j.utils.Collection.tail;

/** Main entry point for running command line utilities. */
public class Runner {

    private static final String USAGE =
            "Usage: epirus version|wallet|solidity|new|import|generate-tests|audit|account ...";

    private static final String LOGO =
            // Generated with https://asciiart.club/
            "║hh»ñ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒H  \n"
                    + "hhhhhh»ñ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒Γ  \n"
                    + "\"╚hhhhhh»»ñ╜╜╜╜╜╜╜╜╜╜╜╜╜╜╜╜   \n"
                    + "   \"Ühhhhhh╠ε,                \n"
                    + "      \"Ühhhhhh╠ε              \n"
                    + "        `\"║hhhhhh             \n"
                    + "        ,╥▓▓▄»hhh             \n"
                    + "      ╔▓ÑÑÑÑÑ▓▓M`             \n"
                    + "   ╓╣ÑÑÑÑÑÑÑÅ\"                \n"
                    + "╓╢ÑÑÑÑÑÑÑ▒Ω,,,,,      ,,,,    \n"
                    + "▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒    ║hhhhhΓ  \n"
                    + "▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒    ╠hhhhhΓ  \n"
                    + "`╙╙╙╙╙╙╙╙╙╙╙╙╙╙╙\"    `╙╙╙╙\"   ";

    public static void main(String[] args) throws Exception {
        System.out.println(LOGO);
        CliConfig config = CliConfig.getConfig(CliConfig.getDefaultEpirusConfigPath().toFile());
        Updater updater = new Updater(config);
        updater.promptIfUpdateAvailable();
        Thread updateThread = new Thread(updater::onlineUpdateCheck);
        updateThread.setDaemon(true);
        updateThread.start();

        if (args.length < 1) {
            Console.exitError(USAGE);
        } else {
            switch (args[0]) {
                case "deploy":
                    DeployRunner.main(tail(args));
                    break;
                case "wallet":
                    WalletRunner.run(tail(args));
                    break;
                case COMMAND_SOLIDITY:
                    SolidityFunctionWrapperGenerator.main(tail(args));
                    break;
                case "truffle":
                    TruffleJsonFunctionWrapperGenerator.run(tail(args));
                    break;
                case COMMAND_NEW:
                    ProjectCreator.main(tail(args));
                    break;
                case COMMAND_IMPORT:
                    ProjectImporter.main(tail(args));
                    break;
                case "version":
                    Console.exitSuccess(
                            "Version: "
                                    + Version.getVersion()
                                    + "\n"
                                    + "Build timestamp: "
                                    + Version.getTimestamp());
                    break;
                case "audit":
                    ContractAuditor.main(tail(args));
                    break;
                case "account":
                    AccountManager.main(config, tail(args));
                    break;
                case COMMAND_GENERATE_TESTS:
                    UnitTestCreator.main(tail(args));
                    break;
                default:
                    Console.exitError(USAGE);
            }
        }
        config.save();
        Console.exitSuccess();
    }
}
