/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {Page, expect, mergeTests} from '@playwright/test';

import {consentManagerConfigurationPageTest} from '../../../fixtures/consentManagerConfigurationPageTest';
import {featureFlagsTest} from '../../../fixtures/featureFlagsTest';
import {loginTest} from '../../../fixtures/loginTest';
import {systemSettingsPageTest} from '../../../fixtures/systemSettingsPageTest';
import {
	clearConsentCookies,
	resetAllConsentManagerConfigurations,
	updateConsentManagerConfiguration,
} from './utils/consentManagerConfigurationHelper';

export const test = mergeTests(
	consentManagerConfigurationPageTest,
	featureFlagsTest({
		'LPD-75064': {enabled: true},
	}),
	loginTest(),
	systemSettingsPageTest
);

test.afterEach(async ({systemSettingsPage}) => {
	await test.step('Reset All Consent Manager Configurations', async () => {
		await resetAllConsentManagerConfigurations(systemSettingsPage);
	});

	await test.step('Clear Consent Cookies if present', async () => {
		await clearConsentCookies(systemSettingsPage.page);
	});
});

async function stubNavigatorGlobalPrivacyControl(page: Page, value: boolean) {
	await page.addInitScript((stubValue) => {
		Object.defineProperty(navigator, 'globalPrivacyControl', {
			configurable: true,
			get: () => stubValue,
		});
	}, value);
}

async function readIsGpcSignalActive(page: Page) {
	return await page.evaluate(async () => {
		const moduleScripts = Array.from(
			document.querySelectorAll<HTMLScriptElement>(
				'script[type="module"]'
			)
		);

		const gpcScript = moduleScripts.find((script) =>
			script.textContent?.includes('initGPC(')
		);

		const importMatch = gpcScript?.textContent?.match(
			/from\s+["']([^"']+)["']/
		);

		if (!importMatch) {
			return false;
		}

		const gpcModule = (await new Function('url', 'return import(url)')(
			importMatch[1]
		)) as {isGPCSignalActive: () => boolean};

		return gpcModule.isGPCSignalActive();
	});
}

test.describe('with Sec-GPC: 1 header', () => {
	test.use({extraHTTPHeaders: {'Sec-GPC': '1'}});

	test(
		'Signal is active when header and navigator both indicate GPC',
		{tag: '@LPD-86456'},
		async ({page}) => {
			await updateConsentManagerConfiguration(page, {
				enabled: true,
				forceReload: true,
				globalPrivacyControlEnabled: true,
			});

			await stubNavigatorGlobalPrivacyControl(page, true);

			await page.goto('/');

			expect(await readIsGpcSignalActive(page)).toBe(true);
		}
	);

	test(
		'Signal is active when only the header indicates GPC',
		{tag: '@LPD-86456'},
		async ({page}) => {
			await updateConsentManagerConfiguration(page, {
				enabled: true,
				forceReload: true,
				globalPrivacyControlEnabled: true,
			});

			await stubNavigatorGlobalPrivacyControl(page, false);

			await page.goto('/');

			expect(await readIsGpcSignalActive(page)).toBe(true);
		}
	);

	test(
		'Signal is inactive when admin does not honor GPC even with header and navigator set',
		{tag: '@LPD-86456'},
		async ({page}) => {
			await updateConsentManagerConfiguration(page, {
				enabled: true,
				forceReload: true,
				globalPrivacyControlEnabled: false,
			});

			await stubNavigatorGlobalPrivacyControl(page, true);

			await page.goto('/');

			expect(await readIsGpcSignalActive(page)).toBe(false);
		}
	);
});

test.describe('without Sec-GPC header', () => {
	test(
		'Signal is active when only navigator.globalPrivacyControl indicates GPC',
		{tag: '@LPD-86456'},
		async ({page}) => {
			await updateConsentManagerConfiguration(page, {
				enabled: true,
				forceReload: true,
				globalPrivacyControlEnabled: true,
			});

			await stubNavigatorGlobalPrivacyControl(page, true);

			await page.goto('/');

			expect(await readIsGpcSignalActive(page)).toBe(true);
		}
	);

	test(
		'Signal is inactive when neither header nor navigator indicates GPC',
		{tag: '@LPD-86456'},
		async ({page}) => {
			await updateConsentManagerConfiguration(page, {
				enabled: true,
				forceReload: true,
				globalPrivacyControlEnabled: true,
			});

			await stubNavigatorGlobalPrivacyControl(page, false);

			await page.goto('/');

			expect(await readIsGpcSignalActive(page)).toBe(false);
		}
	);
});
