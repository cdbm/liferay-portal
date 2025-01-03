/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {Locator, Page} from '@playwright/test';

import {liferayConfig} from '../../../liferay.config';

export class CreateAccountPage {
	readonly screenName: Locator;
	readonly emailAddress: Locator;
	readonly firstName: Locator;
	readonly lastName: Locator;
	readonly password: Locator;
	readonly reenterPassword: Locator;
	readonly saveButton: Locator;
	readonly page: Page;

	constructor(page: Page) {
		this.page = page;
		this.screenName = page.getByLabel('Screen Name');
		this.emailAddress = page.getByLabel('Email Address');
		this.firstName = page.getByLabel('First Name');
		this.lastName = page.getByLabel('Last Name');
		this.password = page.getByLabel('Password', {exact: true});
		this.reenterPassword = page.getByLabel('Reenter Password');
		this.saveButton = page.getByRole('button', {name: 'Save'});
	}

	async goto() {
		await this.page.goto(liferayConfig.environment.baseUrl);
		await this.page.getByRole('button', {name: 'Sign In'}).click();
		await this.page.getByText('Create Account').click();
	}

	async createAccount(
		screenName: string,
		email: string,
		firstName: string,
		lastName: string,
		password: string
	) {
		await this.screenName.fill(screenName);
		await this.emailAddress.fill(email);
		await this.firstName.fill(firstName);
		await this.lastName.fill(lastName);
		await this.password.fill(password);
		await this.reenterPassword.fill(password);

		await this.saveButton.click();
	}
}
