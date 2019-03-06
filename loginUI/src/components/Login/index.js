import React, { Component } from 'react';
import {
  Form, Icon, Input, Button, Checkbox,
} from 'antd';
import { formatMessage, FormattedMessage, setLocale, getLocale } from 'umi/locale';
import 'antd/dist/antd.css';
import styles from './index.css';

class NormalLoginForm extends React.Component {
  makeForm = (query) => {
    console.log(query);
  
    const form = document.createElement('form');
    form.id = 'form-login';
    form.name = 'form-login';
    // 添加到 body 中
    document.body.appendChild(form);
    for (const key in query) {
      if (query[key]!== undefined && Object.hasOwnProperty.call(query, key)) {
        // 创建一个输入
        const input = document.createElement('input');
        input.type = 'text';
        input.name = key;
        input.value = query[key];
        form.appendChild(input);
      }
    }
  
    // form 的提交方式
    form.method = 'POST';
    // form 提交路径
    form.action = '/login';
    form.submit();
    document.body.removeChild(form);
  }

  handleSubmit = (e) => {
    e.preventDefault();
    this.props.form.validateFields((err, values) => {
      if (!err) {
        console.log('Received values of form: ', values);
        this.makeForm(values);
      }
    });
  }

  render() {
    const userNameErrorMsg = formatMessage({ id: 'app.login.userNameErrorMsg' });
    const passwordErrorMsg = formatMessage({ id: 'app.login.passwordErrorMsg' });
    const userNamePlaceholder = formatMessage({ id: 'app.login.userName-placeholder' });
    const passwordPlaceholder = formatMessage({ id: 'app.login.password-placeholder' });
    const submitButtonText = formatMessage({ id: 'app.login.submit-button' });

    const { getFieldDecorator } = this.props.form;
    return (
      <Form onSubmit={this.handleSubmit} className="login-form">
        <Form.Item>
          {getFieldDecorator('username', {
            rules: [{ required: true, message: `${userNameErrorMsg}` }],
            initialValue:'jack.yu05@sap.com',
          })(
            <Input prefix={<Icon type="user" style={{ color: 'rgba(0,0,0,.25)' }} />} placeholder={userNamePlaceholder} />
          )}
        </Form.Item>
        <Form.Item>
          {getFieldDecorator('password', {
            rules: [{ required: true, message: `${passwordErrorMsg}` }],
            initialValue:'123456',
          })(
            <Input prefix={<Icon type="lock" style={{ color: 'rgba(0,0,0,.25)' }} />} type="password" placeholder={passwordPlaceholder} />
          )}
        </Form.Item>
        <Form.Item>
          <Button type="primary" htmlType="submit" className={styles.loginFormButton}>
            {submitButtonText}
          </Button>
        </Form.Item>
      </Form>
    );
  }
}

const WrappedNormalLoginForm = Form.create({ name: 'normal_login' })(NormalLoginForm);

export default WrappedNormalLoginForm;
