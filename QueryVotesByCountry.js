async function main(params) {
    const { CloudantV1 } = require('@ibm-cloud/cloudant');
    const { IamAuthenticator } = require('ibm-cloud-sdk-core');

	const authenticator = new IamAuthenticator({
		apikey: params.iamApiKey
    });
    
    const service = new CloudantV1({
        authenticator: authenticator
    });
    
    service.setServiceUrl(params.url);
    
    const selector = CloudantV1.JsonObject =  {
      vote: {
         '$eq': params.country
      }
   	};
   	
	const vote = service.postFind( {
		db: params.dbname,
		selector: selector,
		fields: ['vote']
    });
    
    var json = await vote.then(results => results.result);
    return CloudantV1.JsonObject = {body: json.docs.length }
}
