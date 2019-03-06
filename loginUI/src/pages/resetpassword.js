import React, { Component } from 'react';
import { LocaleProvider, Tabs, Icon, Select, Divider, Row, Col, Form, Input, Button } from 'antd';
import { formatMessage, FormattedMessage, setLocale, getLocale } from 'umi/locale';
import zhCN from 'antd/lib/locale-provider/zh_CN';
import enUS from 'antd/lib/locale-provider/en_US';
import ResetPassword from '@/components/ResetPassword';
import Link from 'umi/link';
import styles from './resetpassword.css';

class ResetPasswordPage extends Component {
	constructor() {
		super();
		this.state = {
		  locale: null,
		};
	}

	render(){
		const title = formatMessage({ id: 'app.login.resetpassword-title' });

		return(
			<LocaleProvider locale={this.state.locale}>
				<div className={styles.pageContainer}>
					<h2 className={styles.titleClass}>{title}</h2>
					<ResetPassword></ResetPassword>

					<Divider/>
					<Link className={styles.register} to="/">
						<FormattedMessage id="app.login.backto-login" />
					</Link>
				</div>			
			</LocaleProvider>
		);
	}
}
export default ResetPasswordPage;


