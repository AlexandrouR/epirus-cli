/*
 * Copyright 2020 Web3 Labs Ltd.
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
package io.epirus.console.docker.subcommands;

import io.epirus.console.EpirusVersionProvider;
import io.epirus.console.docker.DockerOperations;
import picocli.CommandLine;

import org.web3j.codegen.Console;

import static io.epirus.console.config.ConfigManager.config;

@CommandLine.Command(
        name = "run",
        description = "Run project in docker",
        showDefaultValues = true,
        abbreviateSynopsis = true,
        mixinStandardHelpOptions = true,
        versionProvider = EpirusVersionProvider.class,
        synopsisHeading = "%n",
        descriptionHeading = "%nDescription:%n%n",
        optionListHeading = "%nOptions:%n",
        footerHeading = "%n",
        footer = "Epirus CLI is licensed under the Apache License 2.0")
public class DockerRunCommand implements DockerOperations, Runnable {
    public void run() {
        try {
            executeDocker(
                    new String[] {
                        "docker",
                        "run",
                        "--env",
                        String.format("EPIRUS_LOGIN_TOKEN=%s", config.getLoginToken()),
                        "web3app"
                    });
        } catch (Exception e) {
            Console.exitError(e);
        }
    }
}