import React, { Component } from "react";
import Login from "./member/login/Login";
import { Route, BrowserRouter } from "react-router-dom";
import { Layout } from "antd";
import AppHeader from "./common/AppHeader";
import Register from "./member/register/Register";

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
          <Route path="/register" render={(props) => <Register />}></Route>
          <Route
            path="/mainpage/survey"
            render={(props) => <SurveyList type="All" isLogin={isLogin} />}
          ></Route>
          <Route
            path="/mainpage/myMaking"
            render={(props) => (
              <SurveyList type="Make" userInfo={userInfo} isLogin={isLogin} />
            )}
          ></Route>
          <Route
            path="/mainpage/myVoting"
            render={(props) => (
              <SurveyList type="Vote" userInfo={userInfo} isLogin={isLogin} />
            )}
          ></Route>
        </BrowserRouter>
      </Layout>
    );
  }
}

export default App;
