/**
 * Copyright 2005-2014 The Kuali Foundation
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
package org.kuali.rice.kew.doctype;

import org.kuali.rice.krad.data.jpa.IdClassBase;

/**
 * Compound primary key for {@link DocumentTypePolicy}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentTypePolicyId extends IdClassBase {

    private static final long serialVersionUID = -8024479878884387727L;

    private String documentType;
    private String policyName;

    public DocumentTypePolicyId() {}

    public DocumentTypePolicyId(String documentType, String policyName) {
        this.documentType = documentType;
        this.policyName = policyName;
    }

    public String getDocumentType() {
        return documentType;
    }

    public String getPolicyName() {
        return policyName;
    }

}
