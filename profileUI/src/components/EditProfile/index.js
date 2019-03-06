import { Component } from 'react';
import { Form, Icon, Input, Button, message, Row, Col, Popover, Progress } from 'antd';
import { formatMessage, FormattedMessage, setLocale, getLocale } from 'umi/locale';
import Link from 'umi/link';
import router from 'umi/router';
import { connect } from 'dva';
import styles from './index.css';

const passwordStatusMap = {
  ok: (
    <div className={styles.success}>
      <FormattedMessage id="validation.password.strength.strong" />
    </div>
  ),
  pass: (
    <div className={styles.warning}>
      <FormattedMessage id="validation.password.strength.medium" />
    </div>
  ),
  poor: (
    <div className={styles.error}>
      <FormattedMessage id="validation.password.strength.short" />
    </div>
  ),
};

const passwordProgressMap = {
  ok: 'success',
  pass: 'normal',
  poor: 'exception',
};

@connect(({ register, loading }) => ({
  register,
  submitting: loading.effects['register/submit'],
}))
class EditProfileForm extends Component {
	constructor() {
		super();
		this.state = {
			locale: null,
			count: 0,
			confirmDirty: false,
			visible: false,
			help: '',
		};
	}

  componentDidMount() {
    const { dispatch } = this.props;
    dispatch({
      type: 'register/getUserName',
    });
  }
  handleSubmit = e => {
    e.preventDefault();
    const { form, dispatch } = this.props;
    form.validateFields({ force: true }, (err, values) => {
      if (!err) {
        const { prefix } = this.state;
        dispatch({
          type: 'register/submit',
          payload: {
            ...values,
            prefix,
          },
        });
      }
    });
  };

  componentDidUpdate() {
    const { form, register, dispatch } = this.props;
    const account = form.getFieldValue('email');
    if (!register.status){
      return;
    }

    if (register.status === 'editprofile_success') {
      message.success("edit successfully");
    } else if(register.status === 'editprofile_fail'){
      message.error(register.errorMessage);
    } else if(register.status === 'getusername_success'){
      this.setState({...this.state, username:register.errorMessage});
    } else if(register.status === 'getusername_fail'){
      message.error('refresh again:'+register.errorMessage);
    }
    dispatch({
      type: 'register/reset',
    });
}

  componentWillUnmount() {
    clearInterval(this.interval);
  }

  onGetCaptcha = () => {
    let count = 59;
    this.setState({ count });
    this.interval = setInterval(() => {
      count -= 1;
      this.setState({ count });
      if (count === 0) {
        clearInterval(this.interval);
      }
    }, 1000);
  };

  getPasswordStatus = () => {
    const { form } = this.props;
    const value = form.getFieldValue('password');
    if (value && value.length > 9) {
      return 'ok';
    }
    if (value && value.length > 5) {
      return 'pass';
    }
    return 'poor';
  };

  handleConfirmBlur = e => {
    const { value } = e.target;
    const { confirmDirty } = this.state;
    this.setState({ confirmDirty: confirmDirty || !!value });
  };

  checkConfirm = (rule, value, callback) => {
    const { form } = this.props;
    if (value && value !== form.getFieldValue('password')) {
      callback(formatMessage({ id: 'validation.password.twice' }));
    } else {
      callback();
    }
  };

  checkPassword = (rule, value, callback) => {
    const { visible, confirmDirty } = this.state;
    if (!value) {
      this.setState({
        help: formatMessage({ id: 'validation.password.required' }),
        visible: !!value,
      });
      callback('error');
    } else {
      this.setState({
        help: '',
      });
      if (!visible) {
        this.setState({
          visible: !!value,
        });
      }
      if (value.length < 6) {
        callback('error');
      } else {
        const { form } = this.props;
        if (value && confirmDirty) {
          form.validateFields(['confirm'], { force: true });
        }
        callback();
      }
    }
  };

  changePrefix = value => {
    this.setState({
      prefix: value,
    });
  };

  renderPasswordProgress = () => {
    const { form } = this.props;
    const value = form.getFieldValue('password');
    const passwordStatus = this.getPasswordStatus();
    return value && value.length ? (
      <div className={styles[`progress-${passwordStatus}`]}>
        <Progress
          status={passwordProgressMap[passwordStatus]}
          className={styles.progress}
          strokeWidth={6}
          percent={value.length * 10 > 100 ? 100 : value.length * 10}
          showInfo={false}
        />
      </div>
    ) : null;
  };

