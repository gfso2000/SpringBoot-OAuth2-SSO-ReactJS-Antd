import React, { Component } from 'react';
import { LocaleProvider, Tabs, Icon, Select, Divider, Row, Col, Form, Input, Button } from 'antd';
import { formatMessage, FormattedMessage, setLocale, getLocale } from 'umi/locale';
import zhCN from 'antd/lib/locale-provider/zh_CN';
import enUS from 'antd/lib/locale-provider/en_US';
import Register from '@/components/Register';
import Link from 'umi/link';
import styles from './register.css';

class ResetPasswordPage extends Component {
	constructor() {
		super();
		this.state = {
		  locale: null,
		};
	}

	render(){
		const title = formatMessage({ id: 'app.login.register-title' });

		return(
			<LocaleProvider locale={this.state.locale}>
				<div className={styles.pageContainer}>
					<h2 className={styles.titleClass}>{title}</h2>
					<Register></Register>

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


