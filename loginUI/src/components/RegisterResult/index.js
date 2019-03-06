import React, { Component } from 'react';
import {
  Form, Icon, Input, Button, Checkbox,
} from 'antd';
import { formatMessage, FormattedMessage, setLocale, getLocale } from 'umi/locale';
import styles from './index.less';

class RegisterResult extends React.Component {
	constructor() {
		super();
		this.state = {
		  locale: null,
		};
	}

  render() {
    const description = formatMessage({ id: 'app.login.register-success-desc' });

    return (
        <div className={styles.result}>
            <div className={styles.icon}><Icon className={styles.success} type="check-circle" theme="filled" /></div>
            <div className={styles.title}>{this.props.username}</div>
            <div className={styles.description}>{description}</div>
        </div>
    );
  }
}

export default RegisterResult;
