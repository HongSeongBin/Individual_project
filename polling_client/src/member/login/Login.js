import { Form, Input, Button, notification } from 'antd';
import { UserOutlined, LockOutlined } from '@ant-design/icons';
import React, { Component } from 'react';
import 'antd/dist/antd.css';
import './Login.css';
import MemberService from '../../api/MemberService';
import {withRouter} from "react-router-dom";


class Login extends Component{
  constructor(props) {
    super(props);
  }

  onFinish = values => {
    const login_fail=-1;

    MemberService.login(values)
    .then(response=>{
      if(response.data === login_fail){
        notification.error({
          message: 'Mini Polling',
          description: 'Your Username or Password is incorrect. Please try again!'
      });   
      }
      else{
          notification.success({
            message: 'Mini Polling',
            description: "You're successfully login.",
          });
          this.props.setUserstate(response.data);
          this.props.history.push("/mainpage/survey");
      }
      
    })
    
  };

  render(){
    return (
      <div className="main" id="login">
        <div style={{border:'solid',color:'#A9D0F5',borderRadius:'2em',marginTop:10}}>
          <div style={{margin:50}}>
          <h1>Login</h1>
          <Form
            name="normal_login"
            className="login-form"
            initialValues={{ remember: true }}
            onFinish={this.onFinish}
          >
            <Form.Item
              name="userName"
              rules={[{ required: true, message: 'Please input your Username!' }]}
            >
              <Input prefix={<UserOutlined className="site-form-item-icon" />} placeholder="Username" />
            </Form.Item>
            <Form.Item
              name="passWord"
              rules={[{ required: true, message: 'Please input your Password!' }]}
            >
              <Input
                prefix={<LockOutlined className="site-form-item-icon" />}
                type="password"
                placeholder="Password"
              />
            </Form.Item>
            <Form.Item>
              <Button type="primary" htmlType="submit" className="login-form-button" style={{marginRight:10}}>
                Log in
              </Button>
              Or <a href="/register">register now!</a>
            </Form.Item>
          </Form>
          </div>
        </div>
      </div>
    )
  }
}

export default withRouter(Login);