/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.audit.event.generators.util.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.audit.AuditMessage;
import com.liferay.portal.kernel.audit.AuditRequestThreadLocal;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.security.audit.event.generators.constants.EventTypes;
import com.liferay.portal.security.audit.event.generators.util.AuditMessageBuilder;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Christian Moura
 */
@RunWith(Arquillian.class)
public class AuditMessageBuilderTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testBuildAuditMessageForLogin() throws Exception {
		long companyId = RandomTestUtil.randomLong();
		long userId = RandomTestUtil.randomLong();
		String userName = RandomTestUtil.randomString();

		AuditMessage auditMessage = AuditMessageBuilder.buildAuditMessage(
			companyId, userId, userName, User.class.getName(),
			String.valueOf(userId), EventTypes.LOGIN);

		Assert.assertEquals(User.class.getName(), auditMessage.getClassName());
		Assert.assertEquals(String.valueOf(userId), auditMessage.getClassPK());
		Assert.assertEquals(companyId, auditMessage.getCompanyId());
		Assert.assertEquals(EventTypes.LOGIN, auditMessage.getEventType());
		Assert.assertNotNull(auditMessage.getTimestampDate());
		Assert.assertEquals(userId, auditMessage.getUserId());
		Assert.assertEquals(userName, auditMessage.getUserName());
	}

	@Test
	public void testBuildAuditMessageInjectsDoAsUser() throws Exception {
		AuditRequestThreadLocal auditRequestThreadLocal =
			AuditRequestThreadLocal.getAuditThreadLocal();

		long realUserId = RandomTestUtil.randomLong();

		auditRequestThreadLocal.setRealUserId(realUserId);

		String name = PrincipalThreadLocal.getName();

		long doAsUserId = TestPropsValues.getUserId();

		PrincipalThreadLocal.setName(String.valueOf(doAsUserId));

		try {
			AuditMessage auditMessage = AuditMessageBuilder.buildAuditMessage(
				RandomTestUtil.randomString(), realUserId,
				RandomTestUtil.randomString(), null);

			JSONObject additionalInfoJSONObject =
				auditMessage.getAdditionalInfo();

			Assert.assertEquals(
				String.valueOf(doAsUserId),
				additionalInfoJSONObject.getString("doAsUserId"));
		}
		finally {
			PrincipalThreadLocal.setName(name);

			AuditRequestThreadLocal.removeAuditThreadLocal();
		}
	}

	@Test
	public void testBuildAuditMessagePopulatesRequestContext()
		throws Exception {

		AuditRequestThreadLocal auditRequestThreadLocal =
			AuditRequestThreadLocal.getAuditThreadLocal();

		String clientIP = RandomTestUtil.randomString();
		String serverName = RandomTestUtil.randomString();
		String sessionID = RandomTestUtil.randomString();
		long userId = RandomTestUtil.randomLong();
		String userLogin = RandomTestUtil.randomString();

		auditRequestThreadLocal.setClientIP(clientIP);
		auditRequestThreadLocal.setRealUserId(userId);
		auditRequestThreadLocal.setRealUserLogin(userLogin);
		auditRequestThreadLocal.setServerName(serverName);
		auditRequestThreadLocal.setSessionID(sessionID);

		try {
			AuditMessage auditMessage = AuditMessageBuilder.buildAuditMessage(
				RandomTestUtil.randomLong(), userId,
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				String.valueOf(userId), RandomTestUtil.randomString());

			Assert.assertEquals(clientIP, auditMessage.getClientIP());
			Assert.assertEquals(serverName, auditMessage.getServerName());
			Assert.assertEquals(sessionID, auditMessage.getSessionID());
			Assert.assertEquals(userLogin, auditMessage.getUserLogin());
		}
		finally {
			AuditRequestThreadLocal.removeAuditThreadLocal();
		}
	}

}