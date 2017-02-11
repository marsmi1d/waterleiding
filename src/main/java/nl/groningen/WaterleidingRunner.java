package nl.groningen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

import org.hyperledger.fabric.sdk.Chain;
import org.hyperledger.fabric.sdk.ChainCodeResponse;
import org.hyperledger.fabric.sdk.ChaincodeLanguage;
import org.hyperledger.fabric.sdk.FileKeyValStore;
import org.hyperledger.fabric.sdk.InvokeRequest;
import org.hyperledger.fabric.sdk.Member;
import org.hyperledger.fabric.sdk.QueryRequest;
import org.hyperledger.fabric.sdk.RegistrationRequest;
import org.hyperledger.fabric.sdk.exception.EnrollmentException;
import org.hyperledger.fabric.sdk.exception.RegistrationException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component
public class WaterleidingRunner implements CommandLineRunner {

	private static Logger log = Logger.getLogger(WaterleidingRunner.class.getName());
	@Override
	public void run(String... args) throws Exception {
		
		log.info("starting client");
		Chain testChain = new Chain("chain1");

		//Add the membership service:
		testChain.setMemberServicesUrl("grpc://localhost:7054", null);

		//Set a keyValueStore:
		testChain.setKeyValStore(new FileKeyValStore("/srv/martijn/test.properties"));

		//Add a peer to the chain:
		testChain.addPeer("grpc://localhost:7051", null);

		//Get a member:

		Member registrar = testChain.getMember("admin");
		if (!registrar.isEnrolled()) {
			registrar = testChain.enroll("admin", "Xurw3yU9zI0l");
		}
		testChain.setRegistrar(registrar);
		
		//Enroll a member:
/*
        test_user1: 1 jGlNl6ImkuDo institution_a
        test_user2: 1 zMflqOKezFiA bank_c
        test_user3: 1 vWdLCE00vJy0 bank_a
        test_user4: 1 4nXSrfoYGFCP institution_a
        test_user5: 1 yg5DVhm0er1z bank_b
        test_user6: 1 b7pmSxzKNFiw bank_a
        test_user7: 1 YsWZD4qQmYxo institution_a
        test_user8: 1 W8G0usrU7jRk bank_a
        test_user9: 1 H80SiB5ODKKQ institution_a
        */
		
		String chaincodeId = "cc93372e3978fae26f4bb251d1a4f11e7054b9acb7e252c0f411bbb2008c128d";
	
		ChainCodeResponse deployResponse = null;
		QueryRequest request = new QueryRequest();
		request.setArgs(new ArrayList<>(Arrays.asList("getTransactionsByUser", "john")));
		request.setChaincodeID(chaincodeId);
		request.setChaincodeName(chaincodeId);
		request.setConfidential(false);
		//Member member = getMember(testChain, "john", "bank_a");
		//Member member = testChain.getMember("john");
		
		ChainCodeResponse response = registrar.query(request);
		
		//Member member = getMember(testChain, "john", "bank_a");
		log.info("Response from query: " + response.getMessage());
		log.info("Add thing");
		
		GkbTransaction gkbTransaction = new GkbTransaction();
		gkbTransaction.setAfnemerID("gemeente");
		gkbTransaction.setBedrag(400);
		gkbTransaction.setSomeProperty("asdasd");
		gkbTransaction.setThingID("5");
		gkbTransaction.setUserID("john");
		
		Gson gson = new Gson();
		String json = gson.toJson(gkbTransaction);
		
	    log.info("Transaction: " + json); 
		InvokeRequest invokeRequest = new InvokeRequest();
		invokeRequest.setArgs(new ArrayList<>(Arrays.asList("createThing", json)));
		invokeRequest.setChaincodeID(chaincodeId);
		invokeRequest.setChaincodeName(chaincodeId);
		
		//Member member1 = getMember("User1", "bank_a");
		ChainCodeResponse response1 = registrar.invoke(invokeRequest);
		
		/*
		InvokeRequest r = new InvokeRequest();
		r.setArgs(new ArrayList<>(Arrays.asList("invoke", "a", "b", "200")));
		r.setChaincodeID(chaincodeId);
		r.setChaincodeName(chaincodeId);
		r.setChaincodeLanguage(ChaincodeLanguage.JAVA);
		ChainCodeResponse response1 = registrar.invoke(r);
		*/

		//log.info("" + response1.getMessage());
		log.info("end test");
		
	}
	
	private static Member getMember(Chain testChain, String enrollmentId, String affiliation) throws RegistrationException, EnrollmentException {
		Member member = testChain.getMember(enrollmentId);
		if (!member.isRegistered()) {
			RegistrationRequest registrationRequest = new RegistrationRequest();
			registrationRequest.setEnrollmentID(enrollmentId);
			registrationRequest.setAffiliation(affiliation);
			//registrationRequest.setAccount(); TODO setAccount missing from registrationRequest?
			member = testChain.registerAndEnroll(registrationRequest);
		} else if (!member.isEnrolled()) {
			member = testChain.enroll(enrollmentId, member.getEnrollmentSecret());
		}
		return member;
	}

}
