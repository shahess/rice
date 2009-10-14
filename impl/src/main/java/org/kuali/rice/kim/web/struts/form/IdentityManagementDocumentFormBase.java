/*
 * Copyright 2007-2009 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.kim.web.struts.form;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.kim.util.KimConstants;
import org.kuali.rice.kns.bo.Parameter;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.kns.util.KNSConstants;
import org.kuali.rice.kns.web.struts.form.KualiTableRenderFormMetadata;
import org.kuali.rice.kns.web.struts.form.KualiTransactionalDocumentFormBase;

/**
 * This is a description of what this class does - kellerj don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@SuppressWarnings("serial")
public abstract class IdentityManagementDocumentFormBase extends KualiTransactionalDocumentFormBase {
	protected static final Logger LOG = Logger.getLogger(IdentityManagementDocumentFormBase.class);
    protected static final String MAX_MEMBERS_PER_PAGE_PARM = "MAX_MEMBERS_PER_PAGE";
	protected transient KualiTableRenderFormMetadata memberTableMetadata;
    protected int recordsPerPage = -1;
    protected boolean inquiry = false;
    
	protected static final String CHANGE_NAMESPACE_METHOD_TO_CALL = "methodToCall.changeNamespace";
	protected static final String CHANGE_MEMBER_TYPE_CODE_METHOD_TO_CALL = "methodToCall.changeMemberTypeCode";
	protected static final String CHANGE_DEL_ROLE_MEMBER_METHOD_TO_CALL = "methodToCall.changeDelegationRoleMember";

    /**
     * @see org.kuali.rice.kns.web.struts.form.KualiDocumentFormBase#populate(javax.servlet.http.HttpServletRequest)
     */
    @SuppressWarnings("unchecked")
	@Override
    public void populate(HttpServletRequest request) {
        super.populate(request);

        memberTableMetadata = new KualiTableRenderFormMetadata();

        if (KNSConstants.TableRenderConstants.SWITCH_TO_PAGE_METHOD.equals(getMethodToCall())) {
            // look for the page number to switch to
            memberTableMetadata.setSwitchToPageNumber(-1);

            // the param we're looking for looks like: methodToCall.switchToPage.1.x , where 1 is the page nbr
            String paramPrefix = KNSConstants.DISPATCH_REQUEST_PARAMETER + "." + KNSConstants.TableRenderConstants.SWITCH_TO_PAGE_METHOD + ".";
            for (Enumeration<String> i = request.getParameterNames(); i.hasMoreElements();) {
                String parameterName = i.nextElement();
                if (parameterName.startsWith(paramPrefix) && parameterName.endsWith(".x")) {
                    String switchToPageNumberStr = StringUtils.substringBetween(parameterName, paramPrefix, ".");
                    memberTableMetadata.setSwitchToPageNumber(Integer.parseInt(switchToPageNumberStr));
                }
            }
            if (memberTableMetadata.getSwitchToPageNumber() == -1) {
                throw new RuntimeException("Couldn't find page number");
            }
        }

//        if (KNSConstants.TableRenderConstants.SORT_METHOD.equals(getMethodToCall())) {
//            memberTableMetadata.setColumnToSortIndex(-1);
//
//            // the param we're looking for looks like: methodToCall.sort.1.x , where 1 is the column to sort on
//            String paramPrefix = KNSConstants.DISPATCH_REQUEST_PARAMETER + "." + KNSConstants.TableRenderConstants.SORT_METHOD + ".";
//            for (Enumeration<String> i = request.getParameterNames(); i.hasMoreElements();) {
//                String parameterName = i.nextElement();
//                if (parameterName.startsWith(paramPrefix) && parameterName.endsWith(".x")) {
//                    String columnToSortStr = StringUtils.substringBetween(parameterName, paramPrefix, ".");
//                    memberTableMetadata.setColumnToSortIndex(Integer.parseInt(columnToSortStr));
//                }
//            }
//            if (memberTableMetadata.getColumnToSortIndex() == -1) {
//                throw new RuntimeException("Couldn't find column to sort");
//            }
//        }

    }

	public KualiTableRenderFormMetadata getMemberTableMetadata() {
		return this.memberTableMetadata;
	}

	public void setMemberTableMetadata(
			KualiTableRenderFormMetadata memberTableMetadata) {
		this.memberTableMetadata = memberTableMetadata;
	}

	public int getRecordsPerPage() {
		if ( recordsPerPage == -1 ) {
			Parameter param = KNSServiceLocator.getParameterService().retrieveParameter(KimConstants.NAMESPACE_CODE, KNSConstants.DetailTypes.DOCUMENT_DETAIL_TYPE, MAX_MEMBERS_PER_PAGE_PARM);
			if ( param != null ) {
				try {
					recordsPerPage = Integer.parseInt( param.getParameterValue() );
				} catch ( NumberFormatException ex ) {
					LOG.error( "Unable to parse parameter " + KimConstants.NAMESPACE_CODE+"/"+KNSConstants.DetailTypes.DOCUMENT_DETAIL_TYPE+"/"+MAX_MEMBERS_PER_PAGE_PARM + "(+"+param.getParameterValue()+") as an int - defaulting to 1." );
					recordsPerPage = 1;
				}
			} else {
				LOG.error( "Unable to find " + KimConstants.NAMESPACE_CODE+"/"+KNSConstants.DetailTypes.DOCUMENT_DETAIL_TYPE+"/"+MAX_MEMBERS_PER_PAGE_PARM + " - defaulting to 1." );
				recordsPerPage = 1;
			}
		}
		return recordsPerPage;
	}

	// support for the inquiryControls tag since using same form
	public boolean isCanExport() {
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public List getMemberRows() {
		return new ArrayList();
	}

	/**
	 * @return the inquiry
	 */
	public boolean isInquiry() {
		return this.inquiry;
	}

	/**
	 * @param inquiry the inquiry to set
	 */
	public void setInquiry(boolean inquiry) {
		this.inquiry = inquiry;
	}
}
