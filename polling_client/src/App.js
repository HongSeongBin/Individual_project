import React, { Component } from "react";
import Login from "./member/login/Login";
import { Route, BrowserRouter } from "react-router-dom";
import { Layout } from "antd";
import AppHeader from "./common/AppHeader";
import Register from "./member/register/Register";
import SurveyList from "./survey/SurveyList";
import MakeSurvey from "./survey/make/MakeSurvey";


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
          />
          <Route path="/register" render={(props) => <Register />} />
          <Route
            path="/mainpage/survey"
            render={(props) => <SurveyList type="All" isLogin={isLogin} />}
          />
          <Route
            path="/mainpage/myMaking"
            render={(props) => (
              <SurveyList type="Make" userInfo={userInfo} isLogin={isLogin} />
            )}
          />
          <Route
            path="/mainpage/myVoting"
            render={(props) => (
              <SurveyList type="Vote" userInfo={userInfo} isLogin={isLogin} />
            )}
          />
          <Route
            path="/mainpage/make"
            render={(props) => (
              <MakeSurvey userInfo={userInfo} isLogin={isLogin} />
            )}
          />
        </BrowserRouter>
      </Layout>
    );
  }
}

export default App;
