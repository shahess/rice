/*
 * Copyright 2007-2008 The Kuali Foundation
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
package org.kuali.rice.kim.bo.types.impl;

import java.util.LinkedHashMap;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.kuali.rice.kim.bo.types.KimAttributeData;
import org.kuali.rice.kim.bo.types.dto.KimTypeInfo;
import org.kuali.rice.kim.service.KIMServiceLocator;
import org.kuali.rice.kns.bo.PersistableBusinessObjectBase;
import org.kuali.rice.kns.util.ObjectUtils;

/**
 * This is a description of what this class does - kellerj don't forget to fill this in.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@MappedSuperclass
public class KimAttributeDataImpl extends PersistableBusinessObjectBase implements KimAttributeData {

	private static final long serialVersionUID = 2717672624042902701L;

	@Id
	@Column(name="ATTR_DATA_ID")
	protected String attributeDataId;

	@Column(name="KIM_TYP_ID")
	protected String kimTypeId;

	@Column(name="KIM_ATTR_DEFN_ID")
	protected String kimAttributeId;

	@Column(name="ATTR_VAL")
	protected String attributeValue;

	@ManyToOne(targetEntity=KimAttributeImpl.class, fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "KIM_ATTR_DEFN_ID", insertable = false, updatable = false)
	protected KimAttributeImpl kimAttribute;

	@Transient
	protected transient KimTypeInfo kimType;

	public String getAttributeDataId() {
		return this.attributeDataId;
	}
	public void setAttributeDataId(String attributeDataId) {
		this.attributeDataId = attributeDataId;
	}

	public String getAttributeValue() {
		return this.attributeValue;
	}
	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}
	public String getKimTypeId() {
		return this.kimTypeId;
	}
	public void setKimTypeId(String kimTypeId) {
		this.kimTypeId = kimTypeId;
	}
	public String getKimAttributeId() {
		return this.kimAttributeId;
	}
	public void setKimAttributeId(String kimAttributeId) {
		this.kimAttributeId = kimAttributeId;
	}
	public KimAttributeImpl getKimAttribute() {
		if(ObjectUtils.isNull(this.kimAttribute)) {
				this.refreshReferenceObject("kimAttribute");
		}
		return this.kimAttribute;
	}
	public void setKimAttribute(KimAttributeImpl kimAttribute) {
		this.kimAttribute = kimAttribute;
	}
	public KimTypeInfo getKimType() {
		if ( kimType == null ) {
			kimType = KIMServiceLocator.getTypeInfoService().getKimType(kimTypeId);
		}
		return kimType;
	}

	/**
	 * @see org.kuali.rice.kns.bo.BusinessObjectBase#toStringMapper()
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected LinkedHashMap toStringMapper() {
		LinkedHashMap m = new LinkedHashMap();
		m.put( "attributeDataId", attributeDataId );
		m.put( "kimTypeId", kimTypeId );
		m.put( "kimAttributeId", kimAttributeId );
		m.put( "attributeValue", attributeValue );
		return m;
	}

	/**
	 * @see org.kuali.rice.kns.bo.BusinessObjectBase#toStringBuilder(java.util.LinkedHashMap)
	 */
	@SuppressWarnings("unchecked")
	@Override
    public String toStringBuilder(LinkedHashMap mapper) {
        if(getKimAttribute() != null){
        	return getAttributeValue();
        }
        else {
            return super.toStringBuilder(mapper);
        }
    }

}