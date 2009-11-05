/*
 * Copyright 2007 The Kuali Foundation
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
package org.kuali.rice.kns.workflow;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.kuali.rice.core.exception.RiceRuntimeException;
import org.kuali.rice.kew.docsearch.DocSearchCriteriaDTO;
import org.kuali.rice.kew.docsearch.DocSearchUtils;
import org.kuali.rice.kew.docsearch.DocumentSearchResult;
import org.kuali.rice.kew.docsearch.DocumentSearchResultComponents;
import org.kuali.rice.kew.docsearch.SearchAttributeCriteriaComponent;
import org.kuali.rice.kew.docsearch.SearchableAttribute;
import org.kuali.rice.kew.docsearch.service.DocumentSearchService;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.exception.WorkflowException;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kim.service.KIMServiceLocator;
import org.kuali.rice.kns.UserSession;
import org.kuali.rice.kns.service.DocumentService;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.kns.test.document.AccountWithDDAttributesDocument;
import org.kuali.rice.kns.util.GlobalVariables;
import org.kuali.rice.kns.util.KualiDecimal;
import org.kuali.rice.kns.web.ui.Field;
import org.kuali.rice.kns.web.ui.Row;
import org.kuali.test.KNSTestCase;

/**
 * This class performs various DataDictionarySearchableAttribute-related tests on the doc search, including verification of proper wildcard functionality. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DataDictionarySearchableAttributeTest extends KNSTestCase {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        GlobalVariables.setUserSession(new UserSession("quickstart"));
    }
    
    enum DOCUMENT_FIXTURE {
    	NORMAL_DOCUMENT(new Integer(1234567890), "John Doe", new KualiDecimal(501.77), createDate(2009, Calendar.OCTOBER, 15), createTimestamp(2009, Calendar.NOVEMBER, 1, 0, 0, 0), "SecondState", true),
    	NEGATIVE_NUMBERS_DOCUMENT(new Integer(-42), "John Doe", new KualiDecimal(-100), createDate(2009, Calendar.OCTOBER, 15), createTimestamp(2009, Calendar.NOVEMBER, 1, 0, 0, 0), "SecondState", true),
    	FALSE_AWAKE_DOCUMENT(new Integer(1234567890), "John Doe", new KualiDecimal(501.77), createDate(2009, Calendar.OCTOBER, 15), createTimestamp(2009, Calendar.NOVEMBER, 1, 0, 0, 0), "SecondState", false),
    	ODD_NAME_DOCUMENT(new Integer(1234567890), " ", new KualiDecimal(501.77), createDate(2009, Calendar.OCTOBER, 15), createTimestamp(2009, Calendar.NOVEMBER, 1, 0, 0, 0), "SecondState", true),
    	ODD_TIMESTAMP_DOCUMENT(new Integer(1234567890), "John Doe", new KualiDecimal(501.77), createDate(2009, Calendar.OCTOBER, 15), createTimestamp(2009, Calendar.NOVEMBER, 1, 12, 4, 38), "ThirdState", true);
    	
    	private Integer accountNumber;
    	private String accountOwner;
    	private KualiDecimal accountBalance;
    	private Date accountOpenDate;
    	private Timestamp accountUpdateDateTime;
    	private String accountState;
    	private boolean accountAwake;
    	
    	private DOCUMENT_FIXTURE(Integer accountNumber, String accountOwner, KualiDecimal accountBalance, Date accountOpenDate, Timestamp accountUpdateDateTime, String accountState, boolean accountAwake) {
    		this.accountNumber = accountNumber;
    		this.accountOwner = accountOwner;
    		this.accountBalance = accountBalance;
    		this.accountOpenDate = accountOpenDate;
    		this.accountUpdateDateTime = accountUpdateDateTime;
    		this.accountState = accountState;
    		this.accountAwake = accountAwake;
    	}
    	
    	public AccountWithDDAttributesDocument getDocument(DocumentService docService) throws WorkflowException {
    		AccountWithDDAttributesDocument acctDoc = (AccountWithDDAttributesDocument) docService.getNewDocument("AccountWithDDAttributes");
    		acctDoc.setAccountNumber(this.accountNumber);
    		acctDoc.setAccountOwner(this.accountOwner);
    		acctDoc.setAccountBalance(this.accountBalance);
    		acctDoc.setAccountOpenDate(this.accountOpenDate);
    		acctDoc.setAccountUpdateDateTime(this.accountUpdateDateTime);
    		acctDoc.setAccountState(this.accountState);
    		acctDoc.setAccountAwake(this.accountAwake);
    		
    		return acctDoc;
    	}
    }
	
	/**
	 * Tests the use of multi-select and wildcard searches to ensure that they function correctly for DD searchable attributes on the doc search.
	 */ 
    @Ignore("Now works properly with a simple test doc, but requires the creation of the ACCT_DD_ATTR_DOC table beforehand")
    @Test
	public void testWildcardsAndMultiSelectsOnDDSearchableAttributes() throws Exception {
		DocumentService docService = KNSServiceLocator.getDocumentService();
		//docSearchService = KEWServiceLocator.getDocumentSearchService();
		DocumentType docType = KEWServiceLocator.getDocumentTypeService().findByName("AccountWithDDAttributes");
        String principalName = "quickstart";
        String principalId = KIMServiceLocator.getPersonService().getPersonByPrincipalName(principalName).getPrincipalId();
		
		AccountWithDDAttributesDocument acctDoc = DOCUMENT_FIXTURE.NORMAL_DOCUMENT.getDocument(docService);
		docService.routeDocument(acctDoc, "Routing Document #1", null);
		
		// Ensure that DD searchable attribute integer fields function correctly when searched on.
		assertDDSearchableAttributeWildcardsWork(docType, principalId, "accountNumber",
				new String[] {"1234567890", ">1234567889"},
				new int[]    {1           , 1});
		
		// Ensure that DD searchable attribute string fields function correctly when searched on.
		assertDDSearchableAttributeWildcardsWork(docType, principalId, "accountOwner",
				new String[] {"John Doe", "!John*", "???? Doe"},
				new int[]    {1         , 0       , 1});
		
		// Ensure that DD searchable attribute float fields function correctly when searched on.
		assertDDSearchableAttributeWildcardsWork(docType, principalId, "accountBalance",
				new String[] {"501.77", "<=499.99"},
				new int[]    {1       , 0});
		
		// Ensure that DD searchable attribute date fields function correctly when searched on.
		assertDDSearchableAttributeWildcardsWork(docType, principalId, "accountOpenDate",
				new String[] {"10/15/2009", "Unknown", ">=10/01/2009"},
				new int[]    {1           , -1       , 1});
		
		// Ensure that DD searchable attribute multi-select fields function correctly when searched on.
		// Validation is still broken at the moment (see KULRICE-3681), but this part at least tests the multi-select SQL generation.
		assertDDSearchableAttributeWildcardsWork(docType, principalId, "accountStateMultiselect",
				new String[][] {{"FirstState"}, {"SecondState"}, {"ThirdState","FourthState"}, {"SecondState","ThirdState"}},
				new int[]      {0             , 1              , 0                           , 1});
		
		// Ensure that DD searchable attribute boolean fields function correctly when searched on.
		assertDDSearchableAttributeWildcardsWork(docType, principalId, "accountAwake",
				new String[] {"Y", "N"},
				new int[]    {1  , 0});
		
		// Ensure that DD searchable attribute timestamp fields function correctly when searched on.
		assertDDSearchableAttributeWildcardsWork(docType, principalId, "accountUpdateDateTime",
				new String[] {"11/01/2009", "02/31/2008", "<01/01/2010"},
				new int[]    {1           , -1          , 1});
	}
    
    /**
     * Creates a date quickly
     * 
     * @param year the year of the date
     * @param month the month of the date
     * @param day the day of the date
     * @return a new java.sql.Date initialized to the precise date given
     */
    private static Date createDate(int year, int month, int day) {
    	Calendar date = Calendar.getInstance();
		date.set(year, month, day, 0, 0, 0);
		return new java.sql.Date(date.getTimeInMillis());
    }
    
    /**
     * Utility method to create a timestamp quickly
     * 
     * @param year the year of the timestamp
     * @param month the month of the timestamp
     * @param day the day of the timestamp
     * @param hour the hour of the timestamp
     * @param minute the minute of the timestamp
     * @param second the second of the timestamp
     * @return a new java.sql.Timestamp initialized to the precise time given
     */
    private static Timestamp createTimestamp(int year, int month, int day, int hour, int minute, int second) {
    	Calendar date = Calendar.getInstance();
    	date.set(year, month, day, hour, minute, second);
    	return new java.sql.Timestamp(date.getTimeInMillis());
    }
	
	/*
	 * A method similar to the one from DocumentSearchTestBase. The "value" parameter has to be either a String or a String[].
	 */
	private SearchAttributeCriteriaComponent createSearchAttributeCriteriaComponent(String key,Object value,Boolean isLowerBoundValue,DocumentType docType) {
		String formKey = (isLowerBoundValue == null) ? key : ((isLowerBoundValue != null && isLowerBoundValue.booleanValue()) ? SearchableAttribute.RANGE_LOWER_BOUND_PROPERTY_PREFIX + key : SearchableAttribute.RANGE_UPPER_BOUND_PROPERTY_PREFIX + key);
		String savedKey = key;
		SearchAttributeCriteriaComponent sacc = null;
		if (value instanceof String) {
			sacc = new SearchAttributeCriteriaComponent(formKey,(String)value,savedKey);
		} else {
			sacc = new SearchAttributeCriteriaComponent(formKey,null,savedKey);
			sacc.setValues(Arrays.asList((String[])value));
		}
		Field field = getFieldByFormKey(docType, formKey);
		if (field != null) {
			sacc.setSearchableAttributeValue(DocSearchUtils.getSearchableAttributeValueByDataTypeString(field.getFieldDataType()));
			sacc.setRangeSearch(field.isMemberOfRange());
			sacc.setCaseSensitive(!field.isUpperCase());
			sacc.setSearchInclusive(field.isInclusive());
			sacc.setSearchable(field.isIndexedForSearch());
			sacc.setCanHoldMultipleValues(Field.MULTI_VALUE_FIELD_TYPES.contains(field.getFieldType()));
		}
		return sacc;
	}
	
	/*
	 * A method that was copied from DocumentSearchTestBase.
	 */
	private Field getFieldByFormKey(DocumentType docType, String formKey) {
		if (docType == null) {
			return null;
		}
		for (SearchableAttribute searchableAttribute : docType.getSearchableAttributes()) {
			for (Row row : searchableAttribute.getSearchingRows(DocSearchUtils.getDocumentSearchContext("", docType.getName(), ""))) {
				for (org.kuali.rice.kns.web.ui.Field field : row.getFields()) {
					if (field instanceof Field) {
						if (field.getPropertyName().equals(formKey)) {
							return (Field)field;
						}
					} else {
						throw new RiceRuntimeException("Fields must be of type org.kuali.rice.kns.Field");
					}
				}
			}
		}
		return null;
	}
	
    /**
     * A convenience method for testing wildcards on data dictionary searchable attributes.
     *
     * @param docType The document type containing the attributes.
     * @param principalId The ID of the user performing the search.
     * @param fieldName The name of the field on the test document.
     * @param searchValues The search expressions to test. Has to be a String array (for regular fields) or a String[] array (for multi-select fields).
     * @param resultSizes The number of expected documents to be returned by the search; use -1 to indicate that an error should have occurred.
     * @throws Exception
     */
    private void assertDDSearchableAttributeWildcardsWork(DocumentType docType, String principalId, String fieldName, Object[] searchValues,
    		int[] resultSizes) throws Exception {
    	if (!(searchValues instanceof String[]) && !(searchValues instanceof String[][])) {
    		throw new IllegalArgumentException("'searchValues' parameter has to be either a String[] or a String[][]");
    	}
    	DocSearchCriteriaDTO criteria = null;
        DocumentSearchResultComponents result = null;
        List<DocumentSearchResult> searchResults = null;
        DocumentSearchService docSearchService = KEWServiceLocator.getDocumentSearchService();
        for (int i = 0; i < resultSizes.length; i++) {
        	criteria = new DocSearchCriteriaDTO();
        	criteria.setDocTypeFullName(docType.getName());
        	criteria.addSearchableAttribute(this.createSearchAttributeCriteriaComponent(fieldName, searchValues[i], null, docType));
        	try {
        		docSearchService.validateDocumentSearchCriteria(docSearchService.getStandardDocumentSearchGenerator(), criteria);
        		result = docSearchService.getList(principalId, criteria);
        		searchResults = result.getSearchResults();
        		if (resultSizes[i] < 0) {
        			fail(fieldName + "'s search at loop index " + i + " should have thrown an exception");
        		}
        		if(resultSizes[i] != searchResults.size()){
        			assertEquals(fieldName + "'s search results at loop index " + i + " returned the wrong number of documents.", resultSizes[i], searchResults.size());
        		}
        	} catch (Exception ex) {
        		if (resultSizes[i] >= 0) {
        			fail(fieldName + "'s search at loop index " + i + " should not have thrown an exception");
        		}
        	}
        	GlobalVariables.clear();
        }
    }
}
