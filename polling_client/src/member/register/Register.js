import React, { Component } from 'react';
import {Form, Input, Button, notification} from 'antd';
import {withRouter} from "react-router-dom";
import 'antd/dist/antd.css';
import MemberService from '../../api/MemberService';
import './Register.css';

class Register extends Component{

    formItemLayout = {
        labelCol: {
        xs: {
        span: 24,
        },
        sm: {
        span: 8,
        },
    },
    wrapperCol: {
        xs: {
        span: 24,
        },
        sm: {
        span: 16,
        },
    },
    };
   
    tailFormItemLayout = {
    wrapperCol: {
        xs: {
        span: 24,
        offset: 0,
        },
        sm: {
        span: 16,
        offset: 8,
        },
    },
    };

    

    onFinish = (values) => {
        const register_fail=-1;
        MemberService.register(values)
        .then(response=>{
            if(response.data === register_fail){
              notification.error({
                message: 'Mini Polling',
                description: 'Register fail! Your Id is already exist! Please change '
            });   
            }
            else{
                notification.success({
                    message: 'Mini Polling',
                    description: "You're successfully register.",
                });   
                this.props.history.push("/");
            }
            
          })
    };

    render(){
        return (
            <div class="main" id="register">
                <div className="main" id="login">
                    <div style={{border:'solid',color:'#A9D0F5',borderRadius:'2em',marginTop:100}}>
                        <div style={{margin:50}}>
                        <h1 >Register</h1>
                        <Form
                        {...this.formItemLayout}
                        name="register"
                        onFinish={this.onFinish}
                        scrollToFirstError
                        >
                        <Form.Item
                            name="userName"
                            label="userName"
                            rules={[
                                {
                                required: true,
                                message: 'Please input your userName!',
                                whitespace: true,
                                },
                            ]}
                        >
                            <Input />
                        </Form.Item>

                        <Form.Item
                            name="passWord"
                            label="PassWord"
                            rules={[
                            {
                                required: true,
                                message: 'Please input your password!',
                            },
                            ]}
                            hasFeedback
                        >
                            <Input.Password />
                        </Form.Item>

                        <Form.Item
                            name="confirm"
                            label="Confirm Password"
                            dependencies={['passWord']}
                            hasFeedback
                            rules={[
                            {
                                required: true,
                                message: 'Please confirm your password!',
                            },
                            ({ getFieldValue }) => ({
                                validator(rule, value) {
                                if (!value || getFieldValue('passWord') === value) {
                                    return Promise.resolve();
                                }
                                else
                                    return Promise.reject('The two passwords that you entered do not match!');
                                },
                            }),
                            ]}
                        >
                            <Input.Password />
                        </Form.Item>

                        <Form.Item {...this.tailFormItemLayout}>
                            <Button type="primary" htmlType="submit">
                            Register
                            </Button>
                        </Form.Item>
                        </Form>
                        </div>
                    </div>
                </div>
            </div>
        )
    }  
}

export default withRouter(Register);
