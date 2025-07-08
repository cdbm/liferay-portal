/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.sso.openid.connect.internal.configuration;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.security.sso.openid.connect.configuration.ConfigurationProvider;
import com.liferay.portal.security.sso.openid.connect.configuration.OpenIdConnectProviderConfiguration;

import java.util.Collections;
import java.util.Dictionary;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.service.cm.Configuration;
import org.osgi.service.component.annotations.Component;

/**
 * @author Christian Moura
 */
@Component(
	property = "factoryPid=com.liferay.portal.security.sso.openid.connect.configuration.OpenIdConnectProviderConfiguration",
	service = ConfigurationProvider.class
)
public class OpenIdConnectProviderConfigurationProviderImpl
	implements ConfigurationProvider<OpenIdConnectProviderConfiguration> {

	@Override
	public void deleteConfiguration(String pid) {
		synchronized (_configurations) {
			_configurations.remove(pid);
		}
	}

	@Override
	public OpenIdConnectProviderConfiguration getConfiguration(String pid) {
		if (pid == null) {
			return null;
		}

		synchronized (_configurations) {
			return _configurations.get(pid);
		}
	}

	@Override
	public void registerConfiguration(Configuration configuration) {
		Dictionary<String, Object> properties = configuration.getProperties();

		if (properties == null) {
			properties = new HashMapDictionary<>();
		}

		if (GetterUtil.getStringValues(
				properties.get("customAuthorizationRequestParameters")).
					length == 0) {

			properties.put(
				"customAuthorizationRequestParameters",
				Collections.singletonList(StringPool.BLANK));
		}

		if (GetterUtil.getStringValues(
				properties.get("customTokenRequestParameters")).length == 0) {

			properties.put(
				"customTokenRequestParameters",
				Collections.singletonList(StringPool.BLANK));
		}

		if (GetterUtil.getStringValues(
				properties.get("idTokenSigningAlgValues")).length == 0) {

			properties.put(
				"idTokenSigningAlgValues",
				Collections.singletonList(StringPool.BLANK));
		}

		if (GetterUtil.getStringValues(properties.get("subjectTypes")).length ==
				0) {

			properties.put(
				"subjectTypes", Collections.singletonList(StringPool.BLANK));
		}

		OpenIdConnectProviderConfiguration openIdConnectProviderConfiguration =
			ConfigurableUtil.createConfigurable(
				OpenIdConnectProviderConfiguration.class, properties);

		synchronized (_configurations) {
			_configurations.put(
				configuration.getPid(), openIdConnectProviderConfiguration);
		}
	}

	private final Map<String, OpenIdConnectProviderConfiguration>
		_configurations = new ConcurrentHashMap<>();

}