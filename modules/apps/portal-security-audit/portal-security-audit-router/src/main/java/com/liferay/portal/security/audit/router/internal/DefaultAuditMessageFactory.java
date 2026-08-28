/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.audit.router.internal;

import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerList;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerListFactory;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.audit.AuditMessage;
import com.liferay.portal.kernel.audit.AuditMessageFactory;
import com.liferay.portal.kernel.audit.AuditRequestThreadLocal;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PortalRunMode;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.security.audit.AuditMessageEnricher;

import java.util.Date;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Christian Moura
 */
@Component(service = AuditMessageFactory.class)
public class DefaultAuditMessageFactory extends AuditMessageFactory {

	@Override
	public AuditMessage getAuditMessage(
		long groupId, long companyId, long userId, String userName,
		Date timestampDate, long accountEntryId,
		JSONObject additionalInfoJSONObject, String className, String classPK,
		String contextName, String eventType, String message) {

		AuditMessage auditMessage = new AuditMessage(
			groupId, companyId, userId, userName, timestampDate, accountEntryId,
			additionalInfoJSONObject, className, classPK, contextName,
			eventType, message);

		_populate(auditMessage);

		for (AuditMessageEnricher auditMessageEnricher : _serviceTrackerList) {
			auditMessageEnricher.enrich(auditMessage);
		}

		return auditMessage;
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_serviceTrackerList = ServiceTrackerListFactory.open(
			bundleContext, AuditMessageEnricher.class);
	}

	@Deactivate
	protected void deactivate() {
		_serviceTrackerList.close();
	}

	private void _populate(AuditMessage auditMessage) {
		AuditRequestThreadLocal auditRequestThreadLocal =
			AuditRequestThreadLocal.getAuditThreadLocal();

		auditMessage.setClientHost(auditRequestThreadLocal.getClientHost());
		auditMessage.setClientIP(auditRequestThreadLocal.getClientIP());
		auditMessage.setServerName(auditRequestThreadLocal.getServerName());
		auditMessage.setServerPort(auditRequestThreadLocal.getServerPort());
		auditMessage.setSessionID(auditRequestThreadLocal.getSessionID());
		auditMessage.setUserEmailAddress(
			auditRequestThreadLocal.getRealUserEmailAddress());

		long realUserId = auditRequestThreadLocal.getRealUserId();

		long doAsUserId = 0;

		if (PrincipalThreadLocal.getName() != null) {
			doAsUserId = GetterUtil.getLong(PrincipalThreadLocal.getName());
		}

		JSONObject additionalInfoJSONObject = auditMessage.getAdditionalInfo();

		if ((realUserId > 0) && (doAsUserId != realUserId) &&
			!additionalInfoJSONObject.has("doAsUserId")) {

			additionalInfoJSONObject.put(
				"doAsUserEmailAddress",
				PortalUtil.getUserEmailAddress(doAsUserId)
			).put(
				"doAsUserId", String.valueOf(doAsUserId)
			).put(
				"doAsUserName",
				PortalUtil.getUserName(doAsUserId, StringPool.BLANK)
			);
		}

		if (auditMessage.getUserId() == realUserId) {
			auditMessage.setUserLogin(
				auditRequestThreadLocal.getRealUserLogin());
		}

		// LPS-172507

		else if ((realUserId > 0) && !PortalRunMode.isTestMode()) {
			_log.error(
				"Impersonated actions must be audited on the real user's ID");
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DefaultAuditMessageFactory.class);

	private ServiceTrackerList<AuditMessageEnricher> _serviceTrackerList;

}