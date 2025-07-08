/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.sso.openid.connect.configuration;

import org.osgi.service.cm.Configuration;

/**
 * @author Christian Moura
 */
public interface ConfigurationProvider<T> {

	public void deleteConfiguration(String pid);

	public T getConfiguration(String pid);

	public void registerConfiguration(Configuration configuration);

}