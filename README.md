# Api_BrokenBrain-main

# ReabNEXT
## O próximo passo da reabilitação.

#### Descrição do App:
Aplicativo de geração automatizada de treinos para pessoas com deficiência em reabilitação utilizando a API da Open AI no modelo Chat GPT-3.
Projeto sendo desenvolvido em grupo na faculdade de como challenge ADS da FIAP seguindo metodologia ágil Scrum. Tendo entregas em 4 sprints e acompanhamento ao longo do ano pela empresa Plusoft.

#### Descrição da API
Essa API faz a integração com o Chat GPT através da API pública da OpenAI.
O cadastro do paciente é feito com os seguintes parâmetros:
- Idade
- Altura
- Peso
- Descrição da deficiência
E a partir desses dados é gerado a rotina de treino com o Chat GPT.
#### Desenho da pipeline
![image](https://github.com/MattheusDeus/Api_BrokenBrain-main/assets/101228527/19b0ca1a-350c-4b26-9299-baa449ec039d)

### Código da pipeline

# Maven package Java project Web App to Linux on Azure
# Build your Java project and deploy it to Azure as a Linux web app
# Add steps that analyze code, save build artifacts, deploy, and more:
# https://docs.microsoft.com/azure/devops/pipelines/languages/java

trigger:
- main

variables:

  # Azure Resource Manager connection created during pipeline creation
  azureSubscription: '24235f9e-9595-4de7-973f-3a86f57508e6'

  # Web app name
  webAppName: 'webap-rm95021'

  # Environment name
  environmentName: 'webap-rm95021'

  # Agent VM image name
  vmImageName: 'ubuntu-latest'

stages:
- stage: Build
  displayName: Build stage
  jobs:
  - job: MavenPackageAndPublishArtifacts
    displayName: Maven Package and Publish Artifacts
    pool:
      vmImage: $(vmImageName)

    steps:
    - task: Maven@3
      displayName: 'Maven Package'
      inputs:
        mavenOptions: '-Xmx3072m'
        javaHomeOption: 'JDKVersion'
        jdkVersionOption: '1.17'
        jdkArchitectureOption: 'x64'
        publishJUnitResults: true
        testResultsFiles: '**/surefire-reports/TEST-*.xml'
        goals: 'package'
        mavenPomFile: 'pom.xml'

    - task: CopyFiles@2
      displayName: 'Copy Files to artifact staging directory'
      inputs:
        SourceFolder: '$(System.DefaultWorkingDirectory)'
        Contents: '**/target/*.?(war|jar)'
        TargetFolder: $(Build.ArtifactStagingDirectory)

    - upload: $(Build.ArtifactStagingDirectory)
      artifact: drop

- stage: Deploy
  displayName: Deploy stage
  dependsOn: Build
  condition: succeeded()
  jobs:
  - deployment: DeployLinuxWebApp
    displayName: Deploy Linux Web App
    environment: $(environmentName)
    pool:
      vmImage: $(vmImageName)
    strategy:
      runOnce:
        deploy:
          steps:
          - task: AzureWebApp@1
            displayName: 'Azure Web App Deploy: webap-rm95021'
            inputs:
              azureSubscription: $(azureSubscription)
              appType: webAppLinux
              appName: $(webAppName)
              package: '$(Pipeline.Workspace)/drop/**/target/*.?(war|jar)'

    ### Para execução 

Clone o repositorio na Azure DevOps
Crie sua pipeline fazendo as devidas configuraçoes e setando seu grupo de recurso e seu webap
