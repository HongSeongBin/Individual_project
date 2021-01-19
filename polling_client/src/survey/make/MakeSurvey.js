import React, { Component } from "react";
import "antd/dist/antd.css";
import { Form, Layout, notification, Typography } from "antd";
import { withRouter } from "react-router-dom";
import SurveyService from "../../api/SurveyService";
import MakeObject from "./MakeObject";
import MakeTitle from "./MakeTitle";
import MakeSubject from "./MakeSubject";

const { Title } = Typography;
const { Content, Footer } = Layout;

class MakeSurvey extends Component {
  constructor(props) {
    super(props);

    if (!this.props.isLogin) {
      this.props.history.push("/");
      notification.error({
        message: "Mini Polling",
        description: "Please Login first!",
      });
    }
  }

  onFinish = (values) => {
    console.log(values);
    values.userId = this.props.userInfo.id;

    SurveyService.makeSurvey(values)
      .then((response) => {
        notification.success({
          message: "Mini Polling",
          description: "Successfully make survey!",
        });
        this.props.history.push("/mainpage/survey");
      })
      .catch((error) => {
        notification.error({
          message: "Mini Polling",
          description: "Title is alreay exist! Please change your title!!",
        });
      });
  };

  render() {
    return (
      <Layout>
        <Content
          className="site-layout"
          style={{
            backgroundColor: "white",
            padding: "0 50px",
            margin: 100,
            borderRadius: "2em",
          }}
        >
          <div style={{ marginLeft: 70, marginRight: 70 }}>
            <div style={{ textAlign: "center" }}>
              <Title level={3} style={{ marginTop: 30 }}>
                Make your survey
              </Title>
            </div>
            <Form
              name="dynamic_form_nest_item"
              onFinish={this.onFinish}
              autoComplete="off"
              style={{ margin: 70 }}
            >
              <MakeTitle />
              <hr></hr>
              <MakeObject />
              <hr></hr>
              <MakeSubject />
            </Form>
          </div>
        </Content>
        <Footer style={{ textAlign: "center" }}>
          Mini Polling ©2021 Created by SungBin
        </Footer>
      </Layout>
    );
  }
}

export default withRouter(MakeSurvey);
