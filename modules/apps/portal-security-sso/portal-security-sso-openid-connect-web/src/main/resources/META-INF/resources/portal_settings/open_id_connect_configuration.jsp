<%--
/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>
<%@ page import="com.liferay.portal.security.sso.openid.connect.configuration.OpenIdConnectProviderConfiguration " %>
<%@ page import="com.liferay.portal.security.sso.openid.connect.web.internal.display.context.OpenIdConnectProviderConfigurationDisplayContext" %>
<%@ page import="com.liferay.portal.kernel.util.StringUtil" %>

<%
OpenIdConnectProviderConfigurationDisplayContext openIdConnectProviderConfigurationDisplayContext = (OpenIdConnectProviderConfigurationDisplayContext)request.getAttribute("OPEN_ID_CONNECT_PROVIDER_CONFIGURATION_DISPLAY_CONTEXT");
OpenIdConnectProviderConfiguration configuration = openIdConnectProviderConfigurationDisplayContext.getOpenIdConnectProviderConfiguration();
%>


<aui:input id="providerName" label="provider-name" name="providerName" helpMessage="provider-name-help"  required="<%= true %>" type="text" value="<%= configuration.providerName() %>"/>

<aui:input id="scopes" label="scopes" name="scopes" helpMessage="scopes-help" required="<%= true %>" type="text" value="<%= configuration.scopes() %>" />

<aui:input label="discovery-endpoint" helpMessage="discovery-endpoint-help" name="discoveryEndPoint"  type="text" value="<%= configuration.discoveryEndPoint() %>"/>

<aui:input label="discovery-endpoint-cache-in-millis" helpMessage="discovery-endpoint-cache-in-millis-help" name="discoveryEndPointCacheInMillis"  type="number" value="<%= configuration.discoveryEndPointCacheInMillis() %>"/>

<aui:input label="authorization-endpoint" helpMessage="authorization-endpoint-help" name="authorizationEndPoint"  type="text" value="<%= configuration.authorizationEndPoint() %>"/>

<aui:input label="issuer-url" helpMessage="issuer-url-help" name="issuerURL"  type="text" value="<%= configuration.issuerURL() %>"/>

<aui:input label="jwks-uri" helpMessage="jwks-uri-help" name="jwksURI"  type="text" value="<%= configuration.jwksURI() %>"/>


<aui:fieldset id='<%= liferayPortletResponse.getNamespace() + "idTokenSigningAlgValuesContentBox" %>'  helpMessage="id-token-signing-alg-values-help"  label="id-token-signing-alg-values">

	<%
	int[] idTokenSigningAlgValuesIndexes = openIdConnectProviderConfigurationDisplayContext.getIdTokenSigningAlgValuesIndexes();
	%>

			<%
				for(int i = 0; i < idTokenSigningAlgValuesIndexes.length; i++) {
				String signingAlg = configuration.idTokenSigningAlgValues()[i];
				int index = i;
				String fieldId = "idTokenSigningAlgValues-" + index;
			%>
				<div class="form-group-autofit lfr-form-row user-attribute-mapping-row" >

					<div class="form-group-item">
						<aui:input id="<%= fieldId %>" fieldParam="<%= fieldId %>" label="idTokenSigningAlgValues"  name="<%= fieldId %>"  type="text" value="<%= signingAlg %>"/>
					</div>

				</div>
			<%
				}
			%>

		<aui:input name="idTokenSigningAlgValuesIndexes" type="hidden" value="<%= StringUtil.merge(idTokenSigningAlgValuesIndexes) %>" />
</aui:fieldset>

<aui:script use="liferay-auto-fields">
	new Liferay.AutoFields({
		contentBox: '#<portlet:namespace />idTokenSigningAlgValuesContentBox',
		fieldIndexes:
				'<portlet:namespace />idTokenSigningAlgValuesIndexes',
		namespace: '<portlet:namespace />',
	}).render();
</aui:script>


<aui:fieldset id='<%= liferayPortletResponse.getNamespace() + "subjectTypesContentBox" %>'  helpMessage="subject-types-help"  label="subject-types">

	<%
	int[] subjectTypesIndexes = openIdConnectProviderConfigurationDisplayContext.getSubjectTypesIndexes();
	%>

			<%
				for(int i = 0; i < subjectTypesIndexes.length; i++) {
				String subjectType = configuration.subjectTypes()[i];
				int index = i;
				String fieldId = "subjectTypes-" + index;
			%>
				<div class="form-group-autofit lfr-form-row user-attribute-mapping-row" >

					<div class="form-group-item">
						<aui:input id="<%= fieldId %>" fieldParam="<%= fieldId %>" label="subjectTypes"  name="<%= fieldId %>"  type="text" value="<%= subjectType %>"/>
					</div>

				</div>
			<%
				}
			%>

		<aui:input name="subjectTypesIndexes " type="hidden" value="<%= StringUtil.merge(subjectTypesIndexes) %>" />
</aui:fieldset>

<aui:script use="liferay-auto-fields">
	new Liferay.AutoFields({
		contentBox: '#<portlet:namespace />subjectTypesContentBox',
		fieldIndexes:
				'<portlet:namespace />subjectTypesIndexes ',
		namespace: '<portlet:namespace />',
	}).render();
</aui:script>


<aui:input label="token-endpoint" helpMessage="token-endpoint-help" name="tokenEndPoint"  type="text" value="<%= configuration.tokenEndPoint() %>"/>

<aui:input label="token-connection-timeout" helpMessage="token-connection-timeout-help" name="tokenConnectionTimeout"  type="text" value="<%= configuration.tokenConnectionTimeout() %>"/>