  render() {
    const userNameErrorMsg = formatMessage({ id: 'app.login.userNameErrorMsg' });
    const userNamePlaceholder = formatMessage({ id: 'app.login.userName-placeholder' });
    const submitButtonText = formatMessage({ id: 'app.login.resetpassword-submit-button' });

	  const { form, submitting } = this.props;
    const { getFieldDecorator } = form;
    const { count, prefix, help, visible } = this.state;
    return (
      <div className={styles.main}>
        <Form onSubmit={this.handleSubmit}>
          <Form.Item>
            {getFieldDecorator('email', {
            	initialValue:`${this.state.username == null ? '' : this.state.username}`,
              rules: [
                {
                  required: true,
                  message: formatMessage({ id: 'validation.email.required' }),
                },
                {
                  type: 'email',
                  message: formatMessage({ id: 'validation.email.wrong-format' }),
                },
              ],
            })(
              <Input disabled placeholder={formatMessage({ id: 'form.email.placeholder' })} />
            )}
          </Form.Item>
          <Form.Item>
            {getFieldDecorator('firstName', {
            	initialValue:'Jack',
              rules: [
                {
                  required: true,
                  message: formatMessage({ id: 'validation.firstname.required' }),
                },
              ],
            })(
              <Input placeholder={formatMessage({ id: 'form.firstname.placeholder' })} />
            )}
          </Form.Item>
          <Form.Item>
            {getFieldDecorator('lastName', {
            	initialValue:'Yu',
              rules: [
                {
                  required: true,
                  message: formatMessage({ id: 'validation.lastname.required' }),
                },
              ],
            })(
              <Input placeholder={formatMessage({ id: 'form.lastname.placeholder' })} />
            )}
          </Form.Item>
          <Form.Item help={help}>
            <Popover
              getPopupContainer={node => node.parentNode}
              content={
                <div style={{ padding: '4px 0' }}>
                  {passwordStatusMap[this.getPasswordStatus()]}
                  {this.renderPasswordProgress()}
                  <div style={{ marginTop: 10 }}>
                    <FormattedMessage id="validation.password.strength.msg" />
                  </div>
                </div>
              }
              overlayStyle={{ width: 240 }}
              placement="right"
              visible={visible}
            >
              {getFieldDecorator('password', {
            	  initialValue:'2hsSj2rW$',
                rules: [
                  {
                    validator: this.checkPassword,
                  },
                ],
              })(
                <Input
                  type="password"
                  placeholder={formatMessage({ id: 'form.password.placeholder' })}
                />
              )}
            </Popover>
          </Form.Item>
          <Form.Item>
            {getFieldDecorator('matchingPassword', {
            	initialValue:'2hsSj2rW$',
              rules: [
                {
                  required: true,
                  message: formatMessage({ id: 'validation.confirm-password.required' }),
                },
                {
                  validator: this.checkConfirm,
                },
              ],
            })(
              <Input
                type="password"
                placeholder={formatMessage({ id: 'form.confirm-password.placeholder' })}
              />
            )}
          </Form.Item>
          <Form.Item>
              {getFieldDecorator('mobile', {
            	  initialValue:'13482877377',
                rules: [
                  {
                    required: true,
                    message: formatMessage({ id: 'validation.phone-number.required' }),
                  },
                  {
                    pattern: /^\d{11}$/,
                    message: formatMessage({ id: 'validation.phone-number.wrong-format' }),
                  },
                ],
              })(
                <Input
                  placeholder={formatMessage({ id: 'form.phone-number.placeholder' })}
                />
              )}
          </Form.Item>
          <Form.Item>
            <Row gutter={8}>
              <Col span={16}>
                {getFieldDecorator('captcha', {
                	initialValue:'123456',
                  rules: [
                    {
                      required: true,
                      message: formatMessage({ id: 'validation.verification-code.required' }),
                    },
                  ],
                })(
                  <Input
                    placeholder={formatMessage({ id: 'form.verification-code.placeholder' })}
                  />
                )}
              </Col>
              <Col span={8}>
                <Button
                  disabled={count}
                  className={styles.getCaptcha}
                  onClick={this.onGetCaptcha}
                >
                  {count
                    ? `${count} s`
                    : formatMessage({ id: 'app.register.get-verification-code' })}
                </Button>
              </Col>
            </Row>
          </Form.Item>
          <Form.Item>
            <Button
              loading={submitting}
              className={styles.editProfileFormButton}
              type="primary"
              htmlType="submit"
            >
              <FormattedMessage id="app.register.submit-button" />
            </Button>
          </Form.Item>
        </Form>
      </div>
    );
  }
}

const WrappedNormalLoginForm = Form.create({ name: 'editProfile' })(EditProfileForm);

export default WrappedNormalLoginForm;
