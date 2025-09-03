/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.sso.openid.connect.web.internal.display.context;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.security.sso.openid.connect.configuration.ConfigurationProvider;
import com.liferay.portal.security.sso.openid.connect.configuration.OpenIdConnectProviderConfiguration;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Collections;

/**
 * @author Christian Moura
 */
public class OpenIdConnectProviderConfigurationDisplayContext {

	public OpenIdConnectProviderConfigurationDisplayContext(
		ConfigurationProvider<OpenIdConnectProviderConfiguration>
			openIdConnectProviderConfigurationProvider,
		HttpServletRequest httpServletRequest, String pid) {

		_openIdConnectProviderConfigurationProvider =
			openIdConnectProviderConfigurationProvider;
		_httpServletRequest = httpServletRequest;
		_pid = pid;
	}

	public int[] getCustomAuthorizationRequestParametersIndexes() {
		return _customAuthorizationRequestParametersIndexes;
	}

	public int[] getCustomTokenRequestParametersIndexes() {
		return _customTokenRequestParametersIndexes;
	}

	public int[] getIdTokenSigningAlgValuesIndexes() {
		return _idTokenSigningAlgValuesIndexes;
	}

	public OpenIdConnectProviderConfiguration
		getOpenIdConnectProviderConfiguration() {

		OpenIdConnectProviderConfiguration openIdConnectProviderConfiguration =
			_openIdConnectProviderConfigurationProvider.getConfiguration(_pid);

		if (openIdConnectProviderConfiguration == null) {
			openIdConnectProviderConfiguration =
				ConfigurableUtil.createConfigurable(
					OpenIdConnectProviderConfiguration.class,
					HashMapBuilder.<String, Object>put(
						"customAuthorizationRequestParameters",
						Collections.singletonList(StringPool.BLANK)
					).put(
						"customTokenRequestParameters",
						Collections.singletonList(StringPool.BLANK)
					).put(
						"openIdConnectClientId", StringPool.BLANK
					).put(
						"openIdConnectClientSecret", StringPool.BLANK
					).put(
						"providerName", StringPool.BLANK
					).put(
						"scopes", "openid email profile"
					).put(
						"subjectTypes",
						Collections.singletonList(StringPool.BLANK)
					).build());
		}

		_idTokenSigningAlgValuesIndexes = new int
			[openIdConnectProviderConfiguration.
				idTokenSigningAlgValues().length];

		for (int i = 0; i < _idTokenSigningAlgValuesIndexes.length; i++) {
			_idTokenSigningAlgValuesIndexes[i] = i;
		}

		_subjectTypesIndexes =
			new int[openIdConnectProviderConfiguration.subjectTypes().length];

		for (int i = 0; i < _subjectTypesIndexes.length; i++) {
			_subjectTypesIndexes[i] = i;
		}

		_customTokenRequestParametersIndexes = new int
			[openIdConnectProviderConfiguration.
				customTokenRequestParameters().length];

		for (int i = 0; i < _customTokenRequestParametersIndexes.length; i++) {
			_customTokenRequestParametersIndexes[i] = i;
		}

		_customAuthorizationRequestParametersIndexes = new int
			[openIdConnectProviderConfiguration.
				customAuthorizationRequestParameters().length];

		for (int i = 0; i < _customAuthorizationRequestParametersIndexes.length;
			 i++) {

			_customAuthorizationRequestParametersIndexes[i] = i;
		}

		return openIdConnectProviderConfiguration;
	}

	public int[] getSubjectTypesIndexes() {
		return _subjectTypesIndexes;
	}

	private int[] _customAuthorizationRequestParametersIndexes;
	private int[] _customTokenRequestParametersIndexes;
	private final HttpServletRequest _httpServletRequest;
	private int[] _idTokenSigningAlgValuesIndexes;
	private final ConfigurationProvider<OpenIdConnectProviderConfiguration>
		_openIdConnectProviderConfigurationProvider;
	private final String _pid;
	private int[] _subjectTypesIndexes;

}