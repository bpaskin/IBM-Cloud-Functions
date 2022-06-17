### Example of running Serverless with IBM Cloud Functions (OpenWhisk) 


This is a small example of using [IBM Cloud Functions](https://cloud.ibm.com/functions/) to create a sample flow with different Actions with some Sequences that utilise IBM MQ, Cloudant DB and is writen in Java, PHP, and JavaScript 

![Diagram of flow](https://github.com/bpaskin/IBM-Cloud-Functions/images/serverless.drawio.png)

---

#### Prerequistes:
- An [IBM Cloud Account](https://cloud.ibm.com)
- The [IBM Cloud CLI](https://cloud.ibm.com/docs/cli?topic=cli-getting-started)
- A separate instance of [IBM MQ](https://www.ibm.com/products/mq) to allow for triggering and is available on the internet for requests.  A [containerized IBM MQ](https://hub.docker.com/r/ibmcom/mq/) can be used, as well.

---
#### Setup Cloudant
All the steps can be done through the console.  Some, but not all, can be done using the CLI

Create an [instance](https://cloud.ibm.com/docs/Cloudant?topic=Cloudant-creating-an-ibm-cloudant-instance-on-ibm-cloud-by-using-the-ibm-cloud-cli
):
- login to the IBM Cloud and select which account to use (`ibmcloud login`)
- Target the resource (`ibmcloud target -g default`)
- Create an instance called `cloudant-serverless` (`ibmcloud resource service-instance-create cloudant-serverless cloudantnosqldb lite us-south -p '{"legacyCredentials": false}'
- Generate the credentials (`ibmcloud resource service-key-create serverless-creds Manager --instance-name cloudant-serverless`)
- Show the credentials (`ibmcloud resource service-key serverless-creds`)

The next steps need to be done in the Console UI.
- Select the `cloudant-serverless` instance of the DB
- Click on `Launch Dashboard` blue button in the upper right.  This will open the Cloudant instance in a new panel.
- Click on `Create Database` in the upper right, which will then review a panel.
- Enter the name of the database as `eurovision` and click `Create`

---
#### Update the configuration file
The configuration file can be found in `src/main/java/configuration.json` and must be updated for proper functioning of the Actions.  Only the values in the `< >` symbols need to be updated.

```
{
  "qmgrHostName": "<qmghostname>",
  "qmgrPort": <port>,
  "qmgrName": "SERVERLESS",
  "qmgrChannelName": "SERVERLESS.SVRCONN",
  "username": "username",
  "password": "password",
  "queueName": "EUROVISION",
  "dbname": "eurovision",
  "iamApiKey": "<iam key from Cloudant>",
  "url":"<URL for Cloudant>",
  "serviceName":"cloudant-serverless"
}
```

---
#### Create the Cloud Functions Actions
```
ibmcloud fn package create Eurovision
ibmcloud fn package bind /whisk.system/cloudant Eurovision-Cloudant --param dbname eurovision
ibmcloud fn service bind cloudantnosqldb  Eurovision-Cloudant --instance cloudant-serverless  --keyname 'serverless-creds'
ibmcloud fn action create Eurovision/voteui vote.php --web true
ibmcloud fn action create Eurovision/vote target/Eurovision-1.0-jar-with-dependencies.jar --main com.ibm.example.serverless.AcceptVote
ibmcloud fn action create Eurovision/putQueue target/Eurovision-1.0-jar-with-dependencies.jar --main com.ibm.example.serverless.SendToQueue -P src/main/java/configuration.json
ibmcloud fn action create Eurovision/voting --sequence Eurovision/vote,Eurovision/putQueue --web true
ibmcloud fn action create Eurovision/getQueue target/Eurovision-1.0-jar-with-dependencies.jar --main com.ibm.example.serverless.RetrieveFromQueue -P src/main/java/configuration.json
ibmcloud fn action create Eurovision/updatingDB --sequence Eurovision/getQueue,Eurovision-Cloudant/create-document --web true
ibmcloud fn action create Eurovision/resultsQuery target/Eurovision-1.0-jar-with-dependencies.jar --main com.ibm.example.serverless.QueryVotesByCountry -P src/main/java/configuration.json --web true --memory 512
ibmcloud fn action create Eurovision/resultsui results.php --web true
```

Endpoints:
- Web page for voting: `ibmcloud fn action get Eurovision/voteui --url`
- Web page for results: `ibmcloud fn action get Eurovision/resultsui --url`
- Trigger endpoint for MQ: `ibmcloud fn action get Eurovision/updatingDB --url`

---
#### IBM MQ Setup
This will setup a new QMGR that will accept messages for voting and then trigger and call the `Eurovision/updatingDB` endpoint to process messages

The `EUROVISION.TRIGGER` in the `serverless.mqsc` file will need to be updated with the proper URL from the `Eurovision/updatingDB` Action.

- Create the QMGR: `/usr/mqm/bin/crtmqm SERVERLESS`
- Start the QMGR: `/usr/mqm/bin/strmqm SERVERLESS`
- Add the Objects to the QMGR: `/usr/mqm/bin/runmqsc SERVERLESS < serverless.mqsc`
- Shutdown the QMGR: `/usr/mqm/bin/endmqm -i SERVERLESS`
- Restart the QMGR: `/usr/mqm/bin/strmqm SERVERLESS`

---
#### Test and Enjoy

-+Funeral Winter+-
