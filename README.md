# engine
This is for workflow engine

The workflow_data.json will work as the configuration of the workflows. 
On start of the application the it will read the json, parse it, validate it and create the workflow. 
Each workflow will have an unique id. In order to execute the workflow, external application can either do a rest call 
with the flow id, or the whole project could be imported as jar and the service could call the workflow service in the jar.

There is provision of checking each step status.
