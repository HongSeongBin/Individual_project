import React, { Component } from "react";
import Login from "./member/login/Login";
import { Route, BrowserRouter } from "react-router-dom";
import { Layout } from "antd";
import AppHeader from "./common/AppHeader";

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      userInfo: null,
      isLogin: false,
    };
  }

  setUserstate = (res) => {
    this.setState({ userInfo: res, isLogin: true });
  };

  render() {
    const { userInfo, isLogin } = this.state;

    return (
      <Layout>
        <BrowserRouter>
          {isLogin && <AppHeader />}
          <Route
            path="/"
            exact={true}
            render={(props) => <Login setUserstate={this.setUserstate} />}
          ></Route>
        </BrowserRouter>
      </Layout>
    );
  }
}

export default App;
