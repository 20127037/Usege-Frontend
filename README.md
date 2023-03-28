# Usege Frontend

## Setup amplify

[Installation - AWS Amplify Docs (read for more information)](https://docs.amplify.aws/cli/start/install/)
Install amlify cli
`npm install -g @aws-amplify/cli`

Configure amplify
`amplify configure --profile amplify`

Insert following information from the **frontend** credentials
```
region: ap-southeast-1
accessKeyId: <provided>
secrectAccessKey: <provided>
```

## Pull amplify image to your repository

Go to the Android project file
`amplify pull --appId <appId> --envName dev`