# Android Project Template

## What is it?

This is a template project, its purpose is to allow a developer to rapidly spin up a new project and start developing instead of dealing with adding dependancies and other mundane tasks that are not deving. 

The project consists of 2 modules:
- app: your standard app module that you get when making a new project. Nothing really in it except for the basics provided by android studio
- buildSrc: this contains config files that are used in gradle.

The aim of this template is to accelerate the development process of a new project, without constricting the developer. Hence this is quite bear bone and we should treat it as a building base, which can be built up as needed instead of a project that comes ready to develop in out of the box. 

## How to use?

First, you want to find `setup.gradle` file. First thing you will see in it is a `renameConfig`. This is how it looks:
```
def renameConfig = [
        templateName             : "template", //** DO NOT TOUCH **
        templateAppId            : "template.app.id", //** DO NOT TOUCH **
        templateMaterialThemeName: "TemplateTheme", //** DO NOT TOUCH **
        newPackage               : "domain.yourname.app",
        newProjectName           : "Your Project",
        newMaterialThemeName     : "MyMaterialTheme",

        //Everything below here is subject to change. The above, however, will remain the same

        useAppyxDependencies     : true,

        useHiltDependencies      : false,
        useKoinDependencies      : true,

        useRoomDependencies      : true,

        useRetrofitDependencies  : true,
]
```
**Very important:** Do not touch the lines of code were it says do not touch.
Those lines are the current names that will be replaced `newPackage`, `newProjectName` and `newMaterialThemeName`. 

Everything (and including) this line: `useAppyxDependencies     : true,` defines what dependancies will be in the project. Here you will want to set true for dependancies you want to use and false otherwise. **Be careful**, for example setting true for both koin and hilt will leave the project with both of these dependacies. Wont neceserily brake the project, but that would defeat the point of this template.

So to summarise, the steps are:
1. Open setup.gradle file
2. Change the values of `newPackage`, `newProjectName` and `newMaterialThemeName` to your desired ones
3. Set boolean values for the dependacies you would like to use in the project
4. Run the `renameTemplate` gradle task
5. Commit and push

## How does it work?

The `renameTemplate` gradle task runs the following gradle tasks:
- `keepOrRemoveDependencies`
- `renameAppPackage`
- `replaceTemplateReferences`
- `deleteSetupCode`

`keepOrRemoveDependencies`: Removes dependacies from the project. In the initial state, the template project contains all of the dependacies listed in the renameConfig. Once dependacies are chosen, this gradle task removes the ones that are set to false.

`renameAppPackage`: Renames the template package in the app module. Replaces the imports, package names and material theme references. As there are no references in the buildSrc module to the imports, package names and material theme references, there is no need to do any replacing there.

`replaceTemplateReferences`: Replaces references to template in various files. The task replaces:
    - `templateName` ("template") in `AndroidManifest.xml`, `AppConfig.kt`, `settings.gradle.kts` and in app's `strings.xml`
    - `templateAppId` ("template.app.id") in `AppConfig.kt`

`deleteSetupCode`: Deletes the `setup.gradle` file

## Other useful features

- The project has `dependencyUpdates` gradle task, which will check for the latest stable version of the libraries in the `libs.versions.toml` file, and update them. Be careful here, some of the updates might brake the project. For example, appyx 2.x.x is set up differently than 1.x.x, this means that after this tasks runs, you will need to manually fix it.

- The project comes with githooks and detekt
