/*
 * Copyright 2005-2006 The Kuali Foundation.
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

import edu.iu.uis.eden.EdenConstants;
import edu.iu.uis.eden.KEWServiceLocator;
import edu.iu.uis.eden.exception.WorkflowException;
import edu.iu.uis.eden.routeheader.DocumentRouteHeaderValue;
import edu.iu.uis.eden.user.WorkflowUser;

/**
 * performs a cancel action as a super user
 *
 * @author Kuali Rice Team (kuali-rice@googlegroups.com)
 */
public class SuperUserCancelEvent extends SuperUserActionTakenEvent {
    
    public SuperUserCancelEvent(DocumentRouteHeaderValue routeHeader, WorkflowUser user) {
        super(EdenConstants.ACTION_TAKEN_SU_CANCELED_CD, routeHeader, user);
        this.superUserAction = EdenConstants.SUPER_USER_CANCEL;
    }

    public SuperUserCancelEvent(DocumentRouteHeaderValue routeHeader, WorkflowUser user, String annotation, boolean runPostProcessor) {
        super(EdenConstants.ACTION_TAKEN_SU_CANCELED_CD, routeHeader, user, annotation, runPostProcessor);
        this.superUserAction = EdenConstants.SUPER_USER_CANCEL;
    }

    protected void markDocument() throws WorkflowException {
        //this.event = new DocumentRouteStatusChange(this.routeHeaderId, this.getRouteHeader().getAppDocId(), this.getRouteHeader().getDocRouteStatus(), EdenConstants.ROUTE_HEADER_CANCEL_CD);
        getRouteHeader().markDocumentCanceled();
        KEWServiceLocator.getRouteHeaderService().saveRouteHeader(getRouteHeader());
    }
}