/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.audit;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.module.service.Snapshot;

import java.util.Date;

/**
 * @author Christian Moura
 */
public class AuditMessageFactoryUtil {

	public static AuditMessage getAuditMessage(
		long groupId, long companyId, long userId, String userName,
		Date timestampDate, JSONObject additionalInfoJSONObject,
		String className, String classPK, String eventType, String message) {

		return getAuditMessage(
			groupId, companyId, userId, userName, timestampDate, 0,
			additionalInfoJSONObject, className, classPK, null, eventType,
			message);
	}

	public static AuditMessage getAuditMessage(
		long groupId, long companyId, long userId, String userName,
		Date timestampDate, long accountEntryId,
		JSONObject additionalInfoJSONObject, String className, String classPK,
		String contextName, String eventType, String message) {

		AuditMessageFactory auditMessageFactory =
			_auditMessageFactorySnapshot.get();

		if (auditMessageFactory == null) {
			return new AuditMessage(
				groupId, companyId, userId, userName, timestampDate,
				accountEntryId, additionalInfoJSONObject, className, classPK,
				contextName, eventType, message);
		}

		return auditMessageFactory.getAuditMessage(
			groupId, companyId, userId, userName, timestampDate, accountEntryId,
			additionalInfoJSONObject, className, classPK, contextName,
			eventType, message);
	}

	public static AuditMessage getAuditMessage(
		long companyId, long userId, String userName, Date timestampDate,
		JSONObject additionalInfoJSONObject, String className, String classPK,
		String eventType, String message) {

		return getAuditMessage(
			0, companyId, userId, userName, timestampDate, 0,
			additionalInfoJSONObject, className, classPK, null, eventType,
			message);
	}

	public static AuditMessage getAuditMessage(
		long companyId, long userId, String userName,
		JSONObject additionalInfoJSONObject, String className, String classPK,
		String eventType, String message) {

		return getAuditMessage(
			0, companyId, userId, userName, null, 0, additionalInfoJSONObject,
			className, classPK, null, eventType, message);
	}

	public static AuditMessage getAuditMessage(
		long companyId, long userId, String userName, String eventType) {

		return getAuditMessage(
			0, companyId, userId, userName, null, 0, null, null, null, null,
			eventType, null);
	}

	public static AuditMessage getAuditMessage(
		long companyId, long userId, String userName, String className,
		String classPK, String eventType) {

		return getAuditMessage(
			0, companyId, userId, userName, null, 0, null, className, classPK,
			null, eventType, null);
	}

	public static AuditMessage getAuditMessage(
		long companyId, long userId, String userName, String className,
		String classPK, String eventType, String message) {

		return getAuditMessage(
			0, companyId, userId, userName, null, 0, null, className, classPK,
			null, eventType, message);
	}

	private static final Snapshot<AuditMessageFactory>
		_auditMessageFactorySnapshot = new Snapshot<>(
			AuditMessageFactoryUtil.class, AuditMessageFactory.class);

}