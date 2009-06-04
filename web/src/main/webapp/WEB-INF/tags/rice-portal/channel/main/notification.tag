<%--
 Copyright 2007 The Kuali Foundation.
 
 Licensed under the Educational Community License, Version 1.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 http://www.opensource.org/licenses/ecl1.php
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
--%>
<%@ include file="/rice-portal/jsp/sys/riceTldHeader.jsp"%>


<channel:portalChannelTop channelTitle="Notification" />
<div class="body">
 	yo!
    <ul class="chan">
		<li><portal:portalLink displayTitle="true" title="Notification Search" url="${ConfigProperties.kew.url}/DocumentSearch.do?&docTypeFullName=KualiNotification" /></li>
	    <li><portal:portalLink displayTitle="true" title="Channel Subscriptions" url="${ConfigProperties.ken.url}/DisplayUserPreferences.form" /></li>
	   	<li><portal:portalLink displayTitle="true" title="Delivery Types" url="${ConfigProperties.kcb.url}/prefs.kcb" /></li>
	    
    </ul>
    
 	
</div>
<channel:portalChannelBottom />
