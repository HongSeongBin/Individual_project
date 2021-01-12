import { Layout, Menu } from 'antd';
import React, { Component } from 'react';
import 'antd/dist/antd.css';
import { Link } from "react-router-dom";
import './AppHeader.css';

const { SubMenu } = Menu;
const { Header } = Layout;

class AppHeader extends Component{
    constructor(props) {
        super(props);   
      
    }
    render(){
        return(
        <Layout>
            <Header className="app-header" style={{ position: 'fixed', zIndex: 1, width: '100%' }}>
            
                <div className="app-title" style={{marginLeft : 30}}>
                    <Link to="/mainpage/survey">Mini Polling</Link>
                </div>
                <Menu className="app-menu" theme="dark" mode="horizontal" selectedKeys={false} >
                    <Menu.Item key="1" ><Link to="/mainpage/survey">Survey List</Link></Menu.Item>
                    <Menu.Item key="2"><Link to="/mainpage/make">Make Survey</Link></Menu.Item>
                    <SubMenu key="3" title="My Menu">
                        <Menu.Item key="4"><Link to ="/mainpage/myMaking">My Making</Link></Menu.Item>
                        <Menu.Item key="5"><Link to="/mainpage/myVoting">My Voting</Link></Menu.Item>
                        <Menu.Item key="6"><a href="/">Logout</a></Menu.Item>
                    </SubMenu> 
                </Menu>
            </Header>
        </Layout>
        );
    }
};

export default AppHeader;