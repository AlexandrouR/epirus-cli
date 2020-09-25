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
package io.epirus.console.openapi.subcommands

import io.epirus.console.EpirusVersionProvider
import io.epirus.console.openapi.OpenApiGeneratorService
import io.epirus.console.openapi.OpenApiGeneratorServiceConfiguration
import io.epirus.console.project.TemplateType
import io.epirus.console.token.erc777.ERC777GeneratorService
import org.apache.commons.lang.StringUtils
import picocli.CommandLine
import picocli.CommandLine.Command
import java.io.File
import java.util.concurrent.Callable

@Command(
    name = "new",
    description = ["Create a new Web3j-OpenAPI project."],
    abbreviateSynopsis = true,
    showDefaultValues = true,
    mixinStandardHelpOptions = true,
    versionProvider = EpirusVersionProvider::class,
    synopsisHeading = "%n",
    descriptionHeading = "%nDescription:%n%n",
    optionListHeading = "%nOptions:%n",
    footerHeading = "%n",
    footer = ["Epirus CLI is licensed under the Apache License 2.0"]
)
class NewOpenApiCommand : AbstractSubCommand(), Callable<Int> {

    @CommandLine.Parameters(defaultValue = "NONE")
    var templateType = TemplateType.NONE

    override fun generate(projectFolder: File) {
        when (templateType) {
            TemplateType.NONE, TemplateType.HELLOWORLD -> OpenApiGeneratorService(
                OpenApiGeneratorServiceConfiguration(
                    projectOptions.projectName,
                    packageName,
                    outputDirectory.absolutePath,
                    abis,
                    bins,
                    addressLength,
                    if (projectOptions.contextPath != null) StringUtils.removeEnd(projectOptions.contextPath, "/") else projectOptions.projectName))
                .generate()
            TemplateType.ERC777 -> ERC777GeneratorService(projectOptions.projectName, packageName, outputDirectory.absolutePath).generate()
        }
//        OpenApiGeneratorService(OpenApiGeneratorServiceConfiguration(projectName = projectOptions.projectName,
//            packageName = packageName,
//            outputDir = projectFolder.path,
//            abis = abis,
//            bins = bins,
//            addressLength = addressLength,
//            contextPath = projectOptions.contextPath?.removeSuffix("/") ?: projectOptions.projectName
//        )).generate()
    }
}
