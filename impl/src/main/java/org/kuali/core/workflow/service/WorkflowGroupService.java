/*
 * Copyright 2005-2006 The Kuali Foundation.
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
package org.kuali.core.workflow.service;

import java.util.Collection;

import org.kuali.rice.kew.dto.UserIdDTO;
import org.kuali.rice.kew.dto.WorkgroupDTO;
import org.kuali.rice.kew.exception.KEWUserNotFoundException;
import org.kuali.rice.kew.exception.InvalidWorkgroupException;
import org.kuali.rice.kew.exception.ResourceUnavailableException;
import org.kuali.rice.kew.exception.WorkflowException;



/**
 * This interface defines methods that a Kuali Workflow Group Service must provide.
 * 
 * 
 */
public interface WorkflowGroupService {

    /**
     * method to get a Workflow Group based on groupName
     * 
     * @param groupName
     * @return Workgroup if a group by the name passed in exists
     * @throws InvalidWorkgroupException
     */
    public WorkgroupDTO getWorkgroupByGroupName(String groupName) throws WorkflowException;

    /**
     * method to get a list of the users Workgroups
     * 
     * @param kualiUser
     * @return a list of the groups that the user is a member of
     * @throws ResourceUnavailableException
     * @throws KEWUserNotFoundException
     */
    public Collection getWorkflowUsersGroups(UserIdDTO userId) throws WorkflowException;

}
