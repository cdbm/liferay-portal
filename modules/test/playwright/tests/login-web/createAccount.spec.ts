/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {mergeTests} from '@playwright/test';

import {CaptchaConfigPage} from '../../pages/captcha-web/CaptchaConfigPage';
import {getRandomInt} from '../../utils/getRandomInt';
import performLogin, {performLogout} from '../../utils/performLogin';
import {createAccountPageTest} from './fixtures/createAccountPageTest';
import {CreateAccountPage} from './pages/CreateAccountPage';

export const test = mergeTests(createAccountPageTest);

test('LPD-44960 creating account with used email does not raise an error', async ({
	page,
}) => {
	await performLogin(page, 'test');

	const captchaPage = new CaptchaConfigPage(page);

	await captchaPage.goTo();

	await captchaPage.createAccountCaptchaEnabled.uncheck();

	await captchaPage.saveConfiguration();

	await performLogout(page);

	const createAccountPage = new CreateAccountPage(page);

	await createAccountPage.goto();

	const email = `test-${getRandomInt()}@liferay.com`;

	await createAccountPage.createAccount(
		`test${getRandomInt()}`,
		email,
		`test${getRandomInt()}`,
		`test${getRandomInt()}`,
		'test'
	);

	await createAccountPage.goto();

	await createAccountPage.createAccount(
		`test${getRandomInt()}`,
		email,
		`test${getRandomInt()}`,
		`test${getRandomInt()}`,
		'test'
	);
});
