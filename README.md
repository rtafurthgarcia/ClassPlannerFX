# ClassPlannerFX
Too to manage and plan classes on a yearly basis 

# Project setup
To be able to launch tests, one has to first add the following to their `.vscode/settings.json`:
```{
    "java.configuration.updateBuildConfiguration": "interactive",
    "java.debug.settings.onBuildFailureProceed": true,
    "java.test.config": 
    [
        {
            "name": "testConfig",
            "vmargs": [ 
                "--add-exports=javafx.graphics/com.sun.javafx.application=ALL-UNNAMED"
            ]
        }
    ]
}
```
due to the fact TestFX needs those exports. 
