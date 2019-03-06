import React, { Component } from 'react';
import {
  Form, Icon, Input, Button, Checkbox,
} from 'antd';
import { connect } from 'dva';
import 'antd/dist/antd.css';
import styles from './index.css';

@connect(({ register }) => ({
	register
}))
class LoginQRImage extends React.Component {
  constructor(props) {
    super(props);
    this.myRef = React.createRef();
  }

  componentDidMount() {
    this.props.dispatch({
      type: 'register/getLoginQR',
    });
  }

  componentDidUpdate() {
    const { register, dispatch } = this.props;
    if (register.status === 'showQR') {
      const {imgData} = register;
      dispatch({
        type: 'register/reset',
      });

      //add img content to img
      this.myRef.current.src = "data:image/jpg;base64,"+imgData;
    } else if(register.status === 'redirect'){
      window.location = register.redirectURL;
    }
  }
  
  render() {
    return (
      <img id="QrGen" src="" ref={this.myRef}/>
    );
  }
}

export default LoginQRImage;