<aui:input label="user-info-endpoint" helpMessage="user-info-endpoint-help" name="userInfoEndPoint"  type="text" value="<%= configuration.userInfoEndPoint() %>"/>

<aui:input label="open-id-connect-client-id" helpMessage="open-id-connect-client-id-help" name="openIdConnectClientId"  type="text" required="<%= true %>"  value="<%= configuration.openIdConnectClientId() %>"/>

<aui:input label="open-id-connect-client-secret" helpMessage="open-id-connect-client-secret-help" name="openIdConnectClientSecret"  type="text" required="<%= true %>" value="<%= configuration.openIdConnectClientSecret() %>"/>

<aui:input label="registered-id-token-signing-alg"  helpMessage="registered-id-token-signing-alg-help"  name="registeredIdTokenSigningAlg"  type="text" value="<%= configuration.registeredIdTokenSigningAlg() %>"/>

<aui:fieldset id='<%= liferayPortletResponse.getNamespace() + "customAuthorizationRequestParametersContentBox" %>' helpMessage="custom-authorization-request-parameters-help"  label="custom-authorization-request-parameters">
	<%
	int[] customAuthorizationRequestParametersIndexes = openIdConnectProviderConfigurationDisplayContext.getCustomAuthorizationRequestParametersIndexes();
	%>

			<%
				for(int i = 0; i < customAuthorizationRequestParametersIndexes.length; i++) {
				String customAuthorizationRequestParameter = configuration.customAuthorizationRequestParameters()[i];
				int index = i;
				String fieldId = "customAuthorizationRequestParameters-" + index;
			%>
				<div class="form-group-autofit lfr-form-row user-attribute-mapping-row" >

					<div class="form-group-item">
						<aui:input id="<%= fieldId %>" fieldParam="<%= fieldId %>" label="customAuthorizationRequestParameters"  name="<%= fieldId %>"  type="text" value="<%= customAuthorizationRequestParameter %>"/>
					</div>

				</div>
			<%
				}
			%>

		<aui:input name="customAuthorizationRequestParametersIndexes" type="hidden" value="<%= StringUtil.merge(customAuthorizationRequestParametersIndexes) %>" />
</aui:fieldset>


<aui:script use="liferay-auto-fields">
	new Liferay.AutoFields({
		contentBox: '#<portlet:namespace />customAuthorizationRequestParametersContentBox',
		fieldIndexes:
				'<portlet:namespace />customAuthorizationRequestParametersIndexes',
		namespace: '<portlet:namespace />',
	}).render();
</aui:script>

<aui:fieldset id='<%= liferayPortletResponse.getNamespace() + "customTokenRequestParametersContentBox" %>'  helpMessage="custom-token-request-parameters-help"  label="custom-token-request-parameters">
	<%
	int[] customTokenRequestParametersIndexes = openIdConnectProviderConfigurationDisplayContext.getCustomTokenRequestParametersIndexes();
	%>

			<%
				for(int i = 0; i < customTokenRequestParametersIndexes.length; i++) {
				String customTokenRequestParameter = configuration.customTokenRequestParameters()[i];
				int index = i;
				String fieldId = "customTokenRequestParameters-" + index;
			%>
				<div class="form-group-autofit lfr-form-row user-attribute-mapping-row" >

					<div class="form-group-item">
						<aui:input id="<%= fieldId %>" fieldParam="<%= fieldId %>" label="customTokenRequestParameters"  name="<%= fieldId %>"  type="text" value="<%= customTokenRequestParameter %>"/>
					</div>

				</div>
			<%
				}
			%>

		<aui:input name="customTokenRequestParametersIndexes" type="hidden" value="<%= StringUtil.merge(customTokenRequestParametersIndexes) %>" />
</aui:fieldset>

<aui:script use="liferay-auto-fields">
	new Liferay.AutoFields({
		contentBox: '#<portlet:namespace />customTokenRequestParametersContentBox',
		fieldIndexes:
				'<portlet:namespace />customTokenRequestParametersIndexes',
		namespace: '<portlet:namespace />',
	}).render();
</aui:script>

<aui:input label="custom-claims"  helpMessage="custom-claims-help"  name="customClaims"  type="text" />
<%--
<aui:field-wrapper label="custom-claims">
<div id="userAttributeMappingsContentBox">

<%
for (int i = 0; i < 3; i++) {
%>

<div class="form-group-autofit lfr-form-row user-attribute-mapping-row" data-prefix="">
	<div class="form-group-item">
		<aui:select fieldParam="" id="" inlineField="" label="user-field-expression" name="" showEmptyOption="<%= true %>">

			<%
			for (int j = 0; j < 3; j++) {
			%>

				<aui:option data-authsupported="" label="test" selected="" value="test"></aui:option>

			<%
			}
			%>

		</aui:select>
	</div>

	<div class="form-group-item">
		<aui:input cssClass="saml-attribute-field" fieldParam="" id="" inlineField="" label="saml-attribute" name="" type="text" value="" />
	</div>

	<div class="form-group-item form-group-item-label-spacer form-group-item-shrink">
		<aui:input checked='false' cssClass="primary-ctrl" disabled="false" id='test' inlineField="<%= true %>" label="use-to-match-users" name="attribute:userIdentifierExpressionIndex" type="radio" value="" />
	</div>

</div>


<%
}
%>

<aui:input name='test name' type="hidden" value="test value" />

</div>

<aui:script use="liferay-auto-fields">
	new Liferay.AutoFields({
		contentBox: '#userAttributeMappingsContentBox',
		fieldIndexes:
			'<portlet:namespace />userAttributeMappingsIndexes',
		namespace: '<portlet:namespace />',
	}).render();
</aui:script>

</aui:field-wrapper>
--%>
