/*
 * Copyright 2005-2007 The Kuali Foundation.
 * 
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.iu.uis.eden.actions;

import org.junit.Test;
import org.kuali.workflow.test.KEWTestCase;

import edu.iu.uis.eden.actions.BlanketApproveTest.NotifySetup;
import edu.iu.uis.eden.clientapp.WorkflowDocument;
import edu.iu.uis.eden.clientapp.vo.NetworkIdVO;

/**
 * Test SuperUserCancel through WorkflowDocument
 */
public class SuperUserCancelTest extends KEWTestCase {

    protected void loadTestData() throws Exception {
	loadXmlFile("ActionsConfig.xml");
    }

    @Test
    public void testSuperUserCancel() throws Exception {
	WorkflowDocument document = new WorkflowDocument(new NetworkIdVO("ewestfal"), NotifySetup.DOCUMENT_TYPE_NAME);
	document.routeDocument("");

	document = new WorkflowDocument(new NetworkIdVO("jhopf"), document.getRouteHeaderId());
	assertTrue("WorkflowDocument should indicate jhopf as SuperUser", document.isSuperUser());
	document.superUserCancel("");
	assertTrue("Document should be final after Super User Cancel", document.stateIsCanceled());
    }

    @Test
    public void testSuperUserInitiatorCancel() throws Exception {
	WorkflowDocument document = new WorkflowDocument(new NetworkIdVO("ewestfal"), NotifySetup.DOCUMENT_TYPE_NAME);
	assertTrue("WorkflowDocument should indicate ewestfal as SuperUser", document.isSuperUser());
	document.superUserCancel("");
	assertTrue("Document should be final after Super User Cancel", document.stateIsCanceled());
    }

    @Test
    public void testSuperUserNonInitiatorCancel() throws Exception {
	WorkflowDocument document = new WorkflowDocument(new NetworkIdVO("delyea"), NotifySetup.DOCUMENT_TYPE_NAME);
	document = new WorkflowDocument(new NetworkIdVO("ewestfal"), document.getRouteHeaderId());
	assertTrue("WorkflowDocument should indicate ewestfal as SuperUser", document.isSuperUser());
	document.superUserCancel("");
	assertTrue("Document should be final after Super User Cancel", document.stateIsCanceled());
    }

    @Test
    public void testSuperUserCancelInvalidUser() throws Exception {
	WorkflowDocument document = new WorkflowDocument(new NetworkIdVO("ewestfal"), NotifySetup.DOCUMENT_TYPE_NAME);
	document.routeDocument("");

	document = new WorkflowDocument(new NetworkIdVO("quickstart"), document.getRouteHeaderId());
	try {
	    assertFalse("WorkflowDocument should not indicate quickstart as SuperUser", document.isSuperUser());
	    document.superUserCancel("");
	    fail("invalid user attempted to SuperUserApprove");
	} catch (Exception e) {
	}

    }

}
