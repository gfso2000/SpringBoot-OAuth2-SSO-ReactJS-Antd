import React, { Component } from 'react';
import { LocaleProvider, } from 'antd';
import { formatMessage, FormattedMessage, setLocale, getLocale } from 'umi/locale';
import zhCN from 'antd/lib/locale-provider/zh_CN';
import enUS from 'antd/lib/locale-provider/en_US';
import EditProfileForm from '@/components/EditProfile';
import styles from './index.css';

class ResetPasswordPage extends Component {
	constructor() {
		super();
		this.state = {
		  locale: null,
		};
	}

	render(){
		const title = formatMessage({ id: 'app.login.editprofile-title' });

		return(
      <LocaleProvider locale={this.state.locale}>
        <div className={styles.divContainer}>
          <h2 className={styles.titleClass}>{title}</h2>
          <EditProfileForm></EditProfileForm>
        </div>
    </LocaleProvider>
		);
	}
}
export default ResetPasswordPage;
