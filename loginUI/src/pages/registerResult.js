import React, { Component } from 'react';
import { LocaleProvider, Tabs, Icon, Select, Divider, Row, Col, Form, Input, Button } from 'antd';
import { formatMessage, FormattedMessage, setLocale, getLocale } from 'umi/locale';
import zhCN from 'antd/lib/locale-provider/zh_CN';
import enUS from 'antd/lib/locale-provider/en_US';
import RegisterResult from '@/components/RegisterResult';
import Link from 'umi/link';
import styles from './registerResult.css';

class RegisterResultPage extends Component {
	constructor() {
		super();
		this.state = {
      locale: null,      
		};
	}

	render(){
		const title = formatMessage({ id: 'app.login.register-success' });
    const userName = this.props.location.state.account;

		return(
			<LocaleProvider locale={this.state.locale}>
				<div className={styles.pageContainer}>
					<h2 className={styles.titleClass}>{title}</h2>
					<RegisterResult username={userName}></RegisterResult>

					<Divider/>
					<Link className={styles.register} to="/">
						<FormattedMessage id="app.login.backto-login" />
					</Link>
				</div>			
			</LocaleProvider>
		);
	}
}
export default RegisterResultPage;


