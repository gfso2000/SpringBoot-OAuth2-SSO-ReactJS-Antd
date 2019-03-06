import React, { Component } from 'react';
import {
  Form, Icon, Input, Button, Checkbox,
} from 'antd';
import { formatMessage, FormattedMessage, setLocale, getLocale } from 'umi/locale';
import styles from './index.css';
import { connect } from 'dva';
import router from 'umi/router';

@connect(({ register, loading }) => ({
	register,
	submitting: loading.effects['register/resetPassword'],
}))
class ResetPasswordForm extends React.Component {
	constructor() {
		super();
		this.state = {
		  locale: null,
		  userName: 'jack.yu05@sap.com',
		};
	}

	emitEmpty = () => {
		this.userNameInput.focus();
		this.setState({ userName: '' });
		this.props.form.setFieldsValue({
        userName: '',
    });
	}

	onChangeUserName = (e) => {
		this.setState({ userName: e.target.value });
	}

	componentDidUpdate() {
    const { form, register, dispatch } = this.props;
    const account = form.getFieldValue('userName');
    if (register.status === 'success') {
      dispatch({
        type: 'register/reset',
      });

    	router.push({
        pathname: '/registerResult',
        state: {
          account,
        },
      });
    }
	}
	
  handleSubmit = e => {
    e.preventDefault();
    const { form, dispatch } = this.props;
    form.validateFields({ force: true }, (err, values) => {
      if (!err) {
        const { userName } = this.state;
        dispatch({
          type: 'register/resetPassword',
          payload: {
            userName,
          },
        });
      }
    });
	};
	
  render() {
    const userNameErrorMsg = formatMessage({ id: 'app.login.userNameErrorMsg' });
    const userNamePlaceholder = formatMessage({ id: 'app.login.userName-placeholder' });
    const submitButtonText = formatMessage({ id: 'app.login.resetpassword-submit-button' });

    const { getFieldDecorator } = this.props.form;
    const { userName } = this.state;
		const suffix = userName ? <Icon type="close-circle" onClick={this.emitEmpty} /> : null;

    return (
      <Form onSubmit={this.handleSubmit} className="login-form">
        <Form.Item>
          {getFieldDecorator('userName', {
          	initialValue: userName,
            rules: [{ required: true, message: `${userNameErrorMsg}` }],
          })(
            <Input
				placeholder={userNamePlaceholder}
				prefix={<Icon type="mail" style={{ color: 'rgba(0,0,0,.25)' }} />}
				suffix={suffix}
				onChange={this.onChangeUserName}
				ref={node => this.userNameInput = node}
			/>
          )}
        </Form.Item>
        <Form.Item>
          <Button loading={this.props.submitting} type="primary" htmlType="submit" className={styles.resetPasswordFormButton}>
            {submitButtonText}
          </Button>
        </Form.Item>
      </Form>
    );
  }
}

const WrappedNormalLoginForm = Form.create({ name: 'resetpassword' })(ResetPasswordForm);

export default WrappedNormalLoginForm;
