import React, { Component } from 'react';
import { LocaleProvider, Tabs, Icon, Select, Divider, Row, Col, Form, Input, Button } from 'antd';
import { formatMessage, FormattedMessage, setLocale, getLocale } from 'umi/locale';
import Link from 'umi/link';
import zhCN from 'antd/lib/locale-provider/zh_CN';
import enUS from 'antd/lib/locale-provider/en_US';

import Login from '@/components/Login';
import LoginQR from '@/components/LoginQR';

import styles from './index.css';

const TabPane = Tabs.TabPane;
const Option = Select.Option;

class LoginPage extends Component {
	constructor() {
		super();
		this.state = {
		  locale: null,
		};
	}

	handleChangeLanguage = (value) => {
	  if('zh-CN' === value){
		  this.setState({ locale: zhCN });
	  } else {
		  this.setState({ locale: enUS });
	  }
	  setLocale(value);
	}

	render(){
		const qrTabName = formatMessage({ id: 'app.login.tab-login-qrcode' });
		const emailTabName = formatMessage({ id: 'app.login.tab-login-email' });
		const chooseLanguage = formatMessage({ id: 'app.login.choose-language' });
		const localeText = getLocale();

		return(
			<LocaleProvider locale={this.state.locale}>
				<div className={styles.loginContainer}>
					<div>
					  <Tabs defaultActiveKey="2">
					    <TabPane className={styles.tabClass} tab={<span><Icon type="qrcode" />{qrTabName}</span>} key="1">
					      	<div style={{textAlign:"center"}}>
										<LoginQR/>
									</div>
									<div style={{textAlign:"center"}}>
										<a href="/downloadAPK">Download App</a>
									</div>
					    </TabPane>
					    <TabPane className={styles.tabClass} tab={<span><Icon type="mail" />{emailTabName}</span>} key="2">
					      <Login>
					      </Login>
					    </TabPane>
					  </Tabs>
					  <Divider style={{marginTop:"0px"}}/>
					</div>
					<div className={styles.signup}>
						<Row type="flex" justify="space-around" align="middle">
							<Col span={12}>
								<Select placeholder={chooseLanguage} defaultValue={localeText} className={styles.languageWidth} onChange={this.handleChangeLanguage}>
								  <Option value="zh-CN">中文</Option>
								  <Option value="en-US">English</Option>
								</Select>				
							</Col>
							<Col span={12} style={{textAlign:"right"}}>
								<Link className={styles.register} to="resetpassword">
									<FormattedMessage id="app.login.forgot-password" />
								</Link>
								<Divider type="vertical" />
								<Link className={styles.register} to="register">
									<FormattedMessage id="app.login.signup" />
								</Link>
							</Col>
						</Row>
					</div>
					<div className={styles.other}>
						<FormattedMessage id="app.login.sign-in-with" />
						<a href="/login/facebook">
							<Icon type="facebook" className={styles.icon} theme="outlined" />
						</a>
						<a href="/login/github">
							<Icon type="github" className={styles.icon} theme="outlined" />
						</a>
					</div>
				</div>			
			</LocaleProvider>
		);
	}
}
export default LoginPage;

