package org.sagebionetworks.warehouse.workers.utils;

import static org.junit.Assert.*;

import org.junit.Test;
import org.sagebionetworks.repo.model.audit.AccessRecord;
import org.sagebionetworks.warehouse.workers.utils.AccessRecordUtils;
import org.sagebionetworks.warehouse.workers.utils.Client;
import org.sagebionetworks.warehouse.workers.utils.ProcessedAccessRecord;

public class AccessRecordUtilsTest {

	/*
	 * Client Tests
	 */
	@Test
	public void rClientTest() {
		assertEquals(Client.R, AccessRecordUtils.getClient("synapseRClient/1.3-0"));
	}

	@Test
	public void pythonClientTest() {
		assertEquals(Client.PYTHON, AccessRecordUtils.getClient("synapseclient/1.0.1 python-requests/2.1.0 CPython/2.7.3 Linux/3.2.0-54-virtual"));
	}

	@Test
	public void webClientTest() {
		assertEquals(Client.WEB, AccessRecordUtils.getClient("Synpase-Java-Client/64.0  Synapse-Web-Client/67.0"));
	}

	@Test
	public void javaClientTest() {
		assertEquals(Client.JAVA, AccessRecordUtils.getClient("Synpase-Java-Client/64.0"));
	}

	@Test
	public void commandLineClientTest() {
		assertEquals(Client.COMMAND_LINE,AccessRecordUtils. getClient("synapsecommandlineclient"));
	}

	@Test
	public void elbHealthCheckerClientTest() {
		assertEquals(Client.ELB_HEALTHCHECKER, AccessRecordUtils.getClient("ELB-HealthChecker/1.0"));
	}

	@Test
	public void unknownClientTest() {
		assertEquals(Client.UNKNOWN, AccessRecordUtils.getClient(""));
	}

	/*
	 * Entity ID Tests
	 */
	@Test
	public void entityIdWithPrefixTest() {
		assertEquals("4623841", AccessRecordUtils.getEntityId("/repo/v1/entity/syn4623841/bundle"));
	}

	@Test
	public void entityIdWithoutPrefixTest() {
		assertEquals("4623841", AccessRecordUtils.getEntityId("/repo/v1/entity/4623841/bundle"));
	}

	@Test
	public void endingEntityIdTest() {
		assertEquals("4623841", AccessRecordUtils.getEntityId("/repo/v1/entity/syn4623841"));
	}

	@Test
	public void nullEntityIdTest() {
		assertNull(AccessRecordUtils.getEntityId("/repo/v1/version"));
	}

	/*
	 * Synapse API Tests
	 */
	@Test
	public void urlWithoutParameters() {
		assertEquals("POST /certifiedUserTestResponse", AccessRecordUtils.getSynapseAPI("/repo/v1/certifiedUserTestResponse", "POST"));
	}

	@Test
	public void urlWithSynId() {
		assertEquals("GET /entity/#/bundle", AccessRecordUtils.getSynapseAPI("/repo/v1/entity/syn1571204/bundle", "GET"));
	}

	@Test
	public void urlWithEvaluationSubmissionId() {
		assertEquals("GET /evaluation/submission/#/status", AccessRecordUtils.getSynapseAPI("/repo/v1/evaluation/submission/2813223/status", "GET"));
	}

	@Test
	public void urlWithEntityIdAndVersionNumber() {
		assertEquals("GET /entity/#/version/#/filepreview", AccessRecordUtils.getSynapseAPI("/repo/v1/entity/syn2785825/version/1/filepreview", "GET"));
	}

	@Test
	public void urlWithWiki2AndWikiVersionNumber() {
		assertEquals("PUT /evaluation/#/wiki2/#/#", AccessRecordUtils.getSynapseAPI("/repo/v1/evaluation/2785825/wiki2/2813234/2", "PUT"));
	}

	@Test
	public void urlWith4IdFields() {
		assertEquals("GET /entity/#/table/column/#/row/#/version/#/filepreview", AccessRecordUtils.getSynapseAPI("/repo/v1/entity/syn3456789/table/column/1/row/12/version/2/filepreview", "GET"));
	}

	/*
	 * categorize() Test
	 */
	@Test
	public void categorizeTest() {
		AccessRecord ar = new AccessRecord();
		ar.setUserAgent("Synpase-Java-Client/64.0  Synapse-Web-Client/67.0");
		ar.setMethod("GET");
		ar.setRequestURL("/repo/v1/entity/syn2600225/descendants");
		ar.setSessionId("28a75682-f056-40f7-9a1e-416cb703bed5");

		ProcessedAccessRecord expected = new ProcessedAccessRecord();
		expected.setSessionId("28a75682-f056-40f7-9a1e-416cb703bed5");
		expected.setEntityId("2600225");
		expected.setClient(Client.WEB);
		expected.setSynapseApi("GET /entity/#/descendants");

		assertEquals(expected, AccessRecordUtils.categorize(ar));
	}
}